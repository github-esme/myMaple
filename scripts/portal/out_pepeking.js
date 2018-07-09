function enter(pi) {
	var eim = pi.getPlayer().getEventInstance();

	if (pi.getPlayer().getMapId() == 106021500) 
	{
		var nextMap = 106021501;

		var avail = eim.getProperty("yeti1");
		if (avail != "yes") 
		{
			pi.getPlayer().dropMessage(6, "Please kill the Yeti in order to proceed to the next stage.");
			return false;
		} 
		else 
		{
			pi.warpParty(nextMap);
			eim.schedule("yetiTwo", 100);
			return true;
		}
	}
	else if (pi.getPlayer().getMapId() == 106021501)
	{
		var nextMap = 106021502;

		var avail = eim.getProperty("yeti2");
		if (avail != "yes") {
			pi.getPlayer().dropMessage(6, "Please kill the Yeti in order to proceed to the next stage.");
			return false;
		}
		else 
		{
			pi.warpParty(nextMap);
			eim.schedule("yetiThree", 100);
			return true;
		}
	} 
	else if (pi.getPlayer().getMapId() == 106021502)
	{
		var nextMap = 106021400;

		var target = eim.getEm().getChannelServer().getMapFactory().getMap(106021400);

		var avail = eim.getProperty("yeti3");
		if (avail != "yes") 
		{
			pi.getPlayer().dropMessage(6, "Please kill the Yeti in order to proceed to the next stage.");
			return false;
		}
		else 
		{
			if(eim.getPlayerCount() == 1) { eim.getEm().disposeInstance("YetiPQ_" + pi.getPlayer().getClient().getChannelServer().getId().toString()); }

			eim.unregisterPlayer(pi.getPlayer());
			pi.getPlayer().changeMap(target, target.getPortal(0));
			return true;
		}
	}
	return true;
}