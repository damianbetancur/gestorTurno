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
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Date;
import java.util.List;
import model.Tramite;
import model.Turno;

/**
 *
 * @author Ariel
 */
public class ProcesarTramiteController {

    private final Tramite nuevoTramite;

    private final TurnoJpaController turnoDAO;

    private final AreaJpaController areaDAO;
    private final EmpleadoJpaController empleadoDAO;
    private final TipoEmpleadoJpaController tipoEmpleadoDAO;
    private final TipoAtencionJpaController tipoAtencionDAO;
    private final HorarioAtencionTurnoJpaController horarioTurnoDAO;
    private final EstadoTurnoJpaController estadoTurnoDAO;

    public ProcesarTramiteController() {

        this.turnoDAO = new TurnoJpaController(Conexion.getEmf());

        this.nuevoTramite = new Tramite();

        areaDAO = new AreaJpaController(Conexion.getEmf());

        empleadoDAO = new EmpleadoJpaController(Conexion.getEmf());

        tipoEmpleadoDAO = new TipoEmpleadoJpaController(Conexion.getEmf());

        tipoAtencionDAO = new TipoAtencionJpaController(Conexion.getEmf());

        horarioTurnoDAO = new HorarioAtencionTurnoJpaController(Conexion.getEmf());

        estadoTurnoDAO = new EstadoTurnoJpaController(Conexion.getEmf());
    }

    public List<Turno> buscarTodosLosTurnos() {
        List<Turno> turnosEncontrados = new ArrayList<>();

        LoginController.getInstanceUsuario().getUnEmpleado();

        for (Turno turnoRecorrido : turnoDAO.findTurnoEntities()) {
            turnosEncontrados.add(turnoRecorrido);
        }
        return turnosEncontrados;
    }

    public Tramite getNuevoTramite() {
        return nuevoTramite;
    }

    public List<Turno> buscarTodosLosTurnosDeUnEmpleadoEnElDiaActual() {
        List<Turno> turnosParaAtender = new ArrayList<>();
        //Todos los turnos de un area
        for (Turno turnoRecorrido : areaDAO.findArea(LoginController.getInstanceUsuario().getUnEmpleado().getUnAreaA().getId()).getTurnos()) {
            if (compararFecha(turnoRecorrido.getFecha())) {
                if (LoginController.getInstanceUsuario().getUnEmpleado().getId().equals(turnoRecorrido.getUnEmpleado().getId())) {
                    if (turnoRecorrido.getUnEstadoTurno().getNombre().equals("En Espera")) {
                        turnosParaAtender.add(turnoRecorrido);
                    }
                }
            }
        }
        ordenarTurnos(turnosParaAtender);
        return turnosParaAtender;
    }
    
    private boolean compararFecha(Date fechaB) {
        boolean comparacion = false;

        Calendar calendario = Calendar.getInstance(); // fecha actual        
        int aniofechaA = calendario.get(Calendar.YEAR);
        int mesfechaA = calendario.get(Calendar.MONTH);
        int diafechaA = calendario.get(Calendar.DAY_OF_MONTH)+1;

        calendario.setTime(fechaB); // fecha de turno
        int aniofechaB = calendario.get(Calendar.YEAR);
        int mesfechaB = calendario.get(Calendar.MONTH);
        int diafechaB = calendario.get(Calendar.DAY_OF_MONTH)+2;
        if (aniofechaA == aniofechaB) {
            if (mesfechaA == mesfechaB) {
                if (diafechaA == diafechaB) {
                    comparacion = true;
                }
            }
        }
        return comparacion;
    }
    
    private void ordenarTurnos(List<Turno> turnos){
        Collections.sort(turnos);
    }

}
