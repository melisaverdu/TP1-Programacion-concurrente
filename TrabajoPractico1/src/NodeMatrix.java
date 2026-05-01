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
    private ReentrantLock LockPrinter = new ReentrantLock();
    private final int cantidadNodos = 200;
    

    private final Node[] nodos; // Array para almacenar los Nodos

    public NodeMatrix() {
        this.nodos = new Node[cantidadNodos];
        for (int i = 0; i < cantidadNodos; i++) {
            nodos[i] = new Node(i); // Inicializamos cada Nodo con un ID único
        }
    }

        private Node ocuparNodoAleatorio(){

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
                }
                finally {
                LockPrinter.unlock();
                }
        }


    private boolean setFueradeServicio(int idNodo){
        /*
        En la etapa 2, si el job es inválido, el nodo asociado queda fuera de servicio.
        */
        LockPrinter.lock();
        try {
            if (idNodo >= 0 && idNodo < cantidadNodos) { // Verifico que el ID del nodo sea válido
                Node nodo = nodos[idNodo];
                nodo.setEstado(EstadoNode.FUERA_DE_SERVICIO); // Pongo el nodo en "FUERA DE SERVICIO"
                return true; // Retorno true si se pudo sacar de servicio
            }
            return false; // Retorno false si el ID del nodo no es válido
        } finally {
            LockPrinter.unlock();
        }
    }

    private boolean setLibre(int idNodo){
        /*
        En la etapa 2,una vez que el job es validado, el nodo asociado queda libre nuevamente.
        */
        LockPrinter.lock();
        try {
            if (idNodo >= 0 && idNodo < cantidadNodos) { // Verifico que el ID del nodo sea válido
                Node nodo = nodos[idNodo];
                nodo.setEstado(EstadoNode.LIBRE); // Pongo el nodo en "LIBRE"
                return true; // Retorno true si se pudo liberar el nodo
            }
            return false; // Retorno false si el ID del nodo no es válido
        } finally {
            LockPrinter.unlock();
        }
    }

    public boolean desocuparNodo(int idNodo){
        /*
        En la etapa 3, una vez que el job finaliza su ejecución, el nodo asociado queda libre nuevamente.
        */
        return setLibre(idNodo);
    }

    public boolean sacarDeServicio(int idNodo){
        /*
        En la etapa 2, si el job es inválido, el nodo asociado queda fuera de servicio.
        */
        return setFueradeServicio(idNodo);
    }
    
}





