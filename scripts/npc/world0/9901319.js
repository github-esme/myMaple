// Guild Management

var status = -1;

var mapIDs = new Array(220000006, 100000202, 101000100, 105040310, 280020000, 610020000, 682000200);
var basePoints = new Array(5, 5, 10, 10, 15, 10, 10);
var bonusPoints = new Array(2, 2, 3, 3, 5, 3, 3);

var bosses = new Array(7220000, 9500352, 9300012, 8220007, 9001013, 9400633, 9300339, 9500173, 9400642, 4300013, 9300119, 8220011, 9300105, 9300039, 9300140, 9300089, 8500002, 9300028, 9420513, 9001014, 9400549, 9700037, 9400596, 9400597, 8220004, 9400594, 8220005, 8220006, 8220003, 9500363, 9400575, 9400014, 9400569, 9400121, 9400300, 9400590, 9400591, 9400409);
var bossesHP = new Array("125,000", "93,000", "108,000", "125,000", "135,000", "150,000", "180,000", "200,000", "205,100", "210,000", "250,000", "300,000", "410,000", "480,000", "672,000", "850,000", "1,350,000", "1,590,000", "1,700,000", "2,000,000", "3,000,000", "3,500,000", "4,000,000", "5,000,000", "5,900,000", "7,000,000", "7,600,000", "9,300,000", "13,400,000", "30,000,000", "32,000,000", "35,000,000", "50,000,000", "75,000,000", "150,000,000", "200,000,000", "255,000,000", "450,000,000");

var ringID;
var requiredGuildLevel;
var requiredGuildPoints

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
    	var output = "What would you like to do?\r\n\r\n";
    	output += "#d#eGuild General#k\r\n"
        output += "#r#L0#Do challenges for guild points!\r\n";
        output += "#b#L1#Check the guild rankings#l\r\n";
        output += "#L2#Check my guild's vault#l\r\n";
        output += "#L3#Check how many guild points I have#l\r\n";
        output += "\r\n\r\n";
        output += "#dGuild Management (Leader Only)#k\r\n";
        output += "#b#L4#Upgrade the guild level (Max Level: 100)#l\r\n";
        output += "#L5#Send a guild member guild points#l\r\n";
        output += "\r\n\r\n";
        output += "#dGuild Shop#k\r\n";
        //output += "#b#L6#Buy an EXP rate boost for all guild members#l\r\n";
        output += "#b#L7#Buy a meso rate boost for all guild members#l\r\n";
        output += "#L8#Buy a drop rate boost for all guild members#l\r\n";
        output += "#L9#Purchase a guild ring#l\r\n";
        output += "#L10#Upgrade my guild ring#l#k";

        cm.sendSimple(output);
    }
    else if (status == 1)
    {
    	if (selection == 0)
    	{
            var output = "What kind of challenge would you like to do?\r\n\r\n";
            output += "#d#e#L0#Boss Battle#l#n#k\r\n\r\n#eDescription:#n Earn guild points by killing a series of bosses within 5 minutes. There are a total of 38 bosses that will spawn one after another in chronological order by difficulty. Killing all the bosses within the time limit will earn 50 bonus guild points.\r\n#eRequirements:#n A party with a minimum of two players is required.\r\n#e#r#L1#What kind of bosses will I face?#k#n#l\r\n\r\n";
            output += "#d#e#L2#Jump Quest Challenge#l#n#k\r\n\r\n#eDescription:#n Earn guild points by completing a jump quest of your choice. The more players, the more guild points that are earned. The earnings vary based on the JQ map chosen.\r\n#eRequirements:#n A party with a minimum of one player is required.\r\n\r\n";
            output += "#d#e#L3#Trivial Challenge#l#n#k\r\n\r\n#eDescription:#n Coming soon.\r\n#eRequirements:#n A party with max one of player is required."
            
            cm.sendSimple(output);
        }
        else if (selection == 1)
        {
          cm.displayGuildRanks();
          cm.dispose();
        }
        else if (selection == 2)
        {
            if (cm.getGuild() != null)
            {
                cm.sendOk("Your guild currently has #e#b" + cm.getGuild().getGP() + "#k#n guild points.");
                cm.dispose();
            }
            else
            {
                cm.sendOk("You are in not in a guild.");
                cm.dispose();
            }
        }
        else if (selection == 3)
        {
            if (cm.getGuild() != null)
            {
                cm.sendOk("You currently have #e#b" + cm.getPlayer().getGuildPoints() + "#k#n guild points.");
                cm.dispose();
            }
            else
            {
                cm.sendOk("You are in not in a guild.");
                cm.dispose();
            }
        }
        else if (selection == 4)
        {
            if (cm.getGuild() != null)
            {
                if (cm.getGuild().getGuildLevel() >= 100)
                {
                    cm.sendOk("Guild level 100 is the maximum that you can get!");
                    cm.dispose();
                    return;
                }
    
                if (cm.getPlayer().getId() == cm.getGuild().getLeaderId())
                {
                    var guildLevel = cm.getGuild().getGuildLevel();
                    var cost = 100 * guildLevel;
    
                    status = 2;
    
                    cm.sendYesNo("Your current guild level is: #e" + guildLevel + "#n\r\n\r\nIt will cost #e" + cost + " guild points#n to upgrade your guild level to " + (guildLevel + 1) + ". The points will be taken from the guild's vault.\r\n\r\nWould you like to continue?");
                }
                else
                {
                    cm.sendOk("Only the guild leader can upgrade the guild level.");
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("You must be in a guild!");
                cm.dispose();
            }
        }
        else if (selection == 5)
        {
            if (cm.getGuild() != null)
            {
                if (cm.getPlayer().getId() == cm.getGuild().getLeaderId())
                {
                    status = 3;
    
                    cm.sendGetText("Please enter the player of who you like to send guild points to.\r\n\r\n#eThe player receiving the guild points will need to be online an in the same guild.#n");
                }
                else
                {
                    cm.sendOk("Only the guild leader can send guild points to a guild member.");
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("You must be in a guild!");
                cm.dispose();
            }
        }
        else if (selection == 6)
        {
            if (cm.getGuild() == null)
            {
                cm.sendOk("You must be in a guild!");
                cm.dispose();
            }

            var rate = cm.getGuild().getRate(0.2);

            status = 6;

            cm.sendYesNo("You current have: #e" + cm.getPlayer().getGuildPoints() + "#n guild points.\r\n\r\nSince your guild level is #elevel " + cm.getGuild().getGuildLevel() + "#n, you and your guild members will receive an EXP rate boost of #e" + rate + "%#n for 30 minutes even after they relog. The EXP rate boost is calculated after applying the server rates and 2x EXP card (if applied) which is more beneficial towards you.\r\n\r\nBuying an EXP rate boost for all of your guild members will cost you #e1,000 guild points#n.\r\n\r\nWould you like to continue?");
        }
        else if (selection == 7)
        {
            if (cm.getGuild() == null)
            {
                cm.sendOk("You must be in a guild!");
                cm.dispose();
            }

            var rate = cm.getGuild().getRate(0.2);

            status = 7;

            cm.sendYesNo("You current have: #e" + cm.getPlayer().getGuildPoints() + "#n guild points.\r\n\r\nSince your guild level is #elevel " + cm.getGuild().getGuildLevel() + "#n, you and your guild members will receive a meso rate boost of #e" + rate + "%#n for 30 minutes even after they relog. The meso rate boost is calculated after applying the server rates which is more beneficial towards you.\r\n\r\nBuying a meso rate boost for all of your guild members will cost you #e1,000 guild points#n.\r\n\r\nWould you like to continue?");
        }
        else if (selection == 8)
        {
            if (cm.getGuild() == null)
            {
                cm.sendOk("You must be in a guild!");
                cm.dispose();
            }

            status = 8;

            var duration = 2 * cm.getGuild().getGuildLevel();

            cm.sendYesNo("You current have: #e" + cm.getPlayer().getGuildPoints() + "#n guild points.\r\n\r\nSince your guild level is #elevel " + cm.getGuild().getGuildLevel() + "#n, you and your guild members will receive a drop rate boost of #e100% for " + duration + " minutes#n even after they relog. The 2x drop card will not take effect during the drop rate boost.\r\n\r\nBuying a drop rate boost for all of your guild members will cost you #e1,000 guild points#n.\r\n\r\nWould you like to continue?");
        }
        else if (selection == 9)
        {
            if (cm.getGuild() == null)
            {
                cm.sendOk("You must be in a guild!");
                cm.dispose();
                return;
            }

            status = 9;
            cm.sendYesNo("For #e1,000 guild points#n, you will receive a #v1112499# #t1112499#. You can upgrade the guild ring with me to make it more powerful. Would you like to purchase it?");
        }
        else if (selection == 10)
        {
            if (cm.getGuild() == null)
            {
                cm.sendOk("You must be in a guild!");
                cm.dispose();
                return;
            }

            status = 10;
            cm.sendSimple("Choose which guild ring you want to upgrade.\r\n\r\n" + cm.EquipList(cm.getClient()));
        }
    }
    else if (status == 2) // Challenges
    {
        if (selection == 0) // Boss Battle
        {
            if (cm.getPlayer().getParty() == null)
            {
                cm.sendOk("You have to be in a party to do the boss battle.");
            }
            else if (!cm.isLeader())
            {
                cm.sendOk("The leader of the party must talk to me.");
            }
            else
            {
                if (cm.getGuild() == null)
                {
                    cm.sendOk("You must be in a guild!");
                    cm.dispose();
                    return;
                }

                var guildID = cm.getGuild().getId();
                var party = cm.getPartyMembers();
                var pass = true;

                var iter = party.iterator();
                while (iter.hasNext())
                {
                    var chr = iter.next();
                    
                    if (chr.getGuild() == null || chr.getGuild().getId() != guildID)
                    {
                        pass = false;
                    }
                }

                if (cm.partyMembersInMap() < 2)
                {
                    cm.sendOk("You must have at least two people in your party in the FM!");
                    cm.dispose();
                    return;
                }

                if (pass)
                {
                    var em = cm.getEventManager("GuildBossFight");

                    if (em == null)
                    {
                        cm.sendOk("This PQ is currently broken. Please report it on the forum!");
                        cm.dispose();
                    }
                    else if (em.getInstance("GuildBossFight_" + guildID) != null)
                    {
                        cm.sendOk("Your guild is already doing the the boss fight!");
                        cm.dispose();
                    }
                    else
                    {
                        em.setProperty("guildID", guildID);
                        em.startInstance(cm.getParty(), cm.getPlayer().getMap());
                        cm.dispose();
                    }
                }
                else
                {
                    cm.sendOk("All of your party members must be in the same guild as you!");
                    cm.dispose();
                }
            }
        }
        else if (selection == 1)
        {
            var output = "Below are the bosses that you will face in chronological order. You will receive extra 50 points if you kill all of the bosses within the time limit.\r\n\r\n";
            
            for (var i = 0; i < bosses.length; i++)
            {
                var points = 0;

                if (i <= 10)
                    points = 2;
                else if (i <= 20)
                    points = 3;
                else if (i <= 30)
                    points = 4;
                else if (i <= 35)
                    points = 5;
                else
                    points = 7;

                if (bosses[i] == 9700037)
                {
                    output += "#e#dBoss " + (i + 1) + ":#k\r\n#bBoss Name:#k#n Ghost Ship Captain\r\n#e#bBoss HP:#k#n 4,000,000\r\n#e#bPoint(s) Awarded:#k#n " + points + "\r\n\r\n";
                }
                else
                {
                    output += "#e#dBoss " + (i + 1) + ":#k\r\n#bBoss Name:#k#n #o" + bosses[i] + "#\r\n#e#bBoss HP:#k#n " + bossesHP[i] + "\r\n#e#bPoint(s) Awarded:#k#n " + points + "\r\n\r\n";
                }
            }

            cm.sendOk(output);
            cm.dispose();
        }
        else if (selection == 2) // Jump Quest
        {
            var output = "What jump quest map would you like?\r\n\r\n";

            for (var i = 0; i < mapIDs.length; i++)
            {
                output += "#L" + i + "##e" + mapIDs[i] + " - #b#m" + mapIDs[i] + "##k#n#l\r\n\r\n";
                output += "#ePoint(s) Awarded:#n " + basePoints[i] + " base guild points per completion. Incrementing " + bonusPoints[i] + " bonus guild points for each player that completes the JQ.\r\n\r\n";
            }

            status = 5;

            cm.sendSimple(output);
        }
        else if (selection == 3)
        {
            cm.sendOk("Coming soon.");
            cm.dispose();
        }
    }
    else if (status == 3) // Upgrade guild level
    {
        var cost = 100 * cm.getGuild().getGuildLevel();

        if (cm.getGuild().getGP() >= cost)
        {
            cm.getGuild().gainGP(-cost);
            cm.getGuild().gainGuildLevel(1);

            Packages.net.server.Server.getInstance().broadcastMessage(Packages.tools.MaplePacketCreator.serverNotice(6, "[Guild System] " + cm.getGuild().getName() + " has leveled up their guild to level " + cm.getGuild().getGuildLevel() + "!"));

            cm.sendOk("Your guild level is now: " + cm.getGuild().getGuildLevel());
            cm.dispose();
        }
        else
        {
            cm.sendOk("There's not enough guild points in the guild's vault!");
            cm.dispose();
        }
    }
    else if (status == 4) // Send guild member guild points
    {
        var targetName = cm.getText();   
        target = cm.getClient().getWorldServer().getPlayerStorage().getCharacterByName(targetName);

        if (target == null || target.isIntern() || target.getGuild().getId() != cm.getGuild().getId())
        {
            cm.sendOk("#e" + targetName + "#n is either not online or not in the same guild!");
            cm.dispose();
        }
        else
        {
            var currentGP = cm.getGuild().getGP();
            cm.sendGetNumber("How much guild points would you like to send to #e" + targetName + "#n?\r\n\r\n#bThere's currently " + currentGP + " guild points in the vault.#k", 1, 1, currentGP);
        }
    }
    else if (status == 5) 
    {
        if (selection > cm.getGuild().getGP())
        {
            cm.sendOk("There's not enough guild points in the vault!");
            cm.dispose();
        }
        else
        {
            cm.getGuild().gainGP(-selection);
            target.gainGuildPoints(selection);

            target.message(cm.getPlayer() + " has sent you " + selection + " guild points to you!");

            cm.sendOk("#e" + target.getName() + "#n has received " + selection + " guild points!");
            cm.dispose();
        }
    }
    else if (status == 6) // Jump quest
    {
        if (cm.getPlayer().getParty() == null)
        {
            cm.sendOk("You have to be in a party to do the jump quest.");
        }
        else if (!cm.isLeader())
        {
            cm.sendOk("The leader of the party must talk to me.");
        }
        else
        {
            if (cm.getGuild() == null)
            {
                cm.sendOk("You must be in a guild!");
                cm.dispose();
                return;
            }

            var guildID = cm.getGuild().getId();
            var party = cm.getPartyMembers();
            var pass = true;

            var iter = party.iterator();
            while (iter.hasNext())
            {
                var chr = iter.next();

                if (chr.getGuild() == null || chr.getGuild().getId() != guildID)
                {
                    pass = false;
                }
            }

            if (cm.partyMembersInMap() < 1)
            {
                cm.sendOk("You must have at least one person in your party in the FM!");
                cm.dispose();
                return;
            }

            if (pass)
            {
                var em = cm.getEventManager("GuildJumpQuest");

                if (em == null)
                {
                    cm.sendOk("This PQ is currently broken. Please report it on the forum!");
                    cm.dispose();
                }
                else if (em.getInstance("GuildJumpQuest_" + guildID) != null)
                {
                    cm.sendOk("Your guild is already doing the the jump quest!");
                    cm.dispose();
                }
                else
                {
                    em.setProperty("guildID", guildID);
                    em.setProperty("mapIndex", selection);
                    em.startInstance(cm.getParty(), cm.getPlayer().getMap());
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("All of your party members must be in the same guild as you!");
                cm.dispose();
            }
        }
    }
    else if (status == 7) // EXP rate Boost
    {
        if (cm.getPlayer().getGuildPoints() >= 1000)
        {
            cm.getPlayer().gainGuildPoints(-1000);
            cm.getClient().getWorldServer().addGuildBuffs(cm.getGuild().getId() + "-1-" + cm.getGuild().getRate(0.2) + "-" + cm.getClient().getWorldServer().getTimestamp(30));

            cm.sendOk("Guild EXP rate boost is now active!");
            cm.dispose();
        }
        else
        {
            cm.sendOk("You do not have enough guild points!");
            cm.dispose();
        }
    }
    else if (status == 8) // Meso rate Boost
    {
        if (cm.getPlayer().getGuildPoints() >= 1000)
        {
            cm.getPlayer().gainGuildPoints(-1000);
            cm.getClient().getWorldServer().addGuildBuffs(cm.getGuild().getId() + "-2-" + cm.getGuild().getRate(0.2) + "-" + cm.getClient().getWorldServer().getTimestamp(30));

            cm.sendOk("Guild meso rate boost is now active!");
            cm.dispose();
        }
        else
        {
            cm.sendOk("You do not have enough guild points!");
            cm.dispose();
        }
    }
    else if (status == 9) // Drop rate Boost
    {
        if (cm.getPlayer().getGuildPoints() >= 1000)
        {
            cm.getPlayer().gainGuildPoints(-1000);
            
            var duration = 2 * cm.getGuild().getGuildLevel();

            cm.getClient().getWorldServer().addGuildBuffs(cm.getGuild().getId() + "-3-100-" + cm.getClient().getWorldServer().getTimestamp(duration));

            cm.sendOk("Guild drop rate boost is now active!" + duration);
            cm.dispose();
        }
        else
        {
            cm.sendOk("You do not have enough guild points!");
            cm.dispose();
        }
    }
    else if (status == 10) // Purchase basic guild ring
    {
        if (cm.getPlayer().getGuildPoints() >= 1000)
        {
            if (cm.canHold(1112499) && cm.getPlayer().getItemQuantity(1112499, true) == 0)
            {
                cm.gainItem(1112499);
                cm.getPlayer().gainGuildPoints(-1000);
                cm.sendOk("You have received the #v1112499# #t1112499#!");
                cm.dispose();
            }
            else
            {
                cm.sendOk("Either your inventory is full or you already have this ring.");
                cm.dispose();
            }
        }
        else
        {
            cm.sendOk("You do not have enough guild points!");
            cm.dispose();
        }
    }
    else if (status == 11) // Upgrade guild ring
    {
        ringID = cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getItem(selection).getItemId();
        var newRing = ringID + 1;

        if (ringID >= 1112499 && ringID <= 1112516)
        {
            if (ringID == 1112516)
            {
                cm.sendOk("You cannot upgrade this ring any further!");
                cm.dispose();
            }
            else
            {
                requiredGuildLevel = (newRing - 1112499) * 5;
                requiredGuildPoints = (newRing - 1112499) * 100;

                cm.sendYesNo("#e#bNext guild ring:\r\n#k#n #v" + newRing + "# #t" + newRing + "#\r\n\r\n#e#bRequirements:#k\r\nRequired Guild Level:#n " + requiredGuildLevel + "\r\n#eRequired Guild Points:#n " + requiredGuildPoints);
            }
        }
        else
        {
            cm.sendOk("#v" + ringID + "# #t" + ringID + "# is not a guild ring!");
            cm.dispose();
        }
    }
    else if (status == 12)
    {
        if (cm.hasItem(ringID))
        {
            if (cm.getGuild() == null)
            {
                cm.sendOk("You must be in a guild!");
                cm.dispose();
                return;
            }

            if (cm.getPlayer().getGuildPoints() >= requiredGuildPoints)
            {
                if (cm.getGuild().getGuildLevel() >= requiredGuildLevel)
                {
                    cm.gainItem(ringID, -1);
                    cm.getPlayer().gainGuildPoints(-requiredGuildPoints);
                    cm.gainItem(ringID + 1, 1, true, false);
                    cm.sendOk("You have upgraded the ring to #v" + (ringID + 1) + "# #t" + (ringID + 1) + "#!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Your guild level is not high enough to upgrade the guild ring!");
                    cm.dispose();
                }
            }
            else
            {
                cm.sendOk("You do not have enough guild points!");
                cm.dispose();
            }
        }
        else
        {
            cm.sendOk("You do not have the #v" + ringID + "# #t" + ringID + "#!");
        }
    }
}