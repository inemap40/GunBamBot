// 2020.7.15 write by Arrge
package functions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CustomFunctions {
	
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
	
	public String autoRemoveMessage(TextChannel tc, Message msg, String userId, String botId) {
		String uid = userId;
		
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
		
		uid = msg.getId();
		
		return uid;
	}
	
	public String autoRemoveMessage(TextChannel tc, Message msg, String userId, String botId, String botId2) {
		String uid = userId;
		
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


}
