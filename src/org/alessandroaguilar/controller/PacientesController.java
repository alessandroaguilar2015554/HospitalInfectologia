/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alessandroaguilar.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.util.Date;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.alessandroaguilar.bean.Pacientes;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author programacion
 */
public class PacientesController implements Initializable{
     private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
     private operaciones tipoOperaciones = operaciones.Ninguno;
     private ObservableList <Pacientes> listarPaciente;
     private Principal escenarioPrincipal;
     
     
      
    @FXML private ComboBox cmbcodigoPaciente;
    @FXML private TextField txtnombre;
    @FXML private TextField txtapellido;
    @FXML private TextField txtedad;
    @FXML private TextField txtDpi;
    @FXML private GridPane grpfechaNacimiento;
    
    private DatePicker dtpfechaNacimiento;
    @FXML private TextField txtdireccion;
    @FXML private TextField txtocupacion;
    @FXML private TextField txtsexo;

    @FXML private Button btnagregar;
    @FXML private Button btneditar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte; 
    @FXML private Button btnreporteGeneral;
    
    @FXML private TableView tblpacientes;
    @FXML private TableColumn colcodigoPaciente;
    @FXML private TableColumn colnombre;
    @FXML private TableColumn colapellido;
    @FXML private TableColumn coledad;
    @FXML private TableColumn coldpi;  
    @FXML private TableColumn colfechaNacimiento;  
    @FXML private TableColumn coldireccion; 
    @FXML private TableColumn colocupacion; 
    @FXML private TableColumn colsexo; 
     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoPaciente.setItems(getPacientes());
        
        dtpfechaNacimiento = new DatePicker (Locale.ENGLISH);
       dtpfechaNacimiento.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpfechaNacimiento.getCalendarView().todayButtonTextProperty().set("Today");
       grpfechaNacimiento.add(dtpfechaNacimiento,0,0);
     
    }
    public void cargarDatos(){
       tblpacientes.setItems(getPacientes());
       
        colcodigoPaciente.setCellValueFactory(new PropertyValueFactory <Pacientes,Integer>("codigoPaciente"));
        coldpi.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("DPI"));
        colapellido.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("apellidos"));
        colnombre.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("nombres"));
        colfechaNacimiento.setCellValueFactory(new PropertyValueFactory <Pacientes,Date>("fechaNacimiento"));
        coledad.setCellValueFactory(new PropertyValueFactory <Pacientes,Integer>("edad"));
        coldireccion.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("direccion"));
        colocupacion.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("ocupacion"));
        colsexo.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("sexo"));

    }
    
     public ObservableList <Pacientes> getPacientes(){
        ArrayList<Pacientes> lista = new ArrayList<Pacientes>();
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarPacientes}");
     ResultSet resultado = procedimiento.executeQuery();
     
     while(resultado.next()){
   lista.add(new Pacientes(resultado.getInt("codigoPaciente"),resultado.getString("DPI"),resultado.getString("apellidos"),resultado.getString("nombres"),resultado.getDate("fechaNacimiento"),resultado.getInt("edad"),resultado.getString("direccion"),resultado.getString("ocupacion"),resultado.getString("sexo")));
       
     }
     
    }catch (SQLException e){
        e.printStackTrace();
    }
     return  listarPaciente = FXCollections.observableList(lista);   
    }
     
     public void edit(){
            switch(tipoOperaciones){
                case Ninguno:
                    if(tblpacientes.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdatePacientes(?,?,?,?,?,?,?,?,?)}");
            Pacientes registro = (Pacientes) tblpacientes.getSelectionModel().getSelectedItem();
            
            registro.setCodigoPaciente(Integer.parseInt(cmbcodigoPaciente.getSelectionModel().getSelectedItem().toString()));
            registro.setDPI(txtDpi.getText());
            registro.setApellidos(txtapellido.getText());
            registro.setNombres(txtnombre.getText());
            registro.setFechaNacimiento(dtpfechaNacimiento.getSelectedDate());
            registro.setEdad(Integer.parseInt(txtedad.getText()));
            registro.setDireccion(txtdireccion.getText());
            registro.setOcupacion(txtocupacion.getText());
            registro.setSexo(txtsexo.getText());
            
            procedimiento.setInt(1,registro.getCodigoPaciente());
            procedimiento.setString(2,registro.getDPI());
            procedimiento.setString(3,registro.getApellidos());
            procedimiento.setString(4,registro.getNombres());
            procedimiento.setDate(5,new java.sql.Date (registro.getFechaNacimiento().getTime()));
            procedimiento.setInt(6,registro.getEdad());
            procedimiento.setString(7,registro.getDireccion());
            procedimiento.setString(8,registro.getOcupacion());
            procedimiento.setString(9,registro.getSexo());
            procedimiento.execute();
            
         }catch (SQLException e){
        e.printStackTrace();
            
        }
        
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
     
     
     
     
     
     public void seleccionar(){
        cmbcodigoPaciente.getSelectionModel().select(buscarPacientes(((Pacientes)tblpacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        txtDpi.setText(((Pacientes) tblpacientes.getSelectionModel().getSelectedItem()).getDPI());
        
        txtapellido.setText(((Pacientes) tblpacientes.getSelectionModel().getSelectedItem()).getApellidos());
        txtnombre.setText(((Pacientes) tblpacientes.getSelectionModel().getSelectedItem()).getNombres());
        dtpfechaNacimiento.selectedDateProperty().set(((Pacientes)tblpacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        txtedad.setText(""+((Pacientes) tblpacientes.getSelectionModel().getSelectedItem()).getEdad());
        txtdireccion.setText(((Pacientes) tblpacientes.getSelectionModel().getSelectedItem()).getDireccion());
        txtocupacion.setText(((Pacientes) tblpacientes.getSelectionModel().getSelectedItem()).getOcupacion());
        txtsexo.setText(((Pacientes) tblpacientes.getSelectionModel().getSelectedItem()).getSexo());
       
        
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
        cmbcodigoPaciente.setDisable(false);
        txtDpi.setDisable(false);
        txtapellido.setDisable(false);
        txtnombre.setDisable(false);
        dtpfechaNacimiento.setDisable(false);
        txtedad.setDisable(false);
        txtdireccion.setDisable(false);
        txtocupacion.setDisable(false);
        txtsexo.setDisable(false);
        
        cmbcodigoPaciente.setEditable(true);
        txtDpi.setEditable(true);
        txtapellido.setEditable(true);
        txtnombre.setEditable(true);
        txtedad.setEditable(true);
        txtdireccion.setEditable(true);
        txtocupacion.setEditable(true);
        txtsexo.setEditable(true);
                
    }
    
    public void limpiar(){
         txtDpi.setText("");
         txtapellido.setText("");
        txtnombre.setText("");
        txtedad.setText("");
        txtdireccion.setText("");
        txtocupacion.setText("");
        txtsexo.setText("");
   
    }
    
    public void ingresar(){
        Pacientes registro = new Pacientes();
        
         registro.setDPI(txtDpi.getText());
         registro.setApellidos(txtapellido.getText());
         registro.setNombres(txtnombre.getText());
         registro.setFechaNacimiento( dtpfechaNacimiento.getSelectedDate());
         registro.setEdad(Integer.parseInt(txtedad.getText()));
        registro.setDireccion( txtdireccion.getText());
        registro.setOcupacion(txtocupacion.getText());
        registro.setSexo(txtsexo.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPacientes(?,?,?,?,?,?,?,?) }");
            procedimiento.setString(1, registro.getDPI());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setDate(4, new java.sql.Date (registro.getFechaNacimiento().getTime()));
            procedimiento.setInt(5, registro.getEdad());
            procedimiento.setString(6, registro.getDireccion());
            procedimiento.setString(7, registro.getOcupacion());
            procedimiento.setString(8, registro.getSexo());
            procedimiento.execute();
            listarPaciente.add(registro);
            
            
        }catch(SQLException e){
                e.printStackTrace();
        }
        
        
        
    }
    
     public void Eliminar(){
        if(tipoOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipoOperaciones = operaciones.Eliminar;
        if(tblpacientes.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Paciente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPacientes(?)}");
                    procedimiento.setInt(1,((Pacientes)tblpacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                    procedimiento.execute();
                    listarPaciente.remove(tblpacientes.getSelectionModel().getSelectedIndex());
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
        cmbcodigoPaciente.setDisable(true);
        txtDpi.setDisable(true);
        txtapellido.setDisable(true);
        txtnombre.setDisable(true);
        dtpfechaNacimiento.setDisable(true);
        txtedad.setDisable(true);
        txtdireccion.setDisable(true);
        txtocupacion.setDisable(true);
        txtsexo.setDisable(true);
        
        cmbcodigoPaciente.setEditable(false);
        txtDpi.setEditable(false);
        txtapellido.setEditable(false);
        txtnombre.setEditable(false);
        txtedad.setEditable(false);
        txtdireccion.setEditable(false);
        txtocupacion.setEditable(false);
        txtsexo.setEditable(false);
                
    }
    
    
    
    public void imprimirReporte(){
        if(tblpacientes.getSelectionModel().getSelectedItem() != null){
            int codigoPaciente = ((Pacientes) tblpacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente();
            Map parametros = new HashMap();
            parametros.put("p_codigoPaciente",codigoPaciente);
            GenerarReporte.mostrarReporte("reportePacientes.jasper","reportePacientes", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
    
    
    
    public void imprimirReporteGeneral(){
        if(tblpacientes.getSelectionModel()//.getSelectedItem()
                != null){
            //int codigoPaciente = ((Pacientes) tblpacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente();
            Map parametros = new HashMap();
            //parametros.put("p_codigoPaciente",codigoPaciente);
            GenerarReporte.mostrarReporte("reportePacientesGeneral.jasper","reportePacientesGeneral", parametros);
            
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

