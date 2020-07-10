/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alessandroaguilar.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.alessandroaguilar.bean.MedicoEspecialidad;
import org.alessandroaguilar.bean.Pacientes;
import org.alessandroaguilar.bean.ResponsableTurno;
import org.alessandroaguilar.bean.Turnos;
import org.alessandroaguilar.sistema.Principal;

/**
 *
 * @author aless
 */
public class TurnosController implements Initializable{
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno};
    private operaciones tipoOperaciones = operaciones.Ninguno;
    private ObservableList <Turnos> listarTurno;
    private ObservableList <MedicoEspecialidad> listarMedicoEspecialidad;
    private ObservableList <ResponsableTurno> listarResponsableTurno;
    private ObservableList <Pacientes> listarPacientes;
    private Principal escenarioPrincipal;

    
    /*
       @FXML private ComboBox cmbcodigoMedicoEspecialidad;
       @FXML private ComboBox cmbcodigoMedico;
       @FXML private ComboBox cmbcodigoEspecialidad;
       @FXML private ComboBox cmbcodigoHorario;
      
       @FXML private TableView tblespecialidades;
       @FXML private TableColumn colcodigoMe;
       @FXML private TableColumn colcodigoMedico;
       @FXML private TableColumn colcodigoEspecialidad;
       @FXML private TableColumn colcodigoHorario;
      
       @FXML private Button btnagregar;
       @FXML private Button btneditar;
       @FXML private Button btneliminar;
       @FXML private Button btnreporte; 
       @FXML private Button btnreporteGeneral;
     
*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
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
