package com.android.wealth.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.wealth.App;
import com.android.wealth.bean.Bill;
import com.android.wealth.utils.NumberUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class BillDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "bill";//表名

    private static final String ID = "id";//id自增长
    private static final String STATUS = "status";
    private static final String MONEY = "money";
    private static final String USER_ID = "userId";
    private static final String CATEGORY = "category";
    private static final String CREATE_TIME = "createTime";
    private static final String BILL_DATE = "billDate";
    private static final String BILL_ID = "billId";
    private static final String REMAKE = "remake";

    //创建表结构
    private static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            ID + " integer primary key autoincrement," +
            STATUS + " integer," +
            MONEY + " double," +
            USER_ID + " text," +
            CATEGORY + " integer," +
            CREATE_TIME + " long," +
            BILL_DATE + " text," +
            BILL_ID + " text," +
            REMAKE + " text" +
            ")";

    public static BillDBHelper getInstance() {
        return InnerDB.instance;
    }

    private static class InnerDB {
        private static final BillDBHelper instance = new BillDBHelper(App.getContext());
    }


    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_test",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     * 不过，以现如今的内存容量，估计一辈子也见不到几次内存占满的状态
     * db = getReadableDatabase();
     */
    BillDBHelper(Context context) {
        super(context, "bill_db", null, 1);
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

    /**
     * 添加账单记录
     */
    public boolean addBill(String userId, int status, double money, int category, String remake, String billDate) {
        boolean success = false;
        SQLiteDatabase db = getReadableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(USER_ID, userId);
            cv.put(STATUS, status);
            cv.put(MONEY, money);
            cv.put(CATEGORY, category);
            cv.put(REMAKE, remake);
            cv.put(BILL_DATE, billDate);
            cv.put(BILL_ID, NumberUtil.getRandomId());
            cv.put(CREATE_TIME, System.currentTimeMillis());
            db.insert(TABLE_NAME, null, cv);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return success;
    }

    /**
     * 修改账单记录
     */
    public boolean updateBill(String userId,String billId, int status, double money, int category, String remake, String billDate) {
        boolean success = false;
        SQLiteDatabase db = getReadableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(STATUS, status);
            cv.put(MONEY, money);
            cv.put(CATEGORY, category);
            cv.put(REMAKE, remake);
            cv.put(BILL_DATE, billDate);
            int code = db.update(TABLE_NAME, cv, BILL_ID + "=? and " + USER_ID + "=? ", new String[]{billId, userId});
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return success;
    }

    @SuppressLint("Range")
    public Bill queryBill(String userId, String billId) {
        SQLiteDatabase db = getReadableDatabase();
        Bill bill = null;
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + USER_ID + " =? and " + BILL_ID + " =? ", new String[]{userId, billId});
        if (cursor != null && cursor.moveToFirst()) {
            bill = new Bill();
            bill.status = cursor.getInt(cursor.getColumnIndex(STATUS));
            bill.money = cursor.getDouble(cursor.getColumnIndex(MONEY));
            bill.category = cursor.getInt(cursor.getColumnIndex(CATEGORY));
            bill.createTime = cursor.getLong(cursor.getColumnIndex(CREATE_TIME));;
            bill.remake = cursor.getString(cursor.getColumnIndex(REMAKE));
            bill.userId = userId;
            bill.billId = billId;;
            bill.billDate = cursor.getString(cursor.getColumnIndex(BILL_DATE));
            ;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return bill;
    }

    // 删除账单
    public void deleteBill(String billId) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_NAME, BILL_ID + "=?", new String[]{billId});
    }



    // 根据日期,userId查询账单list
    @SuppressLint("Range")
    public Map<String, List<Bill>> queryBillList(String userId, String queryDay) {
        SQLiteDatabase db = getReadableDatabase();
        Map<String, List<Bill>> billMap = new LinkedHashMap<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+ BILL_DATE + " like ? and " + USER_ID + "=?  " +  " ORDER BY " + CREATE_TIME + " desc "
                , new String[]{"%" + queryDay + "%", userId});

        while (cursor.moveToNext()) {
            Bill bill = new Bill();
            bill.status = cursor.getInt(cursor.getColumnIndex(STATUS));
            bill.money = cursor.getDouble(cursor.getColumnIndex(MONEY));
            bill.category = cursor.getInt(cursor.getColumnIndex(CATEGORY));
            bill.createTime = cursor.getLong(cursor.getColumnIndex(CREATE_TIME));
            bill.remake = cursor.getString(cursor.getColumnIndex(REMAKE));
            bill.userId = userId;
            bill.billDate = cursor.getString(cursor.getColumnIndex(BILL_DATE));;
            bill.billId = cursor.getString(cursor.getColumnIndex(BILL_ID));;
            List<Bill> billList = billMap.get(bill.billDate);
            if (billList == null) {
                billList = new ArrayList<>();
                billList.add(bill);
                billMap.put(bill.billDate, billList);
            } else {
                billList.add(bill);
            }
        }
        cursor.close();
        db.close();
        return billMap;
    }

    // 根据年日期,userId查询账单list
    @SuppressLint("Range")
    public Map<String, ArrayList<Bill>> queryBillListByYear(String userId, String year) {
        SQLiteDatabase db = getReadableDatabase();
        Map<String, ArrayList<Bill>> billMap = new LinkedHashMap<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "+ BILL_DATE + " like ? and " + USER_ID + "=?  " +  " ORDER BY " + CREATE_TIME + " desc "
                , new String[]{"%" + year + "%", userId});

        while (cursor.moveToNext()) {
            Bill bill = new Bill();
            bill.status = cursor.getInt(cursor.getColumnIndex(STATUS));
            bill.money = cursor.getDouble(cursor.getColumnIndex(MONEY));
            bill.category = cursor.getInt(cursor.getColumnIndex(CATEGORY));
            bill.createTime = cursor.getLong(cursor.getColumnIndex(CREATE_TIME));
            bill.remake = cursor.getString(cursor.getColumnIndex(REMAKE));
            bill.userId = userId;
            bill.billDate = cursor.getString(cursor.getColumnIndex(BILL_DATE));;
            bill.billId = cursor.getString(cursor.getColumnIndex(BILL_ID));
            String month = bill.billDate.split("/")[1];
            ArrayList<Bill> billList = billMap.get(month);
            if (billList == null) {
                billList = new ArrayList<>();
                billList.add(bill);
                billMap.put(month, billList);
            } else {
                billList.add(bill);
            }
        }
        cursor.close();
        db.close();
        return billMap;
    }

    // 查询用户所有账单
    @SuppressLint("Range")
    public List<Bill> queryBillList(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        List<Bill> billList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + USER_ID + "=?  " +  " ORDER BY " + CREATE_TIME + " desc "
                , new String[]{userId});
        while (cursor.moveToNext()) {
            Bill bill = new Bill();
            bill.status = cursor.getInt(cursor.getColumnIndex(STATUS));
            bill.money = cursor.getDouble(cursor.getColumnIndex(MONEY));
            bill.category = cursor.getInt(cursor.getColumnIndex(CATEGORY));
            bill.createTime = cursor.getLong(cursor.getColumnIndex(CREATE_TIME));
            bill.remake = cursor.getString(cursor.getColumnIndex(REMAKE));
            bill.userId = userId;
            bill.billDate = cursor.getString(cursor.getColumnIndex(BILL_DATE));;
            bill.billId = cursor.getString(cursor.getColumnIndex(BILL_ID));
            billList.add(bill);
        }
        cursor.close();
        db.close();
        return billList;
    }

}