// Ninja Castle Transporter

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
		cm.sendYesNo("Hey there traveler! Would you like to go back to Mushroom Shrine?");
	}
	else if (status == 1)
	{
		cm.warp(800000000);
		cm.dispose();
	}
}