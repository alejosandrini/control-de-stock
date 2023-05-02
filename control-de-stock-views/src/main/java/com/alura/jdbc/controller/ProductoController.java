package com.alura.jdbc.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.factory.ConnectionFactory;

public class ProductoController {

	public int modificar(String nombre, String descripcion, int id, int cantidad) throws SQLException {
		Connection con = new ConnectionFactory().recuperaConexion();
		
		Statement statement = con.createStatement();
		statement.execute("UPDATE producto SET "
				+ " NOMBRE = '" + nombre + "'"
			    + ", DESCRIPCION = '" + descripcion + "'"
			    + ", CANTIDAD = " + cantidad
			    + " WHERE id = " + id);
		int updateCount = statement.getUpdateCount();
		con.close();
		return updateCount;
	}

	public int eliminar(Integer id) throws SQLException {
		Connection con = new ConnectionFactory().recuperaConexion();
		
		Statement statement = con.createStatement();
		statement.execute("DELETE FROM producto WHERE id ="+ id);
		int updateCount = statement.getUpdateCount();
		// nos devuelve cuantas lineas fueron modificadas luego del execute del statement
		// al tratarse de eliminar un id especifico, deberia ser 1
		con.close();
		return updateCount;
	}

	public List<Map<String,String>> listar() throws SQLException {
		Connection con = new ConnectionFactory().recuperaConexion();
		
		Statement statement = con.createStatement();
		//el statement nos devuelve un boolean, si nos devuelve una lista es true, sino false
		//en este caso que estamos haciendo un select, nos devuelve un listado, si fuese un insert por ejemplo, no
		statement.execute("SELECT id, nombre, descripcion, cantidad FROM producto");
		ResultSet resultSet = statement.getResultSet();
		
		List<Map<String,String>> resultado = new ArrayList<>();
		while (resultSet.next()) {
			Map<String, String> fila = new HashMap<>();
			fila.put("id", String.valueOf(resultSet.getInt("id")));
			fila.put("nombre", resultSet.getString("nombre"));
			fila.put("descripcion", resultSet.getString("descripcion"));
			fila.put("cantidad", String.valueOf(resultSet.getInt("cantidad")));
			
			resultado.add(fila);
		}
		
		con.close();
		
		return resultado;
	}

    public void guardar(HashMap<String, String> producto) throws SQLException {
		Connection con = new ConnectionFactory().recuperaConexion();
		
		/*Statement statement = con.createStatement();
		String sqlInsert = "INSERT INTO producto(nombre, descripcion, cantidad)" 
				+ "VALUES('"+ producto.get("nombre") + "', '" 
				// tenemos que poner comillas simples para que la db reconozca el string
				+ producto.get("descripcion") + "',"
				+ producto.get("cantidad") + ")";
		statement.execute(sqlInsert, Statement.RETURN_GENERATED_KEYS);*/
		//Para saber si se ejecuto bien podemos usar una constante que nos devuelve si se creo una key
		PreparedStatement statement = con.prepareStatement("INSERT INTO producto(nombre, descripcion, cantidad)" 
				+ "VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS); //Evitamos problemas de inyeccion de SQL
		statement.setString(1, producto.get("nombre"));
		statement.setString(2, producto.get("descripcion"));
		statement.setInt(3, Integer.valueOf(producto.get("cantidad")));
		
		statement.execute();
		
		ResultSet resultSet = statement.getGeneratedKeys();
		
		while (resultSet.next()) {
			System.out.println(String.format("Fue insertado el producto de ID %d",
					resultSet.getInt(1)));
			
		}
		con.close();
	}

}
