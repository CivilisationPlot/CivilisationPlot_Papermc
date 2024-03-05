package fr.laptoff.civilisationplot.managers.data;

import fr.laptoff.civilisationplot.CivilisationPlot;

import java.sql.*;

public class Database {

    private static Connection co;
    private static final CivilisationPlot plugin = CivilisationPlot.getInstance();

    public Connection getConnection(){
        return co;
    }

    public void connection(){
        if (!isConnected()){
            try{
                String motorDriver = "org.mariadb";

                if (plugin.getConfig().getString("database.motor") != null && plugin.getConfig().getString("database.motor").equalsIgnoreCase("mysql"))
                    motorDriver = "com.mysql.cj";

                Class.forName(motorDriver + ".jdbc.Driver"); //Driver loading
                co = DriverManager.getConnection("jdbc:" + plugin.getConfig().getString("database.motor") + "://" + plugin.getConfig().getString("database.host") + ":" + plugin.getConfig().getString("Database.port") + "/" + plugin.getConfig().getString("Database.database_name"), plugin.getConfig().getString("Database.user"), plugin.getConfig().getString("Database.password"));
            } catch(SQLException e){
                e.printStackTrace();
            } catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    public void disconnection(){
        if (isConnected()){
            try{
                co.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected(){
        try{
            return co != null && !co.isClosed() && plugin.getConfig().getBoolean("database.enable") && plugin.getDatabase() != null;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isOnline() {
        try {
            if (co != null)
                return co.isClosed();

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean doesTableExist(String tableName){
        try{
            DatabaseMetaData metaData = co.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, null);

            return resultSet.next();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void setup(){

        if (!isConnected())
            return;

        try{
            if (!doesTableExist("civils")){
                PreparedStatement pstmt = this.getConnection().prepareStatement("CREATE TABLE civils (id INT AUTO_INCREMENT PRIMARY KEY, uuid VARCHAR(50), json VARCHAR(50));");
                pstmt.execute();
            }

            if (!doesTableExist("nations")){
                PreparedStatement pstmt = this.getConnection().prepareStatement("CREATE TABLE nations (id INT AUTO_INCREMENT PRIMARY KEY, uuid VARCHAR(50), json VARCHAR(50));");
                pstmt.execute();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
