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
/**
 * @Author Moogra
 */
function enter(pi) {
    if (pi.getPlayer().getMap().getReactorByName("door") != null && pi.getPlayer().getMap().getReactorByName("door").getState() == 1) 
    {
        if (pi.getPlayer().getMapId() >= 925020200 && pi.getClient().getChannelServer().getMapFactory().getMap(pi.getPlayer().getMapId() - 100).getCharacters().size() >= 1)
        {
            pi.getPlayer().message("You may not pass because one of your party members are left behind.")
        }
        else if ((pi.getPlayer().getMapId() / 100) % 100 != 38) 
        {
            if (pi.getPlayer().getMapId() == 925020500 || pi.getPlayer().getMapId() == 925021100 || pi.getPlayer().getMapId() == 925021700 || pi.getPlayer().getMapId() == 925022300 || pi.getPlayer().getMapId() == 925022900 || pi.getPlayer().getMapId() == 925023500)
            {
                if (pi.getPlayer().getShouldRecordingDojoTime())
                {
                    pi.getPlayer().stopDojoTime();
                    pi.getPlayer().recordDojoTime(false);
                }
            }

            if (pi.getPlayer().getMap().getCharacters().size() == 1) 
            {
                pi.resetMap(pi.getPlayer().getMapId());
            }
            
            pi.getPlayer().message("You received " + pi.getPlayer().addDojoPointsByMap(pi.getPlayer().getMapId()) + " training points. Your total training points score is now " + pi.getPlayer().getDojoPoints() + ".");

            pi.warp(pi.getPlayer().getMap().getId() + 100, 0);
        } 
        else if (pi.getPlayer().getMapId() == 925023800)
        {
            if (pi.getPlayer().getShouldRecordingDojoTime())
            {
                pi.getPlayer().stopDojoTime();
                pi.getPlayer().recordDojoTime(true);
            }

            pi.getPlayer().message("You received " + pi.getPlayer().addDojoPointsByMap(pi.getPlayer().getMapId()) + " training points. Your total training points score is now " + pi.getPlayer().getDojoPoints() + ".");
            pi.warp(925020003, 0);
        }
        return true;
    } 
    else 
    {
        pi.getPlayer().message("The door is not open yet.");
        return false;
    }
}
