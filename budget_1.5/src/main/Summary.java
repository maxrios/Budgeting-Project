package main;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Maximiliano Merced Rios
 */
public class Summary implements Serializable {

    private String name;
    private ArrayList<String> entries;

    Summary() {
        this("summary");
    }

    Summary(String name) {
        if(name.equals(""))
            this.name = "summary";
        else
            this.name = name;
        entries = new ArrayList<>();
    }

    /**
     * Adds entry to summary
     * @param entry
     * @return boolean
     */
    public boolean addEntry(Entry entry) {
        return entries.add(entry.toString());
    }

    /**
     * Removes selected entry based on given index in summary list
     * @param index
     * @return entry
     */
    public String removeEntry(int index) {
        return entries.remove(index);
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        if(name.equals(""))
            this.name = "summary";
        else
            this.name = name;
    }

    /**
     * Retrieves all of the entries.
     * @return entries
     */
    public ArrayList<String> getEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * Checks if summary is empty.
     * @return boolean
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    //TODO: Modify to return different output. Refer to notes for potential modification notes.
    /**
     *
     * @return Summary statement
     */
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
