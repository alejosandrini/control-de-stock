package com.alura.jdbc.pruebas;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.alura.jdbc.factory.ConnectionFactory;

public class PruebaDelete {
	public static void main(String[] args) throws SQLException {
		Connection con = new ConnectionFactory().recuperaConexion();
		Statement statement = con.createStatement();
		statement.execute("DELETE FROM producto WHERE id = 99");
		int updateCount = statement.getUpdateCount();
		// nos devuelve cuantas lineas fueron modificadas luego del execute del statement
		// al tratarse de eliminar un id especifico, deberia ser 1
		System.out.println(updateCount);
	}
}
