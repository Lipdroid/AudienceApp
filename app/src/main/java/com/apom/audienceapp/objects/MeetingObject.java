package com.apom.audienceapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lipuhossain on 11/18/17.
 */

public class MeetingObject implements Parcelable {
    private String id = null;
    private String expert_id = null;
    private String expert_name = null;
    private String expert_approval = null;
    private String client_id = null;
    private String client_name = null;
    private String client_approval = null;
    private String admin_approval = null;
    private String meeting_time = null;
    private String meeting_venue = null;
    private String meeting_purpose = null;
    private String meeting_expectation_one = null;
    private String meeting_expectation_two = null;
    private String meeting_expectation_three = null;
    private String meeting_time_changed = null;
    private String meeting_venue_changed = null;
    private String meeting_review = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpert_id() {
        return expert_id;
    }

    public void setExpert_id(String expert_id) {
        this.expert_id = expert_id;
    }

    public String getExpert_name() {
        return expert_name;
    }

    public void setExpert_name(String expert_name) {
        this.expert_name = expert_name;
    }

    public String getExpert_approval() {
        return expert_approval;
    }

    public void setExpert_approval(String expert_approval) {
        this.expert_approval = expert_approval;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_approval() {
        return client_approval;
    }

    public void setClient_approval(String client_approval) {
        this.client_approval = client_approval;
    }

    public String getAdmin_approval() {
        return admin_approval;
    }

    public void setAdmin_approval(String admin_approval) {
        this.admin_approval = admin_approval;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public void setMeeting_time(String meeting_time) {
        this.meeting_time = meeting_time;
    }

    public String getMeeting_venue() {
        return meeting_venue;
    }

    public void setMeeting_venue(String meeting_venue) {
        this.meeting_venue = meeting_venue;
    }

    public String getMeeting_purpose() {
        return meeting_purpose;
    }

    public void setMeeting_purpose(String meeting_purpose) {
        this.meeting_purpose = meeting_purpose;
    }

    public String getMeeting_expectation_one() {
        return meeting_expectation_one;
    }

    public void setMeeting_expectation_one(String meeting_expectation_one) {
        this.meeting_expectation_one = meeting_expectation_one;
    }

    public String getMeeting_expectation_two() {
        return meeting_expectation_two;
    }

    public void setMeeting_expectation_two(String meeting_expectation_two) {
        this.meeting_expectation_two = meeting_expectation_two;
    }

    public String getMeeting_expectation_three() {
        return meeting_expectation_three;
    }

    public void setMeeting_expectation_three(String meeting_expectation_three) {
        this.meeting_expectation_three = meeting_expectation_three;
    }

    public String getMeeting_time_changed() {
        return meeting_time_changed;
    }

    public void setMeeting_time_changed(String meeting_time_changed) {
        this.meeting_time_changed = meeting_time_changed;
    }

    public String getMeeting_venue_changed() {
        return meeting_venue_changed;
    }

    public void setMeeting_venue_changed(String meeting_venue_changed) {
        this.meeting_venue_changed = meeting_venue_changed;
    }

    public String getMeeting_review() {
        return meeting_review;
    }

    public void setMeeting_review(String meeting_review) {
        this.meeting_review = meeting_review;
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.expert_id = in.readString();
        this.expert_name = in.readString();
        this.expert_approval = in.readString();
        this.client_id = in.readString();
        this.client_name = in.readString();
        this.client_approval = in.readString();
        this.admin_approval = in.readString();
        this.meeting_time = in.readString();
        this.meeting_venue = in.readString();
        this.meeting_purpose = in.readString();
        this.meeting_expectation_one = in.readString();
        this.meeting_expectation_two = in.readString();
        this.meeting_expectation_three = in.readString();
        this.meeting_time_changed = in.readString();
        this.meeting_venue_changed = in.readString();
        this.meeting_review = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(expert_id);
        parcel.writeString(expert_name);
        parcel.writeString(expert_approval);
        parcel.writeString(client_id);
        parcel.writeString(client_name);
        parcel.writeString(client_approval);
        parcel.writeString(admin_approval);
        parcel.writeString(meeting_time);
        parcel.writeString(meeting_venue);
        parcel.writeString(meeting_purpose);
        parcel.writeString(meeting_expectation_one);
        parcel.writeString(meeting_expectation_two);
        parcel.writeString(meeting_expectation_three);
        parcel.writeString(meeting_time);
        parcel.writeString(meeting_venue);
        parcel.writeString(meeting_review);
    }

    public MeetingObject() {
        super();
    }

    private MeetingObject(Parcel in) {
        this();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MeetingObject> CREATOR = new Creator<MeetingObject>() {
        @Override
        public MeetingObject createFromParcel(Parcel parcel) {
            return new MeetingObject(parcel);
        }

        @Override
        public MeetingObject[] newArray(int i) {
            return new MeetingObject[i];
        }
    };
}
