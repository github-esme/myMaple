var status = -1;
var em;
var minLevel = 50; 
var maxLevel = 200;

function start()
{
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection)
{
	if (mode <= 0)
	{
		cm.dispose();
		return;
	}
	else
	{
		status++;
	}

	if (status == 0)
	{
		cm.sendNext("Hi there. I am #b#nMu Young#n#k, the temple Keeper.");
	}
	else if (status == 1)
	{
		em = cm.getEventManager("BalrogPQ");

		if (em == null)
		{
			cm.sendOk("This PQ is currently broken. Please report it on the forum!");
			cm.dispose();
			return;
		}

		if(em.getInstance("BalrogPQ_" + cm.getPlayer().getClient().getChannelServer().getId().toString()) == null)
		{
			var text = "This temple is currently under siege by the Balrog troops. We currently do not know who gave the orders. " +
			"For a few weeks now, the #e#b Order of the Altair#n#k has been sending mercenaries, but they were eliminated every time." +
			" So, traveler, would you like to try your luck at defeating this unspeakable horror? \r\n\r\n " +
			"#L0#Yes. Please register me as party leader\r\n#L1#What is the #eOrder of the Altair?";

			cm.sendSimple(text);
		}
		else
		{
			cm.sendOk("Another party is attempting to defend the temple. Please wait for those brave warriors to finish or change channels!");
			cm.dispose();
		}
	}
	else if (status == 2)
	{
		if (selection == 0)
		{
			if (cm.getPlayer().getParty() == null)
			{
				cm.sendOk("You have to be in a party to do the PQ.");
				cm.dispose();
			}
			else if (!cm.isLeader())
			{
				cm.sendOk("The leader of the party must talk to me.");
				cm.dispose();
			}
			else
			{
				cm.sendSimple("Which Balrog mode would you like to do?\r\n\r\n#L0#Easy Mode (Level 50-70)#l\r\n#L1#Normal Mode (Level 50+)#l");
			}
		}
		else if (selection == 1)
		{
			cm.sendOk("The Order of the Altair is a group of elite mercenaries that oversee the world's economy and battle operations. It was founded 40 years ago right after Black Mage was defeated in hopes of forseeing the next possible attack.");
			cm.dispose();
		}
	}
	else if (status == 3)
	{
		var partyMembers = cm.getPlayer().getParty().getMembers();
		var isInMap = true;

		var iter = partyMembers.iterator();
		while (iter.hasNext())
		{
			var member = iter.next();

			if (member.getMapId() != 105100100)
			{
				isInMap = false;
			}
		}

		if (isInMap)
		{
			if(selection == 0)
			{
				var party = cm.getParty().getMembers();
				var next = true;
				var it = party.iterator();

				while (it.hasNext()) 
				{
					var cPlayer = it.next();
					if ((cPlayer.getLevel() >= 50) && (cPlayer.getLevel() <= 70)) 
					{

					} 
					else 
					{
						next = false;
					}
				}

				if(!next)
				{
					cm.sendOk("All players must be at least level 50-70! Please check your party members.");
					cm.dispose();
					return;
				}

				em.setProperty("channel", cm.getPlayer().getClient().getChannelServer().getId().toString());
				em.setProperty("mode", "easy");
				em.startInstance(cm.getParty(), cm.getPlayer().getMap());

				cm.dispose();
				return;
			}
			else if(selection == 1)
			{
				var party = cm.getParty().getMembers();
				var next = true;
				var it = party.iterator();

				while (it.hasNext()) 
				{
					var cPlayer = it.next();
					if ((cPlayer.getLevel() >= 50) && (cPlayer.getLevel() <= 200)) 
					{

					} 
					else 
					{
						next = false;
					}
				}

				if(!next)
				{
					cm.sendOk("All players must be at least level 50-200! Please check your party members.");
					cm.dispose();
					return;
				}

				em.setProperty("channel", cm.getPlayer().getClient().getChannelServer().getId().toString());
				em.setProperty("mode", "hard");
				em.startInstance(cm.getParty(), cm.getPlayer().getMap());

				cm.dispose();
				return;
			}
		}
		else
		{
			cm.sendOk("All party members must be in the map!");
			cm.dispose();
		}
	}
}