
package com.mycompany.modi;
import java.util.*;
public class CalculosMODI {
    
    double[] u;
    double[] v;

    public void calcularPotenciales(DatosTransporte datos) {

        u = new double[datos.filas];
        v = new double[datos.columnas];

        Arrays.fill(u, Double.NaN);
        Arrays.fill(v, Double.NaN);

        u[0] = 0;

        boolean cambio;

        do {

            cambio = false;

            for (int i = 0; i < datos.filas; i++) {
                for (int j = 0; j < datos.columnas; j++) {

                    if (datos.basica[i][j]) {

                        if (!Double.isNaN(u[i]) && Double.isNaN(v[j])) {

                            v[j] = datos.costos[i][j] - u[i];
                            cambio = true;

                        } else if (Double.isNaN(u[i]) && !Double.isNaN(v[j])) {

                            u[i] = datos.costos[i][j] - v[j];
                            cambio = true;
                        }
                    }
                }
            }

        } while (cambio);
    }

    public double[][] calcularCostosReducidos(DatosTransporte datos) {

        double[][] delta = new double[datos.filas][datos.columnas];

        for (int i = 0; i < datos.filas; i++) {
            for (int j = 0; j < datos.columnas; j++) {

                if (!datos.basica[i][j]) {

                    delta[i][j] = datos.costos[i][j] - (u[i] + v[j]);

                } else {

                    delta[i][j] = 0;
                }
            }
        }

        return delta;
    }

    public int[] buscarCeldaEntrante(
            DatosTransporte datos,
            double[][] delta) {

        int fila = -1;
        int columna = -1;

        double menor = -0.000001;

        for (int i = 0; i < datos.filas; i++) {
            for (int j = 0; j < datos.columnas; j++) {

                if (!datos.basica[i][j] && delta[i][j] < menor) {

                    menor = delta[i][j];

                    fila = i;
                    columna = j;
                }
            }
        }

        if (fila == -1) {
            return null;
        }

        return new int[]{fila, columna};
    }

    public List<int[]> construirCiclo(
            DatosTransporte datos,
            int filaEntrada,
            int columnaEntrada) {

        int inicio = filaEntrada;
        int destino = datos.filas + columnaEntrada;

        int totalNodos = datos.filas + datos.columnas;

        int[] padre = new int[totalNodos];
        int[][] aristaPadre = new int[totalNodos][2];

        Arrays.fill(padre, -1);

        Queue<Integer> cola = new LinkedList<>();

        cola.add(inicio);
        padre[inicio] = inicio;

        while (!cola.isEmpty()) {

            int actual = cola.poll();

            if (actual == destino) {
                break;
            }

            if (actual < datos.filas) {

                int fila = actual;

                for (int j = 0; j < datos.columnas; j++) {

                    int siguiente = datos.filas + j;

                    if (datos.basica[fila][j] && padre[siguiente] == -1) {

                        padre[siguiente] = actual;
                        aristaPadre[siguiente] = new int[]{fila, j};

                        cola.add(siguiente);
                    }
                }

            } else {

                int columna = actual - datos.filas;

                for (int i = 0; i < datos.filas; i++) {

                    if (datos.basica[i][columna] && padre[i] == -1) {

                        padre[i] = actual;
                        aristaPadre[i] = new int[]{i, columna};

                        cola.add(i);
                    }
                }
            }
        }

        if (padre[destino] == -1) {
            throw new IllegalStateException("No se pudo construir el ciclo.");
        }

        List<int[]> camino = new ArrayList<>();

        int actual = destino;

        while (actual != inicio) {

            camino.add(aristaPadre[actual]);
            actual = padre[actual];
        }

        Collections.reverse(camino);

        List<int[]> ciclo = new ArrayList<>();

        // La primera celda es la celda entrante (+).
        ciclo.add(new int[]{filaEntrada, columnaEntrada});

        // Las demás celdas siguen el camino alternante.
        ciclo.addAll(camino);

        return ciclo;
    }
}
