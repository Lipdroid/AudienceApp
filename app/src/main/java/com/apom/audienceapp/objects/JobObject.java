package com.apom.audienceapp.objects;

/**
 * Created by mdmunirhossain on 11/2/17.
 */

public class JobObject {
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
}
