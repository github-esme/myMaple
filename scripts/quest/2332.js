var status = -1;

function start(mode, type, selection) {
	qm.getPlayer().getMap().killAllMonsters();
	qm.getPlayer().getMap().spawnMonsterOnGroudBelow(Packages.server.life.MapleLifeFactory.getMonster(3300008), qm.getPlayer().getPosition());
	qm.sendNext("Please, eliminate the Prime Minister!!!");
	qm.forceStartQuest(2333);
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
		qm.gainItem(4032386,1);
		qm.forceCompleteQuest();
		qm.dispose();
}
	