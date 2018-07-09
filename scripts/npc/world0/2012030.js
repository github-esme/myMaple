var status = -1;

function start() {
	status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.isQuestStarted(3114)) {
	cm.forceCompleteQuest(3114);
	cm.playerMessage(5, "The song was played. +20 Fame");
	if ((cm.getPlayer().getFame() + 20) <= 30000) {
	    cm.getPlayer().addFame(20);
	    cm.getPlayer().updateSingleStat(Packages.client.MapleStat.FAME, cm.getPlayer().getFame());
	}
    }
    cm.playSound("orbis/pa");
    cm.dispose();
}