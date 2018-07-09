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
INSERT monsterdrops (monsterid,itemid,chance) VALUES (9300001,4001007,5);
INSERT monsterdrops (monsterid,itemid,chance) VALUES (9300000,4001008,1);
INSERT monsterdrops (monsterid,itemid,chance) VALUES (9300002,4001008,1);
INSERT monsterdrops (monsterid,itemid,chance) VALUES (9300003,4001008,1);
*/

var exitMap;
var minPlayers = 1;

function init() { // Initial loading.
    em.setProperty("EPQOpen", "true"); // allows entrance.
}

function monsterValue(eim, mobId) { // Killed monster.
    return 1; // returns an amount to add onto kill count.
}

function setup() { // Invoked from "EventManager.startInstance()"
    var eim = em.newInstance("EPQ"); // adds a new instance and returns EventInstanceManager.
    var eventTime = 20 * (1000 * 60); // 20 mins.
    em.schedule("timeOut", eim, eventTime); // invokes "timeOut" in how ever many seconds.
    eim.startEventTimer(eventTime); // Sends a clock packet and tags a timer to the players.
    exitMap = em.getChannelServer().getMapFactory().getMap(310000000);
    return eim; // returns the new instance.
}

function playerEntry(eim, player) { // this gets looped for every player in the party.
    var map = eim.getMapInstance(310060000);
    player.changeMap(map, map.getPortal(0)); // We're now in KPQ :D
}

function playerDead(eim, player) {
}

function playerRevive(eim, player) { // player presses ok on the death pop up.
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


function respawn(eim) {	
	var map = eim.getMapInstance(310060100);
	var map2 = eim.getMapInstance(310060110);
	var map3 = eim.getMapInstance(310060120);
	var map4 = eim.getMapInstance(310060200);
	var map5 = eim.getMapInstance(310060210);
	var map6 = eim.getMapInstance(310060220);
	
	if (map.getSummonState()) {	//Map spawns are set to true by default
		map.instanceMapRespawn();
	}
	if(map2.getSummonState()) {
		map2.instanceMapRespawn();
	}
	if(map3.getSummonState()) {
		map3.instanceMapRespawn();
	}
		if(map4.getSummonState()) {
		map4.instanceMapRespawn();
	}
		if(map5.getSummonState()) {
		map5.instanceMapRespawn();
	}
		if(map6.getSummonState()) {
		map6.instanceMapRespawn();
	}
	eim.schedule("respawn", 10000);
}



function playerDisconnected(eim, player) {
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

function leftParty(eim, player) {
    playerExit(eim, player);

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }
}

function disbandParty(eim) {
    end(eim, "The party has been disbanded. The battle is over.");
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));
}

function removePlayer(eim, player) {
    eim.unregisterPlayer(player);
    player.getMap().removePlayer(player);
    player.setMap(exitMap);
}

function clearPQ(eim) {
    end(eim, "Congraduations! You cleared the PQ.");
}

function allMonstersDead(eim) {
}

function cancelSchedule() {
}

function dispose(eim) {
	em.cancelSchedule();
    em.schedule("OpenEPQ", 10000); // 10 seconds ?
}

function OpenEPQ() {
    em.setProperty("EPQOpen", "true");
}

function timeOut(eim) {
    if (eim != null) 
    {
        end(eim,"Time has ran out! Everyone will be warped out.");
    }
}

function end(eim, msg)
{
    for(var i = 0; i < eim.getPlayerCount(); i++)
    {
        var player = eim.getPlayers().get(i);

        player.dropMessage(6, msg);
        eim.unregisterPlayer(player);
        player.changeMap(exitMap, exitMap.getPortal(0));
    }

    eim.dispose();
}