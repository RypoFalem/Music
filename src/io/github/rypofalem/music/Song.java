package io.github.rypofalem.music;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Song {
	ArrayList<MusicalNote> song;
	int tick=0;
	int index=0;
	boolean finished = false;
	
	public Song(File file){
		
	}
	
	Song(ArrayList<MusicalNote> song){
		this.song =song;
	}

	public ArrayList<MusicalNote> playNext() {
		ArrayList<MusicalNote> notes = new ArrayList<MusicalNote>();
		
		while( index<song.size()){
			MusicalNote note = song.get(index);
			if(note.getTick() <= tick){
				notes.add(note);
				index++;
			}else{
				break;
			}
		}
		if(index>=song.size()) finished = true;
		tick++;
		return notes;
	}

	public boolean isFinished() {
		return finished;
	}
}
