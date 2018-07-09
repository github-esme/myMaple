// Music Changer

var status;

var songs = new Array(
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
        "BgmUI/ShopBgm"
    );

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
        var outStr = "You can funk up this server by changing the music in the map or for everyone! What would you like to do?\r\n\r\n";
        outStr += "#e#b#L0#Change the music in the map for 100,000 mesos#l\r\n";
        outStr += "#L1#Change the global music for everyone in all channels for 500,000 mesos#l#k#n";

        cm.sendSimple(outStr);

    }
    else if (status == 1)
    {
        if (selection == 0)
        {
            if (cm.getMeso() >= 100000)
            {
                var outStr = "Please choose a song below:\r\n\r\n";

                for (var i = 0; i < songs.length; i++)
                {
                    outStr += "#e#L" + i + "#" + songs[i] + "#n#l\r\n";
                }

                cm.sendSimple(outStr);
            }
            else
            {
                cm.sendOk("You do not have 100,000 mesos!");
                cm.dispose();
            }
        }
        else if (selection == 1)
        {
            if (cm.getMeso() >= 500000)
            {
                var outStr = "Please choose a song below:\r\n\r\n";

                for (var i = 0; i < songs.length; i++)
                {
                    outStr += "#e#L" + i + "#" + songs[i] + "#n#l\r\n";
                }

                status = 2;

                cm.sendSimple(outStr);
            }
            else
            {
                cm.sendOk("You do not have 500,000 mesos!");
                cm.dispose();
            }
        }
    }
    else if (status == 2)
    {
        cm.gainMeso(-100000);
        cm.getPlayer().getMap().broadcastMessage(Packages.tools.MaplePacketCreator.musicChange(songs[selection]));
        cm.getPlayer().getMap().broadcastMessage(Packages.tools.MaplePacketCreator.serverNotice(6, "[Map Music] " + cm.getPlayer().getName() + " selected to play the music " + songs[selection] + " in this map!"));
        cm.sendOk("The music " + songs[selection] + " is now playing in the FM.");
        cm.dispose();
    }
    else if (status == 3)
    {
        cm.gainMeso(-500000);
        cm.changeMusicEveryone(songs[selection], cm.getPlayer());
        Packages.net.server.Server.getInstance().broadcastMessage(Packages.tools.MaplePacketCreator.serverNotice(6, "[Global Music] " + cm.getPlayer().getName() + " selected to play the music " + songs[selection] + " for everyone!"));
        cm.sendOk("The music " + songs[selection] + " is now playing for everyone.");
        cm.dispose();
    }
}