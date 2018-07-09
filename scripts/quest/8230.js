var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Find the Crimsonwood Keystone.");
	qm.forceStartQuest();
	if (!qm.isQuestStarted(8223) && !qm.isQuestCompleted(8223)) {
		qm.forceStartQuest(8223);
	}
	qm.dispose();
}
function end(mode, type, selection) {
	if (!qm.isQuestCompleted(8223)) {
		qm.sendNext("Please, find it!");
	} else {
		qm.forceCompleteQuest();
		qm.sendNext("Good job. Now we can proceed.");
	}
	qm.dispose();
}
