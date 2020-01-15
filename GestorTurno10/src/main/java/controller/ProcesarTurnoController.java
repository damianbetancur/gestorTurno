/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.AreaJpaController;
import dao.Conexion;
import dao.EmpleadoJpaController;
import dao.EstadoTurnoJpaController;
import dao.HorarioAtencionTurnoJpaController;
import dao.TipoAtencionJpaController;
import dao.TipoEmpleadoJpaController;
import dao.TurnoJpaController;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import model.Area;
import model.Empleado;
import model.HorarioAtencionTurno;
import model.Persona;
import model.TipoAtencion;
import model.TipoEmpleado;
import model.TipoTramite;
import model.Turno;

/**
 *
 * @author Ariel
 */
public class ProcesarTurnoController {

    private final Turno nuevoTurno;

    private final AreaJpaController areaDAO;
    private final EmpleadoJpaController empleadoDAO;
    private final TipoEmpleadoJpaController tipoEmpleadoDAO;
    private final TipoAtencionJpaController tipoAtencionDAO;
    private final TurnoJpaController turnoDAO;
    private final HorarioAtencionTurnoJpaController horarioTurnoDAO;
    private final EstadoTurnoJpaController estadoTurnoDAO;

    public ProcesarTurnoController(Persona unaPersona) {
        nuevoTurno = new Turno();
        nuevoTurno.setUnaPersona(unaPersona);
        nuevoTurno.setUnEmpleado(LoginController.getInstanceUsuario().getUnEmpleado());
        areaDAO = new AreaJpaController(Conexion.getEmf());
        empleadoDAO = new EmpleadoJpaController(Conexion.getEmf());
        tipoEmpleadoDAO = new TipoEmpleadoJpaController(Conexion.getEmf());
        tipoAtencionDAO = new TipoAtencionJpaController(Conexion.getEmf());
        turnoDAO = new TurnoJpaController(Conexion.getEmf());
        horarioTurnoDAO = new HorarioAtencionTurnoJpaController(Conexion.getEmf());
        estadoTurnoDAO = new EstadoTurnoJpaController(Conexion.getEmf());
    }

    public Vector<Area> buscarTodasLasAreas() {
        Vector<Area> areasEncontrados = new Vector<>();
        for (Area areaRecorrido : areaDAO.findAreaEntities()) {
            areasEncontrados.add(areaRecorrido);
        }
        return areasEncontrados;
    }

    public Vector<TipoTramite> buscarTodosLosTramitesPorArea(Area unArea) {
        Vector<TipoTramite> tiposTramitesEncontrados = new Vector<>();
        for (Area areaRecorrido : areaDAO.findAreaEntities()) {
            if (unArea.getId().equals(areaRecorrido.getId())) {
                tiposTramitesEncontrados = (Vector<TipoTramite>) areaRecorrido.getTipoTramite();
            }
        }
        return tiposTramitesEncontrados;
    }

    public Turno getNuevoTurno() {
        return nuevoTurno;
    }

    public List<Empleado> buscarEmpleadoPorAreaTipoTramite(Area unArea, TipoTramite unTipoTramite) {
        List<Empleado> empleadosEncontrados = new ArrayList<>();

        for (Empleado empleadoRecorrido : empleadoDAO.findEmpleadoEntities()) {
            if (empleadoRecorrido.getUnAreaA().getId().equals(unArea.getId())) {
                for (TipoTramite tipoTramiteRecorrido : empleadoRecorrido.getTipoTramite()) {
                    if (tipoTramiteRecorrido.getId().equals(unTipoTramite.getId())) {
                        empleadosEncontrados.add(empleadoRecorrido);
                    }
                }
            }
        }
        return empleadosEncontrados;
    }

    public List<Empleado> buscaEmpleadosPorDNI(Area unArea, TipoTramite unTipoTramite, String dni) {
        List<Empleado> empleadosEncontrados = new ArrayList<>();
        for (Empleado empleadoRecorrido : empleadoDAO.findEmpleadoEntities()) {
            if (empleadoRecorrido.getUnAreaA().getId().equals(unArea.getId())) {
                for (TipoTramite tipoTramiteRecorrido : empleadoRecorrido.getTipoTramite()) {
                    if (tipoTramiteRecorrido.getId().equals(unTipoTramite.getId())) {
                        if (empleadoRecorrido.getDni().contains(dni)) {
                            empleadosEncontrados.add(empleadoRecorrido);
                        }
                    }
                }
            }
        }

        return empleadosEncontrados;
    }

    public Vector<TipoEmpleado> buscarTodosLosTiposDeEmpleados() {
        Vector<TipoEmpleado> tiposEmpleadosEncontrados = new Vector<>();
        for (TipoEmpleado tipoEmpleadoRecorrido : tipoEmpleadoDAO.findTipoEmpleadoEntities()) {
            tiposEmpleadosEncontrados.add(tipoEmpleadoRecorrido);
        }
        return tiposEmpleadosEncontrados;
    }

    public Vector<TipoAtencion> buscarTodasLosTiposDeAtencion() {
        Vector<TipoAtencion> tiposDeAtencionEncontrados = new Vector<>();
        for (TipoAtencion tipoAtencionRecorrido : tipoAtencionDAO.findTipoAtencionEntities()) {
            tiposDeAtencionEncontrados.add(tipoAtencionRecorrido);
        }
        return tiposDeAtencionEncontrados;
    }

    public Vector<HorarioAtencionTurno> buscarHorariosDeTurnoDisponibles() {
        Vector<HorarioAtencionTurno> horariosDeAtencionDisponibles = new Vector<>();
        
        for (HorarioAtencionTurno horariosDeAtencionDisponible : horarioTurnoDAO.findHorarioAtencionTurnoEntities()) {
            horariosDeAtencionDisponibles.add(horariosDeAtencionDisponible);
        }
        return horariosDeAtencionDisponibles;
    }

    public void crearNuevoTurno(Turno nuevoTurno){
        nuevoTurno.setUnEstadoTurno(estadoTurnoDAO.findEstadoTurno(1l));
        turnoDAO.create(nuevoTurno);
    }
    
    
}
