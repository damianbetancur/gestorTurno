/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.TipoRequisitoOpcional;

/**
 *
 * @author Ariel
 */
public class TipoRequisitoOpcionalJpaController implements Serializable {

    public TipoRequisitoOpcionalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoRequisitoOpcional tipoRequisitoOpcional) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tipoRequisitoOpcional);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoRequisitoOpcional tipoRequisitoOpcional) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tipoRequisitoOpcional = em.merge(tipoRequisitoOpcional);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tipoRequisitoOpcional.getId();
                if (findTipoRequisitoOpcional(id) == null) {
                    throw new NonexistentEntityException("The tipoRequisitoOpcional with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoRequisitoOpcional tipoRequisitoOpcional;
            try {
                tipoRequisitoOpcional = em.getReference(TipoRequisitoOpcional.class, id);
                tipoRequisitoOpcional.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoRequisitoOpcional with id " + id + " no longer exists.", enfe);
            }
            em.remove(tipoRequisitoOpcional);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoRequisitoOpcional> findTipoRequisitoOpcionalEntities() {
        return findTipoRequisitoOpcionalEntities(true, -1, -1);
    }

    public List<TipoRequisitoOpcional> findTipoRequisitoOpcionalEntities(int maxResults, int firstResult) {
        return findTipoRequisitoOpcionalEntities(false, maxResults, firstResult);
    }

    private List<TipoRequisitoOpcional> findTipoRequisitoOpcionalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoRequisitoOpcional.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TipoRequisitoOpcional findTipoRequisitoOpcional(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoRequisitoOpcional.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoRequisitoOpcionalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoRequisitoOpcional> rt = cq.from(TipoRequisitoOpcional.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
