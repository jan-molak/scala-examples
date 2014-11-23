import akka.actor.{Props, Actor, ActorRef}

final case object StartProcessingTheFile

class WordCountingFileProcessor(filename: String) extends Actor {

  private var running = false
  private var totalLines = 0
  private var linesProcessed = 0
  private var result = 0
  private var fileSender: Option[ActorRef] = None

  override def receive: Receive = {
    case StartProcessingTheFile => {
      if (running) {
        println("Warning: duplicate start message received, ignoring.")
      } else {
        running = true
        fileSender = Some(sender)

        import scala.io.Source._
        fromFile(filename).getLines.foreach { line =>
          context.actorOf(Props[WordCounter]) ! CountWordsIn(line)
          totalLines += 1
        }
      }
    }

    case TotalWordsInLine(total) => {
      result += total
      linesProcessed += 1

      if (linesProcessed == totalLines) {
        fileSender.map(_ ! result)
      }
    }

    case _ => println("Warning: didn't recognise the message, sorry!")
  }
}
