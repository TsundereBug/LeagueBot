package com.tsunderebug.leaguebot.listener

import java.util

import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.{IRole, IUser, Permissions}

import scala.collection.JavaConverters._

class TeamListener extends IListener[MessageReceivedEvent] {

  override def handle(e: MessageReceivedEvent): Unit = {
    if (e.getMessage.getContent.startsWith("-team ")) {
      if (e.getMessage.getContent.split("\\s+").length >= 3) {
        val name = e.getMessage.getFormattedContent.split("\\s+")(1).toLowerCase
        val members: List[IUser] = e.getAuthor :: e.getMessage.getMentions.asScala.toList
        if (members.exists(_.getRolesForGuild(e.getGuild).asScala.map(_.getName).exists(_.startsWith("team-")))) {
          e.getChannel.sendMessage("You or someone you want to add is already in a team!")
        } else if (e.getGuild.getRoles.asScala.exists((r: IRole) => r.getName.equals("team-" + name))) {
          e.getChannel.sendMessage("Team with that name already exists!")
        } else {
          val role = e.getGuild.createRole()
          role.changeName("team-" + name)
          val channel = e.getGuild.createChannel("team-" + name)
          channel.overrideRolePermissions(e.getGuild.getEveryoneRole, util.EnumSet.noneOf(classOf[Permissions]), util.EnumSet.of(Permissions.READ_MESSAGES))
          channel.overrideRolePermissions(role, util.EnumSet.of(Permissions.READ_MESSAGES), util.EnumSet.noneOf(classOf[Permissions]))
          members.foreach(_.addRole(role))
          role.changeMentionable(true)
          channel.sendMessage("Created team " + role.mention())
          role.changeMentionable(false)
        }
      } else {
        e.getMessage.reply("Incorrect usage! `-team name mention[ mention[ mention...]]]`")
      }
    } else if (e.getMessage.getContent.startsWith("-leave") && e.getChannel.getName.startsWith("team-")) {
      val r = e.getGuild.getRolesByName(e.getChannel.getName).get(0)
      e.getMessage.getAuthor.removeRole(r)
      if(e.getGuild.getUsersByRole(r).isEmpty) {
        e.getChannel.delete()
        r.delete()
      }
    }
  }

}
