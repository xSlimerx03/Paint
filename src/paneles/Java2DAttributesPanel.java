package paneles;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Java2DAttributesPanel extends JPanel {
    // Atributos de texto
    private String text = "Java2D";
    private String fontFamily = "Arial";
    private int fontStyle = Font.PLAIN;
    private int fontSize = 24;
    private Color textColor = Color.BLACK;

    // Atributos de forma
    private int shapeType = 0; // 0=Rect, 1=Elipse, 2=RoundRect
    private Color fillColor = new Color(200, 200, 255);
    private Color strokeColor = Color.BLUE;
    private Stroke stroke = new BasicStroke(3);

    // Transformaciones
    private double rotation = 0;
    private double scale = 1.0;

    // Efectos
    private float alpha = 1.0f;
    private boolean antialiasing = true;
    private Shape clip = null;

    // Tamaños
    private int shapeSize = 200;
    private int arcSize = 30;

    public Java2DAttributesPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Aplicar antialiasing si está activado
        if (antialiasing) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        // Aplicar clip si existe
        if (clip != null) {
            g2.setClip(clip);
        }

        // Guardar transformación original
        AffineTransform originalTransform = g2.getTransform();

        // Mover al centro del panel
        g2.translate(getWidth()/2, getHeight()/2);

        // Aplicar escala y rotación
        g2.scale(scale, scale);
        g2.rotate(Math.toRadians(rotation));

        // Aplicar transparencia
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Dibujar la forma centrada
        int halfSize = shapeSize/2;
        Shape shape = createShape(-halfSize, -halfSize, shapeSize, shapeSize);

        // Relleno
        g2.setColor(fillColor);
        g2.fill(shape);

        // Borde
        g2.setColor(strokeColor);
        g2.setStroke(stroke);
        g2.draw(shape);

        // Dibujar texto centrado
        Font font = new Font(fontFamily, fontStyle, fontSize);
        g2.setFont(font);
        g2.setColor(textColor);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        g2.drawString(text, -textWidth/2, textHeight/4);

        // Restaurar transformación original
        g2.setTransform(originalTransform);

        // Dibujar área de clip si está activado
        if (clip != null) {
            g2.setClip(null); // Limpiar clip temporalmente
            g2.setColor(new Color(255, 0, 0, 100));
            g2.fill(clip);
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                    0, new float[]{5}, 0)); // Línea punteada
            g2.draw(clip);
            g2.setClip(clip); // Restaurar clip
        }
    }

    private Shape createShape(int x, int y, int width, int height) {
        switch (shapeType) {
            case 0: return new Rectangle2D.Double(x, y, width, height);
            case 1: return new Ellipse2D.Double(x, y, width, height);
            case 2: return new RoundRectangle2D.Double(x, y, width, height, arcSize, arcSize);
            default: return new Rectangle2D.Double(x, y, width, height);
        }
    }

    // Métodos para control de texto
    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
        repaint();
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
        repaint();
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        repaint();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        repaint();
    }

    // Métodos para control de forma
    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
        repaint();
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        repaint();
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
        repaint();
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        repaint();
    }

    // Métodos para transformaciones
    public void setRotation(double degrees) {
        this.rotation = degrees;
        repaint();
    }

    public void setScale(double scale) {
        this.scale = scale;
        repaint();
    }

    // Métodos para efectos
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    public void setAntialiasing(boolean enabled) {
        this.antialiasing = enabled;
        repaint();
    }

    public void applyClip() {
        int clipSize = shapeSize + 100;
        this.clip = new Ellipse2D.Double(
                getWidth()/2 - clipSize/2,
                getHeight()/2 - clipSize/2,
                clipSize,
                clipSize
        );
        repaint();
    }

    public void clearClip() {
        this.clip = null;
        repaint();
    }

    public Color getTextColor() {
        return textColor;
    }
}