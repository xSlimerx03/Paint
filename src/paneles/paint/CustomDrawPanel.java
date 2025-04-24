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
    private List<Point> puntosPincel = new ArrayList<>();
    private boolean modoPincel = false;
    private boolean modoGoma = false;
    private boolean modoCubeta = false;
    private float tamanoPincel = 5.0f;
    private float tamanoGoma = 10.0f;
    private Color colorCubeta = Color.WHITE;
    private boolean rellenoCubeta = true;
    private float transparenciaActual = 1.0f;

    // Modos de operación
    private boolean modoSeleccion = false;
    private boolean modoMover = false;
    private boolean modoRotar = false;
    private boolean modoEscalar = false;
    private boolean seleccionMultiple = false;

    // Punto de referencia para rotación/movimiento/escalado
    private Point puntoReferencia;

    // Atributos actuales
    private AtributosDibujo atributosActuales = new AtributosDibujo();

    // Texturas predefinidas
    private BufferedImage[] texturas = new BufferedImage[4];

    // Iconos para las herramientas
    private ImageIcon iconSelect;
    private ImageIcon iconMove;
    private ImageIcon iconRotate;
    private ImageIcon iconScale;
    private ImageIcon iconPencil;
    private ImageIcon iconEraser;
    private ImageIcon iconBucket;
    private ImageIcon iconMerge;
    private ImageIcon iconDelete;
    private ImageIcon iconCopy;
    private ImageIcon iconPaste;
    private ImageIcon iconBringToFront;
    private ImageIcon iconSendToBack;
    private ImageIcon iconGroup;
    private ImageIcon iconUngroup;

    public CustomDrawPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Cargar iconos
        cargarIconos();

        // Configurar atajos de teclado
        configurarAtajosTeclado();

        // Crear texturas predefinidas
        crearTexturas();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                inicioArrastre = e.getPoint();
                puntoReferencia = e.getPoint();

                if (modoPincel) {
                    puntosPincel.clear();
                    puntosPincel.add(e.getPoint());
                } else if (modoGoma) {
                    eliminarFiguraEnPunto(e.getPoint());
                } else if (modoCubeta) {
                    aplicarCubeta(e.getPoint());
                } else if (modoSeleccion) {
                    manejarSeleccion(e);
                } else {
                    figuraActual = crearFigura(e.getX(), e.getY(), 1, 1);
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (figuraActual != null && !modoSeleccion && !modoMover && !modoRotar && !modoEscalar && !modoPincel && !modoGoma && !modoCubeta) {
                    figuras.add(new ShapeAtributos(figuraActual, atributosActuales));
                    figuraActual = null;
                    repaint();
                } else if (modoPincel && puntosPincel.size() > 1) {
                    Path2D path = new Path2D.Double();
                    path.moveTo(puntosPincel.get(0).x, puntosPincel.get(0).y);
                    for (int i = 1; i < puntosPincel.size(); i++) {
                        path.lineTo(puntosPincel.get(i).x, puntosPincel.get(i).y);
                    }
                    figuras.add(new ShapeAtributos(path, atributosActuales));
                    puntosPincel.clear();
                    repaint();
                }
                puntoReferencia = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (modoMover && !figurasSeleccionadas.isEmpty()) {
                    moverFiguras(e);
                } else if (modoRotar && !figurasSeleccionadas.isEmpty()) {
                    rotarFiguras(e);
                } else if (modoEscalar && !figurasSeleccionadas.isEmpty()) {
                    escalarFiguras(e);
                } else if (modoPincel) {
                    puntosPincel.add(e.getPoint());
                    repaint();
                } else if (modoGoma) {
                    eliminarFiguraEnPunto(e.getPoint());
                } else if (figuraActual != null) {
                    int width = e.getX() - inicioArrastre.x;
                    int height = e.getY() - inicioArrastre.y;
                    figuraActual = crearFigura(inicioArrastre.x, inicioArrastre.y, width, height);
                    repaint();
                }
            }
        });
    }

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
        private int texturaIndex = -1;
        private float transparencia = 1.0f;

        public boolean isEsGrupo() {
            return esGrupo;
        }

        public void setEsGrupo(boolean esGrupo) {
            this.esGrupo = esGrupo;
        }

        public float getTransparencia() {
            return transparencia;
        }

        public void setTransparencia(float transparencia) {
            this.transparencia = transparencia;
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
            copia.texturaIndex = this.texturaIndex;
            copia.transparencia = this.transparencia;
            return copia;
        }

        public Stroke crearStroke() {
            if (strokePunteado && patronPunteado != null) {
                return new BasicStroke(anchoContorno, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, patronPunteado, 0.0f);
            } else {
                return new BasicStroke(anchoContorno);
            }
        }

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
        public int getTexturaIndex() { return texturaIndex; }
        public void setTexturaIndex(int texturaIndex) { this.texturaIndex = texturaIndex; }
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

            if (atributos.getTexturaIndex() >= 0 && atributos.getTexturaIndex() < texturas.length && texturas[atributos.getTexturaIndex()] != null) {
                this.textura = new TexturePaint(
                        texturas[atributos.getTexturaIndex()],
                        new Rectangle2D.Double(0, 0,
                                texturas[atributos.getTexturaIndex()].getWidth(),
                                texturas[atributos.getTexturaIndex()].getHeight())
                );
            } else if (atributos.isRellenoImagenActivo() && atributos.getImagenRelleno() != null) {
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

    private void cargarIconos() {
        try {
            iconSelect = new ImageIcon(ImageIO.read(getClass().getResource("/icons/select.png")));
            iconMove = new ImageIcon(ImageIO.read(getClass().getResource("/icons/move.png")));
            iconRotate = new ImageIcon(ImageIO.read(getClass().getResource("/icons/rotate.png")));
            iconScale = new ImageIcon(ImageIO.read(getClass().getResource("/icons/scale.png")));
            iconPencil = new ImageIcon(ImageIO.read(getClass().getResource("/icons/pencil.png")));
            iconEraser = new ImageIcon(ImageIO.read(getClass().getResource("/icons/eraser.png")));
            iconBucket = new ImageIcon(ImageIO.read(getClass().getResource("/icons/bucket.png")));
            iconMerge = new ImageIcon(ImageIO.read(getClass().getResource("/icons/merge.png")));
            iconDelete = new ImageIcon(ImageIO.read(getClass().getResource("/icons/delete.png")));
            iconCopy = new ImageIcon(ImageIO.read(getClass().getResource("/icons/copy.png")));
            iconPaste = new ImageIcon(ImageIO.read(getClass().getResource("/icons/paste.png")));
            iconBringToFront = new ImageIcon(ImageIO.read(getClass().getResource("/icons/bring_to_front.png")));
            iconSendToBack = new ImageIcon(ImageIO.read(getClass().getResource("/icons/send_to_back.png")));
            iconGroup = new ImageIcon(ImageIO.read(getClass().getResource("/icons/group.png")));
            iconUngroup = new ImageIcon(ImageIO.read(getClass().getResource("/icons/ungroup.png")));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los iconos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            // Usar iconos por defecto si hay error
            iconSelect = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconMove = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconRotate = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconScale = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconPencil = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconEraser = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconBucket = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconMerge = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconDelete = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconCopy = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconPaste = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconBringToFront = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconSendToBack = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconGroup = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
            iconUngroup = (ImageIcon)UIManager.getIcon("FileView.directoryIcon");
        }
    }

    private void configurarAtajosTeclado() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Atajos para figuras (CTRL+1 a CTRL+9)
        for (int i = 1; i <= 9; i++) {
            final int figura = i;
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, InputEvent.CTRL_DOWN_MASK), "figura_" + i);
            actionMap.put("figura_" + i, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setFigura(figura);
                }
            });
        }

        // Atajos para herramientas
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "seleccion");
        actionMap.put("seleccion", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setModoSeleccion(true);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "mover");
        actionMap.put("mover", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setModoMover(true);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rotar");
        actionMap.put("rotar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setModoRotar(true);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "escalar");
        actionMap.put("escalar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setModoEscalar(true);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pincel");
        actionMap.put("pincel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setModoPincel(true);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), "goma");
        actionMap.put("goma", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setModoGoma(true);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "cubeta");
        actionMap.put("cubeta", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setModoCubeta(true);
            }
        });

        // Atajos para operaciones
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "borrar");
        actionMap.put("borrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarFigurasSeleccionadas();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copiar");
        actionMap.put("copiar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copiarFigurasSeleccionadas();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "pegar");
        actionMap.put("pegar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pegarFigurasCopiadas();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "deshacer");
        actionMap.put("deshacer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarUltimaFigura();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "frente");
        actionMap.put("frente", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traerAlFrente();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK), "atras");
        actionMap.put("atras", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarAtras();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK), "agrupar");
        actionMap.put("agrupar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agruparFiguras();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), "desagrupar");
        actionMap.put("desagrupar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desagruparFiguras();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "fusionar");
        actionMap.put("fusionar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fusionarFigurasSeleccionadas();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "guardar");
        actionMap.put("guardar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDibujo();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar todas las figuras
        for (ShapeAtributos sa : figuras) {
            dibujarFigura(g2, sa);

            // Resaltar figuras seleccionadas
            if (figurasSeleccionadas.contains(sa)) {
                g2.setColor(new Color(100, 100, 255, 100));
                g2.fill(sa.forma);

                Rectangle2D bounds = sa.forma.getBounds2D();
                g2.setColor(Color.BLUE);
                g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5, 5}, 0));
                g2.draw(bounds);
            }
        }

        // Dibujar figura actual en proceso de creación
        if (figuraActual != null) {
            dibujarFigura(g2, new ShapeAtributos(figuraActual, atributosActuales));
        }

        // Dibujar trazo del pincel
        if (modoPincel && puntosPincel.size() > 1) {
            g2.setColor(atributosActuales.getColorContorno());
            g2.setStroke(new BasicStroke(tamanoPincel, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            Path2D path = new Path2D.Double();
            path.moveTo(puntosPincel.get(0).x, puntosPincel.get(0).y);
            for (int i = 1; i < puntosPincel.size(); i++) {
                path.lineTo(puntosPincel.get(i).x, puntosPincel.get(i).y);
            }
            g2.draw(path);
        }
    }

    private void dibujarFigura(Graphics2D g2, ShapeAtributos sa) {
        // Configurar transparencia
        Composite originalComposite = g2.getComposite();
        if (sa.atributos.getTransparencia() < 1.0f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, sa.atributos.getTransparencia()));
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

        // Restaurar composición original
        g2.setComposite(originalComposite);
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

    private void crearTexturas() {
        // Textura 1: Madera
        texturas[0] = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = texturas[0].createGraphics();
        g2d.setColor(new Color(139, 69, 19)); // Marrón
        g2d.fillRect(0, 0, 50, 50);
        g2d.setColor(new Color(160, 82, 45)); // Marrón claro
        for (int i = 0; i < 50; i += 5) {
            g2d.drawLine(i, 0, i, 50);
        }
        g2d.dispose();

        // Textura 2: Líneas diagonales
        texturas[1] = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        g2d = texturas[1].createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 50, 50);
        g2d.setColor(Color.BLACK);
        for (int i = -50; i < 50; i += 5) {
            g2d.drawLine(i, 0, i + 50, 50);
        }
        g2d.dispose();

        // Textura 3: Puntos
        texturas[2] = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        g2d = texturas[2].createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 50, 50);
        g2d.setColor(Color.BLUE);
        for (int y = 0; y < 50; y += 10) {
            for (int x = 0; x < 50; x += 10) {
                g2d.fillOval(x, y, 5, 5);
            }
        }
        g2d.dispose();

        // Textura 4: Cuadrícula
        texturas[3] = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        g2d = texturas[3].createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 50, 50);
        g2d.setColor(Color.GRAY);
        for (int i = 0; i <= 50; i += 10) {
            g2d.drawLine(i, 0, i, 50);
            g2d.drawLine(0, i, 50, i);
        }
        g2d.dispose();
    }

    private void manejarSeleccion(MouseEvent e) {
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
    }

    private void moverFiguras(MouseEvent e) {
        int dx = e.getX() - puntoReferencia.x;
        int dy = e.getY() - puntoReferencia.y;

        for (ShapeAtributos sa : figurasSeleccionadas) {
            AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
            sa.forma = at.createTransformedShape(sa.forma);
        }

        puntoReferencia = e.getPoint();
        repaint();
    }

    private void rotarFiguras(MouseEvent e) {
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
    }

    private void escalarFiguras(MouseEvent e) {
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

        double distanciaOriginal = Math.sqrt(
                Math.pow(puntoReferencia.x - centerX, 2) +
                        Math.pow(puntoReferencia.y - centerY, 2));
        double distanciaActual = Math.sqrt(
                Math.pow(e.getX() - centerX, 2) +
                        Math.pow(e.getY() - centerY, 2));

        double escala = distanciaActual / distanciaOriginal;

        for (ShapeAtributos sa : figurasSeleccionadas) {
            AffineTransform at = AffineTransform.getTranslateInstance(centerX, centerY);
            at.scale(escala, escala);
            at.translate(-centerX, -centerY);
            sa.forma = at.createTransformedShape(sa.forma);
        }

        puntoReferencia = e.getPoint();
        repaint();
    }

    private void eliminarFiguraEnPunto(Point p) {
        for (int i = figuras.size() - 1; i >= 0; i--) {
            ShapeAtributos sa = figuras.get(i);
            if (sa.forma.contains(p)) {
                figuras.remove(i);
                figurasSeleccionadas.remove(sa);
                repaint();
                break;
            }
        }
    }

    private void aplicarCubeta(Point p) {
        for (int i = figuras.size() - 1; i >= 0; i--) {
            ShapeAtributos sa = figuras.get(i);
            if (sa.forma.contains(p)) {
                if (rellenoCubeta) {
                    sa.atributos.setColorRelleno(colorCubeta);
                    sa.textura = null;
                    sa.atributos.setRellenoImagenActivo(false);
                    sa.atributos.setTexturaIndex(-1);
                }
                sa.atributos.setColorContorno(colorCubeta);
                repaint();
                break;
            }
        }
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
        for (ShapeAtributos sa : figurasSeleccionadas) {
            sa.atributos.setColorRelleno(color);
        }
        repaint();
    }

    public void setColorContorno(Color color) {
        atributosActuales.setColorContorno(color);
        for (ShapeAtributos sa : figurasSeleccionadas) {
            sa.atributos.setColorContorno(color);
        }
        repaint();
    }

    public void setRelleno(boolean activo) {
        atributosActuales.setRellenoActivo(activo);
        for (ShapeAtributos sa : figurasSeleccionadas) {
            sa.atributos.setRellenoActivo(activo);
        }
        repaint();
    }

    public void setContorno(boolean activo) {
        atributosActuales.setContornoActivo(activo);
        for (ShapeAtributos sa : figurasSeleccionadas) {
            sa.atributos.setContornoActivo(activo);
        }
        repaint();
    }

    public void setPatronPunteado(float[] patron) {
        atributosActuales.setPatronPunteado(patron);
        atributosActuales.setStrokePunteado(true);
        for (ShapeAtributos sa : figurasSeleccionadas) {
            sa.atributos.setPatronPunteado(patron);
            sa.atributos.setStrokePunteado(true);
            sa.stroke = sa.atributos.crearStroke();
        }
        repaint();
    }

    public void setAnchoContorno(float ancho) {
        atributosActuales.setAnchoContorno(ancho);
        for (ShapeAtributos sa : figurasSeleccionadas) {
            sa.atributos.setAnchoContorno(ancho);
            sa.stroke = sa.atributos.crearStroke();
        }
        repaint();
    }

    public void setTransparencia(float transparencia) {
        this.transparenciaActual = transparencia;
        atributosActuales.setTransparencia(transparencia);
        for (ShapeAtributos sa : figurasSeleccionadas) {
            sa.atributos.setTransparencia(transparencia);
        }
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
            for (ShapeAtributos sa : figurasSeleccionadas) {
                sa.atributos.setImagenRelleno(imagen);
                sa.atributos.setRellenoImagenActivo(true);
                if (imagen != null) {
                    sa.textura = new TexturePaint(
                            imagen,
                            new Rectangle2D.Double(0, 0,
                                    imagen.getWidth(),
                                    imagen.getHeight())
                    );
                }
            }
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

    void setColor(Color color) {
        atributosActuales.setColorContorno(color);
        repaint();
    }

    public void setModoSeleccion(boolean activo) {
        this.modoSeleccion = activo;
        if (activo) {
            this.modoMover = false;
            this.modoRotar = false;
            this.modoEscalar = false;
            this.modoPincel = false;
            this.modoGoma = false;
            this.modoCubeta = false;
        }
        repaint();
    }

    public void setModoMover(boolean activo) {
        this.modoMover = activo;
        if (activo) {
            this.modoSeleccion = false;
            this.modoRotar = false;
            this.modoEscalar = false;
            this.modoPincel = false;
            this.modoGoma = false;
            this.modoCubeta = false;
        }
        repaint();
    }

    public void setModoRotar(boolean activo) {
        this.modoRotar = activo;
        if (activo) {
            this.modoSeleccion = false;
            this.modoMover = false;
            this.modoEscalar = false;
            this.modoPincel = false;
            this.modoGoma = false;
            this.modoCubeta = false;
        }
        repaint();
    }

    public void setModoEscalar(boolean activo) {
        this.modoEscalar = activo;
        if (activo) {
            this.modoSeleccion = false;
            this.modoMover = false;
            this.modoRotar = false;
            this.modoPincel = false;
            this.modoGoma = false;
            this.modoCubeta = false;
        }
        repaint();
    }

    public void setModoPincel(boolean activo) {
        this.modoPincel = activo;
        if (activo) {
            this.modoSeleccion = false;
            this.modoMover = false;
            this.modoRotar = false;
            this.modoEscalar = false;
            this.modoGoma = false;
            this.modoCubeta = false;
            puntosPincel.clear();
        }
        repaint();
    }

    public void setModoGoma(boolean activo) {
        this.modoGoma = activo;
        if (activo) {
            this.modoSeleccion = false;
            this.modoMover = false;
            this.modoRotar = false;
            this.modoEscalar = false;
            this.modoPincel = false;
            this.modoCubeta = false;
        }
        repaint();
    }

    public void setModoCubeta(boolean activo) {
        this.modoCubeta = activo;
        if (activo) {
            this.modoSeleccion = false;
            this.modoMover = false;
            this.modoRotar = false;
            this.modoEscalar = false;
            this.modoPincel = false;
            this.modoGoma = false;
        }
        repaint();
    }

    public void setTamanoPincel(float tamano) {
        this.tamanoPincel = tamano;
    }

    public void setTamanoGoma(float tamano) {
        this.tamanoGoma = tamano;
    }

    public void setColorCubeta(Color color) {
        this.colorCubeta = color;
    }

    public void setRellenoCubeta(boolean rellenar) {
        this.rellenoCubeta = rellenar;
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

    public void aplicarTextura(int index) {
        if (figurasSeleccionadas.isEmpty()) return;

        for (ShapeAtributos sa : figurasSeleccionadas) {
            sa.atributos.setTexturaIndex(index);
            if (index >= 0 && index < texturas.length && texturas[index] != null) {
                sa.textura = new TexturePaint(
                        texturas[index],
                        new Rectangle2D.Double(0, 0,
                                texturas[index].getWidth(),
                                texturas[index].getHeight())
                );
            } else {
                sa.textura = null;
            }
        }
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
}