package com.apom.audienceapp.utils;

import android.content.Context;

import com.apom.audienceapp.objects.JobObject;
import com.apom.audienceapp.objects.UserObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class APIUtils {
    public static Context sContext = null;

    public APIUtils(Context context) {
        sContext = context;
    }

    public static HashMap<String, Object> parseJSON(int type, String jsonString) {
        HashMap<String, Object> returnHashMap = new HashMap<String, Object>();
        try {
            JSONObject mainJsonObj = new JSONObject(jsonString);
            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();
            String error = new String();
            String result = new String();
            int code = 0;

            switch (type) {
                case Constants.REQUEST_REGISTER_USER:
                    if (mainJsonObj.has(Constants.TAG_USER)) {
                        if(mainJsonObj.getString("success").equals("1")){
                            UserObject userObj = null;
                            userObj = (UserObject) parseUser(mainJsonObj.getJSONObject(Constants.TAG_USER));
                            returnHashMap.put(Constants.TAG_USER, userObj);
                        }
                    }else{
                        if(mainJsonObj.getString("success").equals("0")){
                            returnHashMap.put(Constants.TAG_ERROR, mainJsonObj.getString(Constants.TAG_ERROR_MSG));
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
                 e.printStackTrace();
        }

        return returnHashMap;
    }
    private static UserObject parseUser(JSONObject src) {
        UserObject userObj = new UserObject();
        try {
            if (((JSONObject) src).has(Constants.TAG_ID)) {
                if (!((JSONObject) src).getString(Constants.TAG_ID).equals("null")) {
                    userObj.setLinked_in_id(((JSONObject) src).getString(Constants.TAG_ID));
                } else {
                    userObj.setLinked_in_id("");
                }

            }

            if (((JSONObject) src).has(Constants.TAG_NAME)) {
                if (!((JSONObject) src).getString(Constants.TAG_NAME).equals("null")) {
                    userObj.setFirstName(((JSONObject) src).getString(Constants.TAG_NAME));
                } else {
                    userObj.setFirstName("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_EMAIL)) {
                if (!((JSONObject) src).getString(Constants.TAG_EMAIL).equals("null")) {
                    userObj.setEmail(((JSONObject) src).getString(Constants.TAG_EMAIL));
                } else {
                    userObj.setEmail("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_IMAGE_URL)) {
                if (!((JSONObject) src).getString(Constants.TAG_IMAGE_URL).equals("null")) {
                    userObj.setProfile_image_url(((JSONObject) src).getString(Constants.TAG_IMAGE_URL));
                } else {
                    userObj.setProfile_image_url("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_MOBILE)) {
                if (!((JSONObject) src).getString(Constants.TAG_MOBILE).equals("null")) {
                    userObj.setMobile(((JSONObject) src).getString(Constants.TAG_MOBILE));
                } else {
                    userObj.setMobile("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_INDUSTRY)) {
                if (!((JSONObject) src).getString(Constants.TAG_INDUSTRY).equals("null")) {
                    userObj.setIndustry(((JSONObject) src).getString(Constants.TAG_INDUSTRY));
                } else {
                    userObj.setIndustry("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_TYPE)) {
                if (!((JSONObject) src).getString(Constants.TAG_TYPE).equals("null")) {
                    userObj.setCategory(((JSONObject) src).getString(Constants.TAG_TYPE));
                } else {
                    userObj.setCategory("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_STATUS)) {
                if (!((JSONObject) src).getString(Constants.TAG_STATUS).equals("null")) {
                    userObj.setStatus(((JSONObject) src).getString(Constants.TAG_STATUS));
                } else {
                    userObj.setStatus("");
                }
            }

            //joblisr
            List<JobObject> joblist = new ArrayList<>();
            JobObject currentJob = new JobObject();

            if (((JSONObject) src).has(Constants.TAG_JOB_TITLE)) {
                if (!((JSONObject) src).getString(Constants.TAG_JOB_TITLE).equals("null")) {
                    currentJob.setJob_title(((JSONObject) src).getString(Constants.TAG_JOB_TITLE));
                } else {
                    currentJob.setJob_title("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_JOB_SUMMARY)) {
                if (!((JSONObject) src).getString(Constants.TAG_JOB_SUMMARY).equals("null")) {
                    currentJob.setJob_summary(((JSONObject) src).getString(Constants.TAG_JOB_SUMMARY));
                } else {
                    currentJob.setJob_summary("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_COMPANY)) {
                if (!((JSONObject) src).getString(Constants.TAG_COMPANY).equals("null")) {
                    currentJob.setCompany_name(((JSONObject) src).getString(Constants.TAG_COMPANY));
                } else {
                    currentJob.setCompany_name("");
                }
            }

            if (((JSONObject) src).has(Constants.TAG_LOCATION)) {
                if (!((JSONObject) src).getString(Constants.TAG_LOCATION).equals("null")) {
                    currentJob.setLocation(((JSONObject) src).getString(Constants.TAG_LOCATION));
                } else {
                    currentJob.setLocation("");
                }
            }
            joblist.add(currentJob);
            userObj.setJobsList(joblist);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return userObj;
    }

}
