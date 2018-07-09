// Transmogrification NPC

importPackage(Packages.client);

var status = 0;
var chosenItem;
var selectedSlot;
var equipFrom;

status = -1;

function start() 
{
    status = -1;
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
        cm.sendSimple("I'm a dark and evil wizard that practices transmogrification. I'm well experienced to transmog your equip items. What would you like to do?\r\n\r\n#b#e#L0#What is transmogrification?#l\r\n#L1#Transmog my equip item with stat transfer#l\r\n#L2#Transmog my equip with no stat transfer#l\r\n#L3#Copy stats on my transmogrified equip to a NX equip#l\r\n#L4#Copy transmogrified stats on my NX equip to a different NX equip#l#k#n");
    }
    else if (status == 1)
    {
        if (selection == 0)
        {
            cm.sendOk("The idea of transmogrification came from the game World of Warcraft. It is brought to this server exclusively for the players of myMaple. You can only transmog your non-cash equip, except weapons, shields, and medals, for 5 Enchanted Scroll. You can choose one stat (STR, DEX, WATK, etc.) to transfer over and the percentage of the stat that it will transfer is based on your crafting level. Each crafting level is 5% so if your crafting level is 10, it will transfer over 50% of a specific stat that you choose. Equips that have been transmogrified have a Transmog tag on them which you can equip it over non-NX equips so basically, they act like a NX equip. You can not have a NX equip over a Transmogrified equip. #eDo note that transmogrification is permanent and can not be reversed. All transmogrified equips are untradeable and undroppable.#n");
            cm.dispose();
        }
        else if (selection == 1)
        {
            cm.sendYesNo("In order to transmog your equip item with stat transfer, you need #d5 Enchanted Scroll and 5,000,000 Mesos#k. #eTransmogrified items are permanent and cannot be reversed and are untradeable and undroppable.#n Would you like to continue?");
        }
        else if (selection == 2)
        {
            status = 4;
            cm.sendYesNo("In order to transmog your equip item without stat transfer, you need #d1 Enchanted Scroll and 1,000,000 Mesos#k. #eTransmogrified items are permanent and cannot be reversed.#n Would you like to continue?");
        }
        else if (selection == 3)
        {
            status = 6;
            cm.sendYesNo("In order to copy stats on your transmogrified equip to a NX equip, you need #d1 Enchanted Scroll and 1,000,000 Mesos#k. #eStat copy is permanent and cannot be reversed and are untradeable and undroppable.#n Would you like to continue?");
        }
        else if (selection == 4)
        {
            status = 9;
            cm.sendYesNo("In order to copy transmogrified stats on your NX equip to a different NX equip, you need #d1 Enchanted Scroll and 1,000,000 Mesos#k. #eStat copy is permanent and cannot be reversed and are untradeable and undroppable.#n Would you like to continue?");
        }
    }
    else if (status == 2) // Transmog with stat
    {
        if (cm.haveItem(5221001, 5) && cm.getMeso() >= 5000000)
        {
            cm.sendSimple("Choose which equip you want to transmog! We won't refund your #rEquip#k if you selected wrong! \r\n\r\n" + cm.EquipList(cm.getClient()));
        }
        else
        {
            cm.sendOk("You either do not have 5 Enchanted Scroll or 5,000,000 Mesos!");
            cm.dispose();
        }
    }
    else if (status == 3)
    {
        selectedSlot = selection;
        chosenItem = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection);

        if(Packages.server.MapleItemInformationProvider.getInstance().isCash(chosenItem.getItemId()) || Packages.server.MapleItemInformationProvider.getInstance().isTransmogrified(chosenItem))
        {
            cm.sendOk("You can not transmog a cash equip item or an item that already has been transmogrified!");
            cm.dispose();
        }
        else
        {
            var weapon = Packages.server.MapleItemInformationProvider.getInstance().getWeaponType(chosenItem.getItemId());
            if (weapon == Packages.client.inventory.MapleWeaponType.NOT_A_WEAPON && !cm.isShield(chosenItem.getItemId())) 
            {
                var choices = "#L1#STR#l\r\n"
                choices += "#L2#DEX#l\r\n";
                choices += "#L3#INT#l\r\n";
                choices += "#L4#LUK#l\r\n";
                choices += "#L5#MATK (Magic Attack)#l\r\n";
                choices += "#L6#WATK (Weapon Attack)#l\r\n";
                choices += "#L7#ACC (Accuracy)#l\r\n";
                choices += "#L8#Avoidability#l\r\n";
                choices += "#L9#Jump#l\r\n";
                choices += "#L10#Speed#l\r\n";
                choices += "#L11#WDEF (Weapon Defense)#l\r\n";
                choices += "#L12#MDEF (Magic Defense)#l\r\n";
                choices += "#L13#HP#l\r\n";
                choices += "#L14#MP#l\r\n";
                choices += "#L15#Upgradeable Slots#l";

                var percentage = "";

                if(cm.getPlayer().getCraftingLevel() == 0)
                {
                    percentage = "0%";
                }
                else
                {
                    var tempPercent = cm.getPlayer().getCraftingLevel() * 5;
                    percentage = tempPercent + "%";
                }

                cm.sendSimple("#eYour current crafting level is: " + cm.getPlayer().getCraftingLevel() + "\r\nThe percentage of stat it will transfer is: " + percentage + "\r\n\r\n#nWhich specific stat would you like to transfer over? Any decimal stats will be rounded down.\r\n\r\n" + choices);
            }
            else
            {
                cm.sendOk("You can not choose a weapon or shield to be transmogrified!");
                cm.dispose();
            }
        }
    }
    else if (status == 4)
    {
        Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, selectedSlot, 1, false);
        cm.transmogItemStats(chosenItem, selection);
        cm.gainItem(5221001, -5);
        cm.gainMeso(-5000000);
        cm.sendOk("#v" + chosenItem.getItemId() + "# has been transmogrified!");
        cm.dispose();
    }
    else if (status == 5) // Transmog without stat
    {
        if (cm.haveItem(5221001, 1) && cm.getMeso() >= 1000000)
        {
            cm.sendSimple("Choose which equip you want to transmog! We won't refund your #rEquip#k if you selected wrong! \r\n\r\n" + cm.EquipList(cm.getClient()));
        }
        else
        {
            cm.sendOk("You either do not have 1 Enchanted Scroll or 1,000,000 Mesos!");
            cm.dispose();
            return;
        }
    }
    else if (status == 6)
    {
        selectedSlot = selection;
        chosenItem = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection);

        if(Packages.server.MapleItemInformationProvider.getInstance().isCash(chosenItem.getItemId()) || Packages.server.MapleItemInformationProvider.getInstance().isTransmogrified(chosenItem))
        {
            cm.sendOk("You can not transmog a cash equip item or an item that already has been transmogrified!");
            cm.dispose();
            return;
        }
        else
        {
            var weapon = Packages.server.MapleItemInformationProvider.getInstance().getWeaponType(chosenItem.getItemId());
            if (weapon == Packages.client.inventory.MapleWeaponType.NOT_A_WEAPON) 
            {
                Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, selectedSlot, 1, false);
                cm.transmogWithoutStats(chosenItem.getItemId());
                cm.gainItem(5221001, -1);
                cm.gainMeso(-1000000);
                cm.sendOk("Your #v" + chosenItem.getItemId() + "# #t" + chosenItem.getItemId() + "# has been transmogrified!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("You can not choose a weapon to be transmogrified!");
                cm.dispose();
                return;
            }
        }
    }
    else if (status == 7) // Copy transmog stat from non NX to NX equip
    {
        if (cm.haveItem(5221001, 1) && cm.getMeso() >= 1000000)
        {
            cm.sendSimple("Choose which transmogrified equip stats you want to copy #eFROM#n! #bThis equip will be removed from your inventory.#k We won't refund your #rEquip#k if you selected wrong! \r\n\r\n" + cm.EquipList(cm.getClient()));
        }
        else
        {
            cm.sendOk("You either do not have 1 Enchanted Scroll or 1,000,000 Mesos!");
            cm.dispose();
            return;
        }
    }
    else if (status == 8)
    {
        selectedSlot = selection;
        equipFrom = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection);

        if (!Packages.server.MapleItemInformationProvider.getInstance().isCash(equipFrom.getItemId()) && Packages.server.MapleItemInformationProvider.getInstance().isTransmogrified(equipFrom))
        {
            var weapon = Packages.server.MapleItemInformationProvider.getInstance().getWeaponType(equipFrom.getItemId());
            if (weapon == Packages.client.inventory.MapleWeaponType.NOT_A_WEAPON && !cm.isShield(equipFrom.getItemId())) 
            {
                cm.sendSimple("Choose which NX equip you want to copy the stats #eTO#n! #bThe NX equip has to be the same type as the your transmogrified equip you're copying from. #kFor example, if you want to copy the stats from a TOP, you will need to pick a TOP NX equip. We won't refund your #rEquip#k if you selected wrong! \r\n\r\n" + cm.EquipList(cm.getClient()));
            }
            else
            {
                cm.sendOk("You can not copy stats from a weapon or shield!");
                cm.dispose();
                return;
            }
        }
        else
        {
            cm.sendOk("You can only choose a transmogrified equip that is not a NX equip to copy the stats from!");
            cm.dispose();
            return;
        }
    }
    else if (status == 9)
    {
        var newSelectedSlot = selection;
        var equipTo = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection);

        if (Packages.server.MapleItemInformationProvider.getInstance().isCash(equipTo.getItemId()))
        {
            if (cm.isWeapon(equipTo.getItemId()) || cm.isShield(equipTo.getItemId())) 
            {
                cm.sendOk("You can not copy stats to a weapon or shield!");
                cm.dispose();
                return;
            }
            else
            {
                var matchingTypes = false;

                if (cm.isAccessory(equipFrom.getItemId()) && cm.isAccessory(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isCap(equipFrom.getItemId()) && cm.isCap(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isCape(equipFrom.getItemId()) && cm.isCape(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isTop(equipFrom.getItemId()) && cm.isTop(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isGlove(equipFrom.getItemId()) && cm.isGlove(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isOverall(equipFrom.getItemId()) && cm.isOverall(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isPants(equipFrom.getItemId()) && cm.isPants(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isRing(equipFrom.getItemId()) && cm.isRing(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isShoes(equipFrom.getItemId()) && cm.isShoes(equipTo.getItemId()))
                    matchingTypes = true;

                if (matchingTypes)
                {
                    Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, selectedSlot, 1, false);
                    Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, newSelectedSlot, 1, false);
                    cm.transferTransmogStats(equipFrom, equipTo.getItemId());
                    cm.gainItem(5221001, -1);
                    cm.gainMeso(-1000000);
                    cm.sendOk("The stats of your #v" + equipFrom.getItemId() + "# #t" + equipFrom.getItemId() + "# has been copied to #v" + equipTo.getItemId() + "# #t" + equipTo.getItemId() + "#!");
                    cm.dispose();
                    return;
                }
                else
                {
                    cm.sendOk("The two equips you chose does not have matching types! If you want to choose to copy from stats from a transmogrified TOP, you have to choose a NX TOP to copy the stats to.");
                    cm.dispose();
                    return;
                }
            }
        }
        else
        {
            cm.sendOk("You can only choose a NX equip to copy the stats to!");
            cm.dispose();
            return;
        }
    }
    else if (status == 10) // Copy NX equip stat to NX equip
    {
        if (cm.haveItem(5221001, 1) && cm.getMeso() >= 1000000)
        {
            cm.sendSimple("Choose which NX equip you want to copy the transmogrified stats #eFROM#n! #bThis equip will be removed from your inventory.#k We won't refund your #rEquip#k if you selected wrong! \r\n\r\n" + cm.EquipList(cm.getClient()));
        }
        else
        {
            cm.sendOk("You either do not have 1 Enchanted Scroll or 1,000,000 Mesos!");
            cm.dispose();
            return;
        }
    }
    else if (status == 11)
    {
        selectedSlot = selection;
        equipFrom = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection);

        if (Packages.server.MapleItemInformationProvider.getInstance().isCash(equipFrom.getItemId()) && Packages.server.MapleItemInformationProvider.getInstance().isTransmogrified(equipFrom))
        {
            var weapon = Packages.server.MapleItemInformationProvider.getInstance().getWeaponType(equipFrom.getItemId());
            if (weapon == Packages.client.inventory.MapleWeaponType.NOT_A_WEAPON && !cm.isShield(equipFrom.getItemId())) 
            {
                cm.sendSimple("Choose which NX equip you want to copy the transmogrified stats #eTO#n! #bThis NX equip has to be the same type as the your NX equip you're copying from.#k For example, if you want to copying the stats from a TOP, you will need to pick a TOP NX equip. We won't refund your #rEquip#k if you selected wrong! \r\n\r\n" + cm.EquipList(cm.getClient()));
            }
            else
            {
                cm.sendOk("You can not copy stats from a weapon or shield!");
                cm.dispose();
                return;
            }
        }
        else
        {
            cm.sendOk("You can only choose a NX equip that has transmogrified stats to copy the stats from!");
            cm.dispose();
            return;
        }
    }
    else if (status == 12)
    {
        var newSelectedSlot = selection;
        var equipTo = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection);

        if (Packages.server.MapleItemInformationProvider.getInstance().isCash(equipTo.getItemId()))
        {
            if (cm.isWeapon(equipTo.getItemId()) || cm.isShield(equipTo.getItemId())) 
            {
                cm.sendOk("You can not copy stats to a weapon or shield!");
                cm.dispose();
                return;
            }
            else
            {
                var matchingTypes = false;

                if (cm.isAccessory(equipFrom.getItemId()) && cm.isAccessory(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isCap(equipFrom.getItemId()) && cm.isCap(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isCape(equipFrom.getItemId()) && cm.isCape(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isTop(equipFrom.getItemId()) && cm.isTop(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isGlove(equipFrom.getItemId()) && cm.isGlove(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isOverall(equipFrom.getItemId()) && cm.isOverall(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isPants(equipFrom.getItemId()) && cm.isPants(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isRing(equipFrom.getItemId()) && cm.isRing(equipTo.getItemId()))
                    matchingTypes = true;

                if (cm.isShoes(equipFrom.getItemId()) && cm.isShoes(equipTo.getItemId()))
                    matchingTypes = true;

                if (matchingTypes)
                {
                    Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, selectedSlot, 1, false);
                    Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, newSelectedSlot, 1, false);
                    cm.transferTransmogStats(equipFrom, equipTo.getItemId());
                    cm.gainItem(5221001, -1);
                    cm.gainMeso(-1000000);
                    cm.sendOk("The stats of your #v" + equipFrom.getItemId() + "# #t" + equipFrom.getItemId() + "# has been copied to #v" + equipTo.getItemId() + "# #t" + equipTo.getItemId() + "#!");
                    cm.dispose();
                    return;
                }
                else
                {
                    cm.sendOk("The two equips you chose does not have matching types! If you want to choose to copy from stats from a NX TOP with transmogrified stats, you have to choose a NX TOP to copy the stats to.");
                    cm.dispose();
                    return;
                }
            }
        }
        else
        {
            cm.sendOk("You can only choose a NX equip to copy the stats to!");
            cm.dispose();
            return;
        }
    }
}