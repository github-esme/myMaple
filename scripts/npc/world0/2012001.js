function start() {
    if(cm.haveItem(4031047)){
        // var em = cm.getEventManager("Boats");
        // if (em.getProperty("entry") == "true")
        //     cm.sendYesNo("Do you want to go to Ellinia?");
        // else{
        //     cm.sendOk("The boat to Ellinia is ready to take off, please be patience for next one.");
        //     cm.dispose();
        // }

        cm.sendYesNo("Do you want to go to Ellinia?");
    }else{
        cm.sendOk("Make sure you got a Ellinia ticket to travel in this boat. Check your inventory.");
        cm.dispose();
    }
}
function action(mode, type, selection) {
	if (mode <= 0) {
		cm.sendOk("Okay, talk to me if you change your mind!");
		cm.dispose();
		return;
    }
    // cm.gainItem(4031047, -1);
    // cm.warp(200000112);
    cm.dispose();

    cm.gainItem(4031047, -1);
    cm.warp(101000300);
}