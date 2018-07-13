package com.zhanghai.dbflowapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.zhanghai.dbflowapp.app.FileDatabaseContext;

/**
 * Created by zhanghai on 2018/07/07.
 */

public class StartActivity extends AppCompatActivity {


    private static final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemClock.sleep(1000);//延时加载
        requestPermissions();
    }


    private void openActivity(Class<? extends AppCompatActivity> clazz) {
        initDb();
        startActivity(new Intent(this, clazz));
        finish();
    }

    private void initDb() {
        //自定义路径-初始化方式3--api>=23需授权，再添加次操作
        FileDatabaseContext mSdDatabaseContext = new FileDatabaseContext(getApplicationContext());
        FlowManager.init(mSdDatabaseContext);
    }


    /**
     * 权限申请返回结果
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 申请结果数组，里面都是int类型的数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0x10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //同意权限申请
                    Toast.makeText(this, "同意权限申请", Toast.LENGTH_SHORT).show();
                    openActivity(MainActivity.class);
                } else { //拒绝权限申请
                    Toast.makeText(this, "权限被拒绝了", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }


    }

    private void requestPermissions() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                // 开始提交请求权限
                ActivityCompat.requestPermissions(this, permissions, 0x10);
            } else {
                Log.i("zhh", "权限已申请");
                openActivity(MainActivity.class);
            }
        } else {
            openActivity(MainActivity.class);
        }
    }

}
