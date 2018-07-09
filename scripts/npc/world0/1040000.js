/* Author: aaroncsn (MapleSea Like)
	NPC Name: 		Mike
	Map(s): 		Warning Street: Perion Dungeon Entrance(106000300)
	Description: 		Unknown
	*/

var status = -1;

	function start() 
	{
		status = -1;
		action(1, 0, 0);
	}

	function action(mode, type, selection) 
	{
		if(mode == 1)
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
			if (cm.isQuestStarted(28177) && !cm.haveItem(4032479)) 
			{
				cm.gainItem(4032479,1);
			}

			cm.sendNext("Go through here and you'll find the Center Dungeon of Victoria Island. Please be careful...");
			cm.dispose();
		}
	}