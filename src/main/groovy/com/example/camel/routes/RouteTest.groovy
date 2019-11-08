package com.example.camel.routes

import org.springframework.stereotype.Component
import org.apache.camel.impl.*
import org.apache.camel.builder.*
import org.apache.camel.Exchange
import org.apache.camel.Message

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
      .log("**********************INIT**************************")
      .to("log:DEBUG?showBody=false&showHeaders=true")
      .process { Exchange exchange ->
        exchange.getIn().setHeader("backupPath", "path................path")
        // String outputFile =  (headers.CamelFileNameOnly.matches(/.*\.[0-9]{2}.out$/))?
        //   "${pathResponseOutTemp}/${headers.CamelFileNameOnly}" : "${pathAcknowledgmentOutTemp}/${headers.CamelFileNameOnly}"
      }
      .log("---------------------------------------------------")
      .log("\${headers['backupPath']}")
      .log("---------------------------------------------------")
      .to("log:DEBUG?showBody=false&showHeaders=true")
      .log("**********************END**************************")
  }
}
