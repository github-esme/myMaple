/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.events;

/**
 *
 * @author Torban
 */
import client.MapleCharacter;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.ItemConstants;
import net.server.Server;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.MaplePacketCreator;

public class HalloweenGachapon {

    public void gainWelcomeBack(MapleCharacter player) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Item item = ii.getEquipById(1112918);

        Equip equip = (Equip) item;
        equip.setStr((short) 5);
        equip.setDex((short) 5);
        equip.setInt((short) 5);
        equip.setLuk((short) 5);
        equip.setFlag((byte) ItemConstants.UNTRADEABLE); // <- makes item untradeable

        MapleInventoryManipulator.addByIdProItem(player.getClient(), equip, (short) 1);
    }

    public void gainLuckyGuy(MapleCharacter player) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Item item = ii.getEquipById(1142249);

        Equip equip = (Equip) item;
        equip.setStr((short) 3);
        equip.setDex((short) 3);
        equip.setInt((short) 3);
        equip.setLuk((short) 3);
        equip.setHp((short) 100);
        equip.setMp((short) 100);
        equip.setWatk((short) 1);
        equip.setMatk((short) 1);
        equip.setWdef((short) 100);
        equip.setMdef((short) 100);
        equip.setAcc((short) 20);
        equip.setAvoid((short) 20);
        equip.setJump((short) 0);
        equip.setSpeed((short) 0);

        MapleInventoryManipulator.addByIdProItem(player.getClient(), equip, (short) 1);
    }

    public void gainSpooky(MapleCharacter player) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Item item = ii.getEquipById(1142887);

        Equip equip = (Equip) item;
        equip.setStr((short) 3);
        equip.setDex((short) 3);
        equip.setInt((short) 3);
        equip.setLuk((short) 3);
        equip.setWatk((short) 3);
        equip.setMatk((short) 3);

        MapleInventoryManipulator.addByIdProItem(player.getClient(), equip, (short) 1);
    }

    public void broadcastGachapon(MapleCharacter player, int itemId) {
        Item item = null;
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

        if (ii.getInventoryType(itemId).equals(MapleInventoryType.EQUIP)) {
            item = ii.getEquipById(itemId);
        } else {
            item = new Item(itemId, (short) 0, (short) 1);
        }

        Server.getInstance().broadcastMessage(MaplePacketCreator.gachaponMessage(item, "Halloween", player));
    }

}
