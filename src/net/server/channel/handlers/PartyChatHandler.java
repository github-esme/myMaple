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
import client.autoban.AutobanFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.AbstractMaplePacketHandler;
import net.server.Server;
import net.server.world.World;
import tools.FilePrinter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public final class PartyChatHandler extends AbstractMaplePacketHandler {

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player.getAutobanManager().getLastSpam(7) + 200 > System.currentTimeMillis()) {
            return;
        }
        int type = slea.readByte(); // 0 for buddys, 1 for partys
        int numRecipients = slea.readByte();
        int recipients[] = new int[numRecipients];
        for (int i = 0; i < numRecipients; i++) {
            recipients[i] = slea.readInt();
        }
        String chattext = slea.readMapleAsciiString();
        if (chattext.length() > Byte.MAX_VALUE && !player.isIntern()) {
            AutobanFactory.PACKET_EDIT.alert(c.getPlayer(), c.getPlayer().getName() + " tried to packet edit chats.");
            FilePrinter.printError(FilePrinter.EXPLOITS + c.getPlayer().getName() + ".txt", c.getPlayer().getName() + " tried to send text with length of " + chattext.length() + "\r\n");
            c.disconnect(true, false);
            return;
        }
        World world = c.getWorldServer();

        if (c.getPlayer().isMuted() && !c.getPlayer().isIntern()) {
            c.getPlayer().message("You are muted. You cannot talk when you're muted.");
            return;
        }

        if (type == 0) {
            world.buddyChat(recipients, player.getId(), player.getName(), chattext);

            if (c.getWorldServer().getWatching().size() > 0) {
                boolean logWatch = false;

                for (String watch : c.getWorldServer().getWatching()) {
                    if (watch.split("-")[0].equals(String.valueOf(c.getPlayer().getId()))) {
                        MapleCharacter watcher = c.getWorldServer().getPlayerStorage().getCharacterById(Integer.parseInt(watch.split("-")[1]));

                        if (watcher != null) {
                            watcher.dropMessage(6, "[ Type: Buddy | " + c.getPlayer().getName() + " ] : " + chattext);
                        }

                        logWatch = true;
                    }
                }

                if (logWatch) {
                    DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    FilePrinter.printError(FilePrinter.WATCH_LOGS + c.getAccountName() + "-" + c.getPlayer().getName() + ".txt", "[ " + timeStamp.format(new Date()) + " ] " + "[ Type: Buddy | " + c.getPlayer().getName() + " ] : " + chattext + "\r\n");
                }
            }

        } else if (type == 1 && player.getParty() != null) {
            world.partyChat(player.getParty(), chattext, player.getName());

            if (c.getWorldServer().getWatching().size() > 0) {
                boolean logWatch = false;

                for (String watch : c.getWorldServer().getWatching()) {
                    if (watch.split("-")[0].equals(String.valueOf(c.getPlayer().getId()))) {
                        MapleCharacter watcher = c.getWorldServer().getPlayerStorage().getCharacterById(Integer.parseInt(watch.split("-")[1]));

                        if (watcher != null) {
                            watcher.dropMessage(6, "[ Type: Party | " + c.getPlayer().getName() + " ] : " + chattext);
                        }

                        logWatch = true;
                    }
                }

                if (logWatch) {
                    DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    FilePrinter.printError(FilePrinter.WATCH_LOGS + c.getAccountName() + "-" + c.getPlayer().getName() + ".txt", "[ " + timeStamp.format(new Date()) + " ] " + "[ Type: Party | " + c.getPlayer().getName() + " ] : " + chattext + "\r\n");
                }
            }

        } else if (type == 2 && player.getGuildId() > 0) {
            Server.getInstance().guildChat(player.getGuildId(), player.getName(), player.getId(), chattext);

            if (c.getWorldServer().getWatching().size() > 0) {
                boolean logWatch = false;

                for (String watch : c.getWorldServer().getWatching()) {
                    if (watch.split("-")[0].equals(String.valueOf(c.getPlayer().getId()))) {
                        MapleCharacter watcher = c.getWorldServer().getPlayerStorage().getCharacterById(Integer.parseInt(watch.split("-")[1]));

                        if (watcher != null) {
                            watcher.dropMessage(6, "[ Type: Guild | " + c.getPlayer().getName() + " ] : " + chattext);
                        }

                        logWatch = true;
                    }
                }

                if (logWatch) {
                    DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    FilePrinter.printError(FilePrinter.WATCH_LOGS + c.getAccountName() + "-" + c.getPlayer().getName() + ".txt", "[ " + timeStamp.format(new Date()) + " ] " + "[ Type: Guild | " + c.getPlayer().getName() + " ] : " + chattext + "\r\n");
                }
            }

        } else if (type == 3 && player.getGuild() != null) {
            int allianceId = player.getGuild().getAllianceId();
            if (allianceId > 0) {
                Server.getInstance().allianceMessage(allianceId, MaplePacketCreator.multiChat(player.getName(), chattext, 3), player.getId(), -1);

                if (c.getWorldServer().getWatching().size() > 0) {
                    boolean logWatch = false;

                    for (String watch : c.getWorldServer().getWatching()) {
                        if (watch.split("-")[0].equals(String.valueOf(c.getPlayer().getId()))) {
                            MapleCharacter watcher = c.getWorldServer().getPlayerStorage().getCharacterById(Integer.parseInt(watch.split("-")[1]));

                            if (watcher != null) {
                                watcher.dropMessage(6, "[ Type: Alliance | " + c.getPlayer().getName() + " ] : " + chattext);
                            }

                            logWatch = true;
                        }
                    }

                    if (logWatch) {
                        DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        FilePrinter.printError(FilePrinter.WATCH_LOGS + c.getAccountName() + "-" + c.getPlayer().getName() + ".txt", "[ " + timeStamp.format(new Date()) + " ] " + "[ Type: Alliance | " + c.getPlayer().getName() + " ] : " + chattext + "\r\n");
                    }
                }
            }
        }

        player.getAutobanManager().spam(7);
    }
}
