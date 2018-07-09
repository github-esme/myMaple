// NimaKIN

var status = 0;
var beauty = 0;
var haircolor = Array();
var skin = Array(0, 1, 2, 3, 4, 5, 9, 10);
var hair = Array(31000, 31010, 31020, 31030, 31040, 31050, 31060, 31070, 31080, 31090, 31100, 31110, 31120, 31130, 31140, 31150, 31160, 31170, 31180, 31190, 31200, 31210, 31220, 31230, 31240, 31250, 31260, 31270, 31280, 31290, 31300, 31310, 31320, 31330, 31340, 31350, 31400, 31410, 31420, 31440, 31450, 31460, 31470, 31480, 31490, 31510, 31520, 31530, 31540, 31550, 31560, 31570, 31580, 31590, 31600, 31610, 31620, 31630, 31640, 31650, 31670, 31680, 31690, 31700, 31710, 31720, 31730, 31740, 31750, 31760, 31770, 31780, 31790, 31800);
var donatorhair = Array(37200, 37040, 38500, 38610, 38040, 38600, 38760, 37700, 32570, 31360, 31370, 31380, 31430, 31660, 31810, 31820, 31830, 31840, 31850, 31860, 31870, 31880, 31890, 31900, 31910, 31920, 31930, 31940, 31950, 31960, 31970, 31980, 31990, 34000, 34010, 34020, 34030, 34040, 34050, 34060, 34070, 34080, 34090, 34100, 34110, 34120, 34130, 34140, 34150, 34160, 34170, 34180, 34190, 34200, 34210, 34220, 34230, 34240, 34250, 34260, 34270, 34280, 34290, 34300, 34310, 34320, 34330, 34340, 34350, 34360, 34370, 34380, 34390, 34400, 34410, 34420, 34430);
var donatorhair2 = Array(38070, 38310, 38150, 38470, 38230, 37230, 38760, 34440, 34450, 34460, 34470, 34480, 34490, 34510, 34540, 34560, 34580, 34590, 34600, 34610, 34620, 34630, 34640, 34650, 34660, 34670, 34680, 34690, 34700, 34710, 34720, 34730, 34740, 34750, 34760, 34770, 34780, 34790, 34800, 34810, 34820, 34830, 34840, 34850, 34860, 34870, 34880, 34890, 34900, 34910, 34940, 34950, 34960, 34970, 34980, 34990, 35210, 37560, 37690, 37710, 37750, 37770, 37780, 37790, 37920, 38010);

var hairnew = Array();
var face = Array(21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21009, 21010, 21011, 21012, 21013, 21014, 21016, 21017, 21018, 21019, 21020, 21021, 21022, 21024);
var donatorface = Array(24008, 21025, 21026, 21027, 21028, 21029, 21030, 21031, 21033, 21034, 21035, 21036, 21037, 21038, 21041, 21042, 21043, 21044, 21045, 21046, 21047, 21048, 21049, 21050, 21051, 21052, 21053, 21054, 21055, 21056, 21057, 21058, 21059, 21060, 21061, 21062, 21063, 21064, 21065, 21066, 21067, 21068, 21069, 21070, 21071, 21072, 21073, 21074, 21075, 21076, 21077, 21078, 21079, 21080, 21081, 21082, 21083, 21084, 21085, 21086, 21087, 21088, 21089, 21090, 21091, 21092, 21093, 21094, 21095, 21096, 21097, 21098, 21099);
var facenew = Array();
var colors = Array();

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
            if (cm.getPlayer().getGender() == 0 && !cm.getPlayer().isIntern())
            {
                cm.sendOk("I'm sorry, I only do Females. You should try my sister KIN.");
                cm.dispose();
            }
            else
            {
                cm.sendSimple("Hey there, I can change your look. What would you like to change?\r\n#e#b#L0#Skin#l\r\n#L1#Donator Hair#l\r\n#L7#Donator Hair 2#l\r\n#L2#Hair#l\r\n#L3#Hair Color#l\r\n#L4#Donator Eye#l\r\n#L5#Eye#l\r\n#L6#Eye Color#l#k#n");
            }
        }
        else if (status == 1)
        {
            if (selection == 0)
            {
                beauty = 1;
                cm.sendStyle("Pick one?", skin);
            }
            else if (selection == 1)
            {
                status = 5;
                cm.sendYesNo("It will cost you 1 donator point to use the donator stylist. If you can't find the hair you want, you can request your specific hair by providing us the hair ID. No refunds will be given. Would you like to continue?");
            }
            else if (selection == 2)
            {
                beauty = 2;
                hairnew = Array();
                for(var i = 0; i < hair.length; i++)
                {
                    hairnew.push(hair[i]);
                }
                cm.sendStyle("Pick one?", hairnew);
            }
            else if (selection == 3)
            {
                beauty = 3;
                haircolor = Array();
                var current = parseInt(cm.getPlayer().getHair()/10)*10;
                for(var i = 0; i < 8; i++)
                {
                    haircolor.push(current + i);
                }
                cm.sendStyle("Pick one?", haircolor);
            }
            else if (selection == 4)
            {
                status = 6;
                cm.sendYesNo("It will cost you 1 donator point to use the donator stylist. No refunds will be given. Would you like to continue?");
            }
            else if (selection == 5)
            {
                beauty = 4;
                facenew = Array();
                for(var i = 0; i < face.length; i++)
                {
                    facenew.push(face[i]);
                }
                cm.sendStyle("Pick one?", facenew);
            }
            else if (selection == 6)
            {
                beauty = 5;
                var current = cm.getPlayer().getFace() % 100 + 21000;

                if (cm.getPlayer().getFace() >= 24008)
                {
                    current = cm.getPlayer().getFace() % 100 + 24000;
                }

                //var current = cm.getPlayer().getFace();
                colors = Array();
                colors = Array(current , current + 100, current + 200, current + 300, current +400, current + 500, current + 600, current + 700, current + 800);
                cm.sendStyle("Pick one?", colors);
            }
            else if (selection == 7)
            {
                status = 7;
                cm.sendYesNo("It will cost you 1 donator point to use the donator stylist. If you can't find the hair you want, you can request your specific hair by providing us the hair ID. No refunds will be given. Would you like to continue?");
            }
        }
        else if (status == 2)
        {
            if (beauty == 1)
            {
                cm.setSkin(skin[selection]);
            }
            if (beauty == 2)
            {
                cm.setHair(hairnew[selection]);
            }
            if (beauty == 3)
            {
                cm.setHair(haircolor[selection]);
            }
            if (beauty == 4)
            {
                cm.setFace(facenew[selection]);
            }
            if (beauty == 5)
            {
                cm.setFace(colors[selection]);
            }

            cm.dispose();
        }
        else if (status == 6) // Donator Hair
        {
            if(cm.getDonatorPoints() < 1)
            {
                cm.sendOk("You do not have any donator points.");
                cm.dispose();
            }
            else
            {
                beauty = 1; status = 9;
                hairnew = Array();
                for(var i = 0; i < donatorhair.length; i++)
                {
                    hairnew.push(donatorhair[i]);
                }

                cm.sendStyle("Pick one?", hairnew);
            }
        }
        else if (status == 7) // Donator Eyes
        {
            if(cm.getDonatorPoints() < 1)
            {
                cm.sendOk("You do not have any donator points.");
                cm.dispose();
            }
            else
            {
                beauty = 2; status = 9;
                facenew = Array();
                for(var i = 0; i < donatorface.length; i++)
                {
                    facenew.push(donatorface[i]);
                }
                cm.sendStyle("Pick one?", facenew);
            }
        }
        else if (status == 8) // Donator Hair 2
        {
            if(cm.getDonatorPoints() < 1)
            {
                cm.sendOk("You do not have any donator points.");
                cm.dispose();
            }
            else
            {
                beauty = 1; status = 9;
                hairnew = Array();
                for(var i = 0; i < donatorhair2.length; i++)
                {
                    hairnew.push(donatorhair2[i]);
                }

                cm.sendStyle("Pick one?", hairnew);
            }
        }
        else if (status == 10)
        {
            if (beauty == 1)
            {
                cm.setHair(hairnew[selection]);
                cm.getPlayer().getClient().addDonatorPoints(-1);
            }
            else if (beauty == 2)
            {
                cm.setFace(facenew[selection]);
                cm.getPlayer().getClient().addDonatorPoints(-1);
            }

            cm.dispose();
        }
    }
}