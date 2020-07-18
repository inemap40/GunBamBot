package normal;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class GuildManager {

	public final Scheduler scheduler;

	public GuildManager(Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event)
	{
		scheduler = new Scheduler(guild, tc, msg, event);
	}

}
