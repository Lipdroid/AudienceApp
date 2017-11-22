package com.apom.audienceapp.utils;

/**
 * Created by mdmunirhossain on 11/2/17.
 */

public class Constants {
    //for test purpose
    public static final String USER_ID_ADMIN = "61q3KGilX0";
    public static final String USER_ID_EXPERT = "61q3KGilX01";
    public static final String USER_ID_CLIENT = "61q3KGilX012";


    public static final String ALREADY_LOGGED_IN = "loggedIn";

    public static final int REST_POST = 0;
    public static final int REST_GET = 1;
    public static final int REST_PUT = 2;
    public static final int REST_DELETE = 3;

    public static final String AUTH_USERNAME = null;
    public static final String AUTH_PASSWORD = null;
    public static final int CONNECTION_TIME_OUT = 10000;

    public static final String PARAM_TAG = "tag";
    public static final String PARAM_ID = "id";
    public static final Object SIGN_UP_TAG = "sign_up";
    public static final String PARAM_NAME = "user_name";
    public static final String PARAM_EMAIL = "user_email";
    public static final String PARAM_IMAGE_URL = "user_image_url";
    public static final String PARAM_MOBILE = "user_mobile";
    public static final String PARAM_TYPE = "user_type";
    public static final String PARAM_INDUSTRY = "user_industry";
    public static final String PARAM_COMPANY = "user_company";
    public static final String PARAM_JOB_TITLE = "user_job_title";
    public static final String PARAM_JOB_SUMMARY = "user_job_summary";
    public static final String PARAM_LOCATION = "user_location";
    public static final String PARAM_STATUS = "status";


    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "user_name";
    public static final String TAG_EMAIL = "user_email";
    public static final String TAG_IMAGE_URL = "user_image_url";
    public static final String TAG_MOBILE = "user_mobile";
    public static final String TAG_TYPE = "user_type";
    public static final String TAG_INDUSTRY = "user_industry";
    public static final String TAG_COMPANY = "user_company";
    public static final String TAG_JOB_TITLE = "user_job_title";
    public static final String TAG_JOB_SUMMARY = "user_job_summary";
    public static final String TAG_LOCATION = "user_location";
    public static final String TAG_STATUS = "status";

    public static final String USER_STATUS_ACTIVE = "1";
    public static final String USER_STATUS_DEACTIVE = "0";

    public static final String USER_NOT_YET_ARROVED = "0";
    public static final String USER_ARROVED = "1";
    public static final String USER_REJECTED = "2";

    public static final String NOT_CHANGED_TIME = "0";
    public static final String NOT_CHANGED_VENUE = "1";

    public static final String USER_TYPE_CLIENT = "client";
    public static final String USER_TYPE_EXPERT = "expert";
    public static final String USER_TYPE_ADMIN = "admin";


    public static final String ID = "preference_id";

    public static final int REQUEST_REGISTER_USER = 1;
    public static final int REQUEST_GET_USER_BY_ID = 2;
    public static final int REQUEST_GET_ALL_USERS = 3;
    public static final int REQUEST_UPDATE_USER_BY_ID = 4;
    public static final int REQUEST_ADD_MEETING = 5;
    public static final int REQUEST_GET_ALL_MEETINGS_BY_EXPERT_ID = 6;
    public static final int REQUEST_UPDATE_MEETING_BY_ID = 7;
    public static final int REQUEST_GET_ALL_MEETINGS = 8;
    public static final int REQUEST_UPDATE_REVIEW_MESSAGE = 9;
    public static final int REQUEST_UPDATE_APPROVE_MESSAGE = 10;

    public static final String NOT_YET_REVIEWED = "Not yet reviewed";


    public static final String TAG_USER = "user";
    public static final String TAG_ERROR = "error";
    public static final String TAG_ERROR_MSG = "error_msg";

    public static final String TAG_GET_USER = "get_user_by_id";
    public static final String GET_ALL_USER_TAG = "get_all_users";
    public static final String TAG_UPDATE_USER_STATUS = "verify_user";
    public static final String TAG_GET_USER_EXPERT = "get_all_experts";
    public static final String MEETING_TAG = "add_meeting";
    public static final String TAG_GET_MEETINGS_EXPERT = "get_meeting_request_by_id";
    public static final String TAG_UPDATE_MEETING_STATUS = "status_update";
    public static final String TAG_GET_ALL_MEETINGS = "get_all_meetings";
    public static final String TAG_UPDATE_REVIEW_MESSAGE = "update_review_message";
    public static final String TAG_UPDATE_APPROVE_MESSAGE = "update_confirm_message";


    public static final String PARAM_EXPERT_ID = "expert_id";
    public static final String PARAM_EXPERT_NAME = "expert_name";
    public static final String PARAM_EXPERT_IMAGE_URL = "expert_image_url";
    public static final String PARAM_EXPERT_APPROVAL = "expert_approval";
    public static final String PARAM_CLIENT_ID = "client_id";
    public static final String PARAM_CLIENT_NAME = "client_name";
    public static final String PARAM_CLIENT_IMAGE_URL = "client_image_url";
    public static final String PARAM_CLIENT_APPROVAL = "client_approval";
    public static final String PARAM_ADMIN_APPROVAL = "admin_approval";
    public static final String PARAM_MEETING_TIME = "meeting_time";
    public static final String PARAM_MEETING_VENUE = "meeting_venue";
    public static final String PARAM_MEETING_PURPOSE = "meeting_purpose";
    public static final String PARAM_MEETING_EXPECTATIO_ONE = "meeting_expectation_one";
    public static final String PARAM_MEETING_EXPECTATIO_TWO = "meeting_expectation_two";
    public static final String PARAM_MEETING_EXPECTATIO_THREE = "meeting_expectation_three";
    public static final String PARAM_MEETING_TIME_CHANGED = "meeting_time_changed";
    public static final String PARAM_MEETING_VENUE_CHANGED = "meeting_venue_changed";
    public static final String PARAM_MEETING_REVIEW = "meeting_review";
    public static final String PARAM_APPROVE_MESSAGE = "approve_message";


    public static final String TAG_EXPERT_ID = "expert_id";
    public static final String TAG_EXPERT_NAME = "expert_name";
    public static final String TAG_EXPERT_IMAGE_URL = "expert_image_url";
    public static final String TAG_EXPERT_APPROVAL = "expert_approval";
    public static final String TAG_CLIENT_ID = "client_id";
    public static final String TAG_CLIENT_NAME = "client_name";
    public static final String TAG_CLIENT_IMAGE_URL = "client_image_url";
    public static final String TAG_CLIENT_APPROVAL = "client_approval";
    public static final String TAG_ADMIN_APPROVAL = "admin_approval";
    public static final String TAG_MEETING_TIME = "meeting_time";
    public static final String TAG_MEETING_VENUE = "meeting_venue";
    public static final String TAG_MEETING_PURPOSE = "meeting_purpose";
    public static final String TAG_MEETING_EXPECTATIO_ONE = "meeting_expectation_one";
    public static final String TAG_MEETING_EXPECTATIO_TWO = "meeting_expectation_two";
    public static final String TAG_MEETING_EXPECTATIO_THREE = "meeting_expectation_three";
    public static final String TAG_MEETING_TIME_CHANGED = "meeting_time_changed";
    public static final String TAG_MEETING_VENUE_CHANGED = "meeting_venue_changed";
    public static final String TAG_MEETING_REVIEW = "meeting_review";
    public static final String TAG_APPROVE_MESSAGE = "approve_message";

    public static final String TAG_MEETING = "meeting";

    public static final String DEFAULT_DATE_FORMAT = "EEE-dd-MMMM-yyyy";
    public static final String NOT_YET_MESSAGE = "No Message";

}
