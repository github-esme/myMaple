/*
* @Author Torban
*
* Yeti Party Quest
*/

importPackage(Packages.server.expeditions);

var exitMap;
var channel;
var minPlayers = 1;

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
    channel = em.getChannelServer().getId();
    exitMap = em.getChannelServer().getMapFactory().getMap(106021400); //East Castle Tower

    var eim = em.newInstance("YetiPQ_" + em.getProperty("channel"));
    eim.setProperty("yeti1", "no");
    eim.setProperty("yeti2", "no");
    eim.setProperty("yeti3", "no");

    var timer = 600000; // 10 minutes
    em.schedule("timeOut", timer);
    em.schedule("yetiOne", 100);

    eim.startEventTimer(timer);

    return eim;
}

function playerEntry(eim, player)
{
    var map = eim.getMapInstance(106021500);
    player.changeMap(map, map.getPortal(0));
}

function playerRevive(eim, player)
{
    player.setHp(500);
    player.setStance(0);
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
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
        end(eim, "There are no longer enough players to continue. The battle is over.");
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
    var currentMap = eim.getPlayers().get(0).getMapId();

    if(currentMap == 106021500)
    {
        eim.setProperty("yeti1", "yes");
    }
    else if (currentMap == 106021501)
    {
        eim.setProperty("yeti2", "yes");
    }
    else if (currentMap == 106021502)
    {
        var toDrop = Packages.client.inventory.Item(4032388, java.lang.Short.parseShort(0), java.lang.Short.parseShort(1));

        for(var i = 0; i < eim.getPlayerCount(); i++)
        {
            var target = eim.getPlayers().get(i);
            target.getMap().spawnItemDrop(target, target, toDrop, target.getPosition(), true, true);
        }

        eim.setProperty("yeti3", "yes");
    }
}

function cancelSchedule()
{

}

function timeOut()
{    
    var eim = em.getInstance("YetiPQ_" + channel);

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

        player.dropMessage(5, msg);
        eim.unregisterPlayer(player);
        player.changeMap(exitMap, exitMap.getPortal(0));
    }

    eim.dispose();
}

function yetiOne(eim, player)
{
    em.getInstance("YetiPQ_" + em.getProperty("channel")).getMapFactory().getMap(106021500).spawnMonsterOnGroudBelow(3300005, -310, -68);
}

function yetiTwo(eim, player)
{
    em.getInstance("YetiPQ_" + em.getProperty("channel")).getMapFactory().getMap(106021501).spawnMonsterOnGroudBelow(3300006, -310, -68);
}

function yetiThree(eim, player)
{
    em.getInstance("YetiPQ_" + em.getProperty("channel")).getMapFactory().getMap(106021502).spawnMonsterOnGroudBelow(3300007, -310, -68);
}