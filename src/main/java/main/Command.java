package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;


public class Command extends BotListener{

	public static void nextMonth(TextChannel tc, Message msg) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int dated = Integer.parseInt(sdf.format(date));
		
		//월
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int datem = Integer.parseInt(sdf2.format(date));
		
		//년
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int datey = Integer.parseInt(sdf3.format(date));
	
		int moncount = 0;
		
		if(datem == 1 ) {moncount = 31;}
		else if(datem == 2 ) {
			if(datey%4 == 0) {moncount = 29;}
			else {moncount = 28;}
		}
		else if(datem == 3) {moncount = 31;}
		else if(datem == 4) {moncount = 30;}
		else if(datem == 5) {moncount = 31;}
		else if(datem == 6) {moncount = 30;}
		else if(datem == 7) {moncount = 31;}
		else if(datem == 8) {moncount = 31;}
		else if(datem == 9) {moncount = 30;}
		else if(datem == 10) {moncount = 31;}
		else if(datem == 11) {moncount = 30;}
		else if(datem == 12) {moncount = 31;}
		
	
		int result = moncount - dated + 1;
		
		tc.sendMessage("**다음 달까지 " + result + "일** 남았어요").queue();
		System.out.println("BOT: **다음 달까지 " + result + "일** 남았어요");
	}
	
	public static void today(TextChannel tc, Message msg) {
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int dated = Integer.parseInt(sdf.format(date));
		
		//월
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int datem = Integer.parseInt(sdf2.format(date));
		
		//년
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int datey = Integer.parseInt(sdf3.format(date));
		
		tc.sendMessage("오늘은 **" + datey + "년 " + datem + "월 " + dated + "일** 이에요").queue();
		System.out.println("BOT: 오늘은 **" + datey + "년 " + datem + "월 " + dated + "일** 이에요");
	}
	
	public static void untilDay(TextChannel tc, Message msg) {
		Date date = new Date();
    	String recog = msg.getContentRaw().replaceAll("[^0-9]", "");
    	int recogint = 0;
    	
    	if(msg.getContentRaw().contains("수능")) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    		int datey = Integer.parseInt(sdf.format(date));
    		
    		//월
    		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
    		int datem = Integer.parseInt(sdf2.format(date));
    		
    		//일
    		SimpleDateFormat sdf3 = new SimpleDateFormat("dd");
    		int dated = Integer.parseInt(sdf3.format(date));
    		
    		int inputM = 0;
    		int inputD = 0;
    		int inputY = datey;
    		
    		if(inputY == 2020) {
    			inputM = 12;
    			inputD = 3;
    			
    			if(datem >= inputM && dated >= inputD) {
    				inputY = inputY + 1;
    			}
    		}
    		if(inputY == 2021) {
    			inputM = 11;
    			inputD = 18;
    			
    			if(datem >= inputM && dated >= inputD) {
    				inputY = inputY + 1;
    			}
    		}
    		if(inputY == 2022) {
    			inputM = 11;
    			inputD = 17;
    		}
    		
			calculateLeftDay(tc, date, inputM, inputD, inputY, false);	  
		 }
    	
    	
    	else if(msg.getContentRaw().contains("10월")||msg.getContentRaw().contains("11월")||msg.getContentRaw().contains("12월")) {
			 if(msg.getContentRaw().contains("년")) {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    int year = 0;
					    	
					    if(recogint < 10000000) {
					    	year = recogint/1000;
					    	mon = (recogint - year*1000) / 10;
					    	day = (recogint - year*1000) % 10;
					    } //10일 되기 전들의 월 분리 
					    	
					    else {
					    	year = recogint/10000;
					    	mon = (recogint - year*10000) / 100;
					    	day = (recogint - year*10000) % 100;
					    } // 10일 된 후의 월 분리
					    
					    calculateLeftDay(tc, date, mon, day, year, true);
				    }
				    
				    else {
				    	pardon(tc);
				    }
			 }
			 
			 else {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    	
					    if(recogint < 1000) {
					    	mon = recogint/10;
					    	day = recogint%10;
					    } //10일 되기 전들의 월 분리 
					    	
					    else {
					    	mon = recogint/100;
					    	day = recogint%100;
					    } // 10일 된 후의 월 분리
					    
					    calculateLeftDay(tc, date, mon, day, 0, false);
				    }
				    
				    else {
				    	pardon(tc);
				    }
			 }
			   
		 }
	
    	else if(msg.getContentRaw().contains("1월")||msg.getContentRaw().contains("2월")||msg.getContentRaw().contains("3월")||msg.getContentRaw().contains("4월")||msg.getContentRaw().contains("5월")||msg.getContentRaw().contains("6월")||msg.getContentRaw().contains("7월")||msg.getContentRaw().contains("8월")||msg.getContentRaw().contains("9월")) {
    		if(msg.getContentRaw().contains("년")) {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    int year = 0;
					    	
					    if(recogint < 1000000) {
					    	year = recogint/100;
					    	mon = (recogint - year*100) / 10;
					    	day = (recogint - year*100) % 10;
					    } //10일 되기 전들의 월 분리 
					    	
					    else {
					    	year = recogint/1000;
					    	mon = (recogint - year*1000) / 100;
					    	day = (recogint - year*1000) % 100;
					    } // 10일 된 후의 월 분리
					    
					    calculateLeftDay(tc, date, mon, day, year, true);
				    }
				    
				    else {
				    	pardon(tc);
				    }
			}
    		
    		else {
			    if(isNumber(recog) == true) {
				    recogint = Integer.parseInt(recog);
				    int mon = 0;
				    int day = 0;
				    	
				    if(recogint < 100) {
				    	mon = recogint/10;
				    	day = recogint%10;
				    } //10일 되기 전들의 월 분리 
				    	
				    else {
				    	mon = recogint/100;
				    	day = recogint%100;
				    } // 10일 된 후의 월 분리
				    
				    calculateLeftDay(tc, date, mon, day, 0, false);
			    }
			    
			    else {
			    	pardon(tc);
			    }
    		}   
		}
	
		else {
			
	    	
	    	if(isNumber(recog) == true) {
	    		
	    		recogint = Integer.parseInt(recog);
	    		
	        	SimpleDateFormat sdf = new SimpleDateFormat("dd");
	    		int dated = Integer.parseInt(sdf.format(date));
	    		
	    		//월
	    		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
	    		int datem = Integer.parseInt(sdf2.format(date));
	    		
	    		//년
	    		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
	    		int datey = Integer.parseInt(sdf3.format(date));
	
	    		int moncount = 0;
	    		
	    		if(datem == 1 ) {moncount = 31;}
	    		else if(datem == 2 ) {
	    			if(datey%4 == 0) {moncount = 29;}
	    			else {moncount = 28;}
	    		}
	    		else if(datem == 3) {moncount = 31;}
	    		else if(datem == 4) {moncount = 30;}
	    		else if(datem == 5) {moncount = 31;}
	    		else if(datem == 6) {moncount = 30;}
	    		else if(datem == 7) {moncount = 31;}
	    		else if(datem == 8) {moncount = 31;}
	    		else if(datem == 9) {moncount = 30;}
	    		else if(datem == 10) {moncount = 31;}
	    		else if(datem == 11) {moncount = 30;}
	    		else if(datem == 12) {moncount = 31;}
	    		
	    		if(recogint < dated) {recogint = recogint + moncount;}
	    	
	    		int result = recogint - dated;
	    		
	    		tc.sendMessage("**" + recog + "일까지 " + result + "일** 남았어요").queue();
	    		System.out.println("BOT: **" + recog + "일까지 " + result + "일** 남았어요");
	    	}
	    	else {
	    		Command.pardon(tc);
	    	}
		}
	}
	
	public static void lastDay(TextChannel tc, Message msg) {
		Date date = new Date();
    	String recog = msg.getContentRaw().replaceAll("[^0-9]", "");
    	int recogint = 0;
    	
    	if(msg.getContentRaw().contains("10월")||msg.getContentRaw().contains("11월")||msg.getContentRaw().contains("12월")) {
    		if(msg.getContentRaw().contains("년")) {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    int year = 0;
					    	
					    if(recogint < 10000000) {
					    	year = recogint/1000;
					    	mon = (recogint - year*1000) / 10;
					    	day = (recogint - year*1000) % 10;
					    } //10일 되기 전들의 월 분리 
					    	
					    else {
					    	year = recogint/10000;
					    	mon = (recogint - year*10000) / 100;
					    	day = (recogint - year*10000) % 100;
					    } // 10일 된 후의 월 분리
					    
					    int mo = mon;
					    int da = day;
					    int yea = year;
					    Runnable cal = () -> {
					    	calculateLastDay(tc, date, mo, da, yea);
					    };
					    Thread t = new Thread(cal);
					    t.start();
				    }
				    
				    else {
				    	pardon(tc);
				    }
			 }
    		
    		 else {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    	
					    if(recogint < 1000) {
					    	mon = recogint/10;
					    	day = recogint%10;
					    } //10일 되기 전들의 월 분리 
					    	
					    else {
					    	mon = recogint/100;
					    	day = recogint%100;
					    } // 10일 된 후의 월 분리
					    
					    int mo = mon;
					    int da = day;
					   
					    Runnable cal = () -> {
					    	calculateLastDay(tc, date, mo, da, 0);
					    };
					    Thread t = new Thread(cal);
					    t.start();
					    
				    }
				    
				    else {
				    	pardon(tc);
				    }
    		 }
		 }
	
    	else if(msg.getContentRaw().contains("1월")||msg.getContentRaw().contains("2월")||msg.getContentRaw().contains("3월")||msg.getContentRaw().contains("4월")||msg.getContentRaw().contains("5월")||msg.getContentRaw().contains("6월")||msg.getContentRaw().contains("7월")||msg.getContentRaw().contains("8월")||msg.getContentRaw().contains("9월")) {
    		if(msg.getContentRaw().contains("년")) {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    int year = 0;
					    	
					    if(recogint < 1000000) {
					    	year = recogint/100;
					    	mon = (recogint - year*100) / 10;
					    	day = (recogint - year*100) % 10;
					    } //10일 되기 전들의 월 분리 
					    	
					    else {
					    	year = recogint/1000;
					    	mon = (recogint - year*1000) / 100;
					    	day = (recogint - year*1000) % 100;
					    } // 10일 된 후의 월 분리
					    
					    
					    int mo = mon;
					    int da = day;
					    int yea = year;
					    
					    Runnable cal = () -> {
					    	calculateLastDay(tc, date, mo, da, yea);
					    };
					    Thread t = new Thread(cal);
					    t.start();
					    
				    }
				    
				    else {
				    	pardon(tc);
				    }
			}
    		
    		else {
			    if(isNumber(recog) == true) {
				    recogint = Integer.parseInt(recog);
				    int mon = 0;
				    int day = 0;
				    	
				    if(recogint < 100) {
				    	mon = recogint/10;
				    	day = recogint%10;
				    } //10일 되기 전들의 월 분리 
				    	
				    else {
				    	mon = recogint/100;
				    	day = recogint%100;
				    } // 10일 된 후의 월 분리
				    
				    int mo = mon;
				    int da = day;
				    
				    Runnable cal = () -> {
				    	calculateLastDay(tc, date, mo, da, 0);
				    };
				    Thread t = new Thread(cal);
				    t.start();
  
			    }
			    
			    else {
			    	pardon(tc);
			    }
    		}
		    
		    
		}
	
		else {
	    	tc.sendMessage("**월까지** 얘기해 주세요").queue();
	    	System.out.println("BOT: **월까지** 얘기해 주세요");
		}
	}
	
	public static void pardon(TextChannel tc) {
		
		extend = 1;
		Random r = new Random();
		int i = r.nextInt(2);
		if(i == 0) { 
			tc.sendMessage("네?").queue();
			System.out.println("BOT: 네?");
		}
		
		else {
			tc.sendMessage("넹?").queue();
			System.out.println("BOT: 넹?");
		}
			
	}
	
	public static void dateCount(int plusint, TextChannel tc) {
		
		boolean run = true;
		
		Date date = new Date();
		
		//일
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int dated = Integer.parseInt(sdf.format(date));
		
		//월
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int datem = Integer.parseInt(sdf2.format(date));
		
		//년
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int datey = Integer.parseInt(sdf3.format(date));
		int year = Integer.parseInt(sdf3.format(date));
		
		int result = plusint + dated;
		
		int moncount = 0;
		
		if(datem == 1 ) {moncount = 31;}
		else if(datem == 2 ) {
			if(datey%4 == 0) {moncount = 29;}
			else {moncount = 28;}
		}
		else if(datem == 3) {moncount = 31;}
		else if(datem == 4) {moncount = 30;}
		else if(datem == 5) {moncount = 31;}
		else if(datem == 6) {moncount = 30;}
		else if(datem == 7) {moncount = 31;}
		else if(datem == 8) {moncount = 31;}
		else if(datem == 9) {moncount = 30;}
		else if(datem == 10) {moncount = 31;}
		else if(datem == 11) {moncount = 30;}
		else if(datem == 12) {moncount = 31;}

		while(run) {
			if(datem == 1 ) {moncount = 31;}
			else if(datem == 2 ) {
				if(datey%4 == 0) {moncount = 29;}
				else {moncount = 28;}
			}
			else if(datem == 3) {moncount = 31;}
			else if(datem == 4) {moncount = 30;}
			else if(datem == 5) {moncount = 31;}
			else if(datem == 6) {moncount = 30;}
			else if(datem == 7) {moncount = 31;}
			else if(datem == 8) {moncount = 31;}
			else if(datem == 9) {moncount = 30;}
			else if(datem == 10) {moncount = 31;}
			else if(datem == 11) {moncount = 30;}
			else if(datem == 12) {moncount = 31;}
			
			if(result<moncount) {
				run = false;
				break;
			
			}
			
			else {
				result = result - moncount;
					
				datem = datem + 1;
			}
			
			if(datem > 12) {
				datey = datey + 1;
				datem = datem - 12;
			}
	
		}

		String y;
		if(year == datey) {y = "";}
		else {y = String.valueOf(datey) + "년 ";}
		
		tc.sendMessage("**" + plusint + "일 뒤는 " + y + datem + "월 " + result + "일** 이에요").queue();
		System.out.println("BOT: **" + plusint + "일 뒤는 " + y + datem + "월 " + result + "일** 이에요");
		
	}
	
	public static void dateCountMinus(int minusint, TextChannel tc) {
		
		boolean run = true;
		
		Date date = new Date();
		
		//일
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int dated = Integer.parseInt(sdf.format(date));
		
		//월
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int datem = Integer.parseInt(sdf2.format(date));
		
		//년
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int datey = Integer.parseInt(sdf3.format(date));
		int year = Integer.parseInt(sdf3.format(date));
		

		int moncount = 0;
		
		if(datem == 1 ) {moncount = 31;}
		else if(datem == 2 ) {
			if(datey%4 == 0) {moncount = 29;}
			else {moncount = 28;}
		}
		else if(datem == 3) {moncount = 31;}
		else if(datem == 4) {moncount = 30;}
		else if(datem == 5) {moncount = 31;}
		else if(datem == 6) {moncount = 30;}
		else if(datem == 7) {moncount = 31;}
		else if(datem == 8) {moncount = 31;}
		else if(datem == 9) {moncount = 30;}
		else if(datem == 10) {moncount = 31;}
		else if(datem == 11) {moncount = 30;}
		else if(datem == 12) {moncount = 31;}
		
		int result = dated - minusint;
		
		while(run) {
			if(datem == 2 ) {moncount = 31;}
			else if(datem == 3 ) {
				if(datey%4 == 0) {moncount = 29;}
				else {moncount = 28;}
			}
			else if(datem == 4) {moncount = 31;}
			else if(datem == 5) {moncount = 30;}
			else if(datem == 6) {moncount = 31;}
			else if(datem == 7) {moncount = 30;}
			else if(datem == 8) {moncount = 31;}
			else if(datem == 9) {moncount = 31;}
			else if(datem == 10) {moncount = 30;}
			else if(datem == 11) {moncount = 31;}
			else if(datem == 12) {moncount = 30;}
			else if(datem == 1) {moncount = 31;}
			
			if(result<1) {
				result = result + moncount;
				
				datem = datem - 1;
			}
			
			if(datem < 1) {
				datey = datey - 1;
				datem = datem + 12;
			}
			
			if(result > 0) {
				run = false;
				break;
			}

		}
		
		String y;
		if(year == datey) {y = "";}
		else {y = String.valueOf(datey) + "년 ";}
		
		tc.sendMessage("**" + minusint + "일 전은 " + y + datem + "월 " + result + "일** 이었어요").queue();
		System.out.println("BOT: **" + minusint + "일 전은 " + y + datem + "월 " + result + "일** 이었어요");
		
	}
	
	public static void calculateLeftDay(TextChannel tc, Date date, int mon, int day, int yearInt, boolean sayYear) {
		
		boolean run = true;
		
		if(mon > 12) {
			tc.sendMessage("말이 안돼요").queue();
			return;
		}
		
		if(day > 31) {
			tc.sendMessage("말이 안돼요").queue();
			return;
		}
		
		//일
	    SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int dated = Integer.parseInt(sdf.format(date));
			
		//월
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int datem = Integer.parseInt(sdf2.format(date));
			
		//년
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int year = Integer.parseInt(sdf3.format(date));
		int datey = Integer.parseInt(sdf3.format(date));
		
		if((yearInt != 0 && yearInt < year) || (yearInt != 0 && yearInt <= year && mon < datem) || (yearInt != 0 && yearInt <= year && mon <= datem && day < dated)) {
			tc.sendMessage("말이 안돼요").queue();
			return;
		}
		
		int moncount = 0;
		if(datem == 1) {moncount = 31;}
		else if(datem == 2 ) {
			if(datey%4 == 0) {moncount = 29;}
			else {moncount = 28;}
		}
		else if(datem == 3) {moncount = 31;}
		else if(datem == 4) {moncount = 30;}
		else if(datem == 5) {moncount = 31;}
		else if(datem == 6) {moncount = 30;}
		else if(datem == 7) {moncount = 31;}
		else if(datem == 8) {moncount = 31;}
		else if(datem == 9) {moncount = 30;}
		else if(datem == 10) {moncount = 31;}
		else if(datem == 11) {moncount = 30;}
		else if(datem == 12) {moncount = 31;}

		if(yearInt == 0)
			yearInt = datey;
		
		if((mon <= datem && day <= dated) || mon < datem) {
			yearInt = yearInt + 1;
		}

		int result = 0;
		
		while(run) {
			if(datem == 1 ) {moncount = 31;}
			else if(datem == 2 ) {
				if(datey%4 == 0) {moncount = 29;}
				else {moncount = 28;}
			}
			else if(datem == 3) {moncount = 31;}
			else if(datem == 4) {moncount = 30;}
			else if(datem == 5) {moncount = 31;}
			else if(datem == 6) {moncount = 30;}
			else if(datem == 7) {moncount = 31;}
			else if(datem == 8) {moncount = 31;}
			else if(datem == 9) {moncount = 30;}
			else if(datem == 10) {moncount = 31;}
			else if(datem == 11) {moncount = 30;}
			else if(datem == 12) {moncount = 31;}
			
			dated = dated + 1;
			
			if(dated > moncount) {
				dated = dated - moncount;
				datem = datem + 1;
			}
			
			if(datem > 12) {
				datem = datem - 12;
				datey = datey + 1;
			}
			
			result = result + 1;
			
			if(datey == yearInt && datem == mon && dated == day) {
				run = false;
				break;
			}
		}
		
		String y;
		if(year == yearInt) {y = "";}
		else {y = String.valueOf(datey) + "년 ";}
		
		String send = "**" + y + mon + "월 " + day + "일까지 " + result + "일** 남았어요";
		if(sayYear) {
			send = "**" + yearInt + "년 " + mon + "월 " + day + "일까지 " + result + "일** 남았어요";
		}
		
		tc.sendMessage(send).queue();
		System.out.println("BOT: " + send);
		
	}
	
	public static void calculateLastDay(TextChannel tc, Date date, int mon, int day, int yearInt) {
		
		boolean run = true;
		
		if(mon > 12) {
			tc.sendMessage("말이 안돼요").queue();
			return;
		}
		
		if(day > 31) {
			tc.sendMessage("말이 안돼요").queue();
			return;
		}
		
		//일
	    SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int nowDay = Integer.parseInt(sdf.format(date));
			
		//월
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int nowMonth = Integer.parseInt(sdf2.format(date));
			
		//년
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int nowYear = Integer.parseInt(sdf3.format(date));
		
		int startDay = day;
		int startMonth = mon;
		int startYear = yearInt;

		if(startYear == 0)
			startYear = nowYear;
		
		int dayCount = 0;

		
		if(nowYear <= startYear) {
			if(nowMonth <= startMonth) {
				if(nowDay < startDay) {
					tc.sendMessage("말이 안돼요").queue();
					System.out.println("BOT: 말이 안돼요");
			
					return;
				}
			}	
		}

		if(startYear == 0) {
			if(startMonth >= nowMonth) {
				startYear = nowYear - 1;
			}
			else if(startMonth == nowMonth && startDay > nowDay)
				startYear = nowYear - 1;
		}
		
		
		int moncount = 0;
		
		while(run) {

			if(startYear == nowYear && startMonth == nowMonth && startDay == nowDay) {
				run = false;
				break;
			}
			
			dayCount = dayCount + 1;
			startDay = startDay + 1;
			
			
			if(startMonth == 1) {moncount = 31;}
			else if(startMonth == 2) {
				if(startYear%4 == 0) {moncount = 29;}
				else {moncount = 28;}
			}
			else if(startMonth == 3) {moncount = 31;}
			else if(startMonth == 4) {moncount = 30;}
			else if(startMonth == 5) {moncount = 31;}
			else if(startMonth == 6) {moncount = 30;}
			else if(startMonth == 7) {moncount = 31;}
			else if(startMonth == 8) {moncount = 31;}
			else if(startMonth == 9) {moncount = 30;}
			else if(startMonth == 10) {moncount = 31;}
			else if(startMonth == 11) {moncount = 30;}
			else if(startMonth == 12) {moncount = 31;}
			
			if(startDay > moncount) {
				startMonth = startMonth + 1;
				startDay = startDay - moncount;
				
				if(startMonth > 12) {
					startMonth = startMonth - 12;
					startYear = startYear + 1;
				}
			}

		}
			
		String y;
		if(startYear == nowYear) {y = "";}
		else {y = String.valueOf(yearInt) + "년 ";}
			
		if(yearInt > 0) {
			tc.sendMessage("**" + yearInt + "년 " + mon + "월 " + day + "일은 " + dayCount + "일 전** 이었어요").queue();
			System.out.println("BOT: **" + yearInt + "년 " + mon + "월 " + day + "일은 " + dayCount + "일 전** 이었어요");

		}
		else {
			tc.sendMessage("**" + y + mon + "월 " + day + "일은 " + dayCount + "일 전** 이었어요").queue();
			System.out.println("BOT: **" + y + mon + "월 " + day + "일은 " + dayCount + "일 전** 이었어요");
		}
	}

	
	
}
