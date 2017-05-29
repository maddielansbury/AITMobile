package hu.ait.minesweeper;

public class Section {
    private int x, y;
    private Boolean isMine;
    private Boolean isShown;
    private Boolean isFlag;
    private int nearbyBombs;


    public Section() {
        x = 0;
        y = 0;
        isMine = false;
        isShown = false;
        isFlag = false;
        nearbyBombs = 0;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Boolean getMine() {
        return isMine;
    }

    public void setMine(Boolean mine) {
        isMine = mine;
    }

    public Boolean getShown() {
        return isShown;
    }

    public void setShown(Boolean shown) {
        isShown = shown;
    }

    public Boolean getFlag() {
        return isFlag;
    }

    public void setFlag(Boolean flag) {
        isFlag = flag;
    }

    public int getNearbyBombs() {
        return nearbyBombs;
    }

    public void addNearbyBomb() {
        this.nearbyBombs = nearbyBombs + 1;
    }
}
