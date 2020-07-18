package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

class GuildMusicManager
{

	public final AudioPlayer player;

	public final TrackScheduler scheduler;

	
	public GuildMusicManager(AudioPlayerManager manager, Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event)
	{
		player = manager.createPlayer();
		scheduler = new TrackScheduler(player, guild, tc, msg, event);
		player.addListener(scheduler);
	}

	public AudioPlayerSendHandler getSendHandler()
	{
		return new AudioPlayerSendHandler(player);
	}
}