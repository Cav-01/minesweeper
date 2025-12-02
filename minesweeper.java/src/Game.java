import javax.swing.*;
import java.util.*;

public class Game {
    private final int rows, columns, totalMines;
    private final Board board;

    private boolean gameOver = false;
    private boolean minesPlaced = false;
    private int revealedSafeCells = 0;
    private final int totalSafeCells;

    public Game(int rows, int columns, int totalMines) {
        this.rows = rows;
        this.columns = columns;
        this.totalMines = totalMines;
        this.totalSafeCells = rows * columns - totalMines;
        this.board = new Board(rows, columns, this);

    }

    public Board getBoard() {
        return board;
    }

    public void handleLeftClick(int r, int c) {
        if (gameOver) return;
        Cell cell = board.getCell(r, c);

        if (cell.isRevealed() || cell.isFlagged()) return;

        // first click of the game
        if (!minesPlaced) {
            placeMinesAvoiding(r, c);
            computeAdjacencies();
            minesPlaced = true;
        }

        if (cell.isMine()) {
            revealAllMines();
            gameOver = true;
            JOptionPane.showMessageDialog(board, "you exploded");
            return;
        }

        floodReveal(r, c);
        checkWin();
    }

    public void handleRightClick(int r, int c) {
        if (gameOver) return;
        Cell cell = board.getCell(r, c);

        if (cell.isRevealed()) return;

        boolean newFlag = !cell.isFlagged();
        cell.setFlagged(newFlag);
        if (newFlag) cell.setText("F");
        else cell.setText("");
    }

    private boolean inBounds(int r, int c) {
        if (r >= 0 && r < rows && c >= 0 && c < columns) return true;
        else return false;
    }

    // randomly placing mines on board, avoiding first-clicked cell and a 3x3 area around it
    public void placeMinesAvoiding(int safe_r, int safe_c) {
        Random random = new Random();
        Set<Integer> banned = new HashSet<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = safe_r + dr;
                int nc = safe_c + dc;
                if (inBounds(nr, nc)) {
                    banned.add(nr * columns + nc); // 1-to-1 mapping of (row, column) pairs
                }
            }
        }

        int placed = 0;
        while (placed < totalMines) {
            int r = random.nextInt(rows);
            int c = random.nextInt(columns);
            int index = r * columns + c;
            if (banned.contains(index)) continue;

            Cell cell = board.getCell(r, c);
            if (!cell.isMine()) {
                cell.setMine(true);
                placed++;
            }
        }
    }

    public void computeAdjacencies() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                Cell cell = board.getCell(r, c);
                if (cell.isMine()) {
                    cell.setAdjMines(-1);
                    continue;
                }
                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue; // skip the center cell of the 3x3 grid
                        int nr = r + dr;
                        int nc = c + dc;
                        if (inBounds(nr, nc) && board.getCell(nr, nc).isMine()) count++;
                    }
                }
                cell.setAdjMines(count);
            }
        }
    }

    public void revealAllMines() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                Cell cell = board.getCell(r, c);
                if (cell.isMine()) {
                    cell.setRevealed(true);
                    cell.setText("M");
                }
            }
        }
    }

    public void floodReveal(int r, int c) {
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{r, c});

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int cr = current[0];
            int cc = current[1];
            if (!inBounds(cr, cc)) continue;

            Cell cell = board.getCell(cr, cc);
            if (cell.isRevealed() || cell.isFlagged() || cell.isMine()) continue;

            cell.setRevealed(true);
            revealedSafeCells++;

            int adj = cell.getAdjMines();
            if (adj > 0) {
                cell.setText(String.valueOf(adj));;
            } else {
                cell.setText("");
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        stack.push(new int[]{cr + dr, cc + dc});
                    }
                }
            }
        }
    }

    public void checkWin() {
        if (!gameOver && revealedSafeCells == totalSafeCells) {
            gameOver = true;
            JOptionPane.showMessageDialog(board, "congratulations");
        }
    }
}
