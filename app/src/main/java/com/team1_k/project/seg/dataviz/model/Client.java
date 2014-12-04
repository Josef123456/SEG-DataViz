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
        INVESTOR(
                new String[]{
                        "NY.GDP.MKTP.CD",
                        "SL.UEM.TOTL.ZS",
                        "IC.EXP.COST.CD",
                        "IC.IMP.COST.CD",
                        "NE.EXP.GNFS.KD.ZG",
                        "NE.IMP.GNFS.KD.ZG",
                        "FP.CPI.TOTL.ZG",
                        "SL.TLF.TOTL.IN",
                        "FR.INR.RINR"
                }
        ),
        STUDENT(new String[]{"MINWAGE"});

        String[] interests;

        Type(String[] interests) {
            this.interests = interests ;
        }

        public String[] getInterests() {
            return interests ;
        }
    }
}
