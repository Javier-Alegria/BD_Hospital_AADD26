package com.hospital.hospitalmvcjavafx.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static String url = "jdbc:mysql://localhost:3306/hospital";
    private static String user = "root";
    private static String password = "";
    private static String driver = "com.mysql.cj.jdbc.Driver";

    private static Connection jdbcConexion = null;

    /**
     * Constructor privado. Si no hay conexión abierta, carga el driver JDBC
     * y crea la conexión con la base de datos.
     * @throws RuntimeException si falla
     */
    private Conexion(){
        try{
            if ((jdbcConexion==null || jdbcConexion.isClosed())){
                Class.forName(driver);
                jdbcConexion = DriverManager.getConnection(url,user,password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return conexión de la base de datos (Patrón singleton).
     * Si no existe o está cerrada, la crea.
     */
    public static Connection conectar(){
        new Conexion();
        //Esto es lo que siempre tengo que devolver
        return jdbcConexion;
    }

    /**
     * Libera la conexión estableciéndola a null.
     */
    public static void desconectar(){
        try {
            if(!jdbcConexion.isClosed()||jdbcConexion!=null){
                jdbcConexion=null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
