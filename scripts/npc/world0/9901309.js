// Amdusias Bow Giver

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
        cm.sendYesNo("To kill Amdusias, you will need the #v1452084# #t1452084#. Would you like one?");
    }
    else if (status == 1)
    {
        if (!cm.hasItem(1452084))
        {
        	if (cm.canHold(1452084))
        	{
        		cm.gainItem(1452084);
        		cm.sendOk("I gave you a #v1452084# #t1452084#!");
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
        	cm.sendOk("You already have a #v1452084# #t1452084#!");
        	cm.dispose();
        }
    }
}