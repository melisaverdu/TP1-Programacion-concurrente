/*

Etapa 3 : Ejacucion del Job
Ejecutado por 3 hilos

- Cada hilo toma un Job de JobsEnEjecucion
- Simula su ejecucion con probabilidad -> 90% exito
                                       -> 10% error en ejecucion

Si el Job es exitoso -> pasa a JobsFinalizados

Si el Job tiene un Error -> pasa a JobsFallidos

 */

import java.util.concurrent.ThreadLocalRandom;

public class WorkerExecution implements Runnable {

    private static final int DEMORA_MS = 100;

    private final JobQueue jobsEnEjecucion;
    private final JobQueue jobsFinalizados;
    private final JobQueue jobsFallidos;

    public WorkerExecution(JobQueue jobsEnEjecucion, JobQueue jobsFinalizados, JobQueue jobsFallidos) {
        this.jobsEnEjecucion = jobsEnEjecucion;
        this.jobsFinalizados = jobsFinalizados;
        this.jobsFallidos = jobsFallidos;
    }

    @Override
    public void run() {
        while (true) {
            Job job;

            try {
                job = jobsEnEjecucion.popJobBlocking();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (job == null) {
                return;
            }

            if (ThreadLocalRandom.current().nextInt(100) < 10) {
                job.setEstado(EstadoJob.FALLIDO);
                jobsFallidos.pushJob(job);
            } else {
                job.setEstado(EstadoJob.FINALIZADO);
                jobsFinalizados.pushJob(job);
            }

            try {
                Thread.sleep(DEMORA_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
