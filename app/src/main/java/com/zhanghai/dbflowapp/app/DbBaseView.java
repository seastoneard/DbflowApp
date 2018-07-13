package com.zhanghai.dbflowapp.app;

import java.util.List;

/**
 * Created by zhanghai on 2018/07/07.
 */

public interface DbBaseView<T> {

    void onSuccess(T t);//查询操作

    void onSuccessList(List<T> list);//查询操作

    void onSuccess(boolean isSuccess, DbType type);//增删改操作

    void onError(String msg);

    enum DbType{
        SAVE_MODEL,
        SAVE_MODEL_LIST,
        UPDSTE_DELETE;
    }
}
