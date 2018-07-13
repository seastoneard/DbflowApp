package com.zhanghai.dbflowapp.dbflow;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.zhanghai.dbflowapp.app.DbBaseView;
import com.zhanghai.dbflowapp.bean.BigSeaInfo0;
import com.zhanghai.dbflowapp.bean.BigSeaInfo0_Table;

import java.util.List;

/**
 * Created by zhanghai on 2018/07/07.
 * BigSeaInfo表--对应的事物封装处理
 */

public class DbTransactionSyncUtils {

    private DbBaseView<BigSeaInfo0> dbBaseView = null;

    public DbTransactionSyncUtils(DbBaseView<BigSeaInfo0> dbBaseView) {
        this.dbBaseView = dbBaseView;
    }

    public void saveBaseModle(final BigSeaInfo0 info) {
        resultDb(FlowManager.getDatabase(AppDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<BigSeaInfo0>() {
                            @Override
                            public void processModel(BigSeaInfo0 bigSeaInfo0, DatabaseWrapper wrapper) {
                                // do work here -- i.e. user.delete() or user.update()
                                //增删改操作等
                                 bigSeaInfo0.save();
                            }

                        }).add(info).build()));  // add elements (can also handle multiple)


    }

    public void saveBaseModle(List<BigSeaInfo0> list) {
        resultDb(FlowManager.getDatabase(AppDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<BigSeaInfo0>() {
                            @Override
                            public void processModel(BigSeaInfo0 bigSeaInfo0, DatabaseWrapper wrapper) {
                                // do work here -- i.e. user.delete() or user.update()
                                //增删改操作等
                                boolean success = bigSeaInfo0.save();//事物回滚循环添加

                            }

                        }).addAll(list).build()));  // add elements (can also handle multiple)

    }

    public void updateBaseModle(BigSeaInfo0 info0) {
        //不存在的条件数据做更改，也走成功的方法
        SQLite.update(BigSeaInfo0.class)
                .set(BigSeaInfo0_Table.name.eq("PXXXX"))
                .where(BigSeaInfo0_Table.userId.is(info0.userId))
                .async()
                .error(new Transaction.Error() {
                    @Override
                    public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                        Log.i("zhh_db_sync", "异步修改error---" + error.getMessage());
                        dbBaseView.onError(error.getMessage());
                    }
                }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.i("zhh_db_sync", "异步修改success---");
                dbBaseView.onSuccess(true, DbBaseView.DbType.UPDSTE_DELETE);
            }
        }).execute();
    }

    public void deleteBaseModle(BigSeaInfo0 info0) {
        //不存在的条件数据做更改，也走成功的方法
        SQLite.delete(BigSeaInfo0.class)
                .where(BigSeaInfo0_Table.userId.is(info0.userId))
                .async()
                .error(new Transaction.Error() {
                    @Override
                    public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                        Log.i("zhh_db_sync", "异步修改error---" + error.getMessage());
                        dbBaseView.onError(error.getMessage());
                    }
                }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.i("zhh_db_sync", "异步修改success---");
                dbBaseView.onSuccess(true, DbBaseView.DbType.UPDSTE_DELETE);
            }
        }).execute();
    }


    public void selectModel() {
        //查询方法1:查对象||集合
        SQLite.select().from(BigSeaInfo0.class).async().queryResultCallback(new QueryTransaction.QueryResultCallback<BigSeaInfo0>() {
            @Override
            public void onQueryResult(@NonNull QueryTransaction<BigSeaInfo0> transaction, @NonNull CursorResult<BigSeaInfo0> tResult) {
                BigSeaInfo0 bigSeaInfo0 = tResult.toModel();
//                List<BigSeaInfo0> list=tResult.toList();
                dbBaseView.onSuccess(bigSeaInfo0);
//                dbBaseView.onSuccessList(list);
                tResult.close();//关闭资源
            }
        }).execute();

        //查询方法2：对象
        SQLite.select().from(BigSeaInfo0.class).where(BigSeaInfo0_Table.name.is("P00010"))
                .async()//异步查询
                .querySingleResultCallback(new QueryTransaction.QueryResultSingleCallback<BigSeaInfo0>() {
                    @Override
                    public void onSingleQueryResult(QueryTransaction transaction, @Nullable BigSeaInfo0 bigSeaInfo0) {
                        dbBaseView.onSuccess(bigSeaInfo0);
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
                Log.i("zhh_db_sync", "Sync-----success--" );
            }
        }).execute();

        //查询方法3：集合
        SQLite.select().from(BigSeaInfo0.class)//.where(BigSeaInfo0_Table.name.is(""))
                .async()//异步查询
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<BigSeaInfo0>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<BigSeaInfo0> tResult) {
                        dbBaseView.onSuccessList(tResult);
                    }
                }).error(new Transaction.Error() {
            @Override
            public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                Log.i(" ", "SyncList--error---" + error.getMessage());
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                Log.i("zhh_db_sync", "SyncList---success--" );
            }
        }).execute();


    }


    protected void resultDb(Transaction.Builder builder) {
        builder.error(new Transaction.Error() {
            @Override
            public void onError(Transaction transaction, Throwable error) {
                Log.i("zhh_db_sync", "error结果" + error.getMessage());
                dbBaseView.onError(error.getMessage());
            }
        })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        Log.i("zhh_db_sync", "resultDb操作返回：success");
                        dbBaseView.onSuccess(true, DbBaseView.DbType.SAVE_MODEL);
                    }
                }).build()
                .executeSync();//异步
    }


}
