package tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.server.Server;
import net.server.channel.Channel;
import net.server.world.MapleParty;
import net.server.world.MaplePartyCharacter;
import server.MapleItemInformationProvider;
import server.MapleTrade;
import server.expeditions.MapleExpedition;
import client.MapleCharacter;
import client.inventory.Item;

public class LogHelper {

    public static void logTrade(MapleTrade trade1, MapleTrade trade2) {
        String name1 = trade1.getChr().getName();
        String name2 = trade2.getChr().getName();

        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        String log = "[ " + timeStamp.format(new Date()) + " ] TRADE BETWEEN " + name1 + " AND " + name2 + "\r\n";
        //Trade 1 to trade 2
        log += trade1.getExchangeMesos() + " mesos from " + name1 + " to " + name2 + " \r\n";
        for (Item item : trade1.getItems()) {
            String itemName = MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "(" + item.getItemId() + ")";
            log += item.getQuantity() + " " + itemName + " from " + name1 + " to " + name2 + " \r\n";;
        }
        //Trade 2 to trade 1
        log += trade2.getExchangeMesos() + " mesos from " + name2 + " to " + name1 + " \r\n";
        for (Item item : trade2.getItems()) {
            String itemName = MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "(" + item.getItemId() + ")";
            log += item.getQuantity() + " " + itemName + " from " + name2 + " to " + name1 + " \r\n";;
        }
        log += "\r\n\r\n";
        FilePrinter.printError("trades.txt", log);
    }

    public static void logExpedition(MapleExpedition expedition) {
        Server.getInstance().broadcastGMMessage(MaplePacketCreator.serverNotice(6, expedition.getType().toString() + " Expedition with leader " + expedition.getLeader().getName() + " finished after " + getTimeString(expedition.getStartTime())));

        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        String log = "[ " + timeStamp.format(new Date()) + " ] " + expedition.getType().toString() + " EXPEDITION\r\n";
        log += getTimeString(expedition.getStartTime()) + "\r\n";

        for (MapleCharacter member : expedition.getMembers()) {
            log += ">>" + member.getName() + "\r\n";
        }
        log += "BOSS KILLS\r\n";
        for (String message : expedition.getBossLogs()) {
            log += message;
        }
        log += "\r\n\r\n";
        FilePrinter.printError("expeditions.txt", log);
    }

    public static String getTimeString(long then) {
        long duration = System.currentTimeMillis() - then;
        int seconds = (int) (duration / 1000) % 60;
        int minutes = (int) ((duration / (1000 * 60)) % 60);
        return minutes + " Minutes and " + seconds + " Seconds";
    }

    public static void logSmega(MapleCharacter player, String message) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - " + message + "\r\n";
        FilePrinter.printError("smegas.txt", log);
    }

    public static void logLeaf(MapleCharacter player, String item) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - used Maple Leaf to buy " + item + "\r\n";
        FilePrinter.printError("mapleleaves.txt", log);
    }

    public static void logVote(MapleCharacter player, String item) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - used Vote Point to buy " + item + "\r\n";
        FilePrinter.printError("votepoints.txt", log);
    }

    public static void logFishing(MapleCharacter player, String item) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - used Fishing Point to buy " + item + "\r\n";
        FilePrinter.printError("fishingpoints.txt", log);
    }

    public static void logDonator(MapleCharacter player, String item) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - used Donator Point to buy " + item + "\r\n";
        FilePrinter.printError("donatorpoints.txt", log);
    }

    public static void logGuildJQ(MapleCharacter player, Integer sec) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - in guild " + player.getGuild().getName() + " has completed the guild JQ map " + player.getMapId() + " in " + sec + " seconds\r\n";
        FilePrinter.printError("guildjq.txt", log);
    }

    public static void logEventInstanceError(String error) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + error + "\r\n";
        FilePrinter.printError("eventinstanceerrors.txt", log);
    }

    public static void logMobPoints(MapleCharacter player, String item) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - used Mob Point to buy " + item + "\r\n";
        FilePrinter.printError("mobpoints.txt", log);
    }

    public static void logEnchanted(MapleCharacter player, String item) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - used Enchanted Scroll to buy " + item + "\r\n";
        FilePrinter.printError("enchantedscrolls.txt", log);
    }

    public static void logGeneralStore(MapleCharacter player, String item) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - used the General Store NPC to buy " + item + "\r\n";
        FilePrinter.printError("generalstore.txt", log);
    }

    public static void logHalloweenGachapon(MapleCharacter player, int item) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - used Halloween Candies to get " + item + "\r\n";
        FilePrinter.printError("halloweengachapon.txt", log);
    }

    public static void logEventTrophy(MapleCharacter player, String item) {
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] " + player.getName() + " - used Event Trophy to buy " + item + "\r\n";
        FilePrinter.printError("eventtrophy.txt", log);
    }

    public static void logGacha(MapleCharacter player, int itemid, String map) {
        String itemName = MapleItemInformationProvider.getInstance().getName(itemid);
        java.text.DateFormat timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String log = "[ " + timeStamp.format(new Date()) + " ] - " + player.getName() + " got a " + itemName + "(" + itemid + ") from the " + map + " gachapon.\r\n";
        FilePrinter.printError("gachapon.txt", log);
    }
}
