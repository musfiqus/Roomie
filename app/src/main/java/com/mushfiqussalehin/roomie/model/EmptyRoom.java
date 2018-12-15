package com.mushfiqussalehin.roomie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmptyRoom implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("roomNo")
    @Expose
    private String roomNo;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("timeStamp")
    @Expose
    private long timeStamp;
    @SerializedName("isbooked")
    @Expose
    private String isbooked;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("courseCode")
    @Expose
    private String courseCode;

    public EmptyRoom(String id, String roomNo, String day, String time, long timeStamp, String isbooked, String contactNo, String courseCode) {
        this.id = id;
        this.roomNo = roomNo;
        this.day = day;
        this.time = time;
        this.timeStamp = timeStamp;
        this.isbooked = isbooked;
        this.contactNo = contactNo;
        this.courseCode = courseCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getIsbooked() {
        return isbooked;
    }

    public void setIsbooked(String isbooked) {
        this.isbooked = isbooked;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.roomNo);
        dest.writeString(this.day);
        dest.writeString(this.time);
        dest.writeLong(this.timeStamp);
        dest.writeString(this.isbooked);
        dest.writeString(this.contactNo);
        dest.writeString(this.courseCode);
    }

    protected EmptyRoom(Parcel in) {
        this.id = in.readString();
        this.roomNo = in.readString();
        this.day = in.readString();
        this.time = in.readString();
        this.timeStamp = in.readLong();
        this.isbooked = in.readString();
        this.contactNo = in.readString();
        this.courseCode = in.readString();
    }

    public static final Parcelable.Creator<EmptyRoom> CREATOR = new Parcelable.Creator<EmptyRoom>() {
        @Override
        public EmptyRoom createFromParcel(Parcel source) {
            return new EmptyRoom(source);
        }

        @Override
        public EmptyRoom[] newArray(int size) {
            return new EmptyRoom[size];
        }
    };
}
