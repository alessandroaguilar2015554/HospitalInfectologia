/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alessandroaguilar.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alessandroaguilar.bean.ControlCitas;
import org.alessandroaguilar.bean.Medico;
import org.alessandroaguilar.bean.Pacientes;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author programacion
 */
public class ControlCitasController implements Initializable {
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
      private operaciones tipoOperaciones = operaciones.Ninguno;
      private ObservableList <ControlCitas> listarControlCitas;
      private ObservableList <Pacientes> listarPacientes;
      private ObservableList <Medico> listarMedico;
      private Principal escenarioPrincipal;
      
       private DatePicker dtpfecha;
      @FXML private GridPane grpfecha;
      
      @FXML private TextField txthoraInicio;
      @FXML private TextField txthoraSalida;
      
      @FXML private ComboBox cmbcodigoCita;
      @FXML private ComboBox cmbcodigoPaciente;
      @FXML private ComboBox cmbcodigoMedico;
      
      @FXML private TableView tblcontrolCitas;
      @FXML private TableColumn colcodigoMedico;
      @FXML private TableColumn colcodigoPaciente;
      @FXML private TableColumn colcodigoCita;
      @FXML private TableColumn colfecha;
      @FXML private TableColumn colhoraInicio;
      @FXML private TableColumn colhoraSalida;
      
      @FXML private Button btnagregar;
      @FXML private Button btneditar;
      @FXML private Button btneliminar;
      @FXML private Button btnreporte; 
      @FXML private Button btnreporteGeneral;  
      
      
      
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
        //cmbcodigoCita.setItems(getControlCitas());
        cmbcodigoPaciente.setItems(getPacientes());
        cmbcodigoMedico.setItems(getMedicos());
        
        dtpfecha = new DatePicker (Locale.ENGLISH);
        dtpfecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpfecha.getCalendarView().todayButtonTextProperty().set("Today");
        grpfecha.add(dtpfecha,0,0);
        
    }
    
    
     public ObservableList <ControlCitas> getControlCitas(){
        ArrayList<ControlCitas> lista = new ArrayList<ControlCitas>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarControlCitas}");
        ResultSet resultado = procedimiento.executeQuery();
     
         while(resultado.next()){
         lista.add(new ControlCitas(resultado.getInt("codigoControlCita"),resultado.getInt("codigoMedico"),resultado.getInt("codigoPaciente"),resultado.getDate("fecha"),resultado.getString("horaInicio"),resultado.getString("horaFin")));
     }
     
    }catch (SQLException e){
        e.printStackTrace();
    }
     return listarControlCitas = FXCollections.observableList(lista);   
    }
    
     
      public ObservableList <Medico> getMedicos(){
        ArrayList<Medico> lista = new ArrayList<Medico>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarMedicos}");
        ResultSet resultado = procedimiento.executeQuery();
     
        while(resultado.next()){
        lista.add(new Medico(resultado.getInt("codigoMedico"),resultado.getInt("licenciaMedica"),resultado.getString("nombres"),resultado.getString("apellidos"),resultado.getDate("horaEntrada"),resultado.getDate("horaSalida"),resultado.getString("sexo")));
       
     }
     
    }catch (SQLException e){
        e.printStackTrace();
    }
     return listarMedico = FXCollections.observableList(lista);   
    }
    
    
       public ObservableList <Pacientes> getPacientes(){
        ArrayList<Pacientes> Lista = new ArrayList<Pacientes>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarPacientes}");
        ResultSet resultado = procedimiento.executeQuery();
        
        while(resultado.next()){
        Lista.add(new Pacientes(resultado.getInt("codigoPaciente"),resultado.getString("DPI"),resultado.getString("apellidos"),resultado.getString("nombres"),resultado.getDate("fechaNacimiento"),resultado.getInt("edad"),resultado.getString("direccion"), resultado.getString("ocupacion"),resultado.getString("sexo")));
        
        }
        
    }catch(Exception e){
        e.printStackTrace();
    }    
        return listarPacientes = FXCollections.observableList(Lista);
    }
    
    
    
    public void cargarDatos(){
        tblcontrolCitas.setItems(getControlCitas());
        
        colcodigoCita.setCellValueFactory(new PropertyValueFactory <ControlCitas,Integer>("codigoControlCita"));
        colcodigoMedico.setCellValueFactory(new PropertyValueFactory <ControlCitas,Integer>("codigoMedico"));
        colcodigoPaciente.setCellValueFactory(new PropertyValueFactory <ControlCitas,Integer>("codigoPaciente"));
        colfecha.setCellValueFactory(new PropertyValueFactory <ControlCitas,Date>("fecha"));
        colhoraInicio.setCellValueFactory(new PropertyValueFactory <ControlCitas,String>("horaInicio"));
        colhoraSalida.setCellValueFactory(new PropertyValueFactory <ControlCitas,String>("horaFin"));
        
    }
    
       
    public ControlCitas buscarControlCitas(int codigoControlCita){
        ControlCitas resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_ControlCitas(?) }");
            procedimiento.setInt(1, codigoControlCita);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new ControlCitas(registro.getInt("codigoControlCita"),registro.getInt("codigoMedico"),registro.getInt("codigoPaciente"),registro.getDate("fecha"),registro.getString("horaInicio"),registro.getString("horaFin"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
    
    
    public Pacientes buscarPacientes(int codigoPaciente){
        Pacientes resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Pacientes(?) }");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Pacientes(registro.getInt("codigoPaciente"),registro.getString("DPI"),registro.getString("apellidos"),registro.getString("nombres"),registro.getDate("fechaNacimiento"),registro.getInt("edad"),registro.getString("direccion"),registro.getString("ocupacion"),registro.getString("sexo"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
    
    
    public Medico buscarMedico(int codigoMedico){
        Medico resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Medicos(?) }");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Medico(registro.getInt("codigoMedico"),registro.getInt("licenciaMedica"),registro.getString("nombres"),registro.getString("apellidos"),registro.getDate("horaEntrada"),registro.getDate("horaSalida"),registro.getString("sexo"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
    
    
    
    public void seleccionar(){
        cmbcodigoCita.getSelectionModel().select(buscarControlCitas(((ControlCitas)tblcontrolCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita()));
        cmbcodigoMedico.getSelectionModel().select(buscarMedico(((ControlCitas)tblcontrolCitas.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        cmbcodigoPaciente.getSelectionModel().select(buscarPacientes(((ControlCitas)tblcontrolCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        dtpfecha.selectedDateProperty().set(((ControlCitas)tblcontrolCitas.getSelectionModel().getSelectedItem()).getFecha());
        txthoraInicio.setText(((ControlCitas) tblcontrolCitas.getSelectionModel().getSelectedItem()).getHoraInicio());
        txthoraSalida.setText(((ControlCitas) tblcontrolCitas.getSelectionModel().getSelectedItem()).getHoraFin());
      
    }
    
    
     public void edit(){
            switch(tipoOperaciones){
                case Ninguno:
                    if(tblcontrolCitas.getSelectionModel().getSelectedItem() !=null){
                        btneditar.setText("Actualizar");
                        btnreporte.setText("Cancelar");
                        tipoOperaciones = operaciones.Actualizar;
                        btnagregar.setDisable(true);
                        btneliminar.setDisable(true);
                        activar();
                    }
                  break;
                  
                case Actualizar:
                     actualizar();
                     btneditar.setText("Editar");
                     btnreporte.setText("Reporte");
                     tipoOperaciones = operaciones.Ninguno;
                     btnagregar.setDisable(false);
                     btneliminar.setDisable(false);
                     desactivar();
                     cargarDatos();
                     
                     break;
            }
    }
    
     public void nuevo(){
        switch(tipoOperaciones){
            case Ninguno:
                activar();
                limpiar();
                btnagregar.setText("Guardar");
                btneliminar.setText("Cancelar");
                btneditar.setDisable(true);
                btnreporte.setDisable(true);
                tipoOperaciones = operaciones.Guardar;
            break;
            case Guardar:
                ingresar();
                desactivar();
                btnagregar.setText("Nuevo");
                btneliminar.setText("Eliminar");
                btneditar.setDisable(false);
                btnreporte.setDisable(false);
                tipoOperaciones = operaciones.Ninguno;
                cargarDatos();
            break;
        }
        
    }
     
     public void activar(){
        cmbcodigoMedico.setDisable(false);
        cmbcodigoPaciente.setDisable(false);
        dtpfecha.setDisable(false);
        txthoraInicio.setDisable(false);
        txthoraSalida.setDisable(false);
        
      //cmbcodigoMedico.setEditable(true);
     //cmbcodigoPaciente.setEditable(true);
        txthoraInicio.setEditable(true);
        txthoraSalida.setEditable(true);            
    }
     
     public void limpiar(){
        txthoraInicio.setText("");
        txthoraSalida.setText("");
    }
    
    
      public void cancelar(){
            btnagregar.setText("Nuevo");
            btneliminar.setText("Eliminar");
            btnreporte.setDisable(false);
            btneditar.setDisable(false);
            tipoOperaciones = operaciones.Nuevo;
        }
        
        
    public void desactivar(){
       cmbcodigoCita.setDisable(true);
       cmbcodigoMedico.setDisable(true);
       cmbcodigoPaciente.setDisable(true);
       dtpfecha.setDisable(true);
       txthoraInicio.setDisable(true);
       txthoraSalida.setDisable(true);
       
        
       cmbcodigoCita.setEditable(false);
       cmbcodigoMedico.setEditable(false);
       cmbcodigoPaciente.setEditable(false);
       txthoraInicio.setEditable(false);
       txthoraSalida.setEditable(false);
               
    }
    
    
    public void ingresar(){
        ControlCitas registro = new ControlCitas();
        registro.setCodigoMedico(((Medico)cmbcodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
        registro.setCodigoPaciente(((Pacientes)cmbcodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setFecha(dtpfecha.getSelectedDate());
        registro.setHoraInicio(txthoraInicio.getText());
        registro.setHoraFin(txthoraSalida.getText());
        
          

        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarControlCitas (?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getCodigoPaciente());
            procedimiento.setDate(3, new java.sql.Date (registro.getFecha().getTime()));
            procedimiento.setString(4, registro.getHoraInicio());
            procedimiento.setString(5, registro.getHoraFin());
            
            
            procedimiento.execute();
            listarControlCitas.add(registro);
            
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        
    }
    
    
     public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdateControlCitas(?,?,?,?,?,?)}");
            ControlCitas registro = (ControlCitas)tblcontrolCitas.getSelectionModel().getSelectedItem();
            registro.setCodigoMedico(((Medico)cmbcodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            registro.setCodigoPaciente(((Pacientes)cmbcodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            registro.setFecha(dtpfecha.getSelectedDate());
            registro.setHoraInicio(txthoraInicio.getText());
            registro.setHoraFin(txthoraSalida.getText());
           
           
            
            procedimiento.setInt(1, registro.getCodigoControlCita());
            procedimiento.setInt(2, registro.getCodigoMedico());
            procedimiento.setInt(3, registro.getCodigoPaciente());
            procedimiento.setDate(4,new java.sql.Date (registro.getFecha().getTime()));
            procedimiento.setString(5, registro.getHoraInicio());
            procedimiento.setString(6, registro.getHoraFin());
            
           
            procedimiento.execute();
           listarControlCitas.add(registro);            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }   
    }
    
    
    public void Eliminar(){
        if(tipoOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipoOperaciones = operaciones.Eliminar;
        if(tblcontrolCitas.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Control Citas", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarControlCitas(?)}");
                    procedimiento.setInt(1,((ControlCitas)tblcontrolCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita());
                    procedimiento.execute();
                     listarControlCitas.remove(tblcontrolCitas.getSelectionModel().getSelectedIndex());
                    limpiar();
                    cargarDatos();
                
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        
            else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
            }
        }
    
    
    
    public void imprimirReporte(){
        if(tblcontrolCitas.getSelectionModel().getSelectedItem() != null){
            int codigoControlCita = ((ControlCitas) tblcontrolCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita();
            Map parametros = new HashMap();
            parametros.put("p_codigoControlCita",codigoControlCita);
            GenerarReporte.mostrarReporte("reporteControlCitas.jasper","reporteControlCitas", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
    
    
     public void imprimirReporteGeneral(){
        if(tblcontrolCitas.getSelectionModel()//.getSelectedItem()
                != null){
           // int codigoControlCita = ((ControlCitas) tblcontrolCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita();
            Map parametros = new HashMap();
            //parametros.put("p_codigoControlCita",codigoControlCita);
            GenerarReporte.mostrarReporte("reporteGeneralControlCitas.jasper","reporteGeneralControlCitas", parametros);
            
        }else{
               // JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
   
    
    
   
     public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void MenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    
}

