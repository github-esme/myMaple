/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.partyquest;

import client.MapleCharacter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tools.DatabaseConnection;

/**
 *
 * @author Torban
 */
public class PQLimit {

    public boolean canPQ(MapleCharacter chr, String pqName) {
        Boolean canPQ = false;

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM partyquest WHERE accid = ? AND pqname = ?");
            ps.setInt(1, chr.getAccountID());
            ps.setString(2, pqName);
            ResultSet rs = ps.executeQuery();

            int count = 0;

            while (rs.next()) {
                count++;

                int timesDone = rs.getInt("times");
                long lastTimestamp = rs.getTimestamp("lasttime").getTime();

                if (timesDone < 2) {
                    canPQ = true;

                    if (lastTimestamp + 86400000 <= System.currentTimeMillis()) {
                        ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM partyquest WHERE accid = ? AND pqname = ?");

                        ps.setInt(1, chr.getAccountID());
                        ps.setString(2, pqName);
                        ps.execute();
                    }
                } else if (lastTimestamp + 86400000 <= System.currentTimeMillis()) {
                    canPQ = true;

                    ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM partyquest WHERE accid = ? AND pqname = ?");

                    ps.setInt(1, chr.getAccountID());
                    ps.setString(2, pqName);
                    ps.execute();
                }

            }

            if (count == 0) {
                canPQ = true;
            }

            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return canPQ;
    }

    public void addOrUpdatePQ(MapleCharacter chr, String pqName) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM partyquest WHERE accid = ? AND pqname = ?");
            ps.setInt(1, chr.getAccountID());
            ps.setString(2, pqName);
            ResultSet rs = ps.executeQuery();

            int count = 0;
            int times = 0;

            while (rs.next()) {
                count++;

                times = rs.getInt("times");
            }

            if (count == 0) {
                PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement("INSERT INTO partyquest (accid, pqname, lasttime, times) VALUES (?, ?, CURRENT_TIMESTAMP(), ?)");
                ps2.setInt(1, chr.getAccountID());
                ps2.setString(2, pqName);
                ps2.setInt(3, 1);
                ps2.executeUpdate();

                ps2.close();
            } else {
                PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement("UPDATE partyquest SET lasttime = CURRENT_TIMESTAMP(), times = ? WHERE accid = ? AND pqname = ?");
                times++;

                ps2.setInt(1, times);
                ps2.setInt(2, chr.getAccountID());
                ps2.setString(3, pqName);

                ps2.executeUpdate();

                ps2.close();
            }

            ps.close();
            rs.close();

        } catch (SQLException e) {

        }
    }

    public String getWaitTime(MapleCharacter chr, String pqName) {
        long timestamp = 0;

        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM partyquest WHERE accid = ? AND pqname = ?");
            ps.setInt(1, chr.getAccountID());
            ps.setString(2, pqName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                timestamp = rs.getTimestamp("lasttime").getTime() + 86400000;
            }

            ps.close();
            rs.close();

        } catch (SQLException e) {
        }

        return msToString(timestamp - System.currentTimeMillis());
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
