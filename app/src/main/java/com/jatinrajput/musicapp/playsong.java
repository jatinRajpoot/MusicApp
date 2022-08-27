package com.jatinrajput.musicapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;

public class playsong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }
    TextView SongName;
    ImageView Play;
    ImageView Previous;
    ImageView Next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int positionofSong;
    Thread updateSeek;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        seekBar=findViewById(R.id.seekBar);
        SongName=findViewById(R.id.viewSongName);
        Play=findViewById(R.id.Play);
        Next=findViewById(R.id.next);
        Previous=findViewById(R.id.Previous);
        Intent intent =getIntent();
        songs = (ArrayList) intent.getParcelableArrayListExtra("SongsList");
//        textContent=bundle.getString("CurrentSong"); This line is doing same thing which is done by line below
        textContent=intent.getStringExtra("CurrentSong");
        SongName.setText(textContent);
        positionofSong=intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(positionofSong).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        updateSeek = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;
                try{
                    while(currentPosition<mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    Play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    Play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }

            }
        });
        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(positionofSong!=0){
                    positionofSong =positionofSong - 1;
                }
                else{
                    positionofSong = songs.size() - 1;
                }
                Uri uri = Uri.parse(songs.get(positionofSong).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                Play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(positionofSong).getName().toString();
                SongName.setText(textContent);
            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(positionofSong!=songs.size()-1){
                    positionofSong = positionofSong + 1;
                }
                else{
                    positionofSong = 0;
                }
                Uri uri = Uri.parse(songs.get(positionofSong).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                Play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(positionofSong).getName().toString();
                SongName.setText(textContent);

            }
        });
    }
}