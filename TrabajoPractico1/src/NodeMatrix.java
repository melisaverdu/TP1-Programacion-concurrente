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
    

        public Node ocuparNodoAleatorio(){
 /*
sheduler necesita saber que nodo ocupar para asignar el job
             
Tiene que devolver uun Node si logró ocupar uno
null si justo elijió uno no disponible 
            
Me parece que lo mejor es que el scheduler siga intentando hasta que consiga ocupar un nodo, 
no se queda esperando por uno específico, porque la consigna de esa etapa dice "si el nodo está libre, debe buscar
otro nodo disponiblñe"
*/
            LockPrinter.lock();                                             // adquiero el lock
            try {
                int randomIndex = (int) (Math.random() * cantidadNodos);    // genero un indice al azar entre 0 y cantidadNodos-1
                Node nodo = nodos[randomIndex];                             // obtengo el nodo correspondiente al indice generado   

                if (nodo.getEstado() == EstadoNode.LIBRE) {                 // verifico si el nodo esta libre
                nodo.setEstado(EstadoNode.OCUPADO);                         // si esta libre, lo ocupo
                nodo.incrementarContador();                                 // incremento el contador de ejecuciones del nodo 
                return nodo;                                                // retorno el nodo ocupado
                }

            return null;
        } finally {
            LockPrinter.unlock();
        }
        }
    }
}



