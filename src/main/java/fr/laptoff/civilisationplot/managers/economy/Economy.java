package fr.laptoff.civilisationplot.managers.economy;

import fr.laptoff.civilisationplot.CivilisationPlot;

import java.util.Objects;

public class Economy {

    public static boolean isEnable(){
        return CivilisationPlot.getInstance().getConfig().getBoolean("Economy.enable");
    }

    public static float getStartMoneyBalance(){
        return Float.parseFloat(Objects.requireNonNull(CivilisationPlot.getInstance().getConfig().getString("Economy.start_balance")));
    }

    public static String getCurrencyName(){
        return CivilisationPlot.getInstance().getConfig().getString("Economy.currency_name");
    }

    public static String getCurrencySymbol(){
        return CivilisationPlot.getInstance().getConfig().getString("Economy.currency_symbol");
    }
}
