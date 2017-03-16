package com.david.animatordemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    public void btnMove(View view) {
//        旧版动画实现方法,只是不断调onDraw方法重绘，事件监听等还是在原地
//        TranslateAnimation animation = new TranslateAnimation(0, 200, 0, 0);
//        animation.setDuration(1000);
//        animation.setFillAfter(true);
//        mImageView.startAnimation(animation);

        //即更改了图像的位置，也更改了事件监听的位置
        //几个操作会同时进行
//        ObjectAnimator.ofFloat(mImageView, "translationY", 0F, 200F).setDuration(1000).start();
//        ObjectAnimator.ofFloat(mImageView, "rotation", 0F, 360F).setDuration(1000).start();
//        ObjectAnimator.ofFloat(mImageView, "translationX", 0F, 200F).setDuration(1000).start();

        //PropertyValuesHolder中Google做了一定优化
//        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationY", 0F, 200F);
//        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("rotation", 0F, 360F);
//        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("translationX", 0F, 200F);
//        ObjectAnimator.ofPropertyValuesHolder(mImageView, p1, p2, p3).setDuration(1000).start();


        //强大的动画顺序控制能力
        ObjectAnimator o1 = ObjectAnimator.ofFloat(mImageView, "translationY", 0F, 200F);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(mImageView, "rotation", 0F, 360F);
        ObjectAnimator o3 = ObjectAnimator.ofFloat(mImageView, "translationX", 0F, 200F);
        AnimatorSet set = new AnimatorSet();
        set.play(o1).with(o3);
        set.play(o2).after(o1);
        //可以根据需求选择回调函数
        o2.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Toast.makeText(MainActivity.this, "onAnimationEnd", Toast.LENGTH_SHORT).show();

            }
        });
        //添加动画监听
//        o2.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                Toast.makeText(MainActivity.this, "onAnimationEnd", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//                Toast.makeText(MainActivity.this, "onAnimationRepeat", Toast.LENGTH_SHORT).show();
//            }
//        });
//        set.playTogether(o1, o2, o3);
        set.setDuration(1000);
        set.start();


    }
}
