var status;

function start() {
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
        if (cm.isQuestStarted(3322))
        {
            if (!cm.hasItem(4031697))
            {
                cm.gainItem(4031697);
                cm.sendOk("You have gained a #v4031697# #t4031697#!");
            }
        }

        cm.dispose();
    }
}