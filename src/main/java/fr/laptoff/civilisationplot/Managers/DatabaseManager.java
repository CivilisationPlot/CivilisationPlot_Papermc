package fr.laptoff.civilisationplot.Managers;

import fr.laptoff.civilisationplot.CivilisationPlot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DatabaseManager {

    private Connection co;
    private final CivilisationPlot plugin = CivilisationPlot.getInstance();

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



    public boolean isConnected()
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
}
