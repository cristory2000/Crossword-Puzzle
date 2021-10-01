import java.util.*;
import java.io.*;




public class Crossword 
{
    private static char [][] crossword;
    private static DictInterface D;
    static StringBuilder colStr[];
    static StringBuilder rowStr[];
    static int BoardSize;
    static int score;
    static Map<Character,Character> letters= new HashMap<>();
    private static long previousTime;
    public static void main(String args[]) throws IOException
    {
            //input the dictionary file through arguments
            
            previousTime = System.currentTimeMillis();
            Scanner fileScan = new Scanner(new FileInputStream(args[0]));
            Scanner boardScan = new Scanner(new FileInputStream(args[1]));
            Scanner scoreBoard = new Scanner(new FileInputStream("letterpoints.txt"));
            //add dictionary file and input the the dict8.txt file
            D=new MyDictionary();
            while(fileScan.hasNext())
            {
                D.add(fileScan.next());
            }
            fileScan.close();
            //create the crossword and input the chosen test file
            crosswordBoardcreator(boardScan);
            //making the score hashtable for the crossword
            while(scoreBoard.hasNext())
            {
                String line=scoreBoard.nextLine();
                letters.put(Character.toLowerCase(line.charAt(0)), line.charAt(2));
            }
            //initialize strinbuilder arrays  with sizes
            colStr= new StringBuilder[BoardSize];
            rowStr= new StringBuilder[BoardSize];
               //initalize all stribuilders so they are not null
            for (int i = 0; i < colStr.length; i++) {
                colStr[i] = new StringBuilder("");
                rowStr[i] = new StringBuilder("");
            }
            //crossword backtracking algorithm that fills crossword    
            crossFiller(0,0, rowStr, colStr);           
    }
    public static void crosswordBoardcreator(Scanner board) throws FileNotFoundException
      { 
            // Scanner reader;
            Scanner scan=board;
            //create crossword board and input values
            String line=scan.nextLine();
            BoardSize=Integer.parseInt(line);
            crossword= new char[BoardSize][BoardSize];

           //fill crowssword with test file
            for(int i=0;i<BoardSize;i++)
            {
                 line=scan.nextLine();
                for(int j=0;j<BoardSize;j++)
                {
                    crossword[i][j]=Character.toLowerCase(line.charAt(j));
                }
            }
            scan.close();
         
        }
        public static void crossFiller(int row,int col,StringBuilder[] rowStr,StringBuilder[] colStr)
        {
            Coordinates nextCoords;
            //switch cases for all possible symbols on the board
            switch(crossword[row][col])
            {
                case '+':
                    //iterate for all the possible letters
                    for(char c='a';c<='z';c++)
                    {
                        if(isValid(c,row, col))
                        {
                            rowStr[row].append(c);
                            colStr[col].append(c);
                           
                            //check if board is at bottom right
                            if(row == BoardSize-1 && col == BoardSize-1)
                            {
                                printBoard();
                                System.exit(0);
                            }
                            else
                            {
                                //get next coordinates
                                nextCoords = nextCoordinates(row, col);
                                //recurse too next coordinate
                                crossFiller (nextCoords.row, nextCoords.col, rowStr, colStr);
                                //backtrack and try new letter
                                rowStr[row].deleteCharAt(rowStr[row].length()-1);
                                colStr[col].deleteCharAt(colStr[col].length()-1);  
                            }
                        }
                    }
                    break;
                case '-':
                    //must append no need to check the word
                    rowStr[row].append('-');
                    colStr[col].append('-');
                                      
                    
                    //if at bottom right
                    if(row == BoardSize-1 && col == BoardSize-1)
                        {
                            printBoard();
                            break;
                        }
                        else
                        {
                            nextCoords= nextCoordinates(row,col);
                            crossFiller(nextCoords.row, nextCoords.col, rowStr, colStr);
                            rowStr[row].deleteCharAt(rowStr[row].length()-1);
                            colStr[col].deleteCharAt(colStr[col].length()-1);
                        }
                    break;
                default:
                if(isValid(crossword[row][col], row, col))  
                {
                    rowStr[row].append(crossword[row][col]);
                    colStr[col].append(crossword[row][col]);
                    
                    if(row == BoardSize-1 && col == BoardSize-1)
                    {
                        //print board and score
                         printBoard(); 
                         System.exit(0);
                    }    
                    else
                    {
                        nextCoords=nextCoordinates(row,col);
                        crossFiller(nextCoords.row, nextCoords.col, rowStr, colStr);
                        rowStr[row].deleteCharAt(rowStr[row].length()-1);
                        colStr[col].deleteCharAt(colStr[col].length()-1);
                    }            
                }
            }
            
        }
        
        public static boolean isValid(char c,int row,int col)
        {
            
            rowStr[row].append(c);
            colStr[col].append(c);
            int rtype=0;
            int ctype=0;
            int minR=rowStr[row].toString().indexOf('-');
            int minC=colStr[col].toString().indexOf('-');
           //cases for when there is a minus
          if(minR!=-1 ||minC!=-1)
          {
            
                    rtype= D.searchPrefix(rowStr[row], minR+1, rowStr[row].length()-1);
                     ctype= D.searchPrefix(colStr[col], minC+1, colStr[col].length()-1);
            }
            else{
                 rtype =(D.searchPrefix(rowStr[row]));
                 ctype=(D.searchPrefix(colStr[col]));
            }

           
            //if col is not an end index must be prefix
            if(col!=BoardSize-1)
            {
                if((rtype==2||rtype==0))
                  {
                    rowStr[row].deleteCharAt(rowStr[row].length()-1);
                    colStr[col].deleteCharAt(colStr[col].length()-1);
                    return false;
                  }
            }
            //if col is an end index must be word or there is a minus at +1 
            if(col==BoardSize-1 || crossword[row][col+1]=='-')
            {
                if((rtype==1||rtype==0))
                  {
                    rowStr[row].deleteCharAt(rowStr[row].length()-1);
                    colStr[col].deleteCharAt(colStr[col].length()-1);
                    return false;
                  }
            }
          //if row is not at the end must be a prefix
            if(row!=BoardSize-1)
            {
                if(ctype==2 || ctype==0)
                  {
                    rowStr[row].deleteCharAt(rowStr[row].length()-1);
                    colStr[col].deleteCharAt(colStr[col].length()-1);
                    return false;
                  }
                 
            }
            //if row is at the end must be a word at +1 
            if(row==BoardSize-1||crossword[row+1][col]=='-')
            {
                if((ctype==1||ctype==0))
                  {
                    rowStr[row].deleteCharAt(rowStr[row].length()-1);
                    colStr[col].deleteCharAt(colStr[col].length()-1);
                    return false;
                  }
            }
            rowStr[row].deleteCharAt(rowStr[row].length()-1);
            colStr[col].deleteCharAt(colStr[col].length()-1);
        return true;
        }
        public static void printBoard()
        {
        
            for(int i=0;i<rowStr.length;i++)
            {
                System.out.println(rowStr[i].toString());
            }
            //check letters againt the scores from hashtable keys
            for(int i=0;i<rowStr.length;i++)
            {
                 String word=rowStr[i].toString();
                for(int j=0;j<word.length();j++)
                {
                 if(letters.containsKey(word.charAt(j)))
                 {
                     char v=letters.get(word.charAt(j));
                     score+=Character.getNumericValue(v);
                 }
                }
             }
             
             final long end = System.currentTimeMillis();
             //System.out.println("The program was running: " + (end-previousTime)/1000);
             System.out.println("Score: "+score);
           
        }
        private static Coordinates nextCoordinates(int row, int col)
        {
            Coordinates result = null;
            //if at end of the row
            
            if(col==BoardSize-1)
            {
                result= new Coordinates(row+1,0);
                return result;
            }
            //if not at the end
            if(col!=BoardSize-1)
            {
                result = new Coordinates(row,col+1);
                return result;
            }
            return result;
        }

         
        //inner class
	private static class Coordinates 
    {
		int row;
		int col;

		Coordinates(int row, int col)
        {
			this.row = row;
			this.col = col;
		}
	}
    
    
}
   
