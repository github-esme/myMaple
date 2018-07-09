// Master Goblin's Call quest NPC

var status;

function start()
{
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection)
{
	if (mode == 1)
	{
		status++;
	}
	else
	{
		cm.dispose();
		return;
	}

	if (status == 0)
	{
		cm.sendYesNo("Hi there. You can start the #eMaster Goblin's Call#n quest with me. Would you like to start it?");
	}
	else if (status == 1)
	{
		if (cm.isQuestCompleted(3840))
		{
			cm.startQuest(3841);
			cm.sendOk("The #eMaster Goblin's Call#n has started! Talk to Master Goblin.");
			cm.dispose();
		}
		else
		{
			cm.sendOk("You need to complete the #eWeakening King Sage Cat#n quest first!");
			cm.dispose();
		}
	}
}