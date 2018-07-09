// Kenta's Advice Quest

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
		if (cm.isQuestStarted(3083))
		{
			cm.sendYesNo("Would you like all the Piece of Papers?");
		}
		else
		{
			cm.sendOk("Hi there. I'm the NPC that gives you all the Piece of Paper to complete the Kenta's Advice quest.");
		}
	}
	else if (status == 1)
	{
		if (cm.hasItem(4031274))
		{
			cm.sendOk("You already have the item!");
		}
		else
		{
			cm.gainItem(4031274);
			cm.gainItem(4031275);
			cm.gainItem(4031276);
			cm.gainItem(4031277);
			cm.gainItem(4031278);
			cm.sendOk("You have received all the Piece of Paper!");
			cm.dispose();
		}
	}
}