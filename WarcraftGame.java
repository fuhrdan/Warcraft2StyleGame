import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class WarcraftGame extends JFrame {

    private GamePanel gamePanel;
    private Unit selectedUnit;

    public WarcraftGame() {
        setTitle("Warcraft II Style Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gamePanel = new GamePanel();
        add(gamePanel);

        // Mouse listener to handle clicks for selecting and moving units
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                
                // Check if a unit is clicked
                for (Unit unit : gamePanel.units) {
                    if (unit.contains(clickPoint)) {
                        selectedUnit = unit;
                        return;
                    }
                }

                // If a unit is selected, set its destination to the click point
                if (selectedUnit != null) {
                    selectedUnit.setDestination(clickPoint);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WarcraftGame game = new WarcraftGame();
            game.setVisible(true);
        });
    }

    // Inner class representing the game panel
    private class GamePanel extends JPanel {
        private List<Unit> units;

        public GamePanel() {
            units = new ArrayList<>();

            // Add a few units to the game
            units.add(new Unit(100, 100, Color.BLUE));
            units.add(new Unit(200, 150, Color.RED));
            units.add(new Unit(300, 200, Color.GREEN));

            // Timer for updating the game state (e.g., unit movements)
            Timer timer = new Timer(30, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (Unit unit : units) {
                        unit.updatePosition();
                    }
                    repaint(); // Repaint to reflect updated positions
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the "map" (a simple background)
            g.setColor(new Color(200, 200, 200));
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw each unit
            for (Unit unit : units) {
                unit.draw(g);
            }
        }
    }

    // Inner class representing a unit
    private class Unit {
        private int x, y;
        private int size = 20;
        private Color color;
        private Point destination;

        public Unit(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.destination = new Point(x, y); // Initially, the destination is the current position
        }

        // Check if a point is within this unit (for selection)
        public boolean contains(Point p) {
            return new Rectangle(x - size / 2, y - size / 2, size, size).contains(p);
        }

        // Set the destination for the unit
        public void setDestination(Point destination) {
            this.destination = destination;
        }

        // Update the unit's position towards the destination
        public void updatePosition() {
            if (x < destination.x) x++;
            if (x > destination.x) x--;
            if (y < destination.y) y++;
            if (y > destination.y) y--;
        }

        // Draw the unit
        public void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(x - size / 2, y - size / 2, size, size);

            // Highlight the selected unit with a border
            if (this == selectedUnit) {
                g.setColor(Color.YELLOW);
                g.drawOval(x - size / 2 - 2, y - size / 2 - 2, size + 4, size + 4);
            }
        }
    }
}
