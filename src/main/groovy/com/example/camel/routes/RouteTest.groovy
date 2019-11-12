package com.example.camel.routes

import org.springframework.stereotype.Component
import org.apache.camel.impl.*
import org.apache.camel.builder.*
import org.apache.camel.Exchange
import org.apache.camel.Message
import org.apache.camel.Processor
import org.apache.camel.processor.idempotent.FileIdempotentRepository

@Component
class RouteTest extends RouteBuilder {

  @Override
  void configure() throws Exception {
    from("file://src/test/resources/filesIn?")
      .choice()
        .when(header("CamelFileNameOnly").endsWith(".01.out"))
          .log("...Moving \${headers['CamelFileNameOnly']} file...")
          .to("file://src/test/resources/filesResponses")
          .log("...File \${headers['CamelFileNameOnly']} moved to RESPONSES...")
        .when(header("CamelFileNameOnly").endsWith(".out"))
          .log("...Moving \${headers['CamelFileNameOnly']} file...")
          .to("file://src/test/resources/filesAcknowledgment")
          .log("...File \${headers['CamelFileNameOnly']} moved to ACKNOWLEDGMENT...")

		from("file://src/test/resources/filesTest?noop=true")
      .to("log:DEBUG?showBody=true&showHeaders=true")
      .process { Exchange exchange ->
        exchange.getIn().setHeader("backupPath", "path................path")
      }
      .log("\${headers['backupPath']}")
      .to("log:DEBUG?showBody=true&showHeaders=true")

    from("file://src/test/resources/fileNotDupleicates?noop=true")
      .idempotentConsumer(header("CamelFileName"),
						FileIdempotentRepository.fileIdempotentRepository(new File("src/test/resources/duplicates.txt")))
      .to("file://src/test/resources/filesOut")
      .log("Processed \${headers['CamelFileName']} file")
      .log("-------------------------------")
  }
}
