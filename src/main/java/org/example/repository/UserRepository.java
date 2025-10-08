package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.database.JPAUtil;
import org.example.entity.User;

import java.util.List;

public class UserRepository {

    public void save(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            em.persist(user);
            tx.commit();
        } catch(Exception e){
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            em.remove(em.contains(user) ? user : em.merge(user));
            tx.commit();
        } catch(Exception e){
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<User> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try{
            return em.createQuery("SELECT u FROM User u", User.class).getResultList();
        } catch(Exception e){
            e.printStackTrace();
            return List.of();
        }
        finally {
            em.close();
        }
    }

    public User findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try{
            return em.find(User.class, id);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public void deleteAllUsers() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try
        {
            tx.begin();

            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            for(User user : users) { // when deleting users, it will cascade to orders as well
                em.remove(user);
            }

            tx.commit();
        } catch(Exception e) {
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
}
