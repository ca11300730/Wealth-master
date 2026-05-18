package com.android.wealth.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.wealth.App;
import com.android.wealth.bean.User;
import com.android.wealth.utils.NumberUtil;


public class UserDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "user";//表名

    private static final String ID = "id";//id自增长
    private static final String USER_NAME = "userName";
    private static final String USER_ID = "userId";
    private static final String PASS_WORD = "passWord";
    private static final String AVATAR = "avatar";

    //创建表结构
    private static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            ID + " integer primary key autoincrement," +
            USER_NAME + " text," +
            USER_ID + " text," +
            AVATAR + " text," +
            PASS_WORD + " text" +
            ")";

    public static UserDBHelper getInstance() {
        return InnerDB.instance;
    }

    private static class InnerDB {
        private static final UserDBHelper instance = new UserDBHelper(App.getContext());
    }


    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_test",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     * 不过，以现如今的内存容量，估计一辈子也见不到几次内存占满的状态
     * db = getReadableDatabase();
     */
    UserDBHelper(Context context) {
        super(context, "user_db", null, 1);
    }

    /**
     * 重写两个必须要重写的方法，因为class DBOpenHelper extends SQLiteOpenHelper
     * 而这两个方法是 abstract 类 SQLiteOpenHelper 中声明的 abstract 方法
     * 所以必须在子类 DBOpenHelper 中重写 abstract 方法
     * 因为，一个数据库表，首先是要被创建的，然后免不了是要进行增删改操作的
     * 所以就有onCreate()、onUpgrade()两个方法
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    //版本适应
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    /**
     * 注册新增用户
     * addUser()
     */
    public boolean addUser(String name, String password) {
        boolean success = false;
        SQLiteDatabase db = getReadableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(USER_NAME, name);
            cv.put(PASS_WORD, password);
            cv.put(AVATAR, "");
            cv.put(USER_ID, NumberUtil.getRandomId());
            db.insert(TABLE_NAME, null, cv);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return success;
    }

    public boolean updateUserAvatar(String userId, String avatar) {
        boolean success = false;
        SQLiteDatabase db = getReadableDatabase();
        String updateQuery = "UPDATE " + TABLE_NAME + " SET " + AVATAR + " = '" + avatar + "' WHERE " + USER_ID + " = '" + userId + "'";
        db.beginTransaction();
        try {
            db.execSQL(updateQuery);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常情况
        } finally {
            db.endTransaction();
        }
        return success;
    }

    public boolean changePwd(String userId, String newPwd) {
        boolean success = false;
        SQLiteDatabase db = getReadableDatabase();
        String updateQuery = "UPDATE " + TABLE_NAME + " SET " + PASS_WORD + " = " + newPwd + " WHERE " + USER_ID + " = '" + userId + "'";
        db.beginTransaction();
        try {
            db.execSQL(updateQuery);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常情况
        } finally {
            db.endTransaction();
        }
        success = true;
        return success;
    }

    // 查询用户是否存在
    @SuppressLint("Range")
    public boolean queryUser(String name) {
        SQLiteDatabase db = getReadableDatabase();
        boolean hasUser = false;
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + USER_NAME + " =?", new String[]{name});
        if (cursor != null && cursor.moveToFirst()) {
            String userName = cursor.getString(cursor.getColumnIndex(USER_NAME));
            String passWord = cursor.getString(cursor.getColumnIndex(PASS_WORD));
            hasUser = true;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return hasUser;
    }

    // 查询用户
    @SuppressLint("Range")
    public User queryUserById(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + USER_ID + " =? ", new String[]{userId});
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.userName = cursor.getString(cursor.getColumnIndex(USER_NAME));
            user.passWord = cursor.getString(cursor.getColumnIndex(PASS_WORD));
            user.userId = cursor.getString(cursor.getColumnIndex(USER_ID));
            user.avatar = cursor.getString(cursor.getColumnIndex(AVATAR));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return user;
    }

    // 查询用户名、密码是否正确
    @SuppressLint("Range")
    public User queryUser(String name, String passWord) {
        SQLiteDatabase db = getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + USER_NAME + " =? and " + PASS_WORD + " =? "
                , new String[]{name, passWord});
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.userName = cursor.getString(cursor.getColumnIndex(USER_NAME));
            user.passWord = cursor.getString(cursor.getColumnIndex(PASS_WORD));
            user.userId = cursor.getString(cursor.getColumnIndex(USER_ID));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return user;
    }

}