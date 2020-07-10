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
import org.alessandroaguilar.bean.Cargos;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author aless
 */
public class CargosController implements Initializable{
private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
      private operaciones tipoOperaciones = operaciones.Ninguno;
      private ObservableList <Cargos> listarCargos;
      private Principal escenarioPrincipal;
      
      @FXML private ComboBox cmbcodigoCargo;
      
      @FXML private TextField txtnombreCargo;
      
       @FXML private TableView tblcargos;
       @FXML private TableColumn colcodigoCargo;
       @FXML private TableColumn colnombreCargo;
       
       @FXML private Button btnagregar;
       @FXML private Button btneditar;
       @FXML private Button btneliminar;
       @FXML private Button btnreporte;
       @FXML private Button btnreporteGeneral;
       
       
       
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoCargo.setItems(getCargos());
    }
    
    
     public void cargarDatos(){
        tblcargos.setItems(getCargos());
        colcodigoCargo.setCellValueFactory(new PropertyValueFactory <Cargos,Integer>("codigoCargo"));
        colnombreCargo.setCellValueFactory(new PropertyValueFactory <Cargos,String>("nombreCargo"));
        

    }
    
     public ObservableList <Cargos> getCargos(){
        ArrayList<Cargos> lista = new ArrayList<Cargos>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarCargos}");
        ResultSet resultado = procedimiento.executeQuery();
     
     while(resultado.next()){
     lista.add(new Cargos(resultado.getInt("codigoCargo"),resultado.getString("nombreCargo")));
       
     }
     
    }catch (SQLException e){
        e.printStackTrace();
    }
     return listarCargos = FXCollections.observableList(lista);   
    }
    
    
    public void edit(){
            switch(tipoOperaciones){
                case Ninguno:
                    if(tblcargos.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdateCargos(?,?)}");
            Cargos registro = (Cargos) tblcargos.getSelectionModel().getSelectedItem();
            //registro.setCodigoArea(Integer.parseInt(cmbcodigoArea.getSelectionModel().getSelectedItem().toString()));
            //registro.setLicenciaMedica(Integer.parseInt(txtlicenciaMedica.getText()));
            registro.setNombreCargo(txtnombreCargo.getText());
            
            procedimiento.setInt(1,registro.getCodigoCargo());
            procedimiento.setString(2,registro.getNombreCargo());
            procedimiento.execute();
            
         }catch (SQLException e){
        e.printStackTrace();
            
        }
        
    }
    
    
    public Cargos buscarCargos(int codigoCargo){
        Cargos resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Cargos(?) }");
            procedimiento.setInt(1, codigoCargo);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Cargos(registro.getInt("codigoCargo"),registro.getString("nombreCargo"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
    
    public void seleccionar(){
        cmbcodigoCargo.getSelectionModel().select(buscarCargos(((Cargos)tblcargos.getSelectionModel().getSelectedItem()).getCodigoCargo()));
        txtnombreCargo.setText(((Cargos) tblcargos.getSelectionModel().getSelectedItem()).getNombreCargo());
    
    
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
         cmbcodigoCargo.setDisable(false);
        txtnombreCargo.setDisable(false);
        
        
         cmbcodigoCargo.setEditable(true);
        txtnombreCargo.setEditable(true);
                
    }
    
    public void limpiar(){
        
        txtnombreCargo.setText("");
   
    }
    
     
      public void ingresar(){
        Cargos registro = new Cargos();
        registro.setNombreCargo(txtnombreCargo.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargos(?) }");
            procedimiento.setString(1, registro.getNombreCargo());
            procedimiento.execute();
            listarCargos.add(registro);
            
            
        }catch(SQLException e){
                e.printStackTrace();
        }
        
        
        
    }
      
      
      public void Eliminar(){
        if(tipoOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipoOperaciones = operaciones.Eliminar;
        if(tblcargos.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Cargos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargos(?)}");
                    procedimiento.setInt(1,((Cargos)tblcargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
                    procedimiento.execute();
                    listarCargos.remove(tblcargos.getSelectionModel().getSelectedIndex());
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
        cmbcodigoCargo.setDisable(true);
        txtnombreCargo.setDisable(true);
       
        
        cmbcodigoCargo.setEditable(false);
        txtnombreCargo.setEditable(false);
                
    }
    
    
    public void imprimirReporte(){
        if(tblcargos.getSelectionModel().getSelectedItem() != null){
            int codigoCargo = ((Cargos) tblcargos.getSelectionModel().getSelectedItem()).getCodigoCargo();
            Map parametros = new HashMap();
            parametros.put("p_codigoCargo",codigoCargo);
            GenerarReporte.mostrarReporte("reporteCargo.jasper","reporteCargo", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
    
    public void imprimirReporteGeneral(){
        if(tblcargos.getSelectionModel()//.getSelectedItem()
                != null){
           // int codigoCargo = ((Cargos) tblcargos.getSelectionModel().getSelectedItem()).getCodigoCargo();
            Map parametros = new HashMap();
            //parametros.put("p_codigoCargo",codigoCargo);
            GenerarReporte.mostrarReporte("reporteCargoGeneral.jasper","reporteCargoGeneral", parametros);
            
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
