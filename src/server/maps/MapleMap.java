/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation version 3 as published by
 the Free Software Foundation. You may not use, modify or distribute
 this program under any other version of the GNU Affero General Public
 License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server.maps;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleJob;
import client.autoban.AutobanFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.ItemConstants;
import constants.ServerConstants;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import net.server.Server;
import net.server.channel.Channel;
import scripting.map.MapScriptManager;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.MapleStatEffect;
import server.TimerManager;
import server.events.gm.MapleCoconut;
import server.events.gm.MapleFitness;
import server.events.gm.MapleOla;
import server.events.gm.MapleOxQuiz;
import server.events.gm.MapleSnowball;
import server.life.AbstractLoadedMapleLife;
import server.life.MapleLifeFactory;
import server.life.MapleLifeFactory.selfDestruction;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MapleNPC;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.life.MonsterDropEntry;
import server.life.MonsterGlobalDropEntry;
import server.life.SpawnPoint;
import server.partyquest.MonsterCarnival;
import server.partyquest.Pyramid;
import tools.FilePrinter;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.Randomizer;

public class MapleMap {

    private static final List<MapleMapObjectType> rangedMapobjectTypes = Arrays.asList(MapleMapObjectType.SHOP, MapleMapObjectType.ITEM, MapleMapObjectType.NPC, MapleMapObjectType.MONSTER, MapleMapObjectType.DOOR, MapleMapObjectType.SUMMON, MapleMapObjectType.REACTOR);
    private Map<Integer, MapleMapObject> mapobjects = new LinkedHashMap<>();
    private Collection<SpawnPoint> monsterSpawn = Collections.synchronizedList(new LinkedList<SpawnPoint>());
    private AtomicInteger spawnedMonstersOnMap = new AtomicInteger(0);
    private Collection<MapleCharacter> characters = new LinkedHashSet<>();
    private Map<Integer, MaplePortal> portals = new HashMap<>();
    private Map<Integer, Integer> backgroundTypes = new HashMap<>();
    private List<GuardianSpawnPoint> guardianSpawns = new LinkedList<GuardianSpawnPoint>();
    private List<Rectangle> areas = new ArrayList<>();
    private List<MonsterStatus> redTeamBuffs = new LinkedList<MonsterStatus>();
    private List<MonsterStatus> blueTeamBuffs = new LinkedList<MonsterStatus>();
    private List<String> lastEntered = new LinkedList<String>();
    private MapleFootholdTree footholds = null;
    private int mapid;
    private AtomicInteger runningOid = new AtomicInteger(100);
    private int returnMapId;
    private int channel, world;
    private byte monsterRate;
    private boolean clock;
    private boolean boat;
    private boolean docked;
    private List<Point> takenSpawns = new LinkedList<Point>();
    private String mapName;
    private String streetName;
    private MapleMapEffect mapEffect = null;
    private boolean everlast = false;
    private MapleCharacter mapOwner = null;
    private int mapOwnerTime = 30;
    private int forcedReturnMap = 999999999;
    private long timeLimit;
    private int decHP = 0;
    private int protectItem = 0;
    private boolean town;
    private MapleOxQuiz ox;
    private boolean isOxQuiz = false;
    private boolean dropsOn = true;
    private boolean timer = false;
    private boolean isGodmode = false;
    private String onFirstUserEnter;
    private String onUserEnter;
    private int fieldType;
    private int fieldLimit = 0;
    private int mobCapacity = -1;
    private ScheduledFuture<?> mapMonitor = null;
    private Pair<Integer, String> timeMob = null;
    private short mobInterval = 5000;
    private boolean allowSummons = true; // All maps should have this true at the beginning
    // HPQ
    private int riceCakes = 0;
    private int bunnyDamage = 0;
    // events
    private boolean eventstarted = false, isMuted = false;
    private MapleSnowball snowball0 = null;
    private MapleSnowball snowball1 = null;
    private MapleCoconut coconut;
    //locks
    private final ReadLock chrRLock;
    private final WriteLock chrWLock;
    private final ReadLock objectRLock;
    private final WriteLock objectWLock;

    private final Map<String, Integer> environment = new LinkedHashMap<>();

    public MapleMap(int mapid, int world, int channel, int returnMapId, float monsterRate) {
        this.mapid = mapid;
        this.channel = channel;
        this.world = world;
        this.returnMapId = returnMapId;
        this.monsterRate = (byte) Math.ceil(monsterRate);
        if (this.monsterRate == 0) {
            this.monsterRate = 1;
        }
        final ReentrantReadWriteLock chrLock = new ReentrantReadWriteLock(true);
        chrRLock = chrLock.readLock();
        chrWLock = chrLock.writeLock();

        final ReentrantReadWriteLock objectLock = new ReentrantReadWriteLock(true);
        objectRLock = objectLock.readLock();
        objectWLock = objectLock.writeLock();
    }

    public ReadLock getCharacterReadLock() {
        return chrRLock;
    }

    public WriteLock getCharacterWriteLock() {
        return chrWLock;
    }

    public final int getNumPlayersItemsInArea(final int index) {
        return getNumPlayersItemsInRect(getArea(index));
    }

    public final int getNumPlayersItemsInRect(final Rectangle rect) {
        int ret = getNumPlayersInRect(rect);

        objectRLock.lock();
        try {
            for (MapleMapObject obj : mapobjects.values()) {
                if (obj.getType() == MapleMapObjectType.ITEM) {
                    if (rect.contains(obj.getPosition())) {
                        ret++;
                    }
                }
            }

        } finally {
            objectRLock.unlock();
        }
        return ret;
    }

    public final int getNumPlayersInRect(final Rectangle rect) {
        int ret = 0;

        chrRLock.lock();
        try {
            final Iterator<MapleCharacter> ltr = characters.iterator();
            MapleCharacter a;
            while (ltr.hasNext()) {
                if (rect.contains(ltr.next().getPosition())) {
                    ret++;
                }
            }
        } finally {
            chrRLock.unlock();
        }
        return ret;
    }

    public void broadcastMessage(MapleCharacter source, final byte[] packet) {
        chrRLock.lock();
        try {
            for (MapleCharacter chr : characters) {
                if (chr != source) {
                    chr.getClient().announce(packet);
                }
            }
        } finally {
            chrRLock.unlock();
        }
    }

    public void broadcastGMMessage(MapleCharacter source, final byte[] packet) {
        chrRLock.lock();
        try {
            for (MapleCharacter chr : characters) {
                if (chr != source && (chr.gmLevel() > source.gmLevel())) {
                    chr.getClient().announce(packet);
                }
            }
        } finally {
            chrRLock.unlock();
        }
    }

    public void toggleDrops() {
        this.dropsOn = !dropsOn;
    }

    public List<MapleMapObject> getMapObjectsInRect(Rectangle box, List<MapleMapObjectType> types) {
        objectRLock.lock();
        final List<MapleMapObject> ret = new LinkedList<>();
        try {
            for (MapleMapObject l : mapobjects.values()) {
                if (types.contains(l.getType())) {
                    if (box.contains(l.getPosition())) {
                        ret.add(l);
                    }
                }
            }
        } finally {
            objectRLock.unlock();
        }
        return ret;
    }

    public int getId() {
        return mapid;
    }

    public MapleMap getReturnMap() {
        return Server.getInstance().getWorld(world).getChannel(channel).getMapFactory().getMap(returnMapId);
    }

    public int getReturnMapId() {
        return returnMapId;
    }

    public void setReactorState() {
        objectRLock.lock();
        try {
            for (MapleMapObject o : mapobjects.values()) {
                if (o.getType() == MapleMapObjectType.REACTOR) {
                    if (((MapleReactor) o).getState() < 1) {
                        ((MapleReactor) o).setState((byte) 1);
                        broadcastMessage(MaplePacketCreator.triggerReactor((MapleReactor) o, 1));
                    }
                }
            }
        } finally {
            objectRLock.unlock();
        }
    }

    public int getForcedReturnId() {
        return forcedReturnMap;
    }

    public MapleMap getForcedReturnMap() {
        return Server.getInstance().getWorld(world).getChannel(channel).getMapFactory().getMap(forcedReturnMap);
    }

    public void setForcedReturnMap(int map) {
        this.forcedReturnMap = map;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getTimeLeft() {
        return (int) ((timeLimit - System.currentTimeMillis()) / 1000);
    }

    public int getCurrentPartyId() {
        for (MapleCharacter chr : this.getCharacters()) {
            if (chr.getPartyId() != -1) {
                return chr.getPartyId();
            }
        }
        return -1;
    }

    public void addMapObject(MapleMapObject mapobject) {
        objectWLock.lock();
        try {
            int curOID = getUsableOID();
            mapobject.setObjectId(curOID);
            this.mapobjects.put(curOID, mapobject);
        } finally {
            objectWLock.unlock();
        }
    }

    private void spawnAndAddRangedMapObject(MapleMapObject mapobject, DelayedPacketCreation packetbakery) {
        spawnAndAddRangedMapObject(mapobject, packetbakery, null);
    }

    private void spawnAndAddRangedMapObject(MapleMapObject mapobject, DelayedPacketCreation packetbakery, SpawnCondition condition) {
        chrRLock.lock();
        objectWLock.lock();
        try {
            int curOID = getUsableOID();
            mapobject.setObjectId(curOID);
            this.mapobjects.put(curOID, mapobject);

            Iterator<MapleCharacter> itr = characters.iterator();
            MapleCharacter chr;

            while (itr.hasNext()) {
                chr = itr.next();
                if (condition == null || condition.canSpawn(chr)) {
                    if (chr.getPosition().distanceSq(mapobject.getPosition()) <= 722500) {
                        packetbakery.sendPackets(chr.getClient());
                        chr.addVisibleMapObject(mapobject);
                    }
                }
            }
        } finally {
            chrRLock.unlock();
            objectWLock.unlock();
        }
    }

    private int getUsableOID() {
        if (runningOid.incrementAndGet() > 2000000000) {
            runningOid.set(1000);
        }
        objectRLock.lock();
        try {
            if (mapobjects.containsKey(runningOid.get())) {
                while (mapobjects.containsKey(runningOid.incrementAndGet()));
            }
        } finally {
            objectRLock.unlock();
        }

        return runningOid.get();
    }

    public void removeMapObject(int num) {
        objectWLock.lock();
        try {
            this.mapobjects.remove(Integer.valueOf(num));
        } finally {
            objectWLock.unlock();
        }
    }

    public void removeMapObject(final MapleMapObject obj) {
        removeMapObject(obj.getObjectId());
    }

    private Point calcPointBelow(Point initial) {
        MapleFoothold fh = footholds.findBelow(initial);
        if (fh == null) {
            return null;
        }
        int dropY = fh.getY1();
        if (!fh.isWall() && fh.getY1() != fh.getY2()) {
            double s1 = Math.abs(fh.getY2() - fh.getY1());
            double s2 = Math.abs(fh.getX2() - fh.getX1());
            double s5 = Math.cos(Math.atan(s2 / s1)) * (Math.abs(initial.x - fh.getX1()) / Math.cos(Math.atan(s1 / s2)));
            if (fh.getY2() < fh.getY1()) {
                dropY = fh.getY1() - (int) s5;
            } else {
                dropY = fh.getY1() + (int) s5;
            }
        }
        return new Point(initial.x, dropY);
    }

    public Point calcDropPos(Point initial, Point fallback) {
        Point ret = calcPointBelow(new Point(initial.x, initial.y - 85));
        if (ret == null) {
            return fallback;
        }
        return ret;
    }

    public void dropMessage(int type, String message) {
        for (MapleCharacter chr : characters) {
            chr.dropMessage(type, message);
        }
    }

    public final void limitReactor(final int rid, final int num) {
        List<MapleReactor> toDestroy = new ArrayList<>();
        Map<Integer, Integer> contained = new LinkedHashMap<>();
        objectRLock.lock();
        try {
            for (MapleMapObject obj : mapobjects.values()) {

                if (obj.getType() == MapleMapObjectType.REACTOR) {
                    MapleReactor mr = (MapleReactor) obj;
                    if (contained.containsKey(mr.getId())) {
                        if (contained.get(mr.getId()) >= num) {
                            toDestroy.add(mr);
                        } else {
                            contained.put(mr.getId(), contained.get(mr.getId()) + 1);
                        }
                    } else {
                        contained.put(mr.getId(), 1);
                    }
                }
            }
        } finally {
            objectRLock.unlock();
        }
        for (MapleReactor mr : toDestroy) {
            destroyReactor(mr.getObjectId());
        }
    }

    private void dropFromMonster(final MapleCharacter chr, final MapleMonster mob) {
        if (mob.dropsDisabled() || !dropsOn) {
            return;
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final byte droptype = (byte) (mob.getStats().isExplosiveReward() ? 3 : mob.getStats().isFfaLoot() ? 2 : chr.getParty() != null ? 1 : 0);
        final int mobpos = mob.getPosition().x;
        int chServerrate = chr.getClient().getWorldServer().getDropRate();

        boolean done = false;

        try {
            if (chr.getGuild() != null) {
                if (chr.getClient().getWorldServer().getGuildBuffs().size() > 0) {
                    for (String buff : chr.getClient().getWorldServer().getGuildBuffs()) {
                        if (!done) {
                            int guildID = Integer.parseInt(buff.split("-")[0]);

                            if (guildID == chr.getGuild().getId()) {
                                int buffType = Integer.parseInt(buff.split("-")[1]);

                                if (buffType == 3) {

                                    long endingTime = Long.parseLong(buff.split("-")[3]);

                                    if (endingTime > System.currentTimeMillis()) {
                                        chServerrate = chServerrate * 2;
                                        done = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }

        if (!done) {
            chServerrate = chr.getDropRate();
        }

        done = false;

        Item idrop;
        byte d = 1;
        Point pos = new Point(0, mob.getPosition().y);

        Map<MonsterStatus, MonsterStatusEffect> stati = mob.getStati();
        if (stati.containsKey(MonsterStatus.SHOWDOWN)) {
            chServerrate *= (stati.get(MonsterStatus.SHOWDOWN).getStati().get(MonsterStatus.SHOWDOWN).doubleValue() / 100.0 + 1.0);
        }

        final MapleMonsterInformationProvider mi = MapleMonsterInformationProvider.getInstance();
        final List<MonsterDropEntry> dropEntry = new ArrayList<>(mi.retrieveDrop(mob.getId()));

        Collections.shuffle(dropEntry);
        for (final MonsterDropEntry de : dropEntry) {
            if (Randomizer.nextInt(999999) < de.chance * chServerrate) {
                if (droptype == 3) {
                    pos.x = (mobpos + (d % 2 == 0 ? (40 * (d + 1) / 2) : -(40 * (d / 2))));
                } else {
                    pos.x = (mobpos + ((d % 2 == 0) ? (25 * (d + 1) / 2) : -(25 * (d / 2))));
                }
                if (de.itemId == 0) { // meso
                    int mesos = Randomizer.nextInt(de.Maximum - de.Minimum) + de.Minimum;

                    if (mesos > 0) {
                        if (chr.getBuffedValue(MapleBuffStat.MESOUP) != null) {
                            mesos = (int) (mesos * chr.getBuffedValue(MapleBuffStat.MESOUP).doubleValue() / 100.0);
                        }

                        int totalMesos = mesos * chr.getMesoRate();

                        try {
                            if (chr.getGuild() != null) {
                                if (chr.getClient().getWorldServer().getGuildBuffs().size() > 0) {
                                    for (String buff : chr.getClient().getWorldServer().getGuildBuffs()) {
                                        if (!done) {
                                            int guildID = Integer.parseInt(buff.split("-")[0]);

                                            if (guildID == chr.getGuild().getId()) {
                                                int buffType = Integer.parseInt(buff.split("-")[1]);

                                                if (buffType == 2) {

                                                    long endingTime = Long.parseLong(buff.split("-")[3]);

                                                    if (endingTime > System.currentTimeMillis()) {

                                                        double percentIncrease = Double.parseDouble(buff.split("-")[2]) / 100;

                                                        totalMesos = (int) Math.round(totalMesos + (totalMesos * percentIncrease));
                                                        done = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {

                        }

                        spawnMesoDrop(totalMesos, calcDropPos(pos, mob.getPosition()), mob, chr, false, droptype);
                    }
                } else if (de.itemId == 1003112 && chr.getMapId() == 280030000 && chr.getEventInstance() != null && chr.getEventInstance().getProperty("chaos") != null && chr.getEventInstance().getProperty("chaos").equals("true")) {
                    if (ItemConstants.getInventoryType(de.itemId) == MapleInventoryType.EQUIP) {
                        idrop = ii.randomizeStats((Equip) ii.getEquipById(de.itemId));
                    } else {
                        idrop = new Item(de.itemId, (short) 0, (short) (de.Maximum != 1 ? Randomizer.nextInt(de.Maximum - de.Minimum) + de.Minimum : 1));
                    }
                    spawnDrop(idrop, calcDropPos(pos, mob.getPosition()), mob, chr, droptype, de.questid);
                } else if (de.itemId == 1002357 && chr.getMapId() == 280030000 && chr.getEventInstance() != null && chr.getEventInstance().getProperty("chaos") != null && chr.getEventInstance().getProperty("chaos").equals("true")) {

                } else {
                    if (ItemConstants.getInventoryType(de.itemId) == MapleInventoryType.EQUIP) {
                        idrop = ii.randomizeStats((Equip) ii.getEquipById(de.itemId));
                    } else {
                        idrop = new Item(de.itemId, (short) 0, (short) (de.Maximum != 1 ? Randomizer.nextInt(de.Maximum - de.Minimum) + de.Minimum : 1));
                    }
                    spawnDrop(idrop, calcDropPos(pos, mob.getPosition()), mob, chr, droptype, de.questid);
                }
                d++;
            }
        }
        final List<MonsterGlobalDropEntry> globalEntry = mi.getGlobalDrop();
        // Global Drops
        for (final MonsterGlobalDropEntry de : globalEntry) {
            int chance = de.chance;

            if (mob.getId() == 3400004 || mob.getId() == 3400006) {
                chance = (int) (chance / 1.3);
            } else if (isCPQMap()) {
                chance = (int) (chance / 1.5);
            } else if ((chr.getJob().isA(MapleJob.MAGICIAN) || chr.getJob().isA(MapleJob.BLAZEWIZARD1)) && de.itemId == 5221001) {
                chance = (int) (chance / 1.2);
            }

            if (Randomizer.nextInt(999999) < chance) {
                if (droptype == 3) {
                    pos.x = (mobpos + (d % 2 == 0 ? (40 * (d + 1) / 2) : -(40 * (d / 2))));
                } else {
                    pos.x = (mobpos + ((d % 2 == 0) ? (25 * (d + 1) / 2) : -(25 * (d / 2))));
                }
                if (de.itemId == 0) {
                    //chr.getCashShop().gainCash(1, 80);
                } else {
                    if (ItemConstants.getInventoryType(de.itemId) == MapleInventoryType.EQUIP) {
                        idrop = ii.randomizeStats((Equip) ii.getEquipById(de.itemId));
                    } else {
                        idrop = new Item(de.itemId, (short) 0, (short) (de.Maximum != 1 ? Randomizer.nextInt(de.Maximum - de.Minimum) + de.Minimum : 1));
                    }
                    spawnDrop(idrop, calcDropPos(pos, mob.getPosition()), mob, chr, droptype, de.questid);
                    d++;
                }
            }
        }
    }

    private void spawnDrop(final Item idrop, final Point dropPos, final MapleMonster mob, final MapleCharacter chr, final byte droptype, final short questid) {
        final MapleMapItem mdrop = new MapleMapItem(idrop, dropPos, mob, chr, droptype, false, questid);
        mdrop.setDropTime(System.currentTimeMillis());
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                if (questid <= 0 || (c.getPlayer().getQuestStatus(questid) == 1 && c.getPlayer().needQuestItem(questid, idrop.getItemId()))) {
                    c.announce(MaplePacketCreator.dropItemFromMapObject(mdrop, mob.getPosition(), dropPos, (byte) 1));
                }
            }
        }, null);

        TimerManager.getInstance().schedule(new ExpireMapItemJob(mdrop), 180000);
        activateItemReactors(mdrop, chr.getClient());
    }

    public final void spawnMesoDrop(final int meso, final Point position, final MapleMapObject dropper, final MapleCharacter owner, final boolean playerDrop, final byte droptype) {
        final Point droppos = calcDropPos(position, position);
        final MapleMapItem mdrop = new MapleMapItem(meso, droppos, dropper, owner, droptype, playerDrop);
        mdrop.setDropTime(System.currentTimeMillis());

        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                c.announce(MaplePacketCreator.dropItemFromMapObject(mdrop, dropper.getPosition(), droppos, (byte) 1));
            }
        }, null);

        TimerManager.getInstance().schedule(new ExpireMapItemJob(mdrop), 180000);
    }

    public final void disappearingItemDrop(final MapleMapObject dropper, final MapleCharacter owner, final Item item, final Point pos) {
        final Point droppos = calcDropPos(pos, pos);
        final MapleMapItem drop = new MapleMapItem(item, droppos, dropper, owner, (byte) 1, false);
        broadcastMessage(MaplePacketCreator.dropItemFromMapObject(drop, dropper.getPosition(), droppos, (byte) 3), drop.getPosition());
    }

    public MapleMonster getMonsterById(int id) {
        objectRLock.lock();
        try {
            for (MapleMapObject obj : mapobjects.values()) {
                if (obj.getType() == MapleMapObjectType.MONSTER) {
                    if (((AbstractLoadedMapleLife) obj).getId() == id) {
                        return (MapleMonster) obj;
                    }
                }
            }
        } finally {
            objectRLock.unlock();
        }
        return null;
    }

    public void killMonsterDPS(int charId) {
        for (MapleMapObject mobs : getMapObjects()) {
            if (mobs instanceof MapleMonster) {
                MapleMonster mob = (MapleMonster) mobs;
                if (mob.getBelongsTo() == charId) {
                    killMonster(mob, null, false);
                }
            }
        }
    }

    public int countMonster(int id) {
        int count = 0;
        for (MapleMapObject m : getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER))) {
            MapleMonster mob = (MapleMonster) m;
            if (mob.getId() == id) {
                count++;
            }
        }
        return count;
    }

    public boolean damageMonster(final MapleCharacter chr, final MapleMonster monster, final int damage) {
        if (monster.getId() == 8800000) {
            for (MapleMapObject object : chr.getMap().getMapObjects()) {
                MapleMonster mons = chr.getMap().getMonsterByOid(object.getObjectId());
                if (mons != null) {
                    if (mons.getId() >= 8800003 && mons.getId() <= 8800010) {
                        return true;
                    }
                }
            }
        }

        if (monster.isAlive()) {
            boolean killed = false;
            monster.monsterLock.lock();
            try {
                if (!monster.isAlive()) {
                    return false;
                }
                Pair<Integer, Integer> cool = monster.getStats().getCool();
                if (cool != null) {
                    Pyramid pq = (Pyramid) chr.getPartyQuest();
                    if (pq != null) {
                        if (damage > 0) {
                            if (damage >= cool.getLeft()) {
                                if ((Math.random() * 100) < cool.getRight()) {
                                    pq.cool();
                                } else {
                                    pq.kill();
                                }
                            } else {
                                pq.kill();
                            }
                        } else {
                            pq.miss();
                        }
                        killed = true;
                    }
                }

                if (damage > 0) {
                    monster.damage(chr, damage);
                    if (!monster.isAlive()) {  // monster just died
                        //killMonster(monster, chr, true);
                        killed = true;
                    }
                } else if (monster.getId() >= 8810002 && monster.getId() <= 8810009) {
                    for (MapleMapObject object : chr.getMap().getMapObjects()) {
                        MapleMonster mons = chr.getMap().getMonsterByOid(object.getObjectId());
                        if (mons != null) {
                            if (monster.isAlive() && (monster.getId() >= 8810010 && monster.getId() <= 8810017)) {
                                if (mons.getId() == 8810018) {
                                    killMonster(mons, chr, true);
                                }
                            }
                        }
                    }
                }

            } finally {
                monster.monsterLock.unlock();
            }
            if (monster.getStats().selfDestruction() != null && monster.getStats().selfDestruction().getHp() > -1) {// should work ;p
                if (monster.getHp() <= monster.getStats().selfDestruction().getHp()) {
                    killMonster(monster, chr, true, false, monster.getStats().selfDestruction().getAction());
                    return true;
                }
            }
            if (killed) {
                killMonster(monster, chr, true);
            } else {
                chr.setUsedAOE(false);
            }

            return true;
        }
        return false;
    }

    public int spawnGuardian(MonsterStatus status, int team) {
        List<MapleMapObject> reactors = getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.REACTOR));
        for (GuardianSpawnPoint gs : this.guardianSpawns) {
            for (MapleMapObject o : reactors) {
                MapleReactor reactor = (MapleReactor) o;
                if (reactor.getCancelStatus().equals(status) && (reactor.getId() - 9980000) == team) {
                    return 0;
                }
            }
        }
        GuardianSpawnPoint pt = this.getRandomGuardianSpawn(team);
        if (pt == null) {
            return -1;
        }
        int reactorID = 9980000 + team;
        MapleReactor reactor = new MapleReactor(MapleReactorFactory.getReactor(reactorID), reactorID);
        pt.setTaken(true);
        reactor.setPosition(pt.getPosition());
        this.spawnReactor(reactor);
        reactor.setCancelStatus(status);
        reactor.setGuardian(pt);
        this.buffMonsters(team, status);
        getReactorByOid(reactor.getObjectId()).hitReactor(((MapleCharacter) this.getAllPlayer().get(0)).getClient());
        return 1;
    }

    public void buffMonsters(int team, MonsterStatus status) {
        if (team == 0) {
            redTeamBuffs.add(status);
        } else if (team == 1) {
            blueTeamBuffs.add(status);
        }
        for (MapleMapObject mmo : this.mapobjects.values()) {
            if (mmo.getType() == MapleMapObjectType.MONSTER) {
                MapleMonster mob = (MapleMonster) mmo;
                if (mob.getTeam() == team) {
                    int skillID = getSkillId(status);
                    if (skillID != -1) {
                        MobSkill skill = getMobSkill(skillID, this.getSkillLevel(status));
                        mob.applyMonsterBuff(status, skill.getX(), skill.getSkillId(),
                                1000 * 60 * 10, skill);
                    }
                }
            }
        }
    }

    public int getSkillLevel(MonsterStatus status) {
        switch (status) {
            case WEAPON_ATTACK_UP:
                return 1;
            case MAGIC_ATTACK_UP:
                return 1;
            case WEAPON_DEFENSE_UP:
                return 1;
            case MAGIC_DEFENSE_UP:
                return 1;
            case WEAPON_IMMUNITY:
                return 10;
            case MAGIC_IMMUNITY:
                return 9;
            default:
                break;
        }
        return -1;
    }

    public MobSkill getMobSkill(int skillId, int level) {
        return MobSkillFactory.getMobSkill(skillId, level);
    }

    public int getSkillId(MonsterStatus status) {
        if (status == MonsterStatus.WEAPON_ATTACK_UP) {
            return 100;
        } else if (status.equals(MonsterStatus.MAGIC_ATTACK_UP)) {
            return 101;
        } else if (status.equals(MonsterStatus.WEAPON_DEFENSE_UP)) {
            return 112;
        } else if (status.equals(MonsterStatus.MAGIC_DEFENSE_UP)) {
            return 113;
        } else if (status.equals(MonsterStatus.WEAPON_IMMUNITY)) {
            return 140;
        } else if (status.equals(MonsterStatus.MAGIC_IMMUNITY)) {
            return 141;
        }
        return -1;
    }

    public int spawnMonsterWithCoords(MapleMonster mob, int x, int y, int hp) {
        Point spos = new Point(x, y - 1);
        spos = calcPointBelow(spos);
        spos.y -= 1;

        mob.setPosition(spos);
        mob.setHp(hp);
        spawnMonster(mob);
        return mob.getObjectId();
    }

    public int spawnMonsterWithCoordsEXP(MapleMonster mob, int x, int y, int exp) {
        Point spos = new Point(x, y - 1);
        spos = calcPointBelow(spos);
        spos.y -= 1;

        mob.setPosition(spos);
        mob.setExp(exp);
        mob.disableDrops();

        spawnMonster(mob);
        return mob.getObjectId();
    }

    public int spawnMonsterWithCoords(MapleMonster mob, int x, int y) {
        Point spos = new Point(x, y - 1);
        spos = calcPointBelow(spos);
        spos.y -= 1;

        mob.setPosition(spos);
        spawnMonster(mob);
        return mob.getObjectId();
    }

    public List<MapleMonster> getMonsters() {
        List<MapleMonster> mobs = new ArrayList<MapleMonster>();
        for (MapleMapObject object : this.getMapObjects()) {
            mobs.add(this.getMonsterByOid(object.getObjectId()));
        }
        return mobs;
    }

    public void killMonster(final MapleMonster monster, final MapleCharacter chr, final boolean withDrops) {
        killMonster(monster, chr, withDrops, false, 1);
    }

    public void killMonster(final MapleMonster monster, final MapleCharacter chr, final boolean withDrops, final boolean secondTime, int animation) {
        if (monster.getId() == 8810018 && !secondTime) {
            TimerManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    killMonster(monster, chr, withDrops, true, 1);
                    killAllMonsters();
                }
            }, 3000);
            return;
        }
        if (chr == null) {
            spawnedMonstersOnMap.decrementAndGet();
            monster.setHp(0);
            broadcastMessage(MaplePacketCreator.killMonster(monster.getObjectId(), animation), monster.getPosition());
            removeMapObject(monster);
            return;
        }
        if (monster.getStats().getLevel() >= chr.getLevel() + 30 && !chr.isIntern()) {
            AutobanFactory.GENERAL.alert(chr, " for killing a " + monster.getName() + " which is over 30 levels higher.");
        }
        /*if (chr.getQuest(MapleQuest.getInstance(29400)).getStatus().equals(MapleQuestStatus.Status.STARTED)) {
         if (chr.getLevel() >= 120 && monster.getStats().getLevel() >= 120) {
         //FIX MEDAL SHET
         } else if (monster.getStats().getLevel() >= chr.getLevel()) {
         }
         }*/
        int buff = monster.getBuffToGive();
        if (buff > -1) {
            MapleItemInformationProvider mii = MapleItemInformationProvider.getInstance();
            for (MapleMapObject mmo : this.getAllPlayer()) {
                MapleCharacter character = (MapleCharacter) mmo;
                if (character.isAlive()) {
                    MapleStatEffect statEffect = mii.getItemEffect(buff);
                    character.getClient().announce(MaplePacketCreator.showOwnBuffEffect(buff, 1));
                    broadcastMessage(character, MaplePacketCreator.showBuffeffect(character.getId(), buff, 1), false);
                    statEffect.applyTo(character);
                }
            }
        }
        if (monster.getId() == 8810018 && chr.getMapId() == 240060200) {
            for (Channel cserv : Server.getInstance().getWorld(world).getChannels()) {
                for (MapleCharacter player : cserv.getPlayerStorage().getAllCharacters()) {
                    player.dropMessage(6, "To the crew that have finally conquered Horned Tail after numerous attempts, I salute thee! You are the true heroes of Leafre!!");
                }
            }
        }
        spawnedMonstersOnMap.decrementAndGet();

        monster.setHp(0);
        broadcastMessage(MaplePacketCreator.killMonster(monster.getObjectId(), animation), monster.getPosition());
        //if (monster.getStats().selfDestruction() == null) {//FUU BOMBS D:
        removeMapObject(monster);
        //}
        if (monster.getCP() > 0 && chr.getMonsterCarnival() != null) {
            chr.gainCP(monster.getCP());
        }

        if (monster.getId() >= 8800003 && monster.getId() <= 8800010) {
            boolean makeZakReal = true;
            Collection<MapleMapObject> objects = getMapObjects();
            for (MapleMapObject object : objects) {
                MapleMonster mons = getMonsterByOid(object.getObjectId());
                if (mons != null) {
                    if (mons.getId() >= 8800003 && mons.getId() <= 8800010) {
                        makeZakReal = false;
                        break;
                    }
                }
            }
            if (makeZakReal) {
                for (MapleMapObject object : objects) {
                    MapleMonster mons = chr.getMap().getMonsterByOid(object.getObjectId());
                    if (mons != null) {
                        if (mons.getId() == 8800000) {
                            makeMonsterReal(mons);
                            updateMonsterController(mons);
                            break;
                        }
                    }
                }
            }
        }
        MapleCharacter dropOwner = monster.killBy(chr);

        if (chr.getEventInstance() == null) {
            if (!chr.getUsedAOE() && monster.getLevel() >= 110) {
                chr.gainDecimalMobPoints(0.5d);
            } else if (chr.getUsedAOE() && monster.getLevel() >= 110) {
                chr.gainDecimalMobPoints(0.25d);
            } else if (!chr.getUsedAOE() && (monster.getLevel() - 5) < chr.getLevel()) {
                if (monster.getId() == 3400004 || monster.getId() == 3400006) {
                    chr.gainDecimalMobPoints(0.1d);
                } else {
                    chr.gainDecimalMobPoints(0.3d);
                }
            } else if (chr.getUsedAOE() && (monster.getLevel() - 5) < chr.getLevel()) {
                if (monster.getId() == 3400004 || monster.getId() == 3400006) {
                    chr.gainDecimalMobPoints(0.05d);
                } else {
                    chr.gainDecimalMobPoints(0.1d);
                }
            } else if (chr.getUsedAOE()) {
                if (monster.getId() == 3400004 || monster.getId() == 3400006) {
                    chr.gainDecimalMobPoints(0.25d);
                } else {
                    chr.gainDecimalMobPoints(0.5d);
                }
            } else if (!chr.getUsedAOE()) {
                if (monster.getId() == 3400004 || monster.getId() == 3400006) {
                    chr.gainDecimalMobPoints(0.5d);
                } else {
                    chr.gainMobPoints(1);
                }
            }
        }

        chr.setUsedAOE(false);

        if (withDrops && !monster.dropsDisabled()) {
            if (dropOwner == null) {
                dropOwner = chr;
            }
            dropFromMonster(dropOwner, monster);
        }
    }

    public void addLastEntered(String name) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        lastEntered.add(name + " - " + dateFormat.format(new Date()) + " EST");

        if (lastEntered.size() > 20) {
            int toRemove = lastEntered.size() - 20;

            for (int i = 0; i < toRemove; i++) {
                lastEntered.remove(0);
            }
        }
    }

    public List<String> getLastEntered() {
        return lastEntered;
    }

    public void killFriendlies(MapleMonster mob) {
        this.killMonster(mob, (MapleCharacter) getAllPlayer().get(0), false);
    }

    public void killMonster(int monsId) {
        for (MapleMapObject mmo : getMapObjects()) {
            if (mmo instanceof MapleMonster) {
                if (((AbstractLoadedMapleLife) mmo).getId() == monsId) {
                    this.killMonster((MapleMonster) mmo, (MapleCharacter) getAllPlayer().get(0), false);
                }
            }
        }
    }

    public void monsterCloakingDevice() {
        for (MapleMapObject monstermo : getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER))) {
            MapleMonster monster = (MapleMonster) monstermo;
            broadcastMessage(MaplePacketCreator.makeMonsterInvisible(monster));
        }
    }

    public void softKillAllMonsters() {
        for (MapleMapObject monstermo : getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER))) {
            MapleMonster monster = (MapleMonster) monstermo;
            if (monster.getStats().isFriendly()) {
                continue;
            }
            spawnedMonstersOnMap.decrementAndGet();
            monster.setHp(0);
            removeMapObject(monster);
        }
    }

    public void killAllMonstersNotFriendly() {
        for (MapleMapObject monstermo : getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER))) {
            MapleMonster monster = (MapleMonster) monstermo;
            if (monster.getStats().isFriendly()) {
                continue;
            }
            spawnedMonstersOnMap.decrementAndGet();
            monster.setHp(0);
            broadcastMessage(MaplePacketCreator.killMonster(monster.getObjectId(), true), monster.getPosition());
            removeMapObject(monster);
        }
    }

    public void killAllMonsters() {
        for (MapleMapObject monstermo : getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER))) {
            MapleMonster monster = (MapleMonster) monstermo;
            spawnedMonstersOnMap.decrementAndGet();
            monster.setHp(0);
            broadcastMessage(MaplePacketCreator.killMonster(monster.getObjectId(), true), monster.getPosition());
            removeMapObject(monster);
        }
    }

    public List<MapleMapObject> getAllPlayer() {
        return getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.PLAYER));
    }

    public void destroyReactor(int oid) {
        final MapleReactor reactor = getReactorByOid(oid);
        TimerManager tMan = TimerManager.getInstance();
        broadcastMessage(MaplePacketCreator.destroyReactor(reactor));
        reactor.setAlive(false);
        removeMapObject(reactor);
        reactor.setTimerActive(false);
        if (reactor.getDelay() > 0) {
            tMan.schedule(new Runnable() {
                @Override
                public void run() {
                    respawnReactor(reactor);
                }
            }, reactor.getDelay());
        }
    }

    public void resetReactors() {
        objectRLock.lock();
        try {
            for (MapleMapObject o : mapobjects.values()) {
                if (o.getType() == MapleMapObjectType.REACTOR) {
                    final MapleReactor r = ((MapleReactor) o);
                    r.setState((byte) 0);
                    r.setTimerActive(false);
                    broadcastMessage(MaplePacketCreator.triggerReactor(r, 0));
                }
            }
        } finally {
            objectRLock.unlock();
        }
    }

    public void shuffleReactors() {
        List<Point> points = new ArrayList<>();
        objectRLock.lock();
        try {
            for (MapleMapObject o : mapobjects.values()) {
                if (o.getType() == MapleMapObjectType.REACTOR) {
                    points.add(o.getPosition());
                }
            }
            Collections.shuffle(points);
            for (MapleMapObject o : mapobjects.values()) {
                if (o.getType() == MapleMapObjectType.REACTOR) {
                    o.setPosition(points.remove(points.size() - 1));
                }
            }
        } finally {
            objectRLock.unlock();
        }
    }

    public void shuffleReactors(int first, int last) {
        List<Point> points = new ArrayList<>();
        objectRLock.lock();
        try {
            for (MapleMapObject obj : mapobjects.values()) {
                if (obj.getType() == MapleMapObjectType.REACTOR) {
                    MapleReactor mr = (MapleReactor) obj;
                    if (mr.getId() >= first && mr.getId() <= last) {
                        points.add(mr.getPosition());
                    }
                }
            }
        } finally {
            objectRLock.unlock();
        }
        Collections.shuffle(points);
        objectRLock.lock();
        try {
            for (MapleMapObject obj : mapobjects.values()) {
                if (obj.getType() == MapleMapObjectType.REACTOR) {
                    MapleReactor mr = (MapleReactor) obj;
                    if (mr.getId() >= first && mr.getId() <= last) {
                        mr.setPosition(points.remove(points.size() - 1));
                    }
                }
            }
        } finally {
            objectRLock.unlock();
        }
    }

    public void destroyReactors(final int first, final int last) {
        List<MapleReactor> toDestroy = new ArrayList<>();
        objectRLock.lock();
        try {
            for (MapleMapObject obj : mapobjects.values()) {
                if (obj.getType() == MapleMapObjectType.REACTOR) {
                    MapleReactor mr = (MapleReactor) obj;
                    if (mr.getId() >= first && mr.getId() <= last) {
                        toDestroy.add(mr);
                    }
                }
            }
        } finally {
            objectRLock.unlock();
        }
        for (MapleReactor mr : toDestroy) {
            destroyReactor(mr.getObjectId());
        }
    }

    public MapleReactor getReactorById(int Id) {
        objectRLock.lock();
        try {
            for (MapleMapObject obj : mapobjects.values()) {
                if (obj.getType() == MapleMapObjectType.REACTOR) {
                    if (((MapleReactor) obj).getId() == Id) {
                        return (MapleReactor) obj;
                    }
                }
            }
            return null;
        } finally {
            objectRLock.unlock();
        }
    }

    /**
     * Automagically finds a new controller for the given monster from the chars on the map...
     *
     * @param monster
     */
    public void updateMonsterController(MapleMonster monster) {
        monster.monsterLock.lock();
        try {
            if (!monster.isAlive()) {
                return;
            }
            if (monster.getController() != null) {
                if (monster.getController().getMap() != this) {
                    monster.getController().stopControllingMonster(monster);
                } else {
                    return;
                }
            }
            int mincontrolled = -1;
            MapleCharacter newController = null;
            chrRLock.lock();
            try {
                for (MapleCharacter chr : characters) {
                    if (!chr.isHidden() && (chr.getControlledMonsters().size() < mincontrolled || mincontrolled == -1)) {
                        mincontrolled = chr.getControlledMonsters().size();
                        newController = chr;
                    }
                }
            } finally {
                chrRLock.unlock();
            }
            if (newController != null) {// was a new controller found? (if not no one is on the map)
                if (monster.isFirstAttack()) {
                    newController.controlMonster(monster, true);
                    monster.setControllerHasAggro(true);
                    monster.setControllerKnowsAboutAggro(true);
                } else {
                    newController.controlMonster(monster, false);
                }
            }
        } finally {
            monster.monsterLock.unlock();
        }
    }

    public Collection<MapleMapObject> getMapObjects() {
        return Collections.unmodifiableCollection(mapobjects.values());
    }

    public boolean containsNPC(int npcid) {
        if (npcid == 9000066) {
            return true;
        }
        objectRLock.lock();
        try {
            for (MapleMapObject obj : mapobjects.values()) {
                if (obj.getType() == MapleMapObjectType.NPC) {
                    if (((AbstractLoadedMapleLife) obj).getId() == npcid) {
                        return true;
                    }
                }
            }
        } finally {
            objectRLock.unlock();
        }
        return false;
    }

    public MapleMapObject getMapObject(int oid) {
        return mapobjects.get(oid);
    }

    /**
     * returns a monster with the given oid, if no such monster exists returns null
     *
     * @param oid
     * @return
     */
    public MapleMonster getMonsterByOid(int oid) {
        MapleMapObject mmo = getMapObject(oid);
        if (mmo == null) {
            return null;
        }
        if (mmo.getType() == MapleMapObjectType.MONSTER) {
            return (MapleMonster) mmo;
        }
        return null;
    }

    public void changeEnvironment(String ms, int type) {
        broadcastGMMessage(MaplePacketCreator.environmentChange(ms, type));
    }

    public void moveEnvironment(String ms, int type) {
        broadcastGMMessage(MaplePacketCreator.environmentChange(ms, type));
        environment.put(ms, type);
    }

    public final Map<String, Integer> getEnvironment() {
        return environment;
    }

    public void toggleEnvironment(String ms) {
        if (environment.containsKey(ms)) {
            moveEnvironment(ms, environment.get(ms) == 1 ? 2 : 1);
        } else {
            moveEnvironment(ms, 1);
        }
    }

    public MapleReactor getReactorByOid(int oid) {
        MapleMapObject mmo = getMapObject(oid);
        if (mmo == null) {
            return null;
        }
        return mmo.getType() == MapleMapObjectType.REACTOR ? (MapleReactor) mmo : null;
    }

    public MapleReactor getReactorByName(String name) {
        objectRLock.lock();
        try {
            for (MapleMapObject obj : mapobjects.values()) {
                if (obj.getType() == MapleMapObjectType.REACTOR) {
                    if (((MapleReactor) obj).getName().equals(name)) {
                        return (MapleReactor) obj;
                    }
                }
            }
        } finally {
            objectRLock.unlock();
        }
        return null;
    }

    public void spawnMonsterOnGroudBelow(int id, int x, int y) {
        MapleMonster mob = MapleLifeFactory.getMonster(id);
        spawnMonsterOnGroundBelow(mob, new Point(x, y));
    }

    public void spawnMonsterOnGroudBelow(MapleMonster mob, Point pos) {
        spawnMonsterOnGroundBelow(mob, pos);
    }

    public void spawnMonsterOnGroundBelow(MapleMonster mob, Point pos) {
        Point spos = new Point(pos.x, pos.y - 1);
        spos = calcPointBelow(spos);
        spos.y--;
        mob.setPosition(spos);
        spawnMonster(mob);
    }

    public void spawnCPQMonster(MapleMonster mob, Point pos, int team) {
        Point spos = new Point(pos.x, pos.y - 1);
        spos = calcPointBelow(spos);
        spos.y--;
        mob.setPosition(spos);
        mob.setTeam(team);
        spawnMonster(mob);
    }

    public void addBunnyHit() {
        bunnyDamage++;
        if (bunnyDamage > 5) {
            broadcastMessage(MaplePacketCreator.serverNotice(6, "The Moon Bunny is feeling sick. Please protect it so it can make delicious rice cakes."));
            bunnyDamage = 0;
        }
    }

    private void monsterItemDrop(final MapleMonster m, final Item item, long delay) {
        final ScheduledFuture<?> monsterItemDrop = TimerManager.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (MapleMap.this.getMonsterById(m.getId()) != null && !MapleMap.this.getAllPlayer().isEmpty()) {
                    if (item.getItemId() == 4001101) {
                        MapleMap.this.riceCakes++;
                        MapleMap.this.broadcastMessage(MaplePacketCreator.serverNotice(6, "The Moon Bunny made rice cake number " + (MapleMap.this.riceCakes)));
                    }
                    spawnItemDrop(m, (MapleCharacter) getAllPlayer().get(0), item, m.getPosition(), false, false);
                }
            }
        }, delay, delay);
        if (getMonsterById(m.getId()) == null) {
            monsterItemDrop.cancel(true);
        }
    }

    public void spawnFakeMonsterOnGroundBelow(MapleMonster mob, Point pos) {
        Point spos = getGroundBelow(pos);
        mob.setPosition(spos);
        spawnFakeMonster(mob);
    }

    public Point getGroundBelow(Point pos) {
        Point spos = new Point(pos.x, pos.y - 3); // Using -3 fixes issues with spawning pets causing a lot of issues.
        spos = calcPointBelow(spos);
        spos.y--;//shouldn't be null!
        return spos;
    }

    public void spawnRevives(final MapleMonster monster) {
        monster.setMap(this);

        spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {

                c.announce(MaplePacketCreator.spawnMonster(monster, false));
            }
        });
        updateMonsterController(monster);
        spawnedMonstersOnMap.incrementAndGet();
    }

    public void spawnMonster(final MapleMonster monster) {
        if (mobCapacity != -1 && mobCapacity == spawnedMonstersOnMap.get()) {
            return;//PyPQ
        }
        monster.setMap(this);
        if (!monster.getMap().getAllPlayer().isEmpty()) {
            MapleCharacter chr = (MapleCharacter) getAllPlayer().get(0);
            if (monster.getEventInstance() == null && chr.getEventInstance() != null) {
                chr.getEventInstance().registerMonster(monster);
            }
        }

        spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                c.announce(MaplePacketCreator.spawnMonster(monster, true));

                if (monster.getId() == 8810026) {
                    TimerManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            killMonster(monster, (MapleCharacter) getAllPlayer().get(0), false, false, 3);
                        }
                    }, new Random().nextInt(5000));
                }

            }
        }, null);
        updateMonsterController(monster);

        if (monster.getDropPeriodTime() > 0) { //9300102 - Watchhog, 9300061 - Moon Bunny (HPQ)
            switch (monster.getId()) {
                case 9300102:
                    monsterItemDrop(monster, new Item(4031507, (short) 0, (short) 1), monster.getDropPeriodTime());
                    break;
                case 9300061:
                    monsterItemDrop(monster, new Item(4001101, (short) 0, (short) 1), monster.getDropPeriodTime() / 3);
                    break;
                default:
                    FilePrinter.printError(FilePrinter.UNHANDLED_EVENT, "UNCODED TIMED MOB DETECTED: " + monster.getId());
                    break;
            }
        }
        spawnedMonstersOnMap.incrementAndGet();

        final selfDestruction selfDestruction = monster.getStats().selfDestruction();
        if (monster.getStats().removeAfter() > 0 || selfDestruction != null && selfDestruction.getHp() < 0) {
            if (selfDestruction == null) {
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        killMonster(monster, null, false);
                    }
                }, monster.getStats().removeAfter() * 1000);
            } else {
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        killMonster(monster, null, false, false, selfDestruction.getAction());
                    }
                }, selfDestruction.removeAfter() * 1000);
            }
        }
    }

    public void spawnDojoMonster(final MapleMonster monster) {
        Point[] pts = {new Point(140, 0), new Point(190, 7), new Point(187, 7)};
        spawnMonsterWithEffect(monster, 15, pts[Randomizer.nextInt(3)]);
    }

    public void spawnMonsterWithEffect(final MapleMonster monster, final int effect, Point pos) {
        try {
            monster.setMap(this);
            Point spos = new Point(pos.x, pos.y - 1);
            spos = calcPointBelow(spos);
            spos.y--;
            monster.setPosition(spos);
            if (mapid < 925020000 || mapid > 925030000) {
                monster.disableDrops();
            }
            spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {
                @Override
                public void sendPackets(MapleClient c) {
                    c.announce(MaplePacketCreator.spawnMonster(monster, true, effect));
                }
            });
            if (monster.hasBossHPBar()) {
                broadcastMessage(monster.makeBossHPBarPacket(), monster.getPosition());
            }
            updateMonsterController(monster);

            spawnedMonstersOnMap.incrementAndGet();
        } catch (Exception e) {

        }
    }

    public void spawnFakeMonster(final MapleMonster monster) {
        monster.setMap(this);
        monster.setFake(true);
        spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                c.announce(MaplePacketCreator.spawnFakeMonster(monster, 0));
            }
        });

        spawnedMonstersOnMap.incrementAndGet();
    }

    public boolean isCPQWinnerMap() {
        switch (this.getId()) {
            case 980000103:
            case 980000203:
            case 980000303:
            case 980000403:
            case 980000503:
            case 980000603:
                return true;
        }
        return false;
    }

    public boolean isCPQLoserMap() {
        switch (this.getId()) {
            case 980000104:
            case 980000204:
            case 980000304:
            case 980000404:
            case 980000504:
            case 980000604:
                return true;
        }
        return false;
    }

    public boolean isCPQMap() {
        switch (this.getId()) {
            case 980000101:
            case 980000201:
            case 980000301:
            case 980000401:
            case 980000501:
            case 980000601:
                return true;
        }
        return false;
    }

    public boolean isBlueCPQMap() {
        switch (this.getId()) {
            case 980000501:
            case 980000601:
                return true;
        }
        return false;
    }

    public boolean isPurpleCPQMap() {
        switch (this.getId()) {
            case 980000301:
            case 980000401:
                return true;
        }
        return false;
    }

    public void addClock(int seconds) {
        broadcastMessage(MaplePacketCreator.getClock(seconds));
    }

    public void makeMonsterReal(final MapleMonster monster) {
        monster.setFake(false);
        broadcastMessage(MaplePacketCreator.makeMonsterReal(monster));
        updateMonsterController(monster);
    }

    public void spawnReactor(final MapleReactor reactor) {
        reactor.setMap(this);
        spawnAndAddRangedMapObject(reactor, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                c.announce(reactor.makeSpawnData());
            }
        });

    }

    private void respawnReactor(final MapleReactor reactor) {
        reactor.setState((byte) 0);
        reactor.setAlive(true);
        spawnReactor(reactor);
    }

    public void spawnDoor(final MapleDoor door) {
//        spawnAndAddRangedMapObject(door, new DelayedPacketCreation() {
//            @Override
//            public void sendPackets(MapleClient c) {
//                c.announce(MaplePacketCreator.spawnDoor(door.getOwner().getId(), door.getTargetPosition(), false));
//                if (door.getOwner().getParty() != null && (door.getOwner() == c.getPlayer() || door.getOwner().getParty().containsMembers(c.getPlayer().getMPC()))) {
//                    c.announce(MaplePacketCreator.partyPortal(door.getTown().getId(), door.getTarget().getId(), door.getTargetPosition()));
//                }
//                c.announce(MaplePacketCreator.spawnPortal(door.getTown().getId(), door.getTarget().getId(), door.getTargetPosition()));
//                c.announce(MaplePacketCreator.enableActions());
//            }
//        }, new SpawnCondition() {
//            @Override
//            public boolean canSpawn(MapleCharacter chr) {
//                return chr.getMapId() == door.getTarget().getId() || chr == door.getOwner() && chr.getParty() == null;
//            }
//        });

        spawnAndAddRangedMapObject(door, new DelayedPacketCreation() {
            @Override
            public final void sendPackets(MapleClient c) {
                door.sendSpawnData(c);
                c.getSession().write(MaplePacketCreator.enableActions());
            }
        });
    }

    public int getCharactersSize() {
        int ret = 0;
        chrRLock.lock();
        try {
            final Iterator<MapleCharacter> ltr = characters.iterator();
            MapleCharacter chr;
            while (ltr.hasNext()) {
                chr = ltr.next();
                ret++;
            }
        } finally {
            chrRLock.unlock();
        }
        return ret;
    }

    public List<MapleCharacter> getPlayersInRange(Rectangle box, List<MapleCharacter> chr) {
        List<MapleCharacter> character = new LinkedList<>();
        chrRLock.lock();
        try {
            for (MapleCharacter a : characters) {
                if (chr.contains(a.getClient().getPlayer())) {
                    if (box.contains(a.getPosition())) {
                        character.add(a);
                    }
                }
            }
            return character;
        } finally {
            chrRLock.unlock();
        }
    }

    public void spawnSummon(final MapleSummon summon) {
        spawnAndAddRangedMapObject(summon, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                if (summon != null) {
                    c.announce(MaplePacketCreator.spawnSummon(summon, true));
                }
            }
        }, null);
    }

    public void spawnMist(final MapleMist mist, final int duration, boolean poison, boolean fake, boolean recovery) {
        addMapObject(mist);
        broadcastMessage(fake ? mist.makeFakeSpawnData(30) : mist.makeSpawnData());
        TimerManager tMan = TimerManager.getInstance();
        final ScheduledFuture<?> poisonSchedule;
        if (poison) {
            Runnable poisonTask = new Runnable() {
                @Override
                public void run() {
                    List<MapleMapObject> affectedMonsters = getMapObjectsInBox(mist.getBox(), Collections.singletonList(MapleMapObjectType.MONSTER));
                    for (MapleMapObject mo : affectedMonsters) {
                        if (mist.makeChanceResult()) {
                            MonsterStatusEffect poisonEffect = new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, 1), mist.getSourceSkill(), null, false);
                            ((MapleMonster) mo).applyStatus(mist.getOwner(), poisonEffect, true, duration);
                        }
                    }
                }
            };
            poisonSchedule = tMan.register(poisonTask, 2000, 2500);
        } else if (recovery) {
            Runnable poisonTask = new Runnable() {
                @Override
                public void run() {
                    List<MapleMapObject> players = getMapObjectsInBox(mist.getBox(), Collections.singletonList(MapleMapObjectType.PLAYER));
                    for (MapleMapObject mo : players) {
                        if (mist.makeChanceResult()) {
                            MapleCharacter chr = (MapleCharacter) mo;
                            if (mist.getOwner().getId() == chr.getId() || mist.getOwner().getParty() != null && mist.getOwner().getParty().containsMembers(chr.getMPC())) {
                                chr.addMP(mist.getSourceSkill().getEffect(chr.getSkillLevel(mist.getSourceSkill().getId())).getX() * chr.getMp() / 100);
                            }
                        }
                    }
                }
            };
            poisonSchedule = tMan.register(poisonTask, 2000, 2500);
        } else {
            poisonSchedule = null;
        }
        tMan.schedule(new Runnable() {
            @Override
            public void run() {
                removeMapObject(mist);
                if (poisonSchedule != null) {
                    poisonSchedule.cancel(false);
                }
                broadcastMessage(mist.makeDestroyData());
            }
        }, duration);
    }

    public final void spawnItemDrop(final MapleMapObject dropper, final MapleCharacter owner, final Item item, Point pos, final boolean ffaDrop, final boolean playerDrop) {
        final Point droppos = calcDropPos(pos, pos);
        final MapleMapItem drop = new MapleMapItem(item, droppos, dropper, owner, (byte) (ffaDrop ? 2 : 0), playerDrop);
        drop.setDropTime(System.currentTimeMillis());

        spawnAndAddRangedMapObject(drop, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                c.announce(MaplePacketCreator.dropItemFromMapObject(drop, dropper.getPosition(), droppos, (byte) 1));
            }
        }, null);
        broadcastMessage(MaplePacketCreator.dropItemFromMapObject(drop, dropper.getPosition(), droppos, (byte) 0));

        if (!everlast) {
            TimerManager.getInstance().schedule(new ExpireMapItemJob(drop), 180000);
            activateItemReactors(drop, owner.getClient());
        }
    }

    private void activateItemReactors(final MapleMapItem drop, final MapleClient c) {
        final Item item = drop.getItem();

        for (final MapleMapObject o : getAllReactor()) {
            final MapleReactor react = (MapleReactor) o;

            if (react.getReactorType() == 100) {
                if (react.getReactItem((byte) 0).getLeft() == item.getItemId() && react.getReactItem((byte) 0).getRight() == item.getQuantity()) {

                    if (react.getArea().contains(drop.getPosition())) {
                        if (!react.isTimerActive()) {
                            TimerManager.getInstance().schedule(new ActivateItemReactor(drop, react, c), 5000);
                            react.setTimerActive(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    public final List<MapleMapObject> getAllReactor() {
        return getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.REACTOR));
    }

    public void startMapEffect(String msg, int itemId) {
        startMapEffect(msg, itemId, 30000);
    }

    public void startMapEffect(String msg, int itemId, long time) {
        if (mapEffect != null) {
            return;
        }
        mapEffect = new MapleMapEffect(msg, itemId);
        broadcastMessage(mapEffect.makeStartData());
        TimerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                broadcastMessage(mapEffect.makeDestroyData());
                mapEffect = null;
            }
        }, time);
    }

    public Point getRandomSP(int team) {
        if (takenSpawns.size() > 0) {
            for (SpawnPoint sp : monsterSpawn) {
                for (Point pt : takenSpawns) {
                    if ((sp.getPosition().x == pt.x && sp.getPosition().y == pt.y) || (sp.getTeam() != team && !this.isBlueCPQMap())) {
                        continue;
                    } else {
                        takenSpawns.add(pt);
                        return sp.getPosition();
                    }
                }
            }
        } else {
            for (SpawnPoint sp : monsterSpawn) {
                if (sp.getTeam() == team || this.isBlueCPQMap()) {
                    takenSpawns.add(sp.getPosition());
                    return sp.getPosition();
                }
            }
        }
        return null;
    }

    public GuardianSpawnPoint getRandomGuardianSpawn(int team) {
        boolean alltaken = false;
        for (GuardianSpawnPoint a : this.guardianSpawns) {
            if (!a.isTaken()) {
                alltaken = false;
                break;
            }
        }
        if (alltaken) {
            return null;
        }
        if (this.guardianSpawns.size() > 0) {
            while (true) {
                for (GuardianSpawnPoint gsp : this.guardianSpawns) {
                    if (!gsp.isTaken() && Math.random() < 0.3
                            && (gsp.getTeam() == -1 || gsp.getTeam() == team)) {
                        return gsp;
                    }
                }
            }
        }
        return null;
    }

    public void addPlayer(final MapleCharacter chr) {
        chrWLock.lock();
        try {
            characters.add(chr);
        } finally {
            chrWLock.unlock();
        }
        chr.setMapId(mapid);
        if (onFirstUserEnter.length() != 0 && !chr.hasEntered(onFirstUserEnter, mapid) && MapScriptManager.getInstance().scriptExists(onFirstUserEnter, true)) {
            if (getAllPlayer().size() <= 1) {
                chr.enteredScript(onFirstUserEnter, mapid);
                MapScriptManager.getInstance().getMapScript(chr.getClient(), onFirstUserEnter, true);
            }
        }
        if (onUserEnter.length() != 0) {
            if (onUserEnter.equals("cygnusTest") && (mapid < 913040000 || mapid > 913040006)) {
                chr.saveLocation("INTRO");
            }
            MapScriptManager.getInstance().getMapScript(chr.getClient(), onUserEnter, false);
        }
        if (FieldLimit.CANNOTUSEMOUNTS.check(fieldLimit) && chr.getBuffedValue(MapleBuffStat.MONSTER_RIDING) != null) {
            chr.cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
            chr.cancelBuffStats(MapleBuffStat.MONSTER_RIDING);
        }
        if (mapid == 923010000 && getMonsterById(9300102) == null) { // Kenta's Mount Quest
            spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9300102), new Point(77, 426));
        } else if (mapid == 910010200) {  // Henesys Party Quest Bonus
            chr.announce(MaplePacketCreator.getClock(60 * 5));
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (chr.getMapId() == 910010200) {
                        chr.changeMap(910010400);
                    }
                }
            }, 5 * 60 * 1000);
        } else if (mapid == 200090060) { // To Rien
            chr.announce(MaplePacketCreator.getClock(60));
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (chr.getMapId() == 200090060) {
                        chr.changeMap(140020300);
                    }
                }
            }, 60 * 1000);
        } else if (mapid == 200090070) { // To Lith Harbor
            chr.announce(MaplePacketCreator.getClock(60));
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (chr.getMapId() == 200090070) {
                        chr.changeMap(104000000, 3);
                    }
                }
            }, 60 * 1000);
        } else if (mapid == 200090030) { // To Ereve (SkyFerry)
            chr.getClient().announce(MaplePacketCreator.getClock(60));
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (chr.getMapId() == 200090030) {
                        chr.changeMap(130000210);
                    }
                }
            }, 60 * 1000);
        } else if (mapid == 200090031) { // To Victoria Island (SkyFerry)
            chr.getClient().announce(MaplePacketCreator.getClock(60));
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (chr.getMapId() == 200090031) {
                        chr.changeMap(101000400);
                    }
                }
            }, 60 * 1000);
        } else if (mapid == 200090021) { // To Orbis (SkyFerry)
            chr.getClient().announce(MaplePacketCreator.getClock(60));
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (chr.getMapId() == 200090021) {
                        chr.changeMap(200000161);
                    }
                }
            }, 60 * 1000);
        } else if (mapid == 200090020) { // To Ereve From Orbis (SkyFerry)
            chr.getClient().announce(MaplePacketCreator.getClock(60));
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (chr.getMapId() == 200090020) {
                        chr.changeMap(130000210);
                    }
                }
            }, 60 * 1000);
        } else if (mapid == 103040400) {
            if (chr.getEventInstance() != null) {
                chr.getEventInstance().movePlayer(chr);
            }
        }
//        } else if (MapleMiniDungeon.isDungeonMap(mapid)) {
//            final MapleMiniDungeon dungeon = MapleMiniDungeon.getDungeon(mapid);
//            chr.getClient().announce(MaplePacketCreator.getClock(30 * 60));
//            TimerManager.getInstance().schedule(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (MapleMiniDungeon.isDungeonMap(chr.getMapId())) {
//                        chr.changeMap(dungeon.getBase());
//                    }
//                }
//            }, 30 * 60 * 1000);
//        }
        MaplePet[] pets = chr.getPets();
        for (int i = 0; i < chr.getPets().length; i++) {
            if (pets[i] != null) {
                pets[i].setPos(getGroundBelow(chr.getPosition()));
                chr.announce(MaplePacketCreator.showPet(chr, pets[i], false, false));
            } else {
                break;
            }
        }
        if (chr.isHidden()) {
            broadcastGMMessage(chr, MaplePacketCreator.spawnPlayerMapobject(chr), false);
            chr.announce(MaplePacketCreator.getGMEffect(0x10, (byte) 1));

            List<Pair<MapleBuffStat, Integer>> dsstat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DARKSIGHT, 0));
            broadcastGMMessage(chr, MaplePacketCreator.giveForeignBuff(chr.getId(), dsstat), false);
        } else {
            broadcastMessage(chr, MaplePacketCreator.spawnPlayerMapobject(chr), false);
        }

        sendObjectPlacement(chr.getClient());
        if (isStartingEventMap() && !eventStarted()) {
            chr.getMap().getPortal("join00").setPortalStatus(false);
        }
        if (hasForcedEquip()) {
            chr.getClient().announce(MaplePacketCreator.showForcedEquip(-1));
        }
        if (specialEquip()) {
            chr.getClient().announce(MaplePacketCreator.coconutScore(0, 0));
            chr.getClient().announce(MaplePacketCreator.showForcedEquip(chr.getTeam()));
        }
        objectWLock.lock();
        try {
            this.mapobjects.put(Integer.valueOf(chr.getObjectId()), chr);
        } finally {
            objectWLock.unlock();
        }
        if (chr.getPlayerShop() != null) {
            addMapObject(chr.getPlayerShop());
        }

        final MapleDragon dragon = chr.getDragon();
        if (dragon != null) {
            dragon.setPosition(chr.getPosition());
            this.addMapObject(dragon);
            if (chr.isHidden()) {
                this.broadcastGMMessage(chr, MaplePacketCreator.spawnDragon(dragon));
            } else {
                this.broadcastMessage(chr, MaplePacketCreator.spawnDragon(dragon));
            }
        }

        MapleStatEffect summonStat = chr.getStatForBuff(MapleBuffStat.SUMMON);
        if (summonStat != null) {
            MapleSummon summon = chr.getSummons().get(summonStat.getSourceId());
            summon.setPosition(chr.getPosition());
            chr.getMap().spawnSummon(summon);
            updateMapObjectVisibility(chr, summon);
        }
        if (mapEffect != null) {
            mapEffect.sendStartData(chr.getClient());
        }
        chr.getClient().announce(MaplePacketCreator.resetForcedStats());
        if (mapid == 914000200 || mapid == 914000210 || mapid == 914000220) {
            chr.getClient().announce(MaplePacketCreator.aranGodlyStats());
        }
        if (chr.getEventInstance() != null && chr.getEventInstance().isTimerStarted()) {
            chr.getClient().announce(MaplePacketCreator.getClock((int) (chr.getEventInstance().getTimeLeft() / 1000)));
        }
        if (chr.getFitness() != null && chr.getFitness().isTimerStarted()) {
            chr.getClient().announce(MaplePacketCreator.getClock((int) (chr.getFitness().getTimeLeft() / 1000)));
        }

        if (chr.getOla() != null && chr.getOla().isTimerStarted()) {
            chr.getClient().announce(MaplePacketCreator.getClock((int) (chr.getOla().getTimeLeft() / 1000)));
        }

        if (mapid == 109060000) {
            chr.announce(MaplePacketCreator.rollSnowBall(true, 0, null, null));
        }

        if (chr.getMonsterCarnival() != null) {
            chr.getClient().getSession().write(MaplePacketCreator.getClock(chr.getMonsterCarnival().getTimeLeftSeconds()));
        }

        if (hasClock()) {
            Calendar cal = Calendar.getInstance();
            chr.getClient().announce((MaplePacketCreator.getClockTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND))));
        }
        if (hasBoat() == 2) {
            chr.getClient().announce((MaplePacketCreator.boatPacket(true)));
        } else if (hasBoat() == 1 && (chr.getMapId() != 200090000 || chr.getMapId() != 200090010)) {
            chr.getClient().announce(MaplePacketCreator.boatPacket(false));
        }
        chr.receivePartyMemberHP();
    }

    public void killAllMonsters(boolean drop) {
        List<MapleMapObject> players = null;
        if (drop) {
            players = getAllPlayer();
        }
        List<MapleMapObject> monsters = getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
        for (MapleMapObject monstermo : monsters) {
            MapleMonster monster = (MapleMonster) monstermo;
            spawnedMonstersOnMap.decrementAndGet();
            monster.setHp(0);
            broadcastMessage(MaplePacketCreator.killMonster(monster.getObjectId(), true), monster.getPosition());
            removeMapObject(monster);
            if (drop) {
                int random = (int) Math.random() * (players.size());
                dropFromMonster((MapleCharacter) players.get(random), monster);
            }
        }
    }

    public MaplePortal findClosestPortal(Point from) {
        MaplePortal closest = null;
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (MaplePortal portal : portals.values()) {
            double distance = portal.getPosition().distanceSq(from);
            if (distance < shortestDistance) {
                closest = portal;
                shortestDistance = distance;
            }
        }
        return closest;
    }

    public MaplePortal getRandomSpawnpoint() {
        List<MaplePortal> spawnPoints = new ArrayList<>();
        for (MaplePortal portal : portals.values()) {
            if (portal.getType() >= 0 && portal.getType() <= 2) {
                spawnPoints.add(portal);
            }
        }
        MaplePortal portal = spawnPoints.get(new Random().nextInt(spawnPoints.size()));
        return portal != null ? portal : getPortal(0);
    }

    public void removePlayer(MapleCharacter chr) {
        chrWLock.lock();
        try {
            characters.remove(chr);
        } finally {
            chrWLock.unlock();
        }
        removeMapObject(chr.getObjectId());
        if (!chr.isHidden()) {
            broadcastMessage(MaplePacketCreator.removePlayerFromMap(chr.getId()));
        } else {
            broadcastGMMessage(MaplePacketCreator.removePlayerFromMap(chr.getId()));
        }

        for (MapleMonster monster : chr.getControlledMonsters()) {
            monster.setController(null);
            monster.setControllerHasAggro(false);
            monster.setControllerKnowsAboutAggro(false);
            updateMonsterController(monster);
        }
        chr.leaveMap();
        chr.cancelMapTimeLimitTask();

        try {
            for (MapleSummon summon : chr.getSummons().values()) {
                if (summon.isStationary()) {
                    chr.cancelBuffStats(MapleBuffStat.PUPPET);
                } else {
                    removeMapObject(summon);
                }
            }
        } catch (Exception e) {

        }

        if (chr.getDragon() != null) {
            removeMapObject(chr.getDragon());
            if (chr.isHidden()) {
                this.broadcastGMMessage(chr, MaplePacketCreator.removeDragon(chr.getId()));
            } else {
                this.broadcastMessage(chr, MaplePacketCreator.removeDragon(chr.getId()));
            }
        }
    }

    public void broadcastMessage(final byte[] packet) {
        broadcastMessage(null, packet, Double.POSITIVE_INFINITY, null);
    }

    public void broadcastGMMessage(final byte[] packet) {
        broadcastGMMessage(null, packet, Double.POSITIVE_INFINITY, null);
    }

    /**
     * Nonranged. Repeat to source according to parameter.
     *
     * @param source
     * @param packet
     * @param repeatToSource
     */
    public void broadcastMessage(MapleCharacter source, final byte[] packet, boolean repeatToSource) {
        broadcastMessage(repeatToSource ? null : source, packet, Double.POSITIVE_INFINITY, source.getPosition());
    }

    /**
     * Ranged and repeat according to parameters.
     *
     * @param source
     * @param packet
     * @param repeatToSource
     * @param ranged
     */
    public void broadcastMessage(MapleCharacter source, final byte[] packet, boolean repeatToSource, boolean ranged) {
        broadcastMessage(repeatToSource ? null : source, packet, ranged ? 722500 : Double.POSITIVE_INFINITY, source.getPosition());
    }

    /**
     * Always ranged from Point.
     *
     * @param packet
     * @param rangedFrom
     */
    public void broadcastMessage(final byte[] packet, Point rangedFrom) {
        broadcastMessage(null, packet, 722500, rangedFrom);
    }

    /**
     * Always ranged from point. Does not repeat to source.
     *
     * @param source
     * @param packet
     * @param rangedFrom
     */
    public void broadcastMessage(MapleCharacter source, final byte[] packet, Point rangedFrom) {
        broadcastMessage(source, packet, 722500, rangedFrom);
    }

    private void broadcastMessage(MapleCharacter source, final byte[] packet, double rangeSq, Point rangedFrom) {
        chrRLock.lock();
        try {
            for (MapleCharacter chr : characters) {
                if (chr != source) {
                    if (rangeSq < Double.POSITIVE_INFINITY) {
                        if (rangedFrom.distanceSq(chr.getPosition()) <= rangeSq) {
                            chr.getClient().announce(packet);
                        }
                    } else {
                        chr.getClient().announce(packet);
                    }
                }
            }
        } finally {
            chrRLock.unlock();
        }
    }

    private boolean isNonRangedType(MapleMapObjectType type) {
        switch (type) {
            case NPC:
            case PLAYER:
            case HIRED_MERCHANT:
            case PLAYER_NPC:
            case DRAGON:
            case MIST:
                return true;
            default:
                return false;
        }
    }

    private void sendObjectPlacement(MapleClient mapleClient) {
        MapleCharacter chr = mapleClient.getPlayer();
        objectRLock.lock();
        try {
            for (MapleMapObject o : mapobjects.values()) {
                if (o.getType() == MapleMapObjectType.SUMMON) {
                    MapleSummon summon = (MapleSummon) o;
                    if (summon.getOwner() == chr) {
                        if (chr.getSummons().isEmpty() || !chr.getSummons().containsValue(summon)) {
                            objectWLock.lock();
                            try {
                                mapobjects.remove(o);
                            } finally {
                                objectWLock.unlock();
                            }
                            continue;
                        }
                    }
                }
                if (isNonRangedType(o.getType())) {
                    o.sendSpawnData(mapleClient);
                } else if (o.getType() == MapleMapObjectType.MONSTER) {
                    updateMonsterController((MapleMonster) o);
                }
            }
        } finally {
            objectRLock.unlock();
        }
        if (chr != null) {
            for (MapleMapObject o : getMapObjectsInRange(chr.getPosition(), 722500, rangedMapobjectTypes)) {
                if (o.getType() == MapleMapObjectType.REACTOR) {
                    if (((MapleReactor) o).isAlive()) {
                        o.sendSpawnData(chr.getClient());
                        chr.addVisibleMapObject(o);
                    }
                } else {
                    o.sendSpawnData(chr.getClient());
                    chr.addVisibleMapObject(o);
                }
            }
        }
    }

    public List<MapleMapObjectType> getRangedMapobjectTypes() {
        return rangedMapobjectTypes;
    }

    public List<MapleMapObject> getMapObjectsInRange(Point from, double rangeSq, List<MapleMapObjectType> types) {
        List<MapleMapObject> ret = new LinkedList<>();
        objectRLock.lock();
        try {
            for (MapleMapObject l : mapobjects.values()) {
                if (types.contains(l.getType())) {
                    if (from.distanceSq(l.getPosition()) <= rangeSq) {
                        ret.add(l);
                    }
                }
            }
            return ret;
        } finally {
            objectRLock.unlock();
        }
    }

    public List<MapleMapObject> getMapObjectsInBox(Rectangle box, List<MapleMapObjectType> types) {
        List<MapleMapObject> ret = new LinkedList<>();
        objectRLock.lock();
        try {
            for (MapleMapObject l : mapobjects.values()) {
                if (types.contains(l.getType())) {
                    if (box.contains(l.getPosition())) {
                        ret.add(l);
                    }
                }
            }
            return ret;
        } finally {
            objectRLock.unlock();
        }
    }

    public void addPortal(MaplePortal myPortal) {
        portals.put(myPortal.getId(), myPortal);
    }

    public MaplePortal getPortal(String portalname) {
        for (MaplePortal port : portals.values()) {
            if (port.getName().equals(portalname)) {
                return port;
            }
        }
        return null;
    }

    public MaplePortal getPortal(int portalid) {
        return portals.get(portalid);
    }

    public void addMapleArea(Rectangle rec) {
        areas.add(rec);
    }

    public List<Rectangle> getAreas() {
        return new ArrayList<>(areas);
    }

    public boolean hasTimer() {
        return timer;
    }

    public void setTimer(boolean timer) {
        this.timer = timer;
    }

    public Rectangle getArea(int index) {
        return areas.get(index);
    }

    public void setFootholds(MapleFootholdTree footholds) {
        this.footholds = footholds;
    }

    public MapleFootholdTree getFootholds() {
        return footholds;
    }

    /**
     * it's threadsafe, gtfo :D
     *
     * @param monster
     * @param mobTime
     */
    public void addMonsterSpawn(MapleMonster monster, int mobTime, int team) {
        Point newpos = calcPointBelow(monster.getPosition());
        newpos.y -= 1;
        SpawnPoint sp = new SpawnPoint(monster, newpos, !monster.isMobile(), mobTime, mobInterval, team);
        monsterSpawn.add(sp);
        if (sp.shouldSpawn() || mobTime == -1) {// -1 does not respawn and should not either but force ONE spawn
            spawnMonster(sp.getMonster());
        }

    }

    public Collection<MapleCharacter> getCharacters() {
        return Collections.unmodifiableCollection(this.characters);
    }

    public MapleCharacter getCharacterById(int id) {
        chrRLock.lock();
        try {
            for (MapleCharacter c : this.characters) {
                if (c.getId() == id) {
                    return c;
                }
            }
        } finally {
            chrRLock.unlock();
        }
        return null;
    }

    public void updateMapObjectVisibility(MapleCharacter chr, MapleMapObject mo) {
        if (!chr.isMapObjectVisible(mo)) { // monster entered view range
            if (mo.getType() == MapleMapObjectType.SUMMON || mo.getPosition().distanceSq(chr.getPosition()) <= 722500) {
                chr.addVisibleMapObject(mo);
                mo.sendSpawnData(chr.getClient());
            }
        } else if (mo.getType() != MapleMapObjectType.SUMMON && mo.getPosition().distanceSq(chr.getPosition()) > 722500) {
            chr.removeVisibleMapObject(mo);
            mo.sendDestroyData(chr.getClient());
        }
    }

    public void moveMonster(MapleMonster monster, Point reportedPos) {
        monster.setPosition(reportedPos);
        chrRLock.lock();
        try {
            for (MapleCharacter chr : characters) {
                updateMapObjectVisibility(chr, monster);
            }
        } finally {
            chrRLock.unlock();
        }
    }

    public void movePlayer(MapleCharacter player, Point newPosition) {
        player.setPosition(newPosition);

        Collection<MapleMapObject> visibleObjects = player.getVisibleMapObjects();
        MapleMapObject[] visibleObjectsNow = visibleObjects.toArray(new MapleMapObject[visibleObjects.size()]);

        try {
            for (MapleMapObject mo : visibleObjectsNow) {
                if (mo != null) {
                    if (mapobjects.get(mo.getObjectId()) == mo) {
                        updateMapObjectVisibility(player, mo);
                    } else {
                        player.removeVisibleMapObject(mo);
                    }
                }
            }
        } catch (Exception e) {

        }

        for (MapleMapObject mo : getMapObjectsInRange(player.getPosition(), 722500, rangedMapobjectTypes)) {
            if (!player.isMapObjectVisible(mo)) {
                mo.sendSpawnData(player.getClient());
                player.addVisibleMapObject(mo);
            }
        }
    }

    public MaplePortal findClosestSpawnpoint(Point from) {
        MaplePortal closest = null;
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (MaplePortal portal : portals.values()) {
            double distance = portal.getPosition().distanceSq(from);
            if (portal.getType() >= 0 && portal.getType() <= 2 && distance < shortestDistance && portal.getTargetMapId() == 999999999) {
                closest = portal;
                shortestDistance = distance;
            }
        }
        return closest;
    }

    public Collection<MaplePortal> getPortals() {
        return Collections.unmodifiableCollection(portals.values());
    }

    public int getMapOwnerTime() {
        return mapOwnerTime;
    }

    public void runMapOwnerTimer() {
        TimerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (mapOwner != null) {
                    Boolean cancelTimer = false;

                    if (mapOwner.getMapId() == mapid && mapOwner.getClient().getChannel() == channel) {
                        mapOwner.message("You have re-gained ownership of the map.");
                        cancelTimer = true;
                    }

                    mapOwnerTime--;

                    if (mapOwnerTime <= 0) {
                        mapOwner.message("You have lost map ownership of the map: " + mapOwner.getOwnedMap().mapName);
                        dropMessage(6, "[Map Ownership] " + mapOwner.getName() + " has lost ownership of this map. You can type @mapowner to take ownership of this map.");
                        resetMapOwner();
                        cancelTimer = true;
                    }

                    if (!cancelTimer && mapOwnerTime % 5 == 0) {
                        mapOwner.message("You have " + mapOwnerTime + " seconds to get back to the map or else you will lose ownership of the map.");
                    }

                    if (!cancelTimer) {
                        runMapOwnerTimer();
                    } else {
                        mapOwnerTime = 30;
                    }
                }
            }
        }, 1000);
    }

    public void setMapOwnerTime(int time) {
        mapOwnerTime = time;
    }

    public MapleCharacter getMapOwner() {
        return mapOwner;
    }

    public void setMapOwner(MapleCharacter chr) {
        mapOwner = chr;
    }

    public void resetMapOwner() {
        mapOwner.resetOwnsMap();
        mapOwner = null;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setClock(boolean hasClock) {
        this.clock = hasClock;
    }

    public boolean hasClock() {
        return clock;
    }

    public void setTown(boolean isTown) {
        this.town = isTown;
    }

    public boolean isTown() {
        return town;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean mute) {
        isMuted = mute;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setEverlast(boolean everlast) {
        this.everlast = everlast;
    }

    public boolean getEverlast() {
        return everlast;
    }

    public int getSpawnedMonstersOnMap() {
        return spawnedMonstersOnMap.get();
    }

    public void setMobCapacity(int capacity) {
        this.mobCapacity = capacity;
    }

    public void setBackgroundTypes(HashMap<Integer, Integer> backTypes) {
        backgroundTypes.putAll(backTypes);
    }

    // not really costly to keep generating imo
    public void sendNightEffect(MapleCharacter mc) {
        for (Entry<Integer, Integer> types : backgroundTypes.entrySet()) {
            if (types.getValue() >= 3) { // 3 is a special number
                mc.announce(MaplePacketCreator.changeBackgroundEffect(true, types.getKey(), 0));
            }
        }
    }

    public void broadcastNightEffect() {
        chrRLock.lock();
        try {
            for (MapleCharacter c : characters) {
                sendNightEffect(c);
            }
        } finally {
            chrRLock.unlock();
        }
    }

    public MapleCharacter getCharacterByName(String name) {
        chrRLock.lock();
        try {
            for (MapleCharacter c : this.characters) {
                if (c.getName().toLowerCase().equals(name.toLowerCase())) {
                    return c;
                }
            }
        } finally {
            chrRLock.unlock();
        }
        return null;
    }

    public void debuffMonsters(int team, MonsterStatus status) {
        if (team == 0) {
            removeStatus(status, team);
        } else if (team == 1) {
            removeStatus(status, team);
        }
        for (MapleMapObject mmo : this.mapobjects.values()) {
            if (mmo.getType() == MapleMapObjectType.MONSTER) {
                MapleMonster mob = (MapleMonster) mmo;
                if (mob.getTeam() == team) {
                    int skillID = getSkillId(status);
                    if (skillID != -1) {
                        if (mob.getMonsterBuffs().contains(status)) {
                            mob.cancelMonsterBuff(status);
                        }
                    }
                }
            }
        }
    }

    public void removeStatus(MonsterStatus status, int team) {
        List<MonsterStatus> a = null;
        if (team == 0) {
            a = redTeamBuffs;
        } else if (team == 1) {
            a = blueTeamBuffs;
        }
        List<MonsterStatus> r = new LinkedList<MonsterStatus>();
        for (MonsterStatus ms : a) {
            if (ms.equals(status)) {
                r.add(ms);
            }
        }
        for (MonsterStatus al : r) {
            if (a.contains(al)) {
                a.remove(al);
            }
        }
    }

    public void addGuardianSpawnPoint(GuardianSpawnPoint a) {
        this.guardianSpawns.add(a);
    }

    private class ExpireMapItemJob implements Runnable {

        private MapleMapItem mapitem;

        public ExpireMapItemJob(MapleMapItem mapitem) {
            this.mapitem = mapitem;
        }

        @Override
        public void run() {
            if (mapitem != null && mapitem == getMapObject(mapitem.getObjectId())) {
                mapitem.itemLock.lock();
                try {
                    if (mapitem.isPickedUp()) {
                        return;
                    }
                    MapleMap.this.broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 0, 0), mapitem.getPosition());
                    mapitem.setPickedUp(true);
                } finally {
                    mapitem.itemLock.unlock();
                    MapleMap.this.removeMapObject(mapitem);
                }
            }
        }
    }

    private class ActivateItemReactor implements Runnable {

        private MapleMapItem mapitem;
        private MapleReactor reactor;
        private MapleClient c;

        public ActivateItemReactor(MapleMapItem mapitem, MapleReactor reactor, MapleClient c) {
            this.mapitem = mapitem;
            this.reactor = reactor;
            this.c = c;
        }

        @Override
        public void run() {
            if (mapitem != null && mapitem == getMapObject(mapitem.getObjectId())) {
                mapitem.itemLock.lock();
                try {
                    TimerManager tMan = TimerManager.getInstance();
                    if (mapitem.isPickedUp()) {
                        return;
                    }
                    MapleMap.this.broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 0, 0), mapitem.getPosition());
                    MapleMap.this.removeMapObject(mapitem);
                    reactor.hitReactor(c);
                    reactor.setTimerActive(false);
                    if (reactor.getDelay() > 0) {
                        tMan.schedule(new Runnable() {
                            @Override
                            public void run() {
                                reactor.setState((byte) 0);
                                broadcastMessage(MaplePacketCreator.triggerReactor(reactor, 0));
                            }
                        }, reactor.getDelay());
                    }
                } finally {
                    mapitem.itemLock.unlock();
                }
            }
        }
    }

    public void instanceMapRespawn() {
        final int numShouldSpawn = (short) ((monsterSpawn.size() - spawnedMonstersOnMap.get()));//Fking lol'd
        if (numShouldSpawn > 0) {
            List<SpawnPoint> randomSpawn = new ArrayList<>(monsterSpawn);
            Collections.shuffle(randomSpawn);
            int spawned = 0;
            for (SpawnPoint spawnPoint : randomSpawn) {
                spawnMonster(spawnPoint.getMonster());
                spawned++;
                if (spawned >= numShouldSpawn) {
                    break;
                }
            }
        }
    }

    public boolean isSpawnBuffedMap() {
        return mapid == 270030100 || mapid == 270030200 || mapid == 270030300 || mapid == 270030400 || mapid == 260020000 || mapid == 260020100
                || mapid == 240040511 || mapid == 240040800 || mapid == 270010100 || mapid == 270010200 || mapid == 270010300
                || mapid == 270010400 || mapid == 610020004 || mapid == 610020002 || mapid == 103010001 || mapid == 540020100
                || mapid == 240040600 || mapid == 240030300 || mapid == 240040520 || mapid == 610020013 || mapid == 270020100
                || mapid == 270020200 || mapid == 270020300 || mapid == 270020400 || mapid == 270020500;
    }

    public boolean isExpBuffedMap() {
        return mapid == 100030000 || mapid == 100010000 || mapid == 104040000 || mapid == 104040001 || mapid == 104040002 || mapid == 104030000
                || mapid == 104020000 || mapid == 104010000 || mapid == 104010001 || mapid == 104010002 || mapid == 104000200 || mapid == 104000300
                || mapid == 104000100 || mapid == 103030000 || mapid == 103030100 || mapid == 103030200 || mapid == 103020000 || mapid == 103020100
                || mapid == 103020200 || mapid == 103010000 || mapid == 103000101 || mapid == 103000102 || mapid == 103000103 || mapid == 103000104
                || mapid == 103000105 || mapid == 103000200 || mapid == 103000201 || mapid == 103000202 || mapid == 102050000 || mapid == 102040000
                || mapid == 102030000 || mapid == 102020000 || mapid == 102020100 || mapid == 102020200 || mapid == 102020300 || mapid == 102010000
                || mapid == 101040000 || mapid == 101030400 || mapid == 101030100 || mapid == 101030200 || mapid == 101030300 || mapid == 101030000
                || mapid == 101030401 || mapid == 101030402 || mapid == 101030403 || mapid == 101030404 || mapid == 101030405 || mapid == 101030406
                || mapid == 101020000 || mapid == 101010000 || mapid == 100050000 || mapid == 100040100 || mapid == 100040000 || mapid == 105030000
                || mapid == 105040000 || mapid == 105040100 || mapid == 105040200 || mapid == 200010000 || mapid == 200010100 || mapid == 200010110
                || mapid == 200010111 || mapid == 200010200 || mapid == 200010120 || mapid == 200010121 || mapid == 200010130 || mapid == 200010130
                || mapid == 200010131 || mapid == 200020000 || mapid == 200040000 || mapid == 200030000 || mapid == 200040001 || mapid == 200050000
                || mapid == 200060000 || mapid == 200070000 || mapid == 200080000 || mapid == 230040200 || mapid == 230040300 || mapid == 220070000
                || mapid == 220070100 || mapid == 220070200 || mapid == 220060300 || mapid == 220060000 || mapid == 220060100 || mapid == 220060200
                || mapid == 220060300 || mapid == 101030110 || mapid == 101030111 || mapid == 101030112 || mapid == 101030105 || mapid == 101030106
                || mapid == 101030107 || mapid == 101030108 || mapid == 101030109 || mapid == 211041100 || mapid == 211041200 || mapid == 211041300
                || mapid == 211041400 || mapid == 211041500 || mapid == 211041600 || mapid == 211041700 || mapid == 211041800 || mapid == 250020200
                || mapid == 250020300 || mapid == 251010400 || mapid == 251010500 || mapid == 200082100 || mapid == 103000902 || mapid == 103000905
                || mapid == 103000909;
    }

    public void respawn() {
        if (characters.isEmpty()) {
            return;
        }

//        int spawnedMobs = 0;
//
//        for (MapleMapObject mobs : getMapObjects()) {
//            if (mobs instanceof MapleMonster) {
//                MapleMonster mob = (MapleMonster) mobs;
//                if (mob.isAlive()) {
//                    spawnedMobs++;
//                }
//            }
//        }
//
//        spawnedMonstersOnMap.set(spawnedMobs);
        short numShouldSpawn;

        if (isSpawnBuffedMap()) {
            numShouldSpawn = (short) ((monsterSpawn.size() * 2 - spawnedMonstersOnMap.get()));
        } else if (mapid == 103040200 || mapid == 103040201) { // Yeti and Penguin Claw Machine spawn fix
            numShouldSpawn = 26;

            for (MapleMapObject mobs : getMapObjects()) {
                if (mobs instanceof MapleMonster) {
                    MapleMonster mob = (MapleMonster) mobs;
                    if (mob.isAlive()) {
                        if (mob.getId() == 3400005 || mob.getId() == 3400003) {
                            numShouldSpawn--;
                        }
                    }
                }
            }

            if (numShouldSpawn < 0) {
                numShouldSpawn = 0;
            }
        } else if (isCPQMap()) {
            numShouldSpawn = 26;

            for (MapleMapObject mobs : getMapObjects()) {
                if (mobs instanceof MapleMonster) {
                    MapleMonster mob = (MapleMonster) mobs;
                    if (mob.isAlive()) {
                        numShouldSpawn--;
                    }
                }
            }

            if (numShouldSpawn < 0) {
                numShouldSpawn = 0;
            }

        } else {
            numShouldSpawn = (short) ((monsterSpawn.size() - spawnedMonstersOnMap.get()));
        }

        if (numShouldSpawn > 0) {
            List<SpawnPoint> randomSpawn = new ArrayList<>(monsterSpawn);
            Collections.shuffle(randomSpawn);
            short spawned = 0;
            for (SpawnPoint spawnPoint : randomSpawn) {
                if (spawnPoint.shouldSpawn()) {
                    spawnMonster(spawnPoint.getMonster());
                    spawned++;
                }
                if (spawned >= numShouldSpawn) {
                    break;

                }
            }
        }
    }

    private static interface DelayedPacketCreation {

        void sendPackets(MapleClient c);
    }

    private static interface SpawnCondition {

        boolean canSpawn(MapleCharacter chr);
    }

    public int getHPDec() {
        return decHP;
    }

    public void setHPDec(int delta) {
        decHP = delta;
    }

    public int getHPDecProtect() {
        return protectItem;
    }

    public void setHPDecProtect(int delta) {
        this.protectItem = delta;
    }

    private int hasBoat() {
        return docked ? 2 : (boat ? 1 : 0);
    }

    public void setBoat(boolean hasBoat) {
        this.boat = hasBoat;
    }

    public void setDocked(boolean isDocked) {
        this.docked = isDocked;
    }

    public void setGodmode(boolean godmode) {
        isGodmode = godmode;
    }

    public Boolean isGodmode() {
        return isGodmode;
    }

    public void broadcastGMMessage(MapleCharacter source, final byte[] packet, boolean repeatToSource) {
        broadcastGMMessage(repeatToSource ? null : source, packet, Double.POSITIVE_INFINITY, source.getPosition());
    }

    private void broadcastGMMessage(MapleCharacter source, final byte[] packet, double rangeSq, Point rangedFrom) {
        chrRLock.lock();
        try {
            for (MapleCharacter chr : characters) {
                if (chr != source && chr.isIntern()) {
                    if (rangeSq < Double.POSITIVE_INFINITY) {
                        if (rangedFrom.distanceSq(chr.getPosition()) <= rangeSq) {
                            chr.getClient().announce(packet);
                        }
                    } else {
                        chr.getClient().announce(packet);
                    }
                }
            }
        } finally {
            chrRLock.unlock();
        }
    }

    public void broadcastNONGMMessage(MapleCharacter source, final byte[] packet, boolean repeatToSource) {
        chrRLock.lock();
        try {
            for (MapleCharacter chr : characters) {
                if (chr != source && !chr.isIntern()) {
                    chr.getClient().announce(packet);
                }
            }
        } finally {
            chrRLock.unlock();
        }
    }

    public MapleOxQuiz getOx() {
        return ox;
    }

    public void setOx(MapleOxQuiz set) {
        this.ox = set;
    }

    public void setOxQuiz(boolean b) {
        this.isOxQuiz = b;
    }

    public boolean isOxQuiz() {
        return isOxQuiz;
    }

    public void setOnUserEnter(String onUserEnter) {
        this.onUserEnter = onUserEnter;
    }

    public String getOnUserEnter() {
        return onUserEnter;
    }

    public void setOnFirstUserEnter(String onFirstUserEnter) {
        this.onFirstUserEnter = onFirstUserEnter;
    }

    public String getOnFirstUserEnter() {
        return onFirstUserEnter;
    }

    private boolean hasForcedEquip() {
        return fieldType == 81 || fieldType == 82;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public void clearDrops(MapleCharacter player) {
        List<MapleMapObject> items = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM));
        for (MapleMapObject i : items) {
            player.getMap().removeMapObject(i);
            player.getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(i.getObjectId(), 0, player.getId()));
        }
    }

    public void clearDrops() {
        for (MapleMapObject i : getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM))) {
            removeMapObject(i);
            this.broadcastMessage(MaplePacketCreator.removeItemFromMap(i.getObjectId(), 0, 0));
        }
    }

    public void addMapTimer(int time) {
        timeLimit = System.currentTimeMillis() + (time * 1000);
        broadcastMessage(MaplePacketCreator.getClock(time));
        mapMonitor = TimerManager.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (timeLimit != 0 && timeLimit < System.currentTimeMillis()) {
                    warpEveryone(getForcedReturnId());
                }
                if (getCharacters().isEmpty()) {
                    resetReactors();
                    killAllMonsters();
                    clearDrops();
                    timeLimit = 0;
                    if (mapid >= 922240100 && mapid <= 922240119) {
                        toggleHiddenNPC(9001108);
                    }
                    try {
                        mapMonitor.cancel(true);
                        mapMonitor = null;
                    } catch (Exception e) {

                    }
                }
            }
        }, 1000);
    }

    public void setFieldLimit(int fieldLimit) {
        this.fieldLimit = fieldLimit;
    }

    public int getFieldLimit() {
        return fieldLimit;
    }

    public void resetRiceCakes() {
        this.riceCakes = 0;
    }

    public void allowSummonState(boolean b) {
        MapleMap.this.allowSummons = b;
    }

    public boolean getSummonState() {
        return MapleMap.this.allowSummons;
    }

    public void warpEveryone(int to) {
        List<MapleCharacter> players;
        chrRLock.lock();
        try {
            players = new ArrayList<>(getCharacters());
        } finally {
            chrRLock.unlock();
        }

        for (MapleCharacter chr : players) {
            chr.changeMap(to);
        }
    }

    // BEGIN EVENTS
    public void setSnowball(int team, MapleSnowball ball) {
        switch (team) {
            case 0:
                this.snowball0 = ball;
                break;
            case 1:
                this.snowball1 = ball;
                break;
            default:
                break;
        }
    }

    public MapleSnowball getSnowball(int team) {
        switch (team) {
            case 0:
                return snowball0;
            case 1:
                return snowball1;
            default:
                return null;
        }
    }

    private boolean specialEquip() {//Maybe I shouldn't use fieldType :\
        return fieldType == 4 || fieldType == 19;
    }

    public void setCoconut(MapleCoconut nut) {
        this.coconut = nut;
    }

    public MapleCoconut getCoconut() {
        return coconut;
    }

    public void warpOutByTeam(int team, int mapid) {
        List<MapleCharacter> chars = new ArrayList<>(getCharacters());

        for (MapleCharacter chr : chars) {
            if (chr != null) {
                if (chr.getTeam() == team) {
                    chr.changeMap(mapid);
                }
            }
        }
    }

    public void startEvent(final MapleCharacter chr) {
        if (this.mapid == 109080000 && getCoconut() == null) {
            setCoconut(new MapleCoconut(this));
            coconut.startEvent();
        } else if (this.mapid == 109040000) {
            chr.setFitness(new MapleFitness(chr));
            chr.getFitness().startFitness();
        } else if (this.mapid == 109030101 || this.mapid == 109030201 || this.mapid == 109030301 || this.mapid == 109030401) {
            chr.setOla(new MapleOla(chr));
            chr.getOla().startOla();
        } else if (this.mapid == 109020001 && getOx() == null) {
            setOx(new MapleOxQuiz(this));
            getOx().sendQuestion();
            setOxQuiz(true);
        } else if (this.mapid == 109060000 && getSnowball(chr.getTeam()) == null) {
            setSnowball(0, new MapleSnowball(0, this));
            setSnowball(1, new MapleSnowball(1, this));
            getSnowball(chr.getTeam()).startEvent();
        }
    }

    public boolean eventStarted() {
        return eventstarted;
    }

    public void startEvent() {
        this.eventstarted = true;
    }

    public void setEventStarted(boolean event) {
        this.eventstarted = event;
    }

    public String getEventNPC() {
        StringBuilder sb = new StringBuilder();
        sb.append("Talk to ");
        if (mapid == 60000) {
            sb.append("Paul!");
        } else if (mapid == 104000000) {
            sb.append("Jean!");
        } else if (mapid == 200000000) {
            sb.append("Martin!");
        } else if (mapid == 220000000) {
            sb.append("Tony!");
        } else {
            return null;
        }
        return sb.toString();
    }

    public boolean hasEventNPC() {
        return this.mapid == 60000 || this.mapid == 104000000 || this.mapid == 200000000 || this.mapid == 220000000;
    }

    public boolean isStartingEventMap() {
        return this.mapid == 109040000 || this.mapid == 109020001 || this.mapid == 109010000 || this.mapid == 109030001 || this.mapid == 109030101;
    }

    public boolean isEventMap() {
        return this.mapid >= 109010000 && this.mapid < 109050000 || this.mapid > 109050001 && this.mapid <= 109090000;
    }

    public void timeMob(int id, String msg) {
        timeMob = new Pair<>(id, msg);
    }

    public Pair<Integer, String> getTimeMob() {
        return timeMob;
    }

    public void toggleHiddenNPC(int id) {
        for (MapleMapObject obj : mapobjects.values()) {
            if (obj.getType() == MapleMapObjectType.NPC) {
                MapleNPC npc = (MapleNPC) obj;
                if (npc.getId() == id) {
                    npc.setHide(!npc.isHidden());
                    if (!npc.isHidden()) //Should only be hidden upon changing maps
                    {
                        broadcastMessage(MaplePacketCreator.spawnNPC(npc));
                    }
                }
            }
        }
    }

    public final void removeNpc(final int npcid) {
        chrWLock.lock();
        try {
            for (MapleMapObject obj : mapobjects.values()) {
                if (obj.getType() == MapleMapObjectType.NPC) {
                    MapleNPC npc = (MapleNPC) obj;
                    if (npc.getId() == npcid || npcid == -1) {
                        broadcastMessage(MaplePacketCreator.removeNPC2(npc.getObjectId()));
                        broadcastMessage(MaplePacketCreator.removeNPCController(npc.getObjectId()));
                        mapobjects.remove(obj);
                    }
                }
            }
        } finally {
            chrWLock.unlock();
        }
    }

    public final void spawnNpc(final int id, final Point pos) {
        final MapleNPC npc = MapleLifeFactory.getNPC(id);
        npc.setPosition(pos);
        npc.setCy(pos.y);
        npc.setRx0(pos.x + 50);
        npc.setRx1(pos.x - 50);
        npc.setFh(getFootholds().findBelow(pos).getId());
        addMapObject(npc);
        broadcastMessage(MaplePacketCreator.spawnNPC(npc));
    }

    public void setMobInterval(short interval) {
        this.mobInterval = interval;
    }

    public short getMobInterval() {
        return mobInterval;
    }
}
