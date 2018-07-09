function enter(pi) 
{
	if(pi.isQuestCompleted(2318))
	{
		if(pi.hasItem(2430014))
		{
			pi.openNpc(1300010);
			return false;
		}
		else
		{
			pi.warp(106020400);
			return true;
		}
	}
	else if(pi.hasItem(4000507))
	{
		pi.warp(106020400);
		pi.gainItem(4000507, -1);
		pi.message("You have used a Poison Spore to temporarily penetrate the Mushroom Forest Barrier");
		return true;
	}
	else
	{
		pi.message("You may not enter!");
		return false;
	}
}