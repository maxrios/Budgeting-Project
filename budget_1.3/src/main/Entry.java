package main;

public class Entry {

    private String name;
    private double amount;
    private static int numOfEnt = 1;

    Entry() {
        this("entry" + numOfEnt, 0.0);
    }

    Entry(String name) {
        this(name, 0.0);
    }

    Entry(String name, double amount) {
        this.name = name;
        this.amount = amount;
        numOfEnt++;
    }

    public void setName(String name) {
        if(name.equals(""))
            this.name = "entry" + numOfEnt;
        else
            this.name = name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format(name + " at $%.2f a month", amount);
    }
}
