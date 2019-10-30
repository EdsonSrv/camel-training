package com.example.camel.routes

import org.springframework.stereotype.Component
import org.apache.camel.impl.*
import org.apache.camel.builder.*

@Component
class RouteTest extends RouteBuilder {

  @Override
  void configure() throws Exception {
    from("file://src/test/resources/filesIn?")
      .log("...Moving \${headers['CamelFileNameOnly']} file...")
      .to("file://src/test/resources/filesOut")
      .log("...File \${headers['CamelFileNameOnly']} moved...")
  }
}