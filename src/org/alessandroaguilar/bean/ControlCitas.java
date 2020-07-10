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
public class ControlCitas {
    private int codigoControlCita;
    private int codigoMedico;
    private int codigoPaciente;
    private Date fecha;
    private String horaInicio;
    private String horaFin;

    public ControlCitas(int codigoControlCita, int codigoMedico, int codigoPaciente, Date fecha, String horaInicio, String horaFin) {
        this.codigoControlCita = codigoControlCita;
        this.codigoMedico = codigoMedico;
        this.codigoPaciente = codigoPaciente;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
    
    public ControlCitas(){
        
    }

    public int getCodigoControlCita() {
        return codigoControlCita;
    }

    public void setCodigoControlCita(int codigoControlCita) {
        this.codigoControlCita = codigoControlCita;
    }

    public int getCodigoMedico() {
        return codigoMedico;
    }

    public void setCodigoMedico(int codigoMedico) {
        this.codigoMedico = codigoMedico;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

   @Override
    public String toString() {
        return ""  + codigoControlCita ;
    }



  
    
}

