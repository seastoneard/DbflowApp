package com.zhanghai.dbflowapp.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zhanghai.dbflowapp.dbflow.AppDatabase;

/**
 * Created by zhanghai on 2018/6/26.
 * 升级表示用到的类：解开注释即可
 */
//@Table(database =AppDatabase.class)
//public class BigSeaInfo1 extends BaseModel {
//
//    @PrimaryKey(autoincrement = true)
//    @Column
//    public int storeId;//必须是共有的修饰符,如果使用私有属性则返回：
//    // Error:Execution failed for task ':xxx:compileDebugJavaWithJavac'.> Compilation failed; see the compiler error output for details.
//
//    @Column
//    public String storeName;
//
//    public int getStoreId() {
//        return storeId;
//    }
//
//    public void setStoreId(int storeId) {
//        this.storeId = storeId;
//    }
//
//    public String getStoreName() {
//        return storeName;
//    }
//
//    public void setSotreName(String storeName) {
//        this.storeName = storeName;
//    }
//
//
//    //----------------升级-新增表：改版本好即可
//    //----------------升级-新增列：见AppDataBase.class
//}
