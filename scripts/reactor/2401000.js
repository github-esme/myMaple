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
/* @Author Aexr
 * 2401000.js: Horntail's Cave - Summons Horntail.
*/

function act() {
    //var eim = rm.getPlayer().getEventInstance();
    rm.mapMessage(5, "Please click the NPC to spawn Horntail.");

    // if (eim != null && eim.getProperty("horntailSpawned") == "no")
    // {
    //     eim.setProperty("horntailSpawned", "yes");
    //     rm.changeMusic("Bgm14/HonTale");
    //     rm.mapMessage(6, "From the depths of his cave, here comes Horntail!");
    //     rm.spawnMonster(8810026, 1, 76, 260);
    // }
    // else if (eim == null)
    // {
    //     rm.changeMusic("Bgm14/HonTale");
    //     rm.mapMessage(6, "From the depths of his cave, here comes Horntail!");
    //     rm.spawnMonster(8810026, 1, 76, 260);
    // }
} 

function touch() {
}
function untouch() {
} 