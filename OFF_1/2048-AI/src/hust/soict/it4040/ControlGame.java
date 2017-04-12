/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hust.soict.it4040;

import hust.soict.it4040.Board;
import hust.soict.it4040.ai.AI;
import hust.soict.it4040.dataenum.Direction;
import hust.soict.it4040.dataenum.ActionStatus;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author Hai Quan
 */
public class ControlGame extends JPanel {

    private static final Color BG_COLOR = new Color(0xbbada0);
    private static final String FONT_NAME = "Arial";
    private static final int TILE_SIZE = 80;
    private static final int TILES_MARGIN = 16;

    public Board theGame = new Board();

    public void demoGame() throws CloneNotSupportedException {

        int hintDepth = 5;
        Direction hint = AI.findBestMove(theGame, hintDepth, 2);
        ActionStatus result = ActionStatus.CONTINUE;
        while (result == ActionStatus.CONTINUE || result == ActionStatus.INVALID_MOVE) {
            result = theGame.action(hint);
            repaint();
            if (result == ActionStatus.CONTINUE || result == ActionStatus.INVALID_MOVE) {
                hint = AI.findBestMove(theGame, hintDepth, 2);
            }
        }

    }

    public ControlGame() throws CloneNotSupportedException {
        setFocusable(true);
    }
// vẽ gọi đến hàm drawTile

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
        int[][] arr = theGame.getBoardArray();
        int[][] array = new int[4][4];

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (i == j) {
                    array[i][j] = arr[i][j];
                } else {
                    array[i][j] = arr[j][i];
                }
            }

        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                drawTile(g, array[i][j], i, j);
            }
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
// hàm để vẽ các ô gọi đến các hàm lấy vị trí và lấy màu : offsetCoors,  getForeground    

    private void drawTile(Graphics g2, int tile, int x, int y) {
        Graphics2D g = ((Graphics2D) g2);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);

        g.setColor(getBackground(tile));
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);
        g.setColor(getForeground(tile));

        final Font font = new Font("Arial", Font.BOLD, 32);
        g.setFont(font);

        String s = String.valueOf(tile);
        final FontMetrics fm = getFontMetrics(font);

        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

        if (tile != 0) {
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + theGame.getScore(), 200, 410);

    }

    private static int offsetCoors(int arg) {
        return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
    }

    public static Color getForeground(int tile) {
        return tile < 16 ? new Color(0x776e65) : new Color(0xf9f6f2);
    }

    public static Color getBackground(int tile) {
        switch (tile) {
            case 2:
                return new Color(0xeee4da);
            case 4:
                return new Color(0xede0c8);
            case 8:
                return new Color(0xf2b179);
            case 16:
                return new Color(0xf59563);
            case 32:
                return new Color(0xf67c5f);
            case 64:
                return new Color(0xf65e3b);
            case 128:
                return new Color(0xedcf72);
            case 256:
                return new Color(0xedcc61);
            case 512:
                return new Color(0xedc850);
            case 1024:
                return new Color(0xedc53f);
            case 2048:
                return new Color(0xedc22e);
        }
        return new Color(0xcdc1b4);
    }

    public static void main(String[] args) throws CloneNotSupportedException {

        while (true) {
            printMenu();
            int choice;
            try {
                Scanner sc = new Scanner(System.in);
                choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        JFrame game = new JFrame();
                        game.setTitle("2048 Game");
                        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        game.setSize(410, 450);
                        game.setResizable(false);

                        ControlGame GAME = new ControlGame();
                        game.add(GAME);

                        game.setLocationRelativeTo(null);
                        game.setVisible(true);
                        GAME.demoGame();
                        break;
                    case 2:
                        calculateAccuracy(2);
                        break;
                    case 3:
                        calculateAccuracy(3);
                        break;

                    case 4:
                        calculateAccuracy(4);
                        break;
                    case 5:
                        return;
                    default:
                        throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Wrong choice");
            }
        }
    }

    public static void printMenu() {
        System.out.println();
        System.out.println("Choices:");
        System.out.println("1. Demo 2048 Game");
        System.out.println("2. Estimate the Accuracy of AI with Expectimax");
        System.out.println("3. Estimate the Accuracy of AI with Minimax");
        System.out.println("4. Estimate the Accuracy of AI with AlphaBeta");
        System.out.println("5. Quit");
        System.out.println();
        System.out.println("Enter a number from 1-5:");
    }

    public static void calculateAccuracy(int mode) throws CloneNotSupportedException {
        int wins = 0;
        int total = 100;
        if (mode == 2) {
            System.out.println("Running " + total + " games with Expectimax: ");
        } else if (mode == 3) {
            System.out.println("Running " + total + " games with Minimax: ");
        } else if (mode == 4) {
            System.out.println("Running " + total + " games with AlphaBeta: ");
        }

        long start = System.nanoTime();    
        int hintDepth = 7;
        System.out.println("độ sâu la "+hintDepth);

        for (int i = 0; i < total; ++i) {
            Board theGame = new Board();
            Direction hint = AI.findBestMove(theGame, hintDepth,mode);
            ActionStatus result = ActionStatus.CONTINUE;
            while (result == ActionStatus.CONTINUE || result == ActionStatus.INVALID_MOVE) {
                result = theGame.action(hint);

                if (result == ActionStatus.CONTINUE || result == ActionStatus.INVALID_MOVE) {
                    hint = AI.findBestMove(theGame, hintDepth,mode);
                }
            }
           
            if (result == ActionStatus.WIN) {
                ++wins;
                System.out.println("Game " + (i + 1) + " - won");
            } else {
                System.out.println("Game " + (i + 1) + " - lost");
            }
        }
        long elapsedTime = System.nanoTime() - start;
        System.out.println(" ");
        System.out.println("Time to run "+total+"game"+elapsedTime/1000000000.0+"second");
        System.out.println(wins + " wins out of " + total + " games.");
    }
}
