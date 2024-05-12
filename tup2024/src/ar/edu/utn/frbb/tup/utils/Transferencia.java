package ar.edu.utn.frbb.tup.utils;

public class Transferencia {
	
	// Atributos de Transferencia.
	private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private double monto;
    
    // Constructor
    public Transferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto) {
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
    }
    
    // Métodos getters y setters.
	public Cuenta getCuentaOrigen() {
		return cuentaOrigen;
	}
	
	public void setCuentaOrigen(Cuenta cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}
	
	public Cuenta getCuentaDestino() {
		return cuentaDestino;
	}
	
	public void setCuentaDestino(Cuenta cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}
	
	public double getMonto() {
		return monto;
	}
	
	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	// Método para validar la transferencia
    public boolean validarTransferencia() {
        // Verificar que la cuenta de origen y la cuenta de destino sean diferentes
        if (cuentaOrigen.equals(cuentaDestino)) {
            System.out.println("Error: La cuenta de origen y la cuenta de destino son iguales.");
            return false;
        }
        
        // Verificar que el monto a transferir sea válido (positivo)
        if (monto <= 0) {
            System.out.println("Error: El monto a transferir debe ser mayor que cero.");
            return false;
        }
        
        // La transferencia es válida
        return true;
    }

}
