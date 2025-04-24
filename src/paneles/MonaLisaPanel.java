package paneles;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class MonaLisaPanel extends JPanel {

    public MonaLisaPanel() {
        setPreferredSize(new Dimension(800, 800)); // Tamaño del panel
        setBackground(new Color(240, 240, 220)); // Fondo claro
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Escalar el dibujo para que se ajuste al panel
        double scale = Math.min(getWidth() / 400.98, getHeight() / 400.98);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        g2.transform(at);

        // Dibujar el paisaje de fondo (detrás de la Mona Lisa)
        dibujarPaisaje(g2);

        // Dibujar la Mona Lisa (enfrente del paisaje pero dentro del marco)
        dibujarMonaLisa(g2);

        // Dibujar el marco (encima de todo)
        dibujarMarco(g2);
    }

    private void dibujarPaisaje(Graphics2D g2) {
        // Dibujar el fondo del paisaje
        g2.setColor(new Color(100, 150, 100)); // Color verde para el paisaje
        Path2D paisaje = new Path2D.Double();
        paisaje.moveTo(0, 300);
        paisaje.curveTo(100, 250, 300, 250, 400, 300);
        paisaje.lineTo(400, 400);
        paisaje.lineTo(0, 400);
        paisaje.closePath();
        g2.fill(paisaje);

        // Dibujar montañas en el fondo
        g2.setColor(new Color(120, 160, 120)); // Color verde oscuro para montañas
        Path2D montaña1 = new Path2D.Double();
        montaña1.moveTo(50, 300);
        montaña1.lineTo(150, 200);
        montaña1.lineTo(250, 300);
        montaña1.closePath();
        g2.fill(montaña1);

        Path2D montaña2 = new Path2D.Double();
        montaña2.moveTo(200, 300);
        montaña2.lineTo(300, 220);
        montaña2.lineTo(350, 300);
        montaña2.closePath();
        g2.fill(montaña2);

        // Añadir sombras a las montañas
        g2.setColor(new Color(90, 130, 90)); // Color verde más oscuro para sombras
        Path2D sombraMontaña1 = new Path2D.Double();
        sombraMontaña1.moveTo(100, 250);
        sombraMontaña1.lineTo(150, 200);
        sombraMontaña1.lineTo(200, 250);
        sombraMontaña1.closePath();
        g2.fill(sombraMontaña1);

        Path2D sombraMontaña2 = new Path2D.Double();
        sombraMontaña2.moveTo(250, 250);
        sombraMontaña2.lineTo(300, 220);
        sombraMontaña2.lineTo(320, 250);
        sombraMontaña2.closePath();
        g2.fill(sombraMontaña2);
    }

    private void dibujarMonaLisa(Graphics2D g2) {
        // Dibujar la cabeza
        g2.setColor(new Color(200, 170, 130)); // Color piel
        Ellipse2D cabeza = new Ellipse2D.Double(150, 100, 100, 120);
        g2.fill(cabeza);

        // Dibujar el cabello (arriba de la cabeza)
        g2.setColor(new Color(80, 50, 30)); // Color marrón oscuro para el cabello
        Path2D cabello = new Path2D.Double();
        cabello.moveTo(150, 100);
        cabello.curveTo(120, 80, 110, 60, 130, 50); // Curva hacia arriba
        cabello.curveTo(160, 40, 190, 50, 210, 60); // Curva hacia la derecha
        cabello.curveTo(230, 70, 240, 90, 250, 100); // Curva hacia abajo
        cabello.lineTo(250, 120); // Bajar a la cabeza
        cabello.lineTo(150, 120); // Volver al inicio
        cabello.closePath();
        g2.fill(cabello);

        // Dibujar los ojos
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(170, 140, 10, 15)); // Ojo izquierdo
        g2.fill(new Ellipse2D.Double(220, 140, 10, 15)); // Ojo derecho

        // Dibujar la boca
        g2.setColor(new Color(150, 70, 50)); // Color rojo oscuro para la boca
        Path2D boca = new Path2D.Double();
        boca.moveTo(180, 190);
        boca.curveTo(190, 200, 210, 200, 220, 190);
        g2.setStroke(new BasicStroke(3));
        g2.draw(boca);

        // Dibujar el cuerpo
        g2.setColor(new Color(100, 70, 40)); // Color marrón oscuro para el vestido
        Path2D cuerpo = new Path2D.Double();
        cuerpo.moveTo(150, 220);
        cuerpo.lineTo(150, 300);
        cuerpo.lineTo(250, 300);
        cuerpo.lineTo(250, 220);
        cuerpo.closePath();
        g2.fill(cuerpo);

        // Dibujar las manos
        g2.setColor(new Color(200, 170, 130)); // Color piel para las manos
        g2.fill(new Ellipse2D.Double(130, 250, 40, 60)); // Mano izquierda
        g2.fill(new Ellipse2D.Double(230, 250, 40, 60)); // Mano derecha

        // Dibujar detalles del vestido
        g2.setColor(new Color(120, 90, 60)); // Color marrón claro para detalles
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(150, 220, 250, 220); // Línea superior del vestido
        g2.drawLine(150, 300, 250, 300); // Línea inferior del vestido
    }

    private void dibujarMarco(Graphics2D g2) {
        // Dibujar el marco (encima de todo)
        g2.setColor(new Color(100, 70, 40)); // Color marrón oscuro para el marco
        g2.setStroke(new BasicStroke(10));
        g2.drawRect(10, 10, 380, 380);
    }
}