package io.github.rypofalem.music;

import java.io.File;
import java.util.ArrayList;

/*
 * Instance of a song. A single instance keeps track
 * of how far into the song it is. Playing this song
 * via MusicPlayer to multiple players will always 
 * broadcast the same notes at about the same time.
 * In order for players to hear the same song 
 * independently, multiple instance of this song
 * must be used.
 */

public class Song {
	ArrayList<MusicalNote> song; 	//List of notes
	int tick=0;						// number of times the song has been updated
	int index=0; 					// the index of the next note to play
	boolean finished = false;		// weather or not the song has reached the last note
	float tpb = 10; 				// ticks per beat
	
	public Song(File file){
		tpb =10;
	}
	
	public Song(ArrayList<MusicalNote> song){
		this.song =song;
	}

	public ArrayList<MusicalNote> playNext() {
		ArrayList<MusicalNote> notes = new ArrayList<MusicalNote>();
		
		while( index<song.size()){
			MusicalNote note = song.get(index);
			if(note.getBeat() * tpb <= tick){
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
	
	public void reset(){
		tick = 0;
		index = 0;
		finished = false;
	}
	
	public void setTPB(float tpb){
		if(index > 1) return; //TODO throw exception
		this.tpb = tpb;
	}
	
	public void setBPM(float bpm){
		setTPB(1200f/bpm);
	}
}
