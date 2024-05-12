package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.utils.Cliente;   

import ar.edu.utn.frbb.tup.utils.Cuenta;
import ar.edu.utn.frbb.tup.utils.Movimiento;
import ar.edu.utn.frbb.tup.Banco;
import ar.edu.utn.frbb.tup.utils.TipoPersona;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CuentaInputProcessor extends BaseInputProcessor {

    private static final Scanner scanner = new Scanner(System.in);

    // Método para crear una cuenta bancaria.
    public Cuenta crearCuenta(List<Cliente> clientes) {
    	// Para crear una cuenta si o si el DNI debe coincidir con un cliente ya generado.
        long dniCliente;
        do {
            System.out.print("Ingrese el DNI del cliente para asociar la cuenta (8 dígitos numéricos): ");
            while (!scanner.hasNextLong()) {
                System.out.println("Por favor, ingrese un número válido para el DNI."); // En caso de escribir mal el DNI.
                scanner.next(); // Consumir el valor inválido
            }
            dniCliente = scanner.nextLong();
            // Validación al momento de ingresar el DNI.
        } while (dniCliente < 0 || String.valueOf(dniCliente).length() != 8);

        Cliente clienteAsociado = null;
        for (Cliente cliente : clientes) {
            if (cliente.getDni() == dniCliente) {
                clienteAsociado = cliente;
                break;
            }
        }

        // Si el DNI esta asociado a un cliente, entonces continua al siguiente paso.
        // Se consulta tipo de cuenta: caja ahorro o cta. corriente.
        if (clienteAsociado != null) {
            System.out.print("Ingrese el tipo de cuenta (A para caja de ahorro, C para cuenta corriente): ");
            String tipoCuentaStr = scanner.next().toUpperCase();

            // Validación del tipo de cuenta ingresado.
            Cuenta.TipoCuenta tipoCuenta;
            if (tipoCuentaStr.equals("A")) {
                tipoCuenta = Cuenta.TipoCuenta.CAJA_AHORRO;
            } else if (tipoCuentaStr.equals("C")) {
                tipoCuenta = Cuenta.TipoCuenta.CUENTA_CORRIENTE;
            } else {
                System.out.println("Tipo de cuenta inválido.");
                return null;
            }

            // Se consulta el saldo inicial que tendra la cuenta bancaria.
            double saldoInicial;
            do {
                System.out.print("Ingrese el saldo inicial de la cuenta: ");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Por favor, ingrese un monto válido para el saldo.");
                    scanner.next(); 
                }
                saldoInicial = scanner.nextDouble();
            } while (saldoInicial < 0); // Mientras el saldo sea mayor a 0.

            scanner.nextLine();
        
            // Se solicita un numero de cuenta. Este número debe ser unico. Es el número que identifica cada tarjeta.
            String numeroCuenta;
            do {
                System.out.print("Ingrese el número de cuenta (6 dígitos numéricos): ");
                numeroCuenta = scanner.nextLine();
            } while (numeroCuenta.length() != 6 || !numeroCuenta.matches("\\d+"));

            Cuenta cuenta = new Cuenta(numeroCuenta, saldoInicial, tipoCuenta, clienteAsociado);
            clienteAsociado.addCuenta(cuenta);

            System.out.println("Cuenta creada y asociada correctamente al cliente: " + clienteAsociado.getNombre() + " " + clienteAsociado.getApellido());
            return cuenta;
            
          // En caso de no encontrar ningun cliente con el DNI que se ingreso, devuelve un mensaje de error.
        } else {
            System.out.println("Error. No se ha encontrado ningún cliente con este DNI.");
            return null;
        }
    }

    // Método para realizar un deposito de dinero.
    public static void realizarDeposito(List<Cliente> clientes) {
        System.out.print("Ingrese el número de cuenta en el que desea realizar el depósito: ");
        String numeroCuenta = scanner.nextLine();

        Cuenta cuentaSeleccionada = null;
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getCuentas()) {
                if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                    cuentaSeleccionada = cuenta;
                    break;
                }
            }
            if (cuentaSeleccionada != null) {
                break;
            }
        }

        if (cuentaSeleccionada != null) {
            double monto;
            do {
                System.out.print("Ingrese el monto a depositar: ");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Por favor, ingrese un monto válido para el depósito.");
                    scanner.next(); // Consumir el valor inválido
                }
                monto = scanner.nextDouble();
            } while (monto < 0);

            scanner.nextLine(); // Consumir el salto de línea

            cuentaSeleccionada.depositar(monto);
            
            cuentaSeleccionada.setUltimaOperacion(LocalDateTime.now()); // Actualizar la fecha y hora de la última operación
            cuentaSeleccionada.agregarMovimiento(new Movimiento(Movimiento.TipoMovimiento.DEPOSITO, monto)); // Agregar el movimiento de retiro
            
            System.out.println("Depósito realizado exitosamente. Nuevo saldo: " + cuentaSeleccionada.getSaldo());
        } else {
            System.out.println("No se encontró ninguna cuenta con ese número.");
        }
    }

    // Método para realizar un retiro de dinero.
    public static void realizarRetiro(List<Cliente> clientes) {
        System.out.print("Ingrese el número de cuenta desde el que desea realizar el retiro: ");
        String numeroCuenta = scanner.nextLine();

        Cuenta cuentaSeleccionada = null;
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getCuentas()) {
                if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                    cuentaSeleccionada = cuenta;
                    break;
                }
            }
            if (cuentaSeleccionada != null) {
                break;
            }
        }

        if (cuentaSeleccionada != null) {
            double monto;
            do {
                System.out.print("Ingrese el monto a retirar: ");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Por favor, ingrese un monto válido para el retiro.");
                    scanner.next(); // Consumir el valor inválido
                }
                monto = scanner.nextDouble();
            } while (monto < 0);

            scanner.nextLine(); // Consumir el salto de línea

            if (cuentaSeleccionada.retirar(monto)) {
            	
            	cuentaSeleccionada.setUltimaOperacion(LocalDateTime.now()); // Actualizar la fecha y hora de la última operación
                cuentaSeleccionada.agregarMovimiento(new Movimiento(Movimiento.TipoMovimiento.RETIRO, monto)); // Agregar el movimiento de retiro
                
                System.out.println("Retiro realizado exitosamente! Nuevo saldo: " + cuentaSeleccionada.getSaldo());
            } 
            else {
                System.out.println("Error. Saldo insuficiente para realizar el retiro.");
            }
        } 
        else {
            System.out.println("No se encontró ninguna cuenta con ese número.");
        }
    }

// Método para consultar el saldo de una cuenta bancaria.
public static void consultarSaldo(List<Cliente> clientes) {
    System.out.print("Ingrese el número de cuenta que desea consultar: ");
    String numeroCuenta = scanner.nextLine();

    // Se busca la cuenta correspondiente al número de cuenta ingresado.
    Cuenta cuentaSeleccionada = null;
    for (Cliente cliente : clientes) {
        for (Cuenta cuenta : cliente.getCuentas()) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                cuentaSeleccionada = cuenta;
                break;
            }
        }
        if (cuentaSeleccionada != null) {
            break;
        }
    }

    // Si se encuentra la cuenta, mostrar su saldo.
    if (cuentaSeleccionada != null) {
        System.out.println("Saldo de la cuenta " + numeroCuenta + ": " + cuentaSeleccionada.getSaldo());
    } else {
        System.out.println("No se encontró ninguna cuenta con ese número.");
    }
 }

//Método para ver todos los movimientos de dinero de una cuenta.
public static void mostrarMovimientos(List<Cliente> clientes) {
 System.out.print("Ingrese el número de cuenta para ver los movimientos: ");
 String numeroCuenta = scanner.nextLine();

 // Buscar la cuenta correspondiente al número de cuenta ingresado
 Cuenta cuentaSeleccionada = null;
 for (Cliente cliente : clientes) {
     for (Cuenta cuenta : cliente.getCuentas()) {
         if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
             cuentaSeleccionada = cuenta;
             break;
         }
     }
     if (cuentaSeleccionada != null) {
         break;
     }
 }

 // Si se encuentra la cuenta, mostrar los movimientos
 if (cuentaSeleccionada != null) {
     System.out.println("Movimientos de dinero para la cuenta " + numeroCuenta + ":");

     // Definir el formato deseado para la fecha y hora
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

     // Iterar sobre los movimientos de la cuenta y mostrarlos
     for (Movimiento movimiento : cuentaSeleccionada.getMovimientos()) {
         System.out.println("Tipo de movimiento: " + movimiento.getTipoMovimiento());
         System.out.println("Monto: " + movimiento.getMonto());
         // Formatear la fecha y hora en el formato deseado
         String fechaHoraFormateada = movimiento.getFechaHora().format(formatter);
         System.out.println("Fecha y hora: " + fechaHoraFormateada);
         System.out.println("-------------------------");
     }
 } else {
     System.out.println("No se encontró ninguna cuenta con ese número.");
 }
}

}







