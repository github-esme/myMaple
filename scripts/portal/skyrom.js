function enter(pi) {
    if (pi.isQuestStarted(3935) && !pi.hasItem(4031574)) 
    {
		if(pi.canHold(4031574))
		{
			pi.gainItem(4031574);
			pi.message("You have gained an Sky Jewel!");
			return true;
		}
		else
		{
			pi.message("Please make room in your inventory!");
			return false;	
		}
    }
    else
    {
    	return false;
    }
}