var status = -1;

function start()
{
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.isQuestStarted(3929)) 
    {
		cm.forceCompleteQuest(3929);
		cm.gainExp(12000);
		cm.sendNext("Quest complete.");
    }
    else if (cm.isQuestStarted(3926))
    {
    	cm.forceCompleteQuest(3926);
    	cm.gainExp(39000);
		cm.sendNext("Quest complete.");
    }
    cm.dispose();
}