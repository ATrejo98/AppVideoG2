package com.uth.appvideog2;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.uth.appvideog2.db.DBHelper;

public class ActivityListaVideo extends AppCompatActivity {

    private DBHelper dbHelper;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_video);

        dbHelper = new DBHelper(this);
        listView = findViewById(R.id.listView);

        Cursor cursor = dbHelper.getAllVideos();

        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{DBHelper.COLUMN_VIDEO_URI},
                new int[]{android.R.id.text1},
                0);

        listView.setAdapter(adapter);
    }
}
