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
/* Aldol
 * 
 * @Author Alan (SharpAceX)
 */
 importPackage(Packages.server.expeditions);
 
function start() {
    cm.sendYesNo("If you leave now, you won't be able to return. Are you sure you want to leave?");
}

function action(mode, type, selection) {
	var scarga = MapleExpeditionType.SCARGA;
    var expedition = cm.getExpedition(scarga);
    if (mode < 1)
        cm.dispose();
    else 
    {
        if(cm.getPlayer().getEventInstance() != null)
        {
            if(cm.getPlayer().getEventInstance().getPlayerCount() == 1)
            {
                cm.getPlayer().getMap().killAllMonsters();
                cm.getPlayer().getMap().resetReactors();
                cm.getPlayer().getEventInstance().unregisterPlayer(cm.getPlayer());
                cm.endExpedition(expedition);

                cm.getPlayer().changeMap(551030100);
            }
            else
            {
                if (cm.getPlayer().getEventInstance().getExpedition() != null)
                {
                    cm.getPlayer().getEventInstance().getExpedition().removeMember(cm.getPlayer());
                }

                cm.getPlayer().getEventInstance().removePlayer(cm.getPlayer()); // Triggers playerExit in ScargaBattle.js
            }
        }
        else
        {
            cm.getPlayer().changeMap(551030100);
        }

        cm.dispose();
    }
}