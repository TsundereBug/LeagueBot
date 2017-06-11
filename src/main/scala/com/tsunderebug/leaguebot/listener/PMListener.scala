package com.tsunderebug.leaguebot.listener

import java.net.{HttpURLConnection, URL}
import java.util.stream.Collectors

import com.tsunderebug.leaguebot.Main
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.util.{MessageBuilder, RequestBuffer}

/**
  * Created by aprim on 5/30/2017.
  */
class PMListener extends IListener[MessageReceivedEvent] {

  val verificationChannel: Long = 319259152071524352L

  val names: Map[Int, String] = Map(
    0 -> ":zero:",
    1 -> ":one:",
    2 -> ":two:",
    3 -> ":three:",
    4 -> ":four:",
    5 -> ":five:",
    6 -> ":six:",
    7 -> ":seven:",
    8 -> ":eight:",
    9 -> ":nine:"
  )

  override def handle(e: MessageReceivedEvent): Unit = {
    if (e.getChannel.isPrivate && e.getMessage.getContent.startsWith("-verify")) {
      if (Main.client.getChannelByID(verificationChannel).getGuild.getUsers.contains(e.getAuthor)) {
        if (Main.client.getChannelByID(verificationChannel).getFullMessageHistory.parallelStream().filter(_.getContent.contains(e.getAuthor.getStringID)).collect(Collectors.toList[IMessage]).isEmpty) {
          val args = e.getMessage.getContent.split("\\s+")
          if (args.length == 4) {
            val first = args(1).capitalize
            val last = args(2).capitalize
            val github = args(3)
            Main.client.getChannelByID(verificationChannel).getGuild.setUserNickname(e.getAuthor, first + " " + last + " (" + github + ")")
            val url = new URL("https://github.com/")
            val connection = url.openConnection.asInstanceOf[HttpURLConnection]
            connection.setRequestMethod("GET")
            connection.connect()
            connection.getResponseCode match {
              case 404 => e.getMessage.reply("This does not seem to be a valid GitHub username.")
              case 200 =>
                val mb = new MessageBuilder(Main.client)
                mb.withChannel(verificationChannel)
                mb.appendContent(e.getAuthor.getStringID)
                mb.appendContent("\n\n`")
                mb.appendContent(first)
                mb.appendContent(" ")
                mb.appendContent(last)
                mb.appendContent("` wants to be verified into The LEAGUE.\n")
                mb.appendContent("React with their level to verify or ❎ to not verify. If this person is a teacher, " +
                  "contact an Administrator by doing `@Administrator`.\n\n")
                mb.appendContent("Please verify that their GitHub is https://github.com/")
                mb.appendContent(github)
                mb.appendContent(" or react with ❎ and send them a PM.")
                val m = mb.send()
                for (i <- 0 until names.keySet.size) RequestBuffer.request(() => m.addReaction(names(i))).get()
                RequestBuffer.request(() => m.addReaction("\uD83C\uDDF9")).get()
                RequestBuffer.request(() => m.addReaction("\uD83C\uDD70")).get()
                RequestBuffer.request(() => m.addReaction("❎")).get()
                RequestBuffer.request(() => m.addReaction("✅")).get()
              case _ => e.getMessage.reply("Something went wrong, sorry. Try again in about 30 minutes.")
            }
          } else {
            e.getMessage.reply("Wrong amount of arguments. `-verify [first name] [last name] [github username]`")
          }
        } else {
          e.getMessage.reply("You are still in the verification queue! Wait patiently :)")
        }
      } else {
        e.getMessage.reply("You're not in the LEAGUE server. Here's an invite: https://discord.gg/Zyj4AN8")
      }
    }
  }

}
