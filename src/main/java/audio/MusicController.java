package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;


import functions.*;
import main.BotMusicListener;
import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class MusicController extends CustomFunctions
{
	public static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
	
	public final static Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
	
	public static int isInTotal = 0;
	
	public static String botGuildsId = "", adminGuildsId = "";

	public static Message msg;
	public static HashMap<Guild, TextChannel> alertsList = new HashMap<Guild, TextChannel>();
	public static List<Guild> listg;
	public static List<Message> settingMessage;
	
	public static int gunBamStateRun = 0;
	
	public static Timer gunBamStateTimer = new Timer();
	
	public static TextChannel leaveTc;
	public static Message leaveMsg;
	public static MessageReceivedEvent leaveEvent;
	
	public static void go()    // Initialize everything when bot is started
	{
		playerManager.setItemLoaderThreadPoolSize(2);
		playerManager.setPlayerCleanupThreshold(1800000);
		playerManager.setTrackStuckThreshold(10000);
		playerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
		
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
		
		
		isInTotal = 0;
		
		/*
		final String ipv6Block = CredentialManager.getInstance().get(Credential.IPV6_BLOCK);
	    if (!Config.IS_SNAPSHOT && ipv6Block != null && !ipv6Block.isBlank()) {
	       // LOGGER.info("Configuring YouTube IP rotator");
	        final List<IpBlock> blocks = Collections.singletonList(new Ipv6Block(ipv6Block));
	        final AbstractRoutePlanner planner = new RotatingNanoIpRoutePlanner(blocks);

	        new YoutubeIpRotatorSetup(planner)
	                .forSource(playerManager.source(YoutubeAudioSourceManager.class))
	                .setup();
	    }
	    */
	    
	}
	
	private static synchronized GuildMusicManager getGuildAudioPlayer(Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event)    // Gets the music manager for each Guild
	{
		long guildId = Long.parseLong(guild.getId());
		
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null)
		{
			musicManager = new GuildMusicManager(playerManager, guild, tc, msg, event);
			musicManagers.put(guildId, musicManager);
		}

		try {
			guild.getAudioManager().setSendingHandler((AudioSendHandler) musicManager.getSendHandler());
		}
		catch(Exception e) {
			if(musicManager.scheduler.isIn == 1||musicManager.scheduler.isIn == 2) {
				isInTotal = isInTotal - 1;
			}
			
			String message = BotMusicListener.voiceTcMessage.get(guild);
			try {
				BotMusicListener.voiceTc.deleteMessageById(message).complete();
			}
			catch(Exception f) {}
			
			musicManagers.remove(guildId);
			alertsList.remove(guild);
			String name = "";
			try {
				name = "(동기화 오류/" + guild.getName() + ")";
			}
			catch(Exception g) {
				name = "(동기화 오류)";
			}
			
			BotMusicListener.errortc.sendMessage(":no_entry_sign: " + name + " **" + e.getMessage() + "**" + staticCause(e)).queue();
		}
		return musicManager;
	}
	
	public static void playStandBy(TextChannel channel, Message msg, MessageReceivedEvent event, String input) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		musicManager.scheduler.playStandByInt = 1;
		
		if(input.startsWith("https://")) {
			musicManager.scheduler.playStandBy = input;
    	}
    	else { 
			musicManager.scheduler.playStandBy = input;
			
    	}
	}
	
	public static void checkConnect(TextChannel channel, Message msg, MessageReceivedEvent event, GuildMusicManager musicManager) {
		if(musicManager.scheduler.isPlay == 1) {
			if(!channel.getGuild().getAudioManager().isConnected()) {
				if(Main.saveQueueAllowGuild.contains(channel.getGuild().getId())) {
					MusicController.stopPlaying(channel, msg, event, 9, musicManager.scheduler.save, musicManager.scheduler.setLanguageStop);
		        }
	    		else
	    			MusicController.stopPlaying(channel, msg, event, 9, 0, musicManager.scheduler.setLanguageStop);
			}
		}
	}
	
	public static void loadAndPlay(final TextChannel channel, Message msg, Message res, String trackUr, MessageReceivedEvent event, Member member, String lan, int menu)    // Plays the song
	{
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);

		checkConnect(channel, msg, event, musicManager);
		
		musicManager.scheduler.playStandByInt = 0;
		
		String languageAdd = ":hourglass: `" + trackUr + "` 로딩 중...";
		
		musicManager.scheduler.setLanguageStop = "kor";
		if(lan.equals("eng")) {
			languageAdd = ":hourglass: Loading `" + trackUr + "`.";
			musicManager.scheduler.setLanguageStop = "eng";
		}
		
		String stat = languageAdd;
		
		VoiceChannel myChannel = member.getVoiceState().getChannel();
		VoiceChannel botChannel = channel.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
		
		if(menu == 0) {
			if(musicManager.scheduler.isPlay == 1 && (myChannel != botChannel)) {
				String languageJoin = "**" + botChannel.getName() + "**에 들어간 후 시도하세요";
				
				if(lan.equals("eng")) {
					languageJoin = "Join **" + botChannel.getName() + "** and try again.";
				}
				
				channel.sendMessage(languageJoin).queue();
				playStandBy(channel, msg, event, trackUr);
				
				log(channel, event, "BOT: " + languageJoin);
				
				return;
			}
			
			else if(myChannel == null) {
				String languageJoin = "먼저 음성채널에 들어가세요";
				
				if(lan.equals("eng")) {
					languageJoin = "Join the voice channel first.";
				}
				
				if(res != null)
					res.editMessage(languageJoin).queue();
				else
					channel.sendMessage(languageJoin).queue();
	    		System.out.println("BOT: " + languageJoin);
	    		
	    		log(channel, event, "BOT: " + languageJoin);
	    		
	    		if(trackUr.length() > 1) {
	    			MusicController.playStandBy(channel, msg, event, trackUr);
	    		}
	    		return;
			}
			
		}

		if(trackUr.startsWith("https://")) {
			languageAdd = ":hourglass: 로딩 중..."; 
			
			if(lan.equals("eng")) {
				languageAdd = ":hourglass: Loading...";
			}
    		stat = languageAdd;
    	}
		else {
			if(staticIsNumber(trackUr) && musicManager.scheduler.search == 1) {
				
				int nu = Integer.parseInt(trackUr);
				
				if(nu>0 && nu<=7) {
					languageAdd = "추가 중...";
					if(lan.equals("eng")) 
						languageAdd = "Adding...";
					
					stat = languageAdd;
				}
			}
			
			trackUr = "ytsearch:" + trackUr;
		}
		
		String trackUrl = trackUr;
		int men = menu;
		
		if(musicManager.scheduler.isIn == 0)
			musicManager.scheduler.playAgainStringList.clear();
		
		if(res != null) {
			res.editMessage(stat).queue(response -> {
				loadAndPlayReal(channel, msg, response, trackUrl, men, event, member, musicManager, lan);
			});
		}
		else {
			channel.sendMessage(stat).queue(response -> {
				loadAndPlayReal(channel, msg, response, trackUrl, men, event, member, musicManager, lan);
			});
		}
		
	}
	
	public static void loadAndPlayReal(TextChannel channel, Message msg, Message response, String trackUrl, int men, MessageReceivedEvent event, Member member, GuildMusicManager musicManager, String lan) {
		boolean isPaused = musicManager.player.isPaused();
		
		try {
			if(!staticIsNumber(trackUrl.substring(BotMusicListener.queryCount))) {
				musicManager.scheduler.search = 0;
			}
			
			if(musicManager.scheduler.search == 1) {
				
				int a = Integer.parseInt(trackUrl.substring(BotMusicListener.queryCount)) - 1;
				
				if(a > 6) {a = 6;}
				else if(a < 0) {a = 0;}
				
				String duration = " ``(" + secTo((int)musicManager.scheduler.listSearch.get(a).getDuration()) + ")``";
				if(musicManager.scheduler.listSearch.get(a).getInfo().isStream == true) {
					duration = " ``(생방송)``";
					if(lan.equals("eng"))
						duration = " ``(LIVE)``";
				}
				
				String state = "";
				if (isPaused) {
					state = " (일시정지됨)";
					if(lan.equals("eng"))
						state = " (Paused)";
				}
				
				String language = "**" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "** 를 추가했어요" + state + duration;
				
				if(lan.equals("eng")) {
					language = "Added **" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "**." + state + duration;
				}
				
				String reply = "BOT: " + language + " `(총 " + (int)(musicManager.scheduler.queue.size() + 1) + "개)`";
				response.editMessage(language).queue();
				System.out.println(reply);
				log(channel, event, reply);
				
				musicManager.scheduler.playAgainStringList.add(musicManager.scheduler.listSearch.get(a).getInfo().uri);
				play(channel.getGuild(), msg, member, musicManager, musicManager.scheduler.listSearch.get(a), channel, event, 1, 1, lan);
				
				musicManager.scheduler.menu = 1;
				musicManager.scheduler.menuStr = musicManager.scheduler.listSearch.get(a).getInfo().uri;
				
				return;
			}
			
		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler()
		{
			@Override 
			public void trackLoaded(AudioTrack track) {
				
				String duration = " ``(" + secTo((int)track.getDuration()) + ")``";
				
				if(track.getInfo().isStream == true) {
					duration = " ``(생방송)``";
					if(lan.equals("eng"))
						duration = " ``(LIVE)``";
				}
				
				String state = "";
				if (isPaused) {
					state = " (일시정지됨)";
					if(lan.equals("eng"))
						state = " (Paused)";
				}
				
				String language = "**" + realTitle(track.getInfo().title) + "** 를 추가했어요" + state + duration;
				
				if(lan.equals("eng")) {
					language = "Added **" + realTitle(track.getInfo().title) + "**." + state + duration;
				}
					
				String reply = "BOT: " + language + " `(총 " + (int)(musicManager.scheduler.queue.size() + 1) + "개)`";
				response.editMessage(language).queue();
				System.out.println(reply);
				log(channel, event, reply);
				
				musicManager.scheduler.playAgainStringList.add(trackUrl);
				
				play(channel.getGuild(), msg, member, musicManager, track, channel, event, 1, 1, lan);
				
				if(men == 1) {
					musicManager.scheduler.menu = 0;
				}
				else {
					musicManager.scheduler.menu = 1;
					musicManager.scheduler.menuStr = track.getInfo().uri;
				}
			}

			@Override 
			public void playlistLoaded(AudioPlaylist playlist)  {
				
				// Only add the first song if it was searched for
				GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
				boolean isPaused = musicManager.player.isPaused();
				
				String state = "";
				if (isPaused) {
					state = " (일시정지됨)";
					if(lan.equals("eng"))
						state = " (Paused)";
				}
					
				if (playlist.isSearchResult()) {	
					
					String duration = " ``(" + secTo((int)playlist.getTracks().get(0).getDuration()) + ")``";
						
					if(playlist.getTracks().get(0).getInfo().isStream == true) {
						duration = " ``(생방송)``";
						if(lan.equals("eng"))
							duration = " ``(LIVE)``";
					}
					
					
					String language = "**" + realTitle(playlist.getTracks().get(0).getInfo().title) + "** 를 추가했어요" + state + duration;
						
					if(lan.equals("eng")) {
						language = "Added **" + realTitle(playlist.getTracks().get(0).getInfo().title) + "**." + state + duration;
					}
						
					String reply = "BOT: " + language + " `(총 " + (int)(musicManager.scheduler.queue.size() + 1) + "개)`";
					response.editMessage(language).queue();    //Add the song to the queue
					System.out.println(reply);
					log(channel, event, reply);
						
					play(channel.getGuild(), msg, member, musicManager, playlist.getTracks().get(0), channel, event, 1, 1, lan);
					musicManager.scheduler.menu = 1;
					musicManager.scheduler.menuStr = playlist.getTracks().get(0).getInfo().uri;
					
					musicManager.scheduler.playAgainStringList.add(playlist.getTracks().get(0).getInfo().uri);
					
					return;
				}

				if(playlist.getTracks().isEmpty()) {
					String language = "이 재생목록은 **비어있어요**";
					
					if(lan.equals("eng")) {
						language = "This playlist is **empty**.";
					}
					
					response.editMessage(language).queue();
					log(channel, event, "BOT: " + language);
					
					return;
				}
				
				
				String language = "**" + playlist.getName() + "**의 **" + playlist.getTracks().size() + "개** 항목을 추가했어요" + state;
				
				if(lan.equals("eng")) {
					if(playlist.getTracks().size() <= 1) language = "Added **" + playlist.getTracks().size() + " item** of **" + playlist.getName() + "**." + state;
					else language = "Added **" + playlist.getTracks().size() + " items** of **" + playlist.getName() + "**." + state;
				}
				
				String reply = "BOT: " + language + " `(총 " + (int)(musicManager.scheduler.queue.size() + playlist.getTracks().size()) + "개)`";
				response.editMessage(language).queue();
				System.out.println(reply);
				log(channel, event, reply);
				
				musicManager.scheduler.playAgainStringList.add(trackUrl);
				musicManager.scheduler.audioPlaylist.add(playlist);
				
				musicManager.scheduler.menu = 4;
				musicManager.scheduler.recentAddPlayListCount = playlist.getTracks().size();
				
				/*
				for (AudioTrack track : playlist.getTracks())        // Add all the tracks
				{
					play(channel.getGuild(), msg, member, musicManager, track, channel, event, 1);
				}
				*/
				
				int ended = 0;
				for(int i = 0; i<playlist.getTracks().size(); i++) {
					if(i == playlist.getTracks().size()-1) ended = 1;
					
					play(channel.getGuild(), msg, member, musicManager, playlist.getTracks().get(i), channel, event, 1, ended, lan);
				}
			}

			@Override 
			public void noMatches()
			{
				String ur = "";
				if(trackUrl.startsWith("https://")) ur = trackUrl;
				else ur = trackUrl.substring(BotMusicListener.queryCount);
				String language = "다시 시도하세요\n**" + ur + "**";;
				
				if(lan.equals("eng")) {
					language = "Try again.\n**" + ur + "**";;
				}
				String reply = "BOT: " + language;
				
				
				response.editMessage(language).queue();
				System.out.println(reply);
				log(channel, event, reply);
				
				MusicController.playStandBy(channel, msg, event, ur);
			
			}

			@Override 
			public void loadFailed(FriendlyException exception)
			{
				
				String reply = ":no_entry_sign: **" + exception.getMessage() + "**" + staticCause(exception);
				if(exception.getCause().toString().toLowerCase().contains("null"))
					reply = ":no_entry_sign: **" + exception.getMessage() + "** `(다른 검색어로 시도해 주세요)`";
				
				try {
					response.editMessage(reply).complete();
				}
				catch(Exception e) {
					channel.sendMessage(reply).queue();
				}
				System.out.println(reply);
				log(channel, event, reply);
				
				if(exception.getMessage().contains("Connecting")) return;
				
				/*
				if(exception.getMessage().contains("failed")) {
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + "> 재생이 안됩니다").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
				}
				*/
			}
		});
		}
		catch(Exception e) {
			
			String reply = ":no_entry_sign: (loadItemOrdered) **" + e.getMessage() + "**" + staticCause(e);
			channel.sendMessage(reply).queue();
			log(channel, event, reply);
		}
	}
	
	public static void loadAndPlayRandom(final TextChannel channel, Message msg, String trackUr, MessageReceivedEvent event, String lan)    // Plays the song
	{
		String stat = ":hourglass: 로딩 중...";
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		Member member = event.getMember();

		checkConnect(channel, msg, event, musicManager);
		
		VoiceChannel myChannel = member.getVoiceState().getChannel();
		VoiceChannel botChannel = event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
		
		if(musicManager.scheduler.isPlay == 1 && (myChannel != botChannel)) {
			String language = "**" + botChannel.getName() + "**에 들어간 후 시도하세요";
			if(lan.equals("eng")) 
				language = "Join **" + botChannel.getName() + "** and try again.";
			
			channel.sendMessage(language).queue();
			playStandBy(channel, msg, event, trackUr);
			
			log(channel, event, "BOT: " + language);
			return;
		}
		
		else if(myChannel == null) {
			String language = "먼저 음성채널에 들어가세요";
			if(lan.equals("eng")) 
				language = "Join the voice channel first.";
			
			channel.sendMessage(language).queue();
    		System.out.println("BOT: " + language);
    		
    		log(channel, event, "BOT: " + language);
    		
    		if(trackUr.length() > 1) {
    			MusicController.playStandBy(channel, msg, event, trackUr);
    		}
    		return;
		}

		if(musicManager.scheduler.isIn == 0)
			musicManager.scheduler.playAgainStringList.clear();
		
		channel.sendMessage(stat).queue(response -> {
		
		playerManager.loadItemOrdered(musicManager, trackUr, new AudioLoadResultHandler()
		{
			@Override 
			public void trackLoaded(AudioTrack track) {
				String language = "**재생목록 URL**이 아니네요";
				if(lan.equals("eng")) 
					language = "It is not **URL links to a playlist**.";
				
				response.editMessage(language).queue();
				
			}

			@Override 
			public void playlistLoaded(AudioPlaylist playlist)  {
				// Only add the first song if it was searched for
				
				if(playlist.getTracks().isEmpty()) {
					String language = "이 재생목록은 **비어있어요**";
					
					if(lan.equals("eng")) {
						language = "This playlist is **empty**.";
					}
					
					response.editMessage(language).queue();
					log(channel, event, "BOT: " + language);
					
					return;
				}
				
				GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
				boolean isPaused = musicManager.player.isPaused();
				
				String state = "";
				if (isPaused) {
					state = " (일시정지됨)";
					if(lan.equals("eng"))
						state = " (Paused)";
				}
				
				String languageA = "";
				if(Main.saveQueueAllowGuild.contains(channel.getGuild().getId()) || Main.loadAllowGuild.contains(channel.getGuild().getId())) {
				   languageA = "\n저장할 때는 **섞기전 상태로 저장해요**";
				    if(lan.equals("eng"))
				    	languageA = "\nWhen saving, **it is saved as it was before shuffling**.";
			    }
				
				String language = "**" + playlist.getName() + "**의 **" + playlist.getTracks().size() + "개** 항목을 **랜덤으로** 추가했어요" + state + languageA;
				if(lan.equals("eng")) {
					if(playlist.getTracks().size() <= 1) language = "Added **" + playlist.getTracks().size() + " item** of **" + playlist.getName() + " by **random**." + state + languageA;
					else language = "Added **" + playlist.getTracks().size() + " items** of **" + playlist.getName() + " by **random**." + state + languageA;
				}
				
				String reply = "BOT: " + language;
				response.editMessage(language).queue();
				System.out.println(reply);
				log(channel, event, reply);

				musicManager.scheduler.playAgainStringList.add(trackUr);
				musicManager.scheduler.menu = 4;
				musicManager.scheduler.recentAddPlayListCount = playlist.getTracks().size();
				
				int ended = 0;
				Collections.shuffle(playlist.getTracks());
				for(int i = 0; i<playlist.getTracks().size(); i++) {
					if(i == playlist.getTracks().size()-1) ended = 1;
					play(channel.getGuild(), msg, member, musicManager, playlist.getTracks().get(i), channel, event, 1, ended, lan);
				}
				
			}

			@Override 
			public void noMatches()
			{
				String language = "다시 시도해 보세요";
				if(lan.equals("eng")) {
					language = "Try again.";
				}
				
				String reply = "BOT: " + language;
				
				response.editMessage(reply).queue();
				System.out.println();
				log(channel, event, "BOT: " + reply);

			}

			@Override 
			public void loadFailed(FriendlyException exception) {
				String reply = ":no_entry_sign: **" + exception.getMessage() + "**"  + staticCause(exception);
				if(exception.getCause().toString().toLowerCase().contains("null"))
					reply = ":no_entry_sign: **" + exception.getMessage() + "** `(다른 검색어로 시도해 주세요)`";
				
				try {
					response.editMessage(reply).complete();
				}
				catch(Exception e) {
					channel.sendMessage(reply).queue();
				}
				System.out.println(reply);
				log(channel, event, reply);
				
				if(exception.getMessage().contains("Connecting")) return;
				
				/*
				if(exception.getMessage().contains("failed")) {
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + "> 재생이 안됩니다").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
				}
				*/
			}
		});
		});

		
	}
	
	public static void loadAndPlaySub(final TextChannel channel, Message msg, final String trackUrl, MessageReceivedEvent event, Message response, int i, int ended, String lan, String id)    // Plays the song
	{
		
		final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		Member member = event.getMember();
		
		try {
		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler()
		{
			@Override 
			public void trackLoaded(AudioTrack track) {
				
				if(musicManager.scheduler.terminate == 0) {
					musicManager.scheduler.loadCount = musicManager.scheduler.loadCount - 1;
					play(channel.getGuild(), msg, member, musicManager, track, channel, event, i, ended, null);
					musicManager.scheduler.recentAddPlayListCount = musicManager.scheduler.recentAddPlayListCount + 1;
				}
			}

			@Override 
			public void playlistLoaded(AudioPlaylist playlist)  {
				if (playlist.isSearchResult()) {
					if(response == null) {}
					else response.editMessage("잘못된 접근 방식입니다 (관리자 확인 필요)").queue();
					
					log(channel, event, "<@" + BotMusicListener.admin + "> 잘못된 접근 방식입니다 (관리자 확인 필요)");
					//play(channel.getGuild(), msg, member, musicManager, playlist.getTracks().get(0), channel, event, i, 0);
				}
				
				musicManager.scheduler.loadCount = musicManager.scheduler.loadCount - 1;
				
				if(playlist.getTracks().isEmpty()) {
					String language = "이 재생목록은 **비어있어요**\n(" + trackUrl + ")";
					if(lan.equals("eng"))
						language = "This playlist is **empty**.\n(" + trackUrl + ")";
					
					channel.sendMessage(language).queue();
					log(channel, event, "BOT: " + language);
					
					musicManager.scheduler.playAgainStringList.remove(trackUrl);
					
					if(id != null) {
						File file = new File(BotMusicListener.directoryDefault + "user/" + id + ".txt");
						
						if(musicManager.scheduler.playAgainPersonalIsPlaylistList.get(id) == null) {
							try {
								FileReader filereader = new FileReader(file);
					            BufferedReader bufReader  =  new BufferedReader(filereader);
					            
								List<String> uris = new ArrayList<>();
								
								 String line = "";
						         while((line = bufReader.readLine()) != null){
						        	 if(!line.equals(trackUrl))
						        		 uris.add(line);
						         }
						                     
						         bufReader.close();
						         
						         FileWriter fw = new FileWriter(file);
									
						         for(int k = 0; k<uris.size(); k++) {
						        	 fw.write(uris.get(k) + "\n");
						         }
						         fw.close();
							}
							catch(Exception e) {
								channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
							   	log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
							}
						}
						else {
							musicManager.scheduler.playAgainPersonalIsPlaylistList.get(id).remove(trackUrl);
							
							try {
								
								FileWriter fw = new FileWriter(file);
								
								for(int k = 0; k<musicManager.scheduler.playAgainPersonalIsPlaylistList.get(id).size(); k++) {
									fw.write(musicManager.scheduler.playAgainPersonalIsPlaylistList.get(id).get(k) + "\n");
								}
								fw.close();
							}
							catch(Exception e) {
								channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
							   	log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
							}
						}
					}
					return;
				}
				for(int k = 0; k<playlist.getTracks().size(); k++) {
					play(channel.getGuild(), msg, member, musicManager, playlist.getTracks().get(k), channel, event, 1, ended, null);
				}
				
				musicManager.scheduler.recentAddPlayListCount = musicManager.scheduler.recentAddPlayListCount + playlist.getTracks().size();
			}

			@Override public void noMatches()
			{
				String reply = "BOT: **항목을 불러올 수 없어요**";
				musicManager.scheduler.loadCount = musicManager.scheduler.loadCount - 1;
				musicManager.scheduler.playAgainStringList.remove(trackUrl);
				
				if(response == null) {}
				else {
					channel.sendMessage(reply + "\n(" + trackUrl + ")").queue();
				}
				
				System.out.println(reply);

				log(channel, event, reply);

			}

			@Override public void loadFailed(FriendlyException exception)
			{
				
				String reply = ":no_entry_sign: **" + exception.getMessage() + "**" + staticCause(exception) + "\n(" + trackUrl + ")";
				if(response == null) {}
				else {
					channel.sendMessage(reply).queue();
				}
				
				System.out.println(reply);
				log(channel, event, reply);
				
				if(exception.getMessage().contains("Connecting")) return;
				
				/*
				if(exception.getMessage().contains("failed")) {
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + "> 재생이 안됩니다").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
				}
				*/
				
				musicManager.scheduler.playAgainStringList.remove(trackUrl);
				musicManager.scheduler.loadCount = musicManager.scheduler.loadCount - 1;
			}
		});
		
		}
		catch(Exception e) {
			String reply = ":no_entry_sign: (loadItemOrdered) **" + e.getMessage() + "**" + staticCause(e);
			if(response == null) {}
			else response.editMessage(reply).queue();
			
			System.out.println(reply);
			
			log(channel, event, reply);
		}

	}

	public static void searchAndPlay(final TextChannel channel, Message msg, final String trackUrl, MessageReceivedEvent event, Member member, String lan)    // Plays the song
	{	
		final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		checkConnect(channel, msg, event, musicManager);
		
		VoiceChannel myChannel = member.getVoiceState().getChannel();
		VoiceChannel botChannel = event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
		
		if(musicManager.scheduler.isPlay == 1 && (myChannel != botChannel)) {
			
			String language = "**" + botChannel.getName() + "**에 들어간 후 시도하세요";
	
			if(lan.equals("eng")) {
				language = "Join **" + botChannel.getName() + "** and try again.";
			}
			
			channel.sendMessage(language).queue();
			playStandBy(channel, msg, event, trackUrl);
			
			log(channel, event, "BOT: " + language);
			return;
		}
		
		else if(myChannel == null) {
			
			String language = "먼저 음성채널에 들어가세요";
			
			if(lan.equals("eng")) {
				language = "Join the voice channel first.";
			}
			
			channel.sendMessage(language).queue();
    		System.out.println("BOT: " + language);
    		
    		log(channel, event, "BOT: " + language);
    		
    		if(trackUrl.length() > 1) {
    			MusicController.playStandBy(channel, msg, event, trackUrl);
    		}
    		return;
		}
		
		if(musicManager.scheduler.search == 1) {
			String language = "추가 중...";
			musicManager.scheduler.setLanguageStop = "kor";
			if(lan.equals("eng")) {
				language = "Adding...";
				musicManager.scheduler.setLanguageStop = "eng";
			}
			
			if(musicManager.scheduler.isIn == 0)
				musicManager.scheduler.playAgainStringList.clear();
			
			channel.sendMessage(language).queue(response -> {
				
				boolean isPaused = musicManager.player.isPaused();
		
				int a = Integer.parseInt(trackUrl) - 1;
						
				if(a > 7) {a = 6;}
				else if(a < 0) {a = 0;}
				
				String duration = " ``(" + secTo((int)musicManager.scheduler.listSearch.get(a).getDuration()) + ")``";
				if(musicManager.scheduler.listSearch.get(a).getInfo().isStream == true) {
					duration = " ``(생방송)``";
					if(lan.equals("eng"))
						duration = " ``(LIVE)``";
				}
					
				if (isPaused) {
					String languageAdd = "**" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "** 를 추가했어요 (일시정지됨)" + duration;
					
					if(lan.equals("eng")) {
						languageAdd = "Added **" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "**. (Paused)" + duration;
					}
					
					String reply = "BOT: " + languageAdd;
					response.editMessage(languageAdd).queue();
					System.out.println(reply);
					log(channel, event, reply);
				}
				else {
					
					String languageAdd = "**" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "** 를 추가했어요" + duration;
					
					if(lan.equals("eng")) {
						languageAdd = "Added **" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "**." + duration;
					}
					
					String reply = "BOT: " + languageAdd;
					response.editMessage(languageAdd).queue();
					System.out.println(reply);
					log(channel, event, reply);
				}
					
				musicManager.scheduler.playAgainStringList.add(musicManager.scheduler.listSearch.get(a).getInfo().uri);
				play(channel.getGuild(), msg, member, musicManager, musicManager.scheduler.listSearch.get(a), channel, event, 1, 1, lan);
						
				musicManager.scheduler.menu = 1;
				musicManager.scheduler.menuStr = musicManager.scheduler.listSearch.get(a).getInfo().uri;
			});
		}

	}

	public static void search(TextChannel channel, Message msg, String query, MessageReceivedEvent event, Message response, String lan)    // search the song
	{

		if(!query.startsWith("https://")) {
			query = "ytsearch:" + query;
			
		}
    	
		final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.playStandByInt = 0;
		
		String quer = query;
		
		playerManager.loadItemOrdered(musicManager, query, new AudioLoadResultHandler()
		{
			@Override 
			public void trackLoaded(AudioTrack track) {
				
				EmbedBuilder eb = new EmbedBuilder();
				String languageTitle = "트랙 정보";
				String languageUploder = "올린 이";
				String languageTime = "시간";
				String languageSource = "소스";
				
				if(lan.equals("eng")) {
					languageTitle = "Track info";
					languageUploder = "Uploder";
					languageSource = "Source";
				}
				
				eb.setTitle(languageTitle);
				eb.setColor(Color.decode(BotMusicListener.colorDefault));
				eb.setDescription(track.getInfo().title);
				eb.setThumbnail("https://img.youtube.com/vi/" + track.getIdentifier() + "/mqdefault.jpg");
				eb.addField(languageUploder, track.getInfo().author, true);
				eb.addField(languageTime, musicManager.scheduler.fn.secTo((int)track.getDuration(), 1), true);
				eb.addField(languageSource, track.getSourceManager().getSourceName(), false);
				
				response.editMessage(eb.build()).queue();
				
				musicManager.scheduler.playStandByInt = 1;
				musicManager.scheduler.playStandBy = quer;
			}

			@Override 
			public void playlistLoaded(AudioPlaylist playlist)  {
				if (playlist.isSearchResult()) {
					
					musicManager.scheduler.listSearch.clear();
					
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(Color.decode(BotMusicListener.colorDefault));
					
					String languageTitle = ":mag: '" + quer.substring(BotMusicListener.queryCount) + "' 유튜브 검색 결과";
					if(lan.equals("eng")) languageTitle = ":mag: Search results for '" + quer.substring(BotMusicListener.queryCount) + "' from Youtube";
					
					eb.setTitle(languageTitle, "https://www.youtube.com/results?search_query=" + quer.substring(BotMusicListener.queryCount).replaceAll(" ", "+"));
					
					StringBuilder sb = new StringBuilder();
			        	
					for (int i = 0; i < 7 && i < playlist.getTracks().size(); i++) {

						AudioTrack track = playlist.getTracks().get(i);
						musicManager.scheduler.listSearch.add(track);
							
						String num = "";
							
						if(i == 0) num = ":one:";
						else if(i == 1) num = ":two:";
						else if(i == 2) num = ":three:";
						else if(i == 3) num = ":four:";
						else if(i == 4) num = ":five:";
						else if(i == 5) num = ":six:";
						else if(i == 6) num = ":seven:";
						
						String duration = " `[" + secTo((int)track.getDuration()) + "]`";
						if(track.getInfo().isStream == true) {
							duration = " `[생방송]`";
							if(lan.equals("eng")) {
								duration = " `[LIVE]`";
							}
						}
							
						sb.append(num + duration + " **" + realTitle(track.getInfo().title) + "**\n");
					}
					
					eb.setDescription(sb);
			            
					response.editMessage(eb.build()).queue();
			        System.out.println("BOT: \n" + sb);
			        log(channel, event, "BOT: \n" + sb);
			      
	
					musicManager.scheduler.search = 1;
					musicManager.scheduler.menu = 7;
					return;
				}
				
				EmbedBuilder eb = new EmbedBuilder();
				String languageTitle = "트랙 정보";
				String languageSize = "항목 수";
				String languageSource = "소스";
				
				if(lan.equals("eng")) {
					languageTitle = "Playlist info";
					languageSize = "Size";
					languageSource = "Source";
				}
				eb.setTitle(languageTitle);
				eb.setColor(Color.decode(BotMusicListener.colorDefault));
				eb.setDescription(playlist.getName());
				eb.setThumbnail("https://img.youtube.com/vi/" + playlist.getTracks().get(0).getIdentifier() + "/mqdefault.jpg");
				eb.addField(languageSize, playlist.getTracks().size() + "", false);
				eb.addField(languageSource, playlist.getTracks().get(0).getSourceManager().getSourceName() + "", false);
				
				response.editMessage(eb.build()).queue();
				
				musicManager.scheduler.playStandByInt = 1;
				musicManager.scheduler.playStandBy = quer;
			}

			@Override public void noMatches()
			{
				String language = "다시 시도하세요\n`" + quer.substring(BotMusicListener.queryCount) + "`";
				if(lan.equals("eng"))
					language = "Try again.\n`" + quer.substring(BotMusicListener.queryCount) + "`";
				
				String reply = "BOT: " + language;
				
				
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(Color.decode(BotMusicListener.colorDefault));
				eb.setTitle(":x: " + language);
					
				response.editMessage(eb.build()).queue();	
				
				
				System.out.println(reply);
				log(channel, event, reply);
				
				musicManager.scheduler.playStandByInt = 2;
				musicManager.scheduler.playStandBy = quer;
				
				
			}

			@Override public void loadFailed(FriendlyException exception)
			{
				String reply = ":no_entry_sign: **" + exception.getMessage() + "**" + staticCause(exception);
				if(exception.getCause().toString().toLowerCase().contains("null"))
					reply = ":no_entry_sign: **" + exception.getMessage() + "** `(다른 검색어로 시도해 주세요)`";
				
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(Color.decode(BotMusicListener.colorDefault));
				eb.setTitle(reply);
					
				response.editMessage(eb.build()).queue();	
				
				
				System.out.println("BOT: " + reply);
				log(channel, event, "BOT: " + reply);
			}
		});
	}
	
	
	private static void play(Guild guild, Message msg, Member member, GuildMusicManager musicManager, AudioTrack track, TextChannel channel, MessageReceivedEvent event, int i, int ended, String lan)
	{
		
		if(musicManager.scheduler.isIn == 0) {
			musicManager.scheduler.isIn = 1;
			musicManager.scheduler.enteredTime = System.currentTimeMillis();
			
			connectToVoiceChannel(channel, msg, member, guild.getAudioManager(), event, lan);
			guild.getAudioManager().setAutoReconnect(false);
		
		}
	
		musicManager.scheduler.queue(channel, member, event, track, i, 0, ended);
	}
	
	public static void playAgain(TextChannel channel, Message msg, Member member, MessageReceivedEvent event, String id, int menu, String lan)
	{
		Guild guild = channel.getGuild();
		member = event.getMember();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);

		if(menu == 0) {
			if(musicManager.scheduler.isIn == 0) {
			
				musicManager.scheduler.enteredTime = System.currentTimeMillis();
				if(menu == 0) {
					musicManager.scheduler.queueAgain(channel, msg, lan);
					musicManager.scheduler.playAgainEdited = 0;
				}
				
				else {
					musicManager.scheduler.queueAgainPersonal(channel, msg, id, lan, false);
				}
			}
			else {
				musicManager.scheduler.userPlayAgainId = musicManager.scheduler.fn.autoRemoveMessage(channel, msg, musicManager.scheduler.userPlayAgainId, musicManager.scheduler.botPlayAgainId);
				
				String reply = "BOT: 아직 음성채널에 있어요";
				channel.sendMessage("아직 음성채널에 있어요").queue(response -> {
					musicManager.scheduler.botPlayAgainId = response.getId();
				});
				System.out.println(reply);
				log(channel, event, reply);
				
			}
		}
		
		else {
			musicManager.scheduler.queueAgainPersonal(channel, msg, id, lan, false);
		}
	}

	public static void connectToVoiceChannel(TextChannel channel, Message msg, Member member, AudioManager audioManager, MessageReceivedEvent event, String lan) {  // Connects to the voice channel
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		try {
			VoiceChannel myChannel = member.getVoiceState().getChannel();    //Connect the the voice channel the user is in	
			if(myChannel != null) {
				audioManager.openAudioConnection(myChannel);
				isInTotal = isInTotal + 1;
				
				String reply = "BOT: 음성채널에 들어오지 않아 " + myChannel.getId() + " ``(" + myChannel.getName() + ")`` 로 들어왔어요";
				System.out.println(reply);
				log(channel, event, reply);
					
				leaveTc = channel;
				leaveMsg = msg;
				leaveEvent = event;
				
				String nowVol = "현재볼륨";
				if(musicManager.scheduler.setLanguageStop.equals("eng"))
					nowVol = "Now volume";
				
				String volume = ":sound: " + nowVol + ": " + musicManager.scheduler.player.getVolume();
				if(musicManager.scheduler.player.getVolume() < 12)
					volume = ":speaker: " + nowVol + ": " + musicManager.scheduler.player.getVolume();
				else if(musicManager.scheduler.player.getVolume() > 24)
					volume = ":loud_sound: " + nowVol + ": " + musicManager.scheduler.player.getVolume();
					
				if(!member.getId().equals(BotMusicListener.admin))
					log(channel, event, "BOT: `(" + channel.getGuild().getName() + ")` " + volume);
				
				musicManager.scheduler.fn.removeMessage(channel, musicManager.scheduler.userVolumeMessageId, musicManager.scheduler.botVolumeMessageId);
				
				if(channel.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_ADD_REACTION)) {
					channel.sendMessage(volume).queueAfter(150, TimeUnit.MILLISECONDS, response -> {
						musicManager.scheduler.setVolumeMessageId = response.getId();
						
						Runnable reaction = () -> {
							try {
								response.addReaction("U+1f53d").complete();
								response.addReaction("U+1f53c").complete();
							}
							catch(Exception e) {
								try {
									response.delete().complete();
								}
								catch(Exception f) {}
							}
						};
						Thread t = new Thread(reaction);
						t.start();
						
					});
		        }
			}
		
		
			Runnable write1 = () -> {
				
				File file = new File(BotMusicListener.directoryDefault + "guild/removeVoiceStateMessages.txt");
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(String.valueOf(isInTotal));
					fw.close();
				}
				catch (Exception e) {
					e.printStackTrace();
					channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
				}
			};
			
			Runnable write2 = () -> {
				File file = new File(BotMusicListener.directoryDefault + "guild/usingGuilds.txt");
				try {
					FileWriter fw = new FileWriter(file, true);
					if(file.length() <= 1)
						fw.append(channel.getGuild().getId() + "/" + channel.getId());
					else
						fw.append("\n" + channel.getGuild().getId() + "/" + channel.getId());
					
					fw.close();
				}
				catch (Exception e) {
					e.printStackTrace();
					channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
					log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
				}
			};
			
			Thread t1 = new Thread(write1);
			Thread t2 = new Thread(write2);
			t1.start();
			t2.start();
				
			editguilds(channel, msg, event);
		}
		catch(Exception e) {
			channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
			log(channel, event, ":no_entry_sign: **" + e.getMessage() + "** "+ staticCause(e));
			
			if(Main.saveQueueAllowGuild.contains(channel.getGuild().getId())) {
				MusicController.stopPlaying(channel, msg, event, 6, musicManager.scheduler.save, musicManager.scheduler.setLanguageStop);
	        }
    		else
    			MusicController.stopPlaying(channel, msg, event, 6, 0, musicManager.scheduler.setLanguageStop);
		}

	}
	
	public static void queryNull(TextChannel channel, Message msg, MessageReceivedEvent event, Member member, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(musicManager.scheduler.playStandByInt == 1) { 
			if(musicManager.scheduler.playStandBy.startsWith("https://")) {
				MusicController.loadAndPlay(channel, msg, null, musicManager.scheduler.playStandBy, event, member, lan, 0);
			}
			else {
				MusicController.loadAndPlay(channel, msg, null, musicManager.scheduler.playStandBy, event, member, lan, 0);
				
			}
			
			MusicController.repeat(channel, msg, event);
    		
    		return;
		}
		
		boolean isPaused = musicManager.player.isPaused();
		
		if(isPaused) {
			resume(channel, event, member, lan, 1);
		}
		else {
			String language = "올바른 URL을 입력하세요";
			
			if(lan.equals("eng")) {
				language = "Please input a valid URL.";
			}
			
			String reply = "BOT: " + language;
			channel.sendMessage(language).queue();
	        System.out.println(reply);
	        
			log(channel, event, reply);
		}

	}
	
	public static void queryNullSearch(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(musicManager.scheduler.playStandByInt == 2) { 
			
			String query = musicManager.scheduler.playStandBy;
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.decode(BotMusicListener.colorDefault));
			
			String langu = ":mag: `" + query.substring(BotMusicListener.queryCount) + "` 검색 중...";
    		if(lan.equals("eng"))
    			langu = ":mag: Searching `" + query.substring(BotMusicListener.queryCount) + "`...";
    		
			eb.setTitle(langu);
			
			String langua = lan;
			
        	channel.sendMessage(eb.build()).queue(response -> {
	        	
        		MusicController.search(channel, msg, query, event, response, langua);
	        });
        	
			MusicController.repeat(channel, msg, event);
    		
    		return;
		}
		
		String langu = "올바른 단어를 입력해주세요";
		if(lan.equals("eng"))
			langu = "Please input a valid query.";
			
	    channel.sendMessage(langu).queue();
	    System.out.println("BOT: " + langu);
	        
		log(channel, event, "BOT: " + langu);
	}
	
	public static void skipTrack(TextChannel channel, Message msg, MessageReceivedEvent event, int skipto, String lan) { // Skip the current track and play the next one
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.nextTrack(channel, msg, event, 1, skipto, lan);
	}
	
	public static void randomNextTrack(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) { // Skip the current track and play the next one
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(musicManager.scheduler.playStandByInt == 1) {
			if(musicManager.scheduler.playStandBy.startsWith("https://")) {
				MusicController.loadAndPlayRandom(channel, msg, musicManager.scheduler.playStandBy, event, lan);
			}
			else {
				String language = "재생목록 URL만 가능해요";
				if(lan.equals("eng"))
					language = "Only URL links to playlist possible.";
					
				channel.sendMessage(language).queue();
				log(channel, event, "BOT: " + language);
			}
			
			MusicController.repeat(channel, msg, event);
    		musicManager.scheduler.playStandByInt = 0;
	
		}
		else {
			if(musicManager.scheduler.queue.isEmpty()) {
				String language = "올바른 URL을 입력하세요";
				if(lan.equals("eng"))
					language = "Please input a valid URL.";
				
				channel.sendMessage(language).queue();
				log(channel, event, "BOT: " + language);
				
				return;
			}
			
			Random r = new Random();
			int skipto = r.nextInt(musicManager.scheduler.queue.size() + 1);
			musicManager.scheduler.nextTrack(channel, msg, event, 1, skipto, lan);
		}
	}
	
	public static void now(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.nowInfo(channel, msg, event, lan);	
	}
	
	public static void savedlist(TextChannel channel, Message msg, MessageReceivedEvent event, int page, int fn, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(fn == 2) {
			int in = ((int)(musicManager.scheduler.current + 1) / 10) + 1;
    		page = in;
		}
		
		musicManager.scheduler.savedlist(channel, msg, page, lan);
	}
	
	public static void savePersonalPlaylist(TextChannel channel, Message msg, MessageReceivedEvent event, String id, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.savePersonalPlaylist(channel, msg, event, id, lan);
	}
	
	public static void stopPlaying(TextChannel channel, Message msg, MessageReceivedEvent event, int i, int save, String lan) { // Reset everything and disconnect
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		channel.getGuild().getAudioManager().setSelfMuted(false);
		
		if(musicManager.scheduler.isIn == 0 && musicManager.scheduler.isPlay == 0) {
			String language = "이미 나와있어요";
			if(lan.equals("eng"))
				language = "Already left from the voice channel";
			
			try {
				channel.sendMessage(language).complete();
			}
			catch(Exception e) {}
			String reply = "BOT: " + language;
			System.out.println(reply);
			log(channel, event, reply);
		}
		
		else {

			if(i == 7) {
				VoiceChannel myChannel = musicManager.scheduler.member.getVoiceState().getChannel(); 
				if(myChannel != null) {
					musicManager.scheduler.recoveredNetwork(musicManager.scheduler.tc);
					return;
				}
			}
			musicManager.scheduler.terminate = 1;
			
			String language = ":clipboard: `처리 중...`";
			if(lan.equals("eng"))
				language = ":clipboard: `Processing...`";
			
	
			channel.sendMessage(language).queue(response -> {

				
				if(save == 0) musicManager.scheduler.save = 0;
				
				musicManager.scheduler.clearQueue(channel, msg, response, guild, event, i, musicManager.scheduler.save, lan);
		
				Runnable remove1 = () -> {
					File file = new File(BotMusicListener.directoryDefault + "guild/removeVoiceStateMessages.txt");
					try {
						FileWriter fw = new FileWriter(file);
						fw.write(String.valueOf(isInTotal));
						fw.close();
					}
					catch (Exception e) {
						e.printStackTrace();
						response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
						log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
					}
				};
				
				Runnable remove2 = () -> {
					
					StringBuilder sb = new StringBuilder();
					File file = new File(BotMusicListener.directoryDefault + "guild/usingGuilds.txt");
					
					try{   
			            FileReader filereader = new FileReader(file);
			            BufferedReader bufReader  =  new BufferedReader(filereader);
		
			            String line = "";
			            while((line = bufReader.readLine()) != null){
			            	if(line.contains(channel.getGuild().getId())) {}
			            	else {
			            		if(sb.length() == 0) sb.append(line);
			            		else sb.append("\n" + line);
			            	}
			            
			            }
			            //.readLine()은 끝에 개행문자를 읽지 않는다.            
			            bufReader.close();
			            
			            FileWriter fw = new FileWriter(file);
						fw.write(sb.toString());
						fw.close();
			        }
					catch(Exception e){
			            System.out.println(e);
			            response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
		   				log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
			        }
	
				};
					
				Thread rt1 = new Thread(remove1);
				rt1.start();
				Thread rt2 = new Thread(remove2);
				rt2.start();

			});
			
			if (guild.getAudioManager().isConnected() || musicManager.scheduler.isPlay == 1) {
				guild.getAudioManager().closeAudioConnection();  
				isInTotal = isInTotal - 1;
				if(isInTotal < 0) isInTotal = 0;
				
			}
			editguilds(channel, msg, event);
			
			BotMusicListener.update(guild);
			
			/*
			if(isInTotal == 0) {
				if(gunBamStateRun == 1) {
					gunBamStateTimer.cancel(); 
					gunBamStateTimer = null;
					gunBamStateRun = 0;		
				}
			}
			*/

		}

	}
	
	public static void last(TextChannel channel, Message msg, User user, MessageReceivedEvent event, int i, int last, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);

		musicManager.scheduler.last(channel, msg, event, user, i, last, lan);
         
	}

	public static void shuffle(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);

		musicManager.scheduler.shuffle(channel, msg, event, lan, false);
	}
	
	public static void cancel(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);

		musicManager.scheduler.cancel(channel, msg, event, lan);  
	}
	
	public static void clear(TextChannel channel, List<Message> msgs, Message msg, MessageReceivedEvent event, int co) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.clearMessage(channel, msgs, msg, co);
	}
	
	public static void nowplay(TextChannel channel, Message msg, MessageReceivedEvent event, int i, int fn, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		
		if(fn == 2) {
			int in = ((int)(musicManager.scheduler.current + 1) / 10) + 1;
			
			i = in;
		}
		
        musicManager.scheduler.nowplay(channel, msg, event, i, lan);  
	}
	
	public static void repeat(TextChannel channel, Message msg, MessageReceivedEvent event) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		
        musicManager.scheduler.repeat(channel, msg, event);
	}
	
	public static void removeManyTrack(List<Integer> n, TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
	
        musicManager.scheduler.removeMany(n, channel, msg, event, lan, false); 
	}

	public static void removeTrack(String s, TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		
        musicManager.scheduler.remove(s, channel, msg, event, 1, lan, false);
	}
	
	public static void removeTrackTitle(List<String> s, TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		
        musicManager.scheduler.removeTitle(s, channel, msg, event, lan, false);
	}
	
	public static void volume(TextChannel channel, int volume, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		musicManager.scheduler.setVolume(channel, event, volume, 1, lan);
	}
	
	public static void nowVolume(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
	
		musicManager.scheduler.nowVolume(channel, event, msg, lan);
	}
	
	public static void timer(TextChannel channel, Message msg, User user, int time, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		
		if(musicManager.scheduler.isPlay == 0) {
			String reply = "재생중인 항목이 있어야 해요";
			channel.sendMessage("재생중인 항목이 있어야 해요").queue();
			System.out.println(reply);
			log(channel, event, reply);
			return;
		}
		
		musicManager.scheduler.setTimer(channel, msg, user, event, time, lan);
	}
	
	public static void nowTimer(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
	
		musicManager.scheduler.nowTimer(channel, msg, lan);
	}
	
	public static void timerCancel(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
	
		musicManager.scheduler.timerCancel(channel, msg, lan);
	}
	
	public static void resume(TextChannel channel, MessageReceivedEvent event, Member member, String lan, int send) { // Resume music that was paused
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(send == 1) {
			if(musicManager.scheduler.isPlay == 1) {
				VoiceChannel myChannel = member.getVoiceState().getChannel();
				VoiceChannel botChannel = event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
				
				if(musicManager.scheduler.isPlay == 1 && (myChannel != botChannel)) {
					String languageJoin = "**" + botChannel.getName() + "**에 들어간 후 시도하세요";
					
					if(lan.equals("eng")) {
						languageJoin = "Join **" + botChannel.getName() + "** and try again.";
					}
					
					channel.sendMessage(languageJoin).queue();
					
					log(channel, event, "BOT: " + languageJoin);
					return;
				}
			}
			
			else {
				String languageJoin = "재생중인 항목이 있어야 해요";
				
				if(lan.equals("eng")) {
					languageJoin = "There is no item being played.";
				}
				
				channel.sendMessage(languageJoin).queue();
				
				log(channel, event, "BOT: " + languageJoin);
				return;
			}
		}
		
		boolean isPaused = musicManager.player.isPaused();

		if(isPaused) {
			musicManager.player.setPaused(false);
			channel.getGuild().getAudioManager().setSelfMuted(false);
			musicManager.scheduler.pausedTime(channel, msg, lan, false);
			
			if(send == 0)
				musicManager.player.getPlayingTrack().setPosition(musicManager.player.getPlayingTrack().getPosition() - 3000);
			
			musicManager.scheduler.isIn = 1;
				
			if(send == 1) {
				String language = "플레이어를 재생상태로 설정해요";
	        	if(lan.equals("eng")) language = "It will play this player.";
	        		
				channel.sendMessage(language).queue();
					
				String reply = "BOT: " + language;
				System.out.println();
				log(channel, event, reply);
				
				musicManager.scheduler.voiceStats(musicManager.player);
			}
				
			editguilds(channel, msg, event);
		}
		else {
			if(send == 1) {
				String language = "이미 재생상태로 설정되어있어요";
	        	if(lan.equals("eng")) language = "Already playing  in this server.";
	        		
				channel.sendMessage(language).queue();
				String reply = "BOT: " + language;
				System.out.println(reply);
				log(channel, event, reply);
			}
		}
	}
	
	public static void pause(TextChannel channel, MessageReceivedEvent event, Member member, String lan, int send) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(send == 1) {
			if(musicManager.scheduler.isPlay == 1) {
				VoiceChannel myChannel = member.getVoiceState().getChannel();
				VoiceChannel botChannel = event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
				
				if(musicManager.scheduler.isPlay == 1 && (myChannel != botChannel)) {
					String languageJoin = "**" + botChannel.getName() + "**에 들어간 후 시도하세요";
					
					if(lan.equals("eng")) {
						languageJoin = "Join **" + botChannel.getName() + "** and try again.";
					}
					
					channel.sendMessage(languageJoin).queue();
					
					log(channel, event, "BOT: " + languageJoin);
					
					return;
				}
			}
			else {
				String languageJoin = "재생중인 항목이 있어야 해요";
				
				if(lan.equals("eng")) {
					languageJoin = "There is no item being played.";
				}
				
				channel.sendMessage(languageJoin).queue();
				
				log(channel, event, "BOT: " + languageJoin);
				
				return;
			}
		}
		
		boolean isPaused = musicManager.player.isPaused();

		
		if(!isPaused) {
			musicManager.player.setPaused(true);
			channel.getGuild().getAudioManager().setSelfMuted(true);
			musicManager.scheduler.isIn = 2;
			musicManager.scheduler.pausedTime(channel, msg, lan, true);
			
			if(send == 1) {
				String language = "플레이어를 일시정지했어요";
				if(lan.equals("eng")) language = "Paused this player.";
			
				channel.sendMessage(language).queue();
				String reply = "BOT: " + language;
				System.out.println(reply);	
				log(channel, event, reply);
				
				musicManager.scheduler.voiceStats(musicManager.player);
			}
			
			editguilds(channel, msg, event);
		}
		else {
			String language = "이미 일시정지했어요";
			if(lan.equals("eng")) language = "Already paused this player.";
			
			channel.sendMessage(language).queue();
			String reply = "BOT: " + language;
			System.out.println(reply);
			log(channel, event, reply);
		}

	}
	
	//(manyPeople)
	public static void lock(TextChannel channel, MessageReceivedEvent event, int val) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		musicManager.scheduler.lock = val;
	}
	
	public static void save(TextChannel channel, MessageReceivedEvent event, int val) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		if(val == 2) {
			channel.sendMessage("saveDefault: " + musicManager.scheduler.save).queue();
        	System.out.println("BOT: saveDefault: " + musicManager.scheduler.save);

		}
		else {
			musicManager.scheduler.save = val;
		}
	}
	
	public static void alertTc(TextChannel channel, MessageReceivedEvent event) {
		Guild guild = event.getGuild();

		alertsList.put(guild, channel);

	}
	
	public static void guilds(TextChannel channel, Message msg, MessageReceivedEvent event) {
			
		Runnable r1 = () -> {
			try {
				BotMusicListener.adtc.deleteMessageById(adminGuildsId).complete();
			}
	    	catch(Exception e) {}
				
			adminGuildsId = msg.getId();
		};

		Runnable r2 = () -> {
			try {
				BotMusicListener.adtc.deleteMessageById(botGuildsId).complete();
			}
	    	catch(Exception e) {}
		};
		
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		t1.start();
		t2.start();
		
		BotMusicListener.adtc.sendMessage("로드 중...").queue(response -> {
			botGuildsId = response.getId();
			botGunBamState(channel, msg, event, response, 0);
		});
			
	}
	
	public static void editguilds(TextChannel channel, Message msg, MessageReceivedEvent event) {
		botGunBamState(channel, msg, event, null, 1);
	}
	
	public static void channel(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.channel(channel, msg, lan);
	}
	
	public static void prepare(TextChannel channel, Message msg, MessageReceivedEvent event) {
		
		listg = new ArrayList<>();
		listg.clear();
		if(event == null) {}
		else listg.addAll(event.getJDA().getGuilds());
		
		Runnable load1 = () -> {
			File file2 = new File(BotMusicListener.directoryDefault + "newsPrefix.txt");
			
			try {
				FileReader filereader2 = new FileReader(file2);
		       
		        BufferedReader bufReader2 = new BufferedReader(filereader2);
		        String line2 = "";
		        while((line2 = bufReader2.readLine()) != null){
		        	 BotMusicListener.prefix = line2;
		        }
		               
		        bufReader2.close();
			}
			catch(Exception e) {
				channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
			}
		};

		Thread t1 = new Thread(load1);
		t1.start();
		
	}
	
	public static void memoSave(TextChannel channel, Message msg, MessageReceivedEvent event, Message response) {
		GuildMusicManager musicManager;
		
		StringBuilder s = new StringBuilder();
		s.append("**" + listg.size() + "개** 서버에서 활동 중이에요\n```css\n");
				
		for(int i = 0; i<listg.size(); i++) {
			s.append(listg.get(i).toString() + "\n");

		}

		s.append("```\n**" + (int)(alertsList.size() - 1) + "개** 채널에 공지할 준비가 되었어요\n```css\n");

		Iterator<Guild> it = alertsList.keySet().iterator();
				
		while(it.hasNext()) {
			Guild g = it.next();
			TextChannel value = alertsList.get(g);
					
			if(g.toString().contains(BotMusicListener.base)) s.append(g.toString() + ": [" + value.toString() + "]\n");
			else s.append(g.toString() + ": " + value.toString() + "\n");
						
		}
				
			
		s.append("```\n**" + (int)(isInTotal) + "개** 음성채널에 군밤이 있어요\n```css\n");
		Iterator<Guild> it2 = alertsList.keySet().iterator();
				
		while(it2.hasNext()) {
			Guild g = it2.next();
					
			musicManager = getGuildAudioPlayer(g, channel, msg, event);
					
			if(musicManager.scheduler.isIn == 1||musicManager.scheduler.isIn == 2) {
				s.append(g + ": " + musicManager.scheduler.isIn + " (" + ((long)(System.currentTimeMillis() - musicManager.scheduler.enteredTime)/1000)/3600 + " 시간)\n");
			}
			
			else s.append(g + ": " + musicManager.scheduler.isIn + "\n");	
		}
		
		s.append("```");
				
				
		File file2 = new File(BotMusicListener.directoryDefault + "상황실저장.txt");
		try {
			FileWriter fw = new FileWriter(file2);
			fw.write(s.toString());
			fw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
		}
				
		if(response == null) {}
		else response.editMessage("**상황실.txt** 에 저장했어요").queue();
		
	}
	
	public static void botGunBamState(TextChannel channel, Message msg, MessageReceivedEvent event, Message response, int val) {

		StringBuilder s = new StringBuilder();

		prepare(channel, msg, event);
		
		s.append("**" + listg.size() + "개** 서버에서 활동 중이에요\n");
		s.append("**" + (int)(isInTotal) + "개** 음성채널에 군밤이 있어요\n");
		
		if(val == 0) {
			response.editMessage(s).queue();
			
			if(gunBamStateRun == 1) {
				gunBamStateTimer.cancel(); 
				gunBamStateTimer = null;
				gunBamStateRun = 0;		
			}
			
			gunBamStateTimer = new Timer();
			gunBamStateRun = 1;
			
			updateGuild(channel, msg, event);
		
		}
		else {
			Runnable edit = () -> {
				try {
					BotMusicListener.adtc.editMessageById(botGuildsId, s).complete();
					MusicController.memoSave(channel, msg, event, null);
				}
				catch(Exception e) {
					/*
					if(gunBamStateRun == 1) {
						gunBamStateRun = 0;
						gunBamStateTimer.cancel();
						gunBamStateTimer = null;
					}
					*/
					//BotMusicListener.adtc.sendMessage(":warning: **" + e.getMessage() + "**" + staticCause(e)).queue();
							
				}
				
			};
            Thread t1 = new Thread(edit);
            t1.start();

			if(isInTotal == 0) {
				
				/*
				if(gunBamStateRun == 1) {
					gunBamStateRun = 0;
					gunBamStateTimer.cancel();
					gunBamStateTimer = null;
				}
				*/
				
			}
	
		}
		
	}
	
	public static void updateGunBam(TextChannel channel, Message response) {
		Runtime rt = Runtime.getRuntime();
		String exeFile = BotMusicListener.directoryDefault + "bat/compileJavaProjectsGunBam.bat";
		String exeFile2 = BotMusicListener.directoryDefault + "bat/runGunBam.bat";
		
		Process p; 
		try {
		    p = rt.exec(exeFile);
		    p.waitFor();
		    rt.exec(exeFile2);
			System.exit(0);
		} catch (Exception e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw)); 
			String exceptionAsString = sw.toString();

		    e.printStackTrace();
		    try {
		    	response.editMessage("에러 발생: **" + exceptionAsString + "**").complete();
		    }
		    catch(Exception f) {
		    	channel.sendMessage("에러 발생: **" + exceptionAsString + "**").queue();
		    }
		   
		}
		
	}
	
	public static void updateGuild(TextChannel channel, Message msg, MessageReceivedEvent event) {
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	editguilds(channel, msg, event);
            	
            	Runnable r = () -> {
            		memoSave(channel, msg, event, null);
            	};
            	Thread t1 = new Thread(r);
            	t1.start();
            }
				
        };
        
        gunBamStateTimer.scheduleAtFixedRate(task, 1800000, 1800000);
	}
	
	public static void fixGuilds(TextChannel channel, Message msg, MessageReceivedEvent event, Message response) {
		try {
			Iterator<Guild> it = alertsList.keySet().iterator();
			isInTotal = 0;
			GuildMusicManager musicManager;
			while(it.hasNext()) {
				Guild g = it.next();
				
				musicManager = getGuildAudioPlayer(g, channel, msg, event);
				
				AudioManager manager = g.getAudioManager();
				
				if(manager.isConnected()) {
					isInTotal = isInTotal + 1;
				}
				else {
					if(musicManager.scheduler.isIn != 0 || musicManager.scheduler.isPlay == 1) {
						
						if(Main.saveQueueAllowGuild.contains(channel.getGuild().getId())) {
	            			MusicController.stopPlaying(channel, msg, event, 3, musicManager.scheduler.save, musicManager.scheduler.setLanguageStop);
	                    }
	                    else {
	                    	MusicController.stopPlaying(channel, msg, event, 3, 0, musicManager.scheduler.setLanguageStop);
	                    }
						
					}
				}
			}
			
			Runnable r = () -> {
				File file = new File(BotMusicListener.directoryDefault + "guild/removeVoiceStateMessages.txt");
				try {
				    FileWriter fw = new FileWriter(file);
				    fw.write(String.valueOf(isInTotal));
				    fw.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				    channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
				}
			};
			Thread t = new Thread(r);
			t.start();
	
			editguilds(channel, msg, event);
	
			if(response == null) {}
			else response.editMessage("수정했어요").queue();
		}
		catch(Exception e) {
		
			if(response == null) {channel.sendMessage("다시 시도하세요").queue();}
			else response.editMessage("다시 시도하세요").queue();
			
		}
	}
	
	public static void saveGuilds(TextChannel channel, MessageReceivedEvent event) {
		File file = new File(BotMusicListener.directoryDefault + "guild/guildListSave.txt");
		Iterator<Guild> it = alertsList.keySet().iterator();
		try {
			FileWriter fw = new FileWriter(file);
		      
		    for(int k = 0; k<alertsList.size(); k++) {
		    	Guild g = it.next();
				TextChannel value = alertsList.get(g);
				if(g.toString().contains(BotMusicListener.base)) {}
				else
					fw.write(g.toString() + ": " + value.toString() + "\n");
		    	 
		    }
		      
		    fw.close();

		} 
		catch (Exception e) {
		    e.printStackTrace();
		    channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
		}
		
		channel.sendMessage("목록을 저장했습니다 (크기: " + alertsList.size() + ")").queue();
	}
	
	public static void disconnect(int code, DisconnectEvent event) {
		
		BotMusicListener.disconnectedCode = code;
		BotMusicListener.disconnectedReasonEvent = event;
	}
	
	public static void reconnect() {
		
		try {
			Iterator<Guild> it = alertsList.keySet().iterator();
			
			GuildMusicManager musicManager;
			
			while(it.hasNext()) {
				Guild g = it.next();
				musicManager = getGuildAudioPlayer(g, alertsList.get(g), msg, null);
	
				if(BotMusicListener.disconnectedCode == 1008||BotMusicListener.disconnectedCode == 1000) {
					
					if(musicManager.scheduler.waitingToReconnect == 1) {
						musicManager.scheduler.tc.sendMessage(":page_with_curl: **" + BotMusicListener.disconnectedReasonEvent.getClientCloseFrame().getCloseReason() + "**").queue();
						if(Main.saveQueueAllowGuild.contains(g.getId())) {
							stopPlaying(musicManager.scheduler.tc, musicManager.scheduler.msg, null, 7, musicManager.scheduler.save, musicManager.scheduler.setLanguageStop);
							
			            }	
			            else {
			                stopPlaying(musicManager.scheduler.tc, musicManager.scheduler.msg, null, 7, 0, musicManager.scheduler.setLanguageStop);
			                BotMusicListener.update(g);
							
			            }

					}
					
				}
				
			}
		}
		catch(Exception e) {}

		BotMusicListener.disconnectedCode = 0;
	}

	public static void alert(TextChannel channel, MessageReceivedEvent event) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
	
		musicManager.scheduler.alert(channel);
	}
	
	public static void ping(Guild guild, TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);

		musicManager.scheduler.ping(guild, channel, msg, event, lan);
		
	}
	
	public static void moveVoiceChannel(TextChannel channel, Message msg, MessageReceivedEvent event, Guild guild, String[] args, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		leaveTc = channel;
		leaveMsg = msg;
		leaveEvent = event;
		musicManager.scheduler.tc = channel;
		musicManager.scheduler.msg = msg;
		musicManager.scheduler.event = event;
		
		Member member = null;
		Member memberBot = null;
		VoiceChannel myChannel = null;
		
		
		String soWhich = "";
    	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
    	String getCommand = args[0].toLowerCase();
    	
    	int substring = 0;
    	
        if(getCommand.startsWith("입장")||getCommand.startsWith("이동")||getCommand.startsWith("mv")) {
        	if(args[0].length() > 2)
        		substring = 2;
        }
        else if(getCommand.startsWith("들어가기")||getCommand.startsWith("move")) {
        	if(args[0].length() > 4)
        		substring = 4;
        }
        else if(getCommand.startsWith("enter")) {
        	if(args[0].length() > 5)
        		substring = 5;
        }
        else if(getCommand.startsWith("옮기기")) {
        	if(args[0].length() > 3)
        		substring = 3;
        }
       
    	if(substring != 0) {
    		getMistake = getMistake.substring(substring);
    		
    		soWhich = getMistake;
    	}
    	else {
    		if(args[0].equalsIgnoreCase("입장")||args[0].equalsIgnoreCase("이동")||args[0].equalsIgnoreCase("옮기기")||args[0].equalsIgnoreCase("들어가기")||args[0].equalsIgnoreCase("move")||args[0].equalsIgnoreCase("mv")||args[0].equalsIgnoreCase("enter")) {
		    	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		
		    	soWhich = input.replaceAll(" ", "");
		    	
		    	if(args.length <= 1) {
	        		member = event.getGuild().getMemberById(msg.getMember().getId());
	        		if(member.getVoiceState().getChannel() == null) {
	        			String langu = "음성채널을 들어갈 곳이 없어요";
	                	if(lan.equals("eng"))
	                		langu = "Not available to join the voice channel.";
	                		
	                    channel.sendMessage(langu).queue();
	                    System.out.println("BOT: " + langu);
	                        
	    	        	log(channel, event, "BOT: " + langu);
	    	        	
	    	        	return;
	        		}
	        		soWhich = member.getVoiceState().getChannel().getId();
		    	}
		    	
    		}
	    	
    	}
    	
    	
		try {
			if(soWhich.length() != 18) {

            	String langu = "올바른 id를 입력해주세요";
            	if(lan.equals("eng"))
            		langu = "Please input a valid id.";
            		
                channel.sendMessage(langu).queue();
                System.out.println("BOT: " + langu);
                    
	        	log(channel, event, "BOT: " + langu);
	        	
	        	return;
            }
			else {
				myChannel = guild.getVoiceChannelById(soWhich);
			}
			
			
			memberBot = event.getGuild().getMemberById(BotMusicListener.bot);
			
			String inChannel = "";
			if(memberBot.getVoiceState().getChannel() != null)
				inChannel = memberBot.getVoiceState().getChannel().getName();
			
    		if(myChannel.getName().contentEquals(inChannel)) {
    			String langu = "이미 **" + myChannel.getName() + "** 에 있어요 (" + (int)(myChannel.getMembers().size()-1) + "명)";
        		if(lan.equals("eng"))
        			langu = "Already joined **" + myChannel.getName() + "**. (" + (int)(myChannel.getMembers().size()-1) + "people)";
        		
    			channel.sendMessage(langu).queue();
    			log(channel, event, "BOT: " + langu);
    		}
    		else {
    			
    			guild.getAudioManager().openAudioConnection(myChannel);
    			if(musicManager.scheduler.isIn == 0) {
	    			musicManager.scheduler.isIn = 1;
	    			guild.getAudioManager().setSelfDeafened(true);
	    			isInTotal = isInTotal + 1;
	    			
	    			String nowVol = "현재볼륨";
					if(musicManager.scheduler.setLanguageStop.equals("eng"))
						nowVol = "Now volume";
					
					String volume = ":sound: " + nowVol + ": " + musicManager.scheduler.player.getVolume();
					if(musicManager.scheduler.player.getVolume() < 12)
						volume = ":speaker: " + nowVol + ": " + musicManager.scheduler.player.getVolume();
					else if(musicManager.scheduler.player.getVolume() > 24)
						volume = ":loud_sound: " + nowVol + ": " + musicManager.scheduler.player.getVolume();
						
					if(!member.getId().equals(BotMusicListener.admin))
						log(channel, event, "BOT: `(" + channel.getGuild().getName() + ")` " + volume);
					
					musicManager.scheduler.fn.removeMessage(channel, musicManager.scheduler.userVolumeMessageId, musicManager.scheduler.botVolumeMessageId);
					
					if(channel.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_ADD_REACTION)) {
						channel.sendMessage(volume).queueAfter(300, TimeUnit.MILLISECONDS, response -> {
							musicManager.scheduler.setVolumeMessageId = response.getId();
							
							Runnable reaction = () -> {
								try {
									response.addReaction("U+1f53d").complete();
									response.addReaction("U+1f53c").complete();
								}
								catch(Exception e) {
									response.delete().queue();
								}
							};
							Thread t = new Thread(reaction);
							t.start();
							
						});
			        }
					
					Runnable write1 = () -> {
						
						File file = new File(BotMusicListener.directoryDefault + "guild/removeVoiceStateMessages.txt");
						try {
							FileWriter fw = new FileWriter(file);
							fw.write(String.valueOf(isInTotal));
							fw.close();
						}
						catch (Exception e) {
							e.printStackTrace();
							channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
						}
					};
					
					Runnable write2 = () -> {
						File file = new File(BotMusicListener.directoryDefault + "guild/usingGuilds.txt");
						try {
							FileWriter fw = new FileWriter(file, true);
							if(file.length() <= 1)
								fw.append(channel.getGuild().getId() + "/" + channel.getId());
							else
								fw.append("\n" + channel.getGuild().getId() + "/" + channel.getId());
							
							fw.close();
						}
						catch (Exception e) {
							e.printStackTrace();
							channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
							log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
						}
					};
					
					Thread t1 = new Thread(write1);
					Thread t2 = new Thread(write2);
					t1.start();
					t2.start();
					
					editguilds(channel, msg, event);
    			}

    			String langu = "**" + myChannel.getName() + "** 로 들어왔어요  (" + (int)(myChannel.getMembers().size()) + "명)";
        		if(myChannel.getMembers().size() == 0) {
        			langu = "아무도 없는 **" + myChannel.getName() + "** 로 들어왔어요";
        		}
        		if(lan.equals("eng"))
        			langu = "Joined to **" + myChannel.getName() + "**. (" + (int)(myChannel.getMembers().size()) + " people)";
        		
        		
    			channel.sendMessage(langu).queue();
    			log(channel, event, "BOT: " + langu);
    		}

		}
		catch(Exception e) {	
			channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
			log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));

		}
	}
	
	public static void disconnectVoice(Guild guild, VoiceChannel vc) {
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, leaveTc, leaveMsg, leaveEvent);
		
		musicManager.scheduler.disconnectVoice(guild, vc);
	}
	
	public static void autoPause(Guild gu, GuildVoiceLeaveEvent event) {
		GuildMusicManager musicManager = getGuildAudioPlayer(gu, leaveTc, leaveMsg, leaveEvent);
		
		TextChannel value = musicManager.scheduler.tc;
		boolean isPaused = musicManager.player.isPaused();
		
		if(!isPaused) {
			if(musicManager.scheduler.isPlay == 1) {
				
				pause(value, leaveEvent, null, musicManager.scheduler.setLanguageStop, 0);
				
				String language = ":pause_button: **" + event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getName() + "**에 아무도 없어 자동으로 일시정지했어요";
				if(musicManager.scheduler.setLanguageStop.equals("eng")) 
					language = ":pause_button: Auto paused because nobody in **" + event.getChannelLeft().getName() + "**.";
				
				value.sendMessage(language).queue(response -> {
					musicManager.scheduler.autoPausedId = response.getId();
				});
				
				String reply = "BOT: `(" + event.getGuild().getName() + ")` " + language;
				System.out.println(reply);	
				
				if(!event.getMember().getId().equals(BotMusicListener.admin))
					log(value, null, reply);
				
	    		
				musicManager.scheduler.autoPaused = 1;
			}
		}
		
	}
	
	public static void autoPauseToMove(Guild gu, GuildVoiceMoveEvent event) {
		GuildMusicManager musicManager = getGuildAudioPlayer(gu, leaveTc, leaveMsg, leaveEvent);
		
		TextChannel value = musicManager.scheduler.tc;
		boolean isPaused = musicManager.player.isPaused();
		
		String language = ":pause_button: **" + event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getName() + "**에 아무도 없어 자동으로 일시정지했어요";
		if(musicManager.scheduler.setLanguageStop.equals("eng")) 
			language = ":pause_button: Auto paused because nobody in **" + event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getName() + "**.";
		
		if(!isPaused) {
			if(musicManager.scheduler.isPlay == 1) {
				pause(value, leaveEvent, null, musicManager.scheduler.setLanguageStop, 0);
				
				musicManager.scheduler.voiceChannelId = gu.getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getId();

				value.sendMessage(language).queue(response -> {
					musicManager.scheduler.autoPausedId = response.getId();
				});
				
				String reply = "BOT: `(" + event.getGuild().getName() + ")` " + language;
				System.out.println(reply);
				
				if(!event.getMember().getId().equals(BotMusicListener.admin))
					log(value, null, reply);
				
				musicManager.scheduler.autoPaused = 1;
			}
		}
		else {
			if(musicManager.scheduler.autoPaused == 1) {
				if(event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getId().equals(musicManager.scheduler.voiceChannelId)) return;
				
				musicManager.scheduler.voiceChannelId = gu.getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getId();

				String langu = language;
				Runnable edit = () -> {
					try {
						value.editMessageById(musicManager.scheduler.autoPausedId, langu).complete();
					}
					catch(Exception e) {
						value.sendMessage(langu).queue(response -> {
							musicManager.scheduler.autoPausedId = response.getId();
						});
					}
					
				};
				
				Thread t1 = new Thread(edit);
				t1.start();

			}
		}
		

	}
	
	public static void autoResume(Guild gu, GuildVoiceJoinEvent event, List<Member> members, String valueGuild, GuildVoiceMoveEvent moveEvent) {
		GuildMusicManager musicManager = getGuildAudioPlayer(gu, leaveTc, leaveMsg, leaveEvent);
		
		TextChannel value = musicManager.scheduler.tc;
		boolean isPaused = musicManager.player.isPaused();
		
		if(isPaused) {
			if(musicManager.scheduler.isPlay == 1 && musicManager.scheduler.autoPaused == 1) {
				
				if(event == null)
					resume(value, leaveEvent, null, musicManager.scheduler.setLanguageStop, 2);
				else
					resume(value, leaveEvent, null, musicManager.scheduler.setLanguageStop, 0);

				Runnable delete = () -> {
					try {
						value.deleteMessageById(musicManager.scheduler.autoPausedId).complete();
					}
					catch(Exception e) {}
					
					if(event == null) {
						if(!moveEvent.getMember().getId().equals(BotMusicListener.admin))
							log(value, null, "BOT: `(" + gu.getName() + ")` 다시 들어와 이어서 재생해요");
					}
					else {
						if(!event.getMember().getId().equals(BotMusicListener.admin))
							log(value, null, "BOT: `(" + gu.getName() + ")` 다시 들어와 이어서 재생해요");
					}
	 			};
				
	        	Thread t1 = new Thread(delete);
				t1.start();
				
				musicManager.scheduler.autoPaused = 0;
				
				updateVoiceStatePlaying(gu, event, members, valueGuild, moveEvent);
			}
			else {
				StringBuilder s = new StringBuilder();
	    		StringBuilder t = new StringBuilder();
	    		String title = "";
	    		
	    		if(event == null) {
	    			title = "**(일시정지) " + gu.getName() + "/" + moveEvent.getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
	    		}
	    		else {
	    			title = "**(일시정지) " + gu.getName() + "/" + event.getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
	    		}
	    		
	    		s.append(title);
	    		t.append(title);
	    		
	        	String valu = valueGuild;
	        	Runnable edit = () -> {
		        	try {
		        		BotMusicListener.voiceTc.editMessageById(valu, BotMusicListener.voiceStats(s, members)).complete();	
		        	}
		        		
		        	catch(Exception e) {
		        		BotMusicListener.voiceTc.sendMessage(BotMusicListener.voiceStats(t, members)).queue(response -> {
		        			BotMusicListener.voiceTcMessage.put(event.getGuild(), response.getId());
		
			   			});
		        	}
	        	};
	        	
	        	Thread t1 = new Thread(edit);
				t1.start();
				
			}
			
		}
		else {
			updateVoiceStatePlaying(gu, event, members, valueGuild, moveEvent);
		}

	}
	
	public static void updateVoiceStatePlaying(Guild guild, GuildVoiceJoinEvent event, List<Member> members, String valueGuild, GuildVoiceMoveEvent moveEvent) {
		StringBuilder s = new StringBuilder();
		StringBuilder t = new StringBuilder();
		String title = "";
		if(event == null)
			title = "**" + guild.getName() + "/" + moveEvent.getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
		else 
			title = "**" + guild.getName() + "/" + event.getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
		
		s.append(title);
		t.append(title);
		
    	String valu = valueGuild;
    	Runnable edit = () -> {
        	try {
        		BotMusicListener.voiceTc.editMessageById(valu, BotMusicListener.voiceStats(s, members)).complete();	
        	}
        		
        	catch(Exception e) {
        		BotMusicListener.voiceTc.sendMessage(BotMusicListener.voiceStats(t, members)).queue(response -> {
        			if(event == null)
        				BotMusicListener.voiceTcMessage.put(moveEvent.getGuild(), response.getId());
        			else
        				BotMusicListener.voiceTcMessage.put(event.getGuild(), response.getId());

	   			});
        	}
    	};

    	Thread t1 = new Thread(edit);
    	t1.start();
    	
	}
	
	public static void wantToPlay(TextChannel channel, Message msg, MessageReceivedEvent event, String query) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, null, null);

		musicManager.scheduler.wantToPlay(channel, msg, event, query);
	}
	
	public static void addReaction(TextChannel channel, String msgId, ReactionEmote emote, Member member) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, null, null);

		musicManager.scheduler.addReaction(channel, msgId, emote, member);
	}
	
	
	public static void removeAllReaction(TextChannel channel, String msgId) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, null, null);

		musicManager.scheduler.removeAllReaction(channel, msgId);
	}
	
	public static void removeReaction(TextChannel channel, String msgId, ReactionEmote emote, Member member) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, null, null);

		musicManager.scheduler.removeReaction(channel, msgId, emote, member);
	}
	
	public static void checkLock(TextChannel channel, User user, MessageReceivedEvent event) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);

		if(musicManager.scheduler.lock == 1) {
			if(user.toString().contains("297963786504110083")) {}
			else {
				channel.sendMessage("군밤이 잠겨있어요").queue();
				BotMusicListener.ret = 1;
			}
		}
	}

	public static void reset(TextChannel channel, Message msg, MessageReceivedEvent event) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.terminate = 1;
		musicManager.scheduler.isPlay = 0;
		
		channel.sendMessage(":clipboard: `처리 중...`").queue(response -> {

			if (guild.getAudioManager().isConnected()) {
				guild.getAudioManager().closeAudioConnection();	
				isInTotal = isInTotal - 1;
			}
			
			musicManager.scheduler.clearQueue(channel, msg, response, guild, event, 6, 0, "kor");
			
			
			File file = new File(BotMusicListener.directoryDefault + "guild/removeVoiceStateMessages.txt");
			try {
			    FileWriter fw = new FileWriter(file);
			    fw.write(String.valueOf(isInTotal));
			    fw.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			    response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue(); 
			    log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
			}
			
			editguilds(channel, msg, event);
		});
		
		
		/*
		if(isInTotal == 0) {
			if(gunBamStateRun == 1) {
				gunBamStateTimer.cancel(); 
				gunBamStateTimer = null;
				gunBamStateRun = 0;		
			}
		}
		*/
		
		musicManager.scheduler.reset(channel);
		
	}
	
	public static String realTitle(String title) {
		title = title.replaceAll("\\*", "\\\\*").replaceAll("_", "\\\\_").replaceAll("~", "\\\\~").replaceAll("`", "\\\\`");
		return title;
	}
	
	public static void getListen(TextChannel channel, Message msg, MessageReceivedEvent event, String vc) {
		
		for(int i = 0; i<listg.size(); i++) {
			
			if(listg.get(i).toString().contains(vc)) {
				channel.sendMessage("**" + listg.get(i).toString() + "** 의 텍스트 채널들\n```" + listg.get(i).getTextChannels().toString() + "```").queue();
			}
		}
	}

	public static void log(TextChannel tc, MessageReceivedEvent event, String str) {
		if(BotMusicListener.logOn == 1) {
			if(event == null) {
				if(tc.getGuild().toString().contains(BotMusicListener.base)||tc.getId().equals("717203670365634621")) {}
				else BotMusicListener.logtc.sendMessage(str).queue();
			}
			else {
				if(event.getAuthor().getId().equals(BotMusicListener.admin)||tc.getGuild().toString().contains(BotMusicListener.base)||tc.getId().equals("717203670365634621")) {}
				else BotMusicListener.logtc.sendMessage(str).queue();
			}
		}
		
		File file = new File(BotMusicListener.directoryDefault + "log.txt");
		
		try {
		      FileWriter fw = new FileWriter(file, true);
		      fw.append("\n" + str);
		      
		      fw.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			BotMusicListener.logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
		}
	}
	
	
}