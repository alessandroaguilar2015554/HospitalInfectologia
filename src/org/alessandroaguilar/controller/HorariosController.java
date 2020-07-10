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
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.alessandroaguilar.bean.Horario;
import org.alessandroaguilar.sistema.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;

/**
 *
 * @author aless
 */
public class HorariosController implements Initializable{
private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
      private operaciones tipoOperaciones = operaciones.Ninguno;
      private ObservableList <Horario> listarHorario;
    private Principal escenarioPrincipal;
    
    @FXML private ComboBox cmbcodigoHorario;
    
    @FXML private TextField txtlunes;
    @FXML private TextField txtmartes;
    @FXML private TextField txtmiercoles;
    @FXML private TextField txtjueves;
    @FXML private TextField txtviernes;
    
    @FXML private GridPane grphoraInicio;
    @FXML private GridPane grphoraSalida;
    private DatePicker dtphoraInicio;
    private DatePicker dtphoraSalida;
    
    @FXML private Button btnagregar;
    @FXML private Button btneditar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte; 
    @FXML private Button btnreporteGeneral;
    
    @FXML private TableView tblhorarios;
    @FXML private TableColumn colcodigoHorario;
    @FXML private TableColumn colhoraInicio;
    @FXML private TableColumn colhoraSalida;
    @FXML private TableColumn collunes;
    @FXML private TableColumn colmartes;  
    @FXML private TableColumn colmiercoles;  
    @FXML private TableColumn coljueves; 
    @FXML private TableColumn colviernes; 
    
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoHorario.setItems(getHorarios());
        
        dtphoraInicio = new DatePicker (Locale.ENGLISH);
        dtphoraInicio.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtphoraInicio.getCalendarView().todayButtonTextProperty().set("Today");
        grphoraInicio.add(dtphoraInicio,0,0);
        
        dtphoraSalida = new DatePicker (Locale.ENGLISH);
        dtphoraSalida.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtphoraSalida.getCalendarView().todayButtonTextProperty().set("Today");
        grphoraSalida.add(dtphoraSalida,0,0);
    }
    
    public void cargarDatos(){
        tblhorarios.setItems(getHorarios());
        colcodigoHorario.setCellValueFactory(new PropertyValueFactory <Horario,Integer>("codigoHorario"));
       
        colhoraInicio.setCellValueFactory(new PropertyValueFactory <Horario,Date>("horarioInicio"));
        colhoraSalida.setCellValueFactory(new PropertyValueFactory <Horario,Date>("horarioSalida"));
        
         collunes.setCellValueFactory(new PropertyValueFactory <Horario,Integer>("lunes"));
         colmartes.setCellValueFactory(new PropertyValueFactory <Horario,Integer>("martes"));
         colmiercoles.setCellValueFactory(new PropertyValueFactory <Horario,Integer>("miercoles"));
         coljueves.setCellValueFactory(new PropertyValueFactory <Horario,Integer>("jueves"));
         colviernes.setCellValueFactory(new PropertyValueFactory <Horario,Integer>("viernes"));
        

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
                    if(tblhorarios.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdateHorarios(?,?,?,?,?,?,?,?)}");
            Horario registro = (Horario) tblhorarios.getSelectionModel().getSelectedItem();
            
            //registro.setCodigoHorario(Integer.parseInt(cmbcodigoHorario.getSelectionModel().getSelectedItem().toString()));
           
            registro.setHorarioInicio(dtphoraInicio.getSelectedDate());
            registro.setHorarioSalida(dtphoraSalida.getSelectedDate());
            
            registro.setLunes(Integer.parseInt(txtlunes.getText()));
            registro.setMartes(Integer.parseInt(txtmartes.getText()));
            registro.setMiercoles(Integer.parseInt(txtmiercoles.getText()));
            registro.setJueves(Integer.parseInt(txtjueves.getText()));
            registro.setViernes(Integer.parseInt(txtviernes.getText()));
            
            procedimiento.setInt(1,registro.getCodigoHorario());
            
            procedimiento.setDate(2,new java.sql.Date (registro.getHorarioInicio().getTime()));
            procedimiento.setDate(3,new java.sql.Date (registro.getHorarioSalida().getTime()));
            
           procedimiento.setInt(4,registro.getLunes());
           procedimiento.setInt(5,registro.getMartes());
           procedimiento.setInt(6,registro.getMiercoles());
           procedimiento.setInt(7,registro.getJueves());
           procedimiento.setInt(8,registro.getViernes());
            procedimiento.execute();
            
         }catch (SQLException e){
        e.printStackTrace();
            
        }
        
    }
    
    public Horario buscarHorario(int codigoHorario){
        Horario resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Horarios(?) }");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Horario(registro.getInt("codigoHorario"),registro.getDate("horarioInicio"),registro.getDate("horarioSalida"),registro.getInt("lunes"),registro.getInt("martes"),registro.getInt("miercoles"),registro.getInt("jueves"),registro.getInt("viernes"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
    
    
    public void seleccionar(){
        cmbcodigoHorario.getSelectionModel().select(buscarHorario(((Horario)tblhorarios.getSelectionModel().getSelectedItem()).getCodigoHorario()));
        
        dtphoraInicio.selectedDateProperty().set(((Horario)tblhorarios.getSelectionModel().getSelectedItem()).getHorarioInicio());
        dtphoraSalida.selectedDateProperty().set(((Horario)tblhorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
        
        txtlunes.setText(""+((Horario) tblhorarios.getSelectionModel().getSelectedItem()).getLunes());
        txtmartes.setText(""+((Horario) tblhorarios.getSelectionModel().getSelectedItem()).getMartes());
        txtmiercoles.setText(""+((Horario) tblhorarios.getSelectionModel().getSelectedItem()).getMiercoles());
        txtjueves.setText(""+((Horario) tblhorarios.getSelectionModel().getSelectedItem()).getJueves());
        txtviernes.setText(""+((Horario) tblhorarios.getSelectionModel().getSelectedItem()).getViernes());
       
        
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
        cmbcodigoHorario.setDisable(false);
        dtphoraInicio.setDisable(false);
        dtphoraSalida.setDisable(false);
        txtlunes.setDisable(false);
        txtmartes.setDisable(false);
        txtmiercoles.setDisable(false);
        txtjueves.setDisable(false);
        txtviernes.setDisable(false);
        
        cmbcodigoHorario.setEditable(true);
        txtlunes.setEditable(true);
        txtmartes.setEditable(true);
        txtmiercoles.setEditable(true);
        txtjueves.setEditable(true);
        txtviernes.setEditable(true);
        
                
    }
    
     public void limpiar(){
        txtlunes.setText("");
        txtmartes.setText("");
        txtmiercoles.setText("");
        txtjueves.setText("");
        txtviernes.setText("");
       
    }
     
     
      public void ingresar(){
        Horario registro = new Horario();

        registro.setHorarioInicio(dtphoraInicio.getSelectedDate());
        registro.setHorarioSalida(dtphoraSalida.getSelectedDate());
        
       registro.setLunes(Integer.parseInt( txtlunes.getText()));
       registro.setMartes(Integer.parseInt(txtmartes.getText()));
       registro.setMiercoles(Integer.parseInt(txtmiercoles.getText()));
       registro.setJueves(Integer.parseInt(txtjueves.getText()));
       registro.setViernes(Integer.parseInt(txtviernes.getText()));
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorarios(?,?,?,?,?,?,?) }");
            procedimiento.setDate(1, new java.sql.Date (registro.getHorarioInicio().getTime()));
            procedimiento.setDate(2, new java.sql.Date (registro.getHorarioSalida().getTime()));
            
            procedimiento.setInt(3, registro.getLunes());
            procedimiento.setInt(4, registro.getMartes());
            procedimiento.setInt(5, registro.getMiercoles());
            procedimiento.setInt(6, registro.getJueves());
            procedimiento.setInt(7, registro.getViernes());
            
            procedimiento.execute();
            listarHorario.add(registro);
            
            
        }catch(SQLException e){
                e.printStackTrace();
        }
        
        
        
    }
     
      
       public void Eliminar(){
        if(tipoOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipoOperaciones = operaciones.Eliminar;
        if(tblhorarios.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Horario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarHorarios(?)}");
                    procedimiento.setInt(1,((Horario)tblhorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                    procedimiento.execute();
                     listarHorario.remove(tblhorarios.getSelectionModel().getSelectedIndex());
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
        cmbcodigoHorario.setDisable(true);
        
        dtphoraInicio.setDisable(true);
        dtphoraSalida.setDisable(true);
        
        txtlunes.setDisable(true);
        txtmartes.setDisable(true);
        txtmiercoles.setDisable(true);
        txtjueves.setDisable(true);
        txtviernes.setDisable(true);
        
        cmbcodigoHorario.setEditable(false);
        txtlunes.setEditable(false);
        txtmartes.setEditable(false);
        txtmiercoles.setEditable(false);
        txtjueves.setEditable(false);
        txtviernes.setEditable(false);
        
                
    }
    
    
    
    
     public void imprimirReporte(){
        if(tblhorarios.getSelectionModel().getSelectedItem() != null){
            int codigoHorario = ((Horario) tblhorarios.getSelectionModel().getSelectedItem()).getCodigoHorario();
            Map parametros = new HashMap();
            parametros.put("p_codigoHorario",codigoHorario);
            GenerarReporte.mostrarReporte("reporteHorarios.jasper","reporteHorarios", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
    
    
     public void imprimirReporteGeneral(){
        if(tblhorarios.getSelectionModel()//.getSelectedItem() 
                != null){
            //int codigoHorario = ((Horario) tblhorarios.getSelectionModel().getSelectedItem()).getCodigoHorario();
            Map parametros = new HashMap();
            //parametros.put("p_codigoHorario",codigoHorario);
            GenerarReporte.mostrarReporte("reporteGeneralHorarios.jasper","reporteGeneralHorarios", parametros);
            
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
