package fr.laptoff.civilisationplot.permissions;

public enum Plots {

    PLOT_BREAK("plot.break", 1),
    PLOT_DESTROY("plot.destroy", 2),
    PLOT_MODIFY_PERMISSIONS("plot.modify.permissions", 3);

    private String Name;
    private int Id;

    private Plots(String name, int id){
        this.Name = name;
        this.Id = id;
    }

    public String getName(){
        return this.Name;
    }

    public int getId(){
        return this.Id;
    }

}
