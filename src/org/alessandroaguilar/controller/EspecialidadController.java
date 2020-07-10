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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alessandroaguilar.bean.Especialidad;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author aless
 */
public class EspecialidadController implements Initializable {
     private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
      private operaciones tipoOperaciones = operaciones.Ninguno;
      private ObservableList <Especialidad> listarEspecialidad;
     private Principal escenarioPrincipal;
     
     
     @FXML private ComboBox cmbcodigoEspecialidad;
     @FXML private TextField txtnombreEspecialidad;
     
     @FXML private Button btnagregar;
    @FXML private Button btneditar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte; 
    
    @FXML private Button btnreporteGeneral;
    
    @FXML private TableView tblespecialidades;
    @FXML private TableColumn colcodigoEspecialidad;
    @FXML private TableColumn colnombreEspecialidad;
     
     
     
     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoEspecialidad.setItems(getEspecialidad());
    }
    
    
    
     public void cargarDatos(){
        tblespecialidades.setItems(getEspecialidad());
        colcodigoEspecialidad.setCellValueFactory(new PropertyValueFactory <Especialidad,Integer>("codigoEspecialidad"));
        colnombreEspecialidad.setCellValueFactory(new PropertyValueFactory <Especialidad,String>("nombreEspecialidad"));
    
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdateEspecialidades(?,?)}");
            Especialidad registro = (Especialidad) tblespecialidades.getSelectionModel().getSelectedItem();
            //registro.setCodigoEspecialidad(Integer.parseInt(cmbcodigoEspecialidad.getSelectionModel().getSelectedItem().toString()));
            registro.setNombreEspecialidad(txtnombreEspecialidad.getText());
            
              procedimiento.setInt(1,registro.getCodigoEspecialidad());
              procedimiento.setString(2,registro.getNombreEspecialidad());
            procedimiento.execute();
            
         }catch (SQLException e){
        e.printStackTrace();
            
        }
        
    }
       
       
       public Especialidad buscarEspecialidad(int codigoEspecialidad){
        Especialidad resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Especialidades(?) }");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Especialidad(registro.getInt("codigoEspecialidad"),registro.getString("nombreEspecialidad"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
       
       
          public void seleccionar(){
        cmbcodigoEspecialidad.getSelectionModel().select(buscarEspecialidad(((Especialidad)tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
        txtnombreEspecialidad.setText(((Especialidad) tblespecialidades.getSelectionModel().getSelectedItem()).getNombreEspecialidad());
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
        cmbcodigoEspecialidad.setDisable(false);
        txtnombreEspecialidad.setDisable(false);
        
        
        cmbcodigoEspecialidad.setEditable(true);
        txtnombreEspecialidad.setEditable(true);
                
    }
    
    public void limpiar(){
        txtnombreEspecialidad.setText("");
        
   
    }
          
    
   public void ingresar(){
        Especialidad registro = new Especialidad();
        registro.setNombreEspecialidad(txtnombreEspecialidad.getText());
            
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEspecialidades(?) }");
            procedimiento.setString(1, registro.getNombreEspecialidad());
            procedimiento.execute();
            listarEspecialidad.add(registro);
            
            
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
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEspecialidades(?)}");
                    procedimiento.setInt(1,((Especialidad)tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                    procedimiento.execute();
                     listarEspecialidad.remove(tblespecialidades.getSelectionModel().getSelectedIndex());
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
    
    
     public void cancelar(){
            btnagregar.setText("Nuevo");
            btneliminar.setText("Eliminar");
            btnreporte.setDisable(false);
            btneditar.setDisable(false);
            tipoOperaciones = operaciones.Nuevo;
        }
     
      public void desactivar(){
        cmbcodigoEspecialidad.setDisable(true);
       txtnombreEspecialidad.setDisable(true);
        
        
        cmbcodigoEspecialidad.setEditable(false);
        txtnombreEspecialidad.setEditable(false);
                
    }
    
    
       public void imprimirReporte(){
        if(tblespecialidades.getSelectionModel().getSelectedItem() != null){
            int codigoEspecialidad = ((Especialidad) tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad();
            Map parametros = new HashMap();
            parametros.put("p_codigoEspecialidad",codigoEspecialidad);
            GenerarReporte.mostrarReporte("reporteEspecialidades.jasper","reporteEspecialidades", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
       
       
       public void imprimirReporteGeneral(){
        if(tblespecialidades.getSelectionModel()//.getSelectedItem()
                != null){
           // int codigoEspecialidad = ((Especialidad) tblespecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad();
            Map parametros = new HashMap();
            //parametros.put("p_codigoEspecialidad",codigoEspecialidad);
            GenerarReporte.mostrarReporte("reporteEspecialidadGeneral.jasper","reporteEspecialidadGeneral", parametros);
            
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
