/*

Etapa 1: Ingreso de Jobs

Ejecutado por 3 hilos

-En cada iteracion cada hilo debe asignar un job a un nodo aleatorio
-Si el nodo esta ocupado -> buscar uno libre
-Una vez asignado -> Nodo pasa a estar "Ocupado"
                  -> Job se registra en *JobsEnCola*

*/
public class Scheduler implements Runnable {

    private static final int DEMORA_MS = 100;

    private final JobQueue enCola;
    private final JobQueue creados;
    private final NodeMatrix matriz;

    public Scheduler(JobQueue enCola, JobQueue creados, NodeMatrix matriz) {
        this.enCola = enCola;
        this.creados = creados;
        this.matriz = matriz;
    }

    @Override
    public void run() {
        Job job = creados.popJob();

        while (job != null) {
            Node nodo = buscarNodoDisponible();

            if (nodo == null) {
                return;
            }

            job.setAssignedNodeId(nodo.getID());
            job.setEstado(EstadoJob.EN_COLA);
            enCola.pushJob(job);

            if (!esperarDemora()) {
                return;
            }

            job = creados.popJob();
        }
    }

    private Node buscarNodoDisponible() {
        Node nodo = matriz.ocuparNodoAleatorio();

        while (nodo == null) {
            if (!esperarDemora()) {
                return null;
            }

            nodo = matriz.ocuparNodoAleatorio();
        }

        return nodo;
    }

    private boolean esperarDemora() {
        try {
            Thread.sleep(DEMORA_MS);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
