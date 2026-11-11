
package com.mycompany.modi;
import java.util.Scanner;
public class EntradaDatos {
    
    private Scanner sc = new Scanner(System.in);

    public DatosTransporte capturar() {

        int filas;
        int columnas;

        do {
            System.out.print("Numero de origenes: ");
            filas = sc.nextInt();

            System.out.print("Numero de destinos: ");
            columnas = sc.nextInt();

            if (filas <= 0 || columnas <= 0) {
                System.out.println("Los valores deben ser mayores que cero.");
            }

        } while (filas <= 0 || columnas <= 0);

        DatosTransporte datos = new DatosTransporte(filas, columnas);

        System.out.println("\nIngrese la matriz de costos:");

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                System.out.print("Costo [" + (i + 1) + "][" + (j + 1) + "]: ");
                datos.costos[i][j] = sc.nextDouble();

                if (datos.costos[i][j] < 0) {
                    throw new IllegalArgumentException("Los costos no pueden ser negativos.");
                }
            }
        }

        System.out.println("\nIngrese las ofertas:");

        for (int i = 0; i < filas; i++) {

            System.out.print("Oferta del origen " + (i + 1) + ": ");
            datos.oferta[i] = sc.nextDouble();

            if (datos.oferta[i] < 0) {
                throw new IllegalArgumentException("Las ofertas no pueden ser negativas.");
            }
        }

        System.out.println("\nIngrese las demandas:");

        for (int j = 0; j < columnas; j++) {

            System.out.print("Demanda del destino " + (j + 1) + ": ");
            datos.demanda[j] = sc.nextDouble();

            if (datos.demanda[j] < 0) {
                throw new IllegalArgumentException("Las demandas no pueden ser negativas.");
            }
        }

        return datos;
    }
}
