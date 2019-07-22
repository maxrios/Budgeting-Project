package main;

import java.io.Serializable;

/**
 * @author Maximiliano Merced Rios
 */
public class Entry {

    private String name;
    private double amount;

    Entry() {
        this("entry", 0.0);
    }

    Entry(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        if(name.equals(""))
            this.name = "entry";
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
