package com.apom.audienceapp.apis;

import android.app.Activity;
import android.content.Context;

import com.apom.audienceapp.R;
import com.apom.audienceapp.classes.MySafeAsyncTask;
import com.apom.audienceapp.interfaces.AsyncCallback;
import com.apom.audienceapp.utils.GlobalUtils;

import java.util.HashMap;


public class RequestAsyncTask extends MySafeAsyncTask<String> {
    private HashMap<String, Object> mParameters = null;
    private AsyncCallback mAsyncCallback = null;
    private int mTypeOfRequest = 0;
    private Context mContext = null;

    public RequestAsyncTask(Context context, int typeOfRequest, HashMap<String, Object> parameters, AsyncCallback asyncCallback) {
        mTypeOfRequest = typeOfRequest;
        mParameters = parameters;
        mAsyncCallback = asyncCallback;
        mContext = context;
    }

    @Override
    protected void onPreExecute() throws Exception {
        mAsyncCallback.progress();
    }

    @Override
    public String call() throws Exception {
        if (GlobalUtils.isNetworkConnected()) {
            RequestData getDataObj = new RequestData(mContext);
            return getDataObj.getData(mTypeOfRequest, mParameters);
        } else {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String title = mContext.getResources().getString(R.string.dialog_error_title);
                    String body = mContext.getResources().getString(R.string.dialog_no_internet_connection);
                    GlobalUtils.showInfoDialog(mContext, title, body, null, null);
                }
            });
            return null;
        }
    }

    @Override
    protected void onSuccess(String result) throws Exception {
        mAsyncCallback.done(result);
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        mAsyncCallback.onException(e);
    }

    @Override
    protected void onInterrupted(Exception e) {
        mAsyncCallback.onInterrupted(e);
    }

}
