public class Move {
  public int row;
  public int col;
  
  public Move(int paramInt1, int paramInt2) { row = paramInt1;
    col = paramInt2;
  }
  
  public String toString() {
    return "(" + row + "," + col + ")";
  }
}
