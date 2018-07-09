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
		cm.sendYesNo("Hello there stranger. Would you like to craft Balanced Fury Throwing Stars?");
	}
	else if (status == 1)
	{
		cm.sendYesNo("Okay, you will need fulfill the requirements below. Do you fulfill the requirements?\r\n\r\n#e1 #v4032015# #t4032015#\r\n1 #v4032016# #t4032016#\r\n1 #v4032017# #t4032017#\r\n100 #v4021008# #t4021008#\r\n30 #v4032005# #t4032005#\r\n150,000 Mesos#n");
	}
	else if (status == 2)
	{
		if (cm.hasItem(4032015) && cm.hasItem(4032016) && cm.hasItem(4032017) && cm.hasItem(4021008, 100) && cm.hasItem(4032005, 30) && cm.getMeso() >= 150000)
		{
			cm.gainItem(4032015, -1);
			cm.gainItem(4032016, -1);
			cm.gainItem(4032017, -1);
			cm.gainItem(4021008, -100);
			cm.gainItem(4032005, -30);
			cm.gainMeso(-150000);
			cm.gainItem(2070018);

			cm.sendOk("You have crafted a #v2070018# #t2070018#!");
			cm.dispose();
		}
		else
		{
			cm.sendOk("You do not fulfill the requirements!");
			cm.dispose();
		}
	}
}