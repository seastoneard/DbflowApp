package com.zhanghai.dbflowapp.dbflow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhanghai.dbflowapp.R;

/**
 * Created by zhanghai on 2018/7/07.
 * 数据库升级说明类
 */

public class MigrationActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.migration_layout);
        textView = findViewById(R.id.txt);
        String txt0 = "当你升级数据库@Database版本，只需要添加一个Migration来配置升级操作\n\n" +
                "DBFlow 已经有它的几个现成的实现提供给我们进行使用。\n\n" +
                "1.AlterTableMigration 用于重命名表，增加列\n" +
                "2.IndexMigration/IndexPropertyMigration 用于索引创建和删除\n" +
                "3.UpdateTableMigration 升级数据库的时候更新数据\n\n" +
                "具体见AppDatabase类，数据升级操作<新增表，新增字段[默认值设置]>";
        textView.setText(txt0);
    }
}
