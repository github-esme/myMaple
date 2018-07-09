// General Store

var status;

var UGWeapons = new Array(1032080, 1032081, 1032082, 1032083, 1122081, 1122082, 1122083, 1122084, 1132036, 1132037, 1132038, 1132039, 1112435, 1112436, 1112437, 1112438, 1092070, 1092071, 1092072, 1092073, 1092075, 1092076, 1092077, 1092078, 1092080, 1092081, 1092082, 1092083, 1092070, 1092071, 1092072, 1092073, 1092075, 1092076, 1092077, 1092078, 1092080, 1092081, 1092082, 1092083, 1302143, 1302144, 1302145, 1302146, 1312058, 1312059, 1312060, 1312061, 1322086, 1322087, 1322088, 1322089, 1332116, 1332117, 1332118, 1332119, 1332121, 1332122, 1332123, 1332124, 1342029, 1342030, 1342031, 1342032, 1372074, 1372075, 1372076, 1372077, 1382095, 1382096, 1382097, 1382098, 1402086, 1402087, 1402088, 1402089, 1412058, 1412059, 1412060, 1412061, 1422059, 1422060, 1422061, 1422062, 1432077, 1432078, 1432079, 1432080, 1442107, 1442108, 1442109, 1442110, 1452102, 1452103, 1452104, 1452105, 1462087, 1462088, 1462089, 1462090, 1472113, 1472114, 1472115, 1472116, 1482075, 1482076, 1482077, 1482078, 1492075, 1492076, 1492077, 1492078, 9420064);
var mapleWeapons = new Array(1302020, 1302030, 1302033, 1302058, 1302064, 1302080, 1312032, 1322054, 1332025, 1332055, 1332056, 1372034, 1382009, 1382012, 1382039, 1402039, 1412011, 1412027, 1422014, 1422029, 1432012, 1432040, 1432046, 1442024, 1442030, 1442051, 1452016, 1452022, 1452045, 1462014, 1462019, 1462040, 1472030, 1472032, 1472055, 1482020, 1482021, 1482022, 1492020, 1492021, 1492022, 1092030, 1092045, 1092046, 1092047);
var selectedWeapons = new Array();
var number = 0;

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
        // if(selectedWeapons.length >= 1)
        // {
        //     for(var i = 0; i < selectedWeapons.length; i++)
        //     {
        //         cm.gainItem(selectedWeapons[i]);
        //     }

        //     cm.dispose();
        //     return;
        // }
        // else
        // {
        //     cm.dispose();
        //     return;
        // }

        cm.dispose();
        return;
    }

    if (status == 0)
    {
        var outStr = "Hello, I'm the general store for myMaple that doesn't fit with other NPCs in the FM! What would you like to buy?\r\n\r\n";

        outStr += "#e#L9#Receive a free permanent #v5030006# #t5030006##l\r\n";
        outStr += "#L12#Exchange 100,000 Mesos for #v3011000# #t3011000##l\r\n\r\n";
        outStr += "#L10#Exchange 15,000,000 Mesos for #d30 minute #v5211048# 2X EXP card#k#l\r\n"
        outStr += "#L11#Exchange 25,000,000 Mesos for #d1 hour #v5211048# 2X EXP card#k#l\r\n"
        outStr += "#L4#Exchange 1,000,000 Mesos for #d1 #v5050000# AP Reset#k#l\r\n"
        outStr += "#L5#Exchange 500,000 Mesos for #d1 #v5050001# SP Reset (1st Job)#k#l\r\n";
        outStr += "#L6#Exchange 500,000 Mesos for #d1 #v5050002# SP Reset (2nd Job)#k#l\r\n";
        outStr += "#L7#Exchange 500,000 Mesos for #d1 #v5050003# SP Reset (3rd Job)#k#l\r\n";
        outStr += "#L8#Exchange 500,000 Mesos for #d1 #v5050004# SP Reset (4th Job)#k#l\r\n";
        outStr += "#L0#Exchange 25,000 NX for #d11 #v5050000# #t5050000##k#l\r\n";
        outStr += "#L1#Exchange 75,000 NX for #d35 #v5050000# #t5050000##k#l\r\n";
        outStr += "#L2#Exchange 2 Unwelcome Guest weapons for a #d#v5221001# Enchanted Scroll#k#l\r\n";
        outStr += "#L3#Exchange 10 Maple Weapons for a #d#v5221001# Enchanted Scroll#l#k#n";

        cm.sendSimple(outStr);
    }
    else if (status == 1)
    {
        var nxcredit = cm.getPlayer().getCashShop().getCash(1);

        if (selection == 0)
        {
            if (nxcredit >= 25000)
            {
                if (cm.canHold(5050000, 11))
                {
                    cm.getPlayer().getCashShop().gainCash(1, -25000);
                    cm.gainItem(5050000, 11);
                    cm.logGeneralStore("11 AP Resets Bundle");
                    cm.sendOk("You can received 11 #t5050000#!");
                }
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold these AP Resets!");
                }
            }
            else
            {
                cm.sendOk("Sorry, you don't have enough NX Credits!");
            }

            cm.dispose();
        }
        else if (selection == 1)
        {
            if (nxcredit >= 75000)
            {
                if (cm.canHold(5050000, 35))
                {
                    cm.getPlayer().getCashShop().gainCash(1, -75000);
                    cm.gainItem(5050000, 35);
                    cm.logGeneralStore("35 AP Resets Bundle");
                    cm.sendOk("You can received 35 #t5050000#!");
                }
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold these AP Resets!");
                }
            }
            else
            {
                cm.sendOk("Sorry, you don't have enough NX Credits!");
            }

            cm.dispose();
        }
        else if (selection == 2)
        {
            action(1, 0, 0);
        }
        else if (selection == 3)
        {
            status = 4;
            action(1, 0, 0);
        }
        else if (selection == 4)
        {
            status = 7
            cm.sendGetNumber("How many AP Resets would you like to buy?\r\n\r\n#eYou currently have #r" + cm.getMeso() + "#k Mesos.#n", 1, 1, 1000);
        }
        else if (selection == 5)
        {
            status = 8
            cm.sendGetNumber("How many SP Reset (1st Job) would you like to buy?\r\n\r\n#eYou currently have #r" + cm.getMeso() + "#k Mesos.#n", 1, 1, 1000);
        }
        else if (selection == 6)
        {
            status = 9
            cm.sendGetNumber("How many SP Reset (2nd Job) would you like to buy?\r\n\r\n#eYou currently have #r" + cm.getMeso() + "#k Mesos.#n", 1, 1, 1000);
        }
        else if (selection == 7)
        {
            status = 10
            cm.sendGetNumber("How many SP Reset (3rd Job) would you like to buy?\r\n\r\n#eYou currently have #r" + cm.getMeso() + "#k Mesos.#n", 1, 1, 1000);
        }
        else if (selection == 8)
        {
            status = 11
            cm.sendGetNumber("How many SP Reset (4th Job) would you like to buy?\r\n\r\n#eYou currently have #r" + cm.getMeso() + "#k Mesos.#n", 1, 1, 1000);
        }
        else if (selection == 9)
        {
            if(cm.canHold(5030006)) 
            {
                if (cm.hasItem(5030006))
                {
                    cm.sendOk("You already have a merchant!");
                    cm.dispose();
                }
                else
                {
                    cm.gainItem(5030006);
                    cm.sendOk("You a received a #t5030006#!");
                    cm.dispose();
                }
            } 
            else 
            {
                cm.sendOk("Please make room in your inventory!");
                cm.dispose();
            }
        }
        else if (selection == 10)
        {
            if (cm.getMeso() >= 15000000)
            {
                if (cm.canHold(5211048))
                {
                    cm.gainItem(5211048, 1, false, false, 1800000);
                    cm.gainMeso(-15000000)
                    cm.getPlayer().setRates();
                    cm.logGeneralStore("30 Minute EXP Card");
                    cm.sendOk("Here is your 30 minute EXP card!");
                    cm.dispose();
                } 
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold this EXP card");
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("You do not have enough mesos!");
                cm.dispose();
            }
        }
        else if (selection == 11)
        {
            if (cm.getMeso() >= 25000000)
            {
                if (cm.canHold(5211048))
                {
                    cm.gainItem(5211048, 1, false, false, 3600000);
                    cm.gainMeso(-25000000)
                    cm.getPlayer().setRates();
                    cm.logGeneralStore("1 Hour EXP Card");
                    cm.sendOk("Here is your 1 hour EXP card!");
                    cm.dispose();
                } 
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold this EXP card");
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("You do not have enough mesos!");
                cm.dispose();
            }
        }
        else if (selection == 12)
        {
            if (cm.getMeso() >= 100000)
            {
                if (cm.canHold(3011000))
                {
                    cm.gainItem(3011000);
                    cm.gainMeso(-100000);
                    cm.sendOk("Here is your Fishing Chair!");
                    cm.dispose();
                } 
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold this fishing chair");
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("You do not have enough mesos!");
                cm.dispose();
            }
        }
    }
    else if (status == 2) // UG to ES
    {
        cm.sendSimple("Choose which Unwelcome Guest equips you want to exchange!" + " \r\n\r\n#e#b" + number + " / 2 Unwelcome Guest equip(s) Selected#n#k\r\n\r\n" + cm.EquipList(cm.getClient()));
    }
    else if (status == 3)
    {
        var weaponID = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection).getItemId();

        if(UGWeapons.indexOf(weaponID) == -1)
        {
            status = 1;
            cm.sendOk("This is not a Unwelcome Guest equip that you can exchange! Please try again.");
        }
        else
        {
            number++;
            selectedWeapons.push(weaponID);
            cm.gainItem(weaponID, -1);

            if (number == 2)
            {
                var outStr = "These are the Unwelcome Guest equips you selected. Do you wish to continue?\r\n\r\n";

                for(var i = 0; i < selectedWeapons.length; i++)
                {
                    outStr += "#v" + selectedWeapons[i] + "# #t" + selectedWeapons[i] + "#\r\n";
                }

                cm.sendYesNo(outStr);
            }
            else
            {
                status = 1;
                action(1, 0, 0);
            }
        }
    }
    else if (status == 4)
    {
        cm.gainItem(5221001);

        cm.sendOk("You have exchanged your 2 Unwelcome Guest equips for a #v5221001# #t5221001#!");
        cm.dispose();
        return;
    }
    else if (status == 5) // Maple Weapons to Enchanted Scroll
    {
        cm.sendSimple("Choose which Maple Weapons you want to exchange!" + " \r\n\r\n#e#b" + number + " / 10 Maple Weapon(s) Selected#n#k\r\n\r\n" + cm.EquipList(cm.getClient()));
    }
    else if (status == 6)
    {
        var weaponID = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection).getItemId();

        if(mapleWeapons.indexOf(weaponID) == -1)
        {
            status = 4;
            cm.sendOk("This is not a Maple Weapon that you can exchange! Please try again.");
        }
        else
        {
            number++;
            selectedWeapons.push(weaponID);
            cm.gainItem(weaponID, -1);

            if (number == 10)
            {
                var outStr = "These are the Maple Weapons you selected. Do you wish to continue?\r\n\r\n";

                for(var i = 0; i < selectedWeapons.length; i++)
                {
                    outStr += "#v" + selectedWeapons[i] + "# #t" + selectedWeapons[i] + "#\r\n";
                }

                cm.sendYesNo(outStr);
            }
            else
            {
                status = 4;
                action(1, 0, 0);
            }
        }
    }
    else if (status == 7)
    {
        cm.gainItem(5221001);

        cm.sendOk("You have exchanged your 10 Maple Weapons for a #v5221001# #t5221001#!");
        cm.dispose();
        return;
    }
    else if (status == 8) // Meso For AP Reset
    {
        var mesoNeeded = 1000000 * selection;

        if (cm.getMeso() >= mesoNeeded)
        {
            if (cm.canHold(5050000, selection))
            {
                cm.gainMeso(-mesoNeeded);
                cm.gainItem(5050000, selection);
                cm.logGeneralStore(selection + " AP Resets");
                cm.sendOk("You have received " + selection + " AP Resets!");
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
            cm.sendOk("You do not have enough mesos!");
            cm.dispose();
        }
    }
    else if (status == 9) // Meso For SP Reset 1
    {
        var mesoNeeded = 500000 * selection;

        if (cm.getMeso() >= mesoNeeded)
        {
            if (cm.canHold(5050001, selection))
            {
                cm.gainMeso(-mesoNeeded);
                cm.gainItem(5050001, selection);
                cm.logGeneralStore(selection + " SP Resets (1st Job)");
                cm.sendOk("You have received " + selection + " SP Resets!");
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
            cm.sendOk("You do not have enough mesos!");
            cm.dispose();
        }
    }
    else if (status == 10) // Meso For SP Reset 2
    {
        var mesoNeeded = 500000 * selection;

        if (cm.getMeso() >= mesoNeeded)
        {
            if (cm.canHold(5050002, selection))
            {
                cm.gainMeso(-mesoNeeded);
                cm.gainItem(5050002, selection);
                cm.logGeneralStore(selection + " SP Resets (2nd Job)");
                cm.sendOk("You have received " + selection + " SP Resets!");
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
            cm.sendOk("You do not have enough mesos!");
            cm.dispose();
        }
    }
    else if (status == 11) // Meso For SP Reset 3
    {
        var mesoNeeded = 500000 * selection;

        if (cm.getMeso() >= mesoNeeded)
        {
            if (cm.canHold(5050003, selection))
            {
                cm.gainMeso(-mesoNeeded);
                cm.gainItem(5050003, selection);
                cm.logGeneralStore(selection + " SP Resets (3rd Job)");
                cm.sendOk("You have received " + selection + " SP Resets!");
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
            cm.sendOk("You do not have enough mesos!");
            cm.dispose();
        }
    }
    else if (status == 12) // Meso For SP Reset 4
    {
        var mesoNeeded = 500000 * selection;

        if (cm.getMeso() >= mesoNeeded)
        {
            if (cm.canHold(5050004, selection))
            {
                cm.gainMeso(-mesoNeeded);
                cm.gainItem(5050004, selection);
                cm.logGeneralStore(selection + " SP Resets (4th Job)");
                cm.sendOk("You have received " + selection + " SP Resets!");
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
            cm.sendOk("You do not have enough mesos!");
            cm.dispose();
        }
    }
}