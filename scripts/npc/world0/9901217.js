// Maple History Book I

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
		if (cm.isQuestStarted(2074))
		{
			cm.sendYesNo("Would you like a #v4031157# #t4031157#?");
		}
		else
		{
			cm.sendOk("Hi there. I'm the NPC that gives you a #v4031157# #t4031157# to complete the Find the Maple History Book quest since ships are instant teleportation.");
		}
	}
	else if (status == 1)
	{
		if (cm.hasItem(4031157))
		{
			cm.sendOk("You already have the item!");
		}
		else
		{
			cm.gainItem(4031157);
			cm.sendOk("You have received a #v4031157# #t4031157#!");
			cm.dispose();
		}
	}
}