package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.example.objects.Client;

/**
 * @author Kostiantyn Kvartyrmeister on 18.12.2022
 */
@NoArgsConstructor
public class ServerHandler {

  private List<Client> clients = new ArrayList<>();
  public void start() {
    try (ServerSocket socket = new ServerSocket(8080);
        Socket clientSocket = socket.accept();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));) {

      String inputLine;

      while ((inputLine = reader.readLine()) != null) {
        System.out.println(inputLine);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
