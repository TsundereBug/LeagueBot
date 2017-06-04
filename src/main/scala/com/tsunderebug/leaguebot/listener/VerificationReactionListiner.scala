package com.tsunderebug.leaguebot.listener

import com.tsunderebug.leaguebot.Main
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.util.RequestBuffer

import scala.collection.JavaConverters._

/**
  * Created by aprim on 5/30/2017.
  */
class VerificationReactionListiner extends IListener[ReactionEvent] {

  val verificationChannel: Long = 319259152071524352L
  val unverifiedRole: Long = 319259365662261250L
  val roles: Map[String, Long] = Map(
    "zero" -> 318508185369706506L,
    "one" -> 318508854357000193L,
    "two" -> 318509168065773568L,
    "three" -> 318509134821457920L,
    "four" -> 318509105268654081L,
    "five" -> 318509070707589120L,
    "six" -> 318509030597328898L,
    "seven" -> 318508996308893702L,
    "eight" -> 318508958333665291L,
    "nine" -> 318508911055339520L,
    "regional_indicator_t" -> 318509238529949696L,
    "a" -> 318509321996730370L
  )

  override def handle(event: ReactionEvent): Unit = {
    if (!event.getUser.isBot && event.getMessage.getChannel.getLongID == verificationChannel) {
      val u = Main.client.getUserByID(event.getMessage.getContent.substring(0, event.getMessage.getContent.indexOf('\n')).toLong)
      val r = event.getReaction.getUnicodeEmoji
      if (r.getUnicode == "✅") {
        u.removeRole(Main.client.getRoleByID(unverifiedRole))
        event.getMessage.getReactions.asScala.filter(_.getCount > 1).foreach(r => {
          val e = r.getUnicodeEmoji.getAliases.asScala.find(roles.contains).orNull
          if (e != null) {
            u.addRole(Main.client.getRoleByID(roles(e)))
          }
        })
        event.getMessage.delete()
      } else if (r.getUnicode == "❎") {
        event.getGuild.kickUser(u, "Failed verification")
      } else if (r.getUnicode == "\uD83C\uDD70" && !event.getUser.getPermissionsForGuild(event.getGuild).contains(Permissions.ADMINISTRATOR)) {
        RequestBuffer.request(() => event.getMessage.removeReaction(event.getUser, event.getReaction))
      }
    }
  }

}
