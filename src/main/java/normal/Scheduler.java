package normal;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import functions.*;
import main.BotMusicListener;
import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import parser.*;


public class Scheduler extends CustomFunctions {
	
	TextChannel tc;
	Message msg;
	MessageReceivedEvent event;
	User user;
	
	String userNormalTimerId = "", botNormalTimerId = "", userNowTimerId = "";
	String userReTimerNoneId = "", botReTimerNoneId = "";
	String newsUserMessageId = "", newsBotMessageId = "", newsBotMessage2Id = "";
	String helpUserMessageId = "", helpBotMessageId = "";
	String kospiUserMessageId = "", kospiBotMessageId = "";
	String specUserMessageId = "", specBotMessageId = "";
	String infoUserMessageId = "", infoBotMessageId = "";
	String uptimeUserMessageId = "", uptimeBotMessageId = "";
	String cpuUserMessageId = "", cpuBotMessageId = "";
	String heapUserMessageId = "", heapBotMessageId = "";
	String permissionUserMessageId = "", permissionBotMessageId = "";
	String applyUserMessage1Id = "", applyBotMessage1Id = "";
	String applyUserMessage2Id = "", applyBotMessage2Id = "";
	String applyUserMessage3Id = "", applyBotMessage3Id = "";
	String applyUserMessage4Id = "", applyBotMessage4Id = "";
	String applyUserMessage5Id = "", applyBotMessage5Id = "";
	String backUpUserMessageId = "", backUpBotMessageId = "";
	
	String nicknameSynchronizedUserMessageId = "", nicknameSynchronizedBotMessageId = "";
	String saveQueueAllowGuildUserMessageId = "", saveQueueAllowGuildBotMessageId = "";
	String loadAllowGuildUserMessageId = "", loadAllowGuildBotMessageId = "";
	String muteChannelUserMessageId = "", muteChannelBotMessageId = "";
	String clearAllowUserMessageId = "", clearAllowBotMessageId = "";
	String ignoreArgs0UserMessageId = "", ignoreArgs0BotMessageId = "";
	String ignorePrefixUserMessageId = "", ignorePrefixBotMessageId = "";
	
	HashMap<User, String> userMyInfoMessageId = new HashMap<>();
	HashMap<User, String> botMyInfoMessageId = new HashMap<>();
	
	HashMap<String, String> userNoneWeatherMessageId = new HashMap<>(); // ����, id
	HashMap<String, String> botNoneWeatherMessageId = new HashMap<>(); // ����, id
	HashMap<HashMap<String, String>, String> userWeatherMessageId = new HashMap<>(); // ����, �ð�, id
	HashMap<HashMap<String, String>, String> botWeatherMessageId = new HashMap<>(); // ����, �ð�, id
	
	Message helpUserMessage, helpBotMessage;
	String helpLan = "kor";
	int helpPage = 1;
	int reTimerMinute = 0;
	
	float recentResult = 0f;
	
	String cpu = "[Intel(R) Core(TM) i7-10510U](https://ark.intel.com/content/www/kr/ko/ark/products/196449/intel-core-i7-10510u-processor-8m-cache-up-to-4-90-ghz.html)";
	
	Timer normalTimer = null;
	Timer uptimeTimer = null;
	Timer cpuTimer = null;
	
	int timeLeft = 0;
	int timeUp = 0;
	
	int normalRun = 0;
	int uptimeRun = 0;
	int uptimeCounting = 0;
	
	int cpuRun = 0;
	int cpuCounting = 0;

	
	public Scheduler(Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event) {
		this.tc = tc;
		this.msg = msg;
		this.event = event;
	}
	
	public void apply(TextChannel tc, Message msg, MessageReceivedEvent event, int menu) {
		
		File file1 = new File(BotMusicListener.directoryDefault + "backup/saveQueueAllowGuild.txt");
		File file2 = new File(BotMusicListener.directoryDefault + "guild/loadAllowGuild.txt");
		File file3 = new File(BotMusicListener.directoryDefault + "guild/loadAllowUser.txt");
		File file4 = new File(BotMusicListener.directoryDefault + "guild/muteChannels.txt");
		File file5 = new File(BotMusicListener.directoryDefault + "guild/clearAllowUser.txt");
		
		if(menu == 1) {
			applyUserMessage1Id = autoRemoveMessage(tc, msg, applyUserMessage1Id, applyBotMessage1Id);
			
			tc.sendMessage(applySendMessage(tc, file1, "**1. �ٽ���� ��� ���� ID**```css\n", Main.saveQueueAllowGuild)).queue(response -> {
        		applyBotMessage1Id = response.getId();
			});
		}
		else if(menu == 2) {
			applyUserMessage2Id = autoRemoveMessage(tc, msg, applyUserMessage2Id, applyBotMessage2Id);
			
			tc.sendMessage(applySendMessage(tc, file2, "**2. �ҷ����� ��� ���� ID**```css\n", Main.loadAllowGuild)).queue(response -> {
        		applyBotMessage2Id = response.getId();
			});
		}
		else if(menu == 3) {
			applyUserMessage3Id = autoRemoveMessage(tc, msg, applyUserMessage3Id, applyBotMessage3Id);
			
			tc.sendMessage(applySendMessage(tc, file3, "**3. �ҷ����� ��� ���� ID**```css\n", Main.loadAllowUser)).queue(response -> {
        		applyBotMessage3Id = response.getId();
			});
		}
		else if(menu == 4) {
			applyUserMessage4Id = autoRemoveMessage(tc, msg, applyUserMessage4Id, applyBotMessage4Id);
			
			tc.sendMessage(applySendMessage(tc, file4, "**4. ���� ��ɾ� ���� ä�� ID**```css\n", Main.muteChannels)).queue(response -> {
        		applyBotMessage4Id = response.getId();
			});
		}
		else if(menu == 5) {
			applyUserMessage5Id = autoRemoveMessage(tc, msg, applyUserMessage5Id, applyBotMessage5Id);
			
        	tc.sendMessage(applySendMessage(tc, file5, "**5. clear��ɾ� ��� ID**```css\n", Main.clearAllowUser)).queue(response -> {
        		applyBotMessage5Id = response.getId();
			});
			
		}
    	
	}
	
	public void applyDirectly(TextChannel tc, Message msg, MessageReceivedEvent event, String value, String id) {
		
		File file1 = new File(BotMusicListener.directoryDefault + "backup/saveQueueAllowGuild.txt");
		File file2 = new File(BotMusicListener.directoryDefault + "guild/loadAllowGuild.txt");
		File file3 = new File(BotMusicListener.directoryDefault + "guild/loadAllowUser.txt");
		File file4 = new File(BotMusicListener.directoryDefault + "guild/muteChannels.txt");
		File file5 = new File(BotMusicListener.directoryDefault + "guild/clearAllowUser.txt");
		
		tc.sendMessage("�Է� ��...").queue(response -> {
        	try {
	        	if(value.equals("1")||value.contains("saveQueueAllowGuild")) {
	        		if(Main.saveQueueAllowGuild.contains(id))
	        			response.editMessage("�̹� ��ϵǾ� �־��").queue();
	        		else {
	        			applyDirectlyFile(file1, id, Main.saveQueueAllowGuild, response);
		        		
						Runnable r = () -> {
							StringBuilder list = new StringBuilder();
							list.append("```css\n");
							for(int i = 0; i<Main.saveQueueAllowGuild.size(); i++) {
								list.append(Main.saveQueueAllowGuild.get(i) + "\n");
							}
							list.append("```");
							
							try {
								response.editMessage("**" + tc.getJDA().getGuildById(id).getName() + "**���� �ٽ���� ��ɾ��̿��� **����߾��**" + list(Main.saveQueueAllowGuild)).complete();
							}
							catch(Exception e) {
								response.editMessage("**" + id + "**���� �ٽ���� ��ɾ��̿��� **����߾��**" + list(Main.saveQueueAllowGuild)).queue();
							}
						};
						Thread t = new Thread(r);
						t.start();
	        		}
			
	        	}
	        	else if(value.equals("2")||value.contains("loadAllowGuild")) {
	        		if(Main.loadAllowGuild.contains(id))
	        			response.editMessage("�̹� ��ϵǾ� �־��").queue();
	        		else {
	        			applyDirectlyFile(file2, id, Main.loadAllowGuild, response);
		        		
						Runnable r = () -> {
							
							try {
								response.editMessage("**" + tc.getJDA().getGuildById(id).getName() + "**���� �ҷ�����, �����ɾ��̿��� **����߾��**" + list(Main.loadAllowGuild)).complete();
							}
							catch(Exception e) {
								response.editMessage("**" + id + "**���� �ҷ�����, �����ɾ��̿��� **����߾��**" + list(Main.loadAllowGuild)).queue();
							}
						};
						Thread t = new Thread(r);
						t.start();
	        		}
					
	        	}
	        	else if(value.equals("3")||value.contains("loadAllowUser")) {
	        		if(Main.loadAllowUser.contains(id))
	        			response.editMessage("�̹� ��ϵǾ� �־��").queue();
	        		else {
		        		applyDirectlyFile(file3, id, Main.loadAllowUser, response);
		        		
						Runnable r = () -> {
							try {
								response.editMessage("**" + tc.getJDA().getUserById(id).getName() + "**���� �ҷ�����, �����ɾ **����߾��**" + list(Main.loadAllowUser)).complete();
							}
							catch(Exception e) {
								response.editMessage("**" + id + "**���� �ҷ�����, �����ɾ **����߾��**" + list(Main.loadAllowUser)).queue();
							}
						};
						Thread t = new Thread(r);
						t.start();
	        		}
					
	        	}
	        	else if(value.equals("4")||value.contains("muteChannel")) {
	        		if(Main.muteChannels.contains(id))
	        			response.editMessage("�̹� ��ϵǾ� �־��").queue();
	        		else {
		        		applyDirectlyFile(file4, id, Main.muteChannels, response);
		        		
						Runnable r = () -> {
							try {
								if(tc.getGuild() == tc.getJDA().getTextChannelById(id).getGuild())
									response.editMessage("**" + tc.getJDA().getTextChannelById(id).getName() + "**���� �����ɾ� �̿��� **�����߾��**" + list(Main.muteChannels)).complete();
								else {
									response.editMessage("**" + tc.getJDA().getTextChannelById(id).getGuild() + "/" + tc.getJDA().getTextChannelById(id).getName() + "**���� �����ɾ� �̿��� **�����߾��**" + list(Main.muteChannels)).complete();
								}
							}
							catch(Exception e) {
								response.editMessage("**" + id + "**���� �����ɾ� �̿��� **�����߾��**" + list(Main.muteChannels)).queue();
							}
						};
						Thread t = new Thread(r);
						t.start();
	        		}
	        	}
	        	else if(value.equals("5")||value.contains("clearAllowUser")) {
	        		if(Main.clearAllowUser.contains(id))
	        			response.editMessage("�̹� ��ϵǾ� �־��").queue();
	        		else {
		        		applyDirectlyFile(file5, id, Main.clearAllowUser, response);
		        		
						Runnable r = () -> {
							try {
								response.editMessage("**" + tc.getJDA().getUserById(id).getName() + "**���� clear��ɾ� �̿��ϴ� ���� **����߾��**" + list(Main.clearAllowUser)).complete();
							}
							catch(Exception e) {
								response.editMessage("**" + id + "**���� �����ɾ� �̿��� **����߾��**" + list(Main.clearAllowUser)).queue();
							}
						};
						Thread t = new Thread(r);
						t.start();
	        		}
	        	}
        	}
	        catch(Exception e) {
	    		String reply = ":no_entry_sign: **" + e.getMessage() + "**" + cause(e);
	    		System.out.println(reply);
	    		response.editMessage(reply).queue();
	    		log(tc, event, "BOT: " + reply);
	    	}
    	
    	});
	}
	
	public void discardDirectly(TextChannel tc, Message msg, MessageReceivedEvent event, String value, String id) {
		
		File file1 = new File(BotMusicListener.directoryDefault + "backup/saveQueueAllowGuild.txt");
		File file2 = new File(BotMusicListener.directoryDefault + "guild/loadAllowGuild.txt");
		File file3 = new File(BotMusicListener.directoryDefault + "guild/loadAllowUser.txt");
		File file4 = new File(BotMusicListener.directoryDefault + "guild/muteChannels.txt");
		File file5 = new File(BotMusicListener.directoryDefault + "guild/clearAllowUser.txt");
		
		tc.sendMessage("�Է� ��...").queue(response -> {
        	try {
	        	if(value.equals("1")||value.contains("saveQueueAllowGuild")) {
	        		boolean removed = isRemoved(file1, id, response);
	        		
					if(removed == true) {
	    				Main.saveQueueAllowGuild.remove(id);
	    				
						Runnable r = () -> {
							try {
								response.editMessage("**" + tc.getJDA().getGuildById(id).getName() + "**���� �ٽ���� ��ɾ��̿��� **�����߾��**" + list(Main.saveQueueAllowGuild)).complete();
							}
							catch(Exception e) {
								response.editMessage("**" + id + "**���� �ٽ���� ��ɾ��̿��� **�����߾��**" + list(Main.saveQueueAllowGuild)).queue();
							}
						};
						Thread t = new Thread(r);
						t.start();
					}
					else
						response.editMessage("������ ��� id�� �������� �ʾҾ��").queue();
	    			
	        	}
	        	else if(value.equals("2")||value.contains("loadAllowGuild")) {
	        		boolean removed = isRemoved(file2, id, response);
	    			
					if(removed == true) {
	    				Main.loadAllowGuild.remove(id);
	    				
						Runnable r = () -> {
							try {
								response.editMessage("**" + tc.getJDA().getGuildById(id).getName() + "**���� �ҷ�����, ���� ��ɾ��̿��� **�����߾��**" + list(Main.loadAllowGuild)).complete();
							}
							catch(Exception e) {
								response.editMessage("**" + id + "**���� �ҷ�����, ���� ��ɾ��̿��� **�����߾��**" + list(Main.loadAllowGuild)).queue();
							}
						};
						Thread t = new Thread(r);
						t.start();
					}
					else
						response.editMessage("������ ��� id�� �������� �ʾҾ��").queue();
					
	        	}
	        	else if(value.equals("3")||value.contains("loadAllowUser")) {
	        		boolean removed = isRemoved(file3, id, response);
	        		
					if(removed == true) {
	    				Main.loadAllowUser.remove(id);
	    				
						Runnable r = () -> {
							try {
								response.editMessage("**" + tc.getJDA().getUserById(id).getName() + "**���� �ҷ�����, ���� ��ɾ **�����߾��**" + list(Main.loadAllowUser)).complete();
							}
							catch(Exception e) {
								response.editMessage("**" + id + "**���� �ҷ�����, ���� ��ɾ **�����߾��**" + list(Main.loadAllowUser)).queue();
							}
						
						};
						Thread t = new Thread(r);
						t.start();
					}
					else
						response.editMessage("������ ���� id�� �������� �ʾҾ��").queue();
	        	}
	        	else if(value.equals("4")||value.contains("muteChannel")) {
	        		boolean removed = isRemoved(file4, id, response);
	        	
					if(removed == true) {
	    				Main.muteChannels.remove(id);
	    				
						Runnable r = () -> {
							try {
								if(tc.getGuild() == tc.getJDA().getTextChannelById(id).getGuild())
									response.editMessage("**" + tc.getJDA().getTextChannelById(id).getName() + "**���� �����ɾ� �̿��� **����߾��**" + list(Main.muteChannels)).complete();
								else {
									response.editMessage("**" + tc.getJDA().getTextChannelById(id).getGuild() + "/" + tc.getJDA().getTextChannelById(id).getName() + "**���� �����ɾ� �̿��� **����߾��**" + list(Main.muteChannels)).complete();
								}
							}
							catch(Exception e) {
								response.editMessage("**" + id + "**���� �����ɾ� �̿��� **����߾��**" + list(Main.muteChannels)).queue();
							}
						};
						Thread t = new Thread(r);
						t.start();
					}
					else
						response.editMessage("����� ä�� id�� �������� �ʾҾ��").queue();
	        	}
	        	else if(value.equals("5")||value.contains("clearAllowUser")) {
	        		boolean removed = isRemoved(file5, id, response);
	        		
					if(removed == true) {
	    				Main.clearAllowUser.remove(id);
	    				
						Runnable r = () -> {
							try {
								response.editMessage("**" + tc.getJDA().getUserById(id).getName() + "**���� clear��ɾ� �̿��ϴ� ���� **�����߾��**" + list(Main.clearAllowUser)).complete();	
							}
							catch(Exception e) {
								response.editMessage("**" + id + "**���� clear��ɾ� �̿��ϴ� ���� **�����߾��**" + list(Main.clearAllowUser)).queue();
							}
						};
						Thread t = new Thread(r);
						t.start();
					}
					else
						response.editMessage("������ ���� id�� �������� �ʾҾ��").queue();
	        	}
        	}
	        catch(Exception e) {
	    		String reply = ":no_entry_sign: **" + e.getMessage() + "**" + cause(e);
	    		System.out.println(reply);
	    		response.editMessage(reply).queue();
	    		log(tc, event, "BOT: " + reply);
	    	}
    	
    	});
	}
	
	public void showNicknameSynchronized(TextChannel tc, Message msg, MessageReceivedEvent event) {
		
		nicknameSynchronizedUserMessageId = autoRemoveMessage(tc, msg, nicknameSynchronizedUserMessageId, nicknameSynchronizedBotMessageId);
		
		if(Main.nickNameSynchronizedGuild.equals("null")) {
			tc.sendMessage("�ƹ����� �����Ǿ����� �ʾƿ�").queue(response -> {
				nicknameSynchronizedBotMessageId = response.getId();
			});
		}
		else {
			Object o = null;
			try {
				o = tc.getJDA().getGuildById(Main.nickNameSynchronizedGuild).getName();
			}
			catch(Exception e) {
				o = Main.nickNameSynchronizedGuild;
			}
			
			tc.sendMessage("���� **" + o + "**������ �г����� �����Ǿ��־��").queue(response -> {
				nicknameSynchronizedBotMessageId = response.getId();
			});
		}
			
	}
	
	public void showSaveQueueAllowGuild(TextChannel tc, Message msg, MessageReceivedEvent event) {
		
		saveQueueAllowGuildUserMessageId = autoRemoveMessage(tc, msg, saveQueueAllowGuildUserMessageId, saveQueueAllowGuildBotMessageId);
		
		if(Main.saveQueueAllowGuild.isEmpty()) {
			tc.sendMessage("�ƹ����� �ٽ������ɾ �̿��� �� �����").queue(response -> {
				saveQueueAllowGuildBotMessageId = response.getId();
			});
		}
		else {
			StringBuilder sb = new StringBuilder();
			sb.append("�ٽ���� ��� ���� ���```css\n");
			
			for(int i = 0; i<Main.saveQueueAllowGuild.size(); i++) {	
				try {
					Object o = tc.getJDA().getGuildById(Main.saveQueueAllowGuild.get(i));
					if(o == null)
						sb.append("G:" + Main.saveQueueAllowGuild.get(i) + "\n");
					else
						sb.append(o + "\n");
				}
				catch(Exception e) {
					sb.append("G:" + Main.saveQueueAllowGuild.get(i) + " (Ȯ���ʿ�)\n");
				}
			}
			sb.append("```");
				
			tc.sendMessage(sb).queue(response -> {
				saveQueueAllowGuildBotMessageId = response.getId();
			});
		}	
	}
	
	public void showLoadAllow(TextChannel tc, Message msg, MessageReceivedEvent event) {
		
		loadAllowGuildUserMessageId = autoRemoveMessage(tc, msg, loadAllowGuildUserMessageId, loadAllowGuildBotMessageId);
		
		if(Main.loadAllowGuild.isEmpty() && Main.loadAllowUser.isEmpty()) {
			tc.sendMessage("�ƹ��� �ҷ�����, �����ɾ �̿��� �� �����").queue(response -> {
				loadAllowGuildBotMessageId = response.getId();
			});
		}
		else {
			StringBuilder sb = new StringBuilder();
			sb.append("�ҷ����� ��� ���```css\n");
			
			for(int i = 0; i<Main.loadAllowGuild.size(); i++) {
				try {
					Object o = tc.getJDA().getGuildById(Main.loadAllowGuild.get(i));
					if(o == null)
						sb.append("G:" + Main.loadAllowGuild.get(i) + "\n");
					else
						sb.append(o + "\n");
				}
				catch(Exception e) {
					sb.append("G:" + Main.loadAllowGuild.get(i) + " (Ȯ���ʿ�)\n");
				}
			}
			for(int i = 0; i<Main.loadAllowUser.size(); i++) {
				try {
					Object o = tc.getJDA().getUserById(Main.loadAllowUser.get(i));
					if(o == null)
						sb.append("U:" + Main.loadAllowUser.get(i) + "\n");
					else
						sb.append(o + "\n");
				}
				catch(Exception e) {
					sb.append("U:" + Main.loadAllowUser.get(i) + " (Ȯ���ʿ�)\n");
				}
			}
			sb.append("```");
				
			tc.sendMessage(sb).queue(response -> {
				loadAllowGuildBotMessageId = response.getId();
			});
		}	
	}
	
	public void showMuteChannel(TextChannel tc, Message msg, MessageReceivedEvent event) {
		
		muteChannelUserMessageId = autoRemoveMessage(tc, msg, muteChannelUserMessageId, muteChannelBotMessageId);
		
		if(Main.muteChannels.isEmpty()) {
			tc.sendMessage("��� ������ �� �̿��� �����ؿ�").queue(response -> {
				muteChannelBotMessageId = response.getId();
			});
		}
		else {
			StringBuilder sb = new StringBuilder();
			sb.append("�����ɾ� �Ұ� ä�� ���```css\n");
			
			for(int i = 0; i<Main.muteChannels.size(); i++) {
				try {
					Object o = tc.getJDA().getTextChannelById(Main.muteChannels.get(i));
					if(o == null)
						sb.append("TC:" + Main.muteChannels.get(i) + "\n");
					else
						sb.append(tc.getJDA().getTextChannelById(Main.muteChannels.get(i)).getGuild() + "\n  " + o + "\n");
				}
				catch(Exception e) {
					sb.append("TC:" + Main.muteChannels.get(i) + " (Ȯ���ʿ�)\n");
				}
			}
			sb.append("```");
				
			tc.sendMessage(sb).queue(response -> {
				muteChannelBotMessageId = response.getId();
			});
		}	
	}
	
	public void showClearAllowUser(TextChannel tc, Message msg, MessageReceivedEvent event) {

		clearAllowUserMessageId = autoRemoveMessage(tc, msg, clearAllowUserMessageId, clearAllowBotMessageId);
		
		if(Main.clearAllowUser.isEmpty()) {
			tc.sendMessage("�ƹ��� clear��ɾ �̿��� �� �����").queue(response -> {
				clearAllowBotMessageId = response.getId();
			});
		}
		else {
			StringBuilder sb = new StringBuilder();
			sb.append("clear��ɾ� �̿밡�� ���� ���```css\n");
			
			for(int i = 0; i<Main.clearAllowUser.size(); i++) {
				try {
					Object o = tc.getJDA().getUserById(Main.clearAllowUser.get(i));
					if(o == null)
						sb.append("U:" + Main.clearAllowUser.get(i) + "\n");
					else
						sb.append(o + "\n");
				}
				catch(Exception e) {
					sb.append("U:" + Main.clearAllowUser.get(i) + " (Ȯ���ʿ�)\n");
				}
			}
			sb.append("```");
				
			tc.sendMessage(sb).queue(response -> {
				clearAllowBotMessageId = response.getId();
			});
		}	
	}
	
	public void setNicknameSynchronized(TextChannel tc, Message msg, MessageReceivedEvent event, String id) {
		
		if(Main.nickNameSynchronizedGuild.equals("null")) {
			tc.sendMessage("�г��� ���� ������ �����߾��").queue();
		}
		else {
			Object o = null;
			try {
				o = tc.getJDA().getGuildById(id).getName();
			}
			catch(Exception e) {
				o = id;
			}
			
			try {
				Main.nickNameSynchronizedGuild = id;
				
				File file = new File(BotMusicListener.directoryDefault + "guild/nicknameSynchronizedGuild.txt");
	    		
				FileWriter fw = new FileWriter(file);
				fw.write(id);
				fw.close();
				
				tc.sendMessage("**" + o + "**������ �г����� �����ؿ�").queue();
			}
			catch(Exception e) {
				tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
			}
		}
			
	}
	
	public void showIgnoreArgs0(TextChannel tc, Message msg, MessageReceivedEvent event) {

		ignoreArgs0UserMessageId = autoRemoveMessage(tc, msg, ignoreArgs0UserMessageId, ignoreArgs0BotMessageId);
		
		if(Main.clearAllowUser.isEmpty()) {
			tc.sendMessage("������ �ܾ �����").queue(response -> {
				ignoreArgs0BotMessageId = response.getId();
			});
		}
		else {
			StringBuilder sb = new StringBuilder();
			sb.append("��Ʃ�� �˻����� �����ϴ� �޼��� �����ϴ� �ܾ��```css\n");
			
			for(int i = 0; i<Main.ignoreArgs0.size(); i++) {
				if(i == Main.ignoreArgs0.size() - 1)
					sb.append(Main.ignoreArgs0.get(i));
				else
					sb.append(Main.ignoreArgs0.get(i) + ", ");
			}
			sb.append("```");
				
			tc.sendMessage(sb).queue(response -> {
				ignoreArgs0BotMessageId = response.getId();
			});
		}	
	}
	
	public void showIgnorePrefix(TextChannel tc, Message msg, MessageReceivedEvent event) {

		ignorePrefixUserMessageId = autoRemoveMessage(tc, msg, ignorePrefixUserMessageId, ignorePrefixBotMessageId);
		
		if(Main.clearAllowUser.isEmpty()) {
			tc.sendMessage("������ ���λ簡 �����").queue(response -> {
				ignorePrefixBotMessageId = response.getId();
			});
		}
		else {
			StringBuilder sb = new StringBuilder();
			sb.append("Ư�� ���λ�� �����ϴ� �޼��� ���� �� �����ϴ� ���λ��```css\n");
			
			for(int i = 0; i<Main.ignorePrefix.size(); i++) {
				if(i == Main.ignorePrefix.size() - 1)
					sb.append(Main.ignorePrefix.get(i));
				else
					sb.append(Main.ignorePrefix.get(i) + ", ");
			}
			sb.append("```");
				
			tc.sendMessage(sb).queue(response -> {
				ignorePrefixBotMessageId = response.getId();
			});
		}	
	}
	
	public void calculate(TextChannel tc, Message msg, MessageReceivedEvent event) {
		
		String str = msg.getContentRaw().replaceAll("����", "").replaceAll(" ", "").replaceAll("���", "").replaceAll("=", "");
		//str = "3+2"
		String[] load = null;
		float result = 0f;
		String resultstr = "";
		//
		if(str.contains("*"))
			str = str.replaceAll("\\*", "x");
		
		try {
			if(str.contains("/")) {
				load = str.split("/");
				
				try {
					result = Float.parseFloat(load[0]);
				}
				catch(Exception e) {
					result = recentResult;
				}
				
				
				for(int i = 0; i<load.length-1; i++) {
					result = result / Float.parseFloat(load[i+1]);
				}
			}
			
			else if(str.contains("x")) {
				load = str.split("x");

				try {
					result = Float.parseFloat(load[0]);
				}
				catch(Exception e) {
					result = recentResult;
				}
				
				for(int i = 0; i<load.length-1; i++) {
					result = result * Float.parseFloat(load[i+1]);
				}
			}
			
			else if(str.contains("-")) {
				load = str.split("-");

				try {
					result = Float.parseFloat(load[0]);
				}
				catch(Exception e) {
					result = recentResult;
				}
				
				for(int i = 0; i<load.length-1; i++) {
					result = result - Float.parseFloat(load[i+1]);
				}
			}
			
			else if(str.contains("+")) {
				load = str.split("\\+");

				try {
					result = Float.parseFloat(load[0]);
				}
				catch(Exception e) {
					result = recentResult;
				}
				
				for(int i = 0; i<load.length-1; i++) {
					result = result + Float.parseFloat(load[i+1]);
				}
				
			}

			recentResult = result;
			
			if(result % 1 == 0) 
				resultstr = String.format("%.0f", result);
			else
				resultstr = String.valueOf(result);
			
			tc.sendMessage("**" + resultstr + "**").queue();
			log(tc, event, "BOT: **" + resultstr + "**");
	
		}
		catch (Exception e) {
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
			log(tc, event, "BOT: :no_entry_sign: **" + e.getMessage() + "**" + cause(e));
		}
	
		
	}
	
	public void translateType(TextChannel tc, Message msg, MessageReceivedEvent event) {
		String str = msg.getContentRaw().replaceAll("����", "").replaceAll(" ", "").replaceAll("16����", "").replaceAll("10����", "").replaceAll("8����", "").replaceAll("2����", "").replaceAll("16����", "").replaceAll("10����", "").replaceAll("8����", "").replaceAll("2����", "").replaceAll("[^0-9]", "");
		if(str.equals("")) str = "0";
		
		int result = Integer.parseInt(str);
		
		if(result == 0) {
			if(recentResult > 0 && recentResult % 1 == 0) {
				result = (int)(recentResult);
			}
			else {
				tc.sendMessage("��ȯ�� ���ڸ� �Է��� �ּ���").queue();
				return;
			}
		}
		
		if(msg.getContentRaw().contains("16����") || msg.getContentRaw().contains("16����")) {
			tc.sendMessage(result + " = " + Integer.toHexString(result) + "`(16)`").queue();
		}
		
		else if(msg.getContentRaw().contains("10����") || msg.getContentRaw().contains("10����")) {
			tc.sendMessage(result + " = " + result + "`(10)`").queue();
		}
		
		else if(msg.getContentRaw().contains("8����") || msg.getContentRaw().contains("8����")) {
			tc.sendMessage(result + " = " + Integer.toOctalString(result) + "`(8)`").queue();
		}
		
		else if(msg.getContentRaw().contains("2����") || msg.getContentRaw().contains("2����")) {
			tc.sendMessage(result + " = " + Integer.toBinaryString(result) + "`(2)`").queue();
		}
	}
	
	public void setTimer(User user, TextChannel tc, Message msg, MessageReceivedEvent event, int time) {
		this.user = user;
		
		timeLeft = time;
		reTimerMinute = time;
		
		timeUp = 0;
		
		userNormalTimerId = autoRemoveMessage(tc, msg, userNormalTimerId, botNormalTimerId);
		
		if(normalRun == 1) {
			normalTimer.cancel();
			normalTimer = null;
			normalRun = 0;
		}
		
		if(normalTimer == null)
			normalTimer = new Timer();
		normalTimer(tc, msg, user, event);
	        
		String reply = "BOT: **" + time + "��**�ڿ� �˸��� �����߾��";
        tc.sendMessage("**" + time + "��**�ڿ� �˸��� �����߾��").queue(response -> {
        	botNormalTimerId = response.getId();
        });
        System.out.println(reply);
        log(tc, event, reply);
        
        userReTimerNoneId = "";
        botReTimerNoneId = "";
        
       
        normalRun = 1;
     
	}

	public void reTimer(TextChannel tc, Message msg, MessageReceivedEvent event) {
		if(reTimerMinute == 0) {
			userReTimerNoneId = autoRemoveMessage(tc, msg, userReTimerNoneId, botReTimerNoneId);
			
			tc.sendMessage("������ Ÿ�̸Ӹ� �����߾��� ����� �����").queue(response -> {
				botReTimerNoneId = response.getId();
			});
			log(tc, event, "BOT: ������ Ÿ�̸Ӹ� �����߾��� ����� �����");
			return;
		}
		
		timeLeft = reTimerMinute;
		timeUp = 0;
		
		userNormalTimerId = autoRemoveMessage(tc, msg, userNormalTimerId, botNormalTimerId);
		
		if(normalRun == 1) {
			normalTimer.cancel();
			normalTimer = null;
			normalRun = 0;
		}
		
		if(normalTimer == null)
			normalTimer = new Timer();
		normalTimer(tc, msg, user, event);
	        
		String reply = "BOT: **" + timeLeft + "��**�ڿ� �˸��� �����߾��";
        tc.sendMessage("**" + timeLeft + "��**�ڿ� �˸��� �����߾��").queue(response -> {
        	botNormalTimerId = response.getId();
        });
        System.out.println(reply);
        log(tc, event, reply);
		
        normalRun = 1;
     
	}
	
	public void nowTimer(TextChannel tc, Message msg) {
		
		userNowTimerId = autoRemoveMessage(tc, msg, userNowTimerId, botNormalTimerId);
		
		if(normalRun == 0) {
			String reply = "BOT: Ÿ�̸Ӹ� �������� �ʾҾ��";
			tc.sendMessage("Ÿ�̸Ӹ� �������� �ʾҾ��").queue();
		    System.out.println(reply);
		    log(tc, event, reply);
				
		}
			
		else {
			String reply = "BOT: **" + (int)(timeLeft - timeUp) + "��** ���Ҿ��";
		    tc.sendMessage("**" + (int)(timeLeft - timeUp) + "��** ���Ҿ��").queue(response -> {
		    	botNormalTimerId = response.getId();
		    });
		    System.out.println(reply);
		    log(tc, event, reply);
				
		}
    }

	
	public void timerCancel(TextChannel tc, Message msg) {
		
		if(timeLeft == 0) {
			String reply = "BOT: ������ Ÿ�̸Ӱ� �����";
			tc.sendMessage("������ Ÿ�̸Ӱ� �����").queue();
			System.out.println(reply);
			log(tc, event, reply);
		}
		
		else {
			normalTimer.cancel();
			normalTimer = null;
			timeLeft = 0;
			timeUp = 0;
			normalRun = 0;
	    	
	    	String reply = "BOT: Ÿ�̸Ӹ� ����߾��";
			tc.sendMessage("Ÿ�̸Ӹ� ����߾��").queue();
			System.out.println(reply);
			log(tc, event, reply);
			
			userNormalTimerId = "";
			botNormalTimerId = "";
		}
		
	}
	
	//task
	public void normalTimer(TextChannel tc, Message msg, User user, MessageReceivedEvent event) {
		
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	Runnable edit = () -> {
	            	timeUp = timeUp + 1;
	
	            	if(!tc.getGuild().getJDA().getStatus().toString().toLowerCase().equals("not")) {
		            	try {
		            		tc.editMessageById(botNormalTimerId, "**" + (int)(timeLeft - timeUp) + "��** ���Ҿ��").complete();
		            	}
		            		
		            	catch(Exception e) {
		            		tc.sendMessage("**" + (int)(timeLeft - timeUp) + "��** ���Ҿ��").queue(response -> {
		            			botNormalTimerId = response.getId();
		            		});
		            	}
	            	}
	            	
	            	if(timeUp == timeLeft) {
	            		
	            		normalTimer.cancel();
	            		normalTimer = null;
	   		
	            		removeMessage(tc, userNormalTimerId, botNormalTimerId);
	            		
			        	String reply = "BOT: <@" + user.getId() + ">  **" + timeUp + "��** Ÿ�̸Ӱ� ����Ǿ����";
						tc.sendMessage("<@" + user.getId() + ">  **" + timeUp + "��** Ÿ�̸Ӱ� ����Ǿ����").queue();
						System.out.println(reply);
						log(tc, event, reply);
						
						timeLeft = 0;
	            		timeUp = 0;
	            		normalRun = 0;
	
	            	}
            	};
            	Thread t1 = new Thread(edit);
            	t1.start();

            }
        };

        normalTimer.scheduleAtFixedRate(task, 60000, 60000);
        	
	}

	public void uptime(TextChannel tc, Message msg, MessageReceivedEvent event, int uptime, String lan) {

		if(uptimeRun == 1) {
			uptimeTimer.cancel();
			uptimeTimer = null;
			uptimeRun = 0;
		}
		
		uptimeUserMessageId = autoRemoveMessage(tc, msg, uptimeUserMessageId, uptimeBotMessageId);
		
		String language = "���� **" + uptimeFormat(uptime, lan) + "** ���� ���� ���̿���";
		if(lan.equals("eng"))
			language = "Uptime is **" + uptimeFormat(uptime, lan) + "**";
		
    	tc.sendMessage(language).queue(response -> {
    		uptimeBotMessageId = response.getId();
    	});
    	
    	if(uptimeTimer == null)
			uptimeTimer = new Timer();
    	
    	uptimeCounting = 0;
		uptimeTimer(tc, msg, event, lan);
		uptimeRun = 1;
    	
    	
	}
	
	public void uptimeTimer(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	if(!tc.getGuild().getJDA().getStatus().toString().toLowerCase().equals("not")) return;
            	
            	if(uptimeCounting < 15) {
	            	long nowTime = System.currentTimeMillis();
	            	String language = "���� **" + uptimeFormat((int)(nowTime - BotMusicListener.gunbamStartTime), lan) + "** ���� ���� ���̿���";
	            	if(lan.equals("eng"))
	            		language = "Uptime is **" + uptimeFormat((int)(nowTime - BotMusicListener.gunbamStartTime), lan) + "**";
		            
	            	String lang = language;
	            	Runnable edit = () -> {
		            	try {
			            	tc.editMessageById(uptimeBotMessageId, lang).complete();
			            }
			            		
			            catch(Exception e) {
			            	if(uptimeRun == 1) {
			            		uptimeTimer.cancel();
			            		uptimeTimer = null;
			            		uptimeRun = 0;
			            	}
			            }
	            	};
	                Thread t1 = new Thread(edit);
	                t1.start();
		            	
	                uptimeCounting = uptimeCounting + 3;
            	}
            	else {
            		if(uptimeRun == 1) {
	            		uptimeTimer.cancel();
	            		uptimeTimer = null;
	            		uptimeRun = 0;
	            	}
            	}
            		
	
            }
        };

        uptimeTimer.scheduleAtFixedRate(task, 3000, 3000);
	}
	
	public String uptimeFormat(int getTimes, String lan) { 
		
		getTimes = getTimes/1000;
        int day, hour, min, sec;

        sec  = getTimes % 60;
        min  = getTimes / 60 % 60;
        hour = getTimes / 3600;
        day = hour / 24;
        hour = hour - 24*day;
        
        String s = "";
        String dayStr = String.valueOf(day);
        String hourStr = String.valueOf(hour);
        String minStr = String.valueOf(min);
        String secStr = String.valueOf(sec);
        
        if(day < 1) {
        	s = hourStr + "�ð� " + minStr + "�� " + secStr + "��";
        }
        else 
        	s = dayStr + "�� " + hourStr + "�ð� " + minStr + "�� " + secStr + "��";
		
        if(lan.equals("eng")) {
        	if(hour < 10) hourStr = "0" + hourStr;
        	if(min < 10) minStr = "0" + minStr;
        	if(sec < 10) secStr = "0" + secStr;
        	
        	s = dayStr + ":" + hourStr + ":" + minStr + ":" + secStr;
        }
        
		return s;
	}

	public void useCpu(TextChannel tc, Message msg, String lan) {
		if(cpuRun == 1) {
			cpuTimer.cancel();
			cpuTimer = null;
			cpuRun = 0;
		}
		
		cpuUserMessageId = autoRemoveMessage(tc, msg, cpuUserMessageId, cpuBotMessageId);
		
		EmbedBuilder eb = new EmbedBuilder();
		
		String title = "�ý��� ����";
		String cpuCountStr = "���μ���";
		String usageStr = "��뷮";
		String ramStr = "RAM ��뷮";
		
		if(lan.equals("eng")) {
			title = "System Info";
			cpuCountStr = "Processor";
			usageStr = "Usage";
			ramStr = "RAM";
		}
		
		eb.setColor(Color.decode(BotMusicListener.colorDefault));
		eb.setTitle(title);
		eb.addField("CPU", cpu, false);
		eb.addField(cpuCountStr, "\n", true);
		eb.addField(usageStr, "\n", true);
		eb.addField(ramStr, "\n", true);
		eb.setFooter("Calculating...");
		
		if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
			String language = "��ũ ÷��";
			if(lan.equals("eng"))
				language = "MESSAGE_EMBED_LINKS";
			
			tc.sendMessage(askPermission(language, lan)).queue(response -> {
				cpuBotMessageId = response.getId();
			});
			log(tc, event, "BOT: " + language);
        	return;
        }
		
		tc.sendMessage(eb.build()).queue(response -> {
			cpuBotMessageId = response.getId();
			response.editMessage(showCpu(tc, lan).build()).queue();
		});
		
		if(cpuTimer == null)
			cpuTimer = new Timer();
   
		cpuCounting = 0;
		
		cpuTimer(tc, msg, lan);
		cpuRun = 1;
	
	}
	
	public void cpuTimer(TextChannel tc, Message msg, String lan) {
		TimerTask task = new TimerTask() {
			@Override
            public void run() {
				if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
					removeMessage(tc, cpuUserMessageId, cpuBotMessageId);
					
					if(cpuRun == 1) {
    					cpuTimer.cancel();
    					cpuTimer = null;
    					cpuRun = 0;
    				}
					
		        	return;
		        }
				
	            if(cpuCounting < 30) {
		            
		        	Runnable r = () -> {

		        		if(!tc.getGuild().getJDA().getStatus().toString().toLowerCase().equals("not")) {
		    				
		        			if(!tc.getGuild().getId().equals(BotMusicListener.base))
			        			cpuCounting = cpuCounting + 3;
			     
		        			try {
		        				tc.editMessageById(cpuBotMessageId, showCpu(tc, lan).build()).complete();
		        			}
		        			catch(Exception e) {
		        				if(cpuRun == 1) {
		        					cpuTimer.cancel();
		        					cpuTimer = null;
		        					cpuRun = 0;
		        				}
		        				
		        				removeMessage(tc, cpuUserMessageId, cpuBotMessageId);

		        			}
		    			}
		        	};
		        	Thread t = new Thread(r);
		        	t.start();
		
	            }
	            else {
	            		
	            	if(cpuRun == 1) {
	    				cpuTimer.cancel();
	    				cpuTimer = null;
	    				cpuRun = 0;
	    			}
	            		
	            	removeMessage(tc, cpuUserMessageId, cpuBotMessageId);
	            }
				
            }
        };
        

        cpuTimer.scheduleAtFixedRate(task, 3000, 3000);
	}
	
	@SuppressWarnings("deprecation")
	public EmbedBuilder showCpu(TextChannel tc, String lan) {
		
		com.sun.management.OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
		
		EmbedBuilder eb = new EmbedBuilder();
		
		String title = "�ý��� ����";
		String cpuCountStr = "���μ���";
		String usageStr = "��뷮";
		String ramStr = "RAM ��뷮";
		
		if(lan.equals("eng")) {
			title = "System Info";
			cpuCountStr = "Processor";
			usageStr = "Usage";
			ramStr = "RAM";
		}
		
		String usePercent = "";
		double cpuLoad = osBean.getSystemCpuLoad() * 100;
		if(cpuLoad == 100)
			usePercent = String.format("%.0f", cpuLoad) + "%";
		else
			usePercent = String.format("%.1f", cpuLoad) + "%";
		
		eb.setTitle(title);
		eb.addField("CPU", cpu, false);
		eb.addField(cpuCountStr, String.format("%.0f", (double)(osBean.getAvailableProcessors())), true);
		eb.addField(usageStr, usePercent, true);
		eb.addField(ramStr, String.format("%.2f", (double)((osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize())/1024f/1024f/1024f)) + "GB / " + String.format("%.1f", (double)(osBean.getTotalPhysicalMemorySize()/1024f/1024f/1024f)) + "GB", true);
		
		if((double)(osBean.getSystemCpuLoad() * 100) >= 80 || (double)(osBean.getFreePhysicalMemorySize()/1024f/1024f/1024f) <= 4f)
			eb.setColor(Color.RED);

		else if((double)(osBean.getSystemCpuLoad() * 100) >= 50 || (double)(osBean.getFreePhysicalMemorySize()/1024f/1024f/1024f) <= 8f)
			eb.setColor(Color.YELLOW);
		
		else if((double)(osBean.getSystemCpuLoad() * 100) >= 10)
			eb.setColor(Color.GREEN);
		
		else
			eb.setColor(Color.decode(BotMusicListener.colorDefault));
		
		if(!tc.getGuild().getId().equals(BotMusicListener.base)) {
			int leftSec = (int)(30 - cpuCounting);
			
			String leftStr = String.valueOf(leftSec);
			if(leftSec < 10) {
				leftStr = "0" + leftSec;
			}
			
			eb.setFooter("- 0:" + leftStr);
		}
		else {
			long nowTime = System.currentTimeMillis();
			eb.setFooter(uptimeFormat((int)(nowTime - BotMusicListener.gunbamStartTime), "eng") + "");
		}
		
		return eb;
	}
	
	@SuppressWarnings("static-access")
	public void heap(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		heapUserMessageId = autoRemoveMessage(tc, msg, heapUserMessageId, heapBotMessageId);
		
		if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
			String language = "��ũ ÷��";
			if(lan.equals("eng"))
				language = "MESSAGE_EMBED_LINKS";
			
			tc.sendMessage(askPermission(language, lan)).queue(response -> {
				cpuBotMessageId = response.getId();
			});
			log(tc, event, "BOT: " + language);
        	return;
        }
		
		float heapSize = Runtime.getRuntime().totalMemory()/1024f/1024f/1024f;
		float freeHeapSize = Runtime.getRuntime().freeMemory()/1024f/1024f/1024f;
		
		EmbedBuilder eb = new EmbedBuilder();
		String languageTitle = "JVM �� ũ��";
		if(lan.equals("eng"))
			languageTitle = "JVM Heap size";
		
		String languageHeap = "��";
		if(lan.equals("eng"))
			languageHeap = "Heap";
		
		String languageHeapMax = "��ü ��";
		if(lan.equals("eng"))
			languageHeapMax = "Heap total";
		
		eb.setTitle(languageTitle);
		eb.setColor(Color.decode(BotMusicListener.colorDefault));
		eb.addField(languageHeap, String.format("%.2f", (float)(heapSize - freeHeapSize)) + "GB / " + String.format("%.2f", heapSize) + "GB", false);
		eb.addField(languageHeapMax, String.format("%.1f", (float)(Runtime.getRuntime().maxMemory()/1024f/1024f/1024f)) + "GB", false);
	
		eb.setFooter("JAVA: " + Runtime.getRuntime().version());
		tc.sendMessage(eb.build()).queue(response -> {
			heapBotMessageId = response.getId();
		});
		
	
	}
	
	public void news(TextChannel tc, Message msg) {
		
		newsUserMessageId = autoRemoveMessage(tc, msg, newsUserMessageId, newsBotMessageId, newsBotMessage2Id);
		
		File file = new File(BotMusicListener.directoryDefault + "news.txt");
		MessageBuilder s = new MessageBuilder();
		try {
			FileReader filereader = new FileReader(file);
		       
		    BufferedReader bufReader = new BufferedReader(filereader);
		    String line = "";
		       
		    while((line = bufReader.readLine()) != null){
		    	s.append(line + "\n");
		    }
		               
		    bufReader.close();
		}
		catch(Exception e) {
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue(response -> {
				newsBotMessageId = response.getId();
			});
			log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
		
		}
			
		tc.sendMessage(s.build()).queue(response -> {
			newsBotMessageId = response.getId();
		});
		System.out.println("BOT: \n" + s);			

		File file2 = new File(BotMusicListener.directoryDefault + "news2.txt");
		MessageBuilder s2 = new MessageBuilder();
		try {
			FileReader filereader2 = new FileReader(file2);
		       
		    BufferedReader bufReader2 = new BufferedReader(filereader2);
		    String line2 = "";
		       
		    while((line2 = bufReader2.readLine()) != null){
		        s2.append(line2 + "\n");
		    }
		               
		    bufReader2.close();
		}
		catch(Exception e) {
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue(response -> {
				newsBotMessage2Id = response.getId();
			});
			log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
	
		}
			
		tc.sendMessage(s2.build()).queue(response -> {
			newsBotMessage2Id = response.getId();
		});
			
		System.out.println("BOT: \n" + s2);

	}
	
	public void kospi(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		
		kospiUserMessageId = autoRemoveMessage(tc, msg, kospiUserMessageId, kospiBotMessageId);
		
    	if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
    		String language = "��ũ ÷��";
			if(lan.equals("eng"))
				language = "MESSAGE_EMBED_LINKS";
			
			tc.sendMessage(askPermission(language, lan)).queue(response -> {
				kospiBotMessageId = response.getId();
			});
			log(tc, event, "BOT: MESSAGE_EMBED_LINKS");
        	return;
        }
    	
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.decode(BotMusicListener.colorDefault));
		
		String language = "�ڽ��� �����ҷ����� ��...";
		
		if(lan.equals("eng")) 
			language = "Getting KRX: Kospi...";
		
		eb.setTitle(language);
	    tc.sendMessage(eb.build()).queue(response -> { 
	    	kospiBotMessageId = response.getId();
			StringBuilder m = new StringBuilder();
			StringBuilder n = new StringBuilder();
			StringBuilder o = new StringBuilder();
					
			URL urlm;
	
			try {
				urlm = new URL("https://m.stock.naver.com/sise/siseList.nhn?menu=market_sum&sosok=0");
				BufferedReader br = new BufferedReader(new InputStreamReader(urlm.openStream(), "utf-8"));
				String line = "";
				int checkLine = 0;
						
				while((line = br.readLine()) != null) {
					if(line.contains("<a href=\"/sise/siseIndex.nhn?code=KOSPI\""))
					checkLine = 1;
						
					if(line.contains("https://ssl.pstatic.net/imgfinance/chart/mobile/mini/KOSPI")) 
						checkLine = 0;
							
					if(checkLine == 1) {
								
						if(line.contains("<span class=\"stock_price")) {
							String temp2 = line.split(">")[1].split("<")[0];
									
							n.append(temp2);
									
						}
								
						if(line.contains("<span class=\"gap_price\">")) {
							String temp3 = line.split("<em class=\"ico\">")[1].split("</em>")[0];
							String temp4 = line.split("</em>")[1].split("<")[0];
									
							if(temp3.contains("��")) temp3 = "+";
							else if(temp3.contains("��")) temp3 = "-";
							n.append(" `(" + temp3); //��ȣ
							n.append(temp4 + ")`"); //val
									
						}
								
					}				
				}
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
	       		log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
			}
			    	
			try {
		
				URL url = new URL("https://m.stock.naver.com/marketindex/index.nhn?menu=oilgold#oilgold");
						
				BufferedReader br2 = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
				BufferedReader br3 = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
						
				int checkLine = 0;
				String line = "";
				while((line = br2.readLine()) != null) {
					if(line.contains("<a href=\"/marketindex/item.nhn?marketindexCd=FX_USDKRW&menu=exchange\""))  //FX_USDKRW
						checkLine = 1;
						
					if(line.contains("<a href=\"/marketindex/item.nhn?marketindexCd=FX_EURKRW&menu=exchange\"")) 
						checkLine = 0;
							
					if(checkLine == 1) {
								
						if(line.contains("<span class=\"stock_price\">")) {
							String temp2 = line.split("<span class=\"stock_price\">")[1].split("<")[0];
							m.append(temp2 + "��");
						}
								
						if(line.contains("<span class=\"gap_price\">")) {
								
							String temp3 = line.split("<em class=\"ico\">")[1].split("</em>")[0];
					
							String temp4 = line.split("</em>")[1].split("</span>")[0];
							temp4 = temp4.replaceAll(" ", "");
	
							if(temp3.contains("��")) temp3 = "+";
							else if(temp3.contains("��")) temp3 = "-";
									
							m.append(" `(" + temp3 + temp4 + ")`");
									
						}
					}	
				}
						
				line = "";
				while((line = br3.readLine()) != null) {
					if(line.contains("<a href=\"/marketindex/item.nhn?marketindexCd=CMDT_GD&menu=oilgold\""))  //�ݽü�
						checkLine = 1;
						
					if(line.contains("<a href=\"/marketindex/item.nhn?marketindexCd=CMDT_GC&menu=oilgold\"")) 
						checkLine = 0;
							
					if(checkLine == 1) {
								
						if(line.contains("<span class=\"stock_price\">")) {
							String temp2 = line.split("<span class=\"stock_price\">")[1].split("</span>")[0];
							o.append(temp2 + "��");
									
						}
								
						if(line.contains("<span class=\"gap_price\">")) {
								
							String temp3 = line.split("<span class=\"ico\">")[1].split("</span>")[0];
					
							String temp4 = line.split("</span>")[1].split("</span>")[0];
							temp4 = temp4.replaceAll(" ", "");
	
							if(temp3.contains("��")) temp3 = "+";
							else if(temp3.contains("��")) temp3 = "-";
									
							o.append(" `(" + temp3 + temp4 + ")`");
									
						}
					}	
				}
						
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
	           	log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
			}
	
			String languageTitle = "�ѱ��� �ڽ��� ��Ȳ";
			String languageKospi = "�ڽ��� ����";
			String languageDollar = "1 �̱� �޷�";
			String languageGold = "�� 1g";
			
			if(lan.equals("eng")) {
				languageTitle = "Korea Stock Price";
				languageKospi = "KOSPI";
				languageDollar = "1 US Dollar";
				languageGold = "1g gold";
			}
				
			EmbedBuilder eb2 = new EmbedBuilder();
			eb2.setColor(Color.decode(BotMusicListener.colorDefault));
			
			eb2.setTitle(languageTitle);
			eb2.addField(languageKospi, n.toString(), true);
			eb2.addField(languageDollar, m.toString(), true);
			eb2.addField(languageGold, o.toString(), false);
			response.editMessage(eb2.build()).queue();
			
		});

	}
	
	@SuppressWarnings("deprecation")
	public void showMyInfo(TextChannel tc, Message msg, MessageReceivedEvent event, String lan, Message response) {
		
		Member member = event.getMember();
		
		removeMessage(tc, userMyInfoMessageId.get(member.getUser()), botMyInfoMessageId.get(member.getUser()));
		userMyInfoMessageId.put(member.getUser(), msg.getId());
		
		try {
			String isBot = "��";
			String isFake = "��";
			
			if(lan.equals("eng")) {
				isBot = "TRUE";
				isFake = "TRUE";
			}
				
			if(member.getUser().isBot() == false) {
				isBot = "�ƴϿ�";
				
				if(lan.equals("eng"))
					isBot = "FALSE";
			}
				
			if(member.isFake() == false) {
				isFake = "�ƴϿ�";
				
				if(lan.equals("eng"))
					isFake = "FALSE";
			}
				
			OffsetDateTime create = member.getUser().getTimeCreated();
			OffsetDateTime enter = member.getTimeJoined();
				
			EmbedBuilder eb = new EmbedBuilder();
			try {
				eb.setThumbnail(member.getUser().getAvatarUrl() + "?size=2048");
			}
			catch(Exception e) {
				eb.setThumbnail(member.getUser().getDefaultAvatarUrl() + "?size=2048");
			}
			
			String title = "���� ����";
			String name = "�̸�";
			String timeCreate = "������";
			String timeJoin = "���� ����";
			String status = "����";
			String bot = "�� ����";
			String fake = "��¥ ����";
			String timeBoost = "�ν�Ʈ ����";
			String guilds = "�����̿� �Բ��� ���� ��";
			String id = "���̵�";
			
			if(lan.equals("eng")) {
				title = "User Info";
				name = "Name";
				timeCreate = "Time Created";
				timeJoin = "Time Joined";
				status = "Status";
				bot = "Is Bot";
				fake = "Is Fake";
				timeBoost = "Time Boosted";
				guilds = "Guild count with GunBam";
				id = "ID";
			}
			
			eb.setTitle(title);
			
			eb.setColor(Color.decode(BotMusicListener.colorDefault));
			
			if(member.getNickname() != null)
				eb.addField(name, member.getNickname() + "\n(" + member.getUser().getAsTag() + ")", true);
			
			else eb.addField(name, member.getUser().getAsTag(), true);
				
			eb.addField(timeCreate, createTime(create), true);
			eb.addField(timeJoin, createTime(enter), true);
			
			eb.addField(status, msg.getMember().getOnlineStatus().toString(), true);
			eb.addField(bot, isBot, true);
			eb.addField(fake, isFake, true);
				
			if(member.getTimeBoosted() != null) {
				eb.addField(timeBoost, createTime(member.getTimeBoosted()), true);
			}
				
			eb.addField(guilds, member.getUser().getMutualGuilds().size() + "", true);
			eb.addField(id, member.getUser().getId(), true);
			
			botMyInfoMessageId.put(member.getUser(), response.getId());
			
			
			response.editMessage(eb.build()).queue();
			log(tc, event, "BOT: **" + member.getUser().getName() + "**�� ����");
			
		}
		catch(Exception e) {
			response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
			log(tc, event, "BOT: " + ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
		}
	}
	
	public void weatherToday(TextChannel tc, Message msg, MessageReceivedEvent event, String area) {
		
		try {
            URL url = new URL("http://www.kma.go.kr/XML/weather/sfc_web_map.xml");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            XmlPullParser parser = factory.newPullParser();
            parser.setInput(url.openStream(), "utf-8");
            
            String ItemWeather = "";
            String ItemTmp = "";
            String ItemRn = "";
            String time = "";
            
            boolean bSet = true;
            
            int eventType = parser.getEventType();
            end:
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if (tag.equals("weather")) {
                        	time = parser.getAttributeValue(null, "year") + "/" + 
                        		    parser.getAttributeValue(null, "month") + "/" +
                        		    parser.getAttributeValue(null, "day") + "  " +
                        		    parser.getAttributeValue(null, "hour") + ":00";
                        }
                        
                        if (tag.equals("local")) {
                        	
                            String state = parser.getAttributeValue(null, "desc");
                            String temperature = "";
                            temperature += parser.getAttributeValue(null,"ta");
                            temperature += "";

                            bSet = true;
                            ItemTmp = temperature;
                            ItemWeather = state;
                            ItemRn = parser.getAttributeValue(null,"rn_hr1");
                            if(ItemRn == null) ItemRn = "0.0";
                            else if(ItemRn.toLowerCase().equals("null")) ItemRn = "0.0";

                        }
                        
                        break;

                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        if (bSet) {

                            String region = parser.getText();

                            if(region.equals(area)){
                            	EmbedBuilder eb = new EmbedBuilder();
                            	eb.setTitle(region + " ����");
                            	eb.setColor(Color.decode(BotMusicListener.colorDefault));
                            	eb.addField("���� �µ�", ItemTmp + " ��C", true);
                            	eb.addField("���� ����", ItemWeather, true);
                            	eb.addField("�Ϸ� ������", ItemRn + "mm", true);
                            	eb.setFooter(time + "  (from ���û)");
                            	

                            	HashMap<String, String> hm = new HashMap<>();
                            	hm.put(area, time);
                            	
	                            removeMessage(tc, userWeatherMessageId.get(hm), botWeatherMessageId.get(hm));
	                           
                            	userWeatherMessageId.put(hm, msg.getId());
                        		
                            	tc.sendMessage(eb.build()).queue(response -> {
                            		botWeatherMessageId.put(hm, response.getId());
                            	});
                                
                                bSet = false;
                                break end;
                            }
                        }
                        break;


                }
                eventType = parser.next();
            }
            
            if(bSet == true) {
            	removeMessage(tc, userNoneWeatherMessageId.get(area), botNoneWeatherMessageId.get(area));
        		userNoneWeatherMessageId.put(area, msg.getId());
        		
            	EmbedBuilder eb = new EmbedBuilder();
            	eb.setTitle("�ٸ� �������� �õ��� ������");
            	eb.setColor(Color.decode(BotMusicListener.colorDefault));
            	
            	tc.sendMessage(eb.build()).queue(response -> {
            		botNoneWeatherMessageId.put(area, response.getId());
            	});
            }


        } catch (Exception e) {
        	tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
        	e.printStackTrace();
        }
	}
	
	public void showInfo(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		
		infoUserMessageId = autoRemoveMessage(tc, msg, infoUserMessageId, infoBotMessageId);
		
		String language = "������ ���� ������ �������\n```\n����\n  * ä�� ����Ʈ���� ����: " + BotMusicListener.chatVersion + "\n  * ���� ����: " + BotMusicListener.musicVersion + "\n\n�����\n  * ��Ÿ ���: 2019�� 12�� 31��\n  * ���� ���: 2020�� 1�� 7��\n  * �ѱ� ���: 2022�� 2�� 1��\n\nDEVELOPED by Arrge (powered by JAVA)```����: https://discord.gg/KDstCT2\n";
		if(lan.equals("eng")) language = "Show version info\n```\nVersion\n  * version: " + BotMusicListener.musicVersion + "\n\nRelease info\n  * Beta: 2019.12.31\n  * Alpha: 2020.1.7\n  * Release date: 2020.2.27\n\nDEVELOPED by Arrge (powered by JAVA)```Contact: https://discord.gg/KDstCT2\n";
		tc.sendMessage(language).queue(response -> {
			infoBotMessageId = response.getId();
		});
		
    	System.out.println("BOT: ������ ���� ������ �������\n```����\n  * ä�� ����Ʈ���� ����: " + BotMusicListener.chatVersion + "\n  * ���� ����: " + BotMusicListener.musicVersion + "\n\n�����\n  * ��Ÿ ����: 2019�� 12�� 31��\n  * ���� ���: 2020�� 1�� 7��\n  * �ѱ� ���: 2022�� 2�� 1��\n\nDEVELOPED by Arrge (powered by JAVA)```����: https://discord.gg/KDstCT2\n");
        
	}
	
	public void addReaction(TextChannel tc, String msgId, ReactionEmote emote) {
		if(msgId.equals(helpBotMessageId)) {
			changeHelpPage(tc, msgId, emote);
		}
	}
	
	public void removeReaction(TextChannel tc, String msgId, ReactionEmote emote, Member member) {
		if(msgId.equals(helpBotMessageId)) {
			if(member.getId().equals(BotMusicListener.bot))
				removeMessage(tc, helpUserMessageId, helpBotMessageId);
			else
				changeHelpPage(tc, msgId, emote);
		}
	}
	
	public void removeAllReaction(TextChannel tc, String msgId) {
		if(msgId.equals(helpBotMessageId)) {
			removeMessage(tc, helpUserMessageId, helpBotMessageId);
		}
	}
	
	public void changeHelpPage(TextChannel tc, String msgId, ReactionEmote emote) {
		
		if(emote.getAsCodepoints().equals("U+2b05U+fe0f")) {
			helpPage = helpPage - 1;
			
			if(helpPage < 1) helpPage = 3;
			
			if(helpPage == 3 && helpLan.equals("eng"))
				helpPage = helpPage - 1;
			
		}
		else if(emote.getAsCodepoints().equals("U+27a1U+fe0f")) {
			helpPage = helpPage + 1;
			
			if(helpPage == 3 && helpLan.equals("eng"))
				helpPage = helpPage + 1;
			
			if(helpPage > 3) helpPage = 1;
		}
			
		if(helpPage == 1)
			helpPage1(tc, null, helpBotMessage, helpLan);
		
		else if(helpPage == 2)
			helpPage2(tc, helpBotMessage, helpLan);
		
		else if(helpPage == 3)
			helpPage3(tc, helpBotMessage, helpLan);
		
	}
	
	public void help(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		helpUserMessageId = autoRemoveMessage(tc, msg, helpUserMessageId, helpBotMessageId);
		helpUserMessage = msg;
		
		helpPage = 1;
		helpPage1(tc, msg, null, lan);
	}
	
	public void helpPage1(TextChannel tc, Message msg, Message response, String lan) {
	
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
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
		}
		
		String prefix = "��";
		if(helpUserMessage.getContentRaw().startsWith("$")) prefix = "$";

			
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.decode(BotMusicListener.colorDefault));		
		
		if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
			eb.setTitle("����+ ���� - ����");
		}
		else {
			eb.setTitle("���� ���� - ����");
		}
		
		if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.ADMINISTRATOR)) {
			eb.setAuthor("�����̿��� ������ ����� �ο��ϼ���");
        }
		
		String des = "��� ��ɾ� �տ��� �ѱ��� `��` �Ǵ� `$` �� �����ؿ�";
		helpLan = "kor";
		if(lan.equals("eng")) {
			des = "Prefix is `��` or `$`";
			helpLan = "eng";
		}
			
		eb.setDescription(des);
		
		String title1 = "����";
		if(lan.equals("eng"))
			title1 = "Info";
			
		String add = "";
		
		if(!BotMusicListener.prefix.equals("arrgeNull"))
			add = "\n-" + prefix + BotMusicListener.prefix;
		
		String value1 = "-" + prefix + "����: `��� ����ϴ��� �������` "
					+ "\n-" + prefix + "��: `�����ð��� �������` "
					+ "\n-" + prefix + "�ڽ���: `�ѱ��� �ڽ���, �̱� �޷��� �������` "
					+ "\n-" + prefix + "���� [����]: `���� ������ ������� (�� �̸��� ����)`" 
					+ "\n-" + prefix + "ä��: `����ä�� �� ������ �ִ��� �������` "
					+ "\n-" + prefix + "��������: `���� ������ �������`" 
					+ "\n-" + prefix + "����: `������ ���� ������ �˷����`" 
					+ "\n-" + prefix + "��Ÿ��: `���� �����ð��� �������`" 
					+ "\n-" + prefix + "��뷮: `�������� CPU, RAM ��뷮�� 30�ʰ� �������`" 
					+ "\n-" + prefix + "����: `�������� ���� ���¸� �������`"
					+ "\n-" + prefix + "�Ŀ�: `�Ŀ��� ������ �ּҸ� �˷����`" + add;
			
		if(lan.equals("eng"))
			value1 = "-" + prefix + "help: `showing how to use.` "
				 + "\n-" + prefix + "ping: `showing latency.` "
				 + "\n-" + prefix + "kospi: `showing KRX: KOSPI and 1 US Dollar.` "
				 + "\n-" + prefix + "channel: `showing Gunbam joined the voice channels count.` "
				 + "\n-" + prefix + "myinfo: `showing My info.`"
				 + "\n-" + prefix + "info: `showing Gunbam's info.`"
				 + "\n-" + prefix + "uptime: `showing Gunbam's active time.`"
				 + "\n-" + prefix + "cpu: `showing Gunbam's CPU, RAM usage during 30 seconds.`"
				 + "\n-" + prefix + "permission: `showing Gunbam have permissions`"
				 + "\n-" + prefix + "donate: `showing paypal url.";
	 
		eb.addField(title1, value1, false);
		
		if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
			String title4 = "�߰����";
			if(lan.equals("eng"))
				title4 = "Plus";
			
			String value4 = "-" + prefix + "�ٽ����: `������ ��ɾ�� �������� �ʱ�ȭ �Ǿ��� ���� ���� ��ɾ��`"
					   + "\n-" + prefix + "������: `" + prefix + "�ٽ�������� ����� �׸��� �������`"
					   + "\n-" + prefix + "����: `�ڱ⸸�� ���Ͽ� ���� �׸��� �����ؿ�`"
					   + "\n-" + prefix + "�ҷ�����: `�ڱ⸸�� ���Ͽ� �����߾��� �׸���� �ҷ��Ϳ�`";
			
			if(lan.equals("eng"))
				value4 = "-" + prefix + "playagain: `this command is when the playlist is initialized with the " + prefix + "stop command.`"
					 + "\n-" + prefix + "savelist: `showing items " + prefix + "playagain.`"
					 + "\n-" + prefix + "save: `save queue at your file.`"
					 + "\n-" + prefix + "load: `load saved items from your file.`";
		 
			eb.addField(title4, value4, false);
		}
		
		String footer = "����: " + BotMusicListener.musicVersion + "  |  " + tc.getJDA().getGuilds().size() + "�� ����";
		if(lan.equals("eng"))
			footer = "Version: " + BotMusicListener.musicVersionEng + "  |  " + tc.getJDA().getGuilds().size() + " servers";
		eb.setFooter(footer);
		
		if(response == null) {
			if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
				String language = "��ũ ÷��";
				if(lan.equals("eng"))
					language = "MESSAGE_EMBED_LINKS";
				
				tc.sendMessage(askPermission(language, lan)).queue(error -> {
					helpBotMessageId = error.getId();
				});
				log(tc, event, "BOT: " + language);
	        	return;
	        }
			
			if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_ADD_REACTION)) {
				showHelpOld(tc, msg, lan);
				
				return;
	        }
			
			tc.sendMessage(eb.build()).queue(responses -> {
				
				helpBotMessageId = responses.getId();
				helpBotMessage = responses;
				
				Runnable reaction = () -> {
					try {
						responses.addReaction("U+2b05U+fe0f").complete();
						responses.addReaction("U+27a1U+fe0f").complete();
					}
					catch(Exception e) {
						removeMessage(tc, helpUserMessageId, responses.getId());
					}
				};
				Thread t = new Thread(reaction);
				t.start();
				
			});
			System.out.println(des);
		}
		else {
			
			response.editMessage(eb.build()).queue();
			
		}	
	}
	
	public void helpPage2(TextChannel tc, Message response, String lan) {
		
		String prefix = "��";
		if(helpUserMessage.getContentRaw().startsWith("$")) prefix = "$";

			
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.decode(BotMusicListener.colorDefault));		
		
		if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
			eb.setTitle("����+ ���� - �÷��̾�");
		}
		else {
			eb.setTitle("���� ���� - �÷��̾�");
		}
		
		String title2 = "�÷��̾�";
		if(lan.equals("eng"))
			title2 = "Player";
		
		String value2 = "-" + prefix + "��� [URL|�ܾ�]: `URL�� ����ؿ� (https�� ��)`"
				+ "\n-" + prefix + "������� [������ URL]: `URL�� �������� ���� �� ����ؿ� (https�� ��)`"
				+ "\n-" + prefix + "�˻� [�ܾ�]: `��Ʃ�� �˻���� 7���� �������`"
				+ "\n-" + prefix + "����: `�÷��̾ �Ͻ������ؿ�`"
				+ "\n-" + prefix + "�簳: `�÷��̾ �簳�ؿ�`"
				+ "\n-" + prefix + "��ŵ [����]: `���� �׸����� �뷡�� �ǳʶپ��`"
				+ "\n-" + prefix + "����: `�������� �����`"
				+ "\n-" + prefix + "ť [����]: `�������� ������ 10���� ǥ���ؿ�`"
				+ "\n-" + prefix + "����: `���� �׸��� �ǽð����� �˷����`"
				+ "\n-" + prefix + "������: `����ä���� ������`";
		
		if(lan.equals("eng"))
			value2 = "-" + prefix + "play [URL|query]: `Playing URL. (Only https)`"
				 + "\n-" + prefix + "randomplay [Playlist URL]: `Playing URL links to a playlist after shuffle.`"
				 + "\n-" + prefix + "search [query]: `showing 7 items from Youtube search result.`"
				 + "\n-" + prefix + "pause: `pausing this player.`"
				 + "\n-" + prefix + "resume: `resuming this player.`"
				 + "\n-" + prefix + "skip [num]: `skipping to num.`"
				 + "\n-" + prefix + "shuffle: `shuffle this playlist.`"
				 + "\n-" + prefix + "queue [page]: `showing 10 items in queue.`"
				 + "\n-" + prefix + "nowplay: `showing now playing item.`"
				 + "\n-" + prefix + "stop: `leaving the voice channel.`";
	
		eb.addField(title2, value2, false);
		
		String title3 = "�÷��̾� ����";
		if(lan.equals("eng"))
			title3 = "Setting player";
			
		String value3 = "-" + prefix + "�̵� [����ä��id]: `�Է��� id�� �ش��ϴ� ����ä�η� �̵��ؿ�`"
					+ "\n-" + prefix + "Ÿ�̸� [����]: `�ڵ� ���� �ð��� �����ؿ�`"
					+ "\n-" + prefix + "������ [����]: `�Է��� ��°�� �׸��� ������ ����ä���� ������`"
					+ "\n-" + prefix + "���� [����]: `�÷��̾� ������ �����ؿ�`"
					+ "\n-" + prefix + "���� [����]: `���� �� �׸��� ��Ͽ��� �����ؿ�`"
					+ "\n-" + prefix + "���: `��� �� �۾��� �ǵ�����`";
			
		if(lan.equals("eng"))
			value3 = "-" + prefix + "move [voice channel id]: `moving to voice channel if id is vaild`"
				 + "\n-" + prefix + "timer [minute]: `setting aufo off.`"
				 + "\n-" + prefix + "last [num]: `when no.[num] item finished, it will leave the voice channel.`"
				 + "\n-" + prefix + "volume [num]: `Setting the player volume.`"
				 + "\n-" + prefix + "remove [num]: `removing no.[num] item.`"
				 + "\n-" + prefix + "cancel: `cancel.`";
		 
		eb.addField(title3, value3, false);
		
		
		String footer = "����: " + BotMusicListener.musicVersion + "  |  " + tc.getJDA().getGuilds().size() + "�� ����";
		if(lan.equals("eng"))
			footer = "Version: " + BotMusicListener.musicVersionEng + "  |  " + tc.getJDA().getGuilds().size() + " servers";
		eb.setFooter(footer);
	
		response.editMessage(eb.build()).queue();
		
	}
	
	public void helpPage3(TextChannel tc, Message response, String lan) {
	
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.decode(BotMusicListener.colorDefault));		
		
		if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
			eb.setTitle("����+ ���� - ����");
		}
		else {
			eb.setTitle("���� ���� - ����");
		}
		
		String title = "����";
		
		String value = "- ���� �ֻ���: `�ֻ��� 1���� ������`"
				   + "\n- ���� ����������: `�����̶� ������������ �ؿ�`"
				   + "\n- ���� ���������� ��Ƽ: `���� 2���̼� ������������ �ؿ�`";
					
	
		eb.addField(title, value, false);
		
		eb.addField("�ΰ����", "- ��¥����� �� �� �־��\n- �� ������ ����� �ý��� �ð��� �˷����", false);
		
		String footer = "����: " + BotMusicListener.musicVersion + "  |  " + tc.getJDA().getGuilds().size() + "�� ����";
		
		eb.setFooter(footer);
		
		response.editMessage(eb.build()).queue();
	
	}
	
	public void showHelpOld(TextChannel tc, Message msg, String lan) {
		helpUserMessageId = autoRemoveMessage(tc, msg, helpUserMessageId, helpBotMessageId);
		
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
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
		}
		
		String prefix = "��";
		if(msg.getContentRaw().startsWith("$")) prefix = "$";

			
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.decode(BotMusicListener.colorDefault));
		
		if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
			eb.setTitle("����+ ����");
		}
		else {
			eb.setTitle("���� ����");
		}
		
		if(!tc.getGuild().getSelfMember().hasPermission(tc, Permission.ADMINISTRATOR)) {
			eb.setAuthor("�����̿��� ������ ����� �ο��ϼ���");
        }
		
		String des = "��� ��ɾ� �տ��� �ѱ��� `��` �Ǵ� `$` �� �����ؿ�";
		helpLan = "kor";
		if(lan.equals("eng")) {
			des = "Prefix is `��` or `$`";
			helpLan = "eng";
		}
			
		eb.setDescription(des);
		
		String title1 = "����";
		if(lan.equals("eng"))
			title1 = "Info";
			
		String add = "";
		
		if(!BotMusicListener.prefix.equals("arrgeNull"))
			add = "\n-" + prefix + BotMusicListener.prefix;
		
		String value1 = "-" + prefix + "����: `��� ����ϴ��� �������` "
					+ "\n-" + prefix + "��: `�����ð��� �������` "
					+ "\n-" + prefix + "�ڽ���: `�ѱ��� �ڽ���, �̱� �޷��� �������` "
					+ "\n-" + prefix + "���� [����]: `���� ������ ������� (�� �̸��� ����)`" 
					+ "\n-" + prefix + "ä��: `����ä�� �� ������ �ִ��� �������` "
					+ "\n-" + prefix + "��������: `���� ������ �������`" 
					+ "\n-" + prefix + "����: `������ ���� ������ �˷����`" 
					+ "\n-" + prefix + "��Ÿ��: `���� �����ð��� �������`" 
					+ "\n-" + prefix + "��뷮: `�������� CPU, RAM ��뷮�� 30�ʰ� �������`" 
					+ "\n-" + prefix + "����: `�������� ���� ���¸� �������`"
					+ "\n-" + prefix + "�Ŀ�: `�Ŀ��� ������ �ּҸ� �˷����`" + add;
			
		if(lan.equals("eng"))
			value1 = "-" + prefix + "help: `showing how to use.` "
				 + "\n-" + prefix + "ping: `showing latency.` "
				 + "\n-" + prefix + "kospi: `showing KRX: KOSPI and 1 US Dollar.` "
				 + "\n-" + prefix + "channel: `showing Gunbam joined the voice channels count.` "
				 + "\n-" + prefix + "myinfo: `showing My info.`"
				 + "\n-" + prefix + "info: `showing Gunbam's info.`"
				 + "\n-" + prefix + "uptime: `showing Gunbam's active time.`"
				 + "\n-" + prefix + "cpu: `showing Gunbam's CPU, RAM usage during 30 seconds.`"
				 + "\n-" + prefix + "permission: `showing Gunbam have permissions`"
				 + "\n-" + prefix + "donate: `showing paypal url.";
	 
		eb.addField(title1, value1, false);
		
		String title2 = "�÷��̾�";
		if(lan.equals("eng"))
			title2 = "Player";
		
		String value2 = "-" + prefix + "��� [URL|�ܾ�]: `URL�� ����ؿ� (https�� ��)`"
				+ "\n-" + prefix + "������� [������ URL]: `URL�� �������� ���� �� ����ؿ� (https�� ��)`"
				+ "\n-" + prefix + "�˻� [�ܾ�]: `��Ʃ�� �˻���� 7���� �������`"
				+ "\n-" + prefix + "����: `�÷��̾ �Ͻ������ؿ�`"
				+ "\n-" + prefix + "�簳: `�÷��̾ �簳�ؿ�`"
				+ "\n-" + prefix + "��ŵ [����]: `���� �׸����� �뷡�� �ǳʶپ��`"
				+ "\n-" + prefix + "����: `�������� �����`"
				+ "\n-" + prefix + "ť [����]: `�������� ������ 10���� ǥ���ؿ�`"
				+ "\n-" + prefix + "����: `���� �׸��� �ǽð����� �˷����`"
				+ "\n-" + prefix + "������: `����ä���� ������`";
		
		if(lan.equals("eng"))
			value2 = "-" + prefix + "play [URL|query]: `Playing URL. (Only https)`"
				 + "\n-" + prefix + "randomplay [Playlist URL]: `Playing URL links to a playlist after shuffle.`"
				 + "\n-" + prefix + "search [query]: `showing 7 items from Youtube search result.`"
				 + "\n-" + prefix + "pause: `pausing this player.`"
				 + "\n-" + prefix + "resume: `resuming this player.`"
				 + "\n-" + prefix + "skip [num]: `skipping to num.`"
				 + "\n-" + prefix + "shuffle: `shuffle this playlist.`"
				 + "\n-" + prefix + "queue [page]: `showing 10 items in queue.`"
				 + "\n-" + prefix + "nowplay: `showing now playing item.`"
				 + "\n-" + prefix + "stop: `leaving the voice channel.`";
	
		eb.addField(title2, value2, false);
		
		String title3 = "�÷��̾� ����";
		if(lan.equals("eng"))
			title3 = "Setting player";
			
		
		String value3 = "-" + prefix + "�̵� [����ä��id]: `�Է��� id�� �ش��ϴ� ����ä�η� �̵��ؿ�`"
					+ "\n-" + prefix + "Ÿ�̸� [����]: `�ڵ� ���� �ð��� �����ؿ�`"
					+ "\n-" + prefix + "������ [����]: `�Է��� ��°�� �׸��� ������ ����ä���� ������`"
					+ "\n-" + prefix + "���� [����]: `�÷��̾� ������ �����ؿ�`"
					+ "\n-" + prefix + "���� [����]: `���� �� �׸��� ��Ͽ��� �����ؿ�`"
					+ "\n-" + prefix + "���: `��� �� �۾��� �ǵ�����`";
			
		if(lan.equals("eng"))
			value3 = "-" + prefix + "move [voice channel id]: `moving to voice channel if id is vaild`"
				 + "\n-" + prefix + "timer [minute]: `setting aufo off.`"
				 + "\n-" + prefix + "last [num]: `when no.[num] item finished, it will leave the voice channel.`"
				 + "\n-" + prefix + "volume [num]: `Setting the player volume.`"
				 + "\n-" + prefix + "remove [num]: `removing no.[num] item.`"
				 + "\n-" + prefix + "cancel: `cancel.`";
		 
		eb.addField(title3, value3, false);
		
		if(Main.saveQueueAllowGuild.contains(tc.getGuild().getId())) {
			String title4 = "�߰����";
			if(lan.equals("eng"))
				title4 = "Plus";
			
			String value4 = "-" + prefix + "�ٽ����: `������ ��ɾ�� �������� �ʱ�ȭ �Ǿ��� ���� ���� ��ɾ��`"
					   + "\n-" + prefix + "������: `" + prefix + "�ٽ�������� ����� �׸��� �������`"
					   + "\n-" + prefix + "����: `�ڱ⸸�� ���Ͽ� ���� �׸��� �����ؿ�`"
					   + "\n-" + prefix + "�ҷ�����: `�ڱ⸸�� ���Ͽ� �����߾��� �׸���� �ҷ��Ϳ�`";
			
			if(lan.equals("eng"))
				value4 = "-" + prefix + "playagain: `this command is when the playlist is initialized with the " + prefix + "stop command.`"
					 + "\n-" + prefix + "savelist: `showing items " + prefix + "playagain.`"
					 + "\n-" + prefix + "save: `save queue at your file.`"
					 + "\n-" + prefix + "load: `load saved items from your file.`";
		 
			eb.addField(title4, value4, false);
		}
		
		eb.addField("�ΰ����", "- ��¥����� �� �� �־��\n- �� ������ ����� �ý��� �ð��� �˷����", false);
		
		String footer = "����: " + BotMusicListener.musicVersion + "  |  " + tc.getJDA().getGuilds().size() + "�� ����";
		if(lan.equals("eng"))
			footer = "Version: " + BotMusicListener.musicVersionEng + "  |  " + tc.getJDA().getGuilds().size() + " servers";
		eb.setFooter(footer);
		
		tc.sendMessage(eb.build()).queue(response -> {
			helpBotMessageId = response.getId();
		});
		
	}
	
	public void showPermissions(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		permissionUserMessageId = autoRemoveMessage(tc, msg, permissionUserMessageId, permissionBotMessageId);
		
		List<Object> permission = new ArrayList<>();
		permission.addAll(tc.getGuild().getSelfMember().getPermissions());
		
		StringBuilder sb = new StringBuilder();
		
		String languageTitle = "������ ���� ���\n```css\n";
		if(lan.equals("eng"))
			languageTitle = "GunBam's permissions\n```css\n";
		sb.append(languageTitle);
		
		if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.ADMINISTRATOR)) {
			String language = "������: ��```";
			if(lan.equals("eng"))
				language = "ADMINISTRATOR: true```";
			
			sb.append(language);
		}
		else {
			String languageAd = "������: �ƴϿ�\n----------\n�ʼ� ���\n";
			if(lan.equals("eng"))
				languageAd = "ADMINISTRATOR: false\n----------\n�ʼ� ���\n";
			
			sb.append(languageAd);
			
			String languageVoice = "  ����ä�� ����: �ƴϿ� (�ο� �ʿ�)\n";
			if(lan.equals("eng"))
				languageVoice = "  VOICE_CONNECT: false (Need)\n";
			
			if(tc.getGuild().getSelfMember().hasPermission(Permission.VOICE_CONNECT)) {
				languageVoice = " - ����ä�� ����: ��\n";
				if(lan.equals("eng"))
					languageVoice = " - VOICE_CONNECT: true\n";
				
			}
			sb.append(languageVoice);
			
			String languageEmbed = " - ��ũ ÷��: �ƴϿ� (�ο� �ʿ�)\n";
			if(lan.equals("eng"))
				languageEmbed = " - MESSAGE_EMBED_LINKS: false (Need)\n";
			
			if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_EMBED_LINKS)) {
				languageEmbed = " - ��ũ ÷��: ��\n";
				if(lan.equals("eng"))
					languageEmbed = " - MESSAGE_EMBED_LINKS: true\n";
			}
			sb.append(languageEmbed);
			
			String languageReaction = " - ���� �ޱ�: �ƴϿ� (�ο� �ʿ�)\n";
			if(lan.equals("eng"))
				languageReaction = " - MESSAGE_ADD_REACTION: false (Need)\n";
			
			if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_ADD_REACTION)) {
				languageReaction = " - ���� �ޱ�: ��\n";
				if(lan.equals("eng"))
					languageReaction = " - MESSAGE_ADD_REACTION: true\n";
			}
			sb.append(languageReaction);
			
			String languageDelete = " - �޼��� ����: �ƴϿ� (�ο� �ʿ�)\n";
			if(lan.equals("eng"))
				languageDelete = " - MESSAGE_MANAGE: false (Need)\n";
			
			if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_MANAGE)) {
				languageDelete = " - �޼��� ����: ��\n";
				if(lan.equals("eng"))
					languageDelete = " - MESSAGE_MANAGE: true\n";
				
			}
			sb.append(languageDelete);
			
			sb.append("```");
		}
		
		tc.sendMessage(sb).queue(response -> {
			permissionBotMessageId = response.getId();
		});
		
		log(tc, event, "BOT: " + sb);
	}

	public void log(TextChannel tc, MessageReceivedEvent event, String str) {
		if(BotMusicListener.logOn == 1) {
			if(event.getAuthor().getId().equals(BotMusicListener.admin)||tc.getGuild().toString().contains(BotMusicListener.base)||tc.getId().equals("717203670365634621")) {}
			else BotMusicListener.logtc.sendMessage(str).queue();
		}
		
		File file = new File(BotMusicListener.directoryDefault + "log.txt");
		
		try {
		      FileWriter fw = new FileWriter(file, true);
		      fw.append("\n" + str);
		      
		      fw.close();
		} 
		catch (Exception e) {
		      e.printStackTrace();
		      tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
 			  log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
		}
	}
	
	
	public void reset(TextChannel tc) {
		newsUserMessageId = ""; newsBotMessageId = ""; newsBotMessage2Id = "";
		helpUserMessageId = ""; helpBotMessageId = "";
		kospiUserMessageId = ""; kospiBotMessageId = "";
		specUserMessageId = ""; specBotMessageId = "";
		uptimeUserMessageId = ""; uptimeBotMessageId = "";
	}
	
}
