var status = -1;

function start(mode, type, selection) {
	qm.sendOk("Please bring me 30 #i4032011# Soiled Rags.");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.forceCompleteQuest();
	qm.dispose();
}