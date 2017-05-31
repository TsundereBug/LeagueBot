package com.tsunderebug.leaguebot.util

import com.tsunderebug.leaguebot.Main
import sx.blah.discord.util.MessageBuilder

/**
  * Created by aprim on 5/30/2017.
  */
object DebugUtil {

  val debugChannel: Long = 319284953630375936L

  def ping: Long = {
    val mb = new MessageBuilder(Main.client)
    mb.withChannel(Main.client.getChannelByID(debugChannel))
    mb.withContent("Pinging...")
    val firstTime = System.currentTimeMillis()
    val message = mb.send()
    val secondTime = System.currentTimeMillis()
    message.edit("Pinged! **" + (secondTime - firstTime) + "ms**")
    secondTime - firstTime
  }

}
