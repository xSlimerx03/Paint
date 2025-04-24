package paneles.paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ShapeSelectorPanel extends JPanel {

    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JToggleButton BotonMenu;
    private JScrollPane scrollMenuPanel;
    private CustomDrawPanel drawPanel;
    private JCheckBox chkRelleno;
    private JCheckBox chkContorno;
    private JSlider sliderAnchoContorno;
    private JToggleButton togglePunteado;
    private JSlider sliderPunteado;
    private JLabel lblPunteado;
    private JButton btnGuardar;
    private JToggleButton toggleRellenoImagen;
    private JButton btnCargarImagen;
    private JSlider sliderEscalaImagen;
    private JSlider sliderTransparencia;
    private JLabel lblTransparencia;

    // Herramientas
    private JToggleButton btnSeleccionar;
    private JToggleButton btnMover;
    private JToggleButton btnRotar;
    private JToggleButton btnEscalar;
    private JToggleButton btnPincel;
    private JToggleButton btnGoma;
    private JToggleButton btnCubeta;
    private JButton btnFusionar;
    private JCheckBox chkSeleccionMultiple;
    private JCheckBox chkDegradado;
    private JButton btnColorDegradadoInicio;
    private JButton btnColorDegradadoFin;
    private JComboBox<String> cmbDireccionDegradado;
    private JButton btnEliminar;
    private JButton btnCopiar;
    private JButton btnPegar;
    private JButton btnTraerAlFrente;
    private JButton btnEnviarAtras;
    private JButton btnAgrupar;
    private JButton btnDesagrupar;
    private JSlider sliderTamanoPincel;
    private JButton[] btnTexturas = new JButton[4];
    private JLabel lblPreviewPincel;

    AtributosDibujo ad = new AtributosDibujo();

    public ShapeSelectorPanel(CustomDrawPanel drawPanel) {
        this.drawPanel = drawPanel;
        setLayout(new BorderLayout());
        initComponents();
        applyModernStyle();
    }

    private void initComponents() {
        String[] shapes1 = {"Círculo", "Rectángulo", "Elipse", "Linea", "Arco", "Triángulo", "Estrella", "Curva cuadrática", "Curva cúbica", "Óvalo"};
        String[] shapes2 = {"Corazón", "Cubo simple", "Don Ramón", "Carita feliz", "Carita triste", "Corazón roto", "Sol", "Árbol simple", "Ojo simple", "Diamante"};

        // Panel principal para los controles superiores
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Panel para los ComboBoxes y controles básicos
        JPanel comboPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        comboPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Panel para herramientas de selección y transformación
        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolsPanel.setBorder(BorderFactory.createTitledBorder("Herramientas"));

        // Panel del menú con scroll
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        scrollMenuPanel = new JScrollPane(menuPanel);
        scrollMenuPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollMenuPanel.setPreferredSize(new Dimension(300, 400));
        scrollMenuPanel.setBorder(BorderFactory.createEmptyBorder());

        // Inicializar componentes de herramientas
        initToolComponents(toolsPanel);

        // Inicializar componentes de dibujo
        initDrawingComponents(comboPanel, menuPanel, shapes1, shapes2);

        // Inicializar componentes de degradado
        initGradientComponents(menuPanel);

        // Inicializar componentes de imagen
        initImageComponents(menuPanel);

        // Inicializar componentes de contorno
        initStrokeComponents(menuPanel);

        // Inicializar componentes de transparencia
        initTransparencyComponents(menuPanel);

        // Inicializar componentes de pincel/goma/cubeta
        initPincelGomaCubetaComponents(menuPanel);

        // Inicializar componentes de texturas
        initTextureComponents(menuPanel);

        // Inicializar botón de guardar
        initSaveButton(comboPanel);

        // Configurar el botón de menú
        initMenuButton(comboPanel);

        // Agregar paneles al layout principal
        topPanel.add(toolsPanel, BorderLayout.NORTH);
        topPanel.add(comboPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(scrollMenuPanel, BorderLayout.EAST);

        // Inicialmente ocultar el menú
        scrollMenuPanel.setVisible(false);
    }

    private void initTransparencyComponents(JPanel menuPanel) {
        JPanel panelTransparencia = new JPanel(new GridLayout(0, 1, 5, 5));
        panelTransparencia.setBorder(BorderFactory.createTitledBorder("Transparencia"));

        sliderTransparencia = new JSlider(0, 100, 100);
        sliderTransparencia.setMajorTickSpacing(25);
        sliderTransparencia.setMinorTickSpacing(5);
        sliderTransparencia.setPaintTicks(true);
        sliderTransparencia.setPaintLabels(true);

        lblTransparencia = new JLabel("100%");

        sliderTransparencia.addChangeListener(e -> {
            int value = sliderTransparencia.getValue();
            lblTransparencia.setText(value + "%");
            float transparencia = value / 100.0f;
            drawPanel.setTransparencia(transparencia);
        });

        panelTransparencia.add(new JLabel("Nivel de transparencia:"));
        panelTransparencia.add(sliderTransparencia);
        panelTransparencia.add(lblTransparencia);

        menuPanel.add(panelTransparencia);
    }

    private void initToolComponents(JPanel toolsPanel) {
        // Botón de selección
        btnSeleccionar = new JToggleButton(resizeIcon("/icons/select.png", 16, 16));
        btnSeleccionar.setToolTipText("Seleccionar figuras (S)");
        btnSeleccionar.addActionListener(e -> {
            drawPanel.setModoSeleccion(btnSeleccionar.isSelected());
            if (btnSeleccionar.isSelected()) {
                btnMover.setSelected(false);
                btnRotar.setSelected(false);
                btnEscalar.setSelected(false);
                btnPincel.setSelected(false);
                btnGoma.setSelected(false);
                btnCubeta.setSelected(false);
            }
        });

        // Checkbox para selección múltiple
        chkSeleccionMultiple = new JCheckBox("Selección múltiple (Shift)");
        chkSeleccionMultiple.addActionListener(e -> {
            drawPanel.setSeleccionMultiple(chkSeleccionMultiple.isSelected());
        });

        // Botón de mover
        btnMover = new JToggleButton(resizeIcon("/icons/move.png", 16, 16));
        btnMover.setToolTipText("Mover figuras seleccionadas (M)");
        btnMover.addActionListener(e -> {
            drawPanel.setModoMover(btnMover.isSelected());
            if (btnMover.isSelected()) {
                btnSeleccionar.setSelected(false);
                btnRotar.setSelected(false);
                btnEscalar.setSelected(false);
                btnPincel.setSelected(false);
                btnGoma.setSelected(false);
                btnCubeta.setSelected(false);
            }
        });

        // Botón de rotar
        btnRotar = new JToggleButton(resizeIcon("/icons/rotate.png", 16, 16));
        btnRotar.setToolTipText("Rotar figuras seleccionadas (R)");
        btnRotar.addActionListener(e -> {
            drawPanel.setModoRotar(btnRotar.isSelected());
            if (btnRotar.isSelected()) {
                btnSeleccionar.setSelected(false);
                btnMover.setSelected(false);
                btnEscalar.setSelected(false);
                btnPincel.setSelected(false);
                btnGoma.setSelected(false);
                btnCubeta.setSelected(false);
            }
        });

        // Botón de escalar
        btnEscalar = new JToggleButton(resizeIcon("/icons/scale.png", 16, 16));
        btnEscalar.setToolTipText("Escalar figuras seleccionadas (E)");
        btnEscalar.addActionListener(e -> {
            drawPanel.setModoEscalar(btnEscalar.isSelected());
            if (btnEscalar.isSelected()) {
                btnSeleccionar.setSelected(false);
                btnMover.setSelected(false);
                btnRotar.setSelected(false);
                btnPincel.setSelected(false);
                btnGoma.setSelected(false);
                btnCubeta.setSelected(false);
            }
        });

        // Botón de pincel
        btnPincel = new JToggleButton(resizeIcon("/icons/pencil.png", 16, 16));
        btnPincel.setToolTipText("Dibujo libre (P)");
        btnPincel.addActionListener(e -> {
            drawPanel.setModoPincel(btnPincel.isSelected());
            if (btnPincel.isSelected()) {
                btnSeleccionar.setSelected(false);
                btnMover.setSelected(false);
                btnRotar.setSelected(false);
                btnEscalar.setSelected(false);
                btnGoma.setSelected(false);
                btnCubeta.setSelected(false);
            }
        });

        // Botón de goma
        btnGoma = new JToggleButton(resizeIcon("/icons/eraser.png", 16, 16));
        btnGoma.setToolTipText("Borrar figuras (G)");
        btnGoma.addActionListener(e -> {
            drawPanel.setModoGoma(btnGoma.isSelected());
            if (btnGoma.isSelected()) {
                btnSeleccionar.setSelected(false);
                btnMover.setSelected(false);
                btnRotar.setSelected(false);
                btnEscalar.setSelected(false);
                btnPincel.setSelected(false);
                btnCubeta.setSelected(false);
            }
        });

        // Botón de cubeta
        btnCubeta = new JToggleButton(resizeIcon("/icons/bucket.png", 16, 16));
        btnCubeta.setToolTipText("Rellenar áreas (B)");
        btnCubeta.addActionListener(e -> {
            drawPanel.setModoCubeta(btnCubeta.isSelected());
            if (btnCubeta.isSelected()) {
                btnSeleccionar.setSelected(false);
                btnMover.setSelected(false);
                btnRotar.setSelected(false);
                btnEscalar.setSelected(false);
                btnPincel.setSelected(false);
                btnGoma.setSelected(false);
            }
        });

        // Botón de fusionar
        btnFusionar = new JButton(resizeIcon("/icons/merge.png", 16, 16));
        btnFusionar.setToolTipText("Fusionar figuras seleccionadas (Ctrl+F)");
        btnFusionar.addActionListener(e -> {
            drawPanel.fusionarFigurasSeleccionadas();
        });

        // Botón de eliminar
        btnEliminar = new JButton(resizeIcon("/icons/delete.png", 16, 16));
        btnEliminar.setToolTipText("Eliminar figuras seleccionadas (Del)");
        btnEliminar.addActionListener(e -> {
            drawPanel.eliminarFigurasSeleccionadas();
        });

        // Botón de copiar
        btnCopiar = new JButton(resizeIcon("/icons/copy.png", 16, 16));
        btnCopiar.setToolTipText("Copiar figuras seleccionadas (Ctrl+C)");
        btnCopiar.addActionListener(e -> {
            drawPanel.copiarFigurasSeleccionadas();
        });

        // Botón de pegar
        btnPegar = new JButton(resizeIcon("/icons/paste.png", 16, 16));
        btnPegar.setToolTipText("Pegar figuras copiadas (Ctrl+V)");
        btnPegar.addActionListener(e -> {
            drawPanel.pegarFigurasCopiadas();
        });

        // Botón de traer al frente
        btnTraerAlFrente = new JButton(resizeIcon("/icons/bring_to_front.png", 16, 16));
        btnTraerAlFrente.setToolTipText("Traer al frente (Ctrl+↑)");
        btnTraerAlFrente.addActionListener(e -> {
            drawPanel.traerAlFrente();
        });

        // Botón de enviar atrás
        btnEnviarAtras = new JButton(resizeIcon("/icons/send_to_back.png", 16, 16));
        btnEnviarAtras.setToolTipText("Enviar atrás (Ctrl+↓)");
        btnEnviarAtras.addActionListener(e -> {
            drawPanel.enviarAtras();
        });

        // Botón de agrupar
        btnAgrupar = new JButton(resizeIcon("/icons/group.png", 16, 16));
        btnAgrupar.setToolTipText("Agrupar figuras (Ctrl+G)");
        btnAgrupar.addActionListener(e -> {
            drawPanel.agruparFiguras();
        });

        // Botón de desagrupar
        btnDesagrupar = new JButton(resizeIcon("/icons/ungroup.png", 16, 16));
        btnDesagrupar.setToolTipText("Desagrupar figuras (Ctrl+U)");
        btnDesagrupar.addActionListener(e -> {
            drawPanel.desagruparFiguras();
        });

        // Agregar componentes al panel de herramientas
        toolsPanel.add(btnSeleccionar);
        toolsPanel.add(chkSeleccionMultiple);
        toolsPanel.add(btnMover);
        toolsPanel.add(btnRotar);
        toolsPanel.add(btnEscalar);
        toolsPanel.add(btnPincel);
        toolsPanel.add(btnGoma);
        toolsPanel.add(btnCubeta);
        toolsPanel.add(btnFusionar);
        toolsPanel.add(btnEliminar);
        toolsPanel.add(btnCopiar);
        toolsPanel.add(btnPegar);
        toolsPanel.add(btnTraerAlFrente);
        toolsPanel.add(btnEnviarAtras);
        toolsPanel.add(btnAgrupar);
        toolsPanel.add(btnDesagrupar);
    }

    private void initDrawingComponents(JPanel comboPanel, JPanel menuPanel, String[] shapes1, String[] shapes2) {
        // CheckBox para Relleno
        chkRelleno = new JCheckBox("Relleno", true);
        chkRelleno.addActionListener(e -> {
            drawPanel.setRelleno(chkRelleno.isSelected());
            drawPanel.repaint();
        });

        // CheckBox para Contorno
        chkContorno = new JCheckBox("Contorno", true);
        chkContorno.addActionListener(e -> {
            drawPanel.setContorno(chkContorno.isSelected());
            drawPanel.repaint();
        });

        // Botones de selección de color
        JButton btnRelleno = new JButton("Color Relleno");
        btnRelleno.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Color de Relleno", Color.WHITE);
            if (color != null) {
                drawPanel.setColorRelleno(color);
            }
        });

        JButton btnContorno = new JButton("Color Contorno");
        btnContorno.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Color de Contorno", Color.BLACK);
            if (color != null) {
                drawPanel.setColorContorno(color);
            }
        });

        // ComboBoxes para selección de figuras
        comboBox1 = new JComboBox<>(shapes1);
        comboBox2 = new JComboBox<>(shapes2);

        comboBox1.addActionListener(e -> {
            int selectedIndex = comboBox1.getSelectedIndex() + 1;
            drawPanel.setFigura(selectedIndex);
        });

        comboBox2.addActionListener(e -> {
            int selectedIndex = comboBox2.getSelectedIndex() + 1 + shapes1.length;
            drawPanel.setFigura(selectedIndex);
        });

        // Agregar componentes al panel de combos
        comboPanel.add(chkRelleno);
        comboPanel.add(btnRelleno);
        comboPanel.add(chkContorno);
        comboPanel.add(btnContorno);
        comboPanel.add(comboBox1);
        comboPanel.add(comboBox2);

        // Agregar componentes al menú
        menuPanel.add(chkRelleno);
        menuPanel.add(btnRelleno);
        menuPanel.add(chkContorno);
        menuPanel.add(btnContorno);
    }

    private void initGradientComponents(JPanel menuPanel) {
        // Panel para degradado
        JPanel panelDegradado = new JPanel(new GridLayout(0, 1, 5, 5));
        panelDegradado.setBorder(BorderFactory.createTitledBorder("Degradado"));

        chkDegradado = new JCheckBox("Relleno degradado");
        chkDegradado.addActionListener(e -> {
            drawPanel.getAtributosActuales().setRellenoDegradado(chkDegradado.isSelected());
            drawPanel.repaint();
        });

        btnColorDegradadoInicio = new JButton("Color inicio");
        btnColorDegradadoInicio.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Color inicio degradado", Color.WHITE);
            if (color != null) {
                drawPanel.getAtributosActuales().setColorDegradadoInicio(color);
                drawPanel.repaint();
            }
        });

        btnColorDegradadoFin = new JButton("Color fin");
        btnColorDegradadoFin.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Color fin degradado", Color.BLUE);
            if (color != null) {
                drawPanel.getAtributosActuales().setColorDegradadoFin(color);
                drawPanel.repaint();
            }
        });

        String[] direcciones = {"Horizontal", "Vertical", "Diagonal", "Circular", "Radial"};
        cmbDireccionDegradado = new JComboBox<>(direcciones);
        cmbDireccionDegradado.addActionListener(e -> {
            drawPanel.getAtributosActuales().setDireccionDegradado(cmbDireccionDegradado.getSelectedIndex());
            drawPanel.repaint();
        });

        panelDegradado.add(chkDegradado);
        panelDegradado.add(new JLabel("Dirección:"));
        panelDegradado.add(cmbDireccionDegradado);
        panelDegradado.add(btnColorDegradadoInicio);
        panelDegradado.add(btnColorDegradadoFin);

        menuPanel.add(panelDegradado);
    }

    private void initImageComponents(JPanel menuPanel) {
        JPanel panelRellenoImagen = new JPanel(new GridLayout(0, 1, 5, 5));
        panelRellenoImagen.setBorder(BorderFactory.createTitledBorder("Relleno con Imagen"));

        btnCargarImagen = new JButton("Cargar Imagen");
        btnCargarImagen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                drawPanel.cargarImagenRelleno(fileChooser.getSelectedFile());
                toggleRellenoImagen.setEnabled(true);
                sliderEscalaImagen.setEnabled(true);
            }
        });

        toggleRellenoImagen = new JToggleButton("Relleno Imagen OFF");
        toggleRellenoImagen.addActionListener(e -> {
            boolean activado = toggleRellenoImagen.isSelected();
            toggleRellenoImagen.setText(activado ? "Relleno Imagen ON" : "Relleno Imagen OFF");
            drawPanel.getAtributosActuales().setRellenoImagenActivo(activado);
            drawPanel.repaint();
        });

        JPanel panelEscalaImagen = new JPanel(new GridLayout(0, 1, 5, 5));
        panelEscalaImagen.setBorder(BorderFactory.createTitledBorder("Escala de Imagen"));

        sliderEscalaImagen = new JSlider(10, 200, 100);
        sliderEscalaImagen.setMajorTickSpacing(50);
        sliderEscalaImagen.setMinorTickSpacing(10);
        sliderEscalaImagen.setPaintTicks(true);
        sliderEscalaImagen.setPaintLabels(true);

        JLabel lblEscala = new JLabel("100%");

        sliderEscalaImagen.addChangeListener(e -> {
            int escala = sliderEscalaImagen.getValue();
            lblEscala.setText(escala + "%");
            if (drawPanel.getAtributosActuales().getImagenRelleno() != null) {
                BufferedImage imagenOriginal = drawPanel.getAtributosActuales().getImagenRelleno();
                int newWidth = (int) (imagenOriginal.getWidth() * escala / 100.0);
                int newHeight = (int) (imagenOriginal.getHeight() * escala / 100.0);
                BufferedImage imagenEscalada = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = imagenEscalada.createGraphics();
                g2.drawImage(imagenOriginal, 0, 0, newWidth, newHeight, null);
                g2.dispose();
                drawPanel.getAtributosActuales().setImagenRelleno(imagenEscalada);
                drawPanel.repaint();
            }
        });

        panelEscalaImagen.add(sliderEscalaImagen);
        panelEscalaImagen.add(lblEscala);

        panelRellenoImagen.add(btnCargarImagen);
        panelRellenoImagen.add(toggleRellenoImagen);
        panelRellenoImagen.add(panelEscalaImagen);

        menuPanel.add(panelRellenoImagen);

        // Inicialmente deshabilitados
        toggleRellenoImagen.setEnabled(false);
        sliderEscalaImagen.setEnabled(false);
    }

    private void initStrokeComponents(JPanel menuPanel) {
        JPanel panelContorno = new JPanel(new GridLayout(0, 1, 5, 5));
        panelContorno.setBorder(BorderFactory.createTitledBorder("Estilo de Contorno"));

        sliderAnchoContorno = new JSlider(1, 20, 1);
        sliderAnchoContorno.setMajorTickSpacing(5);
        sliderAnchoContorno.setMinorTickSpacing(1);
        sliderAnchoContorno.setPaintTicks(true);
        sliderAnchoContorno.setPaintLabels(true);
        sliderAnchoContorno.setToolTipText("Ancho del contorno");

        sliderAnchoContorno.addChangeListener(e -> {
            int value = sliderAnchoContorno.getValue();
            drawPanel.setAnchoContorno(value);
        });

        sliderPunteado = new JSlider(1, 20, 4);
        sliderPunteado.setMajorTickSpacing(5);
        sliderPunteado.setMinorTickSpacing(1);
        sliderPunteado.setPaintTicks(true);
        sliderPunteado.setToolTipText("Separación del punteado");

        lblPunteado = new JLabel("Punteado: 4px");

        sliderPunteado.addChangeListener(e -> {
            int value = sliderPunteado.getValue();
            lblPunteado.setText("Punteado: " + value + "px");
            float[] patron = {value, value};
            drawPanel.setPatronPunteado(patron);
        });

        togglePunteado = new JToggleButton("Punteado OFF");
        sliderPunteado.setEnabled(false);
        togglePunteado.setSelected(false);

        togglePunteado.addActionListener(e -> {
            boolean activado = togglePunteado.isSelected();
            togglePunteado.setText(activado ? "Punteado ON" : "Punteado OFF");
            drawPanel.getAtributosActuales().setStrokePunteado(activado);
            sliderPunteado.setEnabled(activado);
            drawPanel.repaint();
        });

        panelContorno.add(new JLabel("Ancho del contorno:"));
        panelContorno.add(sliderAnchoContorno);
        panelContorno.add(new JLabel("Estilo punteado:"));
        panelContorno.add(togglePunteado);
        panelContorno.add(sliderPunteado);
        panelContorno.add(lblPunteado);

        menuPanel.add(panelContorno);
    }

    private void initPincelGomaCubetaComponents(JPanel menuPanel) {
        JPanel panelHerramientas = new JPanel(new GridLayout(0, 1, 5, 5));
        panelHerramientas.setBorder(BorderFactory.createTitledBorder("Herramientas de dibujo"));

        // Configuración del pincel
        JPanel panelPincel = new JPanel(new GridLayout(0, 1, 5, 5));
        panelPincel.setBorder(BorderFactory.createTitledBorder("Pincel"));

        sliderTamanoPincel = new JSlider(1, 50, 5);
        sliderTamanoPincel.setMajorTickSpacing(10);
        sliderTamanoPincel.setMinorTickSpacing(1);
        sliderTamanoPincel.setPaintTicks(true);
        sliderTamanoPincel.setPaintLabels(true);

        JLabel lblTamanoPincel = new JLabel("Tamaño: 5px");

        sliderTamanoPincel.addChangeListener(e -> {
            int value = sliderTamanoPincel.getValue();
            lblTamanoPincel.setText("Tamaño: " + value + "px");
            drawPanel.setTamanoPincel(value);
            updatePincelPreview();
        });

        // Panel de vista previa del pincel
        JPanel previewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = sliderTamanoPincel.getValue();
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;

                g2.setColor(Color.BLACK);
                g2.fillOval(centerX - size/2, centerY - size/2, size, size);
            }
        };
        previewPanel.setPreferredSize(new Dimension(50, 50));
        previewPanel.setBorder(BorderFactory.createTitledBorder("Vista previa"));

        panelPincel.add(new JLabel("Tamaño del pincel:"));
        panelPincel.add(sliderTamanoPincel);
        panelPincel.add(lblTamanoPincel);
        panelPincel.add(previewPanel);

        // Configuración de la goma
        JPanel panelGoma = new JPanel(new GridLayout(0, 1, 5, 5));
        panelGoma.setBorder(BorderFactory.createTitledBorder("Goma"));

        JSlider sliderTamanoGoma = new JSlider(1, 50, 10);
        sliderTamanoGoma.setMajorTickSpacing(10);
        sliderTamanoGoma.setMinorTickSpacing(1);
        sliderTamanoGoma.setPaintTicks(true);
        sliderTamanoGoma.setPaintLabels(true);

        JLabel lblTamanoGoma = new JLabel("Tamaño: 10px");

        sliderTamanoGoma.addChangeListener(e -> {
            int value = sliderTamanoGoma.getValue();
            lblTamanoGoma.setText("Tamaño: " + value + "px");
            drawPanel.setTamanoGoma(value);
        });

        panelGoma.add(new JLabel("Tamaño de la goma:"));
        panelGoma.add(sliderTamanoGoma);
        panelGoma.add(lblTamanoGoma);

        // Configuración de la cubeta
        JPanel panelCubeta = new JPanel(new GridLayout(0, 1, 5, 5));
        panelCubeta.setBorder(BorderFactory.createTitledBorder("Cubeta"));

        JButton btnColorCubeta = new JButton("Color de relleno");
        btnColorCubeta.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Color de relleno", Color.WHITE);
            if (color != null) {
                drawPanel.setColorCubeta(color);
            }
        });

        JCheckBox chkRellenoCubeta = new JCheckBox("Rellenar contornos cerrados", true);
        chkRellenoCubeta.addActionListener(e -> {
            drawPanel.setRellenoCubeta(chkRellenoCubeta.isSelected());
        });

        panelCubeta.add(btnColorCubeta);
        panelCubeta.add(chkRellenoCubeta);

        // Agregar paneles al panel principal
        panelHerramientas.add(panelPincel);
        panelHerramientas.add(panelGoma);
        panelHerramientas.add(panelCubeta);

        menuPanel.add(panelHerramientas);
    }

    private void updatePincelPreview() {
        Component[] components = scrollMenuPanel.getComponents();
        for (Component c : components) {
            if (c instanceof JPanel) {
                JPanel panel = (JPanel) c;
                Component[] subComponents = panel.getComponents();
                for (Component sc : subComponents) {
                    if (sc instanceof JPanel && ((JPanel) sc).getBorder() != null &&
                            ((JPanel) sc).getBorder().toString().contains("Vista previa")) {
                        sc.repaint();
                        return;
                    }
                }
            }
        }
    }

    private void initTextureComponents(JPanel menuPanel) {
        JPanel panelTexturas = new JPanel(new GridLayout(2, 2, 5, 5));
        panelTexturas.setBorder(BorderFactory.createTitledBorder("Texturas"));

        String[] nombresTexturas = {"Madera", "Líneas", "Puntos", "Cuadrícula"};

        for (int i = 0; i < 4; i++) {
            btnTexturas[i] = new JButton(nombresTexturas[i]);
            final int index = i;
            btnTexturas[i].addActionListener(e -> {
                drawPanel.aplicarTextura(index);
            });
            panelTexturas.add(btnTexturas[i]);
        }

        menuPanel.add(panelTexturas);
    }

    private void initSaveButton(JPanel comboPanel) {
        btnGuardar = new JButton("Guardar Imagen (Ctrl+S)");
        btnGuardar.addActionListener(this::guardarImagen);
        comboPanel.add(btnGuardar);
    }

    private void initMenuButton(JPanel comboPanel) {
        BotonMenu = new JToggleButton("Abrir Menu");
        BotonMenu.addActionListener(e -> {
            if (BotonMenu.isSelected()) {
                BotonMenu.setText("Cerrar Menu");
                scrollMenuPanel.setVisible(true);
            } else {
                BotonMenu.setText("Abrir Menu");
                scrollMenuPanel.setVisible(false);
            }
        });
        comboPanel.add(BotonMenu);
    }

    private void guardarImagen(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar dibujo como imagen");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes PNG", "png"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes JPEG", "jpg", "jpeg"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String formato = "png";

            if (fileChooser.getFileFilter().getDescription().contains("JPEG")) {
                formato = "jpg";
                if (!archivo.getName().toLowerCase().endsWith(".jpg") &&
                        !archivo.getName().toLowerCase().endsWith(".jpeg")) {
                    archivo = new File(archivo.getAbsolutePath() + ".jpg");
                }
            } else {
                if (!archivo.getName().toLowerCase().endsWith(".png")) {
                    archivo = new File(archivo.getAbsolutePath() + ".png");
                }
            }

            try {
                drawPanel.guardarComoImagen(archivo);
                JOptionPane.showMessageDialog(this,
                        "Imagen guardada exitosamente en:\n" + archivo.getAbsolutePath(),
                        "Guardado exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar la imagen:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void applyModernStyle() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);

            // Estilo personalizado para los botones
            Color accentColor = new Color(70, 130, 180);
            Color backgroundColor = new Color(240, 240, 240);

            setBackground(backgroundColor);

            UIManager.put("Button.background", new Color(245, 245, 245));
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            UIManager.put("Button.select", accentColor);

            UIManager.put("ToggleButton.background", new Color(245, 245, 245));
            UIManager.put("ToggleButton.foreground", Color.BLACK);
            UIManager.put("ToggleButton.border", BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            UIManager.put("ToggleButton.select", accentColor);

            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ComboBox.border", BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

            UIManager.put("Slider.background", backgroundColor);
            UIManager.put("Slider.trackColor", new Color(200, 200, 200));
            UIManager.put("Slider.thumbColor", accentColor);
            UIManager.put("Slider.highlight", accentColor);

            UIManager.put("Panel.background", backgroundColor);
            UIManager.put("ScrollPane.background", backgroundColor);
            UIManager.put("Viewport.background", backgroundColor);

            UIManager.put("TitledBorder.titleColor", new Color(80, 80, 80));
            UIManager.put("TitledBorder.border", BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ImageIcon resizeIcon(String path, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(ImageIO.read(getClass().getResource(path)));
            Image img = originalIcon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        } catch (IOException e) {
            System.err.println("Error al cargar el icono: " + path);
            return new ImageIcon(); // Icono vacío si hay error
        }
    }
}