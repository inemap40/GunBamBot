package play;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayController {
	
	private final static Map<Long, GuildPlayManager> managers = new HashMap<>();
	
	private static synchronized GuildPlayManager getEachGuild(Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event)    // each Guild
	{
		long guildId = Long.parseLong(guild.getId());
		
		GuildPlayManager manager = managers.get(guildId);

		if (manager == null)
		{
			manager = new GuildPlayManager(guild, tc, msg, event);
			managers.put(guildId, manager);
		}

		return manager;
	}
	
	public static void receiveRsp(TextChannel channel, Message msg, MessageReceivedEvent event, String type, String msgStr) {
		GuildPlayManager manager = getEachGuild(channel.getGuild(), channel, null, null);
		
		if(manager.playScheduler.isRspPlaying) {
			manager.playScheduler.rspWithBot(channel, msg, event, type, msgStr);
		}
		else if(manager.playScheduler.isRspMulti) {
			manager.playScheduler.rspMulti(channel, msg, event, type, msgStr);
			
		}
	}
	
	public static void rsp(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildPlayManager manager = getEachGuild(channel.getGuild(), channel, null, null);
		
		manager.playScheduler.rspWithBot(channel, msg, event, "start", null);
	}
	
	public static void rspMulti(TextChannel channel, Message msg, MessageReceivedEvent event) {
		GuildPlayManager manager = getEachGuild(channel.getGuild(), channel, null, null);
		
		manager.playScheduler.rspMulti(channel, msg, event, "getting", null);
	}
	
	public static void addReaction(TextChannel channel, String msgId, ReactionEmote emote, Member member) {
		GuildPlayManager manager = getEachGuild(channel.getGuild(), channel, null, null);
		
		manager.playScheduler.addReaction(channel, msgId, emote, member);
	}
	
	public static void removeReaction(TextChannel channel, String msgId, ReactionEmote emote, Member member) {
		GuildPlayManager manager = getEachGuild(channel.getGuild(), channel, null, null);
		
		manager.playScheduler.removeReaction(channel, msgId, emote, member);
	}
	
}
