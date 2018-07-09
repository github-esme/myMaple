var status = -1;

function start(mode, type, selection) {
	if (qm.isQuestStarted(2214) && !qm.haveItem(4031894)) { 
		qm.sendOk("You have found a Crumpled Piece of Paper.");
		qm.gainItem(4031894);
		qm.forceCompleteQuest();
		qm.dispose();
	} else {
		qm.sendOk("...");
		qm.dispose();
	}
}
function end(mode, type, selection) {
	if (qm.isQuestStarted(2214) && !qm.haveItem(4031894)) { 
		qm.sendOk("You have found a Crumpled Piece of Paper.");
		qm.gainItem(4031894);
		qm.forceCompleteQuest();
		qm.dispose();
	} else {
		qm.sendOk("...");
		qm.dispose();
	}
}
