package main;

import java.util.Random;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Extend extends BotListener{

public static void plusCommunication(TextChannel tc, User user, Message msg, Guild guild) {
		
	
	System.out.println("");
	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
	
		
        if(msg.getContentRaw().contains("안녕") || msg.getContentRaw().contains("반가워")) {
			
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("반가워요").queue();
				System.out.println("BOT: 반가워요");
			}
			
			else if(i == 1) {
				tc.sendMessage("하이!").queue();
				System.out.println("BOT: 하이!");
			}
			
			else if(i == 2) {
				tc.sendMessage("헤헤").queue();
				System.out.println("BOT: 헤헤");
			}
			
			else {
				tc.sendMessage("안녕!").queue();
				System.out.println("BOT: 안녕!");
			}

		}

        else if(msg.getContentRaw().contains("안해")||msg.getContentRaw().contains("안") || msg.getContentRaw().contains("말고") || msg.getContentRaw().contains("아니야")) {
		
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("힝").queue();
				System.out.println("BOT: 힝");
			}
			
			else if(i == 1) {
				tc.sendMessage("칫").queue();
				System.out.println("BOT: 칫");
			}
			
			else if(i == 2) {
				tc.sendMessage("넴..").queue();
				System.out.println("BOT: 넴..");
			}
			
			else {
				tc.sendMessage("흑흑").queue();
				System.out.println("BOT: 흑흑");
			}
			
		
		}
		
		else if(msg.getContentRaw().contains("심심해") || msg.getContentRaw().contains("놀자") || msg.getContentRaw().contains("놀아")) {
			
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

		else if(msg.getContentRaw().contains("뭐해")) {
			
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("자고있어여").queue();
				System.out.println("BOT: 자고있어여");
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
        
		else if(msg.getContentRaw().contains("잘가")||msg.getContentRaw().contains("바바")||msg.getContentRaw().contains("잘자")) {
			
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
		
		else if(msg.getContentRaw().contains("자자")||msg.getContentRaw().contains("코코넨네")||msg.getContentRaw().contains("코코낸네")) {
			
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
		
		else if(msg.getContentRaw().contains("좋아")||msg.getContentRaw().contains("사랑")) {
			
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("여기서 그러지마여").queue();
	        	System.out.println("BOT: 여기서 그러지마요");
			}
			
			else if(i == 1) {
				tc.sendMessage("시러여").queue();
	        	System.out.println("BOT: 시러여");
			}
			
			else if(i == 2) {
				tc.sendMessage("싫어").queue();
				System.out.println("BOT: 싫어");
			}
			
			else {
				tc.sendMessage("안좋아해").queue();
				System.out.println("BOT: 안좋아해");
			}
			
		}
        
		else if(msg.getContentRaw().contains("맛있")){
        	System.out.println("");
        	System.out.println(user.toString() + ": " + msg.getContentRaw());
        	
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
 
        
		else if(msg.getContentRaw().contains("일전")||msg.getContentRaw().contains("일 전")){
        	

        	String minus = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	if(isNumber(minus) == true) {
        		int minusint = Integer.parseInt(minus);
        		
        		Command.dateCountMinus(minusint, tc);
        		
        		
        	}
        	
        	else {
        		Command.pardon(tc);
        	}
        	
        }
        
		else if(msg.getContentRaw().contains("일뒤")||msg.getContentRaw().contains("일 후")||msg.getContentRaw().contains("일후")||msg.getContentRaw().contains("일 뒤")){
        	

        	String plus = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	if(isNumber(plus) == true) {
        		int plusint = Integer.parseInt(plus);
        		
        		Command.dateCount(plusint, tc);
        		
        	}
        	
        	else {
        		Command.pardon(tc);
        	}
        	
        }
        
		else if(msg.getContentRaw().contains("다음 달까지")||msg.getContentRaw().contains("다음달까지")){
        	Command.nextMonth(tc, msg);
        }
        
		else if((msg.getContentRaw().contains("일까지")||msg.getContentRaw().contains("일 까지"))&&(msg.getContentRaw().contains("얼마")||msg.getContentRaw().contains("며칠")||msg.getContentRaw().contains("몇일"))){
        	
			Command.untilDay(tc, msg);
		}
        
		else if(msg.getContentRaw().contains("일은")&&(msg.getContentRaw().contains("언제")||msg.getContentRaw().contains("며칠")||msg.getContentRaw().contains("몇일"))&&(msg.getContentRaw().contains("전")||msg.getContentRaw().contains("었")||msg.getContentRaw().contains("였"))){
        	
			Command.untilDay(tc, msg);
		}
    }
}
