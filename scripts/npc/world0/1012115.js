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
        if(cm.isQuestStarted(20706))
        {
            cm.sendNext("It looks like there's nothing suspecious in the area.");
        }
        else
        {
            cm.dispose();
            return;
        }
    } else if (status == 1) {
        cm.forceCompleteQuest(20706);
        cm.sendNext("You have spotted the shadow! Better report to #p1103001#.");
    } else if (status == 2) {
        cm.sendNext("The shadow has already been spotted. Better report to #p1103001#.");
        cm.dispose();
    }
}