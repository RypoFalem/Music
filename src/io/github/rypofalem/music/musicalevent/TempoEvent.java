package io.github.rypofalem.music.musicalevent;


public class TempoEvent extends MusicalEvent {
	float bpm;
	
	public TempoEvent(int tempo, float beat){
		if(tempo <=0 ){
			bpm = 0;
		}
		else{
			bpm = 60000000f/(float)tempo;
		}
		this.beat = beat; 
	}
	
	public TempoEvent(float bpm, float beat){
		this.beat = beat;
		this.bpm = bpm;
	}
	
	public float getBPM(){
		return bpm;
	}
}
