package edu.ncuindia.healthapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "email";
    private static final String COL_1 = "name";
    private static final String COL_2 = "email";
    private static final String COL_3 = "date";
    private static final String COL_4 = "place";
    private static final String COL_5 = "gender";
    private static final String COL_6 = "height";
    private static final String COL_7 = "weight";
    private static final String COL_8 = "stepgoal";
    private static final String COL_9 = "weightgoal";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME +
                        "(name text,email text primary key , date text,place text, gender text, height float, weight float, stepgoal int, weightgoal int)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser (String name, String email, String date,String country, String gender, float height, float weight, Integer stepGoal, Integer weightGoal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, email);
        contentValues.put(COL_3, date);
        contentValues.put(COL_4, country);
        contentValues.put(COL_5, gender);
        contentValues.put(COL_6, height);
        contentValues.put(COL_7, weight);
        contentValues.put(COL_8, stepGoal);
        contentValues.put(COL_9, weightGoal);
        long res = db.insert(TABLE_NAME, null, contentValues);
        if(res != -1)
            return true;
        else
            return false;
    }

    public Cursor getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = String.format("select * from %s where %s = %s", TABLE_NAME, COLUMN_ID, email);
        Cursor res =  db.rawQuery(selectQuery , null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateUser (String name, String email, String date,String country, String gender, float height, float weight, Integer stepGoal, Integer weightGoal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_3, date);
        contentValues.put(COL_4, country);
        contentValues.put(COL_5, gender);
        contentValues.put(COL_6, height);
        contentValues.put(COL_7, weight);
        contentValues.put(COL_8, stepGoal);
        contentValues.put(COL_9, weightGoal);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ? ", new String[] { email } );
        return true;
    }

    public Integer deleteUser (String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[] { email });
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(COL_1)));
            res.moveToNext();
        }
        return array_list;
    }
}
