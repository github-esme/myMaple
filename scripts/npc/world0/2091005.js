var disabled = false;
var belts = Array(1132000, 1132001, 1132002, 1132003, 1132004, 1132036);
var belt_points = Array(1000, 4000, 6000, 9000, 12000, 20000);

var accessories = Array(1032080, 1122081, 1112435);
var weapons = Array(1092070, 1092075, 1092080, 1302143, 1312058, 1322086, 1332116, 1332121, 1342029, 1372074, 1382095, 1402086, 1412058, 1422059, 1432077, 1442107, 1452102, 1462087, 1472113, 1482075, 1492075);

var beltBefore;
var belt;
var points;

var status = -1;

function start() 
{
	action(1, 0, 0);
}

function action(mode, type, selection)
{
	if (mode != 1)
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
		if (isRestingSpot(cm.getPlayer().getMap().getId())) 
		{
			var text = "I'm surprised you made it this far! But it won't be easy from here on out. You still want the challenge?\r\n\r\n#e#b#L0#I want to continue#l\r\n#L1#I want to leave#l#n\r\n";

			if (!cm.getPlayer().getDojoParty()) 
			{
				text += "#e#L2#I want to save at this point#l#n";
			}

			cm.sendSimple(text);
		}
		else if (cm.getPlayer().getLevel() >= 25) 
		{
			if (cm.getPlayer().getMap().getId() == 925020001)
			{
				var outStr = "My master is the strongest person in Mu Lung, and you want to challenge him? Fine, but you'll regret it later.\r\n\r\n";
				outStr += "#e#b#L0#I want to challenge him alone#l\r\n";
				outStr += "#L1#I want to challenge him with a party#l\r\n\r\n";
				outStr += "#L7#Fastest solo time ranking (seconds)#l\r\n\r\n";
				outStr += "#L2#I want to receive a belt#l\r\n";
				outStr += "#L3#I want to receive a random Unwelcome Guest accessory#l\r\n";
				outStr += "#L4#I want to choose a Unwelcome Guest weapon#l\r\n";
				outStr += "#L5#I want to receive a medal#l\r\n";
				outStr += "#L6#What is a Mu Lung Dojo?#l#n";

				status = 2;
				cm.sendSimple(outStr);
			} 
			else 
			{
				status = 1;
				cm.sendYesNo("What, you're giving up? You just need to get to the next level! Do you really want to quit and leave?");
			}
		} 
		else 
		{
			cm.sendOk("Hey! Are you mocking my master? Who do you think you are to challenge him? This is a joke! You should at least be level #b25#k.");
			cm.dispose();
		}
	}
	else if (status == 1) // Leave resting spot
	{
		if (selection == 0)
		{
			if (cm.getPlayer().getShouldRecordingDojoTime())
			{
				cm.getPlayer().runDojoTimer(cm.getPlayer().getMap().getId() + 100);	
			}

			cm.warp(cm.getPlayer().getMap().getId() + 100, 0);
			cm.dispose();
		}
		else if (selection == 1)
		{
			cm.warp(925020002);
			cm.dispose();
		}
		else if (selection == 2)
		{
			status = 11;

			cm.sendYesNo("If you save a point, you can start where you left off the next time. Isn't that convenient? Do you want to save?");
		}
	}
	else if (status == 2) // Leave in the middle of dojo
	{
		cm.warp(925020002);
		cm.dispose();
	}
	else if (status == 3)
	{
		for (var i = 1 ; i < 39; i++) 
		{
			if(cm.getClient().getChannelServer().getMapFactory().getMap(925020000 + 100 * i).getCharacters().size() > 0) 
			{
				cm.sendOk("Someone is already in the Dojo." + i);
				cm.dispose();
				return;
			}
		}

		for (var i = 1 ; i < 39; i++) 
		{
			cm.getClient().getChannelServer().getMapFactory().getMap(925020000 + 100 * i).resetReactors();
			cm.getClient().getChannelServer().getMapFactory().getMap(925020000 + 100 * i).killAllMonsters();
		}

		if (selection == 0) // Challenge solo
		{
			if (!cm.getPlayer().hasEntered("dojang_Msg") && !cm.getPlayer().getFinishedDojoTutorial()) 
			{
				cm.sendYesNo("Hey there! You! This is your first time, huh? Well, my master doesn't just meet with anyone. He's a busy man. And judging by your looks, I don't think he'd bother. Ha! But, today's your lucky day... I tell you what, if you can defeat me, I'll allow you to see my Master. So what do you say?");
			}
			else if (cm.getPlayer().getDojoStage() > 0)
			{
				status = 4;

				cm.sendYesNo("The last time you took the challenge by yourself, you went up to level " + cm.getPlayer().getDojoStage() + ". I can take you there right now. Do you want to go there?");
			}
			else
			{
				cm.getPlayer().setDojoParty(false);
				cm.getPlayer().setShouldRecordingDojoTime(true);
				cm.getPlayer().runDojoTimer(925020100);
				cm.warp(925020100, 0);
				cm.dispose();
			}
		}
		else if (selection == 1) // Challenge party
		{
			var party = cm.getPlayer().getParty();

			if (party == null) 
			{
				cm.sendOk("Where do you think you're going? You're not even the party leader! Go tell your party leader to talk to me.");
				cm.dispose();
				return;
			}

			var lowest = cm.getPlayer().getLevel();
			var highest = lowest;
			for (var x = 0; x < party.getMembers().size(); x++) 
			{
				var lvl = party.getMembers().get(x).getLevel();

				if (lvl > highest)
					highest = lvl;
				else if (lvl < lowest)
					lowest = lvl;
			}

			var isBetween30 = highest - lowest <= 30;

			if (party.getLeader().getId() != cm.getPlayer().getId()) 
			{
				cm.sendOk("Where do you think you're going? You're not even the party leader! Go tell your party leader to talk to me.");
				cm.dispose();
			} 
			else if (party.getMembers().size() == 1) 
			{
				cm.sendOk("You're going to take on the challenge as a one-man party?");
				cm.dispose();
			} 
			else if (!isBetween30) 
			{
				cm.sendOk("Your partys level ranges are too broad to enter. Please make sure all of your party members are within #r30 levels#k of each other.");
				cm.dispose();
			} 
			else 
			{
				cm.getPlayer().setShouldRecordingDojoTime(true);
				cm.warpParty(925020100);
				cm.dispose();
			}

			cm.dispose();
		}
        else if (selection == 2) // Receive belt
        {
        	var selStr = "You have #b" + cm.getPlayer().getDojoPoints() + "#k training points. Master prefers those with great talent. If you obtain more points than the average, you can receive a belt depending on your score.\r\n";

        	for (var i = 0; i < belts.length; i++) 
        	{
        		if (cm.getPlayer().getItemQuantity(belts[i], true) > 0) 
        		{
        			selStr += "\r\n#L" + i + "##i" + belts[i] + "# #t" + belts[i] + "# (Obtained)#l";
        		} 
        		else
        			selStr += "\r\n#L" + i + "##i" + belts[i] + "# #t" + belts[i] + "##l";
        	}

        	status = 5;

        	cm.sendSimple(selStr);
        }
        else if (selection == 3) // Receive UG accessory
        {
        	status = 7;

        	cm.sendYesNo("A random Unwelcome Guest accessory will cost you #e15,000 training points#n. You will get a UG Earring, UG Pendant, or UG Ring randomly. Would you like to continue?");
        }
        else if (selection == 4) // Receive UG weapon
        {
        	status = 8;

        	var outStr = "";

			for(var i = 0; i < weapons.length; i++)
			{
				outStr += "#L" + i + "##v" + weapons[i] + "# #t" + weapons[i] + "##l\r\n";
			}
			
			cm.sendSimple("A Unwelcome Guest weapon will cost you #e5,000 training points#n. What weapon would you like?\r\n\r\n" + outStr);
        }
        else if (selection == 5) // Receive medal
        {
        	status = 9;
        	var medal = 1142032 + cm.getPlayer().getVanquisherStage();

        	if (cm.getPlayer().getVanquisherStage() <= 0) 
        	{
        		medal++;
        		cm.sendYesNo("You haven't attempted the medal yet? If you defeat one type of monster in Mu Lung Dojo #b100 times#k you can receive a title called #v" + medal + "# #b#t" + medal + "##k. It looks like you haven't even earned the #b#t" + medal + "##k... Do you want to try out for the #b#t" + medal + "##k?");
        	}
        	else if (medal > 1142064)
        	{
        		cm.sendOk("You have complete all medals challenges. There are no more medals for you to get.");
        		cm.dispose();
        	}
        	else if (medal == 1142064)
        	{
        		if (cm.getPlayer().getVanquisherKills() < 100 || cm.getPlayer().getDojoPoints() < 500000)
        		{
        			cm.sendOk("You do not fulfill the requirements to receive the #b#t" + medal + "##k.\r\n\r\n#eYou currently have:\r\n" + cm.getPlayer().getVanquisherKills() + " Boss Kills #b(100 required)#k\r\n" + cm.getPlayer().getDojoPoints() + " Training Points #b(500,000 Required)#k#n\r\n\r\nPlease try a little harder. As a reminder, only the mosnters that have been summoned by our Master in Mu Lung Dojo are considered. Oh, and make sure you're not hunting the monsters and exiting!#r If you don't go to the next level after defeating the monster, it doesn't count as a win#k.");
        			cm.dispose();
        		}
        	} 
        	else if (cm.getPlayer().getVanquisherKills() < 100 && cm.getPlayer().getVanquisherStage() > 0)
        	{
        		cm.sendOk("You do not fulfill the requirements to receive the #b#t" + medal + "##k.\r\n\r\n#eYou currently have:\r\n" + cm.getPlayer().getVanquisherKills() + " Boss Kills #b(100 required)#k#n\r\n\r\nPlease try a little harder. As a reminder, only the mosnters that have been summoned by our Master in Mu Lung Dojo are considered. Oh, and make sure you're not hunting the monsters and exiting!#r If you don't go to the next level after defeating the monster, it doesn't count as a win#k.");
        		cm.dispose();
        	}
        	else
        	{
        		status = 10;

        		cm.sendYesNo("You completed the mission for the #v" + medal + "# #b#t" + medal + "##k. Would you like one?");
        	}
        }
        else if (selection == 6) // What is dojo
        {
        	cm.sendOk("Our master is the strongest person in Mu Lung. The place he built is called the Mu Lung Dojo, a building that is 38 stories tall! You can train yourself as you go up each level. Of course, it'll be hard for someone at your level to reach the top.");
        	cm.dispose();
        }
        else if (selection == 7)
        {
        	cm.getPlayer().displayDojoRanks();
        	cm.dispose();
        }
    }
	else if (status == 4) // Dojo tutorial
	{
		if(cm.getClient().getChannelServer().getMapFactory().getMap(925020010).getCharacters().size() > 0) 
		{
			cm.sendOk("Someone is already in Dojo");
			cm.dispose();
			return;
		}

		cm.getPlayer().setDojoParty(false);
		cm.warp(925020010, 0);
		cm.getPlayer().setFinishedDojoTutorial();
		cm.dispose();
	}
	else if (status == 5) // Resume dojo
	{
		cm.getPlayer().setDojoParty(false);
		cm.getPlayer().setShouldRecordingDojoTime(false);
		
		cm.warp(mode == 1 ? 925020000 + cm.getPlayer().getDojoStage() * 100 : 925020100, 0);

		cm.getPlayer().setDojoStage(0);

		cm.dispose();
	}
	else if (status == 6) // Receive belt
	{
		belt = belts[selection];
		points = belt_points[selection];

		if (cm.getPlayer().getItemQuantity(belt, true) > 0) 
		{
			cm.sendOk("You already have this belt!");
			cm.dispose();
			return;
		}

		if (belt == 1132000)
		{
			cm.sendYesNo("In order to receive a #v" + belt + "# #t" + belt + "#, you will need #e" + points + " training points#n. Would you like one?");
		}
		else
		{
			beltBefore = belts[selection -1];
			cm.sendYesNo("In order to receive a #v" + belt + "# #t" + belt + "#, you will need #e" + points + " training points and a #v" + beltBefore + "# #t" + beltBefore + "##n. Would you like one?")
		}
	}
	else if (status == 7)
	{
		if (belt == 1132000)
		{
			if (cm.getPlayer().getDojoPoints() > points) 
			{
				if (cm.canHold(belt))
				{
					cm.gainItem(belt);
					cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - points);

					cm.sendOk("You have earned the #v" + belt + "# #t" + belt + "#!");
				}
				else
					cm.sendOk("Please make make room in your inventory!");
			} 
			else
				cm.sendOk("You do not have enough training points!");
		}
		else
		{
			if (cm.getPlayer().getDojoPoints() >= points && cm.hasItem(beltBefore))
			{
				if (cm.canHold(belt))
				{
					cm.gainItem(beltBefore, -1);

					if (belt == 1132036)
						cm.gainItem(belt, 1, true, false);
					else
						cm.gainItem(belt);

					cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - points);

					cm.sendOk("You have earned the #v" + belt + "# #t" + belt + "#!");
				}
				else
					cm.sendOk("Please make room in your inventory!");
			}
			else
				cm.sendOk("You either do not have enough training points or do not a #v" + beltBefore + "# #t" + beltBefore + "#!");
		}

		cm.dispose();
	}
	else if (status == 8) // Receive UG accessory
	{
		if (cm.getPlayer().getDojoPoints() >= 15000)
		{
			var item = accessories[Math.floor(Math.random()*accessories.length)];

			if (cm.canHold(item))
			{
				cm.gainItem(item, 1, true, false);
				cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - 15000);

				cm.sendOk("You have received a #v" + item + "# #t" + item + "#!");
				cm.dispose();
			}
			else
			{
				cm.sendOk("Please make room in your inventory!");
				cm.dispose();
			}
		}
		else
		{
			cm.sendOk("You do not have enough training points!");
			cm.dispose();
		}
	}
	else if (status == 9) // Receive UG weapon
	{
		if (cm.getPlayer().getDojoPoints() >= 5000)
		{
			//var item = weapons[Math.floor(Math.random()*weapons.length)];

			if (cm.canHold(weapons[selection]))
			{
				cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - 5000);
				cm.gainItem(weapons[selection], 1, true, false);

				cm.sendOk("You have received a #v" + weapons[selection] + "# #t" + weapons[selection] + "#!");
				cm.dispose();
			}
			else
			{
				cm.sendOk("Please make room in your inventory!");
				cm.dispose();
			}
		}
		else
		{
			cm.sendOk("You do not have enough training points!");
			cm.dispose();
		}
	}
	else if (status == 10) // Vanquisher
	{
		cm.getPlayer().setVanquisherStage(1);

		var medal = 1142032 + cm.getPlayer().getVanquisherStage();

		cm.sendOk("Please kill 100 of the boss to receive the #v" + medal + "# #t" + medal + "#!");
		cm.dispose();
	}
	else if (status == 11) // Receive medal
	{
		var medal = 1142032 + cm.getPlayer().getVanquisherStage();

		if (cm.canHold(medal))
		{
			if (medal == 1142064)
			{
				cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - 500000);
			}

			cm.gainItem(medal);

			cm.sendOk("You have obtained #b#t" + medal + "##k.");

			cm.getPlayer().setVanquisherStage(cm.getPlayer().getVanquisherStage() + 1);
			cm.getPlayer().setVanquisherKills(0);

			cm.dispose();
		}
		else
		{
			cm.sendOk("Please make room in your inventory!");
			cm.dispose();
		}
	}
	else if (status == 12) // Leave record dojo stage
	{
		if (925020000 + cm.getPlayer().getDojoStage() * 100 == cm.getMapId()) 
		{
			cm.sendOk("Looks like you came all the way up here without recording your score. Sorry, but you can't record now.");
		} 
		else 
		{
			cm.sendOk("I recorded your score. If you tell me the next time you go up, you'll be able to start where you left off.");
			cm.getPlayer().setDojoStage((cm.getMapId() - 925020000) / 100);
		}

		cm.dispose();
	}
}

function isRestingSpot(id) 
{
	return (id / 100 - 9250200) % 6 == 0;
}