/*
Hilo principal
*/
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final int CANTIDAD_JOBS = 500;
    private static final int CANTIDAD_SCHEDULERS = 3;
    private static final int CANTIDAD_PREEXECUTION = 2;
    private static final int CANTIDAD_EJECUTORES = 10;
    private static final int CANTIDAD_POSTPROCESSING = 2;
    private static final Path LOG_DIRECTORY = Path.of("logs");
    private static final DateTimeFormatter LOG_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter LOG_FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void main(String[] args) {
        try {
            Path logPath = crearRutaLog();
            try (PrintWriter log = new PrintWriter(Files.newBufferedWriter(logPath), true)) {
                ejecutarSistema(log);
            }
        } catch (IOException e) {
            System.err.println("No se pudo crear/escribir el archivo de log: " + e.getMessage());
        }
    }

    private static Path crearRutaLog() throws IOException {
        Files.createDirectories(LOG_DIRECTORY);
        String fechaInicio = LocalDateTime.now().format(LOG_FILE_FORMATTER);
        return LOG_DIRECTORY.resolve("log_ejecucion_" + fechaInicio + ".txt");
    }

    private static void ejecutarSistema(PrintWriter log) {
        long tiempoInicio = System.nanoTime();
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
        registrar(log, "Schedulers iniciados...");
        for (Thread t : validatorThreads)
            t.start();
        registrar(log, "PreExecutionChecks iniciados...");
        for (Thread t : workerExecutionThreads)
            t.start();
        registrar(log, "WorkerExecutions iniciados...");
        for (Thread t : postProcessingThreads)
            t.start();
        registrar(log, "PostProcessings iniciados...");

        registrar(log, "Sistema en ejecucion...");

        while (validados.size() + fallidos.size() < CANTIDAD_JOBS) {
            try {
                Thread.sleep(200);
                registrar(log, "Jobs en cola: " + enCola.size()
                        + " | Jobs validados: " + validados.size()
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

        double tiempoTotalSegundos = (System.nanoTime() - tiempoInicio) / 1_000_000_000.0;
        registrar(log, String.format("Sistema finalizado. Tiempo de ejecucion: %.2f segundos.", tiempoTotalSegundos));
    }

    private static void registrar(PrintWriter log, String mensaje) {
        String mensajeConFecha = "[" + LocalDateTime.now().format(LOG_FORMATTER) + "] " + mensaje;
        System.out.println(mensaje);
        log.println(mensajeConFecha);
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
