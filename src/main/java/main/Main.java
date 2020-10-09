//2020.1.7 write byArrge
package main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.security.auth.login.LoginException;

import org.discordbots.api.client.DiscordBotListAPI;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;

import audio.MusicController;

public class Main {
	
	public static JDA jda;
	public static String num = "1";
	public static String because = "복구"; //**2020년 9월 버전으로 업데이트**
	public static List<String> loadAllowGuild = new ArrayList<>();
	public static List<String> loadAllowUser = new ArrayList<>();
	public static List<String> clearAllowUser = new ArrayList<>();
	public static List<String> saveQueueAllowGuild = new ArrayList<>();
	public static List<String> muteChannels = new ArrayList<>();
	public static List<String> ignoreArgs0 = new ArrayList<>();
	public static List<String> ignorePrefix = new ArrayList<>();
	
	public static String nickNameSynchronizedGuild = "null";
	
	public static String apiToken = "";
	
	static Timer discordBotListUpdateTimer = new Timer();
	
	public static void main(String[] args) {
		BotMusicListener.gunbamStartTime = System.currentTimeMillis();
		
		start();
		
	}
	
	@SuppressWarnings("deprecation")
	public static void start() {
		Runnable r = () -> {
			String token = "";
			if(num.equals("1"))
	        	token = ""; //군밤
	        else if(num.equals("2"))
	        	token = ""; //분신군밤
			
			JDABuilder jb = new JDABuilder(token);
	        jb.setAutoReconnect(true);
	        jb.setStatus(OnlineStatus.ONLINE);
	        jb.setAudioSendFactory(new NativeAudioSendFactory());
	        
	        jb.setActivity(Activity.watching("군밤 사용법 | 버전: " + BotMusicListener.musicVersion));
	 
	        jb.addEventListeners(new BotListener());
	        jb.addEventListeners(new BotMusicListener());
	        jb.addEventListeners(new BotEventListener());
	        
	        ScheduledExecutorService s = Executors.newScheduledThreadPool(5);
	        jb.setGatewayPool(s);
	        jb.setRateLimitPool(s);
	        jb.setCallbackPool(s);
	  
	        try {
	            jda = jb.build();
	            jda.awaitReady();
	            
	        } catch (LoginException e) {
	            e.printStackTrace();
	        } 
	        
	        catch (InterruptedException e)
	        {
	            //Due to the fact that awaitReady is a blocking method, one which waits until JDA is fully loaded,
	            // the waiting can be interrupted. This is the exception that would fire in that situation.
	            //As a note: in this extremely simplified example this will never occur. In fact, this will never occur unless
	            // you use awaitReady in a thread that has the possibility of being interrupted (async thread usage and interrupts)
	            e.printStackTrace();
	        }
	        
			
	        MusicController.go();
	        System.out.println(Runtime.getRuntime().totalMemory()/1024/1024 + "MB");
	        
	        if(num.equals("1")) BotMusicListener.bot = "661001856616497162";
			else if(num.equals("2")) BotMusicListener.bot = "661042002531713035";
	        
	        BotMusicListener.adtc = jda.getTextChannelById("679204476808069120");
	        BotMusicListener.logtc = jda.getTextChannelById("686517298470846495");
	        BotMusicListener.listentc = jda.getTextChannelById("686949011705430062");
	        BotMusicListener.dokingtc = jda.getTextChannelById(BotMusicListener.dokingStr);
	        BotMusicListener.jdatc = jda.getTextChannelById("706443648023134259");
	        BotMusicListener.errortc = jda.getTextChannelById("713233574995820627");


	        if(num.equals("1")) {
		        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
		        		.token(apiToken)
		        		.botId(BotMusicListener.bot)
		        		.build();
		        
		        int serverCount = jda.getGuilds().size(); // the total amount of servers across all shards
		
		        api.setStats(serverCount);
		        
		        TimerTask task = new TimerTask() {
		        	@Override
		        	public void run() {
		        		Runnable r = () -> {
		        			if(jda.getStatus().toString().toLowerCase().contains("not")) {
		        				
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
		        				    e.printStackTrace();
		        				}
		        				
		        				return;
		        			}

		        			int updateCount = jda.getGuilds().size(); 
			        		api.setStats(updateCount);
			        		System.out.println("top.gg " + serverCount + " servers 로 수정됨");
		        		};
		        		Thread t = new Thread(r);
		        		t.start();
		        	}
		        };
		        
		        discordBotListUpdateTimer.scheduleAtFixedRate(task, 21600000, 21600000); // 6h
	        }
		};
		Thread start = new Thread(r);
		start.start();
        
        
        File file1 = new File(BotMusicListener.directoryDefault + "backup/saveQueueAllowGuild.txt");
		File file2 = new File(BotMusicListener.directoryDefault + "guild/loadAllowGuild.txt");
		File file3 = new File(BotMusicListener.directoryDefault + "guild/loadAllowUser.txt");
		File file4 = new File(BotMusicListener.directoryDefault + "guild/muteChannels.txt");
		File file5 = new File(BotMusicListener.directoryDefault + "guild/clearAllowUser.txt");
		File file6 = new File(BotMusicListener.directoryDefault + "guild/nickNameSynchronizedGuild.txt");
		
		File file7 = new File(BotMusicListener.directoryDefault + "ignoreArgs0.txt");
		File file8 = new File(BotMusicListener.directoryDefault + "ignorePrefix.txt");
		
		try {
			FileReader filereader = new FileReader(file1);
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";

			while((line = bufReader.readLine()) != null)
				if(!line.equals("")) saveQueueAllowGuild.add(line);
  
			bufReader.close();
			
			
			FileReader filereader2 = new FileReader(file2);
			BufferedReader bufReader2 = new BufferedReader(filereader2);
			line = "";
			
			while((line = bufReader2.readLine()) != null)
				if(!line.equals("")) loadAllowGuild.add(line);
			
			bufReader2.close();
			
			
			FileReader filereader3 = new FileReader(file3);
			BufferedReader bufReader3 = new BufferedReader(filereader3);
			line = "";
			    
			while((line = bufReader3.readLine()) != null)
				if(!line.equals("")) loadAllowUser.add(line);
				
			bufReader3.close();
			
    		
    		FileReader filereader4 = new FileReader(file4);
			BufferedReader bufReader4 = new BufferedReader(filereader4);
			line = "";
			    
			while((line = bufReader4.readLine()) != null)
				if(!line.equals("")) muteChannels.add(line);
			
			bufReader4.close();
			
			
			FileReader filereader5 = new FileReader(file5);
			BufferedReader bufReader5 = new BufferedReader(filereader5);
			line = "";
			    
			while((line = bufReader5.readLine()) != null)
				if(!line.equals("")) clearAllowUser.add(line);
			
			bufReader5.close();
			
			FileReader filereader6 = new FileReader(file6);
			BufferedReader bufReader6 = new BufferedReader(filereader6);
			line = "";
			    
			while((line = bufReader6.readLine()) != null)
				nickNameSynchronizedGuild = line;
			
			bufReader6.close();
			
			FileReader filereader7 = new FileReader(file7);
			BufferedReader bufReader7 = new BufferedReader(filereader7);
			line = "";
			    
			while((line = bufReader7.readLine()) != null)
				if(!line.equals("")) ignoreArgs0.add(line);
			
			bufReader7.close();
			
			FileReader filereader8 = new FileReader(file8);
			BufferedReader bufReader8 = new BufferedReader(filereader8);
			line = "";
			    
			while((line = bufReader8.readLine()) != null)
				if(!line.equals("")) ignorePrefix.add(line);
			
			bufReader8.close();
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

}


