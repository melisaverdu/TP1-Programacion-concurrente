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

import java.util.Random;

public class PreExecutionCheck implements Runnable {
    private NodeMatrix nodeMatrix;
    private JobQueue jobsEnCola;
    private JobQueue jobsEnEjecucion;
    private JobQueue jobsFallidos;

    public PreExecutionCheck(NodeMatrix nodeMatrix, JobQueue jobsEnCola, JobQueue jobsEnEjecucion, JobQueue jobsFallidos) {
        this.nodeMatrix = nodeMatrix;
        this.jobsEnCola = jobsEnCola;
        this.jobsEnEjecucion = jobsEnEjecucion;
        this.jobsFallidos = jobsFallidos;
    }

    @Override
    public void run() {
        Random rnd = new Random();
         while (true) {
            //Tenemos que verificar que haya jobs en cola antes de intentar sacar uno
            if (jobsEnCola.isEmpty()) {
                // No hay más jobs en cola, el hilo puede terminar o esperar
                // Ver de mandar a dormir el hilo, en el main o ver si hacerlo aca.
                break;
            }
            
            Job job = jobsEnCola.popRandomJob();
            
            if (job == null) {
                // No hay más jobs en cola, el hilo puede terminar o esperar
                break;
            }

        if (rnd.nextInt(100) < 15) {
            // El job es inválido
            int nodoId = job.getAssignedNodeId();
            nodeMatrix.sacarDeServicio(nodoId); // Pongo el nodo en "FUERA DE SERVICIO"
            job.setEstado(EstadoJob.FALLIDO); // Actualizo el estado del job a "FALLIDO"
            jobsFallidos.pushJob(job); // Agrego el job a la cola de jobs fallidos
        } else {
            // El job es válido
            int nodoId = job.getAssignedNodeId(); //viene del  de la clase Job
            nodeMatrix.desocuparNodo(nodoId); // Pongo el nodo en "LIBRE"
            job.setEstado(EstadoJob.EN_EJECUCION); // Actualizo el estado del job a "EN EJECUCION"
            jobsEnEjecucion.pushJob(job); // Agrego el job a la cola de jobs en ejecución
            }
        }
    }
}

