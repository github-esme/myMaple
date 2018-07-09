/*
All Time Temple portal
*/

var tomap;

function enter(pi) {
    switch (pi.getMapId()) {
        // Green area
        case 270010100:
            if (pi.isQuestStarted(3502) || pi.isQuestCompleted(3502))
                tomap = 270010110;
            else
                tomap = -1;

            break;
        case 270010200:
        if (pi.isQuestStarted(3503) || pi.isQuestCompleted(3503))
                tomap = 270010210;
            else
                tomap = -1;

            break;

        case 270010300:
        if (pi.isQuestStarted(3504) || pi.isQuestCompleted(3504))
                tomap = 270010310;
            else
                tomap = -1;

            break;
        case 270010400:
        if (pi.isQuestStarted(3505) || pi.isQuestCompleted(3505))
                tomap = 270010410;
            else
                tomap = -1;

            break;
        case 270010500:
        if (pi.isQuestCompleted(3507) || pi.isQuestCompleted(3523) || pi.isQuestCompleted(3524) || pi.isQuestCompleted(3525) || pi.isQuestCompleted(3526) || pi.isQuestCompleted(3527))
                tomap = 270020000;
            else
                tomap = -1;

            break;
        // Blue area
        case 270020100:
        if (pi.isQuestStarted(3509) || pi.isQuestCompleted(3509))
                tomap = 270020110;
            else
                tomap = -1;

            break;
        case 270020200:
        if (pi.isQuestStarted(3510) || pi.isQuestCompleted(3510))
                tomap = 270020210;
            else
                tomap = -1

            break;
        case 270020300:
        if (pi.isQuestStarted(3511) || pi.isQuestCompleted(3511))
                tomap = 270020310;
            else
                tomap = -1

            break;
        case 270020400:
        if (pi.isQuestStarted(3512) || pi.isQuestCompleted(3512))
                tomap = 270020410;
            else
                tomap = -1

            break;
        case 270020500:
        if (pi.isQuestCompleted(3514))
                tomap = 270030000;
            else
                tomap = -1

            break;
        // Red zone
        case 270030100:
        if (pi.isQuestStarted(3516) || pi.isQuestCompleted(3516))
                tomap = 270030110;
            else
                tomap = -1

            break;
        case 270030200:
        if (pi.isQuestStarted(3517) || pi.isQuestCompleted(3517))
                tomap = 270030210;
            else
                tomap = -1

            break;
        case 270030300:
        if (pi.isQuestStarted(3518) || pi.isQuestCompleted(3518))
                tomap = 270030310;
            else
                tomap = -1

            break;
        case 270030400:
        if (pi.isQuestStarted(3519) || pi.isQuestCompleted(3519))
                tomap = 270030410;
            else
                tomap = -1

            break;
        case 270030500:
            tomap = 270040000;

            break;
        case 270040000:
            // Quest 3521 to get the chaos marble

            if (pi.haveItem(4032002)) 
            { 
                cm.gainItem(4032002, -1);
				pi.playPortalSound();
                pi.warp(270040100, "out00");
                pi.message("Now moving to a deep part of the temple.");
                return true;
            }
            else
            {
                pi.message("You do not have a Marble of Chaos!");
                return true;
            }
            break;
        default:
            return false;
    }
	pi.playPortalSound();

    if (tomap == -1)
    {
        pi.message("You can not enter!");
    }
    else
    {
        pi.warp(tomap, "out00");
    }

    return true;
}