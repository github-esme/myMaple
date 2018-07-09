// Halloween NPC

var status;

var normal = new Array(2020013, 2000005, 2022069, 2000001, 2022256, 4031830, 4000075, 2022113, 2030000, 4031816, 2210006, 1112107, 1082077, 1012074, 5121006, 2022068);
var rare = new Array(5221001, 4032444, 4032445, 4032446, 2022273, 2360001, 5050000, 1000003, 3010501, 3010043, 3010040, 1050057, 5010026);
var epic = new Array(1112920, 1052067, 1012044, 1002525, 3010096, 1002526, 1082157, 1052068, 1702246, 1102095, 1102096, 1102097);
var unique = new Array(1142249, 1102086, 2022282, 2340000, 2049100);
var legendary = new Array(1142887, 1102084, 1132016, 1112414);

var halloween = new Packages.server.events.HalloweenGachapon;

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
        var outStr = "Happy Halloween! I only appear during the Halloween event so you probably won't see me again soon. What would you like to do?\r\n\r\n";
        outStr += "#e#b#L0#Try your luck with the spooky gachapon!#l\r\n";
        outStr += "#L1#Exchange your Halloween Stick Candy#l\r\n";
        outStr += "#L2#What can I win from this gachapon?#l\r\n";
        outStr += "#L3#Exchange #v4032119# #t4032119# for 5 #v4031203# #t4031203##k#n#l";

        cm.sendSimple(outStr);
    }
    else if (status == 1)
    {
        if (selection == 0)
        {
            cm.sendYesNo("You will need at least one #v4031203# #t4031203# to do use the gachapon. #eAll mobs have a chance to drop the candy at a 1/1500 chance (1/2000 at KSQ).#n Would you like to use the gachapon?\r\n\r\n#eYou currently have #r" + cm.getPlayer().getItemQuantity(4031203, false) + "#k Halloween Candies.#n\r\n\r\n");
        }
        else if (selection == 1)
        {
            status = 2;
            cm.sendYesNo("You will need the following Halloween Stick Candy below to exchange them for 5 #v4031203# #t4031203#. Do you have the required Halloween Stick Candy?\r\n\r\n#e1 #v4032444# #t4032444#\r\n1 #v4032445# #t4032445#\r\n1 #v4032446# #t4032446##n");
        }
        else if (selection == 2)
        {
            // var outStr = "Below are the items that you can win from this gachapon! Be warned, you may be tricked or treated.\r\n\r\n";
            // outStr += "#e---- Normal ( 65% Chance ) ----#n\r\n"

            // for (var i = 0; i < normal.length; i++)
            // {
            //     outStr += "#v" + normal[i] + "# #t" + normal[i] + "#\r\n";
            // }

            // outStr += "\r\n#e---- Rare ( 25% Chance ) ----#n\r\n"

            // for (var i = 0; i < rare.length; i++)
            // {
            //     outStr += "#v" + rare[i] + "# #t" + rare[i] + "#\r\n";
            // }

            // outStr += "\r\n#e---- Epic ( 9% Chance ) ----#n\r\n"

            // for (var i = 0; i < epic.length; i++)
            // {
            //     outStr += "#v" + epic[i] + "# #t" + epic[i] + "#\r\n";
            // }

            // outStr += "\r\n#e---- Unique ( 0.9% Chance ) ----#n\r\n"

            // for (var i = 0; i < unique.length; i++)
            // {
            //     outStr += "#v" + unique[i] + "# #t" + unique[i] + "#\r\n";
            // }

            // outStr += "\r\n#e---- Legendary ( 0.1% Chance ) ----#n\r\n"

            // for (var i = 0; i < legendary.length; i++)
            // {
            //     outStr += "#v" + legendary[i] + "# #t" + legendary[i] + "#\r\n";
            // }

            var outStr = "Oh boy, that's a mystery. What I can tell you is that you have a chance to get items in any of the categories below. You may be really lucky or really unlucky. Let's just say that you have a possibility of getting better items than a Chaos Scroll or White Scroll.\r\n\r\n";
            outStr += "#eNormal Items: #b65% Chance#k\r\n";
            outStr += "Rare Items: #b25% Chance#k\r\n";
            outStr += "Epic Items: #b9% Chance#k\r\n";
            outStr += "Unique Items: #b0.9% Chance#k\r\n";
            outStr += "Legendary Items: #b0.1% Chance#k#n";
            

            cm.sendOk(outStr);
            cm.dispose();
        }
        else if (selection == 3)
        {
            if (cm.hasItem(4032119))
            {
                if (cm.canHold(4031203, 5))
                {
                    cm.gainItem(4032119, -1);
                    cm.gainItem(4031203, 5);

                    cm.sendOk("You have received 5 #v4031203# #t4031203#!");
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
                cm.sendOk("You do not have a #v4032119# #t4032119#!");
                cm.dispose();
            }
        }
    }
    else if (status == 2)
    {
        if (cm.hasItem(4031203))
        {
            var ranNumber = getRandomInt(0, 1000);

            if (ranNumber <= 650) 
            {
                var item = normal[Math.floor(Math.random()*normal.length)];

                if (cm.canHold(5360006) && cm.canHold(4220109) && cm.canHold(2043203) && cm.canHold(1402009) && cm.canHold(3990017))
                {
                    cm.gainItem(item);
                    cm.logHalloweenGachapon(item);
                    cm.gainItem(4031203, -1);

                    cm.sendOk("You have gained a Normal Item: #v" + item + "# #t" + item + "#!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Please make room in your inventory!");
                    cm.dispose();
                }
            } 
            else if (ranNumber <= 900) 
            {
                var item = rare[Math.floor(Math.random()*rare.length)];

                if (cm.canHold(5360006) && cm.canHold(4220109) && cm.canHold(2043203) && cm.canHold(1402009) && cm.canHold(3990017))
                {
                    cm.gainItem(item);
                    cm.logHalloweenGachapon(item);
                    cm.gainItem(4031203, -1);

                    cm.sendOk("You have gained a Rare Item: #v" + item + "# #t" + item + "#!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Please make room in your inventory!");
                    cm.dispose();
                }
            } 
            else if (ranNumber <= 994) 
            {
                var item = epic[Math.floor(Math.random()*epic.length)];

                if (cm.canHold(5360006) && cm.canHold(4220109) && cm.canHold(2043203) && cm.canHold(1402009) && cm.canHold(3990017))
                {
                    cm.gainItem(item);
                    cm.logHalloweenGachapon(item);
                    cm.gainItem(4031203, -1);

                    cm.sendOk("You have gained a Epic Item: #v" + item + "# #t" + item + "#!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Please make room in your inventory!");
                    cm.dispose();
                }
            } 
            else if (ranNumber <= 999) 
            {
                var item = unique[Math.floor(Math.random()*unique.length)];

                if (cm.canHold(5360006) && cm.canHold(4220109) && cm.canHold(2043203) && cm.canHold(1402009) && cm.canHold(3990017))
                {
                    if (item == 1142249)
                    {
                        halloween.gainLuckyGuy(cm.getPlayer());
                    }
                    else
                    {
                        cm.gainItem(item);
                    }

                    cm.logHalloweenGachapon(item);
                    cm.gainItem(4031203, -1);
                    halloween.broadcastGachapon(cm.getPlayer(), item);

                    cm.sendOk("You have gained a Unique Item: #v" + item + "# #t" + item + "#!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Please make room in your inventory!");
                    cm.dispose();
                }
            } 
            else if (ranNumber == 1000) 
            {
                var item = legendary[Math.floor(Math.random()*legendary.length)];

                if (cm.canHold(5360006) && cm.canHold(4220109) && cm.canHold(2043203) && cm.canHold(1402009) && cm.canHold(3990017))
                {
                    if (item == 1142887)
                    {
                        halloween.gainSpooky(cm.getPlayer());
                    }
                    else
                    {
                        cm.gainItem(item);
                    }

                    cm.logHalloweenGachapon(item);
                    cm.gainItem(4031203, -1);
                    halloween.broadcastGachapon(cm.getPlayer(), item);

                    cm.sendOk("You have gained a Legendary Item: #v" + item + "# #t" + item + "#!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Please make room in your inventory!");
                    cm.dispose();
                }
            }
        }
        else
        {
            cm.sendOk("You do not have any #v4031203# #t4031203#!");
            cm.dispose();
        }
    }
    else if (status == 3)
    {
        if (cm.hasItem(4032444) && cm.hasItem(4032445) && cm.hasItem(4032446))
        {
            if (cm.canHold(4031203, 5))
            {
                cm.gainItem(4032444, -1);
                cm.gainItem(4032445, -1);
                cm.gainItem(4032446, -1);
                
                cm.gainItem(4031203, 5);
            }
            else
            {
                cm.sendOk("Please make room in your inventory!");
                cm.dispose();
            }
        }
        else
        {
            cm.sendOk("You do not have the required items!");
            cm.dispose();
        }
    }
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}