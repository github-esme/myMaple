var status = 0;
var summon;
var nthtext = "bonus";

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1)
        cm.dispose();//ExitChat
    else if (mode == 0)
        cm.dispose();//No
    else{		    //Regular Talk
    	if (mode == 1)
    		status++;
    	else
    		status--;

    	if(status == 0){
    		if(cm.hasItem(4031059))
    		{
    			cm.sendYesNo("Would you like to leave?");
    		}
    		else
    		{
                status = 1;
    			cm.sendYesNo("Since you didn't get a #v4031059# #t4031059#, would you like one and leave?");
    		}
    	}else if(status == 1){
    		if(cm.getMapId() == 108010101)
    		{
    			cm.getPlayer().changeMap(105040305);
    		}
    		else if(cm.getMapId() == 108010201)
    		{
    			cm.getPlayer().changeMap(100040106);
    		}
    		else if(cm.getMapId() == 108010301)
    		{
    			cm.getPlayer().changeMap(105070001);
    		}
    		else if(cm.getMapId() == 108010401)
    		{
    			cm.getPlayer().changeMap(107000402);
    		}
    		else if(cm.getMapId() == 108010501)
    		{
    			cm.getPlayer().changeMap(105040305);
    		}
    		else
    		{
    			cm.getPlayer().changeMap(100000000);
    		}

    		var em = cm.getEventManager("3rdjob");
    		em.getInstance(cm.getPlayer().getName()).unregisterPlayer(cm.getPlayer());
    		cm.dispose();
    	}
    	else if (status == 2)
    	{
    		cm.gainItem(4031059);

    		if(cm.getMapId() == 108010101)
    		{
    			cm.getPlayer().changeMap(105040305);
    		}
    		else if(cm.getMapId() == 108010201)
    		{
    			cm.getPlayer().changeMap(100040106);
    		}
    		else if(cm.getMapId() == 108010301)
    		{
    			cm.getPlayer().changeMap(105070001);
    		}
    		else if(cm.getMapId() == 108010401)
    		{
    			cm.getPlayer().changeMap(107000402);
    		}
    		else if(cm.getMapId() == 108010501)
    		{
    			cm.getPlayer().changeMap(105040305);
    		}
    		else
    		{
    			cm.getPlayer().changeMap(100000000);
    		}

    		var em = cm.getEventManager("3rdjob");
    		em.getInstance(cm.getPlayer().getName()).unregisterPlayer(cm.getPlayer());
    		cm.dispose();
    	}
    }
}