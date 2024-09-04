package main.java;

import java.io.IOException;

/** Driver class for the Talk socket application. */
public class Talk {

  /**
   * The program behaves as a client with the specified client connection.
   * @param client the client interface to use for socket communication
   * @return {@code false} if the host is not available
   * @throws never should return {@code false} rather than throwing an exception
   */
  protected boolean clientMode(BasicTalkInterface client) {
    while(true) {

      try {
        client.asyncIO();
      } catch (IOException e) {
        System.out.println("e");
        break;
      }
    }
    System.out.println("False");
    return false;
  }

  /**
   * The program behaves as a server with the specified server.
   * @param server the server interface to use for socket communication
   * @return {@code false} if the port is not available
   * @throws never should return {@code false} rather than throwing an exception
   */
  protected boolean serverMode(BasicTalkInterface server) {
    server.status();

    while (true) {
      try {
        server.asyncIO();
      } catch (IOException e) {
        break;
      }
    }
    System.out.println("False");
    return false;
  }

  /**
   * The program enters auto mode and behaves as a client attempting to connect to the specified
   * host on the specified port. If the host is not available, the program should behave as a
   * server listening for connections on the specified port.
   * @param hostname the host to connect to
   * @param portnumber the port to connect to or listen on
   * @return {@code false} if the host and port are both unavailable
   * @throws never should return {@code false} rather than throwing an exception
   */
  public boolean autoMode(String hostname, int portnumber) {
    if(!this.clientMode(hostname, portnumber)) {
      return this.serverMode(portnumber);
    }
    return true;
  }

  /**
   * The program behaves as a client connecting to the specified host on the specified port.
   * @param hostname the host to connect to
   * @param portnumber the port to connect to
   * @return {@code false} if the host is not available
   * @throws never should return {@code false} rather than throwing an exception
   */
  public boolean clientMode(String hostname, int portnumber) {
    try {
      return this.clientMode(new TalkClient(hostname, portnumber));
    } catch (IOException e) {
      System.out.println("Client unable to communicate with server");
      return false;
    }
  }

  /**
   * Should print your name and instructions on how to use the talk program. Instructions must
   * at least include the line "Talk [-a | –h | -s] [hostname | IPaddress] [–p portnumber]"
   */
  public void helpMode() {
    System.out.println("Usage: Talk [-a | -h | -s] [hostname | IPaddress] [-p portnumber]");
  }

  /**
   * The program behaves as a server listening for connections on the specified port.
   * @param portnumber the port to listen for connections on
   * @return {@code false} if the port is unavailable
   * @throws never should return {@code false} rather than throwing an exception
   */
  public boolean serverMode(int portnumber) {
    try {
      return this.serverMode(new TalkServer(portnumber));
    } catch (IOException e) {
      System.out.println("Server unable to listen on specified port");
      return false;
    }
  }

  /**
   * Parses the specified args and executes the talk program in its intended mode.
   * @param args the CLI args
   * @throws never should return {@code false} rather than throwing an exception
   */
  public boolean start(String[] args) {
    int defaultPort = 12987;
    String defHostName = "localhost";

    if (args.length > 0) {
      switch (args[0]) {
        case "-h":
          if(args.length == 4) {
            return clientMode(args[1], Integer.parseInt(args[3]));
          }
          else if (args.length == 3) {
            if (args[1].equals("-p")) {
              return clientMode(defHostName, Integer.parseInt(args[2]));
            }
            return clientMode(args[1], Integer.parseInt(args[3]));
          }else if (args.length == 2) {
            return clientMode(args[1], defaultPort);
          }
          else {
            return clientMode(defHostName,defaultPort);
          }
        case "-s":
          if(args.length !=3) {
            return this.serverMode(defaultPort);
          }
          else {
            return this.serverMode(Integer.parseInt(args[2]));
          }

        case "-a":
          if(args.length == 4) {
            return autoMode(args[1], Integer.parseInt(args[3]));
          } else if (args.length == 3){
            if (args[1].equals("-p")) {
              return autoMode(defHostName, Integer.parseInt(args[2]));
            }
            return autoMode(args[1], Integer.parseInt(args[3]));
        } else if (args.length == 2){
            return autoMode(args[1], defaultPort);
        }else {
            return autoMode(defHostName, defaultPort);
          }
        case "-help":
          helpMode();
          return true;
        default:
          break;
      }
    }
    return false;
  }

  public static void main(String[] args) {
    System.exit(new Talk().start(args) ? 0 : 1);
  }
}
