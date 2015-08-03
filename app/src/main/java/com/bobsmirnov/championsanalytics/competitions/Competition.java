package com.bobsmirnov.championsanalytics.competitions;

/**
 * Created by bobsmirnov on 31.07.15.
 */
public abstract class Competition {

    public String viewName;
    public String dbName;
    public int priority;

    protected Competition(String viewName, String dbName, int priority) {
        this.viewName = viewName;
        this.dbName = dbName;
        this.priority = priority;
    }
}
