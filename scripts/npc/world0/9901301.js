// Showa Quests

var status;

function start()
{
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection)
{
    if (mode == 1)
    {
        status++;
    }
    else
    {
        cm.dispose();
        return;
    }

    if (status == 0)
    {
        var questList = "#b#e";

        if (!cm.isQuestCompleted(4305) && !cm.isQuestStarted(4305) && cm.getPlayer().getLevel() >= 40)
        {
            questList += "#L0#Eliminating Dark Cloud Fox#l\r\n";
        }

        if (!cm.isQuestCompleted(4302) && !cm.isQuestStarted(4302) && cm.getPlayer().getLevel() >= 25)
        {
            questList += "#L1#Hanako's Foxtail Muffler 1#l#k#n\r\n";
        }
        else if (!cm.isQuestCompleted(4303) && !cm.isQuestStarted(4303) && cm.getPlayer().getLevel() >= 25)
        {
            questList += "#L2#Hanako's Foxtail Muffler 2#l#k#n\r\n";
        }
        else if (!cm.isQuestCompleted(4304) && !cm.isQuestStarted(4304) && cm.getPlayer().getLevel() >= 25)
        {
            questList += "#L3#Hanako's Foxtail Coat#l#k#n\r\n";
        }

        if (questList == "")
        {
            cm.sendOk("There are no quests available.");
            cm.dispose();
        }
        else
        {
            cm.sendSimple("You can start some of the Hanako's quests with me. What quests would you like to start?\r\n\r\n" + questList);
        }
    }
    else if (status == 1)
    {
        if (selection == 0)
        {
            cm.startQuest(4305);
            cm.sendOk("Hanako's Foxtail Muffler 1 quest has started!");
            cm.dispose();
        }
        else if (selection == 1)
        {
            cm.startQuest(4302);
            cm.sendOk("Hanako's Foxtail Muffler 2 quest has started!");
            cm.dispose();
        }
        else if (selection == 2)
        {
            cm.startQuest(4303);
            cm.sendOk("Kino Konoko vs Fire Raccoon quest has started!");
            cm.dispose();
        }
        else if (selection == 3)
        {
            cm.startQuest(4304);
            cm.sendOk("Hanako's Foxtail Coat quest has started!");
            cm.dispose();
        }
    }
}