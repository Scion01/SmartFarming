package com.example.hauntarl.smartfarming;

public class transactionRequest {
    public String Address;
    public String productName;
    public String productDesc;
    public String cost;
    public Integer status;
    public String failedReason;

    public transactionRequest(String address, String productName, String productDesc, String cost, Integer status, String failedReason) {
        this.Address = address;
        this.productName = productName;
        this.productDesc = productDesc;
        this.cost = cost;
        this.status = status;
        this.failedReason = failedReason;
    }




}
