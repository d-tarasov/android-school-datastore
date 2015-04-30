package ru.tinkoff.example.datastore;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy Tarasov
 */
public class SqliteActivity extends ListActivity {

    private static final String TAG = SqliteActivity.class.getName();

    private EditText idView;
    private EditText valueView;

    private EntityDao dao;

    public static void start(Context context) {
        Intent intent = new Intent(context, SqliteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        idView = (EditText) findViewById(R.id.id_view);
        valueView = (EditText) findViewById(R.id.value_view);

        dao = new EntityDao(this);
        try {
            dao.open();
        } catch (SQLException e) {
            Log.e(TAG, "Cannot open db", e);
        }

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entity e = (Entity) getListAdapter().getItem(position);
                idView.setText(String.valueOf(e.id));
            }
        });
        refreshListView();
    }

    public void onPlusClicked(View v) {
        if (TextUtils.isEmpty(idView.getText())) {
            Entity entity = new Entity();
            entity.value = valueView.getText().toString();
            long id = dao.create(entity);
            if (id > 0) {
                showToast("Created row with id = " + id);
            } else {
                showToast("Cannot insert row");
            }
        } else {
            long id = Long.valueOf(idView.getText().toString());
            Entity entity = new Entity();
            entity.id = id;
            entity.value = valueView.getText().toString();
            if (dao.update(entity) < 1) {
                showToast("Cannot update row. Entry not found");
            }
        }
        refreshListView();
    }

    public void onMinusClicked(View v) {
        if (!TextUtils.isEmpty(idView.getText())) {
            long id = Long.valueOf(idView.getText().toString());
            if (dao.delete(id) < 1) {
                showToast("Not found!");
            }
        } else {
            showToast("Choose or enter id for deletion");
        }
        refreshListView();
    }

    private void refreshListView() {
        List<Entity> entities = dao.getAll();
        ArrayAdapter<Entity> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entities);
        setListAdapter(adapter);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private static class Entity {
        long id;
        String value;

        @Override
        public String toString() {
            return id + " " + value;
        }
    }

    private static class EntityDao {
        SQLiteDatabase database;
        MySqliteOpenHelper dbHelper;

        public EntityDao(Context context) {
            dbHelper = new MySqliteOpenHelper(context);
        }

        public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
        }

        public void close() {
            dbHelper.close();
        }

        public long create(Entity entity) {
            ContentValues cv = new ContentValues();
            cv.put("value", entity.value);
            return database.insert("entity", null, cv);
        }

        public int update(Entity entity) {
            ContentValues cv = new ContentValues();
            cv.put("value", entity.value);
            return database.update("entity", cv, BaseColumns._ID + "=?", new String[] {String.valueOf(entity.id)});
        }

        public int delete(long id) {
            return database.delete("entity", BaseColumns._ID + " = " + id, null);
        }

        public List<Entity> getAll() {
            List<Entity> all = new ArrayList<>();

            Cursor cursor = database.query("entity", new String[]{BaseColumns._ID, "value"}, null, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Entity entity = cursorToEntity(cursor);
                all.add(entity);
            }
            cursor.close();
            return all;
        }

        private Entity cursorToEntity(Cursor cursor) {
            Entity comment = new Entity();
            comment.id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            comment.value = cursor.getString(cursor.getColumnIndex("value"));
            return comment;
        }
    }

    private static class MySqliteOpenHelper extends SQLiteOpenHelper {

        public MySqliteOpenHelper(Context context) {
            super(context, "sqlite.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String ddlScript = "create table ENTITY (" +
                    "'_id' integer primary key autoincrement, " +
                    "'value' text not null" +
                    ");";

            db.execSQL(ddlScript);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            throw new UnsupportedOperationException("Not implemented! Write your schema migration code here");
        }
    }
}
