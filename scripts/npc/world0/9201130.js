var status = -1;

function start()
{
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.dispose();
	return;
    }
    if (status == 0) {
		// if (cm.hasItem(4032493)) {
		// 	cm.sendYesNo("Would you like to move to Valefor's Strolling Place?");
		// } else {
		// 	cm.sendOk("You need the Valefor's Emblem to enter.");
		// 	cm.dispose();
		// }
		cm.sendYesNo("Would you like to move to Valefor's Strolling Place?");
} else {
	cm.warp(677000008,0);
	cm.dispose();
    }
}