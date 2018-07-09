/*
* @Author Torban
*
* Guild Boss Fight
*/

var exitMap;
var minPlayers = 2;

var bosses = new Array(7220000, 9500352, 9300012, 8220007, 9001013, 9400633, 9300339, 9500173, 9400642, 4300013, 9300119, 8220011, 9300105, 9300039, 9300140, 9300089, 8500002, 9300028, 9420513, 9001014, 9400549, 9700037, 9400596, 9400597, 8220004, 9400594, 8220005, 8220006, 8220003, 9500363, 9400575, 9400014, 9400569, 9400121, 9400300, 9400590, 9400591, 9400409);

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
    exitMap = em.getChannelServer().getMapFactory().getMap(910000000); //FM

    var tempGuildID = em.getProperty("guildID");

    var eim = em.newInstance("GuildBossFight_" + tempGuildID);

    var timer = 305000; // 5 minutes
    em.schedule("timeOut", eim, timer);
    em.schedule("spawnNextBoss", eim, 5000);

    eim.setProperty("currentMob", "0");
    eim.setProperty("guildID", tempGuildID);
    eim.setProperty("finished", "false");
    eim.setProperty("pointsEarned", "0");

    eim.startEventTimer(timer);

    return eim;
}

function playerEntry(eim, player)
{
    var map = eim.getMapInstance(910210000);
    map.toggleDrops();

    player.changeMap(map, map.getPortal(0));

    player.dropMessage(5, "The boss fight will start in 3 seconds!");
}

function playerRevive(eim, player)
{
    player.setHp(500);
    player.setStance(0);
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over. Your guild has earned a total of " + eim.getProperty("pointsEarned") + " guild points.");
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
        end(eim, "There are no longer enough players to continue. The battle is over. Your guild has earned a total of " + eim.getProperty("pointsEarned") + " guild points.");
    }
}

function leftParty(eim, player)
{
    playerExit(eim, player);

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over. Your guild has earned a total of " + eim.getProperty("pointsEarned") + " guild points.");
    }
}

function disbandParty(eim)
{
    end(eim, "The party has been disbanded. The battle is over. Your guild has earned a total of " + eim.getProperty("pointsEarned") + " guild points.");
}

function playerExit(eim, player)
{
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.getPlayers().size() < minPlayers)
    {
        end(eim, "There are no longer enough players to continue. The battle is over. Your guild has earned a total of " + eim.getProperty("pointsEarned") + " guild points.");
    }
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
    var currentMob = parseInt(eim.getProperty("currentMob"));
    var pointsEarned = parseInt(eim.getProperty("pointsEarned"));

    var guild = eim.getPlayers().get(0).getGuild();

    if (mobId == bosses[currentMob])
    {
        if (currentMob <= 15)
        {
            for(var z = 0; z < eim.getPlayerCount(); z++)
            {
                var player = eim.getPlayers().get(z);   
                player.dropMessage(5, "Boss #" + currentMob + ": Your guild has earned 2 guild points for this boss kill.");
            }
            guild.gainGP(1);
            pointsEarned += 1;
        }
        else if (currentMob <= 22)
        {
            for(var z = 0; z < eim.getPlayerCount(); z++)
            {
                var player = eim.getPlayers().get(z);
                guild = player.getGuild();   
                player.dropMessage(5, "Boss #" + currentMob + ": Your guild has earned 2 guild points for this boss kill.");
            }
            guild.gainGP(2);
            pointsEarned += 2;
        }
        else if (currentMob <= 30)
        {
            for(var z = 0; z < eim.getPlayerCount(); z++)
            {
                var player = eim.getPlayers().get(z);
                guild = player.getGuild();   
                player.dropMessage(5, "Boss #" + currentMob + ": Your guild has earned 3 guild points for this boss kill.");
            }
            guild.gainGP(3);
            pointsEarned += 3;
        }
        else if (currentMob <= 36)
        {
            for(var z = 0; z < eim.getPlayerCount(); z++)
            {
                var player = eim.getPlayers().get(z);   
                player.dropMessage(5, "Boss #" + currentMob + ": Your guild has earned 4 guild points for this boss kill.");
            }
            guild.gainGP(4);
            pointsEarned += 4;
        }
        else
        {
            for(var z = 0; z < eim.getPlayerCount(); z++)
            {
                var player = eim.getPlayers().get(z);   
                player.dropMessage(5, "Boss #" + currentMob + ": Your guild has earned 5 guild points for this boss kill.");
            }
            guild.gainGP(5);
            pointsEarned += 5;
        }

        eim.setProperty("pointsEarned", pointsEarned.toString());
        currentMob++;

        if (currentMob == bosses.length - 1)
        {
            eim.setProperty("finished", "true");
            pointsEarned += 50;
            eim.setProperty("pointsEarned", pointsEarned.toString());
            guild.gainGP(50);
            end(eim, "Congratulations on completing the whole boss fight! You have earned an extra 50 guild points! Your guild has earned a total of " + eim.getProperty("pointsEarned") + " guild points.");
        }
        else
        {
            eim.setProperty("currentMob", currentMob.toString());
            em.schedule("spawnNextBoss", eim, 1000);
        }
    }
}

function spawnNextBoss(eim)
{
    em.getInstance("GuildBossFight_" + eim.getProperty("guildID")).getMapFactory().getMap(910210000).spawnMonsterWithCoordsEXP(Packages.server.life.MapleLifeFactory.getMonster(bosses[parseInt(eim.getProperty("currentMob"))]), 493, 165, 0);
}

function cancelSchedule()
{

}

function timeOut(eim)
{    
    if (eim.getProperty("finished") == "false")
    {
        if (eim.getPlayerCount() > 0)
        {
            end(eim, "Time has ran out! Everyone will be warped out. Your guild has earned a total of " + eim.getProperty("pointsEarned") + " guild points.");
        }
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