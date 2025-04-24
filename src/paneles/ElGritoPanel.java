package paneles;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class ElGritoPanel extends JPanel {

    public ElGritoPanel() {
        setPreferredSize(new Dimension(800, 600)); // Tamaño preferido del panel
        setBackground(Color.WHITE); // Fondo blanco
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar el cielo ondulado
        g2.setColor(new Color(255, 204, 102)); // Color naranja claro
        Path2D cielo = new Path2D.Double();
        cielo.moveTo(0, 100);
        cielo.curveTo(200, 50, 400, 150, 800, 100);
        cielo.lineTo(800, 0);
        cielo.lineTo(0, 0);
        cielo.closePath();
        g2.fill(cielo);

        // Dibujar el agua ondulada
        g2.setColor(new Color(0, 102, 204)); // Color azul oscuro
        Path2D agua = new Path2D.Double();
        agua.moveTo(0, 500);
        agua.curveTo(200, 550, 400, 450, 800, 500);
        agua.lineTo(800, 600);
        agua.lineTo(0, 600);
        agua.closePath();
        g2.fill(agua);

        // Dibujar la figura central (El Grito)
        g2.setColor(new Color(255, 153, 102)); // Color piel
        Ellipse2D cabeza = new Ellipse2D.Double(350, 200, 100, 120); // Cabeza
        g2.fill(cabeza);

        // Ojos
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(375, 230, 10, 15)); // Ojo izquierdo
        g2.fill(new Ellipse2D.Double(415, 230, 10, 15)); // Ojo derecho

        // Boca
        Path2D boca = new Path2D.Double();
        boca.moveTo(375, 290);
        boca.curveTo(400, 320, 425, 320, 450, 290); // Boca curva
        g2.setStroke(new BasicStroke(3));
        g2.draw(boca);

        // Manos
        g2.setColor(new Color(255, 153, 102)); // Color piel
        g2.fill(new Ellipse2D.Double(300, 300, 40, 60)); // Mano izquierda
        g2.fill(new Ellipse2D.Double(460, 300, 40, 60)); // Mano derecha

        // Cuerpo
        g2.setColor(new Color(204, 102, 0)); // Color marrón
        Path2D cuerpo = new Path2D.Double();
        cuerpo.moveTo(350, 320);
        cuerpo.lineTo(350, 450);
        cuerpo.lineTo(450, 450);
        cuerpo.lineTo(450, 320);
        cuerpo.closePath();
        g2.fill(cuerpo);

        // Líneas de expresión (onduladas)
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        Path2D lineasExpresion = new Path2D.Double();
        lineasExpresion.moveTo(300, 250);
        lineasExpresion.curveTo(350, 270, 450, 270, 500, 250); // Línea superior
        lineasExpresion.moveTo(300, 280);
        lineasExpresion.curveTo(350, 300, 450, 300, 500, 280); // Línea inferior
        g2.draw(lineasExpresion);
    }
}