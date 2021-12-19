# CS 1501 – Algorithm Implementation – Assignment #1 

## Overview
 
* __Purpose__:  To implement a backtracking algorithm.
* __Task__: The main task of the assignment is to create a backtracking algorithm that finds one legal filling of the squares of a given crossword puzzle and the score of a filling (if a legal filling exists), as specified in detail below.

## Background

Crossword puzzles are challenging games that test both our vocabularies and our reasoning skills.  However, creating a legal crossword puzzle is not a trivial task.  This is because the words both across and down must be legal, and the choice of a word in one direction restricts the possibilities of words in the other direction.  This restriction progresses recursively, so that some word choices "early" in the board could make it impossible to complete the board successfully.  For example, look at the simple crossword puzzle below (note: in this example X<sub>1</sub>, X<sub>2</sub>, X<sub>3</sub> are variables not the letter X):

![puzzle](puzzle.png)
 

Assume that the word LENS has been selected for row 0 of the puzzle, as shown in Figure 1 above.  Now, the word in column 1 (the second column) must begin with an E, the word in column 2 must being with an N and the word in column 3 must begin with an S. All single characters are valid words in our dictionary so the L in column 0 is a valid word but is also irrelevant to the rest of the puzzle, since its progress is blocked by a filled-in square.  There are many ways to proceed from this point, and finding a good way is part of the assignment.  However, if we are proceeding character by character in a row-wise fashion, we now need a letter X<sub>1</sub> such that EX<sub>1</sub> is a valid prefix to a word.  Several letters will meet this criterion (EA, EB and EC are all valid prefixes, just to pick the first three letters of the alphabet).  Once a possibility is selected, there are now two restrictions on the next character X<sub>2</sub>: NX<sub>2</sub> must be a valid word and X<sub>1</sub>X<sub>2</sub> must be a valid prefix to a word (see Figure 1). Assume that we choose Q for X<sub>1</sub> (since EQ is a valid prefix). We can then choose U for X<sub>2</sub>, (see Figure 2 (NU is a valid word in our dictionary)). Continuing in the same fashion, we can choose the other letters shown in Figure 2 (in our dictionary QUA, DU and DC are all legal words).

Unfortunately, in row 3, column 1 we run into a problem (Figure 3).  There is no word in our dictionary EQUX<sub>3</sub> for any letter X<sub>3</sub> (note that since we are at a terminating block, we are no longer just looking for a prefix) so we are stuck.  At this point we need to undo some of our previous choices (i.e., backtrack) in order to move forward again toward a solution.  If our algorithm were very intelligent, it would know that the problem that we need to fix is the prefix EQU in the second column .  However, based on the way we progressed in this example, we would simply go back to the previous square (row 3, column 0), try the next legal letter there, and move forward again.  This would again fail at row 3, column 1, as shown in Figure 3.  Note that the backtracking could occur many times for a given board, possibly going all the way back to the first word on more than one occasion.  In fact, the general run-time complexity for this problem is exponential.  However, if the board sizes are not too large, we can likely solve the problem (or determine that no solution exists) in a reasonable amount of time.  One solution (but not the only one) to the puzzle above is shown in Figure 4.

### Filled Puzzle Score
We are going to assign a score to each filled puzzle. The score is computed by adding up the letter points of all the letters on the filled puzzle. Assuming the letter points of the Scrabble game, the score of the puzzle of Figure 4 is computed as follows:

L+E+N+S+T+O+N+A+C+O+T+H+A+W = 1+1+1+1+1+1+1+1+3+1+1+4+1+4=22

## TASK - Finding a Single Solution

Your task in this assignment is to create a legal crossword puzzle (if it exists) and compute its score in the following way:

1. Read a dictionary of words in from a file and form a `MyDictionary` object of these words. The name of the dictionary file should be specified as a command-line argument. The interface `DictInterface` (in `DictInterface.java`) and the class `MyDictionary` (in `MyDictionary.java`) are provided for you in this repository, and you must use them in this assignment.  Read over the code and comments carefully so that you understand what they do and how.  The file used to initialize the `MyDictionary` object will contain ASCII strings, one word per line.  Use the file `dict8.txt`.  If you are unsure of how to use `DictInterface` and `MyDictionary` correctly, see the `DictTest.java` example program (and read the comments). Lab 1 solution can be used for reference as well.

2. Read a crossword board in from a file.  The name of the board file should be specified as a command-line argument.  The crossword board will be formatted in the following way:

    - The first line contains a single integer, N.  This represents the number of rows and columns that will be in the board.  Since the dictionary will contain up to 8-letter words, your program should handle crosswords up to 8x8 in size.
    - The next N lines will each have N characters, representing the NxN total locations on the board.  Each character will be either
      - `+` (plus) which means that any letter can go in this square
      - `–` (minus) which means that the square is solid (filled-in) and no letter can go in here
      - A..Z (a letter from A to Z) which means that the specified letter must be in this square (i.e., the square can be used in the puzzle, but only for the letter indicated)

For the board shown above, the board file sample.txt is as follows:
```
4
++++
-+++
++-+
++++ 
```

Some test boards have been put onto this repository. Please consult `testFiles.md` for an overview of these board files.

3. Create a legal crossword puzzle for the given board and print it out to standard output.  Many of the test files may have many solutions, but for this assignment, you only need to find one solution.  For example, one output to the crossword shown above in Figure 4 would be (note that the puzzle score is also printed):

```
> java Crossword dict8.txt sample.txt
LENS
-TON
AC-O
THAW
Score: 22
>
```

Depending upon your algorithm, the single solution that you find may differ from that of my program or your classmates' programs.  This is fine as long as all of the solutions are legal.  Note that because of the severe performance limitations of the `MyDictionary` class, some of the run-times for the test files will be very long.  See more details on this in `testFiles.html`. 

## Writeup

Once you have completed your algorithm, write a short paper (500-750 words) using [Github Markdown syntax](https://guides.github.com/features/mastering-markdown/) and named `a1.md` that summarizes your project in the following ways:
1.	Discuss how you solved the crossword-filling problem in some detail. Include
    * how you set up the data structures necessary for the problem and 
    * how your algorithm proceeded.  
    * Also indicate any coding or debugging issues you faced and how you resolved them.  If you were not able to get the program to work correctly, still include your approach and speculate as to what still needs to be corrected.
2.	Include the (approximate) run-times for the programs for the various files in a table.  
3.	Include an asymptotic analysis of the worst-case run-time of the program.  Some values to consider in this analysis may include:
    * Number of words in the dictionary
    * Number of characters in a word
    * Number of possible letters in a crossword location
    * Number of crossword locations in the puzzle

