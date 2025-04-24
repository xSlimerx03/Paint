package paneles;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class EX4RandomEllipse extends JPanel {
    private WorkPanel workPanel;
    private ControlPanel controlPanel;
    private JSlider slider;
    private ChangeEventHandler changeEventHandler;
    
    private java.util.List<EllipseWithColor> ellipsesList = new ArrayList<>();
    private Random rand = new Random();
    private int currentSize = 50; // Tamaño base inicial
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ellipses Slider");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new EX4RandomEllipse());
            frame.setVisible(true);
        });
    }

    public EX4RandomEllipse() {
        setLayout(new BorderLayout());
        changeEventHandler = new ChangeEventHandler();
        workPanel = new WorkPanel();
        controlPanel = new ControlPanel();

        add(workPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        // Inicializar con 10 elipses
        slider.setValue(10);
        updateEllipses(10);
    }

    private class ChangeEventHandler implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            int newValue = slider.getValue();
            currentSize = 20 + newValue * 2; // Tamaño proporcional al valor del slider
            updateEllipses(newValue);
            workPanel.repaint();
        }
    }

    private void updateEllipses(int newCount) {
        // Generar nuevo color aleatorio para todas las elipses
        Color newColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        
        // Ajustar número de elipses
        while (ellipsesList.size() < newCount) {
            ellipsesList.add(new EllipseWithColor(newColor, currentSize));
        }
        while (ellipsesList.size() > newCount) {
            ellipsesList.remove(ellipsesList.size() - 1);
        }
        
        // Actualizar tamaño de todas las elipses
        for (EllipseWithColor ew : ellipsesList) {
            ew.resizeEllipse(currentSize);
            ew.color = newColor; // Actualizar color
        }
    }

    class WorkPanel extends JPanel {
        public WorkPanel() {
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.translate(getWidth() / 2, getHeight() / 2);

            for (int i = 0; i < ellipsesList.size(); i++) {
                EllipseWithColor ew = ellipsesList.get(i);
                g2.setColor(ew.color);
                AffineTransform transform = AffineTransform.getRotateInstance(2 * Math.PI * i / ellipsesList.size());
                Shape transformedEllipse = transform.createTransformedShape(ew.ellipse);
                g2.draw(transformedEllipse);
            }
            
            // Mostrar información
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Elipses: " + ellipsesList.size() + " | Tamaño: " + currentSize, 
                          -getWidth()/2 + 10, -getHeight()/2 + 20);
        }
    }

    class ControlPanel extends JPanel {
        public ControlPanel() {
            setBackground(new Color(240, 240, 240));
            setLayout(new GridBagLayout()); // Usamos GridBagLayout para centrar
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            
            slider = new JSlider(JSlider.HORIZONTAL, 1, 50, 10);
            slider.setMajorTickSpacing(10);
            slider.setMinorTickSpacing(1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(changeEventHandler);
            
            JLabel label = new JLabel("Control de elipses: ");
            add(label, gbc);
            gbc.gridx = 1;
            add(slider, gbc);
        }
    }

    class EllipseWithColor {
        Shape ellipse;
        Color color;
        private double baseSize = 50;

        public EllipseWithColor(Color color, int size) {
            this.color = color;
            resizeEllipse(size);
        }

        public void resizeEllipse(int size) {
            double scale = size / baseSize;
            this.ellipse = new Ellipse2D.Double(-50 * scale, -25 * scale, 100 * scale, 50 * scale);
        }
    }
}