import java.awt.Color;

class HumanPlayer extends GomokuPlayer
{
  public Move clicked = null;
  
  HumanPlayer() {}
  
  public synchronized Move chooseMove(Color[][] paramArrayOfColor, Color paramColor) { for (;;) { clicked = null;
      try {
        wait();
      } catch (InterruptedException localInterruptedException) {}
      Move localMove = clicked;
      if ((localMove != null) && (row >= 0) && (row < 8) && (col >= 0) && (col < 8) && (paramArrayOfColor[row][col] == null))
      {

        return localMove;
      }
      System.err.println("Illegal move: " + localMove);
    }
  }
}
