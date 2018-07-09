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
		if (cm.isQuestStarted(20710) && !cm.hasItem(4032136))
		{
			cm.gainItem(4032136);
			cm.sendOk("Here is your #v4032136# #t4032136#!");
		}

		cm.dispose();
	}
}