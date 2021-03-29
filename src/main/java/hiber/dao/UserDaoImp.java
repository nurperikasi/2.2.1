package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   Session session;
   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public User getUserByCar(String model, int series) {
      User user = null;
      long carId = 0;
      Transaction transaction = null;
      session = sessionFactory.openSession();
      try {
         transaction = session.beginTransaction();
         Query query = session.createQuery("FROM Car where model = :paramModel and series = :paramSeries ", Car.class);
         query.setParameter("paramModel", model);
         query.setParameter("paramSeries", series);
         carId = ((Car) query.getSingleResult()).getID();

         Query query1 = session.createQuery("FROM User u INNER JOIN u.car c where c.id = :paramCarID");
         query1.setParameter("paramCarID", carId);
         user = (User) query1.getSingleResult();
         transaction.commit();
      } catch (HibernateException e) {
         e.printStackTrace();
      } finally {
         if (transaction != null) {
            transaction.rollback();
         }
         session.close();
      }
      return user;
   }
}

