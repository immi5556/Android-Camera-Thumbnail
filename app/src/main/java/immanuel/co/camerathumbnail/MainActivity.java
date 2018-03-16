package immanuel.co.camerathumbnail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import immanuel.co.camerathumb.CameraIntent;
import immanuel.co.camerathumb.GlobalConfig;

public class MainActivity extends AppCompatActivity {
Button b;
    MainActivity ma = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = (Button)findViewById(R.id.somme);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ma, CameraIntent.class);
                startActivityForResult (i, 110);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("wewe", String.valueOf(resultCode));
        if (resultCode == Activity.RESULT_OK && requestCode == 110) {
            String requiredValue = data.getStringExtra("Key");
            Log.d("wewe1", requiredValue);
            LinearLayout ll = (LinearLayout)findViewById(R.id.errt);
            for (Bitmap bm: GlobalConfig.imgs) {
                ImageView iv1 = new ImageView(this);
                iv1.setId(immanuel.co.camerathumb.R.id.camera_click);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 30);
                iv1.setLayoutParams(layoutParams);
                iv1.setImageBitmap(bm);
                ll.addView(iv1);
            }
        }
    }
}
