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
        
        if(msg.getContentRaw().contains("가위바위보")) return;
        
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
	    			    
	    		        if(msg.getContentRaw().contains("군밤") && (msg.getContentRaw().contains("하면")||msg.getContentRaw().contains("이면")||msg.getContentRaw().contains("은")||msg.getContentRaw().contains("는")) && (msg.getContentRaw().contains("야")||msg.getContentRaw().contains("라고")||msg.getContentRaw().contains("란다")||msg.getContentRaw().contains("이야")||msg.getContentRaw().contains("이란다")||msg.getContentRaw().contains("해"))) {
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
			    		        				tc.sendMessage("이미 기억하고 있어요").queue();
			    		        				if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
						    				    else
						    				    	BotMusicListener.logtc.sendMessage("BOT: 이미 기억하고 있어요").queue();
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
			    		    			tc.sendMessage("알겠어요 (`" + wordlearn[1] + "`)").queue();
			    		    			System.out.println("BOT: 알겠어요 (`" + wordlearn[1] + "`)");
			    		    		
			    		    			if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
				    				    else
				    				    	BotMusicListener.logtc.sendMessage("BOT: 알겠어요 (`" + wordlearn[1] + "`)").queue();
			    		    			fw.close();
			    		    		}
			    		    		catch (IOException e) {
			    		    			e.printStackTrace();
			    		    			tc.sendMessage("문제가 생겨 추가하지 못했어요 `(" + e.getMessage() + ")`").queue();
			    		    			if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
				    				    else
				    				    	BotMusicListener.logtc.sendMessage("BOT: 문제가 생겨 추가하지 못했어요 `(" + e.getMessage() + ")`").queue();
			    		    		}
		    		            };
		    		            Thread t = new Thread(r);
		    		            t.start();
		   
		    		            proced = 1;
	    				    }
	    				    else {
	    				    	
	    				    }
	    		        }
	    				        
	    				else if(msg.getContentRaw().contains("군밤") && (msg.getContentRaw().contains("잊어")||msg.getContentRaw().contains("지워")||msg.getContentRaw().contains("없애"))) {
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
				    		    			tc.sendMessage("**" + wordlearn[1] + "**에 관한 것들을 모두 잊었어요").queue();
				    		    			System.out.println("BOT: **" + wordlearn[1] + "**에 관한 것들을 모두 잊었어요");
				    		    			if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
					    				    else
					    				    	BotMusicListener.logtc.sendMessage("BOT: **" + wordlearn[1] + "**에 관한 것들을 모두 잊었어요").queue();
				    		    			fw.close();
				    		    		}
				    		    		catch (IOException e) {
				    		    			e.printStackTrace();
				    		    			tc.sendMessage("문제가 생겨  지우지 못했어요 `(" + e.getMessage() + ")`").queue();
				    		    			System.out.println("BOT: 문제가 생겨  지우지 못했어요 `(" + e.getMessage() + ")`");
				    		    			
				    		    			if(guild.getId().equals(BotMusicListener.base) || user.getId().equals(BotMusicListener.admin)) {}
					    				    else tc.sendMessage("BOT: 문제가 생겨  지우지 못했어요 `(" + e.getMessage() + ")`").queue();
				    		    		}
			    		        	}
			    		        	
			    		        	else {
			    		        		Random ran = new Random();
			    		        		int ranint = ran.nextInt(2);
			    		        		
			    		        		if(ranint == 0) {
			    		        			tc.sendMessage("`" + wordlearn[1] + "` 라는 단어는 몰라요").queue();
			    		        			BotMusicListener.logtc.sendMessage("`" + wordlearn[1] + "` 라는 단어는 몰라요").queue();
			    		        		}
			    		        		
			    		        		
			    		        		
			    		        		else if(ranint == 1) {
			    		        			tc.sendMessage("`" + wordlearn[1] + "` 라는 단어는 기억에 없어요").queue();
			    		        			BotMusicListener.logtc.sendMessage("`" + wordlearn[1] + "` 라는 단어는 기억에 없어요").queue();
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
		    					        if(msg.getContentRaw().contains("군밤") && msg.getContentRaw().contains(word[0])) {
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
			if(guild.getId().equals("374071874222686211") || guild.toString().contains("264445053596991498") || tc.toString().contains("553254281495576577") || msg.getContentRaw().contains("형") || msg.getContentRaw().contains("오빠")) {general = tc;}
	 		else {
	 			if(extend == 1) {
	 				Extend.plusCommunication(tc, user, msg, guild);
		        	extend = 0;
		        }
			   
		        if(msg.toString().equals("군밤")||msg.toString().contentEquals("군밤아")) {
		        	tc.sendMessage("넹").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: 넹");
		        }
		        
		        /*
		        else if(user.toString().contains("월찬") && tc.toString().contains("648495662803058696")&&msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("좋아")){
		        	tc.sendMessage("찬하좋아!").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: 찬하좋아!");
		        	
		        }
		        */
 
		        else if(user.toString().contains("!") && tc.toString().contains("648495662803058696")&&msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("좋아")){
		        	tc.sendMessage("나도좋아!").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: 나도좋아!");
		        }
		        
		        else if(tc.toString().contains("648495662803058696")&&msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("좋아")){
		        	tc.sendMessage("시러").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: 시러");
		        }

		        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("좋아")){
		        	tc.sendMessage("여기서 그러지마여").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: 여기서 그러지마요");
		        }
		        
		        else if(msg.getContentRaw().equals("군밤")&& user.toString().contains("297963786504110083")) {
		        	tc.sendMessage("살아있어요").queue();
		        	System.out.println("BOT: 살아있어요");     	
		        }
		        
		        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("안녕")){
		        	tc.sendMessage("안뇽").queue();
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: 안뇽");
		            
		        }
		     
		       
		        
		        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("뭐해")) {
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
					Random r = new Random();
					int i = r.nextInt(3);
					if(i == 0) {
						tc.sendMessage("멍때려여").queue();
						System.out.println("BOT: 멍때려여");
					}
					
					else if(i == 1) {
						tc.sendMessage("자는중").queue();
						System.out.println("BOT: 자는중");
					}
					
					else if(i == 2) {
						tc.sendMessage("흐에").queue();
						System.out.println("BOT: 흐에");
					}
					
					else {
						tc.sendMessage("숨셔요").queue();
						System.out.println("BOT: 숨셔요");
					}
				
				}
		        
		        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("잘가")||msg.getContentRaw().contains("바바")||msg.getContentRaw().contains("잘자"))) {
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
					Random r = new Random();
					int i = r.nextInt(3);
					if(i == 0) {
						tc.sendMessage("바바").queue();
						System.out.println("BOT: 바바");
					}
					
					else if(i == 1) {
						tc.sendMessage("시렁").queue();
						System.out.println("BOT: 시렁");
					}
					
					else if(i == 2) {
						tc.sendMessage("또봐여").queue();
						System.out.println("BOT: 또봐여");
					}
					
					else {
						tc.sendMessage("안댕").queue();
						System.out.println("BOT: 안댕");
					}
				
				}
		        
		        else if(msg.getContentRaw().contains("군밤") && (msg.getContentRaw().contains("심심해") || msg.getContentRaw().contains("놀자") || msg.getContentRaw().contains("놀아"))) {
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
					Random r = new Random();
					int i = r.nextInt(3);
					if(i == 0) {
						tc.sendMessage("시러여").queue();
						System.out.println("BOT: 시러여");
					}
					
					else if(i == 1) {
						tc.sendMessage("혼자놀아요").queue();
						System.out.println("BOT: 혼자놀아요");
					}
					
					else if(i == 2) {
						tc.sendMessage("어떻게 놀아여").queue();
						System.out.println("BOT: 어떻게 놀아여");
					}
					
					else {
						tc.sendMessage("ㅔ..").queue();
						System.out.println("BOT: ㅔ..");
					}
				
				}
		        
		        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("자자")||msg.getContentRaw().contains("코코낸네")||msg.getContentRaw().contains("코코넨네"))){
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
					int i = r.nextInt(3);
					if(i == 0) {
						tc.sendMessage("Zzz").queue();
						System.out.println("BOT: Zzz");
					}
					
					else if(i == 1) {
						tc.sendMessage("코코코").queue();
						System.out.println("BOT: 코코코");
					}
					
					else if(i == 2) {
						tc.sendMessage("코오코오").queue();
						System.out.println("BOT: 코오코오");
					}
					
					else {
						tc.sendMessage("자기시렁").queue();
						System.out.println("BOT: 자기시렁");
					}
		        }
		        
		        else if(msg.getContentRaw().contains("군밤")&&msg.getContentRaw().contains("맛있")){
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
		        		tc.sendMessage("헉!").queue();
		        		System.out.println("BOT: 헉!");
		        	}
		        	
		        	else {
		        		tc.sendMessage("안돼애").queue();
		        		System.out.println("BOT: 안돼애");
		        		
		        	}
	    
		        }
		        
		        else if(msg.getContentRaw().contains("굼밤")||msg.getContentRaw().contains("궁밤")||msg.getContentRaw().contains("군범")){
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
		        	int i = r.nextInt(3);
		        	if(i == 0) {
		        		tc.sendMessage("ㅡ.ㅡ").queue();
		        		System.out.println("BOT: ㅡ.ㅡ");
		        	}
		        	
		        	else if(i == 1) {
		        		tc.sendMessage("ㅡㅡ").queue();
		        		System.out.println("BOT: ㅡㅡ");
		        	}
		        	
		        	else if(i == 2) {
		        		tc.sendMessage("..").queue();
		        		System.out.println("BOT: ..");
		        	}
		        	
		        	else {
		        		tc.sendMessage("ㅡ,ㅡ").queue();
		        		System.out.println("BOT: ㅡ,ㅡ");
		        		
		        	}
	
		        }
		        
		        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("멍청")||msg.getContentRaw().contains("바보")||msg.getContentRaw().contains("못생")||msg.getContentRaw().contains("몬생")||msg.getContentRaw().contains("시러"))){
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
			        		tc.sendMessage("ㅠㅠㅠㅠㅠ").queue();
			        		System.out.println("BOT: ㅠㅠㅠㅠㅠ");
			        	}
			        	
			        	if(i == 1) {
			        		tc.sendMessage("서러워..").queue();
			        		System.out.println("BOT: 서러워..");
			        	}
			        	
			        	if(i == 2) {
			        		tc.sendMessage("흐에엥 ㅠ").queue();
			        		System.out.println("BOT: 흐에엥 ㅠ");
			        	}
			        	
			        	else {
			        		tc.sendMessage("아니야!").queue();
			        		System.out.println("BOT: 아니야!");
			        	}
			       
			        }
		        }
		        
		        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("나이")||msg.getContentRaw().contains("몇살"))){
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
		        	int i = r.nextInt(3);
		        	
		        	if(i == 0) {
		        		tc.sendMessage("1살!").queue();
		        		System.out.println("BOT: 1살!");
		        	}
		        	
		        	if(i == 1) {
		        		tc.sendMessage("1살! 애기에여").queue();
		        		System.out.println("BOT: 1살! 애기에여");
		        	}
		        	
		        	if(i == 2) {
		        		tc.sendMessage("1살이에여").queue();
		        		System.out.println("BOT: 1살이에여");
		        	}
		        	
		        	else {
		        		tc.sendMessage("응앵").queue();
		        		System.out.println("BOT: 응앵");
		        	}
		        	
		        }

		        else if(msg.getContentRaw().contains("군밤")&& msg.getContentRaw().contains(" ")){
		        
		        }
		        
		        else if(msg.getContentRaw().contains("군밤")){
		        	if(msg.getContentRaw().contains("=") || msg.getContentRaw().contains("사용법") || msg.getContentRaw().contains("도움말") || msg.getContentRaw().contains("설명서"))return;
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
			        		tc.sendMessage("삐쳤어").queue();
			        		
			            	System.out.println("BOT: 삐쳤어");
			        	}
			        	else if(i == 1) {
			        		tc.sendMessage("왱").queue();
			        		
			            	System.out.println("BOT: 왱");
			            	
			            	extend = 1;
			        	}
			        	else if(i == 2) {
			        		tc.sendMessage("저 왜여").queue();
			        		
			            	System.out.println("BOT: 저 왜여");
			            	
			            	extend = 1;
			        		
			        	}
			        	else if(i == 3) {
			        		tc.sendMessage("나 왱").queue();
			        		
			            	System.out.println("BOT: 나 왱");
			            	
			            	extend = 1;
			        	}
			        	
		        	}
		        	
		            
		        }
		        
		        /*
		        else if(user.toString().contains(BotMusicListener.chanha) && msg.getContentRaw().contains("찬하는")) {
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
			        	Random r = new Random();
			        	int i = r.nextInt(10);
			        	if(i == 0) {
			        		tc.sendMessage("인싸 흥").queue();
			        		
			        		System.out.println("");
			        		System.out.println(user.toString() + ": " + msg.getContentRaw());
			            	System.out.println("BOT: 인싸");
			        	}

			        	
			        	else{
			        		tc.sendMessage("아싸다").queue();
			        		
			        		System.out.println("");
			        		System.out.println(user.toString() + ": " + msg.getContentRaw());
			            	System.out.println("BOT: 아싸다");
			        	}
		        	}
		        }
		        /*
		        else if(user.toString().contains("호두") && msg.getContentRaw().contains("호두는")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
			        	System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
			        	Random r = new Random();
			        	int i = r.nextInt(10);
			        	if(i == 0) {
			        		tc.sendMessage("귀엽다!").queue();
			        		System.out.println("BOT: 귀엽다!");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("귀엽다").queue();
			        		System.out.println("BOT: 귀엽다");
			        	}
			        	
			        	else if(i == 2) {
			        		tc.sendMessage("찬하인싸!").queue();
			        		System.out.println("BOT: 찬하인싸!");
			        
			        	}
			        	
			        	else if(i >=3 && i<=6) {
			        		tc.sendMessage("찬하인싸").queue();
			        		System.out.println("BOT: 찬하인싸");
			        	}
			       
			        	else{
			        		tc.sendMessage("꾸엽다").queue();
			        		System.out.println("BOT: 꾸엽다");
			        	}
		        	}
		        	
		        	
		        }
		        
		        else if(msg.getContentRaw().contains("폰리는")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
			        	System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
			        	Random r = new Random();
			        	int i = r.nextInt(10);
			        	if(i == 0) {
			        		tc.sendMessage("햄찌").queue();
			        		System.out.println("BOT: 햄찌");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("햄찌!").queue();
			        		System.out.println("BOT: 햄찌!");
			        	}
			        	
			        	else if(i == 2 || i == 3) {
			        		tc.sendMessage("찬하인싸").queue();
			        		System.out.println("BOT: 찬하인싸");
			        	}
			     
			        	else{
			        		tc.sendMessage("햄스터!").queue();
			        		System.out.println("BOT: 햄스터!");
			        	}
		        	}
		        }	
		        
		        else if(msg.getContentRaw().contains("핑크는")||msg.getContentRaw().contains("호두는")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
			        	System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
			        	Random r = new Random();
			        	int i = r.nextInt(10);
			        	if(i == 0) {
			        		tc.sendMessage("귀엽다!").queue();
			        		System.out.println("BOT: 귀엽다!");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("귀엽다").queue();
			        		System.out.println("BOT: 귀엽다");
			        	}
			        	
			        	else if(i >= 2 && i<=3) {
			        		tc.sendMessage("찬하인싸").queue();
			        		System.out.println("BOT: 찬하인싸");
			        	}
			
			        	else{
			        		tc.sendMessage("꾸엽다").queue();
			        		System.out.println("BOT: 꾸엽다");
			        	}
		        	}
		        }
		        */
		        
		        else if(msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("년"))){
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	tc.sendMessage("년..").queue();
		        	System.out.println("BOT: 년..");
		        }
		        
		        else if(msg.getContentRaw().contains("ㅗ")&&msg.getContentRaw().contains(" ")) {
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	System.out.println("BOT: 응답하지 않았습니다");
		        }
		        
		        else if((msg.getContentRaw().contains("군밤")&&(msg.getContentRaw().contains("ㅗ")||msg.getContentRaw().contains("시발")||msg.getContentRaw().contains("머저리")))) {
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
			        		tc.sendMessage("ㅠㅠㅠㅠㅠ욕했어").queue();
			        		System.out.println("BOT: ㅠㅠㅠㅠㅠ욕했어");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("욕했어어ㅓㅓ").queue();
			        		System.out.println("BOT: 욕했어ㅓㅓ");
			        	}
			        	
			        	else if(i >= 2 && i<=3) {
			        		tc.sendMessage("ㅠㅠㅠㅠ나뻐").queue();
			        		System.out.println("BOT: ㅠㅠㅠㅠ나뻐");
			        	}
			        	
			        	else if(i >= 4 && i<=7) {
			        		tc.sendMessage("찬하야 욕해써ㅠㅠㅠ").queue();
			        		System.out.println("BOT: 찬하야 욕해써ㅠㅠㅠ");
			        	}
			
			        	else{
			        		tc.sendMessage("욕했어ㅓㅠㅠㅠ").queue();
			        		System.out.println("BOT: 욕했어ㅓㅠㅠㅠ");
			        	}
		        	}
		        }
		        
		        
		        /*
		        else if(msg.getContentRaw().contains(BotMusicListener.chanha) && (msg.getContentRaw().contains("나이") || msg.getContentRaw().contains("몇살"))) {
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
		        	int i = r.nextInt(10);
		        	if(i == 0) {
		        		tc.sendMessage("20살").queue();
		        		tc.sendMessage("!").queue();
		        		System.out.println("BOT: 20살!");
		        		
		        		MessageHistory mh = new MessageHistory(tc);
			            List<Message> msgs = mh.retrievePast(3).complete();
			            tc.deleteMessages(msgs).complete();
		        		
		        	}
		        	
		        	if(i >= 1 && i<= 4) {
		        		tc.sendMessage("구구구").queue();
		        		System.out.println("BOT: 구구구");
		        	}
		        	
		        	if(i >= 5 && i<= 7) {
		        		tc.sendMessage("9살!").queue();
		        		System.out.println("BOT: 9살!");
		        	}
		        	
		        	else {
		        		System.out.println(user.toString() + ": " + msg.getContentRaw());
		        		System.out.println("BOT: 9살이랭");
		        	}

		        }
		        
		        else if(msg.getContentRaw().contains(BotMusicListener.chanha) &&msg.getContentRaw().contains("생일")) {
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
		        	
		        	Random r = new Random();
		        	int i = r.nextInt(10);
		        	if(i == 0) {
		        		tc.sendMessage("6월 13일!").queue();
		        		tc.sendMessage("!").queue();
		        		System.out.println("BOT: 6월 13일!");
		        		
		        		MessageHistory mh = new MessageHistory(tc);
			            List<Message> msgs = mh.retrievePast(3).complete();
			            tc.deleteMessages(msgs).complete();
		        		
		        	}
		        	
		        	if(i >= 1 && i<= 4) {
		        		tc.sendMessage("13월..").queue();
		        		System.out.println("BOT: 13월..");
		        	}
		        	
		        	if(i >= 5 && i<= 7) {
		        		tc.sendMessage("13월 32일!").queue();
		        		System.out.println("BOT: 13월 32일!");
		        	}
		        	
		        	else {
		        		System.out.println(user.toString() + ": " + msg.getContentRaw());
		        		System.out.println("BOT: 13월이랭 깔깔");
		        	}

		        }
		        
		        else if(msg.getContentRaw().contains(BotMusicListener.chanha) && (msg.getContentRaw().contains(" ") || msg.getContentRaw().contains("이") || msg.getContentRaw().contains("가"))) {
		        	
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
	        		System.out.println("BOT: 응답하지 않았습니다");
		        }
		        
		        
		        else if(msg.getContentRaw().contains("찬하는")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
		        		System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
		        		Random r = new Random();
			        	int i = r.nextInt(10);
			        	
			        	if(i == 0) {
			        		tc.sendMessage("인싸!").queue();
			        		System.out.println("BOT: 인싸!");
			        	}

			        	else if(i == 1) {
			        		tc.sendMessage("흥").queue();
			        		System.out.println("BOT: 흥");
			        	}
			        	
			        	else {
			        		tc.sendMessage("아싸다").queue();
			        		System.out.println("BOT: 아싸다");
			        	}

		        	}
		        }
		        
		        /*
		        else if(msg.getContentRaw().contains("라포") && (msg.getContentRaw().contains(" ") || msg.getContentRaw().contains("이") || msg.getContentRaw().contains("가"))) {
		        	
		        	
		        	System.out.println("");
		        	System.out.println(user.toString() + ": " + msg.getContentRaw());
	        		System.out.println("BOT: 응답하지 않았습니다");
		        }
		        
		        else if(msg.getContentRaw().contains("라포는")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
		        		System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
		        		Random r = new Random();
			        	int i = r.nextInt(10);
			        	
			        	if(i == 0) {
			        		tc.sendMessage("라포는...").queue();
			        		System.out.println("BOT: 라포는...");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("라포지").queue();
			        		System.out.println("BOT: 라포지");
			        	}
			        	else if(i == 2) {
			        		tc.sendMessage("라포야").queue();
			        		System.out.println("BOT: 라포야");}
			        	else if(i == 3) {
			        		tc.sendMessage("흥").queue();
			        		System.out.println("BOT: 흥");
			        	}
			        	else {
			        		tc.sendMessage("라포다").queue();
			        		System.out.println("BOT: 라포다");
			        	}

		        	}
		        }
		        
		        else if(msg.getContentRaw().contains("날밤") && (msg.getContentRaw().contains(" ") || msg.getContentRaw().contains("이") || msg.getContentRaw().contains("가"))) {
		        	
		        	
		        	System.out.println("");
		        	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
	        		System.out.println("BOT: 응답하지 않았습니다");
		        }
		        
		        
		        else if(msg.getContentRaw().contains("날밤은")) {
		        	if(dobae >= 7 ) {
		        		dobaeFunc(tc, user, msg);
		        	}
		        	
		        	else {
		        		System.out.println("");
			        	System.out.println(user.toString() + ": " + msg.getContentRaw());
			        	
		        		Random r = new Random();
			        	int i = r.nextInt(10);
			        	
			        	if(i == 0) {
			        		tc.sendMessage("날밤밤").queue();
			        		System.out.println("BOT: 날밤밤");
			        	}
			        	
			        	else if(i == 1) {
			        		tc.sendMessage("날밤이좋아").queue();
			        		System.out.println("BOT: 날밤이좋아");
			        	}
			        	else if(i == 2) {
			        		tc.sendMessage("군밤").queue();
			        		System.out.println("BOT: 군밤");}
			        	else if(i == 3) {
			        		tc.sendMessage("흥").queue();
			        		System.out.println("BOT: 흥");
			        	}
			        	
			        	else if(i == 4) {
			        		tc.sendMessage("훙먐먀").queue();
			        		System.out.println("BOT: 훙먐먀");
			        	}
			        	
			        	else if(i == 5) {
			        		tc.sendMessage("날밤이야").queue();
			        		System.out.println("BOT: 날밤이야");
			        	}
			        	else if(i == 6) {
			        		tc.sendMessage("뭐").queue();
			        		System.out.println("BOT: 뭐");
			        	}
			        	else if(i == 7) {
			        		tc.sendMessage("날밤이지").queue();
			        		System.out.println("BOT: 날밤이지");
			        	}
			        	else if(i == 8) {
			        		
			        		tc.sendMessage("훙먐먐이야").queue();
			        		System.out.println("BOT: 훙먐먐이야");
			        		
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
			tc.sendMessage("자꾸 부르지맛").queue();
			
			System.out.println("");
			System.out.println(user.toString() + ": " + msg.getContentRaw());
	    	System.out.println("BOT: 자꾸 부르지맛");
		}
		else if(alert == 0 && i == 1) {
			tc.sendMessage("조금 이따가 불러요").queue();
			
			System.out.println("");
			System.out.println(user.toString() + ": " + msg.getContentRaw());
	    	System.out.println("BOT: 조금 이따가 불러요");
		}     
		
		SimpleDateFormat time = new SimpleDateFormat("ss");
		String timeStr = time.format (System.currentTimeMillis());
		alert = 1;
		
		if(timeStr.contains("59")) {dobae = 0;alert = 0;}
		if(timeStr.contains("29")) {dobae = 0;alert = 0;}

    }
	
	
}
