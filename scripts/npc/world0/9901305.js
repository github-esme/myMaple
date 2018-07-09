// Deet and Roi

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
        cm.sendYesNo("Would you like to kill Deet and Roi?");
    }
    else if (status == 1)
    {
        cm.getPlayer().getMap().killAllMonsters();
        cm.sendOk("Deet and Roi has been killed.");
        cm.dispose();
    }
}