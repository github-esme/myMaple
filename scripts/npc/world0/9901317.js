// The Plants are Suspicious

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
        if (cm.isQuestStarted(20717) && !cm.hasItem(4032143, 10))
        {
            if (cm.canHold(4032143, 10))
            {
                cm.gainItem(4032143, 10);
                cm.sendOk("You have gained 10 #v4032143# #t4032143#!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("Please make room in your inventory!");
                cm.dispose();
            }
        }
        else if (!cm.isQuestCompleted(20717))
        {
            cm.sendOk("Please talk to me when you have the The Plants are Suspicious quest.");
            cm.dispose();
        }
    }
}