package com.apom.audienceapp.objects;

import java.util.List;

/**
 * Created by mdmunirhossain on 11/2/17.
 */

public class UserObject {
    private String linked_in_id = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String profile_image_url = null;
    private String industry = null;
    private String mobile = null;
    private String category = null;
    private String status = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private List<JobObject> jobsList = null;

    public List<JobObject> getJobsList() {
        return jobsList;
    }

    public void setJobsList(List<JobObject> jobsList) {
        this.jobsList = jobsList;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getLinked_in_id() {
        return linked_in_id;
    }

    public void setLinked_in_id(String linked_in_id) {
        this.linked_in_id = linked_in_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
