// Enchanted Scroll Exchanger

var status;
var item;
var selected;
var number = 0;
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
        cm.sendSimple("If you want to trade your #v5221001# for random scrolls, you came to the right place! What would you like to exchange your #t5221001# for?\r\n\r\n#eYou currently have #r" + cm.getPlayer().getItemQuantity(5221001, false) + "#k Enchanted Scroll.#n\r\n\r\n" +
            "#L0##eExchange 15 #v5221001# for #d1 #v2049100# Chaos Scroll#k#l\r\n" +
            "#L1##eExchange 20 #v5221001# for #d1 #v2340000# White Scroll#k#l\r\n" +
            "#L2##eExchange 25 #v5221001# for #d1 #v2049019# Clean Slate Scroll 5% (No Boom)#k#l");
    }
    else if (status == 1)
    {    
        if (selection == 0)
        {
            cm.sendGetNumber("How many Chaos Scrolls would you like to buy?\r\n\r\n#eYou currently have #r" + cm.getPlayer().getItemQuantity(5221001, false) + "#k Enchanted Scrolls.#n", 1, 1, 1000);
        }
        else if (selection == 1)
        {
            status = 2;
            cm.sendGetNumber("How many White Scrolls would you like to buy?\r\n\r\n#eYou currently have #r" + cm.getPlayer().getItemQuantity(5221001, false) + "#k Enchanted Scrolls.#n", 1, 1, 1000);
        }
        else if (selection == 2)
        {
            status = 3;
            cm.sendGetNumber("How many Clean Slate Scroll 5% (No Boom) would you like to buy?\r\n\r\n#eYou currently have #r" + cm.getPlayer().getItemQuantity(5221001, false) + "#k Enchanted Scrolls.#n", 1, 1, 1000);
        }
    }
    else if (status == 2)
    {
        if(cm.haveItem(5221001, 15 * selection))
        {
            if (cm.canHold(2049100, selection))
            {
                cm.gainItem(5221001, -(15 * selection)); 
                cm.gainItem(2049100, selection);
                cm.logEnchanted(selection + " Chaos Scroll");
                cm.sendOk("You have received " + selection + " #v2049100# #t2049100#"); 
                cm.dispose();
            }
            else
            {
                cm.sendOk("Please make sure you have enough space to hold " + selection + " #t2049100#!");
                cm.dispose();
            }
        }
        else
        {
            cm.sendOk("You do not have enough #t5221001#!"); 
            cm.dispose();
        }
    }
    else if (status == 3)
    {
        if(cm.haveItem(5221001, 20 * selection))
        {
            if (cm.canHold(2340000, selection))
            {
                cm.gainItem(5221001, -(20 * selection)); 
                cm.gainItem(2340000, selection);
                cm.logEnchanted(selection + " White Scroll");
                cm.sendOk("You have received " + selection + " #v2340000# #t2340000#"); 
                cm.dispose();
            }
            else
            {
                cm.sendOk("Please make sure you have enough space to hold " + selection + " #t2340000#!");
                cm.dispose();
            }
        }
        else
        {
            cm.sendOk("You do not have enough #t5221001#!"); 
            cm.dispose();
        }
    }
    else if (status == 4)
    {
        if(cm.haveItem(5221001, 25 * selection))
        {
            if (cm.canHold(2049019, selection))
            {
                cm.gainItem(5221001, -(25 * selection)); 
                cm.gainItem(2049019, selection);
                cm.logEnchanted(selection + " Clean Slate Scroll 5% (No Boom)");
                cm.sendOk("You have received " + selection + " #v2049019# #t2049019#"); 
                cm.dispose();
            }
            else
            {
                cm.sendOk("Please make sure you have enough space to hold " + selection + " #t2049019#!");
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