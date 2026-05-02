public class PreExecutionCheckTest {

    private static final int CANTIDAD_JOBS = 30;
    private static final int CANTIDAD_PREEXECUTION = 2;

    public static void main(String[] args) throws InterruptedException {

        JobQueue enCola = new JobQueue();
        JobQueue enEjecucion = new JobQueue();
        JobQueue fallidos = new JobQueue();
        NodeMatrix matriz = new NodeMatrix();

        // Simulamos jobs ya asignados a nodos (como si vinieran del Scheduler)
        for (int i = 0; i < CANTIDAD_JOBS; i++) {
            Node nodo = null;

            // Aseguramos que el job tenga un nodo ocupado
            while (nodo == null) {
                nodo = matriz.ocuparNodoAleatorio();
            }

            Job job = new Job(i, EstadoJob.EN_COLA);
            job.setAssignedNodeId(nodo.getID());

            enCola.pushJob(job);
        }

        // Creamos los hilos de validación
        Thread[] hilos = new Thread[CANTIDAD_PREEXECUTION];

        for (int i = 0; i < CANTIDAD_PREEXECUTION; i++) {
            hilos[i] = new Thread(
                new PreExecutionCheck(matriz, enCola, enEjecucion, fallidos),
                "PreExecutionCheck-" + (i + 1)
            );
            hilos[i].start();
        }

        // Esperamos a que terminen
        for (Thread hilo : hilos) {
            hilo.join(5000);// Esperamos un máximo de 5 segundos por hilo
            if (hilo.isAlive()) {
                throw new AssertionError("El PreExecutionCheck no terminó a tiempo.");
            }
        }

        // Validaciones

        int totalProcesados = 0;

        Job job;

        // Revisar jobs en ejecución
        while ((job = enEjecucion.popJob()) != null) {
            if (job.getEstado() != EstadoJob.EN_EJECUCION) {
                throw new AssertionError("Job en ejecución con estado incorrecto.");
            }
            totalProcesados++;
        }

        // Revisar jobs fallidos
        while ((job = fallidos.popJob()) != null) {
            if (job.getEstado() != EstadoJob.FALLIDO) {
                throw new AssertionError("Job fallido con estado incorrecto.");
            }
            totalProcesados++;
        }

        // Verificar que TODOS los jobs fueron procesados
        if (totalProcesados != CANTIDAD_JOBS) {
            throw new AssertionError("No se procesaron todos los jobs. Procesados: " + totalProcesados);
        }

        // Verificar que la cola quedó vacía
        if (!enCola.isEmpty()) {
            throw new AssertionError("Quedaron jobs sin procesar en la cola.");
        }

        System.out.println("PreExecutionCheckTest OK: todos los jobs fueron validados correctamente.");
    }
} 
