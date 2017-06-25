package com.tsunderebug.leaguebot.listener

import com.tsunderebug.leaguebot.{ID, Main}
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.util.RequestBuffer

import scala.collection.JavaConverters._

/**
  * Created by aprim on 5/30/2017.
  */
class VerificationReactionListiner extends IListener[ReactionEvent] {

  override def handle(e: ReactionEvent): Unit = {
    if (!e.getUser.isBot && e.getMessage.getChannel.getLongID == ID.verificationChannel) {
      val id = e.getMessage.getContent.substring(0, e.getMessage.getContent.indexOf('\n')).toLong
      val u = Main.client.getUserByID(id)
      val r = e.getReaction.getUnicodeEmoji
      if (r.getUnicode == "✅") {
        u.removeRole(Main.client.getRoleByID(ID.unverifiedRole))
        e.getMessage.getReactions.asScala.filter(_.getCount > 1).foreach(r => {
          val e = r.getUnicodeEmoji.getAliases.asScala.find(ID.roles.contains).orNull
          if (e != null) {
            u.addRole(Main.client.getRoleByID(ID.roles(e)))
          }
        })
        e.getMessage.delete()
      } else if (r.getUnicode == "❎") {
        e.getGuild.kickUser(u, "Failed verification")
        e.getMessage.delete()
      } else if (r.getUnicode == "\uD83C\uDD70" && !e.getUser.getPermissionsForGuild(e.getGuild).contains(Permissions.ADMINISTRATOR)) {
        RequestBuffer.request(() => e.getMessage.removeReaction(e.getUser, e.getReaction))
      }
    }
  }

}
