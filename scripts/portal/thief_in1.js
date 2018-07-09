function enter(pi) {
    if (pi.isQuestStarted(3925)) {
	pi.forceCompleteQuest(3925);
	pi.message("The quest has been completed.");
	pi.gainExp(6500);
    }
    pi.warp(260010402, 0);

    return true;
}