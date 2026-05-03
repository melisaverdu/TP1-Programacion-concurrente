/*
Hilo principal
*/
public class Main {
    private static final int CANTIDAD_JOBS = 500;
    private static final int CANTIDAD_SCHEDULERS = 3;
    private static final int CANTIDAD_PREEXECUTION = 2;
    private static final int CANTIDAD_EJECUTORES = 10;
    private static final int CANTIDAD_POSTPROCESSING = 2;

    public static void main(String[] args) {
        JobQueue creados = new JobQueue(); // Cola de jobs creados
        JobQueue enCola = new JobQueue(); // Cola de jobs en espera de validacion
        JobQueue enEjecucion = new JobQueue(); // Cola de jobs en ejecucion
        JobQueue finalizados = new JobQueue(); // Cola de jobs finalizados
        JobQueue fallidos = new JobQueue(); // Cola de jobs fallidos
        JobQueue validados = new JobQueue(); // Cola de jobs validados

        NodeMatrix matriz = new NodeMatrix(); // Matriz de nodos

        for (int i = 0; i < CANTIDAD_JOBS; i++) {
            creados.pushJob(new Job(i, EstadoJob.EN_COLA)); // Creo los jobs y los agrego a la cola de creados
        }

        Scheduler scheduler = new Scheduler(enCola, creados, matriz); // Creo el scheduler, le paso las colas y la
                                                                      // matriz de nodos
        Thread[] schedulerThreads = new Thread[CANTIDAD_SCHEDULERS]; // Creo los hilos del scheduler

        for (int i = 0; i < CANTIDAD_SCHEDULERS; i++) {
            schedulerThreads[i] = new Thread(scheduler, "Scheduler-" + (i + 1)); // Creo cada hilo del scheduler con un
                                                                                 // nombre identificativo
        }

        Thread[] validatorThreads = new Thread[CANTIDAD_PREEXECUTION];

        for (int i = 0; i < CANTIDAD_PREEXECUTION; i++) {
            validatorThreads[i] = new Thread(
                    new PreExecutionCheck(matriz, enCola, enEjecucion, fallidos),
                    "PreExecutionCheck-" + (i + 1));
        }

        Thread[] workerExecutionThreads = new Thread[CANTIDAD_EJECUTORES];
        for (int i = 0; i < CANTIDAD_EJECUTORES; i++) {
            workerExecutionThreads[i] = new Thread(
                    new WorkerExecution(enEjecucion, finalizados, fallidos),
                    "WorkerExecution-" + (i + 1));
        }

        Thread[] postProcessingThreads = new Thread[CANTIDAD_POSTPROCESSING];
        for (int i = 0; i < CANTIDAD_POSTPROCESSING; i++) {
            postProcessingThreads[i] = new Thread(
                    new PostProcessing(finalizados, validados, fallidos),
                    "PostProcessing-" + (i + 1));
        }

        for (Thread t : schedulerThreads)
            t.start();
        System.out.println("Schedulers iniciados...");
        for (Thread t : validatorThreads)
            t.start();
        System.out.println("PreExecutionChecks iniciados...");
        for (Thread t : workerExecutionThreads)
            t.start();
        System.out.println("WorkerExecutions iniciados...");
        for (Thread t : postProcessingThreads)
            t.start();
        System.out.println("PostProcessings iniciados...");

        System.out.println("Sistema en ejecución...");

        while (validados.size() + fallidos.size() < CANTIDAD_JOBS) {
            try {
                Thread.sleep(200);
                System.out.println("Jobs en cola: " + enCola.size() + "| Jobs validados: " + validados.size()
                        + " | Jobs fallidos: " + fallidos.size());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        esperarHilos(schedulerThreads);
        enCola.close();

        esperarHilos(validatorThreads);
        enEjecucion.close();

        esperarHilos(workerExecutionThreads);
        finalizados.close();

        esperarHilos(postProcessingThreads);

        System.out.println("Sistema finalizado.");
    }

    private static void esperarHilos(Thread[] threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
