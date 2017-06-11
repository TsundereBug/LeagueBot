package com.tsunderebug.leaguebot.listener

import java.io.InputStreamReader
import java.net.URL

import com.google.gson.{JsonArray, JsonObject, JsonParser}
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

/**
  * Created by firestar155 on 6/11/17.
  */
class ClassInfoListener extends IListener[MessageReceivedEvent] {

  val classesURL = "https://classes.jointheleague.org/rest/classes"

  override def handle(e: MessageReceivedEvent): Unit = {
    if(e.getMessage.getContent.startsWith("-classes ")) {
      try {
        val level = e.getMessage.getContent.split("\\s+")(1).toInt // TODO make this use channel level
        val classes: JsonArray = new JsonParser().parse(new InputStreamReader(new URL(classesURL).openStream())).getAsJsonObject.getAsJsonArray("classes")
      } catch {
        case _: Exception =>
          e.getMessage.reply("An error occured! Usage: `-classes [level]`")
      }
    }
  }

}
