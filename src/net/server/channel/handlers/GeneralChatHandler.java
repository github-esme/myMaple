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

import tools.FilePrinter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleCharacter;
import client.MapleClient;
import client.autoban.AutobanFactory;
import client.command.Commands;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class GeneralChatHandler extends net.AbstractMaplePacketHandler {

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        String s = slea.readMapleAsciiString();
        MapleCharacter chr = c.getPlayer();
        if (chr.getAutobanManager().getLastSpam(7) + 200 > System.currentTimeMillis()) {
            return;
        }
        if (s.length() > Byte.MAX_VALUE && !chr.isIntern()) {
            AutobanFactory.PACKET_EDIT.alert(c.getPlayer(), c.getPlayer().getName() + " tried to packet edit in General Chat.");
            FilePrinter.printError(FilePrinter.EXPLOITS + c.getPlayer().getName() + ".txt", c.getPlayer().getName() + " tried to send text with length of " + s.length() + "\r\n");
            c.disconnect(true, false);
            return;
        }

        char heading = s.charAt(0);
        if (heading == '/' || heading == '!' || heading == '@') {
            String[] sp = s.split(" ");
            sp[0] = sp[0].toLowerCase().substring(1);
            if (!Commands.executePlayerCommand(c, sp, heading)) {

                if (chr.isIntern()) {
                    if (!Commands.executeInternCommand(c, sp, heading)) {
                        if (chr.isGM()) {
                            if (!Commands.executeGMCommand(c, sp, heading)) {
                                if (chr.isAdmin()) {
                                    Commands.executeAdminCommand(c, sp, heading);
                                }
                            }
                        }
                    }
                    String command = "";
                    for (String used : sp) {
                        command += used + " ";
                    }

                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date date = new Date();

                    FilePrinter.printError("usedCommands.txt", "[ " + dateFormat.format(date) + " ] " + c.getPlayer().getName() + " used: " + heading + command + "\r\n");
                }

            }
        } else {
            int show = slea.readByte();
            if (chr.getMap().isMuted() && !chr.isIntern()) {
                chr.message("The map you are in is currently muted. You cannot talk when you're muted.");
                return;
            }
            if (chr.isMuted() && !chr.isIntern()) {
                chr.message("You are muted. You cannot talk when you're muted.");
                return;
            }
            if (!chr.isHidden()) {
                chr.getMap().broadcastMessage(MaplePacketCreator.getChatText(chr.getId(), s, chr.getWhiteChat(), show));

                if (c.getWorldServer().getWatching().size() > 0) {
                    boolean logWatch = false;

                    for (String watch : c.getWorldServer().getWatching()) {
                        if (watch.split("-")[0].equals(String.valueOf(c.getPlayer().getId()))) {
                            MapleCharacter watcher = c.getWorldServer().getPlayerStorage().getCharacterById(Integer.parseInt(watch.split("-")[1]));

                            if (watcher != null) {
                                watcher.dropMessage(6, "[ Type: All | " + c.getPlayer().getName() + " ] : " + s);
                            }

                            logWatch = true;
                        }
                    }

                    if (logWatch) {
                        DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        FilePrinter.printError(FilePrinter.WATCH_LOGS + c.getAccountName() + "-" + c.getPlayer().getName() + ".txt", "[ " + timeStamp.format(new Date()) + " ] " + "[ Type: All | " + c.getPlayer().getName() + " ] : " + s + "\r\n");
                    }
                }

            } else {
                chr.getMap().broadcastGMMessage(MaplePacketCreator.getChatText(chr.getId(), s, chr.getWhiteChat(), show));
            }
        }
        chr.getAutobanManager().spam(7);
    }
}
