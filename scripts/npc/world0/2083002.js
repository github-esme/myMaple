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
*Crystal of Roots
*@Author: Moogra
*@NPC: Crystal of Roots
*/

importPackage(Packages.server.expeditions);

function start() {
    cm.sendYesNo("If you leave now, you'll have to start over. If you have disconnected, I will warp you back into your expedition. Would you like to continue?");
}

function action(mode, type, selection) {
    var horntail = MapleExpeditionType.HORNTAIL;
    var expedition = cm.getExpedition(horntail);
    var playerMapId = cm.getPlayer().getMapId();

    if (mode < 1)
        cm.dispose();
    else 
    {
        if(playerMapId == 240050500)
        {
            if(expedition == null || !expedition.contains(cm.getPlayer()))
            {
                cm.warp(240040700);
            }
            else
            {
                cm.getEventManager("HorntailFight").getInstance("HorntailFight_" + cm.getPlayer().getClient().getChannel()).registerPlayer(cm.getPlayer());

                cm.dispose();
            }
        }
        else if (playerMapId == 240060000 || playerMapId == 240060100 || playerMapId == 240060200)
        {
            if (cm.getPlayer().getEventInstance() != null)
            {
                if(cm.getPlayer().getEventInstance().getPlayerCount() == 1)
                {
                    cm.getPlayer().getMap().killAllMonsters();
                    cm.getPlayer().getMap().resetReactors();
                    cm.getPlayer().getEventInstance().unregisterPlayer(cm.getPlayer());
                    cm.endExpedition(expedition);
    
                    cm.getPlayer().changeMap(240040700);
                }
                else
                {
                    if (cm.getPlayer().getEventInstance().getExpedition() != null)
                    {
                        cm.getPlayer().getEventInstance().getExpedition().removeMember(cm.getPlayer());
                    }
                    
                    cm.getPlayer().getEventInstance().removePlayer(cm.getPlayer()); // Triggers playerExit in HorntailFight.js
    
                    cm.getPlayer().changeMap(240040700);
                }
            }
            else
            {
                cm.getPlayer().changeMap(240040700);
            }
        }
        else
        {
            cm.warp(240040700);
        }

        cm.dispose();
    }
}