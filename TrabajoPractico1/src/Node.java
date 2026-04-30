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

public class Node {
}
