import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Definición de la clase Cliente
class Cliente {
    String nombre;
    double saldoInicial;

    // Constructor de la clase Cliente
    public Cliente(String nombre, double saldoInicial) {
        this.nombre = nombre;
        this.saldoInicial = saldoInicial;
    }
}

// Definición de la clase Transaccion
class Transaccion {
    String nombre;
    char tipo;
    double cantidad;

    // Constructor de la clase Transaccion
    public Transaccion(String nombre, char tipo, double cantidad) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidad = cantidad;
    }
}

// Clase principal del programa
public class App {
    public static void main(String[] args) {
        // Rutas de los archivos de datos
        String archivoClientes = "data/clientes.txt";
        String archivoTransacciones = "data/transacciones.txt";

        // Leer los clientes del archivo y almacenarlos en una lista
        List<Cliente> clientes = leerClientes(archivoClientes);

        // Leer las transacciones del archivo y calcular el saldo final de cada cliente
        calcularSaldosFinales(clientes, archivoTransacciones);

        // Mostrar el reporte de saldos finales
        mostrarReporte(clientes);
    }

    // Función para leer los clientes del archivo y almacenarlos en una lista
    public static List<Cliente> leerClientes(String archivo) {
        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombre = datos[0];
                double saldoInicial = Double.parseDouble(datos[1]);
                Cliente cliente = new Cliente(nombre, saldoInicial);
                clientes.add(cliente);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    // Función para calcular los saldos finales de los clientes a partir de las transacciones
    public static void calcularSaldosFinales(List<Cliente> clientes, String archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            Cliente clienteActual = null;
            double saldoFinal = 0.0;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombre = datos[0];
                char tipo = datos[1].charAt(0);
                double cantidad = Double.parseDouble(datos[2]);

                if (clienteActual == null || !clienteActual.nombre.equals(nombre)) {
                    // Actualizar saldo final del cliente anterior
                    if (clienteActual != null) {
                        clienteActual.saldoInicial = saldoFinal;
                    }

                    // Buscar el cliente actual en la lista de clientes
                    for (Cliente cliente : clientes) {
                        if (cliente.nombre.equals(nombre)) {
                            clienteActual = cliente;
                            saldoFinal = cliente.saldoInicial;
                            break;
                        }
                    }
                }

                // Calcular saldo final según el tipo de transacción
                if (tipo == 'C') {
                    saldoFinal += cantidad;
                } else if (tipo == 'P') {
                    saldoFinal -= cantidad;
                }
            }

            // Actualizar saldo final del último cliente
            if (clienteActual != null) {
                clienteActual.saldoInicial = saldoFinal;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Función para mostrar el reporte de saldos finales
    public static void mostrarReporte(List<Cliente> clientes) {
        System.out.println("Reporte de saldos finales:");

        for (Cliente cliente : clientes) {
            System.out.println("Cliente: " + cliente.nombre);
            System.out.println("Saldo final: " + cliente.saldoInicial);
            System.out.println();
        }
    }
}