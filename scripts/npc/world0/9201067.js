var status;
var choice;

var inventoryItems = new Array(new Array(50, 100));

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
		cm.sendSimple("Hi there! I am the NPC that does inventory stuff for you. Be careful what you choose because you can't be refunded if you misclick.\r\n\r\n#e#b#L1#Remove Specific Item From Inventory#l#k#n");
	}
	else if (status == 1)
	{
		if (selection == 0)
		{
			if (!cm.getPlayer().isIntern())
			{
				cm.sendOk("Inventory sorter is under construction");
				cm.dispose();
				return;
			}

			cm.sendSimple("Which inventory type would you like to sort? #eYou may receive lag for a few seconds during the sorting process.#n\r\n\r\n#e#b#L1#USE#l\r\n#L2#ETC#l#k#n");
		}
		else
		{
			status = 2;
			action(1, 0, 0);
		}
	}
	else if (status == 2)
	{
		if (selection == 0) // EQUIP
		{
			var slotList = cm.EquipSlotList(cm.getClient());

			for (var i = 0; i < slotList.size(); i++)
			{
				var equip = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getEquip(slotList.get(i));

				cm.gainItem(equip.getItemId(), -1);
				cm.gainEquip(equip, false);
			}
		}
		else if (selection == 1) // USE
		{
			var inventory = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.USE);

			var iter = inventory.iterator();

			while (iter.hasNext())
			{
				var itemId = iter.next().getItemId();
				var itemArray = new Array(itemId, cm.getPlayer().getItemQuantity(itemId, false));

				inventoryItems.push(itemArray);
			}

			for (var i = 1; i < inventoryItems.length; i++)
			{
				cm.gainItem(inventoryItems[i][0], -inventoryItems[i][1]);
				cm.gainItem(inventoryItems[i][0], inventoryItems[i][1]);
			}
		}
		else if (selection == 2) // ETC
		{
			var inventory = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.ETC);

			var iter = inventory.iterator();

			while (iter.hasNext())
			{
				var itemId = iter.next().getItemId();
				var itemArray = new Array(itemId, cm.getPlayer().getItemQuantity(itemId, false));

				inventoryItems.push(itemArray);
			}

			for (var i = 1; i < inventoryItems.length; i++)
			{
				cm.gainItem(inventoryItems[i][0], -inventoryItems[i][1]);
				cm.gainItem(inventoryItems[i][0], inventoryItems[i][1]);
			}
		}

		cm.sendOk("Your items has been sorted!");
		cm.dispose();
	}
	else if (status == 3)
	{
		cm.sendSimple("Which inventory type is the item in?\r\n\r\n#e#b#L0#EQUIP#l\r\n#L1#ETC#l\r\n#L2#CASH#l#k#n");
	}
	else if (status == 4)
	{
		choice = selection;

		if (selection == 0)
		{
			cm.sendSimple("Choose the item that you want to remove from your inventory. #eThe item will not be refunded.#n\r\n\r\n" + cm.EquipList(cm.getClient()));
		}
		else if (selection == 1)
		{
			cm.sendSimple("Choose the item that you want to remove from your inventory. #eThe item will not be refunded.#n\r\n\r\n" + cm.ETCList(cm.getClient()));
		}
		else if (selection == 2)
		{
			cm.sendSimple("Choose the item that you want to remove from your inventory. #eThe item will not be refunded.#n\r\n\r\n" + cm.CashList(cm.getClient()));
		}
	}
	else if (status == 5)
	{
		var type = [Packages.client.inventory.MapleInventoryType.EQUIP, Packages.client.inventory.MapleInventoryType.ETC, Packages.client.inventory.MapleInventoryType.CASH];
		var selectedType = type[choice];

		var chosenItem = cm.getPlayer().getInventory(selectedType).getItem(selection).getItemId();

		if (selectedType == Packages.client.inventory.MapleInventoryType.EQUIP)
		{
			Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, selection, 1, false);
		}
		else
		{
			cm.gainItem(chosenItem, -cm.getPlayer().getItemQuantity(chosenItem, false));
		}
		cm.sendOk("#v" + chosenItem + "# #t" + chosenItem + "# has been removed from your inventory!");
		cm.dispose();
	}
}