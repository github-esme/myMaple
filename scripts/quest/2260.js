var status = -1;
var choice1;

function start(mode, type, selection) {
    if (mode < 1) {
        qm.dispose();
        return;
    } 
    if (mode > 0)
        status++;
    if (status == 0)
        qm.sendOk("I want you to train harder so get to level 30. Once you have done that, talk to me again.");
    else if (status == 1) {
        qm.forceStartQuest();
        qm.dispose();
    }
}

function end(mode, type, selection) {
    if(qm.getPlayer().getLevel() >= 30)
    {
        qm.forceCompleteQuest();
        qm.dispose();
        return;
    }
    else
    {
        qm.sendOk("Talk to me when you get to level 30!");
        qm.dispose();
        return;
    }
}