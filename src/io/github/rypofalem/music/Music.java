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
	MusicChannel mp = null;
	static boolean debug=true;
	String songfolderName = "plugins" + File.separator + "music" + File.separator;

	public void onEnable(){
		//load Songs
		getServer().getPluginManager().registerEvents(this, this);
		File songfolder = new File(songfolderName);
		if(!songfolder.exists()){
			songfolder.mkdirs();
		}
		
		try {
			mp =new MusicChannel(new Song(songfolderName + "Dark Cloud - Matataki Village.mid.song"), this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			if((e.getMaterial() == Material.ICE)){
				playMusic(e.getPlayer(), new Song(songfolderName + "Letitgo.song"));
			}
			if((e.getMaterial() == Material.APPLE)){
				playMusic(e.getPlayer(), new Song(songfolderName + "ConcerningHobbits.song"));
			}
			if((e.getMaterial() == Material.EGG)){
				playMusic(e.getPlayer(), new Song(songfolderName + "BanjoKazooie - Gruntildas Lair Click Clock Wood.song"));
			}
			if((e.getClickedBlock()!= null && e.getClickedBlock().getType() == Material.DIAMOND_BLOCK)){
				mp.addLocationListener(e.getClickedBlock().getLocation());
				mp.startSong();
			}
			if(e.getMaterial() == Material.CACTUS){
				mp.pauseSong();
			}
			if(e.getMaterial() == Material.CAKE){
				mp.resumeSong();
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
			mp =new MusicChannel(song, this);
		}
		mp.setSong(song);
		//mp.addPlayerListener(p.getUniqueId());
		mp.startSong();
	}



	public static void print(String s){
		if(!debug) return;
		Bukkit.getServer().broadcastMessage("Music - " + s);
	}
}