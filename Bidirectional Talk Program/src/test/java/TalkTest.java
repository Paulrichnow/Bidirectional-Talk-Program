package test.java;

/**
 * DO NOT DISTRIBUTE.
 *
 * This code is intended to support the education of students associated with the Tandy School of
 * Computer Science at the University of Tulsa. It is not intended for distribution and should
 * remain within private repositories that belong to Tandy faculty, students, and/or alumni.
 */
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import main.java.BasicTalkInterface;
import main.java.Talk;
import main.java.TalkClient;
import main.java.TalkServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.java.TUGrader.DisplayName;
import test.java.TUGrader.Eval;
import test.java.TUGrader.TestGroup;

/**
 * This class provides a set of unit tests for the {@code Talk} classes
 * using the JUnit testing framework and the Java Reflection API.
 */
public class TalkTest {

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 12987;

  @Before
  public void setup() {
    TUGrader.silenceStdout();
  }

  @After
  public void cleanup() {
    TUGrader.resetStdIO();
  }


  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -a\" should call autoMode with default args")
  public void testStartCallsAutoModeWithDefaultArgs() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean autoMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo(TalkTest.DEFAULT_HOST)));
            assertThat(portnumber, is(equalTo(TalkTest.DEFAULT_PORT)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a"}),
        is(true)
      );
    eval.done("\"Talk -a\" should call autoMode");
  }

  @Test
  @TestGroup("autoMode")
  @DisplayName(
        "autoMode should only call clientMode when clientMode is successful using default args")
  public void testAutoModeCallsClientModeButNotServerModeWithDefaultArgs() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo(TalkTest.DEFAULT_HOST)));
            assertThat(portnumber, is(equalTo(TalkTest.DEFAULT_PORT)));
            eval.success();
            return true;
          }

          @Override
          public boolean serverMode(int portnumber) {
            eval.fail("should not call serverMode when clientMode is successful");
            return false;
          }
        };
    assertThat(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a"}),
        is(true)
      );
    eval.done("autoMode should only call clientMode when clientMode is successful");
  }

  @Test
  @TestGroup("autoMode")
  @DisplayName("autoMode should call serverMode when clientMode fails using default args")
  public void testAutoModeCallsServerModeAfterClientModeFailsWithDefaultArgs() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            return false;
          }

          @Override
          public boolean serverMode(int portnumber) {
            assertThat(portnumber, is(equalTo(TalkTest.DEFAULT_PORT)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a"}),
        is(true)
      );
    eval.done("autoMode should call serverMode when clientMode fails");
  }

  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -a test.utulsa.edu\" should call autoMode with host arg")
  public void testStartCallsAutoModeWithHostArg() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean autoMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo("test.utulsa.edu")));
            assertThat(portnumber, is(equalTo(TalkTest.DEFAULT_PORT)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "test.utulsa.edu"}),
        is(true)
      );
    eval.done("\"Talk -a test.utulsa.edu\" should call autoMode");
  }

  @Test
  @TestGroup("autoMode")
  @DisplayName("autoMode should only call clientMode when clientMode is successful using host arg")
  public void testAutoModeCallsClientModeButNotServerModeWithHostArg() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo("test.utulsa.edu")));
            assertThat(portnumber, is(equalTo(TalkTest.DEFAULT_PORT)));
            eval.success();
            return true;
          }

          @Override
          public boolean serverMode(int portnumber) {
            eval.fail("autoMode should not call serverMode when clientMode is successful");
            return false;
          }
        };
    assertThat(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "test.utulsa.edu"}),
        is(true)
      );
    eval.done();
  }

  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -a -p 8080\" should call autoMode")
  public void testStartCallsAutoModeWithPortArg() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean autoMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo(TalkTest.DEFAULT_HOST)));
            assertThat(portnumber, is(equalTo(8080)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "-p", "8080"}),
        is(true)
      );
    eval.done("\"Talk -a -p 8080\" should call autoMode");
  }

  @Test
  @TestGroup("autoMode")
  @DisplayName("autoMode should only call clientMode when clientMode is successful using port arg")
  public void testAutoModeCallsClientModeButNotServerModeWithPortArg() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo(TalkTest.DEFAULT_HOST)));
            assertThat(portnumber, is(equalTo(8080)));
            eval.success();
            return true;
          }

          @Override
          public boolean serverMode(int portnumber) {
            eval.fail("autoMode should not call serverMode when clientMode is successful");
            return false;
          }
        };
    assertThat(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "-p", "8080"}),
        is(true)
      );
    eval.done("autoMode should only call clientMode when clientMode is successful");
  }

  @Test
  @TestGroup("autoMode")
  @DisplayName("autoMode should call serverMode when clientMode fails using port arg")
  public void testAutoModeCallsServerModeAfterClientModeFailsWithPortArg() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            return false;
          }

          @Override
          public boolean serverMode(int portnumber) {
            assertThat(portnumber, is(equalTo(8080)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "-p", "8080"}),
        is(true)
      );
    eval.done("autoMode should call serverMode when clientMode fails");
  }

  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -a test.utulsa.edu -p 8080\" should call autoMode")
  public void testStartCallsAutoModeWithHostAndPortArgs() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean autoMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo("test.utulsa.edu")));
            assertThat(portnumber, is(equalTo(8080)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "test.utulsa.edu", "-p", "8080"}),
        is(true)
      );
    eval.done("\"Talk -a test.utulsa.edu -p 8080\" should call autoMode");
  }

  @Test
  @TestGroup("autoMode")
  @DisplayName(
      "autoMode should only call clientMode when clientMode is successful using host and port args")
  public void testAutoModeCallsClientModeButNotServerModeWithHostAndPortArgs() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo("test.utulsa.edu")));
            assertThat(portnumber, is(equalTo(8080)));
            eval.success();
            return true;
          }

          @Override
          public boolean serverMode(int portnumber) {
            eval.fail("autoMode should not call serverMode when clientMode is successful");
            return false;
          }
        };
    assertThat(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "test.utulsa.edu", "-p", "8080"}),
        is(true)
      );
    eval.done("autoMode should only call clientMode when clientMode is successful");
  }

  @Test
  @TestGroup("autoMode")
  @DisplayName("autoMode should return false when clientMode and serverMode fail")
  public void testAutoModeReturnsFalseWhenClientModeAndServerModeBothFail() {
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            return false;
          }

          @Override
          public boolean serverMode(int portnumber) {
            return false;
          }
        };
    assertThat(
        "autoMode should return false when clientMode and serverMode fail",
        mock.autoMode("test.utulsa.edu", 8080),
        is(false)
      );
  }

  @Test
  @TestGroup("start")
  @DisplayName("start should return false when autoMode fails")
  public void testStartReturnsFalseWhenAutoModeFails() {
    Talk mock =
        new Talk() {
          @Override
          public boolean autoMode(String hostname, int portnumber) {
            return false;
          }
        };
    assertThat(
        "start should return false when autoMode fails",
        mock.start(new String[] {"-a"}),
        is(false)
      );
  }

  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -h\" should call clientMode")
  public void testStartCallsClientModeWithDefaultArgs() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo(TalkTest.DEFAULT_HOST)));
            assertThat(portnumber, is(equalTo(TalkTest.DEFAULT_PORT)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when clientMode is successful",
        mock.start(new String[] {"-h"}),
        is(true)
      );
    eval.done("\"Talk -h\" should call clientMode");
  }

  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -h test.utulsa.edu\" should call clientMode")
  public void testStartCallsClientModeWithHostArg() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo("test.utulsa.edu")));
            assertThat(portnumber, is(equalTo(TalkTest.DEFAULT_PORT)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when clientMode is successful",
        mock.start(new String[] {"-h", "test.utulsa.edu"}),
        is(true)
      );
    eval.done("\"Talk -h test.utulsa.edu\" should call clientMode");
  }

  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -h -p 8080\" should call clientMode")
  public void testStartCallsClientModeWithPortArg() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo(TalkTest.DEFAULT_HOST)));
            assertThat(portnumber, is(equalTo(8080)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when clientMode is successful",
        mock.start(new String[] {"-h", "-p", "8080"}),
        is(true)
      );
    eval.done("\"Talk -h -p 8080\" should call clientMode");
  }

  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -h test.utulsa.edu -p 8080\" should call clientMode")
  public void testStartCallsClientModeWithHostAndPortArgs() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertThat(hostname, is(equalTo("test.utulsa.edu")));
            assertThat(portnumber, is(equalTo(8080)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when clientMode is successful",
        mock.start(new String[] {"-h", "test.utulsa.edu", "-p", "8080"}),
        is(true)
      );
    eval.done("\"Talk -h test.utulsa.edu -p 8080\" should call clientMode");
  }

  @Test
  @TestGroup("clientMode")
  @DisplayName("clientMode should call asyncIO and not syncIO")
  public void testClientModeCallsAsyncIOAndNotSyncIO()
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
          NoSuchMethodException {
    final Eval eval = new Eval();
    Method clientMode = Talk.class.getDeclaredMethod("clientMode", BasicTalkInterface.class);
    clientMode.setAccessible(true);
    clientMode.invoke(
        new Talk(),
        new BasicTalkInterface() {
          public void asyncIO() throws IOException {
            eval.success();
            throw new IOException("__test__");
          }

          public void close() {}

          public String status() {
            return "";
          }

          public void syncIO() throws IOException {
            eval.fail("clientMode should not call syncIO");
            throw new IOException("__test__");
          }
        });
    eval.done("clientMode should call asyncIO and not syncIO");
  }

  @Test
  @TestGroup("start")
  @DisplayName("start returns false when clientMode fails")
  public void testStartReturnsFalseWhenClientModeFails() {
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            return false;
          }
        };
    assertThat(
        "start should return false when clientMode fails",
        mock.start(new String[] {"-h"}),
        is(false)
      );
  }

  @Test
  @TestGroup("start")
  @DisplayName("start reports when client cannot communicate with server")
  public void testStartReportsWhenClientIsUnableToCommunicateWithServer() {
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            return super.clientMode("__test__", 8080);
          }
        };
    ByteArrayOutputStream stdout = TUGrader.captureStdout();
    mock.start(new String[] {"-h"});
    String log = stdout.toString().trim();
    assertThat(log.contains("Client unable to communicate with server"), is(true));
  }

  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -help\" should call helpMode")
  public void testStartCallsHelpMode() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public void helpMode() {
            eval.success();
          }
        };
    assertThat(
        "start should return true after calling helpMode",
        mock.start(new String[] {"-help"}),
        is(true)
      );
    eval.done("\"Talk -help\" should call helpMode");
  }

  @Test
  @TestGroup("help")
  @DisplayName("help reports instructions")
  public void testHelpReportsInstructions() {
    ByteArrayOutputStream log = TUGrader.captureStdout();
    new Talk().start(new String[] {"-help"});
    assertThat(
        log
          .toString()
          .trim()
          .contains("Talk [-a | -h | -s] [hostname | IPaddress] [-p portnumber]"),
        is(true)
      );
  }

  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -s\" should call serverMode")
  public void testStartCallsServerModeWithDefaultArg() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean serverMode(int portnumber) {
            assertThat(portnumber, is(equalTo(TalkTest.DEFAULT_PORT)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when serverMode is successful",
        mock.start(new String[] {"-s"}),
        is(true)
      );
    eval.done("\"Talk -s\" should call serverMode");
  }

  @Test
  @TestGroup("start")
  @DisplayName("\"Talk -s -p 8080\" should call serverMode")
  public void testStartCallsServerModeWithPortArg() {
    final Eval eval = new Eval();
    Talk mock =
        new Talk() {
          @Override
          public boolean serverMode(int portnumber) {
            assertThat(portnumber, is(equalTo(8080)));
            eval.success();
            return true;
          }
        };
    assertThat(
        "start should return true when serverMode is successful",
        mock.start(new String[] {"-s", "-p", "8080"}),
        is(true)
      );
    eval.done("\"Talk -s -p 8080\" should call serverMode");
  }

  @Test
  @TestGroup("serverMode")
  @DisplayName("serverMode should call asyncIO and not syncIO")
  public void testServerModeCallsAsyncIOAndNotSyncIO()
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
          NoSuchMethodException {
    final Eval eval = new Eval();
    Method serverMode = Talk.class.getDeclaredMethod("serverMode", BasicTalkInterface.class);
    serverMode.setAccessible(true);
    serverMode.invoke(
        new Talk(),
        new BasicTalkInterface() {
          public void asyncIO() throws IOException {
            eval.success();
            throw new IOException("__test__");
          }

          public void close() {}

          public String status() {
            return "";
          }

          public void syncIO() throws IOException {
            eval.fail("serverMode should not call asyncIO");
            throw new IOException("__test__");
          }
        });
    eval.done("serverMode should call asyncIO and not syncIO");
  }

  @Test
  @TestGroup("start")
  @DisplayName("start returns false when serverMode fails")
  public void testStartReturnsFalseWhenServerModeFails() {
    Talk mock =
        new Talk() {
          @Override
          public boolean serverMode(int portnumber) {
            return false;
          }
        };
    assertThat(
        "start should return false when serverMode fails",
        mock.start(new String[] {"-s"}),
        is(false)
      );
  }

  @Test
  @TestGroup("start")
  @DisplayName("start reports when server cannot listen on specified port")
  public void testStartReportsWhenServerIsUnableToListenOnSpecifiedPort() {
    Talk mock =
        new Talk() {
          @Override
          public boolean serverMode(int portnumber) {
            try {
              ServerSocket server = new ServerSocket(portnumber);
              boolean flag = super.serverMode(portnumber);
              server.close();
              return flag;
            } catch (IOException e) {
              return false;
            }
          }
        };
    ByteArrayOutputStream stdout = TUGrader.captureStdout();
    mock.start(new String[] {"-s"});
    String log = stdout.toString().trim();
    assertThat(log.contains("Server unable to listen on specified port"), is(true));
  }

  @Test
  @TestGroup("talkClient")
  @DisplayName("TalkClient sends STDIN messages to client using asyncIO")
  public void testTalkClientAsyncIOReportsStdInMessagesToServer()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    StringBuilder msg = new StringBuilder();
    MockSocket socket =
        new MockSocket(
            new OutputStream() {
              public void write(int b) {
                msg.append((char) b);
              }
            });
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    TUGrader.flushToStdin("__test__");
    TalkClient client = constructor.newInstance(socket);
    client.asyncIO();
    assertThat(msg.toString().trim(), is(equalTo("__test__")));
  }

  @Test
  @TestGroup("talkClient")
  @DisplayName("TalkClient reports client messages on STDOUT using asyncIO")
  public void testTalkClientAsyncIOReportsServerMessagesOnStdOut()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              String msg = "__test__\n";
              int cursor = 0;

              @Override
              public int available() {
                return msg.length() - cursor;
              }

              public int read() {
                if (msg.length() <= cursor) return '\0';
                return msg.charAt(cursor++);
              }
            });
    ByteArrayOutputStream stdout = TUGrader.captureStdout();
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    TalkClient client = constructor.newInstance(socket);
    client.asyncIO();
    String log = stdout.toString().trim();
    assertThat(log, is(equalTo("[remote] __test__")));
  }

  @Test
  @TestGroup("talkClient")
  @DisplayName("TalkClient sends STDIN messages to client using syncIO")
  public void testTalkClientSyncIOReportsStdInMessagesToServer()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    StringBuilder msg = new StringBuilder();
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              @Override
              public int available() {
                return 1;
              }

              public int read() {
                return '\n';
              }
            },
            new OutputStream() {
              public void write(int b) {
                msg.append((char) b);
              }
            });
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    TUGrader.flushToStdin("__test__");
    TalkClient client = constructor.newInstance(socket);
    client.syncIO();
    assertThat(msg.toString().trim(), is(equalTo("__test__")));
  }

  @Test
  @TestGroup("talkClient")
  @DisplayName("TalkClient reports client messages on STDOUT using syncIO")
  public void testTalkClientSyncIOReportsServerMessagesOnStdOut()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              String msg = "__test__\n";
              int cursor = 0;

              @Override
              public int available() {
                return 5;
              }

              public int read() {
                if (msg.length() <= cursor) return '\0';
                return msg.charAt(cursor++);
              }
            });
    ByteArrayOutputStream stdout = TUGrader.captureStdout();
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    TUGrader.flushToStdin("\n");
    TalkClient client = constructor.newInstance(socket);
    client.syncIO();
    String log = stdout.toString().trim();
    assertThat(log, is(equalTo("[remote] __test__")));
  }

  @Test
  @TestGroup("talkClient")
  @DisplayName("TalkClient reports STATUS")
  public void testTalkClientReportsStatus()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    MockSocket socket = new MockSocket();
    String status = constructor.newInstance(socket).status();
    assertThat(
        "TalkClient should report address",
        status.contains(socket.getInetAddress().toString()),
        is(true)
      );
    assertThat(
        "TalkClient should report local address",
        status.contains(socket.getLocalAddress().toString()),
        is(true)
      );
    assertThat(
        "TalkClient should report port",
        status.contains(Integer.toString(socket.getPort())),
        is(true)
      );
    assertThat(
        "TalkClient should report local port",
        status.contains(Integer.toString(socket.getLocalPort())),
        is(true)
      );
  }

  @Test
  @TestGroup("talkClient")
  @DisplayName("TalkClient closes sockets when closed")
  public void testTalkClientClosesSocketOnClose()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    MockSocket socket = new MockSocket();
    TalkClient client = constructor.newInstance(socket);
    client.close();
    assertThat("TalkClient should close socket upon close", socket.isClosed(), is(true));
  }

  @Test
  @TestGroup("talkServer")
  @DisplayName("TalkServer reports client messages on STDOUT using asyncIO")
  public void testTalkServerAsyncIOReportsClientMessagesOnStdOut()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              String msg = "__test__\n";
              int cursor = 0;

              @Override
              public int available() {
                return msg.length() - cursor;
              }

              public int read() {
                if (msg.length() <= cursor) return '\0';
                return msg.charAt(cursor++);
              }
            });
    ByteArrayOutputStream stdout = TUGrader.captureStdout();
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    TalkServer server = constructor.newInstance(new MockServerSocket(socket));
    server.asyncIO();
    String log = stdout.toString().trim();
    assertThat(log, is(equalTo("[remote] __test__")));
  }

  @Test
  @TestGroup("talkServer")
  @DisplayName("TalkServer sends STDIN messages to client using asyncIO")
  public void testTalkServerAsyncIOReportsStdInMessagesToClient()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    StringBuilder msg = new StringBuilder();
    MockSocket socket =
        new MockSocket(
            new OutputStream() {
              public void write(int b) {
                msg.append((char) b);
              }
            });
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    TUGrader.flushToStdin("__test__");
    TalkServer server = constructor.newInstance(new MockServerSocket(socket));
    server.asyncIO();
    assertThat(msg.toString().trim(), is(equalTo("__test__")));
  }

  @Test
  @TestGroup("talkServer")
  @DisplayName("TalkServer reports client messages on STDOUT using syncIO")
  public void testTalkServerSyncIOReportsClientMessagesOnStdOut()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              String msg = "__test__\n";
              int cursor = 0;

              @Override
              public int available() {
                return msg.length() - cursor;
              }

              public int read() {
                if (msg.length() <= cursor) return '\0';
                return msg.charAt(cursor++);
              }
            });
    ByteArrayOutputStream stdout = TUGrader.captureStdout();
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    TUGrader.flushToStdin("\n");
    TalkServer server = constructor.newInstance(new MockServerSocket(socket));
    server.syncIO();
    String log = stdout.toString().trim();
    assertThat(log, is(equalTo("[remote] __test__")));
  }

  @Test
  @TestGroup("talkServer")
  @DisplayName("TalkServer sends STDIN messages to client using syncIO")
  public void testTalkServerSyncIOReportsStdInMessagesToClient()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    StringBuilder msg = new StringBuilder();
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              @Override
              public int available() {
                return 1;
              }

              public int read() {
                return '\n';
              }
            },
            new OutputStream() {
              public void write(int b) {
                msg.append((char) b);
              }
            });
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    TUGrader.flushToStdin("__test__");
    TalkServer server = constructor.newInstance(new MockServerSocket(socket));
    server.syncIO();
    assertThat(msg.toString().trim(), is(equalTo("__test__")));
  }

  @Test
  @TestGroup("talkServer")
  @DisplayName("TalkServer reports STATUS")
  public void testTalkServerReportsStatus()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    MockServerSocket server = new MockServerSocket();
    String status = constructor.newInstance(server).status();
    assertThat(
        "TalkServer should report address",
        status.contains(server.getInetAddress().toString()),
        is(true)
      );
    assertThat(
        "TalkServer should report local port",
        status.contains(Integer.toString(server.getLocalPort())),
        is(true)
      );
  }

  @Test
  @TestGroup("talkServer")
  @DisplayName("TalkServer closes sockets when closed")
  public void testTalkServerClosesSocketOnClose()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    MockSocket socket = new MockSocket();
    MockServerSocket serverSocket = new MockServerSocket(socket);
    TalkServer server = constructor.newInstance(serverSocket);
    server.close();
    assertThat("TalkServer should close client socket upon close", socket.isClosed(), is(true));
    assertThat(
        "TalkServer should close server socket upon close",
        serverSocket.isClosed(),
        is(true)
      );
  }
}
