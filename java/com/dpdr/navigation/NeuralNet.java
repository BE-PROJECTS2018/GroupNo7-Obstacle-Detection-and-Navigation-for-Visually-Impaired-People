package com.dpdr.navigation;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by 209de on 11/28/2015.
 */
public class NeuralNet {
    boolean instantiated = false;
    FileOutputStream fos = null;
    public static boolean beingAccessed = false ;
    ObjectOutputStream oos = null;
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    File file;
    Context context;
    KNN network;
    String filename = "knnData.ser";
    private static NeuralNet instance = new NeuralNet();
    private NeuralNet() {
        Log.i("NeuralNet.TAG", "I'm in NeuralNet()");
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "PathFinder"
        );
        file = new File(
                mediaStorageDir.getPath() + File.separator + filename
        );
        Log.i("NeuralNet.TAG", "The path is set to " + file.getAbsolutePath());
        if(file.exists()) {
            try {
                fis = new FileInputStream(file);
            Log.i("NeuralNet.TAG", "fis = "+fis.toString());
            ois = new ObjectInputStream(fis);
            Log.i("NeuralNet.TAG", "fis = "+ois.toString());
            network = (KNN) ois.readObject();
            Log.i("NeuralNet.TAG", "network is " + String.valueOf(network!=null));
            ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            network = new KNN();
        }
       /* try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        network = new KNN();*/
        /*try {
            if (file.createNewFile()) {
                network = new KNN();
                Log.i("NeuralNet.TAG", "network initialized as KNN()");
                //Toast.makeText(this.context, "network initialized as KNN()", Toast.LENGTH_SHORT).show();
            } else {
                fis = new FileInputStream(file);
                Log.i("NeuralNet.TAG", "fis = "+fis.toString());
                ois = new ObjectInputStream(fis);
                Log.i("NeuralNet.TAG", "fis = "+ois.toString());
                network = (KNN) ois.readObject();
                Log.i("NeuralNet.TAG", "network is " + String.valueOf(network!=null));
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }
    public static NeuralNet getInstance(){
        Log.i("NeuralNet.TAG", "I'm in getInstance()");
        return instance;
    }

    public static void setContext(Context context){
        Log.i("NeuralNet.TAG", "I'm in setContext()");
        instance.context = context;
    }

    public void finalise(){
        instance.instantiated = true;
    }
    public void train(double inp[],int id){
        Log.i("NeuralNet.TAG", "I'm in train()");
        //Toast.makeText(instance.context,"Training started ",Toast.LENGTH_SHORT).show();
        instance.network.Train(inp,id);
        //Toast.makeText(instance.context,"Training finished",Toast.LENGTH_SHORT).show();

    }

    public static int query(double inp[]){
        Log.i("NeuralNet.TAG", "I'm in query()");
        Log.i("NeuralNet.TAG","instance is " + String.valueOf(instance!=null));
        return instance.network.Query(inp);
    }

    public void saveData(Context con){
        new SaveAsync().execute();
        Toast.makeText(con,"Training Saved Successfully ",Toast.LENGTH_SHORT).show();
    }

    public static class SaveAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... v) {
            Log.i("NeuralNet.TAG", "I'm in NeuralNet()");
            NeuralNet temp = NeuralNet.getInstance();
            Log.i("NeuralNet.TAG", "temp = NeuralNet.getInstance() is " +String.valueOf(temp!=null));
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "PathFinder"
            );
            final File file = new File(
                    mediaStorageDir.getPath() + File.separator + instance.filename
            );
            try {
                Log.i("NeuralNet.TAG", "I'm in try block in doInBackground()");
                instance.fos = new FileOutputStream(file);
                instance.oos = new ObjectOutputStream(instance.fos);
                instance.oos.writeObject(instance.network);
                Log.i("NeuralNet.TAG", "Just executed instance.oos.writeObject(instance.network)");
                instance.fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
