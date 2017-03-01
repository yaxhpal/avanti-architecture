/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.avanti.student_companion.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import in.avanti.student_companion.StudentCompanionApp;
import in.avanti.student_companion.http.TokenRequest;
import in.avanti.student_companion.utils.EmptyUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link LoginFragment}), retrieves the data and updates
 * the UI as required.
 */
public class LoginPresenter implements ILoginContract.Presenter {

    //region Declarations
    @NonNull
    private final ILoginContract.View mLoginView;

    @Nullable
    private String mToken;


    @Nullable
    private String mProfile;


    // File name which stores the token.
    public static final String TOKEN_FILENAME = "w143kxnu.idj";

    //endregion

    /**
     * Creates a presenter for the add/edit view.
     *
     * @param token ID of the task to edit or null for a new task
     * @param profile a repository of data for tasks
     * @param loginView the login view
     */
    public LoginPresenter(@Nullable String token, @Nullable String profile, @NonNull ILoginContract.View loginView) {
        mToken = token;
        mProfile = profile;
        mLoginView = checkNotNull(loginView);
    }

    @Override
    public void start() {
        if (!isNewToken()) {
            saveToken(mToken);
            saveProfile(mProfile);
        }
    }

    private boolean isNewToken() {
        return mToken == null;
    }

//    @Override
    public void saveProfile(String profile) {

    }

    @Override
    public void getToken(String emailId, String password) {
        //TODO Get

        TokenRequest request = new TokenRequest(emailId, password, getResponseListener(), getErrorListener());
        // Add the request to the RequestQueue. Requests are processed in this queue automatically
        StudentCompanionApp.getInstance().addToRequestQueue(request);
    }

//    @Override
    public void saveToken(String token) {
        FileOutputStream fos = null;
        try {
            // Write token into private file
            fos = StudentCompanionApp.getInstance().getApplicationContext().openFileOutput(TOKEN_FILENAME, Context.MODE_PRIVATE);
            fos.write(mToken.getBytes());
            fos.close();
        } catch (FileNotFoundException err) {
            VolleyLog.wtf(err, "Can not find file: %s", err.getMessage());
        } catch (IOException err) {
            VolleyLog.wtf(err, "Can not write into file %s: %s", TOKEN_FILENAME, err.getMessage());
        }
    }

    private Response.Listener<String> getResponseListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    mToken = jsonResponse.getString("access_token");
                    VolleyLog.v("Token: %s", mToken);
                } catch (JSONException err) {
                    VolleyLog.wtf(err, "Token request parsing failed: %s", err.getMessage());
                } finally {
                    if (EmptyUtils.isNotEmpty(mToken)) {
                        saveToken(mToken);
                        // Write token into private file
                        mLoginView.showMainActivity();
                    } else {
                        mLoginView.showMessage(new Exception("Bad response."));
                    }
                }
                mLoginView.showProgress(false);
            }
        };
    }

    private Response.ErrorListener getErrorListener () {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoginView.showMessage(error);
            }
        };
    }
}

