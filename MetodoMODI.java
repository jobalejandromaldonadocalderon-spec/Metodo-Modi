
package com.mycompany.modi;

import java.util.List;
public class MetodoMODI {
    
    private DatosTransporte datos;
    private CalculosMODI calculos;

    public MetodoMODI(DatosTransporte datos) {
        this.datos = datos;
        this.calculos = new CalculosMODI();
    }

    public void resolver() {

        int iteracion = 1;
        int limite = 10000;

        while (iteracion <= limite) {

            System.out.println("\n========== ITERACION " + iteracion + " ==========");

            calculos.calcularPotenciales(datos);

            double[][] delta = calculos.calcularCostosReducidos(datos);

            imprimirPotenciales();
            imprimirCostosReducidos(delta);

            int[] entrada = calculos.buscarCeldaEntrante(datos, delta);

            if (entrada == null) {

                System.out.println("\nSOLUCION OPTIMA ENCONTRADA.");

                comprobarOptimoAlternativo(delta);
                imprimirResultado();

                return;
            }

            int fila = entrada[0];
            int columna = entrada[1];

            System.out.println("\nCelda entrante: (" + (fila + 1)
                    + ", " + (columna + 1) + ")");

            List<int[]> ciclo = calculos.construirCiclo(
                    datos,
                    fila,
                    columna
            );

            imprimirCiclo(ciclo);

            ajustarAsignaciones(ciclo);

            imprimirAsignaciones();

            iteracion++;
        }

        System.out.println("Se alcanzo el limite de iteraciones.");
    }

    private void ajustarAsignaciones(List<int[]> ciclo) {

        double theta = Double.MAX_VALUE;

        // Buscar el menor valor de las celdas con signo negativo.
        for (int k = 1; k < ciclo.size(); k += 2) {

            int[] celda = ciclo.get(k);

            double valor = datos.asignaciones[celda[0]][celda[1]];

            theta = Math.min(theta, valor);
        }

        System.out.println("Valor de theta: " + theta);

        // Aplicar los signos alternantes.
        for (int k = 0; k < ciclo.size(); k++) {

            int[] celda = ciclo.get(k);

            int i = celda[0];
            int j = celda[1];

            if (k % 2 == 0) {

                datos.asignaciones[i][j] += theta;
                datos.basica[i][j] = true;

            } else {

                datos.asignaciones[i][j] -= theta;

                if (Math.abs(datos.asignaciones[i][j]) < 0.000001) {
                    datos.asignaciones[i][j] = 0;
                }
            }
        }

        // Sacar una variable básica que haya quedado en cero.
        for (int k = 1; k < ciclo.size(); k += 2) {

            int[] celda = ciclo.get(k);

            int i = celda[0];
            int j = celda[1];

            if (Math.abs(datos.asignaciones[i][j]) < 0.000001) {

                datos.basica[i][j] = false;
                break;
            }
        }
    }

    private void comprobarOptimoAlternativo(double[][] delta) {

        for (int i = 0; i < datos.filas; i++) {
            for (int j = 0; j < datos.columnas; j++) {

                if (!datos.basica[i][j]
                        && Math.abs(delta[i][j]) < 0.000001) {

                    System.out.println("Existe una posible solucion optima alternativa.");
                    return;
                }
            }
        }
    }

    private void imprimirPotenciales() {

        System.out.println("\nPotenciales U:");

        for (double valor : calculos.u) {
            System.out.printf("%.2f ", valor);
        }

        System.out.println("\nPotenciales V:");

        for (double valor : calculos.v) {
            System.out.printf("%.2f ", valor);
        }

        System.out.println();
    }

    private void imprimirCostosReducidos(double[][] delta) {

        System.out.println("\nMatriz de costos reducidos:");

        for (int i = 0; i < datos.filas; i++) {
            for (int j = 0; j < datos.columnas; j++) {

                System.out.printf("%10.2f", delta[i][j]);
            }

            System.out.println();
        }
    }

    private void imprimirCiclo(List<int[]> ciclo) {

        System.out.println("\nCiclo encontrado:");

        for (int k = 0; k < ciclo.size(); k++) {

            int[] celda = ciclo.get(k);

            String signo = (k % 2 == 0) ? "+" : "-";

            System.out.println(
                    signo + " (" + (celda[0] + 1)
                    + ", " + (celda[1] + 1) + ")"
            );
        }
    }

    private void imprimirAsignaciones() {

        System.out.println("\nMatriz de asignaciones:");

        for (int i = 0; i < datos.filas; i++) {
            for (int j = 0; j < datos.columnas; j++) {

                System.out.printf("%10.2f", datos.asignaciones[i][j]);
            }

            System.out.println();
        }
    }

    private void imprimirResultado() {

        System.out.println("\n========== RESULTADO FINAL ==========");

        imprimirAsignaciones();

        System.out.printf(
                "\nCosto minimo total: %.2f%n",
                datos.calcularCostoTotal()
        );
    }
}
