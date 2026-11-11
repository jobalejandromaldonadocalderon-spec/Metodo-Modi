
package com.mycompany.modi;


public class DatosTransporte {
    
    int filas;
    int columnas;

    double[][] costos;
    double[][] asignaciones;
    boolean[][] basica;

    double[] oferta;
    double[] demanda;

    public DatosTransporte(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;

        costos = new double[filas][columnas];
        asignaciones = new double[filas][columnas];
        basica = new boolean[filas][columnas];

        oferta = new double[filas];
        demanda = new double[columnas];
    }

    public void balancear() {

        double totalOferta = 0;
        double totalDemanda = 0;

        for (double valor : oferta) {
            totalOferta += valor;
        }

        for (double valor : demanda) {
            totalDemanda += valor;
        }

        if (Math.abs(totalOferta - totalDemanda) < 0.000001) {
            return;
        }

        if (totalOferta > totalDemanda) {

            double[][] nuevosCostos = new double[filas][columnas + 1];

            for (int i = 0; i < filas; i++) {
                System.arraycopy(costos[i], 0, nuevosCostos[i], 0, columnas);
            }

            double[] nuevaDemanda = new double[columnas + 1];
            System.arraycopy(demanda, 0, nuevaDemanda, 0, columnas);

            nuevaDemanda[columnas] = totalOferta - totalDemanda;

            costos = nuevosCostos;
            demanda = nuevaDemanda;
            columnas++;

        } else {

            double[][] nuevosCostos = new double[filas + 1][columnas];

            for (int i = 0; i < filas; i++) {
                System.arraycopy(costos[i], 0, nuevosCostos[i], 0, columnas);
            }

            double[] nuevaOferta = new double[filas + 1];
            System.arraycopy(oferta, 0, nuevaOferta, 0, filas);

            nuevaOferta[filas] = totalDemanda - totalOferta;

            costos = nuevosCostos;
            oferta = nuevaOferta;
            filas++;
        }

        asignaciones = new double[filas][columnas];
        basica = new boolean[filas][columnas];
    }

    public double calcularCostoTotal() {

        double total = 0;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                total += asignaciones[i][j] * costos[i][j];
            }
        }

        return total;
    }
}
