package immanuel.co.camerathumb;

import android.Manifest;
import android.animation.Animator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Immanuel on 3/13/2018.
 */

public class CameraIntent extends AppCompatActivity {
    private CameraIntent mActivity = this;
    private CameraView mPreview;
    private RelativeLayout mLayout;
    private ImageView mImg;
    private ImageView sImg;
    private String DEBUG_TAG = "BDD";
    //private ArrayList<Bitmap> imgs = new ArrayList<Bitmap>();
    int MIN_DISTANCE = 50;
    int OFF_PATH = 100;
    int VELOCITY_THRESHOLD = 75;
    int imageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(GetLayout());
        setContentView(R.layout.activity_camera);
        //mPreview = new CameraPreview(this, 0, CameraPreview.LayoutMode.FitToParent);
        //mLayout = (RelativeLayout) findViewById(R.id.main_layout);
        //RelativeLayout.LayoutParams previewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //mLayout.addView(mPreview, 0, previewLayoutParams);

        if (!checkCameraPermission()){
            finish();
        }
        getSupportActionBar().hide();
        GlobalConfig.imgs.clear();
        mLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mImg = (ImageView) findViewById(R.id.camera_click);
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImg.setVisibility(View.INVISIBLE);
                mPreview.takePic();
            }
        });

        sImg = (ImageView) findViewById(R.id.save_click );
        sImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra("Key", "101");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private FrameLayout GetLayout() {
        FrameLayout fl = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        fl.setLayoutParams(params);

        RelativeLayout.LayoutParams relpara = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout rl1 = new RelativeLayout(this);
        rl1.setId(R.id.main_layout);
        rl1.setLayoutParams(relpara);
        fl.addView(rl1, relpara);

        ImageView iv1 = new ImageView(this);
        iv1.setId(R.id.camera_click);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(R.dimen.actbtn_width, R.dimen.actbtn_height);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,  RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,  RelativeLayout.TRUE);
        layoutParams.setMargins(5, 5, 0, 0);
        //iv1.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.cam_, getApplicationContext().getTheme()));
        } else {
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.cam_));
        }
        rl1.addView(iv1, layoutParams);

        ImageView iv2 = new ImageView(this);
        iv2.setId(R.id.save_click);
        RelativeLayout.LayoutParams iv2lout = new RelativeLayout.LayoutParams(R.dimen.actbtn_width, R.dimen.actbtn_height);
        iv2lout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        iv2lout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        iv2lout.setMargins(5, 5, 0, 0);
        iv2.setLayoutParams(iv2lout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.sav_doc, getApplicationContext().getTheme()));
        } else {
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.sav_doc));
        }
        rl1.addView(iv2, iv2lout);

        HorizontalScrollView hsv = new HorizontalScrollView(this);
        HorizontalScrollView.LayoutParams hl = new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.MATCH_PARENT,
                HorizontalScrollView.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams llout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, R.dimen.img_height);
        LinearLayout l1 = new LinearLayout(this);
        l1.setGravity(Gravity.BOTTOM);
        l1.setOrientation(LinearLayout.HORIZONTAL);
        l1.setBackgroundColor(Color.BLUE);
        l1.setLayoutParams(llout);

        hsv.addView(l1, llout);
        fl.addView(hsv, hl);

        return  fl;
    }

    private LinearLayout getImglout(Bitmap bm) {
        final LinearLayout ll = new LinearLayout(mActivity);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(parms);

        RelativeLayout rl = new RelativeLayout(mActivity);
        RelativeLayout.LayoutParams rlpar = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlpar.setMargins(30,0,0,0);
        //rl.setPadding(30,0,0,0);
        rl.setLayoutParams(rlpar);

        final ImageView iv = new ImageView(mActivity);
        iv.setTag("mytagimg");
        int wdt = mActivity.getResources().getDimensionPixelSize(R.dimen.img_width);
        int hgt = mActivity.getResources().getDimensionPixelSize(R.dimen.img_height);
        RelativeLayout.LayoutParams ivpar = new RelativeLayout.LayoutParams(wdt,hgt);
        ivpar.setMargins(30, 10, 15, 0);
        iv.setLayoutParams(ivpar);
        iv.setImageBitmap(bm);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView ivprev = new ImageView(mActivity);
                FrameLayout.LayoutParams loprev = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                ivprev.setLayoutParams(loprev);
                BitmapDrawable drawable = (BitmapDrawable) ((ImageView)v).getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ivprev.setImageBitmap(bitmap);
                final FrameLayout llp = (FrameLayout)mActivity.findViewById(R.id.imgPrv);
                llp.setClickable(true);
                llp.removeAllViews();
                llp.setBackgroundColor(Color.LTGRAY);

                final ImageView prevbck = new ImageView(mActivity);
                iv.setTag("mytggg");
                int wdt = mActivity.getResources().getDimensionPixelSize(R.dimen.bckbtn_width);
                int hgt = mActivity.getResources().getDimensionPixelSize(R.dimen.bckbtn_height);
                FrameLayout.LayoutParams lbck = new FrameLayout.LayoutParams(wdt,hgt);
                lbck.setMargins(30, 10, 15, 0);
                prevbck.setLayoutParams(lbck);
                prevbck.setImageResource(R.drawable.back_button);

                prevbck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llp.setClickable(false);
                        llp.setBackgroundColor(Color.TRANSPARENT);
                        llp.removeAllViews();
                    }
                });

                llp.addView(prevbck);
                llp.addView(ivprev);
            }
        });

        ImageView ivdel = new ImageView(mActivity);
        int wdt1 = mActivity.getResources().getDimensionPixelSize(R.dimen.actbtn_width);
        int hgt1 = mActivity.getResources().getDimensionPixelSize(R.dimen.actbtn_height);
        RelativeLayout.LayoutParams rlpp = new RelativeLayout.LayoutParams(wdt1,hgt1);
        rlpp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlpp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlpp.setMargins(0, 0, 0, 0);
        ivdel.setLayoutParams(rlpp);
        ivdel.setImageResource(R.drawable.delete_);

        ivdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
//                Bitmap bitmap = drawable.getBitmap();
//                GlobalConfig.imgs.remove(bitmap);
//                LinearLayout ll1 = (LinearLayout) mActivity.findViewById(R.id.in_lay1);
//                Log.d(DEBUG_TAG, String.valueOf(GlobalConfig.imgs.size()));
//                ll1.removeView(ll);

                ll.animate()
                    .translationY(-ll.getHeight())
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //if (ll.findViewWithTag("mytagimg") != null){
                                //BitmapDrawable drawable = (BitmapDrawable) ((ImageView) ll.findViewWithTag("mytagimg")).getDrawable();
                            BitmapDrawable drawable = (BitmapDrawable)iv.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            GlobalConfig.imgs.remove(bitmap);
                            LinearLayout ll2 = (LinearLayout) mActivity.findViewById(R.id.in_lay1);
                            Log.d(DEBUG_TAG, String.valueOf(GlobalConfig.imgs.size()));
                            ll2.removeView(ll);
                            //}

                            FrameLayout llp = (FrameLayout)mActivity.findViewById(R.id.imgPrv);
                            llp.setClickable(false);
                            llp.setBackgroundColor(Color.TRANSPARENT);
                            llp.removeAllViews();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            }
        });

        rl.addView(iv);
        rl.addView(ivdel);

        ll.addView(rl);


        return ll;
    }

    private void addImage(Bitmap bm){
        GlobalConfig.imgs.add(bm);
//        ImageView iv = new ImageView(mActivity);
//        int wdt = mActivity.getResources().getDimensionPixelSize(R.dimen.img_width);
//        int hgt = mActivity.getResources().getDimensionPixelSize(R.dimen.img_height);
//        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(wdt,hgt);
//        parms.setMargins(0, 0, 15, 0);
//        iv.setLayoutParams(parms);
//        iv.setImageBitmap(bm);

        LinearLayout ll1 = getImglout(bm);
        LinearLayout ll = (LinearLayout) mActivity.findViewById(R.id.in_lay1);
        ll.addView(ll1);

        CustomGestureDetector customGestureDetector = new CustomGestureDetector(ll1);
        final GestureDetector mGestureDetector = new GestureDetector(this, customGestureDetector);
        ll1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
        mImg.setVisibility(View.VISIBLE);
    }

    class CustomGestureDetector implements GestureDetector.OnGestureListener,
            GestureDetector.OnDoubleTapListener {
        LinearLayout iv;
        public CustomGestureDetector(LinearLayout iv1){
            this.iv = iv1;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //Log.d(DEBUG_TAG,"onSingleTapConfirmed");
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //Log.d(DEBUG_TAG,"onDoubleTap");
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            //Log.d(DEBUG_TAG, "onDoubleTapEvent");
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            //Log.d(DEBUG_TAG,"onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            //Log.d(DEBUG_TAG,"onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //Log.d(DEBUG_TAG,"onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //Log.d(DEBUG_TAG,"onScroll");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //Log.d(DEBUG_TAG,"onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > OFF_PATH || Math.abs(e1.getX() - e2.getX()) > OFF_PATH)
                return false;
            Log.d(DEBUG_TAG,"onFling - " + (e2.getY() - e1.getY()) + " - " + Math.abs(velocityY));
            if (e1.getX() - e2.getX() > MIN_DISTANCE && Math.abs(velocityX) > VELOCITY_THRESHOLD) {
                // right to left swipe
                Log.d(DEBUG_TAG, "right to left swipe");
            } else if (e2.getX() - e1.getX() > MIN_DISTANCE && Math.abs(velocityX) > VELOCITY_THRESHOLD) {
                // left to right swipe

            } else if (e1.getY() - e2.getY() > MIN_DISTANCE && Math.abs(velocityY) > VELOCITY_THRESHOLD) {
                Log.d(DEBUG_TAG, "bottom to top");
                iv.animate()
                        .translationY(-iv.getHeight())
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                BitmapDrawable drawable = (BitmapDrawable) ((ImageView) iv.findViewWithTag("mytagimg")).getDrawable();
                                Bitmap bitmap = drawable.getBitmap();
                                GlobalConfig.imgs.remove(bitmap);
                                LinearLayout ll = (LinearLayout) mActivity.findViewById(R.id.in_lay1);
                                Log.d(DEBUG_TAG, String.valueOf(GlobalConfig.imgs.size()));
                                ll.removeView(iv);

                                FrameLayout llp = (FrameLayout)mActivity.findViewById(R.id.imgPrv);
                                llp.setClickable(false);
                                llp.setBackgroundColor(Color.TRANSPARENT);
                                llp.removeAllViews();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                Log.d(DEBUG_TAG, String.valueOf(GlobalConfig.imgs.size()));
                // bottom to top

            } else if (e2.getY() - e1.getY() > MIN_DISTANCE && Math.abs(velocityY) > VELOCITY_THRESHOLD) {
                // top to bottom
                Log.d(DEBUG_TAG, "Top TO Down Swipe");
                iv.animate()
                        .translationY(iv.getHeight())
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                BitmapDrawable drawable = (BitmapDrawable) ((ImageView) iv.findViewWithTag("mytagimg")).getDrawable();
                                Bitmap bitmap = drawable.getBitmap();
                                GlobalConfig.imgs.remove(bitmap);
                                LinearLayout ll = (LinearLayout) mActivity.findViewById(R.id.in_lay1);
                                Log.d(DEBUG_TAG, String.valueOf(GlobalConfig.imgs.size()));
                                ll.removeView(iv);

                                final FrameLayout llp = (FrameLayout)mActivity.findViewById(R.id.imgPrv);
                                llp.setClickable(false);
                                llp.setBackgroundColor(Color.TRANSPARENT);
                                llp.removeAllViews();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
            }
            return true;
        }
    }

    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            //ask for authorisation
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, 50);
        else
            //start your camera
            return true;
        return  false;
    }

    protected void onResume() {
        super.onResume();
        // Set the second argument by your choice.
        // Usually, 0 for back-facing camera, 1 for front-facing camera.
        // If the OS is pre-gingerbreak, this does not have any effect.
        mPreview = new CameraView(this, 0, CameraView.LayoutMode.FitToParent);
        mPreview.setCamaraViewListener(new CameraView.CamaraViewListener() {
            @Override
            public void onImageCaptures(Bitmap img) {
                addImage(img);
            }
        });
        RelativeLayout.LayoutParams previewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // Un-comment below lines to specify the size.
        //previewLayoutParams.height = 500;
        //previewLayoutParams.width = 500;

        // Un-comment below line to specify the position.
        //mPreview.setCenterPosition(270, 130);

        mLayout.addView(mPreview, 0, previewLayoutParams);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        mLayout.removeView(mPreview); // This is necessary.
        mPreview = null;
    }
}
