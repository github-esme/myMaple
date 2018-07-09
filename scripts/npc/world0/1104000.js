function start() {
    cm.sendNext("Oh... Did I just found something? Then there's only one way out! Let's fight");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.removeNpc(cm.getMapId(), cm.getNpc());
	cm.getPlayer().getMap().spawnMonsterOnGroudBelow(Packages.server.life.MapleLifeFactory.getMonster(9300285), new java.awt.Point(329, 257));
    }
    cm.dispose();
}