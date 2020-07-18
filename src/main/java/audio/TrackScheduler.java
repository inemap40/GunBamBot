package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import functions.*;
import main.BotMusicListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;



public class TrackScheduler extends AudioEventAdapter
{
	private final AudioPlayer player;
	
	CustomFunctions fn = new CustomFunctions();
	
	ArrayList<AudioTrack> queue;
	
	List<Object> link = new ArrayList<>();
	List<AudioTrack> listSearch = new ArrayList<>();
	List<AudioTrack> trash = new ArrayList<>();
	
	String trackbefore;
	AudioTrack trackInfo;
	private boolean isPlaying = false;
	
	String playStandBy = "";
	int playStandByInt = 0;
	
	int volume = 15;
	
	int current;
	
	TextChannel tc;
	User user;
	MessageReceivedEvent event;
	Message msg;
	
	int isPlay = 0;
	int queuePage = 0;
	
	int menu = 0;
	String menuStr = "";
	
	int recentAddPlayListCount = 0;

	Timer timer = null;
	Timer timer2 = null;
	
	int runqueue = 0;
	int runnow = 0;
	
	int timerOn = 0;
	
	Timer autoOff = null;
	Timer pingTimer = null;
	Timer emptyTimer = null;
	Timer isLoadingTimer = null;
	
	int timeLeft = 0;
	int timeLeftQueue = 0;
	int timeUp = 0;
	int timerIsOn = 0;
	int refresh = 0;
	int pingRun = 0;
	int emptyRun = 0;
	int loadingRun = 0;
	
	long recentPing1 = 0;
	long recentPing2 = 0;

	
	
	String userSavedListId = "", botSavedListId = "";
	String userSaveListId = "", botSaveListId = "";
	String nowUserMessageId = "", nowBotMessageId = "";
	String qUserMessageId = "", qBotMessageId = "";
	String userTimerId = "", botTimerId = "";
	String userSetTimerId = "", botSetTimerId = "";
	String userShuffleId = "", botShuffleId = "";
	String userLastId = "", botLastId = "";
	String userAlreadyLastId = "", botAlreadyLastId = "";
	String userTryCancelId = "", botTryCancelId = "";
	
	String pingUserMessageId = "", pingBotMessageId = "";
	String chUserMessageId = "", chBotMessageId = "";
	String autoPausedId = "";
	
	String userVolumeMessageId = "", botVolumeMessageId = "";
	String statstr = "";
	String voiceChannelId = "";
	
	List<AudioTrack> removeMany = new ArrayList<>();
	List<Integer> removeNum = new ArrayList<>();
	
	List<AudioTrack> playAgainList = new ArrayList<>();
	HashMap<String, List<AudioTrack>> playAgainPersonalList = new HashMap<>();
	
	
	//many
	int waitingToReconnect = 0;
	int playAgainEdited = 0;
	int loadCount = 0;
	int terminate = 0;
	int tryAgain = 0;
	int isIn = 0;
	int lock = 0;
	int save = 1;
	int lastLong = 0;
	int lastNum = 0;
	int last = 0;
	int search = 0;
	int autoPaused = 0;
	int firstRun = 1;
	long enteredTime = 0;
	long timUp = 0;
	int counting = 0;
	int countend = 1;
	int cancelMenu = 0;
	
	Message deleted;
	User userMessage;
	
	
	String resetDate = "";
	String setLanguageNext = "kor", setTimerLanguage = "kor", setLanguageList = "kor", setLanguageStop = "kor";

	//private EqualizerFactory equalizer = new EqualizerFactory();

	//private float[] BASS_BOOST = { 0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f,
    //-0.1f, -0.1f, -0.1f, -0.1f };

	//private float[] ARRGE = { 4f, 4f, 3f, 3f, 2f, 2f, 0f, 0f, 0f, 0f, 2f, 2f, 3f, 4f, 4f };
	//private float[] ARRGE = { 2f, 2f, 2f, 2f, 2f, 2f, 3f, 3f, 5f, 4f, 3f, 2f, 3f, 4f, 4f };
	
	public TrackScheduler(AudioPlayer player, Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event)
	{
		this.player = player;
		this.queue = new ArrayList<>();
		this.tc = tc;
		this.msg = msg;
		this.event = event;
		this.current = 0;
		
	}

	public void queue(TextChannel tc, MessageReceivedEvent event, AudioTrack track, int add, int index)
	{
		
		if(add == 1) queue.add(track);
		else if(add == 2) {
			queue.add(index, track);
		}

		if(isPlay == 0)
		{	
			player.startTrack(track.makeClone(), false);
		    trackInfo = player.getPlayingTrack();
		    
			isPlay = 1;
	
			if(loadingRun == 1) {
				loadingRun = 0;
				isLoadingTimer.cancel();
				isLoadingTimer = null;
			}
			
			loadingRun = 1;
			if(isLoadingTimer == null) 
				isLoadingTimer = new Timer();
			checkLoading(tc, event);
			
			waitingToReconnect = 0;
			
			
			try {
				trackbefore = MusicController.realTitle(queue.get(current).getInfo().title);	
			}
			catch(Exception e) {
				
				if(tc.getGuild().getId().equals(BotMusicListener.born)) {
        			MusicController.stopPlaying(tc, msg, event, 8, save, setLanguageStop);
                }
        			
                else {
                	MusicController.stopPlaying(tc, msg, event, 8, 0, setLanguageStop);
                }
			}
		}
	}
	
	public void disconnectVoice(Guild guild, VoiceChannel vc) {
		
		if(tc.getGuild().getAudioManager().getConnectionStatus().toString().equals("CONNECTED")) {
			Date date = new Date();
	    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
			String str = dayTime.format(date);
			
			if(tc.getGuild().getId().equals(BotMusicListener.base) || event.getAuthor().getId().equals(BotMusicListener.admin)) {}
			else BotMusicListener.logtc.sendMessage(".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n").queue();
	
			
			if(tc.getGuild().getId().equals(BotMusicListener.born)) {
				MusicController.stopPlaying(tc, msg, event, 3, save, setLanguageStop);
	        }
				
	        else {
	        	MusicController.stopPlaying(tc, msg, event, 3, 0, setLanguageStop);
	        }
			
			waitingToReconnect = 1;
		}
	}
	
	
	public void checkLoading(TextChannel tc, MessageReceivedEvent event) {
		
		TimerTask task = new TimerTask() {
            @Override
            public void run() {

            	loadCount = 0;
            	
            	/*
            	if(tc.getGuild().getJDA().getStatus().toString().equals("WAITING_TO_RECONNECT")) {
	            	if(isPlay == 0) {}
	            		
	            }
            	*/
            }
        };

        isLoadingTimer.schedule(task, 30000);
	}
	
	public void queueAgain(TextChannel tc, Message msg, String lan) {

		link.clear();
		recentAddPlayListCount = 0;
		counting = 0;
		countend = 0;
		
		if(loadCount > 0) {
			String language = "아직 로딩이 덜 됐어요 (" + loadCount + "개 남음)";
			if(lan.equals("eng"))
				language = "Not finished loading yet (" + loadCount + " left).";
			
			tc.sendMessage(language).queue();
			System.out.println("BOT: " + language);
			
			return;
		}
		
		String languageT = ":floppy_disk: 불러오는 중...";
		if(lan.equals("eng"))
			languageT = ":floppy_disk: Loading...";
		
		tc.sendMessage(languageT).queue(response -> {
			
			if(playAgainList.isEmpty()) {
				File file = new File(BotMusicListener.directoryDefault + "backup/saveQueue.txt");
	
				if(file.length()<=1) {
					
					String language = "이전에 재생한 항목이 없어요";
					if(lan.equals("eng"))
						language = "File is empty.";
					
					response.editMessage(language).queue();
					System.out.println("BOT: " + language);
					log(tc, event, "BOT: " + language);
					
					return;
				}
	
				try{   
		            //입력 스트림 생성
		            FileReader filereader = new FileReader(file);
		            //입력 버퍼 생성
		            BufferedReader bufReader  =  new BufferedReader(filereader);
	
		            String line = "";
		            while((line = bufReader.readLine()) != null){
		            	link.add(line);
		            	
		            }
		            //.readLine()은 끝에 개행문자를 읽지 않는다.            
		            bufReader.close();
		        }
				catch(Exception e){
		            System.out.println(e);
		            response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
	   				log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
		        }
	
				response.editMessage("이전에 재생했던 **" + link.size() + "개** 항목을 재생해요").queue();
				System.out.println("BOT: 이전에 재생했던 **" + link.size() + "개** 항목들을 재생해요");
	
				loadCount = link.size();
				
				terminate = 0;
				int end = 0;
				for(int i=0; i<link.size(); i++) {
					 String track = link.get(i).toString();
					 
					 if(i == link.size() - 1) end = 1;
					 MusicController.loadAndPlaySub(tc, msg, track, event, response, 1, end);
				}
			}
			
			else {
				
				queue.addAll(playAgainList);
				
				if(isIn == 0) {
					isIn = 1;
					enteredTime = System.currentTimeMillis();
					
					MusicController.connectToMusicVoiceChannel(tc, msg, msg.getMember(), tc.getGuild().getAudioManager(), event);
					
				}
				
				queue(tc, event, queue.get(0), 0, 0);
				
				response.editMessage("이전에 재생했던 **" + playAgainList.size() + "개** 항목을 **다이렉트로** 재생해요").queue();
				System.out.println("BOT: 이전에 재생했던 **" + playAgainList.size() + "개** 항목들을 다이렉트로 재생해요");
			}
			
			menu = 8;	
			
		});
		
	}
	
	public void queueAgainPersonal(TextChannel tc, Message msg, String id, String lan) {

		link.clear();
		trash.clear();
		recentAddPlayListCount = 0;
		counting = 0;
		countend = 0;
		
		if(loadCount > 0) {
			String language = "아직 로딩이 덜 됐어요 (" + loadCount + "개 남음)";
			if(lan.equals("eng"))
				language = "Not finished loading yet (" + loadCount + " left).";
			
			tc.sendMessage(language).queue();
			System.out.println("BOT: " + language);
			
			return;
		}
		
		String language = ":flashlight: 불러오는 중...";
		if(lan.equals("eng"))
			language = ":flashlight: Loading...";
		
		tc.sendMessage(language).queue(response -> {
			
			Object o = null;
			try {
				if(tc.getGuild().getMemberById(id).getNickname() == null)
					o = tc.getJDA().getUserById(id).getName();
				else 
					o = tc.getGuild().getMemberById(id).getNickname();
			}
			catch(Exception e) {
				try {
					o = tc.getJDA().getUserById(id).getName();
				}
				catch(Exception f) {
					o = id;
				}
			}
			
			try {
				tc.getJDA().getUserById(id).getId();
				
				if(playAgainPersonalList.get(id) == null) {
					queueAgainPersonalNormal(tc, response, o, id, lan);
					
					return;
				}
				
				if(playAgainPersonalList.get(id).isEmpty()) {
					
					queueAgainPersonalNormal(tc, response, o, id, lan);
				}
				
				else {
					queue.addAll(playAgainPersonalList.get(id));
					trash.addAll(playAgainPersonalList.get(id));
					

					if(isIn == 0) {
						isIn = 1;
						enteredTime = System.currentTimeMillis();
						
						MusicController.connectToMusicVoiceChannel(tc, msg, msg.getMember(), tc.getGuild().getAudioManager(), event);
					}
					
					queue(tc, event, playAgainPersonalList.get(id).get(0), 0, 0);
		
					String language2 = "**" + o + "**님이 저장했었던 " + "**" + playAgainPersonalList.get(id).size() + "개** 항목을 **다이렉트로** 추가했어요";
					if(lan.equals("eng"))
						language2 = "Added **" + playAgainPersonalList.get(id).size() + "** items directly that **" + o + "** did saved.";
					response.editMessage(language2).queue();
					log(tc, event, "BOT: " + o + "님의 **" + playAgainPersonalList.get(id).size() + "개** 항목들을 다이렉트로 추가했어요");
		
					playAgainPersonalList.remove(id);
		
					menu = 9;
				}
			}
			catch(Exception e) {
	            response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
				log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
			}
	
		});
		
	}
	
	public void queueAgainPersonalNormal(TextChannel tc, Message response, Object o, String id, String lan) {
		File file = new File(BotMusicListener.directoryDefault + "user/" + id + ".txt");
		try{ 
			
			if(file.length()<=1) {
				String language = "저장한 항목이 없어요";
	    		if(lan.equals("eng"))
	    			language = "File is empty.";
	    		
				response.editMessage(language).queue();
				System.out.println("BOT: " + language);
				log(tc, event, "BOT: " + language);
				
				return;
			}

            FileReader filereader = new FileReader(file);
            BufferedReader bufReader  =  new BufferedReader(filereader);
			
            String line = "";
            while((line = bufReader.readLine()) != null){
            	link.add(line);
            }
            //.readLine()은 끝에 개행문자를 읽지 않는다.            
            bufReader.close();
            
            String language = "**" + o + "**님이 저장했었던 " + "**" + link.size() + "개** 항목을 추가했어요";
    		if(lan.equals("eng"))
    			language = "Added **" + link.size() + "** items that **" + o + "** did saved.";
    		response.editMessage(language).queue();
    		
    		log(tc, event, "BOT: " + o + "님의 **" + link.size() + "개** 항목들을 추가했어요");
    		
    		loadCount = link.size();
    		
    		terminate = 0;
    		int end = 0;
    		for(int i=0; i<link.size(); i++) {
    			 String track = link.get(i).toString();
    			 if(i == link.size() - 1) end = 1;
    			 MusicController.loadAndPlaySub(tc, msg, track, event, response, 1, end);
    		}
    		
    		menu = 8;	
            
        }
		catch(Exception e){
            System.out.println(e);
            response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
			log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
        }

		
	}
	
	public void setVolume(TextChannel tc, MessageReceivedEvent event, int volume, int val, String lan) {
		if(volume <= 0) {
			String language = "``" + volume + "``으로 설정할 수 없어요";
			if(lan.equals("eng"))
				language = "Can not set to 0";
			
			String reply = "BOT: " + language;
            tc.sendMessage(language).queue();
            System.out.println(reply);
            log(tc, event, reply);
            
			return;
		}
		
		int volumeBefore = this.volume;
		
        this.volume = volume;
        player.setVolume(volume);
        
        if(volumeBefore == volume) {
        	String language = "이미 볼륨이 ``" + volume + "``이에요";
			if(lan.equals("eng"))
				language = "Volume is already set ``" + volume + "``.";
			
        	 String reply = "BOT: " + language;
             tc.sendMessage(language).queue();
             System.out.println(reply);
             log(tc, event, reply);
        	return;
        }
        
        String language = "볼륨을 ``" + volumeBefore + "``에서 ``" + volume + "``으로 설정해요";
		if(lan.equals("eng"))
			language = "Set volume ``" + volumeBefore + "``to ``" + volume + "``.";
		
        String reply = "BOT: " + language;
        tc.sendMessage(language).queue();
        System.out.println(reply);
        log(tc, event, reply);
        
        volumeBackUp(tc, volume);
		
        if(val == 1) {
	        menu = 3;
	        menuStr = String.valueOf(volumeBefore);
        }
        else {
        	menu = 0;
        }
    }
	
	public void nowVolume(TextChannel tc, MessageReceivedEvent event, Message msg, String lan) {
		this.tc = tc;
		this.event = event;

		userVolumeMessageId = fn.autoRemoveMessage(tc, msg, userVolumeMessageId, botVolumeMessageId);
		
		String language = "현재 볼륨이 ``" + volume + "``이에요";
		if(lan.equals("eng"))
			language = "Now volume is ``" + volume + "``.";
		
		String reply = "BOT: " + language;
        tc.sendMessage(language).queue(response -> {
        	botVolumeMessageId = response.getId();
        });
        System.out.println(reply);
        log(tc, event, reply);
		
    }
	
	public void repeat(TextChannel tc, Message msg, MessageReceivedEvent event) {
		if(firstRun == 1) {
			File file = new File(BotMusicListener.directoryDefault + "backup/volumeBackUp.txt");
			try {
				FileReader filereader = new FileReader(file);
			       
			    BufferedReader bufReader = new BufferedReader(filereader);
			    String line = "";
			    
			    while((line = bufReader.readLine()) != null){
			    	String[] array = line.split(" ");
			        	if(line.contains(tc.getGuild().getId())) {
			        		volume = Integer.parseInt(array[1]);
			        		break;
			        	}
			    }
			               
			    bufReader.close();
			    
			    player.setVolume(this.volume);
			    firstRun = 0;
			}
			catch(Exception e) {
				String reply = ":no_entry_sign: Failed to load value of volume." + fn.cause(e);
				tc.sendMessage(reply).queue();
				log(tc, event, reply);
			}

			/*
			MusicController.playerManager.getConfiguration().setFilterHotSwapEnabled(true);

			
			for (int i = 0; i < ARRGE.length; i++) {
			      equalizer.setGain(i, ARRGE[i]/40f);
			 }

			player.setFilterFactory(equalizer);
			player.setFrameBufferDuration(500);
			*/
		}
		
		this.tc = tc;
		this.event = event;
	 }
	
	public void volumeBackUp(TextChannel tc, int volume) {
		StringBuilder updateVol = new StringBuilder();
		File file = new File(BotMusicListener.directoryDefault + "backup/volumeBackUp.txt");
		try {
			FileReader filereader = new FileReader(file);
	       
	        BufferedReader bufReader = new BufferedReader(filereader);
	        String line = "";
	        
	        while((line = bufReader.readLine()) != null){
	        	 if(line.contains(tc.getGuild().getId())) {}
	        	 else updateVol.append(line + "\n");
	        }
	               
	        bufReader.close();
		}
		catch(Exception e) {
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
			log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
		}
		
		updateVol.append(tc.getGuild().getId() + " " + String.valueOf(volume));
		
		File file2 = new File(BotMusicListener.directoryDefault + "backup/volumeBackUp.txt");
		try {
		    FileWriter fw = new FileWriter(file2);
		      
		    fw.write(updateVol.toString());
		    fw.close();
		} 
		catch (Exception e) {
		    e.printStackTrace();
		    tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
 			log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
		}
	}
	
	public void shuffle(TextChannel tc, Message msg, String lan) {

		userShuffleId = fn.autoRemoveMessage(tc, msg, userShuffleId, botShuffleId);
		
		if(loadCount > 0) {
			String language = "아직 로딩이 덜 됐어요 (" + loadCount + "개 남음)";
			if(lan.equals("eng"))
				language = "Not finished loading yet (" + loadCount + " left).";
				
			tc.sendMessage(language).queue(response -> {
				botShuffleId = response.getId();
			});
			System.out.println("BOT: " + language);
				
			return;
		}
			
			
		if(queue.isEmpty()) {
			String language = "재생목록이 비었어요";
			if(lan.equals("eng"))
				language = "Playlist is empty.";
				
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue(response -> {
				botShuffleId = response.getId();
			});
			System.out.println(reply);
			log(tc, event, reply);
				
			return;
		}
		
		LinkedList<AudioTrack> tQueue = new LinkedList<>(queue);
		AudioTrack current = null;
		try {
			current = tQueue.get(this.current);
			tQueue.remove(current);
		}
		catch(Exception e) {}
		
		if(current != null) {
			if(queue.size() - 1 <= 1) {
				String language = "섞을 항목이 없어요";
				if(lan.equals("eng"))
					language = "Nothing to shuffle items.";
						
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue(response -> {
					botShuffleId = response.getId();
				});
				System.out.println(reply);
				log(tc, event, reply);
						
				return;
			}
		}
		else {
			if(queue.size() <= 1) {
				String language = "섞을 항목이 없어요";
				if(lan.equals("eng"))
					language = "Nothing to shuffle items.";
					
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue(response -> {
					botShuffleId = response.getId();
				});
				System.out.println(reply);
				log(tc, event, reply);
					
				return;
			}
				
		}
	    
	    Collections.shuffle(tQueue);
	    
	    if(current != null)
	    	tQueue.add(this.current, current);
	   
	        
	    queue.clear();
	    queue.addAll(tQueue);  
	    tQueue.clear();
	    
	    String language = "";
	    
	    if(current == null) {
	    	language = "**" + (int)(queue.size()) + "개** 항목을 섞었어요";
		    if(lan.equals("eng"))
		    	language = "Shuffled **" + (int)(queue.size()) + " items**.";
				
	    }
	    else {
	    	language = "나머지 **" + (int)(queue.size() - 1) + "개** 항목을 섞었어요";
		    if(lan.equals("eng"))
		    	language = "Shuffled other **" + (int)(queue.size() - 1) + " items**.";
				
	    }
	    
	    String reply = "BOT: " + language;
	    tc.sendMessage(language).queue(response -> {
	        botShuffleId = response.getId();
	    });
	    System.out.println(reply);
	    log(tc, event, reply);
		
	    menu = 0;

	}
	
	public void removeMany(List<Integer> removeList, TextChannel tc, MessageReceivedEvent event, String lan) {
		if(removeList.size() > 7) {
			String language = "한 번에 최대 7개까지만 가능해요";
			if(lan.equals("eng"))
				language = "Maximum is 7 items.";
			  
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue();
			System.out.println(reply);
			log(tc, event, reply);
			return;
		}
		
		if(emptyRun == 1) {
			emptyTimer.cancel();
  		  	emptyTimer = null;
  		  	emptyRun = 0;
  	  	}
		
		if(loadCount > 0) {
			String language = "아직 로딩이 덜 됐어요 (" + loadCount + "개 남음)";
			if(lan.equals("eng"))
				language = "Not finished loading yet (" + loadCount + " left).";
			
			tc.sendMessage(language).queue();
			System.out.println("BOT: " + language);
			
			return;
		}
		
		if(queue.isEmpty()) {
			String language = "재생목록이 비어있어요";
			if(lan.equals("eng"))
				language = "Playlist is empty.";
			
			tc.sendMessage(language).queue();
			System.out.println("BOT: " + language);
		    log(tc, event, "BOT: " + language);
			return;
		}
		
		removeMany.clear();
		removeNum.clear();
		
		int prev = 0;
		for(int i = 0; i<removeList.size(); i++) {
			removeNum.add(removeList.get(i));
			
			if(removeList.get(i) - 1 >= queue.size() || removeList.get(i) - 1 < 0) {}
			else {
				removeMany.add(queue.get(removeList.get(i) - 1));
					
				if(removeList.get(i) - 1 <= current)
					prev = prev + 1;
			}
		}
		
		current = current - prev;
		
		
	    LinkedList<AudioTrack> end = new LinkedList<>(queue);
	   
		try {
			end.removeAll(removeMany);
		}
		catch(Exception e) {
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
			            	
			log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
		}
		
	
		if(removeMany.isEmpty()) {
			String language = "삭제하지 않았어요";
			if(lan.equals("eng")) {
				language = "Not removing.";
			}
			
			tc.sendMessage(language).queue();
			
			String reply = "BOT: " + language;
			System.out.println(reply);
			log(tc, event, reply);
			
			menu = 0;
		}
		
		else if(removeMany.size() == 1) {
			String language = "``" + (int)(removeList.get(0)) + ".`` **" + MusicController.realTitle(queue.get(removeList.get(0) - 1).getInfo().title) + "**를 목록에서 삭제했어요";
			if(lan.equals("eng"))
				language = "Removed ``" + (int)(removeList.get(0)) + ".`` **" + MusicController.realTitle(queue.get(removeList.get(0) - 1).getInfo().title) + "** from playlist.";
		  
		 
    	  String reply = "BOT: " + language;
	      tc.sendMessage(language).queue();
	      System.out.println(reply);
	      log(tc, event, reply);

	      menu = 2;
	      
		}

		else {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.decode(BotMusicListener.colorDefault));
			
			
			String languageRemove = "제거된 목록";
			if(lan.equals("eng")) {
				languageRemove = "Removed list";
			}
			
			eb.setTitle(languageRemove);
			
			String languageF = "총 " + removeMany.size() + "개";
			if(lan.equals("eng")) {
				if(removeMany.size() == 1) languageF = removeMany.size() + " item";
				else languageF = removeMany.size() + " items";
			}
			
			eb.setFooter(languageF);
			
			StringBuilder sb = new StringBuilder();
			
			for(int rl = 0; rl<removeMany.size(); rl++) {
				sb.append("`" + (int)(removeList.get(rl)) + ".` **" + MusicController.realTitle(removeMany.get(rl).getInfo().title) + "**\n");
			}
		
			eb.setDescription(sb);
			
			
			String reply = "BOT: " + removeMany.size() + "개 제거했어요";
			tc.sendMessage(eb.build()).queue();
			System.out.println(reply);
			log(tc, event, reply);
			      
			menu = 2;
		}
	  
	    queue.clear();
	    queue.addAll(end);
	    
	    end.clear();  
	
	    if(last == 1||lastLong == 1) {
		    lastLong = 0;
			last = 0;
			lastNum = 0;
			
			if(queue.isEmpty()) {}
			else {
				String languageLast = "마지막 설정을 해제했어요";
				if(lan.equals("eng")) {
					languageLast = "Canceled last setting.";
				}
					  
				tc.sendMessage(languageLast).queue();
				
				}
		}
	   
	    if(queue.isEmpty()) {
	    	String languageEmpty = "재생목록이 비었네요";
			if(lan.equals("eng")) {
				languageEmpty = "Playlist is empty.";
			}
			
	    	String repl = "BOT: " + languageEmpty;
	    	tc.sendMessage(languageEmpty).queue();
	    	System.out.println(repl);
	    	log(tc, event, repl);
	  		
	  		current = 0;
	  		isPlay = 0;
	  		
	  		player.startTrack(null, false);
	  		
	  		fn.removeMessage(tc, qUserMessageId, qBotMessageId);
	  		
	  		if(emptyTimer == null)
	  			emptyTimer = new Timer();
	    	emptyRun = 1;
	    	  
	    	TimerTask task = new TimerTask() {
	    		@Override
		        public void run() {
		        	if(queue.isEmpty()) 
		        	MusicController.stopPlaying(tc, msg, event, 5, save, "kor");

			        emptyRun = 0;
		        }
						
		    };
		        
		    emptyTimer.schedule(task, 30000);
		    
		    return;
	    }
	}
	
	public void remove(String itemstr, TextChannel tc, MessageReceivedEvent event, int i, int alert, String lan) {
		if(emptyRun == 1) {
			emptyTimer.cancel();
  		  	emptyTimer = null;
  		  	emptyRun = 0;
  	  	}
		
		if(loadCount > 0) {
			String language = "아직 로딩이 덜 됐어요 (" + loadCount + "개 남음)";
			if(lan.equals("eng"))
				language = "Not finished loading yet (" + loadCount + " left).";
			
			tc.sendMessage(language).queue();
			System.out.println("BOT: " + language);
			
			return;
		}
		
		if(queue.isEmpty()) {
			String language = "재생목록이 비어있어요";
			if(lan.equals("eng"))
				language = "Playlist is empty.";
			
			tc.sendMessage(language).queue();
			System.out.println("BOT: " + language);
		    log(tc, event, "BOT: " + language);
		    
			return;
		}
		
		removeMany.clear();
		removeNum.clear();
		
	    int item = 0;
	    int x = 0, y = 0;
	    
		if(i == 0) {
			 item = Integer.parseInt(itemstr);
		}
		else {
			if(itemstr.contains("~"))
				itemstr = itemstr.replaceAll(" ", "").replaceAll("^~, [^0-9]", "");
			else 
				itemstr = itemstr.replaceAll(" ", "").replaceAll("^-, [^0-9]", "");
			
			try {
				String[] spli = null;
				if(itemstr.contains("~"))
					spli = itemstr.split("~");
				if(itemstr.contains("-"))
					spli = itemstr.split("-");
				
				if(itemstr.contains("~")||itemstr.contains("-")) {
				
					x = Integer.parseInt(spli[0]);
					y = Integer.parseInt(spli[1]);
				}

				
			}
			catch(Exception e) {
				tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
   				log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
   				return;
			}
	
		}
		
		int size = queue.size();
	    LinkedList<AudioTrack> end = new LinkedList<>(queue);
	    
	    if(i == 0) {
		    item = Math.abs(item);
		    if(item > size)
		        item = size; 
		    if(item < 1)
		        item = 1;
		      
		    item --;
		     
		    if(alert == 1) {
		    	String language = "``" + (int)(item + 1) + ".`` **" + MusicController.realTitle(end.get(item).getInfo().title) + "**를 목록에서 삭제했어요";
		    	if(lan.equals("eng"))
					language = "Removed ``" + (int)(item + 1) + ".`` **" + MusicController.realTitle(end.get(item).getInfo().title) + "** from playlist.";
				  
	
				if(item <= current)
					current = current - 1;
				  
				
		    	String reply = "BOT: " + language;
			    tc.sendMessage(language).queue();
			    System.out.println(reply);
			    log(tc, event, reply);
			   
		    }
		      
		    removeMany.add(queue.get(item));
		    removeNum.add(item + 1);
		      
		    end.remove(item);

	    }
	      
	    else if(i == 1) {
  
	    	x = Math.abs(x);
	    	y = Math.abs(y);
	    	  
	    	int z = 0;
		    if(x>y) {
		    	z = x;
		    	x = y;
		    	y = z;
		    }

	    	if(y > size)
		        y = size;
	    	  
	    	if(x == y) {
	    		remove(String.valueOf(x), tc, event, 0, 1, lan);
	    		  
	    		return;
	    	}
	    	  
		    if(x > size) {
		    	String language = "그 항목은 없어요";
				if(lan.equals("eng"))
					language = "Not exists.";
					
		    	String reply = "BOT: " + language;
			    tc.sendMessage(language).queue();
			    System.out.println(reply);
			    log(tc, event, reply);
			    
		    	return;
		    }
		          

		    if(y+1-x > 7) {
		    	String language = "한 번에 최대 7개까지만 가능해요";
				if(lan.equals("eng"))
					language = "Maximum is 7 items.";
				  
		    	String reply = "BOT: " + language;
			    tc.sendMessage(language).queue();
			    System.out.println(reply);
			    log(tc, event, reply);
			    
		    	return;
		    }

		    int prev = 0;
		    for(int r=x; r<y+1; r++) {
		    	removeMany.add(end.get(r-1));
		    	removeNum.add(r);
		    	if(r-1 <= current)
					prev = prev + 1;
		    }
		      
		    end.removeAll(removeMany);
		    current = current - prev;

		    EmbedBuilder eb = new EmbedBuilder();
		    eb.setColor(Color.decode(BotMusicListener.colorDefault));
		    String languageT = "삭제된 항목";
			if(lan.equals("eng")) {
				languageT = "Removed items";
			}
			
		    eb.setTitle(languageT);
		      
		      
		    String languageF = "총 " + removeMany.size() + "개";
			if(lan.equals("eng")) {
				if(removeMany.size() == 1) languageF = removeMany.size() + " item";
				else languageF = removeMany.size() + " items";
			}
			  
		    eb.setFooter(languageF);
		      
		     
		    StringBuilder sb = new StringBuilder();
		    for(int rl = 0; rl<removeMany.size(); rl++) {
		    	sb.append("`" + (int)(rl + x) + ".` **" + MusicController.realTitle(removeMany.get(rl).getInfo().title) + "**\n");
		    }
		      
		    eb.setDescription(sb);
		    
		    String languageRemove = "**" + x + "번** 부터 **" + y + "번** 까지 항목을 목록에서 삭제했어요\n";
			if(lan.equals("eng")) {
				languageRemove = "Removed **no." + x + "** to **no." + y + "** from playlist\n";
			}
			  
		    String reply = "BOT: " + languageRemove;
		      
		    MessageBuilder mb = new MessageBuilder();
		    mb.append(languageRemove);
		    mb.setEmbed(eb.build());
		      
		    tc.sendMessage(mb.build()).queue();
		      
		    System.out.println(reply);
		    log(tc, event, reply);
		   
	    }

	    queue.clear();
	    queue.addAll(end);
	      
	    end.clear();

	    menu = 2;
	      
	     
	    if(last == 1||lastLong == 1) {
	    	lastLong = 0;
			last = 0;
			lastNum = 0;
				
			if(queue.isEmpty()) {}
			else {
				String language = "마지막 설정을 해제했어요";
				if(lan.equals("eng")) {
					language = "Canceled last setting.";
				}
					  
				tc.sendMessage(language).queue();
				
			}
		}
	   
	    if(queue.isEmpty()) {
	    	emptyEvent(tc, msg, event, lan);
	    	
	    	return;
	    }
	
	}
	
    public void emptyEvent(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
    	String language = "재생목록이 비었네요";
		if(lan.equals("eng")) {
			language = "Playlist is empty.";
		}
		
    	String reply = "BOT: " + language;
    	tc.sendMessage(language).queue();
    	System.out.println(reply);
    	log(tc, event, reply);
  		
  		current = 0;
  		isPlay = 0;
  			
  		player.startTrack(null, false);
  		
  		fn.removeMessage(tc, qUserMessageId, qBotMessageId);
  		
  		if(emptyTimer == null)
  			emptyTimer = new Timer();
    	emptyRun = 1;
    	  
    	TimerTask task = new TimerTask() {
    		@Override
	        public void run() {
	        	if(queue.isEmpty()) 
	        	MusicController.stopPlaying(tc, msg, event, 5, save, "kor");

		        emptyRun = 0;
	        }
					
	    };
	        
	    emptyTimer.schedule(task, 30000);
	    return;
    }
	
	public void nextTrack(TextChannel tc, Message msg, MessageReceivedEvent event, int i, int skipto, String lan)
	{
		search = 0;
		
		if(queue.isEmpty()) {
			String language = "재생목록이 비었어요";
			if(lan.equals("eng"))
				language = "Playlist is empty.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue();
			System.out.println(reply);
			log(tc, event, reply);
			
			return;
		}

		current = current + 1;
		
		
		if(i == 1) {
			if(last == 1) {
				
				userTryCancelId = fn.autoRemoveMessage(tc, msg, userTryCancelId, botTryCancelId);
				
				String language = "마지막 설정을 해제하고 시도하세요";
				if(lan.equals("eng"))
					language = "Cancel last setting and try again.";
				
		    	tc.sendMessage(language).queue(response -> {
		    		botTryCancelId = response.getId();
		    	});
		    	log(tc, event, "BOT: " + language);
		    	current = current - 1;
		    	
		    	return;
		    }
			
			if(skipto == 0) {
				String language = "``" + (int)(current) + ".`` **" + trackbefore + "**을 건너뛰었어요";
				if(lan.equals("eng"))
					language = "Skipped ``" + (int)(current) + ".`` **" + trackbefore + "**.";
				
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue();
				System.out.println(reply);
				log(tc, event, reply);
				
			}
		
			else {
				current = skipto - 1;
				if(queue.size() <= current) {current = queue.size() - 1;}
				else if(0 > current) {current = 0;}
				
				String language = "``" + (int)(current + 1) + ".`` **" + MusicController.realTitle(queue.get(current).getInfo().title) + "**부터 재생해요";
				if(lan.equals("eng"))
					language = "Play from ``" + (int)(current + 1) + ".`` **" + MusicController.realTitle(queue.get(current).getInfo().title) + "**.";
				
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue();
				System.out.println(reply);
				log(tc, event, reply);
				
			}
		}

	    if(queue.size() <= current) {current = 0;}
	 
	    trackbefore = MusicController.realTitle(queue.get(current).getInfo().title);
	    
	    player.destroy();
	    player.playTrack(queue.get(current).makeClone());
	   
	    if(lastLong == 1 && lastNum == current + 1) {
	    	
	    	if(i == 0)
	    		fn.removeMessage(tc, botLastId);
	    	
	    	String language = "이 항목이 끝나면 **음성채널을 나가요**";
			if(lan.equals("eng"))
				language = "When this item is finished, it will **leave the voice channel**";
			
	    	String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
			tc.sendMessage(language).queue(response -> {
				botLastId = response.getId();
			});
			System.out.println(reply);
			log(tc, event, reply);
			
			last = 1;
	    }
	  
	    menu = 0;
	    this.event = event;
	}
	
	public void setTimer(TextChannel tc, Message msg, User user, MessageReceivedEvent event, int time, String lan) {
		timeLeft = time;
		
		userSetTimerId = fn.autoRemoveMessage(tc, msg, userSetTimerId, botSetTimerId);
		
		timeUp = 0;
		
		if(timerIsOn == 1) {
			autoOff.cancel();
			autoOff = null;
			timerIsOn = 0;
		}
		
		if(runqueue == 1) {
			timer.cancel(); 
			timer = null;
			runqueue = 0;
		}
        
		
		if(autoOff == null)
			autoOff = new Timer();
		autoOff(tc, msg, user, event);
	        
		String language = "자동 끄기를 **" + time + "분** 으로 설정해요";
		if(lan.equals("eng")) {
			if(time == 1) language = "Auto off was set to **" + time + " minute.";
			else language = "Auto off was set to **" + time + " minutes.";
		}
		
		String reply = "BOT: " + language;
        tc.sendMessage(language).queue(response -> {
        	botSetTimerId = response.getId();
        });
        System.out.println(reply);
        log(tc, event, reply);
        
        timeLeftQueue = time * 60000;
        
        if(timer == null) 
			timer = new Timer();
				
		updateQueueTitle(msg, tc, lan);
		runqueue = 1;
        
		
        timerIsOn = 1;
        menu = 5;
        
    }
	
	public void nowTimer(TextChannel tc, Message msg, String lan) {

		userTimerId = fn.autoRemoveMessage(tc, msg, userTimerId, botTimerId);
		
		if(timerIsOn == 0) {
			String language = "타이머를 설정하지 않았어요";
			if(lan.equals("eng"))
				language = "You didn't set a timer.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue(response -> {
				 botTimerId = response.getId();
			});
		    System.out.println(reply);
		    log(tc, event, reply);
				
		}
			
		else {
			String language = "**" + (int)(timeLeft - timeUp) + "분** 뒤에 음성채널을 나가요";
			setTimerLanguage = "kor";
			
			if(lan.equals("eng")) {
				if((int)(timeLeft - timeUp) == 1) language = "Leave the voice channel after **" + (int)(timeLeft - timeUp) + " minute**";
				else language = "Leave the voice channel after **" + (int)(timeLeft - timeUp) + " minutes**";
				setTimerLanguage = "eng";
			}
			
			String reply = "BOT: " + language;
		        tc.sendMessage(language).queue(response -> {
		        botTimerId = response.getId();
		    });
		    System.out.println(reply);
		    log(tc, event, reply);
				
			}
				
			if(timeLeft == 0) {}
			else {
				refresh = 1;
		}
		
    }
	
	public void timerCancel(TextChannel tc, Message msg, String lan) {
		
		if(timeLeft == 0) {
			String language = "타이머를 설정하지 않았어요";
			if(lan.equals("eng"))
				language = "You didn't set a timer.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue();
			System.out.println(reply);
			log(tc, event, reply);
		}
		
		else {
			autoOff.cancel();
			autoOff = null;
			timeLeft = 0;
			timeUp = 0;
			timerIsOn = 0;
			timeLeftQueue = 0;
			
			if(refresh == 1) {
        		refresh = 0;
        		
        		fn.removeMessage(tc, botTimerId, userTimerId);
			}
    		
			String language = "자동 끄기를 취소했어요";
			if(lan.equals("eng"))
				language = "Canceled auto off.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue();
			System.out.println(reply);
			log(tc, event, reply);
			
		}
		
	}
	
	public void clearMessage(TextChannel tc, List<Message> msgs, Message msg, int count) {
		cancelMenu = 1;
		userMessage = msg.getAuthor();
		tc.sendMessage("`" + msgs.get(count).getContentRaw() + "` 까지 지울게요 (`취소` 명령어로 멈추기)").queue(response -> {
			
			response.delete().queueAfter(3000, TimeUnit.MILLISECONDS, response2 -> {
				if(cancelMenu != 0) {
					cancelMenu = 0;
					tc.sendMessage(count + " deleted `'" + msgs.get(count).getContentRaw() + "' 까지`").queue(response3 -> {
	                		
	                	Runnable remove = () -> {
	                			
	                		try {
	                			tc.deleteMessages(msgs).complete();
	                			response3.delete().queueAfter(1500, TimeUnit.MILLISECONDS);
	                		}
	                		catch(Exception e) {
	                			for(int i = 0; i<count+1; i++) {
	    		                	try {
	    		                		msgs.get(i).delete().complete();
	    		                	}
	    		                	catch(Exception f) {}
	    			                	
	    		                		
	    		                	if(i == count) {
	    		                				
	    					            try {
	    					                response3.delete().complete();
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
			});
			
		});
	}
	
	public void cancel(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		
		if(cancelMenu == 1 && (msg.getAuthor() == userMessage)) {
			cancelMenu = 0;
			
			Runnable r = () -> {
				try {
					msg.delete().complete();
				}
				catch(Exception e) {}
				
			};
			
			Runnable r2 = () -> {
				try {
					BotMusicListener.inputClear.delete().complete();
				}
				catch(Exception e) {}
				
			};
			
			Thread t = new Thread(r);
			Thread t2 = new Thread(r2);
			t.start();
			t2.start();
			
			return;
		}
		
		if(menu == 0) {
			String language = "되돌릴 작업이 없어요";
			if(lan.equals("eng"))
				language = "There is no work to reverse.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue();
			System.out.println(reply);
			log(tc, event, reply);
		}
		
		else if(menu == 1) { //음악 추가 취소
			
			remove(String.valueOf(queue.size()), tc, event, 0, 1, lan);
		}
		
		else if(menu == 2) { // 음악 삭제 취소
			
			for(int i = 0; i<removeMany.size(); i++) {
				queue(tc, event, removeMany.get(i), 2, removeNum.get(i) - 1);
			}

			Runnable r = () -> {
				for(int i = 0; i<queue.size(); i++) {
	
					if(player.getPlayingTrack().getInfo().uri == queue.get(i).getInfo().uri)
						current = i;
				}
			};
			Thread t = new Thread(r);
			t.start();
			
			Object o = fn.secTo((int)removeMany.get(0).getDuration(), 1);
			if(removeMany.get(0).getInfo().isStream == true) {
				o = "생방송";
				if(lan.equals("eng"))
					o = "LIVE";
			}
				
			if(player.isPaused()) {
				if(removeMany.size() == 1) {
					String language = "**" + MusicController.realTitle(removeMany.get(0).getInfo().title) + "** 를 추가했어요 (일시정지됨) ``(" + o + ")``";
					if(lan.equals("eng"))
						language = "Restored **" + MusicController.realTitle(removeMany.get(0).getInfo().title) + "**. (Paused) ``(" + o + ")``";
					tc.sendMessage(language).queue();
					log(tc, event, "BOT: " + language + "`(총 " + (int)(queue.size()) + "개)`");
				}
				else {
					String language = "삭제했었던 **" + removeMany.size() + "개 항목**을 추가했어요 (일시정지됨)";
					if(lan.equals("eng"))
						language = "Restored **" + removeMany.size() + " items** (Paused)";
					tc.sendMessage(language).queue();
					log(tc, event, "BOT: " + language + "`(총 " + (int)(queue.size()) + "개)`");
				}
			}
			
			else {
				if(removeMany.size() == 1) {
					String language = "**" + MusicController.realTitle(removeMany.get(0).getInfo().title) + "** 를 추가했어요 ``(" + o + ")``";
					if(lan.equals("eng"))
						language = "Restored **" + MusicController.realTitle(removeMany.get(0).getInfo().title) + "**. ``(" + o + ")``";
					tc.sendMessage(language).queue();
					log(tc, event, "BOT: " + language + "`(총 " + (int)(queue.size()) + "개)`");
				}
				else {
					String language = "삭제했었던 **" + removeMany.size() + "개 항목**을 추가했어요";
					if(lan.equals("eng"))
						language = "Restored **" + removeMany.size() + " items**.";
					tc.sendMessage(language).queue();
					log(tc, event, "BOT: " + language + "`(총 " + (int)(queue.size()) + "개)`");
				}
			}
			
			removeMany.clear();
			removeNum.clear();
		}
		
		else if(menu == 3) { // 볼륨 취소
			setVolume(tc, event, Integer.parseInt(menuStr), 0, lan);
		}
		
		else if(menu == 4) { // 재생목록 취소
			
			String language = "**" + recentAddPlayListCount + "개** 항목 추가를 취소했어요";
			if(lan.equals("eng"))
				language = "Canceled to adding **" + recentAddPlayListCount + " items**.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue();
			System.out.println(reply);
			log(tc, event, reply);
			
			int length = queue.size();
			
			for(int i=length; i>length - recentAddPlayListCount; i--) {
				remove(String.valueOf(i), tc, event, 0, 0, lan);
			} 
			
		}
		
		else if(menu == 5) { // 타이머 취소
			if(timeLeft == 0) {
				String language = "타이머를 설정하지 않았아요";
				if(lan.equals("eng"))
					language = "You didn't set a timer.";
				
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue();
				System.out.println(reply);
				log(tc, event, reply);
				
			}
			else {
				autoOff.cancel();
				autoOff = null;
				timeLeft = 0;
				timeUp = 0;
				
				if(refresh == 1) {
            		
	        		refresh = 0;

	        		fn.removeMessage(tc, botTimerId, userTimerId);
					
				}
        		
				String language = "자동 끄기를 취소했어요";
				if(lan.equals("eng"))
					language = "Canceled auto off.";
				
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue();
				System.out.println(reply);
				log(tc, event, reply);
				
			}
			
		}
		
		else if(menu == 6) { // 마지막 취소 
			
			if(last == 0 && lastLong == 0) {
				userAlreadyLastId = fn.autoRemoveMessage(tc, msg, userAlreadyLastId, botAlreadyLastId);
				
				String language = "이미 마지막설정이 해제되어있어요";
				if(lan.equals("eng"))
					language = "Already canceled last setting";
				
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue(response -> {
					botAlreadyLastId = response.getId();
				});
				System.out.println(reply);
				log(tc, event, reply);
				
			}
		
			else {
				if(last == 1) {
					String language = "이 항목이 끝나도 **계속 재생해요**";
					if(lan.equals("eng"))
						language = "Even if this item finished, **still play**.";
					
					String reply = "BOT: " + language;
					tc.sendMessage(language).queue();
					System.out.println(reply);
					log(tc, event, reply);
				}
				else if(lastLong == 1) {
					String language = this.lastNum + "번째 항목이 끝나도 **계속 재생해요**";
					if(lan.equals("eng"))
						language = "Even if no." + this.lastNum + " item finished, **still play**.";
					
					String reply = "BOT: " + language;
					tc.sendMessage(language).queue();
					System.out.println(reply);
					log(tc, event, reply);
				}

			}
	
			last = 0;
			lastLong = 0;
			lastNum = 0;
			
		}
		
		else if(menu == 7 ) { // 검색 취소
			listSearch.clear();
			search = 0;
			
			String language = "검색 기록을 삭제했어요";
			if(lan.equals("eng"))
				language = "Removed search history.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue();
			System.out.println(reply);
			log(tc, event, reply);
			
		} 
		
		else if(menu == 8) { // 다시재생, 불러오기 취소
			
			if(loadCount > 0) {
				String language = "아직 로딩이 덜 됐어요 (" + loadCount + "개 남음)";
				if(lan.equals("eng"))
					language = "Not finished loading yet (" + loadCount + " left).";
				
				tc.sendMessage(language).queue();
				System.out.println("BOT: " + language);
				
				return;
			}
			
			if(!playAgainList.isEmpty()) {
				String language = "**" + playAgainList.size() + "개** 항목 추가를 취소했어요";
				if(lan.equals("eng"))
					language = "Canceled to adding **" + playAgainList.size() + " items**.";
				
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue();
				System.out.println(reply);
				log(tc, event, reply);
				
				int length = queue.size();
				
				for(int i=length; i>length - playAgainList.size(); i--) {
					remove(String.valueOf(i), tc, event, 0, 0, lan);
				} 
				
				playAgainList.clear();
				
				return;
			}
			
			terminate = 1;
			loadCount = 0;
			counting = 0;
			countend = 1;
			
			String language = "**" + link.size() + "개** 항목 추가를 취소했어요";
			if(lan.equals("eng"))
				language = "Canceled to adding **" + link.size() + " items**.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue();
			System.out.println(reply);
			log(tc, event, reply);
			
			int length = queue.size();
			
			for(int i=length; i>length - recentAddPlayListCount; i--) {
				remove(String.valueOf(i), tc, event, 0, 0, lan);
			} 
		
		}
		
		else if(menu == 9) { // 다이렉트로 불러오기 취소
	
			if(!trash.isEmpty()) {
				String language = "**" + trash.size() + "개** 항목 추가를 취소했어요";
				if(lan.equals("eng"))
					language = "Canceled to adding **" + trash.size() + " items**.";
				
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue();
				System.out.println(reply);
				log(tc, event, reply);
				
				
				int length = queue.size();
				
				for(int i=length; i>length - trash.size(); i--) {
					remove(String.valueOf(i), tc, event, 0, 0, lan);
				} 
				
				trash.clear();
				
			}
			else {
				String language = "취소할 항목이 없어요";
				if(lan.equals("eng"))
					language = "Nothing to remove any items.";
				
				String reply = "BOT: " + language;
				
				tc.sendMessage(language).queue();
				log(tc, event, reply);
			}
		}
		
		menu = 0;
	}
	
	public void nowplay(TextChannel tc, Message msg, MessageReceivedEvent event, int page, String lan) {
		
		if(runqueue == 1) {
			timer.cancel(); 
			timer = null;
			runqueue = 0;
		}
		
		/*
		if(runnow == 1) {
			timer2.cancel(); 
			timer2 = null;
			runnow = 0;		
		}
		*/
		
		setLanguageList = "kor";
		if(lan.equals("eng")) setLanguageList = "eng";
		
		double f = Math.ceil(queue.size()/10f);
		
		int max = (int)f;

		this.tc = tc;
		this.event = event;

		qUserMessageId = fn.autoRemoveMessage(tc, msg, qUserMessageId, qBotMessageId);
	
		if(isPlay == 0) {
			String language = "재생중인 항목이 없어요";
			if(lan.equals("eng")) language = "There is no item being played.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue(response -> {
				qBotMessageId = response.getId();
			});
			System.out.println(reply);
			log(tc, event, reply);
					
			return;
		}
				
				
		if(queue.isEmpty()) {
			String language = "재생목록이 비었어요";
			if(lan.equals("eng")) language = "Playlist is empty.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue(response -> {
				qBotMessageId = response.getId();
			});
			System.out.println(reply);
			log(tc, event, reply);
						
			return;
		}
		
		String loading = ":hourglass: 잠시만 기다려 주세요...";
		if(lan.equals("eng"))
			loading = ":hourglass: Wait a second...";
		
		tc.sendMessage(loading).queue(response -> {
			qBotMessageId = response.getId();
	
			int pp = page;
				
			if(pp > max)
				pp = max;
			
			EmbedBuilder ret = new EmbedBuilder();
			
			String language = "";
			
			
			String duration = " (" + fn.secTo((int)trackInfo.getPosition(), 0) + "/" + fn.secTo((int)trackInfo.getDuration(), 0) + ")";
			if(trackInfo.getInfo().isStream == true) {
				duration = " (`생방송`)";
				if(lan.equals("eng"))
					duration = " (`LIVE`)";
			}
	
			if (player.isPaused()) {
				language = "현재 ``" + (int)(current + 1) + ".`` **" + trackbefore + "** 일시정지 중이에요" + duration;
				if(lan.equals("eng")) language = "Paused ``" + (int)(current + 1) + ".`` **" + trackbefore + "** now." + duration;
		
			}			 
			else {
				language = "현재 ``" + (int)(current + 1) + ".`` **" + trackbefore + "** 재생 중이에요" + duration;
				if(lan.equals("eng")) language = "Playing ``" + (int)(current + 1) + ".`` **" + trackbefore + "** now." + duration;
			}
				
			String reply = "BOT: " + language;

			MessageBuilder mb = new MessageBuilder();
			mb.setContent(language);
			mb.setEmbed(playBox(ret, lan, pp).build());
					
			response.editMessage(mb.build()).queue();
	
			System.out.println(reply);
			log(tc, event, reply + " `(총 " + queue.size() + "개)`");
		
			System.out.println(ret);
					
			if(timer == null) 
				timer = new Timer();
					
			updateQueueTitle(msg, tc, lan);
			runqueue = 1;
			
		});

	}
	
	public void nowInfo(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		
		if(runnow == 1) {
			timer2.cancel(); 
			timer2 = null;
			runnow = 0;		
		}
		
		/*
		if(runqueue == 1) {
			timer.cancel(); 
			timer = null;
			runqueue = 0;
		}
		*/
		
		nowUserMessageId = fn.autoRemoveMessage(tc, msg, nowUserMessageId, nowBotMessageId);
		
		if(isPlay == 0) {
			String language = "재생중인 항목이 없어요";
			if(lan.equals("eng"))
				language = "There is no item being played";
			
			String reply = "BOT: 재생중인 항목이 없어요";
			
			EmbedBuilder l = new EmbedBuilder();
			l.setColor(Color.decode(BotMusicListener.colorDefault));
			l.setTitle(":x: " + language);
			
			tc.sendMessage(l.build()).queue(response -> {
				nowBotMessageId = response.getId();
			});
			System.out.println(reply);
			log(tc, event, reply);

			return;
		}
		
		EmbedBuilder l = new EmbedBuilder();
		l.setColor(Color.decode(BotMusicListener.colorDefault));
		
		String languageLoad = ":hourglass: 불러오는 중...";
		if(lan.equals("eng"))
			languageLoad = ":hourglass: Loading...";
		
		l.setTitle(languageLoad);
		
		tc.sendMessage(l.build()).queue(response -> {
			nowBotMessageId = response.getId();

			EmbedBuilder eb = new EmbedBuilder();
			
			response.editMessage(embedNow(eb, lan).build()).queue();

			log(tc, event, "BOT: " + MusicController.realTitle(trackInfo.getInfo().title) + "의 정보 `(총 " + queue.size() + "개)`");

			if(timer2 == null) 
				timer2 = new Timer();
			updateMessage(msg, tc, lan);
			
			runnow = 1;
		});
		
	}
	
	public void savedlist(TextChannel tc, Message msg, int page, String lan) {
		
		userSavedListId = fn.autoRemoveMessage(tc, msg, userSavedListId, botSavedListId);
		
		List<Object> list = new ArrayList<>();
			File file = new File(BotMusicListener.directoryDefault + "backup/saveQueue.txt");
			
			
			if(file.length()<=1) {
				String language = "파일이 비었어요";
				if(lan.equals("eng"))
					language = "File is empty.";
				
				tc.sendMessage(language).queue(response -> {
					botSavedListId = response.getId();
				});
				System.out.println("BOT: 파일이 비었어요");
	
				return;
			}
	
			try{   
	            FileReader filereader = new FileReader(file);
	            BufferedReader bufReader = new BufferedReader(filereader);
	            
	            String line = "";
	            while((line = bufReader.readLine()) != null){
	            	list.add(line);
	            }
	 
	            bufReader.close();
	        }
			catch(Exception e){
	            System.out.println(e);
	            tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue(response -> {
	            	botSavedListId = response.getId();
	            });
   				log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
	        }
			
			double f = Math.ceil(list.size()/10f);
			
			int max = (int)f;
	
			this.tc = tc;
		
			if(page > max) {
				page = max;
			}

			StringBuilder ret = new StringBuilder();
			
			String languageTitle = "```저장된 목록 (총 " + list.size() + "개)\n\n";
			if(lan.equals("eng")) {
				if(list.size() == 1) languageTitle = "```Saved list (" + list.size() + " item)\n\n";
				else languageTitle = "```Saved list (" + list.size() + " item)\n\n";
			}
			
			ret.append(languageTitle);

			if(page == max) {
				for(int i = page*10 - 10; i<list.size(); i++) {
					 ret.append("  " + (int)(i+1) + ". " + list.get(i).toString() + "\n");
				}
			}
			
			else {
				for(int i = page*10 - 10; i<page*10; i++) {
					ret.append("  " + (int)(i+1) + ". " + list.get(i).toString() + "\n");
				}
				
				ret.append("  ...\n");
			}
				
			String languagePage = "\n페이지 " + page + "/" + max + "\n";
			String languagePosition = "파일위치: ";
			if(lan.equals("eng")) {
				languagePage = "\nPage " + page + "/" + max + "\n";
				languagePosition = "Directory: ";
			}
			
			ret.append(languagePage);
			ret.append(languagePosition + BotMusicListener.directoryDefault + "backup/saveQueue.txt\n```");
		
	 
			tc.sendMessage(ret).queue(response -> {
				botSavedListId = response.getId();
			});
			
			System.out.println(ret);
			list.clear();
	
	}
	
	public void savePersonalPlaylist(TextChannel tc, Message msg, MessageReceivedEvent event, String id, String lan) {
		
		userSaveListId = fn.autoRemoveMessage(tc, msg, userSaveListId, botSaveListId);
		
		if(queue.isEmpty()) {
			String language = "재생목록이 비어있어요";
			if(lan.equals("eng")) 
				language = "The playlist is empty.";
			
			tc.sendMessage(language).queue(response -> {
				botSaveListId = response.getId();
			});
			log(tc, event, "BOT: "+ language);
			
			return;
		}
		
		if(queue.size() > 15) {
			String language = "15개 항목이 넘어가면 저장할 수 없어요";
			if(lan.equals("eng")) 
				language = "Can't save over 15 items.";
			
			tc.sendMessage(language).queue(response -> {
				botSaveListId = response.getId();
			});
			log(tc, event, "BOT: "+ language);
			
			return;
		}
		
		Object o = null;
		
		if(event.getMember().getNickname() == null) 
			o = event.getAuthor().getName();
		else
			o = event.getMember().getNickname();
		
		Object ob = o;
		String languageT = ":headphones: `" + o + "` 파일에 저장 중...";
		if(lan.equals("eng"))
			languageT = ":headphones: Saving at file `" + o + "`.";
		tc.sendMessage(languageT).queue(response -> {
			File file = new File(BotMusicListener.directoryDefault + "user/" + id + ".txt");
			
			try {
				if(file.exists() == false) {
					file.createNewFile();
				}
					
				FileWriter fw = new FileWriter(file);
					    
				List<AudioTrack> ss = new ArrayList<>();
				ss.addAll(queue);
				playAgainPersonalList.put(id, ss);
					   
				for(int k = 0; k<queue.size(); k++) {
					fw.write(queue.get(k).getInfo().uri + "\n");
				}
						
				fw.close();
			}
			catch(Exception e) {
				response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
				botSaveListId = response.getId();
			   	log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
			   				
			}
				
			String language = "**" + queue.size() + "개**의 항목을 **" + ob + "** 파일에 저장했어요";
			if(lan.equals("eng")) {
				if(queue.size() == 1) language = "Saved at **" + ob + "** with **" + queue.size() + " item**.";
				else language = "Saved at **" + ob + "** file with **" + queue.size() + " items**.";
			}

			response.editMessage(language).queue();
			botSaveListId = response.getId();
					
			log(tc, event, "BOT: "+ language);
			
		});
		
	}
	
	public void ping(Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		if(tc.getId().contains("679204476808069120")) {
			if(pingRun == 1) {
				pingTimer.cancel(); 
				pingTimer = null;
				pingRun = 0;		
			}
		}

		pingUserMessageId = fn.autoRemoveMessage(tc, msg, pingUserMessageId, pingBotMessageId);
        
		long time = System.currentTimeMillis();
			
		long ping = event.getJDA().getGatewayPing();
			
		String languageLoad = "네트워크 측정 중...";
		if(lan.equals("eng"))
			languageLoad = "Measuring network...";

        tc.sendMessage(languageLoad) /* => RestAction<Message> */
        		.queue(response /* => Message */ -> {
        			pingBotMessageId = response.getId();
            	    String statstr = "";
            	    File file2 = new File(BotMusicListener.directoryDefault + "networkStats");
               			try {
	               			FileReader filereader2 = new FileReader(file2);
	               	       
	               	        BufferedReader bufReader2 = new BufferedReader(filereader2);
	               	        String line = "";
	               	        int stat = 0;
	               	        while((line = bufReader2.readLine()) != null){
	               	        	 stat = Integer.parseInt(line);
	               	        }
	               	               
	               	        bufReader2.close();
	               	        
	               	        if(stat == 1) {
	               	        	String language1 = ":warning: `비상용 와이파이에 연결했어요`";
	               				if(lan.equals("eng"))
	               					language1 = ":warning: `Connected at emergency wifi.`";
	               				
	               	        	statstr = language1;
	               	        }
	               	        
	               	        else if(stat == 2) {
	               	        	String language2 = ":sos: `네트워크가 매우 좋지 못해요`";
	               				if(lan.equals("eng"))
	               					language2 = ":sos: `Network state is too bad.`";
	               				
	               	        	statstr = language2;
	               	        }
	               	        
	               	        else if(stat == 3) {
	               	        	String language3 = ":desktop: `유선 이더넷에 연결했어요`";
	               				if(lan.equals("eng"))
	               					language3 = ":desktop: `Connected at Ethernet`";
	               				
	               	        	statstr = language3;
	               	        }
               			}
               			catch(Exception e) {
               				response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
               				log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
               			}
               			
               			recentPing1 = ping;
               			recentPing2 = System.currentTimeMillis() - time;
               			
               			String languagePing = "\n지연시간: %d ms\n응답시간: %s ms";
           				if(lan.equals("eng"))
           					languagePing = "\nGate: %d ms\nRest: %s ms";
           				
               			response.editMessageFormat(statstr + languagePing, recentPing1, recentPing2).queue();
                       
               			if(tc.getId().contains("679204476808069120")) {
               				if(pingTimer == null) 
               					pingTimer = new Timer();
            				updatePing(event, tc, response);
            				
            				pingRun = 1;
            	        }
               			
                });

	}
	
	public void updateQueueTitle(Message msg, TextChannel tc, String lan) {
		
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	Runnable edit = () -> {
            		if(queue.isEmpty()) return;
	            	
	            		try {
	            			String duration = " (" + fn.secTo((int)trackInfo.getPosition(), 0) + "/" + fn.secTo((int)trackInfo.getDuration(), 0) + ")";
	            			if(trackInfo.getInfo().isStream == true) {
	            				duration = " (`생방송`)";
	            				if(lan.equals("eng"))
	            					duration = " (`LIVE`)";
	            			}
	            	
	            			String language = "";
	            			if (player.isPaused()) {
	            				language = "현재 ``" + (int)(current + 1) + ".`` **" + trackbefore + "** 일시정지 중이에요" + duration;
	            				if(lan.equals("eng")) language = "Paused ``" + (int)(current + 1) + ".`` **" + trackbefore + "** now." + duration;
	            		
	            			}			 
	            			else {
	            				language = "현재 ``" + (int)(current + 1) + ".`` **" + trackbefore + "** 재생 중이에요" + duration;
	            				if(lan.equals("eng")) language = "Playing ``" + (int)(current + 1) + ".`` **" + trackbefore + "** now." + duration;
	            			}
	            			EmbedBuilder eb = new EmbedBuilder();
	            			MessageBuilder mb = new MessageBuilder();
	            			mb.setContent(language);
	            			mb.setEmbed(playBox(eb, lan, queuePage).build());
	            			
	            			tc.editMessageById(qBotMessageId, mb.build()).complete();
	            		}
	            		
	            		catch(Exception e) {
	            			if(timeLeftQueue <= 0) {
	            				if(runqueue == 1) {
	            					timer.cancel(); 
	            					timer = null;
	            					runqueue = 0;
	            				}
	            			}
	            			
	            		}
	
            	};
            	
            	Thread t1 = new Thread(edit);
            	t1.start();
       	
            }
        };

        timer.scheduleAtFixedRate(task, 3000, 3000);
        	
	}

	public void updateMessage(Message msg, TextChannel tc, String lan) { // 현재

		TimerTask task = new TimerTask() {
            @Override
            public void run() {
	            Runnable edit = () -> {
	            	EmbedBuilder ebnow = new EmbedBuilder();
	            	
	    			try {
	    				tc.editMessageById(nowBotMessageId, embedNow(ebnow, lan).build()).complete();
	    			}
	    			
	    			catch(Exception e) {
	    				if(runnow == 1) {
	    					timer2.cancel(); 
	    					timer2 = null;
	    					runnow = 0;
	    				}
	    			}
	        	};
	        	
            	Thread t1 = new Thread(edit);
            	t1.start();
	    	
            }
        };

        timer2.scheduleAtFixedRate(task, 6000, 6000);
        	
	}
	
	public void autoOff(TextChannel tc, Message msg, User user, MessageReceivedEvent event) {
	
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	Runnable edit = () -> {
	            	timeUp = timeUp + 1;
	            	
	            	if(refresh == 1) {
	            		try {
	            			String language = "**" + (int)(timeLeft - timeUp) + "분** 뒤에 음성채널을 나가요";
	            			if(setTimerLanguage.equals("eng")) {
	            				if((int)(timeLeft - timeUp) == 1) language = "Leave the voice channel after **" + (int)(timeLeft - timeUp) + " minute**";
	            				else language = "Leave the voice channel after **" + (int)(timeLeft - timeUp) + " minutes**";
	            			}
	 
	            			tc.editMessageById(botTimerId, language).complete();
	            		}
	            		
	            		catch(Exception e) {
	            			refresh = 0;
	            		}
	            	}
	            	
	            	else {
	            		if(refresh == 0) {
	            			if((int)(timeLeft - timeUp) == 1) {
	            				String language = "**" + (int)(timeLeft - timeUp) + "분** 뒤에 음성채널을 나가요";
		            			if(setTimerLanguage.equals("eng"))
		            				language = "Leave the voice channel after **" + (int)(timeLeft - timeUp) + " minute**";
		
	            				tc.sendMessage(language).queue();
	            		    }
	            		}
	            	}
	
	            	if(timeUp == timeLeft) {
	            		timeLeft = 0;
	            		timeUp = 0;
	            		timerIsOn = 0;
	            		autoOff.cancel();
	            		autoOff = null;
	
	            		if(refresh == 1) {
		            		
			        		refresh = 0;
			        		
			        		fn.removeMessage(tc, botTimerId, userTimerId);
			        		
	            		}
		        		
	            	
	            		MusicController.stopPlaying(tc, msg, event, 1, save, setTimerLanguage);
	
	            	}
            	};
            	
            	Thread t1 = new Thread(edit);
            	t1.start();
            	
            	}

        };

        autoOff.scheduleAtFixedRate(task, 60000, 60000);
        	
	}
	
	public void updatePing(MessageReceivedEvent event, TextChannel tc, Message response) {

		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	Runnable edit = () -> {
            		if(tc.getGuild().getJDA().getStatus().toString().equals("CONNECTED")) {
		        	    File file2 = new File(BotMusicListener.directoryDefault + "networkStats");
		       			try {
		           			FileReader filereader2 = new FileReader(file2);
		           	       
		           	        BufferedReader bufReader2 = new BufferedReader(filereader2);
		           	        String line = "";
		           	        int stat = 0;
		           	        while((line = bufReader2.readLine()) != null){
		           	        	 stat = Integer.parseInt(line);
		           	        }
		           	               
		           	        bufReader2.close();
		           	        
		           	        if(stat == 1) {
		           	        	statstr = ":warning: `비상용 와이파이에 연결했어요`";
		           	        }
		           	        
		           	        else if(stat == 2) {
		           	        	statstr = ":sos: `네트워크가 매우 좋지 못해요`";
		           	        }
		           	        
		           	        else if(stat == 3) {
		           	        	statstr = ":desktop: `유선 이더넷에 연결했어요`";
		           	        }
		       			}
		       			catch(Exception e) {
		       				
		       				tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
		       				log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
		       			}
		       			
		            	long ping = event.getJDA().getGatewayPing();
		            	long time = System.currentTimeMillis();
	            	
		                	
		                try {
			                response.editMessage(statstr + "\n지연시간: " + recentPing1 + " ms\n응답시간: " + recentPing2 + " ms").complete();
			                	recentPing2 = System.currentTimeMillis() - time;
			                	recentPing1 = ping;
			                		
			                response.editMessage(statstr + "\n지연시간: " + recentPing1 + " ms\n응답시간: " + recentPing2 + " ms").complete();
		                }
		                catch(Exception e) {
		                	
		                	if(pingRun == 1) {
		        				pingTimer.cancel(); 
		        				pingTimer = null;
		        				pingRun = 0;	
		        			}
		                	
		                	try {
		                		response.delete().complete();
		                	}
		                	catch(Exception f) {}
		                	
		                	fn.removeMessage(tc, pingUserMessageId);
		                	
		                }
            		}
            	};
            	
            	Thread t = new Thread(edit);
            	t.start();
            }
		};
		

        pingTimer.scheduleAtFixedRate(task, 20000, 20000);
	}

	//nowInfo
	public EmbedBuilder embedNow(EmbedBuilder eb, String lan) {
		eb.setColor(Color.decode(BotMusicListener.colorDefault));
	    eb.setThumbnail("https://img.youtube.com/vi/" + trackInfo.getIdentifier() + "/mqdefault.jpg");
			
	    if (player.isPaused()) {
	    	String language = MusicController.realTitle(trackInfo.getInfo().title) + " (일시정지됨)";
			if(lan.equals("eng")) 
				language = MusicController.realTitle(trackInfo.getInfo().title) + " (Paused)";
				
			eb.setTitle(language, trackInfo.getInfo().uri);	
		}
			
		else {
			eb.setTitle(MusicController.realTitle(trackInfo.getInfo().title) , trackInfo.getInfo().uri);
		}
	    	
	    String languageUploader = "올린 이";
		String languageTime = "시간";
		String languagePosition = "위치";
			
		if(lan.equals("eng")) {
			languageUploader = "Uploader";
			languageTime = "Duration";
			languagePosition = "Position";
		}
			
	    eb.addField(languageUploader, MusicController.realTitle(trackInfo.getInfo().author), true);
	    
	    if(trackInfo.getInfo().isStream == true) {
			if(lan.equals("eng"))
				eb.addField(languageTime, "LIVE", true);
			else
				eb.addField(languageTime, "생방송", true);
	    }
	    
	    else
	    	eb.addField(languageTime, fn.secTo((int)trackInfo.getPosition(), 1) + "/" + fn.secTo((int)trackInfo.getDuration(), 1), true);

	    eb.addField(languagePosition, (int)(current + 1) + "/" + queue.size(), true);
		
		return eb;
	}
	
	//nowPlay
	public EmbedBuilder playBox(EmbedBuilder re, String lan, int page) {
		
		double f = Math.ceil(queue.size()/10f);
		int maxFinal = (int)f;
			
		int pageFinal = ((int)(this.current + 1) / 10) + 1;
		
		if(page > 0) {
			pageFinal = page;
			queuePage = page;
		}
		else {
			queuePage = 0;
		}

		if(pageFinal > maxFinal) {
			pageFinal = maxFinal;
			queuePage = maxFinal;
		}
		
		StringBuilder ret = new StringBuilder();
		
		
		long duration = 0;
		long totalDuration = 0;
		
		duration = trackInfo.getDuration() - trackInfo.getPosition();
		
		for(int i = 0; i<queue.size(); i++) {
			totalDuration = totalDuration + queue.get(i).getDuration();
			
			if(i > current)
				duration = duration + queue.get(i).getDuration();
		}
		
		
		if(last == 1) {
			duration = queue.get(current).getDuration() - trackInfo.getPosition();
		}
		
		else if(lastLong == 1) {
			duration = 0;
			int showEnd = lastNum;
			
			if(current > lastNum - 1) {
				showEnd = queue.size();
				for(int i = 0; i<lastNum; i++) {
					duration = duration + queue.get(i).getDuration();
				}
			}
			
			for(int i = 0; i<showEnd; i++) {
				if(i > current)
					duration = duration + queue.get(i).getDuration();
				else if(i == current)
					duration = duration + queue.get(i).getDuration() - trackInfo.getPosition();
			}
			
		}
		
		if(timeLeftQueue > 0) {
			timeLeftQueue = timeLeftQueue - 3000;
			re.setColor(Color.decode(BotMusicListener.colorSetTimer));
			
			if(last == 1 || lastLong == 1) {
				if(duration > timeLeftQueue)
					duration = timeLeftQueue;
			}
			else {
				duration = timeLeftQueue;
			}
			
		}
		else
			re.setColor(Color.decode(BotMusicListener.colorDefault));
	
		String duraStr = " | - " + fn.secTo((int)duration, 1);
		if(duration < 0) {
			duraStr = "";
		}
		
		StringBuilder author = new StringBuilder();
		if(lan.equals("eng")) {
			if(queue.size() == 1)
				author.append("PLAYLIST | " + queue.size() + " item" + duraStr);
			else
				author.append("PLAYLIST | " + queue.size() + " items" + duraStr);
		}
		else 
			author.append("재생목록 | 총 " + queue.size() + "개" + duraStr);

		if(loadCount > 0) {
			
			String languageItem = "\n로딩이 덜 됐어요 (" + loadCount + "개 남음)";
			if(lan.equals("eng")) {
				languageItem = "\nNot ended loading (" + loadCount + "left)";
			}
					
			author.append(languageItem);
			System.out.println("BOT: " + languageItem);

		}

		int show = pageFinal*10;
		if(pageFinal == maxFinal) {
			show = queue.size();
		}
		
		for(int i = pageFinal*10 - 10; i<show; i++) {
			
			String title = MusicController.realTitle(queue.get(i).getInfo().title);
	
			if(player.getPlayingTrack().getInfo().uri == queue.get(i).getInfo().uri) {
				if(last == 1)
					ret.append("`" + (int)(i+1) + ".` **[" + queue.get(i).getInfo().title.replaceAll("\\*", "\\\\*").replaceAll("_", "\\_").replaceAll("~", "\\~").replaceAll("`", "\\`") + "](" + queue.get(i).getInfo().uri + ")**\n");
				else 
					ret.append("`" + (int)(i+1) + ".` [" + queue.get(i).getInfo().title.replaceAll("\\*", "\\\\*").replaceAll("_", "\\_").replaceAll("~", "\\~").replaceAll("`", "\\`") + "](" + queue.get(i).getInfo().uri + ")\n");
			}
			else {
				if(lastLong == 1) {
					if(i == lastNum - 1)
						ret.append("`" + (int)(i+1) + ".` **" + title + "**\n");
					else
						ret.append("`" + (int)(i+1) + ".` " + title + "\n");
				}
				else
					ret.append("`" + (int)(i+1) + ".` " + title + "\n");
			}
				
		}
		
		if(pageFinal != maxFinal) {
			ret.append("`...`");
		}
			
		String languagePage = "페이지 " + pageFinal + "/" + maxFinal + "  |  " + fn.secTo((int)totalDuration, 1);
		if(lan.equals("eng")) {
		    languagePage = "Page " + pageFinal + "/" + maxFinal + "  |  " + fn.secTo((int)totalDuration, 1);
		}
	
		re.setAuthor(author.toString());
		re.setFooter(languagePage);
		re.setDescription(ret);
		
		
		return re;	
		
	}

	@Override 
	public void onTrackStart(AudioPlayer player, AudioTrack track)
	{
	    trackInfo = track;
	    queuePage = 0;
	   
	}

	@Override 
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
	{
		
		if (endReason.mayStartNext)
		{
			if(last == 1) {
				if(tc.getGuild().getId().equals(BotMusicListener.born)) MusicController.stopPlaying(tc, msg, event, 2, save, setLanguageNext);
				else MusicController.stopPlaying(tc, msg, event, 2, 0, setLanguageNext);
			}
			else nextTrack(tc, msg, event, 0, 0, setLanguageNext);
		}
		
	}
	
	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
	   
		if(queue.size() == 1) {
			String language = ":wrench: 플레이어에 문제가 생겨 **음성채널을 나갔어요**";
		    if(setLanguageNext.equals("eng")) language = ":wrench: Track got stuck, **left the voice channel**.";
		    tc.sendMessage(language).queue();
		    	
		    log(tc, event, "BOT: `(" + tc.getGuild().getName() + ", " + thresholdMs + "ms)` " + language);
		    MusicController.stopPlaying(tc, msg, event, 6, save, setLanguageNext);
		    
			return;
		}
		
	    String language = ":wrench: 문제가 있어 다음 항목 재생을 권장해요";
	    if(setLanguageNext.equals("eng")) language = ":wrench: Track got stuck, recommand to play next item.";
	    tc.sendMessage(language).queue();
	    	
	    log(tc, event, "BOT: `(" + tc.getGuild().getName() + ", " + thresholdMs + "ms)` " + language);
	}
	
	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {

		try {
			String language = ":wrench: **" + exception.getMessage() + "**" + fn.cause(exception);
		    tc.sendMessage(language).queue();
		    log(tc, event, "BOT: `(" + tc.getGuild().getName() + ")` " + language);
		    
		    if(exception.getMessage().contains("not supported")) {
		    	int nu = current;
				if(nu == 0)
					nu = queue.size();
				
				remove(String.valueOf(nu-1), tc, event, 0, 0, setLanguageNext);
		    }
		    	
		}
		catch(Exception e) {
			int nu = current;
			if(nu == 0)
				nu = queue.size();
			
			String language = ":wrench: `" + nu + ".` **" + MusicController.realTitle(track.getInfo().title) + "** 항목에 문제가 있어 건너뛰었어요";
		    if(setLanguageNext.equals("eng")) language = ":wrench: `" + nu + ".` **" + MusicController.realTitle(track.getInfo().title) + "** have problem, playing next item.";
		    tc.sendMessage(language).queue();
		    	
		    log(tc, event, "BOT: `(" + tc.getGuild().getName() + ")` " + language);
		}
	}

	@Override 
	public void onPlayerPause(AudioPlayer player) {
		
	}

	@Override 
	public void onPlayerResume(AudioPlayer player) {
		
	}
	
	public void last(TextChannel tc, Message msg, MessageReceivedEvent event, User user, int i, int lastNum, String lan) {
		this.tc = tc;
		this.event = event;
		
		
		if(queue.isEmpty()) {
			userLastId = fn.autoRemoveMessage(tc, msg, userLastId, botLastId);
			
			String language = "재생중인 항목이 없어요";
			if(lan.equals("eng")) language = "There is no item being played.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue(response -> {
				botLastId = response.getId();
			});
			System.out.println(reply);
			log(tc, event, reply);
			
			return;
		}
		
		if(i == 1) {
			
			if(last == 1 && lastNum == -1) {
				userAlreadyLastId = fn.autoRemoveMessage(tc, msg, userAlreadyLastId, botAlreadyLastId);
				
				String language = "이미 마지막설정이 되어있어요";
				if(lan.equals("eng")) language = "Already set last.";
				
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue(response -> {
					botAlreadyLastId = response.getId();
				});
				System.out.println(reply);
				log(tc, event, reply);
				
			}
			else {
				userLastId = fn.autoRemoveMessage(tc, msg, userLastId, botLastId);
				
				if(lastNum == -1) {
					String language = "이 항목이 끝나면 **음성채널을 나가요**";
					if(lan.equals("eng")) {
						setLanguageNext = lan;
						language = "When this item finished, it will **leave the voice channel**.";
					}
					
					String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
					tc.sendMessage(language).queue(response -> {
						botLastId = response.getId();
					});
					System.out.println(reply);
					log(tc, event, reply);
					
					lastLong = 0;
					
					last = 1;
					menu = 6;
					
				}
				else {
					if(lastNum == -2) lastNum = queue.size();
					if(lastNum == current + 1) {
						MusicController.last(tc, msg, user, event, 1, -1, lan);
					}
					else {
						String language = "**" + lastNum + "번째** 항목이 끝나면 **음성채널을 나가요**";
						setLanguageNext = "kor";
						if(lan.equals("eng")) {
							language = "When no." + lastNum + " item finished, it will **leave the voice channel**.";
							setLanguageNext = "eng";
						}
						
						String reply = "BOT: " + language;
						tc.sendMessage(language).queue(response -> {
							botLastId = response.getId();
						});
						System.out.println(reply);
						log(tc, event, reply);
						
						lastLong = 1;
						last = 0;
						menu = 6;
						
						this.lastNum = lastNum;	
					}
				}
			
			}
		
		}
		
		else { 
			
			if(last == 0 && lastLong == 0) {
				userAlreadyLastId = fn.autoRemoveMessage(tc, msg, userAlreadyLastId, botAlreadyLastId);
				
				String language = "이미 마지막 설정이 해제되어있어요";
				if(lan.equals("eng")) language = "Already canceled last setting";
				
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue(response -> {
					botAlreadyLastId = response.getId();
				});
				System.out.println(reply);
				log(tc, event, reply);
				
			}
			else {
				if(last == 1) {
					String language = "이 항목이 끝나도 **계속 재생해요**";
					if(lan.equals("eng")) language = "Even if this item finished, **still play**.";
					
					String reply = "BOT: " + language;
					tc.sendMessage(language).queue();
					System.out.println(reply);
					log(tc, event, reply);
				}
				
				else if(lastLong == 1) {
					String language = "**" + this.lastNum + "번째** 항목이 끝나도 **계속 재생해요**";
					if(lan.equals("eng")) language = "Even if no." + this.lastNum + " item finished, **still play**.";
					
					String reply = "BOT: " + language;
					tc.sendMessage(language).queue();
					System.out.println(reply);
					log(tc, event, reply);
				}
			}
			
			last = 0;
			lastLong = 0;
			this.lastNum = 0;
			menu = 0;
		}
	
	}
	
	public void channel(TextChannel tc, Message msg, String lan) {

		chUserMessageId = fn.autoRemoveMessage(tc, msg, chUserMessageId, chBotMessageId);
		
		if(MusicController.isInTotal == 0) {
			String language = "군밤이가 휴식을 취하고 있나봐요";
			
			if(lan.equals("eng")) 
				language = "Gunbam think taking a break.";
			
			tc.sendMessage(language).queue(response -> {
				chBotMessageId = response.getId();
			});
		}
		else {
			String language = "군밤이가 **" + MusicController.isInTotal + "개 음성채널**에서 소리를 들려주네요";
			
			if(lan.equals("eng")) {
				if(MusicController.isInTotal == 1) language = "Gunbam is in **" + MusicController.isInTotal + " voice channel**.";
				else language = "Gunbam is in **" + MusicController.isInTotal + " voice channels**.";
			}
			
			tc.sendMessage(language).queue(response -> {
				chBotMessageId = response.getId();
			});
		}

	}
	
	public void alert(TextChannel tc) {
	
		File file = new File(BotMusicListener.directoryDefault + "updateInfo.txt");
	
		StringBuilder in = new StringBuilder();

		try{   
            FileReader filereader = new FileReader(file);
        
            BufferedReader bufReader  =  new BufferedReader(filereader);

            String line = "";
            
            while((line = bufReader.readLine()) != null){
            	in.append(line + "\n");
	
            }
      
            bufReader.close();
        }
		catch(Exception e){
            System.out.println(e);
            tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
			log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
        }
		
		
		in.append("공지 하려면 ㅣalert 를 입력하세요");
		
		tc.sendMessage(in).queue();
		
	}

	public void clearQueue(TextChannel tc, Message msg, Message response, Guild guild, MessageReceivedEvent event, int i, int save, String lan) {
		
		if(loadCount > 0) {
			terminate = 1;
			save = 0;
		}
		
		if(queue.size() > 30) save = 0;

		if(save == 1) {
			playAgainList.clear();
			playAgainList.addAll(queue);
			
			File file = new File(BotMusicListener.directoryDefault + "backup/saveQueue.txt");
			try {
			      //파일에 문자열을 쓴다.
			      //하지만 이미 파일이 존재하면 모든 내용을 삭제하고 그위에 덮어쓴다
			      //파일이 손산될 우려가 있다.
			      FileWriter fw = new FileWriter(file);
			      
			      for(int k = 0; k<queue.size(); k++) {
			    	  fw.write(queue.get(k).getInfo().uri + "\n");
			    	  
			      }
			      
			      fw.close();
			    } catch (Exception e) {
			      e.printStackTrace();
			     
			      response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
     			  log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
			    }
			
		}
		
		Runnable end1 = () -> {
			if(runqueue == 1) {
				timer.cancel(); 
				timer = null;
				runqueue = 0;
			}
			
			if(runnow == 1) {
				timer2.cancel(); 
				timer2 = null;
				runnow = 0;
			}
			
			if(emptyRun == 1) {
				emptyTimer.cancel(); 
				emptyTimer = null;
				emptyRun = 0;
			}
			
			if(loadingRun == 1) {
				loadingRun = 0;
				isLoadingTimer.cancel();
				isLoadingTimer = null;
			}
			
			if(refresh == 1) {
				refresh = 0;
			}
			
			if(timeLeft > 0) {
				autoOff.cancel();
				autoOff = null;
				timeLeft = 0;
			}
			
			if(queue.isEmpty()) {}
			else {
				String duration = " (" + fn.secTo((int)trackInfo.getPosition(), 0) + "/" + fn.secTo((int)trackInfo.getDuration(), 0) + ")";
				if(trackInfo.getInfo().isStream == true) {
					duration = " (`생방송`)";
					if(lan.equals("eng"))
						duration = " (`LIVE`)";
				}
		
				String language = "";
				if (player.isPaused()) {
					language = "**(종료됨)** ``" + (int)(current + 1) + ".`` **" + trackbefore + "** 일시정지 중이에요" + duration;
					if(lan.equals("eng")) language = "**(Terminated)** Paused ``" + (int)(current + 1) + ".`` **" + trackbefore + "** now." + duration;
			
				}			 
				else {
					language = "**(종료됨)** ``" + (int)(current + 1) + ".`` **" + trackbefore + "** 재생 중이에요" + duration;
					if(lan.equals("eng")) language = "**(Terminated)** Playing ``" + (int)(current + 1) + ".`` **" + trackbefore + "** now." + duration;
				}
				
				try {
					tc.editMessageById(qBotMessageId, language).complete();
				}
				catch(Exception e) {}
			}
			
			userSaveListId = ""; botSaveListId = "";
			userSavedListId = ""; botSavedListId = "";
			nowUserMessageId = "";nowBotMessageId = "";
			qUserMessageId = ""; qBotMessageId = ""; 
			userTimerId = ""; botTimerId = "";
			userSetTimerId = ""; botSetTimerId = "";
			userShuffleId = ""; botShuffleId = "";
			userLastId = ""; botLastId = "";
			userAlreadyLastId = ""; botAlreadyLastId = "";
			userTryCancelId = ""; botTryCancelId = "";
			autoPausedId = "";
			
			current = 0;
			queuePage = 0;
			
			link.clear();	
			queue.clear();
			listSearch.clear();
			removeMany.clear();
			removeNum.clear();
			trash.clear();
			
		};
		
		int sa = save;
		Runnable end2 = () -> {
			if(i == 0) {
				if(sa == 1) {
					String language = "음성채널을 나갔어요 (목록 저장됨)";
	    			if(lan.equals("eng"))
	    				language = "Left the voice channel. (Saved)";
	    			
	    			String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
					response.editMessage(language).queue();
					System.out.println(reply);
	
					if(tc.getGuild().toString().contains("661044497198743640")) {}
					else log(tc, event, reply);
						
					}
					
				else {
					String language = "음성채널을 나갔어요";
	    			if(lan.equals("eng"))
	    				language = "Left the voice channel.";
	    			
	    			String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
					response.editMessage(language).queue();
					System.out.println(reply);
					
					if(tc.getGuild().toString().contains("661044497198743640")) {}
					else log(tc, event, reply);
						
				}
					
			}
			
			else if(i == 1) {
				if(sa == 1) {
					String language = "타이머가 종료되어 음성채널을 나갔어요 (목록 저장됨)";
	    			if(lan.equals("eng"))
	    				language = "Timer is over so left the voice channel. (Saved)";
	    			
	    			String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
	    			response.editMessage(language).queue();
					System.out.println(reply);
	
					if(tc.getGuild().toString().contains("661044497198743640")) {}
					else log(tc, event, reply);
						
				}
					
				else {
					String language = "타이머가 종료되어 음성채널을 나갔어요";
	    			if(lan.equals("eng"))
	    				language = "Timer is over so left the voice channel. (Saved)";
	    			
	    			String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
	    			response.editMessage(language).queue();
					System.out.println(reply);
	
					if(tc.getGuild().toString().contains("661044497198743640")) {}
					else log(tc, event, reply);
						
				}
					
			}
				
			else if(i == 2) {
				if(sa == 1) {
					String language = "항목이 끝나 음성채널을 나갔어요 (목록 저장됨)";
	    			if(lan.equals("eng"))
	    				language = "Item is over so left the voice channel. (Saved)";
	    			
	    			String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
	    			response.editMessage(language).queue();
					System.out.println(reply);
	
					if(tc.getGuild().toString().contains("661044497198743640")) {}
					else log(tc, event, reply);
						
				}
					
				else {
					String language = "항목이 끝나 음성채널을 나갔어요";
	    			if(lan.equals("eng"))
	    				language = "Item is over so left the voice channel.";
	    			
					String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
					response.editMessage(language).queue();
					System.out.println(reply);
	
					if(tc.getGuild().toString().contains("661044497198743640")) {}
					else log(tc, event, reply);
						
				}
					
			}
				
			else if(i == 3) {
				if(sa == 1) {
					String language = ":loudspeaker: 연결이 **끊어졌어요** (목록 저장됨)";
	    			if(lan.equals("eng"))
	    				language = ":loudspeaker: **Disconnected** from the voice channel. (Saved)";
	    			
					String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
					response.editMessage(language).queue();
					System.out.println(reply);
	
					if(event.getAuthor().getId().equals(BotMusicListener.admin)||tc.getGuild().getId().equals(BotMusicListener.base)) {}
					else log(tc, event, reply);
						
				}
					
				else {
					String language = ":loudspeaker: 연결이 **끊어졌어요**";
	    			if(lan.equals("eng"))
	    				language = ":loudspeaker: **Disconnected** from the voice channel.";
	    			
					String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
					response.editMessage(language).queue();
					System.out.println(reply);
					
					if(event.getAuthor().getId().equals(BotMusicListener.admin)||tc.getGuild().getId().equals(BotMusicListener.base)) {}
					else log(tc, event, reply);
						
				}
					
			}
				
			else if(i == 4) {
				if(sa == 1) {	
					response.editMessage("원격으로 음성채널을 나갔어요 (목록 저장됨)").queue();
				}
					
				else {
					response.editMessage("원격으로 음성채널을 나갔어요").queue();	
				}
			}
				
			else if(i == 5) {
				if(tc.getGuild().toString().contains("661044497198743640")) {}
				else {
					log(tc, event, "BOT: `(" + tc.getGuild().getName() + ")` 아무조치도 하지 않아 음성채널을 나갔어요");
				}
				
				response.delete().queue();
			}
			
			else if(i == 6) {
				response.delete().queue();
			}
			
			else if(i == 7) {
				if(sa == 1) {
					String language = "세션이 재설정되었어요 (목록 저장됨)";
	    			if(lan.equals("eng"))
	    				language = "Connection reset. (Saved)";
	    			
					String reply = "BOT: " + language;
					response.editMessage(language).queue();
					System.out.println(reply);
	
					if(tc.getGuild().toString().contains("661044497198743640")) {}
					else log(tc, event, reply);
						
				}
					
				else {
					String language = "세션이 재설정되었어요";
	    			if(lan.equals("eng"))
	    				language = "Connection reset.";
	    			
					String reply = "BOT: " + language;
					response.editMessage(language).queue();
					System.out.println(reply);
					
					if(tc.getGuild().toString().contains("661044497198743640")) {}
					else log(tc, event, reply);
						
				}
					
			}
			
		};

		Thread t1 = new Thread(end1);
		t1.start();

		Thread t2 = new Thread(end2);
		t2.start();

		search = 0;
	
		player.startTrack(null, false);
		player.setPaused(false);
		player.destroy();
		
		timerIsOn = 0;
		timeLeftQueue = 0;
		loadCount = 0;
		counting = 0;
		countend = 1;
		
		menu = 0;
		lastLong = 0;
		last = 0;
		lastNum = 0;
		playStandByInt = 0;
		autoPaused = 0;
		isIn = 0;
		lock = 0;
		
		setLanguageNext = "kor"; 
		setTimerLanguage = "kor"; 
		setLanguageList = "kor"; 
		setLanguageStop = "kor";
		
		if(tc.getGuild().getId().equals("533443336678146078"))
			this.save = 1;
		

	}

	public boolean getIsPlaying()
	{
		return isPlaying;
	}
	
	public void log(TextChannel tc, MessageReceivedEvent event, String str) {
		if(BotMusicListener.logOn == 1) {
			if(event == null) {}
			else {
				if(event.getAuthor().getId().equals(BotMusicListener.admin)||tc.getGuild().getId().equals(BotMusicListener.base)||tc.getId().equals("717203670365634621")) {}
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
		      
		      BotMusicListener.adtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
 			  
		}
	}
	
	public void reset(TextChannel tc) {
		Date date = new Date();
    	SimpleDateFormat dayTime = new SimpleDateFormat("MM월dd일");
		String str = dayTime.format(date);
		
		if(resetDate.equals(str)) {
			tc.sendMessage("하루에 한 번만 가능해요").queue();
			log(tc, event, "BOT: 하루에 한 번만 가능해요");
			return;
		}
		
		else {
			menuStr = "";

			recentAddPlayListCount = 0;

			timer = null;
			timer2 = null;
			
			runqueue = 0;
			runnow = 0;
			
			timerOn = 0;
			
			autoOff = null;
			pingTimer = null;
			emptyTimer = null;
			queuePage = 0;
			
			timeLeft = 0;
			timeUp = 0;
			refresh = 0;
			pingRun = 0;
			emptyRun = 0;
			
			userSaveListId = ""; botSaveListId = "";
			userSavedListId = ""; botSavedListId = "";
			nowUserMessageId = ""; nowBotMessageId = "";
			qUserMessageId = ""; qBotMessageId = ""; 
			userTimerId = ""; botTimerId = "";
			userSetTimerId = ""; botSetTimerId = "";
			userShuffleId = ""; botShuffleId = "";
			userLastId = ""; botLastId = "";
			userAlreadyLastId = ""; botAlreadyLastId = "";
			userTryCancelId = ""; botTryCancelId = "";
			
			autoPausedId = "";
			if(tc.getGuild().toString().contains(BotMusicListener.base)) {}
			else {
				pingUserMessageId = ""; pingBotMessageId = "";
				recentPing1 = 0;
				recentPing2 = 0;
			}
			chUserMessageId = ""; chBotMessageId = "";
			userVolumeMessageId = ""; botVolumeMessageId = "";
			
			//many

			save = 1;
			lastLong = 0;
			
			enteredTime = 0;
			timUp = 0;
			
			setLanguageNext = "kor"; 
			setTimerLanguage = "kor"; 
			setLanguageList = "kor"; 
			setLanguageStop = "kor";
			
			tc.sendMessage("이 채널의 매개변수 값들을 **재설정 했어요**").queue();
			log(tc, event, "BOT: 이 채널의 매개변수 값들을 **재설정 했어요**");
		}
		
		resetDate = str;
	}

}