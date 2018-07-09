/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.events;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tools.DatabaseConnection;

/**
 *
 * @author John
 */
public class JQRankings {

    public int getFastestTime(int mapid) {
        int getFastestTime = -1;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT MIN(completiontime) AS CompletionTime FROM jqrankings WHERE mapid = ?");
            ps.setInt(1, mapid);
            rs = ps.executeQuery();

            if (rs.next()) {
                getFastestTime = rs.getInt("CompletionTime");
            }

            ps.close();
            rs.close();

        } catch (SQLException e) {

        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }

        return getFastestTime;
    }

    public String getNameOfFastestTime(int mapid) {
        String name = "";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT charname FROM jqrankings WHERE mapid = ? ORDER BY completiontime ASC LIMIT 1");
            ps.setInt(1, mapid);
            rs = ps.executeQuery();

            if (rs.next()) {
                name = rs.getString("charname");
            }

            ps.close();
            rs.close();

        } catch (SQLException e) {

        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }

        return name;
    }

    public int getTime(String name, int mapid) {
        int getTime = -1;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT MIN(completiontime) AS CompletionTime FROM jqrankings WHERE mapid = ? AND charname = ?");
            ps.setInt(1, mapid);
            ps.setString(2, name);
            rs = ps.executeQuery();

            if (rs.next()) {
                getTime = rs.getInt("CompletionTime");
            }

            ps.close();
            rs.close();

        } catch (SQLException e) {

        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }

        return getTime;
    }

    public boolean updateRanking(String name, int mapid, int time) {
        int lastTime = getTime(name, mapid);
        if (lastTime == -1 || lastTime == 0) {
            PreparedStatement ps = null;

            try {
                ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO jqrankings (charname, mapid, completiontime) VALUES (?, ?, ?)");
                ps.setString(1, name);
                ps.setInt(2, mapid);
                ps.setInt(3, time);
                ps.executeUpdate();

                ps.close();

                return true;

            } catch (SQLException e) {

            } finally {
                try {
                    if (ps != null && !ps.isClosed()) {
                        ps.close();
                    }
                } catch (SQLException e) {
                }
            }
        } else {
            PreparedStatement ps = null;

            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE jqrankings SET completiontime = ? WHERE charname = ? AND mapid = ?");
                ps.setInt(1, time);
                ps.setString(2, name);
                ps.setInt(3, mapid);
                ps.executeUpdate();

                ps.close();

                return true;

            } catch (SQLException e) {

            } finally {
                try {
                    if (ps != null && !ps.isClosed()) {
                        ps.close();
                    }
                } catch (SQLException e) {
                }
            }
        }

        return false;
    }
}
