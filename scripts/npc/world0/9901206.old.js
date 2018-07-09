// Crafting NPC

// 4001126 Maple Leaf

var status = 0;
var disabled = false;
var craftingLevel;
var itemType;
var chosenItem;
var mesosRequired;
var mobPoints;
var skillLevel;
var craftingLevelRequired;
var selectedSlot;

var item = [["4000000-100", "4000016-100", "4000019-100", "4001126-1"], ["4000020-100", "4000015-100", "4010001-10", "4001126-2"], ["4000008-200", "4000032-100", "4000024-50", "4001126-3"], ["4000107-100", "4000530-100", "4000108-100", "4001126-4"], ["4000039-100", "4000081-100", "4000022-100", "4004002-10", "4001126-5"], ["4000025-100", "4000040-10", "4000027-100", "4001126-6"], ["4000282-100", "4000143-100", "4000207-100", "4001126-7"], ["4000028-100", "4000145-100", "4000297-100", "4010004-10", "4001126-8"], ["4000079-100", "4000236-100", "4000172-100", "4001126-9"], ["4000057-100", "4000476-100", "4000147-100", "4010006-10", "4001126-10"]];   

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

    if(disabled && !cm.getPlayer().isIntern())
    {
        cm.sendOk("The NPC is under construction.");
        cm.dispose();
        return;
    }


    if (status == 0)
    {
        mobPoints = cm.getPlayer().getMobPoints();
        craftingLevel = cm.getPlayer().getCraftingLevel();
        cm.sendSimple("Hello there! You can upgrade your crafting level by bringing me certain items. You can also upgrade your Unwelcome Guest weapons and gears with me. What would you like to do?\r\n\r\n#eYour current crafting level is: #r" + craftingLevel + "#k#n\r\n\r\n#b#e#L0#Upgrade my crafting level (Max Level: 10)#l\r\n#L1#Upgrade my Unwelcome Guest equip#l\r\n#L6#Reroll my equip's stats#l\r\n#L3#Receive/upgrade my Maker skill#l\r\n#L4#Create iTCG materials#l\r\n#L5#Create iTCG items#l#k#n");
    }
    else if (status == 1)
    {
        if(selection == 0)
        {
            if(craftingLevel == 10)
            {
                cm.sendOk("Crafting level 10 is the maximum level.");
                cm.dispose();
                return;
            }
            else
            {
                var message = "You will need to bring me the following below. Do you have the required items?\r\n\r\n";

                for(var i = 0; i < item[craftingLevel].length; i++)
                {
                    var requiredItem = parseInt(item[craftingLevel][i].split('-')[0]);
                    var requiredQuantity = parseInt(item[craftingLevel][i].split('-')[1]);

                    message += requiredQuantity + " #v" + requiredItem + "# #t" + requiredItem + "#\r\n";
                }

                cm.sendYesNo(message);
            }
        }
        else if (selection == 1)
        {
            cm.sendSimple("Please choose your #rUnwelcome Guest#k equip you want to upgrade! #ePlease note that stats will not transfer over.#n We won't refund if you selected wrong! \r\n\r\n" + cm.EquipList(cm.getClient()));
            status = 2;
        }
        else if (selection == 2)
        {
            cm.sendOk("When you transmogrify an equip using the Transmogrification NPC to the left of me, it transfers a percentage of one stat (STR, DEX, MDEF, WATK, etc.) based on your crafting level. Each crafting level equals 10% so if you have a crafting level of 5, you have a 50% transfer rate. For example, lets say your crafting level was 5. If you wanted to Transmogrify a Zakum helmet that had 10 stats all and wanted to transfer the STR stats, your Transmogrified Zakum helmet would have 5 STR and 0 for all other stats. You can equip Transmogrified equips over non-NX equips so basically, they act like a NX equip. You can not have a NX equip over a Transmogrified equip.");
            cm.dispose();
            return;
        }
        else if (selection == 3)
        {
            skillLevel = cm.getPlayer().getSkillLevel(1007);
            if(skillLevel == 3)
            {
                cm.sendOk("You Maker skill is already maxed out!");
                cm.dispose();
                return;
            }
            else
            {
                status = 4;

                if (skillLevel == 0)
                {
                    mesosRequired = 100000;
                    craftingLevelRequired = 3;
                }
                else if (skillLevel == 1)
                {
                    mesosRequired = 500000;
                    craftingLevelRequired = 5;
                }
                else if (skillLevel == 2)
                {
                    mesosRequired = 1000000;
                    craftingLevelRequired = 7;
                }

                cm.sendYesNo("It looks like you current Maker skill is level: #r" + skillLevel + "#k\r\n\r\nYou need to have the fulfill the following requirements below in order to learn/upgrade the Maker skill.\r\n\r\n#eRequired Crafting Level: " + craftingLevelRequired + "\r\nRequired Mesos: " + mesosRequired + "#n");
            }
        }
        else if (selection == 4)
        {
            var outStr = "Which material would you like to create?\r\n\r\n";
            outStr += "#L0##b#e#v4031758# #t4031758##l\r\n"
            outStr += "#L1##v4031754# #t4031754##l\r\n"
            outStr += "#L2##v4031755# #t4031755##l\r\n#k#n"
            
            status = 5;

            cm.sendSimple(outStr);
        }
        else if (selection == 5)
        {
            var outStr = "Which item would you like to create?\r\n\r\n";
            outStr += "#L0##b#e#v1072344# #t1072344##l\r\n"
            outStr += "#L1##v1082223# #t1082223##l\r\n"
            outStr += "#L2##v2070016# #t2070016##l\r\n"
            outStr += "#L3##v1032048# #t1032048##l\r\n#k#n"
            
            status = 9;

            cm.sendSimple(outStr);
        }
        else if (selection == 6)
        {
            status = 14;

            cm.sendSimple("Choose which equip's stats you want to reroll. You can only choose equips that have not been scrolled. #eThere is no refund if you get stats that are worse!#n \r\n\r\n" + cm.EquipList(cm.getClient()));
        }
    }
    else if (status == 2)
    {
        var hasAllItems = true;

        for(var i = 0; i < item[craftingLevel].length; i++)
        {
            var requiredItem = parseInt(item[craftingLevel][i].split('-')[0]);
            var requiredQuantity = parseInt(item[craftingLevel][i].split('-')[1]);

            if(!cm.haveItem(requiredItem, requiredQuantity))
            {
                hasAllItems = false;
            }
        }

        if(hasAllItems)
        {
            for(var i = 0; i < item[craftingLevel].length; i++)
            {
                var requiredItem = parseInt(item[craftingLevel][i].split('-')[0]);
                var requiredQuantity = parseInt(item[craftingLevel][i].split('-')[1]);

                cm.gainItem(requiredItem, -requiredQuantity)
            }

            cm.getPlayer().gainCraftingLevel(1);
            cm.sendOk("You have leveled your crafting level!\r\n\r\nYour current crafting level is now: " + cm.getPlayer().getCraftingLevel());
            cm.dispose();
            return;
        }
        else
        {
            cm.sendOk("You do not have all the required items!");
            cm.dispose();
            return;
        }
    }
    else if (status == 3) // Upgrade UG Equip
    {
        var selectedType = selection;
        chosenItem = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection).getItemId();

        if(Packages.server.MapleItemInformationProvider.getInstance().isCash(chosenItem))
        {
            cm.sendOk("You can not upgrade a cash equip item or an item that already has been transmogrified!");
            cm.dispose();
            return;
        }
        else
        {
            if (chosenItem == 1032080 || chosenItem == 1122081 || chosenItem == 1132036 || chosenItem == 1112435 || chosenItem == 1092070 || chosenItem == 1092075 || chosenItem == 1092080 || chosenItem == 1092070 || chosenItem == 1092075 || chosenItem == 1092080 || chosenItem == 1302143 || chosenItem == 1312058 || chosenItem == 1322086 || chosenItem == 1332116 || chosenItem == 1332121 || chosenItem == 1342029 || chosenItem == 1372074 || chosenItem == 1382095 || chosenItem == 1402086 || chosenItem == 1412058 || chosenItem == 1422059 || chosenItem == 1432077 || chosenItem == 1442107 || chosenItem == 1452102 || chosenItem == 1462087 || chosenItem == 1472113 || chosenItem == 1482075 || chosenItem == 1492075)
            {
                itemType = "1st";
                cm.sendYesNo("In order to upgrade your #v" + chosenItem + "#" + " #t" + chosenItem + "#, you will need to fulfill the following requirements below. Please note that your stats that you scrolled will not transfer over.\r\n\r\n#eRequired Item: 1 #v4000384# #t4000384#\r\nRequired Min Crafting Level: 2\r\nRequired Mob Points: 300#n");
            }
            else if (chosenItem == 1032081 || chosenItem == 1122082 || chosenItem == 1132037 || chosenItem == 1112436 || chosenItem == 1092071 || chosenItem == 1092076 || chosenItem == 1092081 || chosenItem == 1092071 || chosenItem == 1092076 || chosenItem == 1092081 || chosenItem == 1302144 || chosenItem == 1312059 || chosenItem == 1322087 || chosenItem == 1332117 || chosenItem == 1332122 || chosenItem == 1342030 || chosenItem == 1372075 || chosenItem == 1382096 || chosenItem == 1402087 || chosenItem == 1412059 || chosenItem == 1422060 || chosenItem == 1432078 || chosenItem == 1442108 || chosenItem == 1452103 || chosenItem == 1462088 || chosenItem == 1472114 || chosenItem == 1482076 || chosenItem == 1492076)
            {
                itemType = "2nd";
                cm.sendYesNo("In order to upgrade your #v" + chosenItem + "#" + " #t" + chosenItem + "#, you will need to fulfill the following requirements below. Please note that your stats that you scrolled will not transfer over.\r\n\r\n#eRequired Item: 1 #v4031903# #t4031903#\r\nRequired Min Crafting Level: 4\r\nRequired Mob Points: 500#n");
            }
            else if (chosenItem == 1032082 || chosenItem == 1122083 || chosenItem == 1132038 || chosenItem == 1112437 || chosenItem == 1092072 || chosenItem == 1092077 || chosenItem == 1092082 || chosenItem == 1092072 || chosenItem == 1092077 || chosenItem == 1092082 || chosenItem == 1302145 || chosenItem == 1312060 || chosenItem == 1322088 || chosenItem == 1332118 || chosenItem == 1332123 || chosenItem == 1342031 || chosenItem == 1372076 || chosenItem == 1382097 || chosenItem == 1402088 || chosenItem == 1412060 || chosenItem == 1422061 || chosenItem == 1432079 || chosenItem == 1442109 || chosenItem == 1452104 || chosenItem == 1462089 || chosenItem == 1472115 || chosenItem == 1482077 || chosenItem == 1492077)
            {
                itemType = "3rd";
                cm.sendYesNo("In order to upgrade your #v" + chosenItem + "#" + " #t" + chosenItem + "#, you will need to fulfill the following requirements below. Please note that your stats that you scrolled will not transfer over.\r\n\r\n#eRequired Item: 1 #v4032013# #t4032013#\r\nRequired Min Crafting Level: 6\r\nRequired Mob Points: 700#n");
            }
            else if (chosenItem == 1032083 || chosenItem == 1122084 || chosenItem == 1132039 || chosenItem == 1112438 || chosenItem == 1092073 || chosenItem == 1092078 || chosenItem == 1092083 || chosenItem == 1092073 || chosenItem == 1092078 || chosenItem == 1092083 || chosenItem == 1302146 || chosenItem == 1312061 || chosenItem == 1322089 || chosenItem == 1332119 || chosenItem == 1332124 || chosenItem == 1342032 || chosenItem == 1372077 || chosenItem == 1382098 || chosenItem == 1402089 || chosenItem == 1412061 || chosenItem == 1422062 || chosenItem == 1432080 || chosenItem == 1442110 || chosenItem == 1452105 || chosenItem == 1462090 || chosenItem == 1472116 || chosenItem == 1482078 || chosenItem == 1492078)
            {
                itemType = "Last";
                cm.sendYesNo("In order to upgrade your #v" + chosenItem + "#" + " #t" + chosenItem + "#, you will need to fulfill the following requirements below. Please note that your stats that you scrolled will not transfer over.\r\n\r\n#eRequired Item: 1 #v4032133# #t4032133#\r\nRequired Min Crafting Level: 8\r\nRequired Mob Points: 2000#n");
            }
            else
            {
                cm.sendOk("#v" + chosenItem + "# #t" + chosenItem + "# is not a Unwelcome Guest equip!");
                cm.dispose();
                return;
            }

        }
    }
    else if (status == 4)
    {
        if (itemType == "1st")
        {
            if(!cm.hasItem(4000384, 1) || craftingLevel < 2 || mobPoints < 300)
            {
                cm.sendOk("You do not fulfill the requirements to upgrade your #v" + chosenItem + "# #t" + chosenItem + "#!");
                cm.dispose();
                return;
            }
            else
            {
                cm.gainItem(4000384, -1);
                cm.getPlayer().gainMobPoints(-300);
            }
        }
        else if (itemType == "2nd")
        {
            if(!cm.hasItem(4031903, 1) || craftingLevel < 4 || mobPoints < 500)
            {
                cm.sendOk("You do not fulfill the requirements to upgrade your #v" + chosenItem + "# #t" + chosenItem + "#!");
                cm.dispose();
                return;
            }
            else
            {
                cm.gainItem(4031903, -1);
                cm.getPlayer().gainMobPoints(-500);
            }
        }
        else if (itemType == "3rd")
        {
            if(!cm.hasItem(4032013, 1) || craftingLevel < 6 || mobPoints < 700)
            {
                cm.sendOk("You do not fulfill the requirements to upgrade your #v" + chosenItem + "# #t" + chosenItem + "#!");
                cm.dispose();
                return;
            }
            else
            {
                cm.gainItem(4032013, -1);
                cm.getPlayer().gainMobPoints(-700);
            }
        }
        else if (itemType == "Last")
        {
            if(!cm.hasItem(4032133, 1) || craftingLevel < 8 || mobPoints < 2000)
            {
                cm.sendOk("You do not fulfill the requirements to upgrade your #v" + chosenItem + "# #t" + chosenItem + "#!");
                cm.dispose();
                return;
            }
            else
            {
                cm.gainItem(4032133, -1);
                cm.getPlayer().gainMobPoints(-2000);
            }
        }

        cm.gainItem(chosenItem, -1);
        chosenItem = chosenItem + 1;
        cm.gainItem(chosenItem, 1, true, false);
        cm.sendOk("The equip has upgraded to #v" + chosenItem + "# #t" + chosenItem + "#!");
        cm.dispose();
        return;
    }
    else if (status == 5) // Upgrade Maxer
    {
        if (cm.getMeso() >= mesosRequired && craftingLevel >= craftingLevelRequired)
        {
            skillLevel++;
            cm.teachSkill(1007, skillLevel, 3, -1);
            cm.teachSkill(20001007, skillLevel, 3, -1);
            cm.gainMeso(-mesosRequired);

            if (skillLevel == 0)
            {
                cm.sendOk("Congratulations! You have learned the Maker skill.");
            }
            else
            {
                cm.sendOk("Congratulations! Your Maker skill has been upgraded to level " + skillLevel + ".");
            }

            cm.dispose();
            return;
        }
        else
        {
            cm.sendOk("You did not fulfill the needed requirements!");
            cm.dispose();
            return;
        }
    }
    else if (status == 6) // Create iTCG Material Naricain Jewel ( 4031758 )
    {
        if(selection == 0)
        {
            var outStr = "In order to create #v4031758# #t4031758#, you will need to fulfill the following requirements below.\r\n\r\n";
            outStr += "#e10 #v4007000# #t4007000#\r\n";
            outStr += "10 #v4007001# #t4007001#\r\n";
            outStr += "10 #v4007002# #t4007002#\r\n";
            outStr += "10 #v4007003# #t4007003#\r\n";
            outStr += "10 #v4007004# #t4007004#\r\n";
            outStr += "10 #v4007005# #t4007005#\r\n";
            outStr += "10 #v4007006# #t4007006#\r\n";
            outStr += "10 #v4007007# #t4007007##n";
            
            cm.sendYesNo(outStr);
        }
        else if (selection == 1)
        {
            var outStr = "In order to create #v4031754# #t4031754#, you will need to fulfill the following requirements below.\r\n\r\n";
            outStr += "#e1 #v4031196# #t4031196#\r\n";
            outStr += "1 #v4000175# #t4000175##n";
            
            status = 7;

            cm.sendYesNo(outStr);
        }
        else if (selection == 2)
        {
            var outStr = "In order to create #v4031755# #t4031755#, you will need to fulfill the following requirements below.\r\n\r\n";
            outStr += "#e1 #v4032133# #t4032133#\r\n";
            outStr += "5,000,000 Mesos#n";
            
            status = 8;

            cm.sendYesNo(outStr);
        }
    }
    else if (status == 7)
    {
        if(cm.hasItem(4007000, 10) && cm.hasItem(4007001, 10) && cm.hasItem(4007002, 10) && cm.hasItem(4007003, 10) && cm.hasItem(4007004, 10) && cm.hasItem(4007005, 10) && cm.hasItem(4007006, 10) && cm.hasItem(4007007, 10))
        {
            cm.gainItem(4031758);
            cm.gainItem(4007000, -10);
            cm.gainItem(4007001, -10);
            cm.gainItem(4007002, -10);
            cm.gainItem(4007003, -10);
            cm.gainItem(4007004, -10);
            cm.gainItem(4007005, -10);
            cm.gainItem(4007006, -10);
            cm.gainItem(4007007, -10);

            cm.sendOk("You have successfully created the #v4031758# #t4031758#!");
            cm.dispose();
            return;
        }
        else
        {
            cm.sendOk("You do not fulfill the requirements to create #v4031758# #t4031758#!");
            cm.dispose();
            return;
        }
    }
    else if (status == 8) // Create iTCG Material Black Versal Materia ( 4031754 )
    {
        if(cm.hasItem(4031196) && cm.hasItem(4000175))
        {
            cm.gainItem(4031754);
            cm.gainItem(4031196, -1);
            cm.gainItem(4000175, -1);

            cm.sendOk("You have successfully created the #v4031754# #t4031754#!");
            cm.dispose();
            return;
        }
        else
        {
            cm.sendOk("You do not fulfill the requirements to create #v4031754# #t4031754#!");
            cm.dispose();
            return;
        }
    }
    else if (status == 9) // Create iTCG Material Taru Totem ( 4031755 )
    {
        if(cm.hasItem(4032133) && cm.getPlayer().getMeso() >= 5000000)
        {
            cm.gainItem(4031755);
            cm.gainItem(4032133, -1);
            cm.gainMeso(-5000000);

            cm.sendOk("You have successfully created the #v4031755# #t4031755#!");
            cm.dispose();
            return;
        }
        else
        {
            cm.sendOk("You do not fulfill the requirements to create #v4031755# #t4031755#!");
            cm.dispose();
            return;
        }
    }
    else if (status == 10) // Create iTCG Items
    {
        if(selection == 0)
        {
            var outStr = "In order to create #v1072344# #t1072344#, you will need to fulfill the following requirements below.\r\n\r\n";
            outStr += "#e1 #v4031755# #t4031755#\r\n";
            outStr += "1 #v4031754# #t4031754#\r\n";
            outStr += "50 #v4011001# #t4011001#\r\n";
            outStr += "50 #v4000030# #t4000030#\r\n";
            outStr += "25 #v4000021# #t4000021#\r\n";
            outStr += "4 #v4031917# #t4031917#\r\n";
            outStr += "Required Crafting Level: 10#n";
            
            cm.sendYesNo(outStr);
        }
        else if (selection == 1)
        {
            var outStr = "In order to create #v1082223# #t1082223#, you will need to fulfill the following requirements below.\r\n\r\n";
            outStr += "#e1 #v4031755# #t4031755#\r\n";
            outStr += "1 #v4031754# #t4031754#\r\n";
            outStr += "1 #v4031758# #t4031758#\r\n";
            outStr += "7 #v4005000# #t4005000#\r\n";
            outStr += "15 #v4000021# #t4000021#\r\n";
            outStr += "4 #v4031917# #t4031917#\r\n"
            outStr += "Required Crafting Level: 10#n";

            status = 11;

            cm.sendYesNo(outStr);
        }
        else if (selection == 2)
        {
            var outStr = "In order to create #v2070016# #t2070016#, you will need to fulfill the following requirements below.\r\n\r\n";
            outStr += "#e1 #v4031754# #t4031754#\r\n";
            outStr += "1 Fully Stacked #v2070006# #t2070006#\r\n";
            outStr += "5 #v4005003# #t4005003#\r\n";
            outStr += "7 #v4005004# #t4005004#\r\n";
            outStr += "1 #v4031917# #t4031917#\r\n";
            outStr += "Required Crafting Level: 6#n";

            status = 12;

            cm.sendYesNo(outStr);
        }
        else if (selection == 3)
        {
            var outStr = "In order to create #v1032048# #t1032048#, you will need to fulfill the following requirements below.\r\n\r\n";
            outStr += "#e1 #v4031758# #t4031758#\r\n";
            outStr += "2 #v4021007# #t4021007#\r\n"; 
            outStr += "2 #v4031917# #t4031917#\r\n";
            outStr += "Required Crafting Level: 8#n";

            status = 13;

            cm.sendYesNo(outStr);
        }
    }
    else if (status == 11) // Create iTCG Item Facestompers ( 1072344 )
    {
        if(cm.hasItem(4031755) && cm.hasItem(4031754) && cm.hasItem(4011001, 50) && cm.hasItem(4000030, 50) && cm.hasItem(4000021, 25) && cm.hasItem(4031917, 4) && craftingLevel == 10)
        {
            cm.gainItem(1072344);
            cm.gainItem(4031755, -1);
            cm.gainItem(4031754, -1);
            cm.gainItem(4011001, -50);
            cm.gainItem(4000030, -50);
            cm.gainItem(4000021, -25);
            cm.gainItem(4031917, -4);

            cm.sendOk("You have successfully created the #v1072344# #t1072344#!");
            cm.dispose();
            return;
        }
        else
        {
            cm.sendOk("You do not fulfill the requirements to create #v1072344# #t1072344#!");
            cm.dispose();
            return;
        }
    }
    else if (status == 12) // Create iTCG Item Stormcaster Gloves ( 1082223 )
    {
        if(cm.hasItem(4031755) && cm.hasItem(4031754) && cm.hasItem(4031758) && cm.hasItem(4005000, 7) && cm.hasItem(4000021, 15) && cm.hasItem(4031917, 4) && craftingLevel == 10)
        {
            cm.gainItem(1082223);
            cm.gainItem(4031755, -1);
            cm.gainItem(4031754, -1);
            cm.gainItem(4031758, -1);
            cm.gainItem(4005000, -7);
            cm.gainItem(4000021, -15);
            cm.gainItem(4031917, -4);

            cm.sendOk("You have successfully created the #v1082223# #t1082223#!");
            cm.dispose();
            return;
        }
        else
        {
            cm.sendOk("You do not fulfill the requirements to create #v1082223# #t1082223#!");
            cm.dispose();
            return;
        }
    }
    else if (status == 13) // Create iTCG Item Crystal Ilbi Throwing-Stars ( 2070016 )
    {
        if(cm.hasItem(4031754) && cm.hasItem(2070006, 1000) && cm.hasItem(4005003, 5) && cm.hasItem(4005004, 7) && cm.hasItem(4031917) && craftingLevel >= 6)
        {
            cm.gainItem(2070016);
            cm.gainItem(4031754, -1);
            cm.gainItem(4005003, -5);
            cm.gainItem(4005004, -7);
            cm.gainItem(4031917, -1);
            cm.gainItem(2070006, -1000);

            cm.sendOk("You have successfully created the #v2070016# #t2070016#!");
            cm.dispose();
            return;
        }
        else
        {
            cm.sendOk("You do not fulfill the requirements to create #v2070016# #t2070016#!");
            cm.dispose();
            return;
        }
    }
    else if (status == 14) // Create iTCG Item Crystal Leaf Earrings ( 1032048 )
    {
        if(cm.hasItem(4031758) && cm.hasItem(4021007, 2) && cm.hasItem(4031917, 2) && craftingLevel >= 8)
        {
            cm.gainItem(1032048);
            cm.gainItem(4031758, -1);
            cm.gainItem(4021007, -2);
            cm.gainItem(4031917, -2);

            cm.sendOk("You have successfully created the #v4031758# #t4031758#!");
            cm.dispose();
            return;
        }
        else
        {
            cm.sendOk("You do not fulfill the requirements to create #v4031758# #t4031758#!");
            cm.dispose();
            return;
        }
    }
    else if (status == 15) // Stat reroll
    {
        selectedSlot = selection;
        chosenItem = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection);

        if(Packages.server.MapleItemInformationProvider.getInstance().isCash(chosenItem.getItemId()) || Packages.server.MapleItemInformationProvider.getInstance().isTransmogrified(chosenItem) || (chosenItem.getItemId() >= 1142000 && chosenItem.getItemId() < 1143000))
        {
            cm.sendOk("You can not reroll the stats of medals, cash equip items, or items that has been transmogrified!");
            cm.dispose();
            return;
        }
        else
        {
            var defaultEquip = Packages.server.MapleItemInformationProvider.getInstance().getItemAsEquipById(chosenItem.getItemId());
            var selectedEquip = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getEquip(selection);

            if (defaultEquip.getUpgradeSlots() == selectedEquip.getUpgradeSlots())
            {
                cm.sendYesNo("To reroll the stats on your equip, you will need to fulfill the following requirements below.\r\n\r\n#e1 #v4031917# #t4031917#\r\n5 #v5221001# #t5221001#\r\n7 #v4001126# #t4001126#\r\n1,000,000 Mesos#n");
            }
            else
            {
                cm.sendOk("You can not reroll a equip that has been scrolled!");
                cm.dispose();
                return;
            }
        }
    }
    else if (status == 16)
    {
        if (cm.hasItem(4031917) && cm.hasItem(5221001, 5) && cm.hasItem(4001126, 7) && cm.getMeso() >= 1000000)
        {
            cm.gainItem(4031917, -1);
            cm.gainItem(5221001, -5);
            cm.gainItem(4001126, -7);
            cm.gainMeso(-1000000);
            Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, selectedSlot, 1, false);
            cm.gainItem(chosenItem.getItemId(), 1, true, false);

            cm.sendOk("The stats on your #v" + chosenItem.getItemId() + "# #t" + chosenItem.getItemId() + "# has been rerolled!");
            cm.dispose();
            return;
        }
        else
        {
            cm.sendOk("You do not fulfill the requirements to reroll the stats on your weapon!");
            cm.dispose();
            return;
        }
    }
}