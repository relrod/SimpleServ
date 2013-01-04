package me.elrod.SimpleServ

/** Contains method prototypes to interact with ever protocol ever. */
trait LinkProtocol {
  val socket: Socket
  val config: Config
  val services: scala.collection.mutable.Map[String, Service] = scala.collection.mutable.Map()
  def send(command: String, args: List[String], ending: Option[String] = None) = {
    val prepareSend = ending match {
      case None => command + " " + args.mkString(" ")
      case Some(end) => command + " " + args.mkString(" ") + " :" + end
    }
    socket.send(prepareSend)
  }

  def sendFrom(from: String, command: String, args: List[String], ending: Option[String] = None) =
    send((if (!from.startsWith(":")) ":" else "") + from, command, args, ending)

  def handshake(): Unit

  def addService(serviceClass: String, configGroup: String): Unit

  def getLine(raw: String): Unit
}

class CharybdisIRCD(val socket: Socket, config: Config) extends LinkProtocol {
  def handshake() = {
    send(
      "PASS",
      List(
        config.getString("our.password"),
        "TS",
        "6"),
        config.getString("my.numeric"))
    send(
      "CAPAB",
      List(),
      "QS ENCAP EX CHW IE KNOCK TS6 SAVE EUID")
    send(
      "SERVER",
      List(
        config.getString("my.hostname"),
        "1"),
      config.getString("my.description"))
    send(
      "SVINFO",
      List(
        "6",
        "3",
        "0"),
      System.currentTimeMillis.toString)
  }

  def addService(serviceClass: String, configGroup: String) {
    // This is somewhat inefficient, but we're dealing with network stuff
    // anyway, so the inefficiency is negligable.
    // Also it would be nice if Scala had something akin to Ruby's .succ! to
    // increment the string, but the odds of a clash are literally
    // number_of_services / 170581728179578200000, so it's probably not a huge
    // deal.
    val uid = (for (i <- 1 to 6) yield ('A' to 'Z')(util.Random.nextInt(25))).mkString
    sendFrom(
      config.getString("my.numeric"),
      "EUID",
      List(
        nick,
        "1",
        System.currentTimeMillis.toString,
        umodes,
        username,
        config.getString("my.hostname"),
        "0",
        uid,
        "*",
        "*"),
      name)
  }
}

object ServicesRunner {
  def main() {
    val proto = new CharybdisIRCD(config)
    val socket = new Socket().connect.and.stuff()
    proto.socket = socket

    config.getServices.foreach { service =>
      proto.addService(service.classname, service.configgroup)
    }
  }
}
