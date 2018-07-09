function enter(pi) {
    if (pi.isQuestCompleted(20407)) {
	pi.warp(130000000,0);
	return true;
    } else {
	pi.getPlayer().dropMessage(5, "I have to defeat the Black Witch first!");
	return false;
    }
}