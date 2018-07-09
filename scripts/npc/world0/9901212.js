// Secret Spell Scroll NPC

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
		cm.sendYesNo("Hey there traveler! Since Chief Tatamo is too lazy, I can exchange your #v4031348# #t4031348# for a #v4031860# #t4031860# and a #v4031861# #t4031861#. Would you like to exchange?");
	}
	else if (status == 1)
	{
		if (cm.hasItem(4031348))
		{
			if(cm.canHold(4031511, 2))
			{
				cm.gainItem(4031348, -1);

				if (cm.getPlayer().getJob().isA(Packages.client.MapleJob.WARRIOR))
				{
					cm.gainItem(4031343);
					cm.gainItem(4031344);
				}
				else if (cm.getPlayer().getJob().isA(Packages.client.MapleJob.MAGICIAN))
				{
					cm.gainItem(4031511);
					cm.gainItem(4031512);
				}
				else if (cm.getPlayer().getJob().isA(Packages.client.MapleJob.BOWMAN))
				{
					cm.gainItem(4031514);
					cm.gainItem(4031515);
				}
				else if (cm.getPlayer().getJob().isA(Packages.client.MapleJob.THIEF))
				{
					cm.gainItem(4031517);
					cm.gainItem(4031518);
				}
				else if (cm.getPlayer().getJob().isA(Packages.client.MapleJob.PIRATE))
				{
					cm.gainItem(4031860);
					cm.gainItem(4031861);
				}

				cm.sendOk("You have received a #v4031511# #t4031511# and a #v4031512# #t4031512#!");
				cm.dispose();
			}
			else
			{
				cm.sendOk("Please make room in your inventory.");
				cm.dispose();
			}
		}
		else
		{
			cm.sendOk("You don't have a #v4031348# #t4031348#!");
			cm.dispose();
		}
	}
}