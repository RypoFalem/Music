package io.github.rypofalem.music;

import io.github.rypofalem.music.musicalevent.MusicalEvent;
import io.github.rypofalem.music.musicalevent.NoteEvent;
import io.github.rypofalem.music.musicalevent.TempoEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Reads a song from a .song file
 */
public class SongReader {

	File songfile;
	ArrayList<MusicalEvent> events;

	protected SongReader(File file) throws Exception {
		this.songfile = file;
		events = new ArrayList<MusicalEvent>();
		if(!file.exists()) throw new Exception("file does not exist! " + file.getAbsolutePath());
	}
	
	protected SongReader(String filename) throws Exception {
		this.songfile = new File(filename);
		events = new ArrayList<MusicalEvent>();
		if(!songfile.exists()) throw new Exception("Song file does not exist! " + songfile.getAbsolutePath());
	}

	protected Song getSong(){
		readSong();
		return new Song(events);
	}
	
	protected ArrayList<MusicalEvent> getSongEvents(){
		readSong();
		return this.events;
	}
	
	private void readSong(){
		Pattern songPattern = Pattern.compile("(\\d\\.?)+ +note +(\\d\\.?)+ +\\w+ volume +(\\d\\.?)+", Pattern.CASE_INSENSITIVE);
		Pattern tempoPattern =  Pattern.compile("(\\d\\.?)+ +tempo+ +(\\d\\.?)+", Pattern.CASE_INSENSITIVE);
		try {
			BufferedReader reader = new BufferedReader(new FileReader (songfile));
			while(true){
				String line = reader.readLine();
				if(line == null)break;
				if(songPattern.matcher(line).matches()){
					Scanner scanLine = new Scanner(line);
					scanLine.useDelimiter(Pattern.compile(" "));
					float beat = scanLine.nextFloat();
					scanLine.next();
					float pitch = scanLine.nextFloat();
					String sound = scanLine.next();
					scanLine.next();
					float volume = scanLine.nextFloat();
					scanLine.close();
					addEvent(new NoteEvent(beat, sound, volume, pitch));
				}
				if(tempoPattern.matcher(line).matches()){
					Scanner scanLine = new Scanner(line);
					scanLine.useDelimiter(Pattern.compile(" "));
					float beat = scanLine.nextFloat();
					scanLine.next();
					float tempo = scanLine.nextFloat();
					scanLine.close();
					addEvent(new TempoEvent(tempo, beat));
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void addEvent(MusicalEvent newEvent){
		int i = 0;
		boolean inserted =false;
		for(MusicalEvent me : events){
			if(newEvent.getBeat() <= me.getBeat()){
				events.add(i, newEvent);
				inserted = true;
				i++;
				break;
			}
			i++;
		}
		if(!inserted){
			events.add(events.size(), newEvent);
		}
	}
}
