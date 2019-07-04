package main;

import java.io.Serializable;
import java.util.ArrayList;

public class Summary implements Serializable {

    private String name;
    private ArrayList<String> entries;
    private static int numOfSum = 1;

    Summary() {
        this("summary" + numOfSum);
    }

    Summary(String name) {
        if(name.equals(""))
            this.name = "summary" + numOfSum;
        else
            this.name = name;
        numOfSum++;
        entries = new ArrayList<>();
    }

    Summary(String name, Entry entry) {
        this.name = name;
        numOfSum++;
        entries = new ArrayList<>();
        entries.add(entry.toString());
    }

    public boolean addEntry(Entry entry) {
        return entries.add(entry.toString());
    }

    public String removeEntry(int index) {
        return entries.remove(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.equals(""))
            this.name = "summary" + numOfSum;
        else
            this.name = name;
    }

    public ArrayList<String> getEntries() {
        return new ArrayList<>(entries);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public String toString() {
        if(entries.isEmpty())
            return "No entries.\n";
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for(String s : entries) {
            sb.append("[" + index + "] " + s + "\n");
            index++;
        }

        return sb.toString();
    }
}
