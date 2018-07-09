var items = [[2022338, 100000, "+14 Weapon Attack For 15 Minutes"], [2022339, 900000, "+22 Weapon Attack For 10 Minutes"], [2022340, 3600000, "+90 Weapon Attack For 1 Minute"], [2022341, 900000, "+50 Magic Attack For 10 Minutes"], [2022342, 2500000, "+200 Magic Attack For 30 Seconds"], [2022345, 1600000, "+25 Attack, +30 Magic Attack, +30 Defense For 30 Minutes"]];
var buy = false;
var selectedItem;

function start() {
    var selStr = "So, what do you want?\r\n\r\n";
    for (var i = 0; i < items.length; i++) {
        selStr += "#L" + i + "##v" + items[i][0] + "# #t" + items[i][0] + "# - #b" + items[i][2] + "#l#k\r\n";
    }
    cm.sendSimple(selStr);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (!buy) {
        selectedItem = selection;
        cm.sendGetNumber("Are you sure you want #b#v" + items[selection][0] + "# #t" + items[selection][0] + "##k? How many would you like?\r\n\r\n" + "Each one will cost #e" + items[selection][1] + " mesos.#n", 1, 1, 200);
        buy = true;
    } else {
        if (!cm.canHold(items[selectedItem][0])) {
            cm.sendOk("Please make more space.");
            cm.dispose();
        }
        else if (cm.getPlayer().getMeso() >= (items[selectedItem][1] * selection))
        {
            cm.gainMeso(-(items[selectedItem][1] * selection));
            cm.gainItem(items[selectedItem][0], selection);
            cm.sendOk("There, all done! That was quick, wasn't it? If you need any more items, I'll be waiting here.");
            cm.dispose();
        }
        else
        {
            cm.sendOk("You don't have enough mesos!");
            cm.dispose();
        }
    }
}