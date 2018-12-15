
package com.mushfiqussalehin.roomie.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmptyRoomResponse implements Parcelable
{

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
    private String timeStamp;
    @SerializedName("isbooked")
    @Expose
    private String isbooked;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("courseCode")
    @Expose
    private String courseCode;
    public final static Creator<EmptyRoomResponse> CREATOR = new Creator<EmptyRoomResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public EmptyRoomResponse createFromParcel(Parcel in) {
            return new EmptyRoomResponse(in);
        }

        public EmptyRoomResponse[] newArray(int size) {
            return (new EmptyRoomResponse[size]);
        }

    }
    ;

    protected EmptyRoomResponse(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.roomNo = ((String) in.readValue((String.class.getClassLoader())));
        this.day = ((String) in.readValue((String.class.getClassLoader())));
        this.time = ((String) in.readValue((String.class.getClassLoader())));
        this.timeStamp = ((String) in.readValue((String.class.getClassLoader())));
        this.isbooked = ((String) in.readValue((String.class.getClassLoader())));
        this.contactNo = ((String) in.readValue((String.class.getClassLoader())));
        this.courseCode = ((String) in.readValue((String.class.getClassLoader())));
    }

    public EmptyRoomResponse() {
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(roomNo);
        dest.writeValue(day);
        dest.writeValue(time);
        dest.writeValue(timeStamp);
        dest.writeValue(isbooked);
        dest.writeValue(contactNo);
        dest.writeValue(courseCode);
    }

    public int describeContents() {
        return  0;
    }

}
