package com.david.animatordemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 17/3/15.
 */

public class AnimatorDemoActivity extends Activity implements View.OnClickListener {

    private int[] ImageRes = {R.id.imageView_red, R.id.imageView1, R.id.imageView2,
            R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7};
    private List<ImageView> ImageViewList = new ArrayList<ImageView>();
    private RelativeLayout rl;
    private boolean flag = true;
    private ImageView im;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        rl = (RelativeLayout) findViewById(R.id.rlativeLayout);
        im = (ImageView) findViewById(R.id.imageView_red);
        //找到每张图片，添加事件监听，并将ImageView对象存入一个List中
        for (int i = 0; i < ImageRes.length; i++) {
            ImageView imageView = (ImageView) findViewById(ImageRes[i]);
            imageView.setOnClickListener(this);
            ImageViewList.add(imageView);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_red:
                if (flag) {
                    startAnimator();
                    flag = false;
                } else {
                    closeAnimator();
                    flag = true;
                }
                break;
            case R.id.imageView1:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.imageView2:
                break;
            case R.id.imageView3:
                break;
            case R.id.imageView4:
                break;
            case R.id.imageView5:
                break;
            case R.id.imageView6:
                break;
            case R.id.imageView7:
                break;


        }
    }

    private void startAnimator() {
        //确定启始的位置，Y轴是界面的高度
        int height = rl.getHeight();
        for (int i = 1; i < ImageRes.length; i++) {
            //分别对每个ImageView对象添加动画，在Y轴上向上移动 i*150，让每个图标之间相差150
            ObjectAnimator animator = ObjectAnimator.ofFloat(ImageViewList.get(i), "translationY", height, -i * 150);
            animator.setDuration(1000);
            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
                    //动画结束后可点击
                    im.setClickable(true);

                }

                @Override
                public void onAnimationStart(Animator animation) {
//                    super.onAnimationStart(animation);
                    //动画开始后不可点击
                    im.setClickable(false);
                }
            });
            //差值器，使用不同的差值器作出不同的效果
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setStartDelay(i * 300);
            animator.start();
        }
    }

    private void closeAnimator() {
        int height = rl.getHeight();
        for (int i = ImageRes.length - 1; i > 0; i--) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(ImageViewList.get(i), "translationY", -i * 150, height);
            animator.setDuration(500);
            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    im = (ImageView) findViewById(R.id.imageView_red);
                    im.setClickable(true);

                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    im = (ImageView) findViewById(R.id.imageView_red);
                    im.setClickable(false);
                }
            });
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setStartDelay(i * 300);
            animator.start();
        }
    }

}
