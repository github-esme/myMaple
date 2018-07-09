var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.isQuestStarted(2236)) 
    {
		cm.forceCompleteQuest(2236);
		cm.removeAll(4032263);
		cm.gainExp(60000);
		cm.sendOk("Quest completed.");
    }
    cm.dispose();
}