package org.example;


import org.example.server.ServerHandler;

/**
 * @author Kostiantyn Kvartyrmeister on 17.12.2022
 */

public class Main {

  public static void main(String[] args) {
    ServerHandler handler = new ServerHandler();
    handler.start();

    System.out.println();
  }
}
