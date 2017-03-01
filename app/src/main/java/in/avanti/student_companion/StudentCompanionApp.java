package in.avanti.student_companion;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import in.avanti.student_companion.activities.LoginActivity;
import io.realm.Realm;

/**
 * Created by Pritamp Sukumar on 04/11/16.
 * Updated by Yashpal Meena on 11/02/17
 *
 * Main Application class for maintaining global state (variables that need to be accessed
 * across the application
 */

public class StudentCompanionApp extends Application {

    /**
     * Log or request TAG
     */
    public static final String TAG = "StudentCompanionApp";

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * Authorization token
     */
    private String mToken;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static StudentCompanionApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;

        // initialize the Realm database
        Realm.init(this);
    }

    public Context getContext() {
        return sInstance;
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized StudentCompanionApp getInstance() {
        return sInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Get the auth token which is stored in application private data.
     * @return The Auth token, the token will be read if it is null
     */
    public String getToken() {
        // lazy initialize the auth token,the token will be read when it is accessed
        // for the first time
        if (mToken == null) {
            try {
                FileInputStream fis = openFileInput(LoginActivity.TOKEN_FILENAME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                mToken = reader.readLine();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        return mToken;
    }

    /**
     * Delete the auth token.
     * @return The status whether token file is deleted or not
     */
    public boolean deleteToken() {
        boolean isTokenDeleted = true;
        mToken = null;
        try {
            deleteFile(LoginActivity.TOKEN_FILENAME);
        } catch (Exception err) {
            err.printStackTrace();
            isTokenDeleted = false;
        }
        return isTokenDeleted;
    }
}