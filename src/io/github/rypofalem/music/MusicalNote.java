package io.github.rypofalem.music;

import org.bukkit.Note;
import org.bukkit.Sound;

public class MusicalNote{
	
	int tick;
	Sound sound;
	float volume;
	float pitch;
	
	public MusicalNote(int tick, Sound sound, float volume, float pitch) {
		this.tick = tick;
		this.sound = sound;
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
	
	public int getTick(){
		return tick;
	}
}
