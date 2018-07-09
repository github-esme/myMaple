/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author John
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLQuery {

    public boolean executeQuery(String query) {
        PreparedStatement ps = null;

        try {
            ps = DatabaseConnection.getConnection().prepareStatement(query);
            ps.executeQuery();

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

        return false;
    }

    public boolean updateQuery(String query) {
        PreparedStatement ps = null;

        try {
            ps = DatabaseConnection.getConnection().prepareStatement(query);
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

        return false;
    }

    public String getStringQuery(String query, String paramter) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = DatabaseConnection.getConnection().prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString(paramter);
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

        return null;
    }

    public Integer getIntQuery(String query, String paramter) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = DatabaseConnection.getConnection().prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(paramter);
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

        return null;
    }

    public long getTimestampQuery(String query, String paramter) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = DatabaseConnection.getConnection().prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getTimestamp(paramter).getTime();
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

        return -1;
    }
}
