package paneles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class CustomDrawPanel extends JPanel {
    private List<Shape> figuras = new ArrayList<>();
    private List<Color> coloresBorde = new ArrayList<>();
    private List<Stroke> estilosBorde = new ArrayList<>();
    private Point inicioArrastre;
    private Shape figuraActual;
    private int figuraSeleccionada = 1; // Figura predeterminada: círculo
    private Color colorBordeActual = Color.BLACK; // Color de borde predeterminado
    private Stroke estiloBordeActual = new BasicStroke(1); // Estilo de borde predeterminado

    public CustomDrawPanel() {
        setPreferredSize(new Dimension(800, 600)); // Tamaño preferido del panel
        setBackground(Color.WHITE); // Fondo blanco para mejor visibilidad

        // Agrega listeners para manejar el dibujo de figuras
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                inicioArrastre = e.getPoint();
                figuraActual = crearFigura(e.getX(), e.getY(), 1, 1);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (figuraActual != null) {
                    figuras.add(figuraActual);
                    coloresBorde.add(colorBordeActual);
                    estilosBorde.add(estiloBordeActual);
                    figuraActual = null;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (figuraActual != null) {
                    int width = e.getX() - inicioArrastre.x;
                    int height = e.getY() - inicioArrastre.y;
                    figuraActual = crearFigura(inicioArrastre.x, inicioArrastre.y, width, height);
                    repaint();
                }
            }
        });
    }

    public void setFigura(int figura) {
        this.figuraSeleccionada = figura;
    }

    public void setColorBorde(Color color) {
        this.colorBordeActual = color;
    }

    public void setEstiloBorde(int tipo, float[] patron) {
        switch (tipo) {
            case 0: // Continuo
                this.estiloBordeActual = new BasicStroke(1);
                break;
            case 1: // Discontinuo
                this.estiloBordeActual = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, patron, 0.0f);
                break;
            case 2: // Punteado
                this.estiloBordeActual = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, patron, 0.0f);
                break;
            default:
                this.estiloBordeActual = new BasicStroke(1);
        }
    }

    public void borrarUltimaFigura() {
        if (!figuras.isEmpty()) {
            figuras.remove(figuras.size() - 1);
            coloresBorde.remove(coloresBorde.size() - 1);
            estilosBorde.remove(estilosBorde.size() - 1);
            repaint();
        }
    }

    public void borrarTodo() {
        figuras.clear();
        coloresBorde.clear();
        estilosBorde.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibuja todas las figuras almacenadas con sus colores y estilos correspondientes
        for (int i = 0; i < figuras.size(); i++) {
            g2.setColor(coloresBorde.get(i));
            g2.setStroke(estilosBorde.get(i));
            g2.draw(figuras.get(i));
        }

        // Dibuja la figura actual (si existe) con el color y estilo actual
        if (figuraActual != null) {
            g2.setColor(colorBordeActual);
            g2.setStroke(estiloBordeActual);
            g2.draw(figuraActual);
        }
    }

    private Shape crearFigura(int x, int y, int width, int height) {
        switch (figuraSeleccionada) {
            case 1: return new Ellipse2D.Double(x, y, width, height); // Círculo
            case 2: return new Rectangle2D.Double(x, y, width, height); // Rectángulo
            case 3: return new Ellipse2D.Double(x, y, width, height); // Elipse
            case 4: return new RoundRectangle2D.Double(x, y, width, height, 20, 20); // Rectángulo redondeado
            case 5: return new Arc2D.Double(x, y, width, height, 45, 270, Arc2D.PIE); // Arco
            case 6: return new Polygon(new int[]{x, x + width / 2, x + width}, new int[]{y + height, y, y + height}, 3); // Triángulo
            case 7: return new Polygon(new int[]{x + width / 2, x, x + width / 4, x + width * 3 / 4, x + width}, new int[]{y, y + height / 2, y + height, y + height, y + height / 2}, 5); // Estrella
            case 8: return new QuadCurve2D.Double(x, y + height, x + width / 2, y, x + width, y + height); // Curva cuadrática
            case 9: return new CubicCurve2D.Double(x, y + height, x + width / 4, y, x + width * 3 / 4, y, x + width, y + height); // Curva cúbica
            case 10: return new Ellipse2D.Double(x, y, width, height); // Óvalo
            case 11: return crearCorazon(x, y, width, height); // Corazón
            case 12: return crearCuboSimple(x, y, width, height); // Cubo simple
            case 13: return crearPiramide(x, y, width, height); // Pirámide
            case 14: return crearCaritaFeliz(x, y, width, height); // Carita feliz
            case 15: return crearCaritaTriste(x, y, width, height); // Carita triste
            case 16: return crearCorazonRoto(x, y, width, height); // Corazón roto
            case 17: return crearSol(x, y, width, height); // Sol
            case 18: return crearArbolSimple(x, y, width, height); // Árbol simple
            case 19: return crearOjoSimple(x, y, width, height); // Ojo simple
            case 20: return crearDiamante(x, y, width, height); // Diamante
            default: return null;
        }
    }

    // Métodos para crear figuras especiales (sin cambios)
    private Shape crearCorazon(int x, int y, int width, int height) {
        Path2D corazon = new Path2D.Double();
        corazon.moveTo(x + width / 2, y + height / 4);
        corazon.curveTo(x + width, y, x + width, y + height / 2, x + width / 2, y + height);
        corazon.curveTo(x, y + height / 2, x, y, x + width / 2, y + height / 4);
        corazon.closePath();
        return corazon;
    }

    private Shape crearCuboSimple(int x, int y, int width, int height) {
        Path2D cubo = new Path2D.Double();
        cubo.moveTo(x, y);
        cubo.lineTo(x + width, y);
        cubo.lineTo(x + width, y + height);
        cubo.lineTo(x, y + height);
        cubo.closePath();
        cubo.moveTo(x + width / 4, y - height / 4);
        cubo.lineTo(x + width * 5 / 4, y - height / 4);
        cubo.lineTo(x + width * 5 / 4, y + height * 3 / 4);
        cubo.lineTo(x + width / 4, y + height * 3 / 4);
        cubo.closePath();
        cubo.moveTo(x, y);
        cubo.lineTo(x + width / 4, y - height / 4);
        cubo.moveTo(x + width, y);
        cubo.lineTo(x + width * 5 / 4, y - height / 4);
        cubo.moveTo(x + width, y + height);
        cubo.lineTo(x + width * 5 / 4, y + height * 3 / 4);
        cubo.moveTo(x, y + height);
        cubo.lineTo(x + width / 4, y + height * 3 / 4);
        return cubo;
    }

    private Shape crearPiramide(int x, int y, int width, int height) {
        Polygon piramide = new Polygon();
        piramide.addPoint(x + width / 2, y);
        piramide.addPoint(x, y + height);
        piramide.addPoint(x + width, y + height);
        return piramide;
    }

    private Shape crearCaritaFeliz(int x, int y, int width, int height) {
        Path2D carita = new Path2D.Double();
        carita.append(new Ellipse2D.Double(x, y, width, height), false);
        carita.append(new Ellipse2D.Double(x + width / 4, y + height / 3, width / 6, height / 6), false);
        carita.append(new Ellipse2D.Double(x + width * 2 / 3, y + height / 3, width / 6, height / 6), false);
        carita.append(new Arc2D.Double(x + width / 4, y + height / 2, width / 2, height / 4, 0, -180, Arc2D.OPEN), false);
        return carita;
    }

    private Shape crearCaritaTriste(int x, int y, int width, int height) {
        Path2D carita = new Path2D.Double();
        carita.append(new Ellipse2D.Double(x, y, width, height), false);
        carita.append(new Ellipse2D.Double(x + width / 4, y + height / 3, width / 6, height / 6), false);
        carita.append(new Ellipse2D.Double(x + width * 2 / 3, y + height / 3, width / 6, height / 6), false);
        carita.append(new Arc2D.Double(x + width / 4, y + height * 2 / 3, width / 2, height / 4, 0, 180, Arc2D.OPEN), false);
        return carita;
    }

    private Shape crearCorazonRoto(int x, int y, int width, int height) {
        Path2D corazonRoto = new Path2D.Double();
        corazonRoto.moveTo(x + width / 2, y + height / 4);
        corazonRoto.curveTo(x + width, y, x + width, y + height / 2, x + width / 2, y + height);
        corazonRoto.curveTo(x, y + height / 2, x, y, x + width / 2, y + height / 4);
        corazonRoto.closePath();
        corazonRoto.moveTo(x + width / 2, y + height / 4);
        corazonRoto.lineTo(x + width / 2, y + height);
        return corazonRoto;
    }

    private Shape crearSol(int x, int y, int width, int height) {
        Path2D sol = new Path2D.Double();
        sol.append(new Ellipse2D.Double(x, y, width, height), false);
        for (int i = 0; i < 8; i++) {
            double angle = Math.PI * i / 4;
            sol.moveTo(x + width / 2 + Math.cos(angle) * width / 2, y + height / 2 + Math.sin(angle) * height / 2);
            sol.lineTo(x + width / 2 + Math.cos(angle) * width, y + height / 2 + Math.sin(angle) * height);
        }
        return sol;
    }

    private Shape crearArbolSimple(int x, int y, int width, int height) {
        Path2D arbol = new Path2D.Double();
        // Tronco (rectángulo)
        arbol.append(new Rectangle2D.Double(x + width / 3, y + height / 2, width / 3, height / 2), false);
        // Copa (triángulo)
        arbol.append(new Polygon(new int[]{x, x + width / 2, x + width}, new int[]{y + height / 2, y, y + height / 2}, 3), false);
        return arbol;
    }

    private Shape crearOjoSimple(int x, int y, int width, int height) {
        Path2D ojo = new Path2D.Double();
        ojo.append(new Ellipse2D.Double(x, y, width, height), false);
        ojo.append(new Ellipse2D.Double(x + width / 4, y + height / 4, width / 2, height / 2), false);
        return ojo;
    }

    private Shape crearDiamante(int x, int y, int width, int height) {
        Polygon diamante = new Polygon();
        diamante.addPoint(x + width / 2, y);
        diamante.addPoint(x, y + height / 2);
        diamante.addPoint(x + width / 2, y + height);
        diamante.addPoint(x + width, y + height / 2);
        return diamante;
    }

    public void guardarDibujo() {
        BufferedImage imagen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = imagen.createGraphics();
        paint(g2);
        g2.dispose();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar dibujo");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                ImageIO.write(imagen, "png", fileToSave);
                JOptionPane.showMessageDialog(this, "Dibujo guardado correctamente!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el dibujo: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Draw Panel");
        CustomDrawPanel panel = new CustomDrawPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        // Botón para guardar el dibujo
        JButton guardarButton = new JButton("Guardar Dibujo");
        guardarButton.addActionListener(e -> panel.guardarDibujo());

        // Panel para el botón
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(guardarButton);

        // Agregar el panel de dibujo y el panel del botón al frame
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(panel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}