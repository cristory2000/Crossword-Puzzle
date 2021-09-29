import java.util.*;
import java.io.*;



public class Crossword 
{
    private static char [][] crossword;
    private static DictInterface D;
    static StringBuilder colStr[];
    static StringBuilder rowStr[];
    static int BoardSize;

    public static void main(String args[]) throws IOException
    {
            //input the dictionary file
            
            Scanner fileScan = new Scanner(new FileInputStream("dict8.txt"));
            Scanner boardScan = new Scanner(new FileInputStream(args[1]));
            //type in whether using a dlb
            String input = args[0];
            

            if(input.equals("DLB"))
            {
                D = new MyDictionary();
            }
            else
            {
                D = new MyDictionary();
            }
            //add dictionary file to whichever structure was chosen
            while(fileScan.hasNext())
            {
                D.add(fileScan.next());
                
            }
            fileScan.close();
            

            
            
            
            
            
            
            crosswordBoardcreator();
            colStr= new StringBuilder[BoardSize];
            rowStr= new StringBuilder[BoardSize];
            for (int i = 0; i < colStr.length; i++) {
                colStr[i] = new StringBuilder("");
                rowStr[i] = new StringBuilder("");
            }

                for(int i=0;i<BoardSize;i++)
                {
                    for(int j=0;j<BoardSize;j++)
                    {
                       
                        break;
                    }
                }
                
                crossFiller(0,0, rowStr, colStr);
                
    }
      
      
    public static void crosswordBoardcreator() throws FileNotFoundException
      { 
            // Scanner reader;
            Scanner scan;
            Scanner fReader;
            File fName;
            //enter crowssword board in
        
            while(true)
            { 
                {
                    scan = new Scanner(System.in);
                    System.out.println("Enter Crossword Board");
                    String l=scan.nextLine();
                    fName=new File(l);
                    fReader=new Scanner(fName);
                    break;
                }
            }
            scan.close();
            //create crossword board and input values
            String line=fReader.nextLine();
            BoardSize=Integer.parseInt(line);
            crossword= new char[BoardSize][BoardSize];
           
            for(int i=0;i<BoardSize;i++)
            {
                 line=fReader.nextLine();
                for(int j=0;j<BoardSize;j++)
                {
                    crossword[i][j]=Character.toLowerCase(line.charAt(j));
                }
            }
            fReader.close();
            //print crossword
           /*  for (int i = 0; i < BoardSize; i++)
            {
                for (int j = 0; j < BoardSize; j++)
                {
                    System.out.print(crossword[i][j] + " ");
                }
                System.out.println();
            } */
        }
        public static void crossFiller(int row,int col,StringBuilder[] rowStr,StringBuilder[] colStr)
        {
            Coordinates nextCoords;
            
            switch(crossword[row][col])
            {
                case '+':
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
                               
                                nextCoords = nextCoordinates(row, col);
                                crossFiller (nextCoords.row, nextCoords.col, rowStr, colStr);
                                rowStr[row].deleteCharAt(rowStr[row].length()-1);
                                colStr[col].deleteCharAt(colStr[col].length()-1);  
                               
                            }
                        }
                    }
                    break;
                case '-':
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
              
            
           int rtype =(D.searchPrefix(rowStr[row]));
            int ctype=(D.searchPrefix(colStr[col]));
            //if col is not an end index
            if(col!=BoardSize-1)
            {
                if((rtype==2||rtype==0))
                  {
                    rowStr[row].deleteCharAt(rowStr[row].length()-1);
                    colStr[col].deleteCharAt(colStr[col].length()-1);
                    return false;
                  }
            }
            //if col is an end index
            if(col==BoardSize-1)
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
            //if row is at the end must be a word
            if(row==BoardSize-1)
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
            return;
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
        public static int scoreChecker()
        {
            
            return 1;
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
   
