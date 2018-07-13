package com.zhanghai.dbflowapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhanghai.dbflowapp.dbflow.DbFlowActivity;
import com.zhanghai.dbflowapp.dbflow.DbFlowTransactionActivity;
import com.zhanghai.dbflowapp.dbflow.MigrationActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
    }

    private void openActivity(Class<? extends AppCompatActivity> clazz) {
        startActivity(new Intent(this, clazz));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn0:
                openActivity(DbFlowActivity.class);
                break;
            case R.id.btn1:
                openActivity(DbFlowTransactionActivity.class);
                break;
            case R.id.btn2:
                openActivity(MigrationActivity.class);
                break;

            default:
                break;
        }


    }
}
