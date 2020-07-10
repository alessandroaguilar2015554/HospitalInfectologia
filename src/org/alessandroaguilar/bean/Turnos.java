/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alessandroaguilar.bean;

import java.util.Date;

/**
 *
 * @author aless
 */
public class Turnos {
    private int codigoTurno;
    private int codigoMedicoEspecialidad;
    private int codigoResponsableTurno;
    private int codigoPaciente;
    private Date fechaTurno;
    private Date fechaCita;
    private Double valorCita;

    public Turnos(int codigoTurno, int codigoMedicoEspecialidad, int codigoResponsableTurno, int codigoPaciente, Date fechaTurno, Date fechaCita, Double valorCita) {
        this.codigoTurno = codigoTurno;
        this.codigoMedicoEspecialidad = codigoMedicoEspecialidad;
        this.codigoResponsableTurno = codigoResponsableTurno;
        this.codigoPaciente = codigoPaciente;
        this.fechaTurno = fechaTurno;
        this.fechaCita = fechaCita;
        this.valorCita = valorCita;
    }
    
    
      public Turnos() {
       
    }

    public int getCodigoTurno() {
        return codigoTurno;
    }

    public void setCodigoTurno(int codigoTurno) {
        this.codigoTurno = codigoTurno;
    }

    public int getCodigoMedicoEspecialidad() {
        return codigoMedicoEspecialidad;
    }

    public void setCodigoMedicoEspecialidad(int codigoMedicoEspecialidad) {
        this.codigoMedicoEspecialidad = codigoMedicoEspecialidad;
    }

    public int getCodigoResponsableTurno() {
        return codigoResponsableTurno;
    }

    public void setCodigoResponsableTurno(int codigoResponsableTurno) {
        this.codigoResponsableTurno = codigoResponsableTurno;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public Date getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(Date fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Double getValorCita() {
        return valorCita;
    }

    public void setValorCita(Double valorCita) {
        this.valorCita = valorCita;
    }
    
    
    
    
    
   @Override
    public String toString() {
        return ""  + codigoTurno ;
    } 
    
  
    
}
