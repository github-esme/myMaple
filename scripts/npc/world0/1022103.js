function start() {
    cm.sendYesNo("If you leave now, you'l won't be able to join back. Are you sure you want to leave?");
}

function action(mode, type, selection) {
    if (mode < 1)
        cm.dispose();
    else 
    {
        var eim = cm.getPlayer().getEventInstance();
        
        if (eim != null)
        {
            if(eim.getPlayerCount() == 1)
            {
                cm.getPlayer().getMap().killAllMonsters();
                cm.getPlayer().getMap().resetReactors();
                eim.unregisterPlayer(cm.getPlayer());

                eim.dispose();

                cm.getPlayer().changeMap(910000000);
            }
            else
            {
                eim.removePlayer(cm.getPlayer()); // Triggers playerExit
            }
        }
        else
        {
            cm.getPlayer().changeMap(910000000);
        }

        cm.dispose();
    }
}