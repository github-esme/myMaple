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
package net.server.channel.handlers;

import client.MapleCharacter;
import client.MapleClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.AbstractMaplePacketHandler;
import net.server.Server;
import tools.DatabaseConnection;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

/**
 *
 * @author Flav
 */
public class EnterCashShopHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        try {
            MapleCharacter mc = c.getPlayer();

            if (mc.getCashShop().isOpened()) {
                return;
            }

            if (mc.getEventInstance() != null) {
                mc.dropMessage(1, "You cannot enter cash shop while in an instance!");
                return;
            }

            if (mc.isTestingDPS()) {
                mc.getMap().killMonsterDPS(mc.getId());
                mc.toggleTestingDPS();
                mc.message("DPS testing has been cancelled.");
            }

            //Server.getInstance().getPlayerBuffStorage().addBuffsToStorage(mc.getId(), mc.getAllBuffs());
            mc.cancelAllBuffs(false);
            mc.cancelBuffEffects();
            mc.cancelExpirationTask();
            c.announce(MaplePacketCreator.openCashShop(c, false));
            mc.saveToDB();
            mc.getCashShop().open(true);
            mc.getMap().removePlayer(mc);
            c.getChannelServer().removePlayer(mc);

            c.announce(MaplePacketCreator.showCashInventory(c));
            c.announce(MaplePacketCreator.showGifts(mc.getCashShop().loadGifts()));
            c.announce(MaplePacketCreator.showWishList(mc, false));
            c.announce(MaplePacketCreator.showCash(mc));

            try {
                Connection con = DatabaseConnection.getConnection();

                PreparedStatement ps = con.prepareStatement("SELECT characterslots FROM accounts WHERE id = ?");
                ps.setInt(1, mc.getAccountID());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    mc.getClient().setCharacterSlots(rs.getByte("characterslots"));
                }
                rs.close();
                ps.close();
            } catch (SQLException | RuntimeException e) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
