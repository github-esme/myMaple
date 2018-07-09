//Created by zambookii on 6/29/2014

var map = 980000000; //put your map id here.
var minLvl = 30;
var maxLvl = 70;
var minAmt = 1;
var maxAmt = 6;
var disabled = false;

function start()
{
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection)
{
	if (mode == -1) 
	{
		cm.dispose();
	} 
	else 
	{
		if (mode == 0 && status == 0) 
		{
			cm.dispose();
			return;
		}
		if (mode == 1)
		{
			status++;
		}
		else
		{
			status--;
		}

		if (status == 0) 
		{
			if(disabled && !cm.getPlayer().isIntern())
			{
				cm.sendOk("Carnival PQ is under construction.");
				cm.warp(103000000);
				cm.dispose();
				return;
			}

			if (cm.getParty() == null)
			{
				cm.sendOk("#eCreate a party!#k");
				cm.dispose();
			} 
			else if (!cm.isLeader()) 
			{
				cm.sendOk("If you want to try Carnival PQ, please tell the #bleader of your party#k to talk to me.");
				cm.dispose();
			}
			else
			{
				var party = cm.getParty().getMembers();
				var inMap = cm.partyMembersInMap();
				var lvlOk = 0;
				var isInMap = 0;
				for (var i = 0; i < party.size(); i++) 
				{
					if (party.get(i).getLevel() >= minLvl && party.get(i).getLevel() <= maxLvl) 
					{
						lvlOk++;
					}
					if (party.get(i).getMapId() != 980000000)
					{
						//isInMap = false;
						isInMap++
					}
				}	

				if (party >= 1) 
				{
					cm.sendOk("You don't have enough people in your party. You need a party of #b"+minAmt+"#k - #r"+maxAmt+"#k members and they must be in the map with you. There are #b"+inMap+"#k members here.");
					cm.dispose();
				} 
				else if (lvlOk != inMap) 
				{
					cm.sendOk("Make sure everyone in your party is here and is in the right level range of 30-70!");
					cm.dispose();
				} 
				else if (isInMap > 0) 
				{
					cm.sendOk("All members are not in the map!");
					cm.dispose();
				}
				else
				{
					cm.sendCPQMapLists();	
				}
			}
		} 
		else if (status == 1)
		{
			if (cm.fieldTaken(selection))
			{
				if (cm.fieldLobbied(selection))
				{
					cm.challengeParty(selection);
					cm.dispose();
				} 
				else 
				{
					cm.sendOk("The room is taken.");
					cm.dispose();
				}
			} 
			else 
			{
				cm.cpqLobby(selection);
				cm.dispose();
			}
		}
	}
}