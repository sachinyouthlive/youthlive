package com.yl.youthlive.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.youthlive.DataValidation;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.Login;
import com.yl.youthlive.NetworkUtility;
import com.yl.youthlive.R;
import com.yl.youthlive.Spalsh2;
import com.yl.youthlive.bean;
import com.yl.youthlive.newpasswordPOJO.NewPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.yl.youthlive.bean.getContext;


public class New_Password extends AppCompatActivity {
    private String TAG = "New_Password", userid;
    private EditText password, retype_password;
    private TextView submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        password = (EditText) findViewById(R.id.password);
        retype_password = (EditText) findViewById(R.id.retype_password);
        submit = (TextView) findViewById(R.id.submit);

        Intent intent = getIntent();

        if (intent != null && !intent.getExtras().getString("userid").equals(null)) {

            userid = intent.getExtras().getString("userid");

        } else {
            userid = "";
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DataValidation.isNotValidPassword(password.getText().toString())) {
                    //     AppUtilits.displayMessage(New_Password.this, getString(R.string.password) + " " + getString(R.string.is_invalid));
                    Toast.makeText(New_Password.this, getString(R.string.password) + " " + getString(R.string.is_invalid), Toast.LENGTH_SHORT).show();

                } else if (DataValidation.isNotValidPassword(retype_password.getText().toString())) {
                    //    AppUtilits.displayMessage(New_Password.this, getString(R.string.retype_password) + " " + getString(R.string.is_invalid));
                    Toast.makeText(New_Password.this, getString(R.string.retype_password) + " " + getString(R.string.is_invalid), Toast.LENGTH_SHORT).show();

                } else if (!password.getText().toString().equals(retype_password.getText().toString())) {
                    //     AppUtilits.displayMessage(New_Password.this, getString(R.string.password_not_match));
                    Toast.makeText(New_Password.this, getString(R.string.password_not_match), Toast.LENGTH_SHORT).show();


                } else {
                    // network connection and progroess dialog
                    /// here call retrofit method

                    sendNewPasswordReq();
                }


            }
        });

    }


    public void sendNewPasswordReq() {


        if (!NetworkUtility.isNetworkConnected(New_Password.this)) {
            //   AppUtilits.displayMessage(New_Password.this, getString(R.string.network_not_connected));
            Toast.makeText(New_Password.this, getString(R.string.network_not_connected), Toast.LENGTH_SHORT).show();


        } else {


            final bean b = (bean) getContext().getApplicationContext();

            Call<NewPassword> call = b.getRetrofit().UserNewPassword(userid, password.getText().toString());

            Log.d("password", password.getText().toString());
            ///////

            call.enqueue(new Callback<NewPassword>() {
                @Override
                public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {

                    Log.d(TAG, "reponse : " + response.toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            // response.body().getInformation().getOtp()
                            // start home activity
                            Intent intent = new Intent(New_Password.this, Spalsh2.class);
                            Toast.makeText(New_Password.this, "Password Updated Successfully, Now Login with new Credentials", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finishAffinity();


                        } else {
                            //  AppUtilits.displayMessage(New_Password.this, response.body().getMsg());
                            Toast.makeText(New_Password.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //   AppUtilits.displayMessage(New_Password.this, getString(R.string.failed_request));
                        Toast.makeText(New_Password.this, getString(R.string.failed_request), Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onFailure(Call<NewPassword> call, Throwable t) {
                    Log.e(TAG, " failure " + t.toString());
                    //   AppUtilits.displayMessage(New_Password.this, getString(R.string.failed_request));
                    Toast.makeText(New_Password.this, getString(R.string.failed_request), Toast.LENGTH_SHORT).show();

                }
            });


        }


    }


}