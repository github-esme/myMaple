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
        if (cm.isQuestStarted(3340))
        {
            cm.gainItem(4161031);
            cm.completeQuest(3340);
            cm.startQuest(3341);
            cm.sendOk("The quest has been completed!");
            cm.gainExp(100);
        }

        cm.dispose();
    }
}