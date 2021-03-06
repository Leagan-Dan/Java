package panels;

import mainframe.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;
import java.util.Random;

public class Canvas extends JPanel {
    final MainFrame frame;
    int canvasWidth = 800, canvasHeight = 800;
    BufferedImage image;
    Graphics2D offscreen;
    int rows, cols;
    int boardWidth, boardHeight;
    int cellWidth, cellHeight;
    int padX, padY;
    int stoneSize = 20;
    boolean blueStone = true;
    int[] arrayRandom;
    int indexRandom = 0;
    boolean firstDraw = true;
    int [][] stonesMatrix;
    boolean finish=false;

    public Canvas(MainFrame frame) {
        this.frame = frame;
        createOffscreenImage();
        this.arrayRandom = new int[1000];
        this.firstDraw = true;
        this.stonesMatrix=new int[11][11];
        for(int[] arr1 : stonesMatrix)
            Arrays.fill(arr1, -1);
    }

    public void createOffscreenImage() {
        image = new BufferedImage(
                canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        offscreen = image.createGraphics();
        offscreen.setColor(Color.WHITE);
        offscreen.fillRect(0, 0, canvasWidth, canvasHeight);
    }

    /**
     * Paints the rows, columns, circles and random sticks, on the grid
     * Listens for any clicks, to draw the blue or red stones, depending on the turn
     * Uses stonesMatrix to validate and update any stone drawn
     * If there is no other place to draw a stone, the winner is announced
     */
    private void paintGrid(Graphics2D graphics) {
        offscreen.setStroke(new BasicStroke(1));
        offscreen.setColor(Color.LIGHT_GRAY);
        for (int row = 0; row < rows; row++) {
            int x1 = padX;
            int y1 = padY + row * cellHeight;
            int x2 = padX + boardWidth;
            int y2 = y1;
            offscreen.drawLine(x1, y1, x2, y2);
        }

        for (int col = 0; col < cols; col++) {
            int x1 = padX;
            int y1 = padY + col * cellWidth;
            int x2 = padX + boardHeight;
            int y2 = y1;
            offscreen.drawLine(y1, x1, y2, x2);
        }

        Random rand=new Random();
        this.indexRandom=0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int rand_int1 = rand.nextInt(1000);
                int x = padX + col * cellWidth;
                int y = padY + row * cellHeight;
                offscreen.setColor(Color.DARK_GRAY);
                offscreen.drawOval(x - stoneSize / 2, y - stoneSize / 2, stoneSize, stoneSize);
                JLabel label = new JLabel();
                label.setBounds(x - stoneSize / 2, y - stoneSize / 2, stoneSize, stoneSize);
                label.setOpaque(false);
                int finalRow = row;
                int finalCol = col;
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent event) {
                        if(stonesMatrix[finalRow][finalCol]==0) {
                            drawStone(x, y);
                            stonesMatrix[finalRow][finalCol] = 1;
                            finish = true;
                            for (int index1 = 0; index1 < rows; index1++) {
                                for (int index2 = 0; index2 < cols; index2++) {
                                    if (stonesMatrix[index1][index2] == 0)
                                        finish = false;
                                }
                            }
                            if (finish) {
                                if (blueStone)
                                    System.out.println("Red player wins");
                                else
                                    System.out.println("Blue player wins");
                            }
                        }
                        repaint();
                    }
                });
                this.add(label);
                this.setVisible(true);
                offscreen.setStroke(new BasicStroke(5));
                if (row < rows-1 && col < cols-1) {
                    if (this.firstDraw) {
                        if ((rand_int1) % 2 == 0) {
                            offscreen.drawLine(x, y, padX + (col + 1) * cellWidth, padY + (row) * cellHeight);
                            offscreen.drawLine(x, y, padX + (col) * cellWidth, padY + (row + 1) * cellHeight);
                            stonesMatrix[row][col]=0;
                            stonesMatrix[row][col+1]=0;
                            stonesMatrix[row+1][col]=0;
                        }
                        this.arrayRandom[indexRandom] = rand_int1;
                        this.indexRandom++;
                    } else {
                        if (this.arrayRandom[this.indexRandom] % 2 == 0) {
                            offscreen.drawLine(x, y, padX + (col + 1) * cellWidth, padY + (row) * cellHeight);
                            offscreen.drawLine(x, y, padX + (col) * cellWidth, padY + (row + 1) * cellHeight);
                        }
                        this.indexRandom++;
                    }
                }
                offscreen.setStroke(new BasicStroke(1));
            }
        }
        this.firstDraw=false;


    }

    /**
     * Initializes the frame
     */
    final void init(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.padX = stoneSize + 10;
        this.padY = stoneSize + 10;
        this.cellWidth = (canvasWidth - 2 * padX) / (cols - 1);
        this.cellHeight = (canvasHeight - 2 * padY) / (rows - 1);
        this.boardWidth = (cols - 1) * cellWidth;
        this.boardHeight = (rows - 1) * cellHeight;
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        this.firstDraw=true;
        repaint();
    }

    /**
     * Draws a red or blue circle, depending on the turn, at the mouse coordinates x and y
     * @param x mouse coordinate
     * @param y mouse coordinate
     */
    private void drawStone(int x, int y) {
        if (this.blueStone) {
            offscreen.setColor(Color.BLUE);
            blueStone = false;
        } else {
            offscreen.setColor(Color.RED);
            blueStone = true;
        }

        int w = canvasWidth;
        int h = canvasHeight;
        offscreen.fillOval(x - 10, y - 10, w / 38, h / 38);
    }

    /**
     * Sets an image on the screen
     * @param image loaded image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        offscreen = image.createGraphics();
        offscreen.setColor(Color.WHITE);
        repaint();
    }

    @Override
    public void update(Graphics graphics) {
    }

    /**
     * Creates the canvas component
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, canvasWidth, canvasHeight);
        paintGrid(graphics2D);

        graphics.drawImage(image, 0, 0, this);
    }

}
