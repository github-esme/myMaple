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
        qm.sendAcceptDecline("Are you able to do something for me? I want you to kill 40 #o2100108#. Do you accept this quest?");
    else if (status == 1) {
        qm.forceStartQuest();
        qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.forceCompleteQuest();
    qm.dispose();
    return;
}