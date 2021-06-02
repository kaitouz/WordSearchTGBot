package com.tg.bot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Game {

    Grid grid;
    List<String> WordsDic;
    int GameHeart;
    List<String> WordAnswered = new ArrayList<>();
    List<String> PrintWordAnswered = new ArrayList<>();
    List<Integer> PosAnswered = new ArrayList<>();

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

    // toa do cua 8 huong
    public static final int[][] DIRS = {{1,0}, {0,1}, {1,1}, {1,-1}, {-1,0}, {0,-1}, {-1,-1}, {-1,1}};
    public static final int nRows = 10, nCols = 10;
    public static final int gridSize = nRows * nCols;
    public static final int minWords = 10;
    public static final Random RANDOM = new Random();

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
            System.out.println("Loi");
        }
        return words;
    }

    public Grid createWordSearch(List<String> words) {
        Grid grid = null;
        int numAttempts = 0;

        while(++numAttempts < 100) {
            Collections.shuffle(words);
            grid = new Grid();

            int cellsFilled = 0;

            for(String word : words) {
                cellsFilled += tryPlaceWord(word);

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

    public int tryPlaceWord(String word) {
        int randDir = RANDOM.nextInt(DIRS.length);
        int randPos = RANDOM.nextInt(gridSize);
        for(int dir = 0; dir < DIRS.length; dir++) {
            dir = (dir + randDir) % DIRS.length;
            for(int pos = 0; pos < gridSize; pos++) {
                pos = (pos + randPos) % gridSize;
                int lettersPlaced = tryLocation(word, dir, pos);
                if(lettersPlaced > 0)
                    return lettersPlaced;
            }
        }
        return 0;
    }

    public int tryLocation(String word, int dir, int pos) {
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
            if(this.grid.cells[rr][cc] != 0 && this.grid.cells[rr][cc] != word.charAt(i))
                return 0;
            cc += DIRS[dir][0];
            rr += DIRS[dir][1];
        }
        for( i = 0, rr = r, cc = c; i < length; i++) {
            if (this.grid.cells[rr][cc] == word.charAt(i))
                overlaps++;
            else this.grid.cells[rr][cc] = word.charAt(i);
            if (i < length - 1) {
                cc += DIRS[dir][0];
                rr += DIRS[dir][1];
            }
        }
        int lettersPlaced = length - overlaps;
        if(lettersPlaced >= 0)
            this.grid.Solutions.add(String.format("%-15s", word));


        return lettersPlaced;
    }

    public String CreateTable() {
        String ans = "`                      ";
        for(int i = 0; i < this.GameHeart; i++) {
            ans = ans + "\ud83d\udda4";
        }
        ans = ans + "`\n`  ";
        for(int c = 0; c < nCols; c++) {
            if(c == 0) ans = ans + "_";
            else ans = ans + "__";
            ans = ans + String.valueOf(c);
        }

        ans = ans + "`";

        for(int r = 0; r < nRows; r++) {
            ans = ans + "\n`";
            ans = ans + String.valueOf(r) + "\\|\\";
            for(int c = 0; c < nCols; c++) {
                int pos = r*nCols + c;
                if(this.PosAnswered.contains(pos))
                    ans = ans + "[" + String.valueOf(this.grid.cells[r][c]) + "]";
                else
                    ans = ans + " " + String.valueOf(this.grid.cells[r][c]) + " ";
            }
            ans = ans + "`";
        }
        ans = ans + "\n";
        ans = ans + "\n";

        ans = ans + String.format("`Find %d words\\!!!\\ \n`", (10) - (this.WordAnswered.size()));

        int size = this.PrintWordAnswered.size();

        if(size > 0) {
            ans = ans + "\n`Words found:`\n";
            for(int i = 0; i < size - 1; i += 2) {
                ans = ans + "`" +  this.PrintWordAnswered.get(i) + " " + this.PrintWordAnswered.get(i + 1) + "`" + "\n";
            }
            if(size % 2 == 1) {
                ans = ans + "`" + (PrintWordAnswered.get(size - 1)) + "`";
            }
        }
        return ans;

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