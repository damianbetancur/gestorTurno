/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.Conexion;
import dao.TipoUsuarioJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import model.TipoUsuario;
import model.Usuario;

/**
 *
 * @author Ariel
 */
public class UsuarioController {

    //DAO
    private final UsuarioJpaController usuarioDAO;    
    private final TipoUsuarioJpaController tipoUsuarioDAO;

    

    public UsuarioController() {
        this.usuarioDAO = new UsuarioJpaController(Conexion.getEmf());
        this.tipoUsuarioDAO = new TipoUsuarioJpaController(Conexion.getEmf());
    }

    public boolean agregarUsuario(Usuario nuevoUsuario) {
        //verifica que el dni sea unico
        boolean nicknamePermitido = true;
        for (Usuario usuarioRecorrido : usuarioDAO.findUsuarioEntities()) {
            if (nuevoUsuario.getNombre().equals(usuarioRecorrido.getNombre())) {
                nicknamePermitido = false;
            }
        }

        if (nicknamePermitido) {
            usuarioDAO.create(nuevoUsuario);
        }
        return nicknamePermitido;
    }

    public boolean modificarUsuario(Usuario actualUsuario, Usuario nuevoUsuario) throws Exception {
        //verifica que el dni sea unico
        nuevoUsuario.setId(actualUsuario.getId());
        boolean dniPermitido = true;
        if (actualUsuario.getNombre().equals(nuevoUsuario.getNombre())) {
            usuarioDAO.edit(nuevoUsuario);
        } else {
            for (Usuario usuarioRecorrido : usuarioDAO.findUsuarioEntities()) {
                if (nuevoUsuario.getNombre().equals(usuarioRecorrido.getNombre())) {
                    dniPermitido = false;
                }
            }
            if (dniPermitido) {
                usuarioDAO.edit(nuevoUsuario);
            }
        }
        return dniPermitido;
    }

    public void eliminarUsuario(Usuario nuevoUsuario) throws NonexistentEntityException {
        usuarioDAO.destroy(nuevoUsuario.getId());
    }

    public List<Usuario> buscarTodosLosUsuarios() {
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        usuariosEncontrados = usuarioDAO.findUsuarioEntities();
        return usuariosEncontrados;
    }

    public List<Usuario> buscarUsuariosPorNickName(String nickName) {
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        for (Usuario usuarioRecorrido : usuarioDAO.findUsuarioEntities()) {
            if (usuarioRecorrido.getNombre().contains(nickName)) {
                usuariosEncontrados.add(usuarioRecorrido);
            }
        }
        return usuariosEncontrados;
    }

    public Vector<TipoUsuario> buscarTodosLosTiposDeUsuarios() {
        Vector<TipoUsuario> tiposUsuariosEncontrados = new Vector<>();
        for (TipoUsuario tipoUsuarioRecorrido : tipoUsuarioDAO.findTipoUsuarioEntities()) {
            tiposUsuariosEncontrados.add(tipoUsuarioRecorrido);
        }
        return tiposUsuariosEncontrados;
    }

    
}
