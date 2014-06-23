package com.pwc.sns.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.pwc.sns.dto.Client;
import com.pwc.sns.util.SnsUtil;

public class ClientDao {
	Logger log = Logger.getLogger(ClientDao.class);

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public int len() {
		String sql = "SELECT COUNT(*) FROM client";

		int total = jdbcTemplate.queryForInt(sql);

		return total;
	}

	public List findAll() {
		String sql = "Select * from client";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}

	public Client findClientByUdid(String udid) {
		String sql = "Select * from client where udid= ?";
		try {
			Client client = this.jdbcTemplate.queryForObject(sql,
					new Object[] { udid }, new ClientRowWrapper());
			return client;
		} catch (EmptyResultDataAccessException eae) {
			return null;
		}
	}
	
	public void addClient(Client client){
		String sql = "INSERT INTO client (udid, name, phone, email, client_type, callback_url,status) VALUES (?,?,?,?,?,?,?)";
		Object[] params = new Object[] { client.getUdid(), client.getName(), client.getPhone(), client.getEmail(), client.getClientType(),
				client.getCallbackUrl(),client.getStatus()};
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
				Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
				Types.INTEGER };
		int row = jdbcTemplate.update(sql, params, types);
		
		log.info("New client added:"+row);

	}

	static class ClientRowWrapper implements RowMapper<Client> {
		public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
			Client client = new Client();
			client.setId(rs.getInt("Id"));
			client.setName(rs.getString("NAME"));
			client.setUdid(rs.getString("udid"));
			client.setPhone(rs.getString("phone"));
			client.setEmail(rs.getString("email"));
			client.setClientType(rs.getString("client_type"));
			client.setCallbackUrl(rs.getString("callback_url"));
			client.setStatus(rs.getInt("status"));

			return client;
		}

	}
}
