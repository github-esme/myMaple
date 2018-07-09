var status = -1;

function start()
{
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.isQuestStarted(3927)) {
	cm.forceCompleteQuest(3927);
	cm.sendNext("Quest complete.");
    }
    cm.dispose();
}