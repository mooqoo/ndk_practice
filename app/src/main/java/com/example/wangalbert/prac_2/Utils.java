package com.example.wangalbert.prac_2;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * android
 * <p/>
 * Created by wangalbert on 2/3/16.
 * Copyright (c) 2016 MobiusBobs Inc. All rights reserved.
 */
public class Utils {
  public static void copyAssets(Context pContext, String pAssetFilePath, String pDestDirPath) {
    AssetManager assetManager = pContext.getResources().getAssets();
    InputStream in = null;
    OutputStream out = null;
    try {
      Log.d("Utils", "pAssetFilePath = " + pAssetFilePath);
      in = assetManager.open(pAssetFilePath);
      File outFile = new File(pDestDirPath, pAssetFilePath);
      out = new FileOutputStream(outFile);
      copyFile(in, out);
      in.close();
      in = null;
      out.flush();
      out.close();
      out = null;
    } catch(IOException e) {
      Log.e("tag", "Failed to copy asset file: " + pAssetFilePath, e);
    }
  }
  private static void copyFile(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[1024*16];
    int read;
    while((read = in.read(buffer)) != -1){
      out.write(buffer, 0, read);
    }
  }
}