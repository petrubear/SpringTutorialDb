package emg.demos.spring.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("offerDao")
public class OfferDAO {
	// private JdbcTemplate jdbc;
	private NamedParameterJdbcTemplate jdbc;

	@Autowired
	public void setDataSource(DataSource jdbc) {
		// this.jdbc = new JdbcTemplate(jdbc);
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	public boolean create(Offer offer) {
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(
				offer);

		String sqlQuery = "insert into offers (name, text, email) values (:name, :text, :email)";
		if (offer.getId() != 0) {
			sqlQuery = "insert into offers (id, name, text, email) values (:id, :name, :text, :email)";
		}
		return jdbc.update(sqlQuery, params) == 1;
	}

	// Ejecuta el metodo como una transaccion
	// Se insertan todos los valores de la lista o ninguno
	@Transactional
	public int[] create(List<Offer> offers) {
		SqlParameterSource[] params = SqlParameterSourceUtils
				.createBatch(offers.toArray());
		String sqlQuery = "insert into offers (id, name, text, email) values (:id, :name, :text, :email)";
		return jdbc.batchUpdate(sqlQuery, params);
	}

	public boolean update(Offer offer) {
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(
				offer);
		String sqlQuery = "update offers set name = :name, email = :email, text = :text where id = :id";

		return jdbc.update(sqlQuery, params) == 1;
	}

	public boolean delete(int id) {
		MapSqlParameterSource params = new MapSqlParameterSource("id", id);

		return jdbc.update("delete from offers where id = :id", params) == 1;
	}

	public Offer getOffer(int id) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		// queryForObject devuelve 1 valor
		// parametros se definen con : como :name
		return jdbc.queryForObject("select * from offers where id = :id",
				params, new RowMapper<Offer>() {
					public Offer mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Offer offer = new Offer();
						offer.setId(rs.getInt("id"));
						offer.setName(rs.getString("name"));
						offer.setEmail(rs.getString("email"));
						offer.setText(rs.getString("text"));

						return offer;
					}
				});
	}

	public List<Offer> getOffers() {
		return jdbc.query("select * from offers", new RowMapper<Offer>() {

			public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Offer offer = new Offer();
				offer.setId(rs.getInt("id"));
				offer.setName(rs.getString("name"));
				offer.setEmail(rs.getString("email"));
				offer.setText(rs.getString("text"));

				return offer;
			}
		});
	}
}
