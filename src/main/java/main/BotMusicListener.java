package main;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import normal.Controller;
import audio.MusicController;
import functions.*;


public class BotMusicListener extends ListenerAdapter {
	
	//새벽반
	// CHANNEL #1(539392855383212032)
	// CHANNEL #2(553263893309423619)
	// CHANNEL #3(553263926851403787)
	// CHANNEL #4(553363287958487050)
	// CHANNEL #5(553363447442833421)
	// CHANNEL #6(656487163793506305)
	
	// 2인실 #1(656482527438962708)
	// 2인실 #2(656482588826796032)
	// 2인실 #3(657829301999370261)
	// 3인실 #1(656482606174568458)
	// 3인실 #2(664072238470922250)
	
	//POKEMON
	// POKEMON (586964043353554996)

	//STUDY-ROOM
	// 1(568034740704247839)
	// 2(568036888615583760)
	// 3(568036904654733322)

	//MOBILE
	// MOBILE #1(567450762121314346)
	// MOBILE #2(567450806702440458)

	//KARAOKE
	// #1(539391136263962624)
	// #2(539391150851620884)
	// #3(539391174394511361)

	//ETC
	// BOB(546130598608109599)
	// TV(553678247708196904)
	// zZZ(546130638315454474)
	// ...(553264622703214596)

	
	public static String chatVersion = "2020년 6월";
	public static String musicVersion = "2020년 7월";
	public static String musicVersionEng = "2020.07";
	
	CustomFunctions func = new CustomFunctions();

	int page = 0;
	int send = 0;
	String saveUser = "", saveGuild = "";

	static int administrator = 0;
	
	public static int ret = 0;
	
	String recentId = "";
	static TextChannel general;
	
	int fn = 0;
	public static TextChannel adtc, logtc, listentc, dokingtc, voiceTc, jdatc, errortc;

	public static int logOn = 1;
	public static int listen = 0;
	public static int allExitAlert = 0;
	public static User user = null;
	
	public static String admin = "297963786504110083", chanha = "447003774125342730", bot = "";
	
	public static String colorDefault = "#dddddd"; //dddddd, fff8dc
	public static String colorSetTimer = "#222222";
	
	public static String directoryDefault = "files/";
	public static String base = "661044497198743640", baseVoice = "705715643063861321", born = "533443336678146078";
	public static int queryCount = 9;
	public static HashMap<Guild, String> voiceTcMessage = new HashMap<Guild, String>();
	public static List<Member> members = new ArrayList<>();
	
	public static String prefix = "";
	String dokingStr = "686949011705430062";

	int last = 0;
	
	int isError = 0;
	public static int disconnect = 0;
	public static Message deleted, inputClear;
	
	public static long gunbamStartTime = 0;
	
	private static final AudioPlayerManager myManager = new DefaultAudioPlayerManager();

	
	public static StringBuilder voiceStats(StringBuilder s, List<Member> members) {
		
		 if(members.size() == 0) {
		 	s.append("음성채널에 아무도 없어요");
		 }
		 else {
			 for(int i=0; i<members.size(); i++) {
				 StringBuilder now = new StringBuilder();
		 		if(members.get(i).getVoiceState().isDeafened() == true) now.append(" Deaf");
		 		if(members.get(i).getVoiceState().isStream() == true) now.append(" Stream");
		 			
			 	if(members.get(i).getNickname() == null) {
			 		if(members.get(i).getUser().isBot()) s.append("U:" + members.get(i).getUser().getName() + "(" + members.get(i).getId() + ") [BOT]\n");
			 		else {
			 			s.append("U:" + members.get(i).getUser().getName() + "(" + members.get(i).getId() + ")" + now + "\n");
			 		}
			 	}
			 	else {
			 		if(members.get(i).getUser().isBot()) s.append("MB:" + members.get(i).getNickname() + "(" + members.get(i).getId() + ") [BOT]\n");
			 		else {
			 			s.append("MB:" + members.get(i).getNickname() + "(" + members.get(i).getId() + ")" + now + "\n");
			 		}
			 	}
			 }
		 }
		 s.append("```");
		return s;
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		
        	TextChannel t = event.getJDA().getGuildById(base).getTextChannelById("679204476808069120");
        	voiceTc = event.getJDA().getGuildById(base).getTextChannelById(baseVoice);
        	logtc = event.getJDA().getTextChannelById("686517298470846495");
        	
        	gunbamStartTime = System.currentTimeMillis();
        	
        	Runnable remove1 = () -> {
	        	try {
		        	MessageHistory mh1 = new MessageHistory(t);
		        	List<Message> mh11 = mh1.retrievePast(7).complete();

		        	try {
		        		t.deleteMessages(mh11).complete();
		        	}
		        	catch(Exception e) {
		        		for(int i = 0; i<7; i++) {
		        			try {
		        				mh11.get(i).delete().complete();
		        			}
		        			catch(Exception f) {}
			        	}
		        	}
	
		            //t.deleteMessages(mh1.retrievePast(7).complete()).complete();
	        	}
	        	catch(Exception e) {
	        		try {
	        			
		    			MessageHistory mh1 = new MessageHistory(t);
		    			List<Message> mh11 = mh1.retrievePast(3).complete();
	        			
			        	mh11.get(0).delete().complete();
			            
	        		}
	        		catch(Exception f) {}
	        	}
	        	
	        	t.sendMessage("군밤이 가동을 시작했어요").queue();
			    System.out.println("BOT: 군밤이 가동을 시작헀어요");
        	};
        	
        	if(Main.num.equals("1")) {
	        	Runnable remove2 = () -> {
		        	File file = new File(BotMusicListener.directoryDefault + "guild/removeVoiceStateMessages.txt");
		        	int remove = 0;
		        	
		    		try {
		    			FileReader filereader = new FileReader(file);
		    	       
		    	        BufferedReader bufReader = new BufferedReader(filereader);
		    	        String line = "";
		    	        
		    	        while((line = bufReader.readLine()) != null){
		    	        	remove = Integer.parseInt(line);
		    	        }
		    	               
		    	        bufReader.close();
		    		}
		    		catch(Exception e) {
		    			t.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
		    		}
		    		
		    		try {
		    			if(remove > 1) {
				        	MessageHistory mh1 = new MessageHistory(voiceTc);
				        	List<Message> mh11 = mh1.retrievePast(remove).complete();
				        	for(int i = 0; i<remove; i++) 
				        		mh11.get(i).delete().complete();
				            //voiceTc.deleteMessages(mh1.retrievePast(remove).complete()).complete();
		    			}
		    			
		    			else if(remove == 1) {
		    				
		    				MessageHistory mh1 = new MessageHistory(voiceTc);
		    				List<Message> mh11 = mh1.retrievePast(3).complete();
		    				mh11.get(0).delete().complete();
				            //voiceTc.deleteMessages(mh1.retrievePast(3).complete()).complete();
		    			}
	
		        	}
		        	catch(Exception e) {
		        		t.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
		        	}
		    		
		    		try {
		    		    FileWriter fw2 = new FileWriter(file);
		    		    fw2.write(String.valueOf(0));
		    		    fw2.close();
		    		}
		    		catch (Exception e) {
		    			e.printStackTrace();
		    		    t.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();  
		    		}
	
	        	};
	        	Thread t2 = new Thread(remove2);
	        	t2.start();
	        	
	        	Runnable send = () -> {
					File file = new File(BotMusicListener.directoryDefault + "guild/usingGuilds.txt");
					
					try{   
			            FileReader filereader = new FileReader(file);
			            BufferedReader bufReader  =  new BufferedReader(filereader);
		
			            String line = "";
			            while((line = bufReader.readLine()) != null){
			            	try {
				            	event.getJDA().getTextChannelById(line.split("/")[1]).sendMessage(":mega: 봇이 " + Main.because + " 되었어요").queue();
				            	logtc.sendMessage(":mega: `(" + event.getJDA().getGuildById(line.split("/")[0]).getName() + ")` 봇이 " + Main.because + " 되었어요").queue();
			            	}
			            	catch(Exception e) {
			            		
			            	}
			            }
			            //.readLine()은 끝에 개행문자를 읽지 않는다.            
			            bufReader.close();
			            
			            FileWriter fw = new FileWriter(file, false);
						fw.write("");
						fw.close();
			        }
					catch(Exception e){
			            System.out.println(e);
			            t.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
			        }
	        	};
	        	
	        	Thread t3 = new Thread(send);
	        	t3.start();
        	}
    
        	MusicController.prepare(adtc, null, null);
        	
        	Thread t1 = new Thread(remove1);
        	t1.start();
        	

        	/*
		    voiceTc.sendMessage("**" + event.getJDA().getGuildById(base).getName() + "** \n```css\n(오프라인)```").queue(response -> {
			 		voiceTcMessage.put(event.getJDA().getGuildById(base), response.getId());

			});
			*/
		    
	}
	
	@Override
	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
		if(event.getGuild().getId().equals(born)) { //새벽반
			String name = event.getMember().getNickname();

			String id = event.getMember().getId();
			Guild guild = event.getJDA().getGuildById(base);
			if(id.toString().contains(admin)||guild.getMemberById(id) == null) return;

			guild.getMemberById(id).modifyNickname(name).queue();
		}
	}

	@Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		
		int mat = 0;
		
		members.clear();
		members.addAll(event.getVoiceState().getChannel().getMembers());
		
		
		end:
		for(int i=0; i<members.size(); i++)
	        if(members.get(i).getId().equals(bot)) {
	        	mat = 1;
	        	break end;
	        }
	        
		String value = "";
    	try {
    		value = voiceTcMessage.get(event.getGuild());
    	}
    	catch(Exception e) {}
    	
	    if(mat == 1) {
	    	
        	if(members.size() > 1) {
    			MusicController.autoResume(event.getGuild(), event, members, value, null);
    		}
     
	    }
  
    }
	
	@Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		
		if(event.getGuild().getMemberById(bot).getVoiceState().getChannel() == null) return;
		
		boolean inBot = false;
		
		end1:
		for(int i = 0; i<event.getChannelJoined().getMembers().size(); i++) {
			if(event.getChannelJoined().getMembers().get(i).getId().equals(bot)) {
				inBot = true;
				break end1;
			}
		}
		
		if(inBot == false) {
			end2:
			for(int i = 0; i<event.getChannelLeft().getMembers().size(); i++) {
				if(event.getChannelLeft().getMembers().get(i).getId().equals(bot)) {
					inBot = true;
					break end2;
				}
			}
		}
		
		if(inBot == true) {
			members.clear();
			members.addAll(event.getGuild().getMemberById(bot).getVoiceState().getChannel().getMembers());
	
			int memberSize = event.getGuild().getMemberById(bot).getVoiceState().getChannel().getMembers().size();
	        String value = "";
	        
	        try {
	    		value = voiceTcMessage.get(event.getGuild());
	    	}
	    	catch(Exception e) {}
			
			if(memberSize > 1) {
				MusicController.autoResume(event.getGuild(), null, members, value, event);
			}
			else if(memberSize <= 1) {
				MusicController.autoPauseToMove(event.getGuild(), event);
			}
		
	    	StringBuilder s = new StringBuilder();
	    	StringBuilder t = new StringBuilder();
	    	
	    	String title = "**" + event.getGuild().getName() + "/" + event.getGuild().getMemberById(bot).getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
	    	if(event.getGuild().getAudioManager().isSelfMuted() == true) {
	    		title = "**(일시정지) " + event.getGuild().getName() + "/" + event.getGuild().getMemberById(bot).getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
	    	}
	    	
	    	s.append(title);
	    	t.append(title);
	    	
	        String valu = value;
	        Runnable edit = () -> {
	        	try {
	        		voiceTc.editMessageById(valu, voiceStats(s, members)).complete();	
	        	}
	        	catch(Exception e) {
	        		voiceTc.sendMessage(voiceStats(t, members)).queue(response -> {
		   				 voiceTcMessage.put(event.getGuild(), response.getId());
	
		   			});
	        	}
	        	
	        };
	        Thread t1 = new Thread(edit);
	        t1.start();
		}

	}
	
	@Override
    public void onDisconnect(DisconnectEvent event) {
		jdatc.sendMessage("연결 끊김 코드: **" + event.getCloseCode() + "** `" + event.getClientCloseFrame() + "`").queue();
		System.out.println("연결 끊김 코드: **" + event.getCloseCode() + "**, " + event.getClientCloseFrame());
		
		if(event.getClientCloseFrame().getCloseCode() == 1008||event.getClientCloseFrame().getCloseCode() == 1000) {
			MusicController.disconnect(event.getClientCloseFrame().getCloseCode(), event);
			MusicController.editguilds(adtc, null, null);
		}
		
		else if(event.getClientCloseFrame().getCloseCode() != 4900) {
			MusicController.disconnect(event.getClientCloseFrame().getCloseCode(), event);
			
		}
	
	}
	
	@Override
    public void onResume(ResumedEvent event) {
		jdatc.sendMessage("재연결됨").queue();
	}
	
	@Override
	public void onGuildVoiceStream(GuildVoiceStreamEvent event) {
		try {
			boolean inBot = false;
			
			end:
			for(int i = 0; i<event.getVoiceState().getChannel().getMembers().size(); i++) {
				if(event.getVoiceState().getChannel().getMembers().get(i).getId().equals(bot)) {
					inBot = true;
					break end;
				}
			}
			
			if(inBot == true) {
				members.clear();
				members.addAll(event.getGuild().getMemberById(bot).getVoiceState().getChannel().getMembers());
				
				String value = "";
		        try {
		    		value = voiceTcMessage.get(event.getGuild());
		    	}
		    	catch(Exception e) {}
		        
		        StringBuilder s = new StringBuilder();
		    	StringBuilder t = new StringBuilder();
		    	
		    	String title = "";
				
				if(event.getGuild().getAudioManager().isSelfMuted() == true) {
			    	title = "**(일시정지) " + event.getGuild().getName() + "/" + event.getGuild().getMemberById(bot).getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
				}
				else {
					title = "**" + event.getGuild().getName() + "/" + event.getGuild().getMemberById(bot).getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
				}
				
				s.append(title);
		    	t.append(title);
		    	
		    	String valu = value;
		        Runnable edit = () -> {
		        	try {
		        		voiceTc.editMessageById(valu, voiceStats(s, members)).complete();	
		        	}
		        	catch(Exception e) {
		        		voiceTc.sendMessage(voiceStats(t, members)).queue(response -> {
			   				 voiceTcMessage.put(event.getGuild(), response.getId());

			   			});
		        	}
		        	
		        };
		        Thread t1 = new Thread(edit);
		        t1.start();
			
			}
		}
		catch(Exception e) {}
	}
	
	@Override
	public void onGuildVoiceSelfDeafen(GuildVoiceSelfDeafenEvent event) {
		try {
			boolean inBot = false;
			
			end:
			for(int i = 0; i<event.getVoiceState().getChannel().getMembers().size(); i++) {
				if(event.getVoiceState().getChannel().getMembers().get(i).getId().equals(bot)) {
					inBot = true;
					break end;
				}
			}
			
			if(inBot == true) {
				members.clear();
				members.addAll(event.getGuild().getMemberById(bot).getVoiceState().getChannel().getMembers());
				
				String value = "";
		        try {
		    		value = voiceTcMessage.get(event.getGuild());
		    	}
		    	catch(Exception e) {}
		        
		        StringBuilder s = new StringBuilder();
		    	StringBuilder t = new StringBuilder();
		    	
		    	String title = "";
				
				if(event.getGuild().getAudioManager().isSelfMuted() == true) {
			    	title = "**(일시정지) " + event.getGuild().getName() + "/" + event.getGuild().getMemberById(bot).getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
				}
				else {
					title = "**" + event.getGuild().getName() + "/" + event.getGuild().getMemberById(bot).getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
				}
				
				s.append(title);
		    	t.append(title);
		    	
		    	String valu = value;
		        Runnable edit = () -> {
		        	try {
		        		voiceTc.editMessageById(valu, voiceStats(s, members)).complete();	
		        	}
		        	catch(Exception e) {
		        		voiceTc.sendMessage(voiceStats(t, members)).queue(response -> {
			   				 voiceTcMessage.put(event.getGuild(), response.getId());

			   			});
		        	}
		        	
		        };
		        Thread t1 = new Thread(edit);
		        t1.start();
			
			}
		}
		catch(Exception e) {}
	}
	
	@Override
	public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent event) {
		if(event.getMember().getId().equals(bot)) {
			
			members.clear();
			try {
				members.addAll(event.getGuild().getMemberById(bot).getVoiceState().getChannel().getMembers());
	
		        String value = "";
		        try {
		    		value = voiceTcMessage.get(event.getGuild());
		    	}
		    	catch(Exception e) {}
				
		        
				StringBuilder s = new StringBuilder();
		    	StringBuilder t = new StringBuilder();
		    	String title = "";
				
				if(event.getGuild().getAudioManager().isSelfMuted() == true) {
			    	title = "**(일시정지) " + event.getGuild().getName() + "/" + event.getGuild().getMemberById(bot).getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
				}
				else {
					title = "**" + event.getGuild().getName() + "/" + event.getGuild().getMemberById(bot).getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
				}
	
		    	s.append(title);
		    	t.append(title);
		    	
			    String valu = value;
			    Runnable edit = () -> {
			        try {
			        	voiceTc.editMessageById(valu, voiceStats(s, members)).complete();		
			        }
			        		
			        catch(Exception e) {
			        	voiceTc.sendMessage(voiceStats(t, members)).queue(response -> {
				   		voiceTcMessage.put(event.getGuild(), response.getId());
	
				   		});
			        }
			        	
			    };
			    Thread t1 = new Thread(edit);
			    t1.start();
			   
			}
			catch(Exception e) {}
			
		}
	
	}
	
	@Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		members.clear();
		members.addAll(event.getChannelLeft().getMembers());
		
		if(event.getMember().getId().equals(bot)) {
			
			MusicController.disconnectVoice(event.getGuild(), event.getChannelLeft());
			
	    }
	    else {
	    	boolean available = false;
	    	
	    	end:
	    	for(int i=0; i<members.size(); i++) {
	    		if(members.get(i).getId().equals(bot)) {
	    			available = true;
	    			break end;
	    		}
	    	}
	    	
	    	if(available == false) return;
	
	    	String value = "";
	    
	    	try {
	    		value = voiceTcMessage.get(event.getGuild());
	    	}
	    	catch(Exception e) {}
			
			if(members.size() == 1) {
				if(event.getGuild().getAudioManager().isSelfMuted() == false) {
					MusicController.autoPause(event.getGuild(), event);
					return;
				}
			}

			StringBuilder s = new StringBuilder();
			StringBuilder t = new StringBuilder();
			
			String title = "**" + event.getGuild().getName() + "/" + event.getChannelLeft().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
			if(event.getGuild().getAudioManager().isSelfMuted() == true) {
				title = "**(일시정지) " + event.getGuild().getName() + "/" + event.getChannelLeft().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
			}
			
			s.append(title);
			t.append(title);
			
		    String valu = value;
	    	Runnable edit = () -> {
	    		try {
		    		voiceTc.editMessageById(valu, voiceStats(s, members)).complete();	
	    		}
		    		
			    catch(Exception e) {
			    	voiceTc.sendMessage(voiceStats(t, members)).queue(response -> {
			   			 voiceTcMessage.put(event.getChannelLeft().getGuild(), response.getId());
			
			   		});
			    }
			};
		    Thread t1 = new Thread(edit);
		    t1.start();

	    	
	    }
	}

	public static void update(Guild guild) {
		String eachId = voiceTcMessage.get(guild);
		/*
		try {
			voiceTc.editMessageById(eachId, "**" + guild.getName() + "** \n```css\n(오프라인)```").complete();
		}
		
		catch(Exception e){
			voiceTc.sendMessage("**" + guild.getName() + "** \n```css\n(오프라인)```").queue(response -> {
			 		voiceTcMessage.put(guild, response.getId());

			 	});
		}
		*/
		
		try {
			voiceTc.deleteMessageById(eachId).complete();
		}
		
		catch(Exception e){}
		
	}
	
	
	
	// message receive
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {

		user = event.getAuthor(); 
        final TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();
        Guild guild1 = event.getGuild();
        
        if(Main.num.equals("1")) bot = "661001856616497162";
		else if(Main.num.equals("2")) bot = "661042002531713035";
        
        
        adtc = event.getJDA().getTextChannelById("679204476808069120");
        logtc = event.getJDA().getTextChannelById("686517298470846495");
        listentc = event.getJDA().getTextChannelById("686949011705430062");
        dokingtc = event.getJDA().getTextChannelById(dokingStr);
        jdatc = event.getJDA().getTextChannelById("706443648023134259");
        errortc = event.getJDA().getTextChannelById("713233574995820627");

        if(!event.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_WRITE)) {
        	return;
        }
        
        if(user.toString().contains(bot)) { //군밤
            if(msg.getContentRaw().equals("ㅣ나가기")||msg.getContentRaw().equals("$나가기")) {
            	Guild guild = event.getGuild();
            	
                if(guild.getId().equals(born)) {
                	 MusicController.stopPlaying(tc, msg, event, 3, 2, "kor");
                }
                else {
                	MusicController.stopPlaying(tc, msg, event, 3, 0, "kor");
                }
            }
        }  

        if(listen == 1) {
        	if(tc.toString().contains(dokingStr)) {
        		Date date = new Date();
	        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
	    		String str = dayTime.format(date);
	        	
	        	listentc.sendMessage(".\n.\n" + str + "\n" + guild1 + "\n" + user.toString() + ": " + msg.getContentRaw() + "\n").queue();
        
        	}
        	
        	if(tc.toString().contains("686949011705430062")&&user.toString().contains(admin)) {
        		dokingtc.sendMessage(msg.getContentRaw()).queue();
        	}
        }

  
        if(guild1.getId().equals(born)) {}
        else {MusicController.save(tc, event, 0);}

        
    	
        if(administrator == 1) {
        	if(tc == general) {
        		
	        	System.out.println("");
	         	System.out.println(user.toString() + ": " + msg.getContentRaw());
        	}
        	
	        Administrator.admin(general, msg, guild1, user, event);
        	return;
        }
        
        if(guild1.getId().equals("374071874222686211")&&msg.getContentRaw().startsWith("$")) return;
        if(guild1.toString().contains(base)||tc.toString().contains("553254281495576577")) {}
        else
        	general = tc;
        
	    
        if(tc.toString().contains("662897914405715978")) {
        	if(msg.getContentRaw().equals("사용법")) {
        		if(send == 0) {
        			tc.sendMessage("군밤사용법으로 알아보거나 Haribo 쓰는 것 처럼 써봐요").queue();
        			send = 1;
        		}
        	}
        }
        
        //is bot return
        if(user.isBot())  {
        	return;}
 
        if(msg.getContentRaw().toString().contains("ㅣ")) {}
        else {
        	if(guild1.getId().equals(born))
        		recentId = msg.getId();
        }
     
       
        if(msg.getContentRaw().contains("군밤")&& (msg.getContentRaw().contains("설명서") || msg.getContentRaw().contains("사용법") || msg.getContentRaw().contains("도움말"))){
        	System.out.println("");
    		System.out.println(guild1.toString() + "\n" + user.toString() + ": " + msg.getContentRaw());
    		Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
        	log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	Controller.help(tc, msg, event, "kor");
        	
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("일전")||msg.getContentRaw().contains("일 전"))){
        	System.out.println("");
        	System.out.println(guild1.toString() + "\n" + user.toString() + ": " + msg.getContentRaw());
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
        	log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	String minus = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	if(func.isNumber(minus) == true) {
        		int minusint = Integer.parseInt(minus);
        		Command.dateCountMinus(minusint, tc);
        		
        	}
        	else {
        		Command.pardon(tc);
        	}
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("일뒤")||msg.getContentRaw().contains("일 후")||msg.getContentRaw().contains("일후")||msg.getContentRaw().contains("일 뒤"))){
        	System.out.println("");
        	System.out.println(guild1.toString() + "\n" + user.toString() + ": " + msg.getContentRaw());
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
        	log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	String plus = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	if(func.isNumber(plus) == true) {
        		
        		int plusint = Integer.parseInt(plus);
        		
        		Command.dateCount(plusint, tc);
        	}
        	else {
        		Command.pardon(tc);
        	}
        	
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("오늘")&&(msg.getContentRaw().contains("일")||msg.getContentRaw().contains("칠"))){
        	System.out.println("");
        	System.out.println(guild1.toString() + "\n" + user.toString() + ": " + msg.getContentRaw());
        	
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	
        	Command.today(tc, msg);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("다음 달까지")||msg.getContentRaw().contains("다음달까지"))){
        	System.out.println("");
        	System.out.println(guild1.toString() + "\n" + user.toString() + ": " + msg.getContentRaw());
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
    		
        	Command.nextMonth(tc, msg);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("일까지")||msg.getContentRaw().contains("일 까지"))&&(msg.getContentRaw().contains("얼마")||msg.getContentRaw().contains("며칠")||msg.getContentRaw().contains("몇일"))){
        	System.out.println("");
        	System.out.println(guild1.toString() + "\n" + user.toString() + ": " + msg.getContentRaw());
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
    		
	        Command.untilDay(tc, msg);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("수능")&&(msg.getContentRaw().contains("얼마")||msg.getContentRaw().contains("언제")||msg.getContentRaw().contains("며칠"))){
        	System.out.println("");
        	System.out.println(guild1.toString() + "\n" + user.toString() + ": " + msg.getContentRaw());
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
    		
	        Command.untilDay(tc, msg);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("일은")&&(msg.getContentRaw().contains("언제")||msg.getContentRaw().contains("며칠")||msg.getContentRaw().contains("몇일"))&&(msg.getContentRaw().contains("전")||msg.getContentRaw().contains("었")||msg.getContentRaw().contains("였"))){
        	System.out.println("");
        	System.out.println(guild1.toString() + "\n" + user.toString() + ": " + msg.getContentRaw());
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
    		
	        Command.lastDay(tc, msg);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("정보")){
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	
    		Controller.showInfo(tc, msg, event, "kor");
        	
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("분")&&(msg.getContentRaw().contains("후")||msg.getContentRaw().contains("뒤"))&&msg.getContentRaw().contains("알려")) {
        	String min = msg.getContentRaw().replaceAll("[^0-9]", "");

        	Controller.timer(user, tc, msg, event, Integer.parseInt(min));
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("타이머")&&msg.getContentRaw().contains("분")&&(msg.getContentRaw().contains("설정")||msg.getContentRaw().contains("맞춰")||msg.getContentRaw().contains("세팅"))) {
        	String min = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	Controller.timer(user, tc, msg, event, Integer.parseInt(min));
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("타이머")&&msg.getContentRaw().contains("분")&&(msg.getContentRaw().contains("남았")||msg.getContentRaw().contains("남음")||msg.getContentRaw().contains("뒤에"))) {
        	
        	Controller.nowTimer(tc, msg, event);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("타이머")&&(msg.getContentRaw().contains("없애")||msg.getContentRaw().contains("지워")||msg.getContentRaw().contains("삭제")||msg.getContentRaw().contains("취소")||msg.getContentRaw().contains("제거"))) {
        	
        	Controller.timerCancel(tc, msg, event);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("몇시")||msg.getContentRaw().contains("몇분")||msg.getContentRaw().contains("몇초")||msg.getContentRaw().contains("몇 시")||msg.getContentRaw().contains("몇 분")||msg.getContentRaw().contains("몇 초"))){
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	
        	showTime(tc, user, msg, "kor");
        	
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("=")||msg.getContentRaw().contains("계산"))){
        	Calculate.calculate(tc, msg);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("사진")||msg.getContentRaw().contains("프로필")||msg.getContentRaw().contains("프사"))) {
        	String str = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	EmbedBuilder eb = new EmbedBuilder();
        	eb.setColor(Color.decode(colorDefault));
        	eb.setTitle("불러오는 중...");
        	
        	
        	tc.sendMessage(eb.build()).queue(response -> {
        		try {
	        		eb.setImage(event.getJDA().getUserById(str).getAvatarUrl() + "?size=2048");
	            	eb.setTitle("'" + event.getJDA().getUserById(str).getName() + "' 의 사진", event.getJDA().getUserById(str).getAvatarUrl() + "?size=4096");
        		}
            	
        		catch(Exception e) {
        			eb.setTitle("가져오지 못했어요" + func.cause(e));
        		}
        		
        		response.editMessage(eb.build()).queue();
        	});

        }
       
        
        else if(user.toString().contains(admin) && (msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("누구")||msg.getContentRaw().contains("누군")||msg.getContentRaw().contains("알아")||msg.getContentRaw().contains("찾아")||msg.getContentRaw().contains("조사")))){
        	if(msg.getContentRaw().contains("에서")) {
        		searchPeopleSelectGuild(tc, msg, event);
        	}
        	
        	else {
        		
        		tc.sendMessage("조사 중...").queue(response -> {
        			searchPeople(tc, msg, response);
        		});

        	}
        	
        }
        
        else if(msg.toString().contains("군밤")&&msg.toString().contains("주사위")) {
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	
        	Random random = new Random();
        	int i = random.nextInt(6);
        	
        	if(i == 0) {
        		tc.sendMessage(":one: !").queue();
        		System.out.println("1");
        	}
        	
        	else if(i == 1) {
        		tc.sendMessage(":two: !").queue();
        		System.out.println("2");
        	}
        	
        	else if(i == 2) {
        		tc.sendMessage(":three: !").queue();
        		System.out.println("3");
        	}
        	
        	else if(i == 3) {
        		tc.sendMessage(":four: !").queue();
        		System.out.println("4");
        	}
        	
        	else if(i == 4) {
        		tc.sendMessage(":five: !").queue();
        		System.out.println("5");
        	}
        	
        	else if(i == 5) {
        		tc.sendMessage(":six: !").queue();
        		System.out.println("6");
        	}
        }
        
        
        //명령어 시작
        if(msg.getContentRaw().startsWith("$!")||msg.getContentRaw().startsWith("$$")||msg.getContentRaw().startsWith("$n"))
        	return;
        
        if(msg.getContentRaw().startsWith("ㅣ")||msg.getContentRaw().startsWith("$")) {}
        else {
        	return;
        }
        
        if(guild1.toString().contains("264445053596991498")) return;
        if(isError == 1) {
        	if(user.toString().contains(admin)) {}
        	else {
        		tc.sendMessage("점검 중으로 잠시 뒤에 시도하세요").queue();
        	}
        }

        if(msg.getContentRaw().charAt(0) == 'ㅣ'||msg.getContentRaw().charAt(0) == '$'){
        	if(tc.toString().contains("686949011705430062")) {return;}

        	String[] args;
        	if(msg.getContentRaw().charAt(1) == ' ') {
        		 args = msg.getContentRaw().substring(2).split(" ");
        	}
        	
        	else args = msg.getContentRaw().substring(1).split(" ");
        	
        	
        	if(tc.toString().contains("553254281495576577")||tc.toString().contains("652157102927773699")||tc.toString().contains("599181021681811459")||msg.getContentRaw().contains("ㅣㅊ")||msg.getContentRaw().contains("ㅣclear")||msg.getContentRaw().contains("$ㅊ")||msg.getContentRaw().contains("$clear")) {}
        	else 
        		MusicController.alertTc(tc, event);
       	
        	if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("guilds")||args[0].equalsIgnoreCase("g")||args[0].equalsIgnoreCase("ㅎ"))){
             	MusicController.guilds(tc, msg, event);

             }
        	 
        	if(user.toString().contains(admin)) {}
        	else { //로그찍기
	        	System.out.println("");
	        	System.out.println(guild1 + "\n" + user.toString() + ": " + msg.getContentRaw());        		
	        	Date date = new Date();
		        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
		    	String str = dayTime.format(date);
	            log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        		
        	}
   

        	send = 0;    
        	
        	/*
        	if(ready == 0) {
	        	if(user.toString().contains(admin)) {}
	        	else {
	        		tc.sendMessage("준비 완료하는 중이에요").queue();
	        		System.out.println("BOT: 준비 완료하는 중이에요");
	        		
	        		Date date = new Date();
		        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
		    		String str = dayTime.format(date);
	        		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
	        		return;
	        	}
        	}
        	*/
        	
            if(args.length <= 0) return;
            
            if(user.toString().contains(admin)&&args[0].equalsIgnoreCase("administrator")){
            	/*
            	tc.sendMessage("access Administrator").queue();

            	MessageHistory mh = new MessageHistory(tc);
	            List<Message> msgs = mh.retrievePast(2).complete();
	            tc.deleteMessages(msgs).complete();
            	
	            tc.sendMessage("```봇이 일지 중지됩니다```").queue();
	            */
            	String vc = "";
                try{ 
                	vc =args[1];

                } catch(Exception e){
                	tc.sendMessage(".").queue();
                	System.out.println("BOT: .");
                	 
                    return;
                }
                general = event.getJDA().getTextChannelById(vc);
            	System.out.println(general.getGuild().toString() + "의 " + general.toString() + "채널에 보냅니다");
            	administrator = 1;
            	
            	Administrator.admin(tc, msg, guild1, user, event);
 
            }
  
            else if((user.toString().contains(admin)||user.toString().contains(chanha))&&(args[0].equalsIgnoreCase("myclear")||args[0].equalsIgnoreCase("mc")||args[0].equalsIgnoreCase("ㅡㅊ"))){
            	
                if(args.length != 2) {
                	MessageHistory mh1 = new MessageHistory(tc);
	            	List<Message> msgs3 = mh1.retrievePast(2).complete();
	            	
                	Runnable remove = () -> {
		            	for(int i = 0; i<2; i++) {
		            		if(msg.getAuthor() == msgs3.get(i).getAuthor())
		            			msgs3.get(i).delete().complete();
		            	}
                	};
                	
		            Thread t = new Thread(remove);
                	t.start();
                	
                	return;
                }
                
                int count = 1;
                try{ 
                	count = Integer.parseInt(args[1]);
                } catch(Exception e){
                	String userMsg = msg.getId();
                    tc.sendMessage("write integer type").queue(response -> {
                    	response.delete().queueAfter(2000, TimeUnit.MILLISECONDS);
            
                    });
                    
                    Runnable remove = () -> {
	                    try {
	                		tc.deleteMessageById(userMsg).completeAfter(2000, TimeUnit.MILLISECONDS);
	                	}
	                	catch(Exception f) {}
                    };
                    
                    Thread t = new Thread(remove);
                    t.start();
                    
                    
                    return;
                }
                
                if(count == 2) {
                	
                	try {
	                	MessageHistory mh1 = new MessageHistory(tc);
	 	            	List<Message> msgs3 = mh1.retrievePast(3).complete();
	 	            	
	                	Runnable remove = () -> {
		 	                //tc.deleteMessages(msgs3).complete();
		 	            	for(int i = 0; i<3; i++) {
		 	            		if(msg.getAuthor() == msgs3.get(i).getAuthor()) {
			 	            		try {
			 	            			msgs3.get(i).delete().complete();
			 	            		}
			 	            		catch(Exception e) {}
		 	            		}
		 	            	}
		 	            	
	                	};
	                	
	                	Thread t = new Thread(remove);
	                	t.start();
                	}
                	catch(Exception e) {
                		tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
                		log(tc, event, msg, ":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));
                	}
                	
 	                return;
                }
                
                if(count < 2 | count > 100){
                	String userMsg = msg.getId();
                    tc.sendMessage("only 2~100").queue(response -> {
                    	response.delete().queueAfter(2000, TimeUnit.MILLISECONDS);
          
                    });
                    
                    
                    Runnable remove = () -> {
	                    try {
	                		tc.deleteMessageById(userMsg).completeAfter(2000, TimeUnit.MILLISECONDS);
	                	}
	                	catch(Exception f) {}
                    };
                    
                    Thread t = new Thread(remove);
                    t.start();
                  
                    return;
                }

                try {
                	int co = count;
                	inputClear = msg;
		                MessageHistory mh = tc.getHistory();
		                List<Message> msgs = mh.retrievePast(co + 1).complete();
		                
		                for(int i = 0; i<msgs.size(); i++) {
	 	            		if(msg.getAuthor() != msgs.get(i).getAuthor()) {
		 	            		msgs.remove(i);
		 	            		co = co - 1;
	 	            		}
	 	            	}
		                
		                if(co > 5) {
		                	MusicController.clear(tc, msgs, msg, event, co);
		                	return;
		                }
		                
		                int coun = co;
		                tc.sendMessage(co + " deleted of " + count + " `'" + msgs.get(co).getContentRaw() + "' 까지`").queue(response -> {
	          
	                		Runnable remove = () -> {
	 		                	
		                		try {
				                	tc.deleteMessages(msgs).complete();
				                	response.delete().queueAfter(1500, TimeUnit.MILLISECONDS);
				                }
				                catch(Exception e) {
				                	
				                	for(int i = 0; i<coun+1; i++) {
				                		try {
				                			msgs.get(i).delete().complete();
				                		}
				                		catch(Exception f) {}
					                	
				                		
					                	if(i == coun) {
					                		try {
					                			response.delete().complete();
					                		}
					                		catch(Exception g) {}
					                	}
				      
				                	}
				                }
				               
				            };

		                	Thread t = new Thread(remove);
		                	t.start();
	                		
	                	});
	
                }
                catch(Exception e) {
                	tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
            		log(tc, event, msg, ":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));
                }
 
            }
            
            else if((user.toString().contains(admin)||user.toString().contains(chanha))&&(args[0].equalsIgnoreCase("c")||args[0].equalsIgnoreCase("clear")||args[0].equalsIgnoreCase("ㅊ"))){
            	
                if(args.length != 2) {
                	MessageHistory mh1 = new MessageHistory(tc);
	            	List<Message> msgs3 = mh1.retrievePast(2).complete();
	            	
                	Runnable remove = () -> {
		            	for(int i = 0; i<2; i++)
		                	msgs3.get(i).delete().complete();
                	};
                	
		            Thread t = new Thread(remove);
                	t.start();
                	
                	return;
                }
                
                int count = 1;
                try{ 
                	count = Integer.parseInt(args[1]);
                } catch(Exception e){
                	String userMsg = msg.getId();
                    tc.sendMessage("write integer type").queue(response -> {
                    	response.delete().queueAfter(2000, TimeUnit.MILLISECONDS);
            
                    });
                    
                    Runnable remove = () -> {
	                    try {
	                		tc.deleteMessageById(userMsg).completeAfter(2000, TimeUnit.MILLISECONDS);
	                	}
	                	catch(Exception f) {}
                    };
                    
                    Thread t = new Thread(remove);
                    t.start();
                    
                    
                    return;
                }
                
                if(count == 2) {
                	
                	MessageHistory mh1 = new MessageHistory(tc);
 	            	List<Message> msgs3 = mh1.retrievePast(3).complete();
 	            	
                	Runnable remove = () -> {
	 	                //tc.deleteMessages(msgs3).complete();
	 	            	for(int i = 0; i<3; i++) {
	 	            		try {
	 	            			msgs3.get(i).delete().complete();
	 	            		}
	 	            		catch(Exception e) {}
	 	            	}
	 	            	
                	};
                	
                	Thread t = new Thread(remove);
                	t.start();
                	
 	                return;
                }
                
                if(count < 2 | count > 100){
                	String userMsg = msg.getId();
                    tc.sendMessage("only 2~100").queue(response -> {
                    	response.delete().queueAfter(2000, TimeUnit.MILLISECONDS);
          
                    });
                    
                    
                    Runnable remove = () -> {
	                    try {
	                		tc.deleteMessageById(userMsg).completeAfter(2000, TimeUnit.MILLISECONDS);
	                	}
	                	catch(Exception f) {}
                    };
                    
                    Thread t = new Thread(remove);
                    t.start();
                  
                    return;
                }

                try {
                	int co = count;
                	inputClear = msg;
		                MessageHistory mh = tc.getHistory();
		                List<Message> msgs = mh.retrievePast(co + 1).complete();
		                
		                if(co > 5) {
		                	MusicController.clear(tc, msgs, msg, event, co);
		                	return;
		                }
		                
		                
		                tc.sendMessage(co + " deleted `'" + msgs.get(co).getContentRaw() + "' 까지`").queue(response -> {
	                		
	                		Runnable remove = () -> {
	 		                	
		                		try {
				                	tc.deleteMessages(msgs).complete();
				                	response.delete().queueAfter(1500, TimeUnit.MILLISECONDS);
				                }
				                catch(Exception e) {
				                	
				                	for(int i = 0; i<co+1; i++) {
				                		try {
				                			msgs.get(i).delete().complete();
				                		}
				                		catch(Exception f) {}
					                	
				                		
					                	if(i == co) {
					                		try {
					                			response.delete().complete();
					                		}
					                		catch(Exception g) {}
					                	}
				      
				                	}
				                }
				               
				            };

		                	Thread t = new Thread(remove);
		                	t.start();
	                		
	                	});
    	
	   	
                }
                catch(Exception e) {
                	tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
                }
 
            }
        
            else if(user.toString().contains(admin)&&args[0].equalsIgnoreCase("lock")){
            	MusicController.lock(tc, event, 1);
            }
            
            else if(user.toString().contains(admin)&&args[0].equalsIgnoreCase("상황실")){
            	tc.sendMessage("저장 중...").queue(response -> {
            		MusicController.memoSave(tc, msg, event, response);
            	});
            	
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("완료")||args[0].equalsIgnoreCase("complete"))){
            	File file = new File(BotMusicListener.directoryDefault + "guild/guildListSave.txt");
            	int count = 0;
            	int coun = 0;
            	try{   
                    FileReader filereader = new FileReader(file);
                    BufferedReader bufReader  =  new BufferedReader(filereader);

                    String line = "";
                    TextChannel comp;
                    
                    File file3 = new File(BotMusicListener.directoryDefault + "guild/guildCount.txt");
            		
            		try {
            			FileReader filereader3 = new FileReader(file3);
            	       
            	        BufferedReader bufReader3 = new BufferedReader(filereader3);
            	        String line3 = "";
            	        
            	        while((line3 = bufReader3.readLine()) != null){
            	        	 coun = Integer.parseInt(line3);
            	        }
            	               
            	        bufReader3.close();
            		}
            		catch(Exception e) {
            			logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
            		}
            		
                    String info = "";
                    File file2 = new File(BotMusicListener.directoryDefault + "updateInfoLittle.txt");
            		
            		try {
            			FileReader filereader2 = new FileReader(file2);
            	       
            	        BufferedReader bufReader2 = new BufferedReader(filereader2);
            	        String line2 = "";
            	        while((line2 = bufReader2.readLine()) != null){
            	        	 info = line2;
            	        }
            	               
            	        bufReader2.close();
            		}
            		catch(Exception e) {
            			logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
            		}
                    
                    while((line = bufReader.readLine()) != null){
                    	comp = event.getJDA().getTextChannelById(line);
                    	EmbedBuilder eb = new EmbedBuilder();
                    	
                    	eb.setColor(Color.decode(colorDefault));
                    	eb.addField("버전", musicVersion, false);
                    	eb.addField("내용", info, true);
                    	eb.setAuthor("'자세한 패치노트 보기'", "https://discord.gg/Qq4kuca", null);
                    	
                    	MessageBuilder msgs = new MessageBuilder();
                    	msgs.append("업데이트가 완료되었어요");
                    	msgs.setEmbed(eb.build());
                    	
                    	comp.sendMessage(msgs.build()).queue();
                    	count = count + 1;
                    	
                    	if(count == coun)
                    		break;
                    }
          
                    bufReader.close();
                }
            	catch(Exception e){
                    System.out.println(e);
                    logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
                }
            	
            	tc.sendMessage("**" + count + "개** 채널에 업데이트 완료했어요").queue();
            	coun = 0;
            }
            
            else if(user.toString().contains(admin)&&args[0].equalsIgnoreCase("unlock")){
            	MusicController.lock(tc,event, 0);
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("listen")||args[0].equalsIgnoreCase("도킹"))) {
            	
            	String vc = "";
                try{ 
                	vc =args[1];

                } catch(Exception e){
                	                	 
                    return;
                }

              if(vc.contains("취소")||vc.contains("끄기")||vc.contains("멈춤")||vc.contains("그만")||vc.contains("나가기")) {
            	  tc.sendMessage("```도킹을 그만해요```").queue();
              	  listen = 0;
            	  return;
              }
                	dokingStr = vc;
                	listen = 1;
                	tc.sendMessage("```" + vc + " 도킹을 시작해요```").queue();
  
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("getTextChannel")||args[0].equalsIgnoreCase("채널보기"))) {
            	
            	String vc = "";
                try{ 
                	vc =args[1];

                } catch(Exception e){
                	                	 
                    return;
                }

                MusicController.getListen(tc, msg, event, vc);
	
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("stopListen")||args[0].equalsIgnoreCase("도킹그만")||args[0].equalsIgnoreCase("도킹멈춤")||args[0].equalsIgnoreCase("도킹나가기")||args[0].equalsIgnoreCase("도킹취소")||args[0].equalsIgnoreCase("도킹끄기"))) {
            	tc.sendMessage("```도킹을 그만해요```").queue();
            	listen = 0;
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("알림")||args[0].equalsIgnoreCase("알리기")||args[0].equalsIgnoreCase("allExitAlert")||args[0].equalsIgnoreCase("모두나가면알림"))) {
            	tc.sendMessage("모든 음성채널에서 군밤이 나가면 알림으로 알려줘요").queue();
            	allExitAlert = 1;
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("saveDefault")||args[0].equalsIgnoreCase("sd")||args[0].equalsIgnoreCase("ㄴㅇ"))) {
            	
            	String vc = "";
                try{ 
                	vc =args[1];

                } catch(Exception e){
                	MusicController.save(tc, event, 2);                	 
                    return;
                }

                if(func.isNumber(vc)) {
                	if(Integer.parseInt(args[1]) == 1) {
                		tc.sendMessage("```음성채널을 나갈 때 목록이 저장됩니다```").queue();
                		System.out.println("BOT: ```음성채널을 나갈 때 목록이 저장됩니다```");
                		
                		MusicController.save(tc, event, 1);
                	}
                	
                	else {
                		tc.sendMessage("```css\n음성채널을 나갈 때 [목록을 저장하지 않습니다]```").queue();
                		System.out.println("BOT: ```css\n음성채널을 나갈 때 [목록을 저장하지 않습니다]```");	
                		
                		MusicController.save(tc, event, 0);
                	}
                }
                else {
                	tc.sendMessage("숫자로 입력해 주세요").queue();
                	System.out.println("BOT: 숫자로 입력해 주세요");
 	
                }
	
            }
            
            else if(user.toString().contains(admin)&&args[0].equalsIgnoreCase("점검")) {
            	
            	String vc = "";
                try{ 
                	vc =args[1];

                } catch(Exception e){
                	MusicController.save(tc, event, 2);                	 
                    return;
                }

                if(func.isNumber(vc)) {
                	if(Integer.parseInt(args[1]) == 1) {
                		tc.sendMessage("```점검을 시작합니다```").queue();
                		System.out.println("BOT: ```점검을 시작합니다```");
                		
                		isError = 1;
                	}
                	
                	else {
                		tc.sendMessage("```점검을 끝냅니다```").queue();
                		System.out.println("BOT: ```점검을 끝냅니다```");	
                		
                		isError = 0;
                	}
                }
                else {
                	tc.sendMessage("숫자로 입력해 주세요").queue();
                	System.out.println("BOT: 숫자로 입력해 주세요");
 	
                }
	
            }
 
            else if(user.toString().contains(admin)&&args[0].equalsIgnoreCase("alert")){
            	
            	File file = new File(BotMusicListener.directoryDefault + "guild/guildListSave.txt");
            	int coun = 0;
            	int count = 0;
            	StringBuilder s = new StringBuilder();
            	try{   
                    FileReader filereader = new FileReader(file);
                    BufferedReader bufReader  =  new BufferedReader(filereader);

                    String line = "";
                    TextChannel comp;

                    File file3 = new File(BotMusicListener.directoryDefault + "guild/guildCount.txt");
            		
            		try {
            			FileReader filereader3 = new FileReader(file3);
            	       
            	        BufferedReader bufReader3 = new BufferedReader(filereader3);
            	        String line3 = "";
            	        
            	        while((line3 = bufReader3.readLine()) != null){
            	        	 coun = Integer.parseInt(line3);
            	        }
            	               
            	        bufReader3.close();
            		}
            		catch(Exception e) {
            			logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
            		}
            		
            		File file2 = new File(BotMusicListener.directoryDefault + "updateInfo.txt");
            		
            		try {
            			FileReader filereader2 = new FileReader(file2);
            	       
            	        BufferedReader bufReader2 = new BufferedReader(filereader2);
            	        String line2 = "";
            	        
            	        while((line2 = bufReader2.readLine()) != null){
            	        	 s.append(line2 + "\n");

            	        }
            	               
            	        bufReader2.close();
            		}
            		catch(Exception e) {
            			logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
            		}
                    
                    while((line = bufReader.readLine()) != null){
                    	comp = event.getJDA().getTextChannelById(line);
                    	
                    	
                    	comp.sendMessage(s).queue();
                    	count = count + 1;
                    	
                    	if(count == coun) {
                    		break;
                    		
                    	}
                    }
          
                    bufReader.close();
                }
            	catch(Exception e){
                    System.out.println(e);
                    logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
                }
            	
            	tc.sendMessage("**" + coun + "개** 채널에 공지합니다").queue();
            	coun = 0;
            	count = 0;
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("보완")||args[0].equalsIgnoreCase("수정")||args[0].equalsIgnoreCase("fix"))){
            	tc.sendMessage("수정 중...").queue(response -> {
            		MusicController.fixGuilds(tc, msg, event, response);
            	});
            	
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("clearLog")||args[0].equalsIgnoreCase("로그지우기"))){
            	clearLog(tc, event, msg);
            	tc.sendMessage("로그를 클리어했어요").queue();
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("logOn")||args[0].equalsIgnoreCase("로그켜기"))){
            	clearLog(tc, event, msg);
            	tc.sendMessage("로그를 켜요").queue();
            	logOn = 1;
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("logOn")||args[0].equalsIgnoreCase("로그끄기"))){
            	clearLog(tc, event, msg);
            	tc.sendMessage("로그를 꺼요").queue();
            	logOn = 0;
            }
        
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("saveGuilds")||args[0].equalsIgnoreCase("길드저장"))) {
            	MusicController.saveGuilds(tc, event);
            }
            
            else if(user.toString().contains(admin)&&args[0].equalsIgnoreCase("print")){
            	MusicController.alert(tc, event);
            }
            
            else if(args[0].equalsIgnoreCase("채널")||args[0].equalsIgnoreCase("channel")||args[0].equalsIgnoreCase("ch")||args[0].equalsIgnoreCase("CHANNEL")||args[0].equalsIgnoreCase("CH")){
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";

            	MusicController.channel(tc, msg, event, language);
            }
            
            
            else if(args[0].equals(prefix)) {
            	Controller.news(tc, msg, event);
            }
            
            else if(args[0].equalsIgnoreCase("경제")||args[0].equalsIgnoreCase("economy")||args[0].equalsIgnoreCase("ECONOMY")||args[0].equalsIgnoreCase("money")||args[0].equalsIgnoreCase("MONEY")||args[0].equalsIgnoreCase("돈")||args[0].equalsIgnoreCase("코스피")||args[0].equalsIgnoreCase("KOSPI")||args[0].equalsIgnoreCase("kospi")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	Controller.kospi(tc, msg, event, language);
            }
            
            else if(args[0].equalsIgnoreCase("재설정")||args[0].equalsIgnoreCase("reset")||args[0].equalsIgnoreCase("RESET")||args[0].equalsIgnoreCase("r")||args[0].equalsIgnoreCase("R")) {
            	
        		MusicController.reset(tc, msg, event);
        		Controller.reset(tc, msg, event);
            }
            
            else if(args[0].contains("print")||args[0].contains("guild")||args[0].equalsIgnoreCase("alert")||args[0].equalsIgnoreCase("clear")||args[0].contains("admin")||args[0].contains("logout")||args[0].contains("saveDefault")||args[0].contains("lock")) {
            	
            	tc.sendMessage("관리자만 이용 가능한 명령어에요").queue();
            	System.out.println("BOT: 관리자만 이용 가능한 명령어에요");       	
            }
            
            // user
            else if(args[0].equalsIgnoreCase("1")||args[0].equalsIgnoreCase("2")||args[0].equalsIgnoreCase("3")||args[0].equalsIgnoreCase("4")||args[0].equalsIgnoreCase("5")||args[0].equalsIgnoreCase("6")||args[0].equalsIgnoreCase("7")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	Member member = event.getMember();
            	String language = "kor";
            	if(msg.getContentRaw().startsWith("$")) language = "eng";
            	
		        MusicController.searchAndPlay(tc, msg, args[0], event, member, language);
		        MusicController.repeat(tc, msg, event);    	
            }
            
            else if(args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("p")||args[0].equalsIgnoreCase("play")||args[0].equalsIgnoreCase("제생")||args[0].equalsIgnoreCase("재생")||args[0].equalsIgnoreCase("ㅔ")||args[0].equalsIgnoreCase("추가")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	Member member = event.getMember();
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
 
            	AudioSourceManagers.registerRemoteSources(myManager);
            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            	if(args.length <= 1) { 
            			
                    MusicController.queryNull(tc, msg, event, member, language);
                        
                } 
            	else {
                	MusicController.loadAndPlay(tc, msg, null, input, event, language, 0);
                	MusicController.repeat(tc, msg, event);
                }       	
            }
            
            else if(args[0].equalsIgnoreCase("randomplay")||args[0].equalsIgnoreCase("ranp")||args[0].equalsIgnoreCase("랜덤제생")||args[0].equalsIgnoreCase("랜덤재생")||args[0].equalsIgnoreCase("랜재")||args[0].equalsIgnoreCase("playrandom")||args[0].equalsIgnoreCase("pran")||args[0].equalsIgnoreCase("제생랜덤")||args[0].equalsIgnoreCase("재생랜덤")||args[0].equalsIgnoreCase("재랜")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}

            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	AudioSourceManagers.registerRemoteSources(myManager);
            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            		
            	if(args.length <= 1) {
                    MusicController.randomNextTrack(tc, msg, event, language);
                        
                } 
            	else {
                    if(input.startsWith("https://")) {
	                	MusicController.loadAndPlayRandom(tc, msg, input, event, language);
	                	MusicController.repeat(tc, msg, event);
                    }
                    	
                    else {
                    	String langu = "**재생목록 URL**이 아닌 것 같아요";
                    	if(language.equals("eng"))
                    		langu = "I don't think that URL links to a playlist.";
                    			
                    	tc.sendMessage(langu).queue();
                    	log(tc, event, msg, "BOT: " + langu);
                    }
 			
                }       	
                   
            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("주소")||args[0].equalsIgnoreCase("주소받기")||args[0].equalsIgnoreCase("주소정보"))) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
 
            	try {
            		InetAddress inet = InetAddress.getLocalHost();
            		StringBuilder s = new StringBuilder();
            		s.append("로컬호스트: " + inet);
            		
            		tc.sendMessage(s).queue();
            	}
            	
            	catch(Exception e) {
            		e.printStackTrace();
					tc.sendMessage("오류가 발생했어요\n**" + ":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
            	}

            }
            
            else if(user.toString().contains(admin)&&(args[0].equalsIgnoreCase("test")||args[0].equalsIgnoreCase("테스트"))) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
 
            	Member member = event.getMember();
            	
            	AudioSourceManagers.registerRemoteSources(myManager);
            	VoiceChannel myChannel = member.getVoiceState().getChannel();
            	if(myChannel == null) {
            		tc.sendMessage("먼저 음성채널에 들어가세요").queue();
            		System.out.println("BOT: 먼저 음성채널에 들어가세요");
            		
	        		log(tc, event, msg, "BOT: 먼저 음성채널에 들어가세요");
	       
            		return;
            	}
            		
                String youtubeSearch = "dynamix faithtival";
                
                MusicController.loadAndPlay(tc, msg, null, youtubeSearch, event, "kor", 0);
                MusicController.repeat(tc, msg, event);
      
            }
            		
            
            else if(args[0].equalsIgnoreCase("검색")||args[0].equalsIgnoreCase("search")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";

            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            	if(args.length <= 1) {
            			
            		MusicController.queryNullSearch(tc, msg, event, language);
            			
                } 
            		
            	else {
            		String query = "ytsearch:" + input;
            			
            		EmbedBuilder eb = new EmbedBuilder();
        			eb.setColor(Color.decode(colorDefault));
        				
        			String langu = ":mag: `" + query.substring(queryCount) + "` 검색 중...";
                	if(language.equals("eng"))
                		langu = ":mag: Searching `" + query.substring(queryCount) + "`...";
                		
        			eb.setTitle(langu);
        				
        			String langua = language;
        				
                    tc.sendMessage(eb.build()).queue(response -> {
        		        	
                    	MusicController.search(tc, msg, query, event, response, langua);
        		    });
                    	
                 }
                    	
            }
  
            else if(args[0].equalsIgnoreCase("ㅔㅔ")||args[0].equalsIgnoreCase("일시정지")||args[0].equalsIgnoreCase("정지")||args[0].equalsIgnoreCase("pause")||args[0].equalsIgnoreCase("pp")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	Member member = event.getMember();
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	MusicController.pause(tc, event, member, language, 1);
            }
            
            else if(args[0].equalsIgnoreCase("이동")||args[0].equalsIgnoreCase("옮기기")||args[0].equalsIgnoreCase("move")||args[0].equalsIgnoreCase("mv")) {
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";

            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            	if(args.length <= 1||input.length() != 18) {
            		
            		String langu = "올바른 id를 입력해주세요";
            		if(language.equals("eng"))
            			langu = "Please input a valid id.";
            		
                    tc.sendMessage(langu).queue();
                    System.out.println("BOT: " + langu);
                    
	        		log(tc, event, msg, "BOT: " + langu);
	        		return;
                }
            	
            	String id = input.replaceAll(" ", "");
            	Guild guild = tc.getGuild();
            	
        		try {
        			Member member = event.getGuild().getMemberById(bot);
        			VoiceChannel myChannel = guild.getVoiceChannelById(id);  
        			String inChannel = member.getVoiceState().getChannel().getName();
        			
	        		if(myChannel.getName().contentEquals(inChannel)) {
	        			String langu = "이미 **" + myChannel.getName() + "** 에 있어요 (" + (int)(myChannel.getMembers().size()-1) + "명)";
	            		if(language.equals("eng"))
	            			langu = "Already joined **" + myChannel.getName() + "**. (" + (int)(myChannel.getMembers().size()-1) + "people)";
	            		
	        			tc.sendMessage(langu).queue();
	        			log(tc, event, msg, "BOT: " + langu);
	        		}
	        		else {
	   
	        			guild.getAudioManager().openAudioConnection(myChannel);
	    
	        		    
	        			String langu = "**" + myChannel.getName() + "** 로 이동했어요  (" + (int)(myChannel.getMembers().size()) + "명)";
	            		if(language.equals("eng"))
	            			langu = "Joined to **" + myChannel.getName() + "**.  (" + (int)(myChannel.getMembers().size()) + "people)";
	            		
	        			tc.sendMessage(langu).queue();
	        			log(tc, event, msg, "BOT: " + langu);
	        		}

        		}
        		catch(Exception e) {	
        			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
        			log(tc, event, msg, ":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));

        		}
            }
  
            else if(args[0].equalsIgnoreCase("cpu")||args[0].equalsIgnoreCase("사용량")||args[0].equalsIgnoreCase("spec")||args[0].equalsIgnoreCase("스펙")||args[0].equalsIgnoreCase("사양")||args[0].equalsIgnoreCase("성능")||args[0].equalsIgnoreCase("컴퓨터")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	Controller.useCpu(tc, msg, event, language);

            }
       
            else if(args[0].equalsIgnoreCase("리스트")||args[0].equalsIgnoreCase("재생목록")||args[0].equalsIgnoreCase("목록")||args[0].equalsIgnoreCase("ㅂ")||args[0].equalsIgnoreCase("큐")||args[0].equalsIgnoreCase("q")||args[0].equalsIgnoreCase("queue")||args[0].equalsIgnoreCase("list")||args[0].equalsIgnoreCase("playlist")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll(" ", "");
            	if(args.length <= 1) {
            		input = "1";
            		fn = 2;
                }
            	String in = input;

            	MusicController.nowplay(tc, msg, event, Integer.parseInt(in), fn, language);

            	page = Integer.parseInt(input);
            	fn = 0;
 	
            }
            
            else if(args[0].equalsIgnoreCase("넘기기")||args[0].equalsIgnoreCase("스킵")||args[0].equalsIgnoreCase("건너뛰기")||args[0].equalsIgnoreCase("ㄴ")||args[0].equalsIgnoreCase("skip")||args[0].equalsIgnoreCase("s")||args[0].equalsIgnoreCase("jump")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll(" ", "");
            	if(args.length <= 1) {
            		input = "0";
            	}
            	
            	MusicController.skipTrack(tc, msg, event, Integer.parseInt(input), language);
            	
            }
            
            else if(args[0].equalsIgnoreCase("재거")||args[0].equalsIgnoreCase("제거")||args[0].equalsIgnoreCase("삭제")||args[0].equalsIgnoreCase("remove")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}

            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	if(args.length <= 1) {
            		String langu = "삭제할 항목 번호가 있어야해요";
            		if(language.equals("eng")) langu = "Must have item number.";
            		
            		tc.sendMessage(langu).queue();
            		System.out.println("BOT: " + langu);
            		
	        		log(tc, event, msg, "BOT: " + langu);

            	}
            	else {
            		if(args.length > 2) {
            			String languag = language;
            			Runnable re = () -> {
	            			List<Integer> items = new ArrayList<>();
	            			
	            			for(int i = 1; i<args.length; i++) { 
	            				
	            				if(args[i].contains("~")||args[i].contains("-")) {
	            						
	            					int x = 0, y = 0;
	            						    
	            						
	            					if(args[i].contains("~"))
	            						args[i] = args[i].replaceAll(" ", "").replaceAll("^~, [^0-9]", "");
	            					else 
	            						args[i] = args[i].replaceAll(" ", "").replaceAll("^-, [^0-9]", "");
	            								
	            					try {
	            						String[] spli = null;
	            						if(args[i].contains("~"))
	            							spli = args[i].split("~");
	            						if(args[i].contains("-"))
	            							spli = args[i].split("-");
	            									
	            						if(args[i].contains("~")||args[i].contains("-")) {
	            									
	            							x = Integer.parseInt(spli[0]);
	            							y = Integer.parseInt(spli[1]);
	            							
	            							int z = 0;
	            							
	            							if(x > y) {
	            								z = x;
	            								x = y;
	            								y = z;
	            							}
	            						}
	
	            									
	            					}
	            					catch(Exception e) {
	            						tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
	            					   	log(tc, event, msg, ":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));
	            					   	return;
	            					}
	            						
	            					for(int j = x; j<=y; j++) {
	            						if(items.contains(j)) {}
	            						else items.add(j);
	            					}
	            						
	            				}
	            				else {
	            					if(func.isNumber(args[i])) {
	            						if(items.contains(Integer.parseInt(args[i]))) {}
	            						else
	            							items.add(Integer.parseInt(args[i]));
	            					}
	            				}
	            				
	            			}
	    
	            			Collections.sort(items);
	            			
	            			MusicController.removeManyTrack(items, tc, event, languag);
	            			items.clear();
            			};
            			Thread tr = new Thread(re);
            			tr.start();
            		}
            		
            		else if(args[1].contains("~")||args[1].contains("-")) MusicController.removeTrack(args[1], tc, event, 1, language, 1);
            		else {
            			if(func.isNumber(args[1])) 
            				MusicController.removeTrack(args[1], tc, event, 0, language, 0);
            			else {
            				
            				String langu = "번호만 입력하세요";
                    		if(language.equals("eng")) langu = "Please input number only.";
                    		
            				tc.sendMessage(langu).queue();
            				log(tc, event, msg, "BOT: " + langu);
            			}
            		}
            	}
            }
            
            else if(args[0].equalsIgnoreCase("다시재생")||args[0].equalsIgnoreCase("playagain")) {
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	if(guild1.getId().equals(born)) {
	            	MusicController.checkLock(tc, user, event);
	            	if(ret == 1) {
	            		ret = 0;
	            		return;
	            	}

	            	Member member = event.getMember();
	            	
	            	VoiceChannel myChannel = member.getVoiceState().getChannel();
	            	if(myChannel == null) {
	            		String langu = "먼저 음성채널에 들어가세요";
                		if(language.equals("eng")) langu = "Join the voice channel first.";
                		
	            		tc.sendMessage(langu).queue();
	            		System.out.println("BOT: " + langu);
	            		
		        		log(tc, event, msg, "BOT: " + langu);
	            		return;
	            	}
	  
	            	MusicController.repeat(tc, msg, event);
		            MusicController.playAgain(tc, msg, member, event, "id", 0, language);
            	}
            	
            	else {
            		String langu = "여기서는 쓸 수 없는 기능이에요";
            		if(language.equals("eng")) langu = "Can't use it in this server.";
            		
            		tc.sendMessage(langu).queue();
            		System.out.println("BOT: " + langu);
            		
	        		log(tc, event, msg, "BOT: " + langu);
            	}
            }
            
            else if(args[0].equalsIgnoreCase("불러오기")||args[0].equalsIgnoreCase("load")) {
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	if(guild1.getId().equals(born)||guild1.getId().equals(base)) {
	            	MusicController.checkLock(tc, user, event);
	            	if(ret == 1) {
	            		ret = 0;
	            		return;
	            	}

	            	Member member = event.getMember();
	
	            	VoiceChannel myChannel = member.getVoiceState().getChannel();
	            	if(myChannel == null) {
	            		String langu = "먼저 음성채널에 들어가세요";
                		if(language.equals("eng")) langu = "Join the voice channel first.";
                		
	            		tc.sendMessage(langu).queue();
	            		System.out.println("BOT: " + langu);
	            		
		        		log(tc, event, msg, "BOT: " + langu);
	            		return;
	            	}
	            	
	            	String id = member.getId();
	            	
	            	if(args.length > 1) {
	            		if(args[1].length() == 18) {
	            			id = args[1];
	            			
	            			try {
	            				event.getJDA().getUserById(id);
	            				MusicController.repeat(tc, msg, event);
	    			            MusicController.playAgain(tc, msg, member, event, id, 1, language);
	            			}
	            			catch(Exception e) {
	
	            				tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
	            				log(tc, event, msg, "BOT: :no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));
	            			}
	            		}
	            		
	            		else {
	            			String langu = "올바른 ID를 입력해 주세요";
                    		if(language.equals("eng")) langu = "Please input a valid id.";
                    		
            				tc.sendMessage(langu).queue();
            				log(tc, event, msg, "BOT: " + langu);
            				
            				return;
	            		}
	            	}
	            	else {
		            	MusicController.repeat(tc, msg, event);
			            MusicController.playAgain(tc, msg, member, event, id, 1, language);
	            	}
            	}
            	
            	else {
            		String langu = "여기서는 쓸 수 없는 기능이에요";
            		if(language.equals("eng")) langu = "Can't use it in this server.";
            		
            		tc.sendMessage(langu).queue();
            		System.out.println("BOT: " + langu);
            		
	        		log(tc, event, msg, "BOT: " + langu);
            	}
            }

            else if(args[0].equalsIgnoreCase("틀어")||args[0].equalsIgnoreCase("재개")||args[0].equalsIgnoreCase("resume")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	Member member = event.getMember();
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	MusicController.resume(tc, event, member, language, 1);}
            
            else if(args[0].equalsIgnoreCase("종료")||args[0].equalsIgnoreCase("마지막")||args[0].equalsIgnoreCase("last")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
    
            	int isLast = 0;
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll(" ", "");
            	
            	if(args.length <= 1) {
            		isLast = 1;
            		last = -1;
            	}
            	
            	else if(args[1].equals("0")||input.contains("취소")||input.contains("제거")||input.contains("cancel")||input.contains("remove")||input.contains("disable")) {
            		isLast = 0;
            	}
            	
            	else if(input.contains("목록")||input.contains("큐")||input.contains("재생목록")||input.contains("q")||input.contains("ㅂ")||input.contains("queue")||input.contains("list")||input.contains("playlist")) {
            		isLast = 1;
            		last = -2;
            	}
            	
            	else {
            		if(func.isNumber(input)) {
            			isLast = 1;
            			last = Integer.parseInt(input);
            		
            			if(last <= -3) last = -2;
            		}
            	}
            	

            	MusicController.last(tc, msg, user, event, isLast, last, language);
            	
            }
            
            else if(args[0].equalsIgnoreCase("랜담")||args[0].equalsIgnoreCase("렌덤")||args[0].equalsIgnoreCase("랜덤")||args[0].equalsIgnoreCase("셔플")||args[0].equalsIgnoreCase("shuffle")||args[0].equalsIgnoreCase("random")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	MusicController.shuffle(tc, msg, event, language);
            }
            
            else if(args[0].equalsIgnoreCase("현재")||args[0].equalsIgnoreCase("지금")||args[0].equalsIgnoreCase("now")||args[0].equalsIgnoreCase("nowplay")||args[0].equalsIgnoreCase("np")||args[0].equalsIgnoreCase("nowplaying")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	MusicController.now(tc, msg, event, language);
   
            }
            
            else if(args[0].equalsIgnoreCase("help")||args[0].equalsIgnoreCase("guide") || msg.getContentRaw().equalsIgnoreCase("사용법") || msg.getContentRaw().equalsIgnoreCase("설명서") || args[0].equalsIgnoreCase("도움말")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	Controller.help(tc, msg, event, language);
            }
            
            else if(args[0].equalsIgnoreCase("후원")||args[0].equalsIgnoreCase("donate")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	String langu = "군밤이에게 밥을 주세요\n**페이팔로 후원하기:** https://www.paypal.me/gunbam";
            	if(language.equals("eng")) langu = "**Donate with paypal: **https://www.paypal.me/gunbam**";
            	
            	tc.sendMessage(langu).queue();
            	logtc.sendMessage("<@297963786504110083>").queue();
            }
            
            else if(args[0].contains("info")||args[0].equalsIgnoreCase("정보")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	Controller.showInfo(tc, msg, event, language);
            }
            
            else if(args[0].equalsIgnoreCase("time")||args[0].equalsIgnoreCase("시간")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	showTime(tc, user, msg, language);}
        
            else if(args[0].equalsIgnoreCase("자동끄기")||args[0].equalsIgnoreCase("타이머")||args[0].equalsIgnoreCase("timer")||args[0].equalsIgnoreCase("autooff")) {
            	
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll(" ", "");
            	if(args.length <= 1) {
                	MusicController.nowTimer(tc, msg,  event, language);         	
                    return;
                }
                
                if(input.contains("취소")||input.contains("끄기")||input.contains("제거")||input.contains("해제")||input.contains("off")||input.contains("cancel")||input.contains("remove")||input.contains("disable")) {
                	MusicController.timerCancel(tc, msg,  event, language);
                	return;
                }
                
                if(func.isNumber(input)) {
                	if(Integer.parseInt(input) <= 0) {
                		String langu = "시간은 최소 1분 이상이어야 해요";
                		if(language.equals("eng")) 
                			langu = "Time must over 1 minute.";
                		
                		tc.sendMessage(langu).queue();
                		System.out.println("BOT: " + langu);
                		
    	        		log(tc, event, msg, "BOT: " + langu);
                	}
                	
                	else {
                		MusicController.timer(tc, msg, user, Integer.parseInt(input), event, language);
                		
                	}
                }
                else {
                	String langu = "올바르게 입력해 주세요";
            		if(language.equals("eng")) 
            			langu = "Please input correctly.";
            		
                	tc.sendMessage(langu).queue();
                	System.out.println("BOT: " + langu);
                	log(tc, event, msg, "BOT: " + langu);
                }
            	
            	
            }
            
            else if(args[0].equalsIgnoreCase("소리")||args[0].equalsIgnoreCase("음량")||args[0].equalsIgnoreCase("볼륨")||args[0].equalsIgnoreCase("sound")||args[0].equalsIgnoreCase("vol")||args[0].equalsIgnoreCase("volume")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	String vc = "";
                try{ 
                	vc =args[1];
                } 
                catch(Exception e){
                	MusicController.nowVolume(tc, msg, event, language);
                    return;
                }
                
                if(!func.isNumber(vc)) {
                	String langu = "올바르게 입력해 주세요";
                	if(language.equals("eng"))
                		langu = "Please input correctly.";
                	tc.sendMessage(langu).queue();
                	System.out.println("BOT: " + langu);
                	
	        		log(tc, event, msg, "BOT: " + langu);
                	
                	return;
                }
                
                if(Integer.parseInt(args[1])<0 || Integer.parseInt(args[1])>100) {
                	String langu = "1~100중에서 입력하세요";
                	if(language.equals("eng"))
                		langu = "Please input between 1 and 100.";
                	
                	tc.sendMessage(langu).queue();
                	System.out.println("BOT: " + langu);
                	
	        		log(tc, event, msg, "BOT: " + langu);
                	return;
                }
                
            	MusicController.volume(tc, Integer.parseInt(args[1]), event, language);
            	
            }
            
            else if(args[0].equalsIgnoreCase("ㄴㄴ")||args[0].equalsIgnoreCase("취소")||args[0].equalsIgnoreCase("cancel")||args[0].equalsIgnoreCase("return")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	MusicController.cancel(tc, msg, event, language);
            }
            
            else if(args[0].equalsIgnoreCase("저장목록")||args[0].equalsIgnoreCase("저장된목록")||args[0].equalsIgnoreCase("savedlist")||args[0].equalsIgnoreCase("savelist")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	if(guild1.getId().equals(born)) {
	            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
	            	if(args.length <= 1) {
	            		input = "1";
	            		fn = 2;
	            	}
	
	            	MusicController.savedlist(tc, msg, event, Integer.parseInt(input), fn, language);
	            	
	            	page = Integer.parseInt(input);
            	}
            	
            	else {
            		String langu = "여기서는 쓸 수 없는 기능이에요";
            		if(language.equals("eng")) langu = "Can't use it in this server.";
            		
            		tc.sendMessage(langu).queue();
            		System.out.println(guild1.toString() + " BOT: " + langu);
            		
	        		log(tc, event, msg, guild1.toString() + " BOT: " + langu);
            	}
           
            }
            
            else if(args[0].equalsIgnoreCase("저장")||args[0].equalsIgnoreCase("save")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	if(guild1.getId().equals(born)||guild1.getId().equals(base)) {
	            	
	            	MusicController.savePersonalPlaylist(tc, msg, event, event.getAuthor().getId(), language);
	            	
            	}
            	
            	else {
            		String langu = "여기서는 쓸 수 없는 기능이에요";
            		if(language.equals("eng")) langu = "Can't use it in this server.";
            		
            		tc.sendMessage(langu).queue();
            		System.out.println(guild1.toString() + "BOT: " + langu);
            		
	        		log(tc, event, msg, guild1.toString() + "BOT: " + langu);
            	}
           
            }
            
            else if(args[0].equalsIgnoreCase("멈춤")||args[0].equalsIgnoreCase("중지")||args[0].equalsIgnoreCase("나가게")||args[0].equalsIgnoreCase("낙가ㅣ")||args[0].equalsIgnoreCase("나각기")||args[0].equalsIgnoreCase("나가ㅣ")||args[0].equalsIgnoreCase("나가기")||args[0].equalsIgnoreCase("exit")||args[0].equalsIgnoreCase("out")||args[0].equalsIgnoreCase("stop")||args[0].equalsIgnoreCase("leave")||args[0].equalsIgnoreCase("disconnect")||args[0].equalsIgnoreCase("dc")){
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	Guild guild = event.getGuild();
                AudioManager manager = guild.getAudioManager();
                
                String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
                if(guild.getId().equals(born)) {
                	 MusicController.stopPlaying(tc, msg, event, 0, 2, language);
                }
                else {
                	MusicController.stopPlaying(tc, msg, event, 0, 0, language);
                }
                
	            manager.closeAudioConnection();
	
            }
            
            else if(args[0].equalsIgnoreCase("outNotSave")||args[0].equalsIgnoreCase("exitNotSave")){
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	Guild guild = event.getGuild();
                AudioManager manager = guild.getAudioManager();
                
              
                MusicController.stopPlaying(tc, msg, event, 0, 0, "kor");
	            manager.closeAudioConnection();
 
            }
            
            else if(args[0].equalsIgnoreCase("핑")||args[0].equalsIgnoreCase("ping")||args[0].equalsIgnoreCase("PING")||args[0].equalsIgnoreCase("PONG")||args[0].equalsIgnoreCase("pong")||args[0].equalsIgnoreCase("지연시간")||args[0].equalsIgnoreCase("네트워크")||args[0].equalsIgnoreCase("latency")||args[0].equalsIgnoreCase("network")||args[0].equalsIgnoreCase("NETWORK")||args[0].equalsIgnoreCase("LATENCY")) {
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	MusicController.ping(guild1, tc, msg, event, language);
            }
            
            else if(args[0].equalsIgnoreCase("업")||args[0].equalsIgnoreCase("ㅕㅅ")||args[0].equalsIgnoreCase("업타임")||args[0].equalsIgnoreCase("uptime")||args[0].equalsIgnoreCase("up")) {
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	
            	long nowTime = System.currentTimeMillis();
            	Controller.upTime(tc, msg, event, (int)(nowTime - gunbamStartTime), language);
            	
            }
             
        }
        
    }

	public void showTime(TextChannel tc, User user, Message msg, String lan) {
		SimpleDateFormat time = new SimpleDateFormat("HH시 mm분 ss초");
		String timeStr = time.format (System.currentTimeMillis());
		
		String language = "현재 **" + timeStr + "** 에요";
		if(lan.equals("eng")) {
			language = timeStr + " now. (South Korea)";
		}
		tc.sendMessage(language).queue();
		
		System.out.println("");
		System.out.println(user.toString() + ": " + msg.getContentRaw());
    	System.out.println("BOT: 현재 **" + timeStr + "** 에요");
  
	}
	
	public void searchPeopleSelectGuild(TextChannel tc, Message msg, MessageReceivedEvent event) {
		String str1 = msg.getContentRaw().replaceAll(" ", "");
		String split1 = str1.split("에서")[0];
		String split2 = str1.split("에서")[1];

		String atGuild = split1.replaceAll("[^0-9]", "");
		String object = split2.replaceAll("[^0-9]", "");
    	String who = "nullgunbam";
    	
    	String nickName = "nullgunbam";
    	List<Member> listed = new ArrayList<>();
    	List<Object> roles = new ArrayList<>();
    
    	try {
    		roles.clear();
	    	listed.clear();
	    	listed.addAll(event.getJDA().getGuildById(atGuild).getMembers());
    	}
    	catch(Exception e) {
    		tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
    		return;
    	}
    	
    	for(int i = 0; i<listed.size(); i++) {
    		if(listed.get(i).toString().contains(object)) {
    			who = listed.get(i).getUser().getName();
    			
    			if(listed.get(i).getNickname() != null)
    				nickName = listed.get(i).getNickname();
    			
    			roles.add(listed.get(i).getRoles());
    		}
    	}
    	
    	if(who.equals("nullgunbam")) {
    		tc.sendMessage("누군지 못 찾았어요").queue();
    	}
    	
    	else {
    		String strs = "";
    		if(nickName.equals("nullgunbam")) {
    			strs = "`" + who + "` 이네요";
    		}
    		else {
    			strs = "`" + nickName + "` 이고 이름은 `" + who + "` 이에요";
    		}
    		
    		StringBuilder s = new StringBuilder();
    		s.append("직업은 **");
    		
    		if(roles.isEmpty()) {
    			s.append("없어요**");
    		}
    		else {
	    		for(int i = 0; i<roles.size(); i++) {
	    			if(i == roles.size() -1) s.append(roles.get(i).toString() + "** 이에요");
	    			else s.append(roles.get(i).toString() + ", ");
	    		}
    		}
    		
    		tc.sendMessage(strs + ". " + s).queue();
    	}
	}
	
	public void searchPeople(TextChannel tc, Message msg, Message response) {
		long startTime = System.currentTimeMillis();
		try {
			String str = msg.getContentRaw().replaceAll("[^0-9]", "");
	
	    	String who = null;
	    	StringBuilder where = new StringBuilder();
	    	
	    	List<Guild> listedGuild = new ArrayList<>();
	    	List<Member> listed = new ArrayList<>();
	    	List<Guild> inGuilds = new ArrayList<>();
	    
	    	
	    	listedGuild.clear();
	    	listedGuild.addAll(tc.getJDA().getGuilds());
	    	
	    	end: //중도탈출 할 이름 설정
	    	for(int g = 0; g<listedGuild.size(); g++) {
		    	listed.clear();
		    	listed.addAll(listedGuild.get(g).getMembers());
		    	
		    	for(int i = 0; i<listed.size(); i++) {
		    		if(listed.get(i).getUser().getId().equals(str)) {
		    			who = listed.get(i).getUser().getName();
		    			
		    			inGuilds.addAll(listed.get(i).getUser().getMutualGuilds());
		    			
		    			for(int ig = 0; ig<inGuilds.size(); ig++) {
		    				where.append("  " + inGuilds.get(ig) + "\n");
		    				
		    				//만약 user 조사가 마쳤으면 강제로 for문 종료
		    				if(ig == inGuilds.size() - 1) {
		    					
		    					break end;
		    				}
		    			}
		    		}
		    	}
	    	}
	    	
	    	if(who == null) {
	    		response.editMessage("누군지 못 찾았어요").queue();
	    	}
	    	
	    	else {
	    		long endTime = System.currentTimeMillis();
	    		try {
	    			response.editMessage("`" + who + "` 이네요\n```속해 있는 서버 목록\n\n" + where + "\n" + (long)(endTime - startTime) + "ms```").queue();
	    		}
	    		catch(Exception e) {
	    			response.editMessage(":no_entry_sign: **" + e.getMessage() + func.cause(e)).queue();
	    		}
	    	}
		}
		catch(Exception e) {
			response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));
		}
	}

	
	public void log(TextChannel tc, MessageReceivedEvent event, Message msg, String str) {
		if(logOn == 1) {
			if(user.toString().contains(admin)||tc.getGuild().toString().contains(base)||tc.getId().equals("717203670365634621")) {}
			else 
				logtc.sendMessage(str).queue();
		}
			
		File file = new File(BotMusicListener.directoryDefault + "log.txt");
		
		try {
		      FileWriter fw = new FileWriter(file, true);
		      fw.append("\n" + str);
		      
		      fw.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		      logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
		    }
		}
	
	public void clearLog(TextChannel tc, MessageReceivedEvent event, Message msg) {
		File file = new File(BotMusicListener.directoryDefault + "log.txt");
		
		try {
		      FileWriter fw = new FileWriter(file, true);
		      fw.write(" ");
		      
		      fw.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		      logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
		    }
		}	
	
	
	public static boolean getType(String word) {
		return Pattern.matches("^[a-zA-Z]*$", word);
	}
}

