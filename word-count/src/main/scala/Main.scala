import scala.concurrent.{ExecutionContext, Await}

// based on http://www.toptal.com/scala/concurrency-and-fault-tolerance-made-easy-an-intro-to-akka
object Main extends App {

  import akka.actor.{ Props, ActorSystem }
  import akka.util.Timeout
  import scala.concurrent.duration._
  import akka.pattern.ask
  import akka.dispatch.ExecutionContexts._

  override def main(args: Array[String]) {
    implicit val ec = global
    implicit val timeout = Timeout(25 seconds)

    val system    = ActorSystem("WordCountActorSystem")
    val processor = system.actorOf(Props(new WordCountingFileProcessor(args(0))))

    ask(processor, StartProcessingTheFile).map({ result =>
      println("Total number of words: " + result)

      system.shutdown()
    })
  }
}