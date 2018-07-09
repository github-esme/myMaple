// Searching for the Demon

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
        if (cm.isQuestStarted(28255))
        {
            if (cm.hasItem(4032494))
            {
                cm.sendOk("You already have a #v4032494# #t4032494#!");
                cm.dispose();
            }
            else
            {
                cm.sendYesNo("Would you like a #v4032494# #t4032494#?");
            }
        }
        else
        {
            cm.dispose();
            return;
        }
    }
    else if (status == 1)
    {
        cm.gainItem(4032494);
        cm.sendOk("You have received a #v4032494# #t4032494#!");
        cm.dispose();
    }
}