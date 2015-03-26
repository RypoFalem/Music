package io.github.rypofalem.music;

import io.github.rypofalem.songhelper.SongHelper;

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
	
	public void onEnable(){
		//load Songs
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable(){
		
	}
	
	@EventHandler
	public void play(PlayerInteractEvent e){
		if(!e.hasItem()) return;
		if((e.getMaterial() == Material.DIAMOND)){
			playMusic(e.getPlayer(), new Song(SongHelper.tetrisA()));
		}
		if((e.getMaterial() == Material.EMERALD)){
			playMusic(e.getPlayer(), new Song(SongHelper.zeldaValley()));
		}
		if((e.getMaterial() == Material.IRON_INGOT)){
			playMusic(e.getPlayer(), new Song(SongHelper.mario64Bowser()));
		}
		if((e.getMaterial() == Material.GOLD_INGOT)){
			playMusic(e.getPlayer(), new Song(SongHelper.darkcloudVillage()));
		}
		if((e.getMaterial() == Material.INK_SACK)){ //Ink sack means dye means lapis for some god damned reason
			playMusic(e.getPlayer(), new Song(SongHelper.zeldaOverworld()));
		}
		if((e.getMaterial() == Material.COAL)){
			playMusic(e.getPlayer(), new Song(SongHelper.dearlyBeloved()));
		}
		if((e.getMaterial() == Material.REDSTONE)){
			playMusic(e.getPlayer(), new Song(SongHelper.pokemonChampion()));
		}
		if((e.getMaterial() == Material.GLOWSTONE_DUST)){
			playMusic(e.getPlayer(), new Song(SongHelper.finalCountDown()));
		}
		if((e.getMaterial() == Material.QUARTZ)){
			playMusic(e.getPlayer(), new Song(SongHelper.stillAlive()));
		}
		if((e.getMaterial() == Material.ANVIL)){
			playMusic(e.getPlayer(), new Song(SongHelper.khEndOfWorld()));
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