var status = -1;

function start(mode, type, selection) {
	qm.gainMeso(30000);
	qm.gainExp(7500);
	qm.getPlayer().gainFame(3);
	qm.sendNext("Thank you so much.");
	qm.forceCompleteQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	qm.gainMeso(30000);
	qm.gainExp(7500);
	qm.getPlayer().gainFame(3);
	qm.sendNext("Thank you so much.");
	qm.forceCompleteQuest();
	qm.dispose();
}
