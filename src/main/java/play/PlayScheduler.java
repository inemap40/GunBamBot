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
			tc.sendMessage("���� ���̿���").queue(response -> {
				response.delete().queueAfter(3000, TimeUnit.MILLISECONDS, non -> {
					removeMessage(tc, msg.getId());
				});
			});
			
			log(tc, event, "BOT: ���� ���̿���");
			System.out.println("BOT: ���� ���̿���");
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
					botsRsp = "��";
					break;
				case 1:
					botsRsp = "����";
					break;
				case 2:
					botsRsp = "����";
					break;
			}
			
			tc.sendMessage("����..����..3 `(���� �������� ������)`").queue(three -> {
				three.editMessage("����..����..2 `(���� �������� ������)`").queueAfter(800, TimeUnit.MILLISECONDS, two -> {
					two.editMessage("����..����..1 `(���� �������� ������)`").queueAfter(800, TimeUnit.MILLISECONDS, one -> {
						one.editMessage("����..����.. **�����ϼ���**").queueAfter(800, TimeUnit.MILLISECONDS, go -> {
							go.editMessage("����..����.. **�����ϼ���**").queueAfter(200, TimeUnit.MILLISECONDS, show -> {
								System.out.println("BOT: `(" + tc.getGuild().getName() + ")` �����ϼ��� (" + botsRsp + ")");
								
								Date date = new Date();
					        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy�� MM�� dd�� HH:mm:ss");
					    		String str = dayTime.format(date);
					    		
					    		log(tc, event, ".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n" + event.getJDA().getSelfUser().toString() + ": " + "�����ϼ��� (" + botsRsp + ")" + "\n");
					        	
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
	        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy�� MM�� dd�� HH:mm:ss");
	    		String str = dayTime.format(date);
	    		log(tc, event, ".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n" + msg.getAuthor().toString() + ": " + msg.getContentRaw() + "\n");
			}
			
			if(msgStr.equals("�ָ�")||msgStr.equals("��")) msgStr = "����";
			else if(msgStr.equals("���ڱ�")||msgStr.equals("��")) msgStr = "��";
			else if(msgStr.equals("��")) msgStr = "����";
			
			received = 1;
			
			if(userRsp == null)
				userRsp = msgStr;
			
		
			if(botReceived == 0) {
				return;
			}
			
			String result = "";
			if(msgStr.equals(botsRsp))
				result = "���º�!";
			else if((msgStr.equals("��")&&botsRsp.equals("����")) || (msgStr.equals("����")&&botsRsp.equals("����")) || (msgStr.equals("����")&&botsRsp.equals("��")))
				result = "���� �̰���!";
			else
				result = "**" + msg.getMember().getUser().getName() + "��**�� �̰���!";
			
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
			
			tc.sendMessage("(���� ��) 2�� �� ������ �����ּ���").queue(response -> {
				response.addReaction("U+1f44d").queue();
				getPlayerMessage = response;
				
				if(sec60Timer == null)
					sec60Timer = new Timer();
				secGettingTimer(tc);
				
			});
			
			log(tc, event, "BOT: 2�� ���� ���̿���");
			System.out.println("BOT: 2�� ���� ���̿���");
			
		}
		else if(type.equals("start")) {
			Object player1name = players.get(0).getUser().getName();
			if(players.get(0).getNickname() != null)
				player1name = players.get(0).getNickname();
			
			Object player2name = players.get(1).getUser().getName();
			if(players.get(1).getNickname() != null)
				player2name = players.get(1).getNickname();
			
			tc.sendMessage("**" + player1name + "**�԰� **" + player2name + "**���� ������������ �پ��").queue(response -> {
				System.out.println("BOT: `(" + tc.getGuild().getName() + ")` ������ �����ؿ�");
				log(tc, event, "BOT: `(" + tc.getGuild().getName() + ")` ������ �����ؿ�");
				
				removeMessage(tc, getPlayerMessage.getId());
				isRspMulti = true;

				tc.sendMessage("����..����..3 `(���� �������� ������)`").queueAfter(2000, TimeUnit.MILLISECONDS, three -> {
					three.editMessage("����..����..2 `(���� �������� ������)`").queueAfter(1000, TimeUnit.MILLISECONDS, two -> {
						two.editMessage("����..����..1 `(���� �������� ������)`").queueAfter(1000, TimeUnit.MILLISECONDS, one -> {
							one.editMessage("����..����.. **(�����ϼ���)**").queueAfter(1000, TimeUnit.MILLISECONDS, go -> {
								System.out.println("BOT: `(" + tc.getGuild().getName() + ")` �����ϼ��� (��Ƽ)");
								
								Date date = new Date();
					        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy�� MM�� dd�� HH:mm:ss");
					    		String str = dayTime.format(date);
					    		
					    		log(tc, event, ".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n" + event.getJDA().getSelfUser().toString() + ": " + "�����ϼ��� (��Ƽ)" + "\n");
					        	
								
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
	        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy�� MM�� dd�� HH:mm:ss");
	    		String str = dayTime.format(date);
	    		log(tc, event, ".\n.\n" + str + "\n__" + tc.getGuild() + "__ `" + tc + "`\n" + msg.getAuthor().toString() + ": " + msg.getContentRaw() + "\n");
	        	
				if(msgStr.equals("�ָ�")||msgStr.equals("��")) msgStr = "����";
				else if(msgStr.equals("���ڱ�")||msgStr.equals("��")) msgStr = "��";
				else if(msgStr.equals("��")) msgStr = "����";
				
				
				playerReceive.put(msg.getMember(), msgStr);
				
				if(playerReceive.size() == 2 && !playerReceive.containsValue("null")) {
					isRspMulti = false;
					
					String result = "";
					if(playerReceive.get(players.get(0)).equals(playerReceive.get(players.get(1)))) {
						result = "���º�!";
					}
					
					else if((playerReceive.get(players.get(0)).equals("��")&&playerReceive.get(players.get(1)).equals("����")) || (playerReceive.get(players.get(0)).equals("����")&&playerReceive.get(players.get(1)).equals("����")) || (playerReceive.get(players.get(0)).equals("����")&&playerReceive.get(players.get(1)).equals("��"))) {
						Object winName = players.get(1).getUser().getName();
						if(players.get(0).getNickname() != null)
							winName = players.get(1).getNickname();
						
						result = "**" + winName + "**���� �̰���!";
					}
						
					else {
						Object winName = players.get(0).getUser().getName();
						if(players.get(1).getNickname() != null)
							winName = players.get(0).getNickname();
						
						result = "**" + winName + "**���� �̰���!";
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
	            		result = "�ð� �ʰ��� **" + msg.getMember().getUser().getName() + "��**�� �й�!";
	            		
	            		tc.sendMessage(result).queue();
	            		System.out.println("BOT: " + result);
	            		
		            	Date date = new Date();
			        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy�� MM�� dd�� HH:mm:ss");
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
            			result = "�ƹ��� �Է� ���� ���º�!";
            		}
	            	
            		else if(playerReceive.get(players.get(0)).equals("null")) {
            			loseName = players.get(0).getUser().getName();
						if(players.get(0).getNickname() != null)
							loseName = players.get(0).getNickname();
						
						result = "�ð� �ʰ��� **" + loseName + "��**�� �й�!";
            		}
            		else if(playerReceive.get(players.get(1)).equals("null")) {
            			loseName = players.get(1).getUser().getName();
						if(players.get(1).getNickname() != null)
							loseName = players.get(1).getNickname();
						
						result = "�ð� �ʰ��� **" + loseName + "��**�� �й�!";
            		}
            		
            		tc.sendMessage(result).queue();
            		
            		System.out.println("BOT: " + result);
            		
            		Date date = new Date();
		        	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy�� MM�� dd�� HH:mm:ss");
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
