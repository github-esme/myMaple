function enter(pi) {
    if (pi.isQuestStarted(3309)) {
	pi.forceCompleteQuest(3309);
	pi.gainExp(150000);
	pi.message("Quest complete.");
    }
    pi.warp(261020700,0);
    pi.playPortalSound();
    return true;
}