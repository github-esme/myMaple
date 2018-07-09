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
	Map(s): 		Empress' Road : Training Forest I
	Description: 		Takes you to Tiv's Forest
	*/

	function enter(pi) {
		if(pi.isQuestStarted(20301) || pi.isQuestStarted(20302) || pi.isQuestStarted(20303) || pi.isQuestStarted(20304) || pi.isQuestStarted(20305)) {
			if (pi.getPlayerCount(108010610) == 0) {
				if (pi.haveItem(4032179, 1)) {
					pi.removeNpc(108010610, 1104102);
					var map = pi.getMap(108010610);
					map.spawnNpc(1104102, new java.awt.Point(3307, 88));
					pi.warp(108010610, 0);
				} else {
					pi.message("You do not have the Erev Search Warrent to do so, please get it from Nineheart.");
				}
			} else {
				pi.message("The forest is already being searched by someone else. Better come back later.");
			}
		} else {
			pi.warp(130010020, "out00");
		}
		return true;
	}