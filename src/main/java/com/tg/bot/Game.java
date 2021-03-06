
package com.tg.bot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Game {

    // toa do cua 8 huong
    public static final int[][] DIRS = {{1,0}, {0,1}, {1,1}, {1,-1}, {-1,0}, {0,-1}, {-1,-1}, {-1,1}};
    public static final int nRows = 10, nCols = 10;
    public static final int gridSize = nRows * nCols;
    public static final int minWords = 10;
    public static final Random RANDOM = new Random();

    List<String> WordAnswered = new ArrayList<>();
    List<String> PrintWordAnswered = new ArrayList<>();
    List<Integer> PosAnswered = new ArrayList<>();
    Grid grid;
    List<String> WordsDic;
    int GameHeart;

    public Game() {
        this.GameHeart = 3;
        Grid a = new Grid();
        this.WordsDic = readWords("src/Oxford5000.txt");
        while(true) {
            a = createWordSearch(WordsDic);
            if(a != null && a.numAttempts != 0) break;
        }
        this.grid = a;
    }

    public int CheckAnswer(String Ans) {
        Ans = Ans.toUpperCase();
        if(this.WordAnswered.contains(Ans))
            return 0;
        if(!this.WordsDic.contains(Ans))
            return 0;
        for(int pos = 0; pos < gridSize; pos++) {
            for(int dir = 0; dir < 8; dir++) {
                int r = pos / nCols;
                int c = pos % nCols;
                int length = Ans.length();

                if((DIRS[dir][0] == 1 && (length + c) > nCols)
                        || (DIRS[dir][0] == -1 && (length - 1) > c)
                        || (DIRS[dir][1] == 1  && (length + r) > nRows)
                        || (DIRS[dir][1] == -1 && (length - 1) > r))
                    continue;

                int i, rr, cc;
                int check = 1;
                for( i = 0, rr = r, cc = c; i < length; i++) {
                    if(this.grid.cells[rr][cc] != 0 && this.grid.cells[rr][cc] != Ans.charAt(i)){
                        check = 0; break;
                    }
                    cc += DIRS[dir][0];
                    rr += DIRS[dir][1];
                }
                if(check == 1) {

                    for(i = 0, rr = r, cc = c; i < length; i++) {
                        this.PosAnswered.add(rr*nCols + cc);
                        cc += DIRS[dir][0];
                        rr += DIRS[dir][1];
                    }
                    this.WordAnswered.add(Ans);
                    this.PrintWordAnswered.add(String.format("%-15s", Ans));
                    return 1;
                }
            }
        }
        return 0;
    }

    public static List<String> readWords(String filename) {
        int maxLength = Math.max(nRows, nCols);
        List<String> words = new ArrayList<>();
        try (Scanner sc = new Scanner(new FileReader(filename))) {
            while(sc.hasNext()) {
                String s = sc.next().trim().toLowerCase();
                if (s.matches("^[a-z]{3," + maxLength + "}$")) {
                    words.add(s.toUpperCase());
                    //   System.out.println(s);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
        return words;
    }

    public static Grid createWordSearch(List<String> words) {
        Grid grid = null;
        int numAttempts = 0;

        while(++numAttempts < 100) {
            Collections.shuffle(words);
            grid = new Grid();

            int cellsFilled = 0;

            for(String word : words) {
                cellsFilled += tryPlaceWord(grid, word);

                if(cellsFilled == gridSize) {
                    if(grid.Solutions.size() >= minWords) {
                        grid.numAttempts = numAttempts;
                        return grid;
                    } else break;
                }
            }
        }
        return grid;
    }

    public static int tryPlaceWord(Grid grid, String word) {
        int randDir = RANDOM.nextInt(DIRS.length);
        int randPos = RANDOM.nextInt(gridSize);
        for(int dir = 0; dir < DIRS.length; dir++) {
            dir = (dir + randDir) % DIRS.length;
            for(int pos = 0; pos < gridSize; pos++) {
                pos = (pos + randPos) % gridSize;
                int lettersPlaced = tryLocation(grid, word, dir, pos);
                if(lettersPlaced > 0)
                    return lettersPlaced;
            }
        }
        return 0;
    }

    public static int tryLocation(Grid grid, String word, int dir, int pos) {
        int r = pos / nCols;
        int c = pos % nCols;
        int length = word.length();
        if((DIRS[dir][0] == 1 && (length + c) > nCols)
                || (DIRS[dir][0] == -1 && (length - 1) > c)
                || (DIRS[dir][1] == 1  && (length + r) > nRows)
                || (DIRS[dir][1] == -1 && (length - 1) > r))
            return 0;
        int i, rr, cc, overlaps = 0;
        for( i = 0, rr = r, cc = c; i < length; i++) {
            if(grid.cells[rr][cc] != 0 && grid.cells[rr][cc] != word.charAt(i))
                return 0;
            cc += DIRS[dir][0];
            rr += DIRS[dir][1];
        }
        for( i = 0, rr = r, cc = c; i < length; i++) {
            if (grid.cells[rr][cc] == word.charAt(i))
                overlaps++;
            else grid.cells[rr][cc] = word.charAt(i);
            if (i < length - 1) {
                cc += DIRS[dir][0];
                rr += DIRS[dir][1];
            }
        }
        int lettersPlaced = length - overlaps;
        if(lettersPlaced >= 0)
        //  grid.solutions.add(String.format("%-10s %d,%d %d,%d", word, c, r, cc, rr));
        {
            // ValSolutions a = new ValSolutions(String.format("%-15s", word), false, c, r, cc, rr);
            grid.Solutions.add(String.format("%-15s", word));
        }

        return lettersPlaced;
    }



    public String CreateTable() {
        String content = "`                      ";
        for(int i = 0; i < this.GameHeart; i++)
            content = content + "\ud83d\udda4";

        content = content + "`\n`  ";
        for(int c = 0; c < nCols; c++) {
            if(c == 0) content = content + "_";
            else content = content + "__";
            content = content + String.valueOf(c);
        }

        content = content + "`";
        for(int r = 0; r < nRows; r++) {
            content = content + "\n`";
            content = content + String.valueOf(r) + "\\|\\";
            for(int c = 0; c < nCols; c++) {
                int pos = r*nCols + c;
                if(this.PosAnswered.contains(pos))
                    content = content + "[" + String.valueOf(this.grid.cells[r][c]) + "]";
                else
                    content = content + " " + String.valueOf(this.grid.cells[r][c]) + " ";
            }
            content = content + "`";
        }
        content = content + "\n\n";

        content = content + String.format("`Find %d words\\!!!\\ \n`", (10) - (this.WordAnswered.size()));

        int size = this.PrintWordAnswered.size();

        if(size > 0) {
            content = content + "\n`Words found:`\n";
            for(int i = 0; i < size - 1; i += 2)
                content = content + "`" +  this.PrintWordAnswered.get(i) + " " + this.PrintWordAnswered.get(i + 1) + "`" + "\n";
            if(size % 2 == 1)
                content = content + "`" + (this.PrintWordAnswered.get(size - 1)) + "`";
        }
        return content;
    }


    public void printResult() {
        if(grid == null || grid.numAttempts == 0) {
            System.out.println("No grid to display");
            return;
        }
        int size = grid.Solutions.size();
        System.out.println("Number of Attempts : " + grid.numAttempts);
        System.out.println("Number of Words : " + size);
        System.out.println("\n      ");
        System.out.print("  ");

        System.out.println("\n");
        for(int i = 0; i < size - 1; i += 2) {
            System.out.printf("%s %s%n", grid.Solutions.get(i), grid.Solutions.get(i + 1));
        }
        if(size % 2 == 1) {
            System.out.println(grid.Solutions.get(size - 1));
        }
    }

}