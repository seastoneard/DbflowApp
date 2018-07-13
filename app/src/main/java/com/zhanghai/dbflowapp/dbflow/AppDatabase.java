package com.zhanghai.dbflowapp.dbflow;


import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.zhanghai.dbflowapp.bean.BigSeaInfo0;

/**
 * Created by zhanghai on 2018/7/07.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    //版本号
    public static final int VERSION = 1;

    //数据库名称
    public static final String NAME = "App2018DB";


    //--------------------数据库升级：建议写在该类下
    //记住：后面的代码是执行升级操作的，第一次的就不用后面的代码了
    /*
        AlterTableMigration 用于重命名表，增加列：重命名你的model类@Table(name = "{newName}")
        IndexMigration/IndexPropertyMigration 用于索引创建和删除--字段条件等
        UpdateTableMigration 升级数据库的时候更新数据
    */


    //数据库升级--添加列（设置默认值）&新增表----版本改为2，之前是1（个人建议version=10  ---》参照build.gradle中的versionName：1.0）

    //解开注释：即可使用，

//    @Migration(version = VERSION, database = AppDatabase.class)//=2的升级
//    public static class Migration1 extends AlterTableMigration<BigSeaInfo0> {
////        Migration可设置优先级，高优先级的将先被执行。
////        Migration有3个方法：
////
////        onPreMigrate() 首先被调用，在这儿设置和构造
////        migrate() 具体的迁移在这儿执行
////        onPostMigrate() 执行一些清理工作，或者执行完成的通知
//
//        public Migration1(Class<BigSeaInfo0> table) {
//            super(table);
//        }
//
//        @Override
//        public void onPreMigrate() {
//            //所有Java标准的数据类型(boolean、byte、short、int、long、float、double等)及相应的包装类，
//            // 以及String，当然我们还默认提供了对java.util.Date、java.sql.Date与Calendar的支持。
//
//            // 使用如下：这里值添加remarks2
//
////            addColumn(SQLiteType.get(long.class.getName()), "money");//基本数据类型
////            addColumn(SQLiteType.get(int.class.getName()), "money");//基本数据类型
////            addColumn(SQLiteType.get(double.class.getName()), "money");//基本数据类型:浮点数
////            addColumn(SQLiteType.get(String.class.getName()), "money");//基本数据类型:浮点数
//            addColumn(SQLiteType.TEXT, "remarks2");
//
//        }
//    }

}
