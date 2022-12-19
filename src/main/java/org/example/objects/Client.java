package org.example.objects;

import java.net.Socket;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kostiantyn Kvartyrmeister on 18.12.2022
 */
@AllArgsConstructor
@Getter

public class Client {
private String name;
private LocalDateTime time;
private Socket socket;
}
