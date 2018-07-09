// Memory Lane

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
        if (cm.isQuestCompleted(3539) || cm.isQuestCompleted(3523) || cm.isQuestCompleted(3524) || cm.isQuestCompleted(3525) || cm.isQuestCompleted(3526) || cm.isQuestCompleted(3527))
        {
            cm.forceCompleteQuest(3507);
            cm.gainExp(2159400);

            cm.sendOk("The Memory Lane quest has been completed.");
            cm.dispose();
        }
        else
        {
            cm.sendOk("You haven't talked to your 1st job advancer after receiving the Memory Lane quest!");
            cm.dispose();
        }
    }
}