package com.app.youthlive;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.app.youthlive.loginResponsePOJO.loginResponseBean;
import com.app.youthlive.updatePOJO.updateBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TBX on 12/20/2017.
 */

public class Address2 extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    FloatingActionButton editer;
    TextView username, youthId, phone, gender, birth, bio;
    TimelineProfile per;
    String userId;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext().getApplicationContext());
        View view = inflater.inflate(R.layout.address_layout, container, false);

        per = (TimelineProfile) getActivity();

        userId = getArguments().getString("userId");

        pref = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        edit = pref.edit();

        username = view.findViewById(R.id.username);
        youthId = view.findViewById(R.id.yid);
        phone = view.findViewById(R.id.phone);
        gender = view.findViewById(R.id.gender);
        birth = view.findViewById(R.id.birthday);
        bio = view.findViewById(R.id.bio);

        editer = view.findViewById(R.id.edit);

        Bundle b = getArguments();
        bean b1 = (bean) getContext().getApplicationContext();

        if (!Objects.equals(userId, SharePreferenceUtils.getInstance().getString("userId"))) {
            editer.setVisibility(View.GONE);
            phone.setText("(mobile number not public)");
        } else {
            editer.setVisibility(View.VISIBLE);
            phone.setText(b.getString("phone"));
        }


        try {
            buildGoogleApiClient();
        } catch (Exception e) {
            e.printStackTrace();
        }


        username.setText(b.getString("user"));
        youthId.setText(b.getString("youth"));
        gender.setText(b.getString("gender"));
        birth.setText(b.getString("birth"));
        bio.setText(b.getString("bio"));


        editer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {


                final String[] gend = {""};
                final String[] bday = {""};


                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.edit_user_details_options);
                dialog.show();

                final TextView phone = dialog.findViewById(R.id.phone);
                TextView password = dialog.findViewById(R.id.password);
                final TextView personal = dialog.findViewById(R.id.personal);

                final ProgressBar progress = dialog.findViewById(R.id.progress);

                final LinearLayout phoneHide = dialog.findViewById(R.id.phone_hide);
                final LinearLayout passwordHide = dialog.findViewById(R.id.password_hide);
                final LinearLayout personalHide = dialog.findViewById(R.id.personal_hide);


                final EditText phoneNumber = dialog.findViewById(R.id.phone_number);

                final EditText oldPassword = dialog.findViewById(R.id.old_password);
                final EditText newPassword = dialog.findViewById(R.id.new_password);
                final EditText confirmPassword = dialog.findViewById(R.id.confirm_new);

                final EditText username = dialog.findViewById(R.id.usename);
                Spinner gender = dialog.findViewById(R.id.gender);
                final TextView birth = dialog.findViewById(R.id.birth);
                final EditText bio = dialog.findViewById(R.id.bio);

                List<String> genders = new ArrayList<>();

                final DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        String m = String.valueOf(datePicker.getMonth() - 1);
                        String y = String.valueOf(datePicker.getYear());
                        String d = String.valueOf(datePicker.getDayOfMonth());

                        bday[0] = d + "-" + m + "-" + y;
                        birth.setText(d + "-" + m + "-" + y);


                    }
                }, 2017, 0, 1);

                birth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        datePickerDialog.show();

                    }
                });

                genders.add("Male");
                genders.add("Female");

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, genders);

                gender.setAdapter(adapter);

                gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (i == 0) {
                            gend[0] = "M";
                        } else if (i == 1) {
                            gend[0] = "F";
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                phoneNumber.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        final int DRAWABLE_LEFT = 0;
                        final int DRAWABLE_TOP = 1;
                        final int DRAWABLE_RIGHT = 2;
                        final int DRAWABLE_BOTTOM = 3;
                        if (event.getRawX() >= (phoneNumber.getRight() - phoneNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) - 60) {

                            String pho = phoneNumber.getText().toString();


                                if (Utils.isValidMobile(pho)) {


                                    progress.setVisibility(View.VISIBLE);


                                    Log.d("neetu", "log");
                                    bean b = (bean) getContext().getApplicationContext();

                                    Call<loginResponseBean> call = b.getRetrofit().updatePhone(userId, pho);

                                    call.enqueue(new Callback<loginResponseBean>() {
                                        @Override
                                        public void onResponse(Call<loginResponseBean> call, Response<loginResponseBean> response) {

                                            if (Objects.equals(response.body().getMessage(), "1")) {
                                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                if (mGoogleApiClient.isConnected()) {
                                                    dialog.dismiss();
                                                    signOut();
                                                }

                                                Log.d("kamal", "response");

                                                LoginManager.getInstance().logOut();

                                                edit.remove("type");
                                                edit.remove("user");
                                                edit.remove("pass");
                                                edit.apply();

                                                dialog.dismiss();

                                                Intent i = new Intent(getContext(), Spalsh2.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                                getActivity().finish();
                                            } else {
                                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                            progress.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure(Call<loginResponseBean> call, Throwable t) {

                                            Log.d("nisha", t.toString());
                                            progress.setVisibility(View.GONE);
                                        }
                                    });


                                } else {
                                    Toast.makeText(getContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                                }


                            return true;
                        }
                        return false;
                    }
                });


                confirmPassword.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        final int DRAWABLE_LEFT = 0;
                        final int DRAWABLE_TOP = 1;
                        final int DRAWABLE_RIGHT = 2;
                        final int DRAWABLE_BOTTOM = 3;
                        if (event.getRawX() >= (confirmPassword.getRight() - confirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) - 60) {

                            String old = oldPassword.getText().toString();
                            String newp = newPassword.getText().toString();
                            String conf = confirmPassword.getText().toString();

                            if (old.length() > 0) {

                                if (newp.length() > 0) {

                                    if (conf == newp) {


                                    } else {
                                        Toast.makeText(getContext(), "Password did not match", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getContext(), "Invalid New Password", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(), "Invalid Old Password", Toast.LENGTH_SHORT).show();
                            }


                            return true;
                        }
                        return false;
                    }
                });


                bio.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        final int DRAWABLE_LEFT = 0;
                        final int DRAWABLE_TOP = 1;
                        final int DRAWABLE_RIGHT = 2;
                        final int DRAWABLE_BOTTOM = 3;
                        if (event.getRawX() >= (bio.getRight() - bio.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) - 60) {

                            String u = username.getText().toString();
                            String bi = bio.getText().toString();

                            if (u.length() > 0) {

                                if (gend[0].length() > 0) {

                                    if (bday[0].length() > 0) {

                                        if (bi.length() > 0) {

                                            progress.setVisibility(View.VISIBLE);

                                            final bean b = (bean) getContext().getApplicationContext();

                                            Call<updateBean> call = b.getRetrofit().updateUserData(u, gend[0], bday[0], bi, userId);

                                            call.enqueue(new Callback<updateBean>() {
                                                @Override
                                                public void onResponse(Call<updateBean> call, Response<updateBean> response) {

                                                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                    per.loadData();

                                                    progress.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void onFailure(Call<updateBean> call, Throwable t) {
                                                    progress.setVisibility(View.GONE);
                                                    Log.d("asdasd", t.toString());
                                                }
                                            });


                                        } else {
                                            Toast.makeText(getContext(), "Invalid Bio", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(getContext(), "Invalid Birth Date", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getContext(), "Invalid Gender", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(), "Invalid Username", Toast.LENGTH_SHORT).show();

                            }

                            return true;
                        }
                        return false;
                    }
                });


                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        phoneHide.setVisibility(View.VISIBLE);
                        passwordHide.setVisibility(View.GONE);
                        personalHide.setVisibility(View.GONE);

                    }
                });

                password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        phoneHide.setVisibility(View.GONE);
                        passwordHide.setVisibility(View.VISIBLE);
                        personalHide.setVisibility(View.GONE);

                    }
                });
                personal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        phoneHide.setVisibility(View.GONE);
                        passwordHide.setVisibility(View.GONE);
                        personalHide.setVisibility(View.VISIBLE);

                    }
                });


            }
        });


        return view;
    }

    private synchronized void buildGoogleApiClient() {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onResult(@NonNull Status status) {


                        if (status.isSuccess()) {

                            edit.remove("type");
                            edit.remove("user");
                            edit.remove("pass");
                            edit.apply();
                            Intent i = new Intent(getContext(), Spalsh2.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            getActivity().finish();

                        }


                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
