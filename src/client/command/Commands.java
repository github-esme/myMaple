/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation version 3 as published by
 the Free Software Foundation. You may not use, modify or distribute
 this program under any other version of the GNU Affero General Public
 License.te

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package client.command;

import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import server.life.MobSkillFactory;
import net.MaplePacketHandler;
import net.PacketProcessor;
import net.server.Server;
import net.server.channel.Channel;
import net.server.world.World;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.npc.NPCScriptManager;
import scripting.portal.PortalScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.MapleShopFactory;
import server.maps.HiredMerchant;
import server.TimerManager;
import server.events.gm.MapleEvent;
import server.expeditions.MapleExpedition;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MapleNPC;
import server.life.MonsterDropEntry;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.quest.MapleQuest;
import tools.DatabaseConnection;
import tools.FilePrinter;
import tools.HexTool;
import tools.MapleLogger;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.data.input.ByteArrayByteStream;
import tools.data.input.GenericSeekableLittleEndianAccessor;
import tools.data.input.SeekableLittleEndianAccessor;
import tools.data.output.MaplePacketLittleEndianWriter;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleDisease;
import client.MapleJob;
import client.MapleStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import constants.GameConstants;
import constants.ItemConstants;
import constants.ServerConstants;
import java.util.LinkedList;
import java.util.Map.Entry;
import server.MaplePlayerShopItem;
import server.life.MapleMonsterStats;
import server.maps.FieldLimit;
import tools.LogHelper;
import tools.StringUtil;

public class Commands {

    private static HashMap<String, Integer> gotomaps = new HashMap<String, Integer>();

    private static String[] tips = {
        "Please only use @gm in emergencies or to report somebody.",
        "To report a bug or make a suggestion, use the forum.",
        "Please do not use @gm to ask if a GM is online.",
        "Do not ask if you can receive help, just state your issue.",
        "Do not say 'I have a bug to report', just state it.",};

    private static String[] songs = {
        "Jukebox/Congratulation",
        "Bgm00/SleepyWood",
        "Bgm00/FloralLife",
        "Bgm00/GoPicnic",
        "Bgm00/Nightmare",
        "Bgm00/RestNPeace",
        "Bgm01/AncientMove",
        "Bgm01/MoonlightShadow",
        "Bgm01/WhereTheBarlogFrom",
        "Bgm01/CavaBien",
        "Bgm01/HighlandStar",
        "Bgm01/BadGuys",
        "Bgm02/MissingYou",
        "Bgm02/WhenTheMorningComes",
        "Bgm02/EvilEyes",
        "Bgm02/JungleBook",
        "Bgm02/AboveTheTreetops",
        "Bgm03/Subway",
        "Bgm03/Elfwood",
        "Bgm03/BlueSky",
        "Bgm03/Beachway",
        "Bgm03/SnowyVillage",
        "Bgm04/PlayWithMe",
        "Bgm04/WhiteChristmas",
        "Bgm04/UponTheSky",
        "Bgm04/ArabPirate",
        "Bgm04/Shinin'Harbor",
        "Bgm04/WarmRegard",
        "Bgm05/WolfWood",
        "Bgm05/DownToTheCave",
        "Bgm05/AbandonedMine",
        "Bgm05/MineQuest",
        "Bgm05/HellGate",
        "Bgm06/FinalFight",
        "Bgm06/WelcomeToTheHell",
        "Bgm06/ComeWithMe",
        "Bgm06/FlyingInABlueDream",
        "Bgm06/FantasticThinking",
        "Bgm07/WaltzForWork",
        "Bgm07/WhereverYouAre",
        "Bgm07/FunnyTimeMaker",
        "Bgm07/HighEnough",
        "Bgm07/Fantasia",
        "Bgm08/LetsMarch",
        "Bgm08/ForTheGlory",
        "Bgm08/FindingForest",
        "Bgm08/LetsHuntAliens",
        "Bgm08/PlotOfPixie",
        "Bgm09/DarkShadow",
        "Bgm09/TheyMenacingYou",
        "Bgm09/FairyTale",
        "Bgm09/FairyTalediffvers",
        "Bgm09/TimeAttack",
        "Bgm10/Timeless",
        "Bgm10/TimelessB",
        "Bgm10/BizarreTales",
        "Bgm10/TheWayGrotesque",
        "Bgm10/Eregos",
        "Bgm11/BlueWorld",
        "Bgm11/Aquarium",
        "Bgm11/ShiningSea",
        "Bgm11/DownTown",
        "Bgm11/DarkMountain",
        "Bgm12/AquaCave",
        "Bgm12/DeepSee",
        "Bgm12/WaterWay",
        "Bgm12/AcientRemain",
        "Bgm12/RuinCastle",
        "Bgm12/Dispute",
        "Bgm13/CokeTown",
        "Bgm13/Leafre",
        "Bgm13/Minar'sDream",
        "Bgm13/AcientForest",
        "Bgm13/TowerOfGoddess",
        "Bgm14/DragonLoad",
        "Bgm14/HonTale",
        "Bgm14/CaveOfHontale",
        "Bgm14/DragonNest",
        "Bgm14/Ariant",
        "Bgm14/HotDesert",
        "Bgm15/MureungHill",
        "Bgm15/MureungForest",
        "Bgm15/WhiteHerb",
        "Bgm15/Pirate",
        "Bgm15/SunsetDesert",
        "Bgm16/Duskofgod",
        "Bgm16/FightingPinkBeen",
        "Bgm16/Forgetfulness",
        "Bgm16/Remembrance",
        "Bgm16/Repentance",
        "Bgm16/TimeTemple",
        "Bgm17/MureungSchool1",
        "Bgm17/MureungSchool2",
        "Bgm17/MureungSchool3",
        "Bgm17/MureungSchool4",
        "Bgm18/BlackWing",
        "Bgm18/DrillHall",
        "Bgm18/QueensGarden",
        "Bgm18/RaindropFlower",
        "Bgm18/WolfAndSheep",
        "Bgm19/BambooGym",
        "Bgm19/CrystalCave",
        "Bgm19/MushCatle",
        "Bgm19/RienVillage",
        "Bgm19/SnowDrop",
        "Bgm20/GhostShip",
        "Bgm20/NetsPiramid",
        "Bgm20/UnderSubway",
        "Bgm21/2021year",
        "Bgm21/2099year",
        "Bgm21/2215year",
        "Bgm21/2230year",
        "Bgm21/2503year",
        "Bgm21/KerningSquare",
        "Bgm21/KerningSquareField",
        "Bgm21/KerningSquareSubway",
        "Bgm21/TeraForest",
        "BgmEvent/FunnyRabbit",
        "BgmEvent/FunnyRabbitFaster",
        "BgmEvent/wedding",
        "BgmEvent/weddingDance",
        "BgmEvent/wichTower",
        "BgmGL/amoria",
        "BgmGL/Amorianchallenge",
        "BgmGL/Bigfoot",
        "BgmGL/chapel",
        "BgmGL/cathedral",
        "BgmGL/Courtyard",
        "BgmGL/CrimsonwoodKeep",
        "BgmGL/CrimsonwoodKeepInterior",
        "BgmGL/GrandmastersGauntlet",
        "BgmGL/HauntedHouse",
        "BgmGL/NLChunt",
        "BgmGL/NLCtown",
        "BgmGL/NLCupbeat",
        "BgmGL/PartyQuestGL",
        "BgmGL/PhantomForest",
        "BgmJp/Feeling",
        "BgmJp/BizarreForest",
        "BgmJp/Hana",
        "BgmJp/Yume",
        "BgmJp/Bathroom",
        "BgmJp/BattleField",
        "BgmJp/FirstStepMaster",
        "BgmMY/Highland",
        "BgmMY/KualaLumpur",
        "BgmSG/BoatQuay_field",
        "BgmSG/BoatQuay_town",
        "BgmSG/CBD_field",
        "BgmSG/CBD_town",
        "BgmSG/Ghostship",
        "BgmUI/ShopBgm",
        "BgmUI/Title",
        "BgmUI/NxLogo",
        "BgmUI/NxLogoMS",
        "BgmUI/WCSelect",
        "BgmUI/WzLogo"
    };

    static {
        gotomaps.put("gmmap", 180000000);
        gotomaps.put("southperry", 60000);
        gotomaps.put("amherst", 1010000);
        gotomaps.put("henesys", 100000000);
        gotomaps.put("ellinia", 101000000);
        gotomaps.put("perion", 102000000);
        gotomaps.put("kerning", 103000000);
        gotomaps.put("lith", 104000000);
        gotomaps.put("sleepywood", 105040300);
        gotomaps.put("florina", 110000000);
        gotomaps.put("orbis", 200000000);
        gotomaps.put("happy", 209000000);
        gotomaps.put("elnath", 211000000);
        gotomaps.put("ludi", 220000000);
        gotomaps.put("aqua", 230000000);
        gotomaps.put("leafre", 240000000);
        gotomaps.put("mulung", 250000000);
        gotomaps.put("herb", 251000000);
        gotomaps.put("omega", 221000000);
        gotomaps.put("korean", 222000000);
        gotomaps.put("nlc", 600000000);
        gotomaps.put("excavation", 990000000);
        gotomaps.put("pianus", 230040420);
        gotomaps.put("horntail", 240060200);
        gotomaps.put("mushmom", 100000005);
        gotomaps.put("griffey", 240020101);
        gotomaps.put("manon", 240020401);
        gotomaps.put("horseman", 682000001);
        gotomaps.put("balrog", 105090900);
        gotomaps.put("zakum", 211042300);
        gotomaps.put("papu", 220080001);
        gotomaps.put("showa", 801000000);
        gotomaps.put("guild", 200000301);
        gotomaps.put("shrine", 800000000);
        gotomaps.put("skelegon", 240040511);
        gotomaps.put("hpq", 100000200);
        gotomaps.put("ht", 240050400);
        gotomaps.put("fm", 910000000);
    }

    public static boolean executePlayerCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        if (heading == '!' && player.gmLevel() == 0) {
            player.message("You may not use !" + sub[0] + ", please try @" + sub[0]);
            return false;
        }
        switch (sub[0]) {
            case "help":
            case "commands":
                //player.message("@str/@dex/@int/@luk <amount> - Adds stats faster than clicking.");
                player.message("@dispose: Fixes your character if it is stuck.");
                player.message("@time: Displays the current server time.");
                player.message("@online: Displays the current players online.");
                player.message("@music: Opens the music NPC.");
                player.message("@points: Tells you how many donator, vote, and fishing points and NX you have.");
                player.message("@gm <message>: Sends a message to all online GMs in the case of an emergency.");
                player.message("@bugreport <message>: Saves the report of the bug. Please provide as much information as possible.");
                player.message("@smega <message>: Sends a smega message for free. Only available if you're at least level 10.");
                player.message("@togglesmega: Enables/disables the display of smega messages on your screen.");
                player.message("@togglestream: Enable/disables streaming. Only available for verified streamers.");
                player.message("@toggleinvisible: Enables/disables the display of the map you are in by @online and /find.");
                player.message("@searchmerchants <item name>: Tells you which FM merchant sells the item that you're looking for.");
                player.message("@streamers: Displays a list of current online streamers.");
                player.message("@joinevent: If an event is in progress, use this to warp to the event map.");
                player.message("@leaveevent: If an event has ended, use this to warp to your original map.");
                player.message("@mapowner: You can mark the map you are in as yours.");
                player.message("@afkcheck <IGN>: Allows you to take ownership of the map you're in if the current map owner is AFK for longer than 2 minutes.");
                player.message("@save: Saves your data.");
                player.message("@rates: Check your current rates.");
                player.message("@staff: Lists the staff of myMaple.");
                player.message("@testdps: Tests your DPS (Damage Per Second).");
                player.message("@testepm: Tests your EPM (Experience Per Minute).");
                player.message("@dpsranks: Displays the current DPS rankings.");
                player.message("@ranks: Displays the current rankings.");
                player.message("@uptime: Shows how long myMaple has been online.");
                player.message("@whatdropsfrom <monster name>: Displays a list of drops and chances for a specified monster.");
                player.message("@whodrops <item name>: Displays monsters that drop an item given an item name.");
                player.message("@bosshp: Displays the remaining HP of the bosses on your map.");
                break;
            case "testing":
                c.announce(MaplePacketCreator.fredrickMessage((byte) 0x21));
                break;
            case "bugreport":
                if (sub.length < 2) {
                    player.message("Syntax: @bugreport <message>");
                    break;
                }
                DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                String message = joinStringFrom(sub, 1);
                Server.getInstance().broadcastGMMessage(MaplePacketCreator.sendYellowTip("[Bug Report] " + MapleCharacter.makeMapleReadable(player.getName()) + ": " + message));
                FilePrinter.printError("bugreports.txt", "[ " + timeStamp.format(new Date()) + " ] " + MapleCharacter.makeMapleReadable(player.getName()) + ": " + message + "\r\n");
                player.message("Your bug report has been sent and wil be looked at soon. Thank you for reporting a bug.");
                break;
            case "toggleinvisible":
                if (player.isInvisible()) {
                    player.setInvisible(false);

                    player.message("You are no longer invisible. Players will now be able to see what map you are in through @online and /find.");
                } else {
                    player.setInvisible(true);

                    player.message("You are now invisible. Players will not be able to see what map you are in through @online and /find.");
                }
                break;
            case "searchmerchants":
                if (sub.length >= 2) {
                    int itemID = MapleItemInformationProvider.getInstance().getItemID(joinStringFrom(sub, 1));

                    if (itemID == -1) {
                        player.message("No such item exists in the server. Please enter the exact full name of the item.");
                    } else {
                        int count = 0;

                        for (Channel cserv : Server.getInstance().getChannelsFromWorld(c.getWorld())) {
                            for (Entry<Integer, HiredMerchant> entry : cserv.getHiredMerchants().entrySet()) {
                                HiredMerchant hm = entry.getValue();

                                if (hm != null && hm.isOpen()) {
                                    for (MaplePlayerShopItem merchantItem : hm.getItems()) {
                                        if (merchantItem != null && merchantItem.isExist()) {
                                            if (merchantItem.getItem().getItemId() == itemID) {
                                                player.message(hm.getOwner() + "'s merchant at " + c.getChannelServer().getMapFactory().getMap(hm.getMapId()).getMapName() + " in Channel " + hm.getChannel() + " sells the item for " + merchantItem.getPrice() + " mesos.");
                                                count++;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (count == 0) {
                            player.message("No FM merchants sell the item.");
                        }
                    }

                } else {
                    player.message("Syntax: @searchmerchant <item name>");
                }
                break;
            case "afkcheck":
                if (sub.length == 2) {
                    if (!sub[1].toLowerCase().equals(player.getName().toLowerCase())) {
                        MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                        if (target != null && !target.isIntern()) {
                            if (target.getClient().getChannel() == player.getClient().getChannel() && target.getMapId() == player.getMapId()) {
                                if (player.getMap().getMapOwner() != null) {
                                    if (player.getMap().getMapOwner().getName().toLowerCase().equals(target.getName().toLowerCase())) {
                                        if (!target.isAFKChecked()) {
                                            target.setAFKCheck(true);
                                            target.message(player.getName() + " has ran an AFK check on you. If you do not move after 2 minutes, you will lose ownership of this map.");
                                            target.runAFKCheckTimer(player);
                                            player.message("An AFK check is now running on " + sub[1] + ". The player has two minutes to move or else they will lose ownership of this map.");
                                        } else {
                                            player.message(sub[1] + " already has an AFK check running. The player has " + target.getAFKCheckTime() + " seconds to move to show that they are not AFK.");
                                        }
                                    } else {
                                        player.message(sub[1] + " does not own this map. You can only AFK check the player who has ownership of this map.");
                                    }
                                } else {
                                    player.message("No one owns this map. Type @mapowner to take ownership of this map.");
                                }
                            } else {
                                player.message("You are either not in the same channel and/or not in the same map as the player.");
                            }
                        } else {
                            player.message(sub[1] + " is either offline or does not exist.");
                        }
                    } else {
                        player.message("You cannot AFK check yourself.");
                    }
                } else {
                    player.message("Syntax: @afkcheck <IGN>");
                }
                break;
            case "mapowner":
                if (player.getMap().getMapOwner() == null) {
                    if (!player.ownsMap()) {
                        player.getMap().setMapOwner(player);
                        player.setOwnsMap(player.getMap());
                        player.getMap().dropMessage(6, "[Map Ownership]: " + player.getName() + " is now the owner of this map.");
                        player.message("You are now the map owner of this map. If you log off, go to cash shop, or change channels, you will lose your ownership. If you change maps, you will have 30 seconds to get back.");
                    } else {
                        player.message("You can not own this map because you already own a map. Please wait " + player.getOwnedMap().getMapOwnerTime() + " seconds.");
                    }
                } else if (player.getMap().getMapOwner().getName().equals(player.getName())) {
                    player.message("You already own this map.");
                } else {
                    player.message(player.getMap().getMapOwner().getName() + " already owns this map. If the player is not here, the player has " + player.getMap().getMapOwnerTime() + " seconds to get back. If the player is AFK, please contact a staff.");
                }
                break;
            case "music":
                NPCScriptManager npc = NPCScriptManager.getInstance();
                npc.start(player.getClient(), 9201082, player);
                break;
            case "streamers":
                int streamers = 0;

                player.message("The current online streamers are:");

                for (Channel chan : player.getClient().getWorldServer().getChannels()) {
                    for (MapleCharacter target : chan.getPlayerStorage().getAllCharacters()) {
                        if (target.isStreaming()) {
                            streamers++;
                            player.message(target.getName() + " - " + target.getStreamerURL());
                        }
                    }
                }

                if (streamers == 0) {
                    player.message("There are currently no streams.");
                }

                break;
            case "togglestream":
                if (player.isStreamer()) {
                    if (player.isStreaming()) {
                        player.setStreaming(false);
                        player.message("You are no longer streaming.");
                    } else {
                        player.setStreaming(true);
                        player.message("You are now streaming. You will receive one streamer point every minute. Please note that you can not AFK while leaving @togglestream on.");
                        Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Streaming] " + player.getName() + " is now streaming! You can check out the stream at " + player.getStreamerURL()));
                    }
                } else {
                    player.message("You are not a verified streamer! To verify yourself as a streamer, please contact a myMaple staff.");
                }
                break;
            case "time":
                //DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                player.message("myMaple Server Time: " + dateFormat.format(new Date()) + " EST");
                break;
            case "points":
                player.message("Vote Points: " + player.getClient().getVotePoints() + " | Donator Points: " + player.getClient().getDonatorPoints() + " | Fishing Points: " + player.getFishingPoints() + " | Crafting Level: " + player.getCraftingLevel());
                player.message("NX Credit: " + player.getCashShop().getCash(1) + " | Mob Points: " + player.getMobPoints() + " | Streamer Points: " + player.getStreamerPoints() + " | Dojo Points: " + player.getDojoPoints());

                if (player.getClient().hasVotedAlready()) {
                    Date currentDate = new Date();
                    int time = (int) ((int) 86400 - ((currentDate.getTime() / 1000) - c.getVoteTime())); //ugly as fuck
                    int hours = time / 3600;
                    int minutes = time % 3600 / 60;
                    int seconds = time % 3600 % 60;

                    player.message("You have already voted. You can vote again in " + hours + " Hours " + minutes + " Minutes " + seconds + " Seconds.");
                } else {
                    player.message("You are free to vote! Go vote to earn vote rewards.");
                }

                break;
            case "testepm":
                final MapleCharacter character = player;

                if (character.getLevel() >= 10) {
                    if (!character.isTestingEPM()) {
                        character.toggleTestingEPM();
                        character.message("Your EPM is now being tested. Please kill mobs for experience. Your EPM result will show after 2 minutes.");

                        TimerManager.getInstance().schedule(new Runnable() {
                            public void run() {
                                if (character.isTestingEPM()) {
                                    int epm = character.getEPM() / 2;

                                    character.message("You have gained a total of " + character.getEPM() + " EXP over 2 minutes so your experience per minute is: " + epm);

                                    character.toggleTestingEPM();
                                }
                            }
                        }, 120000);

                    } else {
                        player.message("You are already testing your EPM.");
                    }
                } else {
                    player.message("You have to be level 10 or higher to use the @testepm command.");
                }
                break;
            case "testdps":
                final MapleCharacter chr = player;

                if (chr.getLevel() >= 10) {
                    if (!chr.isTestingDPS()) {
                        chr.toggleTestingDPS();
                        chr.message("Attack the scarecrow for 15 seconds.");
                        final MapleMonster mm = MapleLifeFactory.getMonster(9001007);
                        int distance;

                        int job = chr.getJob().getId();

                        if ((job >= 300 && job < 413) || (job >= 1300 && job < 1500) || (job >= 520 && job < 600)) {
                            distance = 125;
                        } else {
                            distance = 50;
                        }

                        Point p = new Point((int) chr.getPosition().getX() - distance, (int) chr.getPosition().getY());
                        mm.setBelongTo(chr);
                        final int newhp = 2000000000;
                        MapleMonsterStats overrideStats = new MapleMonsterStats();
                        overrideStats.setHp(newhp);
                        mm.setHp(newhp);
                        mm.setOverrideStats(overrideStats);
                        chr.getMap().spawnMonsterOnGroudBelow(mm, p);

                        TimerManager.getInstance().schedule(new Runnable() {
                            public void run() {
                                if (chr.isTestingDPS()) {
                                    int health = mm.getHp();
                                    chr.getMap().killMonster(mm, null, false);
                                    int dps = (newhp - health) / 15;

                                    if (dps == 133333333) {
                                        chr.message("Your damage per second has hit 133333333 which is not possible to get so it has not been recorded.");
                                    } else if (dps > chr.getDPS()) {
                                        chr.message("You have broken your past record! Your damage per second is now " + dps + ". Your ranking has been updated.");

                                        if (dps > chr.getHighestDPS() && !chr.isIntern() && !chr.getNameOfHighestDPS().equals(chr.getName())) {
                                            Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[DPS Ranking] " + chr.getName() + " is now first in the DPS ranking with a DPS of " + dps + "!"));
                                        }

                                        chr.setDPS(dps);
                                    } else {
                                        chr.message("Your damage per second is " + dps + ".");
                                    }

                                    chr.toggleTestingDPS();
                                }
                            }
                        }, 15000);

                    } else {
                        player.message("You are already testing your DPS.");
                    }
                } else {
                    player.message("You have to be level 10 or higher to use the @testdps command.");
                }
                break;
            case "staff":
                player.message("myMaple Staffs:");

                for (String staff : player.getStaff()) {
                    player.message(staff.split("-")[0] + " - " + staff.split("-")[1]);
                }
                break;
            case "togglesmega":
                player.toggleSmega();

                if (player.hasSmegaOn()) {
                    player.message("Smega messages will display on your screen.");
                } else {
                    player.message("Smega messages will not display on your screen.");
                }

                break;
//            case "str":
//            case "int":
//            case "luk":
//            case "dex":
//                if (sub.length == 2) {
//                    int amount = Integer.parseInt(sub[1]);
//                    boolean str = sub[0].equalsIgnoreCase("str");
//                    boolean Int = sub[0].equalsIgnoreCase("int");
//                    boolean luk = sub[0].equalsIgnoreCase("luk");
//                    boolean dex = sub[0].equalsIgnoreCase("dex");
//
//                    if (amount > 0 && amount <= player.getRemainingAp() && amount <= 32763) {
//                        if (str && amount + player.getStr() <= 32767) {
//                            player.setStr(player.getStr() + amount);
//                            player.updateSingleStat(MapleStat.STR, player.getStr());
//                            player.setRemainingAp(player.getRemainingAp() - amount);
//                            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
//                        } else if (Int && amount + player.getInt() <= 32767) {
//                            player.setInt(player.getInt() + amount);
//                            player.updateSingleStat(MapleStat.INT, player.getInt());
//                            player.setRemainingAp(player.getRemainingAp() - amount);
//                            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
//                        } else if (luk && amount + player.getLuk() <= 32767) {
//                            player.setLuk(player.getLuk() + amount);
//                            player.updateSingleStat(MapleStat.LUK, player.getLuk());
//                            player.setRemainingAp(player.getRemainingAp() - amount);
//                            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
//                        } else if (dex && amount + player.getDex() <= 32767) {
//                            player.setDex(player.getDex() + amount);
//                            player.updateSingleStat(MapleStat.DEX, player.getDex());
//                            player.setRemainingAp(player.getRemainingAp() - amount);
//                            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
//                        } else {
//                            player.message("Please make sure the stat you are trying to raise is not over 32,767.");
//                        }
//                    } else {
//                        player.message("Please make sure your AP is not over 32,767 and you have enough to distribute.");
//                    }
//                } else {
//                    player.message("Syntax: @str/@dex/@int/@luk <amount>");
//                }
//
//                break;
            case "smega":
                if (sub.length > 1) { // Check for syntax
                    if (player.getLevel() > 9) { // Check if player is at least level 10
                        if (player.isIntern() || player.getAutobanManager().getLastSpam(10) + 60000 < System.currentTimeMillis()) { // Check if player sent a smega in the last 5 seconds
                            message = joinStringFrom(sub, 1);

                            if (!player.isMuted()) {
                                if (ServerConstants.ENABLE_SMEGA || player.isIntern()) {
                                    if (message.length() < 61) {
                                        for (Channel chan : c.getWorldServer().getChannels()) {
                                            for (MapleCharacter online : chan.getPlayerStorage().getAllCharacters()) {
                                                if (online.hasSmegaOn()) {
                                                    online.getClient().getSession().write(MaplePacketCreator.serverNotice(3, c.getChannel(), c.getPlayer().getName() + " : " + message));
                                                }
                                            }
                                        }

                                        LogHelper.logSmega(player, message);

                                        player.getAutobanManager().spam(10);
                                    } else {
                                        player.message("The smega message is too long! The max character limit is 60.");
                                    }
                                } else {
                                    player.message("All smegas have been disabled.");
                                }
                            } else {
                                player.message("You can not smega when you are muted.");
                            }
                        } else {
                            player.message("You can only send a smega every 60 seconds.");
                        }
                    } else {
                        player.message("You have to be level 10 or higher to use the @smega command.");
                    }
                } else {
                    player.message("Syntax: @smega <message>");
                }
                break;
            case "uptime":
                long milliseconds = System.currentTimeMillis() - Server.uptime;
                int seconds = (int) (milliseconds / 1000) % 60;
                int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
                int days = (int) ((milliseconds / (1000 * 60 * 60 * 24)));
                player.message("myMaple has been online for " + days + " days " + hours + " hours " + minutes + " minutes and " + seconds + " seconds.");
                break;
            case "whatdropsfrom":
                if (sub.length < 2) {
                    player.message("Syntax: @whatdropsfrom <monster name>");
                    break;
                }
                String monsterName = joinStringFrom(sub, 1);
                String output = "";
                int limit = 20;
                Iterator<Pair<Integer, String>> listIterator = MapleMonsterInformationProvider.getMobsIDsFromName(monsterName).iterator();
                for (int i = 0; i < limit; i++) {
                    if (listIterator.hasNext()) {
                        Pair<Integer, String> data = listIterator.next();
                        int mobId = data.getLeft();
                        String mobName = data.getRight();
                        output += mobName + " drops the following items:\r\n\r\n";
                        for (MonsterDropEntry drop : MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId)) {
                            try {
                                String name = MapleItemInformationProvider.getInstance().getName(drop.itemId);
                                if (name == null || drop.chance == 0) {
                                    continue;
                                }
                                //FractionReducer FR = new FractionReducer(drop.chance, 1000000 / player.getDropRate());
//                                float chance = 1000000 / drop.chance / player.getDropRate();
//                                output += "- " + name + " (1/" + (int) chance + ")\r\n";

                                output += "- " + name + " (" + asFraction(drop.chance, 1000000 / player.getDropRate()) + ")\r\n";
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        output += "\r\n";
                    }
                }
                c.announce(MaplePacketCreator.getNPCTalk(9010000, (byte) 0, output, "00 00", (byte) 0));
                break;
            case "whodrops":
                if (sub.length < 2) {
                    player.message("Syntax: @whodrops <item name>");
                    break;
                }
                String searchString = joinStringFrom(sub, 1);
                output = "";
                listIterator = MapleItemInformationProvider.getInstance().getItemDataByName(searchString).iterator();
                if (listIterator.hasNext()) {
                    int count = 1;
                    while (listIterator.hasNext() && count <= 10) {
                        Pair<Integer, String> data = listIterator.next();
                        output += "#b" + data.getRight() + "#k is dropped by:\r\n";
                        try {
                            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM drop_data WHERE itemid = ? LIMIT 50");
                            ps.setInt(1, data.getLeft());
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                String resultName = MapleMonsterInformationProvider.getMobNameFromID(rs.getInt("dropperid"));
                                if (resultName != null) {
                                    output += resultName + ", ";
                                }
                            }
                            rs.close();
                            ps.close();
                        } catch (Exception e) {
                            player.message("There was a problem retreiving the required data. Please try again.");
                            return true;
                        }
                        output += "\r\n\r\n";
                        count++;
                    }
                } else {
                    player.message("The item you searched for doesn't exist.");
                    break;
                }
                c.announce(MaplePacketCreator.getNPCTalk(9010000, (byte) 0, output, "00 00", (byte) 0));
                break;
            case "save":
                player.saveToDB();
                player.message("Your data has been successfully saved.");
                break;
            case "online":
                int playersOn = 0;

                for (Channel ch : Server.getInstance().getChannelsFromWorld(player.getWorld())) {
                    player.yellowMessage("Players in Channel " + ch.getId() + ":");
                    for (MapleCharacter chars : ch.getPlayerStorage().getAllCharacters()) {
                        if (!chars.isIntern()) {
                            playersOn++;

                            if (chars.isInvisible()) {
                                player.message(" >> " + MapleCharacter.makeMapleReadable(chars.getName()) + " - has invisibility toggled on.");
                            } else {
                                switch (chars.getMap().getId()) {
                                    case 682010202:
                                        player.message(" >> " + MapleCharacter.makeMapleReadable(chars.getName()) + " - Chimney Possessed by the Scarecrow.");
                                        break;
                                    case 682010203:
                                        player.message(" >> " + MapleCharacter.makeMapleReadable(chars.getName()) + " - Chimney Possessed by the Clown.");
                                        break;
                                    case 682010201:
                                        player.message(" >> " + MapleCharacter.makeMapleReadable(chars.getName()) + " - Mansion's Main Chimney.");
                                        break;
                                    default:
                                        player.message(" >> " + MapleCharacter.makeMapleReadable(chars.getName()) + " - " + chars.getMap().getMapName() + ".");
                                        break;
                                }
                            }
                        }
                    }
                }
                player.yellowMessage("--------------------------");
                player.message("There are a total of " + playersOn + " players online.");
                break;
            case "rates":
                player.setRates();
                player.message("Your current rates - EXP: " + player.getExpRate() + "x || Meso: " + player.getMesoRate() + "x || Drop: " + player.getDropRate() + "x || Quest: " + player.getQuestRate() + "x.");
                break;
            case "dispose":
                NPCScriptManager.getInstance().dispose(c);
                c.announce(MaplePacketCreator.enableActions());
                c.removeClickedNPC();
                player.message("You have been successfully disposed.");
                break;
            case "gm":
                if (sub.length < 2) {
                    player.message("Syntax: @gm <message>");
                    break;
                } else if (player.getAutobanManager().getLastSpam(11) + 30000 > System.currentTimeMillis()) {
                    player.message("You can only request a GM every 30 seconds. If you found a bug, please report it on the forum.");
                    break;
                }

                timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                message = joinStringFrom(sub, 1);
                Server.getInstance().broadcastGMMessage(MaplePacketCreator.sendYellowTip("[GM Message] " + MapleCharacter.makeMapleReadable(player.getName()) + ": " + message));
                Server.getInstance().broadcastGMMessage(MaplePacketCreator.serverNotice(1, player.getName() + " : " + message));
                FilePrinter.printError("gm.txt", "[ " + timeStamp.format(new Date()) + " ] " + MapleCharacter.makeMapleReadable(player.getName()) + ": " + message + "\r\n");
                player.message("Your message '" + message + "' was sent to GMs.");
                player.getAutobanManager().spam(11);
                break;
//            case "bug":
//                if (sub.length < 2) {
//                    player.message("Syntax: @bug <description>");
//                    break;
//                }
//                message = joinStringFrom(sub, 1);
//                Server.getInstance().broadcastGMMessage(MaplePacketCreator.sendYellowTip("[BUG]:" + MapleCharacter.makeMapleReadable(player.getName()) + ": " + message));
//                Server.getInstance().broadcastGMMessage(MaplePacketCreator.serverNotice(1, message));
//                FilePrinter.printError("bug.txt", MapleCharacter.makeMapleReadable(player.getName()) + ": " + message + "\r\n");
//                player.message("Your bug '" + message + "' was submitted successfully to our developers. Thank you!");
//                break;
            case "joinevent":
            case "event":
            case "join":
                if (!FieldLimit.CHANGECHANNEL.check(player.getMap().getFieldLimit())) {
                    MapleEvent event = c.getChannelServer().getEvent();
                    if (event != null) {
                        if (event.getMapId() != player.getMapId()) {
                            if (event.getLimit() > 0) {
                                player.saveLocation("EVENT");

                                if (event.getMapId() == 109080000 || event.getMapId() == 109060001) {
                                    player.setTeam(event.getLimit() % 2);
                                }

                                event.minusLimit();

                                player.changeMap(event.getMapId());
                            } else {
                                player.message("The limit of players for the event has already been reached.");
                            }
                        } else {
                            player.message("You are already in the event.");
                        }
                    } else {
                        player.message("There is currently no event in progress.");
                    }
                } else {
                    player.message("You are currently in a map where you can't join an event.");
                }
                break;
            case "leaveevent":
            case "leave":
                int returnMap = player.getSavedLocation("EVENT");
                if (returnMap != -1) {
                    if (player.getOla() != null) {
                        player.getOla().resetTimes();
                        player.setOla(null);
                    }
                    if (player.getFitness() != null) {
                        player.getFitness().resetTimes();
                        player.setFitness(null);
                    }

                    player.changeMap(returnMap);
                    if (c.getChannelServer().getEvent() != null) {
                        c.getChannelServer().getEvent().addLimit();
                    }
                } else {
                    player.message("You are not currently in an event.");
                }
                break;
            case "bosshp":
                for (MapleMonster monster : player.getMap().getMonsters()) {
                    if (monster != null && monster.isBoss() && monster.getHp() > 0) {
                        long percent = monster.getHp() * 100L / monster.getMaxHp();
                        player.message(monster.getName() + " has " + percent + "% HP left.");
                        player.message("HP: " + monster.getHp() + " / " + monster.getMaxHp());
                    }
                }
                break;
            case "ranks":
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("SELECT `characters`.`name`, `characters`.`level` FROM `characters` LEFT JOIN accounts ON accounts.id = characters.accountid WHERE `characters`.`gm` = '0' AND `accounts`.`banned` = '0' ORDER BY level DESC, exp DESC LIMIT 50");
                    rs = ps.executeQuery();

                    player.announce(MaplePacketCreator.showPlayerRanks(9010000, rs));
                    ps.close();
                    rs.close();
                } catch (SQLException ex) {
                } finally {
                    try {
                        if (ps != null && !ps.isClosed()) {
                            ps.close();
                        }
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                    }
                }
                break;
            case "dpsranks":
                ps = null;
                rs = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("SELECT `characters`.`name`, `characters`.`dps` FROM `characters` LEFT JOIN accounts ON accounts.id = characters.accountid WHERE `characters`.`gm` = '0' AND `accounts`.`banned` = '0' ORDER BY dps DESC, level DESC LIMIT 50");
                    rs = ps.executeQuery();

                    player.announce(MaplePacketCreator.showPlayerRanksDPS(9010000, rs));
                    ps.close();
                    rs.close();
                } catch (SQLException ex) {
                } finally {
                    try {
                        if (ps != null && !ps.isClosed()) {
                            ps.close();
                        }
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                    }
                }
                break;
            default:
                if (player.gmLevel() == 0) {
                    player.message("Player command " + heading + sub[0] + " does not exist! Type @help for a list of commands.");
                }
                return false;
        }
        return true;
    }

    public static boolean executeInternCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        Channel cserv = c.getChannelServer();
        Server srv = Server.getInstance();

        if (sub[0].equals("say")) {
            if (sub.length < 2) {
                player.message("Syntax: !say <message>");
            } else {
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[" + player.getName() + "] " + joinStringFrom(sub, 1)));
            }
        } else if (sub[0].equals("ignore")) {
            if (sub.length < 2) {
                player.message("Syntax: !ignore <IGN>");
                return true;
            }
            MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);
            if (victim == null) {
                player.message(sub[1] + " is either offline or does not exist.");
                return true;
            }
            boolean monitored = MapleLogger.ignored.contains(victim.getName());
            if (monitored) {
                MapleLogger.ignored.remove(victim.getName());
            } else {
                MapleLogger.ignored.add(victim.getName());
            }
            player.message(victim.getName() + " is " + (!monitored ? "now being ignored." : "no longer being ignored."));
            String message = player.getName() + (!monitored ? " has started ignoring " : " has stopped ignoring ") + victim.getName() + ".";
            Server.getInstance().broadcastGMMessage(MaplePacketCreator.serverNotice(5, message));
        } else if (sub[0].equals("reloadmap")) {
            int map = c.getPlayer().getMap().getId();

            for (MapleCharacter chr : player.getMap().getCharacters()) {
                chr.changeMap(100000000);
            }

            player.changeMap(100000000);

            c.getChannelServer().getMapFactory().disposeMap(map);
            c.getChannelServer().getMapFactory().getMap(map);
        } else if (sub[0].equals("unstuck")) {
            if (sub.length == 2) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);
                if (victim != null) {
                    try {//sometimes bugged because the map = null
                        victim.getMap().removePlayer(victim);
                        victim.getClient().getChannelServer().removePlayer(victim);
                        victim.getClient().disconnect(true, false);
                        victim.unstuck();
                        player.message(sub[1] + " has been unstucked.");
                    } catch (Exception e) {
                    }
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !unstuck <IGN>");
            }
        } else if (sub[0].equals("job")) {
            switch (sub.length) {
                case 2:
                    player.changeJob(MapleJob.getById(Integer.parseInt(sub[1])));
                    player.equipChanged();
                    break;
                case 3:
                    MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                    target.changeJob(MapleJob.getById(Integer.parseInt(sub[2])));
                    player.equipChanged();
                    break;
                default:
                    player.message("Syntax: !job <job ID> || !job <IGN> <job ID>");
                    break;
            }
        } else if (sub[0].equals("findbyip")) {
            if (sub.length == 2) {
                String ign = sub[1];
                String ip = "";
                int accountID = -1;

                try {
                    // Get account ID

                    PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT accountid FROM characters WHERE name = ?");
                    ps.setString(1, ign);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        accountID = rs.getInt("accountid");
                    }

                    rs.close();
                    ps.close();

                    if (accountID == -1) {
                        player.message("No player found in the database with the IGN: " + ign);
                    } else {
                        // Get account IP

                        ps = DatabaseConnection.getConnection().prepareStatement("SELECT ip FROM accounts WHERE id = ?");
                        ps.setInt(1, accountID);

                        rs = ps.executeQuery();

                        if (rs.next()) {
                            ip = rs.getString("ip");
                        }

                        rs.close();
                        ps.close();

                        if (!ip.equals("")) {
                            Boolean banned = false;
                            String bannedReason = "";

                            // Get all accounts of IP
                            ps = DatabaseConnection.getConnection().prepareStatement("SELECT id, name, banned, banreason FROM accounts WHERE ip = ?");
                            ps.setString(1, ip);

                            rs = ps.executeQuery();

                            while (rs.next()) {
                                List<String> characters = new LinkedList<>();

                                String accountName = rs.getString("name");

                                accountID = rs.getInt("id");

                                if (rs.getInt("banned") == 1) {
                                    banned = true;
                                }

                                if (rs.getString("banreason") != null) {
                                    bannedReason = rs.getString("banreason");
                                }

                                // Get all characters from accounts
                                PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement("SELECT name FROM characters WHERE accountid = ?");
                                ps2.setInt(1, accountID);

                                ResultSet rs2 = ps2.executeQuery();

                                while (rs2.next()) {
                                    characters.add(rs2.getString("name"));
                                }

                                ps2.close();
                                rs2.close();

                                if (banned) {
                                    player.message("Account Name: " + accountName + " ( Banned | Reason: " + bannedReason + " )");
                                } else {
                                    player.message("Account Name: " + accountName);
                                }

                                if (characters.isEmpty()) {
                                    player.message("There are no characters for the account.");
                                } else {
                                    String names = "";

                                    for (String chr : characters) {
                                        names += chr + " | ";
                                    }

                                    player.message("Character Names: " + names);
                                }

                                player.message("------");
                            }

                            ps.close();
                            rs.close();
                        } else {
                            player.message("There are no IP address records of the player.");
                        }
                    }

                } catch (Exception e) {
                    player.message("Error! Something went wrong. Could not retrieve data.");
                }
            } else {
                player.message("Syntax: !findbyip <IGN>");
            }
        } else if (sub[0].equals("findbyhwid")) {
            if (sub.length == 2) {
                String ign = sub[1];
                String hwid = "";
                int accountID = -1;

                try {
                    // Get account ID

                    PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT accountid FROM characters WHERE name = ?");
                    ps.setString(1, ign);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        accountID = rs.getInt("accountid");
                    }

                    rs.close();
                    ps.close();

                    if (accountID == -1) {
                        player.message("No player found in the database with the IGN: " + ign);
                    } else {
                        // Get account HWID

                        ps = DatabaseConnection.getConnection().prepareStatement("SELECT hwid FROM accounts WHERE id = ?");
                        ps.setInt(1, accountID);

                        rs = ps.executeQuery();

                        if (rs.next()) {
                            hwid = rs.getString("hwid");
                        }

                        rs.close();
                        ps.close();

                        if (!hwid.equals("")) {
                            // Get all accounts of HWID
                            ps = DatabaseConnection.getConnection().prepareStatement("SELECT id, name, banned, banreason FROM accounts WHERE hwid = ?");
                            ps.setString(1, hwid);

                            rs = ps.executeQuery();

                            while (rs.next()) {
                                Boolean banned = false;
                                String bannedReason = "";

                                List<String> characters = new LinkedList<>();

                                String accountName = rs.getString("name");

                                accountID = rs.getInt("id");

                                if (rs.getInt("banned") == 1) {
                                    banned = true;
                                }

                                if (rs.getString("banreason") != null) {
                                    bannedReason = rs.getString("banreason");
                                }

                                // Get all characters from accounts
                                PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement("SELECT name FROM characters WHERE accountid = ?");
                                ps2.setInt(1, accountID);

                                ResultSet rs2 = ps2.executeQuery();

                                while (rs2.next()) {
                                    characters.add(rs2.getString("name"));
                                }

                                ps2.close();
                                rs2.close();

                                if (banned) {
                                    player.message("Account Name: " + accountName + " ( Banned | Reason: " + bannedReason + " )");
                                } else {
                                    player.message("Account Name: " + accountName);
                                }

                                if (characters.isEmpty()) {
                                    player.message("There are no characters for the account.");
                                } else {
                                    String names = "";

                                    for (String chr : characters) {
                                        names += chr + " | ";
                                    }

                                    player.message("Character Names: " + names);
                                }

                                player.message("------");
                            }

                            ps.close();
                            rs.close();
                        } else {
                            player.message("There are no HWID records of the player.");
                        }
                    }

                } catch (Exception e) {
                    player.message("Error! Something went wrong. Could not retrieve data.");
                }
            } else {
                player.message("Syntax: !findbyhwid <IGN>");
            }
        } else if (sub[0].equals("tag")) {
            MapleMap map = player.getMap();
            List<MapleMapObject> players = map.getMapObjectsInRange(player.getPosition(), (double) 2000, Arrays.asList(MapleMapObjectType.PLAYER));
            for (MapleMapObject closeplayers : players) {
                MapleCharacter playernear = (MapleCharacter) closeplayers;
                if (playernear.isAlive() && playernear != player && !playernear.isIntern()) {
                    if (playernear.isAlive() && playernear != player && !playernear.isIntern()) {
                        int returnMap = playernear.getSavedLocation("EVENT");

                        if (returnMap != -1) {
                            playernear.changeMap(returnMap);
                            playernear.message("You have been tagged.");
                        } else {
                            playernear.changeMap(100000000);
                        }
                    }
                }
            }
        } else if (sub[0].equals("item") || sub[0].equals("drop")) {
            int itemId = Integer.parseInt(sub[1]);
            short quantity = 1;
            try {
                quantity = Short.parseShort(sub[2]);
            } catch (Exception e) {
            }
            if (sub[0].equals("item")) {
                int petid = -1;
                if (ItemConstants.isPet(itemId)) {
                    petid = MaplePet.createPet(itemId);
                }
                MapleInventoryManipulator.addById(c, itemId, quantity, player.getName(), petid, -1);
            } else {
                Item toDrop;
                if (MapleItemInformationProvider.getInstance().getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    toDrop = MapleItemInformationProvider.getInstance().getEquipById(itemId);
                } else {
                    toDrop = new Item(itemId, (short) 0, quantity);
                }
                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
            }
        } else if (sub[0].equals("warpoutevent")) {
            if (sub.length == 2) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);

                if (victim != null) {
                    int returnMap = victim.getSavedLocation("EVENT");

                    if (returnMap != -1) {
                        victim.changeMap(returnMap);
                        player.message(sub[1] + " has been warped out and unregistered from the event.");
                    } else {
                        player.message(sub[1] + " is not registered in the event.");
                    }
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !warpoutevent <IGN>");
            }
        } else if (sub[0].equals("warpalloutevent")) {
            for (MapleCharacter target : player.getMap().getCharacters()) {
                if (!target.isIntern()) {
                    int returnMap = target.getSavedLocation("EVENT");

                    if (returnMap != -1) {
                        target.changeMap(returnMap);
                    }
                }
            }

            player.message("All players that correctly registered in the event has been warped out and unregistered from the event.");
        } else if (sub[0].equals("lastentered")) {
            MapleMap map = player.getMap();

            if (map.getLastEntered() == null || map.getLastEntered().size() <= 0) {
                player.message("There are no records of players entering this map.");
            } else {
                for (String name : map.getLastEntered()) {
                    player.message(name);
                }
            }
        } else if (sub[0].equals("spawn")) {
            MapleMonster monster = MapleLifeFactory.getMonster(Integer.parseInt(sub[1]));
            if (monster == null) {
                player.message("That mob does not exist.");
                return true;
            }

            switch (sub.length) {
                case 2:
                    player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(Integer.parseInt(sub[1])), player.getPosition());
                    break;
                case 3:
                    for (int i = 0; i < Integer.parseInt(sub[2]); i++) {
                        player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(Integer.parseInt(sub[1])), player.getPosition());
                    }
                    break;
                default:
                    player.message("Syntax: !spawn <mob ID> || !spawn <mob ID> <amount>");
                    break;
            }
        } else if (sub[0].equals("maxskills")) {
            for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
                try {
                    Skill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                    if (GameConstants.isInJobTree(skill.getId(), player.getJob().getId())) {
                        player.changeSkillLevel(skill, (byte) skill.getMaxLevel(), skill.getMaxLevel(), -1);
                    }
                } catch (NumberFormatException nfe) {
                    break;
                } catch (NullPointerException npe) {
                }
            }

            player.message("All of your skills has been maxed out.");
        } else if (sub[0].equals("online2")) {
            int total = 0;
            for (Channel ch : Server.getInstance().getChannelsFromWorld(player.getWorld())) {
                int size = ch.getPlayerStorage().getAllCharacters().size();
                total += size;
                String s = "(Channel " + ch.getId() + " Online: " + size + ") : ";
                for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters()) {
                    s += MapleCharacter.makeMapleReadable(chr.getName()) + ", ";
                }
                player.message(s.substring(0, s.length() - 2));
            }
            player.message("There are a total of " + total + " players online.");
        } else if (sub[0].equals("ban")) {
            if (sub.length < 3) {
                player.message("Syntax: !ban <IGN> <reason>");
                return true;
            }

            String ign = sub[1];
            String reason = joinStringFrom(sub, 2);
            String userreason = reason;

            MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(ign);

            if (target != null) {
                if (player.gmLevel() > target.gmLevel()) {
                    String readableTargetName = MapleCharacter.makeMapleReadable(target.getName());
                    String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];

                    reason = c.getPlayer().getName() + " banned " + readableTargetName + " for " + reason + " (IP: " + ip + ") " + "(MAC: " + c.getMacs() + ")";
                    target.ban(reason);
                    target.yellowMessage("You have been permanently banned by " + c.getPlayer().getName() + ". If you believe this was a mistake, please post an appeal thread on the forum.");
                    target.yellowMessage("Ban reason: " + userreason);
                    final MapleCharacter rip = target;
                    TimerManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            rip.getClient().disconnect(false, false);
                        }
                    }, 5000); //5 Seconds
                    Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Banned] " + ign + " has been permanently banned."));
                } else {
                    player.message("You cannot ban a higher level GM.");
                }
            } else if (MapleCharacter.ban(ign, reason, false)) {
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Banned] " + ign + " has been permanently banned."));
            } else {
                player.message("You have entered an invalid character name.");
            }
        } else if (sub[0].equals("hardban")) {
            if (sub.length < 3) {
                player.message("Syntax: !hardban <IGN> <reason>");
                return true;
            }

            String ign = sub[1];
            String reason = joinStringFrom(sub, 2);
            String userreason = reason;
            MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(ign);

            if (target != null) {
                if (player.gmLevel() > target.gmLevel()) {
                    String readableTargetName = MapleCharacter.makeMapleReadable(target.getName());
                    String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];

                    target.getClient().banHWID();

                    //Ban ip
                    PreparedStatement ps;
                    try {
                        Connection con = DatabaseConnection.getConnection();
                        if (ip.matches("/[0-9]{1,3}\\..*")) {
                            ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                            ps.setString(1, ip);
                            ps.executeUpdate();
                            ps.close();
                        }
                    } catch (SQLException ex) {
                        c.getPlayer().message("Error occured while banning IP address");
                        c.getPlayer().message(target.getName() + "'s IP was not banned: " + ip);
                    }

                    //target.getClient().banMacs();
                    reason = c.getPlayer().getName() + " banned " + readableTargetName + " for " + reason + " (IP: " + ip + ") " + "(MAC: " + c.getMacs() + ")";

                    target.ban(reason);
                    target.yellowMessage("You have been banned by " + c.getPlayer().getName() + ". If you believe this was a mistake, please post an appeal thread on the forum.");
                    target.yellowMessage("Ban reason: " + userreason);

                    final MapleCharacter rip = target;
                    TimerManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            rip.getClient().disconnect(false, false);
                        }
                    }, 5000); //5 Seconds
                    player.message(ign + " has been account banned, HWID banned, and IP address banned.");
                    Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Banned] " + ign + " has been permanently banned."));
                } else {
                    player.message("You cannot ban a higher level GM.");
                }
            } else if (MapleCharacter.ban(ign, reason, false)) {
                player.message(ign + " is offline so only his account has been banned.");
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Banned] " + ign + " has been permanently banned."));
            } else {
                player.message("You have entered an invalid character name.");
            }
        } else if (sub[0].equals("tempban")) {
            if (sub.length < 5) {
                player.message("Syntax: !tempban <IGN> <duration> <minutes || days> <reason>");
                return true;
            }

            String ign = sub[1];
            String reason = joinStringFrom(sub, 4);
            String userreason = reason;
            int duration = Integer.parseInt(sub[2]);
            String type = sub[3];

            MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(ign);

            if (target != null) {
                String readableTargetName = MapleCharacter.makeMapleReadable(target.getName());
                String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                reason = c.getPlayer().getName() + " banned " + readableTargetName + " for " + reason + " (IP: " + ip + ") " + "(MAC: " + c.getMacs() + ")";
                target.tempban(reason, duration, type);
                target.yellowMessage("You have been temporarily banned by " + c.getPlayer().getName() + " for " + sub[2] + " " + sub[3] + ".");
                target.yellowMessage("Ban reason: " + userreason);

                final MapleCharacter rip = target;
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        rip.getClient().disconnect(false, false);
                    }
                }, 5000); //5 Seconds
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Temp Banned] " + ign + " has been temporarily banned for " + sub[2] + " " + sub[3] + "."));
            } else if (MapleCharacter.tempban(ign, duration, type, reason, false)) {
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Temp Banned] " + ign + " has been temporarily banned for " + sub[2] + " " + sub[3] + "."));
            } else {
                player.message("You have entered an invalid character name.");
            }
        } else if (sub[0].equals("startevent")) {
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                player.getMap().startEvent(chr);
            }
            c.getChannelServer().setEvent(null);

            player.message("The event has started.");
        } else if (sub[0].equals("openevent")) {
            int players = 1000;

            if (sub.length == 2) {
                players = Integer.parseInt(sub[1]);
            }

            c.getChannelServer().setEvent(new MapleEvent(player.getMapId(), players));
            player.message("The event has been set on " + player.getMap().getMapName() + " and will allow " + players + " players to join.");
        } else if (sub[0].equals("closeevent")) {
            c.getChannelServer().setEvent(null);
            player.message("The event has been closed. No more players can join.");
        } else if (sub[0].equalsIgnoreCase("search")) {
            StringBuilder sb = new StringBuilder();
            if (sub.length > 2) {
                String search = joinStringFrom(sub, 2);
                long start = System.currentTimeMillis();//for the lulz
                MapleData data = null;
                MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz"));
                if (!sub[1].equalsIgnoreCase("ITEM")) {
                    if (sub[1].equalsIgnoreCase("NPC")) {
                        data = dataProvider.getData("Npc.img");
                    } else if (sub[1].equalsIgnoreCase("MOB") || sub[1].equalsIgnoreCase("MONSTER")) {
                        data = dataProvider.getData("Mob.img");
                    } else if (sub[1].equalsIgnoreCase("SKILL")) {
                        data = dataProvider.getData("Skill.img");
                    } else if (sub[1].equalsIgnoreCase("MAP")) {
                        sb.append("#bUse the '/m' command to find a map. If it finds a map with the same name, it will warp you to it.");
                    } else {
                        sb.append("#bInvalid search.\r\nSyntax: '/search [type] [name]', where [type] is NPC, ITEM, MOB, or SKILL.");
                    }
                    if (data != null) {
                        String name;
                        for (MapleData searchData : data.getChildren()) {
                            name = MapleDataTool.getString(searchData.getChildByPath("name"), "NO-NAME");
                            if (name.toLowerCase().contains(search.toLowerCase())) {
                                sb.append("#b").append(Integer.parseInt(searchData.getName())).append("#k - #r").append(name).append("\r\n");
                            }
                        }
                    }
                } else {
                    for (Pair<Integer, String> itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
                        if (sb.length() < 32654) {//ohlol
                            if (itemPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                                //#v").append(id).append("# #k-
                                sb.append("#b").append(itemPair.getLeft()).append("#k - #r").append(itemPair.getRight()).append("\r\n");
                            }
                        } else {
                            sb.append("#bCouldn't load all items, there are too many results.\r\n");
                            break;
                        }
                    }
                }
                if (sb.length() == 0) {
                    sb.append("#bNo ").append(sub[1].toLowerCase()).append("s found.\r\n");
                }
                sb.append("\r\n#kLoaded within ").append((double) (System.currentTimeMillis() - start) / 1000).append(" seconds.");//because I can, and it's free
            } else {
                sb.append("#bInvalid search.\r\nSyntax: '/search [type] [name]', where [type] is NPC, ITEM, MOB, or SKILL.");
            }
            c.announce(MaplePacketCreator.getNPCTalk(9010000, (byte) 0, sb.toString(), "00 00", (byte) 0));
//        } else if (sub[0].equals("warpsnowball")) {
//            List<MapleCharacter> chars = new ArrayList<>(player.getMap().getCharacters());
//            for (MapleCharacter chr : chars) {
//                chr.changeMap(109060000, chr.getTeam());
//            }
        } else if (sub[0].equals("unregisterplayer")) {
            if (sub.length == 2) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target == null) {
                    player.message(sub[1] + " is either offline or does not exist.");
                } else {
                    for (MapleExpedition exped : target.getClient().getChannelServer().getExpeditions()) {
                        if (exped.contains(target)) {
                            if (exped.getMembers().size() == 1) {
                                exped.dispose(true);
                            }

                            exped.removeMember(target);
                        }

                        if (player.getOla() != null) {
                            player.getOla().resetTimes();
                            player.setOla(null);
                        }
                        if (player.getFitness() != null) {
                            player.getFitness().resetTimes();
                            player.setFitness(null);
                        }

                        if (c.getChannelServer().getEvent() != null) {
                            c.getChannelServer().getEvent().addLimit();
                        }
                    }

                    if (target.getEventInstance() != null) {
                        target.getEventInstance().unregisterPlayer(target);
                    }

                    target.stopDojoTime();

                    target.changeMap(100000000);

                    player.message(sub[1] + " has been unregistered.");
                }
            } else {
                player.message("Syntax: !unregisterplayer <IGN>");
            }
        } else if (sub[0].equals("resetmapowner")) {
            player.getMap().resetMapOwner();
            player.getMap().setMapOwnerTime(30);

            player.message("The map owner of this map has been reset.");
        } else if (sub[0].equals("maxstat")) {
            final String[] s = {"setall", String.valueOf(Short.MAX_VALUE)};
            executeGMCommand(c, s, heading);
            player.setLevel(255);
            player.setMaxHp(30000);
            player.setMaxMp(30000);
            player.updateSingleStat(MapleStat.LEVEL, 255);
            player.updateSingleStat(MapleStat.MAXHP, 30000);
            player.updateSingleStat(MapleStat.MAXMP, 30000);

            player.message("All of your stats has been maxed out.");
        } else if (sub[0].equals("levelpro")) {
            while (player.getLevel() < Math.min(255, Integer.parseInt(sub[1]))) {
                player.levelUp(false);
            }

            player.message("Your level has been set to 255.");
        } else if (sub[0].equals("setall")) {
            if (sub.length == 2) {
                final int x = Short.parseShort(sub[1]);
                player.setStr(x);
                player.setDex(x);
                player.setInt(x);
                player.setLuk(x);
                player.updateSingleStat(MapleStat.STR, x);
                player.updateSingleStat(MapleStat.DEX, x);
                player.updateSingleStat(MapleStat.INT, x);
                player.updateSingleStat(MapleStat.LUK, x);

                player.message("All of your stats has been set to " + sub[1] + ".");
            } else {
                player.message("Syntax: !setall <amount>");
            }
        } else if (sub[0].equals("killall")) {
            List<MapleMapObject> monsters = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
            MapleMap map = player.getMap();
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                if (!monster.getStats().isFriendly()) {
                    map.killMonster(monster, player, true);
                    monster.giveExpToCharacter(player, monster.getExp() * c.getPlayer().getExpRate(), true, 1);
                }
            }
            player.message("Killed " + monsters.size() + " monsters.");
        } else if (sub[0].equals("dc")) {
            if (sub.length == 2) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);
                if (victim != null) {
                    if (player.gmLevel() > victim.gmLevel()) {
                        try {//sometimes bugged because the map = null
                            victim.getClient().disconnect(false, false);
                            player.message(sub[1] + " has disconnected.");
                        } catch (Exception e) {
                        }
                    } else {
                        player.message("You can not disconnect a higher ranking GM.");
                    }
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !dc <IGN>");
            }
        } else if (sub[0].equals("chattype")) {
            player.toggleWhiteChat();
            player.message("Your chat is now " + (player.getWhiteChat() ? "white" : "normal") + ".");
        } else if (sub[0].equals("warphere")) {
            if (sub.length == 2) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (victim != null) {
                    if (victim.getEventInstance() != null) {
                        victim.getEventInstance().unregisterPlayer(victim);
                    }

                    victim.stopDojoTime();
                    //Attempt to join the warpers instance.
                    //If victim isn't in an event instance, just warp them.
                    if (player.getClient().getChannel() != victim.getClient().getChannel()) {
                        victim.getClient().changeChannel(player.getClient().getChannel());
                    }

                    victim.changeMap(player.getMapId(), player.getMap().findClosestPortal(player.getPosition()));
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !warphere <IGN>");
            }
        } else if (sub[0].equals("forcesave")) {
            if (sub.length == 2) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    target.saveToDB();

                    player.message(sub[1] + "'s data has been successfully saved.");
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !forcesave <IGN>");
            }
        } else if (sub[0].equals("check")) {
            if (sub.length == 2) {
                String ign = sub[1];
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(ign);

                if (victim != null) {
                    int enchantedscroll = victim.getItemQuantity(5221001, false);

                    int guildVault = 0;

                    if (victim.getGuild() != null) {
                        guildVault = victim.getGuild().getGP();
                    }

                    player.message("Level: " + victim.getLevel() + " | Fame: " + victim.getFame());
                    player.message("NX Credit: " + victim.getCashShop().getCash(1) + " | Mesos: " + victim.getMeso());
                    player.message("Vote Points: " + victim.getClient().getVotePoints() + " | Donator Points: " + victim.getClient().getDonatorPoints() + " | Fishing Points: " + victim.getFishingPoints() + " | Crafting Level: " + victim.getCraftingLevel());
                    player.message("Mob Points: " + victim.getMobPoints() + " | Streamer Points: " + victim.getStreamerPoints() + " | Dojo Points: " + victim.getDojoPoints());
                    player.message("Guild Points: " + victim.getGuildPoints() + " | Guild Vault: " + guildVault);
                    player.message("Enchanted Scroll: " + enchantedscroll);
                    player.message("Total HP: " + victim.getMaxHp() + " | Total MP: " + victim.getMaxMp());
                    player.message("STR " + victim.getTotalStr() + " | DEX: " + victim.getTotalDex() + " | INT: " + victim.getTotalInt() + " | LUK: " + victim.getTotalLuk());
                    player.message("WATK: " + victim.getTotalWatk() + " | MATK: " + victim.getTotalMagic());
                    player.message("Remaining AP: " + victim.getRemainingAp() + " | Remaining SP: " + victim.getRemainingSp());
                    player.message("Job: " + victim.getJob().name() + " | Map: " + victim.getMap().getMapName());

                    if (player.isMuted()) {
                        player.message("Muted: Yes");
                    } else {
                        player.message("Muted: No");
                    }

                    if (player.isStreamer()) {
                        String output = "Verified Streamer: Yes";

                        if (victim.isStreaming()) {
                            output += " | Is Streaming: Yes | Stream URL: " + victim.getStreamerURL();
                        } else {
                            output += " | Is Streaming: No | Stream URL: " + victim.getStreamerURL();
                        }

                        player.message(output);
                    } else {
                        player.message("Verified Streamer: No");
                    }

                    player.message("Account Username: " + victim.getClient().getAccountName());
                    player.message("IP Address: " + victim.getClient().getSession().getRemoteAddress().toString().split(":")[0] + " | HWID: " + victim.getClient().getHWID());
                } else {
                    try {
                        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE name = ?");
                        ps.setString(1, ign);

                        ResultSet rs = ps.executeQuery();

                        int charid = -1;
                        int accid = 0;

                        int level = 0;
                        int fame = 0;
                        int nx = 0;
                        int mesos = 0;
                        int votePoints = 0;
                        int donatorPoints = 0;
                        int mobPoints = 0;
                        int fishingPoints = 0;
                        int streamerPoints = 0;
                        int enchantedScroll = 0;
                        int dojoPoints = 0;
                        int job = 0;
                        int mapID = 0;
                        int craftingLevel = 0;
                        int totalHP = 0;
                        int totalMP = 0;
                        int STR = 0;
                        int DEX = 0;
                        int INT = 0;
                        int LUK = 0;
                        int remainingAP = 0;
                        Boolean banned = false;
                        String remainingSP = "";
                        String ipAddress = "";
                        String accountUsername = "";
                        String HWID = "";
                        Boolean verifiedStreamer = false;

                        if (rs.next()) {
                            charid = rs.getInt("id");
                            accid = rs.getInt("accountid");

                            level = rs.getInt("level");
                            fame = rs.getInt("fame");
                            mesos = rs.getInt("meso");
                            mobPoints = rs.getInt("mobPoints");
                            craftingLevel = rs.getInt("craftinglevel");
                            job = rs.getInt("job");
                            mapID = rs.getInt("map");
                            dojoPoints = rs.getInt("dojoPoints");
                            totalHP = rs.getInt("maxhp");
                            totalMP = rs.getInt("maxmp");
                            STR = rs.getInt("str");
                            DEX = rs.getInt("dex");
                            INT = rs.getInt("int");
                            LUK = rs.getInt("luk");
                            remainingAP = rs.getInt("ap");
                            remainingSP = rs.getString("sp").split(",")[0];
                        }

                        rs.close();
                        ps.close();

                        if (charid == -1) {
                            player.message(ign + " does not exist in the database.");
                        } else {
                            ps = DatabaseConnection.getConnection().prepareStatement("SELECT quantity FROM inventoryitems WHERE characterid = ? AND itemid = 2340000");
                            ps.setInt(1, charid);

                            rs = ps.executeQuery();

                            while (rs.next()) {
                                enchantedScroll += rs.getInt("quantity");
                            }

                            rs.close();
                            ps.close();

                            ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM accounts WHERE id = ?");
                            ps.setInt(1, accid);

                            rs = ps.executeQuery();

                            if (rs.next()) {
                                votePoints = rs.getInt("votes");
                                donatorPoints = rs.getInt("donatorpoints");
                                streamerPoints = rs.getInt("streamerpoints");
                                nx = rs.getInt("nxCredit");
                                ipAddress = rs.getString("ip");
                                HWID = rs.getString("hwid");
                                accountUsername = rs.getString("name");

                                if (rs.getInt("banned") == 1) {
                                    banned = true;
                                }

                                if (rs.getString("streamer") != null) {
                                    verifiedStreamer = true;
                                }
                            }

                            rs.close();
                            ps.close();

                            player.message("Level: " + level + " | Fame: " + fame);
                            player.message("NX Credit: " + nx + " | Mesos: " + mesos);
                            player.message("Vote Points: " + votePoints + " | Donator Points: " + donatorPoints + " | Fishing Points: " + fishingPoints + " | Crafting Level: " + craftingLevel);
                            player.message("Mob Points: " + mobPoints + " | Streamer Points: " + streamerPoints + " | Dojo Points: " + dojoPoints);
                            player.message("Enchanted Scroll: " + enchantedScroll);
                            player.message("Total HP: " + totalHP + " | Total MP: " + totalMP);
                            player.message("STR " + STR + " | DEX: " + DEX + " | INT: " + INT + " | LUK: " + LUK);
                            player.message("WATK: Indeterminable (Offline) | MATK: Indeterminable (Offline)");
                            player.message("Remaining AP: " + remainingAP + " | Remaining SP: " + remainingSP);
                            player.message("Job: " + job + " | Map: " + mapID);

                            player.message("Muted: Indeterminable (Offline)");

                            if (banned) {
                                player.message("Muted: Indeterminable (Offline) | Banned: Yes");
                            } else {
                                player.message("Muted: Indeterminable (Offline) | Banned: No");
                            }

                            if (verifiedStreamer) {
                                player.message("Verified Streamer: Yes");
                            } else {
                                player.message("Verified Streamer: No");
                            }

                            player.message("Account Username: " + accountUsername);
                            player.message("IP Address: " + ipAddress + " | HWID: " + HWID);
                        }

                    } catch (Exception e) {
                        player.message("Error! Something went wrong. Could not retrieve data.");
                    }
                }
            } else {
                player.message("Syntax: !check <IGN>");
            }
        } else if (sub[0].equals("inmap")) {
            String s = "";
            int count = 0;
            int countNonGM = 0;

            for (MapleCharacter chr : player.getMap().getCharacters()) {
                s += chr.getName() + " ";
                count++;

                if (!chr.isIntern()) {
                    countNonGM++;
                }
            }
            player.message(s);
            player.message("There are a total of " + count + " players in this map.");
            player.message("There are a total of " + countNonGM + " players in this map excluding yourself and GMs.");
        } else if (sub[0].equals("cleardrops")) {
            player.getMap().clearDrops(player);
            player.message("Drops have been cleared.");
        } else if (sub[0].equals("go")) {
            if (sub.length == 2) {
                if (gotomaps.containsKey(sub[1])) {
                    MapleMap target = c.getChannelServer().getMapFactory().getMap(gotomaps.get(sub[1]));
                    MaplePortal targetPortal = target.getPortal(0);

                    if (player.getEventInstance() != null) {
                        player.getEventInstance().unregisterPlayer(player);
                    }
                    player.changeMap(target, targetPortal);
                } else {
                    player.message("That map does not exist.");
                }
            } else {
                player.message("Syntax: !go <map name>");
            }
        } else if (sub[0].equals("mutemap")) {
            if (player.getMap().isMuted()) {
                player.getMap().setMuted(false);
                for (MapleCharacter chr : player.getMap().getCharacters()) {
                    chr.message("The map you are in has been unmuted.");
                }
            } else {
                player.getMap().setMuted(true);
                for (MapleCharacter chr : player.getMap().getCharacters()) {
                    chr.message("The map you are in has been muted.");
                }
            }
        } else if (sub[0].equals("mute")) {
            if (sub.length == 2) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    if (target.isMuted()) {
                        target.setMute(false);
                        target.message("You have been unmuted by " + player.getName());
                        player.message(sub[1] + " has been unmuted.");
                    } else {
                        target.setMute(true);
                        target.message("You have been muted by " + player.getName());
                        player.message(sub[1] + " has been muted.");
                    }
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !mute <IGN>");
            }
        } else if (sub[0].equals("bomb")) {
            switch (sub.length) {
                case 1:
                    player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), player.getPosition());
                    break;
                case 2:
                    MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);
                    victim.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), victim.getPosition());

                    player.message(sub[1] + " has been bombed.");
                    break;
                default:
                    player.message("Syntax: !bomb || !bomb <IGN>");
                    break;
            }
        } else if (sub[0].equals("bombmap")) {
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                chr.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), chr.getPosition());
            }

            player.message("The whole map has been bombed.");
        } else if (sub[0].equals("whereami")) { //This is so not going to work on the first commit
            player.message("Map ID: " + player.getMap().getId());
            player.message("NPCs on this map:");
            for (MapleMapObject npcs : player.getMap().getMapObjects()) {
                if (npcs instanceof MapleNPC) {
                    MapleNPC npc = (MapleNPC) npcs;
                    player.message(">> " + npc.getName() + " - " + npc.getId());
                }
            }
            player.message("Monsters on this map:");
            for (MapleMapObject mobs : player.getMap().getMapObjects()) {
                if (mobs instanceof MapleMonster) {
                    MapleMonster mob = (MapleMonster) mobs;
                    if (mob.isAlive()) {
                        player.message(">> " + mob.getName() + " - " + mob.getId());
                    }
                }
            }
        } else if (sub[0].equals("clock")) {
            if (sub.length == 2) {
                player.getMap().broadcastMessage(MaplePacketCreator.getClock(Integer.parseInt(sub[1])));
            } else {
                player.message("Syntax: !clock <seconds>");
            }
        } else if (sub[0].equals("removeclock")) {
            player.getMap().broadcastMessage(MaplePacketCreator.removeClock());
        } else if (sub[0].equals("diseasemap")) {
            if (sub.length == 2) {
                int type = 0;

                if (sub[1].equalsIgnoreCase("SEAL")) {
                    type = 120;
                } else if (sub[1].equalsIgnoreCase("DARKNESS")) {
                    type = 121;
                } else if (sub[1].equalsIgnoreCase("WEAKEN")) {
                    type = 122;
                } else if (sub[1].equalsIgnoreCase("STUN")) {
                    type = 123;
                } else if (sub[1].equalsIgnoreCase("POISON")) {
                    type = 125;
                } else if (sub[1].equalsIgnoreCase("SEDUCE")) {
                    type = 128;
                } else {
                    player.message("Syntax: !diseasemap <seal/darkness/weaken/stun/poison/seduce>");
                    return true;
                }
                for (MapleCharacter chr : player.getMap().getCharacters()) {
                    if (!chr.isIntern()) {
                        chr.giveDebuff(MapleDisease.getType(type), MobSkillFactory.getMobSkill(type, 1), 3600000);
                    }
                }
            } else {
                player.message("Syntax: !diseasemap <seal/darkness/weaken/stun/poison/seduce>");
            }
        } else if (sub[0].equals("disease")) {
            if (sub.length == 3) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (victim != null) {
                    int type;

                    if (sub[2].equalsIgnoreCase("SEAL")) {
                        type = 120;
                    } else if (sub[2].equalsIgnoreCase("DARKNESS")) {
                        type = 121;
                    } else if (sub[2].equalsIgnoreCase("WEAKEN")) {
                        type = 122;
                    } else if (sub[2].equalsIgnoreCase("STUN")) {
                        type = 123;
                    } else if (sub[2].equalsIgnoreCase("POISON")) {
                        type = 125;
                    } else if (sub[2].equalsIgnoreCase("SEDUCE")) {
                        type = 128;
                    } else {
                        player.message("Syntax: !disease <IGN> <seal/darkness/weaken/stun/poison/seduce>");
                        return true;
                    }

                    victim.giveDebuff(MapleDisease.getType(type), MobSkillFactory.getMobSkill(type, 1), 3600000);
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }

            } else {
                player.message("Syntax: !disease <IGN> <seal/darkness/weaken/stun/poison/seduce>");
            }
        } else if (sub[0].equals("curemap")) {
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                chr.dispelDebuffs();
            }
        } else if (sub[0].equals("heal")) {
            switch (sub.length) {
                case 1:
                    player.setHpMp(30000);
                    player.message("You have been healed.");
                    break;
                case 2:
                    MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                    if (victim != null) {
                        victim.setHpMp(30000);
                        player.message(sub[1] + " has been healed.");
                    } else {
                        player.message(sub[1] + " is either offline or does not exist.");
                    }
                    break;
                default:
                    player.message("Syntax: !heal || !heal <IGN>");
            }
        } else if (sub[0].equals("healmap")) {
            for (MapleCharacter target : player.getMap().getCharacters()) {
                target.setHpMp(30000);
            }
            player.message("Everyone in the map has been healed.");
        } else if (sub[0].equals("toggleplayerstream")) {
            if (sub.length == 2) {
                MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);

                if (victim.isStreamer()) {
                    if (victim.isStreaming()) {
                        victim.setStreaming(false);
                        victim.message("You are no longer streaming.");
                        player.message(sub[1] + " stream has been toggled off.");
                    } else {
                        victim.setStreaming(true);
                        victim.message("You are now streaming. You will receive one streamer point every minute. Please note that you can not AFK while leaving @togglestream on.");
                        player.message(sub[1] + " stream has been toggled on.");
                    }
                } else {
                    player.message(sub[1] + " is not a verified streamer so the stream cannot be toggled on.");
                }
            } else {
                player.message("Syntax: !toggleplayerstream <IGN>");
            }
        } else if (sub[0].equals("map")) {
            if (sub.length == 2) {
                MapleMap map;

                try {
                    map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(sub[1]));
                    if (map == null) {
                        player.message("Map ID " + sub[1] + " is invalid.");
                        return true;
                    }
                    if (player.getEventInstance() != null) {
                        player.getEventInstance().unregisterPlayer(player);
                    }
                    player.changeMap(map, map.getPortal(0));
                } catch (Exception ex) {
                    player.message("Map ID " + sub[1] + " is invalid.");
                    return true;
                }
            } else {
                player.message("Syntax: !map <map ID>");
            }
        } else if (sub[0].equals("godmodemap")) {
            if (player.getMap().isGodmode()) {
                player.getMap().setGodmode(false);

                for (MapleCharacter chr : player.getMap().getCharacters()) {
                    chr.message("Godmode has been disabled in the map you are in.");
                }
            } else {
                player.getMap().setGodmode(true);

                for (MapleCharacter chr : player.getMap().getCharacters()) {
                    chr.message("Godmode has been enabled in the map you are in. You will no longer take damage from anything.");
                }
            }
        } else if (sub[0].equals("warp")) {
            switch (sub.length) {
                case 2: {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
                    if (victim == null) {//If victim isn't on current channel or isnt a character try and find him by loop all channels on current world.
                        for (Channel ch : srv.getChannelsFromWorld(c.getWorld())) {
                            victim = ch.getPlayerStorage().getCharacterByName(sub[1]);
                            if (victim != null) {
                                break;//We found the person, no need to continue the loop.
                            }
                        }
                    }

                    if (victim != null) {//If target isn't null attempt to warp.
                        if (player.getEventInstance() != null) {
                            player.getEventInstance().unregisterPlayer(player);
                        }

                        player.changeMap(victim.getMapId(), victim.getMap().findClosestPortal(victim.getPosition()));

                        if (player.getClient().getChannel() != victim.getClient().getChannel()) {//And then change channel if needed.
                            player.message("Changing to channel " + victim.getClient().getChannel() + ".");
                            player.getClient().changeChannel(victim.getClient().getChannel());
                        }

                    } else {
                        player.message(sub[1] + " is either offline or does not exist.");
                    }
                    break;
                }
                case 3: {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
                    MapleMap map;

                    try {
                        map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(sub[2]));
                        if (map == null) {
                            player.message("Map ID " + sub[2] + " is invalid.");
                            return true;
                        }
                    } catch (Exception ex) {
                        player.message("Map ID " + sub[2] + " is invalid.");
                        return true;
                    }
                    if (victim == null) {//If victim isn't on current channel, loop all channels on current world.
                        for (Channel ch : srv.getChannelsFromWorld(c.getWorld())) {
                            victim = ch.getPlayerStorage().getCharacterByName(sub[1]);
                            if (victim != null) {
                                break;//We found the person, no need to continue the loop.
                            }
                        }
                    }
                    if (victim != null) {
                        if (victim.getEventInstance() != null) {
                            victim.getEventInstance().unregisterPlayer(victim);
                        }

                        victim.changeMap(map, map.getPortal(0));
                    } else {
                        player.message(sub[1] + " is either offline or does not exist.");
                    }
                    break;
                }
                default:
                    player.message("Syntax: !warp <IGN> || !warp <IGN> <map ID>");
            }
        } else if (sub[0].equals("muteallsmegas")) {
            if (ServerConstants.ENABLE_SMEGA) {
                ServerConstants.ENABLE_SMEGA = false;
                player.message("All smegas have been disabled.");
            } else {
                ServerConstants.ENABLE_SMEGA = true;
                player.message("All smegas have been enabled.");
            }
        } else if (sub[0].equals("cure")) {
            if (sub.length == 2) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (victim != null) {
                    victim.dispelDebuffs();
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !cure <IGN>");
            }
        } else if (sub[0].equals("itemvac")) {
            List<MapleMapObject> items = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM));
            for (MapleMapObject item : items) {
                MapleMapItem mapitem = (MapleMapItem) item;
                if (!MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true)) {
                    continue;
                }
                mapitem.setPickedUp(true);
                player.getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 2, player.getId()), mapitem.getPosition());
                player.getMap().removeMapObject(item);
            }
        } else if (sub[0].equals("giftguildlevel")) {
            if (sub.length == 3) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    if (target.getGuild() != null) {
                        target.getGuild().gainGuildLevel(Integer.parseInt(sub[2]));

                        player.message(sub[1] + "'s guild has been gifted " + sub[2] + " levels.");
                    } else {
                        player.message(sub[1] + " is not in a guild.");
                    }
                } else {
                    player.message(sub[1] + " is not online.");
                }
            } else {
                player.message("Syntax: !giftguildlevel <IGN> <amount>");
            }
        } else if (sub[0].equals("giftdp")) {
            if (sub.length == 3) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    target.getClient().addDonatorPoints(Integer.parseInt(sub[2]));
                    target.message("You have received " + sub[2] + " donator points from " + player.getName() + ".");

                    player.message(sub[1] + " has been gifted " + sub[2] + " donator points.");
                } else if (c.addDonatorPointsOffline(sub[1], Integer.parseInt(sub[2]))) {
                    player.message(sub[1] + " has been gifted " + sub[2] + " donator points.");
                } else {
                    player.message(sub[1] + " is not found.");
                }
            } else {
                player.message("Syntax: !giftdp <IGN> <amount>");
            }
        } else if (sub[0].equals("giftsp")) {
            if (sub.length == 3) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    target.addStreamerPoints(Integer.parseInt(sub[2]));
                    target.message("You have received " + sub[2] + " streamer points from " + player.getName() + ".");

                    player.message(sub[1] + " has been gifted " + sub[2] + " streamer points.");
                } else if (c.addStreamerPointsOffline(sub[1], Integer.parseInt(sub[2]))) {
                    player.message(sub[1] + " has been gifted " + sub[2] + " streamer points.");
                } else {
                    player.message(sub[1] + " is not found.");
                }
            } else {
                player.message("Syntax: !giftsp <IGN> <amount>");
            }
        } else if (sub[0].equals("petitem")) {
            if (sub.length == 4) {
                int itemId = Integer.parseInt(sub[1]);
                int duration = Integer.parseInt(sub[2]);

                String time = sub[3];

                java.util.Calendar now = java.util.Calendar.getInstance();

                switch (time) {
                    case "second":
                    case "seconds":
                        now.add(java.util.Calendar.SECOND, duration);
                        break;

                    case "minute":
                    case "minutes":
                        now.add(java.util.Calendar.MINUTE, duration);
                        break;

                    case "hour":
                    case "hours":
                        now.add(java.util.Calendar.HOUR, duration);
                        break;

                    case "day":
                    case "days":
                        now.add(java.util.Calendar.DATE, duration);
                        break;
                }

                int petid = -1;
                if (constants.ItemConstants.isPet(itemId)) {
                    petid = MaplePet.createPet(itemId);
                }

                MapleInventoryManipulator.addById(c, itemId, (short) 1, null, petid, now.getTimeInMillis());

            } else {
                player.message("Syntax: !petitem <item ID> <duration> <seconds || minutes || hours || days>");
            }
        } else if (sub[0].equals("giftpetitem")) {
            if (sub.length == 5) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (victim != null) {

                    int itemId = Integer.parseInt(sub[2]);
                    int duration = Integer.parseInt(sub[3]);

                    String time = sub[4];

                    java.util.Calendar now = java.util.Calendar.getInstance();

                    switch (time) {
                        case "second":
                        case "seconds":
                            now.add(java.util.Calendar.SECOND, duration);
                            break;

                        case "minute":
                        case "minutes":
                            now.add(java.util.Calendar.MINUTE, duration);
                            break;

                        case "hour":
                        case "hours":
                            now.add(java.util.Calendar.HOUR, duration);
                            break;

                        case "day":
                        case "days":
                            now.add(java.util.Calendar.DATE, duration);
                            break;
                    }

                    int petid = -1;
                    if (constants.ItemConstants.isPet(itemId)) {
                        petid = MaplePet.createPet(itemId);
                    }

                    MapleInventoryManipulator.addById(victim.getClient(), itemId, (short) 1, null, petid, now.getTimeInMillis());
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !petitem <IGN> <item ID> <duration> <seconds || minutes || hours || days>");
            }
        } else if (sub[0].equals("fame")) {
            switch (sub.length) {
                case 2:
                    player.setFame(Integer.parseInt(sub[1]));
                    player.updateSingleStat(MapleStat.FAME, player.getFame());
                    player.message("Your fame has been set to " + sub[1] + ".");
                    break;
                case 3:
                    MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                    if (victim != null) {
                        victim.setFame(Integer.parseInt(sub[2]));
                        victim.updateSingleStat(MapleStat.FAME, victim.getFame());
                        victim.message(player.getName() + " has set your fame to " + sub[2] + ".");
                        player.message(sub[1] + "'s fame has been set to " + sub[2] + ".");
                    } else {
                        player.message(sub[1] + " is either offline or does not exist.");
                    }

                    break;
                default:
                    player.message("Syntax: !fame <amount> || !fame <IGN> <amount>");
            }
        } else if (sub[0].equals("giftnx")) {
            if (sub.length == 3) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    target.getCashShop().gainCash(1, Integer.parseInt(sub[2]));

                    player.message(sub[1] + " has been gifted " + sub[2] + " NX.");
                    target.message("You have received " + sub[2] + " NX from " + player.getName() + ".");
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !giftnx <IGN> <amount>");
            }
        } else if (sub[0].equals("giftfp")) {
            if (sub.length == 3) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    target.gainFishingPoint(Integer.parseInt(sub[2]));

                    player.message(sub[1] + " has been gifted " + sub[2] + " fishing point(s).");
                    target.message("You have received " + sub[2] + " fishing point(s) from " + player.getName() + ".");
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !giftfp <IGN> <amount>");
            }
        } else if (sub[0].equals("giftmp")) {
            if (sub.length == 3) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    target.gainMobPoints(Integer.parseInt(sub[2]));

                    player.message(sub[1] + " has been gifted " + sub[2] + " mob point(s).");
                    target.message("You have received " + sub[2] + " mob point(s) from " + player.getName() + ".");
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !giftfp <IGN> <amount>");
            }
        } else if (sub[0].equals("giftvp")) {
            if (sub.length == 3) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    target.getClient().addVotePoints(Integer.parseInt(sub[2]));
                    target.message("You have received " + sub[2] + " vote point(s) from " + player.getName() + ".");

                    player.message(sub[1] + " has been gifted " + sub[2] + " vote point(s).");
                } else if (c.addVotePointsOffline(sub[1], Integer.parseInt(sub[2]))) {
                    player.message(sub[1] + " has been gifted " + sub[2] + " vote point(s).");
                } else {
                    player.message(sub[1] + " is not found.");
                }
            } else {
                player.message("Syntax: !giftvp <IGN> <amount>");
            }
        } else if (sub[0].equals("giftgp")) {
            if (sub.length == 3) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    if (target.getGuild() != null) {
                        target.getGuild().gainGP(Integer.parseInt(sub[2]));

                        player.message(sub[1] + "'s guild has been gifted " + sub[2] + " guild points.");
                    } else {
                        player.message(sub[1] + " is not in a guild.");
                    }
                } else {
                    player.message(sub[1] + " is not online.");
                }
            } else {
                player.message("Syntax: !giftgp <IGN> <amount>");
            }
        } else if (sub[0].equals("setdps")) {
            if (sub.length == 3) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (victim != null) {
                    victim.setDPS(Integer.parseInt(sub[2]));

                    player.message(sub[1] + "'s DPS has been set to " + sub[2]);
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !setdps <IGN> <amount>");
            }
        } else {
            return false;
        }

        return true;
    }

    public static boolean executeGMCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        if (sub[0].equals("ap")) {
            switch (sub.length) {
                case 2:
                    player.setRemainingAp(Integer.parseInt(sub[1]));
                    player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                    player.message("Your remaining AP has been set to " + sub[1] + ".");
                    break;
                case 3:
                    MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
                    victim.setRemainingAp(Integer.parseInt(sub[2]));
                    victim.updateSingleStat(MapleStat.AVAILABLEAP, victim.getRemainingAp());
                    player.message(sub[1] + "'s remaining AP has been set to " + sub[2] + ".");
                    break;
                default:
                    player.message("Syntax: !ap <amount> || !ap <amount> <IGN>");
                    break;
            }
        } else if (sub[0].equals("buffme")) {
            final int[] array = {9001000, 9101002, 9101003, 9101008, 2001002, 1101007, 1005, 2301003, 5121009, 1111002, 4111001, 4111002, 4211003, 4211005, 1321000, 2321004, 3121002};
            for (int i : array) {
                SkillFactory.getSkill(i).getEffect(SkillFactory.getSkill(i).getMaxLevel()).applyTo(player);
            }
            player.setHpMp(30000);
        } else if (sub[0].equals("buffmap")) {
            final int[] array = {9001000, 9101002, 9101003, 9101008, 2001002, 1101007, 1005, 2301003, 5121009, 1111002, 4111001, 4111002, 4211003, 4211005, 1321000, 2321004, 3121002};
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                for (int i : array) {
                    SkillFactory.getSkill(i).getEffect(SkillFactory.getSkill(i).getMaxLevel()).applyTo(chr);
                }
                chr.setHpMp(30000);
            }
        } else if (sub[0].equals("restartnotice")) {
            if (sub.length == 2) {
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Notice] The server will be restarting in " + sub[1] + " minutes. It will only take one minute for the server to restart."));
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Notice] El servidor se reinicia en " + sub[1] + " minutos. Solo le tomara un minuto para que el servidor se reinicie."));
            } else {
                player.message("Syntax: !restartnotice <time in minutes>");
            }
        } else if (sub[0].equals("customspawn")) {
            if (sub.length == 4) {
                MapleMonster monster = MapleLifeFactory.getMonster(Integer.parseInt(sub[1]));
                if (monster == null) {
                    player.message("That mob does not exist.");
                    return true;
                } else {
                    Integer hp = Integer.parseInt(sub[2]);
                    Integer exp = Integer.parseInt(sub[3]);

                    MapleMonsterStats overrideStats = new MapleMonsterStats();
                    overrideStats.setHp(hp);
                    overrideStats.setExp(exp);

                    monster.setOverrideStats(overrideStats);
                    c.getPlayer().getMap().spawnMonsterOnGroudBelow(monster, c.getPlayer().getPosition());
                }
            } else {
                player.message("Syntax: !customspawn <mob ID> <hp> <exp>");
            }
        } else if (sub[0].equals("reloaddrops")) {
            MapleMonsterInformationProvider.getInstance().clearDrops();
            player.message("Reloaded Drops");
        } else if (sub[0].equals("reloadportals")) {
            PortalScriptManager.getInstance().reloadPortalScripts();
            player.message("Reloaded Portals");
        } else if (sub[0].equals("music")) {
            if (sub.length < 2) {
                player.message("Syntax: !music <song>");
                for (String s : songs) {
                    player.message(s);
                }
                return true;
            }
            String song = joinStringFrom(sub, 1);
            for (String s : songs) {
                if (s.equals(song)) {
                    player.getMap().broadcastMessage(MaplePacketCreator.musicChange(s));
                    player.message("Now playing song " + song + ".");
                    return true;
                }
            }
            player.message("Song not found, please enter a song below.");
            for (String s : songs) {
                player.message(s);
            }
        } else if (sub[0].equals("monitor")) {
            if (sub.length < 2) {
                player.message("Syntax: !monitor <IGN>");
                return true;
            }
            MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);
            if (victim == null) {
                player.message(sub[1] + " is either offline or does not exist.");
                return true;
            }
            boolean monitored = MapleLogger.monitored.contains(victim.getName());
            if (monitored) {
                MapleLogger.monitored.remove(victim.getName());
            } else {
                MapleLogger.monitored.add(victim.getName());
            }
            player.message(victim.getName() + " is " + (!monitored ? "now being monitored." : "no longer being monitored."));
            String message = player.getName() + (!monitored ? " has started monitoring " : " has stopped monitoring ") + victim.getName() + ".";
            Server.getInstance().broadcastGMMessage(MaplePacketCreator.serverNotice(5, message));
        } else if (sub[0].equals("monitors")) {
            for (String ign : MapleLogger.monitored) {
                player.message(ign + " is being monitored.");
            }
        } else if (sub[0].equals("watch")) {
            if (sub.length == 2) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (victim != null) {
                    if (player.gmLevel() < victim.gmLevel()) {
                        player.message("You cannot watch a GM.");
                    } else {
                        Boolean alreadyWatched = false;
                        int watchIndex = -1;

                        if (c.getWorldServer().getWatching().size() > 0) {
                            for (int i = 0; i < c.getWorldServer().getWatching().size(); i++) {
                                String watch = c.getWorldServer().getWatching().get(i);

                                if (watch.equals(String.valueOf(victim.getId()) + "-" + String.valueOf(player.getId()))) {
                                    alreadyWatched = true;
                                    watchIndex = i;
                                }
                            }
                        }

                        if (alreadyWatched) {
                            c.getWorldServer().getWatching().remove(watchIndex);

                            player.message("You are no longer watching " + sub[1] + ".");
                        } else {
                            c.getWorldServer().getWatching().add(victim.getId() + "-" + player.getId());

                            player.message("You are now watching " + sub[1] + ".");
                        }
                    }
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !watch <IGN>");
            }
        } else if (sub[0].equals("ignored")) {
            for (String ign : MapleLogger.ignored) {
                player.message(ign + " is being ignored.");
            }
        } else if (sub[0].equals("pos")) {
            float xpos = player.getPosition().x;
            float ypos = player.getPosition().y;
            float fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
            player.message("Position: (" + xpos + ", " + ypos + ")");
            player.message("Foothold ID: " + fh);
        } else if (sub[0].equals("closemerchant")) {
            if (sub.length == 2) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (victim == null) {
                    player.message(sub[1] + " is either offline or does not exist.");
                } else {
                    HiredMerchant hm = victim.getHiredMerchant();

                    if (hm == null) {
                        player.message(sub[1] + " does not have an active merchant.");
                    } else {
                        hm.forceClose();
                        Server.getInstance().getChannel(player.getWorld(), player.getClient().getChannel()).removeHiredMerchant(hm.getOwnerId());
                        player.message(sub[1] + "'s merchant has been closed.");
                    }
                }
            } else {
                player.message("Syntax: !closemerchant <IGN>");
            }
        } else if (sub[0].equals("speak")) {
            if (sub.length > 2) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (victim != null) {
                    String text = StringUtil.joinStringFrom(sub, 2);
                    victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), text, false, 0));
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !speak <IGN> <message>");
            }
        } else if (sub[0].equals("speakmap")) {
            if (sub.length > 1) {
                String text = StringUtil.joinStringFrom(sub, 1);
                for (MapleCharacter mch : player.getMap().getCharacters()) {
                    mch.getMap().broadcastMessage(MaplePacketCreator.getChatText(mch.getId(), text, false, 0));
                }
            } else {
                player.message("Syntax: !speakmap <message>");
            }
        } else if (sub[0].equals("gmshop")) {
            MapleShopFactory.getInstance().getShop(1337).sendShop(c);
        } else if (sub[0].equals("toggleexp")) {
            for (World world : Server.getInstance().getWorlds()) {
                if (world.getEnabledEXP()) {
                    world.setEnabledEXP(false);
                    player.message("EXP cards has been disabled.");
                } else {
                    world.setEnabledEXP(true);
                    player.message("EXP cards has been enabled.");
                }

                for (MapleCharacter chr : world.getPlayerStorage().getAllCharacters()) {
                    chr.setRates();
                }
            }
        } else if (sub[0].equals("toggledrop")) {
            for (World world : Server.getInstance().getWorlds()) {
                if (world.getEnabledDrop()) {
                    world.setEnabledDrop(false);
                    player.message("Drop cards has been disabled.");
                } else {
                    world.setEnabledDrop(true);
                    player.message("Drop cards has been enabled.");
                }
            }
        } else if (sub[0].equals("expeds")) {
            for (Channel ch : Server.getInstance().getChannelsFromWorld(0)) {
                if (ch.getExpeditions().isEmpty()) {
                    player.message("No expeditions in channel " + ch.getId());
                    continue;
                }
                player.message("Expeditions in channel " + ch.getId());
                int id = 0;
                for (MapleExpedition exped : ch.getExpeditions()) {
                    id++;
                    player.message("> Expedition " + id);
                    player.message(">> Type: " + exped.getType().toString());
                    player.message(">> Status: " + (exped.isRegistering() ? "REGISTERING" : "UNDERWAY"));
                    player.message(">> Size: " + exped.getMembers().size());
                    player.message(">> Leader: " + exped.getLeader().getName());
                    int memId = 2;
                    for (MapleCharacter member : exped.getMembers()) {
                        if (exped.isLeader(member)) {
                            continue;
                        }
                        player.message(">>> Member " + memId + ": " + member.getName());
                        memId++;
                    }
                }
            }
        } else if (sub[0].equals("kill")) {
            if (sub.length == 2) {
                MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (victim == null) {
                    player.message(sub[1] + " is either offline or does not exist.");
                } else {

                    victim.setHpMp(0);
                    victim.message("You have been killed by " + player.getName() + ".");
                }
            } else {
                player.message("Syntax: !kill <IGN>");
            }
        } else if (sub[0].equals("killmap")) {
            for (MapleCharacter target : player.getMap().getCharacters()) {
                target.setHpMp(0);
                target.message("You have been killed by " + player.getName() + ".");
            }
        } else if (sub[0].equals("seed")) {
            if (player.getMapId() != 910010000) {
                player.message("This command can only be used in HPQ.");
                return true;
            }
            Point pos[] = {new Point(7, -207), new Point(179, -447), new Point(-3, -687), new Point(-357, -687), new Point(-538, -447), new Point(-359, -207)};
            int seed[] = {4001097, 4001096, 4001095, 4001100, 4001099, 4001098};
            for (int i = 0; i < pos.length; i++) {
                Item item = new Item(seed[i], (byte) 0, (short) 1);
                player.getMap().spawnItemDrop(player, player, item, pos[i], false, true);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        } else if (sub[0].equals("reloadmapspawn")) {
            MapleMap map = c.getPlayer().getMap();
            map.respawn();
        } else if (sub[0].equals("popupnotice")) {
            if (sub.length < 2) {
                player.message("Syntax: !popupnotice <message>");
            } else {
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(1, joinStringFrom(sub, 1)));
            }
        } else if (sub[0].equals("sendpopup")) {
            if (sub.length < 3) {
                player.message("Syntax: !sendpopup <IGN> <message>");
            } else {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target == null) {
                    player.message(sub[1] + " is either offline or does not exist.");
                } else {
                    target.dropMessage(1, joinStringFrom(sub, 2));
                    player.message("Popup message has been sent to " + sub[1] + ".");
                }
            }
        } else if (sub[0].equals("monsterdebug")) {
            List<MapleMapObject> monsters = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                player.message("Monster ID: " + monster.getId());
            }
        } else if (sub[0].equals("unbug")) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.enableActions());
            player.message("The map has been unbugged.");
        } else if (sub[0].equals("dropexpiration")) {
            if (sub.length == 4) {
                int itemId = Integer.parseInt(sub[1]);
                int duration = Integer.parseInt(sub[2]);
                short quantity = 1;
                String time = sub[3];

                Item toDrop;
                if (MapleItemInformationProvider.getInstance().getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    toDrop = MapleItemInformationProvider.getInstance().getEquipById(itemId);
                } else {
                    toDrop = new Item(itemId, (short) 0, quantity);
                }

                java.util.Calendar now = java.util.Calendar.getInstance();

                switch (time) {
                    case "second":
                    case "seconds":
                        now.add(java.util.Calendar.SECOND, duration);

                        toDrop.setExpiration(now.getTimeInMillis());
                        break;

                    case "minute":
                    case "minutes":
                        now.add(java.util.Calendar.MINUTE, duration);

                        toDrop.setExpiration(now.getTimeInMillis());
                        break;

                    case "hour":
                    case "hours":
                        now.add(java.util.Calendar.HOUR, duration);

                        toDrop.setExpiration(now.getTimeInMillis());
                        break;

                    case "day":
                    case "days":
                        now.add(java.util.Calendar.DATE, duration);

                        toDrop.setExpiration(now.getTimeInMillis());
                        break;
                }

                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);

            } else {
                player.message("Syntax: !dropexpiration <item ID> <duration> <seconds || minutes || hours || days>");
            }
        } else if (sub[0].equals("level")) {
            switch (sub.length) {
                case 2:
                    player.setLevel(Integer.parseInt(sub[1]) - 1);
                    player.gainExp(-player.getExp(), false, false);
                    player.levelUp(false);

                    player.message("Your level has been set to " + sub[1] + ".");
                    break;
                case 3:
                    MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                    if (target != null) {
                        target.setLevel(Integer.parseInt(sub[2]) - 1);
                        target.gainExp(-target.getExp(), false, false);
                        target.levelUp(false);

                        player.message(sub[1] + "'s level has been set to " + sub[2] + ".");
                        target.message(player.getName() + " has set your level to " + sub[2] + ".");
                    } else {
                        player.message(sub[1] + " is either offline or does not exist.");
                    }
                    break;
                default:
                    player.message("Syntax: !level <number> || !level <IGN> <number>");
            }
        } else if (sub[0].equals("zakum")) {
            player.getMap().spawnFakeMonsterOnGroundBelow(MapleLifeFactory.getMonster(8800000), player.getPosition());
            for (int x = 8800003; x < 8800011; x++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(x), player.getPosition());
            }
        } else if (sub[0].equals("horntail")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8810026), player.getPosition());
        } else if (sub[0].equals("levelup")) {
            if (sub.length == 2) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    target.gainExp(-target.getExp(), false, false);
                    target.levelUp(true);

                    player.message(sub[1] + " has leveled up.");
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else if (sub.length == 3) {
                MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                if (target != null) {
                    for (int i = 0; i < Integer.parseInt(sub[2]); i++) {
                        target.gainExp(-target.getExp(), false, false);
                        target.levelUp(true);
                    }

                    player.message(sub[1] + " has leveled up " + sub[2] + " times.");
                } else {
                    player.message(sub[1] + " is either offline or does not exist.");
                }
            } else {
                player.message("Syntax: !levelup <IGN> || !levelup <IGN> <# of times>");
            }
        } else if (sub[0].equals("mesos")) {
            switch (sub.length) {
                case 2:
                    player.gainMeso(Integer.parseInt(sub[1]), true);
                    player.message("You have gained " + sub[1] + " mesos.");
                    break;
                case 3:
                    MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);

                    if (target != null) {
                        target.gainMeso(Integer.parseInt(sub[2]), true);
                        player.message(sub[1] + " has been gifted " + sub[2] + " mesos.");
                        target.message("You have received " + sub[2] + " mesos from " + player.getName() + ".");
                    } else {
                        player.message(sub[1] + " is either offline or does not exist.");
                    }
                    break;
                default:
                    player.message("Syntax: !mesos <amount> || !mesos <IGN> <amount>");
            }
        } else if (sub[0].equals("notice")) {
            if (sub.length < 2) {
                player.message("Syntax: !notice <message>");
            } else {
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "[Notice] " + joinStringFrom(sub, 1)));
            }
        } else if (sub[0].equals("openportal")) {
            player.getMap().getPortal(sub[1]).setPortalState(true);
            player.message("The portal has been opened");
        } else if (sub[0].equals("pe")) {
            String packet;
            try {
                InputStreamReader is = new FileReader("pe.txt");
                Properties packetProps = new Properties();
                packetProps.load(is);
                is.close();
                packet = packetProps.getProperty("pe");
            } catch (IOException ex) {
                player.message("Failed to load pe.txt");
                return true;
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.write(HexTool.getByteArrayFromHexString(packet));
            SeekableLittleEndianAccessor slea = new GenericSeekableLittleEndianAccessor(new ByteArrayByteStream(mplew.getPacket()));
            short packetId = slea.readShort();
            final MaplePacketHandler packetHandler = PacketProcessor.getProcessor(0, c.getChannel()).getHandler(packetId);
            if (packetHandler != null && packetHandler.validateState(c)) {
                try {
                    player.message("Recieving: " + packet);
                    packetHandler.handlePacket(slea, c);
                } catch (final Throwable t) {
                    FilePrinter.printError(FilePrinter.PACKET_HANDLER + packetHandler.getClass().getName() + ".txt", t, "Error for " + (c.getPlayer() == null ? "" : "player ; " + c.getPlayer() + " on map ; " + c.getPlayer().getMapId() + " - ") + "account ; " + c.getAccountName() + "\r\n" + slea.toString());
                    return true;
                }
            }
        } else if (sub[0].equals("closeportal")) {
            player.getMap().getPortal(sub[1]).setPortalState(false);
            player.message("The portal has been closed");
        } else if (sub[0].equals("proitem")) {
            if (sub.length == 3) {
                int itemId = Integer.parseInt(sub[1]);
                short stats = Short.parseShort(sub[2]);

                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                Item item = ii.getEquipById(itemId);
                item.setOwner(player.getName());

                MapleInventoryManipulator.addByIdProItem(c, ii.hardcoreItem((Equip) item, stats), (short) 1);
            } else {
                player.message("Syntax: !proitem <item ID> <stats>");
            }
        } else if (sub[0].equals("unban")) {
            if (MapleCharacter.unban(sub[1])) {
                player.message(sub[1] + " has been unbanned.");
            } else {
                player.message("Failed to unban " + sub[1]);
            }
        } else if (sub[0].equalsIgnoreCase("night")) {
            player.getMap().broadcastNightEffect();
            player.message("Night effect has been activated.");
        } else {
            return false;
        }

        return true;
    }

    public static void executeAdminCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        switch (sub[0]) {
            case "sp":  //Changed to support giving sp /a
                switch (sub.length) {
                    case 2:
                        player.setRemainingSp(Integer.parseInt(sub[1]));
                        player.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());

                        player.message("Your skill points has been set to " + sub[1]);
                        break;
                    case 3:
                        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
                        victim.setRemainingSp(Integer.parseInt(sub[2]));
                        victim.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());

                        player.message(sub[1] + "'s skill points has been set to " + sub[2]);
                        break;
                    default:
                        player.message("Syntax: !sp <amount> || !sp <IGN> <amount>");
                        break;
                }
                break;
            case "pmob":
                if (sub.length != 3) {
                    player.message("Syntax: !pmob <mob ID> <spawn time>");
                    break;
                }

                int npcId = Integer.parseInt(sub[1]);
                int mobTime = Integer.parseInt(sub[2]);
                if (sub[2] == null) {
                    mobTime = 1;
                }
                int xpos = player.getPosition().x;
                int ypos = player.getPosition().y;
                int fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();

                MapleMonster mob = MapleLifeFactory.getMonster(npcId);
                if (mob != null && !mob.getName().equals("MISSINGNO")) {
                    mob.setPosition(player.getPosition());
                    mob.setCy(ypos);
                    mob.setRx0(xpos + 50);
                    mob.setRx1(xpos - 50);
                    mob.setFh(fh);
                    try {
                        Connection con = DatabaseConnection.getConnection();
                        PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid, mobtime ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                        ps.setInt(1, npcId);
                        ps.setInt(2, 0);
                        ps.setInt(3, fh);
                        ps.setInt(4, ypos);
                        ps.setInt(5, xpos + 50);
                        ps.setInt(6, xpos - 50);
                        ps.setString(7, "m");
                        ps.setInt(8, xpos);
                        ps.setInt(9, ypos);
                        ps.setInt(10, player.getMapId());
                        ps.setInt(11, mobTime);
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        player.dropMessage("Failed to save MOB to the database.");
                    }
//                    final Point position = mob.getPosition();
//                    final boolean mobile = mob.isMobile();
                    player.getMap().addMonsterSpawn(mob, mobTime, fh);
                } else {
                    player.dropMessage("That mob does not exist.");
                }

                break;
            case "pnpc":
                if (sub.length != 2) {
                    player.message("Syntax: !pnpc <NPC ID>");
                    break;
                }

                npcId = Integer.parseInt(sub[1]);
                MapleNPC npc = MapleLifeFactory.getNPC(npcId);
                xpos = player.getPosition().x;
                ypos = player.getPosition().y;
                fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
                if (npc != null && !npc.getName().equals("MISSINGNO")) {
                    npc.setPosition(player.getPosition());
                    npc.setCy(ypos);
                    npc.setRx0(xpos + 50);
                    npc.setRx1(xpos - 50);
                    npc.setFh(fh);
                    try {
                        Connection con = DatabaseConnection.getConnection();
                        PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                        ps.setInt(1, npcId);
                        ps.setInt(2, 0);
                        ps.setInt(3, fh);
                        ps.setInt(4, ypos);
                        ps.setInt(4, ypos);
                        ps.setInt(5, xpos + 50);
                        ps.setInt(6, xpos - 50);
                        ps.setString(7, "n");
                        ps.setInt(8, xpos);
                        ps.setInt(9, ypos);
                        ps.setInt(10, player.getMapId());
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        player.dropMessage("Failed to save NPC to the database.");
                    }
                    player.getMap().addMapObject(npc);
                    player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
                } else {
                    player.dropMessage("That NPC does not exist.");
                }

                break;
            case "reloadevents":
                for (Channel ch : Server.getInstance().getAllChannels()) {
                    ch.reloadEventScriptManager();
                }
                player.message("Reloaded Events");
                break;
            case "packet":
                player.getMap().broadcastMessage(MaplePacketCreator.customPacket(joinStringFrom(sub, 1)));
                break;
            case "servermessage":
                if (sub.length > 1) {
                    c.getWorldServer().setServerMessage(joinStringFrom(sub, 1));
                    player.message("Server message has been set.");
                } else {
                    player.message("Syntax: !servermessage <message>");
                }
                break;
            case "timerdebug":
                TimerManager tMan = TimerManager.getInstance();
                player.message("Total Task: " + tMan.getTaskCount() + " Current Task: " + tMan.getQueuedTasks() + " Active Task: " + tMan.getActiveCount() + " Completed Task: " + tMan.getCompletedTaskCount());
                break;
            case "warpworld":
                Server server = Server.getInstance();
                byte worldb = Byte.parseByte(sub[1]);
                if (worldb <= (server.getWorlds().size() - 1)) {
                    try {
                        String[] socket = server.getIP(worldb, c.getChannel()).split(":");
                        c.getWorldServer().removePlayer(player);
                        player.getMap().removePlayer(player);//LOL FORGOT THIS ><
                        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
                        player.setWorld(worldb);
                        player.saveToDB();//To set the new world :O (true because else 2 player instances are created, one in both worlds)
                        c.announce(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1])));
                    } catch (UnknownHostException | NumberFormatException ex) {
                        player.message("Error when trying to change worlds, are you sure the world you are trying to warp to has the same amount of channels?");
                    }

                } else {
                    player.message("Invalid world; highest number available: " + (server.getWorlds().size() - 1));
                }
                break;
            case "saveall":
                c.getWorldServer().getPlayerStorage().saveAll();

                player.message("All players have been saved.");
                break;
            case "saveall2":
                // Whoever coded this is stupid. - Torban
                for (MapleCharacter chr : c.getWorldServer().getPlayerStorage().getAllCharacters()) {
                    chr.saveToDB();
                }

                player.message("All players have been saved.");
                break;
            case "dcall":
                c.getWorldServer().getPlayerStorage().disconnectAll();
                player.message("All players has been disconnected.");
                break;
            case "mapplayers"://fyi this one is even stupider
                //Adding HP to it, making it less useless.
                String names = "";
                int map = player.getMapId();
                for (World world : Server.getInstance().getWorlds()) {
                    for (MapleCharacter chr : world.getPlayerStorage().getAllCharacters()) {
                        int curMap = chr.getMapId();
                        String hp = Integer.toString(chr.getHp());
                        String maxhp = Integer.toString(chr.getMaxHp());
                        String name = chr.getName() + ": " + hp + "/" + maxhp;
                        if (map == curMap) {
                            names = names.equals("") ? name : (names + ", " + name);
                        }
                    }
                }
                player.message(names);
                break;
            case "npc":
                if (sub.length < 1) {
                    break;
                }
                npc = MapleLifeFactory.getNPC(Integer.parseInt(sub[1]));
                if (npc != null) {
                    npc.setPosition(player.getPosition());
                    npc.setCy(player.getPosition().y);
                    npc.setRx0(player.getPosition().x + 50);
                    npc.setRx1(player.getPosition().x - 50);
                    npc.setFh(player.getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
                    player.getMap().addMapObject(npc);
                    player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
                }
                break;
            case "exprate":
                if (sub.length == 2) {
                    c.getWorldServer().setExpRate(Integer.parseInt(sub[1]));
                    player.message("The EXP rate has been set to " + sub[1] + "x.");
                } else {
                    player.message("Syntax: !exprate <rate>");
                }
                break;
            case "droprate":
                if (sub.length == 2) {
                    c.getWorldServer().setDropRate(Integer.parseInt(sub[1]));
                    player.message("The drop rate has been set to " + sub[1] + "x.");
                } else {
                    player.message("Syntax: !droprate <rate>");
                }
                break;
            case "questrate":
                if (sub.length == 2) {
                    c.getWorldServer().setQuestRate(Integer.parseInt(sub[1]));
                    player.message("The quest rate has been set to " + sub[1] + "x.");
                } else {
                    player.message("Syntax: !questrate <rate>");
                }
                break;
            case "mesorate":
                if (sub.length == 2) {
                    c.getWorldServer().setMesoRate(Integer.parseInt(sub[1]));
                    player.message("The meso rate has been set to " + sub[1] + "x.");
                } else {
                    player.message("Syntax: !mesorate <rate>");
                }
                break;
            case "playernpc":
                if (sub.length == 3) {
                    player.playerNPC(c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]), Integer.parseInt(sub[2]));
                } else {
                    player.message("Syntax: !playernpc <IGN> <script ID>");
                }
                break;
            case "shutdown":
            case "shutdownnow":
                int time = 60000;
                if (sub[0].equals("shutdownnow")) {
                    time = 1;

                } else if (sub.length > 1) {
                    time *= Integer.parseInt(sub[1]);
                }
                TimerManager.getInstance().schedule(Server.getInstance().shutdown(false), time);
                break;
            case "shutdown2":
            case "shutdownnow2":
                time = 60000;
                if (sub[0].equals("shutdownnow2")) {
                    time = 1;

                } else if (sub.length > 1) {
                    time *= Integer.parseInt(sub[1]);
                }
                TimerManager.getInstance().schedule(Server.getInstance().shutdown2(false), time);
                break;
            case "face":
                switch (sub.length) {
                    case 2:
                        player.setFace(Integer.parseInt(sub[1]));
                        player.equipChanged();

                        player.message("You face has been changed to " + sub[1]);

                    case 3:
                        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
                        victim.setFace(Integer.parseInt(sub[2]));
                        victim.equipChanged();

                        player.message(sub[1] + "'s face has been changed to " + sub[2]);

                    default:
                        player.message("Syntax: !face <face ID> || !face <IGN> <face ID>");
                }

                break;
            case "hair":
                switch (sub.length) {
                    case 2:
                        player.setHair(Integer.parseInt(sub[1]));
                        player.equipChanged();

                        player.message("You hair has been changed to " + sub[1]);

                    case 3:
                        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
                        victim.setHair(Integer.parseInt(sub[2]));
                        victim.equipChanged();

                        player.message(sub[1] + "'s hair has been changed to " + sub[2]);

                    default:
                        player.message("Syntax: !hair <hair ID> || !hair <IGN> <hair ID>");
                }
                break;
            case "clearquestcache":
                MapleQuest.clearCache();
                player.message("Quest cache has been cleared.");
                break;
            case "clearquest":
                if (sub.length < 1) {
                    player.message("Syntax: !clearquest <quest ID>");
                    return;
                }
                MapleQuest.clearCache(Integer.parseInt(sub[1]));
                player.message("Quest Cache for quest " + sub[1] + " cleared.");
                break;
            default:
                player.message("The command " + heading + sub[0] + " does not exist.");
                break;
        }
    }

    private static String joinStringFrom(String arr[], int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < arr.length; i++) {
            builder.append(arr[i]);
            if (i != arr.length - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    private static long gcm(long a, long b) {
        return b == 0 ? a : gcm(b, a % b); // Not bad for one line of code :)
    }

    private static String asFraction(long a, long b) {
        long gcm = gcm(a, b);
        return (a / gcm) + "/" + (b / gcm);
    }
}
