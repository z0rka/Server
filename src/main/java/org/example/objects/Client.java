package org.example.objects;

import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author Kostiantyn Kvartyrmeister on 18.12.2022
 */
@AllArgsConstructor
@Getter
@Builder

public class Client {

  private String name;
  private LocalDateTime time;
  private Socket socket;
  private List<String> fileList = new ArrayList<>();
}
