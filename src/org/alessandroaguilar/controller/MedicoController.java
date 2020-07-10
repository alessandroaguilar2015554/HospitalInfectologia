/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alessandroaguilar.controller;


import java.net.URL;
import java.util.Date;

import java.util.Locale;
import eu.schudt.javafx.controls.calendar.DatePicker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

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
/*import javafx.scene.control.DatePicker;*/
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alessandroaguilar.bean.Medico;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;



import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author programacion
 */
public class MedicoController implements Initializable{
    
      private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
      private operaciones tipoOperaciones = operaciones.Ninguno;
      private ObservableList <Medico> listarMedico;
    private Principal escenarioPrincipal;
    
    
    @FXML private ComboBox cmbcodigoMedico;
    @FXML private TextField txtnombres;
    @FXML private TextField txtlicenciaMedica;
    @FXML private TextField txtapellidos;
    @FXML private TextField txtsexo;
    @FXML private GridPane grphoraEntrada;
    @FXML private GridPane grphoraSalida;
    private DatePicker dtphoraEntrada;
    private DatePicker dtphoraSalida;
    @FXML private Button btnagregar;
    @FXML private Button btneditar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte; 
    @FXML private TableView tblmedicos;
    @FXML private TableColumn colcodigoMedico;
    @FXML private TableColumn collicenciaMedica;
    @FXML private TableColumn colnombres;
    @FXML private TableColumn colapellidos;
    @FXML private TableColumn colhoraEntrada;  
    @FXML private TableColumn colhoraSalida;  
    @FXML private TableColumn colsexo; 
      
    @FXML private Button btnreporteGeneral;
    
      
   @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoMedico.setItems(getMedicos());
        
        dtphoraEntrada = new DatePicker (Locale.ENGLISH);
        dtphoraEntrada.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtphoraEntrada.getCalendarView().todayButtonTextProperty().set("Today");
        grphoraEntrada.add(dtphoraEntrada,0,0);
        
        dtphoraSalida = new DatePicker (Locale.ENGLISH);
        dtphoraSalida.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtphoraSalida.getCalendarView().todayButtonTextProperty().set("Today");
        grphoraSalida.add(dtphoraSalida,0,0);
        
    }
    
    public void cargarDatos(){
        tblmedicos.setItems(getMedicos());
        colcodigoMedico.setCellValueFactory(new PropertyValueFactory <Medico,Integer>("codigoMedico"));
        collicenciaMedica.setCellValueFactory(new PropertyValueFactory <Medico,Integer>("licenciaMedica"));
        colnombres.setCellValueFactory(new PropertyValueFactory <Medico,String>("nombres"));
        colapellidos.setCellValueFactory(new PropertyValueFactory <Medico,String>("apellidos"));
        colhoraEntrada.setCellValueFactory(new PropertyValueFactory <Medico,Date>("horaEntrada"));
        colhoraSalida.setCellValueFactory(new PropertyValueFactory <Medico,Date>("horaSalida"));
        colsexo.setCellValueFactory(new PropertyValueFactory <Medico,String>("sexo"));
        

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
    
    
    public void edit(){
            switch(tipoOperaciones){
                case Ninguno:
                    if(tblmedicos.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdateMedicos(?,?,?,?,?,?,?)}");
            Medico registro = (Medico) tblmedicos.getSelectionModel().getSelectedItem();
            registro.setCodigoMedico(Integer.parseInt(cmbcodigoMedico.getSelectionModel().getSelectedItem().toString()));
            registro.setLicenciaMedica(Integer.parseInt(txtlicenciaMedica.getText()));
            registro.setNombres(txtnombres.getText());
            registro.setApellidos(txtapellidos.getText());
            registro.setHoraEntrada(dtphoraEntrada.getSelectedDate());
            registro.setHoraSalida(dtphoraSalida.getSelectedDate());
            registro.setSexo(txtsexo.getText());
            
            procedimiento.setInt(1,registro.getCodigoMedico());
            procedimiento.setInt(2,registro.getLicenciaMedica());
            procedimiento.setString(3,registro.getNombres());
            procedimiento.setString(4,registro.getApellidos());
            procedimiento.setDate(5,new java.sql.Date (registro.getHoraEntrada().getTime()));
            procedimiento.setDate(6,new java.sql.Date (registro.getHoraSalida().getTime()));
            procedimiento.setString(7,registro.getSexo());
            procedimiento.execute();
            
         }catch (SQLException e){
        e.printStackTrace();
            
        }
        
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
        cmbcodigoMedico.getSelectionModel().select(buscarMedico(((Medico)tblmedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        txtlicenciaMedica.setText(""+((Medico) tblmedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica());
        txtnombres.setText(((Medico) tblmedicos.getSelectionModel().getSelectedItem()).getNombres());
        txtapellidos.setText(((Medico) tblmedicos.getSelectionModel().getSelectedItem()).getApellidos());
        txtsexo.setText(((Medico) tblmedicos.getSelectionModel().getSelectedItem()).getSexo());
        dtphoraEntrada.selectedDateProperty().set(((Medico)tblmedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
        dtphoraSalida.selectedDateProperty().set(((Medico)tblmedicos.getSelectionModel().getSelectedItem()).getHoraSalida());
       
        
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
        txtlicenciaMedica.setDisable(false);
        txtnombres.setDisable(false);
        txtapellidos.setDisable(false);
        txtsexo.setDisable(false);
        dtphoraEntrada.setDisable(false);
        dtphoraSalida.setDisable(false);
        
        cmbcodigoMedico.setEditable(true);
        txtlicenciaMedica.setEditable(true);
        txtnombres.setEditable(true);
        txtapellidos.setEditable(true);
        txtsexo.setEditable(true);
                
    }
    
    public void limpiar(){
        txtlicenciaMedica.setText("");
        txtnombres.setText("");
        txtapellidos.setText("");
        txtsexo.setText("");
   
    }
    
    public void ingresar(){
        Medico registro = new Medico();
        registro.setLicenciaMedica(Integer.parseInt(txtlicenciaMedica.getText()));
        registro.setNombres(txtnombres.getText());
        registro.setApellidos(txtapellidos.getText());
        registro.setHoraEntrada(dtphoraEntrada.getSelectedDate());
        registro.setHoraSalida(dtphoraSalida.getSelectedDate());
        registro.setSexo(txtsexo.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicos(?,?,?,?,?,?) }");
            procedimiento.setInt(1, registro.getLicenciaMedica());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setDate(4, new java.sql.Date (registro.getHoraEntrada().getTime()));
            procedimiento.setDate(5, new java.sql.Date (registro.getHoraSalida().getTime()));
            procedimiento.setString(6, registro.getSexo());
            procedimiento.execute();
            listarMedico.add(registro);
            
            
        }catch(SQLException e){
                e.printStackTrace();
        }
        
        
        
    }
    
     public void Eliminar(){
        if(tipoOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipoOperaciones = operaciones.Eliminar;
        if(tblmedicos.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicos(?)}");
                    procedimiento.setInt(1,((Medico)tblmedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                    procedimiento.execute();
                     listarMedico.remove(tblmedicos.getSelectionModel().getSelectedIndex());
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
        cmbcodigoMedico.setDisable(true);
        txtlicenciaMedica.setDisable(true);
        txtnombres.setDisable(true);
        txtapellidos.setDisable(true);
        txtsexo.setDisable(true);
        dtphoraEntrada.setDisable(true);
        dtphoraSalida.setDisable(true);
        
        cmbcodigoMedico.setEditable(false);
        txtlicenciaMedica.setEditable(false);
        txtnombres.setEditable(false);
        txtapellidos.setEditable(false);
        txtsexo.setEditable(false);
                
    }
    
    public void imprimirReporte(){
        if(tblmedicos.getSelectionModel().getSelectedItem() != null){
            int codigoMedico = ((Medico) tblmedicos.getSelectionModel().getSelectedItem()).getCodigoMedico();
            Map parametros = new HashMap();
            parametros.put("p_codigoMedico",codigoMedico);
            GenerarReporte.mostrarReporte("reporteMedicos.jasper","reporteMedicos", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
    
    
     public void imprimirReporteGeneral(){
        if(tblmedicos.getSelectionModel()//.getSelectedItem() 
                != null){
            //int codigoMedico = ((Medico) tblmedicos.getSelectionModel().getSelectedItem()).getCodigoMedico();
            Map parametros = new HashMap();
            //parametros.put("p_codigoMedico",codigoMedico);
            GenerarReporte.mostrarReporte("reporteMedicosGeneral.jasper","reporteMedicosGeneral", parametros);
            
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
