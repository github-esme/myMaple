// Housing

var housing = new Packages.server.events.Housing;

var password;

var openHouses;
var redeemHouses;
var enterHouses;

var selectedMap;
var selectedHouse;
var selectedEnterHouse;

var status;

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function start()
{
	status = -1;

	var allHouses = housing.getAllHousingMaps();

	if (allHouses.size() > 0)
	{
		for (var i = 0; i < allHouses.size(); i++)
		{
			var mapID = allHouses.get(i);

			if (housing.failToRedeem(mapID) || housing.houseExpired(mapID))
			{
				housing.resetHouse(mapID);
			}
		}
	}

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
		var outStr = "Hi there! I'm the real estate agent for myMaple. What would you like to do?\r\n\r\n";
		outStr += "#e#b#L0#Enter a house#l\r\n\r\n";
		outStr += "#L1#View/bid available house(s)#l\r\n";
		outStr += "#L4#Tour the available house(s)#l\r\n";
		outStr += "#L2#Redeem my house(s)#l\r\n\r\n";
		outStr += "#L3#What is the Housing System?#l#k#n#l"
		
		cm.sendSimple(outStr);
	}
	else if (status == 1)
	{
		if (selection == 0)
		{
			enterHouses = housing.getAllOwnedHousingMaps();

			if (enterHouses.size() == 0)
			{
				cm.sendOk("There are no houses.");
				cm.dispose();
			}
			else
			{
				var outStr = "Please choose a house you want to enter.\r\n\r\n";

				for (var i = 0; i < enterHouses.size(); i++)
				{
					var mapID = enterHouses.get(i);
					var houseName = housing.getHouseName(mapID);

					outStr += "#e#bHouse Name: #d" + houseName + "#k#n\r\n";

					outStr += "#e#bHouse Owner: #k#n" + housing.getOwner(mapID) + "\r\n";
					outStr += "#e#bHouse Expires On: #k#n" + housing.getEndingHouseDate(mapID) + " EST\r\n";

					outStr += "#L" + i + "##eEnter this house#n#l\r\n\r\n\r\n\r\n";
				}

				status = 6;

				cm.sendSimple(outStr);
			}
		}
		else if (selection == 1)
		{
			openHouses = housing.getAllOpenHousingMaps();

			if (openHouses.size() == 0)
			{
				cm.sendOk("There are no available houses to purchase! #eYou can suggest more maps to be used as a house by contacting a staff.#n");
				cm.dispose();
				return;
			}
			else
			{
				var outStr = "Below are the available houses that are currently available to bid. Choose the map that you wish to bid. #eYou can suggest more maps to be used as a house by contacting a staff.#n\r\n\r\n";

				for (var i = 0; i < openHouses.size(); i++)
				{
					var mapID = openHouses.get(i);
					var endingTimestamp = housing.getEndingBetTimestamp(mapID);
					var currentTimestamp = new Date().getTime();

					var timestampDiff = endingTimestamp - currentTimestamp;

					outStr += "#e#d" + mapID + " - #m" + mapID + "##k#n\r\n";

					if (housing.getCurrentBetName(mapID) == "")
					{
						var minMesos = housing.getMinMesos(mapID);

						outStr += "#eStatus: #n Betting not started. You can start the bet.\r\n";
						outStr += "#eMin Starting Bet: #n " + numberWithCommas(minMesos) + " Mesos\r\n";
					}
					else
					{
						outStr += "#e#bCurrent Bet: #k#n" + numberWithCommas(housing.getCurrentBet(mapID)) + " Mesos\r\n";
						outStr += "#e#bLast Bet By: #k#n" + housing.getCurrentBetName(mapID) + "\r\n";
						outStr += "#e#bBet Ends In: #k#n" + housing.msToString(timestampDiff) + "\r\n";
					}

					outStr += "#L" + i + "##eBid on this house#n#l\r\n\r\n\r\n\r\n";
				}

				cm.sendSimple(outStr);
			}
		}
		else if (selection == 2)
		{
			redeemHouses = housing.getAllRedeemAvailableMaps(cm.getPlayer().getName());

			if (redeemHouses.size() == 0)
			{
				cm.sendOk("You don't have any houses to redeem! You can redeem your house if you are the highest bidder 24 hours after when the bid has been initialized.");
				cm.dispose();
				return;
			}
			else
			{
				var outStr = "Which house would you like to redeem?\r\n#eFailure to redeem your house 24 hours after winning the bid for your house will cause you to lose your house.#n\r\n\r\n";
				
				for (var i = 0; i < redeemHouses.size(); i++)
				{
					var mapID = redeemHouses.get(i);

					outStr += "#L" + i + "##e#d" + mapID + " - #m" + mapID + "##k#n#l\r\n";
				}

				status = 3;

				cm.sendSimple(outStr);
			}
		}
		else if (selection == 3)
		{
			cm.sendOk("To buy a house, you would need to start or participate in a bidding for a house. The bid lasts for 24 hours after it is initialized by someone. If you have been outbid, you will get a notification if you are online. If you have the highest bid after 24 hours the bid has been initialized, you have to redeem your house within 24 hours or else you will lose the house. After you redeem your house, you get to own the house for two weeks and then it'll be placed back on bid again. A house is a special map that you can control such as kick players out of your house, set a house password, and set your own house name. You can even fish in your house. No one will be able to enter your house without knowing the house password that you set.");
		}
		else if (selection == 4)
		{
			openHouses = housing.getAllOpenHousingMaps();

			if (openHouses.size() == 0)
			{
				cm.sendOk("There are no available houses to tour! #eYou can suggest more maps to be used as a house by contacting a staff.#n");
				cm.dispose();
				return;
			}
			else
			{
				var outStr = "Select which house you want to tour. #e#bClick on the Gaga NPC in the house to leave the map. #kYou can suggest more maps by contacting a staff.#n\r\n\r\n";

				for (var i = 0; i < openHouses.size(); i++)
				{
					var mapID = openHouses.get(i);

					outStr += "#L" + i + "##e#d" + mapID + " - #m" + mapID + "##k#n#l\r\n\r\n";
				}

				status = 8;

				cm.sendSimple(outStr);
			}
		}
	}
	else if (status == 2) // Bid house
	{
		selectedMap = openHouses.get(selection);

		if (housing.getCurrentBetName(selectedMap) == "")
		{
			var minMesos = housing.getMinMesos(selectedMap);

			cm.sendGetNumber("How much mesos do you want to bid?\r\n#eSince this house hasn't been bid, the minimum meso to start the bid is: " + numberWithCommas(minMesos) + " mesos.#n\r\n\r\n#bYou currently have " + cm.getMeso() + " mesos.#k", minMesos, minMesos, cm.getMeso());
		}
		else
		{
			var lastBetMesos = housing.getCurrentBet(selectedMap);

			cm.sendGetNumber("How much mesos do you want to bid?\r\n#eThe last bid was: " + numberWithCommas(lastBetMesos) + " mesos.#n\r\n\r\n#bYou currently have " + cm.getMeso() + " mesos.#k", lastBetMesos + 1, lastBetMesos, cm.getMeso());
		}
	}
	else if (status == 3)
	{
		if (housing.getCurrentBetName(selectedMap) == "")
		{
			var minMesos = housing.getMinMesos(selectedMap);

			housing.makeBet(selectedMap, selection, cm.getPlayer().getName());
			housing.startBetTime(selectedMap);

			var mapName = cm.getPlayer().getClient().getChannelServer().getMapFactory().getMap(selectedMap).getMapName();

			Packages.net.server.Server.getInstance().broadcastMessage(Packages.tools.MaplePacketCreator.serverNotice(6, "[Housing] " + cm.getPlayer().getName() + " has started a bidding of the house: " + selectedMap + " - " + mapName + " for " + selection + " mesos!"));
			
			cm.sendOk("You have successfully started a bet of " + selection + " mesos!");
			cm.dispose();
			return;
		}
		else
		{
			var lastBetMesos = housing.getCurrentBet(selectedMap);
			var lastBetName = housing.getCurrentBetName(selectedMap);

			if (selection > lastBetMesos)
			{
				housing.makeBet(selectedMap, selection, cm.getPlayer().getName());

				var mapName = cm.getPlayer().getClient().getChannelServer().getMapFactory().getMap(selectedMap).getMapName();

				var target = cm.getClient().getWorldServer().getPlayerStorage().getCharacterByName(lastBetName);
				target.message(cm.getPlayer() + " has outbid you for the house: " + selectedMap + " - " + mapName + " by " + numberWithCommas(selection) + " mesos!");

				cm.sendOk("You have successfully placed a bet of " + numberWithCommas(selection) + " mesos!");
				cm.dispose();
				return;
			}
			else
			{
				cm.sendOk("You need to bet mesos higher than " + numberWithCommas(lastBetMesos) + " mesos!");
				cm.dispose();
				return;
			}
		}
	}
	else if (status == 4) // Redeem house
	{
		selectedHouse = redeemHouses.get(selection);

		var currentBet = housing.getCurrentBet(selectedHouse);

		if (cm.getPlayer().getMeso() > currentBet)
		{
			selectedHouse = redeemHouses.get(selection);

			cm.sendGetText("What would you like to set your house password as?\r\n#eMax Characters: 50#n");
		}
		else
		{
			cm.sendOk("You do not have enough mesos to redeem your house!");
			cm.dispose();
			return;
		}
	}
	else if (status == 5)
	{
		password = cm.getText();

		if (password.length > 50)
		{
			cm.sendOk("Please enter a password less than 50 characters!");
			cm.dispose();
			return;
		}
		else if (password == "")
		{
			cm.sendOk("Please enter a password!");
			cm.dispose();
			return;
		}
		else
		{
			cm.sendGetText("What would your house name to be?\r\n#eMax Characters: 50#n");
		}
	}
	else if (status == 6)
	{
		var houseName = cm.getText();

		if (houseName.length > 50)
		{
			cm.sendOk("Please enter a house name less than 50 characters!");
			cm.dispose();
			return;
		}
		else if (houseName == "")
		{
			cm.sendOk("Please enter a house name!");
			cm.dispose();
			return;
		}
		else
		{
			var currentBet = housing.getCurrentBet(selectedHouse);

			cm.gainMeso(-currentBet);

			housing.redeemHouse(selectedHouse, cm.getPlayer().getName(), houseName, password);

			Packages.net.server.Server.getInstance().broadcastMessage(Packages.tools.MaplePacketCreator.serverNotice(6, "[Housing] " + cm.getPlayer().getName() + " redeemed their house called: " + houseName));

			cm.sendOk("You have successfully redeemed your house! Your house will reset after two weeks!");
			cm.dispose();
			return;
		}
	}
	else if (status == 7) // Enter house
	{
		selectedEnterHouse = enterHouses.get(selection);
		cm.sendGetText("Please enter the house password.");
	}
	else if (status == 8)
	{
		var housePass = housing.getPassword(selectedEnterHouse);
		var inputtedPass = cm.getText();

		if (inputtedPass == housePass)
		{
			cm.warp(selectedEnterHouse);
			cm.dispose();
			return;
		}
		else
		{
			cm.sendOk("Incorrect password!");
			cm.dispose();
			return;
		}
	}
	else if (status == 9) // Tour house
	{
		cm.warp(openHouses.get(selection));
		cm.dispose();	
		return;
	}
}