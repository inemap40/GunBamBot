package play;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import functions.CustomFunctions;
import main.BotMusicListener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayScheduler extends CustomFunctions{
	TextChannel tc;
	Message msg;
	MessageReceivedEvent event;
	
	//single
	Timer timer200ms = null;
	boolean isRspPlaying = false;
	String botsRsp = "";
	String userRsp = "";
	int received = 0;
	int botReceived = 0;
	
	//multi
	boolean isRspMulti = false;
	boolean isGetting = false;
	Message getPlayerMessage, getUserMessage;
	Timer sec60Timer = null;
	List<Member> players = new ArrayList<>();
	HashMap<Member, String> playerReceive = new HashMap<>();
	

	public PlayScheduler(Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event) {
		this.tc = tc;
		this.msg = msg;
		this.event = event;
	}
	
	public void rspWithBot(TextChannel tc, Message msg, MessageReceivedEvent event, String type, String msgStr) {
		if(isGetting) {
			tc.sendMessage("모집 중이에요").queue(response -> {
				response.delete().queueAfter(3000, TimeUnit.MILLISECONDS, non -> {
					removeMessage(tc, msg.getId());
				});
			});
			
			log(tc, event, "BOT: 모집 중이에요");
			System.out.println("BOT: 모집 중이에요");
		}
		
		if(type.equals("start")) {
			if(isRspPlaying || isRspMulti) return;
			
			isRspPlaying = true;
			received = 0;
			botReceived = 0;
			userRsp = null;
			
			Random r = new Random();
			
			int i = r.nextInt(3);
			switch(i) {
				case 0:
					botsRsp = "보";
					break;
				case 1:
					botsRsp = "가위";
					break;
				case 2:
					botsRsp = "바위";
					break;
			}
			
			tc.sendMessage("가위..바위..3 `(아직 전송하지 마세요)`").queue(three -> {
				three.editMessage("가위..바위..2 `(아직 전송하지 마세요)`").queueAfter(800, TimeUnit.MILLISECONDS, two -> {
					two.editMessage("가위..바위..1 `(아직 전송하지 마세요)`").queueAfter(800, TimeUnit.MILLISECONDS, one -> {
						one.editMessage("가위..바위.. **전송하세요**").queueAfter(800, TimeUnit.MILLISECONDS, go -> {
							go.editMessage("가위..바위.. **전송하세요**").queueAfter(200, TimeUnit.MILLISECONDS, show -> {
								System.out.println("BOT: `(" + tc.getGuild().getName() + ")` 전송하세요 (" + botsRsp + ")");
								
								Date date = new Date();
					        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
					    		String str = dayTime.format(date);
					    		
					    		log(tc, event, ".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n" + event.getJDA().getSelfUser().toString() + ": " + "전송하세요 (" + botsRsp + ")" + "\n");
					        	
								if(timer200ms == null) {
				        			timer200ms = new Timer();
				        			check200ms(tc, msg, "single");
				        		}
								
								tc.sendMessage(botsRsp).queue();
								
								botReceived = 1;
								
								if(userRsp != null)
									rspWithBot(tc, msg, event, "bot", userRsp);
								
							});
							
						});
					});
				});
			});
			
		}
		else {
			if(!isRspPlaying) return;
			
			if(!type.equals("bot")) {
				Date date = new Date();
	        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
	    		String str = dayTime.format(date);
	    		log(tc, event, ".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n" + msg.getAuthor().toString() + ": " + msg.getContentRaw() + "\n");
			}
			
			if(msgStr.equals("주먹")||msgStr.equals("묵")) msgStr = "바위";
			else if(msgStr.equals("보자기")||msgStr.equals("빠")) msgStr = "보";
			else if(msgStr.equals("찌")) msgStr = "가위";
			
			received = 1;
			
			if(userRsp == null)
				userRsp = msgStr;
			
		
			if(botReceived == 0) {
				return;
			}
			
			String result = "";
			if(msgStr.equals(botsRsp))
				result = "무승부!";
			else if((msgStr.equals("보")&&botsRsp.equals("가위")) || (msgStr.equals("가위")&&botsRsp.equals("바위")) || (msgStr.equals("바위")&&botsRsp.equals("보")))
				result = "제가 이겼어요!";
			else
				result = "**" + msg.getMember().getUser().getName() + "님**이 이겼어요!";
			
			tc.sendMessage(result).queueAfter(500, TimeUnit.MILLISECONDS);
			log(tc, event, "BOT: `(" + tc.getGuild().getName() + ")` " + msgStr + "/" + botsRsp + "/" + result);
			System.out.println("BOT: `(" + tc.getGuild().getName() + ")` " + msgStr + "/" + botsRsp + "/" + result);
			
			isRspPlaying = false;
			
		}
	}
	
	public void rspMulti(TextChannel tc, Message msg, MessageReceivedEvent event, String type, String msgStr) {
		if(isRspPlaying || isGetting) return;

		if(type.equals("getting")) {
			
			getUserMessage = msg;
			isGetting = true;
			
			playerReceive.clear();
			players.clear();	
			
			tc.sendMessage("(모집 중) 2명만 이 반응을 눌러주세요").queue(response -> {
				response.addReaction("U+1f44d").queue();
				getPlayerMessage = response;
				
				if(sec60Timer == null)
					sec60Timer = new Timer();
				secGettingTimer(tc);
				
			});
			
			log(tc, event, "BOT: 2명 모집 중이에요");
			System.out.println("BOT: 2명 모집 중이에요");
			
		}
		else if(type.equals("start")) {
			Object player1name = players.get(0).getUser().getName();
			if(players.get(0).getNickname() != null)
				player1name = players.get(0).getNickname();
			
			Object player2name = players.get(1).getUser().getName();
			if(players.get(1).getNickname() != null)
				player2name = players.get(1).getNickname();
			
			tc.sendMessage("**" + player1name + "**님과 **" + player2name + "**님이 가위바위보를 붙어요").queue(response -> {
				System.out.println("BOT: `(" + tc.getGuild().getName() + ")` 게임을 시작해요");
				log(tc, event, "BOT: `(" + tc.getGuild().getName() + ")` 게임을 시작해요");
				
				removeMessage(tc, getPlayerMessage.getId());
				isRspMulti = true;

				tc.sendMessage("가위..바위..3 `(아직 전송하지 마세요)`").queueAfter(2000, TimeUnit.MILLISECONDS, three -> {
					three.editMessage("가위..바위..2 `(아직 전송하지 마세요)`").queueAfter(1000, TimeUnit.MILLISECONDS, two -> {
						two.editMessage("가위..바위..1 `(아직 전송하지 마세요)`").queueAfter(1000, TimeUnit.MILLISECONDS, one -> {
							one.editMessage("가위..바위.. **(전송하세요)**").queueAfter(1000, TimeUnit.MILLISECONDS, go -> {
								System.out.println("BOT: `(" + tc.getGuild().getName() + ")` 전송하세요 (멀티)");
								
								Date date = new Date();
					        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
					    		String str = dayTime.format(date);
					    		
					    		log(tc, event, ".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n" + event.getJDA().getSelfUser().toString() + ": " + "전송하세요 (멀티)" + "\n");
					        	
								
								if(timer200ms == null) {
				        			timer200ms = new Timer();
				        			check200ms(tc, msg, "multi");
				        		}});
							});
							
						});
					});

			});
		}
		
		else {
			if(isRspMulti) {
				
				Date date = new Date();
	        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
	    		String str = dayTime.format(date);
	    		log(tc, event, ".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n" + msg.getAuthor().toString() + ": " + msg.getContentRaw() + "\n");
	        	
				if(msgStr.equals("주먹")||msgStr.equals("묵")) msgStr = "바위";
				else if(msgStr.equals("보자기")||msgStr.equals("빠")) msgStr = "보";
				else if(msgStr.equals("찌")) msgStr = "가위";
				
				
				playerReceive.put(msg.getMember(), msgStr);
				
				if(playerReceive.size() == 2 && !playerReceive.containsValue("null")) {
					isRspMulti = false;
					
					String result = "";
					if(playerReceive.get(players.get(0)).equals(playerReceive.get(players.get(1)))) {
						result = "무승부!";
					}
					
					else if((playerReceive.get(players.get(0)).equals("보")&&playerReceive.get(players.get(1)).equals("가위")) || (playerReceive.get(players.get(0)).equals("가위")&&playerReceive.get(players.get(1)).equals("바위")) || (playerReceive.get(players.get(0)).equals("바위")&&playerReceive.get(players.get(1)).equals("보"))) {
						Object winName = players.get(1).getUser().getName();
						if(players.get(0).getNickname() != null)
							winName = players.get(1).getNickname();
						
						result = "**" + winName + "**님이 이겼어요!";
					}
						
					else {
						Object winName = players.get(0).getUser().getName();
						if(players.get(1).getNickname() != null)
							winName = players.get(0).getNickname();
						
						result = "**" + winName + "**님이 이겼어요!";
					}
					
					tc.sendMessage(result).queueAfter(500, TimeUnit.MILLISECONDS);
					System.out.println("BOT: `(" + tc.getGuild().getName() + ")` " + playerReceive.get(players.get(0)) + "/" + playerReceive.get(players.get(1)) + "/" + result);
					log(tc, event, "BOT: `(" + tc.getGuild().getName() + ")` " + playerReceive.get(players.get(0)) + "/" + playerReceive.get(players.get(1)) + "/" + result);
				}
			}
		}
	}
	
	public void addReaction(TextChannel tc, String msgId, ReactionEmote emote, Member member) {
		if(!isGetting) return;
		if(msgId.equals(getPlayerMessage.getId())) {
			if(emote.getAsCodepoints().equals("U+1f44d")) {
				players.add(member);
			
				if(players.size() == 2) {
					sec60Timer.cancel();
					sec60Timer = null;
					isGetting = false;
					isRspMulti = true;
					
					for(Member m : players)
						playerReceive.put(m, "null");
					
					rspMulti(tc, null, null, "start", null);
				}
				
			}
		}
	}
	
	public void removeReaction(TextChannel tc, String msgId, ReactionEmote emote, Member member) {
		if(!isGetting) return;
		if(msgId.equals(getPlayerMessage.getId())) {
			if(emote.getAsCodepoints().equals("U+1f44d"))
				players.remove(member);
		}
	}
	
	public void secGettingTimer(TextChannel tc) {
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	removeMessage(tc, getUserMessage.getId(), getPlayerMessage.getId());
            	isGetting = false;
            }
		};

        sec60Timer.schedule(task, 20000);
	}
	
	public void check200ms(TextChannel tc, Message msg, String type) {
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	if(type.equals("single")) {
            		
	            	timer200ms = null;
	            	String result = "";
	            	if(received == 0) {
	            		result = "시간 초과로 **" + msg.getMember().getUser().getName() + "님**의 패배!";
	            		
	            		tc.sendMessage(result).queue();
	            		System.out.println("BOT: " + result);
	            		
		            	Date date = new Date();
			        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
			    		String str = dayTime.format(date);
			    		
			    		log(tc, event, ".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n" + msg.getJDA().getSelfUser().toString() + ": " + result + "\n");
			        	
	            	}
		    		
	            	isRspPlaying = false;
            	}
            	
            	else if(type.equals("multi")) {
            		isRspMulti = false;
	            	timer200ms = null;
	            	
            		String loseName = "";
            		String result = "";
            		
            		if(playerReceive.get(players.get(0)).equals("null") && playerReceive.get(players.get(1)).equals("null")) {
            			result = "아무도 입력 안해 무승부!";
            		}
	            	
            		else if(playerReceive.get(players.get(0)).equals("null")) {
            			loseName = players.get(0).getUser().getName();
						if(players.get(0).getNickname() != null)
							loseName = players.get(0).getNickname();
						
						result = "시간 초과로 **" + loseName + "님**의 패배!";
            		}
            		else if(playerReceive.get(players.get(1)).equals("null")) {
            			loseName = players.get(1).getUser().getName();
						if(players.get(1).getNickname() != null)
							loseName = players.get(1).getNickname();
						
						result = "시간 초과로 **" + loseName + "님**의 패배!";
            		}
            		
            		tc.sendMessage(result).queue();
            		
            		System.out.println("BOT: " + result);
            		
            		Date date = new Date();
		        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
		    		String str = dayTime.format(date);
		    		
		    		log(tc, event, ".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n" + event.getJDA().getSelfUser().toString() + ": " + result + "\n");
		        	
            		
            	}
            }
		};

		if(type.equals("single"))
			timer200ms.schedule(task, 1000);
		else 
			timer200ms.schedule(task, 1300);
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
		      
		      BotMusicListener.adtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
 			  
		}
	}
	
}
