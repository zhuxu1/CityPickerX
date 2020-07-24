package com.zhuxu.citypickerz.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;
import com.zhuxu.citypickerz.model.CityBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.zhuxu.citypickerz.db.DBConfig.COLUMN_C_CODE;
import static com.zhuxu.citypickerz.db.DBConfig.COLUMN_C_NAME;
import static com.zhuxu.citypickerz.db.DBConfig.COLUMN_C_PINYIN;
import static com.zhuxu.citypickerz.db.DBConfig.COLUMN_C_PROVINCE;
import static com.zhuxu.citypickerz.db.DBConfig.DB_NAME_V1;
import static com.zhuxu.citypickerz.db.DBConfig.LATEST_DB_NAME;
import static com.zhuxu.citypickerz.db.DBConfig.TABLE_NAME;

/**
 * Author Bro0cL on 2016/1/26.
 */
public class DBManager {
    private static final int BUFFER_SIZE = 1024;

    private String DB_PATH;
    private Context mContext;

    public DBManager(Context context) {
        this.mContext = context;
        DB_PATH = File.separator + "data"
                + Environment.getDataDirectory().getAbsolutePath() + File.separator
                + context.getPackageName() + File.separator + "databases" + File.separator;
        copyDBFile();
    }

    private void copyDBFile() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //如果旧版数据库存在，则删除
        File dbV1 = new File(DB_PATH + DB_NAME_V1);
        if (dbV1.exists()) {
            dbV1.delete();
        }
        //创建新版本数据库
        File dbFile = new File(DB_PATH + LATEST_DB_NAME);
        if (!dbFile.exists()) {
            InputStream is;
            OutputStream os;
            try {
                is = mContext.getResources().getAssets().open(LATEST_DB_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("zhuxu", "create db failed " + e.getMessage());
            }
        } else {
            Log.e("zhuxu", " ========================= create db file failed ======== dbFile.exists =====================");
        }
    }

    public List<CityBean> getAllCities() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        List<CityBean> result = new ArrayList<>();
        CityBean city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
//            city = new City(name, province, pinyin, code);
            city = new CityBean(name, province, code, pinyin, CityBean.TYPE_STR_LIST);
            result.add(city);
        }
        Log.e("zhuxu", "city size is " + result.size());
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    public List<CityBean> searchCity(final String keyword) {
        String sql = "select * from " + TABLE_NAME + " where "
                + COLUMN_C_NAME + " like ? " + "or "
                + COLUMN_C_PINYIN + " like ? ";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery(sql, new String[]{"%" + keyword + "%", keyword + "%"});

        List<CityBean> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
            CityBean city = new CityBean(name, province, code, pinyin, CityBean.TYPE_STR_LIST);
            result.add(city);
        }
        cursor.close();
        db.close();
        CityComparator comparator = new CityComparator();
        Collections.sort(result, comparator);
        return result;
    }

    /**
     * sort by a-z
     */
    private class CityComparator implements Comparator<CityBean> {
        @Override
        public int compare(CityBean lhs, CityBean rhs) {
            if (TextUtils.isEmpty(lhs.getPinyin())) {
                lhs.setPinyin(Pinyin.toPinyin(lhs.getName().charAt(0)));
            }
            if (TextUtils.isEmpty(rhs.getPinyin())) {
                rhs.setPinyin(Pinyin.toPinyin(rhs.getName().charAt(0)));
            }
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }
}
