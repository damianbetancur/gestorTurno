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
import model.RequisitoOpcional;
import model.Tramite;

/**
 *
 * @author Ariel
 */
public class RequisitoOpcionalJpaController implements Serializable {

    public RequisitoOpcionalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RequisitoOpcional requisitoOpcional) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tramite unTramite = requisitoOpcional.getUnTramite();
            if (unTramite != null) {
                unTramite = em.getReference(unTramite.getClass(), unTramite.getId());
                requisitoOpcional.setUnTramite(unTramite);
            }
            em.persist(requisitoOpcional);
            if (unTramite != null) {
                unTramite.getRequisitoOpcional().add(requisitoOpcional);
                unTramite = em.merge(unTramite);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RequisitoOpcional requisitoOpcional) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RequisitoOpcional persistentRequisitoOpcional = em.find(RequisitoOpcional.class, requisitoOpcional.getId());
            Tramite unTramiteOld = persistentRequisitoOpcional.getUnTramite();
            Tramite unTramiteNew = requisitoOpcional.getUnTramite();
            if (unTramiteNew != null) {
                unTramiteNew = em.getReference(unTramiteNew.getClass(), unTramiteNew.getId());
                requisitoOpcional.setUnTramite(unTramiteNew);
            }
            requisitoOpcional = em.merge(requisitoOpcional);
            if (unTramiteOld != null && !unTramiteOld.equals(unTramiteNew)) {
                unTramiteOld.getRequisitoOpcional().remove(requisitoOpcional);
                unTramiteOld = em.merge(unTramiteOld);
            }
            if (unTramiteNew != null && !unTramiteNew.equals(unTramiteOld)) {
                unTramiteNew.getRequisitoOpcional().add(requisitoOpcional);
                unTramiteNew = em.merge(unTramiteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = requisitoOpcional.getId();
                if (findRequisitoOpcional(id) == null) {
                    throw new NonexistentEntityException("The requisitoOpcional with id " + id + " no longer exists.");
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
            RequisitoOpcional requisitoOpcional;
            try {
                requisitoOpcional = em.getReference(RequisitoOpcional.class, id);
                requisitoOpcional.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The requisitoOpcional with id " + id + " no longer exists.", enfe);
            }
            Tramite unTramite = requisitoOpcional.getUnTramite();
            if (unTramite != null) {
                unTramite.getRequisitoOpcional().remove(requisitoOpcional);
                unTramite = em.merge(unTramite);
            }
            em.remove(requisitoOpcional);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RequisitoOpcional> findRequisitoOpcionalEntities() {
        return findRequisitoOpcionalEntities(true, -1, -1);
    }

    public List<RequisitoOpcional> findRequisitoOpcionalEntities(int maxResults, int firstResult) {
        return findRequisitoOpcionalEntities(false, maxResults, firstResult);
    }

    private List<RequisitoOpcional> findRequisitoOpcionalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RequisitoOpcional.class));
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

    public RequisitoOpcional findRequisitoOpcional(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RequisitoOpcional.class, id);
        } finally {
            em.close();
        }
    }

    public int getRequisitoOpcionalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RequisitoOpcional> rt = cq.from(RequisitoOpcional.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
