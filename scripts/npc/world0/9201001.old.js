// Nana(H) - Maple Leaf Exchanger

importPackage(Packages.tools);

var status = 0;
var leaf = 4001126;
var chairs = new Array(3010000, 3010001, 3010002, 3010003, 3010004, 3010005, 3010006, 3010007, 3010008, 3010009, 3010010, 3010011, 3010012, 3010013, 3010014, 3010015, 3010016, 3010017, 3010018, 3010019, 3010022, 3010023, 3010024, 3010025, 3010026, 3010028, 3011000, 3010040, 3010041, 3010045, 3012005, 3010046, 3010047, 3010072, 3010057, 3010058, 3010060, 3010061, 3010062, 3010063, 3010064, 3010065, 3010066, 3010067, 3010043, 3010071, 3010085, 3010098, 3010099, 3010073, 3010101, 3010106, 3010111, 3010080, 3010081, 3010082, 3010083, 3010084, 3010092, 3010116, 3010069, 3012010, 3012011);
var weapons = new Array(1302020, 1302030, 1302033, 1302058, 1302064, 1302080, 1312032, 1322054, 1332025, 1332055, 1332056, 1372034, 1382009, 1382012, 1382039, 1402039, 1412011, 1412027, 1422014, 1422029, 1432012, 1432040, 1432046, 1442024, 1442030, 1442051, 1452016, 1452022, 1452045, 1462014, 1462019, 1462040, 1472030, 1472032, 1472055, 1482020, 1482021, 1482022, 1492020, 1492021, 1492022, 1092030, 1092045, 1092046, 1092047);

var sixscrolls = new Array(2040001, 2040004, 2040101, 2040106, 2040201, 2040206, 2040301, 2040317, 2040321, 2040326, 2040401, 2040425, 2040501, 2040504, 2040513, 2040516, 2040532, 2040601, 2040618, 2040621, 2040625, 2040701, 2040704, 2040707, 2040801, 2040824, 2040901, 2040924, 2040927, 2040931, 2041001, 2041004, 2041007, 2041010, 2041013, 2041016, 2041019, 2041022, 2041212, 2043001, 2043017, 2043101, 2043112, 2043201, 2043212, 2043301, 2043701, 2043801, 2044001, 2044012, 2044101, 2044112, 2044201, 2044212, 2044301, 2044312, 2044401, 2044412, 2044501, 2044601, 2044701, 2044801, 2044807, 2044901, 2048001, 2048004, 2048010, 2048011, 2048012, 2048013, 2049100, 2040110, 2040111, 2040112, 2040113, 2040114, 2040115, 2040116, 2040117, 2040118, 2040119, 2041211, 2040826, 2041101, 2041104, 2041110, 2041301, 2041304, 2041307, 2041310, 2043021, 2043116, 2043216, 2043311, 2043711, 2043811, 2044024, 2044116, 2044216, 2044316, 2044416, 2044511, 2044611, 2044816, 2044909, 2040759);
var tenscrolls = new Array(2040002, 2040005, 2040100, 2040105, 2040200, 2040205, 2040302, 2040318, 2040323, 2040328, 2040402, 2040427, 2040502, 2040505, 2040514, 2040517, 2040534, 2040602, 2040619, 2040622, 2040627, 2040702, 2040705, 2040708, 2040727, 2040802, 2040805, 2040825, 2040925, 2040928, 2040933, 2041002, 2041005, 2041008, 2041011, 2041014, 2041017, 2041020, 2041023, 2041058, 2043002, 2043019, 2043102, 2043114, 2043202, 2043214, 2043302, 2043702, 2043802, 2044002, 2044014, 2044102, 2044114, 2044202, 2044214, 2044302, 2044314, 2044402, 2044414, 2044502, 2044602, 2044702, 2044802, 2044809, 2044902, 2048002, 2048005, 2040329, 2040330, 2040331, 2041102, 2041105, 2041108, 2041111, 2041302, 2041305, 2041308, 2041311, 2044015, 2040760);
var sevenscrolls = new Array(2040010, 2040320, 2040325, 2040424, 2040531, 2040624, 2040930, 2043016, 2043111, 2043211, 2044011, 2044111, 2044211, 2044311, 2044411, 2044803, 2044806, 2044903);

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
			var outStr = "Hello, you can exchange your Maple Leaf with me!\r\n\r\n";
			outStr += "#eYou currently have #r#c4001126##k Maple Leaf.#n\r\n\r\n";

			outStr += "#e#L0#Exchange 1 Maple Leaf for #d1,500 NX#k#l\r\n";
			outStr += "#L1#Exchange 1 Maple Leaf for #d1 random chair#k#l\r\n";
			outStr += "#L2#Exchange 1 Maple Leaf for #d1 #v3011000# Fishing Chair#k#l\r\n"
			outStr += "#L11#Exchange 1 Maple Leaf for #d1 #v5030006# Permanent Mushroom House Elf#k#l\r\n"
			outStr += "#L3#Exchange 1 Maple Leaf for #d20 #v2000005# Power Elixirs#k#l\r\n";
			outStr += "#L4#Exchange 1 Maple Leaf for #d10 #v2050004# All Cure Potions#k#l\r\n"
			outStr += "#L16#Exchange 1 Maple Leaf for #d5 #v5450000# Miu Miu the Traveling Merchant#k#l\r\n"
			outStr += "#L10#Exchange 1 Maple Leaf for #d1 #v5050000# AP Reset#k#l\r\n"
			outStr += "#L12#Exchange 1 Maple Leaf for #d2 #v5050001# SP Reset (1st Job)#k#l\r\n";
			outStr += "#L13#Exchange 1 Maple Leaf for #d2 #v5050002# SP Reset (2nd Job)#k#l\r\n";
			outStr += "#L14#Exchange 1 Maple Leaf for #d2 #v5050003# SP Reset (3rd Job)#k#l\r\n";
			outStr += "#L15#Exchange 1 Maple Leaf for #d2 #v5050004# SP Reset (4th Job)#k#l\r\n";
			outStr += "#L8#Exchange 7 Maple Leaf for #d30 minutes #v5211048# 2X EXP card#k#l\r\n"
			outStr += "#L9#Exchange 13 Maple Leaf for #d1 hour #v5211048# 2X EXP card#k#l#n\r\n"
			cm.sendSimple(outStr);
		} 
		else if (status == 1) 
		{
			if (selection == 0) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					cm.getPlayer().getCashShop().gainCash(1, 1500);
					cm.getPlayer().announce(MaplePacketCreator.earnTitleMessage("You have received 1,500 NX."));
					cm.gainItem(leaf, -1);
					cm.sendOk("Here is your 1,500 NX!");
					cm.logLeaf("1,500 NX");
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			} 
			else if (selection == 1) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					var chair1 = chairs[Math.floor(Math.random()*chairs.length)];
					if(cm.canHold(chair1))
					{
						cm.gainItem(chair1);
						cm.gainItem(leaf, -1);
						cm.sendOk("Here is your random chair!");
						cm.logLeaf("Random Chair: " + chair1);
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold this chair!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			} 
			else if (selection == 2) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					if(cm.canHold(3011000))
					{
						cm.gainItem(3011000);
						cm.gainItem(leaf, -1);
						cm.sendOk("Here is your Fishing Chair!");
						cm.logLeaf("Fishing Chair");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold the Fishing Chair!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			} 
			else if (selection == 3) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					if(cm.canHold(2000005)) 
					{
						cm.gainItem(2000005, 20);
						cm.gainItem(leaf, -1);
						cm.sendOk("Here are your 20 Power Elixirs!");
						cm.logLeaf("Power Elixirs");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold these Power Elixirs!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			} 
			else if (selection == 4) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					if(cm.canHold(2050004)) 
					{
						cm.gainItem(2050004, 10);
						cm.gainItem(leaf, -1);
						cm.sendOk("Here are your 10 All Cure Potions!");
						cm.logLeaf("All Cure Potions");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold these All Cure Potions!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			} 
			else if (selection == 8) 
			{
				if(cm.haveItem(leaf, 7)) 
				{
					if(cm.canHold(5211048)) 
					{
						cm.gainItem(5211048, 1, false, false, 1800000);
						cm.gainItem(leaf, -7);
						cm.getPlayer().setRates();
						cm.sendOk("Here is your 30 minutes EXP card!");
						cm.logLeaf("30 Minutes EXP Card");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold this EXP card!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf");
				}

				cm.dispose();
			} 
			else if (selection == 9) 
			{
				if(cm.haveItem(leaf, 13)) 
				{
					if(cm.canHold(5211048)) 
					{
						cm.gainItem(5211048, 1, false, false, 3600000);
						cm.gainItem(leaf, -13);
						cm.getPlayer().setRates();
						cm.sendOk("Here is your 1 hour EXP card!");
						cm.logLeaf("1 Hour EXP Card");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold this EXP card!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf");
				}

				cm.dispose();
			} 
			else if (selection == 10) 
			{
				cm.sendGetNumber("How many AP Resets would you like to buy?\r\n\r\n#eYou currently have #r#c4001126##k Maple Leaf.#n", 1, 1, cm.itemQuantity(4001126));
			} 
			else if (selection == 11) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					if(cm.canHold(5030006)) 
					{
						cm.gainItem(5030006);
						cm.gainItem(leaf, -1);
						cm.sendOk("Here is your Permanent Day Mushroom House Elf!");
						cm.logLeaf("Permanent Day Mushroom House Elf");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold this 30 Day Mushroom House Elf!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			} 
			else if (selection == 12) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					if(cm.canHold(5050001)) 
					{
						cm.gainItem(5050001, 2);
						cm.gainItem(leaf, -1);
						cm.sendOk("Here are your SP Reset (1st Job)!");
						cm.logLeaf("SP Reset (1st Job)");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold these SP Reset (1st Job)!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			} 
			else if (selection == 13) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					if(cm.canHold(5050002)) 
					{
						cm.gainItem(5050002, 2);
						cm.gainItem(leaf, -1);
						cm.sendOk("Here are your SP Reset (2nd Job)!");
						cm.logLeaf("SP Reset (2nd Job)");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold these SP Reset (2nd Job)!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			} 
			else if (selection == 14) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					if(cm.canHold(5050003)) 
					{
						cm.gainItem(5050003, 2);
						cm.gainItem(leaf, -1);
						cm.sendOk("Here are your SP Reset (3rd Job)!");
						cm.logLeaf("SP Reset (3rd Job)");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold these SP Reset (3rd Job)!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			} 
			else if (selection == 15) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					if(cm.canHold(5050004)) 
					{
						cm.gainItem(5050004, 2);
						cm.gainItem(leaf, -1);
						cm.sendOk("Here are your SP Reset (4th Job)!");
						cm.logLeaf("SP Reset (4th Job)");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold these SP Reset (4th Job)!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			} 
			else if (selection == 16) 
			{
				if(cm.haveItem(leaf, 1)) 
				{
					if(cm.canHold(5450000)) 
					{
						cm.gainItem(5450000, 5);
						cm.gainItem(leaf, -1);
						cm.sendOk("Here are your Miu Miu the Traveling Merchant!");
						cm.logLeaf("Miu Miu the Traveling Merchant");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold these Miu Miu the Traveling Merchant!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough Maple Leaf!");
				}

				cm.dispose();
			}
		}
		else if (status == 2)
		{
			if(cm.haveItem(leaf, selection)) 
			{
				if(cm.canHold(5050000, selection)) 
				{
					cm.gainItem(5050000, selection);
					cm.gainItem(leaf, -selection);
					cm.sendOk("Here is your " + selection + " AP Reset!");
					cm.logLeaf(selection + " AP Reset");
				} 
				else 
				{
					cm.sendOk("Please make sure you have enough space to hold " + selection + " AP Reset!");
				}
			} 
			else 
			{
				cm.sendOk("Sorry, you don't have enough Maple Leaf!");
			}

			cm.dispose();
		}
	}
}