/*
Hilo principal
*/
public class Main {
    private static final int CANTIDAD_JOBS = 500;
    private static final int CANTIDAD_SCHEDULERS = 3;
    private static final int CANTIDAD_PREEXECUTION = 2;

    public static void main(String[] args) {
        JobQueue creados = new JobQueue();                  // Cola de jobs creados
        JobQueue enCola = new JobQueue();                   // Cola de jobs en espera de validacion
        JobQueue enEjecucion = new JobQueue();              // Cola de jobs en ejecucion
        JobQueue finalizados = new JobQueue();              // Cola de jobs finalizados
        JobQueue fallidos = new JobQueue();                 // Cola de jobs fallidos
        JobQueue validados = new JobQueue();                // Cola de jobs validados

        NodeMatrix matriz = new NodeMatrix();               // Matriz de nodos

        for (int i = 0; i < CANTIDAD_JOBS; i++) {
            creados.pushJob(new Job(i, EstadoJob.EN_COLA)); // Creo los jobs y los agrego a la cola de creados
        }

        Scheduler scheduler = new Scheduler(enCola, creados, matriz);   // Creo el scheduler, le paso las colas y la matriz de nodos
        Thread[] schedulerThreads = new Thread[CANTIDAD_SCHEDULERS];   // Creo los hilos del scheduler

        for (int i = 0; i < CANTIDAD_SCHEDULERS; i++) {
            schedulerThreads[i] = new Thread(scheduler, "Scheduler-" + (i + 1)); // Creo cada hilo del scheduler con un nombre identificativo
        }

        /*
        Estos hilos quedan preparados, pero conviene iniciarlos junto con PreExecutionCheck.
        Si se ejecuta Scheduler solo con 500 jobs, despues de ocupar 200 nodos nadie libera
        recursos y los hilos quedan esperando nodos disponibles.
        */
        System.out.println("Sistema inicializado con " + CANTIDAD_JOBS + " jobs y " + CANTIDAD_SCHEDULERS + " schedulers.");

    
     Thread[] validatorThreads = new Thread[CANTIDAD_PREEXECUTION];

        for (int i = 0; i < CANTIDAD_PREEXECUTION; i++) {
            validatorThreads[i] = new Thread(
                new PreExecutionCheck(matriz, enCola, enEjecucion, fallidos),
                "PreExecutionCheck-" + (i + 1)
            );
        }

       
        for (Thread t : schedulerThreads) t.start();
        for (Thread t : validatorThreads) t.start();

        System.out.println("Sistema en ejecución...");
    }
}
