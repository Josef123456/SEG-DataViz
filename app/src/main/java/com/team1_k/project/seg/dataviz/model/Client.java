package com.team1_k.project.seg.dataviz.model;

/**
 * Created by alexstoick on 11/17/14.
 */
public class Client {

    private Type type;

    public Client(Type type) {
        this.type = type ;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        INVESTOR(new String[]{"NY.GDP.MKTP.CD", "SL.UEM.TOTL.ZS"}), STUDENT(new String[]{"MINWAGE"});

        String[] interests;

        Type(String[] interests) {
            this.interests = interests ;
        }

        public String[] getInterests() {
            return interests ;
        }
    }
}
