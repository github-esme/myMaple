function enter(pi) {
    var mapid = pi.getMapId();
    var questid;

    switch (mapid) {
	case 130030001:
	    questid = 20010;
	    break;
	case 130030002:
	    questid = 20011;
	    break;
	case 130030003:
	    questid = 20012;
	    break;
	case 130030004:
	    questid = 20013;
	    break;
	default:
	    return;
    }
    if (pi.isQuestCompleted(questid)) {
	pi.warp(pi.getMapId() + 1, "sp");
	return true;
    } else if (pi.isQuestStarted(20010) && mapid == 130030001) {
    	pi.warp(pi.getMapId() + 1, "sp");
    	return true;
    } else {
	if (mapid == 130030001) {
	    pi.message("Please click the NPC to receive a quest.");
	    return false;
	} else {
	    pi.message("Please complete the quest.");
	    return false;
	}
    }
}