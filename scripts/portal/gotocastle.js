function enter(pi) {
	if (pi.isQuestStarted(2324)) {
	    pi.forceCompleteQuest(2324);
	    pi.removeAll(2430015);
	    pi.message("Quest complete.");
	}
	pi.warp(106020501,0);
	return true;
}