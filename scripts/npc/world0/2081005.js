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
//Fixed by Moogra
//Fixed grammar, javascript syntax
var status = 0;

function start() {
    if (cm.getMapId() == 240060200)
    {
        status = -1;
        cm.sendYesNo("Would you like to spawn Horntail?");
    }
    else
    {
        cm.sendYesNo("Welcome to Cave of Life - Entrance ! Would you like to go inside and fight #rHorntail#k?");
    }
}

function action(mode, type, selection) {
    if (mode < 1)
    {
        cm.dispose();
        return;
    }
    else
        status++;

    if (status == 0) 
    {
        var eim = pi.getPlayer().getEventInstance();

        if (eim != null && eim.getProperty("horntailSpawned") == "no")
        {
            eim.setProperty("horntailSpawned", "yes");
            
            cm.spawnMonster(8810026, 1, 76, 260);
            cm.removeNpc(2081005);
            cm.dispose();
            return;
        }
    }
    else if (status == 1) {
        if (cm.getLevel() > 119)
        {
            cm.warp(240050000, 0);
        }
        else
        {
            cm.sendOk("I'm sorry. You need to be at least level 100 or above to enter.")
        }
        cm.dispose();
        return;
    }
}