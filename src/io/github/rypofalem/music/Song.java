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
	
	/*
	 * Creates a song  by reading the events from 
	 * the given .song file. The file must be located
	 * in the plugins/music/ directory.
	 */
	public Song(File file) throws Exception{
		this.song =new SongReader(file).getSongEvents();
	}
	
	/*
	 * Creates a song from an ArrayList of 
	 * MusicalEvents.
	 */
	public Song(ArrayList<MusicalEvent> song){
		this.song =song;
	}
	
	/*
	 * Creates a song using the given string as 
	 * the filename representing the .song file's
	 * location. The file must be located in the
	 * plugins/music/ directory
	 */
	public Song(String filename) throws Exception {
		this.song = new SongReader(filename).getSongEvents();
	}
	
	/*
	 * Processes all MusicalEvents (notes and tempo changes)
	 * that should occur in this tick of the song.
	 */
	protected ArrayList<NoteEvent> playNext() {
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
	
	/*
	 * Called when the song is finished or when
	 * the song was started or resumed.
	 */
	protected void setFinished (boolean isFinished){
		finished = isFinished;
	}
	
	/*
	 * Returns true of the song is done playing
	 * either because it was stopped, or because
	 * it reached the last MusicalEvent
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/*
	 * Resets the state of this song
	 * so it is ready to play again
	 */
	protected void reset(){
		tick = 0;
		index = 0;
		finished = false;
	}
	
	/*
	 * Sets the Minecraft Ticks per each 
	 * Song beat. The notes-per beat is song
	 * dependent. Increasing or decreasing
	 * this value will immediately cause
	 * the song to play slower or faster, 
	 * respectively.
	 */
	public void setTPB(float tpb){
		tick = (int) (tick * tpb / this.tpb);
		this.tpb = tpb;
	}
	
	/*
	 * Sets the frequency of the Song beat
	 * in Beats per Minute. The notes per beat
	 * is song dependent. Increasing or decreasing
	 * this value will cause immediately cause the
	 * Song to play faster or slower, respectively.
	 */
	public void setBPM(float bpm){
		setTPB(1200f/bpm);
	}
	
	/*
	 * Returns the beats per minute of this 
	 * Song. The notes per beat is song 
	 * dependent. Higher or lower values mean the
	 * song is playing faster or slower, respectively.
	 */
	public float getBPM(){
		return 1200f/tpb;
	}
}
