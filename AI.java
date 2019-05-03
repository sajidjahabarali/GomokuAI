import java.awt.Color;

public class AI extends GomokuPlayer{

  public Move chooseMove(Color[][] board, Color myColour){


    boolean emptyBoard = checkEmptyBoard(board);
    if(emptyBoard){
      return new Move(4, 4);
    }else{
      return pickMove(board, myColour, true);
    }
  }

  public static Move pickMove(Color[][] board, Color myColour, boolean maxPlayer){
    int bestValue = 0;
    int currentValue = 0;
    final int maxDepth = 3;
    int[] move = new int[2];
    Move myMove = randomMove(board, myColour);

    Color[][] boardCopy = copyBoard(board);


          move = maxPlayer(boardCopy, myColour, 0, maxDepth, (int)Double.POSITIVE_INFINITY);
          myMove = new Move(move[0], move[1]);

    return myMove;
  }

  public static Move randomMove(Color[][] board, Color myColour){
    while (true) {
      int row = (int) (Math.random() * 8);	// values are from 0 to 7
      int col = (int) (Math.random() * 8);
      if (board[row][col] == null)			// is the square vacant?
        return new Move(row, col);
    }
  }

  public static Color[][] copyBoard(Color[][] board){

    Color[][] boardCopy = new Color[8][8];

    for(int i = 0; i<board.length; i++){
      for(int j = 0; j<board[i].length; j++){
        boardCopy[i][j] = board[i][j];
      }
    }

    return boardCopy;

  }

  public static boolean checkEmptyBoard(Color[][] board){
    for(Color[] a : board){
      for(Color b : a){
        if(b != null){
          return false;
        }
      }
    }

    return true;
  }

  public static int[] maxPlayer(Color[][] board, Color myColour, int depth, int maxDepth, int bestPrev){
    int[] myMove = new int[2];
    int bestValue = (int)Double.NEGATIVE_INFINITY;
    int currentValue = (int)Double.NEGATIVE_INFINITY;
    int[] minMove;

      for(int row = 0; row<board.length; row++){
        for(int col = 0; col<board[row].length; col++){
          if (board[row][col] == null){
            Color[][] boardCopy = copyBoard(board);
            if(depth!=maxDepth){
              boardCopy[row][col] = myColour;
              minMove = minPlayer(boardCopy, myColour, depth++, maxDepth, bestValue);
              currentValue = calcMoveValue(boardCopy, myColour, minMove[0], minMove[1]);
            }else{
              currentValue = calcMoveValue(boardCopy, myColour, row, col);
            }
          }
          if(currentValue>bestPrev){
            myMove[0] = row;
            myMove[1] = col;
            return myMove;
          }
          if(currentValue>bestValue){
            myMove[0] = row;
            myMove[1] = col;
            bestValue = currentValue;
            currentValue = (int)Double.NEGATIVE_INFINITY;
          }
        }
      }
    return myMove;
  }

  public static int[] minPlayer(Color[][] board, Color myColour, int depth, int maxDepth, int bestPrev){
    int[] myMove = new int[2];
    int bestValue = (int)Double.POSITIVE_INFINITY;
    int currentValue = (int)Double.POSITIVE_INFINITY;
    int[] maxMove;
    Color opponentColour;
    if(myColour == Color.WHITE){
      opponentColour = Color.BLACK;
    }
    else{
      opponentColour = Color.WHITE;
    }
      for(int row = 0; row<board.length; row++){
        for(int col = 0; col<board[row].length; col++){
          if (board[row][col] == null){
            Color[][] boardCopy = copyBoard(board);
            if(depth!=maxDepth){
              boardCopy[row][col] = opponentColour;
              maxMove = maxPlayer(boardCopy, myColour, depth++, maxDepth, bestValue);
              currentValue = calcMoveValue(boardCopy, myColour, maxMove[0], maxMove[1]);
            }else{
              currentValue = calcMoveValue(boardCopy, myColour, row, col);
            }
          }

          if(currentValue<bestPrev){
            myMove[0] = row;
            myMove[1] = col;
            return myMove;
          }
          if(currentValue<bestValue){
            myMove[0] = row;
            myMove[1] = col;
            bestValue = currentValue;
            currentValue=(int)Double.POSITIVE_INFINITY;
          }
        }
      }
    return myMove;
  }

  public static int calcMoveValue(Color[][] board, Color myColour, int row, int col){
    int horizontalLineValue = checkHorizontal(board, myColour, row, col);
    int verticalLineValue = checkVertical(board, myColour, row, col);
    int diagonalLinesValue = checkDiagonals(board, myColour, row, col);

    int sum = horizontalLineValue + verticalLineValue + diagonalLinesValue;
    return sum;
  }

  public static int checkHorizontal(Color[][] board, Color myColour, int row, int col){
    return buildMyHorizontal(board, myColour, row, col) + stopOpponentsHorizontal(board, myColour, row, col);
  }

  public static int buildMyHorizontal(Color[][] board, Color myColour, int row, int col){

    int streak = 0;
    int i = col+1;
    int j = col-1;
    boolean open3 = false;

    try{



        for(i = col+1; i<=col+4; i++){
          if(i>=0 && i<=7){
            if(board[row][i]==myColour){
              streak++;
            }
            else if(board[row][i]==null){
              if((streak == 3) || ((streak == 2) && board[i+1][col]==myColour)) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
          }
        }



        for(j = col-1; j>=col-4; j--){
          if(j>=0 && j<=7){
            if(board[row][j]==myColour){
              streak++;
            }
            else if(board[row][j]==null){
              if((streak == 3) || ((streak == 2) && board[j+1][col]==myColour)){
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
          }
        }



    }catch(Exception e){
      //System.out.println("Exception");
    }
    //p(""+streak);
    if(streak == 4){
      return ((int)Math.pow(100, streak));
    }
    if(open3){
      return ((int)Math.pow(50, streak));
    }
    return ((int)Math.pow(10, streak));
  }

  public static int stopOpponentsHorizontal(Color[][] board, Color myColour, int row, int col){

    int streak = 0;
    int i = col+1;
    int j = col-1;
    boolean open3 = false;

    try{



        for(i = col+1; i<=col+4; i++){
          if(i>=0 && i<=7){
            if((board[row][i]!=myColour)&&(board[row][i]!=null)){
              streak++;
            }
            else if(board[row][i]==null){
              if((streak == 3) || ((streak == 2) && (board[row][i+1]!=myColour)&&(board[row][i+1]!=null))) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
          }

        }



        for(j = col-1; j>=col-4; j--){
          if(j>=0 && j<=7){
            if((board[row][j]!=myColour)&&(board[row][j]!=null)){
              streak++;
            }
            else if(board[row][j]==null){
              if((streak == 3) || ((streak == 2) && (board[row][j-1]!=myColour)&&(board[row][j-1]!=null))) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
          }

        }


        // p(""+streak);
    }catch(Exception e){
      //System.out.println("Exception");
    }
    if(streak == 4){
      return ((int)Math.pow(50, streak));
    }
    if(open3){
      return ((int)Math.pow(25, streak));
    }
    return ((int)Math.pow(10, streak));

  }

  public static int checkVertical(Color[][] board, Color myColour, int row, int col){
    return buildMyVertical(board, myColour, row, col) + stopOpponentsVertical(board, myColour, row, col);
  }

  public static int buildMyVertical(Color[][] board, Color myColour, int row, int col){
    int streak = 0;
    int i = row+1;
    int j = row-1;
    boolean open3 = false;

    try{



        for(i = row+1; i<=row+4; i++){
          if(i>=0 && i<=7){
            if(board[i][col]==myColour){
              streak++;
            }
            else if(board[i][col]==null){
              if((streak == 3) || ((streak == 2) && board[i+1][col]==myColour)){
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
          }

        }


        for(j = row-1; j>=row-4; j--){
          if(j>=0 && j<=7){
            if(board[j][col]==myColour){
              streak++;
            }
            else if(board[j][col]==null){
              if((streak == 3) || ((streak == 2) && board[j+1][col]==myColour)){
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
          }

        }


    }catch(Exception e){
      //System.out.println("Exception");
    }
    //p(""+streak);
    if(streak == 4){
      return ((int)Math.pow(100, streak));
    }
    if(open3){
      return ((int)Math.pow(50, streak));
    }
    return ((int)Math.pow(10, streak));
  }

  public static int stopOpponentsVertical(Color[][] board, Color myColour, int row, int col){

      int streak = 0;
      int i = row+1;
      int j = row-1;
      boolean open3 = false;

      try{

          for(i = row+1; i<=row+4; i++){
            if(i>=0 && i<=7){
              if((board[i][col]!=myColour)&&(board[i][col]!=null)){
                streak++;
              }
              else if(board[i][col]==null){
                if((streak == 3) || ((streak == 2) && (board[row][i+1]!=myColour)&&(board[row][i+1]!=null))) {
                  open3 = true;
                  break;
                }
              }
              else{
                break;
              }
            }
          }



          for(j = row-1; j>=row-4; j--){
            if(j>=0 && j<=7){
              if((board[j][col]!=myColour)&&(board[j][col]!=null)){
                streak++;
              }
              else if(board[j][col]==null){
                if((streak == 3) || ((streak == 2) && (board[row][j-1]!=myColour)&&(board[row][j-1]!=null))) {
                  open3 = true;
                  break;
                }
              }
              else{
                break;
              }
            }
          }



      }catch(Exception e){
        //System.out.println("Exception");
      }
    //p(""+streak);
    if(streak == 4){
      return ((int)Math.pow(50, streak));
    }
    if(open3){
      return ((int)Math.pow(25, streak));
    }
    return ((int)Math.pow(10, streak));
  }

  public static int checkDiagonals(Color[][] board, Color myColour, int row, int col){
    int posDiagonalScore = buildMyDiagonalPos(board, myColour, row, col) + stopOpponentsDiagonalPos(board, myColour, row, col);
    int negDiagonalScore = buildMyDiagonalNeg(board, myColour, row, col) + stopOpponentsDiagonalNeg(board, myColour, row, col);

    return posDiagonalScore + negDiagonalScore;
  }

  public static int buildMyDiagonalPos(Color[][] board, Color myColour, int row, int col){

    int streak = 0;
    int a = row-1;
    int b = col+1;
    int c = row+1;
    int d = col-1;
    boolean open3 = false;

    try{


        for(a = row-1; a<=row-4; a--){
          if((a>=0 && a<=7)&&(b>=0 && b<=7)){
            if(board[a][b]==myColour){
              streak++;
            }
            else if(board[a][b]==null){
              if((streak == 3) || ((streak == 2) && (board[a-1][b+1]==myColour))) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
            b++;
          }
        }



        for(c = row+1; c>=row+4; c++){
          if((c>=0 && c<=7)&&(d>=0 && d<=7)){
            if(board[c][d]==myColour){
              streak++;
            }
            else if(board[c][d]==null){
              if((streak == 3) || ((streak == 2) && (board[c+1][d-1]==myColour))) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
            d--;
          }
        }


    }catch(Exception e){
      //System.out.println("Exception");
    }
    //p(""+streak);
    if(streak == 4){
      return ((int)Math.pow(100, streak));
    }
    if(open3){
      return ((int)Math.pow(50, streak));
    }
    return ((int)Math.pow(10, streak));

  }

  public static int stopOpponentsDiagonalPos(Color[][] board, Color myColour, int row, int col){
    int streak = 0;
    int a = row+1;
    int b = col-1;
    int c = row-1;
    int d = col+1;
    boolean open3 = false;

    try{


        for(a = row+1; a<=row+4; a++){
          if((a>=0 && a<=7)&&(b>=0 && b<=7)){
            if((board[a][b]!=myColour)&&(board[a][b]!=null)){
              streak++;
            }
            else if(board[a][b]==null){
              if((streak == 3) || ((streak == 2) && ((board[a+1][b-1]!=myColour)&&(board[a+1][b-1]!=null)))) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
            b--;
          }
        }



        for(c = row-1; c>=row-4; c--){
          if((c>=0 && c<=7)&&(d>=0 && d<=7)){
            if((board[c][d]!=myColour)&&(board[c][d]!=null)){
              streak++;
            }
            else if(board[c][d]==null){
              if((streak == 3) || ((streak == 2) && ((board[c-1][d+1]!=myColour)&&(board[c-1][d+1]!=null)))) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
            d++;
          }
        }


    }catch(Exception e){
      //System.out.println("Exception");
    }
    //p(""+streak);
    if(streak == 4){
      return ((int)Math.pow(50, streak));
    }
    if(open3){
      return ((int)Math.pow(25, streak));
    }
    return ((int)Math.pow(10, streak));
  }

  public static int buildMyDiagonalNeg(Color[][] board, Color myColour, int row, int col){
    int streak = 0;
    int a = row+1;
    int b = col+1;
    int c = row-1;
    int d = col-1;
    boolean open3 = false;

    try{


        for(a = row+1; a<=row+4; a++){
          if((a>=0 && a<=7)&&(b>=0 && b<=7)){
            if(board[a][b]==myColour){
              streak++;
            }
            else if(board[a][b]==null){
              if((streak == 3) || ((streak == 2) && (board[a+1][b+1]==myColour))) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
            b++;
          }
        }



        for(c = row-1; c>=row-4; c--){
          if((c>=0 && c<=7)&&(d>=0 && d<=7)){
            if(board[c][d]==myColour){
              streak++;
            }
            else if(board[c][d]==null){
              if((streak == 3) || ((streak == 2) && (board[c-1][d-1]==myColour))) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
            d--;
          }
        }


    }catch(Exception e){
      //System.out.println("Exception");
    }
    //p(""+streak);
    if(streak == 4){
      return ((int)Math.pow(100, streak));
    }
    if(open3){
      return ((int)Math.pow(50, streak));
    }
    return ((int)Math.pow(10, streak));
  }

  public static int stopOpponentsDiagonalNeg(Color[][] board, Color myColour, int row, int col){
    int streak = 0;
    int a = row+1;
    int b = col+1;
    int c = row-1;
    int d = col-1;
    boolean open3 = false;

    try{


        for(a = row+1; a<=row+4; a++){
          if((a>=0 && a<=7)&&(b>=0 && b<=7)) {
            if((board[a][b]!=myColour)&&(board[a][b]!=null)){
              streak++;
            }
            else if(board[a][b]==null){
              if((streak == 3) || ((streak == 2) && ((board[a+1][b+1]!=myColour)&&(board[a+1][b+1]!=null)))) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
            b++;
          }
        }



        for(c = row-1; c>=row-4; c--){
          if((c>=0 && c<=7)&&(d>=0 && d<=7)){
            if((board[c][d]!=myColour)&&(board[c][d]!=null)){
              streak++;
            }
            else if(board[c][d]==null){
              if((streak == 3) || ((streak == 2) && ((board[c-1][d-1]!=myColour)&&(board[c-1][d-1]!=null)))) {
                open3 = true;
                break;
              }
            }
            else{
              break;
            }
            d--;
          }
        }


    }catch(Exception e){
      //System.out.println("Exception");
    }
    //p(""+streak);
    if(streak == 4){
      return ((int)Math.pow(50, streak));
    }
    if(open3){
      return ((int)Math.pow(25, streak));
    }
    return ((int)Math.pow(10, streak));
  }

  public static void p(String p){
    System.out.println(p);
    return;
  }


}
