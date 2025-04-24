package paneles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class EX9_FigurasToolsBar extends JPanel {
    private CustomDrawPanel drawingPanel;
    private Color colorBordeActual = Color.BLACK;
    private JPanel colorPreview;

    public EX9_FigurasToolsBar() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        
        // Crear el panel de dibujo personalizado
        drawingPanel = new CustomDrawPanel();
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        // Configurar el panel de vista previa del color
        colorPreview = new JPanel();
        colorPreview.setPreferredSize(new Dimension(20, 20));
        colorPreview.setBackground(colorBordeActual);
        colorPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Panel principal para las barras de herramientas
        JPanel toolBarsPanel = new JPanel();
        toolBarsPanel.setLayout(new BoxLayout(toolBarsPanel, BoxLayout.Y_AXIS));
        
        // 1. Barra de herramientas de figuras (con fondo azul claro)
        JToolBar figuraToolBar = new JToolBar();
        figuraToolBar.setFloatable(false);
        figuraToolBar.setBackground(new Color(200, 220, 255)); // Azul claro
        figuraToolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(180, 200, 240)));
        
        JButton figuraButton = createModernButton("Figura");
        JPopupMenu figuraMenu = new JPopupMenu();
        
        String[] figurasDisponibles = {
            "Casa", "Árbol", "Coche", "Barco", "Avión",
            "Globo aerostático", "Reloj", "Flor", "Mariposa", "Pájaro",
            "Pez", "Helicóptero", "Cohete", "Castillo", "Puente",
            "Torre Eiffel", "Guitarra", "Violín", "Piano", "Telescopio"
        };
        
        for (int i = 0; i < figurasDisponibles.length; i++) {
            final int figuraIndex = i + 1;
            JMenuItem menuItem = new JMenuItem(figurasDisponibles[i]);
            menuItem.addActionListener(e -> drawingPanel.setFigura(figuraIndex));
            figuraMenu.add(menuItem);
            
            if ((i + 1) % 5 == 0 && i != figurasDisponibles.length - 1) {
                figuraMenu.addSeparator();
            }
        }
        
        figuraButton.addActionListener(e -> 
            figuraMenu.show(figuraButton, 0, figuraButton.getHeight()));
        
        figuraToolBar.add(figuraButton);
        
        // 2. Barra de herramientas de color (con fondo verde claro)
        JToolBar colorToolBar = new JToolBar();
        colorToolBar.setFloatable(false);
        colorToolBar.setBackground(new Color(220, 255, 220)); // Verde claro
        colorToolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 240, 200)));
        
        // Botón para seleccionar color del borde
        JButton colorBordeButton = createModernButton("Color Borde");
        colorBordeButton.addActionListener(e -> {
            Color nuevoColor = JColorChooser.showDialog(this, "Seleccione color del borde", colorBordeActual);
            if (nuevoColor != null) {
                colorBordeActual = nuevoColor;
                colorPreview.setBackground(colorBordeActual);
                drawingPanel.setColorBorde(colorBordeActual);
            }
        });
        colorToolBar.add(colorBordeButton);
        
        // Botón para color aleatorio
        JButton randomColorButton = createModernButton("Aleatorio");
        randomColorButton.addActionListener(e -> {
            colorBordeActual = new Color(
                new Random().nextInt(256),
                new Random().nextInt(256),
                new Random().nextInt(256)
            );
            colorPreview.setBackground(colorBordeActual);
            drawingPanel.setColorBorde(colorBordeActual);
        });
        colorToolBar.add(randomColorButton);
        
        // Panel de vista previa del color
        colorToolBar.add(Box.createHorizontalStrut(10));
        colorToolBar.add(new JLabel("Borde:"));
        colorToolBar.add(Box.createHorizontalStrut(5));
        colorToolBar.add(colorPreview);
        
        // Agregar ambas barras al panel de barras
        toolBarsPanel.add(figuraToolBar);
        toolBarsPanel.add(colorToolBar);
        
        // Panel de controles inferiores
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionPanel.setBackground(Color.WHITE);
        
        JButton botonBorrarUltimo = createModernButton("Borrar Última");
        botonBorrarUltimo.addActionListener(e -> drawingPanel.borrarUltimaFigura());
        
        JButton botonBorrarTodo = createModernButton("Borrar Todo");
        botonBorrarTodo.addActionListener(e -> drawingPanel.borrarTodo());
        
        actionPanel.add(botonBorrarUltimo);
        actionPanel.add(botonBorrarTodo);
        
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(actionPanel);
        
        // Agregar componentes al panel principal
        add(toolBarsPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(new Color(245, 245, 245));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(230, 230, 230));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(245, 245, 245));
            }
        });
        
        return button;
    }
    
    public static JPanel createWrappedPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(new EX9_FigurasToolsBar(), BorderLayout.CENTER);
        return wrapper;
    }
}