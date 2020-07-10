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
public class ContactoUrgencia {
    private int codigoContactoUrgencia;
    private int codigoPaciente;
    private String nombres;
    private String apellidos;
    private String numeroContacto;

    public ContactoUrgencia(int codigoContactoUrgencia, int codigoPaciente, String nombres, String apellidos, String numeroContacto) {
        this.codigoContactoUrgencia = codigoContactoUrgencia;
        this.codigoPaciente = codigoPaciente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.numeroContacto = numeroContacto;
    }
    
    public ContactoUrgencia (){
    
    
}    

    public int getCodigoContactoUrgencia() {
        return codigoContactoUrgencia;
    }

    public void setCodigoContactoUrgencia(int codigoContactoUrgencia) {
        this.codigoContactoUrgencia = codigoContactoUrgencia;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }
    
 
    
    
}
