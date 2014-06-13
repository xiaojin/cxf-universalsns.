package com.pwc.sns.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.pwc.sns.dto.Client;

public class ClientDao {

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
		String sql = "Select * from Client";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}

	public Client findClientByUdid(String udid) {
		String sql = "Select * from Client where udid= ?";
		try {
			Client client = this.jdbcTemplate.queryForObject(sql,
					new Object[] { udid }, new ClientRowWrapper());
			return client;
		} catch (EmptyResultDataAccessException eae) {
			return null;
		}
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
