// Event Trophy Exchanger

var status = 0;

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
        var outStr = "What up winner! You got any Event Trophy? I'll gladly take them off of your hands. What would you like?\r\n\r\n";
        outStr += "#eYou currently have #r#c4000038##k Event Trophy.\r\n\r\n";
        outStr += "#L0#Exchange 1 Event Trophy for #d10,000 NX#k#l\r\n";
        outStr += "#L1#Exchange 1 Event Trophy for #d1 donator point#k#l\r\n";
        outStr += "#L2#Exchange 1 Event Trophy for #d1 hour #v5211048# 2X EXP card#k#l\r\n";
        outStr += "#L3#Exchange 1 Event Trophy for #d3 #v5221001# Enchanted Scroll#k#l\r\n";
        //outStr += "#L4#Exchange 1 Event Trophy for #d3 #v4001126# Maple Leaf#k#l#n\r\n";

        cm.sendSimple(outStr);
    }
    else if (status == 1)
    {
        if (selection == 0)
        {
            if(cm.haveItem(4000038, 1)) 
            {
                cm.getPlayer().getCashShop().gainCash(1, 10000);
                cm.getPlayer().announce(Packages.tools.MaplePacketCreator.earnTitleMessage("You have received 10,000 NX."));
                cm.gainItem(4000038, -1);
                cm.sendOk("Here is your 10,000 NX!");
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Event Trophy!");
            }
            cm.dispose();
        }
        else if (selection == 1)
        {
            if(cm.haveItem(4000038, 1)) 
            {
                cm.getClient().addDonatorPoints(1);
                cm.gainItem(4000038, -1);
                cm.sendOk("Here are your donator points!");
            }
            else 
            {
                cm.sendOk("Sorry, you don't have enough Event Trophy!");
            }
            cm.dispose();
        }
        else if (selection == 2)
        {
            if(cm.haveItem(4000038, 1)) 
            {
                if(cm.canHold(5211048))
                {
                    cm.gainItem(5211048, 1, false, false, 3600000);
                    cm.gainItem(4000038, -1);
                    cm.getPlayer().setRates();
                    cm.sendOk("Here is your 1 hour EXP card!");
                }
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold this EXP card!");
                }

            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Event Trophy!");
            }
            cm.dispose();
        }
        else if (selection == 3)
        {
            if(cm.haveItem(4000038, 1)) 
            {
                if(cm.canHold(5221001))
                {
                    cm.gainItem(5221001, 3);
                    cm.gainItem(4000038, -1);
                    cm.sendOk("Here is your 3 Enchanted Scroll!");
                }
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold these Enchanted Scrolls!");
                }

            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Event Trophy!");
            }
            cm.dispose();
        }
        else if (selection == 4)
        {
            if(cm.haveItem(4000038, 1)) 
            {
                if(cm.canHold(4001126))
                {
                    cm.gainItem(4001126, 3);
                    cm.gainItem(4000038, -1);
                    cm.sendOk("Here is your 3 Maple Leaf!");
                }
                else
                {
                    cm.sendOk("Please make sure you have enough space to hold these Maple Leaves!");
                }

            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Event Trophy!");
            }
            cm.dispose();
        }
    }
}