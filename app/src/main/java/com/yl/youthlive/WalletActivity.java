package com.yl.youthlive;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.yl.youthlive.Activitys.MessaageActivity;
import com.yl.youthlive.Activitys.PersonalInfo;
import com.yl.youthlive.ExchangeDiamondPOJO.ExchangeBean;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.addWalletPOJO.addWalletBean;
import com.yl.youthlive.allMessagePOJO.allMessageBean;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;
import com.yl.youthlive.walletPOJO.walletBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WalletActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Toolbar toolbar;
    EditText amount;
    Button add;
    TextView diamond;
    TextView diamonds , beans , coins;
    ProgressBar progress;

    Dialog dialog;


    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        checkConnection();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        amount = (EditText)findViewById(R.id.amount);
        add = (Button)findViewById(R.id.add);
        diamonds = (TextView)findViewById(R.id.diamonds);
        beans = (TextView)findViewById(R.id.beans);
        coins = (TextView)findViewById(R.id.coins);
        diamond = (TextView)findViewById(R.id.diamond);
        progress = (ProgressBar)findViewById(R.id.progress);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle("Wallet");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        amount.setText("100");

        diamond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog = new Dialog(WalletActivity.this);
                dialog.setContentView(R.layout.diamond);
                dialog.setCancelable(true);
                dialog.show();


                final EditText bean = dialog.findViewById(R.id.beans);
                Button submit = dialog.findViewById(R.id.submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progress.setVisibility(View.VISIBLE);

                        final bean b = (bean) getApplicationContext();

                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.BASE_URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final AllAPIs cr = retrofit.create(AllAPIs.class);


                        Call<ExchangeBean> call = cr.exchange(b.userId ,"",bean.getText().toString());

                        call.enqueue(new Callback<ExchangeBean>() {
                            @Override
                            public void onResponse(Call<ExchangeBean> call, Response<ExchangeBean> response) {


                                Toast.makeText(WalletActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                loadData();
                                dialog.dismiss();

                                progress.setVisibility(View.GONE);




                            }

                            @Override
                            public void onFailure(Call<ExchangeBean> call, Throwable t) {

                                progress.setVisibility(View.GONE);

                            }
                        });




                    }
                });







            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String am = amount.getText().toString();

                if (am.length() > 0)
                {


                    bean b = (bean)getApplicationContext();

                    Random random = new Random();

                    String key = "sL0p7U95";
                    String txnid = String.valueOf(random.nextInt(8));
                    String productinfo = "beans";
                    String firstname = b.userName;
                    String email = "test@gmail.com";
                    String udf1 = "";
                    String udf2 = "";
                    String udf3 = "";
                    String udf4 = "";
                    String udf5 = "";
                    String salt = "jPU0s1mj8V";








                    PayUmoneySdkInitializer.PaymentParam.Builder builder = new
                            PayUmoneySdkInitializer.PaymentParam.Builder();
                    builder.setAmount(Double.parseDouble(am))
                            .setKey("sL0p7U95")
                            //.setKey("dRQuiA")
                            .setMerchantId("5979090")
                            //.setMerchantId("4928174")
                            .setTxnId(txnid)
                            .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                            //.setsUrl("https://test.payumoney.com/mobileapp/payumoney/success.php")
                            .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                            //.setfUrl("https://test.payumoney.com/mobileapp/payumoney/failure.php")
                            .setProductName(productinfo)
                            .setFirstName(firstname)
                            .setEmail(email)
                            .setPhone("9873672433")
                            .setUdf1("")
                            .setUdf2("")
                            .setUdf3("")
                            .setUdf4("")
                            .setUdf5("")
                            .setUdf6("")
                            .setUdf7("")
                            .setUdf8("")
                            .setUdf9("")
                            .setUdf10("")
                            .setIsDebug(false);







                            // Payment amount
                            //.setTxnId(txnid)                                             // Transaction ID
                            //.setPhone("9991262626")                                           // User Phone number
                            //.setProductName("Beans")                   // Product Name or description
                            //.setFirstName(firstname)                              // User First name
                            //.setEmail(email)                                            // User Email ID
                            //.setsUrl("https://test.payumoney.com/mobileapp/payumoney/success.php")                     // Success URL (surl)
                            //.setfUrl("https://test.payumoney.com/mobileapp/payumoney/failure.php")                     //Failure URL (furl)
                            //.setUdf1(udf1)
                            //.setUdf2(udf2)
                            //.setUdf3(udf3)
                            //.setUdf4(udf4)
                            //.setUdf5(udf5)
                            //.setUdf6("")
                            //.setUdf7("")
                            //.setUdf8("")
                            //.setUdf9("")
                            //.setUdf10("")
                            //.setIsDebug(true)                              // Integration environment - true (Debug)/ false(Production)
                            //.setKey(key)                        // Merchant key
                            //.setMerchantId("5979090");




                    mPaymentParams = builder.build();


                    mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);


                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, WalletActivity.this, R.style.AppTheme, false);


                    /*PayUmoneySdkInitializer.PaymentParam paymentParam = builder.build();
//set the hash
                    paymentParam.setMerchantHash(serverCalculatedHash);

                    PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, WalletActivity.this, R.style.AppTheme, false);
*/




                }


            }
        });



    }


    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");


        stringBuilder.append("jPU0s1mj8V");

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }


    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        bean.getInstance().setConnectivityListener(this);

        loadData();

    }
    ////////////////////internet connectivity check///////////////
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

            Toast.makeText(this, "Good! Connected to Internet", Toast.LENGTH_SHORT).show();
            //    message = "Good! Connected to Internet";
            //    color = Color.WHITE;
        } else {
            Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
            try {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("NO INTERNET CONNECTION")
                        .setMessage("Please check your internet connection setting and click refresh")
                        .setPositiveButton(R.string.Refresh, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            catch(Exception e)
            {
                Log.d("TAG", "Show Dialog: "+e.getMessage());
            }
            //      message = "Sorry! Not connected to internet";
            //     color = Color.RED;
        }

       /* Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
        */
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }


    public void loadData() {
        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<walletBean> call = cr.getWalletData(b.userId);

        call.enqueue(new Callback<walletBean>() {
            @Override
            public void onResponse(Call<walletBean> call, Response<walletBean> response) {



                beans.setText(response.body().getData().getBeans());
                diamonds.setText(response.body().getData().getDiamond());
                coins.setText(response.body().getData().getCoin());


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<walletBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction


                    progress.setVisibility(View.VISIBLE);


                    bean b = (bean)getApplicationContext();


                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    Call<addWalletBean> call = cr.addBeans(b.userId , amount.getText().toString());

                    call.enqueue(new Callback<addWalletBean>() {
                        @Override
                        public void onResponse(Call<addWalletBean> call, Response<addWalletBean> response) {

                            Toast.makeText(WalletActivity.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                            loadData();

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<addWalletBean> call, Throwable t) {

                            progress.setVisibility(View.GONE);

                        }
                    });



                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

                Log.d("response", payuResponse);


                /*try {
                    JSONObject obj = new JSONObject(payuResponse);

                    JSONObject res = obj.getJSONObject("result");

                    String tid = res.getString("txnid");
                    final String pid = res.getString("paymentId");

                    Log.d("tid", tid);

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://nationproducts.in/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllAPIs cr = retrofit.create(AllAPIs.class);
                    final bean b = (bean) getApplicationContext();

                    Call<successBean> call = cr.orderSuccess(b.userId, tid, "success");

                    call.enqueue(new Callback<successBean>() {
                        @Override
                        public void onResponse(Call<successBean> call, retrofit2.Response<successBean> response) {

                            String paymentId = pid;
                            //showDialogMessage("Payment Success Id : " + paymentId);

                            new AlertDialog.Builder(CheckOut.this)
                                    .setCancelable(false)
                                    .setMessage("Payment Success Id : " + paymentId)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                        }

                        @Override
                        public void onFailure(Call<successBean> call, Throwable throwable) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("Asdasd", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("asdasdasd", "Both objects are null!");
            }
        }

    }
}
