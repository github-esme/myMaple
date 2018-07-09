/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripting.item;

import client.MapleCharacter;
import client.inventory.Equip;
import client.inventory.MapleInventoryType;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;

/**
 *
 * @author John
 */
public class CustomEquip {

    Equip currentEquip;

    public CustomEquip(int itemID, boolean resetStats) {
        if (MapleItemInformationProvider.getInstance().getInventoryType(itemID) == MapleInventoryType.EQUIP) {
            currentEquip = MapleItemInformationProvider.getInstance().getItemAsEquipById(itemID);

            if (resetStats) {
                currentEquip.setStr((short) 0);
                currentEquip.setDex((short) 0);
                currentEquip.setInt((short) 0);
                currentEquip.setLuk((short) 0);
                currentEquip.setMatk((short) 0);
                currentEquip.setWatk((short) 0);
                currentEquip.setAcc((short) 0);
                currentEquip.setAvoid((short) 0);
                currentEquip.setJump((short) 0);
                currentEquip.setSpeed((short) 0);
                currentEquip.setWdef((short) 0);
                currentEquip.setMdef((short) 0);
                currentEquip.setHp((short) 0);
                currentEquip.setMp((short) 0);
                currentEquip.setUpgradeSlots(0);
            }
        }
    }

    public void gainEquip(MapleCharacter chr) {
        if (currentEquip != null) {
            MapleInventoryManipulator.addFromDrop(chr.getClient(), currentEquip, false);
        }
    }

    public void setStr(int value) {
        currentEquip.setStr((short) value);
    }

    public void setDex(int value) {
        currentEquip.setDex((short) value);
    }

    public void setInt(int value) {
        currentEquip.setInt((short) value);
    }

    public void setLuk(int value) {
        currentEquip.setLuk((short) value);
    }

    public void setMatk(int value) {
        currentEquip.setMatk((short) value);
    }

    public void setWatk(int value) {
        currentEquip.setWatk((short) value);
    }

    public void setAcc(int value) {
        currentEquip.setAcc((short) value);
    }

    public void setAvoid(int value) {
        currentEquip.setAvoid((short) value);
    }

    public void setJump(int value) {
        currentEquip.setJump((short) value);
    }

    public void setSpeed(int value) {
        currentEquip.setSpeed((short) value);
    }

    public void setWdef(int value) {
        currentEquip.setWdef((short) value);
    }

    public void setMdef(int value) {
        currentEquip.setMdef((short) value);
    }

    public void setHp(int value) {
        currentEquip.setHp((short) value);
    }

    public void setMp(int value) {
        currentEquip.setMp((short) value);
    }

    public void setUpgradeSlots(int value) {
        currentEquip.setUpgradeSlots(value);
    }
}
