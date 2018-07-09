var status = 0;

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
        status--;
    }

    if (status == 0)
    {
        cm.sendYesNo("Do you want to obtain Abel's glasses?");
    }
    else if (status == 1) 
    {
        if(!(cm.haveItem(4031853)))
        {
            cm.gainItem(4031853, 1);
        }
        else
        {
            cm.sendOk("You already have Abel's glasses.");
        }
        cm.dispose();
    }
    else
    {
        cm.dispose();
    }
}