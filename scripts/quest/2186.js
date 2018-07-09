/*
    Author: BubblesDev 0.75
    Quest: Abel Glasses Quest
*/

function end(mode, type, selection){
    if(!qm.isQuestCompleted(2186))
        if(qm.haveItem(4031853)){
            qm.gainItem(4031853, -1);
            qm.gainExp(1700);
            qm.gainItem(2030019, 10);
            qm.sendNext("What? You found my glasses? I better put it on first, to make sure that it's really mine. Oh, it really is mine. Thank you so much!");
            qm.forceCompleteQuest();
        }else if(qm.haveItem(4031854) || qm.haveItem(4031855)){ //When I figure out how to make a completance with just a pickup xD
            if(qm.haveItem(4031854))
                qm.gainItem(4031854, -1);
            else
                qm.gainItem(4031855, -1);
            qm.sendNext("Sorry, those aren't my glasses.");
        }
        else
            qm.c.getPlayer().dropMessage(1, "Unknown Error");
    qm.dispose();
}