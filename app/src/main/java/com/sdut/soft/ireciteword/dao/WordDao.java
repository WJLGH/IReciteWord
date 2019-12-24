package com.sdut.soft.ireciteword.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.db.DBOpenHelper;
import com.sdut.soft.ireciteword.utils.Const;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordDao {
    private DBOpenHelper mDBOpenHelper;

    //Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example, Word_Unit;
    public WordDao(Context context) {
        mDBOpenHelper = DBOpenHelper.getInstance(context);
    }

    /**
     * 添加单词到指定的词汇表
     * 返回是否插入成功
     *
     * @param word
     * @param unit
     * @return
     */
    public boolean insertWordToUnit(Word word, Unit unit) {
        SQLiteDatabase database = mDBOpenHelper.getDatabase();
        boolean ret = false;
        try {
            ContentValues contentValues = new ContentValues();
            //Word_Key, Word_Phono, Word_Trans, Word_Example
            contentValues.put("Word_Key", word.getKey());
            contentValues.put("Word_Phono", word.getPhono());
            contentValues.put("Word_Trans", word.getTrans());
            contentValues.put("Word_Example", word.getExample());
            ret = database.insert(unit.getName(), null, contentValues) > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return ret;
    }

    /**
     * 从指定的词汇表中删除单词
     * 返回是否删除成功
     *
     * @param word
     * @param unit
     * @return
     */
    public boolean deleteWordFromUnit(Word word, Unit unit) {
        SQLiteDatabase database = mDBOpenHelper.getDatabase();
        boolean ret = false;
        try {
            ret = database.delete(unit.getName(), "Word_Id = ?", new String[]{"" + word.getId()}) > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return ret;
    }

    /**
     * 获取词汇表中单词的数量
     *
     * @param unit
     * @return
     */
    public int getTotalCnt(Unit unit) {
        String sql = null;
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        sql = " select count(*) from " + unit.getName();
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    /**
     * 查看单词在某个词汇表中的具体信息
     *
     * @param word
     * @param unit
     * @return
     */
    public Word getWordInUnit(Word word, Unit unit) {
        Word nword = null;
        SQLiteDatabase database = mDBOpenHelper.getDatabase();

        String sql = " select Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example from " + unit.getName() + " where Word_Key = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{word.getKey()});
        if (cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndex("Word_Id"));
            String key = cursor.getString(cursor.getColumnIndex("Word_Key"));
            String phono = cursor.getString(cursor.getColumnIndex("Word_Phono"));
            String trans = cursor.getString(cursor.getColumnIndex("Word_Trans"));
            String exam = cursor.getString(cursor.getColumnIndex("Word_Example"));
            nword = new Word(id, key, phono, trans, exam, unit.getId());
        }
        cursor.close();
        return nword;
    }

    /**
     * 查找翻译单词
     *
     * @param str
     * @return
     */
    public List<Word> searchWords(String str) {
        List<Word> words = null;
        String sql = null;
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        //是英文
        Cursor cursor = null;
        if (Character.isUpperCase(str.toUpperCase().charAt(0))) {
            sql = " select Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example ,Word_Unit from " + Const.DEFAULT_UNIT + " where Word_Key like ? ";
            cursor = db.rawQuery(sql, new String[]{String.valueOf(str + "%")});
        } else {
            sql = " select Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example ,Word_Unit  from " + Const.DEFAULT_UNIT + " where Word_Trans like ? ";
            cursor = db.rawQuery(sql, new String[]{String.valueOf("%" + str + "%")});
        }
        if (cursor.moveToFirst()) {
            words = new ArrayList<>(cursor.getCount());
            Word word;
            do {
                int id = cursor.getInt(cursor.getColumnIndex("Word_Id"));
                String key = cursor.getString(cursor.getColumnIndex("Word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("Word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("Word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("Word_Example"));
                Integer unit = cursor.getInt(cursor.getColumnIndex("Word_Unit"));
                word = new Word(id, key, phono, trans, exam, unit);
                words.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }


    /**
     * 获取词汇表中单词的数量
     *
     * @param tableName
     * @return
     */
    public int getTotalCnt(String tableName) {
        String sql = null;
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        sql = " select count(*) from " + tableName;
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }


    /**
     * 背诵新词的时候应该是 [rcindex ,rcindex + perday]
     * offset : rcindex
     * limit : perday
     *
     * @param metaKey
     * @param user
     * @return
     */
    public List<Word> getNewWords(String metaKey, User user) {
        List<Word> words = null;
        String sql = "select Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example ,Word_Unit from " + metaKey + " limit ? OFFSET ?";
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        String[] paras = {String.valueOf(user.getPerday()), String.valueOf(user.getRcindex())};
        Cursor cursor = db.rawQuery(sql, paras);
        if (cursor.moveToFirst()) {
            words = new ArrayList<>(cursor.getCount());
            Word word;
            do {
                int id = cursor.getInt(cursor.getColumnIndex("Word_Id"));
                String key = cursor.getString(cursor.getColumnIndex("Word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("Word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("Word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("Word_Example"));
                Integer unit = cursor.getInt(cursor.getColumnIndex("Word_Unit"));
                word = new Word(id, key, phono, trans, exam, unit);
                words.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }

    /**
     * 获取干扰项
     *
     * @param metaKey
     * @param main
     * @return
     */
    public List<Word> getOptionsFromUnit(String metaKey, Word main) {
        List<Word> words = null;
        String sql = "select Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example ,Word_Unit from " + metaKey + " where Word_Id != ? and Word_Unit = ? ORDER BY RANDOM() limit 3";
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{
                String.valueOf(main.getId()),
                String.valueOf(main.getUnit())
        });
        if (cursor.moveToFirst()) {
            words = new ArrayList<>(cursor.getCount());
            Word word;
            do {
                int id = cursor.getInt(cursor.getColumnIndex("Word_Id"));
                String key = cursor.getString(cursor.getColumnIndex("Word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("Word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("Word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("Word_Example"));
                Integer unit = cursor.getInt(cursor.getColumnIndex("Word_Unit"));
                word = new Word(id, key, phono, trans, exam, unit);
                words.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }

    /**
     * 复习词汇时是 [rvindex, rcindex]
     * offset: rvindex
     * limit: rcindex - rvindex > 0
     *
     * @param metaKey
     * @param user
     * @return
     */
    public List<Word> getReviewWords(String metaKey, User user) {
        List<Word> words = null;
        String sql = "select Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example ,Word_Unit from " + metaKey + " limit ? OFFSET ?";
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        long size = user.getRcindex() - user.getRvindex();
        size = size >= 0 ? size : 0;
        size = size < user.getPerday() ? size : user.getPerday();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(size), String.valueOf(user.getRvindex())});
        if (cursor.moveToFirst()) {
            words = new ArrayList<>(cursor.getCount());
            Word word;
            do {
                int id = cursor.getInt(cursor.getColumnIndex("Word_Id"));
                String key = cursor.getString(cursor.getColumnIndex("Word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("Word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("Word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("Word_Example"));
                Integer unit = cursor.getInt(cursor.getColumnIndex("Word_Unit"));
                word = new Word(id, key, phono, trans, exam, unit);
                words.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }

    /**
     * 根据词汇表和单词id获得单词
     *
     * @param metaKey
     * @param wid
     * @return
     */
    public Word getWordById(String metaKey, int wid) {
        Word word = null;
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        Cursor cursor = db.query(metaKey, null, "Word_Id = ?", new String[]{String.valueOf(wid)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("Word_Id"));
                String key = cursor.getString(cursor.getColumnIndex("Word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("Word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("Word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("Word_Example"));
                Integer unit = cursor.getInt(cursor.getColumnIndex("Word_Unit"));
                word = new Word(id, key, phono, trans, exam, unit);
            } while (cursor.moveToNext());
        }
        return word;
    }

    public List<Word> getNewWordsFromUnit(Unit unit, int perDay) {
        List<Word> words = null;
        String sql = "select Word_Id, Word_Key, Word_Phono, Word_Trans, Word_Example  from " + unit.getName() + " limit ? OFFSET ?";
        SQLiteDatabase db = mDBOpenHelper.getDatabase();
        String[] paras = {String.valueOf(perDay), String.valueOf(unit.getProgress())};
        Cursor cursor = db.rawQuery(sql, paras);
        if (cursor.moveToFirst()) {
            words = new ArrayList<>(cursor.getCount());
            Word word;
            do {
                int id = cursor.getInt(cursor.getColumnIndex("Word_Id"));
                String key = cursor.getString(cursor.getColumnIndex("Word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("Word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("Word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("Word_Example"));
//                Integer unit = cursor.getInt(cursor.getColumnIndex("Word_Unit"));
                word = new Word(id, key, phono, trans, exam, unit.getId());
                words.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }

    public List<Word> getAllWord(String unitName) {
        List<Word> words = null;
        String sql = "select Word_Key, Word_Phono, Word_Trans, Word_Example  from " + unitName;
        SQLiteDatabase db = mDBOpenHelper.getDatabase();

        Cursor cursor = db.rawQuery(sql ,null);
        if (cursor.moveToFirst()) {
            words = new ArrayList<>(cursor.getCount());
            Word word;
            do {
                String key = cursor.getString(cursor.getColumnIndex("Word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("Word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("Word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("Word_Example"));
//                Integer unit = cursor.getInt(cursor.getColumnIndex("Word_Unit"));
                word = new Word( key, phono, trans, exam);
                words.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }
}
