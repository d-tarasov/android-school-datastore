package ru.tinkoff.example.datastore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import ru.tinkoff.example.datastore.view.DrawingView;

/**
 * @author Dmitriy Tarasov
 */
public class ExternalStorageActivity extends Activity {

    private static final String FILE_NAME = "image.png";

    private File file;
    private DrawingView canvas;

    public static void start(Context context) {
        Intent intent = new Intent(context, ExternalStorageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);
        canvas = (DrawingView) findViewById(R.id.drawing_view);

        if (isExternalStorageWritable()) {
            file = getFile();
        } else {
            Toast.makeText(this, "External storage not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void onReadClicked(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        if (bitmap != null) {
            canvas.setBitmap(bitmap);
        }
    }

    public void onDeleteClicked(View view) {
        if (isExternalStorageWritable() && file.exists()) {
            if (file.delete()) {
                canvas.clear();
            } else {
                Toast.makeText(this, "Cannot delete file = " + file.getPath(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onWriteClicked(View view) throws Exception {
        Bitmap b = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        canvas.layout(canvas.getLeft(), canvas.getTop(), canvas.getRight(), canvas.getBottom());
        canvas.draw(c);

        FileOutputStream out = new FileOutputStream(file);
        b.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.close();
        b.recycle();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private File getFile() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FILE_NAME);
    }
}
