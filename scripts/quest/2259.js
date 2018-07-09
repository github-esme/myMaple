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
        qm.sendOk("I'm going to walk over to #m260020700#. I'll meet you over there.");
    else if (status == 1) {
        qm.forceStartQuest();
        qm.dispose();
    }
}

function end(mode, type, selection) {
    if(qm.getPlayer().getMap().getId() == 260020700)
    {
        qm.forceCompleteQuest();
        qm.dispose();
        return;
    }
    else
    {
        qm.sendOk("I told you I was going to meet you over at #m260020700#.");
        qm.dispose();
        return;
    }
}