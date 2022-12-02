package MineSweeper;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static java.lang.Math.round;
import static java.lang.Math.pow;

public class Minesweeper extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final File dir = new File("src/icons/");
	private static final int imageNumber = dir.listFiles().length;
	private static JLabel text;
    private static boolean antiMS = false;
    private final Image[] image = new Image[imageNumber];
    private static final int pixel = 20;

    static final int emptyCell = 0;
    static final int coverForCell = 10;
    static final int markForCell = 10;
    static final int mineCell = 9;
    static final int coveredMineCell = mineCell + coverForCell;
    static final int markedMineCell = coveredMineCell + markForCell;

    private static int rows = 16;
    private static int mines; //= (int) round(rows*rows/6);

    static int[] field;
    static boolean inGame;
    static int minesLeft;

    static int allCells = rows*rows;
    
    static void setSide(int x) { rows = x; }

    static void setMines(int x) { mines = x; }
    
    static void setColor(String color) {
    	if (color == "st") antiMS = false;
    	else antiMS = true;
    }

    public Minesweeper(JLabel bombakSzama) throws ClassNotFoundException, IOException {
    	
    	Seri.load("Serialized");
        addMouseListener(new MinesAdapter());
        Minesweeper.text = bombakSzama;
        mines = (int) round(pow(rows,2)/6);
        newGame();
        setPreferredSize(new Dimension(rows * pixel + 1, rows * pixel + 1));
        //Minesweeper.rows = rows;
        for (int i = 0; i < imageNumber; i++) { image[i] = (new ImageIcon("src/icons/" + i + ".png")).getImage(); }
        
    }

    static void newGame() throws ClassNotFoundException, IOException {

        int cell;
        var random = new Random();
        inGame = true;
        minesLeft = mines;
        text.setText(Integer.toString(minesLeft));
        allCells = rows * rows;
    	Seri.load("Serialized");

        field = new int[allCells];

        for (int i = 0; i < allCells; i++) { field[i] = coverForCell; }

        int i = 0;
        while (i < mines) {

            int position = (int) (allCells * random.nextDouble());

            if ((position < allCells) && (field[position] != coveredMineCell)) {

                int current_col = position % rows;
                field[position] = coveredMineCell;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - rows;
                    if (cell >= 0) {
                        if (field[cell] != coveredMineCell) { field[cell] += 1; }
                    }
                    cell = position - 1;
                    if (cell >= 0) {
                        if (field[cell] != coveredMineCell) { field[cell] += 1; }
                    }

                    cell = position + rows - 1;
                    if (cell < allCells) {
                        if (field[cell] != coveredMineCell) { field[cell] += 1; }
                    }
                }

                cell = position - rows;
                if (cell >= 0) {
                    if (field[cell] != coveredMineCell) { field[cell] += 1; }
                }

                cell = position + rows;
                if (cell < allCells) {
                    if (field[cell] != coveredMineCell) { field[cell] += 1; }
                }

                if (current_col < (rows - 1)) {
                    cell = position - rows + 1;
                    if (cell >= 0) {
                        if (field[cell] != coveredMineCell) { field[cell] += 1; }
                    }
                    cell = position + rows + 1;
                    if (cell < allCells) {
                        if (field[cell] != coveredMineCell) { field[cell] += 1; }
                    }
                    cell = position + 1;
                    if (cell < allCells) {
                        if (field[cell] != coveredMineCell) { field[cell] += 1; }
                    }
                }
            }
        }
    }

    private void find_empty_cells(int j) {

        int current_col = j % rows;
        int cell;

        if (current_col > 0) {
            cell = j - rows - 1;
            if (cell >= 0) {
                if (field[cell] > mineCell) {
                    field[cell] -= coverForCell;
                    if (field[cell] == emptyCell) find_empty_cells(cell);
                }
            }

            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > mineCell) {
                    field[cell] -= coverForCell;
                    if (field[cell] == emptyCell) find_empty_cells(cell);
                }
            }

            cell = j + rows - 1;
            if (cell < allCells) {
                if (field[cell] > mineCell) {
                    field[cell] -= coverForCell;
                    if (field[cell] == emptyCell) find_empty_cells(cell);
                }
            }
        }

        cell = j - rows;
        if (cell >= 0) {
            if (field[cell] > mineCell) {
                field[cell] -= coverForCell;
                if (field[cell] == emptyCell) find_empty_cells(cell);
            }
        }

        cell = j + rows;
        if (cell < allCells) {
            if (field[cell] > mineCell) {
                field[cell] -= coverForCell;
                if (field[cell] == emptyCell) find_empty_cells(cell);
            }
        }

        if (current_col < (rows - 1)) {
            cell = j - rows + 1;
            if (cell >= 0) {
                if (field[cell] > mineCell) {
                    field[cell] -= coverForCell;
                    if (field[cell] == emptyCell) find_empty_cells(cell);
                }
            }

            cell = j + rows + 1;
            if (cell < allCells) {
                if (field[cell] > mineCell) {
                    field[cell] -= coverForCell;
                    if (field[cell] == emptyCell) find_empty_cells(cell);
                }
            }

            cell = j + 1;
            if (cell < allCells) {
                if (field[cell] > mineCell) {
                    field[cell] -= coverForCell;
                    if (field[cell] == emptyCell) find_empty_cells(cell);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < rows; j++) {

                int cell = field[(i * rows) + j];

                if (inGame && cell == mineCell) { inGame = false; }

                if (!inGame) {
                    if (cell == coveredMineCell) {
                        cell = 9;
                    } else if (cell == markedMineCell) {
                        cell = 11;
                    } else if (cell > coveredMineCell) {
                        cell = 12;
                    } else if (cell > mineCell) {
                        cell = 10;
                    }
                } else {

                    if (cell > coveredMineCell) {
                        cell = 11;
                    } else if (cell > mineCell) {
                        cell = 10;
                        uncover++;
                    }
                }
                if  (antiMS == false) g.drawImage(image[cell], (j * pixel), (i * pixel), this);
                else g.drawImage(image[cell+15], (j * pixel), (i * pixel), this);
            }
        }

        if (uncover == 0 && inGame) {
            inGame = false;
            text.setText("Game won!");

        } else if (!inGame) {
        	text.setText("Game lost!");
        }
    }

    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cCol = x / pixel;
            int cRow = y / pixel;

            boolean doRepaint = false;

            if (!inGame) {

            	try {
					Seri.save("Serialized");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try {
					newGame();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                repaint();
            }

            if ((x < rows * pixel) && (y < rows * pixel)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[(cRow * rows) + cCol] > mineCell) {

                        doRepaint = true;

                        if (field[(cRow * rows) + cCol] <= coveredMineCell) {

                            if (minesLeft > 0) {
                                field[(cRow * rows) + cCol] += markForCell;
                                minesLeft--;
                                String msg = Integer.toString(minesLeft);
                                text.setText(msg);
                            } else text.setText("Nincs tobb zaszlo!");
                        } else {

                            field[(cRow * rows) + cCol] -= markForCell;
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            text.setText(msg);
                        }
                    }

                } else {

                    if (field[(cRow * rows) + cCol] > coveredMineCell) return;

                    if ((field[(cRow * rows) + cCol] > mineCell) && (field[(cRow * rows) + cCol] < markedMineCell)) {

                        field[(cRow * rows) + cCol] -= coverForCell;
                        doRepaint = true;

                        if (field[(cRow * rows) + cCol] == mineCell) inGame = false;

                        if (field[(cRow * rows) + cCol] == emptyCell) find_empty_cells((cRow * rows) + cCol);
                    }
                }
                if (doRepaint) repaint();
            }
        }
    }
}