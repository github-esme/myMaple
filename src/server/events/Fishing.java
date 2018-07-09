package server.events;

import client.MapleCharacter;
import java.util.Iterator;
import server.MapleInventoryManipulator;
import tools.MaplePacketCreator;

/**
 *
 * @author FateJiki of RaGeZONE
 */
public class Fishing {

    public static void doFishing(client.MapleCharacter chr) {
//        int mesoMultiplier = 1;
//        int expMultiplier = 1;
//        switch (chr.getWorld()) {
//            case 0:
//                mesoMultiplier = chr.getMesoRate();
//                expMultiplier = chr.getExpRate();
//                break;
//        }
        //int mesoAward = (int) (1400.0 * Math.random() + 1201) * mesoMultiplier + (15 * chr.getLevel() / 5);
        int mesoAward = 1 + (int) (Math.random() * 300);
        //int expAward = (int) (645.0 * Math.random()) * expMultiplier + (15 * chr.getLevel() / 2) / 6;
        int expAward = 1;

        if (String.valueOf(chr.getLevel()).length() == 2) {
            expAward = 1 + (int) (Math.random() * Integer.parseInt((String.valueOf(chr.getLevel()).substring(0, 1) + "0")) * 5);
        } else {
            expAward = 1 + (int) (Math.random() * Integer.parseInt((String.valueOf(chr.getLevel()).substring(0, 2) + "0")) * 10);
        }

        if (chr.getLevel() >= 10 && chr.getMapId() == 910000000) {
            int rand = (int) (2.0 * Math.random());
            switch (rand) {
                case 0:
                    chr.gainMeso(mesoAward, true, true, true);
                    chr.getClient().getSession().write(MaplePacketCreator.catchMonster(9500336, 2000017, (byte) 1));
                    chr.getMap().broadcastMessage(chr, MaplePacketCreator.catchMonster(9500336, 2000017, (byte) 1), false);
                    break;
                case 1:
                    chr.gainExp(expAward, true, true);
                    chr.getClient().getSession().write(MaplePacketCreator.catchMonster(9500336, 2000017, (byte) 1));
                    chr.getMap().broadcastMessage(chr, MaplePacketCreator.catchMonster(9500336, 2000017, (byte) 1), false);
                    break;
                case 2:
                    MapleInventoryManipulator.addById(chr.getClient(), getRandomItem(), (short) 1);
                    chr.getClient().getSession().write(MaplePacketCreator.catchMonster(9500336, 2000017, (byte) 1));
                    chr.getMap().broadcastMessage(chr, MaplePacketCreator.catchMonster(9500336, 2000017, (byte) 1), false);
                    break;
            }

        }
    }

    public static Runnable fish() {
        return new Runnable() {
            public void run() {
                for (net.server.world.World wld : net.server.Server.getInstance().getWorlds()) {
                    for (net.server.channel.Channel chan : wld.getChannels()) {

                        chan.getPlayerStorage().fish();

                        for (int mapID : new Housing().getAllHousingMaps()) {
                            for (MapleCharacter chr : chan.getMapFactory().getMap(mapID).getCharacters()) {
                                if (chr.isFishing()) {
                                    chr.gainFishingPoint(1);
                                }
                            }
                        }

                    }
                }
            }
        };
    }

    public static int getRandomItem() {
        int finalID = 0;
        int rand = (int) (100.0 * Math.random());
        int[] commons = {1002851, 2002020, 2002020, 2000006, 2000018, 2002018, 2002024, 2002027, 2002027, 2000018, 2000018, 2000018, 2000018, 2002030, 2002018, 2000016}; // filler' up
        int[] uncommons = {1000025, 1002662, 1002812, 1002850, 1002881, 1002880, 1012072, 4020009, 2043220, 2043022, 2040543, 2044420, 2040943, 2043713, 2044220, 2044120, 2040429, 2043220, 2040943}; // filler' uptoo
        int[] rares = {1002859, 1002553, 01002762, 01002763, 01002764, 01002765, 01002766, 01002663, 1002788, 1002949, 2049100, 2340000, 2040822, 2040822, 2040822, 2040822, 2040822, 2040822, 2040822, 2040822}; // filler' uplast
        if (rand >= 25) {
            return commons[(int) (commons.length * Math.random())];
        } else if (rand <= 7 && rand >= 5) {
            return uncommons[(int) (uncommons.length * Math.random())];
        } else if (rand <= 3) {
            return rares[(int) (rares.length * Math.random())];
        }

        return finalID;
    }
}
