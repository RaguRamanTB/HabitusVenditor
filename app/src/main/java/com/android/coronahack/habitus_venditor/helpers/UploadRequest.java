package com.android.coronahack.habitus_venditor.helpers;

import java.util.List;

public class UploadRequest {
    public String shopName, customerName, phNum, customerAddress, prescriptionLink = "", timeSlot;
    public int mKey;
    public List<EnterMeds> mList;

    public UploadRequest() {

    }

    public UploadRequest(String shopName, String customerName, String phNum, String customerAddress, List<EnterMeds> mList, int mKey, String timeSlot) {
        this.shopName = shopName;
        this.customerName = customerName;
        this.phNum = phNum;
        this.customerAddress = customerAddress;
        this.mList = mList;
        this.mKey = mKey;
        this.timeSlot = timeSlot;
    }

    public UploadRequest(String shopName, String customerName, String phNum, String customerAddress, String prescriptionLink, List<EnterMeds> mList, int mKey, String timeSlot) {
        this.shopName = shopName;
        this.customerName = customerName;
        this.phNum = phNum;
        this.customerAddress = customerAddress;
        this.prescriptionLink = prescriptionLink;
        this.mList = mList;
        this.mKey = mKey;
        this.timeSlot = timeSlot;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhNum() {
        return phNum;
    }

    public void setPhNum(String phNum) {
        this.phNum = phNum;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getPrescriptionLink() {
        return prescriptionLink;
    }

    public void setPrescriptionLink(String prescriptionLink) {
        this.prescriptionLink = prescriptionLink;
    }

    public int getmKey() {
        return mKey;
    }

    public void setmKey(int mKey) {
        this.mKey = mKey;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public List<EnterMeds> getmList() {
        return mList;
    }

    public void setmList(List<EnterMeds> mList) {
        this.mList = mList;
    }
}
