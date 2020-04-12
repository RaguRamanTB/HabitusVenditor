package com.android.coronahack.habitus_venditor.helpers;

public class UploadTimeSlot {
    String name, timeSlot;
    int count;

    public UploadTimeSlot(String name, String timeSlot, int count) {
        this.name = name;
        this.timeSlot = timeSlot;
        this.count = count;
    }

    public UploadTimeSlot() {
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
