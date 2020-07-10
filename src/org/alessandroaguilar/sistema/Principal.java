
package org.alessandroaguilar.sistema;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.alessandroaguilar.controller.AreasController;
import org.alessandroaguilar.controller.CargosController;
import org.alessandroaguilar.controller.ContactoUrgenciaController;
import org.alessandroaguilar.controller.ControlCitasController;
import org.alessandroaguilar.controller.EspecialidadController;
import org.alessandroaguilar.controller.HorariosController;
import org.alessandroaguilar.controller.MedicoController;
import org.alessandroaguilar.controller.MedicoEspecialidadController;
import org.alessandroaguilar.controller.MenuPrincipalController;
import org.alessandroaguilar.controller.PacientesController;
import org.alessandroaguilar.controller.ProgramadorController;
import org.alessandroaguilar.controller.RecetasController;
import org.alessandroaguilar.controller.ResponsableTurnoController;
import org.alessandroaguilar.controller.TelefonoMedicoController;
import org.alessandroaguilar.controller.TurnosController;
import org.alessandroaguilar.db.Conexion;

/**
 *
 * @author programacion
 */
public class Principal extends Application {
   public final String PAQUETE_VISTA = "/org/alessandroaguilar/view/";
   public Stage escenarioPrincipal;
   private Scene escena;
    @Override
    public void start(Stage escenarioPrincipal) {
        
       this.escenarioPrincipal = escenarioPrincipal;
       escenarioPrincipal.setTitle("Hospital de Infectologia");
       menuPrincipal();
       escenarioPrincipal.show();
    }
   public void menuPrincipal(){
    
        try{
           MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",599,330);
           menuPrincipal.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }


    
     public void ventanaMedicos(){
    
         
         try{
           MedicoController medicoController = (MedicoController)cambiarEscena("MedicoView.fxml",785,451);
           medicoController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
     }
    
    /**
     *
     */
    public void ventanaProgramadores(){
    
         
         try{
           ProgramadorController programadorController = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",600,336);
           programadorController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }
     
    
    public void ventanaPacientes(){
    
         
         try{
           PacientesController pacientesController = (PacientesController)cambiarEscena("PacientesView.fxml",854,479);
           pacientesController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }
    
    
   public void ventanaTelefonoMedicos(){
    
         
         try{
           TelefonoMedicoController telefonoMedicoController = (TelefonoMedicoController)cambiarEscena("TelefonoMedicoView.fxml",952,534);
           telefonoMedicoController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }
   
   
    public void ventanaEspecialidad(){
    
         
         try{
           EspecialidadController especialidadController = (EspecialidadController)cambiarEscena("EspecialidadesView.fxml",704,394);
           especialidadController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }
    
    public void ventanaHorario(){
    
         try{
           HorariosController horariosController = (HorariosController)cambiarEscena("HorariosView.fxml",693,458);
           horariosController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }
    
    
    public void ventanaMedicoEspecialidad(){
    
         try{
           MedicoEspecialidadController medicoEspecialidadController = (MedicoEspecialidadController)cambiarEscena("MedicoEspecialidadView.fxml",634,445);
           medicoEspecialidadController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }
    
     public void ventanaContactoUrgencia(){
    
         try{
           ContactoUrgenciaController contactoUrgenciaController = (ContactoUrgenciaController)cambiarEscena("ContactoUrgenciaView.fxml",720,400);
           contactoUrgenciaController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }
     
     
     public void ventanaArea(){
    
         try{
           AreasController areasController = (AreasController)cambiarEscena("AreasView.fxml",523,523);
           areasController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }
     
     
      public void ventanaCargo(){
    
         try{
           CargosController cargosController = (CargosController)cambiarEscena("CargosView.fxml",589,454);
          cargosController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       }  
    }
      
      public void ventanaResponsableTurnos(){
    
         try{
           ResponsableTurnoController responsableTurnoController = (ResponsableTurnoController)cambiarEscena("ResponsableTurnoView.fxml",647,408);
          responsableTurnoController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }
      
      public void ventanaTurnos(){
    
         try{
           TurnosController turnosController = (TurnosController)cambiarEscena("TurnosView.fxml",626,406);
          turnosController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       } 
    }
    
   public void ventanaRecetas(){
       
       try{
           RecetasController recetasController = (RecetasController)cambiarEscena("RecetasView.fxml",721,405);
           recetasController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }   
   
   public void ventanaControlCitas(){
       try{
           ControlCitasController controlCitasController = (ControlCitasController)cambiarEscena("ControlCitasView.fxml",781,437);
           controlCitasController.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
      
      
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
        
    }
   
    public static void main(String[] args) {
        launch(args);
    }
    
}
