package main;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class Calculate {

	public static void calculate(TextChannel tc, Message msg) {
		String str = msg.getContentRaw().replaceAll("±º¹ã", "").replaceAll(" ", "").replaceAll("°è»ê", "").replaceAll("=", "");

		String[] load = null;
		float result = 0f;
		String resultstr = "";
		
		try {
			if(str.contains("/")) {
				load = str.split("/");
				result = Float.parseFloat(load[0]);
				
				for(int i = 0; i<load.length-1; i++) {
					result = result / Float.parseFloat(load[i+1]);
				}
			}
			
			else if(str.contains("x")) {
				load = str.split("x");
				result = Float.parseFloat(load[0]);
				
				for(int i = 0; i<load.length-1; i++) {
					result = result * Float.parseFloat(load[i+1]);
				}
			}
			
			else if(str.contains("-")) {
				load = str.split("-");
				result = Float.parseFloat(load[0]);
				
				for(int i = 0; i<load.length-1; i++) {
					result = result - Float.parseFloat(load[i+1]);
				}
			}
			
			else if(str.contains("+")) {
				load = str.split("\\+");
				result = Float.parseFloat(load[0]);
				
				for(int i = 0; i<load.length-1; i++) {
					result = result + Float.parseFloat(load[i+1]);
				}
				
			}

			if(result % 1 == 0) 
				resultstr = String.format("%.0f", result);
			else
				resultstr = String.valueOf(result);
			
			tc.sendMessage("**" + resultstr + "**").queue();
	
		}
		catch (Exception e) {
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
		}
	
		
	}
	
	public static String cause(Exception e) {
		String o = "";
		if(e.getCause() != null)
			o = " `(" + e.getCause() + ")`";
			
		return o;
	}
}
