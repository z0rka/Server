package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.NoArgsConstructor;
import org.example.objects.Client;

/**
 * @author Kostiantyn Kvartyrmeister on 18.12.2022 Class that works as server
 */
@NoArgsConstructor
public class ServerHandler {

  protected static final List<Client> clients = new ArrayList<>();
  private final ExecutorService readingPool = Executors.newFixedThreadPool(30);
  private final AtomicInteger integer = new AtomicInteger(1);


  /**
   * Thread for each client to read commands
   *
   * @param socket - client socket
   */
  private void readerThread(Socket socket) {
    Runnable r = () -> {
      try (BufferedReader in = new BufferedReader(
          new InputStreamReader(socket.getInputStream()))
      ) {

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          checkMessage(socket, inputLine);
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    };

    readingPool.execute(r);
    new Thread(r).start();
  }


  /**
   * Check of the command that was sent from client
   *
   * @param socket    - client socket
   * @param inputLine - command
   */

  private void checkMessage(Socket socket, String inputLine) {
    if (inputLine.equals("Exit")) {
      clients.removeIf(client -> client.getSocket().equals(socket));

    } else if (inputLine.contains("-file")) {
      newFilesAdding(socket, inputLine);
    }

  }


  /**
   * Adding file to the list of files for client
   *
   * @param inputLine - name of file
   * @param socket    - client socket
   */

  private void newFilesAdding(Socket socket, String inputLine) {

    inputLine = inputLine.replace("-file", "");
    inputLine = inputLine.trim();

    clients
        .stream().filter(client -> client.getSocket().equals(socket))
        .findAny()
        .get()
        .getFileList()
        .add(inputLine);
  }

  /**
   * Informing of other clients that new client has joined
   *
   * @param newClient - object of client that joined
   */

  private void writerToClients(Client newClient) {
    Runnable r = () -> clients
        .forEach(client -> {
          try (PrintWriter writer =
              new PrintWriter(client.getSocket().getOutputStream(), true)) {

            writer.write("[SERVER] Welcome " + newClient.getName());
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

    try (ServerSocket socket = new ServerSocket(8080)) {
      while (!closeServer) {

        Client client = Client.builder().name("Client" + integer.get()).socket(socket.accept())
            .time(LocalDateTime.now()).build();

        clients.add(client);

        writerToClients(client);
        readerThread(client.getSocket());

        if (clients.isEmpty()) {
          closeServer = true;
        }
      }
      readingPool.shutdown();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}