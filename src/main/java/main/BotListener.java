package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class BotListener extends ListenerAdapter {
	
	int dobae = 0;
	int alert = 0;
	int proced = 0;
	int checkAllow = 0;
	int alreadyExist = 0;
	int checkAvailableRemove = 0;
		
	static int extend = 0;
	
	
	TextChannel general;
	List<Object> wordlist = new ArrayList<>();

	@Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User user = event.getAuthor();
        TextChannel tc = event.getTextChannel();
        Guild guild = event.getGuild();
        Message msg = event.getMessage();

        if(!event.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_WRITE)) {
        	return;
        }
        
        if(msg.getContentRaw().contains("����������")) return;
        
        checkAllow = 0;
        proced = 0;
        if(user.isBot()) return;
        	
		    File filea = new File(BotMusicListener.directoryDefault + "guild/wordAllow.txt");
    		try {
    			FileReader filereadera = new FileReader(filea);
    		       
    		    BufferedReader bufReadera = new BufferedReader(filereadera);
    		    String linea = "";
    		        
    		        
    		    while((linea = bufReadera.readLine()) != null){
    		    	if(linea.equals(guild.getId())) {
	    			    checkAllow = 1;
	    			    
	    		        if(msg.getContentRaw().contains("����") && (msg.getContentRaw().contains("�ϸ�")||msg.getContentRaw().contains("�̸�")||msg.getContentRaw().contains("��")||msg.getContentRaw().contains("��")) && (msg.getContentRaw().contains("��")||msg.getContentRaw().contains("���")||msg.getContentRaw().contains("����")||msg.getContentRaw().contains("�̾�")||msg.getContentRaw().contains("�̶���")||msg.getContentRaw().contains("��"))) {
	    		        	System.out.println("");
	    				    System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
	    				    if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
	    				    else BotMusicListener.logtc.sendMessage(".\n.\n" + guild.toString() + " `(" + tc.toString() + ")`" + "\n" + user.toString() + ": " + msg.getContentRaw()).queue();
	    				    
	    				    String checkGrammer = msg.getContentRaw().replaceAll("[^']", "");
	    				    if(checkGrammer.equals("''''")) {
		    				    String[] wordlearn = msg.getContentRaw().split("'");
		    		        			
		
		    		            StringBuilder s = new StringBuilder();
		    		            Runnable r = () -> {
			    		            File file2 = new File(BotMusicListener.directoryDefault + "guild/wordList.txt");
			    		        	try {
			    		        		FileReader filereader2 = new FileReader(file2);
			    		        		       
			    		        		BufferedReader bufReader2 = new BufferedReader(filereader2);
			    		        		String line2 = "";
			    		        		        
			    		        		int available = 0;
			    		        		alreadyExist = 0;
			    		        		String[] checkWord;
			    		        		while((line2 = bufReader2.readLine()) != null){
			    		        		    if(line2.startsWith(wordlearn[1])) {
			    		        		    	checkWord = line2.split("'");

			    		        		    	for(int i=0; i<checkWord.length; i++) {
			    		        		    		if(checkWord[i].equals(wordlearn[3])) {
			    		        		    			alreadyExist = 1;
			    		        		    		}
			    		        		    	}
			    		        		    	
			    		        		    	s.append(line2 + "'" + wordlearn[3] + "\n");
			    		        		        available = 1;
			    		        		    }
			    		        		    else {
			    		        		        s.append(line2 + "\n");
			    		        		    }
			    		        		        	
			    		        		}
			    		        		        
			    		        		if(available == 0) {
			    		        		    s.append(wordlearn[1] + "'" + wordlearn[3] + "\n");
			    		        		}
			    		        		
			    		        		else {
			    		        			if(alreadyExist == 1) {
			    		        				tc.sendMessage("�̹� ����ϰ� �־��").queue();
			    		        				if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
						    				    else
						    				    	BotMusicListener.logtc.sendMessage("BOT: �̹� ����ϰ� �־��").queue();
			    		        			}

			    		        		}
			    		        		        
			    		        		               
			    		        		bufReader2.close();
			    		        	}
			    		        	catch(Exception e) {
			    		        				
			    		        	}
		    		        			
			    		        	if(alreadyExist == 1) return;
			    		        	File file3 = new File(BotMusicListener.directoryDefault + "guild/wordList.txt");
			    		    		try {
			    		    			FileWriter fw = new FileWriter(file3);
			    		    			fw.write(s.toString());
			    		    			tc.sendMessage("�˰ھ�� (`" + wordlearn[1] + "`)").queue();
			    		    			System.out.println("BOT: �˰ھ�� (`" + wordlearn[1] + "`)");
			    		    		
			    		    			if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
				    				    else
				    				    	BotMusicListener.logtc.sendMessage("BOT: �˰ھ�� (`" + wordlearn[1] + "`)").queue();
			    		    			fw.close();
			    		    		}
			    		    		catch (IOException e) {
			    		    			e.printStackTrace();
			    		    			tc.sendMessage("������ ���� �߰����� ���߾�� `(" + e.getMessage() + ")`").queue();
			    		    			if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
				    				    else
				    				    	BotMusicListener.logtc.sendMessage("BOT: ������ ���� �߰����� ���߾�� `(" + e.getMessage() + ")`").queue();
			    		    		}
		    		            };
		    		            Thread t = new Thread(r);
		    		            t.start();
		   
		    		            proced = 1;
	    				    }
	    				    else {
	    				    	
	    				    }
	    		        }
	    				        
	    				else if(msg.getContentRaw().contains("����") && (msg.getContentRaw().contains("�ؾ�")||msg.getContentRaw().contains("����")||msg.getContentRaw().contains("����"))) {
	    				    System.out.println("");
	    				    System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
	    				    if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
	    				    else
	    				    	BotMusicListener.logtc.sendMessage(".\n.\n" + guild.toString() + " `(" + tc.toString() + ")`" + "\n" + user.toString() + ": " + msg.getContentRaw()).queue();
	    				        	
	    				    String checkGrammer = msg.getContentRaw().replaceAll("[^']", "");
	    				   
	    				    if(checkGrammer.equals("''")) {
		    				    String[] wordlearn = msg.getContentRaw().split("'");
		    				        	
		    				    StringBuilder s = new StringBuilder();
		    				    checkAvailableRemove = 0;
		    				    
		    		            Runnable r = () -> {
			    		            File file2 = new File(BotMusicListener.directoryDefault + "guild/wordList.txt");
			    		        	try {
			    		        		FileReader filereader2 = new FileReader(file2);
			    		        		       
			    		        		BufferedReader bufReader2 = new BufferedReader(filereader2);
			    		        		String line2 = "";
	
			    		        		while((line2 = bufReader2.readLine()) != null){
			    		        		        	
			    		        		    if(line2.startsWith(wordlearn[1])) {
			    		        		    	checkAvailableRemove = 1;
			    		        		    }
			    		        		    else s.append(line2 + "\n");
			    		        		}
			    		        		      
			    		        		               
			    		        		bufReader2.close();
			    		        	}
			    		        	catch(Exception e) {
			    		        				
			    		        	}
			    		        	if(checkAvailableRemove == 1) {
				    		        	File file3 = new File(BotMusicListener.directoryDefault + "guild/wordList.txt");
				    		    		try {
				    		    			FileWriter fw = new FileWriter(file3);
				    		    			fw.write(s.toString());
				    		    			tc.sendMessage("**" + wordlearn[1] + "**�� ���� �͵��� ��� �ؾ����").queue();
				    		    			System.out.println("BOT: **" + wordlearn[1] + "**�� ���� �͵��� ��� �ؾ����");
				    		    			if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
					    				    else
					    				    	BotMusicListener.logtc.sendMessage("BOT: **" + wordlearn[1] + "**�� ���� �͵��� ��� �ؾ����").queue();
				    		    			fw.close();
				    		    		}
				    		    		catch (IOException e) {
				    		    			e.printStackTrace();
				    		    			tc.sendMessage("������ ����  ������ ���߾�� `(" + e.getMessage() + ")`").queue();
				    		    			System.out.println("BOT: ������ ����  ������ ���߾�� `(" + e.getMessage() + ")`");
				    		    			
				    		    			if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
					    				    else tc.sendMessage("BOT: ������ ����  ������ ���߾�� `(" + e.getMessage() + ")`").queue();
				    		    		}
			    		        	}
			    		        	
			    		        	else {
			    		        		Random ran = new Random();
			    		        		int ranint = ran.nextInt(2);
			    		        		
			    		        		if(ranint == 0) {
			    		        			tc.sendMessage("`" + wordlearn[1] + "` ��� �ܾ�� �����").queue();
			    		        			BotMusicListener.logtc.sendMessage("`" + wordlearn[1] + "` ��� �ܾ�� �����").queue();
			    		        		}
			    		        		
			    		        		
			    		        		
			    		        		else if(ranint == 1) {
			    		        			tc.sendMessage("`" + wordlearn[1] + "` ��� �ܾ�� ��￡ �����").queue();
			    		        			BotMusicListener.logtc.sendMessage("`" + wordlearn[1] + "` ��� �ܾ�� ��￡ �����").queue();
			    		        		}
			    		        	}
		    		            
		    		           
		    		            };
		    		            		
		    		            Thread t = new Thread(r);
		    		            t.start();
		
		    		            proced = 1;
	    				    }
	    		            
	    		            else {
	    		            	
	    		            }
	    		            
	    		        }
	   
	    		        	
	    				else {   	
	    					Runnable r1 = () -> {
	    				        		
		    					File file = new File(BotMusicListener.directoryDefault + "guild/wordList.txt");
		    			
		    					try{   
		    					    FileReader filereader = new FileReader(file);
		    					           
		    					    BufferedReader bufReader = new BufferedReader(filereader);
		    					            
		    					    wordlist.clear();
		    					    String line = "";
		    					    while((line = bufReader.readLine()) != null){
		    					    	String[] word = line.split("'");
		    					        if(msg.getContentRaw().contains("����") && msg.getContentRaw().contains(word[0])) {
		    					        	System.out.println("");
		    		    				    System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		    		    				    if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
					    				    else 
					    				    	BotMusicListener.logtc.sendMessage(".\n.\n" + guild.toString() + " `(" + tc.toString() + ")`" + "\n" + user.toString() + ": " + msg.getContentRaw()).queue();
					    				    	
		    					            for(int i = 1; i<word.length; i++) {
		    					            	wordlist.add(word[i]);
		    					            }
		    					            		
		    					            Random r = new Random();
		    					            int ir = r.nextInt(word.length - 1);
		    					            		
		    					            tc.sendMessage("`" + wordlist.get(ir) + "`").queue();
		    					            System.out.println("BOT: `" + wordlist.get(ir) + "`");
		    					            
		    					            if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
					    				    else BotMusicListener.logtc.sendMessage("BOT: `" + wordlist.get(ir) + "`").queue();
		    					            
		    					            proced = 1;
		
		    					        }
		    					       
		    					    }
		    					            
		    					    if(proced == 0) {
		    					    	oldChat(tc, user, guild, msg);
		    					    }
		    					 
		    					    bufReader.close();
		    					            
		    					}
	
		    					catch(IOException e){
		    					    System.out.println(e); 
		    					    BotMusicListener.logtc.sendMessage(":no_entry_sign: (guild/wordList.txt) **" + e.getMessage()).queue();
		    					}
		    					        
		    					        
	    					};
	    				    Thread t = new Thread(r1);
	    				    t.start();
	
	    				}

	    		    }
 	
    		    }
          
    		    bufReadera.close();
    		}
    		catch(Exception e) {
    				
    		}
   
		    if(checkAllow == 0)
		    	oldChat(tc, user, guild, msg);
		}
		        
		public void oldChat(TextChannel tc, User user, Guild guild, Message msg) {
			if(guild.getId().equals("374071874222686211") || guild.toString().contains("264445053596991498") || tc.toString().contains("553254281495576577") || msg.getContentRaw().contains("��") || msg.getContentRaw().contains("����")) {general = tc;}
	 		else {
	 			if(extend == 1) {
	 				Extend.plusCommunication(tc, user, msg, guild);
		        	extend = 0;
		        }
			   
		        if(msg.toString().equals("����")||msg.toString().contentEquals("�����")) {
		        	tc.sendMessage("��").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: ��");
		        }
		        
		        /*
		        else if(user.toString().contains("����") && tc.toString().contains("648495662803058696")&&msg.getContentRaw().contains("����")&&msg.getContentRaw().contains("����")){
		        	tc.sendMessage("��������!").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: ��������!");
		        	
		        }
		        */
 
		        else if(user.toString().contains("!") && tc.toString().contains("648495662803058696")&&msg.getContentRaw().contains("����")&&msg.getContentRaw().contains("����")){
		        	tc.sendMessage("��������!").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: ��������!");
		        }
		        
		        else if(tc.toString().contains("648495662803058696")&&msg.getContentRaw().contains("����")&&msg.getContentRaw().contains("����")){
		        	tc.sendMessage("�÷�").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: �÷�");
		        }

		        else if(msg.getContentRaw().contains("����")&&msg.getContentRaw().contains("����")){
		        	tc.sendMessage("���⼭ �׷�������").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: ���⼭ �׷�������");
		        }
		        
		        else if(msg.getContentRaw().equals("����")&& user.toString().contains("297963786504110083")) {
		        	tc.sendMessage("����־��").queue();
		        	System.out.println("BOT: ����־��");     	
		        }
		        
		        else if(msg.getContentRaw().contains("����")&&msg.getContentRaw().contains("�ȳ�")){
		        	tc.sendMessage("�ȴ�").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: �ȴ�");
		            
		        }
		     
		       
		        
		        else if(msg.getContentRaw().contains("����")&&msg.getContentRaw().contains("����")) {
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
					Random r = new Random();
					int i = r.nextInt(3);
					if(i == 0) {
						tc.sendMessage("�۶�����").queue();
						System.out.println("BOT: �۶�����");
					}
					
					else if(i == 1) {
						tc.sendMessage("�ڴ���").queue();
						System.out.println("BOT: �ڴ���");
					}
					
					else if(i == 2) {
						tc.sendMessage("�忡").queue();
						System.out.println("BOT: �忡");
					}
					
					else {
						tc.sendMessage("���ſ�").queue();
						System.out.println("BOT: ���ſ�");
					}
				
				}
		        
		        else if(msg.getContentRaw().contains("����")&&(msg.getContentRaw().contains("�߰�")||msg.getContentRaw().contains("�ٹ�")||msg.getContentRaw().contains("����"))) {
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
					Random r = new Random();
					int i = r.nextInt(3);
					if(i == 0) {
						tc.sendMessage("�ٹ�").queue();
						System.out.println("BOT: �ٹ�");
					}
					
					else if(i == 1) {
						tc.sendMessage("�÷�").queue();
						System.out.println("BOT: �÷�");
					}
					
					else if(i == 2) {
						tc.sendMessage("�Ǻ���").queue();
						System.out.println("BOT: �Ǻ���");
					}
					
					else {
						tc.sendMessage("�ȴ�").queue();
						System.out.println("BOT: �ȴ�");
					}
				
				}
		        
		        else if(msg.getContentRaw().contains("����") && (msg.getContentRaw().contains("�ɽ���") || msg.getContentRaw().contains("����") || msg.getContentRaw().contains("���"))) {
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
					Random r = new Random();
					int i = r.nextInt(3);
					if(i == 0) {
						tc.sendMessage("�÷���").queue();
						System.out.println("BOT: �÷���");
					}
					
					else if(i == 1) {
						tc.sendMessage("ȥ�ڳ�ƿ�").queue();
						System.out.println("BOT: ȥ�ڳ�ƿ�");
					}
					
					else if(i == 2) {
						tc.sendMessage("��� ��ƿ�").queue();
						System.out.println("BOT: ��� ��ƿ�");
					}
					
					else {
						tc.sendMessage("��..").queue();
						System.out.println("BOT: ��..");
					}
				
				}
		        
		        else if(msg.getContentRaw().contains("����")&&(msg.getContentRaw().contains("����")||msg.getContentRaw().contains("���ڳ���")||msg.getContentRaw().contains("���ڳٳ�"))){
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
					int i = r.nextInt(3);
					if(i == 0) {
						tc.sendMessage("Zzz").queue();
						System.out.println("BOT: Zzz");
					}
					
					else if(i == 1) {
						tc.sendMessage("������").queue();
						System.out.println("BOT: ������");
					}
					
					else if(i == 2) {
						tc.sendMessage("�ڿ��ڿ�").queue();
						System.out.println("BOT: �ڿ��ڿ�");
					}
					
					else {
						tc.sendMessage("�ڱ�÷�").queue();
						System.out.println("BOT: �ڱ�÷�");
					}
		        }
		        
		        else if(msg.getContentRaw().contains("����")&&msg.getContentRaw().contains("����")){
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
		        	int i = r.nextInt(3);
		        	if(i == 0) {
		        		tc.sendMessage("..?").queue();
		        		System.out.println("BOT: ..?");
		        	}
		        	
		        	else if(i == 1) {
		        		tc.sendMessage("!!").queue();
		        		System.out.println("BOT: !!");
		        	}
		        	
		        	else if(i == 2) {
		        		tc.sendMessage("��!").queue();
		        		System.out.println("BOT: ��!");
		        	}
		        	
		        	else {
		        		tc.sendMessage("�ȵž�").queue();
		        		System.out.println("BOT: �ȵž�");
		        		
		        	}
	    
		        }
		        
		        else if(msg.getContentRaw().contains("����")||msg.getContentRaw().contains("�ù�")||msg.getContentRaw().contains("����")){
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
		        	int i = r.nextInt(3);
		        	if(i == 0) {
		        		tc.sendMessage("��.��").queue();
		        		System.out.println("BOT: ��.��");
		        	}
		        	
		        	else if(i == 1) {
		        		tc.sendMessage("�Ѥ�").queue();
		        		System.out.println("BOT: �Ѥ�");
		        	}
		        	
		        	else if(i == 2) {
		        		tc.sendMessage("..").queue();
		        		System.out.println("BOT: ..");
		        	}
		        	
		        	else {
		        		tc.sendMessage("��,��").queue();
		        		System.out.println("BOT: ��,��");
		        		
		        	}
	
		        }
		        
		        else if(msg.getContentRaw().contains("����")&&(msg.getContentRaw().contains("��û")||msg.getContentRaw().contains("�ٺ�")||msg.getContentRaw().contains("����")||msg.getContentRaw().contains("���")||msg.getContentRaw().contains("�÷�"))){
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
		        		dobae = dobae + 1;
			        	Random r = new Random();
			        	int i = r.nextInt(4);
			        	
			        	if(i == 0) {
			        		tc.sendMessage("�ФФФФ�").queue();
			        		System.out.println("BOT: �ФФФФ�");
			        	}
			        	
			        	if(i == 1) {
			        		tc.sendMessage("������..").queue();
			        		System.out.println("BOT: ������..");
			        	}
			        	
			        	if(i == 2) {
			        		tc.sendMessage("�忡�� ��").queue();
			        		System.out.println("BOT: �忡�� ��");
			        	}
			        	
			        	else {
			        		tc.sendMessage("�ƴϾ�!").queue();
			        		System.out.println("BOT: �ƴϾ�!");
			        	}
			       
			        }
		        }
		        
		        else if(msg.getContentRaw().contains("����")&&(msg.getContentRaw().contains("����")||msg.getContentRaw().contains("���"))){
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
		        	int i = r.nextInt(3);
		        	
		        	if(i == 0) {
		        		tc.sendMessage("1��!").queue();
		        		System.out.println("BOT: 1��!");
		        	}
		        	
		        	if(i == 1) {
		        		tc.sendMessage("1��! �ֱ⿡��").queue();
		        		System.out.println("BOT: 1��! �ֱ⿡��");
		        	}
		        	
		        	if(i == 2) {
		        		tc.sendMessage("1���̿���").queue();
		        		System.out.println("BOT: 1���̿���");
		        	}
		        	
		        	else {
		        		tc.sendMessage("����").queue();
		        		System.out.println("BOT: ����");
		        	}
		        	
		        }

		        else if(msg.getContentRaw().contains("����")&& msg.getContentRaw().contains(" ")){
		        
		        }
		        
		        else if(msg.getContentRaw().contains("����")){
		        	if(msg.getContentRaw().contains("=") || msg.getContentRaw().contains("����") || msg.getContentRaw().contains("����") || msg.getContentRaw().contains("����"))return;
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
		        		dobae = dobae + 1;
			        	Random r = new Random();
			        	int i = r.nextInt(7);
			        	
			        	if(i == 0) {
			        		tc.sendMessage("���ƾ�").queue();
			        		
			            	System.out.println("BOT: ���ƾ�");
			        	}
			        	else if(i == 1) {
			        		tc.sendMessage("��").queue();
			        		
			            	System.out.println("BOT: ��");
			            	
			            	extend = 1;
			        	}
			        	else if(i == 2) {
			        		tc.sendMessage("�� �ֿ�").queue();
			        		
			            	System.out.println("BOT: �� �ֿ�");
			            	
			            	extend = 1;
			        		
			        	}
			        	else if(i == 3) {
			        		tc.sendMessage("�� ��").queue();
			        		
			            	System.out.println("BOT: �� ��");
			            	
			            	extend = 1;
			        	}
			        	
		        	}
		        	
		            
		        }
		        
		        /*
		        else if(user.toString().contains(BotMusicListener.chanha) && msg.getContentRaw().contains("���ϴ�")) {
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
			        	Random r = new Random();
			        	int i = r.nextInt(10);
			        	if(i == 0) {
			        		tc.sendMessage("�ν� ��").queue();
			        		
			        		System.out.println("");
			        		System.out.println(user.toString() + ": " + msg.getContentRaw());
			            	System.out.println("BOT: �ν�");
			        	}

			        	
			        	else{
			        		tc.sendMessage("�ƽδ�").queue();
			        		
			        		System.out.println("");
			        		System.out.println(user.toString() + ": " + msg.getContentRaw());
			            	System.out.println("BOT: �ƽδ�");
			        	}
		        	}
		        }
		        /*
		        else if(user.toString().contains("ȣ��") && msg.getContentRaw().contains("ȣ�δ�")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
			        	System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
			        	Random r = new Random();
			        	int i = r.nextInt(10);
			        	if(i == 0) {
			        		tc.sendMessage("�Ϳ���!").queue();
			        		System.out.println("BOT: �Ϳ���!");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("�Ϳ���").queue();
			        		System.out.println("BOT: �Ϳ���");
			        	}
			        	
			        	else if(i == 2) {
			        		tc.sendMessage("�����ν�!").queue();
			        		System.out.println("BOT: �����ν�!");
			        
			        	}
			        	
			        	else if(i >=3 && i<=6) {
			        		tc.sendMessage("�����ν�").queue();
			        		System.out.println("BOT: �����ν�");
			        	}
			       
			        	else{
			        		tc.sendMessage("�ٿ���").queue();
			        		System.out.println("BOT: �ٿ���");
			        	}
		        	}
		        	
		        	
		        }
		        
		        else if(msg.getContentRaw().contains("������")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
			        	System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
			        	Random r = new Random();
			        	int i = r.nextInt(10);
			        	if(i == 0) {
			        		tc.sendMessage("����").queue();
			        		System.out.println("BOT: ����");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("����!").queue();
			        		System.out.println("BOT: ����!");
			        	}
			        	
			        	else if(i == 2 || i == 3) {
			        		tc.sendMessage("�����ν�").queue();
			        		System.out.println("BOT: �����ν�");
			        	}
			     
			        	else{
			        		tc.sendMessage("�ܽ���!").queue();
			        		System.out.println("BOT: �ܽ���!");
			        	}
		        	}
		        }	
		        
		        else if(msg.getContentRaw().contains("��ũ��")||msg.getContentRaw().contains("ȣ�δ�")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
			        	System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
			        	Random r = new Random();
			        	int i = r.nextInt(10);
			        	if(i == 0) {
			        		tc.sendMessage("�Ϳ���!").queue();
			        		System.out.println("BOT: �Ϳ���!");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("�Ϳ���").queue();
			        		System.out.println("BOT: �Ϳ���");
			        	}
			        	
			        	else if(i >= 2 && i<=3) {
			        		tc.sendMessage("�����ν�").queue();
			        		System.out.println("BOT: �����ν�");
			        	}
			
			        	else{
			        		tc.sendMessage("�ٿ���").queue();
			        		System.out.println("BOT: �ٿ���");
			        	}
		        	}
		        }
		        */
		        
		        else if(msg.getContentRaw().contains("����")&&(msg.getContentRaw().contains("��"))){
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	tc.sendMessage("��..").queue();
		        	System.out.println("BOT: ��..");
		        }
		        
		        else if(msg.getContentRaw().contains("��")&&msg.getContentRaw().contains(" ")) {
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: �������� �ʾҽ��ϴ�");
		        }
		        
		        else if((msg.getContentRaw().contains("����")&&(msg.getContentRaw().contains("��")||msg.getContentRaw().contains("�ù�")||msg.getContentRaw().contains("������")))) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
		        		dobae = dobae + 1;
			        	System.out.println("");
			        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
			        	
			        	Random r = new Random();
			        	int i = r.nextInt(10);
			        	if(i == 0) {
			        		tc.sendMessage("�ФФФФп��߾�").queue();
			        		System.out.println("BOT: �ФФФФп��߾�");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("���߾��ä�").queue();
			        		System.out.println("BOT: ���߾�ä�");
			        	}
			        	
			        	else if(i >= 2 && i<=3) {
			        		tc.sendMessage("�ФФФг���").queue();
			        		System.out.println("BOT: �ФФФг���");
			        	}
			        	
			        	else if(i >= 4 && i<=7) {
			        		tc.sendMessage("���Ͼ� ���ؽ�ФФ�").queue();
			        		System.out.println("BOT: ���Ͼ� ���ؽ�ФФ�");
			        	}
			
			        	else{
			        		tc.sendMessage("���߾�äФФ�").queue();
			        		System.out.println("BOT: ���߾�äФФ�");
			        	}
		        	}
		        }
		        
		        
		        /*
		        else if(msg.getContentRaw().contains(BotMusicListener.chanha) && (msg.getContentRaw().contains("����") || msg.getContentRaw().contains("���"))) {
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
		        	int i = r.nextInt(10);
		        	if(i == 0) {
		        		tc.sendMessage("20��").queue();
		        		tc.sendMessage("!").queue();
		        		System.out.println("BOT: 20��!");
		        		
		        		MessageHistory mh = new MessageHistory(tc);
			            List<Message> msgs = mh.retrievePast(3).complete();
			            tc.deleteMessages(msgs).complete();
		        		
		        	}
		        	
		        	if(i >= 1 && i<= 4) {
		        		tc.sendMessage("������").queue();
		        		System.out.println("BOT: ������");
		        	}
		        	
		        	if(i >= 5 && i<= 7) {
		        		tc.sendMessage("9��!").queue();
		        		System.out.println("BOT: 9��!");
		        	}
		        	
		        	else {
		        		System.out.println(user.toString() + ": " + msg.getContentRaw());
		        		System.out.println("BOT: 9���̷�");
		        	}

		        }
		        
		        else if(msg.getContentRaw().contains(BotMusicListener.chanha) &&msg.getContentRaw().contains("����")) {
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
		        	int i = r.nextInt(10);
		        	if(i == 0) {
		        		tc.sendMessage("6�� 13��!").queue();
		        		tc.sendMessage("!").queue();
		        		System.out.println("BOT: 6�� 13��!");
		        		
		        		MessageHistory mh = new MessageHistory(tc);
			            List<Message> msgs = mh.retrievePast(3).complete();
			            tc.deleteMessages(msgs).complete();
		        		
		        	}
		        	
		        	if(i >= 1 && i<= 4) {
		        		tc.sendMessage("13��..").queue();
		        		System.out.println("BOT: 13��..");
		        	}
		        	
		        	if(i >= 5 && i<= 7) {
		        		tc.sendMessage("13�� 32��!").queue();
		        		System.out.println("BOT: 13�� 32��!");
		        	}
		        	
		        	else {
		        		System.out.println(user.toString() + ": " + msg.getContentRaw());
		        		System.out.println("BOT: 13���̷� ���");
		        	}

		        }
		        
		        else if(msg.getContentRaw().contains(BotMusicListener.chanha) && (msg.getContentRaw().contains(" ") || msg.getContentRaw().contains("��") || msg.getContentRaw().contains("��"))) {
		        	
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
	        		System.out.println("BOT: �������� �ʾҽ��ϴ�");
		        }
		        
		        
		        else if(msg.getContentRaw().contains("���ϴ�")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
		        		System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
		        		Random r = new Random();
			        	int i = r.nextInt(10);
			        	
			        	if(i == 0) {
			        		tc.sendMessage("�ν�!").queue();
			        		System.out.println("BOT: �ν�!");
			        	}

			        	else if(i == 1) {
			        		tc.sendMessage("��").queue();
			        		System.out.println("BOT: ��");
			        	}
			        	
			        	else {
			        		tc.sendMessage("�ƽδ�").queue();
			        		System.out.println("BOT: �ƽδ�");
			        	}

		        	}
		        }
		        
		        /*
		        else if(msg.getContentRaw().contains("����") && (msg.getContentRaw().contains(" ") || msg.getContentRaw().contains("��") || msg.getContentRaw().contains("��"))) {
		        	
		        	
		        	System.out.println("");
		        	System.out.println(user.toString() + ": " + msg.getContentRaw());
	        		System.out.println("BOT: �������� �ʾҽ��ϴ�");
		        }
		        
		        else if(msg.getContentRaw().contains("������")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
		        		System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
		        		Random r = new Random();
			        	int i = r.nextInt(10);
			        	
			        	if(i == 0) {
			        		tc.sendMessage("������...").queue();
			        		System.out.println("BOT: ������...");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("������").queue();
			        		System.out.println("BOT: ������");
			        	}
			        	else if(i == 2) {
			        		tc.sendMessage("������").queue();
			        		System.out.println("BOT: ������");}
			        	else if(i == 3) {
			        		tc.sendMessage("��").queue();
			        		System.out.println("BOT: ��");
			        	}
			        	else {
			        		tc.sendMessage("������").queue();
			        		System.out.println("BOT: ������");
			        	}

		        	}
		        }
		        
		        else if(msg.getContentRaw().contains("����") && (msg.getContentRaw().contains(" ") || msg.getContentRaw().contains("��") || msg.getContentRaw().contains("��"))) {
		        	
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
	        		System.out.println("BOT: �������� �ʾҽ��ϴ�");
		        }
		        
		        
		        else if(msg.getContentRaw().contains("������")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
		        		System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
		        		Random r = new Random();
			        	int i = r.nextInt(10);
			        	
			        	if(i == 0) {
			        		tc.sendMessage("�����").queue();
			        		System.out.println("BOT: �����");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("����������").queue();
			        		System.out.println("BOT: ����������");
			        	}
			        	else if(i == 2) {
			        		tc.sendMessage("����").queue();
			        		System.out.println("BOT: ����");}
			        	else if(i == 3) {
			        		tc.sendMessage("��").queue();
			        		System.out.println("BOT: ��");
			        	}
			        	
			        	else if(i == 4) {
			        		tc.sendMessage("�ːθ�").queue();
			        		System.out.println("BOT: �ːθ�");
			        	}
			        	
			        	else if(i == 5) {
			        		tc.sendMessage("�����̾�").queue();
			        		System.out.println("BOT: �����̾�");
			        	}
			        	else if(i == 6) {
			        		tc.sendMessage("��").queue();
			        		System.out.println("BOT: ��");
			        	}
			        	else if(i == 7) {
			        		tc.sendMessage("��������").queue();
			        		System.out.println("BOT: ��������");
			        	}
			        	else if(i == 8) {
			        		
			        		tc.sendMessage("�ːΐ��̾�").queue();
			        		System.out.println("BOT: �ːΐ��̾�");
			        		
			        	}
			        	else {
			        		tc.sendMessage("").queue();
			        		System.out.println("BOT: ");
			        	}

		        	}
		        }
		        
		        */
		        
		       
	        }
	}
        
    

	public static boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e) {return false;}
		
	}

	public void dobaeFunc(TextChannel tc, User user, Message msg) {
		
		Random r = new Random();
		int i = r.nextInt(2);
		if(alert == 0 && i == 0) {
			tc.sendMessage("�ڲ� �θ�����").queue();
			
			System.out.println("");
			System.out.println(user.toString() + ": " + msg.getContentRaw());
	    	System.out.println("BOT: �ڲ� �θ�����");
		}
		else if(alert == 0 && i == 1) {
			tc.sendMessage("���� �̵��� �ҷ���").queue();
			
			System.out.println("");
			System.out.println(user.toString() + ": " + msg.getContentRaw());
	    	System.out.println("BOT: ���� �̵��� �ҷ���");
		}     
		
		SimpleDateFormat time = new SimpleDateFormat("ss");
		String timeStr = time.format (System.currentTimeMillis());
		alert = 1;
		
		if(timeStr.contains("59")) {dobae = 0;alert = 0;}
		if(timeStr.contains("29")) {dobae = 0;alert = 0;}

    }
	
	
}
