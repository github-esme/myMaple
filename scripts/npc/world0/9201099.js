var status;
var selectedItem;

var items = [[2050004, 400], [2050000, 200], [2020012, 4500], [2020013, 5000], [2020014, 8100], [2020015, 9690], [2050001, 200], [2050002, 300], [2050003, 500], [2022000, 1650], [2002017, 5000], [2060004, 40000], [2061004, 40000], [2070010, 2000], [2022003, 1100], [2022177, 620], [2022002, 1000], [2030020, 400]];

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

	if(status == 0)
	{
		if (cm.isQuestCompleted(8224))
		{
			var outStr = "";

			for (var i = 0; i < items.length; i++)
			{
				outStr += "#L" + i + "##v " + items[i][0] + "# #b#e#t" + items[i][0] + "# #k#nfor " + items[i][1] + " mesos#l\r\n";
			}

			cm.sendSimple("What would you like today?\r\n\r\n" + outStr);
		}
		else
		{
			cm.sendOk("I'm only available to those who are in the Raven Ninja Clan.");
			cm.dispose();
		}
	}
	else if (status == 1)
	{
		selectedItem = items[selection];
		cm.sendGetNumber("How many of #e#t" + selectedItem[0] + "##n would you like to buy?\r\n(Per 1,000 stacks for arrows and stars)\r\n\r\n#bYou currently have " + cm.getPlayer().getMeso() + " mesos.#k", 1, 1, 10000);
	}
	else if (status == 2)
	{
		if (cm.getPlayer().getMeso() >= selectedItem[1])
		{
			if (cm.canHold(selectedItem[0]))
			{
				if (selectedItem[0] == 2060004 || selectedItem[0] == 2061004 || selectedItem[0] == 2070010)
				{
					cm.gainItem(selectedItem[0], selection * 1000);
				}
				else
				{
					cm.gainItem(selectedItem[0], selection);
				}
				cm.gainMeso(-selectedItem[1] * selection);
				cm.sendOk("You have bought " + selection + " #t" + selectedItem[0] + "#!");
				cm.dispose();
			}
			else
			{
				cm.sendOk("There is no room in your inventory!");
				cm.dispose();
			}
		}
		else
		{
			cm.sendOk("You do not have enough mesos!");
			cm.dispose();
		}
	}
}