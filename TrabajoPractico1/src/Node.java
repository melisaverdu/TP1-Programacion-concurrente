/*
Es una clase simple, representa a un Nodo

Atributos que debe tener:
    ID (int) -> para que sea unico y reconocible
    Estado(String) -> "LIBRE" , "OCUPADO" , "FUERA DE SERVICIO"
    contador de ejecuciones (int) -> se incrementa cada vez que se asigna un Job al nodo, se utiliza para el algoritmo de asignacion de Jobs

Metodos(sujeto a  cambios) :
    public String getEstado -> No recibe parametros
                            -> Retorna el estado actual del Job

    public int getID -> no recibe parametros
                     -> retorna el ID del Job

    public void incrementarContador -> no recibe parametros
                                    -> incrementa en +1 el contador de ejecuciones

    public setID -> establece el ID del job
 */

import java.util.concurrent.locks.ReentrantLock;

public class Node {
    
    private final int ID; // Atributo para almacenar el ID del Nodo
    private EstadoNode estado; // Atributo para almacenar el estado del Nodo
    private int contadorEjecuciones; // Atributo para contar las ejecuciones asignadas al Nodo
    private final ReentrantLock lock; // Protege el estado y contador de este nodo
    
    public Node(int id) {
        // Constructor para inicializar el Nodo con un ID
        this.ID = id;
        this.estado = EstadoNode.LIBRE; // Inicializamos el estado como LIBRE por defecto
        this.contadorEjecuciones = 0; // Inicializamos el contador de ejecuciones en 0
        this.lock = new ReentrantLock();
    }

    public boolean intentarOcupar() {
        if (!lock.tryLock()) {
            return false;
        }

        try {
            if (estado != EstadoNode.LIBRE) {
                return false;
            }

            estado = EstadoNode.OCUPADO;
            contadorEjecuciones++;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void setEstado(EstadoNode estado) {
        lock.lock();
        try {
            this.estado = estado;
        } finally {
            lock.unlock();
        }
    }

    public EstadoNode getEstado() {
        lock.lock();
        try {
            // Metodo para obtener el estado actual del Nodo
            return this.estado;
        } finally {
            lock.unlock();
        }
    }

    public int getID() {
        // Metodo para obtener el ID del Nodo
        return this.ID;
    }

    public void incrementarContador() {
        lock.lock();
        try {
            // Metodo para incrementar el contador de ejecuciones en +1
            this.contadorEjecuciones++;
        } finally {
            lock.unlock();
        }
    }

    public int getContadorEjecuciones() {
        lock.lock();
        try {
            // Metodo para obtener el contador de ejecuciones
            return this.contadorEjecuciones;
        } finally {
            lock.unlock();
        }
    }
}
