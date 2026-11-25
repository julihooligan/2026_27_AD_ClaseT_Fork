package app;

import conexion.ConexionBD;
import dao.DAOException;
import dao.ProductoDAO;
import dao.implementaciones.ProductoDAOImplMySQL;
import modelo.Producto;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class DemoConexionMySQL {

    private static final int NUM_PRODUCTOS = 25;

    public static void main(String[] args) {

        //(Connection conexion = new ConexionBD(Path.of("src/main/db.properties")).obtenerConexion())
        Random azar = new Random();
        Path ruta = Path.of("src/main/db.properties");
        try  {
            // Nos creamos un fichero
            ConexionBD conexionBD = new ConexionBD(ruta);
            ProductoDAO dao = new ProductoDAOImplMySQL(conexionBD);

//            // 1. Rellenar con 25 productos (igual que en ACT13, pero a través del DAO).
//            for (int i = 0; i < NUM_PRODUCTOS; i++) {
//                double precio = 1 + azar.nextDouble() * 199; // Obtiene un precio entre 1 y 200
//                precio = Math.round(precio * 100) / 100.0;  // Redondea a 2 decimales
//                dao.insertar(new Producto(1001 + i, precio));
//            }

            // 2. Mostrar los productos.
            System.out.println("=== Precios iniciales ===");
            mostrar(dao.listar());

            // 3. Actualizar precios. -10% si precio > 100
            for (Producto p : dao.listar()) {
                double nuevoPrecio;
                if (p.getPrecio() > 100) {
                    nuevoPrecio = p.getPrecio() * 0.90;
                } else {
                    nuevoPrecio = p.getPrecio() * 1.10;
                }
                nuevoPrecio = (double) Math.round(nuevoPrecio * 100) / 100;
                Producto nuevo = new Producto(p.getReferencia(), nuevoPrecio);
                dao.actualizar(nuevo);
            }

//            // 4. Mostrar los productos.
//            System.out.println("=== Precios tras actualización de precios ===");
//            mostrar(dao.listar());
//
//        } catch (DAOException e) {
//            System.err.println("Error al CONECTAR: " + e.getMessage());
//        }

            // 5. Eliminar. Insertar un producto para luego borrarlo
            // id=9001, precio=5.00
            dao.insertar(new Producto(9001, 5.00));

            // Mostrar lita completa.
            mostrar(dao.listar());

            // Eliminar ese registro.
            dao.eliminar(9001);

            // Volver a mostrar lista completa.
            mostrar(dao.listar());

            // 6. Buscar el 1001 y mostrar mensaje de existe o no.
            Producto producto = dao.buscar(1001);
            //if (dao.buscar(1001) != null){
            if (producto != null){
                System.out.println("El producto con id 1001 existe");
            } else {
                System.out.println("No se encontro ese producto");
            }

            System.out.println("Conexion establecida OK.");
        } catch (DAOException e) {
            System.err.println("Error al conectar " + e.getMessage());
        }
    }

    private static void mostrar(List<Producto> productos) {
        for (Producto p : productos){
            System.out.println(p);
        }
    }

//    static void mostrar(List<Producto> lista) {
//        Producto productos;
//        System.out.println("PRODUCTO        PRECIO");
//        for (Iterator<Producto> p  = lista.iterator(); p.hasNext(); ){
//            productos = p.next();
//            System.out.println(productos.getReferencia() + "        " + productos.getPrecio());
//        }
//    }
}