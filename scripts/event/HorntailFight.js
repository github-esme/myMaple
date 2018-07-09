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
* @Author SharpAceX(Alan)
* Horntail fight
*/

importPackage(Packages.server.expeditions);
importPackage(java.lang);

var minPlayers = 1;
var fightTime = 12;

var trial1; //Cave of Life - The Cave of Trial I
var trial2; // Cave of Life - The Cave of Trial II
var fightMap; // Cave of Life - Horntail's Cave
var exitMap;
var baseMobPoints = 5000;

function init() 
{
    em.setProperty("shuffleReactors","false");
    trial1 = em.getChannelServer().getMapFactory().getMap(240060000); //Cave of Life - The Cave of Trial I
    trial2 = em.getChannelServer().getMapFactory().getMap(240060100); // Cave of Life - The Cave of Trial II
    fightMap = em.getChannelServer().getMapFactory().getMap(240060200); // Cave of Life - Horntail's Cave
    exitMap = em.getChannelServer().getMapFactory().getMap(240050500);
}

function setup() 
{
    var eim = em.newInstance("HorntailFight_" + em.getProperty("channel"));
    var timer = 1000 * 60 * 60 * fightTime;

    em.schedule("timeOut", timer);
    em.schedule("headOne", 3000);
    em.schedule("checkHTDeath", 3000);

    eim.setProperty("head1", "no");
    eim.setProperty("head2", "no");
    eim.setProperty("head2spawned", "no");
    eim.setProperty("horntailSpawned", "no");
    eim.setProperty("playerEntered", "no");

    eim.startEventTimer(timer);
    return eim;
}

function specificMonsterKilled(eim, mobId)
{
    if(mobId == 8810018)
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

function playerEntry(eim,player) 
{
    var map = eim.getMapInstance(trial1.getId());
    player.changeMap(map,map.getPortal(0));
}

function playerRevive(eim,player) 
{
    player.setHp(500);
    player.setStance(0);
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }

    return false;
}

function playerDead(eim,player) 
{

}

function playerDisconnected(eim,player) 
{
    removePlayer(eim, player);

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }
}

function monsterValue(eim, mobId) 
{
    return 1;
}

function leftParty(eim, player) 
{

}

function disbandParty(eim) 
{

}

function playerExit(eim,player) 
{
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }
}

function end(eim,msg) {
    for(var i = 0; i < eim.getPlayerCount(); i++)
    {
        var player = eim.getPlayers().get(i);

        player.dropMessage(6, msg);
        eim.unregisterPlayer(player);
        player.changeMap(exitMap, exitMap.getPortal(0));
    }

    eim.dispose();
}

// for offline folk
function removePlayer(eim,player) 
{
    eim.unregisterPlayer(player);
    player.getMap().removePlayer(player);
    player.setMap(exitMap);
}

function clearPQ(eim) {}

function finish(eim) 
{
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
    var currentMap = eim.getPlayers().get(0).getMapId();

    if(currentMap == 240060000)
    {
        eim.setProperty("head1", "yes");
    }
    else if (currentMap == 240060100)
    {
        eim.setProperty("head2", "yes");
    }
}

function cancelSchedule() {
}

function timeOut(eim) 
{
    var eim = em.getInstance("HorntailFight_" + em.getProperty("channel"));

    if (eim.getPlayerCount() > 0)
    {
        end(eim,"Time has ran out! Everyone will be warped out.");
    }
}

function debug(eim,msg) 
{
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) 
    {
        iter.next().getClient().getSession().write(Packages.tools.MaplePacketCreator.serverNotice(6,msg));
    }
}

function headOne() 
{
    var eim = em.getInstance("HorntailFight_" + em.getProperty("channel"));
    for(var i = 0; i < eim.getPlayerCount(); i++)
    {
        var player = eim.getPlayers().get(i);
        player.dropMessage(6, "The Enormous Creature is Approaching from the Deep Cave...");
    }

    eim.getMapFactory().getMap(240060000).spawnMonsterWithCoords(Packages.server.life.MapleLifeFactory.getMonster(8810000), 860, 230);
}

function headTwo() 
{
    var eim = em.getInstance("HorntailFight_" + em.getProperty("channel"));
    for(var i = 0; i < eim.getPlayerCount(); i++)
    {
        var player = eim.getPlayers().get(i);
        player.dropMessage(6, "Watch out for Horntail... Be prepared for a long fight.");
    }

    eim.getMapFactory().getMap(240060100).spawnMonsterWithCoords(Packages.server.life.MapleLifeFactory.getMonster(8810001), -300, 230);
}

function checkHTDeath()
{
    var eim = em.getInstance("HorntailFight_" + em.getProperty("channel"));
    if (eim != null && eim.getPlayerCount() > 0)
    {
        var HTMap = eim.getMapFactory().getMap(240060200);

        var dead = 0;
    
        var deadHT = new Array(8810010, 8810011, 8810012, 8810013, 8810014, 8810015, 8810016, 8810017);
    
        if (HTMap != null)
        {
            for (var i = 0; i < deadHT.length; i++)
            {
                dead += HTMap.countMonster(deadHT[i]);
            }
        }
    
        if (dead == 8)
        {
            for (var i = 0; i < deadHT.length; i++)
            {
                HTMap.killMonster(HTMap.getMonsterById(deadHT[i]), eim.getPlayers().get(0), true);
            }

            HTMap.killMonster(HTMap.getMonsterById(8810018), eim.getPlayers().get(0), true);
        }
        else
        {
            em.schedule("checkHTDeath", 3000);
        }
    }
}