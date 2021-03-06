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
package server;

import client.MapleBuffStat;
import client.MapleClient;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.ModifyInventory;
import constants.ItemConstants;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import tools.MaplePacketCreator;

/**
 *
 * @author Matze
 */
public class MapleInventoryManipulator {

    public static boolean addById(MapleClient c, int itemId, short quantity) {
        return addById(c, itemId, quantity, null, -1, -1);
    }

    public static boolean addById(MapleClient c, int itemId, short quantity, long expiration) {
        return addById(c, itemId, quantity, null, -1, (byte) 0, expiration);
    }

    public static boolean addById(MapleClient c, int itemId, short quantity, String owner, int petid) {
        return addById(c, itemId, quantity, owner, petid, -1);
    }

    public static boolean addById(MapleClient c, int itemId, short quantity, String owner, int petid, long expiration) {
        return addById(c, itemId, quantity, owner, petid, (byte) 0, expiration);
    }

    public static boolean addById(MapleClient c, int itemId, short quantity, String owner, int petid, byte flag, long expiration) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleInventoryType type = ii.getInventoryType(itemId);
        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, itemId);
            List<Item> existing = c.getPlayer().getInventory(type).listById(itemId);
            if (!ItemConstants.isRechargable(itemId)) {
                if (existing.size() > 0) { // first update all existing slots to slotMax
                    Iterator<Item> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            Item eItem = i.next();
                            short oldQ = eItem.getQuantity();
                            if (oldQ < slotMax && (eItem.getOwner().equals(owner) || owner == null)) {
                                short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                eItem.setQuantity(newQ);
                                eItem.setExpiration(expiration);
                                c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(1, eItem))));
                            }
                        } else {
                            break;
                        }
                    }
                }
                while (quantity > 0 || ItemConstants.isRechargable(itemId)) {
                    short newQ = (short) Math.min(quantity, slotMax);
                    if (newQ != 0) {
                        quantity -= newQ;
                        Item nItem = new Item(itemId, (short) 0, newQ, petid);
                        nItem.setFlag(flag);
                        nItem.setExpiration(expiration);
                        short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                        if (newSlot == -1) {
                            c.announce(MaplePacketCreator.getInventoryFull());
                            c.announce(MaplePacketCreator.getShowInventoryFull());
                            return false;
                        }
                        if (owner != null) {
                            nItem.setOwner(owner);
                        }
                        c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, nItem))));
                        if ((ItemConstants.isRechargable(itemId)) && quantity == 0) {
                            break;
                        }
                    } else {
                        c.announce(MaplePacketCreator.enableActions());
                        return false;
                    }
                }
            } else {
                Item nItem = new Item(itemId, (short) 0, quantity, petid);
                nItem.setFlag(flag);
                nItem.setExpiration(expiration);
                short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                if (newSlot == -1) {
                    c.announce(MaplePacketCreator.getInventoryFull());
                    c.announce(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, nItem))));
            }
        } else if (quantity == 1) {
            Item nEquip = ii.getEquipById(itemId);
            nEquip.setFlag(flag);
            nEquip.setExpiration(expiration);
            if (owner != null) {
                nEquip.setOwner(owner);
            }
            short newSlot = c.getPlayer().getInventory(type).addItem(nEquip);
            if (newSlot == -1) {
                c.announce(MaplePacketCreator.getInventoryFull());
                c.announce(MaplePacketCreator.getShowInventoryFull());
                return false;
            }
            c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, nEquip))));
        } else {
            throw new RuntimeException("Trying to create equip with non-one quantity");
        }
        return true;
    }

    public static boolean addByItem(MapleClient c, Item item, short quantity, String owner, int petid, byte flag, long expiration) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        int itemId = item.getItemId();
        MapleInventoryType type = ii.getInventoryType(itemId);
        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, itemId);
            List<Item> existing = c.getPlayer().getInventory(type).listById(itemId);
            if (!ItemConstants.isRechargable(itemId)) {
                if (existing.size() > 0) { // first update all existing slots to slotMax
                    Iterator<Item> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            Item eItem = i.next();
                            short oldQ = eItem.getQuantity();
                            if (oldQ < slotMax && (eItem.getOwner().equals(owner) || owner == null)) {
                                short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                eItem.setQuantity(newQ);
                                eItem.setExpiration(expiration);
                                c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(1, eItem))));
                            }
                        } else {
                            break;
                        }
                    }
                }
                while (quantity > 0 || ItemConstants.isRechargable(itemId)) {
                    short newQ = (short) Math.min(quantity, slotMax);
                    if (newQ != 0) {
                        quantity -= newQ;
                        Item nItem = item;
                        nItem.setFlag(flag);
                        nItem.setExpiration(expiration);
                        short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                        if (newSlot == -1) {
                            c.announce(MaplePacketCreator.getInventoryFull());
                            c.announce(MaplePacketCreator.getShowInventoryFull());
                            return false;
                        }
                        if (owner != null) {
                            nItem.setOwner(owner);
                        }
                        c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, nItem))));
                        if ((ItemConstants.isRechargable(itemId)) && quantity == 0) {
                            break;
                        }
                    } else {
                        c.announce(MaplePacketCreator.enableActions());
                        return false;
                    }
                }
            } else {
                Item nItem = new Item(itemId, (short) 0, quantity, petid);
                nItem.setFlag(flag);
                nItem.setExpiration(expiration);
                short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                if (newSlot == -1) {
                    c.announce(MaplePacketCreator.getInventoryFull());
                    c.announce(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, nItem))));
            }
        } else if (quantity == 1) {
            Item nEquip = item;
            nEquip.setFlag(flag);
            nEquip.setExpiration(expiration);
            if (owner != null) {
                nEquip.setOwner(owner);
            }
            short newSlot = c.getPlayer().getInventory(type).addItem(nEquip);
            if (newSlot == -1) {
                c.announce(MaplePacketCreator.getInventoryFull());
                c.announce(MaplePacketCreator.getShowInventoryFull());
                return false;
            }
            c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, nEquip))));
        } else {
            throw new RuntimeException("Trying to create equip with non-one quantity");
        }
        return true;
    }

    public static boolean addByIdProItem(MapleClient c, Item item, short quantity) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        int itemId = item.getItemId();
        MapleInventoryType type = ii.getInventoryType(itemId);
        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, itemId);
            List<Item> existing = c.getPlayer().getInventory(type).listById(itemId);
            if (!ItemConstants.isRechargable(itemId)) {
                if (existing.size() > 0) { // first update all existing slots to slotMax
                    Iterator<Item> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            Item eItem = i.next();
                            short oldQ = eItem.getQuantity();
                            if (oldQ < slotMax) {
                                short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                eItem.setQuantity(newQ);
                                eItem.setExpiration(-1);
                                c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(1, eItem))));
                            }
                        } else {
                            break;
                        }
                    }
                }
                while (quantity > 0 || ItemConstants.isRechargable(itemId)) {
                    short newQ = (short) Math.min(quantity, slotMax);
                    if (newQ != 0) {
                        quantity -= newQ;
                        short newSlot = c.getPlayer().getInventory(type).addItem(item);
                        if (newSlot == -1) {
                            c.announce(MaplePacketCreator.getInventoryFull());
                            c.announce(MaplePacketCreator.getShowInventoryFull());
                            return false;
                        }

                        c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, item))));
                        if ((ItemConstants.isRechargable(itemId)) && quantity == 0) {
                            break;
                        }
                    } else {
                        c.announce(MaplePacketCreator.enableActions());
                        return false;
                    }
                }
            } else {
                short newSlot = c.getPlayer().getInventory(type).addItem(item);
                if (newSlot == -1) {
                    c.announce(MaplePacketCreator.getInventoryFull());
                    c.announce(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, item))));
            }
        } else if (quantity == 1) {
            short newSlot = c.getPlayer().getInventory(type).addItem(item);
            if (newSlot == -1) {
                c.announce(MaplePacketCreator.getInventoryFull());
                c.announce(MaplePacketCreator.getShowInventoryFull());
                return false;
            }
            c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, item))));
        } else {
            throw new RuntimeException("Trying to create equip with non-one quantity");
        }
        return true;
    }

    public static void removeAllById(MapleClient c, int itemId, boolean checkEquipped) {
        MapleInventoryType type = MapleItemInformationProvider.getInstance().getInventoryType(itemId);
        for (Item item : c.getPlayer().getInventory(type).listById(itemId)) {
            if (item != null) {
                removeFromSlot(c, type, item.getPosition(), item.getQuantity(), true, false);
            }
        }
        if (checkEquipped) {
            Item ii = c.getPlayer().getInventory(type).findById(itemId);
            if (ii != null) {
                c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeItem(ii.getPosition());
                c.getPlayer().equipChanged();
            }
        }
    }

    public static boolean addFromDrop(MapleClient c, Item item, boolean show) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

        if (c.getPlayer() == null || (ii.isPickupRestricted(item.getItemId()) && c.getPlayer().getItemQuantity(item.getItemId(), true) > 0 && item.getOwner() != "Transmog")) {
            c.announce(MaplePacketCreator.getInventoryFull());
            c.announce(MaplePacketCreator.showItemUnavailable());
            return false;
        }

        MapleInventoryType type = ii.getInventoryType(item.getItemId());

        short quantity = item.getQuantity();
        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, item.getItemId());
            List<Item> existing = c.getPlayer().getInventory(type).listById(item.getItemId());
            if (!ItemConstants.isRechargable(item.getItemId())) {
                if (quantity <= 0) { //wth
                    c.getSession().write(MaplePacketCreator.getInventoryFull());
                    c.getSession().write(MaplePacketCreator.showItemUnavailable());
                    return false;
                }

                if (existing.size() > 0) { // first update all existing slots to slotMax
                    Iterator<Item> i = existing.iterator();
                    while (quantity > 0) {
                        if (i.hasNext()) {
                            Item eItem = i.next();
                            short oldQ = eItem.getQuantity();
                            if (oldQ < slotMax && item.getOwner().equals(eItem.getOwner()) && item.getExpiration() == eItem.getExpiration()) {
                                short newQ = (short) Math.min(oldQ + quantity, slotMax);
                                quantity -= (newQ - oldQ);
                                eItem.setQuantity(newQ);
                                c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(1, eItem))));
                            }
                        } else {
                            break;
                        }
                    }
                }
                while (quantity > 0) {
                    short newQ = (short) Math.min(quantity, slotMax);
                    quantity -= newQ;
                    Item nItem = new Item(item.getItemId(), (short) 0, newQ);
                    nItem.setExpiration(item.getExpiration());
                    nItem.setOwner(item.getOwner());
                    nItem.setFlag(item.getFlag());
                    short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                    if (newSlot == -1) {
                        c.announce(MaplePacketCreator.getInventoryFull());
                        c.announce(MaplePacketCreator.getShowInventoryFull());
                        item.setQuantity((short) (quantity + newQ));
                        return false;
                    }
                    c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, nItem))));
                }
            } else {
                Item nItem = new Item(item.getItemId(), (short) 0, quantity);
                short newSlot = c.getPlayer().getInventory(type).addItem(nItem);
                if (newSlot == -1) {
                    c.announce(MaplePacketCreator.getInventoryFull());
                    c.announce(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, nItem))));
                c.announce(MaplePacketCreator.enableActions());
            }
        } else if (quantity == 1) {
            short newSlot = c.getPlayer().getInventory(type).addItem(item);
            if (newSlot == -1) {
                c.announce(MaplePacketCreator.getInventoryFull());
                c.announce(MaplePacketCreator.getShowInventoryFull());
                return false;
            }
            c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(0, item))));
        } else {
            return false;
        }
        if (show) {
            c.announce(MaplePacketCreator.getShowItemGain(item.getItemId(), item.getQuantity()));
        }
        return true;
    }

    public static boolean checkSpace(MapleClient c, int itemid, int quantity, String owner) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleInventoryType type = ii.getInventoryType(itemid);
        if (!type.equals(MapleInventoryType.EQUIP)) {
            short slotMax = ii.getSlotMax(c, itemid);
            List<Item> existing = c.getPlayer().getInventory(type).listById(itemid);
            if (!ItemConstants.isRechargable(itemid)) {
                if (existing.size() > 0) // first update all existing slots to slotMax
                {
                    for (Item eItem : existing) {
                        short oldQ = eItem.getQuantity();
                        if (oldQ < slotMax && owner.equals(eItem.getOwner())) {
                            short newQ = (short) Math.min(oldQ + quantity, slotMax);
                            quantity -= (newQ - oldQ);
                        }
                        if (quantity <= 0) {
                            break;
                        }
                    }
                }
            }
            final int numSlotsNeeded;
            if (slotMax > 0) {
                numSlotsNeeded = (int) (Math.ceil(((double) quantity) / slotMax));
            } else if (ItemConstants.isRechargable(itemid)) {
                numSlotsNeeded = 1;
            } else {
                numSlotsNeeded = 1;
            }
            return !c.getPlayer().getInventory(type).isFull(numSlotsNeeded - 1);
        } else {
            return !c.getPlayer().getInventory(type).isFull();
        }
    }

    public static void removeFromSlot(MapleClient c, MapleInventoryType type, short slot, short quantity, boolean fromDrop) {
        removeFromSlot(c, type, slot, quantity, fromDrop, false);
    }

    public static void removeFromSlot(MapleClient c, MapleInventoryType type, short slot, short quantity, boolean fromDrop, boolean consume) {
        if (c.getPlayer() == null || c.getPlayer().getInventory(type) == null) {
            return;
        }

        Item item = c.getPlayer().getInventory(type).getItem(slot);
        boolean allowZero = consume && ItemConstants.isRechargable(item.getItemId());
        c.getPlayer().getInventory(type).removeItem(slot, quantity, allowZero);
        if (item.getQuantity() == 0 && !allowZero) {
            c.announce(MaplePacketCreator.modifyInventory(fromDrop, Collections.singletonList(new ModifyInventory(3, item))));
        } else {
            c.announce(MaplePacketCreator.modifyInventory(fromDrop, Collections.singletonList(new ModifyInventory(1, item))));
        }
    }

    public static void removeById(MapleClient c, MapleInventoryType type, int itemId, int quantity, boolean fromDrop, boolean consume) {
        int removeQuantity = quantity;

        if (c.getPlayer() == null || c.getPlayer().getInventory(type) == null) {
            return;
        }

        MapleInventory inv = c.getPlayer().getInventory(type);
        int slotLimit = type == MapleInventoryType.EQUIPPED ? 128 : inv.getSlotLimit();

        for (short i = 0; i <= slotLimit; i++) {
            Item item = inv.getItem((short) (type == MapleInventoryType.EQUIPPED ? -i : i));
            if (item != null) {
                if (item.getItemId() == itemId || item.getCashId() == itemId) {
                    if (removeQuantity <= item.getQuantity()) {
                        removeFromSlot(c, type, item.getPosition(), (short) removeQuantity, fromDrop, consume);
                        removeQuantity = 0;
                        break;
                    } else {
                        removeQuantity -= item.getQuantity();
                        removeFromSlot(c, type, item.getPosition(), item.getQuantity(), fromDrop, consume);
                    }
                }
            }
        }

//        if (removeQuantity > 0) {
//            throw new RuntimeException("[HACK] Not enough items available of Item:" + itemId + ", Quantity (After Quantity/Over Current Quantity): " + (quantity - removeQuantity) + "/" + quantity);
//        }
    }

    public static void move(MapleClient c, MapleInventoryType type, short src, short dst) {
        if (src < 0 || dst < 0) {
            return;
        }
        if (dst > c.getPlayer().getInventory(type).getSlotLimit()) {
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Item source = c.getPlayer().getInventory(type).getItem(src);
        Item initialTarget = c.getPlayer().getInventory(type).getItem(dst);
        if (source == null) {
            return;
        }
        short olddstQ = -1;
        if (initialTarget != null) {
            olddstQ = initialTarget.getQuantity();
        }
        short oldsrcQ = source.getQuantity();
        short slotMax = ii.getSlotMax(c, source.getItemId());
        c.getPlayer().getInventory(type).move(src, dst, slotMax);
        final List<ModifyInventory> mods = new ArrayList<>();
        if (!type.equals(MapleInventoryType.EQUIP) && initialTarget != null && initialTarget.getItemId() == source.getItemId() && !ItemConstants.isRechargable(source.getItemId())) {
            if ((olddstQ + oldsrcQ) > slotMax) {
                mods.add(new ModifyInventory(1, source));
                mods.add(new ModifyInventory(1, initialTarget));
            } else {
                mods.add(new ModifyInventory(3, source));
                mods.add(new ModifyInventory(1, initialTarget));
            }
        } else {
            mods.add(new ModifyInventory(2, source, src));
        }
        c.announce(MaplePacketCreator.modifyInventory(true, mods));
    }

    public static void equip(MapleClient c, short src, short dst) {
        Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(src);
        if (source == null || !MapleItemInformationProvider.getInstance().canWearEquipment(c.getPlayer(), source, dst)) {
            c.announce(MaplePacketCreator.enableActions());
            return;
        } else if ((((source.getItemId() >= 1902000 && source.getItemId() <= 1902002) || source.getItemId() == 1912000) && c.getPlayer().isCygnus()) || ((source.getItemId() >= 1902005 && source.getItemId() <= 1902007) || source.getItemId() == 1912005) && !c.getPlayer().isCygnus()) {// Adventurer taming equipment
            return;
        }
        boolean itemChanged = false;
        if (MapleItemInformationProvider.getInstance().isUntradeableOnEquip(source.getItemId())) {
            source.setFlag((byte) ItemConstants.UNTRADEABLE);
            itemChanged = true;
        }
        if (source.getRingId() > -1) {
            c.getPlayer().getRingById(source.getRingId()).equip();
        }

        switch (dst) {
            case -6:
                // unequip the overall
                Item top = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -5);
                if (top != null && isOverall(top.getItemId())) {
                    if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).isFull()) {
                        c.announce(MaplePacketCreator.getInventoryFull());
                        c.announce(MaplePacketCreator.getShowInventoryFull());
                        return;
                    }
                    unequip(c, (byte) -5, c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
                }
                break;
            case -5:
                final Item bottom = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -6);
                if (bottom != null && isOverall(source.getItemId())) {
                    if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).isFull()) {
                        c.announce(MaplePacketCreator.getInventoryFull());
                        c.announce(MaplePacketCreator.getShowInventoryFull());
                        return;
                    }
                    unequip(c, (byte) -6, c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
                }
                break;
            case -10:
                // check if weapon is two-handed
                Item weapon = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                if (weapon != null && MapleItemInformationProvider.getInstance().isTwoHanded(weapon.getItemId())) {
                    if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).isFull()) {
                        c.announce(MaplePacketCreator.getInventoryFull());
                        c.announce(MaplePacketCreator.getShowInventoryFull());
                        return;
                    }
                    unequip(c, (byte) -11, c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
                }
                break;
            case -11:
                Item shield = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
                if (shield != null && MapleItemInformationProvider.getInstance().isTwoHanded(source.getItemId())) {
                    if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).isFull()) {
                        c.announce(MaplePacketCreator.getInventoryFull());
                        c.announce(MaplePacketCreator.getShowInventoryFull());
                        return;
                    }
                    unequip(c, (byte) -10, c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
                }
                break;
            default:
                break;
        }
        if (dst == -18) {
            if (c.getPlayer().getMount() != null) {
                c.getPlayer().getMount().setItemId(source.getItemId());
            }
        }
        if (source.getItemId() == 1122017) {
            c.getPlayer().equipPendantOfSpirit();
        }

        //1112413, 1112414, 1112405 (Lilin's Ring)
        source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(src);
        Equip target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
        c.getPlayer().getInventory(MapleInventoryType.EQUIP).removeSlot(src);
        if (target != null) {
            c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeSlot(dst);
        }

        final List<ModifyInventory> mods = new ArrayList<>();
        if (itemChanged) {
            mods.add(new ModifyInventory(3, source));
            mods.add(new ModifyInventory(0, source.copy()));//to prevent crashes
        }

        source.setPosition(dst);
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).addFromDB(source);
        if (target != null) {
            target.setPosition(src);
            c.getPlayer().getInventory(MapleInventoryType.EQUIP).addFromDB(target);
        }
        if (c.getPlayer().getBuffedValue(MapleBuffStat.BOOSTER) != null && isWeapon(source.getItemId())) {
            c.getPlayer().cancelBuffStats(MapleBuffStat.BOOSTER);
        }

        mods.add(new ModifyInventory(2, source, src));
        c.announce(MaplePacketCreator.modifyInventory(true, mods));
        c.getPlayer().equipChanged();
    }

    public static void unequip(MapleClient c, short src, short dst) {
        Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(src);
        Equip target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);
        if (dst < 0) {
            return;
        }
        if (source == null) {
            return;
        }
        if (target != null && src <= 0) {
            c.announce(MaplePacketCreator.getInventoryFull());
            return;
        }
        if (source.getItemId() == 1122017) {
            c.getPlayer().unequipPendantOfSpirit();
        }
        if (source.getRingId() > -1) {
            c.getPlayer().getRingById(source.getRingId()).unequip();
        }
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).removeSlot(src);
        if (target != null) {
            c.getPlayer().getInventory(MapleInventoryType.EQUIP).removeSlot(dst);
        }
        source.setPosition(dst);
        c.getPlayer().getInventory(MapleInventoryType.EQUIP).addFromDB(source);
        if (target != null) {
            target.setPosition(src);
            c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).addFromDB(target);
        }
        c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(2, source, src))));
        c.getPlayer().equipChanged();
    }

    public static void drop(MapleClient c, MapleInventoryType type, short src, short quantity) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (src < 0) {
            type = MapleInventoryType.EQUIPPED;
        }

        if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
            return;
        }

        Item source = c.getPlayer().getInventory(type).getItem(src);

        boolean isKarmed = false;

        if (source != null) {
            if ((source.getFlag() & ItemConstants.KARMA) == ItemConstants.KARMA) {
                isKarmed = true;
            }
        }

        if (c.getPlayer().getTrade() != null || c.getPlayer().getMiniGame() != null || source == null) { //Only check needed would prob be merchants (to see if the player is in one)
            return;
        }
        int itemId = source.getItemId();
        if (itemId >= 5000000 && itemId <= 5000100) {
            return;
        }
        if (type == MapleInventoryType.EQUIPPED && itemId == 1122017) {
            c.getPlayer().unequipPendantOfSpirit();
        }
        if (c.getPlayer().getItemEffect() == itemId && source.getQuantity() == 1) {
            c.getPlayer().setItemEffect(0);
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.itemEffect(c.getPlayer().getId(), 0));
        } else if (itemId == 5370000 || itemId == 5370001) {
            if (c.getPlayer().getItemQuantity(itemId, false) == 1) {
                c.getPlayer().setChalkboard(null);
            }
        }
        if ((!ItemConstants.isRechargable(itemId) && c.getPlayer().getItemQuantity(itemId, true) < quantity) || quantity < 0 || source == null) {
            return;
        }
        Point dropPos = new Point(c.getPlayer().getPosition());
        if (quantity < source.getQuantity() && !ItemConstants.isRechargable(itemId)) {
            Item target = source.copy();
            target.setQuantity(quantity);
            source.setQuantity((short) (source.getQuantity() - quantity));
            c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(1, source))));
            boolean weddingRing = source.getItemId() == 1112803 || source.getItemId() == 1112806 || source.getItemId() == 1112807 || source.getItemId() == 1112809;
            if (weddingRing) {
                c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
            } else if (c.getPlayer().getMap().getEverlast()) {
                if (ii.isDropRestricted(target)) {
                    if (isKarmed) {
                        target.setFlag((byte) (target.getFlag() ^ ItemConstants.KARMA));
                        c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                    } else {
                        c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
                    }
                } else if (MapleItemInformationProvider.getInstance().isCash(target.getItemId())) {
                    if ((target.getItemId() >= 5360000 && target.getItemId() <= 5360008) || target.getItemId() == 5360042 || target.getItemId() == 5211000 || (target.getItemId() >= 5211014 && target.getItemId() <= 5211018) || target.getItemId() == 5211039 || target.getItemId() == 5211042 || target.getItemId() == 5211045 || target.getItemId() == 5211048) {
                        c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
                        c.getPlayer().setRates();
                    } else {
                        c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                    }
                } else {
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, false);
                }
            } else if (ii.isDropRestricted(target)) {
                if (isKarmed) {
                    target.setFlag((byte) (target.getFlag() ^ ItemConstants.KARMA));
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                } else {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
                }
            } else if (MapleItemInformationProvider.getInstance().isCash(target.getItemId())) {
                if ((target.getItemId() >= 5360000 && target.getItemId() <= 5360008) || target.getItemId() == 5360042 || target.getItemId() == 5211000 || (target.getItemId() >= 5211014 && target.getItemId() <= 5211018) || target.getItemId() == 5211039 || target.getItemId() == 5211042 || target.getItemId() == 5211045 || target.getItemId() == 5211048) {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos);
                    c.getPlayer().setRates();
                } else {
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
                }
            } else {
                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), target, dropPos, true, true);
            }
        } else {
            c.getPlayer().getInventory(type).removeSlot(src);
            c.announce(MaplePacketCreator.modifyInventory(true, Collections.singletonList(new ModifyInventory(3, source))));
            if (src < 0) {
                c.getPlayer().equipChanged();
            }
            if (c.getPlayer().getMap().getEverlast()) {
                if (ii.isDropRestricted(source)) {
                    if (isKarmed) {
                        source.setFlag((byte) (source.getFlag() ^ ItemConstants.KARMA));
                        c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
                    } else {
                        c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos);
                    }
                } else if (ii.isCash(itemId)) {
                    if ((itemId >= 5360000 && itemId <= 5360008) || itemId == 5360042 || itemId == 5211000 || (itemId >= 5211014 && itemId <= 5211018) || itemId == 5211039 || itemId == 5211042 || itemId == 5211045 || itemId == 5211048) {
                        c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos);
                        c.getPlayer().setRates();
                    } else {
                        c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, false);
                    }
                } else {
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, false);
                }
            } else if (ii.isDropRestricted(source)) {
                if (isKarmed) {
                    source.setFlag((byte) (source.getFlag() ^ ItemConstants.KARMA));
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
                } else {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos);
                }
            } else if (ii.isCash(itemId)) {
                if ((itemId >= 5360000 && itemId <= 5360008) || itemId == 5360042 || itemId == 5211000 || (itemId >= 5211014 && itemId <= 5211018) || itemId == 5211039 || itemId == 5211042 || itemId == 5211045 || itemId == 5211048) {
                    c.getPlayer().getMap().disappearingItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos);
                    c.getPlayer().setRates();
                } else {
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, false);
                }
            } else {
                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), source, dropPos, true, true);
            }
        }
    }

    private static boolean isOverall(int itemId) {
        return itemId / 10000 == 105;
    }

    private static boolean isWeapon(int itemId) {
        return itemId >= 1302000 && itemId < 1492024;
    }
}
