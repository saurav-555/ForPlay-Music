package com.example.android.forplaymusic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter<SongFile> {
    public SongAdapter(Activity context , ArrayList<SongFile> songs ){
        // calling superClass
        super(context ,0 , songs);
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item , parent , false);
        }

        SongFile currentSong = getItem(position);
        TextView songName = (TextView)listItem.findViewById(R.id.song_name_text);
        songName.setText(currentSong.getSongName());
        return listItem;
    }

}
