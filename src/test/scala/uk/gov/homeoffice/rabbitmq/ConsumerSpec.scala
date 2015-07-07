package uk.gov.homeoffice.rabbitmq

import scala.concurrent.Promise
import scala.concurrent.duration._
import org.json4s.JValue
import org.json4s.JsonAST.JObject
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import uk.gov.homeoffice.json.{JsonError, NoJsonValidator}

class ConsumerSpec(implicit ev: ExecutionEnv) extends Specification with RabbitSpecification {
  "Consumer" should {
    "consume valid and error message" in {
      val jsonPromise = Promise[JValue]()
      val jsonErrorPromise = Promise[JsonError]()

      val publisher = new Publisher with NoJsonValidator with WithQueue with WithRabbit
                      with JsonConsumer with JsonErrorConsumer {
        def json(json: JValue) = jsonPromise success json
        def jsonError(jsonError: JsonError) = jsonErrorPromise success jsonError
      }

      publisher.publish(JsonError(JObject(), "Error"))
      publisher.publish(JObject())

      jsonPromise.future must beLike[JValue] {
        case _: JValue => ok
      }.awaitFor(10 seconds)

      jsonErrorPromise.future must beLike[JsonError] {
        case JsonError(_, error, _, _) => ok
      }.awaitFor(10 seconds)
    }

    "consume valid message" in {
      val jsonPromise = Promise[JValue]()

      val publisher = new Publisher with NoJsonValidator with WithQueue with WithRabbit
                      with JsonConsumer {
        def json(json: JValue) = jsonPromise success json
      }

      publisher.publish(JObject())

      jsonPromise.future must beLike[JValue] {
        case j: JValue => ok
      }.awaitFor(10 seconds)
    }

    "consume error message" in {
      val jsonErrorPromise = Promise[JsonError]()

      val publisher = new Publisher with NoJsonValidator with WithQueue with WithRabbit
                      with JsonErrorConsumer {
        def jsonError(jsonError: JsonError) = jsonErrorPromise success jsonError
      }

      publisher.publish(JsonError(JObject(), "Error"))

      jsonErrorPromise.future must beLike[JsonError] {
        case JsonError(_, error, _, _) => ok
      }.awaitFor(10 seconds)
    }
  }
}