package com.example.wyd.pay;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.wyd.pay.activity.MainActivity;
import com.sdk.commplatform.Commplatform;
import com.sdk.commplatform.entry.AppInfo;
import com.sdk.commplatform.entry.ErrorCode;
import com.sdk.commplatform.listener.CallbackListener;

import static com.odin.framework.utils.ThreadUtil.runOnUiThread;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        String appid="tjlhyy";
        String appkey="tjlhjf";

        AppInfo appInfo=new AppInfo();
        appInfo.setAppId(appid);
        appInfo.setAppKey(appkey);
        appInfo.setCtx(this);
        appInfo.setVersionCheckStatus(0);

        Commplatform.getInstance().Init(0, appInfo, new CallbackListener<Integer>(){

            @Override
            public void callback(final int i, Integer integer) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("App", ""+i);
                        if (i== ErrorCode.COM_PLATFORM_SUCCESS){
                            Toast.makeText(App.this,
                                    "初始化成功",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if (i== ErrorCode.COM_PLATFORM_ERROR_ONLINE_CHECK_FAILURE)
                        {
                            Toast.makeText(App.this,
                                    "无网络",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if (i== ErrorCode.COM_PLATFORM_ERROR_FORCE_CLOSE){
                            Toast.makeText(App.this,
                                    "初始化异常，强制退出",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(App.this,

                                    "不知道SDK返回的是什么东西",
                                    Toast.LENGTH_SHORT).show();



                        }

                    }
                });

            }
        });
    }
}
