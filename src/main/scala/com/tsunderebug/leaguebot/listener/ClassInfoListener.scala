package com.tsunderebug.leaguebot.listener

import java.io.InputStreamReader
import java.net.URL

import com.google.gson.Gson
import com.tsunderebug.leaguebot.model.JTLClass
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.util.EmbedBuilder

/**
  * Created by firestar155 on 6/11/17.
  */
class ClassInfoListener extends IListener[MessageReceivedEvent] {

  val classesURL = "https://dj-1-dot-jtlclasstracker.appspot.com/post.json"

  override def handle(e: MessageReceivedEvent): Unit = {
    if(e.getMessage.getContent.startsWith("-class")) {
      val gson = new Gson()
      val name: String = e.getAuthor.getDisplayName(e.getGuild).substring(0, e.getAuthor.getDisplayName(e.getGuild).indexOf('(') - 1)
      val level: Int = try {
        e.getChannel.getName.substring(6).toInt
      } catch {
        case _: NumberFormatException => -1
      }
      if(e.getMessage.getContent.split("\\s+").length > 1) {
        try {
          val cNum = e.getMessage.getContent.split("\\s+")(1).toInt
          val post = classesURL + "?classnum=" + cNum
          val r = new InputStreamReader(new URL(post).openStream())
          val c = gson.fromJson(r, classOf[JTLClass])
          r.close()

          val eb = new EmbedBuilder
          val title = c.teachers.mkString(" and ") + "'s Level " + c.level + " class"
          val field = "Takes place " + c.time + " at " + c.location + "\n" + (if(!c.helpers.isEmpty) {
            "_Helpers:_\n" + c.helpers.mkString("\n") + "\n"
          }) +
          "_Students:_\n" + c.students.mkString("\n")
          eb.appendField(title, field, false)
          for(i <- 0 until Math.min(c.days.length, 5)) {
            eb.appendField(c.days(i).date + " - Rated " + c.days(i).rating + "/5",
              "_Assigned:_\n" + c.days(i).assignments.mkString("\n") + "\n" +
              "_Planned:_\n" + c.days(i).planned.mkString("\n"),
              true)
          }
          e.getChannel.sendMessage(eb.build())
        } catch {
          case _: NumberFormatException => e.getMessage.reply("That's not a valid number! Usage: `-class {classnum}`. Use `-class` to see all of your classes.")
        }
      } else {
        val post = classesURL + "?name=" + name + (level match {
          case s if s >= 0 => "&level=" + s
          case -1 => ""
        })
        val r = new InputStreamReader(new URL(post).openStream())
        val c = gson.fromJson(r, classOf[Array[JTLClass]])
        r.close()

        val eb = new EmbedBuilder
        c.foreach((cl) => eb.appendField("Class Number " + cl.classnum + ", " + cl.time, "Teachers: " + cl.teachers.mkString(" and "), true))
        e.getChannel.sendMessage(eb.build())
      }
    }
  }

}
