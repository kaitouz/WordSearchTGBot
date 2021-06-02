package com.tg.bot;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    public static final int nRows = 10, nCols = 10;
    int numAttempts;
    char[][] cells = new char[nRows][nCols];
    List<String> Solutions = new ArrayList<>();
}