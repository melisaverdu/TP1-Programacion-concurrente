public class PostProcessingTest {
       public static void main(String[] args) throws InterruptedException {

        JobQueue finalizados = new JobQueue();
        JobQueue validados = new JobQueue();
        JobQueue fallidos = new JobQueue();

        int totalJobs = 500;

        // 🔹 Cargar jobs iniciales
        for (int i = 0; i < totalJobs; i++) {
            finalizados.pushJob(new Job(i, EstadoJob.FINALIZADO)); // ID y nodo asignado (0 para test)
        }

        // 🔹 Crear workers
        Thread t1 = new Thread(new PostProcessing(finalizados, validados, fallidos));
        Thread t2 = new Thread(new PostProcessing(finalizados, validados, fallidos));

        t1.start();
        t2.start();

        // 🔹 Esperar a que terminen
        t1.join();
        t2.join();

        // 🔹 Resultados
        int countValidados = 0;
        int countFallidos = 0;

        while (!validados.isEmpty()) {
            validados.popJob();
            countValidados++;
        }

        while (!fallidos.isEmpty()) {
            fallidos.popJob();
            countFallidos++;
        }

        int totalProcesados = countValidados + countFallidos;

        System.out.println("==== RESULTADOS ====");
        System.out.println("Validados: " + countValidados);
        System.out.println("Fallidos: " + countFallidos);
        System.out.println("Total: " + totalProcesados);

        // 🧪 VALIDACIONES

        if (totalProcesados != totalJobs) {
            System.out.println("❌ ERROR: pérdida o duplicación de jobs");
        } else {
            System.out.println("✅ Total correcto");
        }

        double porcentajeValidados = (countValidados * 100.0) / totalJobs;
        double porcentajeFallidos = (countFallidos * 100.0) / totalJobs;

        System.out.printf("%% Validados: %.2f%%\n", porcentajeValidados);
        System.out.printf("%% Fallidos: %.2f%%\n", porcentajeFallidos);

        if (porcentajeValidados < 90 || porcentajeValidados > 98) {
            System.out.println("⚠️ Distribución fuera de rango esperado");
        } else {
            System.out.println("✅ Distribución correcta (~95/5)");
        }
    }
} 

