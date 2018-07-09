// Enchanted Scroll Exchanger

var status;
var item;
var selected;
var number = 0;

var tensix = new Array(2040105, 2040106, 2040100, 2040101, 2040205, 2040302, 2040318, 2040323, 2040427, 2040502, 2040514, 2040517, 2040534, 2040619, 2040627, 2040702, 2040705, 2040708, 2040727, 2040802, 2040805, 2040925, 2040933, 2041002, 2041005, 2041014, 2041017, 2041020, 2041023, 2041058, 2043002, 2043102, 2043202, 2043302, 2043702, 2043802, 2044002, 2044102, 2044202, 2044302, 2044402, 2044502, 2044602, 2044702, 2044802, 2044902, 2048002, 2048005, 2040329, 2040330, 2040331, 2041102, 2041105, 2041108, 2041111, 2041302, 2041305, 2041308, 2041311, 2044015, 2040760, 2040206, 2040301, 2040317, 2040321, 2040425, 2040501, 2040513, 2040516, 2040532, 2040618, 2040625, 2040701, 2040704, 2040707, 2040801, 2040924, 2040931, 2041001, 2041004, 2041013, 2041016, 2041019, 2041022, 2043001, 2043101, 2043201, 2043301, 2043701, 2043801, 2044001, 2044101, 2044201, 2044301, 2044401, 2044501, 2044601, 2044701, 2044801, 2044901, 2048001, 2048004, 2048010, 2048011, 2048012, 2048013, 2040826, 2041101, 2041104, 2041110, 2041301, 2041304, 2041307, 2041310, 2040759, 2040025, 2040029, 2040413, 2040418, 2040613, 2040804, 2040817, 2040914, 2040919, 2043009, 2040026, 2040031, 2040412, 2040419, 2040612, 2040816, 2040915, 2040920, 2043008);
var thirtysev = new Array(2040108, 2040109, 2040103, 2040104, 2040013, 2040208, 2040305, 2040307, 2040322, 2040407, 2040426, 2040509, 2040519, 2040521, 2040533, 2040607, 2040626, 2040713, 2040715, 2040717, 2040809, 2040811, 2040907, 2040932, 2041027, 2041029, 2041035, 2041037, 2041039, 2041041, 2043005, 2043105, 2043205, 2043305, 2043705, 2043805, 2044005, 2044105, 2044205, 2044305, 2044405, 2044505, 2044605, 2044705, 2044804, 2044904, 2041113, 2041115, 2041117, 2041119, 2041313, 2041315, 2041317, 2041319, 2049201, 2049203, 2049205, 2049207, 2040012, 2040209, 2040304, 2040306, 2040320, 2040406, 2040424, 2040508, 2040518, 2040520, 2040531, 2040606, 2040624, 2040712, 2040714, 2040716, 2040808, 2040810, 2040906, 2040930, 2041026, 2041028, 2041034, 2041036, 2041038, 2041040, 2043004, 2043104, 2043204, 2043304, 2043704, 2043804, 2044004, 2044104, 2044204, 2044304, 2044404, 2044504, 2044604, 2044704, 2044803, 2044903, 2041112, 2041114, 2041116, 2041118, 2041312, 2041314, 2041316, 2041318, 2049105, 2049107, 2049108, 2049109, 2049110, 2049200, 2049202, 2049204, 2049206, 2040028, 2040410, 2040610, 2040814, 2040916, 2040921, 2043006, 2040030, 2040411, 2040611, 2040815, 2040917, 2043007);
var onehundred = new Array(2044400, 2040512, 2040515, 2041100, 2041103, 2041106, 2041109, 2041300, 2041303, 2041306, 2041309, 2040758, 2040202, 2040207, 2040316, 2040319, 2040530, 2040617, 2040803, 2040823, 2040923, 2040929, 2041006, 2041012, 2041015, 2041018, 2041021, 2048000, 2048003, 2040024, 2040027, 2040414, 2040417, 2040614, 2040818, 2040918, 2043010);

var exchangeable = new Array(2040002, 2040005, 2040100, 2040105, 2040200, 2040205, 2040302, 2040318, 2040323, 2040328, 2040402, 2040427, 2040502, 2040505, 2040514, 2040517, 2040534, 2040602, 2040619, 2040622, 2040627, 2040702, 2040705, 2040708, 2040727, 2040802, 2040805, 2040825, 2040925, 2040928, 2040933, 2041002, 2041005, 2041008, 2041011, 2041014, 2041017, 2041020, 2041023, 2041058, 2043002, 2043019, 2043102, 2043114, 2043202, 2043214, 2043302, 2043702, 2043802, 2044002, 2044014, 2044102, 2044114, 2044202, 2044214, 2044302, 2044314, 2044402, 2044414, 2044502, 2044602, 2044702, 2044802, 2044809, 2044902, 2048002, 2048005, 2040329, 2040330, 2040331, 2041102, 2041105, 2041108, 2041111, 2041302, 2041305, 2041308, 2041311, 2044015, 2040760, 2040001, 2040004, 2040023, 2040101, 2040106, 2040201, 2040206, 2040301, 2040317, 2040321, 2040326, 2040401, 2040425, 2040501, 2040504, 2040513, 2040516, 2040532, 2040601, 2040618, 2040621, 2040625, 2040701, 2040704, 2040707, 2040801, 2040824, 2040901, 2040924, 2040927, 2040931, 2041001, 2041004, 2041007, 2041010, 2041013, 2041016, 2041019, 2041022, 2041212, 2043001, 2043017, 2043101, 2043112, 2043201, 2043212, 2043301, 2043701, 2043801, 2044001, 2044012, 2044101, 2044112, 2044201, 2044212, 2044301, 2044312, 2044401, 2044412, 2044501, 2044601, 2044701, 2044801, 2044807, 2044901, 2048001, 2048004, 2048010, 2048011, 2048012, 2048013, 2040110, 2040111, 2040112, 2040113, 2040114, 2040115, 2040116, 2040117, 2040118, 2040119, 2041211, 2040826, 2041101, 2041104, 2041110, 2041301, 2041304, 2041307, 2041310, 2040759, 2040017, 2040025, 2040029, 2040311, 2040413, 2040418, 2040421, 2040613, 2040804, 2040817, 2040914, 2040919, 2043009, 2040016, 2040026, 2040031, 2040310, 2040412, 2040419, 2040422, 2040612, 2040816, 2040902, 2040915, 2040920, 2043008, 2040015, 2040030, 2040309, 2040411, 2040611, 2040815, 2040905, 2040917, 2043007, 2040014, 2040028, 2040308, 2040410, 2040610, 2040814, 2040904, 2040916, 2040921, 2043006, 2040008, 2040010, 2040012, 2040104, 2040109, 2040204, 2040209, 2040304, 2040306, 2040320, 2040325, 2040404, 2040406, 2040408, 2040424, 2040508, 2040510, 2040518, 2040520, 2040531, 2040604, 2040606, 2040608, 2040624, 2040712, 2040714, 2040716, 2040808, 2040810, 2040812, 2040906, 2040908, 2040930, 2041026, 2041028, 2041030, 2041032, 2041034, 2041036, 2041038, 2041040, 2043004, 2043016, 2043104, 2043111, 2043204, 2043211, 2043304, 2043704, 2043804, 2044004, 2044011, 2044104, 2044111, 2044204, 2044211, 2044304, 2044311, 2044404, 2044411, 2044504, 2044604, 2044704, 2044803, 2044806, 2044903, 2041112, 2041114, 2041116, 2041118, 2041312, 2041314, 2041316, 2041318, 2049105, 2049107, 2049108, 2049109, 2049110, 2049200, 2049202, 2049204, 2049206, 2049208, 2049210, 2040009, 2040011, 2040013, 2040103, 2040108, 2040203, 2040208, 2040303, 2040305, 2040307, 2040322, 2040327, 2040405, 2040407, 2040409, 2040426, 2040507, 2040509, 2040511, 2040519, 2040521, 2040533, 2040605, 2040607, 2040609, 2040626, 2040713, 2040715, 2040717, 2040809, 2040811, 2040813, 2040907, 2040909, 2040932, 2041027, 2041029, 2041031, 2041033, 2041035, 2041037, 2041039, 2041041, 2043005, 2043018, 2043105, 2043113, 2043205, 2043213, 2043305, 2043705, 2043805, 2044005, 2044013, 2044105, 2044113, 2044205, 2044213, 2044305, 2044313, 2044405, 2044413, 2044505, 2044605, 2044705, 2044804, 2044808, 2044904, 2041113, 2041115, 2041117, 2041119, 2041313, 2041315, 2041317, 2041319, 2049201, 2049203, 2049205, 2049207, 2049209, 2049211);

var selectedScrolls = new Array();

function start() 
{
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection)
{
    if (mode != 1) 
    {
        if(selectedScrolls.length >= 1)
        {
            for(var i = 0; i < selectedScrolls.length; i++)
            {
                cm.gainItem(selectedScrolls[i]);
            }

            cm.dispose();
            return;
        }
        else
        {
            cm.dispose();
            return;
        }
    }
    else
    {
        status++;
    }

    if (status == 0)
    {
        cm.sendSimple("If you want to trade your #v5221001# for random scrolls, you came to the right place! There are no useless scrolls such as HP/MP/DEF/Accuracy/Avoidability. What would you like to exchange your #t5221001# for?\r\n\r\n#eYou currently have #r" + cm.getPlayer().getItemQuantity(5221001, false) + "#k Enchanted Scroll.#n\r\n\r\n" +
            "#L8##eExchange 10 scrolls for #d1 #v5221001# Enchanted Scroll#k#l\r\n\r\n" + 
            "#L0##eExchange 1 #v5221001# for #d2 random 10% / 60% scroll#k#l\r\n" + 
            "#L5##eExchange 3 #v5221001# and 2 #v4001126# for #d1 pick of any 10% / 60% scroll#k#l\r\n" + 
            "#L4##eExchange 1 #v5221001# for #d3 random 100% scrolls#k#l\r\n" +
            "#L6##eExchange 3 #v5221001# and 2 #v4001126# for #d1 pick of any 100% scroll#k#l\r\n" +  
            "#L1##eExchange 3 #v5221001# for #d2 random 30% / 70% scroll#k#l\r\n" +
            "#L7##eExchange 5 #v5221001# and 4 #v4001126# for #d1 pick of any 30% / 70% scroll#k#l\r\n" +  
            "#L2##eExchange 15 #v5221001# for #d1 #v2049100# Chaos Scroll#k#l\r\n" +
            "#L3##eExchange 20 #v5221001# for #d1 #v2340000# White Scroll#k#l\r\n" +
            "#L9##eExchange 25 #v5221001# for #d1 #v2049019# Clean Slate Scroll 5% (No Boom)#k#l");
    }
    else if (status == 1)
    {
        if (selection == 0) 
        {
            if(cm.haveItem(5221001, 1))
            {
                item = tensix[Math.floor(Math.random()*tensix.length)];
                item2 = tensix[Math.floor(Math.random()*tensix.length)];
                cm.gainItem(5221001, -1); 
                cm.gainItem(item, 1);
                cm.gainItem(item2, 1);

                cm.logEnchanted("Random 10% / 60% Scrolls: " + item + " , " + item2);

                cm.sendOk("You have received the scroll: #v" + item + "#" + " #t" + item + "# and #v" + item2 + "#" + " #t" + item2 + "#"); 
                cm.dispose();
            }
            else
            {
                cm.sendOk("You do not have enough #t5221001#!"); 
                cm.dispose();
            }

        }
        else if (selection == 1)
        {
            if(cm.haveItem(5221001, 3))
            {
                item = thirtysev[Math.floor(Math.random()*thirtysev.length)];
                item2 = thirtysev[Math.floor(Math.random()*thirtysev.length)];
                cm.gainItem(5221001, -3); 
                cm.gainItem(item, 1);
                cm.gainItem(item2, 1);

                cm.logEnchanted("Random 30% / 70% Scrolls: " + item + " , " + item2);

                cm.sendOk("You have received the scroll: #v" + item + "#" + " #t" + item + "# and #v" + item2 + "#" + " #t" + item2 + "#");
                cm.dispose();
            }
            else
            {
                cm.sendOk("You do not have enough #t5221001#!"); 
                cm.dispose();
            }
        }
        else if (selection == 2)
        {
            if(cm.haveItem(5221001, 15))
            {
                if (cm.canHold(2049100))
                {
                    cm.gainItem(5221001, -15); 
                    cm.gainItem(2049100, 1);
                    cm.logEnchanted("Chaos Scroll");
                    cm.sendOk("You have received the scroll: #v2049100# #t2049100#"); 
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold a #t2049100#!");
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("You do not have enough #t5221001#!"); 
                cm.dispose();
            }
        }
        else if (selection == 3)
        {
            if(cm.haveItem(5221001, 20))
            {
                if (cm.canHold(2340000))
                {
                    cm.gainItem(5221001, -20); 
                    cm.gainItem(2340000, 1);
                    cm.logEnchanted("White Scroll");
                    cm.sendOk("You have received the scroll: #v2340000# #t2340000#"); 
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold a #t2340000#!");
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("You do not have enough #t5221001#!"); 
                cm.dispose();
            }
        }
        else if (selection == 4)
        {
            if(cm.haveItem(5221001, 1))
            {
                item = onehundred[Math.floor(Math.random()*onehundred.length)];
                item2 = onehundred[Math.floor(Math.random()*onehundred.length)];
                item3 = onehundred[Math.floor(Math.random()*onehundred.length)];
                cm.gainItem(5221001, -1); 
                cm.gainItem(item, 1);
                cm.gainItem(item2, 1);
                cm.gainItem(item3, 1);

                cm.logEnchanted("Random 100% Scrolls: " + item + " , " + item2 + " , " + item3);

                cm.sendOk("You have received the scrolls: #v" + item + "#" + " #t" + item + "# and #v" + item2 + "#" + " #t" + item2 + "# and #v" + item3 + "#" + " #t" + item3 + "#"); 
                cm.dispose();
            }
            else
            {
                cm.sendOk("You do not have enough #t5221001#!"); 
                cm.dispose();
            }
        }
        else if (selection == 5)
        {
            if(cm.haveItem(5221001, 3) && cm.haveItem(4001126, 2))
            {
                var outStr = "Which scroll would you like?\r\n\r\n";

                for(var i = 0; i < tensix.length; i++)
                {
                    var scroll = tensix[i];
                    outStr += "#L" + i + "##v" + scroll + "# #t" + scroll + "##l\r\n";
                }

                cm.sendSimple(outStr);
            }
            else
            {
                cm.sendOk("You do not have enough #t5221001# or #t4001126#!"); 
                cm.dispose();
            }
        }
        else if (selection == 6)
        {
            if(cm.haveItem(5221001, 3) && cm.haveItem(4001126, 2))
            {
                var outStr = "Which scroll would you like?\r\n\r\n";

                for(var i = 0; i < onehundred.length; i++)
                {
                    var scroll = onehundred[i];
                    outStr += "#L" + i + "##v" + scroll + "# #t" + scroll + "##l\r\n";
                }

                status = 2;
                cm.sendSimple(outStr);
            }
            else
            {
                cm.sendOk("You do not have enough #t5221001# or #t4001126#!"); 
                cm.dispose();
            }
        }
        else if (selection == 7)
        {
            if(cm.haveItem(5221001, 5) && cm.haveItem(4001126, 4))
            {
                var outStr = "Which scroll would you like?\r\n\r\n";

                for(var i = 0; i < thirtysev.length; i++)
                {
                    var scroll = thirtysev[i];
                    outStr += "#L" + i + "##v" + scroll + "# #t" + scroll + "##l\r\n";
                }

                status = 3;
                cm.sendSimple(outStr);
            }
            else
            {
                cm.sendOk("You do not have enough #t5221001# or #t4001126#!"); 
                cm.dispose();
            }
        }
        else if (selection == 8)
        {
            status = 4;
            action(1, 0, 0);
        }
        else if (selection == 9)
        {
            if(cm.haveItem(5221001, 25))
            {
                if (cm.canHold(2049019))
                {
                    cm.gainItem(5221001, -25); 
                    cm.gainItem(2049019, 1);
                    cm.logEnchanted("Clean Slate Scroll 5%");
                    cm.sendOk("You have received the scroll: #v2049019# #t2049019#"); 
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold a #t2049019#!");
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("You do not have enough #t5221001#!"); 
                cm.dispose();
            }
        }
    }
    else if (status == 2) // 10% / 60%
    {
        var scroll = tensix[selection];

        if (cm.canHold(scroll))
        {
            cm.gainItem(scroll);
            cm.gainItem(5221001, -3);
            cm.gainItem(4001126, -2);

            cm.logEnchanted("Chosen 10% / 60% Scroll: " + scroll);

            cm.sendOk("You have received the scroll: #v" + scroll + "# #t" + scroll + "#");
            cm.dispose();
        }
        else
        {
            cm.sendOk("Please make sure you have enough space to hold a #t" + scroll + "#!");
            cm.dispose();
        }
    }
    else if (status == 3) // 100%
    {
        var scroll = onehundred[selection];

        if (cm.canHold(scroll))
        {
            cm.gainItem(scroll);
            cm.gainItem(5221001, -3);
            cm.gainItem(4001126, -2);

            cm.logEnchanted("Chosen 100% Scroll: " + scroll);

            cm.sendOk("You have received the scroll: #v" + scroll + "# #t" + scroll + "#");
            cm.dispose();
        }
        else
        {
            cm.sendOk("Please make sure you have enough space to hold a #t" + scroll + "#!");
            cm.dispose();
        }
    }
    else if (status == 4) // 30% / 70%
    {
        var scroll = thirtysev[selection];

        if (cm.canHold(scroll))
        {
            cm.gainItem(scroll);
            cm.gainItem(5221001, -5);
            cm.gainItem(4001126, -4);

            cm.logEnchanted("Chosen 30% / 70% Scroll: " + scroll);

            cm.sendOk("You have received the scroll: #v" + scroll + "# #t" + scroll + "#");
            cm.dispose();
        }
        else
        {
            cm.sendOk("Please make sure you have enough space to hold a #t" + scroll + "#!");
            cm.dispose();
        }
    }
    else if (status == 5)
    {
        cm.sendSimple("Choose which scroll you want to exchange! #eAs a side note, you can not exchange all of your 100% scrolls.#n" + " \r\n\r\n#e#b" + number + " / 10 Scroll(s) Selected#n#k\r\n\r\n" + cm.UseList(cm.getClient()));
    }
    else if (status == 6)
    {
        var scrollID = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.USE).getItem(selection).getItemId();

        if(exchangeable.indexOf(scrollID) == -1)
        {
            status = 4;
            cm.sendOk("This is not a scroll that you can exchange! Please try again.");
        }
        else
        {
            number++;
            selectedScrolls.push(scrollID);
            cm.gainItem(scrollID, -1);

            if (number == 10)
            {
                var outStr = "These are the scrolls you selected. Do you wish to continue?\r\n\r\n";

                for(var i = 0; i < selectedScrolls.length; i++)
                {
                    outStr += "#v" + selectedScrolls[i] + "# #t" + selectedScrolls[i] + "#\r\n";
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

        cm.sendOk("You have exchanged your 10 scrolls for a Enchanted Scroll!");
        cm.dispose();
    }
    else 
    {
        cm.dispose();
    }
}  