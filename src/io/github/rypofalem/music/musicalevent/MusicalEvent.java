package io.github.rypofalem.music.musicalevent;

/*
 * Represents a NoteEvent or TempoEvent.
 * These events are executed on the beat
 * they are assigned.
 */
public abstract class MusicalEvent {
	public float beat;

	public float getBeat(){
		return beat;
	}
}
