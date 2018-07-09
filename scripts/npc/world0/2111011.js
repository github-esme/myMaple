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
        if (cm.isQuestStarted(3311))
        {
            cm.completeQuest(3311);
            cm.gainExp(60000);
            cm.sendOk("The quest has been complete!");
        }

        cm.dispose();
    }
}