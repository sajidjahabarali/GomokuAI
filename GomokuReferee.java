import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.io.File;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class GomokuReferee extends Frame implements Runnable
{
  protected static boolean batch = false;
  protected static boolean log = false;
  private static double timeLimit = 10.0D;
  protected static double pauseTime = 1.0D;
  




  boolean pause = false;
  Thread playThread = null;
  Choice whiteChoice = new Choice();
  Choice blackChoice = new Choice();
  Class[] players = new Class[100];
  int playerCount = 0;
  GomokuBoard b;
  GomokuPlayer whitePlayer;
  GomokuPlayer blackPlayer;
  Button startButton;
  Button quitButton;
  Panel controls;
  Label statusLabel;
  static final int space = 80;
  static final int MAXPLAYERS = 100;
  
  public GomokuReferee()
  {
    super("Gomoku AI Player Test Platform");
    
    Object localObject1;
    Object localObject2;
    Object localObject3;
    Object localObject4;
    try
    {
      File localFile = new File(".");
      localObject1 = localFile.list(new GomokuReferee.1(this));
      


      localObject2 = localFile.toURL();
      localObject3 = new URL[] { localObject2 };
      localObject4 = new URLClassLoader((URL[])localObject3);
      for (String str : localObject1)
        try {
          str = str.substring(0, str.length() - 6);
          Class localClass = ((ClassLoader)localObject4).loadClass(str);
          
          if ((GomokuPlayer.class.isAssignableFrom(localClass)) && (localClass != GomokuPlayer.class) && ((!batch) || (localClass != HumanPlayer.class)))
          {

            int k = playerCount;
            for (;;) { k--; if ((k < 0) || (whiteChoice.getItem(k).compareTo(str) <= 0))
                break;
              players[(k + 1)] = players[k]; }
            whiteChoice.insert(str, k + 1);
            blackChoice.insert(str, k + 1);
            players[(k + 1)] = localClass;
            playerCount += 1;
          }
        } catch (ClassNotFoundException localClassNotFoundException) {
          System.err.println(localClassNotFoundException);
        }
    } catch (MalformedURLException localMalformedURLException) {
      System.err.println(localMalformedURLException);
    }
    b = new GomokuBoard();
    if (!batch) {
      b.setSize(8 * 80, 8 * 80);
      setSize(8 * 80 + 10, 8 * 80 + 80);
      setResizable(false);
      setLayout(new BorderLayout());
      add("North", b);
      Label localLabel = new Label("Status:");
      localObject1 = new Label("White:");
      localObject2 = new Label("Black:");
      localObject3 = new Label("");
      localObject4 = new Label("");
      statusLabel = new Label("White to start       ");
      statusLabel.setForeground(Color.blue);
      startButton = new Button("New game");
      quitButton = new Button("Quit");
      controls = new Panel();
      controls.setLayout(new GridLayout(2, 5));
      controls.add(localLabel);
      controls.add((Component)localObject1);
      controls.add((Component)localObject2);
      controls.add((Component)localObject3);
      controls.add((Component)localObject4);
      controls.add(statusLabel);
      controls.add(whiteChoice);
      controls.add(blackChoice);
      controls.add(startButton);
      controls.add(quitButton);
      add("South", controls);
      setVisible(true);
    }
  }
  
  public void init() {
    if (!batch)
      statusLabel.setText("White starts");
    b.init();
    try {
      whitePlayer = ((GomokuPlayer)players[whiteChoice.getSelectedIndex()].newInstance());
      
      blackPlayer = ((GomokuPlayer)players[blackChoice.getSelectedIndex()].newInstance());
      
      pause = ((!batch) && (!(whitePlayer instanceof HumanPlayer)) && (!(blackPlayer instanceof HumanPlayer)));
    }
    catch (Exception localException) {
      System.err.println(localException);
      System.exit(1);
    }
    b.repaint();
  }
  



  public void run()
  {
    ThreadMXBean localThreadMXBean = ManagementFactory.getThreadMXBean();
    if (!localThreadMXBean.isCurrentThreadCpuTimeSupported())
      localThreadMXBean = null;
    while (b.getWinner() == null) {
      long l1 = localThreadMXBean == null ? 0L : localThreadMXBean.getCurrentThreadCpuTime();
      Color localColor = b.getTurn();
      Move localMove;
      try { if (localColor == Color.white) {
          localMove = whitePlayer.chooseMove(b.getPublicBoard(), Color.white);
        } else
          localMove = blackPlayer.chooseMove(b.getPublicBoard(), Color.black);
      } catch (Exception localException) {
        localMove = null;
      }
      long l2 = localThreadMXBean == null ? 0L : localThreadMXBean.getCurrentThreadCpuTime();
      double d = (l2 - l1) / 1.0E9D;
      String str; if (d > timeLimit) {
        b.makeMove(null, localColor);
        str = "Time limit exceeded";
      } else {
        str = b.makeMove(localMove, localColor); }
      if ((batch) || (log)) {
        System.out.println(colorToString(localColor) + ": " + localMove);
        if (b.getWinner() != null) {
          System.out.println(str + "\n" + colorToString(b.getWinner()) + " WINS");
        }
      }
      if (!batch) {
        statusLabel.setText(str);
        b.repaint();
      }
      if ((pause) && (d < pauseTime))
        try { Thread.sleep((int)((pauseTime - d) * 1000.0D));
        } catch (InterruptedException localInterruptedException) {}
    }
    playThread = null;
  }
  
  public void update() {
    b.repaint();
  }
  
  public boolean mouseDown(Event paramEvent, int paramInt1, int paramInt2) {
    if ((target == b) && (b.getWinner() == null)) {
      int i = (paramInt1 - 1) / 80;
      int j = (paramInt2 - 1) / 80;
      if ((b.getTurn() == Color.white) && ((whitePlayer instanceof HumanPlayer)))
      {
        whitePlayer).clicked = new Move(j, i);
        synchronized (whitePlayer) {
          whitePlayer.notifyAll();
        }
      } else if ((b.getTurn() == Color.black) && ((blackPlayer instanceof HumanPlayer)))
      {
        blackPlayer).clicked = new Move(j, i);
        synchronized (blackPlayer) {
          blackPlayer.notifyAll();
        }
      }
    }
    return true;
  }
  
  public boolean action(Event paramEvent, Object paramObject) {
    if (target == startButton) {
      if (playThread != null) {
        playThread.stop();
      }
      init();
      playThread = new Thread(this);
      playThread.start();
    }
    if (target == quitButton) {
      setVisible(false);
      dispose();
      System.exit(0);
    }
    return true;
  }
  
  public static String colorToString(Color paramColor) {
    if (paramColor == null)
      return "NULL";
    if (paramColor == Color.white)
      return "White";
    if (paramColor == Color.black)
      return "Black";
    return "None";
  }
  
  public static void main(String[] paramArrayOfString) {
    for (int i = 0; i < paramArrayOfString.length; i++) {
      if (paramArrayOfString[i].equals("batchTest")) {
        batch = true;
      } else if (paramArrayOfString[i].equals("log")) {
        log = true;
      } else if ((paramArrayOfString[i].equals("limit")) && (i + 1 < paramArrayOfString.length)) {
        timeLimit = Double.parseDouble(paramArrayOfString[(++i)]);
      } else if ((paramArrayOfString[i].equals("delay")) && (i + 1 < paramArrayOfString.length))
        pauseTime = Double.parseDouble(paramArrayOfString[(++i)]);
    }
    GomokuReferee localGomokuReferee = new GomokuReferee();
    if (batch) {
      int[][] arrayOfInt = new int[playerCount][playerCount];
      int k; int n; int i1; int m; for (int j = 0; j < playerCount; j++)
        for (k = 0; k < playerCount; k++)
          if ((paramArrayOfString.length != 2) || (paramArrayOfString[1].equals(whiteChoice.getItem(j))) || (paramArrayOfString[1].equals(whiteChoice.getItem(k))))
          {


            System.out.println("White: " + whiteChoice.getItem(j) + " vs Black: " + whiteChoice.getItem(k));
            try
            {
              arrayOfInt[j][k] = 0;
              whitePlayer = ((GomokuPlayer)players[j].newInstance());
              arrayOfInt[j][k] = 2;
              blackPlayer = ((GomokuPlayer)players[k].newInstance());
            } catch (Exception localException) {
              System.out.println(localException);
              System.out.println(arrayOfInt[j][k] == 0 ? "Black WINS" : "White WINS");
              
              continue;
            }
            playThread = new Thread(localGomokuReferee);
            b.init();
            playThread.start();
            
            n = b.getMoveCount();
            i1 = 0;
            while (b.getWinner() == null) {
              try {
                Thread.currentThread();Thread.sleep(200L);
                i1++;
              } catch (InterruptedException localInterruptedException) {}
              m = b.getMoveCount();
              if (m > n) {
                n = m;
                i1 = 0;

              }
              else if (i1 / 5.0D > timeLimit)
              {
                if (b.getWinner() == null) {
                  System.out.println("No progress");
                  playThread.suspend();
                  if (b.getWinner() != null) {
                    playThread.resume();
                  } else {
                    playThread.stop();
                    b.makeMove(null, b.getTurn());
                    System.out.println("Time limit exceeded\n" + colorToString(b.getWinner()) + " WINS");
                  }
                }
                else {
                  n = m;
                } } }
            if (b.getWinner() == Color.white) {
              arrayOfInt[j][k] = 2;
            } else if (b.getWinner() == Color.black) {
              arrayOfInt[j][k] = 0;
            } else
              arrayOfInt[j][k] = 1;
          }
      for (j = 0; j < playerCount; j++) {
        k = 0;m = 0;n = 0;i1 = 0;int i2 = 0;
        for (int i3 = 0; i3 < playerCount; i3++) {
          k += arrayOfInt[j][i3];
          m += 2 - arrayOfInt[i3][j];
          if (arrayOfInt[j][i3] == 2)
            n++;
          if (arrayOfInt[i3][j] == 0)
            n++;
          if (arrayOfInt[j][i3] == 1)
            i1++;
          if (arrayOfInt[i3][j] == 1)
            i1++;
          if (arrayOfInt[j][i3] == 0)
            i2++;
          if (arrayOfInt[i3][j] == 2)
            i2++;
        }
        System.out.println(whiteChoice.getItem(j) + " " + n + " " + i1 + " " + i2 + " " + k + " " + m + " " + (k + m));
      }
      

      System.exit(0);
    }
  }
}
