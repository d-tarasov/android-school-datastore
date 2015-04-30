package ru.tinkoff.example.datastore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author Dmitriy Tarasov
 */
public class ContentProviderActivity extends Activity {

    public static void start(Context context) {
        Intent intent = new Intent(context, ContentProviderActivity.class);
        context.startActivity(intent);
    }
}
