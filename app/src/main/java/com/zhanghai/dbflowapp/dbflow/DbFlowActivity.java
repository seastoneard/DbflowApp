package com.zhanghai.dbflowapp.dbflow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.zhanghai.dbflowapp.R;
import com.zhanghai.dbflowapp.app.App;
import com.zhanghai.dbflowapp.bean.BigSeaInfo0;
import com.zhanghai.dbflowapp.bean.BigSeaInfo0_Table;

import java.util.List;

/**
 * Created by zhanghai on 2018/07/07.
 * 学习网址：
 * 官方：https://yumenokanata.gitbooks.io/dbflow-tutorials/content/
 * 推荐学习：https://www.jianshu.com/p/0c017a715410
 */

public class DbFlowActivity extends AppCompatActivity implements View.OnClickListener {


    private int index = 0;
    private int page = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbflow_layout);
        findViewById(R.id.db_btn0).setOnClickListener(this);
        findViewById(R.id.db_btn1).setOnClickListener(this);
        findViewById(R.id.db_btn2).setOnClickListener(this);
        findViewById(R.id.db_btn3).setOnClickListener(this);
        findViewById(R.id.db_btn4).setOnClickListener(this);
        findViewById(R.id.db_btn5).setOnClickListener(this);
        findViewById(R.id.db_btn6).setOnClickListener(this);
        findViewById(R.id.db_btn7).setOnClickListener(this);
        findViewById(R.id.db_btn8).setOnClickListener(this);
        findViewById(R.id.db_btn9).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.db_btn0:
                insertBaseModle();
                break;
            case R.id.db_btn1:
                updateBaseModle0();
                break;
            case R.id.db_btn2:
                updateBaseModle1();
                break;
            case R.id.db_btn3:
                deleteBaseModle();
                break;
            case R.id.db_btn4:
                deleteBaseModle1();
                break;
            case R.id.db_btn5:
                selectBaseModle();
                break;
            case R.id.db_btn6:
                selectBaseModleOrderBy();
                break;
            case R.id.db_btn7:
                selectBaseModleGroupBy();
                break;
            case R.id.db_btn8:
                selectPageBaseModle(page++);
                break;
            case R.id.db_btn9:
                selectBaseModleSync();
                break;

            default:
                break;
        }

    }


    //DBFlow 对数据的增删改查已经做了封装，使用起来比较简单
    private void insertBaseModle() {
        BigSeaInfo0 product = new BigSeaInfo0();
        product.name = "P000" + index;
        product.age = 18 + index;
        product.remarks = "备注-" + index;
        product.money = 300 * index;
//        product.insert();
        boolean success = product.save();
        index++;
        // 执行到这里之后 id 已经被赋值
        App.showToast(this, "添加结果：" + success);
    }


    /**
     * 更新和删除可以为先查询后操作，只要查到对应的数据，在 bean 上做修改，
     * 然后调用 update() 方法，数据库就能修改完成。还有另一中更接近 sql 语法的方式。
     */
    private void updateBaseModle0() {
        // 第一种 先查后改
        BigSeaInfo0 product = SQLite.select()
                .from(BigSeaInfo0.class)//查询第一个
                .where(BigSeaInfo0_Table.name.is("P0000"))
                .querySingle();// 区别与 queryList()
        if (product != null) {
            Log.d("zhh_Bd", "Update: " + product.name + " update to P0000");
            product.name = "P000X";
            boolean success = product.update();
            App.showToast(this, "修改结果：" + success);
        } else {
            App.showToast(this, "name=P0000的条件数据不存在：");
        }
    }

    // update：第二种 高级用法，增删改查都是同理
    private void updateBaseModle1() {
        SQLite.update(BigSeaInfo0.class)
                .set(BigSeaInfo0_Table.name.eq("PXXXX"))
                .where(BigSeaInfo0_Table.name.is("P0001"))
                .execute();

    }

    //删除1
    private void deleteBaseModle() {
        // 第一种 先查后删
        BigSeaInfo0 product = SQLite.select()
                .from(BigSeaInfo0.class)
                .where(BigSeaInfo0_Table.name.is("P00010"))
                .querySingle();//查询单个
        if (product != null) {
            product.delete();
            Log.d("zhh_db", "Delete: " + product.name);
        }

        //删除表
        // Delete a whole table
//        Delete.table(BigSeaInfo0.class);

// Delete multiple instantly
//        Delete.tables(MyTable1.class, MyTable2.class);


    }

    //删除2
    private void deleteBaseModle1() {
        // 第二种
        SQLite.delete(BigSeaInfo0.class)
                .where(BigSeaInfo0_Table.name.is("PXXXX"))
                .execute();

    }

    //查询
    private void selectBaseModle() {

//        List<BigSeaInfo0> list = SQLite.select().from(BigSeaInfo0.class).queryList();
        List<BigSeaInfo0> list = SQLite.select().from(BigSeaInfo0.class).where(BigSeaInfo0_Table.name.like("P%")).queryList();
        printData(list);

    }

    //异步执行查询:尽管是异步的，但是线程安全的
    private void selectBaseModleSync() {
        SQLite.select().from(BigSeaInfo0.class)//.where(BigSeaInfo0_Table.name.is(""))
                .async().queryListResultCallback(new QueryTransaction.QueryResultListCallback<BigSeaInfo0>() {
            @Override
            public void onListQueryResult(QueryTransaction transaction, @NonNull List<BigSeaInfo0> tResult) {
                printData(tResult);//更新UI
            }
        });


    }

    //指定字段升降序查询
    private void selectBaseModleOrderBy() {
        //true为'ASC'正序, false为'DESC'反序
        List<BigSeaInfo0> list = SQLite.select()
                .from(BigSeaInfo0.class)
                .where()
                .orderBy(BigSeaInfo0_Table.userId, true)
                .queryList();
        printData(list);
    }

    //分组查询--以年龄+名字分组查询：先排序后分组
    private void selectBaseModleGroupBy() {
        List<BigSeaInfo0> list = SQLite.select()
                .from(BigSeaInfo0.class)
                .groupBy(BigSeaInfo0_Table.age, BigSeaInfo0_Table.name)
                .queryList();
        printData(list);

    }

    //分页查询--每页查询3条--》limit后面跟的是3条数据，offset：是从第1条开始读取
    private void selectPageBaseModle(int page) {
        List<BigSeaInfo0> list = SQLite.select()
                .from(BigSeaInfo0.class)
                .limit(3)//条数-3
                .offset(page * 3)//当前页数
                .queryList();
        printData(list);


    }

    private void printData(List<BigSeaInfo0> list) {
        if (null != list && list.size() > 0) {
            for (BigSeaInfo0 bigSeaInfo0 : list) {
                Log.i("zhh_db", "id---" + bigSeaInfo0.userId + ",name->" + bigSeaInfo0.name +
                        ",age---" + bigSeaInfo0.age + ",note--" + bigSeaInfo0.remarks + ",money-->" + bigSeaInfo0.money);
            }
        } else {
            App.showToast(this, "数据为空");
        }

    }


}
