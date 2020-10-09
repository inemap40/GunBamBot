// 2020.7.15 write by Arrge
package functions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CustomFunctions {
	
	//queueAgainPersonal
	public TextChannel waitQueueAgainPersonalTc; 
	public Message waitQueueAgainPersonalMessage; 
	public String waitQueueAgainPersonalId = "";
	public String waitQueueAgainPersonalLan = "";
	
	//shuffle
	public TextChannel waitShuffleTc; 
	public Message waitShuffleMessage; 
	public MessageReceivedEvent waitShuffleEvent; 
	public String waitShuffleLan = "";
	
	//remove
	public String waitRemoveItemstr = "";
	public List<Integer> waitRemoveManyList = new ArrayList<>();
	public List<String> waitRemoveTitleList = new ArrayList<>();
	public TextChannel waitRemoveTc; 
	public Message waitRemoveMessage; 
	public MessageReceivedEvent waitRemoveEvent; 
	public String waitRemoveLan = "";
	
	
	
	// removeMessage Field
	public void removeMessage(TextChannel tc, String id) {
		
		Runnable r1 = () -> {
			try {
				tc.deleteMessageById(id).complete();
			}
			catch(Exception e) {}
			
		};
		
		Thread t1 = new Thread(r1);
		t1.start();

	}
	
	public void removeMessage(TextChannel tc, String userId, String botId) {
		
		if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_MANAGE)) {
			Runnable r1 = () -> {
				try {
					tc.deleteMessageById(userId).complete();
				}
				catch(Exception e) {}
				
			};
			
			Runnable r2 = () -> {
				try {
					tc.deleteMessageById(botId).complete();
				}
				catch(Exception e) {}
			};
			
			Thread t1 = new Thread(r1);
			Thread t2 = new Thread(r2);
			t1.start();
			t2.start();
		}
	}
	
	public String autoRemoveMessage(TextChannel tc, Message msg, String userId, String botId) {
		String uid = userId;
		
		if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_MANAGE)) {
			Runnable r1 = () -> {
				try {
					tc.deleteMessageById(userId).complete();
				}
				catch(Exception e) {}
				
			};
			
			Runnable r2 = () -> {
				try {
					tc.deleteMessageById(botId).complete();
				}
				catch(Exception e) {}
			};
			
			Thread t1 = new Thread(r1);
			Thread t2 = new Thread(r2);
			t1.start();
			t2.start();
		}
		
		if(msg != null)
			uid = msg.getId();
		
		return uid;
	}
	
	public String autoRemoveMessage(TextChannel tc, Message msg, String userId, String botId, String botId2) {
		String uid = userId;
		
		if(tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_MANAGE)) {
			Runnable r1 = () -> {
				try {
					tc.deleteMessageById(userId).complete();
				}
				catch(Exception e) {}
				
			};
			
			Runnable r2 = () -> {
				try {
					tc.deleteMessageById(botId).complete();
				}
				catch(Exception e) {}
			};
			
			Runnable r3 = () -> {
				try {
					tc.deleteMessageById(botId2).complete();
				}
				catch(Exception e) {}
			};
			
			Thread t1 = new Thread(r1);
			Thread t2 = new Thread(r2);
			Thread t3 = new Thread(r3);
			t1.start();
			t2.start();
			t3.start();
		}
		
		if(msg != null)
			uid = msg.getId();
		
		return uid;
	}
	
	
	//time field
	public static String secTo(int secs) {
		secs = secs/1000;
        int hour, min, sec;

        sec  = secs % 60;
        min  = secs / 60 % 60;
        hour = secs / 3600;
        
        String s = "";
        String hourStr = String.valueOf(hour);
        String minStr = String.valueOf(min);
        String secStr = String.valueOf(sec);
        
        if(hour == 0) {
        	if(sec < 10) secStr = "0" + sec;
        	else secStr = "" + sec;
        	
        	s = minStr + ":" + secStr;
        }
        
        else {
        	if(sec < 10) secStr = "0" + sec;
        	if(min < 10) minStr = "0" + min;
        	
        	s = hourStr + ":" + minStr + ":" + secStr;

        }

        return s;
    }

	public String secTo(int secs, int i) {
		secs = secs/1000;
        int hour, min, sec;

        sec  = secs % 60;
        min  = secs / 60 % 60;
        hour = secs / 3600;
        
        String s = "";
        String hourStr = String.valueOf(hour);
        String minStr = String.valueOf(min);
        String secStr = String.valueOf(sec);
        
        if(hour == 0) {
        	if(sec < 10) secStr = "0" + sec;
        	else secStr = "" + sec;
        	
        	if(i == 0) s = "``" + minStr + ":" + secStr + "``";
        	else if(i == 1) s = minStr + ":" + secStr;
        	
        }
        
        else {
        	if(sec < 10) secStr = "0" + sec;
        	if(min < 10) minStr = "0" + min;
        	
        	if(i == 0) s = "``" + hourStr + ":" + minStr + ":" + secStr + "``";
        	else if(i == 1) s = hourStr + ":" + minStr + ":" + secStr;
        	
        }

        return s;
    }
	
	public String slowModeSec(int secs) {
        int hour, min, sec;

        sec  = secs % 60;
        min  = secs / 60 % 60;
        hour = secs / 3600;
        
        String s = "";
        String hourStr = String.valueOf(hour) + "h ";
        String minStr = String.valueOf(min) + "m ";
        String secStr = String.valueOf(sec) + "s";
      
        if(hour < 1)
        	hourStr = "";
        if(min < 1)
        	minStr = "";
        if((hour > 0 || min > 0) && sec < 1)
        	secStr = "";
        
        s = hourStr + minStr + secStr;
        	
        

        return s;
    }
	
	
	//catch field
	public static String staticCause(Exception e) {
		String o = "";
		if(e.getCause() != null)
			o = " `(" + e.getCause() + ")`";
			
		return o;
	}
	
	public String cause(Exception e) {
		String o = "";
		if(e.getCause() != null)
			o = " `(" + e.getCause() + ")`";
			
		return o;
	}
	
	
	//isNumber field
	public static boolean staticIsNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e) {
			try {
				Long.parseLong(s);
			}
			catch(Exception f) {
				return false;
			}
			return false;}
	}
	
	public boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e) {
			try {
				Long.parseLong(s);
			}
			catch(Exception f) {
				return false;
			}
			return false;}
	}
	
	
	// finished play loading and process
	public String finishLoading(TextChannel tc, String id, String str) {
		
		Runnable r = () -> {
			try {
				tc.editMessageById(id, str).complete();
			}
			catch(Exception e) {
				tc.sendMessage(str).queue();
			}
		};
		Thread t = new Thread(r);
		t.start();
		
		return "";
	}
	
	public String finishLoading(TextChannel tc, String id, MessageEmbed embed) {
		
		Runnable r = () -> {
			try {
				tc.editMessageById(id, embed).complete();
			}
			catch(Exception e) {
				tc.sendMessage(embed).queue();
			}
		};
		Thread t = new Thread(r);
		t.start();
		
		return "";
	}
	
	public String finishLoading(TextChannel tc, String id, Message msg) {
		
		Runnable r = () -> {
			try {
				tc.editMessageById(id, msg).complete();
			}
			catch(Exception e) {
				tc.sendMessage(msg).queue();
			}
		};
		Thread t = new Thread(r);
		t.start();
		
		return "";
	}
	
	
	//Get Guild, User, TC, VC Information createTime
	public String createTime(OffsetDateTime dateTime) {
		String str = "";

		String hourCreateStr = String.valueOf(dateTime.getHour());
		String minCreateStr = String.valueOf(dateTime.getMinute());
		String secCreateStr = String.valueOf(dateTime.getSecond());
		
		if(dateTime.getHour() < 10) hourCreateStr = "0" + dateTime.getHour();
		if(dateTime.getMinute() < 10) minCreateStr = "0" + dateTime.getMinute();
		if(dateTime.getSecond() < 10) secCreateStr = "0" + dateTime.getSecond();
		
		str = dateTime.getYear() + "." + dateTime.getMonthValue() + "." + dateTime.getDayOfMonth() + " `(" + hourCreateStr + ":" + minCreateStr + ":" + secCreateStr + ")`";
		return str;
	}
	
	
	//Scheduler.apply
	public String applySendMessage(TextChannel tc, File file, String menu, List<String> list) {
		String messageStr = "";
		
		try {
			FileReader filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			boolean none = true;
			String line = "";
			    
			StringBuilder sb = new StringBuilder();
			sb.append(menu);
			
			list.clear();
			while((line = bufReader.readLine()) != null) {
				list.add(line);
				none = false;
				sb.append(line + "\n");
			}
		
			if(none == true)
				sb.append("없음\n");
			
			sb.append("```");
			bufReader.close();
			
			messageStr = sb.toString();	
		}
		catch(Exception e) {
			String reply = ":no_entry_sign: **" + e.getMessage() + "**" + cause(e);
			System.out.println(reply);
			messageStr = reply;
		}
		
		return messageStr;
	}
	
	
	//Scheduler.applyDirectly
	public void applyDirectlyFile(File file, String id, List<String> list, Message response) {
		list.add(id);
		try {
			FileWriter fw = new FileWriter(file, true);
			if(file.length() <= 1)
				fw.append(id);
			else
				fw.append("\n" + id);
			
			fw.close();
		}
		catch(Exception e) {
			response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
		}
		
	}
	
	
	//Scheduler.apply, discard show list
	public String list(List<String> list) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("```css\n");
		for(int i = 0; i<list.size(); i++) {
			sb.append(list.get(i) + "\n");
		}
		sb.append("```");
		
		return sb.toString();
		
	}
	
	//Scheduler.discard
	public boolean isRemoved(File file, String id, Message response) {
		boolean removed = false;
		StringBuilder sb = new StringBuilder();
		
		try {
			FileReader filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";
	
			while((line = bufReader.readLine()) != null) {
				if(line.equals(id))
					removed = true;
				else {
					if(sb.length() == 0) sb.append(line);
	        		else 
	        			sb.append("\n" + line);
					
				}
			}
			
			bufReader.close();
			
			FileWriter fw = new FileWriter(file);
			fw.write(sb.toString());
			fw.close();
		}
		catch(Exception e) {
			response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
		}
		
		return removed;
	}

	
	//missing permission
	public String missingPermission(String s) {
		return ":no_entry_sign: **Cannot perform action due to a lack of Permission. Missing permission: " + s + "**";
	}
	
	//alert need permission
	public String askPermission(String str, String lan) {
		if(lan.equals("eng")) {
			return ":shield: Need permission: **" + str + "**";
		}
		else {
			return ":shield: 다음 권한이 필요해요: **" + str + "**";
		}
	}

}
