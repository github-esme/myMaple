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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import tools.DatabaseConnection;

public class Housing {

    public List<Integer> getAllHousingMaps() {
        List<Integer> mapIDList = new ArrayList<>();

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                mapIDList.add(rs.getInt("mapid"));
            }

            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return mapIDList;
    }

    public List<Integer> getAllOpenHousingMaps() {
        List<Integer> mapIDList = new ArrayList<>();

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE owner = ''");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getTimestamp("bettime") == null) {
                    mapIDList.add(rs.getInt("mapid"));
                } else {
                    long endingBetTimestamp = rs.getTimestamp("bettime").getTime() + 86400000;

                    if (endingBetTimestamp > System.currentTimeMillis()) {
                        mapIDList.add(rs.getInt("mapid"));
                    }
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return mapIDList;
    }

    public List<Integer> getAllOwnedHousingMaps() {
        List<Integer> mapIDList = new ArrayList<>();

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE owner != ''");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                mapIDList.add(rs.getInt("mapid"));
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return mapIDList;
    }

    public boolean isInOwnedHousingMap(MapleCharacter chr) {
        List<Integer> mapIDList = new ArrayList<>();

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE owner != ''");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                mapIDList.add(rs.getInt("mapid"));
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        boolean isIn = false;

        for (int mapID : mapIDList) {
            if (chr.getMapId() == mapID) {
                isIn = true;
            }
        }

        return isIn;
    }

    public boolean isInHousingMap(MapleCharacter chr) {
        List<Integer> mapIDList = new ArrayList<>();

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                mapIDList.add(rs.getInt("mapid"));
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        boolean isIn = false;

        for (int mapID : mapIDList) {
            if (chr.getMapId() == mapID) {
                isIn = true;
            }
        }

        return isIn;
    }

    public List<Integer> getAllOwnedHousingMaps(String owner) {
        List<Integer> mapIDList = new ArrayList<>();

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE owner = ?");
            ps.setString(1, owner);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                mapIDList.add(rs.getInt("mapid"));
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return mapIDList;
    }

    public List<Integer> getAllRedeemAvailableMaps(String betName) {
        List<Integer> mapIDList = new ArrayList<>();

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE betname = ? AND owner = ''");
            ps.setString(1, betName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getTimestamp("bettime") != null) {
                    long endingBetTimestamp = rs.getTimestamp("bettime").getTime() + 86400000;

                    if (endingBetTimestamp < System.currentTimeMillis()) {
                        mapIDList.add(rs.getInt("mapid"));
                    }
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return mapIDList;
    }

    public List<Integer> getAllBiddedMaps(String betName) {
        List<Integer> mapIDList = new ArrayList<>();

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE betname = ?");
            ps.setString(1, betName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                mapIDList.add(rs.getInt("mapid"));
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return mapIDList;
    }

    public String getHouseName(int mapID) {
        String houseName = "";

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                houseName = rs.getString("housename");
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return houseName;
    }

    public boolean isAvailable(int mapID) {
        Boolean isAvailable = false;

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getTimestamp("bettime") == null) {
                    isAvailable = true;
                } else {
                    long endingBetTimestamp = rs.getTimestamp("bettime").getTime() + 86400000;

                    if (endingBetTimestamp > System.currentTimeMillis()) {
                        isAvailable = true;
                    }
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return isAvailable;
    }

    public boolean failToRedeem(int mapID) {
        Boolean failed = false;

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ? AND owner = ''");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getTimestamp("bettime") != null) {
                    long endingBetTimestamp = rs.getTimestamp("bettime").getTime() + 86400000 + 86400000;

                    if (endingBetTimestamp < System.currentTimeMillis()) {
                        failed = true;
                    }
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return failed;
    }

    public boolean houseExpired(int mapID) {
        Boolean expired = false;

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getTimestamp("timestart") != null) {
                    long endingTimestamp = rs.getTimestamp("timestart").getTime() + 1209600000;

                    if (endingTimestamp < System.currentTimeMillis()) {
                        expired = true;
                    }
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return expired;
    }

    public String getEndingHouseDate(int mapID) {
        String endingDate = "";

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getTimestamp("timestart") != null) {
                    long time = rs.getTimestamp("timestart").getTime() + 1209600000;

                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    endingDate = dateFormat.format(new Date(time));
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return endingDate;
    }

    public String getStartingHouseDate(int mapID) {
        String endingDate = "";

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getTimestamp("timestart") != null) {
                    long time = rs.getTimestamp("timestart").getTime();

                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    endingDate = dateFormat.format(new Date(time));
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return endingDate;
    }

    public String getStartingBetDate(int mapID) {
        String endingDate = "";

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getTimestamp("bettime") != null) {
                    long time = rs.getTimestamp("bettime").getTime();

                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    endingDate = dateFormat.format(new Date(time));
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return endingDate;
    }

    public String getEndingBetDate(int mapID) {
        String endingDate = "";

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getTimestamp("bettime") != null) {
                    long time = rs.getTimestamp("bettime").getTime() + 86400000;

                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    endingDate = dateFormat.format(new Date(time));
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return endingDate;

    }

    public long getEndingBetTimestamp(int mapID) {
        long endingTimestamp = 0;

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getTimestamp("bettime") != null) {
                    endingTimestamp = rs.getTimestamp("bettime").getTime() + 86400000;
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return endingTimestamp;

    }

    public int getCurrentBet(int mapID) {
        int currentBet = 0;

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                currentBet = rs.getInt("betmesos");
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return currentBet;
    }

    public String getCurrentBetName(int mapID) {
        String currentBetName = "";

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                currentBetName = rs.getString("betname");
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return currentBetName;
    }

    public String getPassword(int mapID) {
        String password = "";

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                password = rs.getString("password");
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return password;
    }

    public void setPassword(int mapID, String password) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("UPDATE housing SET password = ? WHERE mapid = ?")) {
                ps.setString(1, password);
                ps.setInt(2, mapID);
                ps.executeUpdate();

                ps.close();
            }
        } catch (SQLException e) {
        }
    }

    public void startBetTime(int mapID) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("UPDATE housing SET bettime = CURRENT_TIMESTAMP() WHERE mapid = ?")) {
                ps.setInt(1, mapID);
                ps.executeUpdate();

                ps.close();
            }
        } catch (SQLException e) {
        }
    }

    public void makeBet(int mapID, int betmesos, String betname) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("UPDATE housing SET betmesos = ?, betname = ? WHERE mapid = ?")) {
                ps.setInt(1, betmesos);
                ps.setString(2, betname);
                ps.setInt(3, mapID);
                ps.executeUpdate();

                ps.close();
            }
        } catch (SQLException e) {
        }
    }

    public int getMinMesos(int mapID) {
        int minMesos = 0;

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                minMesos = rs.getInt("minmesos");
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return minMesos;
    }

    public boolean isOwner(int mapID, String owner) {
        Boolean isOwner = false;

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("owner").equals(owner)) {
                    isOwner = true;
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return isOwner;
    }

    public String getOwner(int mapID) {
        String owner = "";

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                owner = rs.getString("owner");
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return owner;
    }

    public void setHouseName(int mapID, String newName) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("UPDATE housing SET housename = ? WHERE mapid = ?")) {
                ps.setString(1, newName);
                ps.setInt(2, mapID);
                ps.executeUpdate();

                ps.close();
            }
        } catch (SQLException e) {
        }
    }

    public void redeemHouse(int mapID, String buyer, String houseName, String password) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("UPDATE housing SET owner = ?, housename = ?, password = ?, timestart = CURRENT_TIMESTAMP() WHERE mapid = ?")) {
                ps.setString(1, buyer);
                ps.setString(2, houseName);
                ps.setString(3, password);
                ps.setInt(4, mapID);
                ps.executeUpdate();

                ps.close();
            }
        } catch (SQLException e) {
        }
    }

    public void resetHouse(int mapID) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("UPDATE housing SET owner = '', housename = '', password = '', bettime = NULL, betmesos = 0, betname = '', timestart = NULL WHERE mapid = ?")) {
                ps.setInt(1, mapID);
                ps.executeUpdate();

                ps.close();
            }
        } catch (SQLException e) {
        }
    }

    public boolean isNewHouse(int mapID) {
        boolean newHouse = false;

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM housing WHERE mapid = ?");
            ps.setInt(1, mapID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("owner").equals("")) {
                    newHouse = true;
                }
            }
            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return newHouse;
    }

    public String msToString(long ms) {
        long totalSecs = ms / 1000;
        long hours = (totalSecs / 3600);
        long mins = (totalSecs / 60) % 60;
        long secs = totalSecs % 60;
        String minsString = (mins == 0)
                ? "00"
                : ((mins < 10)
                        ? "0" + mins
                        : "" + mins);
        String secsString = (secs == 0)
                ? "00"
                : ((secs < 10)
                        ? "0" + secs
                        : "" + secs);
        if (hours > 0) {
            return hours + " Hours " + minsString + " Minutes " + secsString + " Seconds";
        } else if (mins > 0) {
            return mins + " Minutes " + secsString + " Seconds";
        } else {
            return secsString + " Seconds";
        }
    }
}
