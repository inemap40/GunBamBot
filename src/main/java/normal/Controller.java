package normal;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.HashMap;
import java.util.Map;


public class Controller
{
	
	private final static Map<Long, GuildManager> managers = new HashMap<>();
	
	private static synchronized GuildManager getEachGuild(Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event)    // each Guild
	{
		long guildId = Long.parseLong(guild.getId());
		
		GuildManager manager = managers.get(guildId);

		if (manager == null)
		{
			manager = new GuildManager(guild, tc, msg, event);
			managers.put(guildId, manager);
		}

		return manager;
	}
	
	public static void timer(User user, TextChannel channel, Message msg, MessageReceivedEvent event, int time) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.setTimer(user, channel, msg, event, time);
	}
	
	public static void nowTimer(TextChannel channel, Message msg, MessageReceivedEvent event) {
		Guild guild = channel.getGuild();
		GuildManager manager = getEachGuild(guild, channel, msg, event);
	
		manager.scheduler.nowTimer(channel, msg);
	}
	
	public static void timerCancel(TextChannel channel, Message msg, MessageReceivedEvent event) {
		Guild guild = channel.getGuild();
		GuildManager manager = getEachGuild(guild, channel, msg, event);
	
		manager.scheduler.timerCancel(channel, msg);
	}
	
	public static void showInfo(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showInfo(channel, msg, event, lan);
	}
	
	public static void useCpu(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.useCpu(channel, msg, lan);
	}
	
	public static void help(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.help(channel, msg, event, lan);
	}
	
	public static void kospi(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);

		manager.scheduler.kospi(channel, msg, event, lan);
		
	}

	public static void news(TextChannel channel, Message msg, MessageReceivedEvent event)    // Skip the current track and play the next one
	{
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.news(channel, msg);
		
	}
	
	public static void upTime(TextChannel channel, Message msg, MessageReceivedEvent event, int uptime, String lan) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);

		manager.scheduler.uptime(channel, msg, event, uptime, lan);
	}
	
	
	public static void reset(TextChannel channel, Message msg, MessageReceivedEvent event) {

		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
	
		manager.scheduler.reset(channel);
		
	}
	
	public static boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e) {return false;}
		
	}
	
	
}
