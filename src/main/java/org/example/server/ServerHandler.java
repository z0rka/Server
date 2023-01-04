package org.example.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.NoArgsConstructor;
import org.example.objects.Client;

/**
 * @author Kostiantyn Kvartyrmeister on 18.12.2022 Class that works as server
 */
@NoArgsConstructor
public class ServerHandler {

  private static final int PORT = 8080;
  protected static final List<Client> clients = new ArrayList<>();

  private final AtomicInteger integer = new AtomicInteger(1);


  /**
   * Thread for each client to read commands
   *
   * @param client - client
   */
  private void readerThread(Client client) {
    Runnable r = () -> {
      try (ObjectInputStream in = new ObjectInputStream(
          client.getSocket().getInputStream())
      ) {

        Object input;
        while (!client.getSocket().isClosed()) {
          input = in.readObject();
          checkMessage(client, input);
        }
        clients.remove(client);

      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    };

    new Thread(r).start();
  }

  /**
   * Method checks what client has sent
   *
   * @param client - client
   * @param input  - object that was sent by client
   */
  private void checkMessage(Client client, Object input) throws IOException {
    if (input.getClass().equals(String.class) && input.equals("exit")) {
      client.getSocket().close();
      clients.remove(client);

    } else if (input.getClass().equals(File.class)) {
      clients
          .stream()
          .filter(client1 -> client1.equals(client))
          .findFirst()
          .ifPresent(client1 -> client1.getFileList().add((File) input));

    }
  }

  /**
   * Informing of other clients that new client has joined
   *
   * @param newClient - object of client that joined
   */

  private void writerToClients(Client newClient) {
    Runnable r = () ->
        clients
            .stream()
            .filter(client -> !client.getName().equals(newClient.getName()))
            .forEach(client -> {
              try {
                PrintWriter writer = new PrintWriter(client.getSocket().getOutputStream(), true);
                writer.println("Hello! " + newClient.getName());
                System.out.println(client.getName());
              } catch (IOException e) {
                e.printStackTrace();
              }
            });

    new Thread(r).start();
  }

  /**
   * start of the server
   */

  public void start() {

    boolean closeServer = false;

    try (ServerSocket socket = new ServerSocket(PORT)) {
      while (!closeServer) {

        Client client = new Client("Client" + integer.get(), LocalDateTime.now(), socket.accept());
        clients.add(client);

        integer.set(integer.get() + 1);

        writerToClients(client);
        readerThread(client);

        if (clients.isEmpty()) {
          closeServer = true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}