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
package server.life;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleJob;
import client.Skill;
import client.SkillFactory;
import client.inventory.Item;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.ServerConstants;
import constants.skills.FPMage;
import constants.skills.Hermit;
import constants.skills.ILMage;
import constants.skills.NightLord;
import constants.skills.NightWalker;
import constants.skills.Shadower;
import constants.skills.SuperGM;
import java.awt.Point;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import net.server.world.MapleParty;
import net.server.world.MaplePartyCharacter;
import scripting.event.EventInstanceManager;
import server.TimerManager;
import server.life.MapleLifeFactory.BanishInfo;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.ArrayMap;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.Randomizer;

public class MapleMonster extends AbstractLoadedMapleLife {

    private MapleMonsterStats overrideStats;
    private MapleMonsterStats stats;
    private int hp, mp;
    private WeakReference<MapleCharacter> controller = new WeakReference<>(null);
    private boolean controllerHasAggro, controllerKnowsAboutAggro;
    private EventInstanceManager eventInstance = null;
    private Collection<MonsterListener> listeners = new LinkedList<>();
    private EnumMap<MonsterStatus, MonsterStatusEffect> stati = new EnumMap<>(MonsterStatus.class);
    private ArrayList<MonsterStatus> alreadyBuffed = new ArrayList<MonsterStatus>();
    private MapleMap map;
    private int VenomMultiplier = 0;
    private boolean fake = false;
    private boolean dropsDisabled = false;
    private List<Pair<Integer, Integer>> usedSkills = new ArrayList<>();
    private Map<Pair<Integer, Integer>, Integer> skillsUsed = new HashMap<>();
    private List<MonsterStatus> monsterBuffs = new ArrayList<MonsterStatus>();
    private List<Integer> stolenItems = new ArrayList<>();
    private int team;
    private final HashMap<Integer, AtomicInteger> takenDamage = new HashMap<>();

    private int dojoPotSpawned = 1;
    private int dojoPotHPRate = 800000;

    public ReentrantLock monsterLock = new ReentrantLock();

    public MapleMonster(int id, MapleMonsterStats stats) {
        super(id);
        initWithStats(stats);
    }

    public MapleMonster(MapleMonster monster) {
        super(monster);
        initWithStats(monster.stats);
    }

    private void initWithStats(MapleMonsterStats stats) {
        setStance(5);
        this.stats = stats;
        hp = stats.getHp();
        mp = stats.getMp();
    }

    public void disableDrops() {
        this.dropsDisabled = true;
    }

    private int belongsTo = -1;

    public boolean belongsToSomeone() {
        return belongsTo != -1;
    }

    public int getBelongsTo() {
        return belongsTo;
    }

    public void setBelongTo(MapleCharacter chr) {
        belongsTo = chr.getId();
    }

    public boolean dropsDisabled() {
        return dropsDisabled;
    }

    public void setMap(MapleMap map) {
        this.map = map;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        if (overrideStats != null) {
            return overrideStats.getHp();
        }
        return stats.getHp();
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.mp = mp;
    }

    public int getMaxMp() {
        return stats.getMp();
    }

    public int getExp() {
        if (overrideStats != null) {
            return overrideStats.getExp();
        }
        return stats.getExp();
    }

    public void setExp(int xp) {
        stats.setExp(xp);
    }

    public int getLevel() {
        return stats.getLevel();
    }

    public int getCP() {
        return stats.getCP();
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getVenomMulti() {
        return this.VenomMultiplier;
    }

    public void setVenomMulti(int multiplier) {
        this.VenomMultiplier = multiplier;
    }

    public MapleMonsterStats getStats() {
        return stats;
    }

    public boolean isBoss() {
        return stats.isBoss() || isHT();
    }

    public int getAnimationTime(String name) {
        return stats.getAnimationTime(name);
    }

    private List<Integer> getRevives() {
        return stats.getRevives();
    }

    private byte getTagColor() {
        return stats.getTagColor();
    }

    private byte getTagBgColor() {
        return stats.getTagBgColor();
    }

    /**
     *
     * @param from the player that dealt the damage
     * @param damage
     */
    public synchronized void damage(MapleCharacter from, int damage) { // may be pointless synchronization
        if (!isAlive()) {
            return;
        }

        if (belongsToSomeone()) {
            if (getBelongsTo() != from.getId()) {
                return;
            }
        }

        // Because some Dojo bosses doesn't spawn potions
        if (getId() >= 9300184 && getId() <= 9300215) {
            if (!from.getDojoParty()) {
                if (getId() == 9300214 || getId() == 9300215) {
                    if (dojoPotHPRate != 500000) {
                        dojoPotHPRate = 500000;
                    }
                } else if (dojoPotHPRate != 800000) {
                    dojoPotHPRate = 800000;
                }
            } else if (from.getDojoPartyCount() == 2) {
                if (getId() == 9300214 || getId() == 9300215) {
                    if (dojoPotHPRate != 850000) {
                        dojoPotHPRate = 850000;
                    }
                } else if (dojoPotHPRate != 1300000) {
                    dojoPotHPRate = 1300000;
                }
            } else if (from.getDojoPartyCount() == 3) {
                if (getId() == 9300214 || getId() == 9300215) {
                    if (dojoPotHPRate != 1200000) {
                        dojoPotHPRate = 1200000;
                    }
                } else if (dojoPotHPRate != 1800000) {
                    dojoPotHPRate = 1800000;
                }
            } else if (from.getDojoPartyCount() == 4) {
                if (getId() == 9300214 || getId() == 9300215) {
                    if (dojoPotHPRate != 1550000) {
                        dojoPotHPRate = 1550000;
                    }
                } else if (dojoPotHPRate != 2300000) {
                    dojoPotHPRate = 2300000;
                }
            } else if (from.getDojoPartyCount() == 5) {
                if (getId() == 9300214 || getId() == 9300215) {
                    if (dojoPotHPRate != 1900000) {
                        dojoPotHPRate = 1900000;
                    }
                } else if (dojoPotHPRate != 2800000) {
                    dojoPotHPRate = 2800000;
                }
            } else if (from.getDojoPartyCount() == 6) {
                if (getId() == 9300214 || getId() == 9300215) {
                    if (dojoPotHPRate != 2250000) {
                        dojoPotHPRate = 2250000;
                    }
                } else if (dojoPotHPRate != 3300000) {
                    dojoPotHPRate = 3300000;
                }
            }

            if (getHp() <= getMaxHp() - (dojoPotSpawned * dojoPotHPRate)) {
                dojoPotSpawned++;

                for (MapleCharacter chr : from.getMap().getCharacters()) {
                    Item toDrop = new Item(2022162, (short) 0, (short) 1);
                    chr.getMap().spawnItemDrop(chr, chr, toDrop, chr.getPosition(), true, true);
                }
            }
        }

        int trueDamage = Math.min(hp, damage); // since magic happens otherwise B^)

        hp -= damage;
        if (takenDamage.containsKey(from.getId())) {
            takenDamage.get(from.getId()).addAndGet(trueDamage);
        } else {
            takenDamage.put(from.getId(), new AtomicInteger(trueDamage));
        }

        if (hasBossHPBar()) {
            from.getMap().broadcastMessage(makeBossHPBarPacket(), getPosition());
        } else if (!isBoss()) {
            int remainingHP = (int) Math.max(1, hp * 100f / getMaxHp());
            byte[] packet = MaplePacketCreator.showMonsterHP(getObjectId(), remainingHP);
            if (from.getParty() != null) {
                for (MaplePartyCharacter mpc : from.getParty().getMembers()) {
                    MapleCharacter member = from.getMap().getCharacterById(mpc.getId()); // god bless
                    if (member != null) {
                        member.announce(packet.clone()); // clone it just in case of crypto
                    }
                }
            } else {
                from.announce(packet);
            }
        }
    }

    public void heal(int hp, int mp) {
        int hp2Heal = getHp() + hp;
        int mp2Heal = getMp() + mp;
        if (hp2Heal >= getMaxHp()) {
            hp2Heal = getMaxHp();
        }
        if (mp2Heal >= getMaxMp()) {
            mp2Heal = getMaxMp();
        }
        setHp(hp2Heal);
        setMp(mp2Heal);
        getMap().broadcastMessage(MaplePacketCreator.healMonster(getObjectId(), hp));
    }

    public boolean isAttackedBy(MapleCharacter chr) {
        return takenDamage.containsKey(chr.getId());
    }

    private void distributeExperienceToParty(int pid, int exp, int killer, Map<Integer, Integer> expDist) {
        LinkedList<MapleCharacter> members = new LinkedList<>();

        map.getCharacterReadLock().lock();
        Collection<MapleCharacter> chrs = map.getCharacters();
        try {
            for (MapleCharacter mc : chrs) {
                if (mc.getPartyId() == pid) {
                    members.add(mc);
                }
            }
        } finally {
            map.getCharacterReadLock().unlock();
        }

        final int minLevel = getLevel() - 5;

        int partyLevel = 0;
        int leechMinLevel = 0;

        for (MapleCharacter mc : members) {
            if (mc.getLevel() >= minLevel) {
                leechMinLevel = Math.min(mc.getLevel() - 5, minLevel);
            }
        }

        int leechCount = 0;
        for (MapleCharacter mc : members) {
            if (mc.getLevel() >= leechMinLevel) {
                partyLevel += mc.getLevel();
                leechCount++;
            }
        }

        final int mostDamageCid = getHighestDamagerId();

        for (MapleCharacter mc : members) {
            int id = mc.getId();
            int level = mc.getLevel();
            if (expDist.containsKey(id)
                    || level >= leechMinLevel) {
                boolean isKiller = killer == id;
                boolean mostDamage = mostDamageCid == id;
                int xp = (int) (exp * 0.80f * level / partyLevel);
                if (mostDamage) {
                    xp += (exp * 0.20f);
                }
                giveExpToCharacter(mc, xp, isKiller, leechCount);

                if (members.size() >= 3 && !isKiller) {
                    mc.gainDecimalMobPoints(0.1d);
                }
            }
        }
    }

    public void distributeExperience(int killerId) {
        if (isAlive()) {
            return;
        }

        int exp;

        if (map.isExpBuffedMap()) {
            exp = (int) (getExp() * 0.5) + getExp();
        } else {
            exp = getExp();
        }

        int totalHealth = getMaxHp();
        Map<Integer, Integer> expDist = new HashMap<>();
        Map<Integer, Integer> partyExp = new HashMap<>();
        // 80% of pool is split amongst all the damagers
        for (Entry<Integer, AtomicInteger> damage : takenDamage.entrySet()) {
            expDist.put(damage.getKey(), (int) (0.80f * exp * damage.getValue().get() / totalHealth));
        }
        map.getCharacterReadLock().lock(); // avoid concurrent mod
        Collection<MapleCharacter> chrs = map.getCharacters();
        try {
            for (MapleCharacter mc : chrs) {
                if (expDist.containsKey(mc.getId())) {
                    boolean isKiller = mc.getId() == killerId;
                    int xp = expDist.get(mc.getId());
                    if (isKiller) {
                        xp += exp / 5;
                    }
                    MapleParty p = mc.getParty();

                    if (p != null) {
                        int pID = p.getId();
                        int pXP = xp + (partyExp.containsKey(pID) ? partyExp.get(pID) : 0);
                        partyExp.put(pID, pXP);
                    } else {
                        if (mc.getId() == killerId && mc.getEventInstance() == null) {
                            switch (getId()) {
                                case 9400549:
                                    mc.gainMobPoints(50);
                                    mc.message("You have earned 50 mob points for killing a boss monster.");
                                    break;
                                case 9400121:
                                    mc.gainMobPoints(600);
                                    mc.message("You have earned 600 mob points for killing a boss monster.");
                                    break;
                                case 9400575:
                                    mc.gainMobPoints(300);
                                    mc.message("You have earned 300 mob points for killing a boss monster.");
                                    break;
                                case 8510000:
                                case 8520000:
                                    mc.gainMobPoints(180);
                                    mc.message("You have earned 180 mob points for killing a boss monster.");
                                    break;
                                case 9400014:
                                    mc.gainMobPoints(200);
                                    mc.message("You have earned 200 mob points for killing a boss monster.");
                                    break;
                                case 8220003:
                                    mc.gainMobPoints(40);
                                    mc.message("You have earned 40 mob points for killing a boss monster.");
                                    break;
                                case 8500002:
                                    mc.gainMobPoints(150);
                                    mc.message("You have earned 150 mob points for killing a boss monster.");
                                    break;
                                default:
                                    break;
                            }
                        }

                        giveExpToCharacter(mc, xp, isKiller, 1);
                    }
                }
            }
        } finally {
            map.getCharacterReadLock().unlock();
        }
        for (Entry<Integer, Integer> party : partyExp.entrySet()) {
            LinkedList<MapleCharacter> members = new LinkedList<>();

            for (MapleCharacter mc : chrs) {
                if (mc.getPartyId() == party.getKey()) {
                    members.add(mc);
                }
            }

            for (MapleCharacter mc : members) {
                if (mc.getEventInstance() == null) {
                    switch (getId()) {
                        case 9400549:
                            mc.gainMobPoints(Math.round(50 / members.size()));
                            mc.message("You and your party member(s) earned " + Math.round(50 / members.size()) + " mob points for killing a boss monster.");
                            break;
                        case 9400121:
                            mc.gainMobPoints(Math.round(600 / members.size()));
                            mc.message("You and your party member(s) earned " + Math.round(600 / members.size()) + " mob points for killing a boss monster.");
                            break;
                        case 9400575:
                            mc.gainMobPoints(Math.round(300 / members.size()));
                            mc.message("You and your party member(s) earned " + Math.round(300 / members.size()) + " mob points for killing a boss monster.");
                            break;
                        case 8510000:
                        case 8520000:
                            mc.gainMobPoints(Math.round(180 / members.size()));
                            mc.message("You and your party member(s) earned " + Math.round(180 / members.size()) + " mob points for killing a boss monster.");
                            break;
                        case 9400014:
                            mc.gainMobPoints(Math.round(200 / members.size()));
                            mc.message("You and your party member(s) earned " + Math.round(200 / members.size()) + " mob points for killing a boss monster.");
                            break;
                        case 8220003:
                            mc.gainMobPoints(Math.round(40 / members.size()));
                            mc.message("You and your party member(s) earned " + Math.round(40 / members.size()) + " mob points for killing a boss monster.");
                            break;
                        case 8500002:
                            mc.gainMobPoints(Math.round(150 / members.size()));
                            mc.message("You and your party member(s) earned " + Math.round(150 / members.size()) + " mob points for killing a boss monster.");
                            break;
                        default:
                            break;
                    }
                }
            }

            distributeExperienceToParty(party.getKey(), party.getValue(), killerId, expDist);
        }
    }

    public void giveExpToCharacter(MapleCharacter attacker, int exp, boolean isKiller, int numExpSharers) {
        if (isKiller) {
            if (eventInstance != null) {
                eventInstance.monsterKilled(attacker, this);
            }
        }
        final int partyModifier = numExpSharers > 1 ? (110 + (5 * (numExpSharers - 2))) : 0;

        int partyExp = 0;

        if (overrideStats != null) {
            exp = overrideStats.getExp();
        }

        if (attacker.getHp() > 0) {
            int personalExp = exp * attacker.getExpRate();

            if (exp > 0) {
                if (partyModifier > 0) {
                    partyExp = (int) (personalExp * ServerConstants.PARTY_EXPERIENCE_MOD * partyModifier / 1000f);
                }
                Integer holySymbol = attacker.getBuffedValue(MapleBuffStat.HOLY_SYMBOL);
                boolean GMHolySymbol = attacker.getBuffSource(MapleBuffStat.HOLY_SYMBOL) == SuperGM.HOLY_SYMBOL;
                if (holySymbol != null) {
                    if (numExpSharers == 1 && !GMHolySymbol) {
                        personalExp *= 1.0 + (holySymbol.doubleValue() / 500.0);
                    } else {
                        personalExp *= 1.0 + (holySymbol.doubleValue() / 100.0);
                    }
                }
                if (stati.containsKey(MonsterStatus.SHOWDOWN)) {
                    personalExp *= (stati.get(MonsterStatus.SHOWDOWN).getStati().get(MonsterStatus.SHOWDOWN).doubleValue() / 100.0 + 1.0);
                }
            }
            if (exp < 0) {//O.O ><
                personalExp = Integer.MAX_VALUE;
            }

            attacker.gainExp(personalExp, partyExp, true, false, isKiller);

            attacker.mobKilled(getId());
            attacker.increaseEquipExp(personalExp);//better place
        }
    }

    public MapleCharacter killBy(MapleCharacter killer) {
        distributeExperience(killer != null ? killer.getId() : 0);

        if (getController() != null) { // this can/should only happen when a hidden gm attacks the monster
            getController().getClient().announce(MaplePacketCreator.stopControllingMonster(this.getObjectId()));
            getController().stopControllingMonster(this);
        }

        final List<Integer> toSpawn = this.getRevives(); // this doesn't work (?)
        if (toSpawn != null) {
            final MapleMap reviveMap = killer.getMap();
            if (toSpawn.contains(9300216) && reviveMap.getId() > 925000000 && reviveMap.getId() < 926000000) {
                reviveMap.broadcastMessage(MaplePacketCreator.playSound("Dojang/clear"));
                reviveMap.broadcastMessage(MaplePacketCreator.showEffect("dojang/end/clear"));
            }
            Pair<Integer, String> timeMob = reviveMap.getTimeMob();
            if (timeMob != null) {
                if (toSpawn.contains(timeMob.getLeft())) {
                    reviveMap.broadcastMessage(MaplePacketCreator.serverNotice(6, timeMob.getRight()));
                }

                if (timeMob.getLeft() == 9300338 && (reviveMap.getId() >= 922240100 && reviveMap.getId() <= 922240119)) {
                    if (!reviveMap.containsNPC(9001108)) {
                        MapleNPC npc = MapleLifeFactory.getNPC(9001108);
                        npc.setPosition(new Point(172, 9));
                        npc.setCy(9);
                        npc.setRx0(172 + 50);
                        npc.setRx1(172 - 50);
                        npc.setFh(27);
                        reviveMap.addMapObject(npc);
                        reviveMap.broadcastMessage(MaplePacketCreator.spawnNPC(npc));
                    } else {
                        reviveMap.toggleHiddenNPC(9001108);
                    }
                }
            }
            TimerManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    for (Integer mid : toSpawn) {
                        final MapleMonster mob = MapleLifeFactory.getMonster(mid);
                        mob.setPosition(getPosition());
                        if (dropsDisabled()) {
                            mob.disableDrops();
                        }
                        reviveMap.spawnMonster(mob);
                    }
                }
            }, getAnimationTime("die1"));
        }
        if (eventInstance != null) {
            if (!this.getStats().isFriendly()) {
                eventInstance.monsterKilled(this);
                eventInstance.specificMonsterKilled(this);
            }
        }
        // idk V just a troll
        for (MonsterListener listener : listeners.toArray(new MonsterListener[listeners.size()])) {
            listener.monsterKilled(getAnimationTime("die1"));
        }

        MapleCharacter looter = map.getCharacterById(getHighestDamagerId());

        return looter != null ? looter : killer;
    }

    // should only really be used to determine drop owner
    private int getHighestDamagerId() {
        int curId = 0;
        int curDmg = 0;

        for (Entry<Integer, AtomicInteger> damage : takenDamage.entrySet()) {
            curId = damage.getValue().get() >= curDmg ? damage.getKey() : curId;
            curDmg = damage.getKey() == curId ? damage.getValue().get() : curDmg;
        }

        return curId;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public MapleCharacter getController() {
        return controller.get();
    }

    public void setController(MapleCharacter controller) {
        this.controller = new WeakReference<>(controller);
    }

    public void switchController(MapleCharacter newController, boolean immediateAggro) {
        MapleCharacter controllers = getController();
        if (controllers == newController) {
            return;
        }
        if (controllers != null) {
            controllers.stopControllingMonster(this);
            controllers.getClient().announce(MaplePacketCreator.stopControllingMonster(getObjectId()));
        }
        newController.controlMonster(this, immediateAggro);
        setController(newController);
        if (immediateAggro) {
            setControllerHasAggro(true);
        }
        setControllerKnowsAboutAggro(false);
    }

    public void addListener(MonsterListener listener) {
        listeners.add(listener);
    }

    public boolean isControllerHasAggro() {
        return fake ? false : controllerHasAggro;
    }

    public void setControllerHasAggro(boolean controllerHasAggro) {
        if (fake) {
            return;
        }
        this.controllerHasAggro = controllerHasAggro;
    }

    public void setOverrideStats(MapleMonsterStats override) {
        this.overrideStats = override;
        this.hp = override.getHp();
        this.mp = override.getMp();
    }

    public boolean isControllerKnowsAboutAggro() {
        return fake ? false : controllerKnowsAboutAggro;
    }

    public void setControllerKnowsAboutAggro(boolean controllerKnowsAboutAggro) {
        if (fake) {
            return;
        }
        this.controllerKnowsAboutAggro = controllerKnowsAboutAggro;
    }

    public byte[] makeBossHPBarPacket() {
        return MaplePacketCreator.showBossHP(getId(), getHp(), getMaxHp(), getTagColor(), getTagBgColor());
    }

    public boolean hasBossHPBar() {
        return (isBoss() && getTagColor() > 0) || isHT();
    }

    private boolean isHT() {
        return getId() == 8810018;
    }

    @Override
    public void sendSpawnData(MapleClient c) {
        if (!isAlive()) {
            return;
        }
        if (isFake()) {
            c.announce(MaplePacketCreator.spawnFakeMonster(this, 0));
        } else {
            c.announce(MaplePacketCreator.spawnMonster(this, false));
        }
        if (stati.size() > 0) {
            for (final MonsterStatusEffect mse : this.stati.values()) {
                c.announce(MaplePacketCreator.applyMonsterStatus(getObjectId(), mse, null));
            }
        }
        if (hasBossHPBar()) {
            if (this.getMap().countMonster(8810026) > 0 && this.getMap().getId() == 240060200) {
                this.getMap().killAllMonsters();
                return;
            }
            c.announce(makeBossHPBarPacket());
        }
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.announce(MaplePacketCreator.killMonster(getObjectId(), false));
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.MONSTER;
    }

    public void setEventInstance(EventInstanceManager eventInstance) {
        this.eventInstance = eventInstance;
    }

    public EventInstanceManager getEventInstance() {
        return eventInstance;
    }

    public boolean isMobile() {
        return stats.isMobile();
    }

    public ElementalEffectiveness getEffectiveness(Element e) {
        if (stati.size() > 0 && stati.get(MonsterStatus.DOOM) != null) {
            return ElementalEffectiveness.NORMAL; // like blue snails
        }
        return stats.getEffectiveness(e);
    }

    public boolean applyStatus(MapleCharacter from, final MonsterStatusEffect status, boolean poison, long duration) {
        return applyStatus(from, status, poison, duration, false);
    }

    public boolean applyStatus(MapleCharacter from, final MonsterStatusEffect status, boolean poison, long duration, boolean venom) {
        switch (stats.getEffectiveness(status.getSkill().getElement())) {
            case IMMUNE:
            case STRONG:
            case NEUTRAL:
                return false;
            case NORMAL:
            case WEAK:
                break;
            default: {
                System.out.println("Unknown elemental effectiveness: " + stats.getEffectiveness(status.getSkill().getElement()));
                return false;
            }
        }

        if (status.getSkill().getId() == FPMage.ELEMENT_COMPOSITION) { // fp compo
            ElementalEffectiveness effectiveness = stats.getEffectiveness(Element.POISON);
            if (effectiveness == ElementalEffectiveness.IMMUNE || effectiveness == ElementalEffectiveness.STRONG) {
                return false;
            }
        } else if (status.getSkill().getId() == ILMage.ELEMENT_COMPOSITION) { // il compo
            ElementalEffectiveness effectiveness = stats.getEffectiveness(Element.ICE);
            if (effectiveness == ElementalEffectiveness.IMMUNE || effectiveness == ElementalEffectiveness.STRONG) {
                return false;
            }
        } else if (status.getSkill().getId() == NightLord.VENOMOUS_STAR || status.getSkill().getId() == Shadower.VENOMOUS_STAB || status.getSkill().getId() == NightWalker.VENOM) {// venom
            if (stats.getEffectiveness(Element.POISON) == ElementalEffectiveness.WEAK) {
                return false;
            }
        }
        if (poison && getHp() <= 1) {
            return false;
        }

        final Map<MonsterStatus, Integer> statis = status.getStati();
        if (stats.isBoss()) {
            if (!(statis.containsKey(MonsterStatus.SPEED)
                    && statis.containsKey(MonsterStatus.NINJA_AMBUSH)
                    && statis.containsKey(MonsterStatus.WATK))) {
                return false;
            }
        }

        for (MonsterStatus stat : statis.keySet()) {
            final MonsterStatusEffect oldEffect = stati.get(stat);
            if (oldEffect != null) {
                oldEffect.removeActiveStatus(stat);
                if (oldEffect.getStati().isEmpty()) {
                    oldEffect.cancelTask();
                    oldEffect.cancelDamageSchedule();
                }
            }
        }

        TimerManager timerManager = TimerManager.getInstance();
        final Runnable cancelTask = new Runnable() {

            @Override
            public void run() {
                if (isAlive()) {
                    byte[] packet = MaplePacketCreator.cancelMonsterStatus(getObjectId(), status.getStati());
                    map.broadcastMessage(packet, getPosition());
                    if (getController() != null && !getController().isMapObjectVisible(MapleMonster.this)) {
                        getController().getClient().announce(packet);
                    }
                }
                for (MonsterStatus stat : status.getStati().keySet()) {
                    stati.remove(stat);
                }
                setVenomMulti(0);
                status.cancelDamageSchedule();
            }
        };
        if (poison) {
            int poisonLevel = from.getSkillLevel(status.getSkill());
            int poisonDamage = Math.min(Short.MAX_VALUE, (int) (getMaxHp() / (70.0 - poisonLevel) + 0.999));
            status.setValue(MonsterStatus.POISON, Integer.valueOf(poisonDamage));
            status.setDamageSchedule(timerManager.register(new DamageTask(poisonDamage, from, status, cancelTask, 0), 1000, 1000));
        } else if (venom) {
            if (from.getJob() == MapleJob.NIGHTLORD || from.getJob() == MapleJob.SHADOWER || from.getJob().isA(MapleJob.NIGHTWALKER3)) {
                int poisonLevel, matk, id = from.getJob().getId();
                int skill = (id == 412 ? NightLord.VENOMOUS_STAR : (id == 422 ? Shadower.VENOMOUS_STAB : NightWalker.VENOM));
                poisonLevel = from.getSkillLevel(SkillFactory.getSkill(skill));
                if (poisonLevel <= 0) {
                    return false;
                }
                matk = SkillFactory.getSkill(skill).getEffect(poisonLevel).getMatk();
                int luk = from.getLuk();
                int maxDmg = (int) Math.ceil(Math.min(Short.MAX_VALUE, 0.2 * luk * matk));
                int minDmg = (int) Math.ceil(Math.min(Short.MAX_VALUE, 0.1 * luk * matk));
                int gap = maxDmg - minDmg;
                if (gap == 0) {
                    gap = 1;
                }
                int poisonDamage = 0;
                for (int i = 0; i < getVenomMulti(); i++) {
                    poisonDamage += (Randomizer.nextInt(gap) + minDmg);
                }
                poisonDamage = Math.min(Short.MAX_VALUE, poisonDamage);
                status.setValue(MonsterStatus.VENOMOUS_WEAPON, Integer.valueOf(poisonDamage));
                status.setDamageSchedule(timerManager.register(new DamageTask(poisonDamage, from, status, cancelTask, 0), 1000, 1000));
            } else {
                return false;
            }

        } else if (status.getSkill().getId() == Hermit.SHADOW_WEB || status.getSkill().getId() == NightWalker.SHADOW_WEB) { //Shadow Web
            if (getBelongsTo() == -1) {
                status.setDamageSchedule(timerManager.schedule(new DamageTask((int) (getMaxHp() / 50.0 + 0.999), from, status, cancelTask, 1), 3500));
            } else {
                return false;
            }
        } else if (status.getSkill().getId() == 4121004 || status.getSkill().getId() == 4221004) { // Ninja Ambush
            final Skill skill = SkillFactory.getSkill(status.getSkill().getId());
            final byte level = from.getSkillLevel(skill);
            final int damage = (int) ((from.getStr() + from.getLuk()) * (1.5 + (level * 0.05)) * skill.getEffect(level).getDamage());
            /*if (getHp() - damage <= 1)  { make hp 1 betch
             damage = getHp() - (getHp() - 1);
             }*/

            status.setValue(MonsterStatus.NINJA_AMBUSH, Integer.valueOf(damage));
            status.setDamageSchedule(timerManager.register(new DamageTask(damage, from, status, cancelTask, 2), 1000, 1000));
        }
        for (MonsterStatus stat : status.getStati().keySet()) {
            stati.put(stat, status);
            alreadyBuffed.add(stat);
        }
        int animationTime = status.getSkill().getAnimationTime();
        byte[] packet = MaplePacketCreator.applyMonsterStatus(getObjectId(), status, null);
        map.broadcastMessage(packet, getPosition());
        if (getController() != null && !getController().isMapObjectVisible(this)) {
            getController().getClient().announce(packet);
        }
        status.setCancelTask(timerManager.schedule(cancelTask, duration + animationTime));
        return true;
    }

    public void applyMonsterBuff(final MonsterStatus status, final int x, int skillId, long duration, MobSkill skill) {
        TimerManager timerManager = TimerManager.getInstance();
        Map<MonsterStatus, Integer> stats = new ArrayMap<MonsterStatus, Integer>();
        stats.put(status, skill.getX());

        final Runnable cancelTask = new Runnable() {
            @Override
            public void run() {
                if (isAlive()) {
                    byte[] packet = MaplePacketCreator.cancelMonsterStatus(getObjectId(), Collections.singletonMap(status, Integer.valueOf(x)));
                    map.broadcastMessage(packet, getPosition());
                    if (getController() != null && !getController().isMapObjectVisible(MapleMonster.this)) {
                        getController().getClient().getSession().write(packet);
                    }
                    stati.remove(status);

                }
            }
        };
        final MonsterStatusEffect effect = new MonsterStatusEffect(stats, null, skill, true);
        byte[] packet = MaplePacketCreator.applyMonsterStatus(getObjectId(), Collections.singletonMap(status, x), skillId, true, 0, skill);
        map.broadcastMessage(packet, getPosition());
        if (getController() != null && !getController().isMapObjectVisible(this)) {
            getController().getClient().getSession().write(packet);
        }
        timerManager.schedule(cancelTask, duration);
        stati.put(status, effect);
        alreadyBuffed.add(status);

    }

    public void applyMonsterBuff(final Map<MonsterStatus, Integer> stats, final int x, int skillId, long duration, MobSkill skill, final List<Integer> reflection) {
        TimerManager timerManager = TimerManager.getInstance();
        final Runnable cancelTask = new Runnable() {

            @Override
            public void run() {
                if (isAlive()) {
                    byte[] packet = MaplePacketCreator.cancelMonsterStatus(getObjectId(), stats);
                    map.broadcastMessage(packet, getPosition());
                    if (getController() != null && !getController().isMapObjectVisible(MapleMonster.this)) {
                        getController().getClient().announce(packet);
                    }
                    for (final MonsterStatus stat : stats.keySet()) {
                        stati.remove(stat);
                    }
                }
            }
        };
        final MonsterStatusEffect effect = new MonsterStatusEffect(stats, null, skill, true);
        byte[] packet = MaplePacketCreator.applyMonsterStatus(getObjectId(), effect, reflection);
        map.broadcastMessage(packet, getPosition());
        for (MonsterStatus stat : stats.keySet()) {
            stati.put(stat, effect);
            alreadyBuffed.add(stat);
        }
        if (getController() != null && !getController().isMapObjectVisible(this)) {
            getController().getClient().announce(packet);
        }
        effect.setCancelTask(timerManager.schedule(cancelTask, duration));
    }

    public void debuffMob(int skillid) {
        //skillid is not going to be used for now until I get warrior debuff working
        MonsterStatus[] stats = {MonsterStatus.WEAPON_ATTACK_UP, MonsterStatus.WEAPON_DEFENSE_UP, MonsterStatus.MAGIC_ATTACK_UP, MonsterStatus.MAGIC_DEFENSE_UP};
        for (int i = 0; i < stats.length; i++) {
            if (isBuffed(stats[i])) {
                final MonsterStatusEffect oldEffect = stati.get(stats[i]);
                byte[] packet = MaplePacketCreator.cancelMonsterStatus(getObjectId(), oldEffect.getStati());
                map.broadcastMessage(packet, getPosition());
                if (getController() != null && !getController().isMapObjectVisible(MapleMonster.this)) {
                    getController().getClient().announce(packet);
                }
                stati.remove(stats);
            }
        }
    }

    public boolean isBuffed(MonsterStatus status) {
        return stati.containsKey(status);
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    public boolean isFake() {
        return fake;
    }

    public MapleMap getMap() {
        return map;
    }

    public List<Pair<Integer, Integer>> getSkills() {
        return stats.getSkills();
    }

    public boolean hasSkill(int skillId, int level) {
        return stats.hasSkill(skillId, level);
    }

    public boolean canUseSkill(MobSkill toUse) {
        if (toUse == null) {
            return false;
        }
        for (Pair<Integer, Integer> skill : usedSkills) {
            if (skill.getLeft() == toUse.getSkillId() && skill.getRight() == toUse.getSkillLevel()) {
                return false;
            }
        }
        if (toUse.getLimit() > 0) {
            if (this.skillsUsed.containsKey(new Pair<>(toUse.getSkillId(), toUse.getSkillLevel()))) {
                int times = this.skillsUsed.get(new Pair<>(toUse.getSkillId(), toUse.getSkillLevel()));
                if (times >= toUse.getLimit()) {
                    return false;
                }
            }
        }
        if (toUse.getSkillId() == 200) {
            Collection<MapleMapObject> mmo = getMap().getMapObjects();
            int i = 0;
            for (MapleMapObject mo : mmo) {
                if (mo.getType() == MapleMapObjectType.MONSTER) {
                    i++;
                }
            }
            if (i > 100) {
                return false;
            }
        }
        return true;
    }

    public void usedSkill(final int skillId, final int level, long cooltime) {
        this.usedSkills.add(new Pair<>(skillId, level));
        if (this.skillsUsed.containsKey(new Pair<>(skillId, level))) {
            int times = this.skillsUsed.get(new Pair<>(skillId, level)) + 1;
            this.skillsUsed.remove(new Pair<>(skillId, level));
            this.skillsUsed.put(new Pair<>(skillId, level), times);
        } else {
            this.skillsUsed.put(new Pair<>(skillId, level), 1);
        }
        final MapleMonster mons = this;
        TimerManager tMan = TimerManager.getInstance();
        tMan.schedule(
                new Runnable() {

            @Override
            public void run() {
                mons.clearSkill(skillId, level);
            }
        }, cooltime);
    }

    public void clearSkill(int skillId, int level) {
        int index = -1;
        for (Pair<Integer, Integer> skill : usedSkills) {
            if (skill.getLeft() == skillId && skill.getRight() == level) {
                index = usedSkills.indexOf(skill);
                break;
            }
        }
        if (index != -1) {
            usedSkills.remove(index);
        }
    }

    public int getNoSkills() {
        return this.stats.getNoSkills();
    }

    public boolean isFirstAttack() {
        return this.stats.isFirstAttack();
    }

    public int getBuffToGive() {
        return this.stats.getBuffToGive();
    }

    public List<MonsterStatus> getMonsterBuffs() {
        return monsterBuffs;
    }

    public void cancelMonsterBuff(MonsterStatus status) {
        if (isAlive()) {
            byte[] packet = MaplePacketCreator.cancelMonsterStatus(getObjectId(),
                    Collections.singletonMap(status, 1));
            map.broadcastMessage(packet, getPosition());
            if (getController() != null && !getController().isMapObjectVisible(MapleMonster.this)) {
                getController().getClient().getSession().write(packet);
            }
            removeMonsterBuff(status);
        }
    }

    public void removeMonsterBuff(MonsterStatus status) {
        this.monsterBuffs.remove(status);
    }

    private final class DamageTask implements Runnable {

        private final int dealDamage;
        private final MapleCharacter chr;
        private final MonsterStatusEffect status;
        private final Runnable cancelTask;
        private final int type;
        private final MapleMap map;

        private DamageTask(int dealDamage, MapleCharacter chr, MonsterStatusEffect status, Runnable cancelTask, int type) {
            this.dealDamage = dealDamage;
            this.chr = chr;
            this.status = status;
            this.cancelTask = cancelTask;
            this.type = type;
            this.map = chr.getMap();
        }

        @Override
        public void run() {
            int damage = dealDamage;

            if (damage >= hp) {
                damage = hp - 1;
                if (type == 1 || type == 2) {
                    map.broadcastMessage(MaplePacketCreator.damageMonster(getObjectId(), damage), getPosition());
                    cancelTask.run();
                    status.getCancelTask().cancel(false);
                }
            }
            if (hp > 1 && damage > 0) {
                damage(chr, damage);
                if (type == 1) {
                    map.broadcastMessage(MaplePacketCreator.damageMonster(getObjectId(), damage), getPosition());
                }
            }
        }
    }

    public String getName() {
        return stats.getName();
    }

    public void addStolen(int itemId) {
        stolenItems.add(itemId);
    }

    public List<Integer> getStolen() {
        return stolenItems;
    }

    public void setTempEffectiveness(Element e, ElementalEffectiveness ee, long milli) {
        final Element fE = e;
        final ElementalEffectiveness fEE = stats.getEffectiveness(e);
        if (!stats.getEffectiveness(e).equals(ElementalEffectiveness.WEAK)) {
            stats.setEffectiveness(e, ee);
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    stats.removeEffectiveness(fE);
                    stats.setEffectiveness(fE, fEE);
                }
            }, milli);
        }
    }

    public Collection<MonsterStatus> alreadyBuffedStats() {
        return Collections.unmodifiableCollection(alreadyBuffed);
    }

    public BanishInfo getBanish() {
        return stats.getBanishInfo();
    }

    public void setBoss(boolean boss) {
        this.stats.setBoss(boss);
    }

    public int getDropPeriodTime() {
        return stats.getDropPeriod();
    }

    public int getPADamage() {
        return stats.getPADamage();
    }

    public Map<MonsterStatus, MonsterStatusEffect> getStati() {
        return stati;
    }
}
