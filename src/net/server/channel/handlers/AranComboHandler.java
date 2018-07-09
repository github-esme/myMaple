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
package net.server.channel.handlers;

import client.MapleCharacter;
import client.MapleClient;
import client.SkillFactory;
import constants.GameConstants;
import constants.skills.Aran;
import net.AbstractMaplePacketHandler;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.data.input.SeekableLittleEndianAccessor;

public class AranComboHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        final MapleCharacter player = c.getPlayer();
        int skillLevel = player.getSkillLevel(SkillFactory.getSkill(Aran.COMBO_ABILITY));
        if (GameConstants.isAran(player.getJob().getId()) && (skillLevel > 0 || player.getJob().getId() == 2000)) {
            final long currentTime = System.currentTimeMillis();
            short combo = player.getCombo();
            if ((currentTime - player.getLastCombo()) > 3000 && combo > 0) {
                combo = 0;
            }
            combo++;
            switch (combo) {
                case 10:
                case 20:
                case 30:
                case 40:
                case 50:
                case 60:
                case 70:
                case 80:
                case 90:
                case 100:
                    if (player.getJob().getId() != 2000 && (combo / 10) > skillLevel) {
                        break;
                    }
                    SkillFactory.getSkill(Aran.COMBO_ABILITY).getEffect(combo / 10).applyComboBuff(player, combo);
                    break;
            }

            // Combo medal achievements
            switch (combo) {
                case 50:
                    if (player.getCustomQuestStatus(0) == 0) {
                        if (player.getItemQuantity(1142134, true) == 0 && player.getItemQuantity(1142135, true) == 0 && player.getItemQuantity(1142136, true) == 0) {
                            if (player.getInventory(MapleItemInformationProvider.getInstance().getInventoryType(1142134)).getNextFreeSlot() > -1) {
                                MapleInventoryManipulator.addById(player.getClient(), 1142134, (short) 1);
                                player.message("Congrats! You have earned a Combo Mania medal for hitting 50 combos.");
                            } else {
                                player.message("Please make room in your EQUIP inventory to receive your Combo Mania medal for achieving 50 combos!");
                            }
                        }
                    }
                    break;
                case 200:
                    if (player.getCustomQuestStatus(0) == 0) {
                        if (player.getItemQuantity(1142135, true) == 0 && player.getItemQuantity(1142136, true) == 0) {
                            if (player.getInventory(MapleItemInformationProvider.getInstance().getInventoryType(1142135)).getNextFreeSlot() > -1) {
                                MapleInventoryManipulator.addById(player.getClient(), 1142135, (short) 1);
                                player.message("Congrats! You have earned a Combo Master medal for hitting 200 combos.");
                            } else {
                                player.message("Please make room in your EQUIP inventory to receive your Combo Master medal for achieving 200 combos!");
                            }
                        }
                    }
                    break;
                case 500:
                    if (player.getCustomQuestStatus(0) == 0) {
                        if (player.getItemQuantity(1142136, true) == 0) {
                            if (player.getInventory(MapleItemInformationProvider.getInstance().getInventoryType(1142136)).getNextFreeSlot() > -1) {
                                MapleInventoryManipulator.addById(player.getClient(), 1142136, (short) 1);
                                player.completeCustomQuest(0);
                                player.message("Congrats! You have earned a Combo King medal for hitting 500 combos.");
                            } else {
                                player.message("Please make room in your EQUIP inventory to receive your Combo King medal for achieving 500 combos!");
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

            player.setCombo(combo);
            player.setLastCombo(currentTime);
        }
    }
}
