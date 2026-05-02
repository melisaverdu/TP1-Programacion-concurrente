/*

Etapa 3 : Ejacucion del Job
Ejecutado por 3 hilos

- Cada hilo toma un Job de JobsEnEjecucion
- Simula su ejecucion con probabilidad -> 90% exito
                                       -> 10% error en ejecucion

Si el Job es exitoso -> pasa a JobsFinalizados

Si el Job tiene un Error -> pasa a JobsFallidos

 */

public class WorkerExecution implements Runnable{
    @Override
    public void run() {

    }
}
