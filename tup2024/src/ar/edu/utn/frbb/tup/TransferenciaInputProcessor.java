package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.utils.Cuenta;
import ar.edu.utn.frbb.tup.utils.Movimiento;
import ar.edu.utn.frbb.tup.utils.Cliente;
import ar.edu.utn.frbb.tup.utils.Transferencia;

import java.util.List;
import java.util.Scanner;

public class TransferenciaInputProcessor extends BaseInputProcessor{
	
	private static final Scanner scanner = new Scanner(System.in);
	
	public static void transferir(List<Cliente> clientes) {
	    System.out.print("Ingrese el número de cuenta desde la que va a enviar dinero: ");
	    String numeroCuentaOrigen = scanner.nextLine();

	    System.out.print("Ingrese el número de cuenta a la que desea transferir dinero: ");
	    String numeroCuentaDestino = scanner.nextLine();

	    Cuenta cuentaOrigen = buscarCuenta(clientes, numeroCuentaOrigen);
	    Cuenta cuentaDestino = buscarCuenta(clientes, numeroCuentaDestino);

	    if (cuentaOrigen != null && cuentaDestino != null) {
	        double monto;
	        do {
	            System.out.print("Ingrese el monto a transferir: ");
	            while (!scanner.hasNextDouble()) {
	                System.out.println("Por favor, ingrese un monto válido para la transferencia.");
	                scanner.next(); // Consumir el valor inválido
	            }
	            monto = scanner.nextDouble();
	        } while (monto <= 0); // Mientras el monto sea mayor que 0.

	        scanner.nextLine(); // Consumir el salto de línea

	        // Validar la transferencia
	        Transferencia transferencia = new Transferencia(cuentaOrigen, cuentaDestino, monto);
	        if (transferencia.validarTransferencia()) {
	            // Realizar la transferencia
	            if (cuentaOrigen.retirar(monto)) {
	                cuentaDestino.depositar(monto);

	                // Registrar la transferencia en las cuentas
	                cuentaOrigen.agregarMovimiento(new Movimiento(Movimiento.TipoMovimiento.TRANSFERENCIA_SALIDA, monto));
	                cuentaDestino.agregarMovimiento(new Movimiento(Movimiento.TipoMovimiento.TRANSFERENCIA_ENTRADA, monto));

	                System.out.println("Transferencia realizada exitosamente.");
	            } else {
	                System.out.println("Error. Saldo insuficiente en la cuenta origen.");
	            }
	        }
	    } else {
	        System.out.println("Error. Una de las cuentas especificadas no existe.");
	    }
	}
	
	// Método auxiliar para buscar una cuenta por su número en la lista de clientes
    private static Cuenta buscarCuenta(List<Cliente> clientes, String numeroCuenta) {
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getCuentas()) {
                if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                    return cuenta;
                }
            }
        }
        return null;
    }
	
	

}
