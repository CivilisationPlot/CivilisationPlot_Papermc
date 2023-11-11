package fr.laptoff.civilisationplot.Managers;

import fr.laptoff.civilisationplot.CivilisationPlot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private Connection co;
    private final CivilisationPlot plugin = CivilisationPlot.getInstance();

    public Connection getConnection()
    {
        return co;
    }

    public void connection()
    {
        if(!isOnline())
        {
            try
            {
                Class.forName("org.mariadb.jdbc.Driver");
                co = DriverManager.getConnection("jdbc:" + plugin.getConfig().getString("database.Motor") + "://" + plugin.getConfig().getString("database.Host") + ":" + plugin.getConfig().getString("database.Port") + "/" + plugin.getConfig().getString("database.Database_Name"), plugin.getConfig().getString("database.User"), plugin.getConfig().getString("database.Password"));
                return;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deconnection()
    {
        if (isOnline())
        {
            try
            {
                co.close();
                return;
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
    }



    public boolean isOnline()
    {
        if (!plugin.getConfig().getBoolean("database.enable"))
            return false;

        try {
            return co != null && !co.isClosed();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

}
