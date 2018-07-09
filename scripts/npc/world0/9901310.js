// Buy Cracked Piece

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
        cm.sendYesNo("Would you like to buy a #v4031179# #t4031179# for 15,000,000 mesos?");
    }
    else if (status == 1)
    {
        if (cm.getMeso() >= 15000000)
        {
        	if (cm.canHold(4031179))
        	{
                cm.gainMeso(-15000000);
        		cm.gainItem(4031179);
        		cm.sendOk("Here is your #v4031179# #t4031179#!");
        		cm.dispose();
        	}
        	else
        	{
        		cm.sendOk("Please make room in your inventory!");
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