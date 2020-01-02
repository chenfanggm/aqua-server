package com.aqua.example;

import com.aqua.server.HttpServer;


public class Main {
  public static void main(String[] args) {
    final Integer port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;

    final HttpServer httpServer = new HttpServer();
    httpServer
      .listen(port)
      .thenRun(() -> {
        System.out.println("Example server is listening on port " + port);
      });
  }
}
