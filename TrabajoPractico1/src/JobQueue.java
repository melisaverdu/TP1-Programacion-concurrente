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
    private boolean closed;

    public JobQueue() {
        this.queue = new LinkedList<>();
        this.closed = false;
    }

    public synchronized void pushJob(Job job) {
        if (closed) {
            throw new IllegalStateException("No se puede agregar jobs a una cola cerrada");
        }
        queue.addLast(job);
        notifyAll();
    }

    public synchronized Job popJob() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.removeFirst();
    }

    /*
     * El enunciado dice:
     * Cada job debe ser revisado por un solo hilo a la vez
     * Cada job puede ser procesado una sola vez.
     * Entonces yo creo que tendría que elegir uno aleatorio y sacarlo de la cola.
     */
    public synchronized Job popRandomJob() {
        if (queue.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * queue.size());
        return queue.remove(randomIndex);
    }

    public synchronized Job popJobBlocking() throws InterruptedException {
        while (queue.isEmpty() && !closed) {
            wait();
        }

        if (queue.isEmpty()) {
            return null;
        }

        return queue.removeFirst();
    }

    public synchronized Job popRandomJobBlocking() throws InterruptedException {
        while (queue.isEmpty() && !closed) {
            wait();
        }

        if (queue.isEmpty()) {
            return null;
        }

        int randomIndex = (int) (Math.random() * queue.size());
        return queue.remove(randomIndex);
    }

    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }
}
