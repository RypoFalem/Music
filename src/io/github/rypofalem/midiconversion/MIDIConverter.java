package io.github.rypofalem.midiconversion;

import io.github.rypofalem.music.musicalevent.MusicalEvent;
import io.github.rypofalem.music.musicalevent.NoteEvent;
import io.github.rypofalem.music.musicalevent.TempoEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.bukkit.Sound;

public class MIDIConverter {

	public static final int NOTE_ON = 0x90;
	public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	public static final Sound[] SOUNDS = { Sound.NOTE_BASS, Sound.NOTE_PIANO, Sound.ORB_PICKUP};
	public static final float[] PITCHES ={.5f, .53f, .56f, .6f, .63f, .67f, .7f, .75f, .8f, .85f, .9f, .95f, 
		1f, 1.05f, 1.1f, 1.2f, 1.25f, 1.32f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f, 2f};
	public static final int TEMPO = 0x51;
	public static final int SMPTE_OFFSET = 0x54;
	public static final int TIMESIG = 0x58;
	public static ArrayList<MusicalEvent> events = new ArrayList<MusicalEvent>();
	static String FileName= "Dark Cloud - Matataki Village.mid";
	
	public static void main(String[] args){
		Sequence sequence = null;
		try {
			sequence = MidiSystem.getSequence(new File(FileName));
			File songfile = new File(FileName + ".song");
			songfile.delete();
		} catch (InvalidMidiDataException e) {e.printStackTrace();System.exit(1);} catch (IOException e) {e.printStackTrace(); System.exit(1);}
		float tpb= sequence.getResolution();
		System.out.println(tpb);
		for(Track track: sequence.getTracks()){
			for(int i=0; i<track.size(); i++){
				MidiEvent e = track.get(i);
				MidiMessage m = e.getMessage();
				if(m instanceof MetaMessage	){
					String typeString ="";
					int type = ((MetaMessage) m).getType();
					switch(type){
					case TEMPO : typeString = "Tempo";
					addEvent(new TempoEvent(toInt(((MetaMessage) m).getData()), ((float)e.getTick()/tpb)));
					break;
					case SMPTE_OFFSET : typeString = "SMPTE offset";
					break;
					case TIMESIG : typeString = "Time Signature";
					break;
					default: typeString = m.getClass().getName();
					break;
					}
				}
				if (m instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage) m;
					if (sm.getCommand() == NOTE_ON) {
						int key = sm.getData1()-9-36;
						int octave = (key) / 24;
						int note = key % 12;
						//String noteName = NOTE_NAMES[note];
						int velocity = sm.getData2();
						float beat =(float)e.getTick()/tpb;
						Sound sound = getSoundFromOctave(octave);
						float pitch = getPitchFromKey(key);
						float volume = (float)velocity*3 / (float)100;
						if(sound == Sound.NOTE_BASS || sound == Sound.ORB_PICKUP) volume /=4;
						if(volume == 0) continue;
						addEvent(new NoteEvent(beat, sound, volume , pitch));
						//System.out.println(e.getTick() + " " + " key=" + key + " velocity: " + velocity);
					}
				}
			}
		}
		for(MusicalEvent e: events){
			if(e instanceof NoteEvent){
				NoteEvent ne = (NoteEvent)e;
				writeToFile(ne.getBeat() + " Note " + ne.getPitch() + " " + ne.getSound().toString() +" Volume " + ne.getVolume());
			}else if(e instanceof TempoEvent){
				TempoEvent te = (TempoEvent)e;
				writeToFile(te.getBeat() + " Tempo " + te.getBPM());
			}
		}
	}

	public static void writeToFile(String s){
		FileWriter writer = null;
		PrintWriter printer =null;
		File file = new File(FileName.substring(0, FileName.lastIndexOf(".mid")) + ".song");
		try {
			writer = new FileWriter(file, true);
			printer = new PrintWriter(writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printer.println(s);
		printer.close();
	}
	
	//takes the last 4 bytes of a byte array and shoves the data into an integer.
	public static int toInt(byte[] data){
		int dataInt=0;
		for(int j = 0; j< data.length; j++){
			dataInt = dataInt | Byte.toUnsignedInt(data[j])<<(8*(data.length-1-j));
		}
		return dataInt;
	}
	
	public static void addEvent(MusicalEvent newEvent){
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
	
	public static Sound getSoundFromOctave(int octave){
		if(octave < 0 || octave > SOUNDS.length-1){
			System.out.println("Octave out of bounds" + octave);
			return Sound.BURP;
		}
		return SOUNDS[octave];
	}
	
	public static float getPitchFromKey(int key){
		return PITCHES[Math.abs((key)%24)];
	}
}


