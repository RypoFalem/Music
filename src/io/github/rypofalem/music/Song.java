package io.github.rypofalem.music;

import io.github.rypofalem.music.musicalevent.MusicalEvent;
import io.github.rypofalem.music.musicalevent.NoteEvent;
import io.github.rypofalem.music.musicalevent.TempoEvent;

import java.io.File;
import java.util.ArrayList;

/*
 * Instance of a song. A single instance keeps track
 * of how far into the song it is. Playing this song
 * via MusicPlayer to multiple players will always 
 * broadcast the same notes at about the same time.
 * In order for players to hear the same song 
 * independently, multiple instance of MusicPlayer
 * with this song must be used.
 */

public class Song {
	private ArrayList<MusicalEvent> song; 	//List of notes and tempo changes
	private int tick=0;						// number of times the song has been updated
	private int index=0; 					// the index of the next note to play
	private boolean finished = false;		// weather or not the song is no longer playing
	private float tpb = 10; 				// ticks per beat
	
	public Song(File file){
		
	}
	
	public Song(ArrayList<MusicalEvent> song){
		this.song =song;
	}

	public ArrayList<NoteEvent> playNext() {
		ArrayList<NoteEvent> notes = new ArrayList<NoteEvent>();
		
		while( index<song.size()){
			MusicalEvent me = song.get(index);
			if((me.getBeat() * tpb) <= tick){
				if(me instanceof NoteEvent){
					notes.add((NoteEvent)me);
				}else if(me instanceof TempoEvent){
					setBPM(((TempoEvent) me).getBPM());
				}
				index++;
			}else{
				break;
			}
		}
		if(index>=song.size()) setFinished(true);
		tick++;
		return notes;
	}
	
	protected void setFinished (boolean isFinished){
		finished = isFinished;
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
		tick = (int) (tick * tpb / this.tpb);
		this.tpb = tpb;
	}
	
	public void setBPM(float bpm){
		setTPB(1200f/bpm);
	}
	
	public float getBPM(){
		return 1200f/tpb;
	}
}
