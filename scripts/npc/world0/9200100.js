/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/*
   Npc Name: Dr. Lenu
--------------------------------------
   Map Name: 100000103 - Victoria Road - Henesys Plastic Surgery
--------------------------------------
   Editor Name: Dstollie from Ragezone
*/
var status = 0;
var beauty = 0;
var colors = Array();

function start() {
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
        if (status == 0)
            cm.sendSimple("Why hello there! I'm Dr. Lenu, in charge of the cosmetic lenses here at the Henesys Plastic Surgery Shop! With #b#t5152010##k or #b#t5152013##k, you can have the kind of look you've always wanted! All you have to do is find the cosmetic lens that most fits you, then let us take care of the rest. Now, what would you like to use?\r\n#L0##bCosmetic Lenses at Henesys (Reg coupon)#k#l\r\n#L1##bCosmetic Lenses at Henesys (VIP coupon)#k#l");
        else if (status == 1) {
            if (selection == 0) {
                beauty = 1;
                if (cm.getPlayer().getGender() == 0)
                    var current = cm.getPlayer().getFace()% 100 + 20000;
                if (cm.getPlayer().getGender() == 1)
                    var current = cm.getPlayer().getFace()% 100 + 21000;
                colors = Array();
                colors = Array(current , current + 100, current + 200, current + 300, current +400, current + 500, current + 600, current + 700);
                cm.sendYesNo("If you use the regular coupon, you'll be assigned a random pair of cosmetic lenses. do you still want to use #b#t5152010##k and make the change to your eyes?");
            } else if (selection == 1) {
                beauty = 2;
                if (cm.getPlayer().getGender() == 0)
                    var current = cm.getPlayer().getFace()% 100 + 20000;
                if (cm.getPlayer().getGender() == 1)
                    var current = cm.getPlayer().getFace() % 100 + 21000;
                colors = Array();
                colors = Array(current , current + 100, current + 200, current + 300, current +400, current + 500, current + 600, current + 700);
                cm.sendStyle("With our specialized machine, you can see the results of your potential treatment in advance. What kind of lens would you like to wear? Please Choose the style of your liking.", colors);
            }
        }
        else if (status == 2){
            cm.dispose();
            if (beauty == 1){
                if (cm.haveItem(5152010) == true){
                    cm.gainItem(5152010, -1);
                    cm.setFace(colors[Math.floor(Math.random() * colors.length)]);
                    cm.sendOk("Enjoy your new and improved cosmetic lenses!");
                } else
                    cm.sendOk("I'm sorry, but I don't think you have our cosmetic lens coupon with you. We can't proceed without the coupon.");
            }
            if (beauty == 2){
                if (cm.haveItem(5152013) == true){
                    cm.gainItem(5152013, -1);
                    cm.setFace(colors[selection]);
                    cm.sendOk("Enjoy your new and improved cosmetic lenses!");
                } else
                    cm.sendOk("I'm sorry, but I don't think you have our cosmetic lens coupon with you. We can't proceed without the coupon.");
            }
        }
    }
}
