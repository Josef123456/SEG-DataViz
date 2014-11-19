package com.team1_k.project.seg.dataviz.model;

/**
 * Created by alexstoick on 11/17/14.
 */
public class Client {

    private Type type;

    Client(Type type) {
        this.type = type ;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        INVESTOR(new String[]{"GDP", "UNEMPLOYMENT"}), STUDENT(new String[]{"MINWAGE"});

        String[] interests;

        Type(String[] interests) {
            this.interests = interests ;
        }

        String[] getInterests() {
            return interests ;
        }
    }
}
