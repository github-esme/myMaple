var status;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
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
    	cm.sendYesNo("If you leave now, you will not be able to join back. Would you like to leave?");
    }
    else if (status == 1)
    {
        if(cm.getPlayer().getEventInstance() != null)
        {
            if(cm.getPlayer().getEventInstance().getPlayerCount() == 1)
            {
                cm.getPlayer().getMap().killAllMonsters();
                cm.getPlayer().getEventInstance().unregisterPlayer(cm.getPlayer());
                cm.getEventManager("BalrogPQ").getInstance("BalrogPQ_" + cm.getPlayer().getClient().getChannel()).dispose();
                cm.warp(105100100);

                cm.dispose();
            }
        }
        else
        {
            cm.getPlayer().getEventInstance().removePlayer(cm.getPlayer()); // Triggers playerExit in BalrogPQ.js
            cm.dispose();
        }
    }
}