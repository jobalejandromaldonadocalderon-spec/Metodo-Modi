
package com.mycompany.modi;


public class SolucionInicial {
    
    public void aplicarEsquinaNoroeste(DatosTransporte datos) {

        double[] ofertaRestante = datos.oferta.clone();
        double[] demandaRestante = datos.demanda.clone();

        int i = 0;
        int j = 0;

        while (i < datos.filas && j < datos.columnas) {

            double cantidad = Math.min(
                    ofertaRestante[i],
                    demandaRestante[j]
            );

            datos.asignaciones[i][j] = cantidad;

            if (cantidad > 0) {
                datos.basica[i][j] = true;
            }

            ofertaRestante[i] -= cantidad;
            demandaRestante[j] -= cantidad;

            boolean terminaFila = Math.abs(ofertaRestante[i]) < 0.000001;
            boolean terminaColumna = Math.abs(demandaRestante[j]) < 0.000001;

            if (terminaFila) {
                i++;
            }

            if (terminaColumna) {
                j++;
            }
        }

        completarBase(datos);
    }

    private void completarBase(DatosTransporte datos) {

        int cantidadNecesaria = datos.filas + datos.columnas - 1;
        int cantidadActual = contarBasicas(datos);

        while (cantidadActual < cantidadNecesaria) {

            boolean agregada = false;

            for (int i = 0; i < datos.filas && !agregada; i++) {
                for (int j = 0; j < datos.columnas && !agregada; j++) {

                    if (!datos.basica[i][j]) {

                        if (!formariaCiclo(datos, i, j)) {

                            datos.basica[i][j] = true;
                            datos.asignaciones[i][j] = 0;

                            cantidadActual++;
                            agregada = true;
                        }
                    }
                }
            }

            if (!agregada) {
                throw new IllegalStateException(
                        "No fue posible completar la base inicial."
                );
            }
        }
    }

    private int contarBasicas(DatosTransporte datos) {

        int contador = 0;

        for (int i = 0; i < datos.filas; i++) {
            for (int j = 0; j < datos.columnas; j++) {

                if (datos.basica[i][j]) {
                    contador++;
                }
            }
        }

        return contador;
    }

    private boolean formariaCiclo(DatosTransporte datos, int fila, int columna) {

        boolean[] visitados = new boolean[datos.filas + datos.columnas];

        return buscarCamino(
                datos,
                fila,
                datos.filas + columna,
                visitados
        );
    }

    private boolean buscarCamino(
            DatosTransporte datos,
            int actual,
            int destino,
            boolean[] visitados) {

        if (actual == destino) {
            return true;
        }

        visitados[actual] = true;

        if (actual < datos.filas) {

            int fila = actual;

            for (int j = 0; j < datos.columnas; j++) {

                int siguiente = datos.filas + j;

                if (datos.basica[fila][j] && !visitados[siguiente]) {

                    if (buscarCamino(datos, siguiente, destino, visitados)) {
                        return true;
                    }
                }
            }

        } else {

            int columna = actual - datos.filas;

            for (int i = 0; i < datos.filas; i++) {

                if (datos.basica[i][columna] && !visitados[i]) {

                    if (buscarCamino(datos, i, destino, visitados)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
