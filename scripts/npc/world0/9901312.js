// Vote Point Exchanger

var chairs = new Array(3010097, 3010107, 3010108, 3010120, 3010126, 3010127, 3010109, 3010110, 3010021, 3010049, 3010050, 3010051, 3010054, 3010055, 3010113, 3010114, 3010115, 3010117, 3010095, 3010096, 3010128, 3010129, 3010130, 3010131, 3010132, 3010133, 3010134, 3010137, 3010152, 3010124, 3010125, 3010036, 3010037, 3010038, 3010039, 3010119, 3010155, 3010161, 3010168, 3010169, 3010170, 3010171, 3010172, 3010173, 3010174, 3010175, 3010177, 3010180, 3010181, 3010183, 3010188, 3010189, 3010139, 3010052, 3010075, 3010077, 3010078, 3010079, 3010102, 3010104, 3010112, 3011000, 3012001, 3012002, 3012004, 3012005, 3013000, 3013001, 3011002, 3011004, 3011006, 3016400, 3016401, 3016402, 3016403, 3016404, 3016405, 3016406, 3016407, 3016408, 3016409, 3016410, 3016411, 3016412, 3016413, 3016414, 3016415, 3016416, 3016417, 3016419, 3016420, 3016421, 3016422, 3011518, 3011005, 3014000, 3014001, 3014002, 3014003, 3014004, 3014006, 3014007, 3014008, 3014009, 3014010, 3014011, 3014012, 3016418, 3016423, 3016424, 3016425, 3016426, 3016427, 3016428, 3016429, 3016430, 3016433, 3016434, 3016435, 3016436, 3016437, 3016438, 3016439, 3016440, 3016441, 3016442, 3016443, 3016444, 3010059, 3010070, 3010074, 3010086, 3010087, 3010088, 3010089, 3010090, 3010091, 3010093, 3010094, 3010100, 3010121, 3010122, 3010138, 3010140, 3010141, 3010142, 3010144, 3010145, 3010148, 3010149, 3010150, 3010154, 3012006, 3012007, 3012009, 3012012, 3012099, 3013000, 3013337, 3017950, 3013999, 3019100, 3010196, 3010197, 3010200, 3010201, 3010151, 3010194, 3010211, 3010208, 3018011, 3018010, 3018009, 3018008, 3018007, 3018006, 3018005, 3018004, 3018003, 3018002, 3018001, 3018000, 3013006, 3010204, 3010156, 3010162, 3010164, 3010179, 3010185, 3010195, 3010296, 3010290, 3010285, 3010282, 3010253, 3010252, 3010251, 3010191, 3010157, 3010136, 3010123, 3010186, 3010184);

importPackage(Packages.tools);

var votepoints;
var status = 0;

function start() {
	votepoints = cm.getClient().getVotePoints();

	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1)
		cm.dispose();
	else {
		if (mode == 0 && status == 0)
			cm.dispose();
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			var outStr = "Hello, you can exchange your vote points with me!\r\n\r\n";
			outStr += "#eYou currently have #r" + votepoints + "#k vote points.#n\r\n\r\n";

			outStr += "#e#L1#Exchange 1 vote point for #d10,000 NX#k#l\r\n";
			outStr += "#L2#Exchange 1 vote point for 6 #v5041000# #dVIP Teleport Rock#k#l\r\n";
			outStr += "#L4#Exchange 1 vote point for #d1 hour #v5211048# 2X EXP card#k#l\r\n";
			//outStr += "#L6#Exchange 1 vote point for #d2 days unlimited use #v5040004# #t5040004##k#l\r\n"
			outStr += "#L3#Exchange 2 vote points for a #drandom rare chair#k#l\r\n";
			outStr += "#L5#Exchange 2 vote points for #d3 hour #v5211048# 2X EXP card#k#l#n\r\n";

			cm.sendSimple(outStr);
		} else if (status == 1) {
			if (selection == 0) {
				if(votepoints > 0) {
					if(cm.canHold(4001126)){
						cm.gainItem("4001126", 2);
						cm.getClient().addVotePoints(-1);
						cm.sendOk("Here are your 2 Maple Leaf!");
						cm.logVote("2 Maple Leaf");
					} else {
						cm.sendOk("Please make sure you have enough space to hold these Maple Leaf!");
					}
				} else {
					cm.sendOk("Sorry, you don't have enough vote points!");
				}
				cm.dispose();
			} else if (selection == 1) {
				if(votepoints > 0) {
					cm.getPlayer().getCashShop().gainCash(1, 10000);
					cm.getPlayer().announce(MaplePacketCreator.earnTitleMessage("You have received 10,000 NX."));
					cm.getClient().addVotePoints(-1);
					cm.sendOk("Here is your 10,000 NX!");
					cm.logVote("10,000 NX");
				} else {
					cm.sendOk("Sorry, you don't have enough vote points!");
				}
				cm.dispose();
			} else if (selection == 2) {
				if(votepoints > 0) {
					if(cm.canHold(5041000)){
						cm.gainItem("5041000", 6);
						cm.getClient().addVotePoints(-1);
						cm.sendOk("Here are your 6 VIP Teleport Rock!");
						cm.logVote("VIP Teleport Rock");
					} else {
						cm.sendOk("Please make sure you have enough space to hold these VIP Teleprot Rock!");
					}
				} else {
					cm.sendOk("Sorry, you don't have enough vote points!");
				}
				cm.dispose();
			} else if (selection == 3) {
				if(votepoints > 1) {
					var chair1 = chairs[Math.floor(Math.random()*chairs.length)];
					if(cm.canHold(chair1)){
						cm.gainItem(chair1);
						cm.getClient().addVotePoints(-2);
						cm.sendOk("Here are your rare random chair!");
						cm.logVote("Rare Chair: " + chair1);
					} else {
						cm.sendOk("Please make sure you have enough space to hold this chair!");
					}
				} else {
					cm.sendOk("Sorry, you don't have enough vote points!");
				}
				cm.dispose();
			} else if (selection == 4) {
				if(votepoints > 0) {
					if(cm.canHold(5211048)){
						cm.gainItem(5211048, 1, false, false, 3600000);
						cm.getClient().addVotePoints(-1);
						cm.getPlayer().setRates();
						cm.sendOk("Here is your 1 hour EXP card!");
						cm.logVote("1 Hour EXP Card");
					} else {
						cm.sendOk("Please make sure you have enough space to hold this EXP card");
					}
				} else {
					cm.sendOk("Sorry, you don't have enough vote points!");
				}
				cm.dispose();
			} else if (selection == 5) {
				if(votepoints > 1) {
					if(cm.canHold(5211048)){
						cm.gainItem(5211048, 1, false, false, 10800000);
						cm.getClient().addVotePoints(-2);
						cm.getPlayer().setRates();
						cm.sendOk("Here is your 3 hour EXP card!");
						cm.logVote("3 Hour EXP Card");
					} else {
						cm.sendOk("Please make sure you have enough space to hold this EXP card");
					}
				} else {
					cm.sendOk("Sorry, you don't have enough vote points!");
				}
				cm.dispose();
			} else if(selection == 6) {
				if(votepoints > 0) {
					if(cm.canHold(5040004)){
						cm.gainItem(5040004, 1, false, false, 172800000);
						cm.getClient().addVotePoints(-1);
						cm.sendOk("Here is your 2 days unlimited use Hyper Teleport Rock!");
						cm.logVote("2 Days Hyper Teleport Rock");
					} else {
						cm.sendOk("Please make sure you have enough space to hold this EXP card");
					}
				} else {
					cm.sendOk("Sorry, you don't have enough vote points!");
				}
				cm.dispose();
			} else {
				cm.sendOk("Come back later!");
				cm.dispose();
			}
		}
	}
}