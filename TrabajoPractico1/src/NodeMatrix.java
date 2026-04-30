/*
Implementa una matriz de Nodos
ES UN RECURSO COMPARTIDO ENTRE HILOS CADA VEZ QUE SE USE
DEBE OBLIGATORIAMENTE HABER EXCLUSION MUTUA

Atributos (sujeto a cambios):
    Cant Nodos(int) -> establece cuantos nodos tiene la matriz (opcional)

Metodos(sujeto a  cambios) :
    public Node tryAcquireRandomNode() -> intenta que un hilo tome un nodo si esta libre
    se puede hacer con reentrant Lock     1- genera un indice al azar entre 0 y 199
    para que solo 1 hilo intente tomar    2- revisa si el nodo esta libre
    1 nodo a la vez                       3- cambia en nodo a ocupado , incrementa el contador y returna el nodo
                                          4- si el nodo elejido esta ocupado devuelve NULL

    public void releaseNode() -> pone el nodo en "LIBRE"

    public void decommissionNode() -> pone el nodo en "FUERA DE SERVICIO"

    
 */
public class NodeMatrix {
}
