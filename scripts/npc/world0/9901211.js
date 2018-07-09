// Rush Skill NPC

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
        if (cm.isQuestCompleted(6110))
        {
            cm.sendOk("You need to complete the Manji's Test quest in order to get your Rush skill!");
            cm.dispose();
        }
        else
        {
            if (cm.hasItem(2280012) || cm.getPlayer().getSkillLevel(1321003) != 0 || cm.getPlayer().getSkillLevel(1121006) != 0 || cm.getPlayer().getSkillLevel(1221007) != 0)
            {
                cm.sendOk("You already have the Rush skill!");
                cm.dispose();
            }
            else
            {
                cm.gainItem(2280012);
                cm.sendOk("You have received a #v2280012# #t2280012#!");
                cm.dispose();
            }
        }
    }
}