function enter(pi) {
    if (pi.isQuestCompleted(20407)) {
	pi.warp(924010200,0);
	return true;
    } else if (pi.isQuestCompleted(20406)) {
	pi.warp(924010100,0);
	return true;
    } else if (pi.isQuestCompleted(20404)) {
	pi.warp(924010000,0);
	return true;
    } else {
	pi.getPlayer().dropMessage(5, "I shouldn't go here.. it's creepy!");
	return false;
    }
}