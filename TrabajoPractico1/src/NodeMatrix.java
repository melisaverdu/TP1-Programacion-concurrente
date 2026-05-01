import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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

    con el lock hacemos un trylock para que si el nodo esta ocupado no se quede esperando el hilo y 
    pueda seguir intentando con otros nodos

    public void releaseNode() -> pone el nodo en "LIBRE"

    public void decommissionNode() -> pone el nodo en "FUERA DE SERVICIO"
 */
public class NodeMatrix {
    private ReentrantLock LockPrinter;
    private final int cantidadNodos;
    
    LockPrinter = new ReentrantLock();
    private final Node[] nodos; // Array para almacenar los Nodos

    public NodeMatrix(int cantidadNodos) {
        this.cantidadNodos = cantidadNodos;
        this.nodos = new Node[cantidadNodos];
        for (int i = 0; i < cantidadNodos; i++) {
            nodos[i] = new Node(i); // Inicializamos cada Nodo con un ID único
        }
    
        public boolean ocupar(){

            LockPrinter.lock();
            try {

                int randomIndex = (int) (Math.random() * cantidadNodos);
                Node nodoSeleccionado = nodos[randomIndex];
                if (nodoSeleccionado.getEstado() == EstadoNode.LIBRE) {
                    nodoSeleccionado.setEstado(EstadoNode.OCUPADO);
                    nodoSeleccionado.incrementarContador();
                    return true; // Nodo ocupado exitosamente
                }
                return false; // Nodo ya estaba ocupado
            } finally {
                LockPrinter.unlock();
            }


        }
    
    


}
