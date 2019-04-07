package fr.gsb.rv.dr.technique;

import java.sql.*;
import java.util.Properties;

public class Connexion {

    private final static String host = "localhost";
    private final static String user = "root";
    private final static String mdp = "";
    private final static String dbName = "gsbrv2";
    private final static String port = "3306";

    public static Connection getConnexion(){
        Connection conn = null;
        Properties props = new Properties();
        String url = "jdbc:mysql://"+host+":"+port+"/"+dbName;
        props.setProperty("user", user);
        props.setProperty("password", mdp);
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
