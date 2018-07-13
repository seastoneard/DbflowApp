package com.zhanghai.dbflowapp.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zhanghai.dbflowapp.dbflow.AppDatabase;

/**
 * Created by zhanghai on 2018/07/07.
 * 创建完成后记得: 点击Build下架的：Make Project    或者点击一个'锤子'的图标
 */

@Table(database =AppDatabase.class)
public class BigSeaInfo0 extends BaseModel{

    @PrimaryKey(autoincrement = true)
    @Column
    public int userId;//必须是共有的修饰符，如果使用私有属性则返回（老的版本记得写get/set方法就可以了）：
    // Error:Execution failed for task ':xxx:compileDebugJavaWithJavac'.> Compilation failed; see the compiler error output for details.


    @Column
    public String name;


    @Column(defaultValue = "18")//默认值无效
    public int age=20;//有效

    @Column//(name = "desc")
    public String remarks;

    @Column
    public long money=3000;

    @Column
    public String remarks2="未知";

    //备注：DBFlow会根据你的类名自动生成一个表明，以此为例：
    //这个类对应的表名为：UserData_Table


    //常用注解操作符：

   // @Table、
    // @PrimaryKey、//主键
    // @Column、//列
    // @Unique、:唯一的
    // @ForeignKey:外键,一般数据裤不常用
    // 等

}
