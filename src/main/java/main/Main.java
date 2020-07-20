//2020.1.7 write byArrge
package main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

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
	public static String because = "복구";
	
	public static String apiToken = "";
	
	static Timer discordBotListUpdateTimer = new Timer();
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		String token = "";
		if(num.equals("1"))
        		token = ""; //군밤
        	else if(num.equals("2"))
        		token = ""; //분신군밤
		
		JDABuilder jb = new JDABuilder(token);
		jb.setAutoReconnect(true);
		jb.setStatus(OnlineStatus.ONLINE);
		jb.setAudioSendFactory(new NativeAudioSendFactory());
		jb.setActivity(Activity.watching("군밤 사용법 | 버전: 2020년 7월"));

		jb.addEventListeners(new BotListener());
		jb.addEventListeners(new BotMusicListener());

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
					int updateCount = jda.getGuilds().size(); 
					api.setStats(updateCount);
					System.out.println("top.gg " + serverCount + " servers 로 수정됨");
				};
				Thread t = new Thread(r);
				t.start();
			}
		};

		discordBotListUpdateTimer.scheduleAtFixedRate(task, 86400000, 86400000); // 1day
        
	}

}


