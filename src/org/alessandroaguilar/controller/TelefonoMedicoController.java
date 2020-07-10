
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
import org.alessandroaguilar.bean.Medico;
import org.alessandroaguilar.bean.TelefonoMedico;
import org.alessandroaguilar.db.Conexion;
import org.alessandroaguilar.report.GenerarReporte;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author aless
 */
public class TelefonoMedicoController implements Initializable{
    private enum operaciones{Nuevo, Guardar, Eliminar, Editar, Actualizar, Cancelar,Ninguno};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.Ninguno;
    private ObservableList<Medico> listaMedico;
    private ObservableList<TelefonoMedico> listaTelefonoMedico;    
    
    @FXML private TextField txtcodigoTelefonoMedico;
    @FXML private TextField txttelefonoPersonal;
    @FXML private TextField txttelefonoTrabajo;
    @FXML private ComboBox  cmbcodigoMedico;
    
    @FXML private TableView  tbltelefonoMedico;
    @FXML private TableColumn colcodigoTelefono;
    @FXML private TableColumn coltelefonoPersonal;
    @FXML private TableColumn  coltelefonoTrabajo;
    @FXML private TableColumn  colcodigoMedico;   
    
     @FXML private Button btnagregar;
    @FXML private Button btneditar;
    @FXML private Button btneliminar;
    @FXML private Button btnreporte; 
    @FXML private Button btnreporteGeneral;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodigoMedico.setItems(getMedicos());
    }
    
    
    
   public void cargarDatos(){
        tbltelefonoMedico.setItems(getTelefonosMedico());
        colcodigoTelefono.setCellValueFactory(new PropertyValueFactory <Medico, Integer>("codigoTelefonoMedico"));
        coltelefonoPersonal.setCellValueFactory(new PropertyValueFactory <Medico, Integer>("telefonoPersonal"));
        coltelefonoTrabajo.setCellValueFactory(new PropertyValueFactory <Medico, String>("telefonoTrabajo"));
        colcodigoMedico.setCellValueFactory(new PropertyValueFactory <Medico, Integer>("codigoMedico"));
    }    
    
    
 
    
  public ObservableList<TelefonoMedico> getTelefonosMedico(){
        ArrayList<TelefonoMedico> lista = new ArrayList<TelefonoMedico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrartelefonosMedico}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoMedico(resultado.getInt("codigoTelefonoMedico"),resultado.getString("telefonoPersonal"),resultado.getString("telefonoTrabajo"),resultado.getInt("codigoMedico")));    
                
            }        
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTelefonoMedico = FXCollections.observableList(lista);
    }
  
  
   
    public ObservableList<Medico> getMedicos(){
        ArrayList<Medico> lista = new ArrayList<Medico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarMedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
     lista.add(new Medico(resultado.getInt("codigoMedico"),resultado.getInt("licenciaMedica"),resultado.getString("nombres"),resultado.getString("apellidos"),resultado.getDate("horaEntrada"),resultado.getDate("horaSalida"),resultado.getString("sexo")));
     
            }        
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedico = FXCollections.observableList(lista);
    }
     
     
     public void edit()throws SQLException{
            switch(tipoDeOperacion){
                case Ninguno:
                    if(tbltelefonoMedico.getSelectionModel().getSelectedItem() !=null){
                        btneditar.setText("Actualizar");
                        btnreporte.setText("Cancelar");
                        tipoDeOperacion = operaciones.Actualizar;
                        btnagregar.setDisable(true);
                        btneliminar.setDisable(true);
                        activar();
                    }
                  break;
                  
                case Actualizar:
                    actualizar();
                     btneditar.setText("Editar");
                     btnreporte.setText("Reporte");
                     tipoDeOperacion = operaciones.Ninguno;
                     btnagregar.setDisable(false);
                     btneliminar.setDisable(false);
                     desactivar();
                     cargarDatos();
                     
                     break;
            }
    }
     
     
     public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_UpdatetelefonosMedico(?,?,?,?)}");
            TelefonoMedico registro = (TelefonoMedico)tbltelefonoMedico.getSelectionModel().getSelectedItem();
            registro.setTelefonoPersonal(txttelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txttelefonoTrabajo.getText());
            registro.setCodigoMedico(((Medico)cmbcodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            
            procedimiento.setInt(1, registro.getCodigoTelefonoMedico());
            procedimiento.setString(2, registro.getTelefonoPersonal());
            procedimiento.setString(3, registro.getTelefonoTrabajo());
            procedimiento.setInt(4, registro.getCodigoMedico());
            procedimiento.execute();
            listaTelefonoMedico.add(registro);            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }   
    }
      
   
      
      public TelefonoMedico buscarTelefonoMedico(int codigoTelefonoMedico){
        TelefonoMedico resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_telefonosMedico(?) }");
            procedimiento.setInt(1, codigoTelefonoMedico);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new TelefonoMedico(registro.getInt("codigoTelefonoMedico"),registro.getString("telefonoPersonal"),registro.getString("telefonoTrabajo"),registro.getInt("codigoMedico"));
            }
            
                
        }catch (SQLException e){
        e.printStackTrace();  
                
            }
            return resultado;
        }
     
      
      
       public void seleccionar(){
        
        txtcodigoTelefonoMedico.setText(""+((TelefonoMedico) tbltelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
        txttelefonoPersonal.setText(((TelefonoMedico)tbltelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        txttelefonoTrabajo.setText(((TelefonoMedico)tbltelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
       cmbcodigoMedico.getSelectionModel().select( buscarTelefonoMedico(((TelefonoMedico)tbltelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico()));
       
        
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case Ninguno:
                activar();
                limpiar();
                btnagregar.setText("Guardar");
                btneliminar.setText("Cancelar");
                btneditar.setDisable(true);
                btnreporte.setDisable(true);
                tipoDeOperacion = operaciones.Guardar;
            break;
            case Guardar:
                ingresar();
                desactivar();
                btnagregar.setText("Nuevo");
                btneliminar.setText("Eliminar");
                btneditar.setDisable(false);
                btnreporte.setDisable(false);
                tipoDeOperacion = operaciones.Ninguno;
                cargarDatos();
            break;
        }
        
    }
    
    public void activar(){
     
        txtcodigoTelefonoMedico.setDisable(false);
        txttelefonoPersonal.setDisable(false);
        txttelefonoTrabajo.setDisable(false);
        cmbcodigoMedico.setDisable(false);
        
        
        
        txtcodigoTelefonoMedico.setEditable(true);
        txttelefonoPersonal.setEditable(true);
        txttelefonoTrabajo.setEditable(true);
         cmbcodigoMedico.setEditable(true);
                
    }
    
    public void limpiar(){
       txtcodigoTelefonoMedico.setText("");
        txttelefonoPersonal.setText("");
        txttelefonoTrabajo.setText("");
        cmbcodigoMedico.getSelectionModel().clearSelection();
   
    }
    
    
    public void ingresar(){
        TelefonoMedico registro = new TelefonoMedico();
            registro.setTelefonoPersonal(txttelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txttelefonoTrabajo.getText());
            registro.setCodigoMedico(((Medico)cmbcodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());       


        try{
            
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregartelefonosMedico (?,?,?)}");
            procedimiento.setString(1, registro.getTelefonoPersonal());
            procedimiento.setString(2, registro.getTelefonoTrabajo());
            procedimiento.setInt(3, registro.getCodigoMedico());
            procedimiento.execute();
            listaTelefonoMedico.add(registro);            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
     public void Eliminar(){
        if(tipoDeOperacion == operaciones.Guardar)
            cancelar();
        else
            tipoDeOperacion = operaciones.Eliminar;
        if(tbltelefonoMedico.getSelectionModel().getSelectedItem() !=null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar?", "Eliminar Telefono Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_NO_OPTION){
                
            try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminartelefonosMedico(?)}");
                    procedimiento.setInt(1,((TelefonoMedico)tbltelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
                    procedimiento.execute();
                    listaTelefonoMedico.remove(tbltelefonoMedico.getSelectionModel().getSelectedIndex());
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
            tipoDeOperacion = operaciones.Nuevo;
        }
        
        
    public void desactivar(){
        
        txtcodigoTelefonoMedico.setDisable(true);
        txttelefonoPersonal.setDisable(true);
        txttelefonoTrabajo.setDisable(true);
        cmbcodigoMedico.setDisable(true);
        
        
        txtcodigoTelefonoMedico.setEditable(false);
        txttelefonoPersonal.setEditable(false);
        txttelefonoTrabajo.setEditable(false);
        cmbcodigoMedico.setEditable(false);
                
    }
    
     public void imprimirReporte(){
        if(tbltelefonoMedico.getSelectionModel().getSelectedItem() != null){
            int codigoTelefonoMedico = ((TelefonoMedico) tbltelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico();
            Map parametros = new HashMap();
            parametros.put("p_codigoTelefonoMedico",codigoTelefonoMedico);
            GenerarReporte.mostrarReporte("reporteTelefonoMedico.jasper","reporteTelefonoMedico", parametros);
            
        }else{
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }
     
     public void imprimirReporteGeneral(){
        if(tbltelefonoMedico.getSelectionModel()//.getSelectedItem() 
                != null){
            //int codigoTelefonoMedico = ((TelefonoMedico) tbltelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico();
            Map parametros = new HashMap();
            //parametros.put("p_codigoTelefonoMedico",codigoTelefonoMedico);
            GenerarReporte.mostrarReporte("reporteTelefonoMedicoGeneral.jasper","reporteTelefonoMedicoGeneral", parametros);
            
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
  
