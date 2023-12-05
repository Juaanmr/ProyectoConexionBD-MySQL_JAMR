/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.conexionbd;

import java.sql.*;
import java.text.ParseException;
import java.util.Scanner;


/**
 *
 * @author Juan
 */
public class ConexionBD {

    //Datos de conexion a la base de datos
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/jcvd";
    static final String USER = "juan";
    static final String PASS = "1234";

    public static void main(String[] args) {
        try {
            //asi llamo a los metodos 
            //System.out.println(buscarNombre("Valorant"));
            System.out.println(lanzaConsulta("SELECT * FROM videojuegos"));
            //nuevoRegistroParametro("C:S", "Shooter", "2023-11-24", "Steam", 00.00);
            //nuevoRegistroTeclado();
            //System.out.println(eliminarRegistro("Valorant"));

        } catch (Exception e) {
            //excepcion que salta si hay algun problema 
            e.printStackTrace();
        }
    }

    /**
     * metodo que busca un videojuego en la base de datos
     * @param nombre variable donde se guardara el nombre del videojuego
     * @return me devuelve true si ha encontrado el videojuego o false si no existe
     */
    private static boolean buscarNombre(String nombre) {
        //consulta para buscar el videojuego en la base de datos el videojuego
        String consulta = "SELECT *FROM videojuegos WHERE Nombre = '" + nombre + "'";

        //establzco conexion con la base de datos y ejecuto la consulta
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery(consulta);) {

            //creo una variable para ver si existe el nombre que ha insertido el usuario
            boolean existe = false;

            while (rs.next()) {
                //obtengo el nombre del juego a traves de la columna Nombre de la base de datos
                String nJuego = rs.getString("Nombre");
                //compara el nombre del juego con el nombre proporcionado
                if (nJuego.equals(nombre)) {
                    existe = true;
                }
            }

            //cierro la conexion
            conn.close();
            
            //devuelvo true si el nombre existe
            return existe;
        } catch (Exception e) {
            //excepcion que salta si hay algun error derante la ejecucion de la consulta
            e.printStackTrace();
            return false;
        }
    }

    /**
     * lanza la consulta que le paso por parametro
     * @param consulta variable donde se guarda la consulta
     * @return devuelve el resultado de la consulta
     */
    public static String lanzaConsulta(String consulta) {
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(consulta)) {

            while (rs.next()){
                //imprimo a traves de consola la informacion detallada de cada fila
                System.out.print("EL ID es : "+rs.getInt("id"));
                System.out.print(", EL Nombre es : "+rs.getString("Nombre"));
                System.out.print(", EL Genere es : "+rs.getString("Genero"));
                System.out.print(", la Fecha de Lanzamiento es : "+rs.getDate("FechaLanzamiento"));
                System.out.print(", La Compañia es : "+rs.getString("Compañia"));
                System.out.println(", EL Precio es : "+rs.getFloat("Precio"));                
            } 
            
            //obtiene la informacion sobre las columnas de la base 
            ResultSetMetaData meta = rs.getMetaData();
            int colum = meta.getColumnCount();
            
            StringBuilder resultado = new StringBuilder();

            while (rs.next()) {
                for (int i = 1; i <= colum; i++) {
                    //Obtengo el valor de cada columna y lo agregao a la cadena
                    String valor = rs.getString(i);
                    resultado.append(meta.getColumnName(i)).append(": ").append(valor).append("\t");
                }
                resultado.append("\n");
            }

            //cierro la conexion
            conn.close();
            
            //devuelvo la cadena con los resultados de la consulta
            return resultado.toString();

        } catch (Exception e) {
            //excepcion que salta si hay algun error durante la ejecucion de la consulta y devuelve error
            e.printStackTrace();
            return "error";
        }
    }
    
    /**
     * añade un videojuego que le meto por paramtero 
     * @param Nombre variable donde guardo el nombre del juego
     * @param Genero variable donde guardo el tipo de genero del videojuego
     * @param FechaLanzamiento variable donde gurado la fecha del lanzamiento del videojuego
     * @param Compañia variable donde gurado la compañia del videojuego
     * @param Precio variable donde guardo el precio del juego
     */
    public static void nuevoRegistroParametro(String Nombre, String Genero, String FechaLanzamiento, String Compañia, double Precio) {
        //creo la consulta que quiero para añadir un videojuego a la base de datos y uso la varibales que he creado antes
        String Query = "INSERT INTO `videojuegos` (`Nombre`, `Genero`, `FechaLanzamiento`, `Compañia`, `Precio`) "
            + "VALUES ('" + Nombre + "', '" + Genero + "', '" + FechaLanzamiento + "', '" + Compañia + "', " + Precio + ")";
        
        //hago la conexion a la base de datos y ejecuto la consulta
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();) {

            //ejecuto la consulta y ejecuto un mensaje si se añade el videojuego
            stmt.executeUpdate(Query);
            System.out.println("videojuego insertado");
            
            //cierro la conexion
            conn.close();
            
        } catch (Exception e) {
            //excepcion que salta si hay algun error durante la ejecucion de la consulta y devuelve error
            e.printStackTrace();
        }
    }
    
    /**
     * añadir un videojuego por teclado
     * @throws ParseException 
     */
    public static void nuevoRegistroTeclado() throws ParseException {
        //creo el objeto scanner para leer la entrada por teclado
        Scanner sc = new Scanner(System.in);
        String Query;
        String Nombre, Genero, FechaLanzamiento, Compañia;
        float Precio;

        //hago la conexion a la base de datos
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); 
                Statement stmt = conn.createStatement();) {

            System.out.println("Añade un nuevo videojuego: ");
            System.out.print("\t - Nombre: ");
            Nombre = sc.nextLine();
            System.out.print("\t - Genero: ");
            Genero = sc.nextLine();
            System.out.print("\t - Fecha de lanzamiento (yyyy-MM-dd): ");
            FechaLanzamiento = sc.nextLine();
            System.out.print("\t - Compañia: ");
            Compañia = sc.nextLine();
            System.out.print("\t - Precio (00,00): ");
            Precio = sc.nextFloat();
            
            //construyo la consulta con los datos que ha ingresado el usuario
            Query = "INSERT INTO `videojuegos` (`id`, `Nombre`, `Genero`, `FechaLanzamiento`, `Compañia`, `Precio`) "
                    + "VALUES (NULL,'" + Nombre + "', '" + Genero + "', '" + FechaLanzamiento + "', '" + Compañia + "', " + Precio + ")";
            
            //ejecuto la consulta y enseño un mensaje si se ha añadido el videojuego
            stmt.executeUpdate(Query);
            System.out.println("\n\tvideojuego insertado");

            //cierro la conexion 
            conn.close();

        } catch (SQLException e) {
            //excepcion que salta si hay algun error durante la ejecucion de la consulta
            e.printStackTrace();
        }
    }
    
    /**
     * elimina un videojuego
     * @param nombre variable donde se introduce el nombre del juego que se quiere eliminar
     * @return true si el juego ha sido eliminado o false si no ha sido eliminado
     */
    public static boolean eliminarRegistro(String nombre) {
        //consulta para eliminar el juego qu emete el usuario por teclado
        String delQuery = "DELETE FROM videojuegos WHERE Nombre = '" + nombre + "'";
        
        //verifico si existe el nombre del videojuego en l abase de datos llamando al metodo buscarNombre
        if (buscarNombre(nombre)) {
            //establezco la conexion con la base de datos
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();) {
            
                //ejecuto la consulta para eliminar un juego
                stmt.executeUpdate(delQuery);

                //cierro la conexion con la base de datos
                conn.close();
                
                //el programa me devuelve true si se ha eliminado correctamente
                return true;

            } catch (Exception e) {
                //excepcion que salta si hay algun error durante la ejecucion de la consulta
                e.printStackTrace();
                //el programa me devuelve false si hay algun problema a la hora de eliminar el videojuego
                return false;
            }
        } else {
            //si le videojuego no existe en la base de datos imprime este  mensaje 
            System.out.println("El videojuego introducido no existe en la base");
            //el programa me devuelve este mensaje para indicarme que el videojuego no existe y por lo tanto no lo puede eliminar
            return false;
        }
    }
}