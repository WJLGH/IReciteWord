package com.sdut.soft.ireciteword.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.db.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class UnitDao {
    private DBOpenHelper mDBOpenHelper;

    public UnitDao(Context context) {
        mDBOpenHelper = DBOpenHelper.getInstance(context);
    }

    /**
     * 添加新的词汇表
     * 返回是否创建成功
     * @param unitName
     */
    public boolean createUnit(String unitName) {
        SQLiteDatabase database = mDBOpenHelper.getDatabase();
        try {
            database.execSQL("CREATE TABLE \""+unitName+"\" ( " +
                    "  Word_Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "  Word_Key  TEXT, " +
                    "  Word_Phono TEXT, " +
                    "  Word_Trans TEXT, " +
                    "  Word_Example TEXT   "+
                    " )");

            ContentValues contentValues = new ContentValues();
            contentValues.put("Unit_name",unitName);
            database.insert("TABLE_UNIT",null,contentValues);

            database.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 查询所有词汇表
     * 返回所有的词汇表
     * @return
     */
    public List<Unit> getUnits() {
        List<Unit> units = null;
        String sql = "select Unit_id, Unit_name ,Unit_Progress from TABLE_UNIT";
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
            Unit unit;
            units = new ArrayList<>(cursor.getCount());
            do {
                int id = cursor.getInt(cursor.getColumnIndex("Unit_Id"));
                String name = cursor.getString(cursor.getColumnIndex("Unit_Name"));
                int progress = cursor.getInt(cursor.getColumnIndex("Unit_Progress"));
                int cnt = getTotalCnt(name);
                unit = new Unit(id, name,progress,cnt);
                units.add(unit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return units;
    }
    /**
     * 获取词汇表中单词的数量
     * @param unitName
     * @return
     */
    public int getTotalCnt(String unitName) {
        String sql = null;
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        sql = " select count(*) from " + unitName;
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    /**
     * 删除词汇表
     */
    public boolean deleteUnit(Unit unit) {
        SQLiteDatabase database = mDBOpenHelper.getDatabase();
        try{
            database.delete("TABLE_UNIT","Unit_Id = ?",new String[]{""+unit.getId()});
            database.execSQL("drop table "+unit.getName());

        }catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
        return true;
    }

    public Unit getUnitByName(String tableName) {
        Unit unit= null;
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        Cursor cursor = db.query("TABLE_UNIT", null, "Unit_Name = ? ", new String[]{tableName}, null, null, null);
        int count = 0;
        if (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("Unit_Id"));
            String name = cursor.getString(cursor.getColumnIndex("Unit_Name"));
            int progress = cursor.getInt(cursor.getColumnIndex("Unit_Progress"));
            int cnt = getTotalCnt(name);
            unit = new Unit(id, name,progress,cnt);
        }
        cursor.close();
       return unit;
    }

    public boolean saveUnit(Unit unit) {
        //update
        SQLiteDatabase database = mDBOpenHelper.getDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Unit_Progress",unit.getProgress());
        int update = database.update("TABLE_UNIT", contentValues, "Unit_Id = ?", new String[]{"" + unit.getId()});
        return update > 0 ;
    }
}
