# AnimatorDemo

<br/>　　在Android动画实现中有两个框架：Animation 和 Animator，Animator是在Android4.0中添加的一个动画框架。

<br/>　　Animation框架定义了透明度，旋转，缩放和位移几种常见的动画，而且控制的是一个整个View动画。通过不断调用onDraw方法去重绘界面，会很耗费GPU资源。
<br/>　　在Animator框架中使用最多的是AnimatorSet和ObjectAnimator配合，使用ObjectAnimator进行更精细化控制，只控制一个对象的一个属性值，多个ObjectAnimator组合到AnimatorSet形成一个动画。

<br/>　　使用Animation有一个很大的问题相信很多人也遇见过，比如写一个ImageView，给它添加一个点击事件，弹出Toast，然后对ImageView做一个很简单的动画效果。动画效果很完美，然后点击ImageView发现没有弹出Toast，点击开始位置，Toast弹出，这就很尴尬了。Animation只是重绘了动画，改变了ImageView的显示位置，但对于事件监听的位置却没有改变，所以Animation不适合做具有交互功能的动画效果。
<br/>　　所以还是学习Animator动画，紧跟Google爸爸的潮流。

# Animator
## ObjectAnimator使用
<br/>　　ObjectAnimator类是在Animator动画中最常使用到的一个类。

```
       //Animation旧版动画实现方法,只是不断调onDraw方法重绘，事件监听等还是在原地
       TranslateAnimation animation = new TranslateAnimation(0, 200, 0, 0);
       animation.setDuration(1000);
       animation.setFillAfter(true);
       mImageView.startAnimation(animation);
```

```
        //Animator既更改了图像的位置，又更改了事件监听的位置
        ObjectAnimator.ofFloat(mImageView, "translationX", 0F, 200F).setDuration(1000).start();
``` 
<br/>　　上面两端代码实现的是同一个动画效果，将ImageView向右移动200，Animator的代码感觉会更简洁一些。ofFloat()中的四个参数分别为要操作的对象、要改变的属性、起始位置、结束位置。

常见属性

```
   alpha 透明度

   rotation z轴旋转

   rotationX x轴旋转

   rotationY y轴旋转

   translationX x水平偏移

   translationY y水平偏移

   ScaleX x轴缩放

   ScaleY y轴缩放
```

## 多个动画

```
        //几个操作会同时进行
        ObjectAnimator.ofFloat(mImageView, "translationY", 0F, 200F).setDuration(1000).start();
        ObjectAnimator.ofFloat(mImageView, "rotation", 0F, 360F).setDuration(1000).start();
        ObjectAnimator.ofFloat(mImageView, "translationX", 0F, 200F).setDuration(1000).start();
```

<br/>　　有多个动画时，动画效果会是同时执行。当然这种情况Google也考虑到了，提供了另一种方式PropertyValuesHolder来完成上面的效果，并进行了一些优化

```
        //PropertyValuesHolder中Google做了一定优化
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationY", 0F, 200F);
        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("rotation", 0F, 360F);
        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("translationX", 0F, 200F);
        ObjectAnimator.ofPropertyValuesHolder(mImageView, p1, p2, p3).setDuration(1000).start();
```

## 动画顺序控制
<br/>　　如果我们想将多个动画融合在一起，做成一个动画集合，一个执行完后另一个开始，可以使用AnimatorSet。

```
        //强大的动画顺序控制能力
        ObjectAnimator o1 = ObjectAnimator.ofFloat(mImageView, "translationY", 0F, 200F);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(mImageView, "rotation", 0F, 360F);
        ObjectAnimator o3 = ObjectAnimator.ofFloat(mImageView, "translationX", 0F, 200F);
        AnimatorSet set = new AnimatorSet();
        set.play(o1).with(o3);
        set.play(o2).after(o1);
        // set.playTogether(o1, o2, o3);  //同时
        set.setDuration(1000);
        set.start();
```
<br/>　　这样的一段代码使用AnimatorSet使用with让o1动画和o3动画同时执行，使用after让o2动画在o1、o3执行结束后再执行。AnimatorSet拥有强大的动画顺序控制能力。

## 动画监听
<br/>　　我们可以为动画添加监听，在动画的各个时期做对应的操作。

```
        //添加动画监听
        o2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Toast.makeText(MainActivity.this, "Animation Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Toast.makeText(MainActivity.this, "Animation End", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
```

<br/>　　一般我们会使用其中的onAnimationStart和onAnimationEnd来判断动画是否开始、是否结束，而剩下的的两种就很少使用了。不想有这么多方法出现，我们可以使用另外一种添加监听的方法。

![](https://www.davidprogram.com/wp-content/uploads/2017/03/Animator01.png)

```
        //可以根据需求选择回调函数
        o2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Toast.makeText(MainActivity.this, "onAnimationEnd", Toast.LENGTH_SHORT).show();
            }
        });
```


<br/>　　我只想要动画结束的监听就可以像上面一样,更简洁方便。

# 卫星菜单Demo

<br/>　　效果图
<br/>　　![](https://www.davidprogram.com/wp-content/uploads/2017/03/mydemogif.gif)

<br/>　　如果上面的动态效果图看不了

<br/>　　![](https://www.davidprogram.com/wp-content/uploads/2017/03/mydemo002.png)

<br/>　　每一个弹出的菜单都是一张图片，我们只需要将几张图片重叠放在一起，然后给除了最上面图片的所有图片添加一个向上的动画，移动的长度不同就可以达到弹出菜单的效果了。

```
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
```

```
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
                    //动画结束后可点击
                    im.setClickable(true);
                }

                @Override
                public void onAnimationStart(Animator animation) {
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
```

<br/>　　结束的动画和startAnimator()基本相同，方向相反就可以了。[代码地址](https://github.com/David1840/AnimatorDemo)
