// Nana(H)

var status = 0;

function start() 
{
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection)
{
	if (mode == -1)
	{
		cm.dispose();
	}
	else 
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
			if (cm.getJobId() == 2000 || cm.getPlayer().getJob().isA(Packages.client.MapleJob.ARAN1))
			{
				if (cm.getCustomQuestStatus(0) == 0)
				{
					cm.sendYesNo("Would you like to get medals for hitting a certain combo number?\r\n\r\n#v1142134# #t1142134# - 50 Combos\r\n#v1142135# #t1142135# - 200 Combos\r\n#v1142136# #t1142136# - 500 Combos");
				}
			}
			else
			{
				cm.sendOk("Hi there.");
				cm.dispose();
			}
		}
		else if (status == 1)
		{
			cm.startCustomQuest(0);
			cm.sendOk("You will now receive medals automatically.");
			cm.dispose();
		}
	}
}