function enter(pi) 
{
	if (!pi.haveItem(4000381)) 
	{
		pi.playerMessage(5, "You do not have White Essence.");
		return false;
	} 
	else 
	{
		if (pi.getPlayerCount(541010100) <= 0) 
		{
			var captMap = pi.getMap(541010100);
			pi.playPortalSound();
			pi.warp(541010100, "sp");
			return true;
		} 
		else 
		{
			pi.playerMessage(5, "Someone is already fighting the boss. Please try a different channel.");
			return false;
		}
	}
}