package com.tsunderebug.leaguebot.listener

import com.tsunderebug.leaguebot.{ID, Main}
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent
import sx.blah.discord.util.MessageBuilder

/**
  * Created by aprim on 5/30/2017.
  */
class UserJoinListener extends IListener[UserJoinEvent] {

  override def handle(e: UserJoinEvent): Unit = {
    e.getUser.addRole(Main.client.getRoleByID(ID.unverifiedRole))
    val mb = new MessageBuilder(Main.client)
    mb.withChannel(e.getUser.getOrCreatePMChannel())
    mb.withContent("Heyo! You will have to be verified by LEAGUE teachers before you can talk in the channels.\n\n" +
      "Please run `-verify [first name] [last name] [github username] [additional info]` to start the verification process.")
    mb.send()
  }

}
