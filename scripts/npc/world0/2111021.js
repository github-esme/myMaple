var status;

function start() {
    status = -1;
    action(1, 0, 0);
}


function action(mode, type, selection) {
    if(mode == 1)
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
        if(cm.isQuestStarted(3345))
        {
            cm.gainExp(2200);
            cm.completeQuest(3345);
            cm.sendOk("The quest has been completed!");
        }

        cm.dispose();
    }
}