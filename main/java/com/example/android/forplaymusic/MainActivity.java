package com.example.android.forplaymusic;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    final ArrayList<SongFile> songs = new ArrayList<SongFile>() ;
    private int songIdx = 0 , totalSongs = 1, focus = 0;
    private boolean isPlaying = false ;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                focus = 1;
                if(isPlaying) {
                    mediaPlayer.start();
                }
            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                focus = 0;
                isPlaying = false;
                if(mediaPlayer != null){
                    mediaPlayer.pause();
                }
                ImageView button = (ImageView) findViewById(R.id.play_pause_button);
                button.setImageResource(R.drawable.baseline_play_arrow_white_48);
            }else {
                focus = -1;
                mediaPlayer.pause();
            }
        }
    };

    private MediaPlayer.OnCompletionListener CompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mMediaPlayer) {
            //here, focus is always 1
            releaseMediaPlayer();
            songIdx = (songIdx + 1) % totalSongs;
            TextView songName = (TextView)findViewById(R.id.song_name);
            songName.setText(songs.get(songIdx).getSongName());
            mediaPlayer = MediaPlayer.create(MainActivity.this , songs.get(songIdx).getSongResourceId());
            mediaPlayer.setOnCompletionListener(CompletionListener);
            mediaPlayer.start();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView songName = (TextView) findViewById(R.id.song_name);
        songName.setSelected(true);

        songs.add(new SongFile("Animals -- Maroon" , R.raw.animals));


        SongAdapter songAdapter = new SongAdapter(this, songs);
        ListView listView = (ListView)findViewById(R.id.song_list_view);
        listView.setAdapter(songAdapter);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = MediaPlayer.create(MainActivity.this, songs.get(songIdx).getSongResourceId());
        mediaPlayer.setOnCompletionListener(CompletionListener);
        ImageView playPauseButton = (ImageView)findViewById(R.id.play_pause_button);
        playPauseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(focus == 0){
                    int result = audioManager.requestAudioFocus(focusChangeListener , AudioManager.STREAM_MUSIC , AudioManager.AUDIOFOCUS_GAIN);
                    if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                        focus = 1;
                        mediaPlayer.start();
                        ImageView button = (ImageView) findViewById(R.id.play_pause_button);
                        button.setImageResource(R.drawable.baseline_pause_white_48);
                        isPlaying = true;
                    }
                }else if(focus == 1){
                    ImageView button = (ImageView) findViewById(R.id.play_pause_button);
                    if(isPlaying){
                        mediaPlayer.pause();
                        button.setImageResource(R.drawable.baseline_play_arrow_white_48);
                        isPlaying = false;
                    }else {
                        mediaPlayer.start();
                        button.setImageResource(R.drawable.baseline_pause_white_48);
                        isPlaying = true;
                    }
                }else{
                    ImageView button = (ImageView) findViewById(R.id.play_pause_button);
                    if(isPlaying){
                        button.setImageResource(R.drawable.baseline_play_arrow_white_48);
                        isPlaying = false;
                    }else {
                        button.setImageResource(R.drawable.baseline_pause_white_48);
                        isPlaying = true;
                    }
                }
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int idx, long l) {
                mediaPlayer.release();
                songIdx = idx;
                mediaPlayer = MediaPlayer.create(MainActivity.this , songs.get(songIdx).getSongResourceId());
                mediaPlayer.setOnCompletionListener(CompletionListener);

                TextView songName = (TextView)findViewById(R.id.song_name);
                songName.setText(songs.get(songIdx).getSongName());
                if(focus == 0){
                    int result = audioManager.requestAudioFocus(focusChangeListener , AudioManager.STREAM_MUSIC , AudioManager.AUDIOFOCUS_GAIN);
                    if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                        focus = 1;
                        mediaPlayer.start();
                        ImageView button = (ImageView) findViewById(R.id.play_pause_button);
                        button.setImageResource(R.drawable.baseline_pause_white_48);
                        isPlaying = true;
                    }
                }else if(focus == 1){
                    mediaPlayer.start();
                    ImageView button = (ImageView) findViewById(R.id.play_pause_button);
                    button.setImageResource(R.drawable.baseline_pause_white_48);
                    isPlaying = true;
                }else{
                    isPlaying = true;
                }
            }
        });
    }


//      to make activity not destroyed on back button is clicked
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    private void releaseMediaPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//        releaseMediaPlayer();
//    }

}