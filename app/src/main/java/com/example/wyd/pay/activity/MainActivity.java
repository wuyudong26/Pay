package com.example.wyd.pay.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wyd.pay.R;
import com.sdk.commplatform.Commplatform;
import com.sdk.commplatform.entry.AppInfo;
import com.sdk.commplatform.entry.AuthResult;
import com.sdk.commplatform.entry.CyclePayment;
import com.sdk.commplatform.entry.ErrorCode;
import com.sdk.commplatform.entry.PayResult;
import com.sdk.commplatform.entry.Payment;
import com.sdk.commplatform.entry.PaymentState;
import com.sdk.commplatform.entry.ProductInfos;
import com.sdk.commplatform.entry.QueryPayment;
import com.sdk.commplatform.listener.CallbackListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

private Button button1;
private Button button2;
private Button button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        appid1=findViewById(R.id.appid);
//        appkey1=findViewById(R.id.appkey);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                sendAuthRequest("tjlhcsby","2");
             break;
            case R.id.button2:
                pay();
                break;
            case R.id.button3:
                query();
                break;

        }
    }
    private void sendAuthRequest(String productId, String productType){
        Commplatform.getInstance().authPermission(productId,
                productType,
                new CallbackListener<AuthResult>(){

                    @Override
                    public void callback(final int code, final AuthResult authResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

//                                String aaa=authResult.description.toString();
                                if (code == ErrorCode.COM_PLATFORM_SUCCESS)
                                {
                                    Toast.makeText(MainActivity.this,
                                            "请求已受理",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else if(code == ErrorCode.COM_PLATFORM_ERROR_PARAM)
                                {
                                    Toast.makeText(MainActivity.this,
                                           "请求de参数错误",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(MainActivity.this,
                                            "SDK未初始化及其它错误",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });

    }
    private boolean isPaying=false;
    private CyclePayment getCyclePayment(){
        String tradeNo = "[B@2ca79b10";//订单号
        String productId = "123";//商品ID
        String note = "好玩的游戏";
        String thirdAppId = "";
        String thirdAppName = "";
        String thirdAppPkgname = "";
        String notifyURL = "";
        if (tradeNo == null || tradeNo.trim().equals("".trim()))
        {
            Toast.makeText(this,
                  "订单号不能为空",
                    Toast.LENGTH_SHORT).show();
            return null;
        }

        CyclePayment cyclePayment = new CyclePayment();
        cyclePayment.setTradeNo(tradeNo);
        cyclePayment.setProductId(productId);
        cyclePayment.setNote(note);
        cyclePayment.setThirdAppId(thirdAppId);
        cyclePayment.setThirdAppName(thirdAppName);
        cyclePayment.setThirdAppPkgname(thirdAppPkgname);
        cyclePayment.setNotifyURL(notifyURL);
        return cyclePayment;

    }
    private int pay() {
        isPaying = true;

        int ret = Commplatform.getInstance().subsPay(getCyclePayment(), this, new CallbackListener<PayResult>() {

            @Override
            public void callback(final int arg0, PayResult arg1) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        isPaying = false;
                        if (arg0 == ErrorCode.COM_PLATFORM_SUCCESS) {
                            Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        } else if (arg0 == ErrorCode.COM_PLATFORM_ERROR_PAY_FAILURE) {
                            Toast.makeText(MainActivity.this,"支付失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Purchase failed. Error code:" + arg0, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        if (ret == 0)
        {
            return 0;
        }
        else
        {//返回错误，支付结束
            isPaying = false;
            return -1;
        }

    }
    private void query(){
        QueryPayment queryInfo = new QueryPayment();
       String tradeNo = "[B@2ca79b10";
        queryInfo.setTradeNo(tradeNo);
        queryInfo.setThirdAppId("");
        Commplatform.getInstance().queryPayment(queryInfo,
                this,
                new CallbackListener<PaymentState>()
                {

                    @Override
                    public void callback(final int arg0, final PaymentState arg1)
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (arg0 == ErrorCode.COM_PLATFORM_SUCCESS)
                                {
                                    Toast.makeText(MainActivity.this,
                                           "查询成功",
                                            Toast.LENGTH_SHORT).show();
                                }

                                if (null != arg1)
                                {
                                    showPayResultDialog(arg1.toString());
                                }
                                else
                                {
                                    showPayResultDialog("Query failed!");
                                }
                            }
                        });
                    }
                });

    }
    private String showPayResultDialog(String strs){
        Toast.makeText(MainActivity.this,
                ""+strs,
                Toast.LENGTH_SHORT).show();
        return strs;
    }
    }
