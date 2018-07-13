package com.zhanghai.dbflowapp.app;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;


/**
 * Created by zhanghai on 2018/07/07.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //DBFlow初始配置：3中方式


//        FlowManager.init(this);// 初始化方式1


//        FlowManager.init(new FlowConfig.Builder(this).build());//初始化方式2

//

        //自定义路径-初始化方式3-->  api>=23需授权，再添加此操作，具体见StartActivity
//        FileDatabaseContext mSdDatabaseContext = new FileDatabaseContext(getApplicationContext());
//        FlowManager.init(mSdDatabaseContext);

        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);//添加日志

    }


    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
