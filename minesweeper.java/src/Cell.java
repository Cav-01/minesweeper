import java.awt.Font;
import javax.swing.*;

public class Cell extends JButton {
    private boolean isFlagged;
    private boolean isMine;
    private boolean isRevealed;
    private int adjMines;

    public Cell() {
        super(); // invokes constructor of parent class
        this.isFlagged = false;
        this.isMine = false;
        this.isRevealed = false;
        this.adjMines = 0;
        setFont(new Font("Arial", Font.BOLD, 30));
    }

    public boolean isRevealed() { return isRevealed; }
    public void setRevealed(boolean isRevealed) { 
        this.isRevealed = isRevealed;
        setEnabled(!isRevealed); // disables interactability of the JButton
        }
        
    public boolean isFlagged() { return isFlagged; }
    public void setFlagged(boolean isFlag) { this.isFlagged = isFlag; }

    public boolean isMine() { return isMine; }
    public void setMine(boolean isMine) { this.isMine = isMine; }

    public int getAdjMines() { return adjMines; }
    public void setAdjMines(int adjMines) { this.adjMines = adjMines; }
}