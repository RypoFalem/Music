package io.github.rypofalem.music;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MusicPlayer {
	
	Plugin plugin;
	Song song;
	UpdateSongTask songTask;
	HashSet<UUID> listeners = new HashSet<UUID>();
	
	MusicPlayer(Song song, Plugin plugin){
		this.song = song;
		this.plugin = plugin;
		UpdateSongTask update = new UpdateSongTask();
		update.runTaskTimer(plugin, 0L, 1L);
	}
	
	void startSong(){
		stopSong();
		UpdateSongTask update = new UpdateSongTask();
		update.runTaskTimer(plugin, 0L, 1L);
	}
	
	void stopSong(){
		if(songTask == null) return;
		songTask.cancel();
		songTask = null;
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
		ArrayList<MusicalNote> notes =song.playNext();
		playNotes(notes);
		if(song.isFinished()) stopSong();
	}
	
	private void playNotes(ArrayList<MusicalNote> notes) {
		if(notes == null) return;
		for(UUID listener: listeners){
			Player p =Bukkit.getServer().getPlayer(listener);
			if(p == null ) continue;
			if(!p.isOnline()) continue;
			for(MusicalNote note : notes){
				p.playSound(p.getLocation(), note.getSound(), note.getVolume(), note.getPitch());
				Music.print(""+ note.getSound().toString());
			}
		}
	}

	class UpdateSongTask extends BukkitRunnable{
		
		public UpdateSongTask(){
		}

		@Override
		public void run() {
			updateSong();
		}
	}
}
