var status = -1;

function start()
{
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.isQuestStarted(3923) && !cm.hasItem(4031578)) 
    {
	cm.gainItem(4031578);
	cm.sendOk("Here is your #v4031578# #t4031578#!");
    }
    cm.dispose();
}