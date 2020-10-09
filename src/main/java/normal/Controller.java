package normal;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
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
	
	public static void calculate(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.calculate(channel, msg, event);
	}
	
	public static void translateType(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.translateType(channel, msg, event);
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
	
	public static void reTimer(TextChannel channel, Message msg, MessageReceivedEvent event) {
		Guild guild = channel.getGuild();
		GuildManager manager = getEachGuild(guild, channel, msg, event);
	
		manager.scheduler.reTimer(channel, msg, event);
	}
	
	public static void showMyInfo(TextChannel channel, Message msg, MessageReceivedEvent event, String lan, Message response) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showMyInfo(channel, msg, event, lan, response);
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
	
	public static void showHelpOld(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showHelpOld(channel, msg, lan);
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
	
	public static void apply(TextChannel channel, Message msg, MessageReceivedEvent event, int menu) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.apply(channel, msg, event, menu);
	}
	
	public static void discardDirectly(TextChannel channel, Message msg, MessageReceivedEvent event, String value, String id) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.discardDirectly(channel, msg, event, value, id);
	}
	
	public static void applyDirectly(TextChannel channel, Message msg, MessageReceivedEvent event, String value, String id) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.applyDirectly(channel, msg, event, value, id);
	}
	
	public static void showNicknameSynchronized(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showNicknameSynchronized(channel, msg, event);
	}
	
	public static void showSaveQueueAllowGuild(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showSaveQueueAllowGuild(channel, msg, event);
	}
	
	public static void showLoadAllow(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showLoadAllow(channel, msg, event);
	}
	
	public static void showMuteChannel(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showMuteChannel(channel, msg, event);
	}
	
	public static void showClearAllowUser(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showClearAllowUser(channel, msg, event);
	}
	
	public static void setNicknameSynchronized(TextChannel channel, Message msg, MessageReceivedEvent event, String id) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.setNicknameSynchronized(channel, msg, event, id);
	}
	
	public static void showIgnoreArgs0(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showIgnoreArgs0(channel, msg, event);
	}
	
	public static void showIgnorePrefix(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showIgnorePrefix(channel, msg, event);
	}
	
	public static void weatherToday(TextChannel channel, Message msg, MessageReceivedEvent event, String area) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.weatherToday(channel, msg, event, area);
	}
	
	public static void heap(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.heap(channel, msg, event, lan);
	}
	
	public static void showPermissions(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, msg, event);
		
		manager.scheduler.showPermissions(channel, msg, event, lan);
	}
	
	public static void addReaction(TextChannel channel, String msgId, ReactionEmote emote) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, null, null);
		
		manager.scheduler.addReaction(channel, msgId, emote);
	}
	
	
	public static void removeAllReaction(TextChannel channel, String msgId) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, null, null);
		
		manager.scheduler.removeAllReaction(channel, msgId);
	}
	
	public static void removeReaction(TextChannel channel, String msgId, ReactionEmote emote, Member member) {
		GuildManager manager = getEachGuild(channel.getGuild(), channel, null, null);
		
		manager.scheduler.removeReaction(channel, msgId, emote, member);
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
