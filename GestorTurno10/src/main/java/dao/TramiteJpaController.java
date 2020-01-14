/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Area;
import model.RequisitoObligatorio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.RequisitoOpcional;
import model.Tramite;

/**
 *
 * @author Ariel
 */
public class TramiteJpaController implements Serializable {

    public TramiteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tramite tramite) {
        if (tramite.getRequisitoObligatorio() == null) {
            tramite.setRequisitoObligatorio(new ArrayList<RequisitoObligatorio>());
        }
        if (tramite.getRequisitoOpcional() == null) {
            tramite.setRequisitoOpcional(new ArrayList<RequisitoOpcional>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Area unAreaC = tramite.getUnAreaC();
            if (unAreaC != null) {
                unAreaC = em.getReference(unAreaC.getClass(), unAreaC.getId());
                tramite.setUnAreaC(unAreaC);
            }
            List<RequisitoObligatorio> attachedRequisitoObligatorio = new ArrayList<RequisitoObligatorio>();
            for (RequisitoObligatorio requisitoObligatorioRequisitoObligatorioToAttach : tramite.getRequisitoObligatorio()) {
                requisitoObligatorioRequisitoObligatorioToAttach = em.getReference(requisitoObligatorioRequisitoObligatorioToAttach.getClass(), requisitoObligatorioRequisitoObligatorioToAttach.getId());
                attachedRequisitoObligatorio.add(requisitoObligatorioRequisitoObligatorioToAttach);
            }
            tramite.setRequisitoObligatorio(attachedRequisitoObligatorio);
            List<RequisitoOpcional> attachedRequisitoOpcional = new ArrayList<RequisitoOpcional>();
            for (RequisitoOpcional requisitoOpcionalRequisitoOpcionalToAttach : tramite.getRequisitoOpcional()) {
                requisitoOpcionalRequisitoOpcionalToAttach = em.getReference(requisitoOpcionalRequisitoOpcionalToAttach.getClass(), requisitoOpcionalRequisitoOpcionalToAttach.getId());
                attachedRequisitoOpcional.add(requisitoOpcionalRequisitoOpcionalToAttach);
            }
            tramite.setRequisitoOpcional(attachedRequisitoOpcional);
            em.persist(tramite);
            if (unAreaC != null) {
                unAreaC.getTramites().add(tramite);
                unAreaC = em.merge(unAreaC);
            }
            for (RequisitoObligatorio requisitoObligatorioRequisitoObligatorio : tramite.getRequisitoObligatorio()) {
                Tramite oldUnTramiteOfRequisitoObligatorioRequisitoObligatorio = requisitoObligatorioRequisitoObligatorio.getUnTramite();
                requisitoObligatorioRequisitoObligatorio.setUnTramite(tramite);
                requisitoObligatorioRequisitoObligatorio = em.merge(requisitoObligatorioRequisitoObligatorio);
                if (oldUnTramiteOfRequisitoObligatorioRequisitoObligatorio != null) {
                    oldUnTramiteOfRequisitoObligatorioRequisitoObligatorio.getRequisitoObligatorio().remove(requisitoObligatorioRequisitoObligatorio);
                    oldUnTramiteOfRequisitoObligatorioRequisitoObligatorio = em.merge(oldUnTramiteOfRequisitoObligatorioRequisitoObligatorio);
                }
            }
            for (RequisitoOpcional requisitoOpcionalRequisitoOpcional : tramite.getRequisitoOpcional()) {
                Tramite oldUnTramiteOfRequisitoOpcionalRequisitoOpcional = requisitoOpcionalRequisitoOpcional.getUnTramite();
                requisitoOpcionalRequisitoOpcional.setUnTramite(tramite);
                requisitoOpcionalRequisitoOpcional = em.merge(requisitoOpcionalRequisitoOpcional);
                if (oldUnTramiteOfRequisitoOpcionalRequisitoOpcional != null) {
                    oldUnTramiteOfRequisitoOpcionalRequisitoOpcional.getRequisitoOpcional().remove(requisitoOpcionalRequisitoOpcional);
                    oldUnTramiteOfRequisitoOpcionalRequisitoOpcional = em.merge(oldUnTramiteOfRequisitoOpcionalRequisitoOpcional);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tramite tramite) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tramite persistentTramite = em.find(Tramite.class, tramite.getId());
            Area unAreaCOld = persistentTramite.getUnAreaC();
            Area unAreaCNew = tramite.getUnAreaC();
            List<RequisitoObligatorio> requisitoObligatorioOld = persistentTramite.getRequisitoObligatorio();
            List<RequisitoObligatorio> requisitoObligatorioNew = tramite.getRequisitoObligatorio();
            List<RequisitoOpcional> requisitoOpcionalOld = persistentTramite.getRequisitoOpcional();
            List<RequisitoOpcional> requisitoOpcionalNew = tramite.getRequisitoOpcional();
            List<String> illegalOrphanMessages = null;
            for (RequisitoObligatorio requisitoObligatorioOldRequisitoObligatorio : requisitoObligatorioOld) {
                if (!requisitoObligatorioNew.contains(requisitoObligatorioOldRequisitoObligatorio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RequisitoObligatorio " + requisitoObligatorioOldRequisitoObligatorio + " since its unTramite field is not nullable.");
                }
            }
            for (RequisitoOpcional requisitoOpcionalOldRequisitoOpcional : requisitoOpcionalOld) {
                if (!requisitoOpcionalNew.contains(requisitoOpcionalOldRequisitoOpcional)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RequisitoOpcional " + requisitoOpcionalOldRequisitoOpcional + " since its unTramite field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (unAreaCNew != null) {
                unAreaCNew = em.getReference(unAreaCNew.getClass(), unAreaCNew.getId());
                tramite.setUnAreaC(unAreaCNew);
            }
            List<RequisitoObligatorio> attachedRequisitoObligatorioNew = new ArrayList<RequisitoObligatorio>();
            for (RequisitoObligatorio requisitoObligatorioNewRequisitoObligatorioToAttach : requisitoObligatorioNew) {
                requisitoObligatorioNewRequisitoObligatorioToAttach = em.getReference(requisitoObligatorioNewRequisitoObligatorioToAttach.getClass(), requisitoObligatorioNewRequisitoObligatorioToAttach.getId());
                attachedRequisitoObligatorioNew.add(requisitoObligatorioNewRequisitoObligatorioToAttach);
            }
            requisitoObligatorioNew = attachedRequisitoObligatorioNew;
            tramite.setRequisitoObligatorio(requisitoObligatorioNew);
            List<RequisitoOpcional> attachedRequisitoOpcionalNew = new ArrayList<RequisitoOpcional>();
            for (RequisitoOpcional requisitoOpcionalNewRequisitoOpcionalToAttach : requisitoOpcionalNew) {
                requisitoOpcionalNewRequisitoOpcionalToAttach = em.getReference(requisitoOpcionalNewRequisitoOpcionalToAttach.getClass(), requisitoOpcionalNewRequisitoOpcionalToAttach.getId());
                attachedRequisitoOpcionalNew.add(requisitoOpcionalNewRequisitoOpcionalToAttach);
            }
            requisitoOpcionalNew = attachedRequisitoOpcionalNew;
            tramite.setRequisitoOpcional(requisitoOpcionalNew);
            tramite = em.merge(tramite);
            if (unAreaCOld != null && !unAreaCOld.equals(unAreaCNew)) {
                unAreaCOld.getTramites().remove(tramite);
                unAreaCOld = em.merge(unAreaCOld);
            }
            if (unAreaCNew != null && !unAreaCNew.equals(unAreaCOld)) {
                unAreaCNew.getTramites().add(tramite);
                unAreaCNew = em.merge(unAreaCNew);
            }
            for (RequisitoObligatorio requisitoObligatorioNewRequisitoObligatorio : requisitoObligatorioNew) {
                if (!requisitoObligatorioOld.contains(requisitoObligatorioNewRequisitoObligatorio)) {
                    Tramite oldUnTramiteOfRequisitoObligatorioNewRequisitoObligatorio = requisitoObligatorioNewRequisitoObligatorio.getUnTramite();
                    requisitoObligatorioNewRequisitoObligatorio.setUnTramite(tramite);
                    requisitoObligatorioNewRequisitoObligatorio = em.merge(requisitoObligatorioNewRequisitoObligatorio);
                    if (oldUnTramiteOfRequisitoObligatorioNewRequisitoObligatorio != null && !oldUnTramiteOfRequisitoObligatorioNewRequisitoObligatorio.equals(tramite)) {
                        oldUnTramiteOfRequisitoObligatorioNewRequisitoObligatorio.getRequisitoObligatorio().remove(requisitoObligatorioNewRequisitoObligatorio);
                        oldUnTramiteOfRequisitoObligatorioNewRequisitoObligatorio = em.merge(oldUnTramiteOfRequisitoObligatorioNewRequisitoObligatorio);
                    }
                }
            }
            for (RequisitoOpcional requisitoOpcionalNewRequisitoOpcional : requisitoOpcionalNew) {
                if (!requisitoOpcionalOld.contains(requisitoOpcionalNewRequisitoOpcional)) {
                    Tramite oldUnTramiteOfRequisitoOpcionalNewRequisitoOpcional = requisitoOpcionalNewRequisitoOpcional.getUnTramite();
                    requisitoOpcionalNewRequisitoOpcional.setUnTramite(tramite);
                    requisitoOpcionalNewRequisitoOpcional = em.merge(requisitoOpcionalNewRequisitoOpcional);
                    if (oldUnTramiteOfRequisitoOpcionalNewRequisitoOpcional != null && !oldUnTramiteOfRequisitoOpcionalNewRequisitoOpcional.equals(tramite)) {
                        oldUnTramiteOfRequisitoOpcionalNewRequisitoOpcional.getRequisitoOpcional().remove(requisitoOpcionalNewRequisitoOpcional);
                        oldUnTramiteOfRequisitoOpcionalNewRequisitoOpcional = em.merge(oldUnTramiteOfRequisitoOpcionalNewRequisitoOpcional);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tramite.getId();
                if (findTramite(id) == null) {
                    throw new NonexistentEntityException("The tramite with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tramite tramite;
            try {
                tramite = em.getReference(Tramite.class, id);
                tramite.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tramite with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RequisitoObligatorio> requisitoObligatorioOrphanCheck = tramite.getRequisitoObligatorio();
            for (RequisitoObligatorio requisitoObligatorioOrphanCheckRequisitoObligatorio : requisitoObligatorioOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tramite (" + tramite + ") cannot be destroyed since the RequisitoObligatorio " + requisitoObligatorioOrphanCheckRequisitoObligatorio + " in its requisitoObligatorio field has a non-nullable unTramite field.");
            }
            List<RequisitoOpcional> requisitoOpcionalOrphanCheck = tramite.getRequisitoOpcional();
            for (RequisitoOpcional requisitoOpcionalOrphanCheckRequisitoOpcional : requisitoOpcionalOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tramite (" + tramite + ") cannot be destroyed since the RequisitoOpcional " + requisitoOpcionalOrphanCheckRequisitoOpcional + " in its requisitoOpcional field has a non-nullable unTramite field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Area unAreaC = tramite.getUnAreaC();
            if (unAreaC != null) {
                unAreaC.getTramites().remove(tramite);
                unAreaC = em.merge(unAreaC);
            }
            em.remove(tramite);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tramite> findTramiteEntities() {
        return findTramiteEntities(true, -1, -1);
    }

    public List<Tramite> findTramiteEntities(int maxResults, int firstResult) {
        return findTramiteEntities(false, maxResults, firstResult);
    }

    private List<Tramite> findTramiteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tramite.class));
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

    public Tramite findTramite(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tramite.class, id);
        } finally {
            em.close();
        }
    }

    public int getTramiteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tramite> rt = cq.from(Tramite.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
