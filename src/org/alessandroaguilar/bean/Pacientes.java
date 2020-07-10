/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alessandroaguilar.bean;

import java.util.Date;

/**
 *
 * @author programacion
 */
public class Pacientes {
     private int codigoPaciente;
     private String DPI;
     private String apellidos;
     private String nombres;
     private Date fechaNacimiento;
     private int edad;
     private String direccion;
     private String ocupacion;
     private String sexo;
     
     
    public Pacientes(int codigoPaciente, String DPI, String apellidos, String nombres, Date fechaNacimiento, int edad, String direccion, String ocupacion,  String sexo) {
        this.codigoPaciente = codigoPaciente;
        this.DPI =  DPI;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.direccion = direccion;
        this.ocupacion = ocupacion;
        this.sexo = sexo;
    } 
    public Pacientes() {
       
    }


    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public String getDPI() {
        return DPI;
    }

    public void setDPI(String DPI) {
        this.DPI = DPI;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }


  
    
     
     @Override
    public String toString() {
        return ""  + codigoPaciente ;
    }
       
     
}
