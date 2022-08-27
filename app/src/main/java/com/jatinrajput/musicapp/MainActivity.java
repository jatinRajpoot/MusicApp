package com.jatinrajput.musicapp;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView =findViewById(R.id.list_item);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Log.d(String.valueOf(MainActivity.this), "onPermissionGranted: ");
                        Toast.makeText(MainActivity.this, "Runtime Permission access", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mysongs = fetchsongs(Environment.getExternalStorageDirectory());
                        String [] items = new String[mysongs.size()];
                        Log.d("Mysongs are fetched", "onPermissionGranted: This step has been done");
                        for(int i=0;i<mysongs.size();i++){
                            items[i]=mysongs.get(i).getName().replace(".mp3","");
                        }
                        Log.d("For loop ended", "onPermissionGranted: For loop ended");
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(adapter);
                        Log.d("Set Adapter has been done", "onPermissionGranted: ");
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Intent intent =new Intent(MainActivity.this,playsong.class);
                                String currentsong=listView.getItemAtPosition(position).toString();
                                intent.putExtra("SongsList", mysongs);
                                intent.putExtra("CurrentSong",currentsong);
                                intent.putExtra("position",position);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                    PermissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
    public ArrayList<File> fetchsongs(File file){
        ArrayList arrayList=new ArrayList();
        File [] songs=file.listFiles();
        if(songs!=null){
            for (File myfile:songs){
                if (!myfile.isHidden() && myfile.isDirectory()){
                    arrayList.addAll(fetchsongs(myfile));
                }
                else {
                    if (myfile.getName().endsWith(".mp3") && !myfile.getName().startsWith(".")){
                        arrayList.add(myfile);
                    }
                }
            }
        }
        return arrayList;
    }
}