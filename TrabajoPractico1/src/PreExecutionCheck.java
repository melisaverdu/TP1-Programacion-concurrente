/*
Etapa 2 : Validacion de Job
Ejecutado por 2 hilos

-Cada hilo toma un job aleatorio de JobsEnCola
-Se valida la configuracion del job -> 85% valido
                                    -> 15% invalido

Implementar que tome como parámetro el id del nodo
Si el Job es valido :
            El nodo vuelva a estar libre
            El Job pasa a JobsEnEjecucion

Si el Job es invalido :
            El nodo pasa a "Fuera de servicio"
            El Job pasa a JobsFallidos
            
El constructor  recibe como parámetro la matriz de nodos, los jobs en cola, jobs en ejecucion y jobs fallidos

 */

import java.util.concurrent.ThreadLocalRandom;

public class PreExecutionCheck implements Runnable {

    private NodeMatrix nodeMatrix;
    private JobQueue jobsEnCola;
    private JobQueue jobsEnEjecucion;
    private JobQueue jobsFallidos;

    public PreExecutionCheck(NodeMatrix nodeMatrix, JobQueue jobsEnCola,
                             JobQueue jobsEnEjecucion, JobQueue jobsFallidos) {
        this.nodeMatrix = nodeMatrix;
        this.jobsEnCola = jobsEnCola;
        this.jobsEnEjecucion = jobsEnEjecucion;
        this.jobsFallidos = jobsFallidos;
    }

    @Override
    public void run() {

        while (true) {
            Job job;

            try {
                job = jobsEnCola.popRandomJobBlocking();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            if (job == null) {
                break;
            }

            int nodoId = job.getAssignedNodeId();

            if (ThreadLocalRandom.current().nextInt(100) < 15) {
                nodeMatrix.sacarDeServicio(nodoId);
                job.setEstado(EstadoJob.FALLIDO);
                jobsFallidos.pushJob(job);
            } else {
                nodeMatrix.desocuparNodo(nodoId);
                job.setEstado(EstadoJob.EN_EJECUCION);
                jobsEnEjecucion.pushJob(job);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
