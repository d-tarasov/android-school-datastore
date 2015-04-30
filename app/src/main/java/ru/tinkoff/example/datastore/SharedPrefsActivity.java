package ru.tinkoff.example.datastore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitriy Tarasov
 */
public class SharedPrefsActivity extends Activity {

    private static final String SHARED_PREFS = "prefs.xml";

    private static final String BOOL = "bool";
    private static final String FLOAT = "float";
    private static final String INT = "int";
    private static final String LONG = "long";
    private static final String STRING = "string";
    private static final String STRING_SET = "string_set";

    private CheckBox boolValue;
    private EditText floatValue;
    private EditText intValue;
    private EditText longValue;
    private EditText stringValue;
    private EditText stringSetValue;

    private SharedPreferences prefs;

    public static void start(Context context) {
        Intent intent = new Intent(context, SharedPrefsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_prefs);

        boolValue = (CheckBox) findViewById(R.id.bool_value);
        floatValue = (EditText) findViewById(R.id.float_value);
        intValue = (EditText) findViewById(R.id.int_value);
        longValue = (EditText) findViewById(R.id.long_value);
        stringValue = (EditText) findViewById(R.id.string_value);
        stringSetValue = (EditText) findViewById(R.id.string_set_value);

        prefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void onReadClicked(View view) {
        boolean b = prefs.getBoolean(BOOL, true);
        float f = prefs.getFloat(FLOAT, 0.0f);
        int i = prefs.getInt(INT, 0);
        long l = prefs.getLong(LONG, 0L);
        String s = prefs.getString(STRING, "Default String");
        Set<String> ss = prefs.getStringSet(STRING_SET, new LinkedHashSet<>(Arrays.asList("Default", "string", "set")));

        boolValue.setChecked(b);
        floatValue.setText(String.valueOf(f));
        intValue.setText(String.valueOf(i));
        longValue.setText(String.valueOf(l));
        stringValue.setText(s);
        stringSetValue.setText(ss.toString().replaceAll("[\\[\\]]", ""));
    }

    public void onWriteClicked(View view) {
        boolean b = boolValue.isChecked();
        float f = Float.valueOf(floatValue.getText().toString());
        int i = Integer.valueOf(intValue.getText().toString());
        long l = Long.valueOf(longValue.getText().toString());
        String s = stringValue.getText().toString();
        Set<String> ss = new LinkedHashSet<>(Arrays.asList(stringSetValue.getText().toString().split(",")));

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(BOOL, b);
        editor.putFloat(FLOAT, f);
        editor.putInt(INT, i);
        editor.putLong(LONG, l);
        editor.putString(STRING, s);
        editor.putStringSet(STRING_SET, ss);
        editor.commit();
    }
}
