import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel {
    private final Cell[][] cells;
    private final int rows, columns;
    private Game game; 

    public Board(int rows, int columns, Game game) {
        super(new GridLayout(rows, columns));
        this.rows = rows;
        this.columns = columns;
        this.game = game;
        this.cells = new Cell[rows][columns];
        setPreferredSize(new Dimension(columns * 75, rows * 75));
        initCells();

    }

    private void initCells() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < rows; c++) {
                Cell cell = new Cell();
                cells[r][c] = cell;

                final int rr = r;
                final int cc = c;

                // left click
                cell.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (game != null) {
                            game.handleLeftClick(rr, cc);
                        }
                    }
                });
                // right click
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (game != null && SwingUtilities.isRightMouseButton(e)) {
                            game.handleRightClick(rr, cc);
                        }
                    }
                });
                add(cell);
            }
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Cell getCell(int r, int c) {
        return cells[r][c];
    }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }

}