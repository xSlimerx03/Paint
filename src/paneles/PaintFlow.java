/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paneles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PaintFlow extends JPanel {
    private DrawPanel drawPanel;

    public PaintFlow() {
        setLayout(new BorderLayout()); // Usamos BorderLayout para estructura principal

        // Barra de herramientas con FlowLayout
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Alineación a la izquierda
        JButton clearBtn = new JButton("Borrar");
        JButton colorBtn = new JButton("Color");
        JButton strokeWidthBtn = new JButton("Ancho del Trazo");

        toolBar.add(clearBtn);
        toolBar.add(colorBtn);
        toolBar.add(strokeWidthBtn);
        add(toolBar, BorderLayout.NORTH);

        // Lienzo de dibujo en el centro
        drawPanel = new DrawPanel();
        add(drawPanel, BorderLayout.CENTER);

        // Acción para borrar el dibujo
        clearBtn.addActionListener(e -> drawPanel.clearCanvas());

        // Selector de color
        colorBtn.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Selecciona un color", drawPanel.getCurrentColor());
            if (newColor != null) {
                drawPanel.setCurrentColor(newColor);
            }
        });

        // Selector de ancho del trazo
        strokeWidthBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Introduce el ancho del trazo:", drawPanel.getStrokeWidth());
            try {
                float strokeWidth = Float.parseFloat(input);
                drawPanel.setStrokeWidth(strokeWidth);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, introduce un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Panel de dibujo personalizado
    class DrawPanel extends JPanel {
        private Image image;
        private Graphics2D g2;
        private int prevX, prevY;
        private Color currentColor = Color.BLACK;
        private float strokeWidth = 2.0f;

        public DrawPanel() {
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    prevX = e.getX();
                    prevY = e.getY();
                    if (g2 == null) {
                        initGraphics();
                    }
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    g2.setColor(currentColor);
                    g2.setStroke(new BasicStroke(strokeWidth));
                    g2.drawLine(prevX, prevY, x, y);
                    prevX = x;
                    prevY = y;
                    repaint();
                }
            });
        }

        private void initGraphics() {
            image = createImage(getWidth(), getHeight());
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(strokeWidth));
            g2.setColor(currentColor);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        public void clearCanvas() {
            if (g2 != null) {
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(currentColor);
                repaint();
            }
        }

        public void setCurrentColor(Color color) {
            this.currentColor = color;
        }

        public Color getCurrentColor() {
            return currentColor;
        }

        public void setStrokeWidth(float strokeWidth) {
            this.strokeWidth = strokeWidth;
        }

        public float getStrokeWidth() {
            return strokeWidth;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, null);
            }
        }
    }

    public static void main(String[] args) {
        
        // Crear el JFrame y agregar el PaintFlow como contenido
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mini Paint - FlowLayout");
            frame.setSize(900, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            PaintFlow paintFlow = new PaintFlow();
            frame.add(paintFlow); // Agregar el PaintFlow al JFrame

            frame.setVisible(true);
        });
    }
}