var status = 0;
var maps = [104000000, 102000000, 100000000, 103000000, 120000000];
var cost = [1000, 1000, 1000, 1000, 800];
var selectedMap = -1;
var taxiCoupon = 4032288;
var mesos;

function start() {
    cm.sendNext("Hello, I drive the Regular Cab. If you want to go from town to town safely and fast, then ride our cab. We'll glady take you to your destination with an affordable price.");
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status == 1 && mode == 0) {
            cm.dispose();
            return;
        } else if (status >= 2 && mode == 0) {
            cm.sendNext("There's a lot to see in this town, too. Come back and find us when you need to go to a different town.");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 1) {
            var selStr = "";
            if (cm.getJobId() == 0)
                selStr += "We have a special 90% discount for beginners.";
            selStr += "Choose your destination, for fees will change from place to place.#b";

            if(cm.getPlayer().getMap().getId() == 101000000 && cm.hasItem(4032288))
            {
                for (var i = 0; i < maps.length; i++)
                {
                    var costTaxi = cost[i];
                    if(i == 2)
                    {
                        costTaxi = "Free with #v4032288#";
                        selStr += "\r\n#L" + i + "##m" + maps[i] + "# (Free with #v4032288#)#l";
                    }
                    else
                    {
                         selStr += "\r\n#L" + i + "##m" + maps[i] + "# (" + (cm.getJobId() == 0 ? costTaxi / 10 : costTaxi) + " mesos)#l";
                    }
                }

                status = 3;
            }
            else
            {
                for (var i = 0; i < maps.length; i++)
                {
                    selStr += "\r\n#L" + i + "##m" + maps[i] + "# (" + (cm.getJobId() == 0 ? cost[i] / 10 : cost[i]) + " mesos)#l";
                }
            }
            
            cm.sendSimple(selStr);
        } else if (status == 2) {
            cm.sendYesNo("You don't have anything else to do here, huh? Do you really want to go to #b#m" + maps[selection] + "##k? It'll cost you #b"+ (cm.getJobId() == 0 ? cost[selection] / 10 : cost[selection]) + " mesos#k.");
            selectedMap = selection;
        } else if (status == 3) {
            if (cm.getMeso() < (cm.getJobId() == 0 ? cost[selection] / 10 : cost[selection])) {
                cm.sendNext("You don't have enough mesos. Sorry to say this, but without them, you won't be able to ride the cab.");
                cm.dispose();
                return;
            }
            if (cm.getJobId() == 0) {
            	mesos = -cost[selectedMap] / 10;
            } else {
            	mesos = -cost[selectedMap];
            }
            cm.gainMeso(mesos);
            cm.warp(maps[selectedMap], 0);
            cm.dispose();
        } else if (status == 4) {
            if(maps[selection] == 100000000)
            {
                cm.sendYesNo("You don't have anything else to do here, huh? Do you really want to go to #b#m" + maps[selection] + "##k? It'll will be free because you have a #v4032288# #bNeinheart's Taxi Coupon#k.");
            }
            else
            {
                cm.sendYesNo("You don't have anything else to do here, huh? Do you really want to go to #b#m" + maps[selection] + "##k? It'll cost you #b"+ (cm.getJobId() == 0 ? cost[selection] / 10 : cost[selection]) + " mesos#k.");
            }
            
            selectedMap = selection;
        } else if (status == 5) {
            if(cost[selectedMap] == 100000000)
            {
                cm.gainItem(4032288, -1);
                cm.warp(maps[selectedMap], 0);
                cm.dispose();
            }
            else
            {
                if (cm.getMeso() < (cm.getJobId() == 0 ? cost[selection] / 10 : cost[selection])) {
                    cm.sendNext("You don't have enough mesos. Sorry to say this, but without them, you won't be able to ride the cab.");
                    cm.dispose();
                    return;
                }
                if (cm.getJobId() == 0) {
                    mesos = -cost[selectedMap] / 10;
                } else {
                    mesos = -cost[selectedMap];
                }
                cm.gainMeso(mesos);
                cm.warp(maps[selectedMap], 0);
                cm.dispose();
            }
        }
    }
}