package io.github.rypofalem.music;


import io.github.rypofalem.music.musicalevent.NoteEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/*
 * A MusicPlayer that broadcasts a song 
 * to all of it's listeners
 */
public class MusicChannel {
	
	private Plugin plugin;
	private Song song;
	private UpdateSongTask songTask;
	private HashSet<UUID> playerListeners = new HashSet<UUID>();
	private HashSet<Location> locationListeners = new HashSet<Location>();
	
	public MusicChannel(Song song, Plugin plugin){
		this.song = song;
		this.plugin = plugin;
	}
	
	/*Starts a song over from the beginning.
	* if you want to resume a paused song
	* use resumeSong();
	*/
	public void startSong(){
		stopSong();
		if(song.isFinished()) song.setFinished(false);
		songTask = new UpdateSongTask();
		songTask.runTaskTimer(plugin, 0L, 1L);
	}
	
	/*
	 * Stops the UpdateSongTask and resets
	 * the song. Use pauseSong() if you want
	 * to resume the song from where it left
	 * off later.
	 */
	public void stopSong(){
		if(songTask == null) return;
		songTask.cancel();
		songTask = null;
		song.reset();
	}
	
	/*
	 * Starts the UpdateSongTask, continuing
	 * the song from the last note played.
	 * If you want to start a song over from
	 * the beginning, use startSong().
	 * 
	 * Fails silently if the song task is
	 * still active.
	 */
	public void resumeSong(){
		if(songTask != null ) return;
		if(song.isFinished()) song.setFinished(false);
		songTask = new UpdateSongTask();
		songTask.runTaskTimer(plugin, 0L, 1L);
	}
	
	/*
	 * Stops the UpdateSongTask to pause the song,
	 * allowing it to be resumed with resumeSong()
	 * 
	 */
	public void pauseSong(){
		if(songTask == null) return;
		songTask.cancel();
		songTask = null;
	}
	
	/*
	 * Stops the current song and sets a new song
	 * new song must be started with startSong()
	 */
	public void setSong(Song song){
		stopSong();
		this.song = song;
	}
	
	/*
	 * Adds the player represented but the player's
	 * UUID to the list of players to broadcast
	 * the musicplayer's song to. The notes
	 * will broadcast to this player on the next
	 * tick.
	 */
	public void addPlayerListener(UUID listener){
		if(playerListeners.contains(listener)) return;
		playerListeners.add(listener);
	}
	
	/*
	 * Removes the player represented but the player's
	 * UUID from the list of players to broadcast
	 * the musicplayer's song to. The notes
	 * will not broadcast to this player on the next
	 * tick.
	 */
	public void removePlayerListener(UUID listener){
		if(!playerListeners.contains(listener)) return;
		playerListeners.remove(listener);
	}
	
	/*
	 * Removes the location from the list of locations
	 * to broadcast musicplayer's songs to. The notes
	 * will broadcast to this location on the next
	 * tick.
	 */
	public void addLocationListener(Location listener){
		if(locationListeners.contains(listener)) return;
		locationListeners.add(listener);
	}
	
	/*
	 * Removes the location from the list of locations
	 * to broadcast musicplayer's songs to. The notes
	 * will not broadcast to this location on the next
	 * tick.
	 */
	public void removeLocationListener(Location listener){
		if(!locationListeners.contains(listener)) return;
		locationListeners.remove(listener);
	}
	
	/*
	 * Moves the song to the next tick. Retrieves
	 * all the notes that need to be played that tick
	 * and plays them. Stops the song if the song ended.
	 */
	public void updateSong() {
		ArrayList<NoteEvent> notes =song.playNext();
		playNotes(notes);
		if(song.isFinished()) stopSong();
	}
	
	/*
	 * Called by updateSong(), broadcasts all NoteEvents
	 * set to play that tick to all playerListeners and 
	 * locationListeners
	 */
	private void playNotes(ArrayList<NoteEvent> notes) {
		if(notes == null) return;
		if(notes.isEmpty())	return;
		
		//play notes to players
		for(UUID listener: playerListeners){
			Player p =Bukkit.getServer().getPlayer(listener);
			if(p == null ) continue;
			if(!p.isOnline()) continue;
			for(NoteEvent note : notes){
				p.playSound(p.getLocation().add(new Vector().setY(1)), note.getSound(), note.getVolume(), note.getPitch()); //play note 1 block above player's head
			}
		}
		
		//play notes to locations
		for(Location location: locationListeners){
			Music.print("Going through listeners");
			if(!location.getChunk().isLoaded()) continue;
			Music.print("It's Loaded!");
			Music.print("Playing at " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
			for(NoteEvent note : notes){
				location.getWorld().playSound(location, note.getSound(), note.getVolume(), note.getPitch());
			}
		}
	}
	
	/*
	 * A task that runs once per tick and
	 * calls updateSong() each tick.
	 */
	private class UpdateSongTask extends BukkitRunnable{
		@Override
		public void run() {
			updateSong();
		}
	}
}
