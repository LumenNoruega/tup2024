package ar.edu.utn.frbb.tup.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimiento {
    
    // Enumeración para representar el tipo de movimiento
    public enum TipoMovimiento {
        DEPOSITO,
        RETIRO,
        TRANSFERENCIA_ENTRADA,
        TRANSFERENCIA_SALIDA
    }
    
    // Atributos de Movimiento
    private TipoMovimiento tipoMovimiento;
    private double monto;
    private LocalDateTime fechaHora;

    // Constructor
    public Movimiento(TipoMovimiento tipoMovimiento, double monto) {
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
    }

    // Métodos getter y setter
    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
 // Método para obtener la fecha formateada (De forma más legible)
    public String obtenerFechaFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return fechaHora.format(formatter);
    }
}