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
package scripting.event;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.script.ScriptException;

import net.server.world.MapleParty;
import net.server.world.MaplePartyCharacter;
import provider.MapleDataProviderFactory;
import server.TimerManager;
import server.expeditions.MapleExpedition;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleMapFactory;
import tools.DatabaseConnection;
import client.MapleCharacter;
import tools.LogHelper;

/**
 *
 * @author Matze
 */
public class EventInstanceManager {

    private List<MapleCharacter> chars = new ArrayList<>();
    private List<MapleMonster> mobs = new LinkedList<>();
    private Map<MapleCharacter, Integer> killCount = new HashMap<>();
    private EventManager em;
    private MapleMapFactory mapFactory;
    private String name;
    private Properties props = new Properties();
    private long timeStarted = 0;
    private long eventTime = 0;
    private MapleExpedition expedition = null;
    private boolean disposed = false;

    public EventInstanceManager(EventManager em, String name) {
        this.em = em;
        this.name = name;
        mapFactory = new MapleMapFactory(MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Map.wz")), MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/String.wz")), (byte) 0, (byte) 1);//Fk this
        mapFactory.setChannel(em.getChannelServer().getId());
    }

    public EventManager getEm() {
        return em;
    }

    public void registerPlayer(MapleCharacter chr) {
        if (chr == null || !chr.isLoggedin() || disposed) {
            return;
        }
        try {
            chars.add(chr);
            chr.setEventInstance(this);
            em.getIv().invokeFunction("playerEntry", this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    public void startEventTimer(long time) {
        if (disposed) {
            return;
        }

        timeStarted = System.currentTimeMillis();
        eventTime = time;
    }

    public boolean isTimerStarted() {
        return eventTime > 0 && timeStarted > 0;
    }

    public long getTimeLeft() {
        return eventTime - (System.currentTimeMillis() - timeStarted);
    }

    public void registerParty(MapleParty party, MapleMap map) {
        if (disposed) {
            return;
        }

        for (MaplePartyCharacter pc : party.getMembers()) {
            MapleCharacter c = map.getCharacterById(pc.getId());
            registerPlayer(c);
        }
    }

    public MapleExpedition getExpedition() {
        return expedition;
    }

    public void registerExpedition(MapleExpedition exped) {
        expedition = exped;
        registerPlayer(exped.getLeader());
    }

    public void unregisterPlayer(MapleCharacter chr) {
        chars.remove(chr);
        chr.setEventInstance(null);
    }

    public int getPlayerCount() {
        return chars.size();
    }

    public void changedMap(final MapleCharacter chr, final int mapid) {
        try {
            em.getIv().invokeFunction("changedMap", this, chr, mapid);
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    public List<MapleCharacter> getPlayers() {
        return new ArrayList<>(chars);
    }

    public void registerMonster(MapleMonster mob) {
        if (disposed) {
            return;
        }

        if (!mob.getStats().isFriendly()) { //We cannot register moon bunny
            mobs.add(mob);
            mob.setEventInstance(this);
        }
    }

    public void movePlayer(MapleCharacter chr) {
        if (disposed) {
            return;
        }

        try {
            em.getIv().invokeFunction("moveMap", this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    public void monsterKilled(MapleMonster mob) {
        if (disposed) {
            return;
        }

        mobs.remove(mob);
        if (mobs.isEmpty()) {
            try {
                em.getIv().invokeFunction("allMonstersDead", this);
            } catch (ScriptException | NoSuchMethodException ex) {
            }
        }
    }

    public void specificMonsterKilled(MapleMonster mob) {
        if (disposed) {
            return;
        }

        try {
            em.getIv().invokeFunction("specificMonsterKilled", this, mob.getId());
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    public void playerKilled(MapleCharacter chr) {
        if (disposed) {
            return;
        }

        try {
            em.getIv().invokeFunction("playerDead", this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    public boolean revivePlayer(MapleCharacter chr) {
        if (disposed) {
            return false;
        }

        try {
            Object b = em.getIv().invokeFunction("playerRevive", this, chr);
            if (b instanceof Boolean) {
                return (Boolean) b;
            }
        } catch (ScriptException | NoSuchMethodException ex) {
        }
        return true;
    }

    public void playerDisconnected(MapleCharacter chr) {
        if (disposed) {
            return;
        }

        try {
            em.getIv().invokeFunction("playerDisconnected", this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    /**
     *
     * @param chr
     * @param mob
     */
    public void monsterKilled(MapleCharacter chr, MapleMonster mob) {
        if (disposed) {
            return;
        }

        try {
            Integer kc = killCount.get(chr);
            int inc = ((Number) em.getIv().invokeFunction("monsterValue", this, mob.getId())).intValue();
            if (kc == null) {
                kc = inc;
            } else {
                kc += inc;
            }
            killCount.put(chr, kc);
            if (expedition != null) {
                expedition.monsterKilled(chr, mob);
            }
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    public int getKillCount(MapleCharacter chr) {
        if (disposed) {
            return 0;
        }

        Integer kc = killCount.get(chr);
        if (kc == null) {
            return 0;
        } else {
            return kc;
        }
    }

    public void dispose() {
        if (disposed || em == null) {
            return;
        }

        try {
            em.getIv().invokeFunction("dispose", this);
        } catch (ScriptException | NoSuchMethodException ex) {
        }
        disposed = true;
        chars.clear();
        mobs.clear();
        killCount.clear();
        mapFactory = null;
        if (expedition != null) {
            em.getChannelServer().getExpeditions().remove(expedition);
        }
        em.disposeInstance(name);
        em = null;
    }

    public boolean disposeIfPlayerBelow(byte size, int towarp) {
        if (disposed) {
            return true;
        }

        MapleMap map = null;
        if (towarp > 0) {
            map = this.getMapFactory().getMap(towarp);
        }

        try {
            if (chars != null && chars.size() <= size) {
                final List<MapleCharacter> chrs = new LinkedList<>(chars);
                for (MapleCharacter chr : chrs) {
                    if (chr == null) {
                        continue;
                    }
                    unregisterPlayer(chr);
                    if (towarp > 0) {
                        chr.changeMap(map, map.getPortal(0));
                    }
                }
                return true;
            }
        } catch (Exception ex) {

        }
        return false;
    }

    public MapleMapFactory getMapFactory() {
        return mapFactory;
    }

    public void schedule(final String methodName, long delay) {
        if (disposed) {
            return;
        }

        TimerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (disposed || EventInstanceManager.this == null || em == null) {
                    return;
                }
                try {
                    em.getIv().invokeFunction(methodName, EventInstanceManager.this);
                } catch (NullPointerException npe) {
                } catch (NoSuchMethodException | ScriptException ex) {
                    LogHelper.logEventInstanceError("Event Name" + em.getName() + ", Instance Name : " + name + ", Method Name (Schedule) : " + methodName + " :\n" + ex);
                }
            }
        }, delay);
    }

    public String getName() {
        return name;
    }

    public void saveWinner(MapleCharacter chr) {
        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO eventstats (event, instance, characterid, channel) VALUES (?, ?, ?, ?)")) {
                ps.setString(1, em.getName());
                ps.setString(2, getName());
                ps.setInt(3, chr.getId());
                ps.setInt(4, chr.getClient().getChannel());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
        }
    }

    public MapleMap getMapInstance(int mapId) {
        if (disposed) {
            return null;
        }

        MapleMap map = mapFactory.getMap(mapId);

        if (!mapFactory.isMapLoaded(mapId)) {
            if (em.getProperty("shuffleReactors") != null && em.getProperty("shuffleReactors").equals("true")) {
                map.shuffleReactors();
            }
        }
        return map;
    }

    public MapleMap getMapInstanceEmpty(int mapId) {
        if (disposed) {
            return null;
        }

        MapleMap map = mapFactory.getMapEmpty(mapId);

        if (!mapFactory.isMapLoaded(mapId)) {
            if (em.getProperty("shuffleReactors") != null && em.getProperty("shuffleReactors").equals("true")) {
                map.shuffleReactors();
            }
        }
        return map;
    }

    public void setProperty(String key, String value) {
        if (disposed) {
            return;
        }

        props.setProperty(key, value);
    }

    public Object setProperty(String key, String value, boolean prev) {
        if (disposed) {
            return null;
        }

        return props.setProperty(key, value);
    }

    public String getProperty(String key) {
        if (disposed) {
            return "";
        }

        return props.getProperty(key);
    }

    public Properties getProperties() {
        return props;
    }

    public void leftParty(MapleCharacter chr) {
        if (disposed) {
            return;
        }

        try {
            em.getIv().invokeFunction("leftParty", this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    public void disbandParty() {
        if (disposed) {
            return;
        }

        try {
            em.getIv().invokeFunction("disbandParty", this);
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    public void finishPQ() {
        if (disposed) {
            return;
        }

        try {
            em.getIv().invokeFunction("clearPQ", this);
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    public void removePlayer(MapleCharacter chr) {
        if (disposed) {
            return;
        }

        try {
            em.getIv().invokeFunction("playerExit", this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
        }
    }

    public boolean isLeader(MapleCharacter chr) {
        return (chr.getParty().getLeader().getId() == chr.getId());
    }
}
