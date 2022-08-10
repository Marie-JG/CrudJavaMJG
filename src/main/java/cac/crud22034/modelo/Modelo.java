
package cac.crud22034.modelo;

import java.util.List;


public interface Modelo {
  
    public List<Paciente> getPacientes();
    
 
    public Paciente getPaciente(int id);
    

    public int addPaciente(Paciente paciente);
    
    
    public int updatePaciente(Paciente paciente);
    
    
    public int removePaciente(int id);
}
