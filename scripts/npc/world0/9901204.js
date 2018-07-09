// Mushroom Kingdom Guardian

importPackage(Packages.client);

var status = -1;

function start() 
{
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, selection)
{
    if (mode != 1) 
    {
        cm.dispose();
        return;
    }
    else
    {
        status++;
    }

    if (status == 0)
    {
        var questlist = "The Test\r\nThe Story Behind the Case\r\nExploring Mushroom Forest (1)\r\nExploring Mushroom Forest (2)\r\nExploring Mushroom Forest (3)\r\nKiller Mushroom Spores (1)\r\nKiller Mushroom Spores (2)\r\nKiller Mushroom Spores (3)\r\nOver the Castle Walls (1)\r\nOver the Castle Walls (2)\r\nOver the Castle Walls (3)\r\nOver the Castle Walls (4)\r\n Sending Provisions (1)\r\n Sending Provisions (2)\r\nStop the Wedding\r\nThe Seal of the Empire\r\nWhere's Violetta?\r\nThe Story of Betrayal\r\nThe Identity of the Princess\r\nTruth Revealed";
        
        if ((cm.isQuestCompleted(2312) || cm.isQuestCompleted(2313) || cm.isQuestCompleted(2314) || cm.isQuestCompleted(2316) || cm.isQuestCompleted(2319) || cm.isQuestCompleted(2324) || cm.isQuestCompleted(2329) || cm.isQuestCompleted(2330) || cm.isQuestCompleted(2331) || cm.isQuestCompleted(2332) || cm.isQuestCompleted(2333) || cm.isQuestCompleted(2334) || cm.isQuestCompleted(2336)) && cm.getPlayer().getLevel() > 38)
        {
            status = 1;
            cm.sendYesNo("It looks like you overleveled to get the medal. Would you like the #v1142188# #t1142188#?");
        }
        else if (cm.getPlayer().getLevel() > 38)
        {
            cm.sendOk("You have overleveled and did not complete a single Mushroom Kingdom quest so I can not give you the medal.");
            cm.dispose();
        }
        else
        {
            cm.sendYesNo("I can give you #v1142188# #t1142188# for completing all the follow quests listed below. Would you like the medal?\r\n\r\n" + questlist);
        }
    }
    else if (status == 1)
    {
        if(cm.hasItem(1142188))
        {
            cm.sendOk("You already have the medal!");
            cm.dispose();
        }
        else
        {
            if(cm.isQuestCompleted(2312) && cm.isQuestCompleted(2313) && cm.isQuestCompleted(2314) && cm.isQuestCompleted(2316) && cm.isQuestCompleted(2319) && cm.isQuestCompleted(2324) && cm.isQuestCompleted(2329) && cm.isQuestCompleted(2330) && cm.isQuestCompleted(2331) && cm.isQuestCompleted(2332) && cm.isQuestCompleted(2333) && cm.isQuestCompleted(2334) && cm.isQuestCompleted(2336))
            {
                cm.sendOk("Here's your medal.");
                cm.gainItem(1142188);
                cm.dispose();
            }
            else
            {
                cm.sendOk("Please complete all the required quests.");
                cm.dispose();
            }
        }
    }
    else if (status == 2)
    {
        if(cm.hasItem(1142188))
        {
            cm.sendOk("You already have the medal!");
            cm.dispose();
        }
        else
        {
            cm.sendOk("Since you are overleveled but did one of the required quests, here's your medal.");
            cm.gainItem(1142188);
            cm.dispose();
        }
    }
}