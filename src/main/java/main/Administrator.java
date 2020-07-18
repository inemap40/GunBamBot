package main;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Scanner;


public class Administrator extends BotMusicListener {

	public static void admin(TextChannel tc, Message msg, Guild guild, User user, MessageReceivedEvent event) {

		
		Scanner scanner = new Scanner(System.in);
	   	String str = scanner.nextLine();
	    tc = general;
    	
	    if(str.equalsIgnoreCase("/logout")) {
	    	/*
	    	tc.sendMessage("/logout").queue();
        	tc.sendMessage("```관리자 로그아웃```").queue();
        	
        	MessageHistory mh = new MessageHistory(tc);
            List<Message> msgs = mh.retrievePast(2).complete();
            tc.deleteMessages(msgs).complete();
        	
            tc.sendMessage("```봇을 다시 재생합니다```").queue();
            */
        	
        	System.out.println("로그아웃합니다");
        	BotMusicListener.administrator = 0;
        	
        }
	    
	    else {
	    	tc.sendMessage(str).queue();
	    	System.out.println("BOT: " + str);
	    }

	}

}
