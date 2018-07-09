// Mounts for Aran in Rien

var status = -1;

function start()
{
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
        cm.sendSimple("Hey! I sell mounts to Cygnus and Aran players due to the original quests to obtain them being broken. What mount are you interested in?\r\n\r\n#b#e#L0#Cygnus Mounts#l\r\n#L1#Aran Mounts#l#k#n");
    }
    else if (status == 1)
    {
        if(selection == 0)
        {
            cm.sendSimple("What would you like to purchase?\r\n\r\n#e#d#L0##v1902005# #t1902005##l#k\r\n\r\n#bRequirements:#k#n\r\n#eRequired Level:#n 50\r\n#eRequired Mesos:#n 1,000,000\r\r\n\n#e#d#L1##v1902006# #t1902006##l#k\r\n\r\n#bRequirements:#k#n\r\n\r\n#eRequired Level:#n 100\r\n#eRequired Item:#n 90 #v4000263# #t4000263#\r\n#eRequired Item:#n 90 #v4000262# #t4000262#\r\n#eRequired Item:#n 90 #v4000265# #t4000265#\r\n#eRequired Item:#n 90 #v4000236# #t4000236#\r\n#eRequired Item:#n 90 #v4000237# #t4000237#\r\n#eRequired Item:#n 90 #v4000238# #t4000238#\r\n#eRequired Item:#n 90 #v4000239# #t4000239#\r\n#eRequired Item:#n 90 #v4000241# #t4000241#\r\n#eRequired Item:#n 90 #v4000242# #t4000242#\r\n#eRequired Item:#n 1 #v1902005# #t1902005#\r\n#eRequired Mesos:#n 5,000,000\r\n#e#d#L2##v1902007# #t1902007##l#k\r\n\r\n#bRequirements:#k#n\r\n#eRequired Level:#n 120\r\n#eRequired Item:#n 1 #v1902006#\r\n#eRequired Mesos:#n 10,000,000");    
        } else if (selection == 1)
        {
            status = 2;
            cm.sendSimple("What would you like to purchase?\r\n\r\n#e#d#L0##v1902015# #t1902015##l#k\r\n\r\n#bRequirements:#k#n\r\n#eRequired Level:#n 50\r\n#eRequired Mesos:#n 1,000,000\r\n#eRequired Item:#n 50 #v4000157# #t4000157#\r\n\r\n#e#d#L1##v1902016# #t1902016##l#k\r\n\r\n#bRequirements:#k#n\r\n#eRequired Level:#n 100\r\n#eRequired Item:#n 200 #v4000182# #t4000182#\r\n#eRequired Item:#n 200 #v4000183# #t4000183#\r\n#eRequired Item:#n 200 #v4000184# #t4000184#\r\n#eRequired Item:#n 1 #v1902015# #t1902015#\r\n#eRequired Mesos:#n 40,000,000\r\n\r\n#e#d#L2##v1802017# #t1802017##l#k\r\n\r\n#bRequirements:#k#n\r\n#eRequired Level:#n 150\r\n#eRequired Item:#n 500 #v4000274# #t4000274#\r\n#eRequired Item:#n 500 #v4000268# #t4000268#\r\n#eRequired Item:#n 500 #v4000181# #t4000181#\r\n#eRequired Item:#n 1 #v1902016# #t1902016#\r\n#eRequired Mesos:#n 80,000,000\r\n\r\n#e#d#L3##v1802018# #t1802018##l#k\r\n\r\n#bRequirements:#k#n\r\n#eRequired Level:#n 200\r\n#eRequired Item:#n 1 #v1912017#\r\n#eRequired Mesos:#n 100,000,000");
        }
    }
    else if (status == 2)
    {
        if (selection == 0)
        {
            if (cm.getLevel() >= 50 && cm.getMeso() >= 1000000)
            {
                cm.gainMeso(-1000000);
                cm.gainItem(1902005, 1);
                cm.gainItem(1912005, 1);
                
                if (cm.getPlayer().getJob().isA(Packages.client.MapleJob.DAWNWARRIOR1) || cm.getPlayer().getJob().isA(Packages.client.MapleJob.BLAZEWIZARD1) || cm.getPlayer().getJob().isA(Packages.client.MapleJob.WINDARCHER1) || cm.getPlayer().getJob().isA(Packages.client.MapleJob.NIGHTWALKER1))
                {
                    cm.teachSkill(10001004, 1, 1, -1);
                }
                else if (cm.getPlayer().getJob().isA(Packages.client.MapleJob.ARAN2))
                {
                    cm.teachSkill(20001004, 1, 1, -1);
                }
                else
                {
                    cm.teachSkill(1004, 1, 1, -1);
                }

                cm.sendOk("Your mount is born!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("The requirements have not been met.");
                cm.dispose();
            }
        }
        else if (selection == 1)
        {
            if (cm.getLevel() >= 100 && cm.getMeso() >= 5000000 && cm.hasItem(1902005, 1) && cm.hasItem(4000262, 90) && cm.hasItem(4000263, 90) && cm.hasItem(4000265, 90) && cm.hasItem(4000236, 90) && cm.hasItem(4000267, 90) && cm.hasItem(4000238, 90) && cm.hasItem(4000239, 90) && cm.hasItem(4000241, 90) && cm.hasItem(4000242, 90))
            {
                cm.gainMeso(-5000000);
                cm.gainItem(4000262, -90);
                cm.gainItem(4000263, -90);
                cm.gainItem(4000265, -90);
                cm.gainItem(4000236, -90);
                cm.gainItem(4000237, -90);
                cm.gainItem(4000238, -90);
                cm.gainItem(4000239, -90);
                cm.gainItem(4000241, -90);
                cm.gainItem(4000242, -90);
                cm.gainItem(1902005, -1);
                cm.gainItem(1902006, 1);
                cm.sendOk("Your mount is reborn!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("The requirements have not been met.");
                cm.dispose();
            }    
        }
        else if (selection == 2)
        {
            if (cm.getLevel() >= 120 && cm.getMeso() >= 10000000 && cm.hasItem(1902006, 1))
            {
                cm.gainItem(1902006, -1);
                cm.gainItem(1902007, 1);
                cm.gainMeso(-10000000);
                cm.sendOk("Your mount has reached its final form!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("The requirements have not been met.");
                cm.dispose();
            }
        }
    }
    else if (status == 3)
    {
        if (selection == 0)
        {
            if (cm.getLevel() >= 50 && cm.getMeso() >= 1000000 && cm.hasItem(4000157, 50))
            {
                cm.gainItem(1902015, 1);
                cm.gainItem(4000157, -50);
                cm.gainMeso(-1000000);
                cm.gainItem(1912011, 1);
                
                if (cm.getPlayer().getJob().isA(Packages.client.MapleJob.DAWNWARRIOR1) || cm.getPlayer().getJob().isA(Packages.client.MapleJob.BLAZEWIZARD1) || cm.getPlayer().getJob().isA(Packages.client.MapleJob.WINDARCHER1) || cm.getPlayer().getJob().isA(Packages.client.MapleJob.NIGHTWALKER1))
                {
                    cm.teachSkill(10001004, 1, 1, -1);
                }
                else if (cm.getPlayer().getJob().isA(Packages.client.MapleJob.ARAN2))
                {
                    cm.teachSkill(20001004, 1, 1, -1);
                }
                else
                {
                    cm.teachSkill(1004, 1, 1, -1);
                }
                
                cm.sendOk("Your mount is born!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("The requirements have not been met.");
                cm.dispose();
            }
        }
        else if (selection == 1)
        {
            if (cm.getLevel() >= 100 && cm.getMeso() >= 40000000 && cm.hasItem(4000182, 100) && cm.hasItem(4000183, 100) && cm.hasItem(4000184, 100) && cm.hasItem(1902015, 1))
            {
                cm.gainMeso(-40000000);
                cm.gainItem(4000182, -200);
                cm.gainItem(4000183, -200);
                cm.gainItem(4000184, -200);
                cm.gainItem(1902015, -1);
                cm.gainItem(1902016, 1);
                cm.sendOk("Your mount has been reborn!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("The requirements have not been met.");
                cm.dispose();
            }
        }
        else if (selection == 2)
        {
            if (cm.getLevel() >= 150 && cm.getMeso() >= 80000000 && cm.hasItem(4000274, 200) && cm.hasItem(4000268, 200) && cm.hasItem(4000181, 200) && cm.hasItem(1902016, 1))
            {
                cm.gainMeso(-80000000);
                cm.gainItem(4000274, -500);
                cm.gainItem(4000181, -500);
                cm.gainItem(4000268, -500);
                cm.gainItem(1912016, -1);
                cm.gainItem(1912017, 1);
                cm.sendOk("Your mount has been reborn!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("The requirements have not been met.");
                cm.dispose();
            }
        }
        else if (selection == 3)
        {
            if (cm.getLevel() >= 200 && cm.getMeso() >= 10000000 && cm.hasItem(1912017, 1))
            {
                cm.gainMeso(-10000000);
                cm.gainItem(1912017 -1);
                cm.gainItem(1912018, 1);
                cm.sendOk("Your mount has reached its final form!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("The requirements have not been met.");
                cm.dispose();
            }  
        }
    }
}