package ru.tinkoff.example.datastore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author Dmitriy Tarasov
 */
public class InternalStorageActivity extends Activity {

    private static final String FILE_NAME = "internal.txt";

    private EditText inputView;

    public static void start(Context context) {
        Intent intent = new Intent(context, InternalStorageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);
        inputView = (EditText) findViewById(R.id.input);
        readFile();
    }

    public void onReadClicked(View view) throws Exception {
        readFile();
    }

    public void onWriteClicked(View view) throws Exception {
        String input = inputView.getText().toString();
        FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
        fos.write(input.getBytes());
        fos.close();
    }

    private void readFile() {
        try {
            // TODO use Apache File Utils
            FileInputStream fis = openFileInput(FILE_NAME);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] b = new byte[1];
            int bytesRead;
            while ((bytesRead = fis.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            byte[] bytes = bos.toByteArray();

            fis.close();
            bos.close();

            inputView.setText(new String(bytes));
        } catch (Exception e) {
            Toast.makeText(this, "Cannot read data from file " + FILE_NAME, Toast.LENGTH_SHORT).show();
        }
    }
}
