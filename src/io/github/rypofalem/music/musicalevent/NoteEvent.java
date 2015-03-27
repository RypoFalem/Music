package io.github.rypofalem.music.musicalevent;

import org.bukkit.Sound;

public class NoteEvent extends MusicalEvent{
	
	Sound sound;
	float volume;
	float pitch;
	
	public NoteEvent(float beat, Sound sound, float volume, float pitch) {
		this.beat = beat;
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public NoteEvent(float beat, String sound, float volume, float pitch) {
		this.beat = beat;
		this.sound = Sound.valueOf(sound);
		this.volume = volume;
		this.pitch = pitch;
	}

	public Sound getSound() {
		return sound;
	}

	public float getVolume() {
		return volume;
	}

	public float getPitch() {
		return pitch;
	}
	
	public float getBeat(){
		return this.beat;
	}
}
