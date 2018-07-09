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
 * @Author Raz
 * 
 * Ludi Maze PQ
 */

var exitMap;
var instanceId;
var finishMap;
var minPlayers = 2;

function init() {
    instanceId = 1;
    em.setProperty("shuffleReactors", "true");
}



function monsterValue(eim, mobId) {
    return 1;
}

function setup() {
    exitMap = em.getChannelServer().getMapFactory().getMap(809050017);
    finishMap = em.getChannelServer().getMapFactory().getMap(809050016);
    var instanceName = "LudiMazePQ" + instanceId;
    var eim = em.newInstance(instanceName);
    var mf = eim.getMapFactory();
    instanceId++;
    var eventTime = 15 * (1000 * 60);
    em.schedule("timeOut", eim, eventTime);
    eim.startEventTimer(eventTime);
    return eim;
}

function playerEntry(eim, player) {
	var random = Math.floor((Math.random() * 16));
    var map = eim.getMapInstance(809050000 + random);
    player.changeMap(map, map.getPortal(0));
	
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


function playerFinish(eim, player) {
    eim.unregisterPlayer(player);
    player.changeMap(finishMap, finishMap.getPortal(0));
}

//for offline players
function removePlayer(eim, player) {
    eim.unregisterPlayer(player);
    player.getMap().removePlayer(player);
    player.setMap(exitMap);
}

function clearPQ(eim) {
    end(eim, "Congraduations! You cleared the PQ.");
}

function allMonstersDead(eim) {
//do nothing; LMPQ has nothing to do with monster killing
}

function cancelSchedule() {
}

function dispose(eim) {

}

function timeOut(eim) {
    if (eim.getPlayerCount() > 0)
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

function playerRevive(eim, player) {
     
}