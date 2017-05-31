package com.tsunderebug.leaguebot.listener

import com.tsunderebug.leaguebot.Main
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent
import sx.blah.discord.util.MessageBuilder

/**
  * Created by aprim on 5/30/2017.
  */
class UserJoinListener extends IListener[UserJoinEvent] {

  val unverifiedRole: Long = 319259365662261250L

  override def handle(event: UserJoinEvent): Unit = {
    event.getUser.addRole(Main.client.getRoleByID(unverifiedRole))
    val mb = new MessageBuilder(Main.client)
    mb.withChannel(event.getUser.getOrCreatePMChannel())
    mb.withContent("Heyo! You will have to be verified by LEAGUE teachers before you can talk in the channels.\n\n" +
      "Please run `-verify [first name] [last name] [github username]` to start the verification process.")
    mb.send()
  }

}
