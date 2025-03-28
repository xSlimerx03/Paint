/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paneles.paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

public class AtributosDibujo {

    private Color colorRelleno;
    private Color colorContorno;
    private boolean rellenoActivo;
    private boolean contornoActivo;
    private float anchoContorno = 1.0f; // Valor por defecto
    ///
    private boolean rellenoDegradado = false;
    private Color colorDegradadoInicio = Color.WHITE;
    private Color colorDegradadoFin = Color.BLUE;
    private int direccionDegradado = 0; // 0=horizontal, 1=vertical, 2=diagonal
    ////
    private boolean strokePunteado = false;
    private float[] patronPunteado = {10f, 5f}; // {trazo, espacio}

    private boolean rellenoImagenActivo = false;
    private transient BufferedImage imagenRelleno;
    
    

    // Constructor con valores por defecto
    public AtributosDibujo() {
        this.colorRelleno = Color.WHITE;
        this.colorContorno = Color.BLACK;
        this.rellenoActivo = true;
        this.contornoActivo = true;
    }

    // Constructor personalizado
    public AtributosDibujo(Color colorRelleno, Color colorContorno,
            boolean rellenoActivo, boolean contornoActivo) {
        this.colorRelleno = colorRelleno;
        this.colorContorno = colorContorno;
        this.rellenoActivo = rellenoActivo;
        this.contornoActivo = contornoActivo;
    }

    // Getters y Setters
    
    
    // Getters y Setters
    public boolean isRellenoImagenActivo() {
        return rellenoImagenActivo;
    }

    public void setRellenoImagenActivo(boolean activo) {
        this.rellenoImagenActivo = activo;
    }

    public BufferedImage getImagenRelleno() {
        return imagenRelleno;
    }

    public void setImagenRelleno(BufferedImage imagen) {
        this.imagenRelleno = imagen;
    }
    
    
    public Color getColorRelleno() {
        return colorRelleno;
    }

    public void setColorRelleno(Color colorRelleno) {
        this.colorRelleno = colorRelleno;
    }

    public Color getColorContorno() {
        return colorContorno;
    }

    public void setColorContorno(Color colorContorno) {
        this.colorContorno = colorContorno;
    }

    public boolean isRellenoActivo() {
        return rellenoActivo;
    }

    public void setRellenoActivo(boolean rellenoActivo) {
        this.rellenoActivo = rellenoActivo;
    }

    public boolean isContornoActivo() {
        return contornoActivo;
    }

    public void setContornoActivo(boolean contornoActivo) {
        this.contornoActivo = contornoActivo;
    }

    public float getAnchoContorno() {
        return anchoContorno;
    }

    public void setAnchoContorno(float anchoContorno) {
        this.anchoContorno = anchoContorno;
    }

    public boolean isRellenoDegradado() {
        return rellenoDegradado;
    }

    public void setRellenoDegradado(boolean rellenoDegradado) {
        this.rellenoDegradado = rellenoDegradado;
    }

    public Color getColorDegradadoInicio() {
        return colorDegradadoInicio;
    }

    public void setColorDegradadoInicio(Color colorDegradadoInicio) {
        this.colorDegradadoInicio = colorDegradadoInicio;
    }

    public Color getColorDegradadoFin() {
        return colorDegradadoFin;
    }

    public void setColorDegradadoFin(Color colorDegradadoFin) {
        this.colorDegradadoFin = colorDegradadoFin;
    }

    public int getDireccionDegradado() {
        return direccionDegradado;
    }

    public void setDireccionDegradado(int direccionDegradado) {
        this.direccionDegradado = direccionDegradado;
    }

    public boolean isStrokePunteado() {
        return strokePunteado;
    }

    public void setStrokePunteado(boolean activado) {
        this.strokePunteado = activado;
    }

    public float[] getPatronPunteado() {
        return patronPunteado;
    }

    public void setPatronPunteado(float[] patronPunteado) {
        this.patronPunteado = patronPunteado;
    }

    public BasicStroke crearStroke() {
        if (strokePunteado) {
            return new BasicStroke(
                    anchoContorno,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f,
                    patronPunteado,
                    0f
            );
        } else {
            return new BasicStroke(anchoContorno);
        }
    }

    // Método para copiar los atributos actuales
    public AtributosDibujo copiar() {
        return new AtributosDibujo(
                this.colorRelleno,
                this.colorContorno,
                this.rellenoActivo,
                this.contornoActivo
        );
    }

}
