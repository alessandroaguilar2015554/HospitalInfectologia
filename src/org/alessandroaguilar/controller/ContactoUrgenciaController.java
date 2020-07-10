/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alessandroaguilar.controller;
import javafx.fxml.Initializable;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alessandroaguilar.bean.ContactoUrgencia;
import org.alessandroaguilar.bean.Pacientes;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author aless
 */
public class ContactoUrgenciaController implements Initializable {
private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
      private operaciones tipoOperaciones = operaciones.Ninguno;
      private ObservableList <ContactoUrgencia> listarContactoUrgencia;
      private ObservableList <Pacientes> listarPacientes;
      private Principal escenarioPrincipal;
    
    @FXML private ComboBox cmbcodigoPaciente;
            
    @FXML private TextField txtnumeroContacto;
    @FXML private TextField txtnombres;
    @FXML private TextField txtapellidos;
             
     @FXML private TableView tblcontactoUrgencia;
     @FXML private TableColumn colcodigoUrgencia;
     @FXML private TableColumn colcodigoPaciente;
     @FXML private TableColumn colnumeroContacto;
     @FXML private TableColumn colnombres;
     @FXML private TableColumn colapellidos;
             
    @FXML private Button btnagregar;
    @FXML private Button btneditar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte; 
     @FXML private Button btnreporteGeneral;
     
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoPaciente.setItems(getPacientes());
    }
    
    
    public void cargarDatos(){
        tblcontactoUrgencia.setItems(getContactoUrgencia());
        colcodigoUrgencia.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, Integer>("codigoContactoUrgencia"));
        colcodigoPaciente.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, Integer>("codigoPaciente"));
        colnombres.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String>("nombres"));
        colapellidos.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String>("apellidos"));
        colnumeroContacto.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, Integer>("numeroContacto"));
        
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
    
    
    public ObservableList <ContactoUrgencia> getContactoUrgencia(){
    ArrayList<ContactoUrgencia> lista = new ArrayList<ContactoUrgencia>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarcontactoUrgencia}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new ContactoUrgencia(resultado.getInt("codigoContactoUrgencia"),resultado.getInt("codigoPaciente"),resultado.getString("nombres"),resultado.getString("apellidos"),resultado.getString("numeroContacto")));
   
        }
    }catch(Exception e){
    e.printStackTrace();
    }    
        
        return listarContactoUrgencia = FXCollections.observableList(lista);
    }
    
    
     public void edit(){
            switch(tipoOperaciones){
                case Ninguno:
                    if(tblcontactoUrgencia.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdatecontactoUrgencia(?,?,?,?,?)}");
            ContactoUrgencia registro = (ContactoUrgencia)tblcontactoUrgencia.getSelectionModel().getSelectedItem();
            registro.setCodigoPaciente(((ContactoUrgencia)cmbcodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            registro.setNombres(txtnombres.getText());
            registro.setApellidos(txtapellidos.getText());
            registro.setNumeroContacto(txtnumeroContacto.getText());
           
            
            procedimiento.setInt(1, registro.getCodigoContactoUrgencia());
            procedimiento.setInt(2, registro.getCodigoPaciente());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setString(5, registro.getNumeroContacto());
           
            procedimiento.execute();
            listarContactoUrgencia.add(registro);            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }   
    }
    
    
    public ContactoUrgencia buscarContactoUrgencia(int codigoContactoUrgencia){
        ContactoUrgencia resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_contactoUrgencia(?) }");
            procedimiento.setInt(1, codigoContactoUrgencia);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new ContactoUrgencia(registro.getInt("codigoContactoUrgencia"),registro.getInt("codigoPaciente"),registro.getString("nombres"),registro.getString("apellidos"),registro.getString("numeroContacto"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
    
    
    public void seleccionar(){
        cmbcodigoPaciente.getSelectionModel().select(buscarContactoUrgencia(((ContactoUrgencia)tblcontactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        txtnombres.setText(((ContactoUrgencia) tblcontactoUrgencia.getSelectionModel().getSelectedItem()).getNombres());
        txtapellidos.setText(((ContactoUrgencia) tblcontactoUrgencia.getSelectionModel().getSelectedItem()).getApellidos());
        txtnumeroContacto.setText(((ContactoUrgencia) tblcontactoUrgencia.getSelectionModel().getSelectedItem()).getNumeroContacto());
        
       
        
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
        txtnombres.setDisable(false);
        txtapellidos.setDisable(false);
        txtnumeroContacto.setDisable(false);
       
        
     //   cmbcodigoPaciente.setEditable(true);
        txtnombres.setEditable(true);
        txtapellidos.setEditable(true);
        txtnumeroContacto.setEditable(true);
                
    }
     
     public void limpiar(){
        txtnombres.setText("");
        txtapellidos.setText("");
        txtnumeroContacto.setText("");
   
    }
     
     public void ingresar(){
        ContactoUrgencia registro = new ContactoUrgencia();
        registro.setCodigoPaciente(((Pacientes)cmbcodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setNombres(txtnombres.getText());
        registro.setApellidos(txtapellidos.getText());
        registro.setNumeroContacto(txtnumeroContacto.getText());
          

        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarcontactoUrgencia (?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNumeroContacto());
            
            procedimiento.execute();
            listarContactoUrgencia.add(registro);
            
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        
    }
     
     
    
    public void Eliminar(){
        if(tipoOperaciones == operaciones.Guardar)
            cancelar();
        else
            tipoOperaciones = operaciones.Eliminar;
        if(tblcontactoUrgencia.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Contacto Urgencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarcontactoUrgencia(?)}");
                    procedimiento.setInt(1,((ContactoUrgencia)tblcontactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
                    procedimiento.execute();
                     listarContactoUrgencia.remove(tblcontactoUrgencia.getSelectionModel().getSelectedIndex());
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
        txtnombres.setDisable(true);
        txtapellidos.setDisable(true);
        txtnumeroContacto.setDisable(true);
      
        
        cmbcodigoPaciente.setEditable(false);
        txtnombres.setEditable(false);
        txtapellidos.setEditable(false);
        txtnumeroContacto.setEditable(false);
                
    }
    
     public void imprimirReporte(){
        if(tblcontactoUrgencia.getSelectionModel().getSelectedItem() != null){
            int codigoContactoUrgencia = ((ContactoUrgencia) tblcontactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia();
            Map parametros = new HashMap();
            parametros.put("p_codigoContactoUrgencia",codigoContactoUrgencia);
            GenerarReporte.mostrarReporte("reporteContactoUrgencia.jasper","reporteContactoUrgencia", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
     
     public void imprimirReporteGeneral(){
        if(tblcontactoUrgencia.getSelectionModel()//.getSelectedItem() 
                != null){
            //int codigoContactoUrgencia = ((ContactoUrgencia) tblcontactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia();
            Map parametros = new HashMap();
           // parametros.put("p_codigoContactoUrgencia",codigoContactoUrgencia);
            GenerarReporte.mostrarReporte("reporteContactoUrgenciaGeneral.jasper","reporteContactoUrgenciaGeneral", parametros);
            
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
