importPackage(Packages.server.expeditions);
importPackage(java.lang);

var minPlayers = 1;
var fightTime = 1;
var fightMap;

var exitMap;
var baseMobPoints = 25000;

function init() {
	em.setProperty("leader", "true");
    em.setProperty("state", "0");
    fightMap = em.getChannelServer().getMapFactory().getMap(270050100);
}

function setup(eim, leaderid) {
    var eim = em.newInstance("PinkBeanBattle_" + em.getProperty("channel"));
    var timer = 3600000 * fightTime;

    eim.startEventTimer(timer); // 1 hr
    return eim;
}

function playerEntry(eim,player) 
{
    var map = eim.getMapInstance(fightMap.getId());
    player.changeMap(map,map.getPortal(0));
}


function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    eim.disposeIfPlayerBelow(100, 270050300);
    em.setProperty("state", "0");
		em.setProperty("leader", "true");
}

function changedMap(eim, player, mapid) {
    if (mapid != 270050100) {
	eim.unregisterPlayer(player);

	if (eim.disposeIfPlayerBelow(0, 0)) {
	    em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    return 1;
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);

    if (eim.disposeIfPlayerBelow(0, 0)) {
	em.setProperty("state", "0");
		em.setProperty("leader", "true");
    }
}

function end(eim) {
    if (eim.disposeIfPlayerBelow(100, 270050300)) {
	em.setProperty("state", "0");
		em.setProperty("leader", "true");
    }
}

function clearPQ(eim) {
    end(eim);
}

function allMonstersDead(eim) {
}

function leftParty (eim, player) {}
function disbandParty (eim) {}
function playerDead(eim, player) {}
function cancelSchedule() {}