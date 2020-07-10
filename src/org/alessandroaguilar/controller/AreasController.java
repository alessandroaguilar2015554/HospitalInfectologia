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
import org.alessandroaguilar.bean.Areas;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author aless
 */
public class AreasController implements Initializable{
 private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
      private operaciones tipoOperaciones = operaciones.Ninguno;
      private ObservableList <Areas> listarAreas;
      private Principal escenarioPrincipal;
      
      @FXML private ComboBox cmbcodigoArea;
      
      @FXML private TextField txtnombreCargo;
      
       @FXML private TableView tblareas;
       @FXML private TableColumn colcodigoArea;
       @FXML private TableColumn colnombreCargo;
       
       @FXML private Button btnagregar;
       @FXML private Button btneditar;
       @FXML private Button btneliminar;
       @FXML private Button btnreporte;
       @FXML private Button btnreporteGeneral;
    
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoArea.setItems(getAreas());
    }
    
    public void cargarDatos(){
        tblareas.setItems(getAreas());
        colcodigoArea.setCellValueFactory(new PropertyValueFactory <Areas,Integer>("codigoArea"));
        colnombreCargo.setCellValueFactory(new PropertyValueFactory <Areas,String>("nombreArea"));
        

    }
    
    
    public ObservableList <Areas> getAreas(){
        ArrayList<Areas> lista = new ArrayList<Areas>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarAreas}");
     ResultSet resultado = procedimiento.executeQuery();
     
     while(resultado.next()){
     lista.add(new Areas(resultado.getInt("codigoArea"),resultado.getString("nombreArea")));
       
     }
     
    }catch (SQLException e){
        e.printStackTrace();
    }
     return listarAreas = FXCollections.observableList(lista);   
    }
    
  public void edit(){
            switch(tipoOperaciones){
                case Ninguno:
                    if(tblareas.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdateAreas(?,?)}");
            Areas registro = (Areas) tblareas.getSelectionModel().getSelectedItem();
            //registro.setCodigoArea(Integer.parseInt(cmbcodigoArea.getSelectionModel().getSelectedItem().toString()));
            //registro.setLicenciaMedica(Integer.parseInt(txtlicenciaMedica.getText()));
            registro.setNombreArea(txtnombreCargo.getText());
            
            procedimiento.setInt(1,registro.getCodigoArea());
            procedimiento.setString(2,registro.getNombreArea());
            procedimiento.execute();
            
         }catch (SQLException e){
        e.printStackTrace();
            
        }
        
    }
    
    public Areas buscarAreas(int codigoArea){
        Areas resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Areas(?) }");
            procedimiento.setInt(1, codigoArea);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Areas(registro.getInt("codigoArea"),registro.getString("nombreArea"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
    
    
    public void seleccionar(){
        cmbcodigoArea.getSelectionModel().select(buscarAreas(((Areas)tblareas.getSelectionModel().getSelectedItem()).getCodigoArea()));
        txtnombreCargo.setText(((Areas) tblareas.getSelectionModel().getSelectedItem()).getNombreArea());
     
        
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
        cmbcodigoArea.setDisable(false);
        txtnombreCargo.setDisable(false);
        
        
        cmbcodigoArea.setEditable(true);
        txtnombreCargo.setEditable(true);
                
    }
    
    public void limpiar(){
        
        txtnombreCargo.setText("");
   
    }
    
    
    public void ingresar(){
        Areas registro = new Areas();
        registro.setNombreArea(txtnombreCargo.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarAreas(?) }");
            procedimiento.setString(1, registro.getNombreArea());
            procedimiento.execute();
            listarAreas.add(registro);
            
            
        }catch(SQLException e){
                e.printStackTrace();
        }
        
        
        
    }
    
    
    public void Eliminar(){
        if(tipoOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipoOperaciones = operaciones.Eliminar;
        if(tblareas.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Area", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarAreas(?)}");
                    procedimiento.setInt(1,((Areas)tblareas.getSelectionModel().getSelectedItem()).getCodigoArea());
                    procedimiento.execute();
                    listarAreas.remove(tblareas.getSelectionModel().getSelectedIndex());
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
        cmbcodigoArea.setDisable(true);
        txtnombreCargo.setDisable(true);
       
        
        cmbcodigoArea.setEditable(false);
        txtnombreCargo.setEditable(false);
                
    }
    
    public void imprimirReporte(){
        if(tblareas.getSelectionModel().getSelectedItem() != null){
            int codigoArea = ((Areas) tblareas.getSelectionModel().getSelectedItem()).getCodigoArea();
            Map parametros = new HashMap();
            parametros.put("p_codigoArea",codigoArea);
            GenerarReporte.mostrarReporte("reporteAreas.jasper","reporteAreas", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
    
     public void imprimirReporteGeneral(){
        if(tblareas.getSelectionModel()//.getSelectedItem()
                != null){
           // int codigoArea = ((Areas) tblareas.getSelectionModel().getSelectedItem()).getCodigoArea();
            Map parametros = new HashMap();
           // parametros.put("p_codigoArea",codigoArea);
            GenerarReporte.mostrarReporte("reporteGeneralAreas.jasper","reporteGeneralAreas.jasper", parametros);
            
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
