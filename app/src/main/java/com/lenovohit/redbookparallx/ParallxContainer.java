package com.lenovohit.redbookparallx;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuzhijun on 2017/9/11.
 */
public class ParallxContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
    private List<ParallaxFragment> fragments;
    private ParallaxAdapter adapter;
    private float containerWidth;
    private ImageView iv_man;

    public ParallxContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public void setUp(int... childIds) {
        fragments = new ArrayList<>();
        for (int i = 0; i < childIds.length; i++) {
            ParallaxFragment f = new ParallaxFragment();
            Bundle args = new Bundle();
            //页面索引
            args.putInt("index", i);
            //Fragment中需要加载的布局文件id
            args.putInt("layoutId", childIds[i]);
            f.setArguments(args);
            fragments.add(f);
        }

        //实例化适配器
        SplashActivity activity = (SplashActivity)getContext();
        adapter = new ParallaxAdapter(activity.getSupportFragmentManager(), fragments);

        //实例化ViewPager
        ViewPager vp = new ViewPager(getContext());
        vp.setId(R.id.parallax_pager);
        vp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

        //绑定
        vp.setAdapter(adapter);
        addView(vp,0);

        vp.addOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.containerWidth = getWidth();
        //在翻页的过程中，不断根据视图的标签中对应的动画参数，改变视图的位置或者透明度
        //获取到进入的页面
        ParallaxFragment inFragment = null;
        try {
            inFragment = fragments.get(position - 1);
        } catch (Exception e) {}

        //获取到退出的页面
        ParallaxFragment outFragment = null;
        try {
            outFragment = fragments.get(position);
        } catch (Exception e) {}

        if (inFragment != null) {
            //获取Fragment上所有的视图，实现动画效果
            List<View> inViews = inFragment.getViews();
            if (inViews != null) {
                for (View view : inViews) {
                    //获取标签，从标签上获取所有的动画参数
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }
                    //translationY改变view的偏移位置，translationY=100，代表view在其原始位置向下移动100
                    //仔细观察进入的fragment中view从远处过来，不断向下移动，最终停在原始位置
                    ViewHelper.setTranslationY(view, (containerWidth - positionOffsetPixels) * tag.yIn);
                    ViewHelper.setTranslationX(view, (containerWidth - positionOffsetPixels) * tag.xIn);
                }
            }
        }

        if(outFragment != null){
            List<View> outViews = outFragment.getViews();
            if (outViews != null) {
                for (View view : outViews) {
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }
                    //仔细观察退出的fragment中view从原始位置开始向上移动，translationY应为负数
                    ViewHelper.setTranslationY(view, 0 - positionOffsetPixels * tag.yOut);
                    ViewHelper.setTranslationX(view, 0 - positionOffsetPixels * tag.xOut);
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == adapter.getCount() - 1) {
            iv_man.setVisibility(INVISIBLE);
        }else{
            iv_man.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        AnimationDrawable animation = (AnimationDrawable) iv_man.getBackground();
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                animation.start();
                break;

            case ViewPager.SCROLL_STATE_IDLE:
                animation.stop();
                break;

            default:
                break;
        }
    }

    public void setIv_man(ImageView iv_man) {
        this.iv_man = iv_man;
    }
}
