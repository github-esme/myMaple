/*
* @Author Torban
*
* Balrog PQ
*/

var exitMap;
var channel;
var minPlayers = 1;
var EasyBalrogParts = new Array(8830007, 8830011, 8830009);
var HardBalrogParts = new Array(8830000, 8830004, 8830002);
var easy = true;
var killed = 0;

//8830011
// 8830004

function init()
{
    em.setProperty("shuffleReactors","false");
}

function monsterValue(eim, mobId)
{
    return 1;
}

function specificMonsterKilled(eim, mobId)
{
    if(mobId == 8830007 || mobId == 8830009 || mobId == 8830000 || mobId == 8830002)
    {
        killed++;
    }

    if(easy)
    {
        if(killed >= 4)
        {
            for(var i = 0; i < eim.getPlayerCount(); i++)
            {
                var player = eim.getPlayers().get(i);

                player.gainExp(260000, true, true);
            }
        }
    }
    else
    {
        if(killed >= 4)
        {
            for(var i = 0; i < eim.getPlayerCount(); i++)
            {
                var player = eim.getPlayers().get(i);

                player.gainExp(520000, true, true);
            }
        }
    }
}

function setup()
{
    channel = em.getChannelServer().getId();
    exitMap = em.getChannelServer().getMapFactory().getMap(105100100);

    var eim = em.newInstance("BalrogPQ_" + em.getProperty("channel"));
    eim.setProperty("allKilled", "false");

    if(em.getProperty("mode") == "hard")
    {
        easy = false;
    }

    var timer = 3600000; // 60 minutes
    em.schedule("spawnBalrogs", 1000);
    em.schedule("timeOut", timer);

    eim.startEventTimer(timer);

    return eim;
}

function playerEntry(eim, player)
{
    var map = eim.getMapInstance(105100300);
    player.changeMap(map);
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
    var party = eim.getPlayers();

    for (var i = 0; i < party.size(); i++)
    {
        playerExit(eim, party.get(i));
    }

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }
}

function playerExit(eim, player)
{
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }
}

function removePlayer(eim, player)
{
    eim.unregisterPlayer(player);
    player.getMap().removePlayer(player);
    player.setMap(exitMap);

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over.");
    }
}

function clearPQ(eim)
{
    var party = eim.getPlayers();

    for (var i = 0; i < party.size(); i++)
    {
        playerExit(eim, party.get(i));
    }

    eim.getEm().disposeInstance("BalrogPQ_" + channel);
}

function allMonstersDead(eim)
{
    eim.setProperty("allKilled", "true");
}

function cancelSchedule()
{

}

function timeOut()
{    
    var eim = em.getInstance("BalrogPQ_" + channel);

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

    eim.getEm().disposeInstance("BalrogPQ_" + channel);
}

function spawnBalrogs()
{
    if(easy)
    {
        for (var i = 0; i < EasyBalrogParts.length; i++) {
            em.getInstance("BalrogPQ_" + em.getProperty("channel")).getMapFactory().getMap(105100300).spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(EasyBalrogParts[i]), new java.awt.Point(412, 258));
        }
    }
    else
    {
        for (var i = 0; i < HardBalrogParts.length; i++) {
            em.getInstance("BalrogPQ_" + em.getProperty("channel")).getMapFactory().getMap(105100300).spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(HardBalrogParts[i]), new java.awt.Point(412, 258));
        }
    }
}