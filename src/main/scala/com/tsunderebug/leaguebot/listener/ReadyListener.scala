package com.tsunderebug.leaguebot.listener

import java.util.concurrent.{ScheduledExecutorService, ScheduledThreadPoolExecutor, TimeUnit}

import com.tsunderebug.leaguebot.util.DebugUtil
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.ReadyEvent

import scala.collection.JavaConverters._

/**
  * Created by aprim on 5/30/2017.
  */
class ReadyListener extends IListener[ReadyEvent] {

  val green = 0
  val yellow = 125
  val red = 200

  override def handle(e: ReadyEvent): Unit = {
    val spe = new ScheduledThreadPoolExecutor(1)
    spe.scheduleAtFixedRate(() => {
      val ping: Long = DebugUtil.ping
      ping match {
        case x if green until yellow contains x => e.getClient.online("Ping: " + ping + "ms | jointheleague.org")
        case x if yellow until red contains x => e.getClient.idle("Ping: " + ping + "ms | jointheleague.org")
        case _ => e.getClient.streaming("Slow! Ping: " + ping + "ms | jointheleague.org", "https://twitch.tv/directory")
      }
    }, 0, 30, TimeUnit.SECONDS)
  }

}
