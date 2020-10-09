// 2020.7.27 write by Arrge
package main;

import audio.MusicController;
import functions.CustomFunctions;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEmoteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import normal.Controller;
import play.PlayController;

public class BotEventListener extends ListenerAdapter {

	CustomFunctions func = new CustomFunctions();
	
	@Override
	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
		if(event.getGuild().getId().equals(Main.nickNameSynchronizedGuild)) { //»õº®¹Ý
			String name = event.getMember().getNickname();

			String id = event.getMember().getId();
			Guild guild = event.getJDA().getGuildById(BotMusicListener.base);
			if(guild.getMemberById(id) == null) return;

			try {
				guild.getMemberById(id).modifyNickname(name).complete();
			}
			catch(Exception e) {
				BotMusicListener.errortc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + func.cause(e) + " `(" + event.getMember().getId() + ")`").queue();
			}
		}
	}
	
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if(event.getMember().getId().equals(BotMusicListener.bot)) return;
		Controller.addReaction(event.getChannel(), event.getMessageId(), event.getReactionEmote());
		MusicController.addReaction(event.getChannel(), event.getMessageId(), event.getReactionEmote(), event.getMember());
		PlayController.addReaction(event.getChannel(), event.getMessageId(), event.getReactionEmote(), event.getMember());
		//System.out.println(event.getReactionEmote().getAsCodepoints());
	}
	
	@Override
	public void onGuildMessageReactionRemoveEmote(GuildMessageReactionRemoveEmoteEvent event) {
		//Controller.removeEmoteReaction(event.getChannel());
	}
	
	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		Controller.removeReaction(event.getChannel(), event.getMessageId(), event.getReactionEmote(), event.getMember());
		MusicController.removeReaction(event.getChannel(), event.getMessageId(), event.getReactionEmote(), event.getMember());
		PlayController.removeReaction(event.getChannel(), event.getMessageId(), event.getReactionEmote(), event.getMember());
	}

	@Override
	public void onGuildMessageReactionRemoveAll(GuildMessageReactionRemoveAllEvent event) {
		Controller.removeAllReaction(event.getChannel(), event.getMessageId());
		MusicController.removeAllReaction(event.getChannel(), event.getMessageId());
	}
	
}
