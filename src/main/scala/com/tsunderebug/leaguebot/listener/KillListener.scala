package com.tsunderebug.leaguebot.listener

import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class KillListener extends IListener[MessageReceivedEvent] {

  override def handle(e: MessageReceivedEvent): Unit = {
    if(e.getMessage.getContent == "-kill") {
      e.getAuthor.getName + "#" + e.getAuthor.getDiscriminator match {
        case "Sock#4177" | "TsundereBug#0641" =>
          e.getClient.logout()
          System.exit(0)
      }
    }
  }

}
