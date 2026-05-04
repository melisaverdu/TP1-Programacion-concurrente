public class SchedulerTest {
    private static final int CANTIDAD_JOBS = 30;
    private static final int CANTIDAD_SCHEDULERS = 3;

    public static void main(String[] args) throws InterruptedException {
        JobQueue creados = new JobQueue();
        JobQueue enCola = new JobQueue();
        NodeMatrix matriz = new NodeMatrix();

        for (int i = 0; i < CANTIDAD_JOBS; i++) {
            creados.pushJob(new Job(i, EstadoJob.EN_COLA));
        }

        Scheduler scheduler = new Scheduler(enCola, creados, matriz);
        Thread[] hilos = new Thread[CANTIDAD_SCHEDULERS];

        for (int i = 0; i < CANTIDAD_SCHEDULERS; i++) {
            hilos[i] = new Thread(scheduler, "SchedulerTest-" + (i + 1));
            hilos[i].start();
        }

        for (Thread hilo : hilos) {
            hilo.join(5000);
            if (hilo.isAlive()) {
                throw new AssertionError("El scheduler no termino dentro del tiempo esperado.");
            }
        }

        if (!creados.isEmpty()) {
            throw new AssertionError("Quedaron jobs sin procesar en la cola de creados.");
        }

        boolean[] idsProcesados = new boolean[CANTIDAD_JOBS];
        int jobsEnCola = 0;
        Job job = enCola.popJob();

        while (job != null) {
            if (job.getAssignedNodeId() < 0) {
                throw new AssertionError("El job " + job.getID() + " no tiene nodo asignado.");
            }

            if (job.getEstado() != EstadoJob.EN_COLA) {
                throw new AssertionError("El job " + job.getID() + " no quedo en estado EN_COLA.");
            }

            if (job.getID() < 0 || job.getID() >= CANTIDAD_JOBS) {
                throw new AssertionError("El job tiene un ID fuera de rango: " + job.getID());
            }

            if (idsProcesados[job.getID()]) {
                throw new AssertionError("El job " + job.getID() + " fue procesado mas de una vez.");
            }

            idsProcesados[job.getID()] = true;
            jobsEnCola++;
            job = enCola.popJob();
        }

        if (jobsEnCola != CANTIDAD_JOBS) {
            throw new AssertionError("Se esperaban " + CANTIDAD_JOBS + " jobs en cola, pero hubo " + jobsEnCola + ".");
        }

        System.out.println("SchedulerTest OK: todos los jobs fueron asignados y enviados a enCola.");
    }
}
