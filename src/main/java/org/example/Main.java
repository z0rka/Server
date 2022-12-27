package org.example;


import org.example.server.ServerHandler;

/**
 * @author Kostiantyn Kvartyrmeister on 25.12.2022
 */
public class Main {


  public static void main(String[] args) {
    ServerHandler handler = new ServerHandler();
    handler.start();
  }


}
