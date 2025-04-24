package paneles;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.Random;

public class EX5mosaico extends JPanel {
    private WorkPanel workPanel;
    private ControlPanel controlPanel;
    private JSlider sliderRows, sliderCols;
    private ChangeEventHandler changeEventHandler;
    private int rows = 5, cols = 5;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mosaico Mejorado");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new EX5mosaico());
            frame.setVisible(true);
        });
    }

    public EX5mosaico() {
        setLayout(new BorderLayout());
        changeEventHandler = new ChangeEventHandler();
        workPanel = new WorkPanel();
        controlPanel = new ControlPanel();

        add(workPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
    }

    private class ChangeEventHandler implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            rows = sliderRows.getValue();
            cols = sliderCols.getValue();
            workPanel.repaint();
        }
    }

    class WorkPanel extends JPanel {
        public WorkPanel() {
            setBackground(Color.white);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth() / cols;
            int h = getHeight() / rows;
            Random rand = new Random();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int x = j * w;
                    int y = i * h;
                    drawComplexPattern(g2, x, y, w, h, new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
                }
            }
        }
    }

    private void drawComplexPattern(Graphics2D g2, int x, int y, int w, int h, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI * i / 5;
            int x1 = x + (int) (w / 2 + (w / 2) * Math.cos(angle));
            int y1 = y + (int) (h / 2 + (h / 2) * Math.sin(angle));
            int x2 = x + (int) (w / 2 + (w / 2) * Math.cos(angle + Math.PI / 3));
            int y2 = y + (int) (h / 2 + (h / 2) * Math.sin(angle + Math.PI / 3));
            g2.draw(new QuadCurve2D.Double(x + w / 2, y + h / 2, x1, y1, x2, y2));
        }
    }

    class ControlPanel extends JPanel {
        public ControlPanel() {
            setBackground(new Color(40, 40, 40));
            setLayout(new GridLayout(4, 1));
            
            JLabel labelRows = new JLabel("Filas: ", SwingConstants.CENTER);
            labelRows.setForeground(Color.WHITE);
            sliderRows = new JSlider(JSlider.VERTICAL, 1, 20, 5);
            sliderRows.setPaintTicks(true);
            sliderRows.setPaintLabels(true);
            sliderRows.addChangeListener(changeEventHandler);
            
            JLabel labelCols = new JLabel("Columnas: ", SwingConstants.CENTER);
            labelCols.setForeground(Color.WHITE);
            sliderCols = new JSlider(JSlider.VERTICAL, 1, 20, 5);
            sliderCols.setPaintTicks(true);
            sliderCols.setPaintLabels(true);
            sliderCols.addChangeListener(changeEventHandler);
            
            add(labelRows);
            add(sliderRows);
            add(labelCols);
            add(sliderCols);
        }
    }
}
