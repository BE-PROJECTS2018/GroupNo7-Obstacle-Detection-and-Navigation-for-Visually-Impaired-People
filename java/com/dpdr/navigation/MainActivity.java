package com.dpdr.navigation;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.gc.materialdesign.views.ButtonRectangle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ButtonRectangle test,train;
    File file;
    String filename;
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    IntentFilter rec;
    BroadcastReceiver myrec;
    int temp = 0;
    static TextToSpeech tts;
    Vibrator v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    Log.e("Say it","Setting Language in Main Activity after onResume();");
                    tts.speak("main screen", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        /*rec = new IntentFilter(Intent.ACTION_USER_PRESENT);
        myrec = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent intent1 = new Intent(context,MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        };
        registerReceiver(myrec,rec);*/
        filename = "Network.ser";
        //test = (ButtonRectangle)findViewById(R.id.test);
        //train = (ButtonRectangle)findViewById(R.id.train);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
       /* if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        }
        //Toast.makeText(getApplicationContext(),Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(),Toast.LENGTH_LONG).show();
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTrain = new Intent(MainActivity.this,TrainDash.class);
                toTrain.putExtra("Test/Train","TEST");
                startActivity(toTrain);
            }
        });
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTrain = new Intent(MainActivity.this,TrainDash.class);
                toTrain.putExtra("Test/Train","TRAIN");
                startActivity(toTrain);
            }
        });*/
    }
    boolean scheduled = false;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.e("Press.TAG","ACTION_DOWN");
                new CountDownTimer(1000, 500) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    if (scheduled==false){
                        scheduled = true;
                        Log.e("Press.TAG","3 seconds done. Scheduled = True");
                        Intent toTrain = new Intent(MainActivity.this,TrainDash.class);
                        toTrain.putExtra("Test/Train","TRAIN");
                        Log.e("Press.TAG","Just about to start Train Activity after 3 seconds.");
                        startActivity(toTrain);
                    }
                }
            }.start();
                break;
            case MotionEvent.ACTION_UP:
                if(scheduled == false){
                    scheduled = true;
                    Log.e("Press.TAG","Finger lifted. Scheduled = True");
                    Intent toTrain = new Intent(MainActivity.this,TrainDash.class);
                    toTrain.putExtra("Test/Train","TEST");
                    Log.e("Press.TAG","Just about to start Test Activity because finger lifted.");
                    startActivity(toTrain);
                }
                break;
        }
        return true;
    }
    private void launchApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent toTrain = new Intent(MainActivity.this,TrainDash.class);
            toTrain.putExtra("Test/Train","TRAIN");
            startActivity(toTrain);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onPause() {
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        // Vibrate for 250 milliseconds
        Log.e("VBR","Vibrating to go out of MainActivity");
        super.onPause();
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "BlindWalker"
        );
        file = new File(
                mediaStorageDir.getPath() + File.separator + filename
        );
        try {
            //Log.e("File Dest",file.getAbsolutePath());
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeInt(temp);
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        v.vibrate(200);
        //NeuralNet.getInstance().saveData(getApplicationContext());
    }
    @Override
    protected void onResume() {
        super.onResume();
        scheduled = false;
        Log.e("VBR","Vibrating in MainActivity");
        v.vibrate(200);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    Log.e("Say it","Setting Language in Main Activity after onResume();");
                        tts.speak("main screen", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        //NeuralNet.setContext(getApplicationContext());
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "BlindWalker"
        );
        file = new File(
                mediaStorageDir.getPath() + File.separator + filename
        );
        try {
            /*if(){
            }else{*/
                file.createNewFile();/*
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                temp = (int)ois.readInt();
                temp++;
                ois.close();*/
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public class ScreenReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context,MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            }
        }*/
    }
