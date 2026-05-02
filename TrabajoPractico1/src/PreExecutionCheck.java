/*
Etapa 2 : Validacion de Job
Ejecutado por 2 hilos

-Cada hilo toma un job aleatorio de JobsEnCola
-Se valida la configuracion del job -> 85% valido
                                    -> 15% invalido

Implementar que tome como parámetro el id del nodo
Si el Job es valido :
            El nodo vuelva a estar libre
            El Job pasa a JobsEnEjecucion

Si el Job es invalido :
            El nodo pasa a "Fuera de servicio"
            El Job pasa a JobsFallidos
            
El constructor  recibe como parámetro la matriz de nodos, los jobs en cola, jobs en ejecucion y jobs fallidos

 */

public class PreExecutionCheck implements Runnable{
    @Override
    public void run() {

    }
}
