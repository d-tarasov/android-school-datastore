package ru.tinkoff.example.datastore;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * @author Dmitriy Tarasov
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onInternalStorageClicked(View view) {
        InternalStorageActivity.start(this);
    }

    public void onExternalStorageClicked(View view) {
        ExternalStorageActivity.start(this);
    }

    public void onSharedPrefsClicked(View view) {
        SharedPrefsActivity.start(this);
    }

    public void onSqliteClicked(View view) {
        SqliteActivity.start(this);
    }

    public void onOrmliteClicked(View view) {
        OrmliteActivity.start(this);
    }

    public void onSqlCipherClicked(View view) {
        SqlCipherActivity.start(this);
    }

    public void onContentProviderClicked(View view) {
        ContentProviderActivity.start(this);
    }
}
