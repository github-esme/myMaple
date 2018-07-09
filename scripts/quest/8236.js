var status = -1;

function start(mode, type, selection) {
	qm.sendOk("Please bring me 1 #i4031903# Jack O'Latern");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.forceCompleteQuest();
	qm.dispose();
}