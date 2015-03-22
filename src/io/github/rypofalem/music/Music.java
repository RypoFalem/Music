package io.github.rypofalem.music;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Music extends JavaPlugin implements Listener {
	MusicPlayer mp;
	
	public void onEnable(){
		//load Songs
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable(){
		
	}
	
	@EventHandler
	public void play(PlayerInteractEvent e){
		if(!e.hasItem()) return;
		if(!(e.getMaterial() == Material.DIAMOND)) return;
		playMusic(e.getPlayer());
	}
	
	public void playMusic(Player p){
		print("Playing music!");
		ArrayList<MusicalNote> notes = new ArrayList<MusicalNote>();
		Sound test = Sound.NOTE_PIANO;
		notes.add(new MusicalNote(1, test, 3, 1.05f));
		notes.add(new MusicalNote(2.5f, test, 3, 1.05f));
		notes.add(new MusicalNote(3, test, 3, 1.6f));
		notes.add(new MusicalNote(4, test, 3, 1.6f));
		notes.add(new MusicalNote(5, test, 3, 1.8f));
		notes.add(new MusicalNote(6.5f, test, 3, 1.8f));
		notes.add(new MusicalNote(7f, test, 3, 1.6f));
		Song song = new Song(notes);
		song.setBPM(90);
		mp = new MusicPlayer(song, this);
		mp.addListener(p.getUniqueId());
		mp.startSong();
	}
	
	public static void print(String s){
		Bukkit.getServer().broadcastMessage("Music - " + s);
	}
}