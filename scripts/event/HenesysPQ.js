/* 
*
*Henesys PQ : FightDesign ->RaGEZONE / FXP
*
*/
// Significant maps (this was already here, helpful though)
// 100000200 - Pig Park
// 910010000 - 1st Stage
// 910010100 - Shortcut
// 910010200 - Bonus
// 910010300 - Exit
// Significant items
// 4001101 - Rice Cake
// Significant monsters
// 9300061 - Bunny
// 9300062 - Flyeye
// 9300063 - Stirge
// 9300064 - Goblin Fires
// Significant NPCs
// 1012112 - Troy
// 1012113 - Tommy
// 1012114 - Growlie
// map effects
// Map/Obj/Effect/quest/gate/3 - warp activation glow
// quest/party/clear - CLEAR text
// Party1/Clear - clear sound
/* INSERT monsterdrops (monsterid,itemid,chance) VALUES (9300061,4001101,1);
 */


importPackage(Packages.net.world);
importPackage(Packages.tools);

var exitMap;
var mainMap;
var minPlayers = 2;
var pqTime = 20; //20 Minutes. Default was 10

function init() {
    exitMap = em.getChannelServer().getMapFactory().getMap(910010400); // <exit>
    exitClearMap = em.getChannelServer().getMapFactory().getMap(910010100); // <clear>
    mainMap = em.getChannelServer().getMapFactory().getMap(910010000); // <main>
    em.setProperty("HPQOpen", "true"); // allows entrance.
}

function monsterValue(eim, mobId) {
    return 1;
}




function setup() {
    em.setProperty("HPQOpen", "false")
    var eim = em.newInstance("HenesysPQ_" + em.getProperty("latestLeader"));
    eim.setProperty("stage", "0");
    eim.setProperty("clear", "false");
    eim.getMapInstance(910010000).allowSummonState(false);
    eim.getMapInstance(910010000).killAllMonsters();
    respawn(eim);
    var timer = 1000 * 60 * pqTime; // 20 minutes default was ten
    em.schedule("timeOut", eim, timer);
    eim.startEventTimer(timer);
    return eim;
}


function respawn(eim) {	
	var map = eim.getMapInstance(910010000);
	if (map.getSummonState()) {	
		map.instanceMapRespawn();
	}
	eim.schedule("respawn", 10000);
}


function playerEntry(eim, player) {
    var map = eim.getMapInstance(mainMap.getId());
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

    if (eim.isLeader(player))
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

function playerExitClear(eim, player) {
    eim.unregisterPlayer(player);
    player.changeMap(exitClearMap, exitClearMap.getPortal(0));
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
    var party = eim.getPlayers();
    for (var i = 0; i < party.size(); i++)
        playerExitClear(eim, party.get(i));
    eim.dispose();
}

function allMonstersDead(eim) {}

function dispose() {
    em.cancelSchedule();
    em.schedule("OpenHPQ", 5000);
}

function cancelSchedule(eim) {
	//This needed? It causes problem on reloadevents
    //eim.startEventTimer(0);
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

function OpenHPQ() {
    em.setProperty("HPQOpen", "true");
} 