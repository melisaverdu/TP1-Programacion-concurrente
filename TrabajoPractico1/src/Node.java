/*
Es una clase simple, representa a un Nodo

Atributos que debe tener:
    ID (int) -> para que sea unico y recnocible
    Estado(String) -> "LIBRE" , "OCUPADO" , "FUERA DE SERVICIO"
    contador de ejecuciones (int) ->

Metodos(sujeto a  cambios) :
    public String getEstado -> No recibe parametros
                            -> Retorna el estado actual del Job

    public int getID -> no recibe parametros
                     -> retorna el ID del Job

    public void incrementarContador -> no recibe parametros
                                    -> incrementa en +1 el contador de ejecuciones
 */

public class Node {
    
    private final int ID; // Atributo para almacenar el ID del Nodo
    private EstadoNode estado; // Atributo para almacenar el estado del Nodo
    private int contadorEjecuciones; // Atributo para contar las ejecuciones asignadas al Nodo
    
    public Node(int id) {
        // Constructor para inicializar el Nodo con un ID
        this.ID = id;
        this.estado = EstadoNode.LIBRE; // Inicializamos el estado como LIBRE por defecto
        this.contadorEjecuciones = 0; // Inicializamos el contador de ejecuciones en 0
    }

    public void setEstado(EstadoNode estado){
        this.estado = estado;
    }

    public EstadoNode getEstado() {
        // Metodo para obtener el estado actual del Nodo
        return this.estado;
    }

    public int getID() {
        // Metodo para obtener el ID del Nodo
        return this.ID;
    }

    public void incrementarContador() {
        // Metodo para incrementar el contador de ejecuciones en +1
        this.contadorEjecuciones++;
    }

    public int getContadorEjecuciones() {
        // Metodo para obtener el contador de ejecuciones
        return this.contadorEjecuciones;
    }
}
