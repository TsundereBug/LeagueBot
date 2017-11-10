package com.tsunderebug.leaguebot.listener

import java.net.{HttpURLConnection, URL}
import java.util.stream.Collectors

import com.tsunderebug.leaguebot.{ID, Main}
import com.vdurmont.emoji.EmojiManager
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.util.{DiscordException, MessageBuilder, RequestBuffer}

/**
  * Created by aprim on 5/30/2017.
  */
class PMListener extends IListener[MessageReceivedEvent] {

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
      if (Main.client.getChannelByID(ID.verificationChannel).getGuild.getUsers.contains(e.getAuthor)) {
        if (Main.client.getChannelByID(ID.verificationChannel).getFullMessageHistory.parallelStream().filter(_.getContent.contains(e.getAuthor.getStringID)).collect(Collectors.toList[IMessage]).isEmpty) {
          val args = e.getMessage.getFormattedContent.split("\\s+")
          if (args.length >= 4) {
            val first = args(1).replaceAll("""[\[\]]""", "").capitalize
            val last = args(2).replaceAll("""[\[\]]""", "").capitalize
            val github = args(3).replaceAll("""[\[\]]""", "")
            val additional = args.drop(4).mkString(" ").replaceAll("""[\[\]]""", "")
            try {
              Main.client.getChannelByID(ID.verificationChannel).getGuild.setUserNickname(e.getAuthor, first + " " + last + " (" + github + ")")
            } catch {
              case _: DiscordException =>
                try {
                  Main.client.getChannelByID(ID.verificationChannel).getGuild.setUserNickname(e.getAuthor, first + last + "(" + github + ")")
                } catch {
                  case _: DiscordException =>
                    try {
                      Main.client.getChannelByID(ID.verificationChannel).getGuild.setUserNickname(e.getAuthor, first.head + ". " + last + " (" + github + ")")
                    } catch {
                      case _: DiscordException =>
                        Main.client.getChannelByID(ID.verificationChannel).getGuild.setUserNickname(e.getAuthor, first + last.head + "(" + github + ")")
                    }
                }
            }
            val url = new URL("https://github.com/")
            val connection = url.openConnection.asInstanceOf[HttpURLConnection]
            connection.setRequestMethod("GET")
            connection.connect()
            connection.getResponseCode match {
              case 404 => e.getMessage.reply("This does not seem to be a valid GitHub username.")
              case 200 =>
                val mb = new MessageBuilder(Main.client)
                mb.withChannel(ID.verificationChannel)
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
                mb.appendContent("\n\nAdditional info: " + additional.mkString(" "))
                val m = mb.send()
                for (i <- 0 until names.keySet.size) RequestBuffer.request(() => m.addReaction(EmojiManager.getForAlias(names(i).drop(1).dropRight(1)))).get()
                RequestBuffer.request(() => m.addReaction(EmojiManager.getByUnicode("\uD83C\uDDF9"))).get()
                RequestBuffer.request(() => m.addReaction(EmojiManager.getForAlias("a"))).get()
                RequestBuffer.request(() => m.addReaction(EmojiManager.getForAlias("negative_squared_cross_mark"))).get()
                RequestBuffer.request(() => m.addReaction(EmojiManager.getForAlias("white_check_mark"))).get()
              case _ => e.getMessage.reply("Something went wrong, sorry. Try again in about 30 minutes.")
            }
          } else {
            e.getMessage.reply("Wrong amount of arguments. `-verify [first name] [last name] [github username] [additional info]`. **Please include your level, levels you TA, and/or levels you teach in additional info.**")
          }
        } else {
          e.getMessage.reply("You are still in the verification queue! Wait patiently :)")
        }
      } else {
        e.getMessage.reply("You're not in the LEAGUE server. Here's an invite: https://discord.gg/Zyj4AN8")
      }
    } else if(e.getMessage.getContent.startsWith("-verifyhelp")) {
      e.getMessage.reply(
        """
          |you can verify youreself as a LEAGUE student by following these instructions:
          |
          |1. Open a DM with me, <@319254008105664512>
          |2. In said DM, run this command (with the correct info):
          |`-verify [first name] [last name] [github name] [levels in/teach/ta]`
          |For example, if my name is John Smith, my github is <https://github.com/jsmith> and I am a teacher for level 4, 5, and 6, I would run
          |`-verify John Smith jsmith I teach 4, 5, 6`
          |
          |Failing verification will kick you from the server, but never fear! You can rejoin with https://discord.gg/league
        """.stripMargin)
    }
  }

}
