// The Imperiled Grays

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
    	if (cm.isQuestCompleted(3456) || !cm.hasItem(4031927))
    	{
    		cm.sendOk("Look at this ship, it's amazing!");
    		cm.dispose();
    	}
        else if ((cm.isQuestCompleted(3455) || cm.isQuestStarted(3455) || cm.hasItem(4031927)) && !cm.isQuestCompleted(3456))
        {
            cm.forceCompleteQuest(3455);
            cm.forceCompleteQuest(3456);

            if (cm.hasItem(4031927))
            {
            	cm.gainItem(4031927, -1);
            }

            cm.sendOk("The quest has been complete.");
            cm.dispose();
        }
        else
        {
            cm.sendOk("Look at this ship, it's amazing!");
    		cm.dispose();
        }
    }
}