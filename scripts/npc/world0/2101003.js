var status;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if(mode == 1)
	{
		status++;
	}
	else
	{
		cm.dispose();
		return;
	}

	if(status == 0)
	{
		if(cm.isQuestStarted(3933))
		{
			if(cm.canHold(2012000, 10))
			{
				cm.completeQuest(3933);
				cm.gainExp(7250);
				cm.gainItem(2012000, 10);
				cm.gainItem(2012002, 10);
				cm.sendOk("The quest has been completed!");
			}
			else
			{
				cm.sendOk("Please make more space in your ETC inventory and talk to me again.");
				cm.dispose();
			}
		}

		cm.dispose();
	}
}