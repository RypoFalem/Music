package io.github.rypofalem.music;


import io.github.rypofalem.music.musicalevent.NoteEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/*
 * A MusicPlayer that broadcasts a song 
 * to all of it's listeners
 */
public class MusicPlayer {
	
	Plugin plugin;
	Song song;
	UpdateSongTask songTask;
	HashSet<UUID> listeners = new HashSet<UUID>();
	
	public MusicPlayer(Song song, Plugin plugin){
		this.song = song;
		this.plugin = plugin;
	}
	
	//Starts a song over from the beginning.
	// if you want to resume a paused song
	//use resumeSong();
	public void startSong(){
		stopSong();
		if(song.isFinished()) song.setFinished(false);
		songTask = new UpdateSongTask();
		songTask.runTaskTimer(plugin, 0L, 1L);
	}
	
	public void stopSong(){
		if(songTask == null) return;
		songTask.cancel();
		song.reset();
	}
	
	public void resumeSong(){
		
	}
	public void pauseSong(){
		if(songTask == null) return;
		songTask.cancel();
	}
	//Stops the current song and sets a new song
	// new song must be started with startSong();
	public void setSong(Song song){
		stopSong();
		this.song = song;
	}
	
	public void addListener(UUID listener){
		if(listeners.contains(listener)) return;
		listeners.add(listener);
	}
	
	public void removeListener(UUID listener){
		if(!listeners.contains(listener)) return;
		listeners.remove(listener);
	}
	
	public void updateSong() {
		ArrayList<NoteEvent> notes =song.playNext();
		playNotes(notes);
		if(song.isFinished()) stopSong();
	}
	
	private void playNotes(ArrayList<NoteEvent> notes) {
		if(notes == null) return;
		for(UUID listener: listeners){
			Player p =Bukkit.getServer().getPlayer(listener);
			if(p == null ) continue;
			if(!p.isOnline()) continue;
			for(NoteEvent note : notes){
				p.playSound(p.getLocation().add(new Vector().setY(1)), note.getSound(), note.getVolume(), note.getPitch());
				
			}
		}
	}

	private class UpdateSongTask extends BukkitRunnable{

		@Override
		public void run() {
			updateSong();
		}
	}
}
