import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class CellularAutomataFrame extends JFrame {
    CellularAutomataCanvas gridCanvas = null;

    public CellularAutomataFrame(Grid grid) {
        super("Cellular Automata");
        this.setSize(600, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
//        this.validate();
        gridCanvas = new CellularAutomataCanvas(grid, 1000);
        gridCanvas.setSize(500, 500);
        gridCanvas.setBackground(Color.CYAN);
        this.add(gridCanvas);
        Thread gridThread = new Thread(gridCanvas, "CellularAutomataThread");
        gridThread.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gridCanvas.stop();
    }
}

class CellularAutomataCanvas extends Canvas implements Runnable {
    private Grid grid;
    private int interval;
    private boolean flag = true;

    CellularAutomataCanvas(Grid grid, int interval) {
        this.grid = grid;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (flag) {
            try {
                grid.update();
                this.repaint();
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void stop() {
        flag = false;
    }

    @Override
    public void paint(Graphics g) {
        int nx = grid.getMaxX();
        int ny = grid.getMaxY();
        int wx = this.getWidth() / nx;
        int wy = this.getHeight() / ny;
        Set<Coordinate> coordinates = grid.keySet();
        try {
            for (Coordinate coordinate : coordinates) {
                FilledCell cell = (FilledCell) grid.get(coordinate);
                g.setColor(cell.getCurrentSymbol());
                g.fillRect(coordinate.getX() * wx, coordinate.getY() * wy, wx, wy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}