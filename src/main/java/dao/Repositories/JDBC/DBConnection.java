package dao.Repositories.JDBC;


import commons.Configuration;
import commons.Constants;
import jakarta.inject.Inject;

import lombok.Data;


import java.sql.*;
@Data

public class DBConnection {
    private Configuration config;

    @Inject
    public DBConnection(Configuration config) {
        this.config = config;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(config.getProperty("urlDB"), config.getProperty("user_name"), config.getProperty("password"));
        IO.println(Constants.connected);
        return conn;
    }

    public void closeConnection(Connection connArg){
        System.out.println(Constants.closingconnection);
        try {
            connArg.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void releaseResource(PreparedStatement ps){
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void releaseResource(ResultSet rs){
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void releaseResource (Statement st){
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

