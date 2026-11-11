
package com.mycompany.modi;

public class Main {
    
    public static void main(String[] args) {

        try {

            EntradaDatos entrada = new EntradaDatos();

            DatosTransporte datos = entrada.capturar();

            System.out.println("\nBalanceando problema...");
            datos.balancear();

            SolucionInicial inicial = new SolucionInicial();

            System.out.println("\nGenerando solucion inicial...");
            inicial.aplicarEsquinaNoroeste(datos);

            System.out.println("\nSolucion inicial:");

            for (int i = 0; i < datos.filas; i++) {
                for (int j = 0; j < datos.columnas; j++) {
                    System.out.printf("%10.2f", datos.asignaciones[i][j]);
                }
                System.out.println();
            }

            MetodoMODI modi = new MetodoMODI(datos);

            modi.resolver();

        } catch (Exception e) {

            System.out.println("\nError: " + e.getMessage());
        }
    }
}