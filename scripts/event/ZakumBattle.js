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
    */

/*
 * @Author Stereo
 * @Modified By XkelvinchiaX(Kelvin)
 * @Modified By Moogra
 * @Modified By SharpAceX(Alan)
 * Zakum Battle
 */

 var exitMap;
 var altarMap;
 var minPlayers = 1;
 var fightTime = 60;
 var altarTime = 15;
 var baseMobPoints = 1000;
 var gate;

 function init() {
    em.setProperty("shuffleReactors","false");
    exitMap = em.getChannelServer().getMapFactory().getMap(211042300);
	altarMap = em.getChannelServer().getMapFactory().getMap(280030000);// Last Mission: Zakum's Altar
	gate = exitMap.getReactorByName("gate");
	gate.setState(0);//Open gate
}

function setup() {
    var eim = em.newInstance("ZakumBattle_" + em.getProperty("channel"));
    var timer = 1000 * 60 * fightTime;

    eim.setProperty("summoned", "false");

    em.schedule("timeOut", eim, timer);
    em.schedule("altarTimeOut", eim, 1000 * 60 * altarTime);

    eim.startEventTimer(timer);

	gate.setState(1);//Close gate
	return eim;
}



function playerEntry(eim,player) {
	var altar = eim.getMapInstance(altarMap.getId());
    player.changeMap(altar, altar.getPortal(0));

    if(eim.getProperty("summoned") == "false")
    {
        player.dropMessage(5, "The Zakum Shrine will close if you do not summon Zakum in " + altarTime + " minutes.");
    }
}

function playerRevive(eim,player) {
    player.setHp(500);
    player.setStance(0);
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }
}

function playerDead(eim,player) {
}

function playerDisconnected(eim,player) {
    removePlayer(eim, player);

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }
}

function monsterValue(eim,mobId) { // potentially display time of death? does not seem to work
    if (mobId == 8800002) { // 3rd body
        var iter = eim.getPlayers().iterator();
        while (iter.hasNext()) {
            iter.next().dropMessage(6, "Congratulations on defeating Zakum!");
        }
    }
    return -1;
}

function leftParty(eim,player) { // do nothing in Zakum
}

function disbandParty(eim) { // do nothing in Zakum
}

function playerExit(eim,player) {
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }
}

function end(eim,msg) {
    Packages.server.expeditions.MapleExpedition.dispose(true);

    for(var i = 0; i < eim.getPlayerCount(); i++)
    {
        var player = eim.getPlayers().get(i);

        player.dropMessage(6, msg);
        eim.unregisterPlayer(player);
        player.getClient().getChannelServer().getExpeditions().remove(exped);
        player.changeMap(exitMap, exitMap.getPortal(0));
    }

    gate.setState(0);//Open gate
    eim.dispose();
}

function removePlayer(eim,player) {
    eim.unregisterPlayer(player);
    player.getMap().removePlayer(player);
}

function clearPQ(eim) { //When the hell does this get executed?
    end(eim,"As the sound of battle fades away, you feel strangely unsatisfied.");

}

function finish(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) 
    {
        var player = iter.next();
        eim.unregisterPlayer(player);
        player.changeMap(exitMap, exitMap.getPortal(0));
    }
    eim.dispose();
}

function allMonstersDead(eim) {
}

function cancelSchedule() {
}

function specificMonsterKilled(eim, mobId)
{
    if(mobId == 8800002)
    {
        var playerCount = eim.getPlayerCount();

        var mobPoints = Math.round(baseMobPoints / playerCount);

        for(var i = 0; i < playerCount; i++)
        {
            var player = eim.getPlayers().get(i);

            player.gainMobPoints(mobPoints);
            player.dropMessage(6, "You and your expedition members has received " + mobPoints + " mob points.");
        }
    }
}

function altarTimeOut(eim) {
    if (eim != null && eim.getProperty("summoned").equals("false")) {
        if (eim.getPlayerCount() > 0) {
            var pIter = eim.getPlayers().iterator();
            while (pIter.hasNext()){
                var player = pIter.next();
                player.dropMessage(6, "The Shrine has closed since you did not summon Zakum within " + altarTime + " minutes.");
                playerExit(eim, player);
            }
        }
        eim.dispose();
    }
}

function timeOut(eim) 
{
    var eim = em.getInstance("ZakumBattle_" + em.getProperty("channel"));

    if (eim.getPlayerCount() > 0)
    {
        end(eim,"Time has ran out! Everyone will be warped out.");
    }
}

function debug(eim,msg) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) 
    {
        iter.next().getClient().getSession().write(Packages.tools.MaplePacketCreator.serverNotice(6,msg));
    }
}