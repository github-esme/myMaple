/* 
 * This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
	THIS  FILE WAS MADE BY JVLAPLE. REMOVING THIS NOTICE MEANS YOU CAN'T USE THIS SCRIPT OR ANY OTHER SCRIPT PROVIDED BY JVLAPLE.
 */

/*
 * @Author Jvlaple
 * 
 * Orbis Party Quest
 */

importPackage(java.lang);
importPackage(Packages.world);
importPackage(Packages.client);
importPackage(Packages.server.maps);
importPackage(Packages.server.life);
importPackage(Packages.scripting.npc);

var exitMap;
var minPlayers = 1;
var channel;

function init() {
    
}


function monsterValue(eim, mobId) {
    return 1;
}

function setup() {
    exitMap = em.getChannelServer().getMapFactory().getMap(920011200); 
    channel = em.getChannelServer().getId();

    var instanceName = "OrbisPQ_" + em.getProperty("channel");
    var eim = em.newInstance(instanceName);
    var mf = eim.getMapFactory();

    var map = mf.getMap(920010000);

    map.spawnNpc(2013001, new java.awt.Point(376, 99));

    var centerMap = eim.getMapInstance(920010100);
    centerMap.getPortal(13).setScriptName("orbisPQSealedRoom");
    centerMap.getPortal(4).setScriptName("orbisPQWalkway");
    centerMap.getPortal(12).setScriptName("orbisPQStorage");
    centerMap.getPortal(5).setScriptName("orbisPQLobby");
    centerMap.getPortal(14).setScriptName("orbisPQOnTheWayUp");
    centerMap.getPortal(15).setScriptName("orbisPQLounge");
    centerMap.getPortal(16).setScriptName("orbisPQRoomOfDarkness");
    var walkwayMap = eim.getMapInstance(920010200);
    var storageMap = eim.getMapInstance(920010300);
    var lobbyMap = eim.getMapInstance(920010400);
    var sealedRoomMap = eim.getMapInstance(920010500);
    var loungeMap = eim.getMapInstance(920010600);
    var onTheWayUpMap = eim.getMapInstance(920010700);
    var bossMap = eim.getMapInstance(920010800);
    var jailMap = eim.getMapInstance(920010900);
    var roomOfDarknessMap = eim.getMapInstance(920011000);
    var bonusMap = eim.getMapInstance(920011100);
    var endMap = eim.getMapInstance(920011300);
    walkwayMap.getPortal(13).setScriptName("orbisPQWalkwayExit");
    storageMap.getPortal(1).setScriptName("orbisPQStorageExit");
    lobbyMap.getPortal(8).setScriptName("orbisPQLobbyExit");
    sealedRoomMap.getPortal(3).setScriptName("orbisPQSRExit");
    loungeMap.getPortal(17).setScriptName("orbisPQLoungeExit");
    onTheWayUpMap.getPortal(23).setScriptName("orbisPQOnTheWayUpExit");
    bossMap.getPortal(1).setScriptName("orbisPQGardenExit");
    roomOfDarknessMap.getPortal(1).setScriptName("orbisPQRoomOfDarknessExit");

    eim.setProperty("killedCellions", "0");
    eim.setProperty("papaSpawned", "no");
    em.schedule("timeOut", 60 * 60000);
    em.schedule("broadcastClock", 1500);
    eim.setProperty("entryTimestamp",System.currentTimeMillis() + (60 * 60000));
	
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(920010000);
    player.changeMap(map, map.getPortal(0));
    player.getClient().getSession().write(Packages.tools.MaplePacketCreator.getClock((Long.parseLong(eim.getProperty("entryTimestamp")) - System.currentTimeMillis()) / 1000));
    
    var texttt = "Hi, my name is Eak, the Chamberlain of the Goddess. Don't be alarmed; you won't be able to see me right now. Back when the Goddess turned into a block of stone, I simultaneously lost my own power. If you gather up the power of the Magic Cloud of Orbis, however, then I'll be able to recover my body and re-transform back to my original self. Please collect #b20#k Magic Clouds and bring them back to me. Right now, you'll only see me as a tiny, flickering light."
    player.getClient().getSession().write(Packages.tools.MaplePacketCreator.getNPCTalk(2013001, 0, texttt, "00 00", 0));
}

function playerDead(eim, player) {
}

function playerRevive(eim, player) {
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.isLeader(player)) 
    { 
        end(eim, "The party leader has died. The battle is over.");
    }
    else 
    {
        if (eim.getPlayers().size() < minPlayers)
        {
            end(eim, "There are no longer enough players to continue. The battle is over.");
        }
    }
}

function playerDisconnected(eim, player)
{
    removePlayer(eim, player);

    if (eim.isLeader(player) || eim.getPlayers().size() < minPlayers) 
    { 
        end(eim, "The party leader has disconnected. The battle is over.");
    }
    else 
    {
        if (eim.getPlayers().size() < minPlayers)
        {
            end(eim, "There are no longer enough players to continue. The battle is over.");
        }
    }
}

function leftParty(eim, player)
{
    playerExit(eim, player);

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }
}

function disbandParty(eim)
{
    end(eim, "The party has been disbanded. The battle is over.");
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    player.cancelAllBuffs();
    player.changeMap(exitMap, exitMap.getPortal(0));
}

function removePlayer(eim, player) {
    eim.unregisterPlayer(player);
    player.getMap().removePlayer(player);
    player.setMap(exitMap);
}

function clearPQ(eim) {
    var iter = eim.getPlayers().iterator();
    var bonusMap = eim.getMapInstance(920011100);
    while (iter.hasNext()) {
        var player = iter.next();
        player.changeMap(bonusMap, bonusMap.getPortal(0));
        eim.setProperty("entryTimestamp",System.currentTimeMillis() + (1 * 60000));
        player.getClient().getSession().write(Packages.tools.MaplePacketCreator.getClock(60));
    }
    eim.schedule("finish", 60000)
}

function finish(eim) {
    var dMap = eim.getMapInstance(920011300);
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        eim.unregisterPlayer(player);
        player.changeMap(dMap, dMap.getPortal(0));
    }
    eim.dispose();
}

function allMonstersDead(eim) {

}

function cancelSchedule() {
}

function timeOut()
{    
    var eim = em.getInstance("OrbisPQ_" + channel);

    if (eim.getPlayerCount() > 0)
    {
        end(eim,"Time has ran out! Everyone will be warped out.");
    }
}

function playerClocks(eim, player) {
    if (player.getMap().hasTimer() == false){
        player.getClient().getSession().write(Packages.tools.MaplePacketCreator.getClock((Long.parseLong(eim.getProperty("entryTimestamp")) - System.currentTimeMillis()) / 1000));
    }
}

function playerTimer(eim, player) {
    if (player.getMap().hasTimer() == false) {
        player.getMap().setTimer(true);
    }
}

function broadcastClock(eim, player) {
    //var party = eim.getPlayers();
    var iter = em.getInstances().iterator();
    while (iter.hasNext()) {
        var eim = iter.next();
        if (eim.getPlayerCount() > 0) {
            var pIter = eim.getPlayers().iterator();
            while (pIter.hasNext()) {
                playerClocks(eim, pIter.next());
            }
        }
    }

    var iterr = em.getInstances().iterator();
    while (iterr.hasNext()) {
        var eim = iterr.next();
        if (eim.getPlayerCount() > 0) {
            var pIterr = eim.getPlayers().iterator();
            while (pIterr.hasNext()) {
                playerTimer(eim, pIterr.next());
            }
        }
    }
    em.schedule("broadcastClock", 1600);
}

function end(eim, msg)
{
    for(var i = 0; i < eim.getPlayerCount(); i++)
    {
        var player = eim.getPlayers().get(i);
        player.cancelAllBuffs();
        player.dropMessage(6, msg);
        eim.unregisterPlayer(player);
        player.changeMap(exitMap, exitMap.getPortal(0));
    }

    eim.dispose();
}