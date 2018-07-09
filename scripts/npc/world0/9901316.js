// The Lantern at Mushroom Shrine

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
        if (cm.isQuestStarted(4306))
        {
            cm.sendOk("The Lantern at Mushroom Shrine quest has been completed!");
            cm.gainExp(63000);
            cm.forceCompleteQuest(4306);
            cm.dispose();
        }
        else if (!cm.isQuestCompleted(4306))
        {
            cm.sendOk("Please talk to me when you have the Lantern at Mushroom Shrine quest.");
            cm.dispose();
        }
    }
}