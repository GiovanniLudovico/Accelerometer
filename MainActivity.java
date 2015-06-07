package com.example.giovanni.accelerometer;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import java.text.SimpleDateFormat;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
public class MainActivity extends ActionBarActivity {
    private

    static final String LOG_TAG = "AudioRecordTest";
    static String mFileName = null;
    Timer timer;
    TimerTask timerTask;
    File audio_folder;
    static int i = 0;

    MediaRecorder mRecorder = null;
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    //instanzia una cartella nel telefono (/audio_recordings) dove poi salverà i file

    private File create_audio_folder()
    {
        if(isExternalStorageWritable()) {
            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/audio_recordings");
            boolean success = false;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            if (!success) {
                Log.e(LOG_TAG, "Folder not created.");
            } else {
                Log.e(LOG_TAG, "Folder created!");
            }
            return folder;
        }
        return null;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audio_folder=create_audio_folder();


    }
    //quando viene cliccato il bottone rec viene fatto partire il timer che fa scattere le registrazioni
    public void audio_press(View view) {
        startTimer();


    }
    //quando viene cliccato il bottone stop viene fatto fermare il timer che fa scattere le registrazioni
    public void stop_press(View view) {
        // Is the button now checked?

        stopRecording();


    }

    private void stopRecording() {
        //stop the timer, if it's not already null

        if (timer != null) {

            timer.cancel();

            timer = null;

        }

        //mRecorder.stop();
        //mRecorder.release();
        //mRecorder = null;
    }

    public void play_press(View view) {

    }

    private void startRecording() {
        //instanzia l'oggetto utile a registrare
        mRecorder = new MediaRecorder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();

        mFileName = audio_folder.getAbsolutePath();
        mFileName += "/"+ formatter.format(now).toString()+ ".3gp";
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setMaxDuration(2000);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    mRecorder.stop();
                    mRecorder.reset();
                }
            }
        });


        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    public MainActivity() {


    }
   public void initializeTimerTask() {
        timerTask = new TimerTask()
        {

            public void run() {
                startRecording();
            }

        };
    }


            public void startTimer() {

        //set a new Timer

        timer = new Timer();

        //initialize the TimerTask's job

        initializeTimerTask();

        //schedule the timer, after the first 50ms the TimerTask will run every 50000ms

        timer.schedule(timerTask, 500, 5000); //

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

