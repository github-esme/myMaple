/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.partyquest;

import java.util.concurrent.ScheduledFuture;
import client.MapleCharacter;
import net.server.world.MapleParty;
import server.maps.MapleMap;
import net.server.channel.Channel;
import net.server.world.MaplePartyCharacter;
import server.TimerManager;
import tools.MaplePacketCreator;

/**
 *
 * @author David
 */
public class MonsterCarnival {

    public static int D = 3;
    public static int C = 2;
    public static int B = 1;
    public static int A = 0;

    private MapleParty p1, p2;
    private MapleMap map;
    private ScheduledFuture<?> timer;
    private ScheduledFuture<?> effectTimer;
    private long startTime;
    private MapleCharacter leader1, leader2, Grupo1, Grupo2;
    private int redCP, blueCP, redTotalCP, blueTotalCP;

    public MonsterCarnival(MapleParty p1, MapleParty p2, int mapid) {
        this.p1 = p1;
        this.p2 = p2;
        int chnl = p1.getLeader().getChannel();
        int chnl1 = p2.getLeader().getChannel();
        if (chnl != chnl1) {
            throw new RuntimeException("CPQ leaders are on different channels..");
        }
        Channel cs = p1.getLeader().getPlayer().getClient().getChannelServer();
        p1.setEnemy(p2);
        p2.setEnemy(p1);

        cs.getMapFactory().disposeMap(mapid);
        map = cs.getMapFactory().getMap(mapid);

        int redPortal = 0;
        int bluePortal = 0;
        if (map.isPurpleCPQMap()) {
            redPortal = 2;
            bluePortal = 1;
        }
        for (MaplePartyCharacter mpc : p1.getMembers()) {
            MapleCharacter mc;
            mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
            if (mc != null) {
                mc.setMonsterCarnival(this);
                mc.changeMap(map, map.getPortal(redPortal));
                mc.setTeam(0);
                if (p1.getLeader().getId() == mc.getId()) {
                    leader1 = mc;
                }
                Grupo1 = mc;
            }
        }
        for (MaplePartyCharacter mpc : p2.getMembers()) {
            MapleCharacter mc;
            mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
            if (mc != null) {
                mc.setMonsterCarnival(this);
                mc.changeMap(map, map.getPortal(bluePortal));
                mc.setTeam(1);
                if (p2.getLeader().getId() == mc.getId()) {
                    leader2 = mc;
                }
                Grupo2 = mc;
            }
        }
        Grupo1.getClient().getSession().write(MaplePacketCreator.startCPQ(Grupo1.getTeam(), Grupo1, Grupo2));
        Grupo2.getClient().getSession().write(MaplePacketCreator.startCPQ(Grupo2.getTeam(), Grupo2, Grupo1));

        startTime = System.currentTimeMillis() + 60 * 10000;
        timer = TimerManager.getInstance().schedule(new Runnable() {
            public void run() {
                timeUp();
            }
        }, 10 * 60 * 1000);
        effectTimer = TimerManager.getInstance().schedule(new Runnable() {
            public void run() {
                complete();
            }
        }, 10 * 60 * 1000 - 10 * 1000);
        TimerManager.getInstance().schedule(new Runnable() {
            public void run() {
                map.addClock(60 * 10);
            }
        }, 2000);
    }

    public void playerDisconnected(int charid) {
        if (leader1.getId() == charid
                || leader2.getId() == charid) {
            earlyFinish();
            int team = -1;

            if (leader1.getParty() != null) {
                for (MaplePartyCharacter mpc : leader1.getParty().getMembers()) {
                    if (mpc.getId() == charid) {
                        team = 0;
                    }
                }
            }

            if (leader2.getParty() != null) {
                for (MaplePartyCharacter mpc : leader2.getParty().getMembers()) {
                    if (mpc.getId() == charid) {
                        team = 1;
                    }
                }
            }

            if (team == -1) {
                team = 1;
            }
            String teamS = "undefined";

            switch (team) {
                case 0:
                    teamS = "Red";
                    break;
                case 1:
                    teamS = "Blue";
                    break;
            }
            map.broadcastMessage(MaplePacketCreator.serverNotice(5, "Maple " + teamS + " has quitted the Monster Carnival."));
        } else {
            map.broadcastMessage(MaplePacketCreator.serverNotice(5, Channel.getInstance(1).getMarket().getCharacterName(charid) + " has quitted the Monster Carnival."));
        }
    }

    public void earlyFinish() {
        dispose(true);
    }

    public void leftParty(int charid) {
        playerDisconnected(charid);
    }

    protected int getRankByCP(int cp) {
        if (cp < 50) {
            return D;
        } else if (cp > 50 && cp < 100) {
            return C;
        } else if (cp > 100 && cp < 300) {
            return B;
        } else if (cp > 300) {
            return A;
        }
        return D;
    }

    protected void dispose() {
        dispose(false);
    }

    protected void dispose(boolean warpout) {
        Channel cs = p1.getLeader().getPlayer().getClient().getChannelServer();
        MapleMap out = cs.getMapFactory().getMap(980000000);

        if (leader1 != null && leader1.getParty() != null) {
            for (MaplePartyCharacter mpc : leader1.getParty().getMembers()) {
                if (mpc != null) {
                    MapleCharacter mc;
                    mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
                    if (mc != null) {
                        mc.setCpqRanking(getRankByCP(this.redTotalCP));
                        mc.resetCP();
                        if (warpout) {
                            mc.changeMap(out, out.getPortal(0));
                        }
                    }
                }
            }
        }

        if (leader2 != null && leader2.getParty() != null) {
            for (MaplePartyCharacter mpc : leader2.getParty().getMembers()) {
                if (mpc != null) {
                    MapleCharacter mc;
                    mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
                    if (mc != null) {
                        mc.setCpqRanking(getRankByCP(this.blueTotalCP));
                        mc.resetCP();
                        if (warpout) {
                            mc.changeMap(out, out.getPortal(0));
                        }
                    }
                }
            }
        }

        timer.cancel(false);
        effectTimer.cancel(false);
        redTotalCP = 0;
        blueTotalCP = 0;

        if (leader1 != null && leader1.getParty() != null) {
            leader1.getParty().setEnemy(null);
        }

        if (leader2 != null && leader2.getParty() != null) {
            leader2.getParty().setEnemy(null);
        }
    }

    public void exit() {
        dispose();
    }

    public ScheduledFuture<?> getTimer() {
        return this.timer;
    }

    public void finish(int winningTeam) {
        int chnl = leader1.getClient().getChannel();
        int chnl1 = leader2.getClient().getChannel();
        if (chnl != chnl1) {
            throw new RuntimeException("CPQ leaders are on different channels..");
        }
        Channel cs = p1.getLeader().getPlayer().getClient().getChannelServer();
        if (winningTeam == 0) {
            for (MaplePartyCharacter mpc : leader1.getParty().getMembers()) {
                MapleCharacter mc;
                mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
                if (mc != null) {
                    mc.setCpqRanking(getRankByCP(this.redTotalCP));
                    mc.changeMap(cs.getMapFactory().getMap(map.getId() + 2), cs.getMapFactory().getMap(map.getId() + 2).getPortal(0));
                    mc.setTeam(-1);
                }
            }
            for (MaplePartyCharacter mpc : leader2.getParty().getMembers()) {
                MapleCharacter mc;
                mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
                if (mc != null) {
                    mc.setCpqRanking(getRankByCP(this.blueTotalCP));
                    mc.changeMap(cs.getMapFactory().getMap(map.getId() + 3), cs.getMapFactory().getMap(map.getId() + 3).getPortal(0));
                    mc.setTeam(-1);
                }
            }
        } else if (winningTeam == 1) {
            for (MaplePartyCharacter mpc : leader2.getParty().getMembers()) {
                MapleCharacter mc;
                mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
                if (mc != null) {
                    mc.changeMap(cs.getMapFactory().getMap(map.getId() + 2), cs.getMapFactory().getMap(map.getId() + 2).getPortal(0));
                    mc.setTeam(-1);
                }
            }
            for (MaplePartyCharacter mpc : leader1.getParty().getMembers()) {
                MapleCharacter mc;
                mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
                if (mc != null) {
                    mc.changeMap(cs.getMapFactory().getMap(map.getId() + 3), cs.getMapFactory().getMap(map.getId() + 3).getPortal(0));
                    mc.setTeam(-1);
                }
            }
        }
        dispose();
    }

    public void timeUp() {
        int cp1 = this.redTotalCP;
        int cp2 = this.blueTotalCP;
        if (cp1 == cp2) {
            extendTime();
            return;
        }
        if (cp1 > cp2) {
            finish(0);
        } else {
            finish(1);
        }
    }

    public long getTimeLeft() {
        return (startTime - System.currentTimeMillis());
    }

    public int getTimeLeftSeconds() {
        return (int) (getTimeLeft() / 1000);
    }

    public void extendTime() {
        map.broadcastMessage(MaplePacketCreator.serverNotice(5, "The time has been extended."));
        startTime = System.currentTimeMillis() + 3 * 1000;
        map.addClock(3 * 60);
        timer = TimerManager.getInstance().schedule(new Runnable() {
            public void run() {
                timeUp();
            }
        }, 3 * 60 * 1000);
        effectTimer = TimerManager.getInstance().schedule(new Runnable() {
            public void run() {
                complete();
            }
        }, 3 * 60 * 1000 - 10);
    }

    public void complete() {
        int cp1 = this.redTotalCP;
        int cp2 = this.blueTotalCP;
        if (cp1 == cp2) {
            return;
        }
        boolean redWin = cp1 > cp2;
        int chnl = leader1.getClient().getChannel();
        int chnl1 = leader2.getClient().getChannel();
        if (chnl != chnl1) {
            throw new RuntimeException("CPQ leaders are on different channels..");
        }
        Channel cs = p1.getLeader().getPlayer().getClient().getChannelServer();
        map.killAllMonsters(false);
        for (MaplePartyCharacter mpc : leader1.getParty().getMembers()) {
            MapleCharacter mc;
            mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
            if (mc != null) {
                if (redWin) {
                    mc.getClient().getSession().write(MaplePacketCreator.showEffect("quest/carnival/win"));
                    mc.getClient().getSession().write(MaplePacketCreator.playSound("MobCarnival/Win"));
                } else {
                    mc.getClient().getSession().write(MaplePacketCreator.showEffect("quest/carnival/lose"));
                    mc.getClient().getSession().write(MaplePacketCreator.playSound("MobCarnival/Lose"));
                }
            }
        }
        for (MaplePartyCharacter mpc : leader2.getParty().getMembers()) {
            MapleCharacter mc;
            mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
            if (mc != null) {
                if (!redWin) {
                    mc.getClient().getSession().write(MaplePacketCreator.showEffect("quest/carnival/win"));
                    mc.getClient().getSession().write(MaplePacketCreator.playSound("MobCarnival/Win"));
                } else {
                    mc.getClient().getSession().write(MaplePacketCreator.showEffect("quest/carnival/lose"));
                    mc.getClient().getSession().write(MaplePacketCreator.playSound("MobCarnival/Lose"));
                }
            }
        }
    }

    public MapleParty getRed() {
        return p1;
    }

    public void setRed(MapleParty p1) {
        this.p1 = p1;
    }

    public MapleParty getBlue() {
        return p2;
    }

    public void setBlue(MapleParty p2) {
        this.p2 = p2;
    }

    public MapleCharacter getLeader1() {
        return leader1;
    }

    public void setLeader1(MapleCharacter leader1) {
        this.leader1 = leader1;
    }

    public MapleCharacter getLeader2() {
        return leader2;
    }

    public void setLeader2(MapleCharacter leader2) {
        this.leader2 = leader2;
    }

    public MapleCharacter getEnemyLeader(int team) {
        switch (team) {
            case 0:
                return leader2;
            case 1:
                return leader1;
        }
        return null;
    }

    public int getBlueCP() {
        return blueCP;
    }

    public void setBlueCP(int blueCP) {
        this.blueCP = blueCP;
    }

    public void nascer(MapleCharacter chr) {

        Channel cs = p1.getLeader().getPlayer().getClient().getChannelServer();
        for (MaplePartyCharacter mpc : p1.getMembers()) {
            MapleCharacter mc;
            mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
            if (mc != null) {
                Grupo1 = mc;
                mc.setTeam(0);
            }
        }
        for (MaplePartyCharacter mpc : p2.getMembers()) {
            MapleCharacter mc;
            mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
            if (mc != null) {
                Grupo2 = mc;
                mc.setTeam(1);
            }
        }

        if (chr.getTeam() == 0) {
            Grupo1.getClient().getSession().write(MaplePacketCreator.startCPQ(Grupo1.getTeam(), Grupo1, Grupo2));
        } else {
            Grupo2.getClient().getSession().write(MaplePacketCreator.startCPQ(Grupo2.getTeam(), Grupo2, Grupo1));

        }
    }

    public int getBlueTotalCP() {
        return blueTotalCP;
    }

    public void setBlueTotalCP(int blueTotalCP) {
        this.blueTotalCP = blueTotalCP;
    }

    public int getRedCP() {
        return redCP;
    }

    public void setRedCP(int redCP) {
        this.redCP = redCP;
    }

    public int getRedTotalCP() {
        return redTotalCP;
    }

    public void setRedTotalCP(int redTotalCP) {
        this.redTotalCP = redTotalCP;
    }

    public int getTotalCP(int team) {
        switch (team) {
            case 0:
                return redTotalCP;
            case 1:
                return blueTotalCP;
            default:
                throw new RuntimeException("Unknown team");
        }
    }

    public void setTotalCP(int totalCP, int team) {
        if (team == 0) {
            this.redTotalCP = totalCP;
        } else if (team == 1) {
            this.blueTotalCP = totalCP;
        }
    }

    public int getCP(int team) {
        switch (team) {
            case 0:
                return redCP;
            case 1:
                return blueCP;
            default:
                throw new RuntimeException("Unknown team");
        }
    }

    public void setCP(int CP, int team) {
        if (team == 0) {
            this.redCP = CP;
        } else if (team == 1) {
            this.blueCP = CP;
        }
    }
}
