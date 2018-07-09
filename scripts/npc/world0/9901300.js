// Mushroom Shrine Quests

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

        if (!cm.isQuestCompleted(4300) && !cm.isQuestStarted(4300) && cm.getPlayer().getLevel() >= 20)
        {
            questList += "#L0#Kino Konoko's Concern#l\r\n";
        }

        if (!cm.isQuestCompleted(4301) && !cm.isQuestStarted(4301) && cm.getPlayer().getLevel() >= 25)
        {
            questList += "#L1#Kino Konoko vs Fire Raccoon#l#k#n\r\n";
        }

        if (questList == "")
        {
            cm.sendOk("There are no quests available.");
            cm.dispose();
        }
        else
        {
            cm.sendSimple("You can start some of the Kino Konoko's quests with me. What quests would you like to start?\r\n\r\n" + questList);
        }
    }
    else if (status == 1)
    {
        if (selection == 0)
        {
            cm.startQuest(4300);
            cm.sendOk("Kino Konoko's Concern quest has started!");
            cm.dispose();
        }
        else if (selection == 1)
        {
            cm.startQuest(4301);
            cm.sendOk("Kino Konoko vs Fire Raccoon quest has started!");
            cm.dispose();
        }
    }
}