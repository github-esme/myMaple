var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.isQuestStarted(3421)) {
    cm.gainItem(4031117, 6);
	cm.message("You have found all the Meteorite Sample.");
    }
    cm.dispose();
}