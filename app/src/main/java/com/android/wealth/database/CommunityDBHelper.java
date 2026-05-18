package com.android.wealth.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.android.wealth.App;
import com.android.wealth.bean.Bill;
import com.android.wealth.bean.Comment;
import com.android.wealth.utils.NumberUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CommunityDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "community";//表名

    private static final String ID = "id";//id自增长
    private static final String USER_NAME = "userName";
    private static final String USER_ID = "userId";
    private static final String CONTENT = "content";
    private static final String COMMENT_ID = "commentId";
    private static final String CREATE_TIME = "createTime";
    private static final String COMMENT_NUM = "commentNum";
    private static final String PARENT_COMMENT_ID = "parentCommentId";

    //创建表结构
    private static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            ID + " integer primary key autoincrement," +
            CONTENT + " text," +
            USER_ID + " text," +
            USER_NAME + " text," +
            CREATE_TIME + " long," +
            COMMENT_NUM + " integer," +
            COMMENT_ID + " text," +
            PARENT_COMMENT_ID + " text" +
            ")";

    public static CommunityDBHelper getInstance() {
        return InnerDB.instance;
    }

    private static class InnerDB {
        private static final CommunityDBHelper instance = new CommunityDBHelper(App.getContext());
    }


    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_test",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     * 不过，以现如今的内存容量，估计一辈子也见不到几次内存占满的状态
     * db = getReadableDatabase();
     */
    CommunityDBHelper(Context context) {
        super(context, "community_db", null, 1);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean addComment(String userId, String userName, String content) {
        return this.addComment(userId, userName, content,"",0);
    }

    public boolean addComment(String userId, String userName, String content, String parentCommentId,int commentNum) {
        boolean success = false;
        SQLiteDatabase db = getReadableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(USER_ID, userId);
            cv.put(USER_NAME, userName);
            cv.put(CONTENT, content);
            cv.put(COMMENT_NUM, 0);
            cv.put(PARENT_COMMENT_ID, parentCommentId);
            cv.put(COMMENT_ID, NumberUtil.getRandomId());
            cv.put(CREATE_TIME, System.currentTimeMillis());
            db.insert(TABLE_NAME, null, cv);
            if (!TextUtils.isEmpty(parentCommentId)){
                cv.clear();
                cv.put(COMMENT_NUM, commentNum);
                db.update(TABLE_NAME, cv, COMMENT_ID + "=? ", new String[]{parentCommentId});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return success;
    }

    @SuppressLint("Range")
    public Comment queryComment(String userId, String commentId) {
        SQLiteDatabase db = getReadableDatabase();
        Comment comment = null;
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + USER_ID + " =? and " + COMMENT_ID + " =? ", new String[]{userId, commentId});
        if (cursor != null && cursor.moveToFirst()) {
            comment = new Comment();
            comment.createTime = cursor.getLong(cursor.getColumnIndex(CREATE_TIME));
            comment.content = cursor.getString(cursor.getColumnIndex(CONTENT));
            comment.userId = cursor.getString(cursor.getColumnIndex(USER_ID));
            comment.userName = cursor.getString(cursor.getColumnIndex(USER_NAME));
            comment.commentId = cursor.getString(cursor.getColumnIndex(COMMENT_ID));
            comment.commentNum = cursor.getInt(cursor.getColumnIndex(COMMENT_NUM));
            comment.parentCommentId = cursor.getString(cursor.getColumnIndex(PARENT_COMMENT_ID));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return comment;
    }

    @SuppressLint("Range")
    public List<Comment> queryCommentList( String commentId) {
        SQLiteDatabase db = getReadableDatabase();
        List<Comment> list= new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " +  PARENT_COMMENT_ID + " =? "+ " ORDER BY " + CREATE_TIME + " desc "
                , new String[]{commentId});

        while (cursor.moveToNext()) {
            Comment comment = new Comment();
            comment.createTime = cursor.getLong(cursor.getColumnIndex(CREATE_TIME));
            comment.content = cursor.getString(cursor.getColumnIndex(CONTENT));
            comment.userId = cursor.getString(cursor.getColumnIndex(USER_ID));
            comment.userName = cursor.getString(cursor.getColumnIndex(USER_NAME));
            comment.commentId = cursor.getString(cursor.getColumnIndex(COMMENT_ID));
            comment.commentNum = cursor.getInt(cursor.getColumnIndex(COMMENT_NUM));
            comment.parentCommentId = cursor.getString(cursor.getColumnIndex(PARENT_COMMENT_ID));
            list.add(comment);
        }
        cursor.close();
        db.close();
        return list;
    }


    @SuppressLint("Range")
    public List<Comment> queryCommentList() {
        SQLiteDatabase db = getReadableDatabase();
        List<Comment> list= new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + PARENT_COMMENT_ID +  "=?  " + " ORDER BY " + CREATE_TIME + " desc "
                , new String[]{""});

        while (cursor.moveToNext()) {
            Comment comment = new Comment();
            comment.createTime = cursor.getLong(cursor.getColumnIndex(CREATE_TIME));
            comment.content = cursor.getString(cursor.getColumnIndex(CONTENT));
            comment.userId = cursor.getString(cursor.getColumnIndex(USER_ID));
            comment.userName = cursor.getString(cursor.getColumnIndex(USER_NAME));
            comment.commentId = cursor.getString(cursor.getColumnIndex(COMMENT_ID));
            comment.commentNum = cursor.getInt(cursor.getColumnIndex(COMMENT_NUM));
            comment.parentCommentId = cursor.getString(cursor.getColumnIndex(PARENT_COMMENT_ID));
            list.add(comment);
        }
        cursor.close();
        db.close();
        return list;
    }

}