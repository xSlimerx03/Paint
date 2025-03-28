package paneles;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Ex3EllipsesSlider extends JPanel {
    private WorkPanel workPanel;
    private ControlPanel controlPanel;
    private int n = 10; // Número inicial de elipses
    private int thickness = 2; // Grosor inicial de las elipses
    private Color ellipseColor = Color.MAGENTA; // Color inicial de las elipses
    private JSlider sliderElipses, sliderThickness, sliderColorRed, sliderColorGreen, sliderColorBlue;
    private ChangeEventHandler changeEventHandler;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnippetFrame(new Ex3EllipsesSlider(), "Ellipses Slider"));
    }

    public Ex3EllipsesSlider() {
        super(true);
        setLayout(new BorderLayout());

        changeEventHandler = new ChangeEventHandler(); 
        workPanel = new WorkPanel();
        controlPanel = new ControlPanel();
        
        add(workPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private class ChangeEventHandler implements ChangeListener
    {
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == sliderElipses) {
                n = sliderElipses.getValue();
            } else if (e.getSource() == sliderThickness) {
                thickness = sliderThickness.getValue();
            } else if (e.getSource() == sliderColorRed || e.getSource() == sliderColorGreen || e.getSource() == sliderColorBlue) {
                ellipseColor = new Color(sliderColorRed.getValue(), sliderColorGreen.getValue(), sliderColorBlue.getValue());
            }
            System.out.println("Número de elipses: " + n + ", Grosor: " + thickness + ", Color: " + ellipseColor);
            workPanel.repaint();
        }
    }
    
    class WorkPanel extends JPanel {
        public WorkPanel() {
            setBackground(GV.PALE_BLUE_COLOR);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Ellipse2D ellipse = new Ellipse2D.Double(-50, -25, 100, 50); // Tamaño de la elipse
            g2.translate(getWidth() / 2, getHeight() / 2); // Centrar el dibujo

            System.out.println("Número de elipses a dibujar: " + n);

            g2.setColor(ellipseColor);
            g2.setStroke(new BasicStroke(thickness)); // Establecer grosor
            for (int i = 0; i < n; i++) {
                AffineTransform transform = AffineTransform.getRotateInstance(2 * Math.PI * i / n);
                Shape transformedEllipse = transform.createTransformedShape(ellipse);
                g2.draw(transformedEllipse);
            }
        }
    }

    class ControlPanel extends JPanel {
        public ControlPanel() {
            setBackground(GV.PALE_RED_COLOR);
            setLayout(new FlowLayout());

            sliderElipses = new JSlider(JSlider.HORIZONTAL, 1, 50, n); // Rango del slider de elipses
            sliderElipses.setMajorTickSpacing(10);
            sliderElipses.setMinorTickSpacing(1);
            sliderElipses.setPaintTicks(true);
            sliderElipses.setPaintLabels(true);
            sliderElipses.addChangeListener(changeEventHandler);

            sliderThickness = new JSlider(JSlider.HORIZONTAL, 1, 10, thickness); // Rango del slider de grosor
            sliderThickness.setMajorTickSpacing(1);
            sliderThickness.setMinorTickSpacing(1);
            sliderThickness.setPaintTicks(true);
            sliderThickness.setPaintLabels(true);
            sliderThickness.addChangeListener(changeEventHandler);

            sliderColorRed = new JSlider(JSlider.HORIZONTAL, 0, 255, ellipseColor.getRed()); // Rango del slider de rojo
            sliderColorRed.setMajorTickSpacing(50);
            sliderColorRed.setMinorTickSpacing(1);
            sliderColorRed.setPaintTicks(true);
            sliderColorRed.setPaintLabels(true);
            sliderColorRed.addChangeListener(changeEventHandler);

            sliderColorGreen = new JSlider(JSlider.HORIZONTAL, 0, 255, ellipseColor.getGreen()); // Rango del slider de verde
            sliderColorGreen.setMajorTickSpacing(50);
            sliderColorGreen.setMinorTickSpacing(1);
            sliderColorGreen.setPaintTicks(true);
            sliderColorGreen.setPaintLabels(true);
            sliderColorGreen.addChangeListener(changeEventHandler);

            sliderColorBlue = new JSlider(JSlider.HORIZONTAL, 0, 255, ellipseColor.getBlue()); // Rango del slider de azul
            sliderColorBlue.setMajorTickSpacing(50);
            sliderColorBlue.setMinorTickSpacing(1);
            sliderColorBlue.setPaintTicks(true);
            sliderColorBlue.setPaintLabels(true);
            sliderColorBlue.addChangeListener(changeEventHandler);

            add(new JLabel("Número de elipses: "));
            add(sliderElipses);

            add(new JLabel("Grosor: "));
            add(sliderThickness);

            add(new JLabel("Color Rojo: "));
            add(sliderColorRed);

            add(new JLabel("Color Verde: "));
            add(sliderColorGreen);

            add(new JLabel("Color Azul: "));
            add(sliderColorBlue);
        }
    }
}
