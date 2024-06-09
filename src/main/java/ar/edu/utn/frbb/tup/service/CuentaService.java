package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotSupportedExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

public class CuentaService {

    private CuentaDao cuentaDao;
    private ClienteService clienteService;

    public CuentaService(CuentaDao cuentaDao, ClienteService clienteService) {
        this.cuentaDao = cuentaDao;
        this.clienteService = clienteService;
    }

    public CuentaService() {
    }

    public void darDeAltaCuenta(Cuenta cuenta, long dni) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, ClienteAlreadyExistsException {
        Cuenta cuentaExistente = cuentaDao.find(cuenta.getNumeroCuenta());
        if (cuentaExistente != null) {
            throw new CuentaAlreadyExistsException("Cuenta ya existe con el número de cuenta: " + cuenta.getNumeroCuenta());
        }

        Cliente cliente = clienteService.buscarClientePorDni(dni);
        for (Cuenta c : cliente.getCuentas()) {
            if (c.getTipoCuenta() == cuenta.getTipoCuenta()) {
                throw new TipoCuentaAlreadyExistsException("El cliente ya tiene una cuenta de tipo: " + cuenta.getTipoCuenta());
            }
        }

        clienteService.agregarCuenta(cuenta, dni);
        cuentaDao.save(cuenta);
    }

    public void validarCuentaSoportada(TipoCuenta tipoCuenta) throws CuentaNotSupportedExcepcion {
        if (!tipoDeCuentaSoportada(tipoCuenta.toString())) {
            throw new CuentaNotSupportedExcepcion("Tipo de cuenta no soportado: " + tipoCuenta);
        }
    }

    public Cuenta find(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'find'");
    }

    public boolean tipoDeCuentaSoportada(String tipoCuenta) {
        if (tipoCuenta == null) {
            return false;
        }
        switch (tipoCuenta) {
            case "CA$":
            case "CC$":
            case "CAU$S":
                return true;
            default:
                return false;
        }
    }
}