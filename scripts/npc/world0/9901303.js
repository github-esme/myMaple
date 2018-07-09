// Streamer

var status;

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
        var outStr = "Hello, you can exchange your streamer points with me! If you are not a verified streamer and want to get streamer points by streaming myMaple, please contact a staff. You will gain 1 streamer point per minute for streaming.\r\n\r\n";
        outStr += "#eYou currently have #r" + cm.getStreamerPoints() + "#k streamer points.#n\r\n\r\n";

        outStr += "#e#L0#Exchange 60 streamer points for 5 #v5041000# #dVIP Teleport Rock#k#l\r\n";
        outStr += "#e#L1#Exchange 180 streamer points for #d30 minutes #v5211048# 2X EXP card#k#l\r\n";
        outStr += "#e#L2#Exchange 300 streamer points for #d1 hour #v5211048# 2X EXP card#k#l\r\n";
        outStr += "#e#L3#Exchange 300 streamer points for #d1 donator point#k#l\r\n";
        outStr += "#e#L4#Exchange 300 streamer points for #d1 #v5221001# #t5221001##k#l\r\n";

        cm.sendSimple(outStr);

        //cm.sendOk("The streamer NPC is under construction.");
    }
    else if (status == 1) 
    {
        if(selection == 0)
        {  
            if (cm.getStreamerPoints() >= 50)
            {
                if(cm.canHold(5041000, 5))
                {
                    cm.gainItem(5041000, 5);
                    cm.getPlayer().addStreamerPoints(-50);
                    cm.sendOk("Here is your VIP Teleport Rock!");
                    cm.logFishing("VIP Teleport Rock");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold a VIP Teleport Rock!");
                }
            }
            else
            {
                cm.sendOk("Sorry, you don't have enough Streamer Points!");
            }

            cm.dispose();
        }
        else if(selection == 1)
        {  
            if (cm.getStreamerPoints() >= 180)
            {
                if(cm.canHold(5211048))
                {
                    cm.gainItem(5211048, 1, false, false, 1800000);
                    cm.getPlayer().addStreamerPoints(-180);
                    cm.sendOk("You have received a 30 minutes EXP card!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this EXP card!");
                }
            }
            else
            {
                cm.sendOk("Sorry, you don't have enough Streamer Points!");
            }

            cm.dispose();
        }
        else if(selection == 2)
        {  
            if (cm.getStreamerPoints() >= 300)
            {
                if(cm.canHold(5211048))
                {
                    cm.gainItem(5211048, 1, false, false, 3600000);
                    cm.getPlayer().addStreamerPoints(-300);
                    cm.sendOk("You have received a 1 hour EXP card!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this EXP card!");
                }
            }
            else
            {
                cm.sendOk("Sorry, you don't have enough Streamer Points!");
            }

            cm.dispose();
        }
        else if(selection == 3)
        {  
            if (cm.getStreamerPoints() >= 300)
            {
                cm.getClient().addDonatorPoints(1);
                cm.getPlayer().addStreamerPoints(-300);
                cm.sendOk("You have received a donator point!");
            }
            else
            {
                cm.sendOk("Sorry, you don't have enough Streamer Points!");
            }

            cm.dispose();
        }
        else if(selection == 4)
        {  
            if (cm.getStreamerPoints() >= 300)
            {
                if(cm.canHold(5221001))
                {
                    cm.gainItem(5221001);
                    cm.getPlayer().addStreamerPoints(-300);
                    cm.sendOk("You have received an Enchanted Scroll!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this Enchanted Scroll!");
                }
            }
            else
            {
                cm.sendOk("Sorry, you don't have enough Streamer Points!");
            }

            cm.dispose();
        }
    }
}