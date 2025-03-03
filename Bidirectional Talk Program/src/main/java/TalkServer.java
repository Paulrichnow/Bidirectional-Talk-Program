package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server implementation of the talk interface that prints messages from the talk client on
 * STDOUT and prints messages from STDIN to the talk client.
 */
public class TalkServer implements BasicTalkInterface {

  private Socket client;
  private ServerSocket server;
  private BufferedReader sockin;
  private PrintWriter sockout;
  private BufferedReader stdin;

  /**
   * Constructs a socket listening on the specified port.
   * @param portnumber the port to listen for connections on
   */
  public TalkServer(int portnumber) throws IOException {
    this(new ServerSocket(portnumber));
  }

  /**
   * Constructs a talk server from the specified socket server.
   * @param server a connected socket to the use for the server
   */
  public TalkServer(ServerSocket server) throws IOException {
    this.server = server;
    this.client = this.server.accept();
    this.sockin = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
    this.sockout = new PrintWriter(this.client.getOutputStream(), true);
    this.stdin = new BufferedReader(new InputStreamReader(System.in));
  }

  /**
   * Performs asynchronous IO using polling. Should print messages from the client on STDOUT and
   * print messages from STDIN to the client. Messages printed on STDOUT should be prepended with
   * "[remote] ".
   */
  public void asyncIO() throws IOException {
    String userInput;

    if (stdin.ready()) {
      userInput = stdin.readLine();
      if (userInput != null) {
        if ("STATUS".equalsIgnoreCase(userInput)) {
          System.out.println(status());
        } else if ("EXIT".equalsIgnoreCase(userInput)) {
          System.out.println("exiting");
          System.exit(1);
        } else {
          sockout.println(userInput);
        }

      }
    }
      if (sockin.ready()) {
        System.out.printf("[remote] %s\n", this.sockin.readLine());
      }

  }

  /** Closes the socket and frees its resources. */
  public void close() throws IOException {
    this.stdin.close();
    this.sockout.close();
    this.sockin.close();
    this.client.close();
    this.server.close();
  }

  /**
   * Returns the status of the current socket connection as a String. Must include IP addresses
   * and ports. Each IP address and port should be combined as {@code <IPaddress>:<port>}.
   */
  public String status() {
    return "Client: " + client.getInetAddress() + ":" + client.getPort()
            + "; Server: " + server.getInetAddress() + ":" + server.getLocalPort();
  }

  /**
   * Performs synchronous IO by blocking on input. Should print messages from the client on STDOUT
   * and print messages from STDIN to the client. Messages printed on STDOUT should be prepended
   * with "[remote] ".
   */
  public void syncIO() throws IOException {
    String userInput = stdin.readLine();

    if(userInput != null) {
      if ("STATUS".equalsIgnoreCase(userInput)) {
        System.out.println(status());
      } else if ("EXIT".equalsIgnoreCase(userInput)) {
        System.out.println("exiting");
        System.exit(1);
      } else
        sockout.println(userInput);
    }

    // Check if input is available from the server
    if (this.sockin.ready()) {
      System.out.printf("[remote] %s\n", this.sockin.readLine());
    }
  }
}
