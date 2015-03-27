package io.github.rypofalem.music;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Music extends JavaPlugin implements Listener {
	MusicPlayer mp = null;
	static boolean debug=true;
	String songfolderName = "plugins" + File.separator + "music" + File.separator;
	
	public void onEnable(){
		//load Songs
		getServer().getPluginManager().registerEvents(this, this);
		File songfolder = new File(songfolderName);
		if(!songfolder.exists()){
			songfolder.mkdirs();
		}
	}
	
	public void onDisable(){
		
	}
	
	@EventHandler
	public void play(PlayerInteractEvent e){
		if(!e.hasItem()) return;
		try{
		if((e.getMaterial() == Material.GOLD_INGOT)){
			playMusic(e.getPlayer(), new Song(songfolderName + "Dark Cloud - Matataki Village.mid.song"));
		}
		}catch(Exception exception){
			exception.printStackTrace();
		}
		if((e.getMaterial() == Material.ROTTEN_FLESH)){
			mp.stopSong();
		}
	}
	
	
	public void playMusic(Player p, Song song){
		if(mp== null){
			mp =new MusicPlayer(song, this);
		}
		mp.setSong(song);
		mp.addListener(p.getUniqueId());
		mp.startSong();
	}
	
	
	
	public static void print(String s){
		if(!debug) return;
		Bukkit.getServer().broadcastMessage("Music - " + s);
	}
}