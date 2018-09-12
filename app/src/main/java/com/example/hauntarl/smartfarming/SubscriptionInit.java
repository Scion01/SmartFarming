package com.example.hauntarl.smartfarming;

public class SubscriptionInit {
    public Integer isActive;
    public String typeOfSub;
    public String startFrom;
    public String validTill;
    public String TID;

    public SubscriptionInit(Integer isActive, String typeOfSub, String startFrom, String validTill, String TID) {
        this.isActive = isActive;
        this.typeOfSub = typeOfSub;
        this.startFrom = startFrom;
        this.validTill = validTill;
        this.TID = TID;
    }
}
