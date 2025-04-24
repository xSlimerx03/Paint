package paneles;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class EX10FigurasMejoradas extends JPanel {
    private CustomDrawPanel drawingPanel;
    private JPanel colorPreview;
    private JSlider grosorSlider, dashSlider, gapSlider;
    private JCheckBox dashedCheck;

    public EX10FigurasMejoradas() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // Panel de dibujo principal
        drawingPanel = new CustomDrawPanel();
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Panel de selección de figuras
        JPanel figuraPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        figuraPanel.setBorder(BorderFactory.createTitledBorder("Figuras"));

        JButton figuraButton = new JButton("Seleccionar Figura");
        JPopupMenu figuraMenu = new JPopupMenu();

        String[] figuras = {"Casa", "Árbol", "Coche", "Barco", "Avión", "Globo", "Reloj",
                "Flor", "Mariposa", "Pájaro", "Pez", "Helicóptero", "Cohete",
                "Castillo", "Puente", "Torre Eiffel", "Guitarra", "Violín",
                "Piano", "Telescopio"};

        for (int i = 0; i < figuras.length; i++) {
            JMenuItem item = new JMenuItem(figuras[i]);
            int index = i + 1;
            item.addActionListener(e -> drawingPanel.setFigura(index));
            figuraMenu.add(item);
        }

        figuraButton.addActionListener(e -> figuraMenu.show(figuraButton, 0, figuraButton.getHeight()));
        figuraPanel.add(figuraButton);

        // Panel de controles
        JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Panel de Color
        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        colorPanel.setBorder(BorderFactory.createTitledBorder("Color del Borde"));

        colorPreview = new JPanel();
        colorPreview.setPreferredSize(new Dimension(20, 20));
        colorPreview.setBackground(Color.BLACK); // Color inicial negro
        colorPreview.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JButton colorBtn = new JButton("Elegir Color");
        colorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Color del borde", colorPreview.getBackground());
            if (c != null) {
                drawingPanel.setColorBorde(c);
                colorPreview.setBackground(c);
            }
        });

        JButton randomBtn = new JButton("Aleatorio");
        randomBtn.addActionListener(e -> {
            Color c = new Color(
                    (int)(Math.random() * 256),
                    (int)(Math.random() * 256),
                    (int)(Math.random() * 256)
            );
            drawingPanel.setColorBorde(c);
            colorPreview.setBackground(c);
        });

        colorPanel.add(colorBtn);
        colorPanel.add(randomBtn);
        colorPanel.add(colorPreview);

        // Panel de Estilo de Línea
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setBorder(BorderFactory.createTitledBorder("Estilo de Línea"));

        dashedCheck = new JCheckBox("Línea Punteada");
        dashedCheck.addActionListener(e -> updateLineStyle());

        grosorSlider = new JSlider(1, 20, 2);
        grosorSlider.setBorder(BorderFactory.createTitledBorder("Grosor"));
        grosorSlider.setMajorTickSpacing(5);
        grosorSlider.setPaintTicks(true);

        dashSlider = new JSlider(1, 20, 5);
        dashSlider.setBorder(BorderFactory.createTitledBorder("Longitud de Trazo"));
        dashSlider.setEnabled(false);

        gapSlider = new JSlider(1, 20, 3);
        gapSlider.setBorder(BorderFactory.createTitledBorder("Espacio"));
        gapSlider.setEnabled(false);

        // Previsualización
        JPanel previewPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                float[] dash = dashedCheck.isSelected() ?
                        new float[]{dashSlider.getValue(), gapSlider.getValue()} : null;

                g2.setStroke(new BasicStroke(
                        grosorSlider.getValue(),
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f,
                        dash,
                        0.0f
                ));

                g2.setColor(Color.BLACK);
                g2.drawLine(20, getHeight()/2, getWidth()-20, getHeight()/2);
            }
        };
        previewPanel.setPreferredSize(new Dimension(150, 30));

        // Listeners
        ChangeListener sliderListener = e -> {
            updateLineStyle();
            previewPanel.repaint();
        };

        grosorSlider.addChangeListener(sliderListener);
        dashSlider.addChangeListener(sliderListener);
        gapSlider.addChangeListener(sliderListener);

        dashedCheck.addActionListener(e -> {
            dashSlider.setEnabled(dashedCheck.isSelected());
            gapSlider.setEnabled(dashedCheck.isSelected());
            updateLineStyle();
            previewPanel.repaint();
        });

        sliderPanel.add(dashedCheck);
        sliderPanel.add(grosorSlider);
        sliderPanel.add(dashSlider);
        sliderPanel.add(gapSlider);
        sliderPanel.add(previewPanel);

        controlPanel.add(colorPanel);
        controlPanel.add(sliderPanel);

        // Panel inferior
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton clearBtn = new JButton("Borrar Todo");
        clearBtn.addActionListener(e -> drawingPanel.borrarTodo());

        JButton saveBtn = new JButton("Guardar Dibujo");
        saveBtn.addActionListener(e -> drawingPanel.guardarDibujo());

        bottomPanel.add(clearBtn);
        bottomPanel.add(saveBtn);

        // Organización final
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(figuraPanel);
        topContainer.add(controlPanel);

        add(topContainer, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Actualizar estilo inicial
        updateLineStyle();
    }

    private void updateLineStyle() {
        float[] dash = dashedCheck.isSelected() ?
                new float[]{dashSlider.getValue(), gapSlider.getValue()} : null;

        drawingPanel.setEstiloBorde(
                dashedCheck.isSelected() ? 1 : 0, // 1 para discontinuo, 0 para continuo
                dash
        );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Editor de Figuras Mejorado");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new EX10FigurasMejoradas());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}