import java.awt.Color;

abstract class GomokuPlayer
{
  GomokuPlayer() {}
  
  public abstract Move chooseMove(Color[][] paramArrayOfColor, Color paramColor);
}
