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
package server.partyquest;

import client.MapleCharacter;

import java.util.ArrayList;
import java.util.List;

import tools.FilePrinter;
import net.server.Server;
import net.server.world.MapleParty;
import net.server.world.MaplePartyCharacter;

/**
 *
 * @author kevintjuh93
 */
public class PartyQuest {

    int channel, world;
    MapleParty party;
    List<MapleCharacter> participants = new ArrayList<>();

    public PartyQuest(MapleParty party) {
        this.party = party;
        MaplePartyCharacter leader = party.getLeader();
        channel = leader.getChannel();
        world = leader.getWorld();
        int mapid = leader.getMapId();
        for (MaplePartyCharacter pchr : party.getMembers()) {
            if (pchr.getChannel() == channel && pchr.getMapId() == mapid) {
                MapleCharacter chr = Server.getInstance().getWorld(world).getChannel(channel).getPlayerStorage().getCharacterById(pchr.getId());
                if (chr != null) {
                    this.participants.add(chr);
                }
            }
        }
    }

    public MapleParty getParty() {
        return party;
    }

    public List<MapleCharacter> getParticipants() {
        return participants;
    }

    public void removeParticipant(MapleCharacter chr) throws Throwable {
        synchronized (participants) {
            participants.remove(chr);
            chr.setPartyQuest(null);
            if (participants.isEmpty()) {
                super.finalize();
            }
            //System.gc();
        }
    }

    public static int getExp(String PQ, int level) {
        switch (PQ) {
            case "HenesysPQ":
                return 700 * level / 5;
            case "KerningPQFinal":
                return 1000 * level / 5;
            case "KerningPQ4th":
                return 600 * level / 5;
            case "KerningPQ3rd":
                return 500 * level / 5;
            case "KerningPQ2nd":
                return 400 * level / 5;
            case "KerningPQ1st":
                return 300 * level / 5;
            case "LudiMazePQ":
                if (level < 60) {
                    return 3000 * level / 5;
                } else {
                    return 3500 * level / 5;
                }
            case "LudiPQ1st":
                return 1000 * level / 5;
            case "LudiPQ2nd":
                return 1200 * level / 5;
            case "LudiPQ3rd":
                return 1400 * level / 5;
            case "LudiPQ4th":
                return 1600 * level / 5;
            case "LudiPQ5th":
                return 1800 * level / 5;
            case "LudiPQ6th":
                return 2000 * level / 5;
            case "LudiPQ7th":
                return 2200 * level / 5;
            case "LudiPQ8th":
                return 2400 * level / 5;
            case "LudiPQLast":
                return 2600 * level / 5;
            default:
                break;
        }
        FilePrinter.print(FilePrinter.NPC, "Unhandled PartyQuest: " + PQ);
        return 0;
    }
}
