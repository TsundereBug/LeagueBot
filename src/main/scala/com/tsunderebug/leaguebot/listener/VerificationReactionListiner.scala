package com.tsunderebug.leaguebot.listener

import com.tsunderebug.leaguebot.Main
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent

/**
  * Created by aprim on 5/30/2017.
  */
class VerificationReactionListiner extends IListener[ReactionEvent] {

  val verificationChannel: Long = 319259152071524352L
  val levels: Map[Int, Long] = Map(
    0 -> 318508185369706506L,
    1 -> 318508854357000193L,
    2 -> 318509168065773568L,
    3 -> 318509134821457920L,
    4 -> 318509105268654081L,
    5 -> 318509070707589120L,
    6 -> 318509030597328898L,
    7 -> 318508996308893702L,
    8 -> 318508958333665291L,
    9 -> 318508911055339520L
  )

  override def handle(event: ReactionEvent): Unit = {
    if(!event.getUser.isBot && event.getMessage.getChannel.getLongID == verificationChannel) {
      val u = Main.client.getUserByID(event.getMessage.getContent.substring(0, event.getMessage.getContent.indexOf('\n')).toLong)
      event.getReaction.getUnicodeEmoji.getAliases.get(0) match {
        case ":zero:" => u.addRole(Main.client.getRoleByID(levels(0)))
        case ":one:" => u.addRole(Main.client.getRoleByID(levels(1)))
        case ":two:" => u.addRole(Main.client.getRoleByID(levels(2)))
        case ":three:" => u.addRole(Main.client.getRoleByID(levels(3)))
        case ":four:" => u.addRole(Main.client.getRoleByID(levels(4)))
        case ":five:" => u.addRole(Main.client.getRoleByID(levels(5)))
        case ":six:" => u.addRole(Main.client.getRoleByID(levels(6)))
        case ":seven:" => u.addRole(Main.client.getRoleByID(levels(7)))
        case ":eight:" => u.addRole(Main.client.getRoleByID(levels(8)))
        case ":nine:" => u.addRole(Main.client.getRoleByID(levels(9)))
        case _ => event.getMessage.getChannel.getGuild.kickUser(u, "Failed verification")
      }
      event.getMessage.delete()
    }
  }

}
