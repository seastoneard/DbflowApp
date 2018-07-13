package com.zhanghai.dbflowapp.dbflow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.zhanghai.dbflowapp.R;
import com.zhanghai.dbflowapp.app.App;
import com.zhanghai.dbflowapp.bean.BigSeaInfo0;
import com.zhanghai.dbflowapp.bean.BigSeaInfo0_Table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhanghai on 2018/07/07.
 * 事物回滚+异步
 * 封装事物处理:见DbTransactionSyncUtils
 */

public class DbFlowTransactionActivity extends AppCompatActivity implements View.OnClickListener {


    private int index = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbflow_transaction_layout);
        findViewById(R.id.db_btn0).setOnClickListener(this);
        findViewById(R.id.db_btn1).setOnClickListener(this);
        findViewById(R.id.db_btn2).setOnClickListener(this);
        findViewById(R.id.db_btn3).setOnClickListener(this);
        findViewById(R.id.db_btn4).setOnClickListener(this);
        findViewById(R.id.db_btn5).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.db_btn0:
                insertBaseModle0();
                break;
            case R.id.db_btn1:
                insertBaseModle1();
                break;
            case R.id.db_btn2:
                insertBaseModle2();
                break;
            case R.id.db_btn3:
                updateBaseModle0();
                break;
            case R.id.db_btn4:
                selectBaseModleSync();
                break;
            case R.id.db_btn5:
                selectBaseModleSyncList();
                break;
            default:
                break;
        }

    }

    private void insertBaseModle0() {
        BigSeaInfo0 product = new BigSeaInfo0();
        product.name = "P000" + index;
        product.age = 18 + index;
        product.remarks = "备注-" + index;
        product.money = 300 * index;
//        boolean success = product.save();
        boolean success = product.async().save();//添加成功，但是返回为false,因为是异步，还未执行完成异步就返回值了，所以需要配置事物操作，
        index++;
        // 执行到这里之后 id 已经被赋值
        App.showToast(this, "添加结果：" + success);
    }

    //异步执行查询:尽管是异步的，但是线程安全的
    private void insertBaseModle1() {
        //自己去实现事务批量保存
        Transaction transaction = FlowManager.getDatabase(AppDatabase.class).beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // todo 处理list保存
                //批量添加
                int i = 0;
                Iterator<BigSeaInfo0> iterator = resultList().iterator();
                while (iterator.hasNext()) {
                    BigSeaInfo0 info = iterator.next();
                    boolean success = info.save();
                    i = success ? ++i : i;
                }
                Log.i("zhh_db_sync", "成功添加了数据条数：" + i);


            }
        }).build();
        transaction.execute();
//        transaction.executeSync();//异步执行
    }

    private void insertBaseModle2() {
        FlowManager.getDatabase(AppDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<BigSeaInfo0>() {
                            @Override
                            public void processModel(BigSeaInfo0 bigSeaInfo0, DatabaseWrapper wrapper) {
                                // do work here -- i.e. user.delete() or user.update()
                                //增删改操作等
                                boolean success = bigSeaInfo0.save();
                                Log.i("zhh_db_sync", "添加结果" + success);
                            }

                        }).addAll(resultList()).build())  // add elements (can also handle multiple)
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                        App.showToast(DbFlowTransactionActivity.this, "error结果：" + error.getMessage());
                        Log.i("zhh_db_sync", "error结果" + error.getMessage());
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        App.showToast(DbFlowTransactionActivity.this, "success结果：" + transaction.name());
                        Log.i("zhh_db_sync", "添加success");
                    }
                }).build()
                // .execute();//同步
                .executeSync();//异步
    }

    private void updateBaseModle0() {
        //不存在的条件数据做更改，也走成功的方法
        SQLite.update(BigSeaInfo0.class)
                .set(BigSeaInfo0_Table.name.eq("PXXXX"))
                .where(BigSeaInfo0_Table.name.is("P0001"))
                .async()
                .error(new Transaction.Error() {
                    @Override
                    public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                        Log.i("zhh_db_sync", "异步修改error---" + error.getMessage());
                    }
                }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.i("zhh_db_sync", "异步修改success");
            }
        }).execute();
    }

    //异步查询：两种方式
    private void selectBaseModleSync() {
        SQLite.select().from(BigSeaInfo0.class).async().queryResultCallback(new QueryTransaction.QueryResultCallback<BigSeaInfo0>() {
            @Override
            public void onQueryResult(@NonNull QueryTransaction<BigSeaInfo0> transaction, @NonNull CursorResult<BigSeaInfo0> tResult) {
                BigSeaInfo0 bigSeaInfo0 = tResult.toModel();
                Log.i("zhh_db_sync", "对象查询-----异步1-->id---" + bigSeaInfo0.userId + ",name->" + bigSeaInfo0.name +
                        ",age---" + bigSeaInfo0.age + ",note--" + bigSeaInfo0.remarks + ",money-->" + bigSeaInfo0.money);
               // tResult.toList();集合返回
                tResult.close();//关闭资源
            }
        }).execute();

        SQLite.select().from(BigSeaInfo0.class).where(BigSeaInfo0_Table.name.is("P0000"))
                .async()//异步查询
                .querySingleResultCallback(new QueryTransaction.QueryResultSingleCallback<BigSeaInfo0>() {
                    @Override
                    public void onSingleQueryResult(QueryTransaction transaction, @Nullable BigSeaInfo0 bigSeaInfo0) {
                        if(null!=bigSeaInfo0){
                            Log.i("zhh_db_sync", "对象查询-----异步2--id---" + bigSeaInfo0.userId + ",name->" + bigSeaInfo0.name +
                                    ",age---" + bigSeaInfo0.age + ",note--" + bigSeaInfo0.remarks + ",money-->" + bigSeaInfo0.money);
                        }else{
                            App.showToast(DbFlowTransactionActivity.this,"数据为空");
                        }
                    }

                })
                .error(new Transaction.Error() {
                    @Override
                    public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                        Log.i("zhh_db_sync", "Sync-----error--" + error.getMessage());
                    }
                }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.i("zhh_db_sync", "Sync-----success--" + transaction.name());
            }
        }).execute();

    }

    //异步执行查询:尽管是异步的，但是线程安全的
    private void selectBaseModleSyncList() {
        SQLite.select().from(BigSeaInfo0.class)//.where(BigSeaInfo0_Table.name.is(""))
                .async()//异步查询
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<BigSeaInfo0>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<BigSeaInfo0> tResult) {
                        printData(tResult);//更新UI
                    }
                }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                Log.i("zhh_db_sync", "SyncList--error---" + error.getMessage());
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.i("zhh_db_sync", "SyncList---success--" );
            }
        }).execute();


    }

    private void printData(List<BigSeaInfo0> list) {
        if (null != list && list.size() > 0) {
            for (BigSeaInfo0 bigSeaInfo0 : list) {
                Log.i("zhh_db_sync", "id---" + bigSeaInfo0.userId + ",name->" + bigSeaInfo0.name +
                        ",age---" + bigSeaInfo0.age + ",note--" + bigSeaInfo0.remarks +
                        ",money-->" + bigSeaInfo0.money+"，remarks2："+bigSeaInfo0.remarks2);
            }
        } else {
            App.showToast(this, "数据为空");
        }

    }

    private List<BigSeaInfo0> resultList() {
        final List<BigSeaInfo0> list = new ArrayList<>();
        BigSeaInfo0 product0 = new BigSeaInfo0();
        product0.name = "P00" + index;
        product0.age = index;
        product0.remarks = "备注-" + index;
        product0.money = 500 * index;
        index++;
        BigSeaInfo0 product1 = new BigSeaInfo0();
        product1.name = "P00" + index;
        product1.age = index;
        product1.remarks = "备注-" + index;
        list.add(product0);
        list.add(product1);
        return list;
    }

}
