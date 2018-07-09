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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.AbstractMaplePacketHandler;
import net.server.world.World;
import tools.LogHelper;
import tools.DatabaseConnection;
import tools.FilePrinter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleCharacter;
import client.MapleClient;
import client.autoban.AutobanFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Matze
 */
public final class WhisperHandler extends AbstractMaplePacketHandler {

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        byte mode = slea.readByte();
        if (mode == 6) { // whisper
            String recipient = slea.readMapleAsciiString();
            String text = slea.readMapleAsciiString();
            MapleCharacter player = c.getChannelServer().getPlayerStorage().getCharacterByName(recipient);
            if (c.getPlayer().getAutobanManager().getLastSpam(7) + 200 > System.currentTimeMillis()) {
                return;
            }
            if (text.length() > Byte.MAX_VALUE && !player.isIntern()) {
                AutobanFactory.PACKET_EDIT.alert(c.getPlayer(), c.getPlayer().getName() + " tried to packet edit with whispers.");
                FilePrinter.printError(FilePrinter.EXPLOITS + c.getPlayer().getName() + ".txt", c.getPlayer().getName() + " tried to send text with length of " + text.length() + "\r\n");
                c.disconnect(true, false);
                return;
            }
            if (player != null) {
                if (c.getPlayer().isMuted() && !c.getPlayer().isIntern()) {
                    c.getPlayer().message("You are muted. You cannot talk when you're muted.");
                    return;
                }

                player.getClient().announce(MaplePacketCreator.getWhisper(c.getPlayer().getName(), c.getChannel(), text));

                if (c.getWorldServer().getWatching().size() > 0) {
                    boolean logWatch = false;

                    for (String watch : c.getWorldServer().getWatching()) {
                        if (watch.split("-")[0].equals(String.valueOf(c.getPlayer().getId()))) {
                            MapleCharacter watcher = c.getWorldServer().getPlayerStorage().getCharacterById(Integer.parseInt(watch.split("-")[1]));

                            if (watcher != null) {
                                watcher.dropMessage(6, "[ Type: Whisper | " + c.getPlayer().getName() + " ] : " + text);
                            }

                            logWatch = true;
                        }
                    }

                    if (logWatch) {
                        DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        FilePrinter.printError(FilePrinter.WATCH_LOGS + c.getAccountName() + "-" + c.getPlayer().getName() + ".txt", "[ " + timeStamp.format(new Date()) + " ] " + "[ Type: Whisper | " + c.getPlayer().getName() + " ] : " + text + "\r\n");
                    }
                }

                if (player.isHidden() && player.gmLevel() > c.getPlayer().gmLevel()) {
                    c.announce(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                } else {
                    c.announce(MaplePacketCreator.getWhisperReply(recipient, (byte) 1));
                }
            } else {// not found
                World world = c.getWorldServer();
                if (world.isConnected(recipient)) {
                    world.whisper(c.getPlayer().getName(), recipient, c.getChannel(), text);

                    player = world.getPlayerStorage().getCharacterByName(recipient);
                    if (player.isHidden() && player.gmLevel() > c.getPlayer().gmLevel()) {
                        c.announce(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                    } else {
                        c.announce(MaplePacketCreator.getWhisperReply(recipient, (byte) 1));
                    }
                } else {
                    c.announce(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                }
            }
            c.getPlayer().getAutobanManager().spam(7);
        } else if (mode == 5) { // - /find
            String recipient = slea.readMapleAsciiString();
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(recipient);

            if (victim != null && victim.isInvisible() && !c.getPlayer().isIntern()) {
                c.getPlayer().message("'" + victim.getName() + "' has set their status as invisible. Type @toggleinvisible if you like to be set as invisible.");
            } else if (victim != null && c.getPlayer().gmLevel() >= victim.gmLevel()) {
                if (victim.getCashShop().isOpened()) {
                    //} else if (victim.inMTS()) {
                    //    c.announce(MaplePacketCreator.getFindReply(victim.getName(), -1, 0));
                } else {
                    c.announce(MaplePacketCreator.getFindReply(victim.getName(), victim.getMap().getId(), 1));
                }
            } else { // not found
                try {
                    PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT gm FROM characters WHERE name = ?");
                    ps.setString(1, recipient);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt("gm") > c.getPlayer().gmLevel()) {
                            c.announce(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                            return;
                        }
                    }
                    rs.close();
                    ps.close();
                    byte channel = (byte) (c.getWorldServer().find(recipient) - 1);
                    if (channel > -1) {
                        c.announce(MaplePacketCreator.getFindReply(recipient, channel, 3));
                    } else {
                        c.announce(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else if (mode == 0x44) {
            //Buddy find?
        }
    }
}
