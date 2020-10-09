package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import functions.*;
import main.BotMusicListener;
import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
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
	final AudioPlayer player;
	
	CustomFunctions fn = new CustomFunctions();
	
	ArrayList<AudioTrack> queue;
	
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
	Member member;
	
	int isPlay = 0;
	int queuePage = 0;
	int removed = 0;
	int menu = 0;
	String menuStr = "";
	
	int recentAddPlayListCount = 0;
	int recentAddURLCount = 0;

	Timer timer = null;
	Timer timer2 = null;
	
	int runqueue = 0;
	int runnow = 0;
	
	int timerOn = 0;
	
	Timer autoOff = null;
	Timer pingTimer = null;
	Timer emptyTimer = null;
	Timer isLoadingTimer = null;
	Timer pausedTimeTimer = null;
	
	int timeLeft = 0;
	int timeLeftQueue = 0;
	int timeUp = 0;
	int timerIsOn = 0;
	int refresh = 0;
	int pingRun = 0;
	int emptyRun = 0;
	int loadingRun = 0;
	int pausedTimeRun = 0;
	
	long recentPing1 = 0;
	long recentPing2 = 0;

	
	
	String userSavedListId = "", botSavedListId = "";
	String userSaveListId = "", botSaveListId = "";
	String userCannotSaveListId = "", botCannotSaveListId = "";
	String nowUserMessageId = "", nowBotMessageId = "";
	String qUserMessageId = "", qBotMessageId = "";
	String userTimerId = "", botTimerId = "";
	String userSetTimerId = "", botSetTimerId = "";
	String userShuffleId = "", botShuffleId = "";
	String userLastId = "", botLastId = "";
	String userAlreadyLastId = "", botAlreadyLastId = "";
	String userTryCancelId = "", botTryCancelId = "";
	String userCancelId = "", botCancelId = "";
	String userPlayAgainId = "", botPlayAgainId = "";
	
	String pingUserMessageId = "", pingBotMessageId = "";
	String chUserMessageId = "", chBotMessageId = "";
	String autoPausedId = "";
	
	String userVolumeMessageId = "", botVolumeMessageId = "";
	String userAlreadyVolumeMessageId = "", botAlreadyVolumeMessageId = "";
	
	String statstr = "";
	String voiceChannelId = "";
	
	//only bot message
	String setVolumeMessageId = "";
	
	// old, new
	String mode = "new";
	
	//waitLoading
	String waitQueueAgainPersonal = "", waitQueueAgainPersonalUser = "";
	String waitShuffle = "";
	String waitRemoveMany = "", waitRemoveManyUser = "";
	String waitRemove = "", waitRemoveUser = "";
	String waitRemoveTitle = "", waitRemoveTitleUser = "";
	
	List<AudioTrack> removeMany = new ArrayList<>();
	List<Integer> removeNum = new ArrayList<>();
	
	List<String> playAgainStringList = new ArrayList<>();
	List<String> playAgainStringListBefore = new ArrayList<>();
	List<AudioTrack> playAgainList = new ArrayList<>();
	List<AudioPlaylist> audioPlaylist = new ArrayList<>();
	
	HashMap<String, List<AudioTrack>> playAgainPersonalList = new HashMap<>();
	HashMap<String, List<String>> playAgainPersonalIsPlaylistList = new HashMap<>();
	
	HashMap<String, Timer> wantToPlayHashTimer = new HashMap<>();
	HashMap<String, Message> wantToPlayHashMessage = new HashMap<>();
	HashMap<String, String> wantToPlayHashQuery = new HashMap<>();
	
	boolean pausedQueueTitle = false;
	boolean pausedNowInfo = false;
	boolean wasDeletedPlaylistElements = false;
	
	//many
	int waitingToReconnect = 0;
	int playAgainEdited = 0;
	int loadCount = 0;
	int terminate = 0;
	
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
	String waitingPlay = "";
	
	User userMessage;
	
	
	String resetDate = "";
	String setLanguageNext = "kor", setTimerLanguage = "kor", setLanguageList = "kor", setLanguageStop = "kor", setLanguageNow = "kor", setLanguageLast = "kor";

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

	public void queue(TextChannel tc, Member member, MessageReceivedEvent event, AudioTrack track, int add, int index, int ended)
	{
		
		this.tc = tc;
		this.member = member;
		
		if(add == 1) queue.add(track);
		else if(add == 2) {
			queue.add(index, track);
		}

		if(isPlay == 0) {	

			player.startTrack(track.makeClone(), false);
		    trackInfo = player.getPlayingTrack();
		    
			isPlay = 1;
			
			waitingToReconnect = 1;
			if(tc.getGuild().getAudioManager().isSelfDeafened()) 
				tc.getGuild().getAudioManager().setSelfDeafened(false);
	
			if(loadingRun == 1) {
				loadingRun = 0;
				isLoadingTimer.cancel();
				isLoadingTimer = null;
			}
			
			loadingRun = 1;
			if(isLoadingTimer == null) 
				isLoadingTimer = new Timer();
			checkLoading(tc, event);
			
			trackbefore = queue.get(current).getInfo().uri;
			try {
				trackbefore = MusicController.realTitle(queue.get(current).getInfo().title);
			}
			catch(Exception e) {
				
				/*
				if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
        			MusicController.stopPlaying(tc, msg, event, 8, save, setLanguageStop);
                }
        			
                else {
                	MusicController.stopPlaying(tc, msg, event, 8, 0, setLanguageStop);
                }
                */
			}
			
		}
		
		if(ended == 1) {
			finishLoading();
		}
		
		if(!waitingPlay.equals("")) {
			try {
				tc.deleteMessageById(waitingPlay).complete();
			}
			catch(Exception e) {}
			waitingPlay = "";
		}
		
		
	}
	
	public void disconnectVoice(Guild guild, VoiceChannel vc) {
		
		if(!guild.getAudioManager().getConnectionStatus().toString().toLowerCase().contains("not")) {
			Date date = new Date();
	    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
			String str = dayTime.format(date);
			
			if(tc.getGuild().getId().equals(BotMusicListener.base) || event.getAuthor().getId().equals(BotMusicListener.admin)) {}
			else BotMusicListener.logtc.sendMessage(".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n").queue();
	
			
			if(Main.saveQueueAllowGuild.contains(guild.getId())) {
				MusicController.stopPlaying(tc, msg, event, 3, save, setLanguageStop);
	        }
				
	        else {
	        	MusicController.stopPlaying(tc, msg, event, 3, 0, setLanguageStop);
	        }
		}
	}
	
	
	public void checkLoading(TextChannel tc, MessageReceivedEvent event) {
		
		TimerTask task = new TimerTask() {
            @Override
            public void run() {

            	loadCount = 0;
            	finishLoading();
            	/*
            	if(tc.getGuild().getJDA().getStatus().toString().equals("WAITING_TO_RECONNECT")) {
	            	if(isPlay == 0) {}
	            		
	            }
            	*/
            }
        };

        isLoadingTimer.schedule(task, 30000);
	}
	
	public void finishLoading() {
		if(!waitQueueAgainPersonal.equals("")) {
			queueAgainPersonal(fn.waitQueueAgainPersonalTc, fn.waitQueueAgainPersonalMessage, fn.waitQueueAgainPersonalId, fn.waitQueueAgainPersonalLan, true);
			return;
		}
		
		if(!waitShuffle.equals(""))
			shuffle(fn.waitShuffleTc, fn.waitShuffleMessage, fn.waitShuffleEvent, fn.waitShuffleLan, true);
		
		if(!waitRemoveMany.equals(""))
			removeMany(fn.waitRemoveManyList, fn.waitRemoveTc, fn.waitRemoveMessage, fn.waitRemoveEvent, fn.waitRemoveLan, true);
		
		if(!waitRemove.equals(""))
			remove(fn.waitRemoveItemstr, fn.waitRemoveTc, fn.waitRemoveMessage, fn.waitRemoveEvent, 1, fn.waitRemoveLan, true);
		
		if(!waitRemoveTitle.equals(""))
			removeTitle(fn.waitRemoveTitleList, fn.waitRemoveTc, fn.waitRemoveMessage, fn.waitRemoveEvent, fn.waitRemoveLan, true);
	}
	
	public void recoveredNetwork(TextChannel tc) {
		if((removed == 1 && (queue.size() > 30 || queue.isEmpty())) || playAgainStringList.size() > 30 || (!playAgainStringList.toString().contains("list=") && (queue.size() > 30 || queue.isEmpty()))) {
			current = 0;
			BotMusicListener.update(tc.getGuild());
			return;
		}

		String language = ":globe_with_meridians: 네트워크가 **복구되었어요** (목록 복구됨)";
		if(setLanguageStop.equals("eng"))
			language = ":globe_with_meridians: **Reconnected** to discord. (Recovered)";
		
		String reply = "BOT: " + language;
		tc.sendMessage(language).queue();
		System.out.println(reply);
		
		if(tc.getGuild().getId().equals(BotMusicListener.base)) {}
		else log(tc, event, reply);
		
		fn.removeMessage(tc, setVolumeMessageId);
		enteredTime = System.currentTimeMillis();
		
		MusicController.isInTotal = 0;
		
		MusicController.connectToVoiceChannel(tc, msg, member, tc.getGuild().getAudioManager(), event, setLanguageStop);
		
	}
	
	public void queueAgain(TextChannel tc, Message msg, String lan) {

		recentAddPlayListCount = 0;
		counting = 0;
		countend = 0;
		
		String languageT = ":floppy_disk: 불러오는 중...";
		if(lan.equals("eng"))
			languageT = ":floppy_disk: Loading...";
		
		tc.sendMessage(languageT).queue(response -> {
			
			if(playAgainList.isEmpty()) {
				File file = new File(BotMusicListener.directoryDefault + "backup/saveQueue" + tc.getGuild().getId() + ".txt");
	
				if(file.length()<=1) {
					userPlayAgainId = fn.autoRemoveMessage(tc, msg, userPlayAgainId, botPlayAgainId);
					
					String language = "이전에 재생한 항목이 없어요";
					if(lan.equals("eng"))
						language = "File is empty.";
					
					response.editMessage(language).queue(botMsg -> {
						botPlayAgainId = botMsg.getId();
					});
					System.out.println("BOT: " + language);
					log(tc, event, "BOT: " + language);
					
					return;
				}
			
				List<String> isPlayList = new ArrayList<>();
				List<String> notPlayList = new ArrayList<>();
				try{   
		            //입력 스트림 생성
		            FileReader filereader = new FileReader(file);
		            //입력 버퍼 생성
		            BufferedReader bufReader  =  new BufferedReader(filereader);
	
		            String line = "";

		    		playAgainStringList.clear();
		            while((line = bufReader.readLine()) != null) {
		            	
		            	if(line.contains("list=")) 
		            		isPlayList.add(line);
		            	else
		            		notPlayList.add(line);
		            	
		            	playAgainStringList.add(line);
		            	
		            }
		            //.readLine()은 끝에 개행문자를 읽지 않는다.            
		            bufReader.close();
		        }
				catch(Exception e){
		            System.out.println(e);
		            response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
	   				log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
		        }
	
				String state = "이전에 재생했던 **" + notPlayList.size() + "개** 항목을 재생해요";
				if(!isPlayList.isEmpty() && notPlayList.isEmpty()) {
					state = "이전에 재생했던 **재생목록 " + isPlayList.size() + "개**를 재생해요";
				}
				else if(!isPlayList.isEmpty()) {
					state = "이전에 재생했던 **" + notPlayList.size() + "개** 항목과 **재생목록 " + isPlayList.size() + "개**를 재생해요";
				}
				
				response.editMessage(state).queue();
				System.out.println("BOT: " + state);
				
				if(waitingPlay.equals("")) {
					tc.sendMessage(":satellite: `추가 대기 중...`").queue(waitMsg -> {
						waitingPlay = waitMsg.getId();

						loadCount = playAgainStringList.size();
						terminate = 0;
						
						int end = 0;
						for(int i = 0; i<playAgainStringList.size(); i++) {
							String track = playAgainStringList.get(i).toString();
								 
							if(i == playAgainStringList.size() - 1) end = 1;
							MusicController.loadAndPlaySub(tc, msg, track, event, response, 1, end, lan, null);
						}
				
					});
				}
			}
			else {
				queue.addAll(playAgainList);
				recentAddPlayListCount = playAgainList.size();
				
				queue(tc, msg.getMember(), event, queue.get(0), 0, 0, 1);
				
				if(playAgainStringList.isEmpty()) {
					for(int i = 0; i<playAgainList.size(); i++) {
						playAgainStringList.add(playAgainList.get(i).getInfo().uri);
					}
				}
				
				List<String> isPlayList = new ArrayList<>();
				List<String> notPlayList = new ArrayList<>();
				
				for(int i = 0; i<playAgainStringList.size(); i++) {
					
					if(playAgainStringList.get(i).contains("list=")) {
						isPlayList.add(playAgainStringList.get(i));
					}
					else
						notPlayList.add(playAgainStringList.get(i));
				}
				
				String state = "이전에 재생했던 **" + notPlayList.size() + "개** 항목을 **다이렉트로** 재생해요";
				if(!isPlayList.isEmpty() && notPlayList.isEmpty()) {
					state = "이전에 재생했던 **재생목록 " + isPlayList.size() + "개**를 **다이렉트로** 재생해요";
				}
				else if(!isPlayList.isEmpty()) {
					state = "이전에 재생했던 **" + notPlayList.size() + "개** 항목과 **재생목록 " + isPlayList.size() + "개**를 **다이렉트로** 재생해요";
				}
				
				response.editMessage(state).queue();
				System.out.println("BOT: " + state);
			}
			
			userPlayAgainId = ""; botPlayAgainId = "";
			
			if(isIn == 0) {
				isIn = 1;
				enteredTime = System.currentTimeMillis();
				
				MusicController.connectToVoiceChannel(tc, msg, msg.getMember(), tc.getGuild().getAudioManager(), event, lan);
				
			}
			
			menu = 8;	
			
		});
		
	}
	
	public void queueAgainPersonal(TextChannel tc, Message msg, String id, String lan, boolean isWait) {

		VoiceChannel myChannel = msg.getMember().getVoiceState().getChannel();
		VoiceChannel botChannel = tc.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
		
		if(tc.getGuild().getAudioManager().isConnected() && myChannel != botChannel) {
			String language = "**" + botChannel.getName() + "**에 들어간 후 시도하세요";
			if(lan.equals("eng")) 
				language = "Join **" + botChannel.getName() + "** and try again.";
			
			tc.sendMessage(language).queue();
			
			log(tc, event, "BOT: " + language);
			return;
		}
		
		trash.clear();
		recentAddPlayListCount = 0;
		counting = 0;
		countend = 0;
		
		if(isWait == false) {
			if(loadCount > 0) {
				String language = ":hourglass_flowing_sand: 로딩이 다 되면 실행돼요";
				if(lan.equals("eng"))
					language = ":hourglass_flowing_sand: Auto execute when finish loading.";
				
				tc.sendMessage(language).queue(response -> {
					waitQueueAgainPersonal = response.getId();
					fn.waitQueueAgainPersonalTc = tc;
					fn.waitQueueAgainPersonalMessage = msg;
					fn.waitQueueAgainPersonalId = id;
					fn.waitQueueAgainPersonalLan = lan;
					
				});
				System.out.println("BOT: " + language);
				
				return;
			}
		}
		
		String language = ":flashlight: 불러오는 중...";
		if(lan.equals("eng"))
			language = ":flashlight: Loading...";
		
		if(waitQueueAgainPersonal.equals("")) {
			tc.sendMessage(language).queue(response -> {
				queueAgainPersonalReal(tc, msg, response, id, lan);
		
			});
		}
		else {
			queueAgainPersonalReal(tc, msg, null, id, lan);
		}
		
		if(isIn == 0)
			playAgainStringList.clear();
		
	}
	
	public void queueAgainPersonalReal(TextChannel tc, Message msg, Message response, String id, String lan) {

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
			
			if(playAgainPersonalList.get(id) == null) {
				queueAgainPersonalNormal(tc, msg, response, o, id, lan);
				
				return;
			}
			
			if(playAgainPersonalList.get(id).isEmpty()) {
				
				queueAgainPersonalNormal(tc, msg, response, o, id, lan);
			}
			
			else {
				if(isIn == 0) {
					isIn = 1;
					enteredTime = System.currentTimeMillis();
					
					if(response != null)
						MusicController.connectToVoiceChannel(tc, msg, msg.getMember(), tc.getGuild().getAudioManager(), event, lan);
				}
				
				for(int i = 0; i<playAgainPersonalList.get(id).size(); i++) {
					queue.add(playAgainPersonalList.get(id).get(i).makeClone());
					trash.add(playAgainPersonalList.get(id).get(i).makeClone());
				}
				
				queue(tc, msg.getMember(), event, playAgainPersonalList.get(id).get(0), 0, 0, 1);
				
				List<String> isPlayList = new ArrayList<>();
				List<String> notPlayList = new ArrayList<>();
				
				for(int i = 0; i<playAgainPersonalIsPlaylistList.get(id).size(); i++) {
					if(playAgainPersonalIsPlaylistList.get(id).get(i).contains("list=")) isPlayList.add("list=");
					else notPlayList.add("single");
					
				}
				
				recentAddURLCount = playAgainPersonalIsPlaylistList.get(id).size();
				playAgainStringList.addAll(playAgainPersonalIsPlaylistList.get(id));
				
				String language = "**" + o + "**님이 저장했었던 " + "**" + notPlayList.size() + "개** 항목을 **다이렉트로** 추가했어요";
	    		if(lan.equals("eng")) {
	    			if(notPlayList.size() == 1) language = "Added directly **" + notPlayList.size() + " item** that **" + o + "** did saved.";
	    			else language = "Added directly **" + notPlayList.size() + " items** that **" + o + "** did saved.";
	    		}
	    		
				if(!isPlayList.isEmpty() && notPlayList.isEmpty()) {
					language = "**" + o + "**님이 저장했었던 **재생목록 " + isPlayList.size() + "개**를 **다이렉트로** 추가했어요";
					if(lan.equals("eng")) {
						language = "Added directly **" + notPlayList.size() + " playlist** that **" + o + "** did saved.";
		    		}
				}
				else if(!isPlayList.isEmpty()) {
					language = "**" + o + "**님이 저장했었던 " + "**" + notPlayList.size() + "개** 항목과 **재생목록 " + isPlayList.size() + "개**를 **다이렉트로** 추가했어요";
					if(lan.equals("eng")) {
		    			if(notPlayList.size() == 1) language = "Added directly **" + notPlayList.size() + " item** and **" + isPlayList.size() + " playlist** that **" + o + "** did saved.";
		    			else language = "Added directly **" + notPlayList.size() + " items** and **" + isPlayList.size() + " playlist** that **" + o + "** did saved.";
		    		}
				}
				
				String plusMent = "";
				if(response == null) {
					plusMent = "로딩이 완료되어 ";
					if(lan.equals("eng"))
						plusMent = "";
					waitQueueAgainPersonal = fn.finishLoading(tc, waitQueueAgainPersonal, plusMent + language);
				}
				else
					response.editMessage(language).queue();
				
				log(tc, event, "BOT: " + plusMent + language);
	
				menu = 9;

			}
		}
		catch(Exception e) {
			String str = ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e);
			if(response == null) {
				waitQueueAgainPersonal = fn.finishLoading(tc, waitQueueAgainPersonal, str);
			}
			else
				response.editMessage(str).queue();
			log(tc, event, str);
		}
	}
	
	public void queueAgainPersonalNormal(TextChannel tc, Message msg, Message response, Object o, String id, String lan) {
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

			if(isIn == 0) {
				isIn = 1;
				enteredTime = System.currentTimeMillis();
				
				if(response != null)
					MusicController.connectToVoiceChannel(tc, msg, msg.getMember(), tc.getGuild().getAudioManager(), event, lan);
			}
			
            FileReader filereader = new FileReader(file);
            BufferedReader bufReader  =  new BufferedReader(filereader);
			
            List<String> isPlayList = new ArrayList<>();
            List<String> notPlayList = new ArrayList<>();
            List<String> trackUrls = new ArrayList<>();
            
            String line = "";
            while((line = bufReader.readLine()) != null){
            	if(line.contains("list=")) isPlayList.add(line);
            	else notPlayList.add(line);
            	
            	trackUrls.add(line);
            }
            //.readLine()은 끝에 개행문자를 읽지 않는다.            
            bufReader.close();
            
            playAgainStringList.addAll(trackUrls);
            recentAddURLCount = trackUrls.size();
            
            String language = "**" + o + "**님이 저장했었던 " + "**" + notPlayList.size() + "개** 항목을 추가했어요";
    		if(lan.equals("eng")) {
    			if(notPlayList.size() == 1) language = "Added **" + notPlayList.size() + " item** that **" + o + "** did saved.";
    			else language = "Added **" + notPlayList.size() + " items** that **" + o + "** did saved.";
    		}
    		
			if(!isPlayList.isEmpty() && notPlayList.isEmpty()) {
				language = "**" + o + "**님이 저장했었던 **재생목록 " + isPlayList.size() + "개**를 추가했어요";
				if(lan.equals("eng")) {
					language = "Added **" + notPlayList.size() + " playlist** that **" + o + "** did saved.";
	    		}
			}
			else if(!isPlayList.isEmpty()) {
				language = "**" + o + "**님이 저장했었던 " + "**" + notPlayList.size() + "개** 항목과 **재생목록 " + isPlayList.size() + "개**를 추가했어요";
				if(lan.equals("eng")) {
	    			if(notPlayList.size() == 1) language = "Added **" + notPlayList.size() + " item** and **" + isPlayList.size() + " playlist** that **" + o + "** did saved.";
	    			else language = "Added **" + notPlayList.size() + " items** and **" + isPlayList.size() + " playlist** that **" + o + "** did saved.";
	    		}
			}
			
    		String plusMent = "";
    		if(response == null) {
    			plusMent = "로딩이 완료되어 ";
    			if(lan.equals("eng"))
					plusMent = "";
 				waitQueueAgainPersonal = fn.finishLoading(tc, waitQueueAgainPersonal, plusMent + language);
 			}
 			else
 				response.editMessage(language).queue();

    		log(tc, event, "BOT: " + plusMent + language);
    		

			if(waitingPlay.equals("")) {
				tc.sendMessage(":satellite: `추가 대기 중...`").queue(waitMsg -> {
					waitingPlay = waitMsg.getId();

		    		loadCount = trackUrls.size();
		    		terminate = 0;
		    		
			    	int end = 0;
			    	for(int i=0; i<trackUrls.size(); i++) {
			    		String track = trackUrls.get(i).toString();
			    		if(i == trackUrls.size() - 1) end = 1;
			    		MusicController.loadAndPlaySub(tc, msg, track, event, response, 1, end, lan, id);
			    	}
			    	
		    		menu = 8;	
		            
				});
			}
    		
        }
		catch(Exception e){
            System.out.println(e);
            String str = ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e);
            if(response == null) {
				waitQueueAgainPersonal = fn.finishLoading(tc, waitQueueAgainPersonal, str);
			}
			else
				response.editMessage(str).queue();
            
			log(tc, event, str);
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
		
		repeat(tc, null, event);
		
		int volumeBefore = this.volume;
		
        this.volume = volume;
        player.setVolume(volume);
        
        if(volumeBefore == volume) {
        	userAlreadyVolumeMessageId = fn.autoRemoveMessage(tc, msg, userAlreadyVolumeMessageId, botAlreadyVolumeMessageId);
    		
        	String language = "이미 볼륨이 ``" + volume + "``이에요";
			if(lan.equals("eng"))
				language = "Volume is already set ``" + volume + "``.";
			
        	 String reply = "BOT: " + language;
             tc.sendMessage(language).queue(response -> {
            	 botAlreadyVolumeMessageId = response.getId();
             });
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
        
        String nowVol = "현재볼륨";
		if(setLanguageStop.equals("eng"))
			nowVol = "Now volume";
		
		String volumeStr = ":sound: " + nowVol + ": " + volume;
		if(volume < 12)
			volumeStr = ":speaker: " + nowVol + ": " + volume;
		else if(volume > 24)
			volumeStr = ":loud_sound: " + nowVol + ": " + volume;
			
		String vol = volumeStr;
		Runnable r = () -> {
			try {
		    	tc.editMessageById(setVolumeMessageId, vol).complete();
		    }
		    catch(Exception e) {
		    	try {
			    	tc.editMessageById(botVolumeMessageId, vol).complete();
			    }
			    catch(Exception f) {}
		    }
		};
		Thread t = new Thread(r);
		t.start();
		
		userAlreadyVolumeMessageId = "";
		botAlreadyVolumeMessageId = "";
		
        if(val == 1) {
	        menu = 3;
	        menuStr = String.valueOf(volumeBefore);
        }
        else {
        	menu = 0;
        }
    }
	
	
	public void nowVolume(TextChannel tc, MessageReceivedEvent event, Message msg, String lan) {
		repeat(tc, msg, event);

		userVolumeMessageId = fn.autoRemoveMessage(tc, msg, userVolumeMessageId, botVolumeMessageId);
		
		String nowVol = "현재볼륨";
		if(lan.equals("eng"))
			nowVol = "Now volume";
		
		String volumeStr = ":sound: " + nowVol + ": " + volume;
		if(player.getVolume() < 12)
			volumeStr = ":speaker: " + nowVol + ": " + volume;
		else if(player.getVolume() > 24)
			volumeStr = ":loud_sound: " + nowVol + ": " + volume;
			
		if(!msg.getMember().getId().equals(BotMusicListener.admin))
			log(tc, event, "BOT: `(" + tc.getGuild().getName() + ")` 볼륨: " + volume);
		
		if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_ADD_REACTION)) {
			fn.removeMessage(tc, setVolumeMessageId);
			
			tc.sendMessage(volumeStr).queue(response -> {
				botVolumeMessageId = response.getId();
				
				Runnable reaction = () -> {
					try {
						response.addReaction("U+1f53d").complete();
						response.addReaction("U+1f53c").complete();
					}
					catch(Exception e) {
						fn.removeMessage(tc, userVolumeMessageId, response.getId());
					}
				};
				Thread t = new Thread(reaction);
				t.start();
				
			});
        }
		else {
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
		    
		    updateVol.append(tc.getGuild().getId() + " " + String.valueOf(volume));
			
		    FileWriter fw = new FileWriter(file);
		      
			fw.write(updateVol.toString());
			fw.close();
		}
		catch(Exception e) {
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
			log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
		}
		
	}
	
	public void shuffle(TextChannel tc, Message msg, MessageReceivedEvent event, String lan, boolean isWait) {

		if(isWait == false) {
			userShuffleId = fn.autoRemoveMessage(tc, msg, userShuffleId, botShuffleId);
			
			if(loadCount > 0) {
				String language = ":hourglass_flowing_sand: 로딩이 다 되면 실행돼요";
				if(lan.equals("eng"))
					language = ":hourglass_flowing_sand: Auto execute when finish loading.";
					
				tc.sendMessage(language).queue(response -> {
					botShuffleId = response.getId();
					waitShuffle = response.getId();
					fn.waitShuffleTc = tc;
					fn.waitShuffleMessage = msg;
					fn.waitShuffleEvent = event;
					fn.waitShuffleLan = lan;
				});
				System.out.println("BOT: " + language);
					
				return;
			}
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
	    
	    String languageA = "";
	    if(removed == 0) {
		    if(playAgainStringList.toString().contains("list=")) {
			    if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId()) || Main.loadAllowGuild.contains(tc.getGuild().getId())) {
				    languageA = "\n**재생목록을 추가한 상태에서 항목을 섞어** 저장할 때 섞기전 상태로 저장해요";
				    if(lan.equals("eng"))
				    	languageA = "\nThey are saved in the state before shuffling because **contains playlist**.";
			    }
		    }
	    }
	    
	    String language = "";
	    
	    if(current == null) {
	    	language = "**" + (int)(queue.size()) + "개** 항목을 섞었어요" + languageA;
		    if(lan.equals("eng"))
		    	language = "Shuffled **" + (int)(queue.size()) + " items**." + languageA;
				
	    }
	    else {
	    	language = "나머지 **" + (int)(queue.size() - 1) + "개** 항목을 섞었어요" + languageA;
		    if(lan.equals("eng"))
		    	language = "Shuffled other **" + (int)(queue.size() - 1) + " items**." + languageA;
				
	    }
	    
	    String plusMent = "";
	    if(waitShuffle.equals("")) {
		    tc.sendMessage(language).queue(response -> {
		        botShuffleId = response.getId();
		    });
	    }
	    else {
	    	plusMent = "로딩이 완료되어 ";
	    	if(lan.equals("eng"))
				plusMent = "";
	    	waitShuffle = fn.finishLoading(tc, waitShuffle, plusMent + language);
	    }
	    
	    String reply = "BOT: " + plusMent + language;
	    System.out.println(reply);
	    log(tc, event, reply);
	    
	    if(!playAgainStringList.contains("list=")) {
	    	playAgainStringList.clear();
	    	for(int i = 0; i<queue.size(); i++) {
	    		playAgainStringList.add(queue.get(i).getInfo().uri);
	    	}
	    }
		
	    menu = 0;

	}
	
	public void removeMany(List<Integer> removeList, TextChannel tc, Message msg, MessageReceivedEvent event, String lan, boolean isWait) {
	
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
		
		if(isWait == false) {
			
			if(loadCount > 0) {
				if(!waitRemove.equals("")) {
					waitRemoveManyUser = fn.autoRemoveMessage(tc, msg, waitRemoveUser, waitRemove);
					waitRemove = "";
				}
				else if(!waitRemoveTitle.equals("")) {
					waitRemoveUser = fn.autoRemoveMessage(tc, msg, waitRemoveTitleUser, waitRemoveTitle);
					waitRemoveTitle = "";
				}
				else
					waitRemoveManyUser = fn.autoRemoveMessage(tc, msg, waitRemoveManyUser, waitRemoveMany);
				
				String language = ":hourglass_flowing_sand: 로딩이 다 되면 실행돼요";
				if(lan.equals("eng"))
					language = ":hourglass_flowing_sand: Auto execute when finish loading.";
				
				fn.waitRemoveManyList.clear();
				fn.waitRemoveManyList.addAll(removeList);
				
				tc.sendMessage(language).queue(response -> {
					waitRemoveMany = response.getId();
					
					fn.waitRemoveTc = tc;
					fn.waitRemoveMessage = msg;
					fn.waitRemoveEvent = event;
					fn.waitRemoveLan = lan;
				});
				System.out.println("BOT: " + language);
				
				return;
			}
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
		
		if(removeList.size() == 1) {
			if(removeList.get(0) > queue.size()) {
				removeList.remove(0);
				removeList.add(queue.size());
			}
		}
		
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
			String language = "삭제할 항목이 없어요";
			if(lan.equals("eng")) {
				language = "Nothing to removing.";
			}
			
			if(!waitRemoveMany.equals("")) {
				waitRemoveMany = fn.finishLoading(tc, waitRemoveMany, language);
			}
			else
				tc.sendMessage(language).queue();
			
			String reply = "BOT: " + language;
			System.out.println(reply);
			log(tc, event, reply);
			
			return;
		}
		
		else if(removeMany.size() == 1) {
			String title = queue.get(removeList.get(0) - 1).getInfo().uri;
			try {
				title = MusicController.realTitle(queue.get(removeList.get(0) - 1).getInfo().title);
			}
			catch(Exception e) {}
			
			String language = "``" + (int)(removeList.get(0)) + ".`` **" + title + "**를 목록에서 삭제했어요";
			if(lan.equals("eng"))
				language = "Removed ``" + (int)(removeList.get(0)) + ".`` **" + title + "** in queue.";
		  
		 
			String plusMent = "";
	    	if(!waitRemoveMany.equals("")) {
	    		plusMent = "로딩이 완료되어 ";
	    		if(lan.equals("eng"))
					plusMent = "";
	    		waitRemoveMany = fn.finishLoading(tc, waitRemoveMany, plusMent + language);
	    	}
	    	else
	    		tc.sendMessage(language).queue();
	    	
	    	String reply = "BOT: " + plusMent + language;
		    System.out.println(reply);
		    log(tc, event, reply);
		    
		   
		    if(playAgainStringList.toString().contains(queue.get(removeList.get(0) - 1).getIdentifier())) {
		    	List<String> temp = new ArrayList<>();
		    	temp.addAll(playAgainStringList);
		    	playAgainStringListBefore.clear();
		    	playAgainStringListBefore.addAll(temp);
		    		
		    	end:
		    	for(int k = 0; k<playAgainStringList.size(); k++) {
		    		if(playAgainStringList.get(k).toString().contains(queue.get(removeList.get(0) - 1).getIdentifier())) {
		    			playAgainStringList.remove(k);
		    			break end;
		    		}
		    	}
		    		
		    }
		    else {
		    	if(playAgainStringList.toString().contains("list=") && removed == 0) {
			    	if(removed == 0) {
				    	wasDeletedPlaylistElements = true;
				    }
				    else {
				    	wasDeletedPlaylistElements = false;
				    	playAgainStringList.clear();
				    	audioPlaylist.clear();
				    }
			    		
					removed = 1;
					    
					if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId()) || Main.loadAllowGuild.contains(tc.getGuild().getId())) {
						String languageE = ":information_source: 재생목록을 추가한 상태에서 항목을 지워 **다수 저장이 불가해요**";
						if(lan.equals("eng"))
						    languageE = ":information_source: **Cannot save many items** because added playlist and deleted.";
						tc.sendMessage(languageE).queue();
					}
		    	}
		    	else {
		    		wasDeletedPlaylistElements = false;
		    	}
		    }
		    
		  
		}
		else {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.decode(BotMusicListener.colorDefault));
			
			
			String languageRemove = "삭제된 항목";
			if(lan.equals("eng")) {
				languageRemove = "Removed items";
			}
			
			if(!waitRemoveMany.equals("")) {
				languageRemove = "삭제된 항목 (로딩 완료)";
				if(lan.equals("eng")) {
					languageRemove = "Removed items (finished loading)";
				}
			}
			
			eb.setTitle(languageRemove);
			
			String languageF = "총 " + removeMany.size() + "개";
			if(lan.equals("eng")) {
				if(removeMany.size() == 1) languageF = removeMany.size() + " item";
				else languageF = removeMany.size() + " items";
			}
			
			eb.setFooter(languageF);
			
			StringBuilder sb = new StringBuilder();
			
			boolean availableAll = true;
			for(int rl = 0; rl<removeMany.size(); rl++) {
				if(!playAgainStringList.toString().contains(removeMany.get(rl).getIdentifier())) availableAll = false;
				
				String title = removeMany.get(rl).getInfo().uri;
				try {
					title = MusicController.realTitle(removeMany.get(rl).getInfo().title);
				}
				catch(Exception e) {}
				
				sb.append("`" + (int)(removeList.get(rl)) + ".` **" + title + "**\n");
			}
		
			eb.setDescription(sb);

		    if(playAgainStringList.toString().contains("list=") && availableAll == false) {
		    	if(removed == 0) {
		    		wasDeletedPlaylistElements = true;
		    	}
		    	else {
		    		wasDeletedPlaylistElements = false;
		    		playAgainStringList.clear();
		    	}
		    	
			    removed = 1;
			    if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId()) || Main.loadAllowGuild.contains(tc.getGuild().getId())) {
				    String language = "재생목록을 추가한 상태에서 항목을 지워 다수 저장이 불가해요";
				    if(lan.equals("eng"))
				    	language = "Cannot save many items because added playlist and deleted.";
				    eb.setFooter(language);
			    }
		    }
		    else {
		    	List<String> temp = new ArrayList<>();
		    	playAgainStringListBefore.clear();
		    	temp.addAll(playAgainStringList);
		    	playAgainStringListBefore.addAll(temp);
		    	
		    	for(int rl = 0; rl<removeMany.size(); rl++) {
			    	if(playAgainStringList.toString().contains(removeMany.get(rl).getIdentifier())) {
				    	end:
				    	for(int k = 0; k<playAgainStringList.size(); k++) {
				    		if(playAgainStringList.get(k).toString().contains(removeMany.get(rl).getIdentifier())) {
				    			playAgainStringList.remove(k);
				    			break end;
				    		}
				    	}
			    		
			    	}
		    	}
		    	
		    	wasDeletedPlaylistElements = false;
		    }
		    
			MessageBuilder mb = new MessageBuilder();
			
			String mbTitle = "**" + removeMany.size() + "개** 항목을 제거했어요";
			if(lan.equals("eng")) {
				if(removeMany.size() == 1)
					mbTitle = "Removed **" + removeMany.size() + " item**.";
				else
					mbTitle = "Removed **" + removeMany.size() + " items**.";
			}
			
			mb.setContent(mbTitle);
			mb.setEmbed(eb.build());
			
			String plusMent = "";
			if(!waitRemoveMany.equals("")) {
				plusMent = "로딩이 완료되어 ";
				if(lan.equals("eng"))
					plusMent = "";
	    		waitRemoveMany = fn.finishLoading(tc, waitRemoveMany, mb.build());
	    	}
	    	else
	    		tc.sendMessage(mb.build()).queue();
			
			String reply = "BOT: " + plusMent + removeMany.size() + "개 제거했어요";
			System.out.println(reply);
			log(tc, event, reply);
			
		}
	  
	    menu = 2;
	    
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
	    	emptyEvent(tc, msg, event, lan);
	    	
	    	return;
	    }
	}
	
	public void remove(String itemstr, TextChannel tc, Message msg, MessageReceivedEvent event, int alert, String lan, boolean isWait) {
		if(emptyRun == 1) {
			emptyTimer.cancel();
  		  	emptyTimer = null;
  		  	emptyRun = 0;
  	  	}
		
		if(isWait == false) {
			
			if(loadCount > 0) {
				if(!waitRemoveMany.equals("")) {
					waitRemoveUser = fn.autoRemoveMessage(tc, msg, waitRemoveManyUser, waitRemoveMany);
					waitRemoveMany = "";
				}
				else if(!waitRemoveTitle.equals("")) {
					waitRemoveUser = fn.autoRemoveMessage(tc, msg, waitRemoveTitleUser, waitRemoveTitle);
					waitRemoveTitle = "";
				}
				else
					waitRemoveUser = fn.autoRemoveMessage(tc, msg, waitRemoveUser, waitRemove);
				
				String language = ":hourglass_flowing_sand: 로딩이 다 되면 실행돼요";
				if(lan.equals("eng"))
					language = ":hourglass_flowing_sand: Auto execute when finish loading.";
				
				String itemst = itemstr;
				tc.sendMessage(language).queue(response -> {
					waitRemove = response.getId();
					fn.waitRemoveItemstr = itemst;
					fn.waitRemoveTc = tc;
					fn.waitRemoveMessage = msg;
					fn.waitRemoveEvent = event;
					fn.waitRemoveLan = lan;
				});
				System.out.println("BOT: " + language);
				
				return;
			}
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
	    
		int size = queue.size();
	    LinkedList<AudioTrack> end = new LinkedList<>(queue);
	    
	    
		item = Math.abs(item);
		if(item > size)
		    item = size; 
		if(item < 1)
		    item = 1;
		      
		item --;
		     
		if(alert == 1) {
			String title = end.get(item).getInfo().uri;
			try {
				title = MusicController.realTitle(end.get(item).getInfo().title);
			}
			catch(Exception e) {}
			
			String language = "``" + (int)(item + 1) + ".`` **" + title + "**를 목록에서 삭제했어요";
		    if(lan.equals("eng"))
				language = "Removed ``" + (int)(item + 1) + ".`` **" + title + "** in queue.";
				  
			if(item <= current)
				current = current - 1;
				  
		    String plusMent = "";
		    if(!waitRemove.equals("")) {
		    	plusMent = "로딩이 완료되어 ";
		    	if(lan.equals("eng"))
					plusMent = "";
		    	waitRemove = fn.finishLoading(tc, waitRemove, plusMent + language);
		    }
		    else
		    	tc.sendMessage(language).queue();
		    	
		    String reply = "BOT: " + plusMent + language;
			System.out.println(reply);
			log(tc, event, reply);
			    
			if(playAgainStringList.toString().contains(end.get(item).getIdentifier())) {
			    ArrayList<String> temp = new ArrayList<>();
			    temp.addAll(playAgainStringList);

			    playAgainStringListBefore.clear();
			    playAgainStringListBefore.addAll(temp);
			    	
			    end:
			    for(int k = 0; k<playAgainStringList.size(); k++) {
			    	if(playAgainStringList.get(k).toString().contains(end.get(item).getIdentifier())) {
			    		playAgainStringList.remove(k);
			    		break end;
			    	}
			    }
			    	
		    }
		    else {
		    	if(playAgainStringList.toString().contains("list=") && removed == 0 && alert != 0) {
			    	
			    	if(removed == 0) {
				    	wasDeletedPlaylistElements = true;
				    }
				    else {
				    	wasDeletedPlaylistElements = false;
				    	playAgainStringList.clear();
				    }
			    		
					removed = 1;
					   
					if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId()) || Main.loadAllowGuild.contains(tc.getGuild().getId())) {
						String languageE = ":inforamtion_source: 재생목록을 추가한 상태에서 항목을 지워 **다수 저장이 불가해요**";
						if(lan.equals("eng"))
							languageE = ":inforamtion_source: **Cannot save many items** because added playlist and deleted.";
						tc.sendMessage(languageE).queue();
					}
			    }
		    	else wasDeletedPlaylistElements = false;
			}
			    
		}
		      
		removeMany.add(queue.get(item));
		removeNum.add(item + 1);
		      
		end.remove(item);


	    queue.clear();
	    queue.addAll(end);
	      
	    end.clear();

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
	    
	    if(alert == 1)
		    menu = 2;
	
	}
	
	public void removeTitle(List<String> title, TextChannel tc, Message msg, MessageReceivedEvent event, String lan, boolean isWait) {
		
		if(emptyRun == 1) {
			emptyTimer.cancel();
  		  	emptyTimer = null;
  		  	emptyRun = 0;
  	  	}
		
		
		if(isWait == false) {
			
			if(loadCount > 0) {
				if(!waitRemove.equals("")) {
					waitRemoveTitleUser = fn.autoRemoveMessage(tc, msg, waitRemoveUser, waitRemove);
					waitRemove = "";
				}
				else if(!waitRemoveMany.equals("")) {
					waitRemoveTitleUser = fn.autoRemoveMessage(tc, msg, waitRemoveManyUser, waitRemoveMany);
					waitRemoveMany = "";
				}
				else
					waitRemoveTitleUser = fn.autoRemoveMessage(tc, msg, waitRemoveTitleUser, waitRemoveTitle);
				
				String language = ":hourglass_flowing_sand: 로딩이 다 되면 실행돼요";
				if(lan.equals("eng"))
					language = ":hourglass_flowing_sand: Auto execute when finish loading.";
				
				tc.sendMessage(language).queue(response -> {
					waitRemoveTitle = response.getId();
					fn.waitRemoveTitleList.clear();
					fn.waitRemoveTitleList.addAll(title);
					fn.waitRemoveTc = tc;
					fn.waitRemoveMessage = msg;
					fn.waitRemoveEvent = event;
					fn.waitRemoveLan = lan;
				});
				System.out.println("BOT: " + language);
				
				return;
			}
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
		
		removeNum.clear();
		removeMany.clear();
		

		int prev = 0;
		for(int i = 0; i<queue.size(); i++) {
			
			int match = 0;
			for(int tit = 0; tit<title.size(); tit++) {
				String value = title.get(tit).replaceAll(" ", "").toLowerCase();
				
				if(queue.get(i).getInfo().title.replaceAll(" ", "").toLowerCase().contains(value))
					match = match + 1;	
			}	
			
			if(match == title.size()) {
				removeMany.add(queue.get(i));
				removeNum.add(i+1);
			}
		}
		
		for(int i = 0; i<removeNum.size(); i++) {
			if(removeNum.get(i) - 1 <= current)
				prev = prev + 1;
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
			String language = "삭제할 항목이 없어요";
			if(lan.equals("eng")) {
				language = "Nothing to removing.";
			}
			
			if(!waitRemoveTitle.equals("")) {
				waitRemoveTitle = fn.finishLoading(tc, waitRemoveTitle, language);
			}
			else
				tc.sendMessage(language).queue();
			
			String reply = "BOT: " + language;
			System.out.println(reply);
			log(tc, event, reply);
			
			return;
		}
		
		else if(removeMany.size() == 1) {
			String thisTitle = queue.get(removeNum.get(0) - 1).getInfo().uri;
			try {
				thisTitle = MusicController.realTitle(queue.get(removeNum.get(0) - 1).getInfo().title);
			}
			catch(Exception e) {}
			
			String language = "``" + (int)(removeNum.get(0)) + ".`` **" + thisTitle + "**를 목록에서 삭제했어요";
			if(lan.equals("eng"))
				language = "Removed ``" + (int)(removeNum.get(0)) + ".`` **" + thisTitle + "** in queue.";
		  
		 
			String plusMent = "";
	    	if(!waitRemoveTitle.equals("")) {
	    		plusMent = "로딩이 완료되어 ";
	    		if(lan.equals("eng"))
					plusMent = "";
	    		waitRemoveTitle = fn.finishLoading(tc, waitRemoveTitle, plusMent + language);
	    	}
	    	else
	    		tc.sendMessage(language).queue();
	    	
	    	String reply = "BOT: " + plusMent + language;
		    System.out.println(reply);
		    log(tc, event, reply);
		    
		    if(playAgainStringList.toString().contains(queue.get(removeNum.get(0) - 1).getIdentifier())) {
	    		ArrayList<String> temp = new ArrayList<>();
		    	temp.addAll(playAgainStringList);

		    	playAgainStringListBefore.clear();
		    	playAgainStringListBefore.addAll(temp);
		    	
		    	end:
		    	for(int k = 0; k<playAgainStringList.size(); k++) {
		    		if(playAgainStringList.get(k).toString().contains(queue.get(removeNum.get(0) - 1).getIdentifier())) {
		    			playAgainStringList.remove(k);
		    			break end;
		    		}
		    	}
		    	
	    	}
	    	else {
	    		if(playAgainStringList.toString().contains("list=") && removed == 0) {
		    		if(removed == 0) {
			    		wasDeletedPlaylistElements = true;
			    	}
			    	else {
			    		wasDeletedPlaylistElements = false;
			    		playAgainStringList.clear();
			    	}
		    		
				    removed = 1;
				    
				    if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId()) || Main.loadAllowGuild.contains(tc.getGuild().getId())) {
					    String languageE = ":inforamtion_source: 재생목록을 추가한 상태에서 항목을 지워 **다수 저장이 불가해요**";
					    if(lan.equals("eng"))
					    	languageE = ":inforamtion_source: **Cannot save many items** because added playlist and deleted.";
					    tc.sendMessage(languageE).queue();
				    }
		    	}
	    		else wasDeletedPlaylistElements = false;
		    }
		   
	
		}
		else {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.decode(BotMusicListener.colorDefault));
			
			StringBuilder titlesSb = new StringBuilder();
			titlesSb.append("'");
			for(int i = 0; i<title.size(); i++) {
				if(i == title.size() - 1)
					titlesSb.append(title.get(i) + "'");
				else 
					titlesSb.append(title.get(i) + " ");
			}
			
			String languageRemove = titlesSb + "로 제거된 목록";
			if(lan.equals("eng")) {
				languageRemove = "Removed list by String";
			}
			
			if(!waitRemoveMany.equals("")) {
				languageRemove = titlesSb + "로 제거된 목록 (로딩 완료)";
				if(lan.equals("eng")) {
					languageRemove = "Removed list by String (finished loading)";
				}
			}
			
			eb.setTitle(languageRemove);
			
			String languageF = "총 " + removeMany.size() + "개";
			if(lan.equals("eng")) {
				if(removeMany.size() == 1) languageF = removeMany.size() + " item";
				else languageF = removeMany.size() + " items";
			}
			
			eb.setFooter(languageF);
			
			StringBuilder sb = new StringBuilder();
			
			boolean availableAll = true;
			
			for(int rl = 0; rl<removeMany.size(); rl++) {
				if(!playAgainStringList.toString().contains(removeMany.get(rl).getIdentifier())) availableAll = false;
				
				String thisTitle = removeMany.get(rl).getInfo().uri;
				try {
					thisTitle = MusicController.realTitle(removeMany.get(rl).getInfo().title);
				}
				catch(Exception e) {}
				
				sb.append("`" + (int)(removeNum.get(rl)) + ".` **" + thisTitle + "**\n");
			}
		
			eb.setDescription(sb);
			
			if(playAgainStringList.toString().contains("list=") && availableAll == false) {
			    removed = 1;
			    playAgainStringList.clear();
			    if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId()) || Main.loadAllowGuild.contains(tc.getGuild().getId())) {
				    String languageE = "재생목록을 추가한 상태에서 항목을 지워 다수 저장이 불가해요";
				    if(lan.equals("eng"))
				    	languageE = "Cannot save many items because added playlist and deleted.";
				    eb.setFooter(languageE);
			    }
		    }
			else {
				List<String> temp = new ArrayList<>();
		    	playAgainStringListBefore.clear();
		    	temp.addAll(playAgainStringList);
		    	playAgainStringListBefore.addAll(temp);
		    	
		    	for(int rl = 0; rl<removeMany.size(); rl++) {
			    	if(playAgainStringList.toString().contains(removeMany.get(rl).getIdentifier())) {
				    	end:
				    	for(int k = 0; k<playAgainStringList.size(); k++) {
				    		if(playAgainStringList.get(k).toString().contains(removeMany.get(rl).getIdentifier())) {
				    			playAgainStringList.remove(k);
				    			break end;
				    		}
				    	}
			    	}
		    	}
			}
		
			MessageBuilder mb = new MessageBuilder();

			String mbTitle = "**" + removeMany.size() + "개** 항목을 제거했어요";
			if(lan.equals("eng")) {
				if(removeMany.size() == 1)
					mbTitle = "Removed **" + removeMany.size() + " item**.";
				else
					mbTitle = "Removed **" + removeMany.size() + " items**.";
			}
			
			mb.setContent(mbTitle);
			mb.setEmbed(eb.build());
			
			String plusMent = "";
			if(!waitRemoveTitle.equals("")) {
				plusMent = "로딩이 완료되어 ";
				if(lan.equals("eng"))
					plusMent = "";
	    		waitRemoveTitle = fn.finishLoading(tc, waitRemoveTitle, mb.build());
	    	}
	    	else
	    		tc.sendMessage(mb.build()).queue();
			
			String reply = "BOT: " + plusMent + removeMany.size() + "개를 " + titlesSb + "로 제거했어요";
			System.out.println(reply);
			log(tc, event, reply);
			
		}
	      
		menu = 2;
		
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
  		tc.getGuild().getAudioManager().setSelfDeafened(true);
  		
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
				
				String title = queue.get(current).getInfo().uri;
				try {
					title = MusicController.realTitle(queue.get(current).getInfo().title);
				}
				catch(Exception e) {}
				
				String language = "``" + (int)(current + 1) + ".`` **" + title + "**부터 재생해요";
				if(lan.equals("eng"))
					language = "Play from ``" + (int)(current + 1) + ".`` **" + title + "**.";
				
				String reply = "BOT: " + language;
				tc.sendMessage(language).queue();
				System.out.println(reply);
				log(tc, event, reply);
				
			}
		}

	    if(queue.size() <= current) {current = 0;}
	
	    trackbefore = queue.get(current).getInfo().uri;
	    try {
	    	trackbefore = MusicController.realTitle(queue.get(current).getInfo().title);
	    }
	    catch(Exception e) {}
	    
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
			userCancelId = fn.autoRemoveMessage(tc, msg, userCancelId, botCancelId);
			
			String language = "되돌릴 작업이 없어요";
			if(lan.equals("eng"))
				language = "There is no work to reverse.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue(response -> {
				botCancelId = response.getId();
			});
			System.out.println(reply);
			log(tc, event, reply);
			
			return;
		}
		
		userCancelId = ""; botCancelId = "";
		
		if(menu == 1) { //음악 추가 취소
			
			remove(String.valueOf(queue.size()), tc, msg, event, 1, lan, false);
			
		}
		
		else if(menu == 2) { // 음악 삭제 취소
			List<String> temp = new ArrayList<>();
			temp.addAll(playAgainStringListBefore);
			playAgainStringList.clear();
			playAgainStringList.addAll(playAgainStringListBefore);
			
			if(wasDeletedPlaylistElements) {
				wasDeletedPlaylistElements = false;

				if(audioPlaylist.isEmpty() == false) {
					for(int i = 0; i<audioPlaylist.size(); i++) {
						for(int j = 0; j<removeMany.size(); j++) {
							if(audioPlaylist.get(i).getTracks().contains(removeMany.get(i))) {
								removed = 0;
							}
						}
					}
				}
			}
			
			for(int i = 0; i<removeMany.size(); i++) {
				queue(tc, msg.getMember(), event, removeMany.get(i), 2, removeNum.get(i) - 1, 1);
			}

			
			for(int i = 0; i<removeNum.size(); i++) {
				if(current >= removeNum.get(i) - 1 || current == -1)
					current = current + 1;
			}
			
			Object o = fn.secTo((int)removeMany.get(0).getDuration(), 1);
			if(removeMany.get(0).getInfo().isStream == true) {
				o = "생방송";
				if(lan.equals("eng"))
					o = "LIVE";
			}
			
			String state = "";
				
			if(player.isPaused()) {
				state = " (일시정지됨)";
				if(lan.equals("eng"))
					state = " (Paused)";
			}
			
			if(removeMany.size() == 1) {
				String language = "**" + MusicController.realTitle(removeMany.get(0).getInfo().title) + "** 를 추가했어요" + state + " ``(" + o + ")``";
				if(lan.equals("eng"))
					language = "Restored **" + MusicController.realTitle(removeMany.get(0).getInfo().title) + "**." + state + " ``(" + o + ")``";
				tc.sendMessage(language).queue();
				log(tc, event, "BOT: " + language + "`(총 " + (int)(queue.size()) + "개)`");
			}
			else {
				String language = "삭제했었던 **" + removeMany.size() + "개 항목**을 추가했어요" + state;
				if(lan.equals("eng"))
					language = "Restored **" + removeMany.size() + " items**" + state;
				tc.sendMessage(language).queue();
				log(tc, event, "BOT: " + language + "`(총 " + (int)(queue.size()) + "개)`");
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
			
			playAgainStringList.remove(playAgainStringList.size() - 1);
			
			int length = queue.size();
			
			for(int i=length; i>length - recentAddPlayListCount; i--) {
				remove(String.valueOf(i), tc, msg, event, 0, lan, false);
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
		
		else if(menu == 7) { // 검색 취소
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
			
			terminate = 1;
			
			String language = "**" + recentAddPlayListCount + "개** 항목 추가를 취소했어요";
			if(lan.equals("eng"))
				language = "Canceled to adding **" + recentAddPlayListCount + " items**.";
			
			String reply = "BOT: " + language;
			tc.sendMessage(language).queue();
			System.out.println(reply);
			log(tc, event, reply);
			
			int length = queue.size();
			int value = length - recentAddPlayListCount;
			if(loadCount > 0)
				value = value - loadCount;
			
			loadCount = 0;
			end:
			for(int i=length; i>value; i--) {
				if(!queue.isEmpty())
					remove(String.valueOf(i), tc, msg, event, 0, lan, false);
				else
					break end;
			} 
			
			int endValue = playAgainStringList.size()-recentAddURLCount;
			for(int i = playAgainStringList.size(); i>endValue; i--) {
				playAgainStringList.remove(i-1);
			}
			
			counting = 0;
			countend = 1;
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
					remove(String.valueOf(i), tc, msg, event, 0, lan, false);
				} 
				
				int endValue = playAgainStringList.size()-recentAddURLCount;
				for(int i = playAgainStringList.size(); i>endValue; i--) {
					playAgainStringList.remove(i-1);
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
		
		
		if(tc.getGuild().getAudioManager().isConnected() == false) {
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
			
			EmbedBuilder eb = new EmbedBuilder();
			
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

			if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
				MessageBuilder mb = new MessageBuilder();
				mb.setContent(language);
				mb.setEmbed(playBox(eb, lan, pp).build());
						
				response.editMessage(mb.build()).queue();
	        }
			else {
				StringBuilder ret = new StringBuilder();
				ret.append(language);
				response.editMessage(playBoxOld(ret, lan, pp)).queue();
				
				mode = "old";
			}
			
			System.out.println(reply);
			log(tc, event, reply + " `(총 " + queue.size() + "개)`");
		
			System.out.println(eb);
					
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
		
		if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
			String language = "링크 첨부";
			if(lan.equals("eng"))
				language = "MESSAGE_EMBED_LINKS";
			tc.sendMessage(fn.askPermission(language, lan)).queue(error -> {
				nowBotMessageId = error.getId();
			});
			log(tc, event, "BOT: " + language);
        	return;
        }
		
		if(tc.getGuild().getAudioManager().isConnected() == false) {
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

		setLanguageNow = "kor";
		
		String languageLoad = ":hourglass: 불러오는 중...";
		if(lan.equals("eng")) {
			languageLoad = ":hourglass: Loading...";
			setLanguageNow = lan;
		}
		
		l.setTitle(languageLoad);
		
		tc.sendMessage(l.build()).queue(response -> {
			nowBotMessageId = response.getId();

			EmbedBuilder eb = new EmbedBuilder();
			
			response.editMessage(embedNow(eb, lan).build()).queue(setting -> {
				if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_ADD_REACTION)) {
					Runnable reaction = () -> {
						try {
							setting.addReaction("U+23ea").complete();
							setting.addReaction("U+23e9").complete();
						}
						catch(Exception e) {
							Runnable remove1 = () -> {
								try {
									setting.removeReaction("U+23ea").complete();
								}
								catch(Exception f) {}
							};
							
							Runnable remove2 = () -> {
								try {
									setting.removeReaction("U+23e9").complete();
								}
								catch(Exception f) {}
							};
							
							Thread t1 = new Thread(remove1);
							Thread t2 = new Thread(remove2);
							t1.start();
							t2.start();
							
						}
					};
					Thread t = new Thread(reaction);
					t.start();
		        }
			});

			log(tc, event, "BOT: " + trackbefore + "의 정보 `(총 " + queue.size() + "개)`");

			if(timer2 == null) 
				timer2 = new Timer();
			updateMessage(msg, tc, lan);
			
			runnow = 1;
		});
		
	}
	
	public void addReaction(TextChannel tc, String msgId, ReactionEmote emote, Member member) {
		if(msgId.equals(nowBotMessageId)) {
			if(emote.getAsCodepoints().equals("U+23ea")||emote.getAsCodepoints().equals("U+23e9")) {
				
				VoiceChannel myChannel = member.getVoiceState().getChannel();
				VoiceChannel botChannel = tc.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
				
				if(myChannel != botChannel) return;
				
				if(emote.getAsCodepoints().equals("U+23ea")) {
					trackInfo.setPosition(trackInfo.getPosition() - 12000);
					
					if(!member.getId().equals(BotMusicListener.admin))
						log(tc, event, "BOT: `(" + tc.getGuild().getName() + "/" + member.getUser().getName() + ")` 위치: -12s");
				}
				
				else if(emote.getAsCodepoints().equals("U+23e9")) {
					long position = trackInfo.getPosition() + 12000;
					if(position <= trackInfo.getDuration())
						trackInfo.setPosition(position);
					else 
						trackInfo.setPosition(trackInfo.getDuration());
					
					if(!member.getId().equals(BotMusicListener.admin))
						log(tc, event, "BOT: `(" + tc.getGuild().getName() + "/" + member.getUser().getName() + ")` 위치: +12s");
				}
				
				if(runnow == 1) {
					timer2.cancel(); 
					timer2 = null;
					runnow = 0;
				}
				
				EmbedBuilder ebnow = new EmbedBuilder();
							
			    try {
			    	tc.editMessageById(nowBotMessageId, embedNow(ebnow, setLanguageNow).build()).complete();
			    }
			    catch(Exception e) {}
				
			    if(timer2 == null) 
					timer2 = new Timer();
				updateMessage(msg, tc, setLanguageNow);
				
				runnow = 1;
			}
		}
		
		else if(msgId.equals(setVolumeMessageId) || msgId.equals(botVolumeMessageId)) {
			if(emote.getAsCodepoints().equals("U+1f53c") || emote.getAsCodepoints().equals("U+1f53d")) {
				VoiceChannel myChannel = member.getVoiceState().getChannel();
				VoiceChannel botChannel = tc.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
				
				if(myChannel != botChannel) return;
				
				if(emote.getAsCodepoints().equals("U+1f53c")) {
					if(volume >= 100) return;
					
					volume = volume + 7;
					if(volume >= 100)
						volume = 100;
				}
				else if(emote.getAsCodepoints().equals("U+1f53d")) {
					if(volume <= 1) return;
					
					if(volume == 100)
					 volume = volume - 8;
					else volume = volume - 7;
					
					if(volume <= 1)
						volume = 1;
				}

				volumeBackUp(tc, volume);
				
				String nowVol = "현재볼륨";
				if(setLanguageStop.equals("eng"))
					nowVol = "Now volume";
				
				String volumeStr = ":sound: " + nowVol + ": " + volume;
				if(volume < 12)
					volumeStr = ":speaker: " + nowVol + ": " + volume;
				else if(volume > 24)
					volumeStr = ":loud_sound: " + nowVol + ": " + volume;
					
				player.setVolume(volume);
				
				String str = volumeStr;
				Runnable r = () -> {
					try {
				    	tc.editMessageById(msgId, str).complete();
				    }
				    catch(Exception e) {}
				};
				Thread t = new Thread(r);
				t.start();
				
				if(!member.getId().equals(BotMusicListener.admin))
					log(tc, event, "BOT: `(" + tc.getGuild().getName() + "/" + member.getUser().getName() + ")` 볼륨: " + volume);
			}

		}
		
		else if(wantToPlayHashMessage.containsKey(msgId)) {
			if(emote.getAsCodepoints().equals("U+1f1fd")) {
				try {
					wantToPlayHashMessage.get(msgId).delete().complete();
					wantToPlayHashTimer.get(msgId).cancel();
					wantToPlayHashTimer.remove(msgId);
				}
				catch(Exception e) {}
			}
			else if(emote.getAsCodepoints().equals("U+2705")) {
				wantToPlayHashTimer.get(msgId).cancel();
				wantToPlayHashTimer.remove(msgId);
				
				Runnable remove = () -> {
					try {
						wantToPlayHashMessage.get(msgId).clearReactions().complete();
					}
					catch(Exception e) {}
				};
				
				Thread t1 = new Thread(remove);
				t1.start();
				
				
				MusicController.loadAndPlay(tc, null, wantToPlayHashMessage.get(msgId), wantToPlayHashQuery.get(msgId), null, member, "kor", 0);
            	repeat(tc, msg, event);
            	
            	wantToPlayHashMessage.remove(msgId);
            	wantToPlayHashQuery.remove(msgId);
			}
		}
	}
	
	public void removeReaction(TextChannel tc, String msgId, ReactionEmote emote, Member member) {
		if(msgId.equals(nowBotMessageId)) {
			if(member.getId().equals(BotMusicListener.bot)) {
				fn.removeMessage(tc, nowUserMessageId, nowBotMessageId);
				return;
			}
			
			if(emote.getAsCodepoints().equals("U+23ea")||emote.getAsCodepoints().equals("U+23e9")) {
				VoiceChannel myChannel = member.getVoiceState().getChannel();
				VoiceChannel botChannel = tc.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
				
				if(myChannel != botChannel) return;
				
				if(emote.getAsCodepoints().equals("U+23ea")) {
					trackInfo.setPosition(trackInfo.getPosition() - 12000);
					
					if(!member.getId().equals(BotMusicListener.admin))
						log(tc, event, "BOT: `(" + tc.getGuild().getName() + "/" + member.getUser().getName() + ")` 위치: -12s");
				}
				
				else if(emote.getAsCodepoints().equals("U+23e9")) {
					long position = trackInfo.getPosition() + 12000;
					if(position <= trackInfo.getDuration())
						trackInfo.setPosition(position);
					else
						trackInfo.setPosition(trackInfo.getDuration());
					
					if(!member.getId().equals(BotMusicListener.admin))
						log(tc, event, "BOT: `(" + tc.getGuild().getName() + "/" + member.getUser().getName() + ")` 위치: +12s");
				}
				
				if(runnow == 1) {
					timer2.cancel(); 
					timer2 = null;
					runnow = 0;
				}
				
				EmbedBuilder ebnow = new EmbedBuilder();
							
			    try {
			    	tc.editMessageById(nowBotMessageId, embedNow(ebnow, setLanguageNow).build()).complete();
			    }
			    catch(Exception e) {}
				
			    if(timer2 == null) 
					timer2 = new Timer();
				updateMessage(msg, tc, setLanguageNow);
				
				runnow = 1;
				
			}
		}
		
		else if(msgId.equals(setVolumeMessageId) || msgId.equals(botVolumeMessageId)) {
			if(member.getId().equals(BotMusicListener.bot)) {
				fn.removeMessage(tc, msgId, userVolumeMessageId);
				return;
			}
			
			VoiceChannel myChannel = member.getVoiceState().getChannel();
			VoiceChannel botChannel = tc.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
			
			if(myChannel != botChannel) return;
			
			if(emote.getAsCodepoints().equals("U+1f53c") || emote.getAsCodepoints().equals("U+1f53d")) {
				if(emote.getAsCodepoints().equals("U+1f53c")) {
					if(volume >= 100 || volume <= 1) return;
					
					volume = volume + 7;
					if(volume >= 100)
						volume = 100;
					
				}
				else if(emote.getAsCodepoints().equals("U+1f53d")) {
					if(volume <= 1) return;
					
					if(volume == 100)
						volume = volume - 8;
					else 
						volume = volume - 7;
					
					if(volume <= 1)
						volume = 1;
				}

				volumeBackUp(tc, volume);
				
				String nowVol = "현재볼륨";
				if(setLanguageStop.equals("eng"))
					nowVol = "Now volume";
				
				String volumeStr = ":sound: " + nowVol + ": " + volume;
				if(volume < 12)
					volumeStr = ":speaker: " + nowVol + ": " + volume;
				else if(volume > 24)
					volumeStr = ":loud_sound: " + nowVol + ": " + volume;
				
				player.setVolume(volume);
				
				String str = volumeStr;
				Runnable r = () -> {
					try {
				    	tc.editMessageById(msgId, str).complete();
				    }
				    catch(Exception e) {}
				};
				Thread t = new Thread(r);
				t.start();
				
				if(!member.getId().equals(BotMusicListener.admin))
					log(tc, event, "BOT: `(" + tc.getGuild().getName() + "/" + member.getUser().getName() + ")` 볼륨: " + volume);
			}
		}

		else if(wantToPlayHashMessage.containsKey(msgId)) {
			if(member.getId().equals(BotMusicListener.bot)) {
				fn.removeMessage(tc, msgId);
				wantToPlayHashMessage.remove(msgId);
				wantToPlayHashTimer.remove(msgId);
				wantToPlayHashQuery.remove(msgId);
				return;
			}
			
			if(emote.getAsCodepoints().equals("U+1f1fd")) {
				try {
					wantToPlayHashMessage.get(msgId).delete().complete();
					wantToPlayHashTimer.get(msgId).cancel();
					wantToPlayHashTimer.remove(msgId);
				}
				catch(Exception e) {}
			}
			else if(emote.getAsCodepoints().equals("U+2705")) {
				wantToPlayHashTimer.get(msgId).cancel();
				wantToPlayHashTimer.remove(msgId);
				
				Runnable remove = () -> {
					try {
						wantToPlayHashMessage.get(msgId).clearReactions().complete();
					}
					catch(Exception e) {}
				};
				
				Thread t1 = new Thread(remove);
				t1.start();
				
				MusicController.loadAndPlay(tc, null, wantToPlayHashMessage.get(msgId), wantToPlayHashQuery.get(msgId), null, member, "kor", 0);
            	repeat(tc, msg, event);
            	
            	wantToPlayHashMessage.remove(msgId);
            	wantToPlayHashQuery.remove(msgId);
			}
		}
	}
	
	public void removeAllReaction(TextChannel tc, String msgId) {
		if(msgId.equals(nowBotMessageId)) {
			fn.removeMessage(tc, nowUserMessageId, nowBotMessageId);
		}
		
		else if(msgId.equals(setVolumeMessageId)) {
			fn.removeMessage(tc, setVolumeMessageId);
		}
		
		else if(wantToPlayHashMessage.containsKey(msgId)) {
			fn.removeMessage(tc, msgId);
			wantToPlayHashMessage.remove(msgId);
			wantToPlayHashTimer.remove(msgId);
			wantToPlayHashQuery.remove(msgId);
		}
	}
	
	public void savedlist(TextChannel tc, Message msg, int page, String lan) {
			
		userSavedListId = fn.autoRemoveMessage(tc, msg, userSavedListId, botSavedListId);
		
		List<Object> list = new ArrayList<>();
		File file = new File(BotMusicListener.directoryDefault + "backup/saveQueue" + tc.getGuild().getId() + ".txt");
			
			
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
		ret.append(languagePosition + BotMusicListener.directoryDefault + "backup/saveQueue" + tc.getGuild().getId() + ".txt\n```");
		
		tc.sendMessage(ret).queue(response -> {
			botSavedListId = response.getId();
		});
			
		System.out.println(ret);
		list.clear();
	
	}
	
	public void savePersonalPlaylist(TextChannel tc, Message msg, MessageReceivedEvent event, String id, String lan) {
		
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
		
		if(removed == 1) {
			
			if(queue.size() > 15) {
				userCannotSaveListId = fn.autoRemoveMessage(tc, msg, userCannotSaveListId, botCannotSaveListId);
				
				String language = "15개 항목이 넘어가면 저장할 수 없어요";
				if(lan.equals("eng")) 
					language = "Can't save over 15 items.";
				
				tc.sendMessage(language).queue(response -> {
					botCannotSaveListId = response.getId();
				});
				log(tc, event, "BOT: "+ language);
				
				return;
			}
		}
		else {
			if(playAgainStringList.size() > 15) {
				userCannotSaveListId = fn.autoRemoveMessage(tc, msg, userCannotSaveListId, botCannotSaveListId);
				
				String language = "15개 URL이 넘어가면 저장할 수 없어요";
				if(lan.equals("eng")) 
					language = "Can't save over 15 URLs.";
				
				tc.sendMessage(language).queue(response -> {
					botCannotSaveListId = response.getId();
				});
				log(tc, event, "BOT: "+ language);
				
				return;
			}
		}

		userSaveListId = fn.autoRemoveMessage(tc, msg, userSaveListId, botSaveListId);
		
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
			
			List<String> uris = new ArrayList<>();
			try {
				if(file.exists() == false) {
					file.createNewFile();
				}
					
				FileWriter fw = new FileWriter(file);
					    
				List<AudioTrack> ss = new ArrayList<>();
				ss.addAll(queue);
				playAgainPersonalList.put(id, ss);
				
				if(removed == 1) {
					for(int k = 0; k<queue.size(); k++) {
						fw.write(queue.get(k).getInfo().uri + "\n");
						uris.add(queue.get(k).getInfo().uri);
					}
				}
				else {
					for(int k = 0; k<playAgainStringList.size(); k++) {
						fw.write(playAgainStringList.get(k) + "\n");
						uris.add(playAgainStringList.get(k));
					}
				}
				
				playAgainPersonalIsPlaylistList.put(id, uris);
				
				fw.close();
			}
			catch(Exception e) {
				response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
				botSaveListId = response.getId();
			   	log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
			}
			
			List<String> isPlayList = new ArrayList<>();
			
			List<String> notPlayList = new ArrayList<>();
			
			for(int k = 0; k<uris.size(); k++) {
				if(uris.get(k).contains("list=")) isPlayList.add("list=");
				else notPlayList.add("single");
			}
				
			String language = "**" + notPlayList.size() + "개**의 항목을 **" + ob + "** 파일에 저장했어요";
			if(lan.equals("eng")) {
				if(queue.size() == 1) language = "Saved at **" + ob + "** with **" + notPlayList.size() + " item**.";
				else language = "Saved at **" + ob + "** file with **" + notPlayList.size() + " items**.";
			}
			
			if(!isPlayList.isEmpty() && notPlayList.isEmpty()) {
				language = "**재생목록 " + isPlayList.size() + "개**를 **" + ob + "** 파일에 저장했어요";
				if(lan.equals("eng")) {
					language = "Saved at **" + ob + "** file with **" + isPlayList.size() + "** playlist.";
				}
			}
			
			else if(!isPlayList.isEmpty()) {
				language = "**" + notPlayList.size() + "개** 항목과 재생목록 **" + isPlayList.size() + "개**를 **" + ob + "** 파일에 저장했어요";
				if(lan.equals("eng")) {
					if(notPlayList.size() == 1) language = "Saved at **" + ob + "** with **" + notPlayList.size() + " item** and **" + isPlayList.size() + "** playlist.";
					else language = "Saved at **" + ob + "** file with **" + notPlayList.size() + " items** and **" + isPlayList.size() + "** playlist.";
				}
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
	            		
	            		
	            		if(player.isPaused() == false)
	            			pausedQueueTitle = false;
	            		
	            		if(pausedQueueTitle == false) {
	            			if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
		            			EmbedBuilder eb = new EmbedBuilder();
			            		MessageBuilder mb = new MessageBuilder();
			            		mb.setContent(language);
			            		mb.setEmbed(playBox(eb, lan, queuePage).build());
			            		
			            		if(!tc.getGuild().getJDA().getStatus().toString().toLowerCase().contains("not"))
			            			tc.editMessageById(qBotMessageId, mb.build()).complete();
		            			
		            			mode = "new";
	            	        }
	            			else {
	            				if(mode.equals("new")) {
	            					fn.removeMessage(tc, qUserMessageId, qBotMessageId);
	            					if(runqueue == 1) {
	    	            				timer.cancel(); 
	    	            				timer = null;
	    	            				runqueue = 0;
	    	            			}
	            				}
	            				else {
			            			StringBuilder ret = new StringBuilder();
			        				ret.append(language);
			        				
			        				if(!tc.getGuild().getJDA().getStatus().toString().toLowerCase().contains("not"))
			        					tc.editMessageById(qBotMessageId, playBoxOld(ret, lan, queuePage)).complete();
			        				
	            				}
	            				
	            				mode = "old";
	            			}
	            		}
	            		
	            		if(player.isPaused())
	            			pausedQueueTitle = true;
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
            	if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
    				fn.removeMessage(tc, nowUserMessageId, nowBotMessageId);
    				
    				if(runnow == 1) {
    					timer2.cancel(); 
    					timer2 = null;
    					runnow = 0;
    				}
    				
    	        	return;
    	        }
            	
	            Runnable edit = () -> {
	            	if(player.isPaused() == false)
	            		pausedNowInfo = false;
	            	
	            	if(pausedNowInfo == false) {
		            	EmbedBuilder ebnow = new EmbedBuilder();
		            	
		    			try {
		    				if(!tc.getGuild().getJDA().getStatus().toString().toLowerCase().contains("not"))
		    					tc.editMessageById(nowBotMessageId, embedNow(ebnow, lan).build()).complete();
		    			}
		    			catch(Exception e) {
		    				if(runnow == 1) {
		    					timer2.cancel(); 
		    					timer2 = null;
		    					runnow = 0;
		    				}
		    			}
	            	}
	            	
	            	if(player.isPaused())
	            		pausedNowInfo = true;
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
	 
	            			if(!tc.getGuild().getJDA().getStatus().toString().toLowerCase().contains("not"))
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
		        		
	            		if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
	        				MusicController.stopPlaying(tc, msg, event, 1, save, setTimerLanguage);
	        	        }
	            		else
	            			MusicController.stopPlaying(tc, msg, event, 1, 0, setTimerLanguage);
	
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
            		if(!tc.getGuild().getJDA().getStatus().toString().contains("not")) {
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
		String durationTrack = fn.secTo((int)trackInfo.getPosition(), 1);
		
		eb.setThumbnail("https://img.youtube.com/vi/" + trackInfo.getIdentifier() + "/mqdefault.jpg");
		eb.setColor(Color.decode(BotMusicListener.colorDefault));
	    	
		String title = trackInfo.getInfo().uri;
		try {
			title = MusicController.realTitle(trackInfo.getInfo().title);
		}
		catch(Exception e) {}
		
	    if (player.isPaused()) {
	    	String language = title + " (일시정지됨)";
			if(lan.equals("eng")) 
				language = title + " (Paused)";
				
			eb.setTitle(language, trackInfo.getInfo().uri);	
		}
			
		else {
			eb.setTitle(title, "https://youtu.be/" + trackInfo.getIdentifier() + "?t=" + (int)(trackInfo.getPosition()/1000 - 2));
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
	    	eb.addField(languageTime, durationTrack + "/" + fn.secTo((int)trackInfo.getDuration(), 1), true);

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
		if(duration < 0 || trackInfo.getInfo().isStream) {
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
			
		}

		int show = pageFinal*10;
		if(pageFinal == maxFinal) {
			show = queue.size();
		}
		
		for(int i = pageFinal*10 - 10; i<show; i++) {
			
			String title = queue.get(i).getInfo().uri;
			try {
				title = MusicController.realTitle(queue.get(i).getInfo().title);
			}
			catch(Exception e) {
				/*
				Runnable r = () -> {
					try {
						tc.editMessageById(qBotMessageId, ":no_entry_sign: **" + e.getMessage() + "**" + e.getCause()).complete();
					}
					catch(Exception g) {}
					log(tc, event, "BOT: get title error");
				};
				Thread t = new Thread(r);
				t.start();
				*/
			}
	
			if((player.getPlayingTrack().getInfo().uri == queue.get(i).getInfo().uri) && i == current) {
				if(last == 1)
					ret.append("`" + (int)(i+1) + ".` **[" + title + "](" + queue.get(i).getInfo().uri + ")**\n");
				else 
					ret.append("`" + (int)(i+1) + ".` [" + title + "](" + queue.get(i).getInfo().uri + ")\n");
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
		
		String totalDuraStr = "  |  " + fn.secTo((int)totalDuration, 1);
		if(trackInfo.getInfo().isStream) {
			totalDuraStr = "";
		}
			
		String languagePage = "페이지 " + pageFinal + "/" + maxFinal + totalDuraStr;
		if(lan.equals("eng")) {
		    languagePage = "Page " + pageFinal + "/" + maxFinal + totalDuraStr;
		}
	
		re.setAuthor(author.toString());
		re.setFooter(languagePage);
		re.setDescription(ret);
		
		
		return re;	
		
	}
	
	//nowPlay Old
	public StringBuilder playBoxOld(StringBuilder ret, String lan, int page) {
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
		
		String languageList = "```css\n재생목록 | 총 " + queue.size() + "개\n";
		
		if(lan.equals("eng")) {
			if(queue.size() <= 1) languageList = "```css\nPLAYLIST | " + queue.size() + " item\n";
			else languageList = "```css\nPLAYLIST | " + queue.size() + " items\n";
		}
		
		ret.append(languageList);
	
		if(loadCount > 0) {
			
			String languageItem = "\n로딩이 덜 됐어요 (" + loadCount + "개 남음)";
			if(lan.equals("eng")) {
				languageItem = "\nNot ended loading (" + loadCount + "left)";
			}
					
			ret.append(languageItem);
			System.out.println("BOT: " + languageItem);

		}
				 
		ret.append("\n");
		
		int show = pageFinal*10;
		if(pageFinal == maxFinal) {
			show = queue.size();
		}
		
		for(int i = pageFinal*10 - 10; i<show; i++) {
			String title = queue.get(i).getInfo().uri;
			try {
				title = queue.get(i).getInfo().title;
			}
			catch(Exception e) {}
			
			if(current == i) ret.append(">#" + (int)(i+1) + ". " + title + " <\n");
			else ret.append("  " + (int)(i+1) + ". " + title + "\n");

		}
		
		if(pageFinal != maxFinal) {
			ret.append("  ...\n");
		}
				
		String languagePage = "\n페이지 " + pageFinal + "/" + maxFinal + "```\n";
		if(lan.equals("eng")) {
			languagePage = "\nPage " + pageFinal + "/" + maxFinal + "```\n";
		}
		
		ret.append(languagePage);
		
		return ret;

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
				if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) MusicController.stopPlaying(tc, msg, event, 2, save, setLanguageLast);
				else MusicController.stopPlaying(tc, msg, event, 2, 0, setLanguageLast);
			}
			else nextTrack(tc, msg, event, 0, 0, setLanguageNext);
			
		}
		
	}
	
	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
	   
		if(queue.size() == 1) {
			String language = ":wrench: 플레이어에 문제가 생겨 **음성채널을 나갔어요**";
		    if(setLanguageStop.equals("eng")) language = ":wrench: Track got stuck, **left the voice channel**.";
		   	
		    tc.sendMessage(language).queue();
		    log(tc, event, "BOT: `(" + tc.getGuild().getName() + ", " + thresholdMs + "ms)` " + language);
		   
		    MusicController.stopPlaying(tc, msg, event, 6, 0, setLanguageStop);
		    
			return;
		}
		
	    String language = ":wrench: 문제가 있어 다음 항목 재생을 권장해요";
	    if(setLanguageStop.equals("eng")) language = ":wrench: Track got stuck, recommand to play next item.";
	    tc.sendMessage(language).queue();
	    	
	    log(tc, event, "BOT: `(" + tc.getGuild().getName() + ", " + thresholdMs + "ms)` " + language);
	}
	
	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		if(fn.cause(exception).contains("response: 429") || exception.getMessage().contains("No available machine")) {
			tc.sendMessage(":no_entry_sign: **" + exception.getMessage() + "**" + fn.cause(exception)).queue();
			MusicController.stopPlaying(tc, msg, event, 0, 0, setLanguageStop);
			log(tc, event, "BOT: :no_entry_sign: **" + exception.getMessage() + "**" + fn.cause(exception));
			BotMusicListener.errortc.sendMessage("<@" + BotMusicListener.admin + "> mac주소 변경").queue();
			BotMusicListener.errortc.sendMessage("<@" + BotMusicListener.admin + "> 재생이 안됩니다").queue();
			BotMusicListener.errortc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
			System.exit(0);
			return;
		}
		
		int nu = 0;
		
		end:
		for(int i = 0; i<queue.size(); i++) {
			if(queue.get(i).getIdentifier().equals(track.getIdentifier())) {
				nu = i + 1;
				break end;
			}
		}
		
		try {
			if(queue.size() == 1 && exception.getCause().toString().toLowerCase().contains("null")) return;
			
			String language = "`" + nu + ".` " + track.getInfo().title + "\n:wrench: **" + exception.getMessage() + "**" + fn.cause(exception);
		    tc.sendMessage(language).queue();
		    log(tc, event, "BOT: `(" + tc.getGuild().getName() + ")` " + language);
		    
		    if(exception.getCause().toString().contains("Unable to play") || exception.getMessage().contains("not supported") || exception.getMessage().contains("private") || exception.getMessage().contains("blocked") || exception.getMessage().contains("not available"))
				remove(String.valueOf(nu), tc, msg, event, 0, setLanguageStop, false);
		   	
		}
		catch(Exception e) {
			if(queue.size() == 1) return;
			String title = track.getInfo().uri;
			try {
				title = MusicController.realTitle(track.getInfo().title);
			}
			catch(Exception f) {}
			
			String language = ":wrench: `" + nu + ".` **" + title + "** 로드 중 문제가 생겨 건너뛰었어요";
		    if(setLanguageStop.equals("eng")) language = ":wrench: `" + nu + ".` **" + title + "** load problem, playing next item.";
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
	
	public void voiceStats(AudioPlayer player) {
	    ArrayList<Member> members = new ArrayList<>();
	    members.addAll(tc.getGuild().getAudioManager().getConnectedChannel().getMembers());
	    
	    Object o = tc.getGuild().getAudioManager().getConnectedChannel().getName();
	    
	    
		String title = "**" + tc.getGuild().getName() + "/" + o + "**의 인원 (" + members.size() + "명)\n```css\n";
	    if(player.isPaused()) {
	    	title = "**(일시정지) " + tc.getGuild().getName() + "/" + o + "**의 인원 (" + members.size() + "명)\n```css\n";
	    }
	    
		StringBuilder s = new StringBuilder();
	    StringBuilder t = new StringBuilder();
	    	
	    s.append(title);
	    t.append(title);
	    
	    String value = "";
        
        try {
    		value = BotMusicListener.voiceTcMessage.get(tc.getGuild());
    	}
    	catch(Exception e) {}
        
	    String valu = value;
	    Runnable edit = () -> {
	        try {
	        	BotMusicListener.voiceTc.editMessageById(valu, BotMusicListener.voiceStats(s, members)).complete();
	        }
	        catch(Exception e) {
	        	BotMusicListener.voiceTc.sendMessage(BotMusicListener.voiceStats(t, members)).queue(response -> {
	        		BotMusicListener.voiceTcMessage.put(tc.getGuild(), response.getId());

		   		});
	        }
	        	
	    };
	    Thread t1 = new Thread(edit);
	    t1.start();
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
		
		
		setLanguageLast = "kor";
		if(lan.equals("eng"))
			setLanguageLast = "eng";
		
		if(i == 1) {
			if(last == 1 && lastNum == -1) {
				userAlreadyLastId = fn.autoRemoveMessage(tc, msg, userAlreadyLastId, botAlreadyLastId);
				userLastId = ""; botLastId = "";
				
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
				if(lastNum == -1) {
					userLastId = fn.autoRemoveMessage(tc, msg, userLastId, botLastId);
					userAlreadyLastId = ""; botAlreadyLastId = "";
					
					String language = "이 항목이 끝나면 **음성채널을 나가요**";
					if(lan.equals("eng")) {
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
						userLastId = fn.autoRemoveMessage(tc, msg, userLastId, botLastId);
						userAlreadyLastId = ""; botAlreadyLastId = "";
						
						String language = "**" + lastNum + "번째** 항목이 끝나면 **음성채널을 나가요**";
						
						if(lan.equals("eng")) {
							language = "When no." + lastNum + " item finished, it will **leave the voice channel**.";
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
				userLastId = ""; botLastId = "";
				userAlreadyLastId = ""; botAlreadyLastId = "";
				
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
            
            while((line = bufReader.readLine()) != null) {
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
	
	public void wantToPlay(TextChannel tc, Message msg, MessageReceivedEvent event, String query) {
		if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_ADD_REACTION) || !tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_MANAGE)) return;
		
		Timer wantToPlayTimer = new Timer();
		String willAdd = ":bulb: '" + query + "' 유튜브에서 검색하고 추가할까요?";
		if(query.startsWith("https://"))
			willAdd = ":bulb: '" + query + "' 검색하고 추가할까요?";
		
		tc.sendMessage(willAdd).queue(response -> {
			Runnable r = () -> {
				try {
					response.addReaction("U+1f1fd").complete();
					response.addReaction("U+2705").complete();
				}
				catch(Exception e) {
					response.delete().queue();
				}
			};
			Thread t = new Thread(r);
			t.start();
			
			wantToPlayHashTimer.put(response.getId(), wantToPlayTimer);
			wantToPlayHashMessage.put(response.getId(), response);
			wantToPlayHashQuery.put(response.getId(), query);
			
			wantToPlayTimer(tc, response);
		});
		
		log(tc, event, "BOT: :bulb: '" + query + "' 유튜브에서 검색하고 추가할까요?");
		System.out.println("BOT: :bulb: '" + query + "' 유튜브에서 검색하고 추가할까요?");
		
	}
	
	public void wantToPlayTimer(TextChannel tc, Message response) {
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	try {
            		response.delete().complete();
            	}
            	catch(Exception e) {}
            	
            	wantToPlayHashTimer.remove(response.getId());
            	wantToPlayHashMessage.remove(response.getId());
            	wantToPlayHashQuery.remove(response.getId());
            }
		};

        wantToPlayHashTimer.get(response.getId()).schedule(task, 6000);
	}
	
	public void pausedTime(TextChannel tc, Message msg, String lan, boolean run) {
		if(pausedTimeRun == 1) {
			pausedTimeRun = 0;
			pausedTimeTimer.cancel();
			pausedTimeTimer = null;
		}
		
		if(run) {
			if(pausedTimeTimer == null)
				pausedTimeTimer = new Timer();
			
			pausedTimeTask(tc, msg, lan);
			pausedTimeRun = 1;
		}
	}
	public void pausedTimeTask(TextChannel tc, Message msg, String lan) {
		
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	if(!tc.getGuild().getAudioManager().getConnectionStatus().toString().toLowerCase().contains("not")) {
        			Date date = new Date();
        	    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
        			String str = dayTime.format(date);
        			
        			if(tc.getGuild().getId().equals(BotMusicListener.base) || event.getAuthor().getId().equals(BotMusicListener.admin)) {}
        			else BotMusicListener.logtc.sendMessage(".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n").queue();
        	
        			
        			if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
        				MusicController.stopPlaying(tc, msg, event, 10, save, setLanguageStop);
        	        }
        				
        	        else {
        	        	MusicController.stopPlaying(tc, msg, event, 10, 0, setLanguageStop);
        	        }
        		}
            	
            	pausedTimeRun = 0;
            }
		};

        pausedTimeTimer.schedule(task, 21600000);
	}

	public void clearQueue(TextChannel tc, Message msg, Message response, Guild guild, MessageReceivedEvent event, int i, int save, String lan) {
		
		if(loadCount > 0) {
			terminate = 1;
			save = 0;
		}
		

		if((removed == 1 && (queue.size() > 30 || queue.isEmpty())) || (playAgainStringList.toString().contains("list=") && playAgainStringList.size() > 30) || (!playAgainStringList.toString().contains("list=") && (queue.size() > 30 || queue.isEmpty()))) save = 0;

		if(save == 1) {
			if(removed == 1 || !playAgainStringList.toString().contains("list=")) {
				
				playAgainList.clear();
				playAgainList.addAll(queue);
					
				File file = new File(BotMusicListener.directoryDefault + "backup/saveQueue" + tc.getGuild().getId() + ".txt");
				try {
					  //파일에 문자열을 쓴다.
					  //하지만 이미 파일이 존재하면 모든 내용을 삭제하고 그위에 덮어쓴다
					  //파일이 손산될 우려가 있다.
						
					if(file.exists() == false) {
						file.createNewFile();
					}
						
					FileWriter fw = new FileWriter(file);
					      
					for(int k = 0; k<queue.size(); k++) {
					    fw.write(queue.get(k).getInfo().uri + "\n");
					}
					      
					fw.close();
				} 
				catch (Exception e) {
					  e.printStackTrace();
					     
					  response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
					  log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
				}
			}
			else {
				playAgainList.clear();
				playAgainList.addAll(queue);
				File file = new File(BotMusicListener.directoryDefault + "backup/saveQueue" + tc.getGuild().getId() + ".txt");
				try {
					  //파일에 문자열을 쓴다.
					  //하지만 이미 파일이 존재하면 모든 내용을 삭제하고 그위에 덮어쓴다
					  //파일이 손산될 우려가 있다.
						
					if(file.exists() == false) {
						file.createNewFile();
					}
						
					FileWriter fw = new FileWriter(file);
					
					for(int k = 0; k<playAgainStringList.size(); k++) {
						fw.write(playAgainStringList.get(k) + "\n");
					}
					      
					fw.close();
				} 
				catch (Exception e) {
					  e.printStackTrace();
					     
					  response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e)).queue();
					  log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + fn.cause(e));
				}
			}
			
		}
		
		if(!Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
			playAgainList.clear();
			playAgainStringList.clear();
		}
		
		fn.removeMessage(tc, setVolumeMessageId);
		
		int sa = save;
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
			
			if(pausedTimeRun == 1) {
				pausedTimeRun = 0;
				pausedTimeTimer.cancel();
				pausedTimeTimer = null;
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
	        		if(mode.equals("new")) {
	        			tc.editMessageById(qBotMessageId, language).complete();
	        		}
	        		else {
	        			StringBuilder ret = new StringBuilder();
        				ret.append(language);
        				tc.editMessageById(qBotMessageId, playBoxOld(ret, lan, queuePage)).complete();
	        		}
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
			userCancelId = ""; botCancelId = "";
			userPlayAgainId = ""; botPlayAgainId = "";
			autoPausedId = "";
			
			//waitLoad
			waitQueueAgainPersonal = ""; waitQueueAgainPersonalUser = "";
			waitShuffle = "";
			waitRemoveMany = ""; waitRemoveManyUser = "";
			waitRemove = ""; waitRemoveUser = "";
			
			if(i == 7 && sa == 1) {}
			else
				current = 0;
			
			queuePage = 0;
			waitingToReconnect = 0;
			
			queue.clear();
			
			listSearch.clear();
			removeMany.clear();
			removeNum.clear();
			trash.clear();
			
			playAgainStringListBefore.clear();
			audioPlaylist.clear();
		};
		
		Runnable end2 = () -> {
			String state = "";
			if(sa == 1) {
				if(removed == 1 || !playAgainStringList.toString().contains("list=")) {
					state = " (목록 저장됨)";
					if(lan.equals("eng"))
						 state = " (Saved)";
				}
				else {
					state = " (다수 저장됨)";
					if(lan.equals("eng"))
						state = " (Multiple saved)";
				}
				
			}
			
			if(i == 0) {
				String language = ":clipboard: 음성채널을 나갔어요" + state;
    			if(lan.equals("eng"))
    				language = ":clipboard: Left the voice channel." + state;
    			
    			String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
				response.editMessage(language).queue();
				System.out.println(reply);
				
				if(tc.getGuild().getId().equals(BotMusicListener.base)) {}
				else log(tc, event, reply);
			}
			
			else if(i == 1) {
				String language = ":alarm_clock: 타이머가 종료되어 음성채널을 나갔어요" + state;
    			if(lan.equals("eng"))
    				language = ":alarm_clock: Timer is over so left the voice channel." + state;
    			
    			String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
    			response.editMessage(language).queue();
				System.out.println(reply);

				if(tc.getGuild().getId().equals(BotMusicListener.base)) {}
				else log(tc, event, reply);
			}
				
			else if(i == 2) {
				String language = ":bell: 항목이 끝나 음성채널을 나갔어요" + state;
    			if(lan.equals("eng"))
    				language = ":bell: Item is over so left the voice channel." + state;
    			
				String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
				response.editMessage(language).queue();
				System.out.println(reply);

				if(tc.getGuild().getId().equals(BotMusicListener.base)) {}
				else log(tc, event, reply);
					
			}
				
			else if(i == 3) {
				String language = ":loudspeaker: 연결이 **끊어졌어요**" + state;
    			if(lan.equals("eng"))
    				language = ":loudspeaker: **Disconnected** from the voice channel." + state;
    			
				String reply = "BOT: `(" + tc.getGuild().getName() + ")` " + language;
				response.editMessage(language).queue();
				System.out.println(reply);
				
				if(tc.getGuild().getId().equals(BotMusicListener.base)) {}
				else log(tc, event, reply);
					
			}
				
			else if(i == 4) {
				response.editMessage("원격으로 음성채널을 나갔어요" + state).queue();	
			}
				
			else if(i == 5) {
				if(tc.getGuild().getId().equals(BotMusicListener.base)) {}
				else {
					log(tc, event, "BOT: `(" + tc.getGuild().getName() + ")` 아무조치도 하지 않아 음성채널을 나갔어요");
				}
				
				response.delete().queue();
			}
			
			else if(i == 6) {
				response.delete().queue();
			}
			
			else if(i == 7) {
				String language = ":globe_with_meridians: 네트워크가 **복구되었어요**" + state;
    			if(setLanguageStop.equals("eng"))
    				language = ":globe_with_meridians: **Reconnected** to discord." + state;
    			
				String reply = "BOT: " + language;
				response.editMessage(language).queue();
				System.out.println(reply);
				
				if(tc.getGuild().getId().equals(BotMusicListener.base)) {}
				else log(tc, event, reply);
					
			}
			
			else if(i == 8) {
				String language = ":x: 트랙 정보를 받는 중 문제가 생겨 **음성채널을 나갔어요**" + state;
    			if(setLanguageStop.equals("eng"))
    				language = ":x: **Left the voice channel** because error to receive track info." + state;
    			
				String reply = "BOT: " + language;
				response.editMessage(language).queue();
				System.out.println(reply);

				if(tc.getGuild().getId().equals(BotMusicListener.base)) {}
				else log(tc, event, reply);
					
			}
			
			else if(i == 9) {
				String language = ":clipboard: 다시 재생할 준비가 되었어요" + state;
    			if(setLanguageStop.equals("eng"))
    				language = ":clipboard: Ready to play." + state;
    			
				String reply = "BOT: " + language;
				response.editMessage(language).queue();
				System.out.println(reply);

				if(tc.getGuild().getId().equals(BotMusicListener.base)) {}
				else log(tc, event, reply);
					
			}
			
			else if(i == 10) {
				String language = ":stopwatch: 오랫동안 일시정지 상태여서 **음성채널을 나갔어요**" + state;
    			if(setLanguageStop.equals("eng"))
    				language = ":stopwatch: Paused for a long time so **left the voice channel**." + state;
    			
				String reply = "BOT: " + language;
				response.editMessage(language).queue();
				System.out.println(reply);

				if(tc.getGuild().getId().equals(BotMusicListener.base)) {}
				else log(tc, event, reply);
					
			}
			
		};

		Thread t1 = new Thread(end1);
		t1.start();

		Thread t2 = new Thread(end2);
		t2.start();


		player.startTrack(null, false);
		player.setPaused(false);
		player.destroy();
		
		wasDeletedPlaylistElements = false;
		
		search = 0;
	
		timerIsOn = 0;
		timeLeftQueue = 0;
		loadCount = 0;
		counting = 0;
		countend = 1;
		
		menu = 0;
		removed = 0;
		lastLong = 0;
		last = 0;
		lastNum = 0;
		playStandByInt = 0;
		autoPaused = 0;
		isIn = 0;
		isPlay = 0;
		lock = 0;
		
		setLanguageNext = "kor"; 
		setTimerLanguage = "kor"; 
		setLanguageList = "kor"; 
		setLanguageStop = "kor";
		setLanguageNow = "kor";
		setLanguageLast = "kor";
		
		
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
			userCancelId = ""; botCancelId = "";
			userPlayAgainId = ""; botPlayAgainId = "";
			
			//waitLoad
			waitQueueAgainPersonal = ""; waitQueueAgainPersonalUser = "";
			waitShuffle = "";
			waitRemoveMany = ""; waitRemoveManyUser = "";
			waitRemove = ""; waitRemoveUser = "";
			
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