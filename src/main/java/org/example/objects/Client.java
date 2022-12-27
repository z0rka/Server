package org.example.objects;

import java.io.File;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Kostiantyn Kvartyrmeister on 18.12.2022
 */
@RequiredArgsConstructor
@Getter

public class Client {

  private final String name;
  private final LocalDateTime time;
  private final Socket socket;
  private List<File> fileList = new ArrayList<>();
}
