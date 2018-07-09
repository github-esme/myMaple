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
package scripting.npc;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.server.Server;
import net.server.guild.MapleAlliance;
import net.server.guild.MapleGuild;
import net.server.world.MapleParty;
import net.server.world.MaplePartyCharacter;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import scripting.AbstractPlayerInteraction;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.events.gm.MapleEvent;
import server.gachapon.MapleGachapon;
import server.gachapon.MapleGachapon.MapleGachaponItem;
import server.maps.MapleMap;
import server.maps.MapleMapFactory;
import server.partyquest.Pyramid;
import server.partyquest.Pyramid.PyramidMode;
import server.quest.MapleQuest;
import tools.LogHelper;
import tools.DatabaseConnection;
import tools.FilePrinter;
import tools.MaplePacketCreator;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleJob;
import client.MapleSkinColor;
import client.MapleStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.ItemFactory;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import constants.ExpTable;
import constants.ItemConstants;
import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.server.channel.Channel;
import provider.MapleDataProvider;
import provider.MapleDataTool;
import server.MapleInventoryManipulator;
import server.TimerManager;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMapObject;
import server.partyquest.MonsterCarnival;
import tools.StringUtil;

/**
 *
 * @author Matze
 */
public class NPCConversationManager extends AbstractPlayerInteraction {

    private int npc;
    private String scriptName;
    private String getText;
    private List<MaplePartyCharacter> otherParty;

    public NPCConversationManager(MapleClient c, int npc, String scriptName) {
        super(c);
        this.npc = npc;
        this.scriptName = scriptName;
    }

    public NPCConversationManager(MapleClient c, int npc, List<MaplePartyCharacter> otherParty, int b) {
        super(c);
        this.c = c;
        this.npc = npc;
        this.otherParty = otherParty;
    }

    public int getNpc() {
        return npc;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void dispose() {
        NPCScriptManager.getInstance().dispose(this);
    }

    public void sendNext(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", (byte) 0));
    }

    public void sendPrev(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", (byte) 0));
    }

    public void sendNextPrev(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", (byte) 0));
    }

    public void sendOk(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", (byte) 0));
    }

    public void sendYesNo(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", (byte) 0));
    }

    public void sendAcceptDecline(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", (byte) 0));
    }

    public void sendSimple(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) 0));
    }

    public void sendNext(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", speaker));
    }

    public void sendPrev(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", speaker));
    }

    public void sendNextPrev(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", speaker));
    }

    public void sendOk(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", speaker));
    }

    public void sendYesNo(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", speaker));
    }

    public void sendAcceptDecline(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", speaker));
    }

    public void sendSimple(String text, byte speaker) {
        getClient().announce(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", speaker));
    }

    public void sendStyle(String text, int styles[]) {
        getClient().announce(MaplePacketCreator.getNPCTalkStyle(npc, text, styles));
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        getClient().announce(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
    }

    public void sendGetText(String text) {
        getClient().announce(MaplePacketCreator.getNPCTalkText(npc, text, ""));
    }

    /*
	 * 0 = ariant colliseum
	 * 1 = Dojo
	 * 2 = Carnival 1
	 * 3 = Carnival 2
	 * 4 = Ghost Ship PQ?
	 * 5 = Pyramid PQ
	 * 6 = Kerning Subway
     */
    public void sendDimensionalMirror(String text) {
        getClient().announce(MaplePacketCreator.getDimensionalMirror(text));
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return this.getText;
    }

    public int getJobId() {
        return getPlayer().getJob().getId();
    }

    public MapleJob getJob() {
        return getPlayer().getJob();
    }

    public MapleCharacter getChrById(int id) {
        Channel cs = c.getChannelServer();
        return cs.getPlayerStorage().getCharacterById(id);
    }

    public void startCPQ(final MapleCharacter challenger, int field) {
        try {
            if (challenger != null) {
                if (challenger.getParty() == null) {
                    throw new RuntimeException("Challenger's party was null!");
                }
                for (MaplePartyCharacter mpc : challenger.getParty().getMembers()) {
                    MapleCharacter mc;
                    mc = c.getChannelServer().getPlayerStorage().getCharacterByName(mpc.getName());
                    if (mc != null) {
                        mc.changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().getPortal(0));
                        mc.getClient().getSession().write(MaplePacketCreator.getClock(10));
                    }
                }
            }
            final int mapid = c.getPlayer().getMap().getId() + 1;
            TimerManager.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    MapleMap map;
                    Channel cs = c.getChannelServer();
                    map = cs.getMapFactory().getMap(mapid);

                    new MonsterCarnival(getPlayer().getParty(), challenger.getParty(), mapid);
                    map.broadcastMessage(MaplePacketCreator.serverNotice(5, "Monster Carnival has begun!"));
                }
            }, 10000);
            mapMessage(5, "Monster Carnival will begin in 10 seconds!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setAsFishingChair(int itemID) {
        MapleInventoryManipulator.addById(c, itemID, (short) 1, "Fishing", -1, -1);
    }

    public String getReadableMillis(long startMillis, long endMillis) {
        return StringUtil.getReadableMillis(startMillis, endMillis);
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public void donatorItem(int itemID) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Item item = ii.getEquipById(itemID);

        Equip equip = (Equip) item;

        equip.setStr((short) 0);
        equip.setDex((short) 0);
        equip.setInt((short) 0);
        equip.setLuk((short) 0);
        equip.setMatk((short) 0);
        equip.setWatk((short) 0);
        equip.setAcc((short) 0);
        equip.setAvoid((short) 0);
        equip.setJump((short) 0);
        equip.setSpeed((short) 0);
        equip.setWdef((short) 0);
        equip.setMdef((short) 0);
        equip.setHp((short) 0);
        equip.setMp((short) 0);
        equip.setUpgradeSlots(0);

        equip.setFlag((byte) ItemConstants.UNTRADEABLE); // <-- makes item untradeable

        MapleInventoryManipulator.addFromDrop(getClient(), equip, false);
    }

    public void fishingItem(int itemID) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Item item = ii.getEquipById(itemID);

        Equip equip = (Equip) item;

        equip.setStr((short) 0);
        equip.setDex((short) 0);
        equip.setInt((short) 0);
        equip.setLuk((short) 0);
        equip.setMatk((short) 0);
        equip.setWatk((short) 0);
        equip.setAcc((short) 0);
        equip.setAvoid((short) 0);
        equip.setJump((short) 0);
        equip.setSpeed((short) 0);
        equip.setWdef((short) 0);
        equip.setMdef((short) 0);
        equip.setHp((short) 0);
        equip.setMp((short) 0);
        equip.setUpgradeSlots(0);

        MapleInventoryManipulator.addFromDrop(getClient(), equip, false);
    }

    public void transmogWithoutStats(int itemID) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Item item = ii.getEquipById(itemID);
        item.setOwner("Transmog");

        Equip equip = (Equip) item;

        equip.setStr((short) 0);
        equip.setDex((short) 0);
        equip.setInt((short) 0);
        equip.setLuk((short) 0);
        equip.setMatk((short) 0);
        equip.setWatk((short) 0);
        equip.setAcc((short) 0);
        equip.setAvoid((short) 0);
        equip.setJump((short) 0);
        equip.setSpeed((short) 0);
        equip.setWdef((short) 0);
        equip.setMdef((short) 0);
        equip.setHp((short) 0);
        equip.setMp((short) 0);
        equip.setUpgradeSlots(0);
        equip.setOwner("Transmog");

        MapleInventoryManipulator.addFromDrop(getClient(), equip, false);
    }

    public void transmogItemStats(Equip selectedEquip, int type) {

        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Item item = ii.getEquipById(selectedEquip.getItemId());
        item.setOwner("Transmog");

        int stats = 0;

        switch (type) {
            case 1:
                stats = (int) ((int) selectedEquip.getStr() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 2:
                stats = (int) ((int) selectedEquip.getDex() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 3:
                stats = (int) ((int) selectedEquip.getInt() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 4:
                stats = (int) ((int) selectedEquip.getLuk() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 5:
                stats = (int) ((int) selectedEquip.getMatk() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 6:
                stats = (int) ((int) selectedEquip.getWatk() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 7:
                stats = (int) ((int) selectedEquip.getAcc() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 8:
                stats = (int) ((int) selectedEquip.getAvoid() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 9:
                stats = (int) ((int) selectedEquip.getJump() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 10:
                stats = (int) ((int) selectedEquip.getSpeed() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 11:
                stats = (int) ((int) selectedEquip.getWdef() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 12:
                stats = (int) ((int) selectedEquip.getMdef() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 13:
                stats = (int) ((int) selectedEquip.getHp() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 14:
                stats = (int) ((int) selectedEquip.getMp() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
            case 15:
                stats = (int) ((int) selectedEquip.getUpgradeSlots() * ((float) getPlayer().getCraftingLevel() * 5 / 100.0f));
                break;
        }

        MapleInventoryManipulator.addFromDrop(getClient(), ii.setTransmogStats((Equip) item, (short) stats, type), false);
    }

    public void transferTransmogStats(Equip selectedEquip, int newItemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Item item = ii.getEquipById(newItemId);
        item.setOwner("Transmog");

        Equip newEquip = (Equip) item;
        newEquip.setOwner("Transmog");
        newEquip.setStr(selectedEquip.getStr());
        newEquip.setDex(selectedEquip.getDex());
        newEquip.setInt(selectedEquip.getInt());
        newEquip.setLuk(selectedEquip.getLuk());
        newEquip.setMatk(selectedEquip.getMatk());
        newEquip.setWatk(selectedEquip.getWatk());
        newEquip.setAcc(selectedEquip.getAcc());
        newEquip.setAvoid(selectedEquip.getAvoid());
        newEquip.setJump(selectedEquip.getJump());
        newEquip.setSpeed(selectedEquip.getSpeed());
        newEquip.setWdef(selectedEquip.getWdef());
        newEquip.setMdef(selectedEquip.getMdef());
        newEquip.setHp(selectedEquip.getHp());
        newEquip.setMp(selectedEquip.getMp());
        newEquip.setUpgradeSlots(selectedEquip.getUpgradeSlots());

        newEquip.setFlag((byte) ItemConstants.UNTRADEABLE);

        MapleInventoryManipulator.addFromDrop(getClient(), newEquip, false);
    }

    public boolean isAccessory(int itemId) {
        return (itemId >= 1010000 && itemId < 1040000) || (itemId >= 1122000 && itemId < 1123000) || (itemId >= 1142000 && itemId < 1143000);
    }

    public boolean isMedal(int itemId) {
        return itemId >= 1142000 && itemId < 1143000;
    }

    public boolean isCap(int itemId) {
        return itemId >= 1000000 && itemId < 1010000;
    }

    public boolean isCape(int itemId) {
        return itemId >= 1102000 && itemId < 1103000;
    }

    public boolean isTop(int itemId) {
        return itemId >= 1040000 && itemId < 1050000;
    }

    public boolean isGlove(int itemId) {
        return itemId >= 1080000 && itemId < 1090000;
    }

    public boolean isOverall(int itemId) {
        return itemId >= 1050000 && itemId < 1060000;
    }

    public boolean isPants(int itemId) {
        return itemId >= 1060000 && itemId < 1070000;
    }

    public boolean isRing(int itemId) {
        return itemId >= 1112000 && itemId < 1120000;
    }

    public boolean isShield(int itemId) {
        return itemId >= 1092000 && itemId < 1100000;
    }

    public boolean isShoes(int itemId) {
        return itemId >= 1070000 && itemId < 1080000;
    }

    public boolean isWeapon(int itemId) {
        return itemId >= 1300000 && itemId < 1800000;
    }

    public String returnAsDate(long timestamp) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return dateFormat.format(new Date(timestamp));
    }

    public boolean fieldTaken(int field) {
        if (c.getChannelServer().getMapFactory().getMap(980000100 + field * 100).getAllPlayer().size() != 0) {
            return true;
        }
        if (c.getChannelServer().getMapFactory().getMap(980000101 + field * 100).getAllPlayer().size() != 0) {
            return true;
        }
        if (c.getChannelServer().getMapFactory().getMap(980000102 + field * 100).getAllPlayer().size() != 0) {
            return true;
        }
        return false;
    }

    public boolean fieldLobbied(int field) {
        if (c.getChannelServer().getMapFactory().getMap(980000100 + field * 100).getAllPlayer().size() != 0) {
            return true;
        }
        return false;
    }

    public void cpqLobby(int field) {
        try {
            MapleMap map;
            Channel cs = c.getChannelServer();
            map = cs.getMapFactory().getMap(980000100 + 100 * field);
            for (MaplePartyCharacter mpc : c.getPlayer().getParty().getMembers()) {
                MapleCharacter mc;
                mc = cs.getPlayerStorage().getCharacterByName(mpc.getName());
                if (mc != null) {
                    mc.changeMap(map, map.getPortal(0));
                    mc.getClient().getSession().write(MaplePacketCreator.serverNotice(5,
                            "You will now recieve challenges from other parties. If you do not accept a challenge in 3 minutes, you will be kicked out."));
                    mc.getClient().getSession().write(MaplePacketCreator.getClock(3 * 60));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean addCapacityToAlliance(int cost) {
        try {
            final MapleGuild gs = getClient().getPlayer().getGuild();
            if (gs != null && getClient().getPlayer().getGuildRank() == 1 && getClient().getPlayer().getAllianceRank() == 1) {
                if (getClient().getPlayer().getMGC().getAllianceRank() == 1 && Server.getInstance().increaseAllianceCapacity(gs.getAllianceId())) {
                    gainMeso(-cost);
                    return true;
                }
            }
        } catch (Exception re) {
        }
        return false;
    }

    public int calcAvgLvl(int map) {
        int num = 0;
        int avg = 0;
        for (MapleMapObject mmo
                : c.getChannelServer().getMapFactory().getMap(map).getAllPlayer()) {
            avg += ((MapleCharacter) mmo).getLevel();
            num++;
        }
        avg /= num;
        return avg;
    }

    public void sendCPQMapLists() {
        String msg = "Pick a field:\\r\\n";
        for (int i = 0; i < 6; i++) {
            if (fieldTaken(i)) {
                if (fieldLobbied(i)) {
                    msg += "#b#L" + i + "#Monster Carnival Field " + (i + 1) + " Avg Lvl: "
                            + calcAvgLvl(980000100 + i * 100) + "#l\\r\\n";
                } else {
                    continue;
                }
            } else {
                msg += "#b#L" + i + "#Monster Carnival Field " + (i + 1) + "#l\\r\\n";
            }
        }
        sendSimple(msg);
    }

    public int calculateCPQRanking() {
        if (c.getPlayer().getMap().isCPQWinnerMap()) {
            if (c.getPlayer().getTotalCP() > 250) {
                return 1; // Rank [A] Ganhador
            } else if (c.getPlayer().getTotalCP() > 100) {
                return 2; // Rank [B] Ganhador
            } else if (c.getPlayer().getTotalCP() > 50) {
                return 3; // Rank [C] Ganhador
            } else if (c.getPlayer().getTotalCP() < 50) {
                return 4; // Rank [D] Ganhador
            } else if (c.getPlayer().getTotalCP() == 0) {
                return 4; // Rank [D] Ganhador
            }
        } else if (c.getPlayer().getMap().isCPQLoserMap()) {
            if (c.getPlayer().getTotalCP() > 250) {
                return 10; // Rank [A] Perdedor
            } else if (c.getPlayer().getTotalCP() > 100) {
                return 20; // Rank [B] Perdedor
            } else if (c.getPlayer().getTotalCP() > 50) {
                return 30; // Rank [C] Perdedor
            } else if (c.getPlayer().getTotalCP() < 50) {
                return 40; // Rank [D] Perdedor
            } else if (c.getPlayer().getTotalCP() == 0) {
                return 40; // Rank [D] Perdedor
            }
        }
        return 999; //
    }

    public void challengeParty(int field) {
        MapleCharacter leader = null;
        MapleMap map = c.getChannelServer().getMapFactory().getMap(980000100 + 100 * field);
        for (MapleMapObject mmo : map.getAllPlayer()) {
            MapleCharacter mc = (MapleCharacter) mmo;
            if (mc.getParty().getLeader().getId() == mc.getId()) {
                leader = mc;
                break;
            }
        }
        if (leader != null) {
            if (!leader.isChallenged()) {
                List<MaplePartyCharacter> party = new LinkedList<>();
                for (MaplePartyCharacter player : c.getPlayer().getParty().getMembers()) {
                    party.add(player);
                }
                NPCScriptManager.getInstance().start("cpqchallenge", leader.getClient(), npc, party);
            } else {
                sendOk("The other party is currently taking on a different challenge.");
            }
        } else {
            sendOk("Could not find leader!");
        }
    }

    public void startQuest(short id) {
        try {
            MapleQuest.getInstance(id).forceStart(getPlayer(), npc);
        } catch (NullPointerException ex) {
        }
    }

    public void completeQuest(short id) {
        try {
            MapleQuest.getInstance(id).forceComplete(getPlayer(), npc);
        } catch (NullPointerException ex) {
        }
    }

    public void startQuest(int id) {
        try {
            MapleQuest.getInstance(id).forceStart(getPlayer(), npc);
        } catch (NullPointerException ex) {
        }
    }

    public void completeQuest(int id) {
        try {
            MapleQuest.getInstance(id).forceComplete(getPlayer(), npc);
        } catch (NullPointerException ex) {
        }
    }

    public void startCustomQuest(int questID) {
        getPlayer().startCustomQuest(questID);
    }

    public void completeCustomQuest(int questID) {
        getPlayer().completeCustomQuest(questID);
    }

    public void getCustomQuestStatus(int questID) {
        getPlayer().getCustomQuestStatus(questID);
    }

    public void deleteCustomQuest(int questID) {
        getPlayer().deleteCustomQuest(questID);
    }

    public int getMeso() {
        return getPlayer().getMeso();
    }

    public int getVotePoints() {
        return getPlayer().getClient().getVotePoints();
    }

    public int getDonatorPoints() {
        return getPlayer().getClient().getDonatorPoints();
    }

    public int getStreamerPoints() {
        return getPlayer().getStreamerPoints();
    }

    public boolean isStreamer() {
        return getPlayer().isStreamer();
    }

    public void changeMusicEveryone(String song, MapleCharacter player) {
        for (Channel ch : Server.getInstance().getChannelsFromWorld(player.getWorld())) {
            for (MapleCharacter chars : ch.getPlayerStorage().getAllCharacters()) {
                chars.getMap().broadcastMessage(MaplePacketCreator.musicChange(song));
            }
        }
    }

    public void gainMeso(int gain) {
        getPlayer().gainMeso(gain, true, false, true);
    }

    public void gainExp(int gain) {
        getPlayer().gainExp(gain, true, true);
    }

    public int getLevel() {
        return getPlayer().getLevel();
    }

    public MapleCharacter getChar() {
        return c.getPlayer();
    }

    public void showEffect(String effect) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(effect, 3));
    }

    public void setHair(int hair) {
        getPlayer().setHair(hair);
        getPlayer().updateSingleStat(MapleStat.HAIR, hair);
        getPlayer().equipChanged();
    }

    public void setFace(int face) {
        getPlayer().setFace(face);
        getPlayer().updateSingleStat(MapleStat.FACE, face);
        getPlayer().equipChanged();
    }

    public void setSkin(int color) {
        getPlayer().setSkinColor(MapleSkinColor.getById(color));
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    public List<Integer> EquipSlotList(MapleClient c) {
        List<Integer> slotList = new ArrayList<>();
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        for (Item item : equip.list()) {
            slotList.add((int) item.getPosition());
        }

        return slotList;
    }

    public String EquipList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<String> stra = new LinkedList<String>();
        for (Item item : equip.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String UseList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory use = c.getPlayer().getInventory(MapleInventoryType.USE);
        List<String> stra = new LinkedList<String>();
        for (Item item : use.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String CashList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory cash = c.getPlayer().getInventory(MapleInventoryType.CASH);
        List<String> stra = new LinkedList<String>();
        for (Item item : cash.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String ETCList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory etc = c.getPlayer().getInventory(MapleInventoryType.ETC);
        List<String> stra = new LinkedList<String>();
        for (Item item : etc.list()) {

            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String SetupList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory setup = c.getPlayer().getInventory(MapleInventoryType.SETUP);
        List<String> stra = new LinkedList<String>();
        for (Item item : setup.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public int itemQuantity(int itemid) {
        return getPlayer().getInventory(MapleItemInformationProvider.getInstance().getInventoryType(itemid)).countById(itemid);
    }

    public void displayGuildRanks() {
        MapleGuild.displayGuildRanks(getClient(), npc);
    }

    @Override
    public MapleParty getParty() {
        return getPlayer().getParty();
    }

    public void gainCloseness(int closeness) {
        for (MaplePet pet : getPlayer().getPets()) {
            if (pet.getCloseness() > 30000) {
                pet.setCloseness(30000);
                return;
            }
            pet.gainCloseness(closeness);
            while (pet.getCloseness() > ExpTable.getClosenessNeededForLevel(pet.getLevel())) {
                pet.setLevel((byte) (pet.getLevel() + 1));
                byte index = getPlayer().getPetIndex(pet);
                getClient().announce(MaplePacketCreator.showOwnPetLevelUp(index));
                getPlayer().getMap().broadcastMessage(getPlayer(), MaplePacketCreator.showPetLevelUp(getPlayer(), index));
            }
            Item petz = getPlayer().getInventory(MapleInventoryType.CASH).getItem(pet.getPosition());
            getPlayer().forceUpdateItem(petz);
        }
    }

    public void gainPetItem(int itemID, int days) {
        java.util.Calendar now = java.util.Calendar.getInstance();
        now.add(java.util.Calendar.DATE, days);

        int petid = -1;
        if (constants.ItemConstants.isPet(itemID)) {
            petid = MaplePet.createPet(itemID);
        }

        MapleInventoryManipulator.addById(c, itemID, (short) 1, null, petid, now.getTimeInMillis());
    }

    public String getName() {
        return getPlayer().getName();
    }

    public int getGender() {
        return getPlayer().getGender();
    }

    public MapleCharacter getCharByName(String name) {
        try {
            return c.getWorldServer().getPlayerStorage().getCharacterByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    public void changeJobById(int a) {
        getPlayer().changeJob(MapleJob.getById(a));
    }

    public void changeJob(MapleJob job) {
        getPlayer().changeJob(job);
    }

    public MapleJob getJobName(int id) {
        return MapleJob.getById(id);
    }

    public MapleStatEffect getItemEffect(int itemId) {
        return MapleItemInformationProvider.getInstance().getItemEffect(itemId);
    }

    public void resetStats() {
        getPlayer().resetStats();
    }

    public void maxMastery() {
        for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
            try {
                Skill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                getPlayer().changeSkillLevel(skill, (byte) 0, skill.getMaxLevel(), -1);
            } catch (NumberFormatException nfe) {
                break;
            } catch (NullPointerException npe) {
                continue;
            }
        }
    }

    public void summonMobAtPosition(int mobid, int amount, int posx, int posy) {
        if (amount <= 1) {
            MapleMonster npcmob = MapleLifeFactory.getMonster(mobid);
            npcmob.setHp(npcmob.getMaxHp());
            getPlayer().getMap().spawnMonsterOnGroudBelow(npcmob, new Point(posx, posy));
        } else {
            for (int i = 0; i < amount; i++) {
                MapleMonster npcmob = MapleLifeFactory.getMonster(mobid);
                npcmob.setHp(npcmob.getMaxHp());
                getPlayer().getMap().spawnMonsterOnGroudBelow(npcmob, new Point(posx, posy));
            }
        }
    }

    public void spawnMobOnDiffMap(int mapid, int mobid, int amount, int xpos, int ypos) {
        MapleMap target = c.getChannelServer().getMapFactory().getMap(mapid);
        for (int x = 0; x < amount; x++) {
            target.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobid), new Point(xpos, ypos));
        }
    }

    public void doGachapon() {
        int[] maps = {100000000, 101000000, 102000000, 103000000, 105040300, 800000000, 809000101, 809000201, 600000000, 120000000};

        MapleGachaponItem item = MapleGachapon.getInstance().process(npc);

        Item itemGained = gainItem(item.getId(), (short) (item.getId() / 10000 == 200 ? 100 : 1), true, true); // For normal potions, make it give 100.

        sendOk("You have obtained a #b#t" + item.getId() + "##k.");

        String map = c.getChannelServer().getMapFactory().getMap(maps[(getNpc() != 9100117 && getNpc() != 9100109) ? (getNpc() - 9100100) : getNpc() == 9100109 ? 8 : 9]).getMapName();

        LogHelper.logGacha(getPlayer(), item.getId(), map);

        if (item.getTier() > 0) { //Uncommon and Rare
            Server.getInstance().broadcastMessage(MaplePacketCreator.gachaponMessage(itemGained, map, getPlayer()));
        }
    }

    public void disbandAlliance(MapleClient c, int allianceId) {
        PreparedStatement ps = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM `alliance` WHERE id = ?");
            ps.setInt(1, allianceId);
            ps.executeUpdate();
            ps.close();
            Server.getInstance().allianceMessage(c.getPlayer().getGuild().getAllianceId(), MaplePacketCreator.disbandAlliance(allianceId), -1, -1);
            Server.getInstance().disbandAlliance(allianceId);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public boolean canBeUsedAllianceName(String name) {
        if (name.contains(" ") || name.length() > 12) {
            return false;
        }
        try {
            ResultSet rs;
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT name FROM alliance WHERE name = ?")) {
                ps.setString(1, name);
                rs = ps.executeQuery();
                if (rs.next()) {
                    ps.close();
                    rs.close();
                    return false;
                }
            }
            rs.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static MapleAlliance createAlliance(MapleCharacter chr1, MapleCharacter chr2, String name) {
        int id;
        int guild1 = chr1.getGuildId();
        int guild2 = chr2.getGuildId();
        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO `alliance` (`name`, `guild1`, `guild2`) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setInt(2, guild1);
                ps.setInt(3, guild2);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        MapleAlliance alliance = new MapleAlliance(name, id, guild1, guild2);
        try {
            Server.getInstance().setGuildAllianceId(guild1, id);
            Server.getInstance().setGuildAllianceId(guild2, id);
            chr1.setAllianceRank(1);
            chr1.saveGuildStatus();
            chr2.setAllianceRank(2);
            chr2.saveGuildStatus();
            Server.getInstance().addAlliance(id, alliance);
            Server.getInstance().allianceMessage(id, MaplePacketCreator.makeNewAlliance(alliance, chr1.getClient()), -1, -1);
        } catch (Exception e) {
            return null;
        }
        return alliance;
    }

    public boolean hasMerchant() {
        return getPlayer().hasMerchant();
    }

    public boolean hasMerchantItems() {
        try {
            if (!ItemFactory.MERCHANT.loadItems(getPlayer().getId(), false).isEmpty()) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        if (getPlayer().getMerchantMeso() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void showFredrick() {
        c.announce(MaplePacketCreator.getFredrick(getPlayer()));
    }

    public int partyMembersInMap() {
        int inMap = 0;
        for (MapleCharacter char2 : getPlayer().getMap().getCharacters()) {
            if (char2.getParty() == getPlayer().getParty()) {
                inMap++;
            }
        }
        return inMap;
    }

    public MapleEvent getEvent() {
        return c.getChannelServer().getEvent();
    }

    public void divideTeams() {
        if (getEvent() != null) {
            getPlayer().setTeam(getEvent().getLimit() % 2); //muhaha :D
        }
    }

    public MapleCharacter getMapleCharacter(String player) {
        MapleCharacter target = Server.getInstance().getWorld(c.getWorld()).getChannel(c.getChannel()).getPlayerStorage().getCharacterByName(player);
        return target;
    }

    public void logLeaf(String item) {
        LogHelper.logLeaf(getPlayer(), item);
    }

    public void logVote(String item) {
        LogHelper.logVote(getPlayer(), item);
    }

    public void logFishing(String item) {
        LogHelper.logFishing(getPlayer(), item);
    }

    public void logDonator(String item) {
        LogHelper.logDonator(getPlayer(), item);
    }

    public void logGuildJQ(Integer sec) {
        LogHelper.logGuildJQ(getPlayer(), sec);
    }

    public void logMobPoints(String item) {
        LogHelper.logMobPoints(getPlayer(), item);
    }

    public void logEnchanted(String item) {
        LogHelper.logEnchanted(getPlayer(), item);
    }

    public void logGeneralStore(String item) {
        LogHelper.logGeneralStore(getPlayer(), item);
    }

    public void logHalloweenGachapon(int item) {
        LogHelper.logHalloweenGachapon(getPlayer(), item);
    }

    public void logEventTrophy(String item) {
        LogHelper.logEventTrophy(getPlayer(), item);
    }

    public boolean createPyramid(String mode, boolean party) {//lol
        PyramidMode mod = PyramidMode.valueOf(mode);

        MapleParty partyz = getPlayer().getParty();
        MapleMapFactory mf = c.getChannelServer().getMapFactory();

        MapleMap map = null;
        int mapid = 926010100;
        if (party) {
            mapid += 10000;
        }
        mapid += (mod.getMode() * 1000);

        for (byte b = 0; b < 5; b++) {//They cannot warp to the next map before the timer ends (:
            map = mf.getMap(mapid + b);
            if (map.getCharacters().size() > 0) {
                continue;
            } else {
                break;
            }
        }

        if (map == null) {
            return false;
        }

        if (!party) {
            partyz = new MapleParty(-1, new MaplePartyCharacter(getPlayer()));
        }
        Pyramid py = new Pyramid(partyz, mod, map.getId());
        getPlayer().setPartyQuest(py);
        py.warp(mapid);
        dispose();
        return true;
    }
}
