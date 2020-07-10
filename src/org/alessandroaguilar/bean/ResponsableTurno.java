/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alessandroaguilar.bean;

/**
 *
 * @author aless
 */
public class ResponsableTurno {
    private int codigoResponsableTurno;
    private String nombreResponsable;
    private String apellidosResponsable;
    private String telefonoPersonal;
    private int codigoArea;
    private int codigoCargo;

    public ResponsableTurno(int codigoResponsableTurno, String nombreResponsable, String apellidosResponsable, String telefonoPersonal, int codigoArea, int codigoCargo) {
        this.codigoResponsableTurno = codigoResponsableTurno;
        this.nombreResponsable = nombreResponsable;
        this.apellidosResponsable = apellidosResponsable;
        this.telefonoPersonal = telefonoPersonal;
        this.codigoArea = codigoArea;
        this.codigoCargo = codigoCargo;
    }
    
    public ResponsableTurno() {
        
    }

    public int getCodigoResponsableTurno() {
        return codigoResponsableTurno;
    }

    public void setCodigoResponsableTurno(int codigoResponsableTurno) {
        this.codigoResponsableTurno = codigoResponsableTurno;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getApellidosResponsable() {
        return apellidosResponsable;
    }

    public void setApellidosResponsable(String apellidosResponsable) {
        this.apellidosResponsable = apellidosResponsable;
    }

    public String getTelefonoPersonal() {
        return telefonoPersonal;
    }

    public void setTelefonoPersonal(String telefonoPersonal) {
        this.telefonoPersonal = telefonoPersonal;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public int getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(int codigoCargo) {
        this.codigoCargo = codigoCargo;
    }
    
    
    
    @Override
    public String toString() {
        return ""  + codigoResponsableTurno ;
    }
    
    
    
}
