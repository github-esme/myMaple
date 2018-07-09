var status = -1;

function start() {
    if (cm.isQuestCompleted(3360)) {
	if (cm.getMapId() == 261010000) {
	    cm.sendOk("Your name is on the list. You'll now be transported to the secret tunnel.");
	    cm.warp(261030000, "sp_jenu");
	} else {
	    cm.sendOk("Your name is on the list. You'll now be transported to the secret tunnel.");
	    cm.warp(261030000, "sp_alca");
	}
	cm.dispose();
    } else if (cm.isQuestStarted(3360)) {
	cm.sendGetText("Please enter the password.");
    } else {
	cm.dispose();
    }
}

function action(mode, type, selection) {
    var pw = cm.getText();
	cm.forceCompleteQuest(3360);
	cm.sendOk("The security device has been disengaged.");
    cm.dispose();
}