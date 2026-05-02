/*
Es una clase simple, representa a un Job

Atributos que debe tener:
    ID (int) -> para que sea unico y recnocible
    Estado(String) -> "EN COLA" , "EN EJECUCION" , "FINALIZADO" , "FALLIDO" , "VALIDADOS"

Metodos(sujeto a  cambios) :
    public String getEstado -> No recibe parametros
                            -> Retorna el estado actual del Job
    
    public void setEstado -> Recibe un String con el nuevo estado del Job
                            -> Establece el nuevo estado del Job

    public int getID -> no recibe parametros
                     -> retorna el ID del Job

    public void setID -> establece el ID del job

 */


public class Job {
  
    // Enum para representar los posibles estados de un Job
    
    private final int ID; // Atributo para almacenar el ID del Job
    private EstadoJob estado; // Atributo para almacenar el estado del Job
    private int nodoAsignado; // Para saber en qué nodo se está procesando
    
    public Job(int id, EstadoJob estado) {
        // Constructor para inicializar el Job con un ID y un estado
        this.ID = id;
        this.estado = estado;
        this.nodoAsignado = -1;
    }

    public EstadoJob getEstado() {
        // Metodo para obtener el estado actual del Job
        return this.estado;
    }

    public void setEstado(EstadoJob nuevoEstado) {
        // Metodo para establecer un nuevo estado para el Job
        this.estado = nuevoEstado;
    }

    public int getID() {
        // Metodo para obtener el ID del Job
        return this.ID;
    }

    public int getAssignedNodeId(){
        return this.nodoAsignado;
    }

    public void setAssignedNodeId(int assignedNodeId) {
        this.nodoAsignado = assignedNodeId;
    }
}
