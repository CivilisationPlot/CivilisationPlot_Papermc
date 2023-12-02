package fr.laptoff.civilisationplot.Managers;

import fr.laptoff.civilisationplot.CivilisationPlot;

import java.sql.*;
import java.util.Objects;

public class DatabaseManager {

    private static Connection co;
    private static final CivilisationPlot plugin = CivilisationPlot.getInstance();

    public Connection getConnection()
    {
        return co;
    }

    public void connection()
    {
        if(!isConnected())
        {
            try
            {
                String motorDriver = "org.mariadb";

                if (Objects.requireNonNull(plugin.getConfig().getString("Database.motor")).equalsIgnoreCase("mysql"))
                    motorDriver = "com.mysql.cj";

                Class.forName(motorDriver + ".jdbc.Driver"); //loading of the driver.
                co = DriverManager.getConnection("jdbc:" + plugin.getConfig().getString("Database.motor") + "://" + plugin.getConfig().getString("Database.host") + ":" + plugin.getConfig().getString("Database.port") + "/" + plugin.getConfig().getString("Database.database_name"), plugin.getConfig().getString("Database.user"), plugin.getConfig().getString("Database.password"));
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void disconnection()
    {
        if (isConnected())
        {
            try
            {
                co.close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
    }



    public static boolean isConnected()
    {
        try {
            return co != null && !co.isClosed() && plugin.getConfig().getBoolean("Database.enable");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isOnline() {
        try {
            return !(co == null) || !co.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean doesTableExist(String tableName) {
        try {
            DatabaseMetaData metaData = co.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, null);

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public void setup(){
        try {
            //create the civils table
            if (!doesTableExist("civils")){
                PreparedStatement pstmt = this.getConnection().prepareStatement("CREATE TABLE civils (id INT AUTO_INCREMENT PRIMARY KEY, uuid VARCHAR(50), name VARCHAR(50), money INT);");
                pstmt.execute();
            }

            //create the nations table
            if (!doesTableExist("nations")){
                PreparedStatement pstmt = this.getConnection().prepareStatement("CREATE TABLE nations (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), uuid VARCHAR(50), leader_uuid VARCHAR(50));");
                pstmt.execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
