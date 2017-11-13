package com.apom.audienceapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mdmunirhossain on 11/2/17.
 */

public class JobObject implements Parcelable {
    private String company_name = null;
    private String job_title = null;
    private String job_summary = null;
    private String location = null;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_summary() {
        return job_summary;
    }

    public void setJob_summary(String job_summary) {
        this.job_summary = job_summary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(company_name);
        dest.writeString(job_summary);
        dest.writeString(job_title);
        dest.writeString(location);
    }

    public void readFromParcel(Parcel in) {
        this.company_name = in.readString();
        this.job_summary = in.readString();
        this.job_title = in.readString();
        this.location = in.readString();

    }

    public JobObject() {
        super();
    }

    private JobObject(Parcel in) {
        this();
        readFromParcel(in);
    }

    // default creator for parcelable
    public static final Creator<JobObject> CREATOR = new Creator<JobObject>() {

        @Override
        public JobObject[] newArray(int size) {
            return new JobObject[size];
        }

        @Override
        public JobObject createFromParcel(Parcel source) {
            return new JobObject(source);
        }
    };
}
