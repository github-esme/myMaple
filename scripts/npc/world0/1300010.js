/* ===========================================================
			Resonance
	NPC Name: 		Killer Mushroom Spore
	Map(s): 		Mushroom Castle: Deep inside Mushroom Forest(106020300)
	Description: 	Breaking the Barrier
=============================================================
Version 1.0 - Script Done.(18/7/2010)
=============================================================
*/

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
		if(status == 0 && mode == 0){
			cm.sendOk("You have canceled the use of the item.");
			cm.dispose();
		}
		if (mode == 1)
            status++;
        else
            status--;
		}
	if(status == 0){
		if(cm.hasItem(2430014))
		{
			cm.sendYesNo("Are you going to use the #bKiller Mushroom Spore#k?....#e#r* Take Note#n..Please do not apply directly on the body!..If swallowed, please see the nearest doctor!");
		}
		else
		{
			cm.sendOk("You do not have a #bKiller Mushroom Spore#k");
			cm.dispose();
		}
	}if(status == 1) {
		cm.removeAll(2430014);
		cm.warp(106020400);
		cm.sendOk("The Mushroom Forest Barrier has been removed, and penetrated.");
		cm.dispose();
	}
}
			