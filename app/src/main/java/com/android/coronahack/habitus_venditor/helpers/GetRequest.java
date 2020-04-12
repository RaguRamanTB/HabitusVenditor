package com.android.coronahack.habitus_venditor.helpers;

import java.util.List;

public class GetRequest {

    String getName, getPhNum, getAddress;
    List<EnterMeds> requestList;
    Boolean isPres;

    public GetRequest(String getName, String getPhNum, String getAddress, List<EnterMeds> requestList, Boolean isPres) {
        this.getName = getName;
        this.getPhNum = getPhNum;
        this.getAddress = getAddress;
        this.requestList = requestList;
        this.isPres = isPres;
    }

    public String getGetName() {
        return getName;
    }

    public void setGetName(String getName) {
        this.getName = getName;
    }

    public String getGetPhNum() {
        return getPhNum;
    }

    public void setGetPhNum(String getPhNum) {
        this.getPhNum = getPhNum;
    }

    public String getGetAddress() {
        return getAddress;
    }

    public void setGetAddress(String getAddress) {
        this.getAddress = getAddress;
    }

    public List<EnterMeds> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<EnterMeds> requestList) {
        this.requestList = requestList;
    }

    public Boolean getPres() {
        return isPres;
    }

    public void setPres(Boolean pres) {
        isPres = pres;
    }
}
