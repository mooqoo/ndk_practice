package com.example.wangalbert.prac_2;

import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * android
 * <p/>
 * Created by wangalbert on 2/3/16.
 * Copyright (c) 2016 MobiusBobs Inc. All rights reserved.
 */
public class VideoFrameAdapter  extends PagerAdapter {
  List<String> mlFramePaths;
  MainActivity mlAct;
  VideoFrameAdapter(MainActivity pAct, String pFrameFolderPath) {
    mlAct = pAct;
    File frameFolder = new File(pFrameFolderPath);
    File[] framePaths = frameFolder.listFiles();
    mlFramePaths = new ArrayList<String>();
    for (File aFile : framePaths) {
      if (aFile.getAbsolutePath().endsWith(".jpg")) {
        mlFramePaths.add(aFile.getAbsolutePath());
      }
    }
  }
  public Object instantiateItem(View collection, int position) {
    ImageView view = new ImageView(mlAct);
    view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
      LayoutParams.FILL_PARENT));
    view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    view.setImageDrawable(Drawable.createFromPath(mlFramePaths.get(position)));
    ((ViewPager) collection).addView(view);
    return view;
  }

  @Override
  public void destroyItem(View arg0, int arg1, Object arg2) {
    ((ViewPager) arg0).removeView((View) arg2);
  }

  @Override
  public int getCount() {
    if (null != mlFramePaths) {
      return mlFramePaths.size();
    } else {
      return 0;
    }
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }
}
