// Trade for quest items NPC

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
		cm.sendYesNo("Hi there. Do you have a #v4031705# #t4031705#? I will trade you it for a #v4031703# #t4031703#.");
	}
	else if (status == 1)
	{
		if (cm.hasItem(4031705))
		{
			cm.gainItem(4031703);
			cm.gainItem(4031705, -1);
			cm.sendOk("Thank you! Here is your #v4031703# #t4031703#.");
			cm.dispose();
		}
		else
		{
			cm.sendOk("You do not have a #v4031705# #t4031705#.");
			cm.dispose();
		}
	}
}