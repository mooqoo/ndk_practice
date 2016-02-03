package com.example.wangalbert.prac_2;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
  private static final String FRAME_DUMP_FOLDER_PATH = Environment.getExternalStorageDirectory()
    + File.separator + "android-ffmpeg-tutorial01";
  private EditText mEditNumOfFrames;

  TextView tv_msg;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clickAction();
      }
    });

    tv_msg = (TextView) findViewById(R.id.tv_msg);
    tv_msg.setText(getMsgFromJni());

    setupTutorial();
  }

  private void setupTutorial() {
    //create directory for the tutorial
    File dumpFolder = new File(FRAME_DUMP_FOLDER_PATH);
    if (!dumpFolder.exists()) {
      dumpFolder.mkdirs();
    }

    //copy input video file from assets folder to directory
    Utils.copyAssets(this, "1.mp4", FRAME_DUMP_FOLDER_PATH);

  }

  private void clickAction() {
    int numOfFrames = 5;

    Log.d("click action", "starting async test!");
    // process frame using async
    DumpFrameTask task = new DumpFrameTask(MainActivity.this, numOfFrames);
    task.execute();
  }


  private static class DumpFrameTask extends AsyncTask<Void, Integer, Void> {
    int mlNumOfFrames;
    ProgressDialog mlDialog;
    MainActivity mlOuterAct;
    DumpFrameTask(MainActivity pContext, int pNumOfFrames) {
      mlNumOfFrames = pNumOfFrames;
      mlOuterAct = pContext;
    }
    @Override
    protected void onPreExecute() {
      mlDialog = ProgressDialog.show(mlOuterAct, "Dump Frames", "Processing..Wait..", false);
    }
    @Override
    protected Void doInBackground(Void... params) {
      mlOuterAct.naMain(mlOuterAct, FRAME_DUMP_FOLDER_PATH + File.separator + "1.mp4", mlNumOfFrames);
      return null;
    }
    @Override
    protected void onPostExecute(Void param) {
      if (null != mlDialog && mlDialog.isShowing()) {
        mlDialog.dismiss();
      }
    }
  }

  private void saveFrameToPath(Bitmap bitmap, String pPath) {
    int BUFFER_SIZE = 1024 * 8;
    try {
      File file = new File(pPath);
      file.createNewFile();
      FileOutputStream fos = new FileOutputStream(file);
      final BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
      bos.flush();
      bos.close();
      fos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static {
    System.loadLibrary("ffmpeg");
    System.loadLibrary("my-module");
  }

  public native String getMsgFromJni();

  private native int naMain(MainActivity pObject, String pVideoFileName, int pNumOfFrames);

}
