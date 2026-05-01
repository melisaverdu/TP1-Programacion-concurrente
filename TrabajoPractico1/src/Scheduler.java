/*

Etapa 1: Ingreso de Jobs

Ejecutado por 3 hilos

-En cada iteracion cada hilo debe asignar un job a un nodo aleatorio
-Si el nodo esta ocupado -> buscar uno libre
-Una vez asignado -> Nodo pasa a estar "Ocupado"
                  -> Job se registra en *JobsEnCola*

*/
public class Scheduler implements Runnable{

    JobQueue enCola;
    JobQueue creados;
    NodeMatrix matriz;

    public Scheduler(JobQueue enCola, JobQueue creados, NodeMatrix matriz) {
        this.enCola = enCola;
        this.creados = creados;
        this.matriz = matriz;
    }

    @Override
    public void run(){
        Job job = creados.popJob(); // saco un job de los creados
        if (job != null) { // si hay un job para procesar
            Node nodo = matriz.ocuparNodoAleatorio(); // intento ocupar un nodo aleatorio

            while (nodo == null) { // si no pude ocupar un nodo, sigo intentando
                nodo = matriz.ocuparNodoAleatorio();
                if(nodo == null) {
                    try {
                        Thread.sleep(100); // espero un poco antes de intentar de nuevo para evitar un ciclo muy rápido
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // restablecer el estado de interrupción
                    }
                }
            }
            job.setAssignedNodeId(nodo.getID()); // asigno el nodo al job
            enCola.pushJob(job); // agrego el job a la cola de jobs en espera de validacion
        }
    }
}
