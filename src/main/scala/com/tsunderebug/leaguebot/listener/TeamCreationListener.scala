package com.tsunderebug.leaguebot.listener

import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class TeamCreationListener extends IListener[MessageReceivedEvent] {

  override def handle(event: MessageReceivedEvent): Unit = {
    if(event.getMessage.getContent.startsWith("-team")) {

    }
  }

}
