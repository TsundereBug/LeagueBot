package com.tsunderebug.leaguebot

import java.io.{BufferedReader, InputStreamReader}

import com.tsunderebug.leaguebot.listener.{ReadyListener, UserJoinListener}
import sx.blah.discord.api.{ClientBuilder, IDiscordClient}

/**
  * Created by aprim on 5/30/2017.
  */
object Main {

  val client: IDiscordClient = new ClientBuilder()
    .withToken(new BufferedReader(new InputStreamReader(getClass.getResourceAsStream("/token"))).readLine())
    .registerListener(new ReadyListener)
    .registerListener(new UserJoinListener)
    .build()

  def main(args: Array[String]): Unit = {
    client.login()
  }

}
