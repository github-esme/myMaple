function start()
{
	cm.sendSimple("#b\r\n#L0#Yeti PQ (Requires party of at least 1 people)#l\r\n#L1#Violetta#l#k");
}

function action(mode,type,selection)
{
	if (mode == 1)
	{
		switch(selection)
		{
			case 0:
			if (cm.getPlayer().getParty() == null)
			{
				cm.sendOk("You have to be in a party to do the PQ.");
			}
			else if (!cm.isLeader())
			{
				cm.sendOk("The leader of the party must talk to me.");
			}
			else
			{
				var party = cm.getPlayer().getParty().getMembers();
				var next = true;
				var size = 0;
				var it = party.iterator();
				while (it.hasNext())
				{
					var cPlayer = it.next();
					var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
					if (ccPlayer == null)
					{
						next = false;
						break;
					}
					size += (ccPlayer.isIntern() ? 4 : 1);
				}

				if (next && (cm.getPlayer().isIntern() || size >= 1))
				{
					var em = cm.getEventManager("YetiPQ");

					if (em == null)
					{
						cm.sendOk("This PQ is currently broken. Please report it on the forum!");
						cm.dispose();
						return;
					}

					if (em.getInstance("YetiPQ_" + cm.getPlayer().getClient().getChannelServer().getId().toString()) != null)
					{
						cm.sendOk("Someone is already attempting the PQ. Please wait for them to finish, or find another channel.");
						cm.dispose();
						return;
					}

					em.setProperty("channel", cm.getPlayer().getClient().getChannelServer().getId().toString());
					em.startInstance(cm.getParty(), cm.getPlayer().getMap());

					cm.dispose();
					return;
				}
				else
				{
					cm.sendOk("You must have a party of at least 3 in the map.");
				}
			}
			break;
			
			case 1:
			cm.warp(106021401,0);
			break;
		}
	}
	cm.dispose();
}
