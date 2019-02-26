package com.aidan.log;

public class LogBean {

    public int level;
    public long time;
    public String tag = "";
    public String text = "";

    @Override
    public String toString() {
        StringBuffer returnThis = new StringBuffer();
        returnThis
                .append("\t\tlevel : ").append(level).append("\n")
                .append("\t\ttime : ").append(time).append("\n")
                .append("\t\ttag : ").append(tag).append("\n")
                .append("\t\ttext : ").append(text).append("\n");
        return returnThis.toString();
    }
}
