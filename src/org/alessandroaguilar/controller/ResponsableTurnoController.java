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
import org.alessandroaguilar.bean.Cargos;
import org.alessandroaguilar.bean.ResponsableTurno;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author aless
 */
public class ResponsableTurnoController implements Initializable{
private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
      private operaciones tipoOperaciones = operaciones.Ninguno;
      private ObservableList <ResponsableTurno> listarResponsableTurno;
      private ObservableList <Cargos> listarCargos;
      private ObservableList <Areas> listarAreas;
      private Principal escenarioPrincipal;
    
    @FXML private ComboBox cmbcodigoCargo;
    @FXML private ComboBox cmbcodigoArea;
    
    @FXML private TextField txtnombres;
    @FXML private TextField txtapellidos;
    @FXML private TextField txttelefonoResponsable;
    
    @FXML private TableView tblresponsableTurno;
    @FXML private TableColumn colcodigoResponsable;
    @FXML private TableColumn colnombres;
    @FXML private TableColumn colapellidos;
    @FXML private TableColumn coltelefonos;
    @FXML private TableColumn colcodigoArea;
    @FXML private TableColumn colcodigoCargo;
    
    @FXML private Button btnagregar;
    @FXML private Button btneditar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte;
    @FXML private Button btnreporteGeneral;
    
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoArea.setItems(getAreas());
        cmbcodigoCargo.setItems(getCargos());
    }
    
     public void cargarDatos(){
        tblresponsableTurno.setItems(getResponsableTurno());
        colcodigoResponsable.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoResponsableTurno"));
        colnombres.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("nombreResponsable"));
        colapellidos.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("apellidosResponsable"));
        coltelefonos.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("telefonoPersonal"));
        colcodigoArea.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoArea"));
        colcodigoCargo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoCargo"));
    }
     
     
     public ObservableList <ResponsableTurno> getResponsableTurno(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();        
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarResponsableTurno}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new ResponsableTurno(resultado.getInt("codigoResponsableTurno"),resultado.getString("nombreResponsable"),resultado.getString("apellidosResponsable"),resultado.getString("telefonoPersonal"),resultado.getInt("codigoArea"),resultado.getInt("codigoCargo")));
        }
            }catch(Exception e){
    e.printStackTrace();
        }    
        return listarResponsableTurno = FXCollections.observableList(lista);
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
                    if(tblresponsableTurno.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdateResponsableTurno(?,?,?,?,?,?)}");
            ResponsableTurno registro = (ResponsableTurno)tblresponsableTurno.getSelectionModel().getSelectedItem();
            
            registro.setNombreResponsable(txtnombres.getText());
            registro.setApellidosResponsable(txtapellidos.getText());
            registro.setTelefonoPersonal(txttelefonoResponsable.getText());
            registro.setCodigoArea(((Areas)cmbcodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
            registro.setCodigoCargo(((Cargos)cmbcodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
            
            
            procedimiento.setInt(1, registro.getCodigoResponsableTurno());
            procedimiento.setString(2, registro.getNombreResponsable());
            procedimiento.setString(3, registro.getApellidosResponsable());
            procedimiento.setString(4, registro.getTelefonoPersonal());
            procedimiento.setInt(5, registro.getCodigoArea());
            procedimiento.setInt(6, registro.getCodigoCargo());
            procedimiento.execute();
          listarResponsableTurno.add(registro);        
          
          
        }catch(Exception e){
            e.printStackTrace();
            
            
        }      
        
        
    }
    
    
    public ResponsableTurno buscarResponsableTurno(int codigoResponsableTurno){
        ResponsableTurno resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_ResponsableTurno(?) }");
            procedimiento.setInt(1, codigoResponsableTurno);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new ResponsableTurno(registro.getInt("codigoResponsableTurno"),registro.getString("nombreResponsable"),registro.getString("apellidosResponsable"),registro.getString("telefonoPersonal"),registro.getInt("codigoArea"),registro.getInt("codigoCargo"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
    
    
    public void seleccionar(){
        txtnombres.setText(((ResponsableTurno) tblresponsableTurno.getSelectionModel().getSelectedItem()).getNombreResponsable());
        txtapellidos.setText(((ResponsableTurno) tblresponsableTurno.getSelectionModel().getSelectedItem()).getApellidosResponsable());
        txttelefonoResponsable.setText(((ResponsableTurno) tblresponsableTurno.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        
        cmbcodigoArea.getSelectionModel().select(buscarResponsableTurno(((ResponsableTurno)tblresponsableTurno.getSelectionModel().getSelectedItem()).getCodigoArea()));
        cmbcodigoCargo.getSelectionModel().select(buscarResponsableTurno(((ResponsableTurno)tblresponsableTurno.getSelectionModel().getSelectedItem()).getCodigoCargo()));
        
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
         txtnombres.setDisable(false);
         txtapellidos.setDisable(false);
         txttelefonoResponsable.setDisable(false);
         
         cmbcodigoArea.setDisable(false);
         cmbcodigoCargo.setDisable(false);
        
        
         txtnombres.setEditable(true);
         txtapellidos.setEditable(true);
         txttelefonoResponsable.setEditable(true);
         
         cmbcodigoArea.setEditable(true);
         cmbcodigoCargo.setEditable(true);
                
    }
     
     
    public void limpiar(){
        
        txtnombres.setText("");
        txtapellidos.setText("");
        txttelefonoResponsable.setText("");
   
    }
    
    
    public void ingresar(){
            ResponsableTurno registro = new ResponsableTurno();
            registro.setNombreResponsable(txtnombres.getText());
            registro.setApellidosResponsable(txtapellidos.getText());
            registro.setTelefonoPersonal(txttelefonoResponsable.getText());
            registro.setCodigoArea(((Areas)cmbcodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
            registro.setCodigoCargo(((Cargos)cmbcodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
            
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsableTurno(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoResponsableTurno());
            procedimiento.setString(2, registro.getNombreResponsable());
            procedimiento.setString(3, registro.getApellidosResponsable());
            procedimiento.setString(4, registro.getTelefonoPersonal());
            procedimiento.setInt(5, registro.getCodigoArea());
            procedimiento.setInt(6, registro.getCodigoCargo());
            procedimiento.execute();
            //listarResponsableTurno.add(registro);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
    
    
    public void Eliminar(){
        if(tipoOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipoOperaciones = operaciones.Eliminar;
        if(tblresponsableTurno.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Responsable Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarResponsableTurno(?)}");
                    procedimiento.setInt(1,((ResponsableTurno)tblresponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
                    procedimiento.execute();
                    listarResponsableTurno.remove(tblresponsableTurno.getSelectionModel().getSelectedIndex());
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
        txtnombres.setDisable(true);
        txtapellidos.setDisable(true);
        txttelefonoResponsable.setDisable(true);
        
        cmbcodigoArea.setDisable(true);
        cmbcodigoCargo.setDisable(true);
       
        
        txtnombres.setEditable(false);
        txtapellidos.setEditable(false);
        txttelefonoResponsable.setEditable(false);
        
        cmbcodigoArea.setEditable(false);
        cmbcodigoCargo.setEditable(false);
                
    }
    
    
    
     public void imprimirReporte(){
        if(tblresponsableTurno.getSelectionModel().getSelectedItem() != null){
            int codigoResponsableTurno = ((ResponsableTurno) tblresponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno();
            Map parametros = new HashMap();
            parametros.put("p_codigoResponsableTurno",codigoResponsableTurno);
            GenerarReporte.mostrarReporte("reporteResponsableTurno.jasper","reporteResponsableTurno", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
     
     public void imprimirReporteGeneral(){
        if(tblresponsableTurno.getSelectionModel()//.getSelectedItem()
                != null){
            //int codigoResponsableTurno = ((ResponsableTurno) tblresponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno();
            Map parametros = new HashMap();
            //parametros.put("p_codigoResponsableTurno",codigoResponsableTurno);
            GenerarReporte.mostrarReporte("reporteResponsableTurnoGeneral.jasper","reporteResponsableTurnoGeneral", parametros);
            
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
