package paneles.paint;

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

    private List<ShapeAtributos> figuras = new ArrayList<>();
    private List<ShapeAtributos> figurasSeleccionadas = new ArrayList<>();
    private List<ShapeAtributos> portapapeles = new ArrayList<>();
    private Point inicioArrastre;
    private Shape figuraActual;
    private int figuraSeleccionada = 1;

    // Modos de operación
    private boolean modoSeleccion = false;
    private boolean modoMover = false;
    private boolean modoRotar = false;
    private boolean seleccionMultiple = false;

    // Punto de referencia para rotación/movimiento
    private Point puntoReferencia;

    // Atributos actuales
    private AtributosDibujo atributosActuales = new AtributosDibujo();

    // Clase AtributosDibujo con los métodos isEsGrupo y setEsGrupo
    public class AtributosDibujo {
        private Color colorRelleno = Color.WHITE;
        private Color colorContorno = Color.BLACK;
        private boolean rellenoActivo = true;
        private boolean contornoActivo = true;
        private float anchoContorno = 1.0f;
        private float[] patronPunteado = null;
        private boolean strokePunteado = false;
        private BufferedImage imagenRelleno = null;
        private boolean rellenoImagenActivo = false;
        private boolean rellenoDegradado = false;
        private Color colorDegradadoInicio = Color.WHITE;
        private Color colorDegradadoFin = Color.BLUE;
        private int direccionDegradado = 0;
        private boolean esGrupo = false;

        public boolean isEsGrupo() {
            return esGrupo;
        }

        public void setEsGrupo(boolean esGrupo) {
            this.esGrupo = esGrupo;
        }

        public AtributosDibujo copiar() {
            AtributosDibujo copia = new AtributosDibujo();
            copia.colorRelleno = this.colorRelleno;
            copia.colorContorno = this.colorContorno;
            copia.rellenoActivo = this.rellenoActivo;
            copia.contornoActivo = this.contornoActivo;
            copia.anchoContorno = this.anchoContorno;
            copia.patronPunteado = this.patronPunteado != null ? this.patronPunteado.clone() : null;
            copia.strokePunteado = this.strokePunteado;
            copia.imagenRelleno = this.imagenRelleno;
            copia.rellenoImagenActivo = this.rellenoImagenActivo;
            copia.rellenoDegradado = this.rellenoDegradado;
            copia.colorDegradadoInicio = this.colorDegradadoInicio;
            copia.colorDegradadoFin = this.colorDegradadoFin;
            copia.direccionDegradado = this.direccionDegradado;
            copia.esGrupo = this.esGrupo;
            return copia;
        }

        public Stroke crearStroke() {
            if (strokePunteado && patronPunteado != null) {
                return new BasicStroke(anchoContorno, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, patronPunteado, 0.0f);
            } else {
                return new BasicStroke(anchoContorno);
            }
        }

        // Resto de getters y setters...
        public Color getColorRelleno() { return colorRelleno; }
        public void setColorRelleno(Color colorRelleno) { this.colorRelleno = colorRelleno; }
        public Color getColorContorno() { return colorContorno; }
        public void setColorContorno(Color colorContorno) { this.colorContorno = colorContorno; }
        public boolean isRellenoActivo() { return rellenoActivo; }
        public void setRellenoActivo(boolean rellenoActivo) { this.rellenoActivo = rellenoActivo; }
        public boolean isContornoActivo() { return contornoActivo; }
        public void setContornoActivo(boolean contornoActivo) { this.contornoActivo = contornoActivo; }
        public float getAnchoContorno() { return anchoContorno; }
        public void setAnchoContorno(float anchoContorno) { this.anchoContorno = anchoContorno; }
        public float[] getPatronPunteado() { return patronPunteado; }
        public void setPatronPunteado(float[] patronPunteado) { this.patronPunteado = patronPunteado; }
        public boolean isStrokePunteado() { return strokePunteado; }
        public void setStrokePunteado(boolean strokePunteado) { this.strokePunteado = strokePunteado; }
        public BufferedImage getImagenRelleno() { return imagenRelleno; }
        public void setImagenRelleno(BufferedImage imagenRelleno) { this.imagenRelleno = imagenRelleno; }
        public boolean isRellenoImagenActivo() { return rellenoImagenActivo; }
        public void setRellenoImagenActivo(boolean rellenoImagenActivo) { this.rellenoImagenActivo = rellenoImagenActivo; }
        public boolean isRellenoDegradado() { return rellenoDegradado; }
        public void setRellenoDegradado(boolean rellenoDegradado) { this.rellenoDegradado = rellenoDegradado; }
        public Color getColorDegradadoInicio() { return colorDegradadoInicio; }
        public void setColorDegradadoInicio(Color colorDegradadoInicio) { this.colorDegradadoInicio = colorDegradadoInicio; }
        public Color getColorDegradadoFin() { return colorDegradadoFin; }
        public void setColorDegradadoFin(Color colorDegradadoFin) { this.colorDegradadoFin = colorDegradadoFin; }
        public int getDireccionDegradado() { return direccionDegradado; }
        public void setDireccionDegradado(int direccionDegradado) { this.direccionDegradado = direccionDegradado; }
    }

    private class ShapeAtributos {
        Shape forma;
        AtributosDibujo atributos;
        Stroke stroke;
        TexturePaint textura;

        public ShapeAtributos(Shape forma, AtributosDibujo atributos) {
            this.forma = forma;
            this.atributos = atributos.copiar();
            this.stroke = atributos.crearStroke();
            if (atributos.isRellenoImagenActivo() && atributos.getImagenRelleno() != null) {
                this.textura = new TexturePaint(
                        atributos.getImagenRelleno(),
                        new Rectangle2D.Double(0, 0,
                                atributos.getImagenRelleno().getWidth(),
                                atributos.getImagenRelleno().getHeight())
                );
            } else {
                this.textura = null;
            }
        }
    }

    // Métodos de operación (selección, movimiento, rotación, etc.)
    public void setModoSeleccion(boolean activo) {
        this.modoSeleccion = activo;
        if (activo) {
            this.modoMover = false;
            this.modoRotar = false;
        }
        repaint();
    }

    public void setModoMover(boolean activo) {
        this.modoMover = activo;
        if (activo) {
            this.modoSeleccion = false;
            this.modoRotar = false;
        }
        repaint();
    }

    public void setModoRotar(boolean activo) {
        this.modoRotar = activo;
        if (activo) {
            this.modoSeleccion = false;
            this.modoMover = false;
        }
        repaint();
    }

    public void setSeleccionMultiple(boolean multiple) {
        this.seleccionMultiple = multiple;
        if (!multiple && figurasSeleccionadas.size() > 1) {
            ShapeAtributos ultima = figurasSeleccionadas.get(figurasSeleccionadas.size() - 1);
            figurasSeleccionadas.clear();
            figurasSeleccionadas.add(ultima);
        }
        repaint();
    }

    public void fusionarFigurasSeleccionadas() {
        if (figurasSeleccionadas.size() < 2) return;

        Area areaFusionada = new Area();
        AtributosDibujo atributosFusion = figurasSeleccionadas.get(0).atributos.copiar();

        for (ShapeAtributos sa : figurasSeleccionadas) {
            areaFusionada.add(new Area(sa.forma));
            figuras.remove(sa);
        }

        ShapeAtributos fusion = new ShapeAtributos(areaFusionada, atributosFusion);
        figuras.add(fusion);
        figurasSeleccionadas.clear();
        figurasSeleccionadas.add(fusion);
        repaint();
    }

    public void copiarFigurasSeleccionadas() {
        portapapeles.clear();
        for (ShapeAtributos sa : figurasSeleccionadas) {
            portapapeles.add(new ShapeAtributos(cloneShape(sa.forma), sa.atributos.copiar()));
        }
    }

    public void pegarFigurasCopiadas() {
        if (portapapeles.isEmpty()) return;

        figurasSeleccionadas.clear();
        for (ShapeAtributos sa : portapapeles) {
            AffineTransform at = AffineTransform.getTranslateInstance(10, 10);
            Shape formaDesplazada = at.createTransformedShape(sa.forma);

            ShapeAtributos nueva = new ShapeAtributos(formaDesplazada, sa.atributos.copiar());
            figuras.add(nueva);
            figurasSeleccionadas.add(nueva);
        }
        repaint();
    }

    public void traerAlFrente() {
        for (ShapeAtributos sa : figurasSeleccionadas) {
            figuras.remove(sa);
            figuras.add(sa);
        }
        repaint();
    }

    public void enviarAtras() {
        for (ShapeAtributos sa : figurasSeleccionadas) {
            figuras.remove(sa);
            figuras.add(0, sa);
        }
        repaint();
    }

    public void agruparFiguras() {
        if (figurasSeleccionadas.size() < 2) return;

        Rectangle2D bounds = null;
        for (ShapeAtributos sa : figurasSeleccionadas) {
            if (bounds == null) {
                bounds = sa.forma.getBounds2D();
            } else {
                bounds = bounds.createUnion(sa.forma.getBounds2D());
            }
        }

        Shape grupo = new Rectangle2D.Double(
                bounds.getX(), bounds.getY(),
                bounds.getWidth(), bounds.getHeight()
        );

        ShapeAtributos grupoAtributos = new ShapeAtributos(
                grupo,
                figurasSeleccionadas.get(0).atributos.copiar()
        );

        grupoAtributos.atributos.setEsGrupo(true);

        figuras.removeAll(figurasSeleccionadas);
        figuras.add(grupoAtributos);

        figurasSeleccionadas.clear();
        figurasSeleccionadas.add(grupoAtributos);

        repaint();
    }

    public void desagruparFiguras() {
        if (figurasSeleccionadas.isEmpty()) return;

        List<ShapeAtributos> paraDesagrupar = new ArrayList<>();
        List<ShapeAtributos> nuevasFiguras = new ArrayList<>();

        for (ShapeAtributos sa : figurasSeleccionadas) {
            if (sa.atributos.isEsGrupo()) {
                paraDesagrupar.add(sa);
            }
        }

        if (paraDesagrupar.isEmpty()) return;

        for (ShapeAtributos grupo : paraDesagrupar) {
            Rectangle2D bounds = grupo.forma.getBounds2D();

            ShapeAtributos nueva1 = new ShapeAtributos(
                    new Ellipse2D.Double(
                            bounds.getX(), bounds.getY(),
                            bounds.getWidth()/2, bounds.getHeight()/2
                    ),
                    grupo.atributos.copiar()
            );

            ShapeAtributos nueva2 = new ShapeAtributos(
                    new Rectangle2D.Double(
                            bounds.getX() + bounds.getWidth()/2,
                            bounds.getY() + bounds.getHeight()/2,
                            bounds.getWidth()/2,
                            bounds.getHeight()/2
                    ),
                    grupo.atributos.copiar()
            );

            nuevasFiguras.add(nueva1);
            nuevasFiguras.add(nueva2);

            figuras.remove(grupo);
        }

        figuras.addAll(nuevasFiguras);
        figurasSeleccionadas.clear();
        figurasSeleccionadas.addAll(nuevasFiguras);
        repaint();
    }

    public void eliminarFigurasSeleccionadas() {
        if (figurasSeleccionadas.isEmpty()) return;

        figuras.removeAll(figurasSeleccionadas);
        figurasSeleccionadas.clear();
        repaint();
    }

    private Shape cloneShape(Shape shape) {
        if (shape instanceof Line2D) {
            Line2D line = (Line2D) shape;
            return new Line2D.Double(line.getP1(), line.getP2());
        } else if (shape instanceof Rectangle2D) {
            Rectangle2D rect = (Rectangle2D) shape;
            return new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        } else if (shape instanceof Ellipse2D) {
            Ellipse2D ellipse = (Ellipse2D) shape;
            return new Ellipse2D.Double(ellipse.getX(), ellipse.getY(), ellipse.getWidth(), ellipse.getHeight());
        } else if (shape instanceof Path2D) {
            Path2D path = (Path2D) shape;
            return (Path2D) path.clone();
        }
        return null;
    }

    // Resto de los métodos (paintComponent, crearFigura, etc.)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (ShapeAtributos sa : figuras) {
            if (figurasSeleccionadas.contains(sa)) {
                g2.setColor(new Color(100, 100, 255, 100));
                g2.fill(sa.forma);

                Rectangle2D bounds = sa.forma.getBounds2D();
                g2.setColor(Color.BLUE);
                g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5, 5}, 0));
                g2.draw(bounds);
            }

            if (sa.atributos.isRellenoActivo()) {
                if (sa.textura != null) {
                    g2.setPaint(sa.textura);
                } else if (sa.atributos.isRellenoDegradado()) {
                    Paint degradado = crearDegradado(sa.forma.getBounds2D(), sa.atributos);
                    g2.setPaint(degradado);
                } else {
                    g2.setColor(sa.atributos.getColorRelleno());
                }
                g2.fill(sa.forma);
            }

            if (sa.atributos.isContornoActivo()) {
                g2.setColor(sa.atributos.getColorContorno());
                g2.setStroke(sa.stroke);
                g2.draw(sa.forma);
            }
        }

        if (figuraActual != null) {
            if (atributosActuales.isRellenoActivo()) {
                if (atributosActuales.isRellenoImagenActivo() && atributosActuales.getImagenRelleno() != null) {
                    TexturePaint textura = new TexturePaint(
                            atributosActuales.getImagenRelleno(),
                            new Rectangle2D.Double(0, 0,
                                    atributosActuales.getImagenRelleno().getWidth(),
                                    atributosActuales.getImagenRelleno().getHeight())
                    );
                    g2.setPaint(textura);
                } else if (atributosActuales.isRellenoDegradado()) {
                    Paint degradado = crearDegradado(figuraActual.getBounds2D(), atributosActuales);
                    g2.setPaint(degradado);
                } else {
                    g2.setColor(atributosActuales.getColorRelleno());
                }
                g2.fill(figuraActual);
            }

            if (atributosActuales.isContornoActivo()) {
                g2.setColor(atributosActuales.getColorContorno());
                g2.setStroke(atributosActuales.crearStroke());
                g2.draw(figuraActual);
            }
        }
    }

    private Paint crearDegradado(Rectangle2D bounds, AtributosDibujo atributos) {
        Color color1 = atributos.getColorDegradadoInicio();
        Color color2 = atributos.getColorDegradadoFin();

        switch (atributos.getDireccionDegradado()) {
            case 0: // Horizontal
                return new GradientPaint(
                        (float)bounds.getX(), (float)bounds.getCenterY(),
                        color1,
                        (float)bounds.getMaxX(), (float)bounds.getCenterY(),
                        color2
                );
            case 1: // Vertical
                return new GradientPaint(
                        (float)bounds.getCenterX(), (float)bounds.getY(),
                        color1,
                        (float)bounds.getCenterX(), (float)bounds.getMaxY(),
                        color2
                );
            case 2: // Diagonal
                return new GradientPaint(
                        (float)bounds.getX(), (float)bounds.getY(),
                        color1,
                        (float)bounds.getMaxX(), (float)bounds.getMaxY(),
                        color2
                );
            case 3: // Circular
                return new RadialGradientPaint(
                        new Point2D.Double(bounds.getCenterX(), bounds.getCenterY()),
                        (float)Math.max(bounds.getWidth(), bounds.getHeight())/2,
                        new float[]{0.0f, 1.0f},
                        new Color[]{color1, color2}
                );
            default: // Radial
                return new RadialGradientPaint(
                        new Point2D.Double(bounds.getCenterX(), bounds.getCenterY()),
                        (float)Math.max(bounds.getWidth(), bounds.getHeight()),
                        new Point2D.Double(bounds.getCenterX() - bounds.getWidth()/4, bounds.getCenterY() - bounds.getHeight()/4),
                        new float[]{0.0f, 0.8f},
                        new Color[]{color1, color2},
                        MultipleGradientPaint.CycleMethod.NO_CYCLE
                );
        }
    }

    public CustomDrawPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                inicioArrastre = e.getPoint();
                puntoReferencia = e.getPoint();

                if (modoSeleccion) {
                    boolean encontrada = false;
                    ShapeAtributos figuraBajoCursor = null;

                    for (int i = figuras.size() - 1; i >= 0; i--) {
                        ShapeAtributos sa = figuras.get(i);
                        if (sa.forma.contains(e.getPoint())) {
                            figuraBajoCursor = sa;
                            encontrada = true;
                            break;
                        }
                    }

                    if (encontrada) {
                        if (!seleccionMultiple || !e.isShiftDown()) {
                            figurasSeleccionadas.clear();
                        }

                        if (!figurasSeleccionadas.contains(figuraBajoCursor)) {
                            figurasSeleccionadas.add(figuraBajoCursor);
                        } else {
                            figurasSeleccionadas.remove(figuraBajoCursor);
                        }
                    } else if (!e.isShiftDown()) {
                        figurasSeleccionadas.clear();
                    }
                } else {
                    figuraActual = crearFigura(e.getX(), e.getY(), 1, 1);
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (figuraActual != null && !modoSeleccion && !modoMover && !modoRotar) {
                    figuras.add(new ShapeAtributos(figuraActual, atributosActuales));
                    figuraActual = null;
                    repaint();
                }
                puntoReferencia = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (modoMover && !figurasSeleccionadas.isEmpty()) {
                    int dx = e.getX() - puntoReferencia.x;
                    int dy = e.getY() - puntoReferencia.y;

                    for (ShapeAtributos sa : figurasSeleccionadas) {
                        AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
                        sa.forma = at.createTransformedShape(sa.forma);
                    }

                    puntoReferencia = e.getPoint();
                    repaint();
                } else if (modoRotar && !figurasSeleccionadas.isEmpty()) {
                    Rectangle2D bounds = null;
                    for (ShapeAtributos sa : figurasSeleccionadas) {
                        if (bounds == null) {
                            bounds = sa.forma.getBounds2D();
                        } else {
                            bounds = bounds.createUnion(sa.forma.getBounds2D());
                        }
                    }

                    double centerX = bounds.getCenterX();
                    double centerY = bounds.getCenterY();

                    double angulo = Math.atan2(e.getY() - centerY, e.getX() - centerX) -
                            Math.atan2(puntoReferencia.y - centerY, puntoReferencia.x - centerX);

                    for (ShapeAtributos sa : figurasSeleccionadas) {
                        AffineTransform at = AffineTransform.getRotateInstance(angulo, centerX, centerY);
                        sa.forma = at.createTransformedShape(sa.forma);
                    }

                    puntoReferencia = e.getPoint();
                    repaint();
                } else if (figuraActual != null) {
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

    public void borrarUltimaFigura() {
        if (!figuras.isEmpty()) {
            figuras.remove(figuras.size() - 1);
            repaint();
        }
    }

    public void borrarTodo() {
        figuras.clear();
        figurasSeleccionadas.clear();
        repaint();
    }

    public void setColorRelleno(Color color) {
        atributosActuales.setColorRelleno(color);
    }

    public void setColorContorno(Color color) {
        atributosActuales.setColorContorno(color);
    }

    public void setRelleno(boolean activo) {
        atributosActuales.setRellenoActivo(activo);
    }

    public void setContorno(boolean activo) {
        atributosActuales.setContornoActivo(activo);
    }

    public void setPatronPunteado(float[] patron) {
        atributosActuales.setPatronPunteado(patron);
        atributosActuales.setStrokePunteado(true);
        repaint();
    }

    public void setAnchoContorno(float ancho) {
        atributosActuales.setAnchoContorno(ancho);
        repaint();
    }

    public AtributosDibujo getAtributosActuales() {
        return atributosActuales;
    }

    public void guardarComoImagen(File archivo) throws IOException {
        BufferedImage imagen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagen.createGraphics();
        paint(g2d);
        g2d.dispose();

        String formato = archivo.getName().toLowerCase().endsWith(".png") ? "png" : "jpg";
        ImageIO.write(imagen, formato, archivo);
    }

    public void cargarImagenRelleno(File archivo) {
        try {
            BufferedImage imagen = ImageIO.read(archivo);
            atributosActuales.setImagenRelleno(imagen);
            atributosActuales.setRellenoImagenActivo(true);
            repaint();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Shape crearFigura(int x, int y, int width, int height) {
        int xAjustado = x;
        int yAjustado = y;
        int widthAjustado = width;
        int heightAjustado = height;

        if (width < 0) {
            xAjustado = x + width;
            widthAjustado = -width;
        }
        if (height < 0) {
            yAjustado = y + height;
            heightAjustado = -height;
        }

        switch (figuraSeleccionada) {
            case 1: return new Ellipse2D.Double(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 2: return new Rectangle2D.Double(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 3: return new Ellipse2D.Double(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 4: return crearLineaDiscontinua(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 5: return new Arc2D.Double(xAjustado, yAjustado, widthAjustado, heightAjustado, 45, 270, Arc2D.PIE);
            case 6: return new Polygon(
                    new int[]{xAjustado, xAjustado + widthAjustado / 2, xAjustado + widthAjustado},
                    new int[]{yAjustado + heightAjustado, yAjustado, yAjustado + heightAjustado}, 3);
            case 7: return new Polygon(
                    new int[]{xAjustado + widthAjustado / 2, xAjustado, xAjustado + widthAjustado / 4,
                            xAjustado + widthAjustado * 3 / 4, xAjustado + widthAjustado},
                    new int[]{yAjustado, yAjustado + heightAjustado / 2, yAjustado + heightAjustado,
                            yAjustado + heightAjustado, yAjustado + heightAjustado / 2}, 5);
            case 8: return new QuadCurve2D.Double(
                    xAjustado, yAjustado + heightAjustado,
                    xAjustado + widthAjustado / 2, yAjustado,
                    xAjustado + widthAjustado, yAjustado + heightAjustado);
            case 9: return new CubicCurve2D.Double(
                    xAjustado, yAjustado + heightAjustado,
                    xAjustado + widthAjustado / 4, yAjustado,
                    xAjustado + widthAjustado * 3 / 4, yAjustado,
                    xAjustado + widthAjustado, yAjustado + heightAjustado);
            case 10: return new Ellipse2D.Double(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 11: return crearCorazon(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 12: return crearCuboSimple(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 13: return crearDonRamon(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 14: return crearCaritaFeliz(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 15: return crearCaritaTriste(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 16: return crearCorazonRoto(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 17: return crearSol(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 18: return crearArbolSimple(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 19: return crearOjoSimple(xAjustado, yAjustado, widthAjustado, heightAjustado);
            case 20: return crearDiamante(xAjustado, yAjustado, widthAjustado, heightAjustado);
            default: return null;
        }
    }

    // Métodos para crear figuras específicas
    private Shape crearCorazon(int x, int y, int width, int height) {
        Path2D corazon = new Path2D.Double();
        corazon.moveTo(x + width / 2, y + height / 4);
        corazon.curveTo(x + width, y - height / 4, x + width, y + height / 2, x + width / 2, y + height);
        corazon.curveTo(x, y + height / 2, x, y - height / 4, x + width / 2, y + height / 4);
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

    public static Shape crearDonRamon(int x, int y, int width, int height) {
        Path2D detective = new Path2D.Double();
        detective.moveTo(x + width * 0.2, y + height * 0.2);
        detective.lineTo(x + width * 0.8, y + height * 0.2);
        detective.lineTo(x + width * 0.7, y);
        detective.lineTo(x + width * 0.3, y);
        detective.closePath();
        detective.moveTo(x + width * 0.25, y + height * 0.2);
        detective.lineTo(x + width * 0.75, y + height * 0.2);
        detective.lineTo(x + width * 0.8, y + height * 0.65);
        detective.lineTo(x + width * 0.6, y + height * 0.8);
        detective.lineTo(x + width * 0.4, y + height * 0.8);
        detective.lineTo(x + width * 0.2, y + height * 0.65);
        detective.closePath();
        detective.moveTo(x + width * 0.4, y + height * 0.35);
        detective.lineTo(x + width * 0.42, y + height * 0.35);
        detective.lineTo(x + width * 0.42, y + height * 0.37);
        detective.lineTo(x + width * 0.4, y + height * 0.37);
        detective.closePath();
        detective.moveTo(x + width * 0.58, y + height * 0.35);
        detective.lineTo(x + width * 0.6, y + height * 0.35);
        detective.lineTo(x + width * 0.6, y + height * 0.37);
        detective.lineTo(x + width * 0.58, y + height * 0.37);
        detective.closePath();
        detective.moveTo(x + width * 0.2, y + height * 0.55);
        detective.lineTo(x + width * 0.8, y + height * 0.55);
        detective.lineTo(x + width * 0.5, y + height * 0.45);
        detective.closePath();
        return detective;
    }

    private Shape crearLineaDiscontinua(int x, int y, int width, int height) {
        Path2D lineaDiscontinua = new Path2D.Double();
        lineaDiscontinua.moveTo(x, y + height / 2);
        lineaDiscontinua.lineTo(x + width, y + height / 2);
        return lineaDiscontinua;
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
        arbol.append(new Rectangle2D.Double(x + width / 3, y + height / 2, width / 3, height / 2), false);
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

    void setColor(Color RED) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}