/*
* @Author Torban
*
* Guild Boss Fight
*/

var exitMap;
var instanceID;
var minPlayers = 1;
var pointsEarned = 0;
var guild;
var mapIndex;

var mapIDs = new Array(220000006, 100000202, 101000100, 105040310, 280020000, 610020000, 682000200);
var basePoints = new Array(5, 5, 10, 10, 15, 10, 10);
var bonusPoints = new Array(2, 2, 3, 3, 5, 3, 3);

function init()
{
    em.setProperty("shuffleReactors","false");
}

function monsterValue(eim, mobId)
{
    return 1;
}

function setup()
{
    instanceID = em.getProperty("guildID");
    mapIndex = em.getProperty("mapIndex");

    exitMap = em.getChannelServer().getMapFactory().getMap(910000000); //FM

    currentMob = 0;
    finished = false;
    pointsEarned = 0;

    var eim = em.newInstance("GuildJumpQuest_" + instanceID);

    eim.setProperty("bonus", "0");
    eim.setProperty("basePoints", basePoints[mapIndex]);
    eim.setProperty("bonusPoints", bonusPoints[mapIndex]);

    return eim;
}

function playerEntry(eim, player)
{
    var map = eim.getMapInstanceEmpty(mapIDs[mapIndex]);

    if (mapIndex == 0)
    {
        map.spawnNpc(1022101, new java.awt.Point(505, -1139));
    }
    else if (mapIndex == 1)
    {
        map.spawnNpc(1022101, new java.awt.Point(-1598, -1586));
    }
    else if (mapIndex == 2)
    {
        map.spawnNpc(1022101, new java.awt.Point(33, -2962));
    }
    else if (mapIndex == 3)
    {
        map.spawnNpc(1022101, new java.awt.Point(-25, -2685));
    }
    else if (mapIndex == 4)
    {
        map.spawnNpc(1022101, new java.awt.Point(5602, -215));
    }
    else if (mapIndex == 5)
    {
        map.spawnNpc(1022101, new java.awt.Point(1790, -1472));
    }
    else if (mapIndex == 6)
    {
        map.spawnNpc(1022101, new java.awt.Point(132, -3191));
    }
    

    player.changeMap(map, map.getPortal(0));

    player.message("Your guild JQ will be cancelled if you get out of the JQ map such as by going through portals or logging out. Click on the Rooney NPC at the end to complete the JQ.");

    player.runGuildJQTimer();

    guild = player.getGuild();
}

function playerRevive(eim, player)
{
    player.setHp(500);
    player.setStance(0);
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The jump quest is over.");
    }
}

function playerDead(eim, player)
{

}

function playerDisconnected(eim, player)
{
    removePlayer(eim, player);

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The jump quest is over.");
    }
}

function leftParty(eim, player)
{
    playerExit(eim, player);

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The jump quest is over.");
    }
}

function disbandParty(eim)
{
    end(eim, "The party has been disbanded. The jump quest is over.");
}

function playerExit(eim, player)
{
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));
}

function removePlayer(eim, player)
{
    eim.unregisterPlayer(player);
    player.getMap().removePlayer(player);
    player.setMap(exitMap);
}

function clearPQ(eim)
{
    end(eim, "Congratulations! You cleared the PQ.");
}

function allMonstersDead(eim)
{
    
}

function specificMonsterKilled(eim, mobId)
{
    
}

function cancelSchedule()
{

}

function changedMap(eim, player, mapid) {
    if (mapid != mapIDs[mapIndex])
    {
        player.stopGuildJQTime();
        
        playerExit(eim, player);

        if (eim.getPlayers().size() < minPlayers)
        {
            end(eim, "There are no longer enough players to continue.");
        }
    }
}

function timeOut()
{    
    var eim = em.getInstance("GuildJumpQuest_" + instanceID) 
    if (eim.getPlayerCount() > 0)
    {
        end(eim, "Time has ran out! Everyone will be warped out.");
    }
}

function end(eim, msg)
{
    for(var i = 0; i < eim.getPlayerCount(); i++)
    {
        var player = eim.getPlayers().get(i);

        player.dropMessage(5, msg);
        eim.unregisterPlayer(player);
        player.changeMap(exitMap, exitMap.getPortal(0));
    }

    eim.dispose();
}