package hu.ait.minesweeper.model;

import java.util.Random;

import hu.ait.minesweeper.Section;

public class MinesweeperModel {

    private static MinesweeperModel instance = null;

    private static boolean SWEEP = false;
    private static boolean FLAG = true;

    private boolean mode;

    private boolean gameOver;
    private boolean hitMine;
    private boolean wrongFlag;
    private boolean won;

    private int bombsLeft;

    private MinesweeperModel() {
        initMinefield();
    }

    public static MinesweeperModel getInstance() {
        if (instance == null) {
            instance = new MinesweeperModel();
        }
        return instance;
    }

    private Section[][] minefield = new Section[9][9];

    public void initMinefield() {

        mode = SWEEP;
        hitMine = false;
        wrongFlag = false;
        gameOver = false;
        won = false;
        bombsLeft = 10;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                minefield[i][j] = new Section();
            }
        }

        for (int i = 0; i < 10; i++) {
            boolean planted = false;
            while (!planted) {
                planted = plantMine();
            }
        }
    }

    private boolean plantMine() {
        Random rand = new Random();
        int i = rand.nextInt(9);
        int j = rand.nextInt(9);
        if (!minefield[i][j].getMine()) {
            minefield[i][j].setMine(Boolean.TRUE);
            for(int x = Math.max(0, i-1); x <= Math.min(i+1, 8); x++){
                    for(int y = Math.max(0, j-1); y <= Math.min(j+1, 8); y++){
                        if(x != i || y != j){
                            minefield[x][y].addNearbyBomb();
                        }
                    }
                }
            return true;
        } else {
            return false;
        }
    }

    public void sweepSection(int x, int y) {
        if (!isGameOver()) {
            minefield[x][y].setShown(Boolean.TRUE);
            if (minefield[x][y].getMine()) {
                hitMine = true;
                gameOver();
            }
        }
    }

    public void flagSection(int x, int y) {
        if (!isGameOver()) {
                minefield[x][y].setFlag(Boolean.TRUE);
                if (!minefield[x][y].getMine()) {
                    wrongFlag = true;
            } else {
                    bombsLeft--;
                    if (bombsLeft == 0) {
                        won = true;
                        gameOver();
                    }
                }
        }
    }

    public Section getSectionContents(int x, int y) {
        return minefield[x][y];
    }

    public void toggleMode() {
        if (this.mode) {
            this.mode = SWEEP;
        } else {
            this.mode = FLAG;
        }
    }

    public boolean getMode() {
        return this.mode;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    private void gameOver() {
        this.gameOver = true;
    }

    public boolean isWon() {
        return this.won;
    }

    public boolean isHitMine() {
        return this.hitMine;
    }

    public boolean isWrongFlag() {
        return this.wrongFlag;
    }

}
