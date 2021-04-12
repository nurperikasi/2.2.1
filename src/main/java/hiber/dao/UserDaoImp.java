package hiber.dao;

import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   private final SessionFactory sessionFactory;

   public UserDaoImp(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().merge(user);
   }

   Session session;

   @Override
   public List<User> listUsers() {
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public User getUserByCar(String model, int series) {
      session = sessionFactory.openSession();
      TypedQuery<User> query = session.createQuery("select user from User user " +
              "left join user.car car where car.model = :paramModel and car.series = :paramSeries");
      query.setParameter("paramModel", model);
      query.setParameter("paramSeries", series);
      User user = query.getResultList().get(0);
      session.close();
      return user;
   }
}
