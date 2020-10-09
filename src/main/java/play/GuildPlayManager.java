package play;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GuildPlayManager {

	public final PlayScheduler playScheduler;

	public GuildPlayManager(Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event)
	{
		playScheduler = new PlayScheduler(guild, tc, msg, event);
	}

}