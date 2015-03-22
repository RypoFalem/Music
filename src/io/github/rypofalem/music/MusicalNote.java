package io.github.rypofalem.music;

import org.bukkit.Sound;

public class MusicalNote{
	
	float beat;
	Sound sound;
	float volume;
	float pitch;
	
	public MusicalNote(float beat, Sound sound, float volume, float pitch) {
		this.beat = beat;
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
	
	public float getBeat(){
		return beat;
	}
}
