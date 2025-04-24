package paneles;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class Ex3EllipsesSlider extends JPanel {
    private WorkPanel workPanel;
    private ControlPanel controlPanel;
    private int n = 10; // Número inicial de elipses
    private int thickness = 2; // Grosor inicial
    private Color ellipseColor = Color.MAGENTA; // Color inicial
    private JSlider sliderElipses, sliderThickness, sliderColorRed, sliderColorGreen, sliderColorBlue;
    private JPanel colorBox; // Panel para mostrar el color actual

    public Ex3EllipsesSlider() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        
        ChangeHandler changeHandler = new ChangeHandler();
        workPanel = new WorkPanel();
        controlPanel = new ControlPanel(changeHandler);
        
        add(workPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private class ChangeHandler implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == sliderElipses) {
                n = sliderElipses.getValue();
            } else if (e.getSource() == sliderThickness) {
                thickness = sliderThickness.getValue();
            } else if (e.getSource() == sliderColorRed || e.getSource() == sliderColorGreen || e.getSource() == sliderColorBlue) {
                ellipseColor = new Color(
                    sliderColorRed.getValue(), 
                    sliderColorGreen.getValue(), 
                    sliderColorBlue.getValue()
                );
                // Actualizar el cuadro de color
                colorBox.repaint();
            }
            workPanel.repaint();
        }
    }
    
    class WorkPanel extends JPanel {
        public WorkPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(220, 220, 230)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            // Configurar renderizado de alta calidad
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Guardar la transformación original
            AffineTransform originalTransform = g2.getTransform();
            
            try {
                // Dibujar elipses
                Ellipse2D ellipse = new Ellipse2D.Double(-50, -25, 100, 50);
                g2.translate(getWidth() / 2, getHeight() / 2);
                
                g2.setColor(ellipseColor);
                g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                for (int i = 0; i < n; i++) {
                    AffineTransform transform = AffineTransform.getRotateInstance(2 * Math.PI * i / n);
                    Shape transformedEllipse = transform.createTransformedShape(ellipse);
                    g2.draw(transformedEllipse);
                }
                
                // Restaurar transformación para el texto
                g2.setTransform(originalTransform);
                
                // Mostrar información
                g2.setColor(new Color(80, 80, 80));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                String info = String.format("Elipses: %d | Grosor: %d | Color: RGB(%d, %d, %d)", 
                    n, thickness, ellipseColor.getRed(), ellipseColor.getGreen(), ellipseColor.getBlue());
                g2.drawString(info, 10, 20);
            } finally {
                // Asegurarse de restaurar la transformación
                g2.setTransform(originalTransform);
            }
        }
    }

    class ControlPanel extends JPanel {
        public ControlPanel(ChangeListener listener) {
            setBackground(new Color(250, 250, 255));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 230)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            setLayout(new GridLayout(3, 2, 15, 10));
            
            // Configuración de estilo para etiquetas
            Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
            
            // Slider de cantidad de elipses
            JPanel elipsesPanel = createControlPanel("Número de elipses:", labelFont);
            sliderElipses = createSlider(1, 50, n, 10, 1);
            sliderElipses.addChangeListener(listener);
            elipsesPanel.add(sliderElipses);
            
            // Slider de grosor
            JPanel thicknessPanel = createControlPanel("Grosor del trazo:", labelFont);
            sliderThickness = createSlider(1, 10, thickness, 1, 1);
            sliderThickness.addChangeListener(listener);
            thicknessPanel.add(sliderThickness);
            
            // Sliders de color RGB
            JPanel redPanel = createControlPanel("Rojo:", labelFont);
            sliderColorRed = createColorSlider(ellipseColor.getRed());
            sliderColorRed.addChangeListener(listener);
            redPanel.add(sliderColorRed);
            
            JPanel greenPanel = createControlPanel("Verde:", labelFont);
            sliderColorGreen = createColorSlider(ellipseColor.getGreen());
            sliderColorGreen.addChangeListener(listener);
            greenPanel.add(sliderColorGreen);
            
            JPanel bluePanel = createControlPanel("Azul:", labelFont);
            sliderColorBlue = createColorSlider(ellipseColor.getBlue());
            sliderColorBlue.addChangeListener(listener);
            bluePanel.add(sliderColorBlue);
            
            // Panel de visualización de color
            JPanel colorPreviewPanel = new JPanel(new BorderLayout());
            colorPreviewPanel.setOpaque(false);
            JLabel previewLabel = new JLabel("Color actual:", JLabel.CENTER);
            previewLabel.setFont(labelFont);
            colorPreviewPanel.add(previewLabel, BorderLayout.NORTH);
            
            colorBox = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(ellipseColor);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.BLACK);
                    g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
                }
            };
            colorBox.setPreferredSize(new Dimension(50, 30));
            colorPreviewPanel.add(colorBox, BorderLayout.CENTER);
            
            // Añadir todos los paneles
            add(elipsesPanel);
            add(thicknessPanel);
            add(redPanel);
            add(greenPanel);
            add(bluePanel);
            add(colorPreviewPanel);
        }
        
        private JPanel createControlPanel(String labelText, Font font) {
            JPanel panel = new JPanel(new BorderLayout(10, 5));
            panel.setOpaque(false);
            
            JLabel label = new JLabel(labelText);
            label.setFont(font);
            label.setForeground(new Color(70, 70, 70));
            
            panel.add(label, BorderLayout.WEST);
            return panel;
        }
        
        private JSlider createSlider(int min, int max, int value, int majorTick, int minorTick) {
            JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
            slider.setMajorTickSpacing(majorTick);
            slider.setMinorTickSpacing(minorTick);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setBackground(new Color(250, 250, 255));
            slider.setForeground(new Color(70, 70, 70));
            slider.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            return slider;
        }
        
        private JSlider createColorSlider(int value) {
            JSlider slider = createSlider(0, 255, value, 50, 10);
            slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider) {
                @Override
                public void paintTrack(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    Rectangle trackBounds = trackRect;
                    int trackHeight = 6;
                    int y = trackBounds.y + (trackBounds.height - trackHeight) / 2;
                    
                    // Fondo de la barra
                    g2d.setColor(new Color(220, 220, 230));
                    g2d.fillRoundRect(trackBounds.x, y, trackBounds.width, trackHeight, trackHeight, trackHeight);
                    
                    // Barra de progreso
                    int progressWidth = thumbRect.x + thumbRect.width/2 - trackBounds.x;
                    g2d.setColor(new Color(180, 180, 190));
                    g2d.fillRoundRect(trackBounds.x, y, progressWidth, trackHeight, trackHeight, trackHeight);
                }
                
                @Override
                public void paintThumb(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    Color thumbColor = new Color(100, 100, 100);
                    g2d.setColor(thumbColor);
                    g2d.fillOval(thumbRect.x, thumbRect.y + (thumbRect.height - 14)/2, 14, 14);
                    
                    // Efecto de brillo
                    g2d.setColor(new Color(255, 255, 255, 150));
                    g2d.fillOval(thumbRect.x + 3, thumbRect.y + (thumbRect.height - 14)/2 + 3, 5, 5);
                }
            });
            return slider;
        }
    }
}