package com.uth.appvideog2.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "VideosDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_VIDEOS = "videos";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_VIDEO_URI = "video_uri";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VIDEOS_TABLE = "CREATE TABLE " + TABLE_VIDEOS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_VIDEO_URI + " TEXT" +
                ")";
        db.execSQL(CREATE_VIDEOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        onCreate(db);
    }

    public long insertVideo(String videoUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VIDEO_URI, videoUri);
        long id = db.insert(TABLE_VIDEOS, null, values);
        db.close();
        return id;
    }

    public Cursor getAllVideos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_VIDEOS, new String[]{COLUMN_ID + " AS _id", COLUMN_VIDEO_URI}, null, null, null, null, null);
    }


}
