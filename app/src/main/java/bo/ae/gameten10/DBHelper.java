package bo.ae.gameten10;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE points (_id INTEGER PRIMARY KEY, count INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý khi nâng cấp cơ sở dữ liệu
    }

    // Phương thức để thêm giá trị count vào cơ sở dữ liệu
    public void addCount(int count) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("count", count);

        db.insert("points", null, values);

        db.close();
    }

    // Phương thức để lấy giá trị count từ cơ sở dữ liệu
    public ArrayList<Integer> getTop10Points() {
        ArrayList<Integer> topPointsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("points", new String[]{"count"}, null, null, null, null, "count DESC", "10");

        while (cursor.moveToNext()) {
            int point = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
            topPointsList.add(point);
        }

        cursor.close();
        db.close();

        return topPointsList;
    }
}
