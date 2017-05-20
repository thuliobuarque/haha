package com.accenture.treinamento.portal.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.accenture.treinamento.factory.ConexaoMySQL;
import com.accenture.treinamento.portal.model.UsuarioBean;
import com.accenture.treinamento.sistema.exception.ProjetoException;

public class UsuarioDAO implements IUsuarioDAO {

	private Connection conexao = null;

	public UsuarioBean autenticarUsuario(UsuarioBean usuario)
			throws ProjetoException {

		System.out.println(usuario.getLogin() + usuario.getSenha());

		conexao = ConexaoMySQL.getConnection();

		String sql = "select usuarios.login, usuarios.senha, usuarios.nome from acl.usuarios where usuarios.login = ? and usuarios.senha = ?";

		UsuarioBean ub = null;

		try {

			PreparedStatement pstmt = conexao.prepareStatement(sql);
			pstmt.setString(1, usuario.getLogin().toUpperCase());
			pstmt.setString(2, usuario.getSenha().toUpperCase());

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {	
				ub = new UsuarioBean();
				ub.setLogin(rs.getString("login"));
				ub.setSenha(rs.getString("senha"));
				ub.setNome(rs.getString("nome"));
			}

			return ub;
		} catch (SQLException ex) {
			throw new ProjetoException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
		}
	}

	public boolean cadastrarUsuario(UsuarioBean usuario) {

		String sql = "insert into acl.usuarios (login, senha) values (?, ?)";

		try {
			conexao = ConexaoMySQL.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, usuario.getLogin());
			stmt.setString(2, usuario.getSenha());
			stmt.execute();

			conexao.commit();

			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

}
