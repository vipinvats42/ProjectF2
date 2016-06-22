package com.example.vipin.projectf2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;


public class MainActivity extends Activity
{
    private int flag=1;
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        info = (TextView)findViewById(R.id.textView);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                info.setText(
//                        "User ID: "
//                                + loginResult.getAccessToken().getUserId()
//                                + "\n" +
//                                "Auth Token: "
//                                + loginResult.getAccessToken().getToken());
                flag=2;
                requestData();
                disconnectFromFacebook();
                logoutfunction();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed.");
            }
        });





    }

    public void logoutfunction()
    {
        if(flag==1)
        {
            info.setText("");
        }else
        {
            info.setText("");
        }
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            flag=1;
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/name/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

               // LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }





    public void requestData()
    {
        GraphRequest request=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {
                JSONObject json=response.getJSONObject();


                try
                {
                    if(json!=null)
                    {
                        String text="Name :"+ json.getString("name");

                        String email="Email :"+json.getString("link");
                        info.setText(text);
                    }



                }catch(Exception e)
                {
                   Log.i("error",e.toString());
                }
            }


        });

         Bundle parameter=new Bundle();
        parameter.putString("fields","name,link");
        request.setParameters(parameter);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }


}