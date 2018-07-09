/* 
 * NPC :      Mihai
 * Map :      Timu's Forest
 */

function start() {
    cm.sendNext("Oh... Did I just found something? Then there's only one way out! Let's fight like a #rBlack Wing#k should!");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.removeNpc(cm.getMapId(), cm.getNpc());
	cm.getPlayer().getMap().spawnMonsterOnGroudBelow(Packages.server.life.MapleLifeFactory.getMonster(9001009), new java.awt.Point(542, 88));
    }
    cm.dispose();
}