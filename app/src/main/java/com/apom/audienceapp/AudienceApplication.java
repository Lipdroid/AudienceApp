package com.apom.audienceapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.apom.audienceapp.utils.ImageUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;


/**
 * Created by lipuhossain on 1/25/17.
 */

public class AudienceApplication extends Application {

    private final static String LOG_TAG = Application.class.getSimpleName();
    private static Context sContext = null;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "Application.onCreate - Initializing application...");
        super.onCreate();
        sContext = AudienceApplication.this;
        new ImageUtils(sContext);
        Log.d(LOG_TAG, "Application.onCreate - Application initialized OK");

    }

    public static Context AudienceApplication() {
        return sContext;
    }

    // uncaught exception handler variable
    private Thread.UncaughtExceptionHandler defaultUEH;

    // handler listener
//    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
//            new Thread.UncaughtExceptionHandler() {
//                @Override
//                public void uncaughtException(Thread thread, Throwable ex) {
//
//                    // here I do logging of exception to a db
//                    PendingIntent myActivity = PendingIntent.getActivity(getApplicationContext(),
//                            192837, new Intent(getApplicationContext(), SplashActivity.class),
//                            PendingIntent.FLAG_ONE_SHOT);
//
//                    AlarmManager alarmManager;
//                    alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                            15000, myActivity );
//                    System.exit(2);
//
//                    // re-throw critical exception further to the os (important)
//                    defaultUEH.uncaughtException(thread, ex);
//                }
//            };
//
//    public LimeApplication() {
//        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
//        // setup handler for uncaught exception
//        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
//    }


}