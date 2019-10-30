package com.example.camel.routes

import org.springframework.stereotype.Component
import org.apache.camel.impl.*
import org.apache.camel.builder.*

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
  }
}