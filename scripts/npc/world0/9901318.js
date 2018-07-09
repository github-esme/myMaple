// Zakum Warper

importPackage(Packages.server.expeditions);
importPackage(Packages.tools);
importPackage(Packages.scripting.event);

var status;

var zakum = MapleExpeditionType.ZAKUM;
var em;
var expedition;

function start()
{
    status = -1;

    expedition = cm.getExpedition(zakum);
    em = cm.getEventManager("ZakumBattle");

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
        if (expedition != null && expedition.isInProgress() && expedition.contains(cm.getPlayer()))
        {
            cm.sendYesNo("Would you like to get warped back into Zakum?");
        }
        else
        {
            cm.sendOk("You can talk to me to get warped back into Zakum if you disconnect in the middle of Zakum.");
            cm.dispose();
        }
    }
    else if (status == 1)
    {
        if (em.getInstance("ZakumBattle_" + cm.getPlayer().getClient().getChannel()) != null)
        {
            em.getInstance("ZakumBattle_" + cm.getPlayer().getClient().getChannel()).registerPlayer(cm.getPlayer());
            cm.dispose();
        }
        else
        {
            cm.sendOk("You are unable to join the expedition.");
            cm.dispose();
        }
    }
}