/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alessandroaguilar.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alessandroaguilar.bean.Especialidad;
import org.alessandroaguilar.bean.Horario;
import org.alessandroaguilar.bean.Medico;
import org.alessandroaguilar.bean.MedicoEspecialidad;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author aless
 */
public class MedicoEspecialidadController implements Initializable{
private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
      private operaciones tipoOperaciones = operaciones.Ninguno;
      private ObservableList <MedicoEspecialidad> listarMedicoEspecialidad;
      private ObservableList <Horario> listarHorario;
      private ObservableList <Medico> listarMedico;
      private ObservableList <Especialidad> listarEspecialidad;
      private Principal escenarioPrincipal;
      
      
      @FXML private ComboBox cmbcodigoMedicoEspecialidad;
      @FXML private ComboBox cmbcodigoMedico;
      @FXML private ComboBox cmbcodigoEspecialidad;
      @FXML private ComboBox cmbcodigoHorario;
      
     @FXML private TableView tblespecialidades;
     @FXML private TableColumn colcodigoMe;
     @FXML private TableColumn colcodigoMedico;
     @FXML private TableColumn colcodigoEspecialidad;
     @FXML private TableColumn colcodigoHorario;
      
    @FXML private Button btnagregar;
    @FXML private Button btneditar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte; 
     @FXML private Button btnreporteGeneral;
      
     
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         cargarDatos();
        //cmbcodigoMedicoEspecialidad.setItems(getMedicosEspecialidad());
        cmbcodigoEspecialidad.setItems(getEspecialidad());
        cmbcodigoMedico.setItems(getMedicos());
        cmbcodigoHorario.setItems(getHorarios());
    }
    
    
     public void cargarDatos(){
        tblespecialidades.setItems(getMedicosEspecialidad());
        colcodigoMe.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad,Integer>("codigoMedicoEspecialidad"));
        colcodigoMedico.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad,Integer>("codigoMedico"));
        colcodigoEspecialidad.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad,Integer>("codigoEspecialidad"));
        colcodigoHorario.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad,Integer>("codigoHorario"));
        

    }
     
      public ObservableList <MedicoEspecialidad> getMedicosEspecialidad(){
        ArrayList<MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarMedico_Especialidad}");
     ResultSet resultado = procedimiento.executeQuery();
     
     while(resultado.next()){
     lista.add(new MedicoEspecialidad(resultado.getInt("codigoMedicoEspecialidad"),resultado.getInt("codigoMedico"),resultado.getInt("codigoEspecialidad"),resultado.getInt("codigoHorario")));
       
     }
     
    }catch (SQLException e){
        e.printStackTrace();
    }
     return listarMedicoEspecialidad = FXCollections.observableList(lista);   
    }
      
    
      
     public ObservableList <Especialidad> getEspecialidad(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarEspecialidades}");
     ResultSet resultado = procedimiento.executeQuery();
     
     while(resultado.next()){
     lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),resultado.getString("nombreEspecialidad")));
       
     }
     
    }catch (SQLException e){
        e.printStackTrace();
    }
     return listarEspecialidad = FXCollections.observableList(lista);   
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
    
    
      public ObservableList <Horario> getHorarios(){
        ArrayList<Horario> lista = new ArrayList<Horario>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarHorarios}");
     ResultSet resultado = procedimiento.executeQuery();
     
     while(resultado.next()){
    lista.add(new Horario(resultado.getInt("codigoHorario"),resultado.getDate("horarioInicio"),resultado.getDate("horarioSalida"),resultado.getInt("lunes"),resultado.getInt("martes"),resultado.getInt("miercoles"),resultado.getInt("jueves"),resultado.getInt("viernes")));
       
     }
     
    }catch (SQLException e){
        e.printStackTrace();
    }
     return listarHorario = FXCollections.observableList(lista);   
    }
      
      
     public void edit(){
            switch(tipoOperaciones){
                case Ninguno:
                    if(tblespecialidades.getSelectionModel().getSelectedItem() !=null){
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
     
     
     
      public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdateMedico_Especialidad(?,?,?,?)}");
            MedicoEspecialidad registro = (MedicoEspecialidad) tblespecialidades.getSelectionModel().getSelectedItem();
            
            //registro.setCodigoMedicoEspecialidad(Integer.parseInt(cmb.getSelectionModel().getSelectedItem().toString()));
            //registro.setCodigoMedicoEspecialidad(((MedicoEspecialidad)cmbcodigoMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
            registro.setCodigoMedico(((MedicoEspecialidad)cmbcodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            registro.setCodigoEspecialidad(((MedicoEspecialidad)cmbcodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
            registro.setCodigoHorario(((MedicoEspecialidad)cmbcodigoHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
            
            
            procedimiento.setInt(1,registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(2,registro.getCodigoMedico());
            procedimiento.setInt(3,registro.getCodigoEspecialidad());
            procedimiento.setInt(4,registro.getCodigoHorario());
           
            procedimiento.execute();
            listarMedicoEspecialidad.add(registro);   
         }catch (SQLException e){
        e.printStackTrace();
            
        }
        
    }
      
      
     public MedicoEspecialidad buscarMedicoEspecialidad(int codigoMedicoEspecialidad){
        MedicoEspecialidad resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Medico_Especialidad(?) }");
            procedimiento.setInt(1, codigoMedicoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new MedicoEspecialidad(registro.getInt("codigoMedicoEspecialidad"),registro.getInt("codigoMedico"),registro.getInt("codigoEspecialidad"),registro.getInt("codigoHorario")) ;
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
    
     
     public void seleccionar(){
        //cmbcodigoMedicoEspecialidad.getSelectionModel().select(buscarMedicoEspecialidad(((MedicoEspecialidad)tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad()));
        cmbcodigoMedico.getSelectionModel().select(buscarMedicoEspecialidad(((MedicoEspecialidad)tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        cmbcodigoEspecialidad.getSelectionModel().select(buscarMedicoEspecialidad(((MedicoEspecialidad)tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
        cmbcodigoHorario.getSelectionModel().select(buscarMedicoEspecialidad(((MedicoEspecialidad)tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoHorario()));
    
        
        
    }
     
     public void nuevo(){
        switch(tipoOperaciones){
            case Ninguno:
                activar();
                //limpiar();
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
        cmbcodigoMedicoEspecialidad.setDisable(false);
        cmbcodigoMedico.setDisable(false);
        cmbcodigoEspecialidad.setDisable(false);
        cmbcodigoHorario.setDisable(false);
        
        
        cmbcodigoMedicoEspecialidad.setEditable(true);
        cmbcodigoMedico.setDisable(false);
        cmbcodigoEspecialidad.setDisable(false);
        cmbcodigoHorario.setDisable(false);
        
                
    }
      
      
      public void ingresar(){
        MedicoEspecialidad registro = new MedicoEspecialidad();
           // registro.setCodigoMedico(((MedicoEspecialidad)cmbcodigoMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
            registro.setCodigoMedico(((Medico)cmbcodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            registro.setCodigoEspecialidad(((Especialidad)cmbcodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
            registro.setCodigoHorario(((Horario)cmbcodigoHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedico_Especialidad(?,?,?) }");
           // procedimiento.setInt(1, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(1, registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getCodigoEspecialidad());
            procedimiento.setInt(3, registro.getCodigoHorario());
            
            procedimiento.execute();
            listarMedicoEspecialidad.add(registro);
            
            
        }catch(SQLException e){
                e.printStackTrace();
        }
        
        
        
    }
      
     
      public void Eliminar(){
        if(tipoOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipoOperaciones = operaciones.Eliminar;
        if(tblespecialidades.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Medico Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedico_Especialidad(?)}");
                    procedimiento.setInt(1,((MedicoEspecialidad)tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
                    procedimiento.execute();
                     listarMedicoEspecialidad.remove(tblespecialidades.getSelectionModel().getSelectedIndex());
                   // limpiar();
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
    
     public void cancelar(){
            btnagregar.setText("Nuevo");
            btneliminar.setText("Eliminar");
            btnreporte.setDisable(false);
            btneditar.setDisable(false);
            tipoOperaciones = operaciones.Nuevo;
        }
        
        
    public void desactivar(){
        cmbcodigoMedicoEspecialidad.setDisable(true);
        cmbcodigoMedico.setDisable(true);
        cmbcodigoEspecialidad.setDisable(true);
        cmbcodigoHorario.setDisable(true);
        
        
        cmbcodigoMedicoEspecialidad.setEditable(false);
        cmbcodigoMedico.setEditable(false);
        cmbcodigoEspecialidad.setEditable(false);
        cmbcodigoHorario.setEditable(false);
        
                
    }
    
    
     public void imprimirReporte(){
        if(tblespecialidades.getSelectionModel().getSelectedItem() != null){
            int codigoMedicoEspecialidad = ((MedicoEspecialidad) tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad();
            Map parametros = new HashMap();
            parametros.put("p_codigoMedicoEspecialidad",codigoMedicoEspecialidad);
            GenerarReporte.mostrarReporte("reporteMedicoEspecialidad.jasper","reporteMedicoEspecialidad", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
     
     
      public void imprimirReporteGeneral(){
        if(tblespecialidades.getSelectionModel()//.getSelectedItem()
                != null){
            //int codigoMedicoEspecialidad = ((MedicoEspecialidad) tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad();
            Map parametros = new HashMap();
            //parametros.put("p_codigoMedicoEspecialidad",codigoMedicoEspecialidad);
            GenerarReporte.mostrarReporte("reporteMedicoEspecialidadGeneral.jasper","reporteMedicoEspecialidadGeneral", parametros);
            
        }else{
                //JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
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
