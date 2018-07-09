var status;
var ranScrolls = new Array(2040704, 2040705, 2040707, 2040708);

function start()
{
	status = -1;
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
		if(cm.isQuestStarted(3239) && cm.getPlayer().getMap().getId() != 922000000)
		{
			var map = cm.getClient().getChannelServer().getMapFactory().getMap(922000000);
			map.resetReactors();
			cm.getPlayer().changeMap(map, map.getPortal(0));

			cm.dispose();
		}
		else if(cm.isQuestStarted(3239) && cm.getPlayer().getMap().getId() == 922000000)
		{
			if (cm.hasItem(4031092, 10))
			{
				var scroll = ranScrolls[Math.floor(Math.random()*ranScrolls.length)];

				cm.gainItem(4031092, -10);
				cm.gainItem(scroll);
				cm.completeQuest(3239);
				cm.gainExp(2700);

				cm.sendOk("Thank you. The quest has been complete!");
			}
			else
			{
				cm.sendOk("You do not have 10 #v4031092# #t4031092#!");
				cm.dispose();
			}
		}
		else if(cm.isQuestCompleted(3239))
		{
			cm.sendOk("Thanks to you, the Toy Factory is running smoothly again. I'm so glad you came to help us out. We've been keeping an extra eye on all of our partys, so don't worry about it. Well then, I need to get back to work!");
		}
		else
		{
			var map = cm.getClient().getChannelServer().getMapFactory().getMap(220020000);
			cm.getPlayer().changeMap(map, map.getPortal(0));
			
			cm.dispose();
		}
	}
	else if (status == 1)
	{
		var map = cm.getClient().getChannelServer().getMapFactory().getMap(220020000);
		cm.getPlayer().changeMap(map, map.getPortal(0));

		cm.dispose();
	}
}