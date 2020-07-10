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
import org.alessandroaguilar.bean.ControlCitas;
import org.alessandroaguilar.bean.Recetas;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author programacion
 */
public class RecetasController implements Initializable{
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
      private operaciones tipoOperaciones = operaciones.Ninguno;
      private ObservableList <Recetas> listarRecetas;
      private ObservableList <ControlCitas> listarControlCitas;
      private Principal escenarioPrincipal;
      
      
      @FXML private TextField txtdescripcionReceta;
      
      @FXML private ComboBox cmbcodigoReceta;
      @FXML private ComboBox cmbcodigoControlCita;
      
      @FXML private TableView tblrecetas;
      @FXML private TableColumn colcodigoReceta;
      @FXML private TableColumn coldescripcionReceta;
      @FXML private TableColumn colcodigoControlC;
      
      @FXML private Button btnagregar;
      @FXML private Button btneditar;
      @FXML private Button btneliminar;
      @FXML private Button btnreporte; 
      @FXML private Button btnreporteGeneral;  
      
      
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         cargarDatos();
  
        //cmbcodigoReceta.setItems(getRecetas());
        cmbcodigoControlCita.setItems(getControlCitas());
        
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
    
    
    public ObservableList <Recetas> getRecetas(){
        ArrayList<Recetas> lista = new ArrayList<Recetas>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarRecetas}");
        ResultSet resultado = procedimiento.executeQuery();
     
         while(resultado.next()){
        lista.add(new Recetas(resultado.getInt("codigoReceta"),resultado.getString("descripcionReceta"),resultado.getInt("codigoControlCita")));
     }
     
    }catch (SQLException e){
        e.printStackTrace();
    }
     return listarRecetas = FXCollections.observableList(lista);   
    }
    
    
     public void cargarDatos(){
        tblrecetas.setItems(getRecetas());
        
        colcodigoReceta.setCellValueFactory(new PropertyValueFactory <Recetas,Integer>("codigoReceta"));
        coldescripcionReceta.setCellValueFactory(new PropertyValueFactory <Recetas,String>("descripcionReceta"));
        colcodigoControlC.setCellValueFactory(new PropertyValueFactory <Recetas,Integer>("codigoControlCita"));
        
        
    }
    
    
     public Recetas buscarRecetas(int codigoReceta){
        Recetas resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Recetas(?) }");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
               resultado = new Recetas(registro.getInt("codigoReceta"),registro.getString("descripcionReceta"),registro.getInt("codigoControlCita"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
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
     
    
    public void seleccionar(){
        cmbcodigoReceta.getSelectionModel().select(buscarRecetas(((Recetas)tblrecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
        txtdescripcionReceta.setText(((Recetas) tblrecetas.getSelectionModel().getSelectedItem()).getDescripcionReceta());
        cmbcodigoControlCita.getSelectionModel().select(buscarControlCitas(((Recetas)tblrecetas.getSelectionModel().getSelectedItem()).getCodigoControlCita()));
    
    }
    
    
    public void edit(){
            switch(tipoOperaciones){
                case Ninguno:
                    if(tblrecetas.getSelectionModel().getSelectedItem() !=null){
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
         txtdescripcionReceta.setDisable(false);
         cmbcodigoControlCita.setDisable(false);
        
  
         txtdescripcionReceta.setEditable(true);
    // cmbcodigoControlCita.setEditable(true);
                    
    }
    
    public void limpiar(){
        txtdescripcionReceta.setText("");
        
    }
    
    public void cancelar(){
            btnagregar.setText("Nuevo");
            btneliminar.setText("Eliminar");
            btnreporte.setDisable(false);
            btneditar.setDisable(false);
            tipoOperaciones = operaciones.Nuevo;
        }
    
    
    public void desactivar(){
       cmbcodigoReceta.setDisable(true);
       txtdescripcionReceta.setDisable(true);
       cmbcodigoControlCita.setDisable(true);
      
        
       cmbcodigoReceta.setEditable(false);
       txtdescripcionReceta.setEditable(false);
       cmbcodigoControlCita.setEditable(false);
               
    }
    
    
    public void ingresar(){
        Recetas registro = new Recetas();
        registro.setDescripcionReceta(txtdescripcionReceta.getText());
        registro.setCodigoControlCita(((ControlCitas)cmbcodigoControlCita.getSelectionModel().getSelectedItem()).getCodigoControlCita());
       

        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarRecetas (?,?)}");
            procedimiento.setString(1, registro.getDescripcionReceta());
            procedimiento.setInt(2, registro.getCodigoControlCita());
            
            
            procedimiento.execute();
            listarRecetas.add(registro);
            
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        
    }
    
        
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdateRecetas(?,?,?)}");
            Recetas registro = (Recetas)tblrecetas.getSelectionModel().getSelectedItem();
            registro.setDescripcionReceta(txtdescripcionReceta.getText());
            registro.setCodigoControlCita(((ControlCitas)cmbcodigoControlCita.getSelectionModel().getSelectedItem()).getCodigoControlCita());
           
           
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setString(2, registro.getDescripcionReceta());
            procedimiento.setInt(3, registro.getCodigoControlCita());
            
            
            procedimiento.execute();
           listarRecetas.add(registro);            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }   
    }
    
    
    
    public void Eliminar(){
        if(tipoOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipoOperaciones = operaciones.Eliminar;
        if(tblrecetas.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Recetas", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarRecetas(?)}");
                    procedimiento.setInt(1,((Recetas)tblrecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                    procedimiento.execute();
                    listarRecetas.remove(tblrecetas.getSelectionModel().getSelectedIndex());
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
        if(tblrecetas.getSelectionModel().getSelectedItem() != null){
            int codigoReceta = ((Recetas) tblrecetas.getSelectionModel().getSelectedItem()).getCodigoReceta();
            Map parametros = new HashMap();
            parametros.put("p_codigoReceta",codigoReceta);
            GenerarReporte.mostrarReporte("reporteRecetas.jasper","reporteRecetas", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
      
      
      public void imprimirReporteGeneral(){
        if(tblrecetas.getSelectionModel()//.getSelectedItem() 
                != null){
           // int codigoReceta = ((Recetas) tblrecetas.getSelectionModel().getSelectedItem()).getCodigoReceta();
            Map parametros = new HashMap();
           // parametros.put("p_codigoReceta",codigoReceta);
            GenerarReporte.mostrarReporte("reporteRecetasGeneral.jasper","reporteRecetasGeneral", parametros);
            
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
