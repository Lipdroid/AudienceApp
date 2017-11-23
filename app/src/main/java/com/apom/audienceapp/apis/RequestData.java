package com.apom.audienceapp.apis;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.apom.audienceapp.utils.Constants;
import com.apom.audienceapp.utils.UrlConstants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Request and get data from API
 *
 * @author PhanTri
 */
public class RequestData {
    private JsonParser mJsonParser = null;
    private String REQUEST_DATA_URL = null;
    private int mRestType = 0;
    private Context mContex = null;

    public RequestData(Context context) {
        mJsonParser = new JsonParser();
        mContex = context;
    }

    /**
     * TODO <br>
     * Function to get data
     *
     * @return json in string
     * @author Phan Tri
     * @date Oct 15, 2014
     */
    @SuppressWarnings("unchecked")
    public String getData(int typeOfRequest, final HashMap<String, Object> parameters) {
        ArrayList<Object> listParams = new ArrayList<Object>();
        ArrayList<NameValuePair> nameValueParams = new ArrayList<NameValuePair>();
        ArrayList<Map.Entry<String, Bitmap>> bitmapParams = new ArrayList<Map.Entry<String, Bitmap>>();
        JSONObject returnJson = null;

        switch (typeOfRequest) {

            case Constants.REQUEST_REGISTER_USER:
                mRestType = Constants.REST_POST;
                REQUEST_DATA_URL = UrlConstants.BASE_URL;

                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
                        (String) parameters.get(Constants.PARAM_TAG)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_ID,
                        (String) parameters.get(Constants.PARAM_ID)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_EMAIL,
                        (String) parameters.get(Constants.PARAM_EMAIL)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_NAME,
                        (String) parameters.get(Constants.PARAM_NAME)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_EMAIL,
                        (String) parameters.get(Constants.PARAM_EMAIL)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_IMAGE_URL,
                        (String) parameters.get(Constants.PARAM_IMAGE_URL)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MOBILE,
                        (String) parameters.get(Constants.PARAM_MOBILE)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TYPE,
                        (String) parameters.get(Constants.PARAM_TYPE)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_INDUSTRY,
                        (String) parameters.get(Constants.PARAM_INDUSTRY)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_COMPANY,
                        (String) parameters.get(Constants.PARAM_COMPANY)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_JOB_TITLE,
                        (String) parameters.get(Constants.PARAM_JOB_TITLE)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_JOB_SUMMARY,
                        (String) parameters.get(Constants.PARAM_JOB_SUMMARY)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_LOCATION,
                        (String) parameters.get(Constants.PARAM_LOCATION)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_STATUS,
                        (String) parameters.get(Constants.PARAM_STATUS)));

                break;

            case Constants.REQUEST_ADD_MEETING:
                mRestType = Constants.REST_POST;
                REQUEST_DATA_URL = UrlConstants.BASE_URL;

                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
                        (String) parameters.get(Constants.PARAM_TAG)));

                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_EXPERT_ID,
                        (String) parameters.get(Constants.PARAM_EXPERT_ID)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_EXPERT_NAME,
                        (String) parameters.get(Constants.PARAM_EXPERT_NAME)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_EXPERT_IMAGE_URL,
                        (String) parameters.get(Constants.PARAM_EXPERT_IMAGE_URL)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_EXPERT_APPROVAL,
                        (String) parameters.get(Constants.PARAM_EXPERT_APPROVAL)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_CLIENT_ID,
                        (String) parameters.get(Constants.PARAM_CLIENT_ID)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_CLIENT_NAME,
                        (String) parameters.get(Constants.PARAM_CLIENT_NAME)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_CLIENT_IMAGE_URL,
                        (String) parameters.get(Constants.PARAM_CLIENT_IMAGE_URL)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_CLIENT_APPROVAL,
                        (String) parameters.get(Constants.PARAM_CLIENT_APPROVAL)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_ADMIN_APPROVAL,
                        (String) parameters.get(Constants.PARAM_ADMIN_APPROVAL)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MEETING_TIME,
                        (String) parameters.get(Constants.PARAM_MEETING_TIME)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MEETING_VENUE,
                        (String) parameters.get(Constants.PARAM_MEETING_VENUE)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MEETING_PURPOSE,
                        (String) parameters.get(Constants.PARAM_MEETING_PURPOSE)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MEETING_EXPECTATIO_ONE,
                        (String) parameters.get(Constants.PARAM_MEETING_EXPECTATIO_ONE)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MEETING_EXPECTATIO_TWO,
                        (String) parameters.get(Constants.PARAM_MEETING_EXPECTATIO_TWO)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MEETING_EXPECTATIO_THREE,
                        (String) parameters.get(Constants.PARAM_MEETING_EXPECTATIO_THREE)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MEETING_TIME_CHANGED,
                        (String) parameters.get(Constants.PARAM_MEETING_TIME_CHANGED)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MEETING_VENUE_CHANGED,
                        (String) parameters.get(Constants.PARAM_MEETING_VENUE_CHANGED)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MEETING_REVIEW,
                        (String) parameters.get(Constants.PARAM_MEETING_REVIEW)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_APPROVE_MESSAGE,
                        (String) parameters.get(Constants.PARAM_APPROVE_MESSAGE)));
                break;


            case Constants.REQUEST_GET_USER_BY_ID:
                mRestType = Constants.REST_POST;
                REQUEST_DATA_URL = UrlConstants.BASE_URL;

                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
                        (String) parameters.get(Constants.PARAM_TAG)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_ID,
                        (String) parameters.get(Constants.PARAM_ID)));

                break;

            case Constants.REQUEST_GET_ALL_MEETINGS_BY_EXPERT_ID:
                mRestType = Constants.REST_POST;
                REQUEST_DATA_URL = UrlConstants.BASE_URL;

                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
                        (String) parameters.get(Constants.PARAM_TAG)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_ID,
                        (String) parameters.get(Constants.PARAM_ID)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TYPE,
                        (String) parameters.get(Constants.PARAM_TYPE)));
                break;
            case Constants.REQUEST_GET_ALL_MEETINGS:
                mRestType = Constants.REST_POST;
                REQUEST_DATA_URL = UrlConstants.BASE_URL;

                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
                        (String) parameters.get(Constants.PARAM_TAG)));
                break;


            case Constants.REQUEST_UPDATE_APPROVE_MESSAGE:
                mRestType = Constants.REST_POST;
                REQUEST_DATA_URL = UrlConstants.BASE_URL;

                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
                        (String) parameters.get(Constants.PARAM_TAG)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_ID,
                        (String) parameters.get(Constants.PARAM_ID)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_APPROVE_MESSAGE,
                        (String) parameters.get(Constants.PARAM_APPROVE_MESSAGE)));
                break;
            case Constants.REQUEST_UPDATE_REVIEW_MESSAGE:
                mRestType = Constants.REST_POST;
                REQUEST_DATA_URL = UrlConstants.BASE_URL;

                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
                        (String) parameters.get(Constants.PARAM_TAG)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_ID,
                        (String) parameters.get(Constants.PARAM_ID)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MEETING_REVIEW,
                        (String) parameters.get(Constants.PARAM_MEETING_REVIEW)));
                break;
            case Constants.REQUEST_UPDATE_MEETING_BY_ID:
                mRestType = Constants.REST_POST;
                REQUEST_DATA_URL = UrlConstants.BASE_URL;

                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
                        (String) parameters.get(Constants.PARAM_TAG)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_ID,
                        (String) parameters.get(Constants.PARAM_ID)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TYPE,
                        (String) parameters.get(Constants.PARAM_TYPE)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_STATUS,
                        (String) parameters.get(Constants.PARAM_STATUS)));
                break;

//
//            case Constants.REQUEST_UPDATE_PROFILE:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.UPDATE_PROFILE;
//
//                if (parameters.containsKey(Constants.PARAM_POST_IMAGE)) {
//                    // create hash map to save avatar bitmap
//                    Map.Entry<String, Bitmap> hashIcon = new Map.Entry<String, Bitmap>() {
//
//                        @Override
//                        public String getKey() {
//                            // TODO Auto-generated method stub
//                            return Constants.PARAM_POST_IMAGE;
//                        }
//
//                        @Override
//                        public Bitmap getValue() {
//                            // TODO Auto-generated method stub
//                            return (Bitmap) parameters.get(Constants.PARAM_POST_IMAGE);
//                        }
//
//                        @Override
//                        public Bitmap setValue(Bitmap object) {
//                            // TODO Auto-generated method stub
//                            return (Bitmap) parameters.get(Constants.PARAM_POST_IMAGE);
//                        }
//                    };
//
//                    bitmapParams.add(hashIcon);
//                }
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//
//                break;
//
//            case Constants.REQUEST_SUBMIT_POST:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.BASE_URL;
////                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
////                        (String) parameters.get(Constants.PARAM_TAG)));
////                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_POST_TITLE,
////                        (String) parameters.get(Constants.PARAM_POST_TITLE)));
////                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_ADDRESS,
////                        (String) parameters.get(Constants.PARAM_ADDRESS)));
////                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_RATE,
////                        (String) parameters.get(Constants.PARAM_RATE)));
////                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_POST_ORGANIZER,
////                        (String) parameters.get(Constants.PARAM_POST_ORGANIZER)));
////                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_POST_ORGANIZER_ID,
////                        (String) parameters.get(Constants.PARAM_POST_ORGANIZER_ID)));
////                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_POST_TIME,
////                        (String) parameters.get(Constants.PARAM_POST_TIME)));
////                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_POST_DATE,
////                        (String) parameters.get(Constants.PARAM_POST_DATE)));
////                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_POST_CONTACT_INFO,
////                        (String) parameters.get(Constants.PARAM_POST_CONTACT_INFO)));
////                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_POST_DESCRIPTION,
////                        (String) parameters.get(Constants.PARAM_POST_DESCRIPTION)));
////
////                if (parameters.containsKey(Constants.PARAM_POST_IMAGE)) {
////                    // create hash map to save avatar bitmap
////                    Map.Entry<String, Bitmap> hashIcon = new Map.Entry<String, Bitmap>() {
////
////                        @Override
////                        public String getKey() {
////                            // TODO Auto-generated method stub
////                            return Constants.PARAM_POST_IMAGE;
////                        }
////
////                        @Override
////                        public Bitmap getValue() {
////                            // TODO Auto-generated method stub
////                            return (Bitmap) parameters.get(Constants.PARAM_POST_IMAGE);
////                        }
////
////                        @Override
////                        public Bitmap setValue(Bitmap object) {
////                            // TODO Auto-generated method stub
////                            return (Bitmap) parameters.get(Constants.PARAM_POST_IMAGE);
////                        }
////                    };
////
////                    bitmapParams.add(hashIcon);
////                }
//                break;
//
            case Constants.REQUEST_GET_ALL_USERS:
                mRestType = Constants.REST_POST;
                REQUEST_DATA_URL = UrlConstants.BASE_URL;
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
                        (String) parameters.get(Constants.PARAM_TAG)));
                break;

            case Constants.REQUEST_UPDATE_USER_BY_ID:
                mRestType = Constants.REST_POST;
                REQUEST_DATA_URL = UrlConstants.BASE_URL;
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TAG,
                        (String) parameters.get(Constants.PARAM_TAG)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_ID,
                        (String) parameters.get(Constants.PARAM_ID)));
                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_STATUS,
                        (String) parameters.get(Constants.PARAM_STATUS)));
                break;

//
//
//            case Constants.REQUEST_GET_USER_TYPE:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.GET_USER_TYPE_URL;
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//                break;
//
//            case Constants.REQUEST_GET_PROMOCODE_LIST:
//                mRestType = Constants.REST_GET;
//                REQUEST_DATA_URL = ConstantURLS.GET_MERCHANT_PROMOCODE_LIST_URL;
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//                break;
//            case Constants.REQUEST_GET_PROMOCODE_LIST_BY_DATE:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.GET_PROMOCODE_LIST_BY_DATE_URL_USER;
//
//
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_START_DATE,
//                        (String) parameters.get(Constants.PARAM_START_DATE)));
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_END_DATE,
//                        (String) parameters.get(Constants.PARAM_END_DATE)));
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//                break;
//            case Constants.REQUEST_GET_PROMOCODE:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.GET_PROMO_URL;
//
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MERHCANT_ID,
//                        (String) parameters.get(Constants.PARAM_MERHCANT_ID)));
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_PRODUCT_ID,
//                        (String) parameters.get(Constants.PARAM_PRODUCT_ID)));
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_VALIDITY,
//                        (String) parameters.get(Constants.PARAM_VALIDITY)));
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_DISCOUNT,
//                        (String) parameters.get(Constants.PARAM_DISCOUNT)));
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//                break;
//
//            case Constants.REQUEST_CHANGE_STATUS:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.URL_CHANGE_STATUS;
//
//
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_PROMO_ID,
//                        (String) parameters.get(Constants.PARAM_PROMO_ID)));
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_STATUS,
//                        (String) parameters.get(Constants.PARAM_STATUS)));
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//                break;
//            case Constants.REQUEST_GET_PROMOCODE_DETAILS:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.GET_PROMOCODE_DETAILS;
//
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_MERHCANT_ID,
//                        (String) parameters.get(Constants.PARAM_MERHCANT_ID)));
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_PRODUCT_ID,
//                        (String) parameters.get(Constants.PARAM_PRODUCT_ID)));
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_VALIDITY,
//                        (String) parameters.get(Constants.PARAM_VALIDITY)));
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_DISCOUNT,
//                        (String) parameters.get(Constants.PARAM_DISCOUNT)));
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//                break;
//            case Constants.REQUEST_FORGOT_PASSWORD:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.FORGOT_PASSWORD;
//
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_EMAIL,
//                        (String) parameters.get(Constants.PARAM_EMAIL)));
//
//                break;
//
//            case Constants.REQUEST_RESET_PASSWORD:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.RESET_PASSWORD;
//
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_PASSWORD,
//                        (String) parameters.get(Constants.PARAM_PASSWORD)));
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_CONFIRM_PASSWORD,
//                        (String) parameters.get(Constants.PARAM_CONFIRM_PASSWORD)));
//
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//
//                break;
//
//
//            case Constants.REQUEST_VALIDATE_PROMO:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.URL_VALIDATE_PROMO;
//
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_PROMO_ID_V2,
//                        (String) parameters.get(Constants.PARAM_PROMO_ID_V2)));
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//
//                break;
//            case Constants.REQUEST_REMOVE_PROMO:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.URL_REMOVE_PROMO;
//
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_PROMO_ID_V2,
//                        (String) parameters.get(Constants.PARAM_PROMO_ID_V2)));
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//
//                break;
//
//
//            case Constants.REQUEST_SOCIAL_LOGIN:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.URL_SOCIAL_LOGIN;
//
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_TOKEN,
//                        (String) parameters.get(Constants.PARAM_TOKEN)));
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_PROVIDER,
//                        (String) parameters.get(Constants.PARAM_PROVIDER)));
//
//                break;
//            case Constants.REQUEST_USER_TYPE_UPDATE:
//                mRestType = Constants.REST_POST;
//                REQUEST_DATA_URL = ConstantURLS.URL_UPDATE_USER_TYPE;
//
//                nameValueParams.add(new BasicNameValuePair(Constants.PARAM_CATEGORY,
//                        (String) parameters.get(Constants.PARAM_CATEGORY)));
//
//                GlobalUtils.addAditionalHeader = true;
//                GlobalUtils.additionalHeaderTag = "Authorization";
//                GlobalUtils.additionalHeaderValue = "bearer " + SharedPreferencesUtils.getString(mContex, Constants.TOKEN, null);
//
//                break;

            default:
                break;
        }

        listParams.add(nameValueParams);
        listParams.add(bitmapParams);
        // Building Parameters
        Log.e("Request URL:", REQUEST_DATA_URL);
        returnJson = mJsonParser.getJSONFromUrl(REQUEST_DATA_URL, mRestType, listParams);

        return (returnJson != null) ? returnValues(returnJson) : null;
    }

    /**
     * TODO <br>
     * Function to return values from server after check <br>
     *
     * @param returnObj
     * @return
     * @author Phan Tri
     * @date Oct 15, 2014
     */
    public String returnValues(JSONObject returnObj) {
        return returnObj.toString();
    }
}
