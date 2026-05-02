import java.lang.reflect.Constructor;

public class WorkerExecutionTest {

    private static final int CANTIDAD_JOBS = 30;
    private static final int CANTIDAD_WORKERS = 3;

    public static void main(String[] args) throws Exception {
        JobQueue enEjecucion = new JobQueue();
        JobQueue finalizados = new JobQueue();
        JobQueue fallidos = new JobQueue();

        for (int i = 0; i < CANTIDAD_JOBS; i++) {
            Job job = new Job(i, EstadoJob.EN_EJECUCION);
            job.setAssignedNodeId(i);
            enEjecucion.pushJob(job);
        }

        Runnable workerExecution = crearWorkerExecution(enEjecucion, finalizados, fallidos);
        Thread[] hilos = new Thread[CANTIDAD_WORKERS];

        for (int i = 0; i < CANTIDAD_WORKERS; i++) {
            hilos[i] = new Thread(workerExecution, "WorkerExecutionTest-" + (i + 1));
            hilos[i].start();
        }

        for (Thread hilo : hilos) {
            hilo.join(5000);
            if (hilo.isAlive()) {
                throw new AssertionError("El WorkerExecution no termino dentro del tiempo esperado.");
            }
        }

        if (!enEjecucion.isEmpty()) {
            throw new AssertionError("Quedaron jobs sin procesar en la cola de ejecucion.");
        }

        boolean[] idsProcesados = new boolean[CANTIDAD_JOBS];
        int totalProcesados = 0;

        totalProcesados += validarCola(finalizados, EstadoJob.FINALIZADO, idsProcesados);
        totalProcesados += validarCola(fallidos, EstadoJob.FALLIDO, idsProcesados);

        if (totalProcesados != CANTIDAD_JOBS) {
            throw new AssertionError("Se esperaban " + CANTIDAD_JOBS + " jobs procesados, pero hubo " + totalProcesados + ".");
        }

        System.out.println("WorkerExecutionTest OK: todos los jobs fueron ejecutados correctamente.");
    }

    private static Runnable crearWorkerExecution(JobQueue enEjecucion, JobQueue finalizados, JobQueue fallidos)
            throws Exception {
        try {
            Constructor<WorkerExecution> constructor =
                    WorkerExecution.class.getConstructor(JobQueue.class, JobQueue.class, JobQueue.class);
            return constructor.newInstance(enEjecucion, finalizados, fallidos);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(
                    "Falta implementar el constructor WorkerExecution(JobQueue enEjecucion, JobQueue finalizados, JobQueue fallidos)."
            );
        }
    }

    private static int validarCola(JobQueue cola, EstadoJob estadoEsperado, boolean[] idsProcesados) {
        int cantidad = 0;
        Job job = cola.popJob();

        while (job != null) {
            if (job.getEstado() != estadoEsperado) {
                throw new AssertionError("El job " + job.getID() + " tiene estado incorrecto.");
            }

            if (job.getID() < 0 || job.getID() >= idsProcesados.length) {
                throw new AssertionError("El job tiene un ID fuera de rango: " + job.getID());
            }

            if (idsProcesados[job.getID()]) {
                throw new AssertionError("El job " + job.getID() + " fue procesado mas de una vez.");
            }

            idsProcesados[job.getID()] = true;
            cantidad++;
            job = cola.popJob();
        }

        return cantidad;
    }
}
