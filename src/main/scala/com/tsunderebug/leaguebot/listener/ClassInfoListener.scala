package com.tsunderebug.leaguebot.listener

import java.io.{IOException, InputStreamReader}
import java.net.URL

import com.google.gson.Gson
import com.tsunderebug.leaguebot.model.JTLClass
import sx.blah.discord.api.events.IListener
import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.util.EmbedBuilder

/**
  * Created by firestar155 on 6/11/17.
  */
class ClassInfoListener extends IListener[MessageReceivedEvent] {

  val classesURL = "https://ac-2-dot-jtlclasstracker.appspot.com/post.json"

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
          try {
            val r = new InputStreamReader(new URL(post).openStream())
            val c = gson.fromJson(r, classOf[JTLClass])
            r.close()

            val eb = new EmbedBuilder
            eb.setLenient(false)
            val title = c.teachers.mkString(", ") + "'s Level " + c.level + " class"
            eb.withTitle(title)
            if (!c.helpers.isEmpty) eb.appendField("Helpers:", c.helpers.mkString("\n"), true)
            eb.appendField("Students:", c.students.mkString("\n"), true)
            val role = e.getGuild.getRolesByName("Level " + c.level).get(0)
            eb.withColor(role.getColor)
            for (i <- 0 until Math.min(c.days.length, 5)) {
              eb.appendField(c.days(i).date,
                c.days(i).assignments,
                true)
            }
            e.getChannel.sendMessage(eb.build())
          } catch {
            case _: IOException => e.getChannel.sendMessage("That's not a valid class number!")
          }
        } catch {
          case _: NumberFormatException =>
            val post = classesURL + "?name=" + e.getMessage.getContent.split("\\s+").drop(1).mkString(" ").replaceAll(" ", "%20")
            e.getChannel.sendMessage(embedForClassList(post))
        }
      } else {
        val post = classesURL + "?name=" + name.replaceAll(" ", "%20") + (level match {
          case s if s >= 0 => "&level=" + s
          case -1 => ""
        })
        e.getChannel.sendMessage(embedForClassList(post))
      }
    }
  }

  def embedForClassList(post: String): EmbedObject = {
    val gson = new Gson()
    val r = new InputStreamReader(new URL(post).openStream())
    val c = gson.fromJson(r, classOf[Array[JTLClass]])
    r.close()

    val eb = new EmbedBuilder
    c.foreach((cl) => eb.appendField("Class Number " + cl.classnum + ", " + cl.time, "Teachers: " + cl.teachers.mkString(", "), true))
    eb.build()
  }

}
