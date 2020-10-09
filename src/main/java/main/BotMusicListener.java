package main;

import java.awt.Color;
import java.io.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


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
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import normal.Controller;
import play.PlayController;
import audio.MusicController;
import functions.*;


public class BotMusicListener extends ListenerAdapter {
	
	public static String chatVersion = "2020년 6월";
	public static String musicVersion = "2020년 9월";
	public static String musicVersionEng = "2020.09";
	
	CustomFunctions func = new CustomFunctions();

	public static int ret = 0;
	
	public static TextChannel adtc, logtc, listentc, dokingtc, voiceTc, jdatc, errortc;

	public static int logOn = 1;
	public static int listen = 0;
	public static int allExitAlert = 0;
	public static User user = null;
	
	public static String admin = "297963786504110083", bot = "";
	
	public static String colorDefault = "#dddddd"; //dddddd, fff8dc
	public static String colorSetTimer = "#222222";
	
	public static String directoryDefault = "files/";
	public static String base = "661044497198743640", baseVoice = "705715643063861321"; // born = "533443336678146078";
	public static int queryCount = 9;
	public static HashMap<Guild, String> voiceTcMessage = new HashMap<Guild, String>();
	
	public static String prefix = "";
	public static String dokingStr = "686949011705430062";

	int isError = 0;
	
	public static int disconnectedCode = 0;
	public static DisconnectEvent disconnectedReasonEvent;

	public static Message deleted, inputClear, updateResponse;
	
	public static long gunbamStartTime = 0;
	
	public static StringBuilder voiceStats(StringBuilder s, List<Member> members) {
		
		 if(members.size() == 0) {
		 	s.append("음성채널에 아무도 없어요");
		 }
		 else {
			 for(int i=0; i<members.size(); i++) {
				 StringBuilder now = new StringBuilder();
				if(members.get(i).getUser().isBot()) now.append(" [BOT]");
		 		if(members.get(i).getVoiceState().isDeafened() == true) now.append(" Deaf");
		 		if(members.get(i).getVoiceState().isStream() == true) now.append(" Stream");
		 			
			 	if(members.get(i).getNickname() == null) {
			 		s.append("U:" + members.get(i).getUser().getName() + "(" + members.get(i).getId() + ")" + now + "\n");	
			 	}
			 	else {
			 		s.append("MB:" + members.get(i).getNickname() + "(" + members.get(i).getId() + ")" + now + "\n");	
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
       
        if(Main.num.equals("1")) {
        	
		    Runnable remove1 = () -> {
			    try {
				    MessageHistory mh1 = new MessageHistory(t);
				    List<Message> mh11 = mh1.retrievePast(9).complete();
		
				    try {
				    	t.deleteMessages(mh11).complete();
				    }
				    catch(Exception e) {
				    	for(int i = 0; i<mh11.size(); i++) {
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
			        	
			   
		    };
		    Thread t1 = new Thread(remove1);
		    t1.start();
        
        	
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
        }
    
        File fileUpdate = new File(BotMusicListener.directoryDefault + "updateMessageId.txt");
        String updateMessageId = "";
    	try {
    		FileReader filereader = new FileReader(fileUpdate);
    	     
    	    BufferedReader bufReader = new BufferedReader(filereader);
    	    String line = "";
    	   
    	    while((line = bufReader.readLine()) != null){
    	       updateMessageId = line;
    	    }
    	               
    	    bufReader.close();
    	}
    	catch(Exception e) {
    		 t.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
    	}
    	
    	if(!updateMessageId.equals("")) {
    		if(Main.num.equals("1"))
    			t.sendMessage("군밤이 업데이트 되었어요").queue();
    		try {
    			TextChannel value = event.getJDA().getTextChannelById(updateMessageId.split("/")[0]);
    			func.removeMessage(value, updateMessageId.split("/")[1], updateMessageId.split("/")[2]);
    		}
    		catch(Exception e) {}
    		
    		
    		try {
    			FileWriter fw = new FileWriter(fileUpdate);
    		    fw.write("");
    		      
    		    fw.close();
    		} 
    		catch (Exception e) {
    		    e.printStackTrace();
    		    logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
    		}
    	}
    	else {
    		if(Main.num.equals("1"))
    			t.sendMessage("군밤이 가동을 시작했어요").queue();
    	}
    	
        MusicController.prepare(adtc, null, null);
        System.out.println("BOT: 군밤이 가동을 시작헀어요");
        
        Runnable send = () -> {
			File file = new File(BotMusicListener.directoryDefault + "guild/usingGuilds.txt");
				
			try{   
		        FileReader filereader = new FileReader(file);
		        BufferedReader bufReader  =  new BufferedReader(filereader);
	
		        String line = "";
		        while((line = bufReader.readLine()) != null){
		            try {
			            event.getJDA().getTextChannelById(line.split("/")[1]).sendMessage(":mega: 봇이 " + Main.because + " 되었어요").queue();
			            if(Main.num.equals("1")) 
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
        
        Runnable restarted = () -> {
	        File file = new File(BotMusicListener.directoryDefault + "restartMessageId.txt");
	        String restartMessageId = "";
	    	try {
	    		FileReader filereader = new FileReader(file);
	    	     
	    	    BufferedReader bufReader = new BufferedReader(filereader);
	    	    String line = "";
	    	   
	    	    while((line = bufReader.readLine()) != null){
	    	        restartMessageId = line;
	    	    }
	    	               
	    	    bufReader.close();
	    	}
	    	catch(Exception e) {
	    		 t.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
	    	}
	    	
	    	if(!restartMessageId.equals("")) {
	    		
	    		try {
	    			event.getJDA().getTextChannelById(restartMessageId.split("/")[0]).editMessageById(restartMessageId.split("/")[1], "봇이 재시작되었어요").queue();
	    		}
	    		catch(Exception e) {}
	    		
	    		
        		try {
        			FileWriter fw = new FileWriter(file);
        		    fw.write("");
        		      
        		    fw.close();
        		} 
        		catch (Exception e) {
        		    e.printStackTrace();
        		    logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
        		}
	    	}
        };
        Thread t4 = new Thread(restarted);
        t4.start();
        
	}
	
	
	@Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		
		int mat = 0;
		
		if(event.getVoiceState().getChannel().getMembers().contains(event.getGuild().getSelfMember()))
			mat = 1;
		
		String value = "";
    	try {
    		value = voiceTcMessage.get(event.getGuild());
    	}
    	catch(Exception e) {}
    	
	    if(mat == 0) return;

		ArrayList<Member> members = new ArrayList<>();
		members.addAll(event.getVoiceState().getChannel().getMembers());
		
        if(members.size() > 1) {
    		MusicController.autoResume(event.getGuild(), event, members, value, null);
    	}
        else {
        		
        	StringBuilder s = new StringBuilder();
			StringBuilder t = new StringBuilder();
				
			String title = "**" + event.getGuild().getName() + "/" + event.getChannelJoined().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
			if(event.getGuild().getAudioManager().isSelfMuted() == true) {
				title = "**(일시정지) " + event.getGuild().getName() + "/" + event.getChannelJoined().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
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
				    	voiceTcMessage.put(event.getChannelJoined().getGuild(), response.getId());
				
				   	});
				}
			};
			Thread t1 = new Thread(edit);
			t1.start();

	    }
  
    }
	
	@Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		
		if(event.getGuild().getAudioManager().isConnected() == false) return;
		
		boolean inBot = false;
		
		if(event.getMember().getVoiceState().getChannel().getMembers().contains(event.getGuild().getSelfMember()))
			inBot = true;
		

        if(inBot == false) return;

		String value = "";
        
        try {
    		value = voiceTcMessage.get(event.getGuild());
    	}
    	catch(Exception e) {}
		
        ArrayList<Member> members = new ArrayList<>();
        
        if(event.getMember().getId().equals(bot)) {
			members.addAll(event.getChannelJoined().getMembers());
        }
        else {
			members.addAll(event.getGuild().getAudioManager().getConnectedChannel().getMembers());
        }
        	

		if(members.size() > 1) {
			MusicController.autoResume(event.getGuild(), null, members, value, event);
			return;
		}
		else if(members.size() <= 1) {
			MusicController.autoPauseToMove(event.getGuild(), event);
				
			StringBuilder s = new StringBuilder();
		    StringBuilder t = new StringBuilder();
		    	
		   
		    Object o = event.getGuild().getAudioManager().getConnectedChannel().getName();
		    if(event.getMember().getId().equals(bot)) {
		    	o = event.getChannelJoined().getName();
		    }
		    
		    String title = "**" + event.getGuild().getName() + "/" + o + "**의 인원 (" + members.size() + "명)\n```css\n";
		    if(event.getGuild().getAudioManager().isSelfMuted() == true) {
		    	title = "**(일시정지) " + event.getGuild().getName() + "/" + o + "**의 인원 (" + members.size() + "명)\n```css\n";
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
		
		System.out.println("연결 끊김 코드: **" + event.getCloseCode() + "**, " + event.getClientCloseFrame());
		
		if(event.getClientCloseFrame().getCloseCode() == 1008||event.getClientCloseFrame().getCloseCode() == 1000) {
			MusicController.disconnect(event.getClientCloseFrame().getCloseCode(), event);
			MusicController.editguilds(adtc, null, null);
		}
		
		else if(event.getClientCloseFrame().getCloseCode() != 4900) {
			MusicController.disconnect(event.getClientCloseFrame().getCloseCode(), event);
			
		}
		
		jdatc.sendMessage("연결 끊김 코드: **" + event.getCloseCode() + "** `" + event.getClientCloseFrame() + "`").queue();
	}
	
	@Override
    public void onResume(ResumedEvent event) {
		MusicController.reconnect();
		jdatc.sendMessage("- resume됨").queue();
	}
	
	@Override 
	public void onReconnect(ReconnectedEvent event) {
		MusicController.reconnect();
		jdatc.sendMessage("- reconnect됨").queue();
	}
	
	@Override
	public void onGuildVoiceStream(GuildVoiceStreamEvent event) {
		if(event.getGuild().getAudioManager().isConnected() == false) return;
		try {
			ArrayList<Member> members = new ArrayList<>();
			members.addAll(event.getGuild().getAudioManager().getConnectedChannel().getMembers());
			
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
	
	@Override
	public void onGuildVoiceSelfDeafen(GuildVoiceSelfDeafenEvent event) {
		
		if(event.getGuild().getAudioManager().isConnected() == false) return;
		
		try {
			ArrayList<Member> members = new ArrayList<>();
			members.addAll(event.getGuild().getAudioManager().getConnectedChannel().getMembers());
			
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
	
	/*
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
				
				if(event.getGuild().getAudioManager().isSelfMuted() == false) return;
			   
				title = "**(일시정지) " + event.getGuild().getName() + "/" + event.getGuild().getMemberById(bot).getVoiceState().getChannel().getName() + "**의 인원 (" + members.size() + "명)\n```css\n";
				
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
	*/
	
	@Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		
		if(event.getMember().getId().equals(bot)) {
			MusicController.disconnectVoice(event.getGuild(), event.getChannelLeft());
	    }
	    else {

	    	if(event.getGuild().getAudioManager().isConnected() == false) return;
	    	
	    	ArrayList<Member> members = new ArrayList<>();
	    	members.addAll(event.getChannelLeft().getMembers());
	    	
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
		
		Runnable delete = () -> {
			String eachId = voiceTcMessage.get(guild);
			
			try {
				voiceTc.deleteMessageById(eachId).complete();
			}
			catch(Exception e) {}
			
			if(MusicController.isInTotal == 0) {
				if(allExitAlert == 1) {
					MusicController.updateGunBam(adtc, updateResponse);
					allExitAlert = 0;
					
				}
			}
		};
		Thread t = new Thread(delete);
		t.start();
	}
	
	
	// message receive
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {

		user = event.getAuthor(); 
        final TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();
        Guild guild1 = event.getGuild();
        
       
        if(!event.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_WRITE)) {
        	return;
        }
        
        if(user.toString().contains(bot)) { //군밤
            if(msg.getContentRaw().equals("ㅣ나가기")||msg.getContentRaw().equals("$나가기")) {
            	Guild guild = event.getGuild();
            	
                if(Main.saveQueueAllowGuild.contains(guild.getId())) {
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
        	
        	if(tc.toString().contains("686949011705430062")&&user.getId().equals(admin)) {
        		dokingtc.sendMessage(msg.getContentRaw()).queue();
        	}
        }

        
        if((guild1.getId().equals("374071874222686211")&&msg.getContentRaw().startsWith("$")) || msg.getContentRaw().startsWith("$botify")) return;
        
	    
        //is bot return
        if(user.isBot())  {
        	return;}
 
        
        //새벽반의 general채널에선 불가
        if(!Main.muteChannels.contains(tc.getId()) && msg.getContentRaw().contains("군밤")&& (msg.getContentRaw().contains("설명서") || msg.getContentRaw().contains("사용법") || msg.getContentRaw().contains("도움말"))){
        	System.out.println("");
    		System.out.println(guild1.toString() + "\n" + user.toString() + ": " + msg.getContentRaw());
    		Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
        	log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	Controller.help(tc, msg, event, "kor");
        	
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("일전")||msg.getContentRaw().contains("일 전"))){
        	System.out.println("");
        	System.out.println(guild1.toString() + "\n" + user.toString() + ": " + msg.getContentRaw());
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
        	log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
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
        	log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
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
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("분")&&(msg.getContentRaw().contains("후")||msg.getContentRaw().contains("뒤"))&&(msg.getContentRaw().contains("알려")||msg.getContentRaw().contains("알랴"))) {
        	String min = msg.getContentRaw().replaceAll("[^0-9]", "");

        	Controller.timer(user, tc, msg, event, Integer.parseInt(min));
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("타이머")&&msg.getContentRaw().contains("분")&&(msg.getContentRaw().contains("설정")||msg.getContentRaw().contains("맞춰")||msg.getContentRaw().contains("세팅"))) {
        	String min = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	Controller.timer(user, tc, msg, event, Integer.parseInt(min));
        }
       
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("타이머")&&(msg.getContentRaw().contains("없애")||msg.getContentRaw().contains("지워")||msg.getContentRaw().contains("삭제")||msg.getContentRaw().contains("취소")||msg.getContentRaw().contains("제거"))) {
        	
        	Controller.timerCancel(tc, msg, event);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("타이머")&&msg.getContentRaw().contains("다시")) {
        	
        	Controller.reTimer(tc, msg, event);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("타이머")) {
        	
        	Controller.nowTimer(tc, msg, event);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("몇시")||msg.getContentRaw().contains("몇분")||msg.getContentRaw().contains("몇초")||msg.getContentRaw().contains("몇 시")||msg.getContentRaw().contains("몇 분")||msg.getContentRaw().contains("몇 초"))){
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	
        	showTime(tc, user, msg, "kor");
        	
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("=")||msg.getContentRaw().contains("계산"))) {
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	
        	Controller.calculate(tc, msg, event);
        }
        
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("16진수")||msg.getContentRaw().contains("8진수")||msg.getContentRaw().contains("2진수")||msg.getContentRaw().contains("16진법")||msg.getContentRaw().contains("10진법")||msg.getContentRaw().contains("8진법")||msg.getContentRaw().contains("2진법")) && (msg.getContentRaw().contains("변환")||msg.getContentRaw().contains("바꿔"))) {
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	
        	Controller.translateType(tc, msg, event);
        }
  
        //getInfoJDA
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("정보")) {
        	String checkNum = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
        		String str = "링크 첨부";
    			
    			tc.sendMessage(func.askPermission(str, "kor")).queue();
    			log(tc, event, msg, "BOT: " + str);
            	return;
            }
        	
        	if(checkNum.equals("")) {
        		if(msg.getContentRaw().contains("내")||msg.getContentRaw().contains("나")) {
        			EmbedBuilder eb = new EmbedBuilder();
    	        	eb.setColor(Color.decode(colorDefault));
    	        	eb.setAuthor("집계 중...");
    	        	
    	        	tc.sendMessage(eb.build()).queue(response -> {
    	        		Controller.showMyInfo(tc, msg, event, "kor", response);
    	        	});
        		}
        		else {
        			Controller.showInfo(tc, msg, event, "kor");
        		}
        	}
        	else {
	        	EmbedBuilder eb = new EmbedBuilder();
	        	eb.setColor(Color.decode(colorDefault));
	        	eb.setAuthor("집계 중...");
	        	
	        	tc.sendMessage(eb.build()).queue(response -> {
	        		informationAboutId(tc, msg, response, checkNum);
	        	});
        	}

        }
   
        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("사진")||msg.getContentRaw().contains("프로필")||msg.getContentRaw().contains("프사"))) {
        	String str = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	EmbedBuilder eb = new EmbedBuilder();
        	eb.setColor(Color.decode(colorDefault));
        	eb.setTitle("불러오는 중...");
        	
        	if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
        		String message = "링크 첨부";
    			
    			tc.sendMessage(func.askPermission(message, "kor")).queue();
    			log(tc, event, msg, "BOT: " + message);
            	return;
            }
        	
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
       
        else if(user.getId().equals(admin) && (msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("누구")||msg.getContentRaw().contains("누군")||msg.getContentRaw().contains("알아")||msg.getContentRaw().contains("찾아")||msg.getContentRaw().contains("조사")))){
        	if(msg.getContentRaw().contains("에서")) {
        		searchPeopleSelectGuild(tc, msg, event);
        	}
        	
        	else {
        		
        		tc.sendMessage("조사 중...").queue(response -> {
        			searchPeople(tc, msg, response);
        		});

        	}
        	
        }
        
        else if(user.getId().equals(admin) && (msg.getContentRaw().equals("ㅣ프로세스강제종료")||msg.getContentRaw().equals("ㅣ프로세스 강제종료")||msg.getContentRaw().equals("ㅣ프로세스 강제중지")||msg.getContentRaw().equals("ㅣ프로세스강제중지")||msg.getContentRaw().equals("ㅣ프로그램 강제종료")||msg.getContentRaw().equals("ㅣ프로그램 강제중지")||msg.getContentRaw().equals("ㅣ프로그램강제종료")||msg.getContentRaw().equals("ㅣ프로그램강제중지"))) {
	        tc.sendMessage("군밤이를 종료해요").queue();
	        System.exit(0);
        }
        
        else if(user.getId().equals(admin) && (msg.getContentRaw().equals("ㅣ프로세스종료")||msg.getContentRaw().equals("ㅣ프로세스 종료")||msg.getContentRaw().equals("ㅣ프로세스 중지")||msg.getContentRaw().equals("ㅣ프로세스중지")||msg.getContentRaw().equals("ㅣ프로그램 종료")||msg.getContentRaw().equals("ㅣ프로그램 중지")||msg.getContentRaw().equals("ㅣ프로그램종료")||msg.getContentRaw().equals("ㅣ프로그램중지"))) {
        	if(MusicController.isInTotal == 0) {
	        	tc.sendMessage("군밤이를 종료해요").queue();
	        	event.getJDA().shutdown();
	        	System.exit(0);
        	}
        	else {
        		tc.sendMessage("이용중인 사람이 있어요").queue();
        	}
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("주사위")) {
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
        
        else if(msg.getContentRaw().equals("가위")||msg.getContentRaw().equals("바위")||msg.getContentRaw().equals("보")||msg.getContentRaw().equals("묵")||msg.getContentRaw().equals("찌")||msg.getContentRaw().equals("빠")||msg.getContentRaw().equals("보자기")||msg.getContentRaw().equals("주먹")) {
        	
        	PlayController.receiveRsp(tc, msg, event, "receive", msg.getContentRaw());
        }
        
        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("가위바위보")) {
        	Date date = new Date();
        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    		String str = dayTime.format(date);
    		log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        	
    		if(msg.getContentRaw().contains("멀티")||msg.getContentRaw().contains("같이"))
    			PlayController.rspMulti(tc, msg, event);
    		else 
    			PlayController.rsp(tc, msg, event);
        }
        
        
        //명령어 시작
        for(int i = 0; i<Main.ignorePrefix.size(); i++) {
        	if(msg.getContentRaw().startsWith(Main.ignorePrefix.get(i)))
        		return;
        }
        
        if(msg.getContentRaw().startsWith("ㅣ")||msg.getContentRaw().startsWith("$")) {}
        else {
        	return;
        }
        
        if(guild1.toString().contains("264445053596991498")) return;
        if(isError == 1) {
        	if(user.getId().equals(admin)) {}
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
       	
        	
        	if(user.getId().equals(admin)) {}
        	else { //로그찍기
	        	System.out.println("");
	        	System.out.println(guild1 + "\n" + user.toString() + ": " + msg.getContentRaw());        		
	        	Date date = new Date();
		        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
		    	String str = dayTime.format(date);
	            log(tc, event, msg, ".\n.\n" + str + "\n__" + guild1 + "__ `" + tc + "`\n" + user.toString() + ": " + msg.getContentRaw() + "\n");
        		
        	}
   
        	
           if(args.length <= 0) return;
            
           if((user.getId().equals(admin)||Main.clearAllowUser.contains(user.getId()))&&(args[0].equalsIgnoreCase("myclear")||args[0].equalsIgnoreCase("mc")||args[0].equalsIgnoreCase("ㅡㅊ"))){
            	
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
            
            else if((user.getId().equals(admin)||Main.clearAllowUser.contains(user.getId()))&&(args[0].equalsIgnoreCase("c")||args[0].equalsIgnoreCase("clear")||args[0].equalsIgnoreCase("ㅊ"))){
            	
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
        
            else if(user.getId().equals(admin)&&args[0].equalsIgnoreCase("lock")){
            	MusicController.lock(tc, event, 1);
            }
            
            else if(user.getId().equals(admin)&&args[0].equalsIgnoreCase("상황실")){
            	tc.sendMessage("저장 중...").queue(response -> {
            		MusicController.memoSave(tc, msg, event, response);
            	});
            	
            }
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("완료")||args[0].equalsIgnoreCase("complete"))){
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
                    	
                    	if(comp.getGuild().getSelfMember().hasPermission(comp, Permission.MESSAGE_EMBED_LINKS)) {
                    		comp.sendMessage(msgs.build()).queue();
        	            }
                    	
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
            	
            	tc.sendMessage("**" + count + "개** 채널에 업데이트 공지했어요").queue();
            	coun = 0;
            }
            
            else if(user.getId().equals(admin)&&args[0].equalsIgnoreCase("unlock")){
            	MusicController.lock(tc,event, 0);
            }
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("listen")||args[0].equalsIgnoreCase("도킹"))) {
            	
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
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("getTextChannel")||args[0].equalsIgnoreCase("채널보기"))) {
            	
            	String vc = "";
                try{ 
                	vc =args[1];

                } catch(Exception e){
                	                	 
                    return;
                }

                MusicController.getListen(tc, msg, event, vc);
	
            }
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("도킹그만")||args[0].equalsIgnoreCase("도킹멈춤")||args[0].equalsIgnoreCase("도킹나가기")||args[0].equalsIgnoreCase("도킹취소")||args[0].equalsIgnoreCase("도킹끄기"))) {
            	tc.sendMessage("```도킹을 그만해요```").queue();
            	listen = 0;
            }
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("업데이트")||args[0].equalsIgnoreCase("update"))) {
            	if(args.length <= 1) {
	            	String str = "봇을 컴파일 후 실행해요";
	            	if(MusicController.isInTotal != 0) {
	            		str = "모든 음성채널에서 군밤이 나가면 재시작해요";
		            	allExitAlert = 1;
	            	}
	            	
	            	tc.sendMessage(str).queue(response -> {
	            		updateResponse = response;
	        			File file = new File(BotMusicListener.directoryDefault + "updateMessageId.txt");
	            		
	            		try {
	            			FileWriter fw = new FileWriter(file);
	            		    fw.write(tc.getId() + "/" + msg.getId() + "/" + response.getId());
	            		      
	            		    fw.close();
	            		} 
	            		catch (Exception e) {
	            		    e.printStackTrace();
	            		    logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
	            		}
	            		
		            	if(MusicController.isInTotal == 0)
		            		MusicController.updateGunBam(tc, response);
	        		});
	        		
            	}
            	
            	else {
            		if(args[1].equals("취소") || args[1].equalsIgnoreCase("cancel")) {
            			if(allExitAlert != 0) {
	            			tc.sendMessage("업데이트 예약을 취소했어요").queue(response -> {
	            				File file = new File(BotMusicListener.directoryDefault + "updateMessageId.txt");
	    	            		
	    	            		try {
	    	            			FileWriter fw = new FileWriter(file);
	    	            		    fw.write("");
	    	            		      
	    	            		    fw.close();
	    	            		} 
	    	            		catch (Exception e) {
	    	            		    e.printStackTrace();
	    	            		    logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
	    	            		}
	            			});
	            			allExitAlert = 0;
            			}
            			else
            				tc.sendMessage("이미 업데이트 예약을 하지 않았어요").queue();
            		}
            	}
            }
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("saveDefault")||args[0].equalsIgnoreCase("sd")||args[0].equalsIgnoreCase("ㄴㅇ"))) {
            	
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
            
            else if(user.getId().equals(admin)&&args[0].equalsIgnoreCase("점검")) {
            	
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
 
            else if(user.getId().equals(admin)&&args[0].equalsIgnoreCase("alert")){
            	
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
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("보완")||args[0].equalsIgnoreCase("수정")||args[0].equalsIgnoreCase("fix"))){
            	tc.sendMessage("수정 중...").queue(response -> {
            		MusicController.fixGuilds(tc, msg, event, response);
            	});
            	
            }
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("clearLog")||args[0].equalsIgnoreCase("로그지우기"))){
            	clearLog(tc, event, msg);
            	tc.sendMessage("로그를 클리어했어요").queue();
            }
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("logOn")||args[0].equalsIgnoreCase("로그켜기"))){
            	clearLog(tc, event, msg);
            	tc.sendMessage("로그를 켜요").queue();
            	logOn = 1;
            }
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("logOn")||args[0].equalsIgnoreCase("로그끄기"))){
            	clearLog(tc, event, msg);
            	tc.sendMessage("로그를 꺼요").queue();
            	logOn = 0;
            }
        
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("saveGuilds")||args[0].equalsIgnoreCase("길드저장"))) {
            	MusicController.saveGuilds(tc, event);
            }
            
            else if(user.getId().equals(admin)&&args[0].equalsIgnoreCase("print")){
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
           
            else if(args[0].equalsIgnoreCase("힙")||args[0].equalsIgnoreCase("heap")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
        		Controller.heap(tc, msg, event, language);
            }
           

            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("주소")||args[0].equalsIgnoreCase("주소받기")||args[0].equalsIgnoreCase("주소정보"))) {
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
            
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("apply")||args[0].equalsIgnoreCase("적용"))) {
            	
                if(args.length > 2) {
                		
                    if(args[2].length() != 18) {
                    	tc.sendMessage("ID가 올바르지 않아요").queue();
                    	return;
                    }
                    	
                    if(args[1].equals("1")||args[1].equals("2")||args[1].equals("3")||args[1].equals("4")||args[1].equals("5")||args[1].contains("saveQueueAllowGuild")||args[1].contains("loadAllowGuild")||args[1].contains("loadAllowUser")||args[1].contains("muteChannel")||args[1].contains("clearAllowUser"))
                		Controller.applyDirectly(tc, msg, event, args[1], args[2]);
                    else {
                		tc.sendMessage("적용위치```css\n1: saveQueueAllowGuild.txt\n2: loadAllowGuild.txt\n3: loadAllowUser.txt\n4: muteChannels.txt\n5: clearAllowUser.txt```").queue(response -> {
                			response.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
                			msg.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
                		});
                	}
                }
            		
                else if(args.length == 1) {
                	tc.sendMessage("적용위치```css\n1: saveQueueAllowGuild.txt\n2: loadAllowGuild.txt\n3: loadAllowUser.txt\n4: muteChannels.txt\n5: clearAllowUser.txt```").queue(response -> {
		            	response.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
		            	msg.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
		            });
                	
                }
                else {
                	if(args[1].equals("1")||args[1].equals("2")||args[1].equals("3")||args[1].equals("4")||args[1].equals("5")||args[1].contains("saveQueueAllowGuild")||args[1].contains("loadAllowGuild")||args[1].contains("loadAllowUser")||args[1].contains("muteChannel")||args[1].contains("clearAllowUser"))
                		Controller.apply(tc, msg, event, Integer.parseInt(args[1]));
                	else {
	                	tc.sendMessage("적용위치```css\n1: saveQueueAllowGuild.txt\n2: loadAllowGuild.txt\n3: loadAllowUser.txt\n4: muteChannels.txt\n5: clearAllowUser.txt```").queue(response -> {
		            		response.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
		            		msg.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
		            	});
                	}
                	
                }	
            }
           
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("discard")||args[0].equalsIgnoreCase("적용취소")||args[0].equalsIgnoreCase("적용해제"))) {
            	if(args.length < 2) {
            		tc.sendMessage("적용해제위치```css\n1: saveQueueAllowGuild.txt\n2: loadAllowGuild.txt\n3: loadAllowUser.txt\n4: muteChannels.txt\n5: clearAllowUser.txt```").queue(response -> {
        				response.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
        				msg.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
        			});
            		return;
            	}
            	
            	if(args.length > 2) {

                	if(args[2].length() != 18) {
                		tc.sendMessage("ID가 올바르지 않아요").queue();
                		return;
                	}
                	
	            	if(args[1].equals("1")||args[1].equals("2")||args[1].equals("3")||args[1].equals("4")||args[1].equals("5")||args[1].contains("saveQueueAllowGuild")||args[1].contains("loadAllowGuild")||args[1].contains("loadAllowUser")||args[1].contains("muteChannel")||args[1].contains("clearAllowUser"))
	            		Controller.discardDirectly(tc, msg, event, args[1], args[2]);
	            	else {
	            		tc.sendMessage("적용해제위치```css\n1: saveQueueAllowGuild.txt\n2: loadAllowGuild.txt\n3: loadAllowUser.txt\n4: muteChannels.txt\n5: clearAllowUser.txt```").queue(response -> {
	            			response.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
	            			msg.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
	            		});
	            	}
            	}
            	else {
            		tc.sendMessage("적용해제위치```css\n1: saveQueueAllowGuild.txt\n2: loadAllowGuild.txt\n3: loadAllowUser.txt\n4: muteChannels.txt\n5: clearAllowUser.txt```").queue(response -> {
            			response.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
            			msg.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
            		});
            	}
            
            }
           
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("nicknameSynchronized")||args[0].equalsIgnoreCase("닉네임동기화")||args[0].equalsIgnoreCase("닉네임연동"))) {
            	if(args.length < 2) {
            		Controller.showNicknameSynchronized(tc, msg, event);
            	}
            	else {
            		if(args[1].length() != 18 && !args[1].equals("null"))
            			tc.sendMessage("ID가 올바르지 않아요").queue();
            		else
            			Controller.setNicknameSynchronized(tc, msg, event, args[1]);
            	}
            }
           
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("showSaveQueueAllowGuild")||args[0].equalsIgnoreCase("다시재생허용길드")||args[0].equalsIgnoreCase("다시재생허용서버")||args[0].equalsIgnoreCase("다시재생허용목록"))) {
            
            	Controller.showSaveQueueAllowGuild(tc, msg, event);
            }
           
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("showLoadAllowUser")||args[0].equalsIgnoreCase("showLoadAllowGuild")||args[0].equalsIgnoreCase("불러오기허용길드")||args[0].equalsIgnoreCase("불러오기허용서버")||args[0].equalsIgnoreCase("불러오기허용유저")||args[0].equalsIgnoreCase("불러오기허용목록"))) {
                
            	Controller.showLoadAllow(tc, msg, event);
            }
           
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("showMuteChannel")||args[0].equalsIgnoreCase("재생불가목록")||args[0].equalsIgnoreCase("재생불가채널")||args[0].equalsIgnoreCase("재생불가채널목록"))) {
                
            	Controller.showMuteChannel(tc, msg, event);
            }
           
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("showClearAllowUser")||args[0].equalsIgnoreCase("지우기허용유저")||args[0].equalsIgnoreCase("clear허용유저")||args[0].equalsIgnoreCase("지우기허용목록")||args[0].equalsIgnoreCase("clear허용목록")||args[0].equalsIgnoreCase("지우기허용유저목록")||args[0].equalsIgnoreCase("clear허용유저목록"))) {
                
            	Controller.showClearAllowUser(tc, msg, event);
            }
           
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("재시작")||args[0].equalsIgnoreCase("restart"))) {
                tc.sendMessage(":hourglass_flowing_sand: 봇이 재시작됩니다").queue(response -> {
                	File file = new File(BotMusicListener.directoryDefault + "restartMessageId.txt");
            		
            		try {
            			FileWriter fw = new FileWriter(file);
            		    fw.write(tc.getId() + "/" + response.getId());
            		      
            		    fw.close();
            		} 
            		catch (Exception e) {
            		    e.printStackTrace();
            		    logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
            		}
           
          
                	event.getJDA().shutdown();
                	
                	MusicController.musicManagers.clear();
                	Main.start();
                });
            }
           
            else if(args[0].equalsIgnoreCase("ㄴㄴ")||args[0].equalsIgnoreCase("취소")||args[0].equalsIgnoreCase("되돌리기")||args[0].equalsIgnoreCase("cancel")||args[0].equalsIgnoreCase("return")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	MusicController.cancel(tc, msg, event, language);
            }
            
            else if(args[0].contains("print")||args[0].contains("guild")||args[0].equalsIgnoreCase("alert")||args[0].contains("saveDefault")||args[0].contains("lock")) {
            	tc.sendMessage("관리자만 이용 가능한 명령어에요").queue();
            	System.out.println("BOT: 관리자만 이용 가능한 명령어에요");       	
            }
            
            else if(args[0].equalsIgnoreCase("clear")||args[0].equalsIgnoreCase("ㅊ")) {
            	tc.sendMessage("허용된 사람만 이용 가능한 명령어에요").queue();
            	System.out.println("BOT: 허용된 사람만 이용 가능한 명령어에요");
            }
           
           	//check mute channel
            if(Main.muteChannels.contains(tc.getId())) return;
            
            String lowerArgs0 = args[0].toLowerCase();
           
            if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("guilds")||args[0].equalsIgnoreCase("g")||args[0].equalsIgnoreCase("ㅎ"))){
             	MusicController.guilds(tc, msg, event);
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
            
            else if(args[0].equalsIgnoreCase("틀어")||args[0].equalsIgnoreCase("재개")||args[0].equalsIgnoreCase("resume")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	Member member = event.getMember();
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	MusicController.resume(tc, event, member, language, 1);
            }
            
            else if(lowerArgs0.startsWith("리스트")||lowerArgs0.startsWith("재생목록")||lowerArgs0.startsWith("목록")||lowerArgs0.startsWith("ㅂ")||lowerArgs0.startsWith("큐")||lowerArgs0.startsWith("q")||lowerArgs0.startsWith("queue")||lowerArgs0.startsWith("list")||lowerArgs0.startsWith("playlist")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}

            	int fn = 0;
            	 
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("리스트")) {
	            	if(lowerArgs0.length() > 3)
	            		substring = 3;
	            }
	            else if(lowerArgs0.startsWith("재생목록") || lowerArgs0.startsWith("list")) {
	            	if(lowerArgs0.length() > 4)
	            		substring = 4;
	            }
	            else if(lowerArgs0.startsWith("목록")) {
	            	if(lowerArgs0.length() > 2)
	            		substring = 2;
	            }
	            else if(lowerArgs0.startsWith("queue")) {
	            	if(lowerArgs0.length() > 5)
	            		substring = 5;
	            }
	            else if(lowerArgs0.startsWith("ㅂ") || lowerArgs0.startsWith("q") || lowerArgs0.startsWith("큐")) {
	            	if(lowerArgs0.length() > 1)
	            		substring = 1;
	            }
	            else if(lowerArgs0.startsWith("playlist")) {
	            	if(lowerArgs0.length() > 8)
	            		substring = 8;
	            }
	            
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	soWhich = getMistake;
	            	
	            }
	            else {
		            //original
	            	if(args[0].equalsIgnoreCase("리스트")||args[0].equalsIgnoreCase("재생목록")||args[0].equalsIgnoreCase("목록")||args[0].equalsIgnoreCase("ㅂ")||args[0].equalsIgnoreCase("큐")||args[0].equalsIgnoreCase("q")||args[0].equalsIgnoreCase("queue")||args[0].equalsIgnoreCase("list")||args[0].equalsIgnoreCase("playlist")) {
	            
		            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll(" ", "");
		            	if(args.length <= 1) {
		            		input = "1";
		            		fn = 2;
		                }
		            	
		            	soWhich = input;
	            	}
	            }
	            
	            if(!func.isNumber(soWhich)) {
	            	soWhich = "1";
            		fn = 2;
	            }
	            
	            MusicController.nowplay(tc, msg, event, Integer.parseInt(soWhich), fn, language);

            }

            else if(args[0].equalsIgnoreCase("다시재생")||args[0].equalsIgnoreCase("playagain")) {
            	
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	
            	if(Main.saveQueueAllowGuild.contains(guild1.getId())) {
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
            
            else if(lowerArgs0.startsWith("randomplay")||lowerArgs0.startsWith("ranp")||lowerArgs0.startsWith("랜덤제생")||lowerArgs0.startsWith("랜덤재생")||lowerArgs0.startsWith("랜재")||lowerArgs0.startsWith("playrandom")||lowerArgs0.startsWith("pran")||lowerArgs0.startsWith("제생랜덤")||lowerArgs0.startsWith("재생랜덤")||lowerArgs0.startsWith("재랜")) {
	            MusicController.checkLock(tc, user, event);
	        	if(ret == 1) {
	        		ret = 0;
	        		return;
	        	}
	
	        	String language = "kor";
	        	if(getType(lowerArgs0) == true) language = "eng";
	        	
	        	String soWhich = "";
	        	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
	        	
	        	int substring = 0;
	        	
	            if(lowerArgs0.startsWith("randomplay") || lowerArgs0.startsWith("playrandom")) {
	            	if(lowerArgs0.length() > 10)
	            		substring = 10;
	            }
	            else if(lowerArgs0.startsWith("ranp") || lowerArgs0.startsWith("랜덤재생") || lowerArgs0.startsWith("랜덤제생") || lowerArgs0.startsWith("pran") || lowerArgs0.startsWith("제생랜덤") || lowerArgs0.startsWith("재생랜덤")) {
	            	if(lowerArgs0.length() > 4)
	            		substring = 4;
	            }
	            else if(lowerArgs0.startsWith("랜재") || lowerArgs0.startsWith("재랜")) {
	            	if(lowerArgs0.length() > 2)
	            		substring = 2;
	            }
	            
	            
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	
	            	soWhich = getMistake;
	            	
	            }
	            else {
		            //original
		            if(args[0].equalsIgnoreCase("randomplay")||args[0].equalsIgnoreCase("ranp")||args[0].equalsIgnoreCase("랜덤제생")||args[0].equalsIgnoreCase("랜덤재생")||args[0].equalsIgnoreCase("랜재")||args[0].equalsIgnoreCase("playrandom")||args[0].equalsIgnoreCase("pran")||args[0].equalsIgnoreCase("제생랜덤")||args[0].equalsIgnoreCase("재생랜덤")||args[0].equalsIgnoreCase("재랜")) {
	            	
		            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		
		            		
		            	if(args.length <= 1) {
		                    MusicController.randomNextTrack(tc, msg, event, language);
		                        
		                } 
		            	else {
		            		soWhich = input;
		            	}
		            }
	            }
	            
	            if(soWhich.startsWith("https://")) {
                	MusicController.loadAndPlayRandom(tc, msg, soWhich, event, language);
                	MusicController.repeat(tc, msg, event);
                }
                	
                else {
                	if(!soWhich.equals("")) {
	                	String langu = "**재생목록 URL**이 아닌 것 같아요";
	                	if(language.equals("eng"))
	                		langu = "I don't think that URL links to a playlist.";
	                			
	                	tc.sendMessage(langu).queue();
	                	log(tc, event, msg, "BOT: " + langu);
                	}
                }
			   
            }
            
            else if(lowerArgs0.startsWith("add")||lowerArgs0.startsWith("p")||lowerArgs0.startsWith("play")||lowerArgs0.startsWith("제생")||lowerArgs0.startsWith("재생")||lowerArgs0.startsWith("ㅔ")||lowerArgs0.startsWith("추가")) {
            	
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	Member member = event.getMember();
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";
 
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("add")) {
	            	if(lowerArgs0.length() > 3)
	            		substring = 3;
	            }
	            else if(lowerArgs0.startsWith("play")) {
	            	if(lowerArgs0.length() > 4)
	            		substring = 4;
	            }
	            else if(lowerArgs0.startsWith("p") || lowerArgs0.startsWith("ㅔ")) {
	            	if(lowerArgs0.length() > 1)
	            		substring = 1;
	            }
	            else if(lowerArgs0.startsWith("제생") || lowerArgs0.startsWith("재생") || lowerArgs0.startsWith("추가")) {
	            	if(lowerArgs0.length() > 2)
	            		substring = 2;
	            }
	           
            	if(substring != 0) {
            		if(substring == 1 && lowerArgs0.startsWith("p")) return;
            		
            		getMistake = getMistake.substring(substring);
            		
            		soWhich = getMistake;
            		
            	}
            	else {
            		//original
	            	if(args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("p")||args[0].equalsIgnoreCase("play")||args[0].equalsIgnoreCase("제생")||args[0].equalsIgnoreCase("재생")||args[0].equalsIgnoreCase("ㅔ")||args[0].equalsIgnoreCase("추가")) {
		            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		
		            	if(args.length <= 1) { 
		            			
		                    MusicController.queryNull(tc, msg, event, member, language);
		                    return;
		                } 
		            	
		            	soWhich = input;
		                
		            }
            	}
            	
            	MusicController.loadAndPlay(tc, msg, null, soWhich, event, event.getMember(), language, 0);
            	MusicController.repeat(tc, msg, event);
            }
            
            else if(user.getId().equals(admin)&&(args[0].equalsIgnoreCase("test")||args[0].equalsIgnoreCase("테스트"))) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
 
            	Member member = event.getMember();
            	
            	VoiceChannel myChannel = member.getVoiceState().getChannel();
            	if(myChannel == null) {
            		tc.sendMessage("먼저 음성채널에 들어가세요").queue();
            		System.out.println("BOT: 먼저 음성채널에 들어가세요");
            		
	        		log(tc, event, msg, "BOT: 먼저 음성채널에 들어가세요");
	       
            		return;
            	}
            		
                String youtubeSearch = "https://www.youtube.com/watch?v=8JmaQsiYn3o";
                
                MusicController.loadAndPlay(tc, msg, null, youtubeSearch, event, event.getMember(), "kor", 0);
                MusicController.repeat(tc, msg, event);
      
            }
            
            else if(lowerArgs0.startsWith("검색")||lowerArgs0.startsWith("search")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";
            	
        		if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
        			String str = "링크 첨부";
        			if(language.equals("eng"))
        				str = "MESSAGE_EMBED_LINKS";
        			
        			tc.sendMessage(func.askPermission(str, language)).queue();
        			log(tc, event, msg, "BOT: " + str);
    	        	return;
    	        }
        		
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("검색")) {
	            	if(lowerArgs0.length() > 2)
	            		substring = 2;
	            }
	            else if(lowerArgs0.startsWith("search")) {
	            	if(lowerArgs0.length() > 6)
	            		substring = 6;
	            }
	           
            	if(substring != 0) {
            		getMistake = getMistake.substring(substring);
            		
            		soWhich = getMistake;
            		
            	}
            	else {
	            	//original
	            	if(args[0].equalsIgnoreCase("검색")||args[0].equalsIgnoreCase("search")) {
	            	
		            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		            	if(args.length <= 1) {
		            		MusicController.queryNullSearch(tc, msg, event, language);
		            		return;
		                } 
		            	else {
		            		soWhich = input;
		            	}
	            	}
            	}
            	
            	EmbedBuilder eb = new EmbedBuilder();
        		eb.setColor(Color.decode(colorDefault));
        				
        		String langu = ":mag: `" + soWhich + "` 검색 중...";
                if(language.equals("eng"))
                	langu = ":mag: Searching `" + soWhich + "`...";
                	
        		eb.setTitle(langu);
        			
        		String langua = language;
        		String soWhic = soWhich;
                tc.sendMessage(eb.build()).queue(response -> {
        		       	
                   	MusicController.search(tc, msg, soWhic, event, response, langua);
        		});
                	
            }
  
            else if(lowerArgs0.startsWith("입장")||lowerArgs0.startsWith("이동")||lowerArgs0.startsWith("옮기기")||lowerArgs0.startsWith("들어가기")||lowerArgs0.startsWith("move")||lowerArgs0.startsWith("mv")||lowerArgs0.startsWith("enter")) {
            
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";

            	Guild guild = tc.getGuild();
            	
            	MusicController.repeat(tc,  msg,  event);
            	MusicController.moveVoiceChannel(tc, msg, event, guild, args, language);
            }
  
            else if(args[0].equalsIgnoreCase("cpu")||args[0].equalsIgnoreCase("사용량")||args[0].equalsIgnoreCase("spec")||args[0].equalsIgnoreCase("스펙")||args[0].equalsIgnoreCase("사양")||args[0].equalsIgnoreCase("성능")||args[0].equalsIgnoreCase("컴퓨터")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	Controller.useCpu(tc, msg, event, language);

            }
            
            else if(args[0].equalsIgnoreCase("stop")||args[0].equalsIgnoreCase("멈춤")||args[0].equalsIgnoreCase("중지")||args[0].equalsIgnoreCase("나가게")||args[0].equalsIgnoreCase("낙가ㅣ")||args[0].equalsIgnoreCase("나각기")||args[0].equalsIgnoreCase("나가ㅣ")||args[0].equalsIgnoreCase("나가기")||args[0].equalsIgnoreCase("exit")||args[0].equalsIgnoreCase("out")||args[0].equalsIgnoreCase("leave")||args[0].equalsIgnoreCase("disconnect")||args[0].equalsIgnoreCase("dc")){
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	Guild guild = event.getGuild();
                AudioManager manager = guild.getAudioManager();
                
                String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
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
            
            else if(lowerArgs0.startsWith("넘기기")||lowerArgs0.startsWith("스킵")||lowerArgs0.startsWith("건너뛰기")||lowerArgs0.startsWith("ㄴ")||lowerArgs0.startsWith("skip")||lowerArgs0.startsWith("s")||lowerArgs0.startsWith("jump")) {
            	if(lowerArgs0.startsWith("ㄴㄴ")) return;
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";
            	
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("넘기기")) {
	            	if(lowerArgs0.length() > 3)
	            		substring = 3;
	            }
	            else if(lowerArgs0.startsWith("skip") || lowerArgs0.startsWith("jump") || lowerArgs0.startsWith("건너뛰기")) {
	            	if(lowerArgs0.length() > 4)
	            		substring = 4;
	            }
	            else if(lowerArgs0.startsWith("스킵")) {
	            	if(lowerArgs0.length() > 2)
	            		substring = 2;
	            }
	            else if(lowerArgs0.startsWith("ㄴ") || lowerArgs0.startsWith("s")) {
	            	if(lowerArgs0.length() > 1)
	            		substring = 1;
	            }
	           
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	
	            	soWhich = getMistake;
	            }
	            else {
	            	//original
	            	if(args[0].equalsIgnoreCase("넘기기")||args[0].equalsIgnoreCase("스킵")||args[0].equalsIgnoreCase("건너뛰기")||args[0].equalsIgnoreCase("ㄴ")||args[0].equalsIgnoreCase("skip")||args[0].equalsIgnoreCase("s")||args[0].equalsIgnoreCase("jump")) {
	            	
		            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll(" ", "");
		            	if(args.length <= 1) {
		            		input = "0";
		            	}
		            	
		            	soWhich = input;
	            	}
	            }
	            
	            if(func.isNumber(soWhich))
	            	MusicController.skipTrack(tc, msg, event, Integer.parseInt(soWhich), language);
	            
            }
            
            else if(lowerArgs0.startsWith("재거")||lowerArgs0.startsWith("제거")||lowerArgs0.startsWith("삭제")||lowerArgs0.startsWith("remove")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";
            	
            	if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
            		String str = "링크 첨부";
        			if(language.equals("eng"))
        				str = "MESSAGE_EMBED_LINKS";
        			
        			tc.sendMessage(func.askPermission(str, language)).queue();
        			log(tc, event, msg, "BOT: " + str);
    	        	return;
    	        }
            	
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("재거") || lowerArgs0.startsWith("제거") || lowerArgs0.startsWith("삭제")) {
	            	if(lowerArgs0.length() > 2)
	            		substring = 2;
	            }
	            else if(lowerArgs0.startsWith("remove")) {
	            	if(lowerArgs0.length() > 6)
	            		substring = 6;
	            }
	            
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	
	            	soWhich = getMistake;
	            	
	            }
	            else {
		            //original
	            	if(args[0].equalsIgnoreCase("재거")||args[0].equalsIgnoreCase("제거")||args[0].equalsIgnoreCase("삭제")||args[0].equalsIgnoreCase("remove")) {
	            	
		            	if(args.length <= 1) {
		            		String langu = "삭제할 항목 번호가 있어야해요";
		            		if(language.equals("eng")) langu = "Must have item number.";
		            		
		            		tc.sendMessage(langu).queue();
		            		System.out.println("BOT: " + langu);
		            		
			        		log(tc, event, msg, "BOT: " + langu);
			        		return;
		            	}
		            	
		            	if(args.length >= 2) {
		            		String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
		            		soWhich = input;
		            	}
	            	}
	            }
	            
	            List<String> inputs = new ArrayList<>();
    			
    			boolean onlyNumber = true;
    			
    			String[] split = null;
    			
    			String checkOnlyNumber = soWhich.replaceAll(",", "").replaceAll(" ", "").replaceAll("-", "").replaceAll("~", "");
    			
    			if(soWhich.contains(",")) {
    				soWhich = soWhich.replaceAll(" ", "");
    				split = soWhich.split(",");
    			}
    			else
    				split = soWhich.split(" ");
    			
    			if(!func.isNumber(checkOnlyNumber))
    				onlyNumber = false;
    			
    			for(int i = 0; i<split.length; i++) { 
    				inputs.add(split[i]);
    			}
    			
    			if(onlyNumber == false) {
    				MusicController.removeTrackTitle(inputs, tc, msg, event, language);
        			
    				return;
    			}
    			
    			String languag = language;
    			String[] spli = split;
    			Runnable re = () -> {
        			List<Integer> items = new ArrayList<>();
        			
        			for(int i = 0; i<spli.length; i++) { 
        				
        				if(spli[i].contains("~")||spli[i].contains("-")) {
        						
        					int x = 0, y = 0;
        						    
        						
        					if(spli[i].contains("~"))
        						spli[i] = spli[i].replaceAll(" ", "").replaceAll("^~, [^0-9]", "");
        					else 
        						spli[i] = spli[i].replaceAll(" ", "").replaceAll("^-, [^0-9]", "");
        								
        					try {
        						String[] divide = null;
        						if(spli[i].contains("~"))
        							divide = spli[i].split("~");
        						if(spli[i].contains("-"))
        							divide = spli[i].split("-");
        									
        						if(spli[i].contains("~")||spli[i].contains("-")) {
        									
        							x = Integer.parseInt(divide[0]);
        							y = Integer.parseInt(divide[1]);
        							
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
        					if(func.isNumber(spli[i])) {
        						if(items.contains(Integer.parseInt(spli[i]))) {}
        						else
        							items.add(Integer.parseInt(spli[i]));
        					}
        				}
        				
        			}

        			Collections.sort(items);
        			
        			MusicController.removeManyTrack(items, tc, msg, event, languag);
        			items.clear();
    			};
    			Thread tr = new Thread(re);
    			tr.start();
            }
            
            else if(lowerArgs0.startsWith("불러오기")||lowerArgs0.startsWith("load")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}

            	
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";
     

            	Member member = event.getMember();

        		if(!Main.loadAllowGuild.contains(guild1.getId())&&!Main.loadAllowUser.contains(user.getId())) {
        			String langu = "여기서는 쓸 수 없는 기능이에요";
        			if(language.equals("eng")) 
        				langu = "Can't use it in this server.";
            		
            		tc.sendMessage(langu).queue();
            		System.out.println("BOT: " + langu);
            		
            		log(tc, event, msg, "BOT: " + langu);
            		
            		return;
        		}
            	
            	VoiceChannel myChannel = member.getVoiceState().getChannel();
            	if(myChannel == null) {
            		String langu = "먼저 음성채널에 들어가세요";
            		if(language.equals("eng")) langu = "Join the voice channel first.";
            		
            		tc.sendMessage(langu).queue();
            		System.out.println("BOT: " + langu);
            		
	        		log(tc, event, msg, "BOT: " + langu);
            		return;
            	}
            	
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("불러오기") || lowerArgs0.startsWith("load")) {
	            	if(lowerArgs0.length() > 4)
	            		substring = 4;
	            }
	            
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	
	            	soWhich = getMistake;
	            	
		        }
	            else {
	            	//original
	            	if(args[0].equalsIgnoreCase("불러오기")||args[0].equalsIgnoreCase("load")) {
	            		
	            		String id = member.getId();
	            		
			            if(args.length > 1) {
			            	soWhich = args[1];
			            }
			            else
			            	soWhich = id;
	            		
	            	}
		            		
            	}
	            
	            if(soWhich.length() == 18) {
        			try {
        				event.getJDA().getUserById(soWhich);
        				MusicController.repeat(tc, msg, event);
			            MusicController.playAgain(tc, msg, member, event, soWhich, 1, language);
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
        		}
    	
            }

            else if(lowerArgs0.startsWith("종료")||lowerArgs0.startsWith("마지막")||lowerArgs0.startsWith("last")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
    
            	int isLast = 0;
            	int last = -1;
            	
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";
            	
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("종료")) {
	            	if(lowerArgs0.length() > 2)
	            		substring = 2;
	            }
	            else if(lowerArgs0.startsWith("마지막")) {
	            	if(lowerArgs0.length() > 3)
	            		substring = 3;
	            }
	            else if(lowerArgs0.startsWith("last")) {
	            	if(lowerArgs0.length() > 4)
	            		substring = 4;
	            }
	            
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	soWhich = getMistake;
	            	
	            }
	            
	            else {
	            	//original
	            	if(args[0].equalsIgnoreCase("종료")||args[0].equalsIgnoreCase("마지막")||args[0].equalsIgnoreCase("last")) {
	            	
		            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll(" ", "");
		            	soWhich = input;
		            	
		            	if(args.length <= 1) {
		            		isLast = 1;
		            	}
	            	}
	            }

            	if(soWhich.equals("0")||soWhich.contains("취소")||soWhich.contains("제거")||soWhich.contains("cancel")||soWhich.contains("remove")||soWhich.contains("disable")) {
            		isLast = 0;
            	}
            	
            	else if(soWhich.contains("목록")||soWhich.contains("큐")||soWhich.contains("재생목록")||soWhich.contains("q")||soWhich.contains("ㅂ")||soWhich.contains("queue")||soWhich.contains("list")||soWhich.contains("playlist")) {
            		isLast = 1;
            		last = -2;
            	}
            	
            	else {
            		if(func.isNumber(soWhich)) {
            			isLast = 1;
            			last = Integer.parseInt(soWhich);
            		
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
            
            else if(lowerArgs0.startsWith("명령어")||lowerArgs0.startsWith("help")||lowerArgs0.startsWith("guide") || lowerArgs0.startsWith("사용법") || lowerArgs0.startsWith("설명서") || lowerArgs0.startsWith("도움말")) {
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";
            	
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("도움말") || lowerArgs0.startsWith("사용법") || lowerArgs0.startsWith("설명서") || lowerArgs0.startsWith("명령어")) {
	            	if(lowerArgs0.length() > 3)
	            		substring = 3;
	            }
	            
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	soWhich = getMistake;
	            }
	            
	            else {
	            	//original
	            	if(args[0].equalsIgnoreCase("명령어")||args[0].equalsIgnoreCase("help")||args[0].equalsIgnoreCase("guide") || args[0].equalsIgnoreCase("사용법") || args[0].equalsIgnoreCase("설명서") || args[0].equalsIgnoreCase("도움말")) {
	            	
		            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll(" ", "");
		            	soWhich = input;
		     
	            	}
	            }
	            
	            if(soWhich.equals("확장") || soWhich.equals("전체") || soWhich.equalsIgnoreCase("extend") || soWhich.equalsIgnoreCase("all"))
	            	Controller.showHelpOld(tc, msg, event, language);
	            else
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
            
            else if(args[0].contains("myinfo")||args[0].contains("myInfo")||args[0].equalsIgnoreCase("내정보")||args[0].equalsIgnoreCase("나의정보")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	EmbedBuilder eb = new EmbedBuilder();
	        	eb.setColor(Color.decode(colorDefault));
	        	
	        	String kor = "집계 중...";
	        	if(language.equals("eng"))
	        		kor = "Picking...";
	        	
	        	eb.setAuthor(kor);
	        	
	        	String lan = language;
	        	
	        	if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
	        		String str = "링크 첨부";
        			if(language.equals("eng"))
        				str = "MESSAGE_EMBED_LINKS";
        			
        			tc.sendMessage(func.askPermission(str, language)).queue();
        			log(tc, event, msg, "BOT: " + str);
	            	return;
	            }
	    		
	        	tc.sendMessage(eb.build()).queue(response -> {
	        		Controller.showMyInfo(tc, msg, event, lan, response);
	        	});
            }
            
            else if(args[0].contains("info")||args[0].equalsIgnoreCase("정보")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	Controller.showInfo(tc, msg, event, language);
            }
            
            else if(args[0].equalsIgnoreCase("time")||args[0].equalsIgnoreCase("시간")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	showTime(tc, user, msg, language);
            }
        
            else if(lowerArgs0.startsWith("자동끄기")||lowerArgs0.startsWith("타이머")||lowerArgs0.startsWith("timer")||lowerArgs0.startsWith("autooff")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";
            	
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("자동끄기")) {
	            	if(lowerArgs0.length() > 4)
	            		substring = 4;
	            }
	            else if(lowerArgs0.startsWith("타이머")) {
	            	if(lowerArgs0.length() > 3)
	            		substring = 3;
	            }
	            else if(lowerArgs0.startsWith("timer")) {
	            	if(lowerArgs0.length() > 5)
	            		substring = 5;
	            }
	            else if(lowerArgs0.startsWith("autooff")) {
	            	if(lowerArgs0.length() > 7)
	            		substring = 7;
	            }
	            
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	soWhich = getMistake;
	            	
	            }
	            else {
	            	//original
	            	if(args[0].equalsIgnoreCase("자동끄기")||args[0].equalsIgnoreCase("타이머")||args[0].equalsIgnoreCase("timer")||args[0].equalsIgnoreCase("autooff")) {
	            	
	            	
		            	String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replaceAll(" ", "");
		            	if(args.length <= 1) {
		                	MusicController.nowTimer(tc, msg,  event, language);         	
		                    return;
		                }
		                
		            	soWhich = input;
	            	}
            	}
            	
            	if(soWhich.contains("취소")||soWhich.contains("끄기")||soWhich.contains("제거")||soWhich.contains("해제")||soWhich.contains("off")||soWhich.contains("cancel")||soWhich.contains("remove")||soWhich.contains("disable")) {
                	MusicController.timerCancel(tc, msg,  event, language);
                	return;
                }
                
                if(func.isNumber(soWhich)) {
                	if(Integer.parseInt(soWhich) <= 0) {
                		String langu = "시간은 최소 1분 이상이어야 해요";
                		if(language.equals("eng")) 
                			langu = "Time must over 1 minute.";
                		
                		tc.sendMessage(langu).queue();
                		System.out.println("BOT: " + langu);
                		
    	        		log(tc, event, msg, "BOT: " + langu);
                	}
                	
                	else {
                		MusicController.timer(tc, msg, user, Integer.parseInt(soWhich), event, language);
                		
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
            
            else if(lowerArgs0.startsWith("소리")||lowerArgs0.startsWith("음량")||lowerArgs0.startsWith("볼륨")||lowerArgs0.startsWith("sound")||lowerArgs0.startsWith("vol")||lowerArgs0.startsWith("volume")) {
            	MusicController.checkLock(tc, user, event);
            	if(ret == 1) {
            		ret = 0;
            		return;
            	}
            	
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";
            	
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("소리") || lowerArgs0.startsWith("음량") || lowerArgs0.startsWith("볼륨")) {
	            	if(lowerArgs0.length() > 2)
	            		substring = 2;
	            }
	            else if(lowerArgs0.startsWith("vol")) {
	            	if(lowerArgs0.length() > 3)
	            		substring = 3;
	            }
	            else if(lowerArgs0.startsWith("sound")) {
	            	if(lowerArgs0.length() > 5)
	            		substring = 5;
	            }
	            else if(lowerArgs0.startsWith("volume")) {
	            	if(lowerArgs0.length() > 6)
	            		substring = 6;
	            }
	            
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	
	            	soWhich = getMistake;
	            }
	            else {
	            	//original
	            	if(args[0].equalsIgnoreCase("소리")||args[0].equalsIgnoreCase("음량")||args[0].equalsIgnoreCase("볼륨")||args[0].equalsIgnoreCase("sound")||args[0].equalsIgnoreCase("vol")||args[0].equalsIgnoreCase("volume")) {
	            	
		                try{ 
		                	soWhich = args[1];
		                } 
		                catch(Exception e){
		                	MusicController.nowVolume(tc, msg, event, language);
		                    return;
		                }
	            	}
	            }

                if(soWhich.equals("기본") || soWhich.equals("초기화") || soWhich.equals("default") || soWhich.equals("reset"))
                	soWhich = "15";


                if(Integer.parseInt(soWhich) < 0 || Integer.parseInt(soWhich) > 100) {
                	String langu = "1~100중에서 입력하세요";
                	if(language.equals("eng"))
                		langu = "Please input between 1 and 100.";
                	
                	tc.sendMessage(langu).queue();
                	System.out.println("BOT: " + langu);
                	
	        		log(tc, event, msg, "BOT: " + langu);
	        		
                	return;
                }
                
                else if(!func.isNumber(soWhich)) {
                	String langu = "올바르게 입력해 주세요";
                	if(language.equals("eng"))
                		langu = "Please input correctly.";
                	tc.sendMessage(langu).queue();
                	System.out.println("BOT: " + langu);
                	
	        		log(tc, event, msg, "BOT: " + langu);
                	
                	return;
                }
                
                
            	MusicController.volume(tc, Integer.parseInt(soWhich), event, language);
            	
            }
            
            else if(lowerArgs0.startsWith("저장목록")||lowerArgs0.startsWith("저장된목록")||lowerArgs0.startsWith("savedlist")||lowerArgs0.startsWith("savelist")) {
            	String language = "kor";
            	if(getType(lowerArgs0) == true) language = "eng";

            	if(!Main.saveQueueAllowGuild.contains(guild1.getId())) {
            		String langu = "여기서는 쓸 수 없는 기능이에요";
            		if(language.equals("eng")) langu = "Can't use it in this server.";
            		
            		tc.sendMessage(langu).queue();
            		System.out.println(guild1.toString() + " BOT: " + langu);
            		
	        		log(tc, event, msg, guild1.toString() + " BOT: " + langu);
	        		
	        		return;
            	}
            	
            	int fn = 0;
            	
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("저장목록")) {
	            	if(lowerArgs0.length() > 4)
	            		substring = 4;
	            }
	            else if(lowerArgs0.startsWith("저장된목록")) {
	            	if(lowerArgs0.length() > 5)
	            		substring = 5;
	            }
	            else if(lowerArgs0.startsWith("savedlist")) {
	            	if(lowerArgs0.length() > 9)
	            		substring = 9;
	            }
	            else if(lowerArgs0.startsWith("savelist")) {
	            	if(lowerArgs0.length() > 8)
	            		substring = 8;
	            }
	            
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	
	            	soWhich = getMistake;
	            	
	            }
	            else {
		            //original
	            	if(args[0].equalsIgnoreCase("저장목록")||args[0].equalsIgnoreCase("저장된목록")||args[0].equalsIgnoreCase("savedlist")||args[0].equalsIgnoreCase("savelist")) {
	            	
			            String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
			            if(args.length <= 1) {
			            	input = "1";
			            	fn = 2;
			            }
			            soWhich = input;
			            	
			            
	            	}
	            }
	            
	            MusicController.savedlist(tc, msg, event, Integer.parseInt(soWhich), fn, language);
            }
            
            else if(args[0].equalsIgnoreCase("저장")||args[0].equalsIgnoreCase("save")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	if(Main.loadAllowGuild.contains(guild1.getId())||Main.loadAllowUser.contains(user.getId())) {
            		if(args.length > 1) {
            			String lan = "저장 명령어는 뒤에 아무것도 붙지 않아요";
            			if(language.equals("eng"))
            				lan = "Save command doesn't follow anything.";
            			
            			tc.sendMessage(lan).queue();
            			log(tc, event, msg, "BOT: " + lan);
            		}
            		else
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
            
            else if(lowerArgs0.startsWith("날씨")||lowerArgs0.startsWith("현재날씨")||lowerArgs0.startsWith("오늘날씨")) {
        		if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
        			String str = "링크 첨부";
        			
        			tc.sendMessage(func.askPermission(str, "kor")).queue();
        			log(tc, event, msg, "BOT: " + str);
    	        	return;
    	        }
        		
            	String soWhich = "";
            	String getMistake = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	
            	int substring = 0;
            	
	            if(lowerArgs0.startsWith("현재날씨") || lowerArgs0.startsWith("오늘날씨")) {
	            	if(lowerArgs0.length() > 4)
	            		substring = 4;
	            }
	            else if(lowerArgs0.startsWith("날씨")) {
	            	if(lowerArgs0.length() > 2)
	            		substring = 2;
	            }
	           
	            if(substring != 0) {
	            	getMistake = getMistake.substring(substring);
	            	
	            	soWhich = getMistake;
	            }
	            else {
	            	//original
	            	if(args[0].equalsIgnoreCase("날씨")||args[0].equalsIgnoreCase("현재날씨")||args[0].equalsIgnoreCase("오늘날씨")) {
	            		String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
	            		
		            	if(args.length < 2) {
		            		tc.sendMessage("도시 이름도 입력해주세요").queue();
		            		return;
		            	}
		            	
		            	soWhich = input;
	            	}
	            }
	            
	            soWhich = soWhich.replaceAll(" ", "");
	            
	            Controller.weatherToday(tc, msg, event, soWhich);
            	
            }
            
            else if(args[0].equalsIgnoreCase("권한") || args[0].equalsIgnoreCase("설정") || args[0].equalsIgnoreCase("permission") || args[0].equalsIgnoreCase("setting")) {
            	String language = "kor";
            	if(getType(args[0]) == true) language = "eng";
            	
            	Controller.showPermissions(tc, msg, event, language);
            }

            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("명령어무시")||args[0].equalsIgnoreCase("ignoreprefix"))) {
            	if(args.length < 2) {
            		Controller.showIgnorePrefix(tc, msg, event);
            	}
            	else {
            		if(args[1].contains("ㅣ")) {
            			tc.sendMessage("ㅣ가 포함되어 있으면 안돼요").queue();
            			return;
            		}
            		
            		if(Main.ignorePrefix.contains(args[1])) {
            			tc.sendMessage("`" + args[1] + "`는 이미 추가되어 있어요").queue();
            			return;
            		}
            		Main.ignorePrefix.add(args[1]);
            		
            		Runnable write = () -> {
        				File file = new File(BotMusicListener.directoryDefault + "ignorePrefix.txt");
        				try {
        					FileWriter fw = new FileWriter(file, true);
        					fw.append(args[1] + "\n");
        					
        					fw.close();
        				}
        				catch (Exception e) {
        					e.printStackTrace();
        					tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();  
        					log(tc, event, msg, ":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));
        				}
        			};
        			
        			Thread t = new Thread(write);
        			t.start();
        			
            		tc.sendMessage("접두사 `" + args[1] + "`로 시작하는 메세지를 받으면 무시해요").queue();
            	}
            }
            
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("명령어무시취소")||args[0].equalsIgnoreCase("ignoreprefixcancel"))) {
            	if(args.length < 2) {
            		Controller.showIgnorePrefix(tc, msg, event);
            	}
            	else {
            		if(Main.ignorePrefix.contains(args[1])) {
	            		
	            		Runnable write = () -> {
	            			Main.ignorePrefix.remove(args[1]);
		            		
		            		StringBuilder sb = new StringBuilder();
		            		for(int i = 0; i<Main.ignorePrefix.size(); i++) {
		            			sb.append(Main.ignorePrefix.get(i) + "\n");
		            		}
		            		
	        				File file = new File(BotMusicListener.directoryDefault + "ignorePrefix.txt");
	        				try {
	        					FileWriter fw = new FileWriter(file);
	        					fw.write(sb.toString());
	        					
	        					fw.close();
	        				}
	        				catch (Exception e) {
	        					e.printStackTrace();
	        					tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();  
	        					log(tc, event, msg, ":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));
	        				}
	        			};
	        			
	        			Thread t = new Thread(write);
	        			t.start();
	        			
	            		tc.sendMessage("접두사 `" + args[1] + "`로 시작하는 메세지에 반응해요").queue();
            		}
            		else {
            			tc.sendMessage("접두사 `" + args[1] + "`는 무시대상에 없어요").queue();
            		}
            	}
            }
            
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("무시")||args[0].equalsIgnoreCase("ignore"))) {
            	if(args.length < 2) {
            		Controller.showIgnoreArgs0(tc, msg, event);
            	}
            	else {
            		String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            		if(input.startsWith("https://")) {
            			tc.sendMessage("URL는 무시 대상에 추가하지 않아요").queue();
            			return;
            		}

            		if(Main.ignoreArgs0.contains(input)) {
            			tc.sendMessage("`" + input + "`는 이미 추가되어 있어요").queue();
            			return;
            		}
            		Main.ignoreArgs0.add(input);
            		
            		Runnable write = () -> {
        				File file = new File(BotMusicListener.directoryDefault + "ignoreArgs0.txt");
        				try {
        					FileWriter fw = new FileWriter(file, true);
        					fw.append(input + "\n");
        					
        					fw.close();
        				}
        				catch (Exception e) {
        					e.printStackTrace();
        					tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();  
        					log(tc, event, msg, ":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));
        				}
        			};
        			
        			Thread t = new Thread(write);
        			t.start();
        			
            		tc.sendMessage("`" + input + "`을 검색할지 선택하는 메세지를 보내지 않아요").queue();
            	}
            }
            
            else if(user.getId().equals(admin) && (args[0].equalsIgnoreCase("무시취소")||args[0].equalsIgnoreCase("ignorecancel"))) {
            	if(args.length < 2) {
            		Controller.showIgnoreArgs0(tc, msg, event);
            	}
            	else {
            		String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            		
            		if(Main.ignoreArgs0.contains(input)) {
	            		
	            		Runnable write = () -> {
	            			Main.ignoreArgs0.remove(input);
		            		
		            		StringBuilder sb = new StringBuilder();
		            		for(int i = 0; i<Main.ignoreArgs0.size(); i++) {
		            			sb.append(Main.ignoreArgs0.get(i) + "\n");
		            		}
		            		
	        				File file = new File(BotMusicListener.directoryDefault + "ignoreArgs0.txt");
	        				try {
	        					FileWriter fw = new FileWriter(file);
	        					fw.write(sb.toString());
	        					
	        					fw.close();
	        				}
	        				catch (Exception e) {
	        					e.printStackTrace();
	        					tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();  
	        					log(tc, event, msg, ":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));
	        				}
	        			};
	        			
	        			Thread t = new Thread(write);
	        			t.start();
	        			
	            		tc.sendMessage("`" + input + "`을 유튜브에서 검색할지 선택하는 메세지를 보내요").queue();
            		}
            		else {
            			tc.sendMessage("`" + input + "`는 무시대상에 없어요").queue();
            		}
            	}
            }
           
            else { // 이도저도 아닐 때
            	if(msg.getContentRaw().startsWith("$")) return;
            	if(args[0].equalsIgnoreCase("ㄴㄴ")||args[0].equalsIgnoreCase("취소")||args[0].equalsIgnoreCase("되돌리기")||args[0].equalsIgnoreCase("cancel")||args[0].equalsIgnoreCase("return")) return;
            	if((user.getId().equals(admin)||Main.clearAllowUser.contains(user.getId()))&&(args[0].equalsIgnoreCase("myclear")||args[0].equalsIgnoreCase("mc")||args[0].equalsIgnoreCase("ㅡㅊ")||args[0].equalsIgnoreCase("c")||args[0].equalsIgnoreCase("clear")||args[0].equalsIgnoreCase("ㅊ")||args[0].equalsIgnoreCase("update")||args[0].equalsIgnoreCase("업데이트"))) return;
            	
            	String input = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            	if(Main.ignoreArgs0.contains(input)) return;
            	
            	MusicController.wantToPlay(tc, msg, event, input);
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
	    			response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e)).queue();
	    		}
	    	}
		}
		catch(Exception e) {
			response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e));
		}
	}
	

	public void informationAboutId(TextChannel tc, Message msg, Message response, String id) {
		
		try { // 길드 찾기 시도
			tc.getJDA().getGuildById(id).getName();
			informationGuild(tc, id, response);
		}
		catch(Exception e) { // 유저 찾기 시도
			try {
				tc.getJDA().getUserById(id).getName();
				informationUser(tc, msg, id, response);
			}
			catch(Exception f) { // 텍스트채널 찾기 시도
				try {
					tc.getJDA().getTextChannelById(id).getName();
					informationTC(tc, id, response);
				}
				catch(Exception g) { // 음성채널 찾기 시도
					try {
						tc.getJDA().getVoiceChannelById(id).getName();
						informationVC(tc, id, response);
					}
					catch(Exception h) {
						EmbedBuilder eb = new EmbedBuilder();
						eb.setColor(Color.decode(colorDefault));
						eb.setTitle(":no_entry_sign: " + h);
						
						if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
							String str = "링크 첨부";
		        			
		        			tc.sendMessage(func.askPermission(str, "kor")).queue();
		        			
			            	return;
			            }
						response.editMessage(eb.build()).queue();
					}
				}
			}
		}
	}
	
	public void informationGuild(TextChannel tc, String id, Message response) {
		Guild guild = tc.getJDA().getGuildById(id);
		
		OffsetDateTime create = guild.getTimeCreated();
		int botCount = 0, onlineCount = 0, idleCount = 0, busyCount = 0, offlineCount = 0;
		
		EmbedBuilder eb = new EmbedBuilder();
		
		try {
			eb.setThumbnail(guild.getIconUrl() + "?size=2048");
		}
		catch(Exception e) {}
		
		eb.setTitle("서버 정보");
		eb.setColor(Color.decode(colorDefault));
		eb.addField("이름", guild.getName(), true);
		eb.addField("주인", tc.getJDA().getUserById(guild.getOwnerId()).getAsTag(), true);
		eb.addField("주인아이디", guild.getOwnerId(), true);
		
		eb.addField("부스트 수", guild.getBoostCount() + "", true);
		eb.addField("지역", guild.getRegionRaw(), true);
		eb.addField("생성일", func.createTime(create), true);
		
		eb.addField("유저 수", guild.getMembers().size() + "", true);
		
		for(int i = 0; i<guild.getMembers().size(); i++) {
			if(guild.getMembers().get(i).getUser().isBot() == true)
				botCount = botCount + 1;
			if(guild.getMembers().get(i).getOnlineStatus().toString().equalsIgnoreCase("online")) onlineCount = onlineCount + 1;
			else if(guild.getMembers().get(i).getOnlineStatus().toString().equalsIgnoreCase("idle")) idleCount = idleCount + 1;
			else if(guild.getMembers().get(i).getOnlineStatus().toString().equalsIgnoreCase("do_not_disturb")) busyCount = busyCount + 1;
			else offlineCount = offlineCount + 1;
		}
		eb.addField("봇 수", botCount + "" , true);
		eb.addField("상태", ":green_circle: " + onlineCount + " :yellow_circle: " + idleCount + " :red_circle: " + busyCount + " :white_circle: " + offlineCount, true);
		
		eb.addField("텍스트 채널 수", guild.getTextChannels().size() + "", true);
		eb.addField("음성채널 수", guild.getVoiceChannels().size() + "", true);
		eb.addField("역할 수", guild.getRoles().size() + "", true);
		
		
		response.editMessage(eb.build()).queue();
		
	}
	
	@SuppressWarnings("deprecation")
	public void informationUser(TextChannel tc, Message msg, String id, Message response) {
		User user = tc.getJDA().getUserById(id);
		
		String isBot = "예";
		String isFake = "예";
		
		if(user.isBot() == false) 
			isBot = "아니오";
		
		if(user.isFake() == false)
			isFake = "아니오";
		
		OffsetDateTime create = user.getTimeCreated();
		
		EmbedBuilder eb = new EmbedBuilder();
		try {
			eb.setThumbnail(user.getAvatarUrl() + "?size=2048");
		}
		catch(Exception e) {
			eb.setThumbnail(user.getDefaultAvatarUrl() + "?size=2048");
		}
		
		eb.setTitle("유저 정보");
		eb.setColor(Color.decode(colorDefault));
		
		if(tc.getGuild().getMemberById(id) != null) {
			if(tc.getGuild().getMemberById(id).getNickname() != null)
				eb.addField("이름", tc.getGuild().getMemberById(id).getNickname() + "\n(" + user.getAsTag() + ")", true);
		}
		else eb.addField("이름", user.getAsTag(), true);
		
		eb.addField("생성일", func.createTime(create), true);
		
		if(tc.getGuild().getMemberById(id) != null) {
			OffsetDateTime enter = tc.getGuild().getMemberById(id).getTimeJoined();
			
			eb.addField("서버입장", func.createTime(enter), true);
			eb.addField("상태", tc.getGuild().getMemberById(id).getOnlineStatus().toString(), true);
		}

		eb.addField("봇 여부", isBot, true);
		eb.addField("가짜 여부", isFake, true);
		
		if(tc.getGuild().getMemberById(id) != null && tc.getGuild().getMemberById(id).getTimeBoosted() != null) {
			eb.addField("부스트 시작", func.createTime(tc.getGuild().getMemberById(id).getTimeBoosted()), true);
		}
		
		eb.addField("군밤이와 함께인 서버 수", user.getMutualGuilds().size() + "", true);
		
		response.editMessage(eb.build()).queue();
		
	}
	
	public void informationTC(TextChannel tc, String id, Message response) {
		
		TextChannel textChannel = tc.getJDA().getTextChannelById(id);
		
		OffsetDateTime create = textChannel.getTimeCreated();

		String isNSFW = "네";
		if(textChannel.isNSFW() == false) 
			isNSFW = "아니오";
		
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("텍스트채널 정보");
		eb.setColor(Color.decode(colorDefault));
		eb.addField("이름", textChannel.getName(), true);
		eb.addField("위치", textChannel.getGuild().getName(), true);
		eb.addField("위치 아이디", textChannel.getGuild().getId(), true);
		
		try {
			eb.addField("분류", textChannel.getParent().getName(), true);
		}
		catch(Exception e) {
			eb.addField("분류", "", true);
		}
		eb.addField("생성일", func.createTime(create), true);

		eb.addField("성인 인증", isNSFW, true);
		eb.addField("멤버 수", textChannel.getMembers().size() + "", true);
		
		int botCount = 0;
		for(int i = 0; i<textChannel.getMembers().size(); i++) {
			if(textChannel.getMembers().get(i).getUser().isBot() == true)
				botCount = botCount + 1;
		}
		eb.addField("봇 수", botCount + "", true);
		eb.addField("슬로우 시간", func.slowModeSec(textChannel.getSlowmode()), true);
		
		try {
			if(!textChannel.getTopic().toString().equals(""))
				eb.addField("채널 주제", textChannel.getTopic(), true);
		}
		catch(Exception e) {}
		
		response.editMessage(eb.build()).queue();
		
	}
	
	public void informationVC(TextChannel tc, String id, Message response) {
		VoiceChannel voiceChannel = tc.getJDA().getVoiceChannelById(id);
		
		OffsetDateTime create = voiceChannel.getTimeCreated();
		
		String limit = "없음";
		
		if(voiceChannel.getUserLimit() > 0) {
			limit = String.valueOf(voiceChannel.getUserLimit());
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("음성채널 ");
		eb.setColor(Color.decode(colorDefault));
		eb.addField("이름", voiceChannel.getName(), true);
		eb.addField("위치", voiceChannel.getGuild().getName(), true);
		eb.addField("위치 아이디", voiceChannel.getGuild().getId(), true);
		
		eb.addField("분류", voiceChannel.getParent().getName(), true);
		eb.addField("생성일", func.createTime(create), true);
		eb.addField("음질", voiceChannel.getBitrate()/1000 + "kbps", true);
		
		eb.addField("유저제한", limit + "", true);
		eb.addField("이용자 수", voiceChannel.getMembers().size() + "", true);
		
		response.editMessage(eb.build()).queue();
		
	}

	
	public void log(TextChannel tc, MessageReceivedEvent event, Message msg, String str) {
		if(logOn == 1) {
			if(user.getId().equals(admin)||tc.getGuild().toString().contains(base)||tc.getId().equals("717203670365634621")) {}
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

