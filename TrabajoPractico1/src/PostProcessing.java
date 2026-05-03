/*
Etapa 4: Verificacion final
Ejecutado por 2 hilos

-Cada hilo toma un job de JobsFinalizados
Se valida el resultado -> 95% correcto
                       -> 5% inconsistente

Si el Job es correcto -> pasa a JobsValidados
Si el Job es incorrecto -> pasa a JobsFallidos
 */

import java.util.concurrent.ThreadLocalRandom;

public class PostProcessing implements Runnable{
    
    private JobQueue jobsFinalizados;
    private JobQueue jobsValidados;
    private JobQueue jobsFallidos;

    public PostProcessing(JobQueue jobsFinalizados, JobQueue jobsValidados, JobQueue jobsFallidos) {
        this.jobsFinalizados = jobsFinalizados;
        this.jobsValidados = jobsValidados;
        this.jobsFallidos = jobsFallidos;
    }

    @Override
    public void run() {
        while (true) {
            Job job;

            try {
                job = jobsFinalizados.popRandomJobBlocking();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            if (job == null) {
                break;
            }

            if (ThreadLocalRandom.current().nextInt(100) < 5) {
                job.setEstado(EstadoJob.FALLIDO);
                jobsFallidos.pushJob(job);
            } else {
                job.setEstado(EstadoJob.VALIDADO);
                jobsValidados.pushJob(job);
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
