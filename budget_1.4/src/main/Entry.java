package main;

/**
 * @author Maximiliano Merced Rios
 */
public class Entry {

    private String name;
    private double amount;
    private static int numOfEnt = 1;

    Entry() {
        this("entry" + numOfEnt, 0.0);
    }

    Entry(String name, double amount) {
        this.name = name;
        this.amount = amount;
        numOfEnt++;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        if(name.equals(""))
            this.name = "entry" + numOfEnt;
        else
            this.name = name;
    }

    /**
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns monthly amount based on given amount.
     * @return Entry statement
     */
    @Override
    public String toString() {
        return String.format(name + " at $%.2f a month", amount);
    }
}
