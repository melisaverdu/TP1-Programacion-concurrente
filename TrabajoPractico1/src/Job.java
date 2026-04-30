/*
Es una clase simple, representa a un Job

Atributos que debe tener:
    ID (int) -> para que sea unico y recnocible
    Estado(String) -> "EN COLA" , "EN EJECUCION" , "FINALIZADO" , "FALLIDO" , "VALIDADOS"

Metodos(sujeto a  cambios) :
    public String getEstado -> No recibe parametros
                            -> Retorna el estado actual del Job

    public int getID -> no recibe parametros
                     -> retorna el ID del Job

    public void setID -> establece el ID del job

 */


public class Job {
  
    // Enum para representar los posibles estados de un Job
    public enum Estado {
        SIN_ESTADO,
        EN_COLA,
        EN_EJECUCION,
        FINALIZADO,
        FALLIDO,
        VALIDADOS
    }
    
    private final int ID; // Atributo para almacenar el ID del Job
    private Estado estado; // Atributo para almacenar el estado del Job
    private int nodoAsignado; // Para saber en qué nodo se está procesando
    
    public Job(int id, Estado estado) {
        // Constructor para inicializar el Job con un ID y un estado
        this.ID = id;
        this.estado = Estado.SIN_ESTADO; // Inicializamos el estado como SIN_ESTADO por defecto
        this.nodoAsignado = -1; // Para saber en qué nodo se está procesando
    }

    public Estado getEstado() {
        // Metodo para obtener el estado actual del Job
        return this.estado;
    }

    public void setEstado(Estado nuevoEstado) {
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
