// Donator Point Exchanger

importPackage(Packages.tools);

var donatorpoints;
var points;
var status = 0;
var chairs = new Array(3010097, 3010107, 3010108, 3010120, 3010126, 3010127, 3010109, 3010110, 3010021, 3010049, 3010050, 3010051, 3010054, 3010055, 3010113, 3010114, 3010115, 3010117, 3010095, 3010096, 3010128, 3010129, 3010130, 3010131, 3010132, 3010133, 3010134, 3010137, 3010152, 3010124, 3010125, 3010036, 3010037, 3010038, 3010039, 3010119, 3010155, 3010161, 3010168, 3010169, 3010170, 3010171, 3010172, 3010173, 3010174, 3010175, 3010177, 3010180, 3010181, 3010183, 3010188, 3010189, 3010139, 3010052, 3010075, 3010077, 3010078, 3010079, 3010102, 3010104, 3010112, 3011000, 3012001, 3012002, 3012004, 3012005, 3013000, 3013001, 3011002, 3011004, 3011006, 3016400, 3016401, 3016402, 3016403, 3016404, 3016405, 3016406, 3016407, 3016408, 3016409, 3016410, 3016411, 3016412, 3016413, 3016414, 3016415, 3016416, 3016417, 3016419, 3016420, 3016421, 3016422, 3011518, 3011005, 3014000, 3014001, 3014002, 3014003, 3014004, 3014006, 3014007, 3014008, 3014009, 3014010, 3014011, 3014012, 3016418, 3016423, 3016424, 3016425, 3016426, 3016427, 3016428, 3016429, 3016430, 3016433, 3016434, 3016435, 3016436, 3016437, 3016438, 3016439, 3016440, 3016441, 3016442, 3016443, 3016444, 3010059, 3010070, 3010074, 3010086, 3010087, 3010088, 3010089, 3010090, 3010091, 3010093, 3010094, 3010100, 3010121, 3010122, 3010138, 3010140, 3010141, 3010142, 3010144, 3010145, 3010148, 3010149, 3010150, 3010154, 3012006, 3012009, 3012012, 3012099, 3013000, 3013337, 3017950, 3013999, 3019100, 3010196, 3010197, 3010200, 3010201, 3010151, 3010194, 3010211, 3010208, 3018011, 3018010, 3018009, 3018008, 3018007, 3018006, 3018005, 3018004, 3018003, 3018002, 3018001, 3018000, 3013006, 3010204, 3010156, 3010162, 3010164, 3010179, 3010185, 3010195, 3010296, 3010290, 3010285, 3010282, 3010253, 3010252, 3010251, 3010191, 3010157, 3010136, 3010123, 3010186, 3010184);

var donorhats = new Array(1002076, 1004163, 1003136, 1003463, 1004471, 1802365, 1004000, 1003461, 1002368, 1002225, 1003367, 1002506, 1003387, 1004107, 1004057, 1002842, 1002843, 1004162, 1001098,1001103,1000031,1000032,1000035,1000036,1000037,1000038,1004024,1000041,1000043,1000044,1000045,1000047,1000050,1000058,1000060,1000061,1000062,1000069,1000070,1000071,1000072,1000074,1001010,1001011,1001036,1001037,1001061,1001066,1001069,1001068,1001070,1001072,1001076,1001084,1001088,1001089,1001090,1001091,1002671,1002998,1002999,1003000,1003001,1003005,1003015,1003070,1003109,1003131,1003132,1003133,1003148,1003149,1003170,1003186,1003187,1003207,1003204,1003208,1003210,1003211,1003216,1003219,1003221,1003222,1003237,1003247,1003268,1003269,1003272,1003279,1003390,1003399,1003401,1003402,1003404,1003417,1003459,1003510,1003533,1003548,1003568,1003575,1003581,1003586,1003587,1003588,1003596,1003597,1003680,1003746,1003747,1003748,1003750,1003753,1003759,1003760,1003763,1003769,1003802,1003803,1003807,1003808,1003809,1003829,1003830,1003846,1003847,1003861,1003862,1003884,1003892,1003901,1003902,1003903,1003904,1003906,1003909,1003910,1003912,1003919,1003934,1003963,1003980,1003981,1004005,1004025,1004026,1004027,1004028,1004029,1004033,1004036,1004038,1004040,1004043,1004045,1004050,1004051,1004052,1004072,1004092,1004094,1004095,1004106,1004108,1004112,1004114,1004117,1004126,1004137,1004139,1004140,1004141,1004142,1004143,1004144,1004145,1004146,1004147,1004148,1004156,1004157,1004158,1004167,1004193,1004198,1004200);
var donorshirts = new Array(1040027, 1051298, 1040001,1040005,1040036,1040077,1040078,1040123,1040126,1040133,1040135,1040138,1040192,1041073,1040193,1041046,1041135,1041136,1041137,1041138,1041194,1041195,1042049,1042050,1042067,1042082,1042092,1042104,1042105,1042106,1042108,1042130,1042132,1042134,1042141,1042142,1042143,1042144,1042146,1042149,1042147,1042150,1042152,1042154,1042155,1042156,1042157,1042158,1042159,1042161,1042162,1042163,1042164,1042165,1042166,1042168,1042169,1042170,1042171,1042172,1042174,1042176,1042177,1042178,1042181,1042182,1042183,1042184,1042185,1042189,1042190,1042193,1042195,1042196,1042197,1042199,1042200,1042201,1042202,1042203,1042204,1042206,1042207,1042208,1042209,1042210,1042212,1042213,1042214,1042215,1042216,1042217,1042218,1042219,1042220,1042222,1042228,1042229,1042230,1042232,1042235,1042236,1042237,1042238,1042240,1042241,1042242,1042250,1042251,1042252,1042260,1042263,1042265,1042267,1042270,1042271,1042276,1042277,1042278,1042279,1042280,1042281,1042282,1042285,1042287,1042288,1042289,1042290,1042292,1042293,1042311,1042312,1042313,1042314,1042315,1042316,1042320,1042321,1048001,1048002);
var donorshorts = new Array(1060003,1060054,1060055,1060067,1060108,1060113,1060114,1060117,1060120,1060121,1060126,1060138,1060142,1060179,1060180,1060181,1060182,1060183,1061005,1061039,1061107,1061127,1061128,1061129,1061137,1061130,1061131,1061133,1061138,1061140,1061141,1061142,1061143,1061148,1061170,1061203,1061205,1061206,1061207,1061208,1061209,1062046,1062067,1062068,1062070,1062071,1062072,1062076,1062077,1062081,1062083,1062084,1062085,1062089,1062091,1062093,1062094,1062095,1062096,1062097,1062098,1062100,1062101,1062103,1062104,1062105,1062106,1062107,1062108,1062109,1062110,1062111,1062112,1062113,1062114,1062116,1062117,1062118,1062119,1062121,1062122,1062123,1062124,1062126,1062131,1062133,1062134,1062135,1062136,1062137,1062138,1062139,1062151,1062152,1062153,1062155,1062157,1062175,1062183,1062184,1062189,1062203,1062204,1062208,1062209,1062210,1062211);
var donoroveralls = new Array(1052289, 1051386, 1052724, 1052598, 1052596, 1052762, 1052948, 1052145, 1050019, 1050119, 1052625, 1051373, 1052617,1052574,1052575,1052587,1052092,1052067,1051131,1052408,1050121,1050122,1050126,1052975,1050129,1052671,1050140,1050141,1050142,1050143,1050146,1050148,1050147,1050151,1050152,1050154,1050153,1050157,1050161,1050168,1050170,1050177,1050179,1050188,1050190,1050207,1050215,1050221,1050220,1050222,1050226,1050227,1050230,1050234,1050235,1050242,1050243,1050244,1050246,1050248,1050255,1050284,1050285,1050287,1050289,1050291,1050293,1050296,1050298,1050301,1050302,1050303,1050304,1050305,1050311,1050312,1050314,1050329,1050330,1050331,1050332,1050333,1050334,1050324,1050325,1050326,1050327,1051048,1051070,1051075,1051076,1051132,1051133,1051134,1051135,1051137,1051139,1051150,1051151,1051152,1051153,1051158,1051159,1051162,1051163,1051173,1051174,1051175,1051176,1051183,1051185,1051188,1051189,1051190,1051192,1051194,1051195,1051198,1051211,1051212,1051218,1051219,1051228,1051229,1051234,1051235,1051224,1051225,1051256,1051261,1051262,1051264,1051265,1051270,1051271,1051272,1051277,1051278,1051280,1051282,1051284,1051291,1051292,1051294,1051296,1051303,1051332,1051345,1051352,1051354,1051355,1051366,1051367,1051369,1051371,1051372,1051374,1051375,1051376,1051382,1051387,1052245,1052246,1052248,1052255,1052256,1052275,1052293,1052294,1052295,1052296,1052306,1052324,1052329,1052372,1052421,1052423,1052440,1052446,1052447,1052458,1052506);
var donorshoes = new Array(1072462, 1072859, 1072805, 1070005, 1072507, 1072831, 1070002,1070004,1070006,1070015,1070016,1070020,1070026,1070027,1070028,1070057,1070059,1070061,1071000,1071001,1071013,1071014,1071026,1071030,1071031,1071037,1071044,1071074,1072279,1072280,1072282,1072283,1072322,1072330,1072331,1072333,1072334,1072336,1072341,1072349,1072367,1072371,1072393,1072394,1072395,1072406,1072407,1072426,1072437,1072440,1072482,1072484,1072514,1072517,1072529,1072530,1072531,1072637,1072650,1072651,1072779,1072780,1072791,1072803,1072808,1072820,1072823,1072829,1072857,1072858,1072860,1072866,1072867,1072876,1072917,1072918,1072919,1072941,1072942);
var donorgloves = new Array(1082101, 1081000,1081001,1081003,1081004,1081006,1082165,1082227,1082233,1082247,1082250,1082263,1082267,1082421,1082422,1082448,1082493,1082495,1082502,1082520,1082557,1082542,1082549,1082550,1082551,1082552,1082563,1082587,1082588);
var donorcapes = new Array(1102540, 1102692, 1102665, 1102380, 1102095, 1102142, 1102148,1102152,1102184,1102186,1102187,1102188,1102094,1102096,1102097,1102107,1102208,1102209,1102210,1102213,1102214,1102215,1102216,1102218,1102222,1102223,1102229,1102232,1102236,1102240,1102244,1102251,1102252,1102253,1102257,1102258,1102271,1102273,1102274,1102301,1102310,1102318,1102325,1102326,1102334,1102349,1102353,1102358,1102373,1102376,1102377,1102378,1102385,1102386,1102387,1102388,1102389,1102390,1102392,1102395,1102396,1102420,1102450,1102451,1102452,1102453,1102466,1102487,1102496,1102509,1102510,1102532,1102546,1102547,1102548,1102549,1102554,1102629,1102616,1102617,1102632,1102642,1102644,1102668,1102705,1102707,1102708,1102723,1102724,1102729,1102730,1102755,1102756,1102758,1102809,1103010,1103001,1103000);
var donorweapons = new Array(1702327, 1702239, 1702400, 1702480, 1702529, 1702118, 1702136, 1702008, 1702120, 1702119, 1092067,1702435,1702453,1702526,1702574,1702233,1702236,1702237,1702238,1702224,1702252,1702263,1702265,1702266,1702267,1702274,1702275,1702276,1702277,1702278,1702279,1702280,1702281,1702282,1702286,1702287,1702288,1702289,1702291,1702293,1702298,1702299,1702301,1702306,1702307,1702308,1702309,1702310,1702348,1702349,1702350,1702351,1702352,1702357,1702366,1702367,1702368,1702369,1702371,1702372,1702373,1702374,1702375,1702376,1702377,1702378,1702379,1702380,1702381,1702382,1702423,1702424,1702433,1702440);
var donoraccessories = new Array(1022090, 1012044, 1012557, 1012525, 1012530, 1012531, 1012517, 1012511, 1012384, 1012508, 1012479, 1012482, 1012468, 1012437, 1022173, 1022207, 1022243);

var donormounts = new Array(1907009, 1907020, 1907020, 1907020, 1902714, 01902046,01902048,01902051,01902052,01902125,01902300,01902301,01902309,01902316,01902321,01902323,01902337,01902339,01902340,01902504,01902505,01902506,01902507,01902508,01902509,01902510,01902512,01902514,01902519,01902527,01902539,01902800,01902802,01902803,01902806,01902807,01902808,01902809,01902810,01902811,01906003,01906004,01906006,01908124,01908125,01908131,01908132,01908133,01908134,01908135,01908136,01908137,01908138,01908139,01908140,01909000,01909001,01909002,01909004,01909005,01909006,01909007,01909008,01909009,01909010,01909011,01909012,01909013,01909014,01909015,01909016,01909017,01909021,01909022,01909023,01909024,01909025,01909026);
var donorpets = new Array(5000020,5000021,5000022,5000023,5000024,5000025,5000026,5000028,5000029,5000030,5000031,5000032,5000033,5000036,5000037,5000039,5000041,5000042,5000044,5000045,5000047,5000048,5000049,5000050,5000051,5000052,5000053,5000054,5000055,5000058,5000060,5000066,5000067,5000070,5000074,5000076,5000083,5000084,5000085,5000086,5000089,5000090,5000091,5000098,5000130,5000131,5000132,5000133,5000134,5000135,5000138,5000139,5000142,5000143,5000144,5000145,5000146,5000147,5000148,5000149,5000150,5000151,5000152,5000155,5000156,5000167,5000216,5000217,5000243,5000244,5000245,5000290,5000291,5000292,5000293,5000294,5000295,5000296,5000297,5000298,5000309,5000310,5000311,5000316,5000318,5000330,5000331,5000332,5000341,5000342,5000343,5000344,5000345);

var itemSelection;

function start() 
{
	donatorpoints = cm.getClient().getDonatorPoints();

	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) 
{
	if (mode == -1)
		cm.dispose();
	else 
	{
		if (mode == 0 && status == 0)
			cm.dispose();
		if (mode == 1)
			status++;
		else
			status--;

		if (status == 0) 
		{
			var outStr = "Hello, you can exchange your donator points with me! All donator comestics are non-transferable and untradeable. #eYou can request any comestics that you don't see here by contacting a staff.#n\r\n\r\n";
			outStr += "#eYou currently have #r" + donatorpoints + "#k donator points.#n\r\n\r\n";

			//outStr += "#e#L16#Gift Donator Point(s) to Another Player#l#k\r\n\r\n";  
			//outStr += "#e#L0#Exchange 1 donator point for 5 #v5090000# #dNote#k#l\r\n";

			outStr += "#e#L1#Exchange 1 donator point to choose a #drare cosmetic hat#k#l\r\n";
			outStr += "#L2#Exchange 1 donator point to choose a #drare cosmetic shirt#k#l\r\n";
			outStr += "#L3#Exchange 1 donator point to choose a #drare cosmetic shorts#k#l\r\n";
			outStr += "#L4#Exchange 1 donator point to choose a #drare cosmetic overall#k#l\r\n";
			outStr += "#L5#Exchange 1 donator point to choose a #drare cosmetic shoe#k#l\r\n";
			outStr += "#L6#Exchange 1 donator point to choose a #drare cosmetic glove#k#l\r\n";
			outStr += "#L8#Exchange 1 donator point to choose a #drare cosmetic weapon#k#l\r\n";
			outStr += "#L17#Exchange 1 donator point to choose a #drare cosmetic face/eye accessory#k#l\r\n";
			outStr += "#L7#Exchange 2 donator points to choose a #drare cosmetic cape#k#l\r\n";
			outStr += "#L9#Exchange 2 donator points for a #drandom rare permanent mount (includes saddle)#k#l\r\n";
			outStr += "#L10#Exchange 2 donator points for a #drandom rare 30 day pet#k#l\r\n";
			outStr += "#L11#Exchange 2 donator points for a #drandom rare chair#k#l\r\n";
			outStr += "#L16#Exchange 5 donator points for a #drandom rare 1 year pet#k#l\r\n"
			outStr += "#L12#Exchange 5 donator points for a #v1142229# #dmyMaple Supporter medal (no stats)#k#l\r\n";
			outStr += "#L14#Exchange 5 donator points for a #dsex change#k#l\r\n";
			outStr += "#L13#Exchange 10 donator points for a #dname change#k#l\r\n";
			outStr += "#L15#Exchange 50 donator points to a #dcustom NPC#k#l#n\r\n";

			cm.sendSimple(outStr);
		} 
		else if (status == 1) 
		{
			if (selection == 0) 
			{
				if(donatorpoints > 0) 
				{
					if(cm.canHold(5090000))
					{
						cm.gainItem(5090000, 5);
						cm.getClient().addDonatorPoints(-1);
						cm.sendOk("Here are your 5 Notes!");
						cm.logDonator("Note");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold these notes!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
				}
				cm.dispose();
			}
			else if (selection == 1) 
			{
				if(donatorpoints > 0) 
				{
					var outStr = "";
					itemSelection = donorhats;

					for(var i = 0; i < itemSelection.length; i++)
					{
						outStr += "#L" + i + "##v" + itemSelection[i] + "# #t" + itemSelection[i] + "##l\r\n";
					}

					cm.sendSimple(outStr);
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
					cm.dispose();
				}
			}
			else if (selection == 2) 
			{
				if(donatorpoints > 0) 
				{
					var outStr = "";
					itemSelection = donorshirts;

					for(var i = 0; i < itemSelection.length; i++)
					{
						outStr += "#L" + i + "##v" + itemSelection[i] + "# #t" + itemSelection[i] + "##l\r\n";
					}

					cm.sendSimple(outStr);
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
					cm.dispose();
				}
			}
			else if (selection == 3) 
			{
				if(donatorpoints > 0) 
				{
					var outStr = "";
					itemSelection = donorshorts;

					for(var i = 0; i < itemSelection.length; i++)
					{
						outStr += "#L" + i + "##v" + itemSelection[i] + "# #t" + itemSelection[i] + "##l\r\n";
					}

					cm.sendSimple(outStr);
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
					cm.dispose();
				}
			} 
			else if (selection == 4) 
			{
				if(donatorpoints > 0) 
				{
					var outStr = "";
					itemSelection = donoroveralls;

					for(var i = 0; i < itemSelection.length; i++)
					{
						outStr += "#L" + i + "##v" + itemSelection[i] + "# #t" + itemSelection[i] + "##l\r\n";
					}

					cm.sendSimple(outStr);
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
					cm.dispose();
				}
			}
			else if (selection == 5) 
			{
				if(donatorpoints > 0) 
				{
					var outStr = "";
					itemSelection = donorshoes;

					for(var i = 0; i < itemSelection.length; i++)
					{
						outStr += "#L" + i + "##v" + itemSelection[i] + "# #t" + itemSelection[i] + "##l\r\n";
					}

					cm.sendSimple(outStr);
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
					cm.dispose();
				}
			}
			else if (selection == 6) 
			{
				if(donatorpoints > 0) 
				{
					var outStr = "";
					itemSelection = donorgloves;

					for(var i = 0; i < itemSelection.length; i++)
					{
						outStr += "#L" + i + "##v" + itemSelection[i] + "# #t" + itemSelection[i] + "##l\r\n";
					}

					cm.sendSimple(outStr);
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
					cm.dispose();
				}
			}
			else if (selection == 7) 
			{
				if(donatorpoints > 1) 
				{
					var outStr = "";
					itemSelection = donorcapes;

					for(var i = 0; i < itemSelection.length; i++)
					{
						outStr += "#L" + i + "##v" + itemSelection[i] + "# #t" + itemSelection[i] + "##l\r\n";
					}

					cm.sendSimple(outStr);
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
					cm.dispose();
				}
			}
			else if (selection == 8) 
			{
				if(donatorpoints > 0) 
				{
					var outStr = "";
					itemSelection = donorweapons;

					for(var i = 0; i < itemSelection.length; i++)
					{
						outStr += "#L" + i + "##v" + itemSelection[i] + "# #t" + itemSelection[i] + "##l\r\n";
					}

					cm.sendSimple(outStr);
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
					cm.dispose();
				}
			}
			else if (selection == 9) 
			{
				if(donatorpoints > 1) 
				{
					var ranitem = donormounts[Math.floor(Math.random()*donormounts.length)];
					if(cm.canHold(ranitem))
					{
						cm.gainItem(ranitem);

						if (!cm.hasItem(1912999))
						{
							cm.gainItem(1912999);
						}

						cm.getClient().addDonatorPoints(-2);
						cm.sendOk("Here is your rare permanent mount! #ePlease note that some monuts have a chance to dc people in the map. Please report this to the staff immediately if this happens. If you abuse this, you will be banned permanently.#n");
						cm.logDonator("Rare permanent Mount " + ranitem);
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold this mount!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
				}
				cm.dispose();
			}
			else if (selection == 10) 
			{
				if(donatorpoints > 1) 
				{
					var ranitem = donorpets[Math.floor(Math.random()*donorpets.length)];
					if(cm.canHold(ranitem))
					{
						cm.gainPetItem(ranitem, 30);
						cm.getClient().addDonatorPoints(-2);
						cm.sendOk("Here is your 30 day pet!");
						cm.logDonator("Rare 30 Day Pet " + ranitem);
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold this pet!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
				}
				cm.dispose();
			}
			else if (selection == 11) 
			{
				if(donatorpoints > 1) 
				{
					var chair1 = chairs[Math.floor(Math.random()*chairs.length)];
					if(cm.canHold(chair1))
					{
						cm.gainItem(chair1);
						cm.getClient().addDonatorPoints(-2);
						cm.sendOk("Here is your rare random chair!");
						cm.logDonator("Rare Chair: " + chair1);
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold this chair!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
				}
				cm.dispose();
			} 
			else if (selection == 12) 
			{
				if(donatorpoints > 4) 
				{
					if(cm.canHold(1142229))
					{
						cm.gainItem(1142229);
						cm.getClient().addDonatorPoints(-5);
						cm.sendOk("Here is your medal!");
						cm.logDonator("myMaple Donator Medal");
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold this medal!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
				}
				cm.dispose();
			} 
			else if (selection == 13) 
			{
				if(donatorpoints > 9) 
				{
					cm.sendOk("Please contact a GM to get your name changed!");
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
				}
				cm.dispose();
			}
			else if (selection == 14) 
			{
				if(donatorpoints > 4) 
				{
					cm.sendOk("Please contact a GM to get your sex changed!");
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
				}
				cm.dispose();
			} 
			else if (selection == 15) 
			{
				if(donatorpoints > 49) 
				{
					cm.sendOk("Please contact a GM to become a custom NPC you rich bitch!");
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
				}
				cm.dispose();
			} 
			else if (selection == 16) 
			{
				if(donatorpoints > 4) 
				{
					var ranitem = donorpets[Math.floor(Math.random()*donorpets.length)];
					if(cm.canHold(ranitem))
					{
						cm.gainPetItem(ranitem, 365);
						cm.getClient().addDonatorPoints(-5);
						cm.sendOk("Here is your 1 year pet!");
						cm.logDonator("Rare 1 Year Pet " + ranitem);
					} 
					else 
					{
						cm.sendOk("Please make sure you have enough space to hold this pet!");
					}
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
				}
				cm.dispose();
			}
			else if (selection == 17) 
			{
				if(donatorpoints > 0) 
				{
					var outStr = "";
					itemSelection = donoraccessories;

					for(var i = 0; i < itemSelection.length; i++)
					{
						outStr += "#L" + i + "##v" + itemSelection[i] + "# #t" + itemSelection[i] + "##l\r\n";
					}

					cm.sendSimple(outStr);
				} 
				else 
				{
					cm.sendOk("Sorry, you don't have enough donator points!");
					cm.dispose();
				}
			}
			else 
			{
				cm.dispose();
			}
		}
		else if (status == 2)
		{
			var ranitem = itemSelection[selection];
			if(cm.canHold(ranitem))
			{
				cm.donatorItem(ranitem);
				if(itemSelection == donorcapes)
				{
					cm.getClient().addDonatorPoints(-2);
				}
				else
				{
					cm.getClient().addDonatorPoints(-1);
				}
				cm.sendOk("Here is your rare permanent cosmetic equip!");
				cm.logDonator("Rare Permanent Cosmetic Equip " + ranitem);
			} 
			else 
			{
				cm.sendOk("Please make sure you have enough space to hold this equip!");
			}

			cm.dispose();
		}
		else if (status == 3)
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
				cm.sendGetNumber("How much Donator Points would you like to gift to #e" + targetName + "#n?\r\n\r\n#bYou currently have " + donatorpoints + " Donator Points.#k", 1, 1, donatorpoints);
			}
		}
		else if (status == 4)
		{
			if (selection > cm.getClient().getDonatorPoints())
			{
				cm.sendOk("Sorry, you don't have enough Donator Points!");
				cm.dispose();
			}
			else
			{
				points = selection;

				cm.sendYesNo("The player will receive a total of #e#b" + points + "#n#k Donator Point(s). Would you like to continue?");
			}
		}
		else if (status == 5)
		{
			cm.getClient().addDonatorPoints(-points);
			target.getClient().addDonatorPoints(points);

			target.message(cm.getPlayer() + " has gifted " + points + " Donator Points to you!");

			cm.sendOk("#e" + target.getName() + "#nhas been gifted " + points + " Donator Points!");
			cm.dispose();
		}
	}
}