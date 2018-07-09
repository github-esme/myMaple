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
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/*
   Npc: Ms. Tan
--------------------------------------
   Map: 100000105 - Victoria Road - Henesys Skin-Care
--------------------------------------
   Editor: Dstollie from Ragezone
*/
var status = 0;
var skin = Array(0, 1, 2, 3, 4);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendNext("Well, hello! Welcome to the Henesys Skin-Care! Would you like to have a firm, tight, healthy looking skin like mine?  With a #b#t5153000##k, you can let us help you to have the kind of skin you've always wanted!");
		} else if (status == 1) {
			cm.sendStyle("With our specialized machine, you can see your expected results after treatment in advance. What kind of skin-treatmetn would you like to do? Choose your preferred style.", skin);
		} else if (status == 2) {
			cm.dispose();
			if (cm.haveItem(5153000) == true){
				cm.gainItem(5153000, -1);
				cm.setSkin(skin[selection]);
				cm.sendOk("Enjoy your new and improved skin!");
			} else {
				cm.sendOk("Um...you don't have the skin-care coupon you need to receive the treatment. Sorry, but I am afraid we can't do it for you.");
			}
		}
	}
}