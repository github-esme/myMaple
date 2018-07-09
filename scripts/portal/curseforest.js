function enter(pi) {
	if(pi.isQuestStarted(2228))
	{
		pi.warp(910100001,0);
    	return true;
	}
    else
    {
    	pi.warp(910100000,0);
    	return true;
    }
}