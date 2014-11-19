package com.team1_k.project.seg.dataviz.model;

/**
 * Created by alexstoick on 11/17/14.
 */
public class Client {

    Type type;

    public enum Type {
        INVESTOR(new String[]{"GDP", "UNEMPLOYMENT"}), STUDENT(new String[]{"MINWAGE"});

        String[] interests;

        Type(String[] interests) {
            this.interests = interests ;
        }
    }
}
