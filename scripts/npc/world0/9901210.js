// Mob Points Exchanger

var status; 
var mobpoints;
var points;
var target;

var donorhats = new Array(1003387, 1004107, 1004057, 1002842, 1002843, 1004162, 1001098,1001103,1000031,1000032,1000035,1000036,1000037,1000038,1004024,1000041,1000043,1000044,1000045,1000047,1000050,1000058,1000060,1000061,1000062,1000069,1000070,1000071,1000072,1000074,1001010,1001011,1001036,1001037,1001061,1001066,1001069,1001068,1001070,1001072,1001076,1001084,1001088,1001089,1001090,1001091,1002671,1002998,1002999,1003000,1003001,1003005,1003015,1003070,1003109,1003131,1003132,1003133,1003148,1003149,1003170,1003186,1003187,1003207,1003204,1003208,1003210,1003211,1003216,1003219,1003221,1003222,1003237,1003247,1003268,1003269,1003272,1003279,1003390,1003399,1003401,1003402,1003404,1003417,1003459,1003510,1003533,1003548,1003568,1003575,1003581,1003586,1003587,1003588,1003596,1003597,1003680,1003746,1003747,1003748,1003750,1003753,1003759,1003760,1003763,1003769,1003802,1003803,1003807,1003808,1003809,1003829,1003830,1003846,1003847,1003861,1003862,1003884,1003892,1003901,1003902,1003903,1003904,1003906,1003909,1003910,1003912,1003919,1003934,1003963,1003980,1003981,1004005,1004025,1004026,1004027,1004028,1004029,1004033,1004036,1004038,1004040,1004043,1004045,1004050,1004051,1004052,1004072,1004092,1004094,1004095,1004106,1004108,1004112,1004114,1004117,1004126,1004137,1004139,1004140,1004141,1004142,1004143,1004144,1004145,1004146,1004147,1004148,1004156,1004157,1004158,1004167,1004193,1004198,1004200);
var donorshirts = new Array(1051298, 1040001,1040005,1040036,1040077,1040078,1040123,1040126,1040133,1040135,1040138,1040192,1041073,1040193,1041046,1041135,1041136,1041137,1041138,1041194,1041195,1042049,1042050,1042067,1042082,1042092,1042104,1042105,1042106,1042108,1042130,1042132,1042134,1042141,1042142,1042143,1042144,1042146,1042149,1042147,1042150,1042152,1042154,1042155,1042156,1042157,1042158,1042159,1042161,1042162,1042163,1042164,1042165,1042166,1042168,1042169,1042170,1042171,1042172,1042174,1042176,1042177,1042178,1042181,1042182,1042183,1042184,1042185,1042189,1042190,1042193,1042195,1042196,1042197,1042199,1042200,1042201,1042202,1042203,1042204,1042206,1042207,1042208,1042209,1042210,1042212,1042213,1042214,1042215,1042216,1042217,1042218,1042219,1042220,1042222,1042228,1042229,1042230,1042232,1042235,1042236,1042237,1042238,1042240,1042241,1042242,1042250,1042251,1042252,1042260,1042263,1042265,1042267,1042270,1042271,1042276,1042277,1042278,1042279,1042280,1042281,1042282,1042285,1042287,1042288,1042289,1042290,1042292,1042293,1042311,1042312,1042313,1042314,1042315,1042316,1042320,1042321,1048001,1048002);
var donorshorts = new Array(1060003,1060054,1060055,1060067,1060108,1060113,1060114,1060117,1060120,1060121,1060126,1060138,1060142,1060179,1060180,1060181,1060182,1060183,1061005,1061039,1061107,1061127,1061128,1061129,1061137,1061130,1061131,1061133,1061138,1061140,1061141,1061142,1061143,1061148,1061170,1061203,1061205,1061206,1061207,1061208,1061209,1062046,1062067,1062068,1062070,1062071,1062072,1062076,1062077,1062081,1062083,1062084,1062085,1062089,1062091,1062093,1062094,1062095,1062096,1062097,1062098,1062100,1062101,1062103,1062104,1062105,1062106,1062107,1062108,1062109,1062110,1062111,1062112,1062113,1062114,1062116,1062117,1062118,1062119,1062121,1062122,1062123,1062124,1062126,1062131,1062133,1062134,1062135,1062136,1062137,1062138,1062139,1062151,1062152,1062153,1062155,1062157,1062175,1062183,1062184,1062189,1062203,1062204,1062208,1062209,1062210,1062211);
var donoroveralls = new Array(1051373, 1052617,1052574,1052575,1052587,1052092,1052067,1051131,1052408,1050121,1050122,1050126,1052975,1050129,1052671,1050140,1050141,1050142,1050143,1050146,1050148,1050147,1050151,1050152,1050154,1050153,1050157,1050161,1050168,1050170,1050177,1050179,1050188,1050190,1050207,1050215,1050221,1050220,1050222,1050226,1050227,1050230,1050234,1050235,1050242,1050243,1050244,1050246,1050248,1050255,1050284,1050285,1050287,1050289,1050291,1050293,1050296,1050298,1050301,1050302,1050303,1050304,1050305,1050311,1050312,1050314,1050329,1050330,1050331,1050332,1050333,1050334,1050324,1050325,1050326,1050327,1051048,1051070,1051075,1051076,1051132,1051133,1051134,1051135,1051137,1051139,1051150,1051151,1051152,1051153,1051158,1051159,1051162,1051163,1051173,1051174,1051175,1051176,1051183,1051185,1051188,1051189,1051190,1051192,1051194,1051195,1051198,1051211,1051212,1051218,1051219,1051228,1051229,1051234,1051235,1051224,1051225,1051256,1051261,1051262,1051264,1051265,1051270,1051271,1051272,1051277,1051278,1051280,1051282,1051284,1051291,1051292,1051294,1051296,1051303,1051332,1051345,1051352,1051354,1051355,1051366,1051367,1051369,1051371,1051372,1051374,1051375,1051376,1051382,1051387,1052245,1052246,1052248,1052255,1052256,1052275,1052293,1052294,1052295,1052296,1052306,1052324,1052329,1052372,1052421,1052423,1052440,1052446,1052447,1052458,1052506);
var donorshoes = new Array(1072507, 1072831, 1070002,1070004,1070006,1070015,1070016,1070020,1070026,1070027,1070028,1070057,1070059,1070061,1071000,1071001,1071013,1071014,1071026,1071030,1071031,1071037,1071044,1071074,1072279,1072280,1072282,1072283,1072322,1072330,1072331,1072333,1072334,1072336,1072341,1072349,1072367,1072371,1072393,1072394,1072395,1072406,1072407,1072426,1072437,1072440,1072482,1072484,1072514,1072517,1072529,1072530,1072531,1072637,1072650,1072651,1072779,1072780,1072791,1072803,1072808,1072820,1072823,1072829,1072857,1072858,1072860,1072866,1072867,1072876,1072917,1072918,1072919,1072941,1072942);
var donorgloves = new Array(1081000,1081001,1081003,1081004,1081006,1082165,1082227,1082233,1082247,1082250,1082263,1082267,1082421,1082422,1082448,1082493,1082495,1082502,1082520,1082557,1082542,1082549,1082550,1082551,1082552,1082563,1082587,1082588);
var donorcapes = new Array(1102380, 1102095, 1102142, 1102148,1102152,1102184,1102186,1102187,1102188,1102094,1102096,1102097,1102107,1102208,1102209,1102210,1102213,1102214,1102215,1102216,1102218,1102222,1102223,1102229,1102232,1102236,1102240,1102244,1102251,1102252,1102253,1102257,1102258,1102271,1102273,1102274,1102301,1102310,1102318,1102325,1102326,1102334,1102349,1102353,1102358,1102373,1102376,1102377,1102378,1102385,1102386,1102387,1102388,1102389,1102390,1102392,1102395,1102396,1102420,1102450,1102451,1102452,1102453,1102466,1102487,1102496,1102509,1102510,1102532,1102546,1102547,1102548,1102549,1102554,1102629,1102616,1102617,1102632,1102642,1102644,1102668,1102705,1102707,1102708,1102723,1102724,1102729,1102730,1102755,1102756,1102758,1102809,1103010,1103001,1103000);
var donorweapons = new Array(1702120, 1702120, 1092067,1702435,1702453,1702526,1702574,1702233,1702236,1702237,1702238,1702224,1702252,1702263,1702265,1702266,1702267,1702274,1702275,1702276,1702277,1702278,1702279,1702280,1702281,1702282,1702286,1702287,1702288,1702289,1702291,1702293,1702298,1702299,1702301,1702306,1702307,1702308,1702309,1702310,1702348,1702349,1702350,1702351,1702352,1702357,1702366,1702367,1702368,1702369,1702371,1702372,1702373,1702374,1702375,1702376,1702377,1702378,1702379,1702380,1702381,1702382,1702423,1702424,1702433,1702440);

var chairs = new Array(3010000, 3010001, 3010002, 3010003, 3010004, 3010005, 3010006, 3010007, 3010008, 3010009, 3010010, 3010011, 3010012, 3010013, 3010014, 3010015, 3010016, 3010017, 3010018, 3010019, 3010022, 3010023, 3010024, 3010025, 3010026, 3010028, 3011000, 3010040, 3010041, 3010045, 3012005, 3010046, 3010047, 3010072, 3010057, 3010058, 3010060, 3010061, 3010062, 3010063, 3010064, 3010065, 3010066, 3010067, 3010043, 3010071, 3010085, 3010098, 3010099, 3010073, 3010101, 3010106, 3010111, 3010080, 3010081, 3010082, 3010083, 3010084, 3010092, 3010116, 3010069, 3012010, 3012011);

var tao = new Array(4032015, 4032016, 4032017);

function start() 
{
    mobpoints = cm.getPlayer().getMobPoints();

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
        var outStr = "Hello, you can exchange your mob points with me! You get a certain amount of mob points each time you kill a mob.\r\n\r\n";
        outStr += "#eYou currently have #r" + mobpoints + "#k mob points.#n\r\n\r\n";

        outStr += "#ePlease choose a category:\r\n";
        outStr += "#b#L0#Transportation#l\r\n";
        outStr += "#L1#Boosters#l\r\n";
        //outStr += "#L2#Comestics#l\r\n";
        outStr += "#L3#Items#l\r\n";        
        outStr += "#L4#Inventory Slots#l\r\n\r\n";
        outStr += "#L5#How Mob Points Are Calculated#l\r\n";      
        outStr += "#L6#Gift Mob Point(s) to Another Player#l#n#k";  

        cm.sendSimple(outStr);
    } 
    else if (status == 1) 
    { 
        if(selection == 0)
        {
            var outStr = "#e#L0#Exchange 100 mob points for #d30 #v2030000# #t2030000##k#l\r\n";
            outStr += "#L1#Exchange 100 mob points for #d10 #v2030001# #t2030001##k#l\r\n";
            outStr += "#L2#Exchange 100 mob points for #d10 #v2030002# #t2030002##k#l\r\n";
            outStr += "#L3#Exchange 100 mob points for #d10 #v2030003# #t2030003##k#l\r\n";
            outStr += "#L4#Exchange 100 mob points for #d10 #v2030004# #t2030004##k#l\r\n";
            outStr += "#L5#Exchange 100 mob points for #d10 #v2030005# #t2030005##k#l\r\n";
            outStr += "#L6#Exchange 100 mob points for #d10 #v2030006# #t2030006##k#l\r\n";
            outStr += "#L7#Exchange 100 mob points for #d10 #v2030007# #t2030007##k#l\r\n";
            outStr += "#L8#Exchange 750 mob points for #d6 #v5041000# #t5041000##k#l#n\r\n";

            cm.sendSimple(outStr);
        }
        else if(selection == 1)
        {
            var outStr = "#e#L0#Exchange 500 mob points for #dparty #s9001000# Haste#k#l\r\n";
            outStr += "#L1#Exchange 500 mob points for #d1 #v2022273# #t2022273##k#l\r\n";
            outStr += "#L2#Exchange 1,000 mob points for #d1 #v2012008# #t2012008##k#l\r\n";
            outStr += "#L3#Exchange 1,500 mob points for #d1 #v2022179# #t2022179##k#l\r\n";
            outStr += "#L4#Exchange 2,000 mob points for #d30 minutes #v5211048# 2X EXP card#k#l\r\n";
            outStr += "#L5#Exchange 3,000 mob points for #d1 hour #v5211048# 2X EXP card#k#l#n\r\n";

            status = 2;

            cm.sendSimple(outStr);
        }
        else if (selection == 2)
        {
            var outStr = "#e#L0#Exchange 300 mob points for #d1 random chair#k#l\r\n";
            outStr += "#L1#Exchange 500 mob points for a #drandom rare cosmetic hat#k#l\r\n";
            outStr += "#L2#Exchange 500 mob points for a #drandom rare cosmetic shirt#k#l\r\n";
            outStr += "#L3#Exchange 500 mob points for a #drandom rare cosmetic shorts#k#l\r\n";
            outStr += "#L4#Exchange 500 mob points for a #drandom rare cosmetic overall#k#l\r\n";
            outStr += "#L5#Exchange 500 mob points for a #drandom rare cosmetic shoe#k#l\r\n";
            outStr += "#L6#Exchange 500 mob points for a #drandom rare cosmetic glove#k#l\r\n";
            outStr += "#L7#Exchange 500 mob points for a #drandom rare cosmetic weapon#k#l\r\n";
            outStr += "#L8#Exchange 1,000 mob points for a #drandom rare cosmetic cape#k#l#n\r\n";

            status = 3;

            cm.sendSimple(outStr);
        }
        else if (selection == 3)
        {
            var outStr = "#e#L0#Exchange 250 mob points for #d10 #v5076000# #t5076000##k#l\r\n";
            outStr += "#L8#Exchange 250 mob points for #d10 #v5450000# #t5450000##k#l\r\n";
            outStr += "#L7#Exchange 300 mob points for #d5 #v5130000# #t5130000##k#l\r\n";
            outStr += "#L1#Exchange 500 mob points for #d5 #v5510000# #t5510000##k#l\r\n";
            outStr += "#L2#Exchange 750 mob points for #d1 random Tao of Shadows/Sight/Harmony#k#l\r\n";
            outStr += "#L3#Exchange 750 mob points for #d100 #v4006000# #t4006000##k#l\r\n";
            outStr += "#L4#Exchange 1000 mob points for #d1 #v4031179# #t4031179##k#l\r\n";
            outStr += "#L5#Exchange 1500 mob points for #d100 #v4006001# #t4006001##k#l\r\n";
            outStr += "#L6#Exchange 2000 mob points for #d1 #v4031917# #t4031917##k#l#n\r\n";

            status = 4;

            cm.sendSimple(outStr);
        }
        else if (selection == 4)
        {
            var outStr = "#e#L0#Exchange 200 mob points to increase #dEQUIP by 4#k#l\r\n";
            outStr += "#L1#Exchange 200 mob points to increase #dUSE by 4#k#l\r\n";
            outStr += "#L2#Exchange 200 mob points to increase #dSETUP by 4#k#l\r\n";
            outStr += "#L3#Exchange 200 mob points to increase #dETC by 4#k#l\r\n";
            outStr += "#L4#Exchange 200 mob points to increase #dSTORAGE by 4#k#l#n\r\n";

            status = 5;

            cm.sendSimple(outStr);
        }
        else if (selection == 5)
        {
            var outStr = "How Mob Points work is every time you kill a mob, you will gain a certain amount of mob points. Below is how the mob points are calculated. You can only fit in one of the following categories:\r\n\r\n";
            outStr += "#e#bKilling a mob 5 levels below you with using AOE: #d0.1 mob points\r\n";
            outStr += "#bKilling a mob 5 levels below you without using AOE: #d0.3 mob points\r\n";
            outStr += "#bKilling a mob using AOE: #d0.5 mob points\r\n";
            outStr += "#bKilling a mob without using AOE: #d1 mob point\r\n";
            outStr += "#bKilling a mob equal or above level 110 without using AOE: #d0.5 mob points\r\n";
            outStr += "#bKilling a mob equal or above level 110 with using AOE: #d0.25 mob points#n#k\r\n\r\n";

            outStr += "The definition of AOE is a skill that allows you to attack monsters above and/or below you. Examples of skills are Genesis, Blizzard, Dragon Roar, etc.\r\n\r\n";

            outStr += "In addition, you get bonuses for killing bosses. If you are in a party, the flat mob points below will be divided by the number of party members. These are the flat bonuses for each boss:\r\n\r\n";

            outStr += "#e#bZakum: #d1,000 mob points\r\n";
            outStr += "#bHorntail: #d5,000 mob points\r\n";
            outStr += "#bScarlion: #d750 mob points\r\n";
            outStr += "#bTarga: #d750 mob points\r\n";
            outStr += "#bPink Bean: #d25,000 mob points\r\n";
            outStr += "#bPapulatus: #d150 mob points\r\n";
            outStr += "#bPianus: #d180 mob points\r\n";
            outStr += "#bBlack Crow: #d200 mob points\r\n";
            outStr += "#bLeviathan: #d40 mob points\r\n";
            outStr += "#bHeadless Horseman: #d50 mob points\r\n";
            outStr += "#bFemale Boss (Anego): #d600 mob points\r\n";
            outStr += "#bBigfoot: #d300 mob points#n#k\r\n\r\n";

            outStr += "For example, if you were in a party of 4 and killed Zakum, everyone in the party will get 250 mob points. The less people in the party, the more mob points bonus you get.\r\n\r\n";

            outStr += "In order to also improve community based game play, everyone in a party will receive an #e#dadditional 0.1 mob points#n#k besides the killer for whatever mob a party member kills no matter what level the mob is. At least 3 people are required in the party for this.";

            cm.sendOk(outStr);
            cm.dispose();
        }
        else if (selection == 6)
        {
            status = 6;

            cm.sendGetText("Please enter the player of who you like to gift your Mob Points to.\r\n\r\n#eThe player receiving the Mob Points will need to be online. #rThere will be a 10% tax on every transfer.#k#n");
        }
    }
    else if (status == 2) // Transportation
    {
        if (selection == 0)
        {
            if(cm.getPlayer().getMobPoints() >= 100) 
            {
                if(cm.canHold(2030000, 30))
                {
                    cm.gainItem(2030000, 30);
                    cm.getPlayer().gainMobPoints(-100);
                    cm.logMobPoints("Return Scroll Nearest Town");
                    cm.sendOk("Here are your 30 #t2030000#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these Return Scrolls!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 1)
        {
            if(cm.getPlayer().getMobPoints() >= 100) 
            {
                if(cm.canHold(2030001, 10))
                {
                    cm.gainItem(2030001, 10);
                    cm.getPlayer().gainMobPoints(-100);
                    cm.logMobPoints("Return Scroll to Lith Harbor");
                    cm.sendOk("Here are your 10 #t2030001#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these Return Scrolls!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 2)
        {
            if(cm.getPlayer().getMobPoints() >= 100) 
            {
                if(cm.canHold(2030002))
                {
                    cm.gainItem(2030002, 10);
                    cm.getPlayer().gainMobPoints(-100);
                    cm.logMobPoints("Return Scroll to Ellinia");
                    cm.sendOk("Here are your 10 #t2030002#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these Return Scrolls!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 3)
        {
            if(cm.getPlayer().getMobPoints() >= 100) 
            {
                if(cm.canHold(2030003, 10))
                {
                    cm.gainItem(2030003, 10);
                    cm.getPlayer().gainMobPoints(-100);
                    cm.logMobPoints("Return Scroll to Perion");
                    cm.sendOk("Here are your 10 #t2030003#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these Return Scrolls!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 4)
        {
            if(cm.getPlayer().getMobPoints() >= 100) 
            {
                if(cm.canHold(2030004, 10))
                {
                    cm.gainItem(2030004, 10);
                    cm.getPlayer().gainMobPoints(-100);
                    cm.logMobPoints("Return Scroll to Henesys");
                    cm.sendOk("Here are your 10 #t2030004#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these Return Scrolls!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 5)
        {
            if(cm.getPlayer().getMobPoints() >= 100) 
            {
                if(cm.canHold(2030005, 10))
                {
                    cm.gainItem(2030005, 10);
                    cm.getPlayer().gainMobPoints(-100);
                    cm.logMobPoints("Return Scroll to Kerning City");
                    cm.sendOk("Here are your 10 #t2030005#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these Return Scrolls!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 6)
        {
            if(cm.getPlayer().getMobPoints() >= 100) 
            {
                if(cm.canHold(2030006, 10))
                {
                    cm.gainItem(2030006, 10);
                    cm.getPlayer().gainMobPoints(-100);
                    cm.logMobPoints("Return Scroll to Sleepywood");
                    cm.sendOk("Here are your 10 #t2030006#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these Return Scrolls!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 7)
        {
            if(cm.getPlayer().getMobPoints() >= 100) 
            {
                if(cm.canHold(2030007, 10))
                {
                    cm.gainItem(2030007, 10);
                    cm.getPlayer().gainMobPoints(-100);
                    cm.logMobPoints("Return Scroll for Dead Mine");
                    cm.sendOk("Here are your 10 #t2030007#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these Return Scrolls!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 8)
        {
            if(cm.getPlayer().getMobPoints() >= 750) 
            {
                if(cm.canHold(5041000, 6))
                {
                    cm.gainItem(5041000, 6);
                    cm.getPlayer().gainMobPoints(-750);
                    cm.logMobPoints("VIP Teleport Rock");
                    cm.sendOk("Here are your 6 #t5041000#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these #t5041000#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
    }
    else if (status == 3) // Boosters
    {
        if(selection == 0)
        {
            if(cm.getPlayer().getMobPoints() >= 500) 
            {
                if(cm.getPlayer().getParty() != null)
                {
                    var partyMembers = cm.getPartyMembers();
                    for(var member = 0; member < partyMembers.size(); member++)
                    {
                        Packages.client.SkillFactory.getSkill(9001000).getEffect(Packages.client.SkillFactory.getSkill(9001000).getMaxLevel()).applyTo(partyMembers.get(member));
                    }

                    cm.getPlayer().gainMobPoints(-500);
                    cm.logMobPoints("Haste");
                    cm.sendOk("All of your party members has received Haste for 30 minutes!");
                }
                else
                {
                    cm.sendOk("You must be in a party!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 1)
        {
            if(cm.getPlayer().getMobPoints() >= 500) 
            {
                if(cm.canHold(2022273))
                {
                    cm.gainItem(2022273);
                    cm.getPlayer().gainMobPoints(-500);
                    cm.logMobPoints("Ssiws Cheese");
                    cm.sendOk("Here is your #t2022273#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this #t2022273#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 2)
        {
            if(cm.getPlayer().getMobPoints() >= 1000) 
            {
                if(cm.canHold(2012008))
                {
                    cm.gainItem(2012008);
                    cm.getPlayer().gainMobPoints(-1000);
                    cm.logMobPoints("Unripe Onyx Apple");
                    cm.sendOk("Here is your #t2012008#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this #t2012008#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 3)
        {
            if(cm.getPlayer().getMobPoints() >= 1500) 
            {
                if(cm.canHold(2022179))
                {
                    cm.gainItem(2022179);
                    cm.getPlayer().gainMobPoints(-1500);
                    cm.logMobPoints("Onyx Apple");
                    cm.sendOk("Here is your #t2022179#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this #t2022179#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 4)
        {
            if(cm.getPlayer().getMobPoints() >= 2000) 
            {
                if(cm.canHold(5211048))
                {
                    cm.gainItem(5211048, 1, false, false, 1800000);
                    cm.getPlayer().gainMobPoints(-2000);
                    cm.getPlayer().setRates();
                    cm.logMobPoints("30 Minutes EXP Card");
                    cm.sendOk("Here is your 30 minutes EXP card!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this EXP card!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 5)
        {
            if(cm.getPlayer().getMobPoints() >= 3000) 
            {
                if(cm.canHold(5211048))
                {
                    cm.gainItem(5211048, 1, false, false, 3600000);
                    cm.getPlayer().gainMobPoints(-3000);
                    cm.getPlayer().setRates();
                    cm.logMobPoints("1 Hour EXP Card");
                    cm.sendOk("Here is your 1 hour EXP card!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this EXP card!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
    }
    else if (status == 4) // Comestics
    {
        if (selection == 0) 
        {
            if(cm.getPlayer().getMobPoints() >= 300) 
            {
                var ranitem = chairs[Math.floor(Math.random()*chairs.length)];
                if(cm.canHold(ranitem))
                {
                    cm.gainItem(ranitem);
                    cm.getPlayer().gainMobPoints(-300);
                    cm.logMobPoints("Random Chair: " + ranitem);
                    cm.sendOk("Here is your random chair!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this chair!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 1) 
        {
            if(cm.getPlayer().getMobPoints() >= 500) 
            {
                var ranitem = donorhats[Math.floor(Math.random()*donorhats.length)];
                if(cm.canHold(ranitem))
                {
                    cm.donatorItem(ranitem);
                    cm.getPlayer().gainMobPoints(-500);
                    cm.logMobPoints("Rare Pernament Cosmetic Hat: " + ranitem);
                    cm.sendOk("Here is your rare pernament cosmetic hat!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this hat!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 2) 
        {
            if(cm.getPlayer().getMobPoints() >= 500) 
            {
                var ranitem = donorshirts[Math.floor(Math.random()*donorshirts.length)];
                if(cm.canHold(ranitem))
                {
                    cm.donatorItem(ranitem);
                    cm.getPlayer().gainMobPoints(-500);
                    cm.logMobPoints("Rare Pernament Cosmetic Shirt: " + ranitem);
                    cm.sendOk("Here is your rare pernament cosmetic shirt!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this shirt!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 3) 
        {
            if(cm.getPlayer().getMobPoints() >= 500) 
            {
                var ranitem = donorshorts[Math.floor(Math.random()*donorshorts.length)];
                if(cm.canHold(ranitem))
                {
                    cm.donatorItem(ranitem);
                    cm.getPlayer().gainMobPoints(-500);
                    cm.logMobPoints("Rare Pernament Cosmetic Shorts: " + ranitem);
                    cm.sendOk("Here is your rare pernament cosmetic shorts!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this shorts!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        } 
        else if (selection == 4) 
        {
            if(cm.getPlayer().getMobPoints() >= 500) 
            {
                var ranitem = donoroveralls[Math.floor(Math.random()*donoroveralls.length)];
                if(cm.canHold(ranitem))
                {
                    cm.donatorItem(ranitem);
                    cm.getPlayer().gainMobPoints(-500);
                    cm.logMobPoints("Rare Pernament Cosmetic Overall: " + ranitem);
                    cm.sendOk("Here is your rare pernament cosmetic overall!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this overall!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 5) 
        {
            if(cm.getPlayer().getMobPoints() >= 500) 
            {
                var ranitem = donorshoes[Math.floor(Math.random()*donorshoes.length)];
                if(cm.canHold(ranitem))
                {
                    cm.donatorItem(ranitem);
                    cm.getPlayer().gainMobPoints(-500);
                    cm.logMobPoints("Rare Pernament Cosmetic Shoe: " + ranitem);
                    cm.sendOk("Here is your rare pernament cosmetic shoe!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this shoe!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 6) 
        {
            if(cm.getPlayer().getMobPoints() >= 500) 
            {
                var ranitem = donorgloves[Math.floor(Math.random()*donorgloves.length)];
                if(cm.canHold(ranitem))
                {
                    cm.donatorItem(ranitem);
                    cm.getPlayer().gainMobPoints(-500);
                    cm.logMobPoints("Rare Pernament Cosmetic Glove: " + ranitem);
                    cm.sendOk("Here is your rare pernament cosmetic glove!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this glove!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 7) 
        {
            if(cm.getPlayer().getMobPoints() >= 500) 
            {
                var ranitem = donorweapons[Math.floor(Math.random()*donorweapons.length)];
                if(cm.canHold(ranitem))
                {
                    cm.donatorItem(ranitem);
                    cm.getPlayer().gainMobPoints(-500);
                    cm.logMobPoints("Rare Pernament Cosmetic Weapon: " + ranitem);
                    cm.sendOk("Here is your rare pernament cosmetic weapon!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this weapon!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 8) 
        {
            if(cm.getPlayer().getMobPoints() >= 1000) 
            {
                var ranitem = donorcapes[Math.floor(Math.random()*donorcapes.length)];
                if(cm.canHold(ranitem))
                {
                    cm.donatorItem(ranitem);
                    cm.getPlayer().gainMobPoints(-1000);
                    cm.logMobPoints("Rare Pernament Cosmetic Cape: " + ranitem);
                    cm.sendOk("Here is your rare pernament cosmetic cape!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this cape!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
    }
    else if (status == 5) // Items
    {
        if (selection == 0)
        {
            if(cm.getPlayer().getMobPoints() >= 250) 
            {
                if(cm.canHold(50760001, 10))
                {
                    cm.gainItem(5076000, 10);
                    cm.getPlayer().gainMobPoints(-250);
                    cm.logMobPoints("Item Megaphone");
                    cm.sendOk("Here are your 10 #t5076000#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these #t5076000#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 1)
        {
            if(cm.getPlayer().getMobPoints() >= 500) 
            {
                if(cm.canHold(5510000, 5))
                {
                    cm.gainItem(5510000, 5);
                    cm.getPlayer().gainMobPoints(-500);
                    cm.logMobPoints("Wheel of Destiny");
                    cm.sendOk("Here are your 5 #t5510000#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these #t5510000#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 2)
        {
            if(cm.getPlayer().getMobPoints() >= 750) 
            {
                var ranitem = tao[Math.floor(Math.random()*tao.length)];

                if(cm.canHold(ranitem))
                {
                    cm.gainItem(ranitem);
                    cm.getPlayer().gainMobPoints(-750);
                    cm.logMobPoints("Random Tao: " + ranitem);
                    cm.sendOk("Here is your #t" + ranitem + "#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold a Tao!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 3)
        {
            if(cm.getPlayer().getMobPoints() >= 750) 
            {
                if(cm.canHold(4006000, 100))
                {
                    cm.gainItem(4006000, 100);
                    cm.getPlayer().gainMobPoints(-750);
                    cm.logMobPoints("The Magic Rock");
                    cm.sendOk("Here are your 100 #t4006000#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these #t4006000#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 4)
        {
            if(cm.getPlayer().getMobPoints() >= 1000) 
            {
                if(cm.canHold(4031179))
                {
                    cm.gainItem(4031179);
                    cm.getPlayer().gainMobPoints(-1000);
                    cm.logMobPoints("Piece of Cracked Dimension");
                    cm.sendOk("Here is your #t4031179#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this #t4031179#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 5)
        {
            if(cm.getPlayer().getMobPoints() >= 1500) 
            {
                if(cm.canHold(4006001, 100))
                {
                    cm.gainItem(4006001, 100);
                    cm.getPlayer().gainMobPoints(-1000);
                    cm.logMobPoints("The Summoning Rock");
                    cm.sendOk("Here are your #t4006001#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these #t4006001#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 6)
        {
            if(cm.getPlayer().getMobPoints() >= 2000) 
            {
                if(cm.canHold(4031917))
                {
                    cm.gainItem(4031917);
                    cm.getPlayer().gainMobPoints(-2000);
                    cm.logMobPoints("Crystal Shard");
                    cm.sendOk("Here is your #t4031917#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold this #t4031917#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 7)
        {
            if(cm.getPlayer().getMobPoints() >= 300) 
            {
                if(cm.canHold(5130000, 5))
                {
                    cm.gainItem(5130000, 5);
                    cm.getPlayer().gainMobPoints(-300);
                    cm.logMobPoints("Safety Charm");
                    cm.sendOk("Here are your #t5130000#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these #t5130000#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
        else if (selection == 8)
        {
            if(cm.getPlayer().getMobPoints() >= 250) 
            {
                if(cm.canHold(5450000, 10))
                {
                    cm.gainItem(5450000, 10);
                    cm.getPlayer().gainMobPoints(-250);
                    cm.logMobPoints("Miu Miu the Traveling Merchant");
                    cm.sendOk("Here are your 10 #t5450000#!");
                } 
                else 
                {
                    cm.sendOk("Please make sure you have enough space to hold these #t5450000#!");
                }
            } 
            else 
            {
                cm.sendOk("Sorry, you don't have enough Mob Points!");
            }
            cm.dispose();
        }
    }
    else if (status == 6)
    {
        if(cm.getPlayer().getMobPoints() >= 200) 
        {
            if(selection == 0)
            {
                if (cm.getPlayer().gainSlots(1, 4, true)) 
                {
                    cm.getPlayer().gainMobPoints(-200);
                    cm.getPlayer().getClient().announce(Packages.tools.MaplePacketCreator.showBoughtInventorySlots(1, cm.getPlayer().getSlots(1)));
                    cm.logMobPoints("Increase EQUIP Slot");
                    cm.sendOk("Your EQUIP slot has increased by 4!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Your EQUIP slot is already maxed out!");
                    cm.dispose();
                }
            }
            else if(selection == 1)
            {
                if (cm.getPlayer().gainSlots(2, 4, true)) 
                {
                    cm.getPlayer().gainMobPoints(-200);
                    cm.getPlayer().getClient().announce(Packages.tools.MaplePacketCreator.showBoughtInventorySlots(2, cm.getPlayer().getSlots(2)));
                    cm.logMobPoints("Increase USE Slot");
                    cm.sendOk("Your USE slot has increased by 4!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Your USE slot is already maxed out!");
                    cm.dispose();
                }
            }
            else if(selection == 2)
            {
                if (cm.getPlayer().gainSlots(3, 4, true)) 
                {
                    cm.getPlayer().gainMobPoints(-200);
                    cm.getPlayer().getClient().announce(Packages.tools.MaplePacketCreator.showBoughtInventorySlots(3, cm.getPlayer().getSlots(3)));
                    cm.logMobPoints("Increase SETUP Slot");
                    cm.sendOk("Your SETUP slot has increased by 4!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Your SETUP slot is already maxed out!");
                    cm.dispose();
                }
            }
            else if(selection == 3)
            {
                if (cm.getPlayer().gainSlots(4, 4, true)) 
                {
                    cm.getPlayer().gainMobPoints(-200);
                    cm.getPlayer().getClient().announce(Packages.tools.MaplePacketCreator.showBoughtInventorySlots(4, cm.getPlayer().getSlots(4)));
                    cm.logMobPoints("Increase ETC Slot");
                    cm.sendOk("Your ETC slot has increased by 4!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Your ETC slot is already maxed out!");
                    cm.dispose();
                }
            }
            else if(selection == 4)
            {
                if (cm.getPlayer().getStorage().gainSlots(4))
                {
                    cm.getPlayer().gainMobPoints(-200);
                    cm.getPlayer().getClient().announce(Packages.tools.MaplePacketCreator.showBoughtStorageSlots(cm.getPlayer().getStorage().getSlots()));
                    cm.logMobPoints("Increase STORAGE Slot");
                    cm.sendOk("Your STORAGE slot has increased by 4!");
                    cm.dispose();
                }
                else
                {
                    cm.sendOk("Your STORAGE slot is already maxed out!");
                    cm.dispose();
                }
            }
        }
        else
        {
            cm.sendOk("Sorry, you don't have enough Mob Points!");
            cm.dispose();
        }
    }
    else if (status == 7)
    {
        var targetName = cm.getText();   
        target = cm.getClient().getWorldServer().getPlayerStorage().getCharacterByName(targetName);

        if (target == null || target.isIntern())
        {
            cm.sendOk("#e" + targetName + "#n is not online!");
            cm.dispose();
        }
        else
        {
            cm.sendGetNumber("How much Mob Points would you like to gift to #e" + targetName + "#n?\r\n\r\n#bYou currently have " + mobpoints + " Mob Points.#k", 1, 1, mobpoints);
        }
    }
    else if (status == 8)
    {
        if (selection > cm.getPlayer().getMobPoints())
        {
            cm.sendOk("Sorry, you don't have enough Mob Points!");
            cm.dispose();
        }
        else
        {
            points = selection;
            tax = parseInt(points * 0.1);
            points = points - tax;

            cm.sendYesNo("The tax will be #e#b" + tax + "#n#k Mob Point(s) so the player will receive a total of #e#b" + points + "#n#k Mob Point(s). Would you like to continue?");
        }
    }
    else if (status == 9)
    {
        cm.getPlayer().gainMobPoints(-(points + tax));
        target.gainMobPoints(points);

        target.message(cm.getPlayer() + " has gifted " + points + " Mob Points to you!");

        cm.sendOk("#e" + target.getName() + "#n has been gifted " + points + " Mob Points!\r\n\r\n#eThe tax was " + tax + " Mob Points.#n");
        cm.dispose();
    }
    else
    {
        cm.dispose();
    }
}  