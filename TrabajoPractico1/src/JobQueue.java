import java.util.LinkedList;

/*
Es una clase base para las colas que vamos a necesitar

Atributos que debe tener:


Metodos(sujeto a  cambios) :
    public void pushJob(Job job) -> agrega un hilo al final de la cola, debe ser synchronized

    public Job popJob() -> saca y retorna el primer job de la cola

    public Job getRandomJob() retorna un job aleatorio de la cola

    public isEmpty() -> retorna true si la cola esta vacia, false en caso contrario
    
 */
public class JobQueue {
    
    private final LinkedList<Job> queue; // Cola para almacenar los Jobs

    public JobQueue() {
        this.queue = new LinkedList<>();
    }

    public synchronized void pushJob(Job job) {
        queue.addLast(job);
    }

    public synchronized Job popJob() { 
        if (queue.isEmpty()) {
            return null;
        }
        return queue.removeFirst();
    }

    /*El enunciado dice:
        Cada job debe ser revisado por un solo hilo a la vez
        Cada job puede ser procesado una sola vez.
        Entonces yo creo que tendría que elegir uno aleatorio y sacarlo de la cola.
     */
     public synchronized Job popRandomJob() {
        if (queue.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * queue.size());
        return queue.remove(randomIndex);
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
