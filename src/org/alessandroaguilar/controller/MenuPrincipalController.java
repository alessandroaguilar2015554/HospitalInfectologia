
package org.alessandroaguilar.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.alessandroaguilar.bean.Medico;
import org.alessandroaguilar.sistema.Principal;


public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    
    @FXML private Button btnreporteMedicoH;
    
    @Override
    
    
    
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMedico(){
         this.escenarioPrincipal.ventanaMedicos();
    }
    
    public void ventanaProgramador(){
        this.escenarioPrincipal.ventanaProgramadores();
    }
    
      public void ventanaPaciente(){
        this.escenarioPrincipal.ventanaPacientes();
    }
      
     public void ventanaTelefonoMedico(){
         this.escenarioPrincipal.ventanaTelefonoMedicos();
     }
     
     public void ventanaEspecialidades(){
         this.escenarioPrincipal.ventanaEspecialidad();
     }
     
       public void ventanaHorarios(){
         this.escenarioPrincipal.ventanaHorario();
     }
       
        public void ventanaMedicoEspecialidades(){
         this.escenarioPrincipal.ventanaMedicoEspecialidad();
     }
        
         public void ventanaContactoUrgencias(){
         this.escenarioPrincipal.ventanaContactoUrgencia();
     }
         
         
         public void ventanaAreas(){
         this.escenarioPrincipal.ventanaArea();
     }
         
         
         public void ventanaCargos(){
         this.escenarioPrincipal.ventanaCargo();
     }
        
         public void ventanaResponsableTurno(){
             this.escenarioPrincipal.ventanaResponsableTurnos();
         }
         
         public void ventanaTurno(){
             this.escenarioPrincipal.ventanaTurnos();
         }
         
         public void ventanaReceta(){
             this.escenarioPrincipal.ventanaRecetas();
         }
    
         public void ventanaControlCita(){
             this.escenarioPrincipal.ventanaControlCitas();
         }
         
    public void salir(){
        System.exit(0);
}
    
    /*public void imprimirReporteGeneral(){
        if(btnbtnreporteMedicoH.getSelectionModel()//.getSelectedItem() 
                != null){
            //int codigoMedico = ((Medico) tblmedicos.getSelectionModel().getSelectedItem()).getCodigoMedico();
            Map parametros = new HashMap();
            //parametros.put("p_codigoMedico",codigoMedico);
            GenerarReporte.mostrarReporte("reporteMedicosGeneral.jasper","reporteMedicosGeneral", parametros);
            
        }else{
                //JOptionPane.showMessageDialog(null, "Debe Seleccionar un Registro");
          
         }
    }*/
    
}

