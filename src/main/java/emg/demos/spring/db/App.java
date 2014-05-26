package emg.demos.spring.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

public class App {
	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"emg/demos/spring/db/beans/beans.xml");
		try {
			OfferDAO offersDao = (OfferDAO) context.getBean("offerDao");
			List<Offer> offers = offersDao.getOffers();
			for (Offer offer : offers) {
				System.out.println(offer);
			}

			//Offer offer = offersDao.getOffer(1);
			//System.out.println("Should be Mike: " + offer);

			Offer offer1 = new Offer("Dave", "dave@mail.com", "coding java");
			Offer offer2 = new Offer("Karen", "karen@mail.com",
					"sotfware testing");
			Offer offer3 = new Offer(50, "Nom", "nom@mail.com", "omnom");

			System.out.println("Created record1: " + offersDao.create(offer1));
			System.out.println("Created record2: " + offersDao.create(offer2));
			System.out.println("Created record3: " + offersDao.create(offer3));

			offer3.setName("drop");
			System.out.println("Updated record: " + offersDao.update(offer3));

			System.out.println("Deleted records: " + offersDao.delete(50));

			List<Offer> offersList = new ArrayList<Offer>();
			offersList
					.add(new Offer(100, "Chris", "chris@mail.com", "coding python"));
			offersList.add(new Offer(103, "Naoto", "naoto@mail.com", "coding lua"));

			int[] result = offersDao.create(offersList);
			for (int item : result) {
				System.out.println("Updated " + item + " rows");
			}
		} catch (CannotGetJdbcConnectionException ex) {
			System.out.println("Cannot get database connection");
		} catch (DataAccessException ex) {
			System.out.println(ex.getLocalizedMessage());
			System.out.println(ex.getClass());
		}
		((ClassPathXmlApplicationContext) context).close();

	}

}
