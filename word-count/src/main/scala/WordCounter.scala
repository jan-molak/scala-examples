import akka.actor.Actor

final case class CountWordsIn(line: String)
final case class TotalWordsInLine(total: Int)

class WordCounter extends Actor {
  override def receive = {
    case CountWordsIn(line) => {
      val wordCount = line.split(" ").length

      sender ! TotalWordsInLine(wordCount)
    }
    case _ => {
      println("Warning: message not recognised")
    }
  }
}
