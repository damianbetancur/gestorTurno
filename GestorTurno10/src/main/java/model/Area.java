/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author USER
 */
@Entity
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "numero", nullable = false)
    private int numero;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "fk_organismo", nullable = false, updatable = false)
    private Organismo unOrganismoA;

    @OneToMany(mappedBy = "unAreaA")
    private List<Empleado> empleados;

    @OneToMany(mappedBy = "unAreaB")
    private List<Turno> turnos;

    @OneToMany(mappedBy = "unAreaC")
    private List<Tramite> tramites;

    @JoinTable(
            name = "rel_Tipo_Tramite_Area",
            joinColumns = @JoinColumn(name = "FK_Tipo_Tramite", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "FK_Area", nullable = false)
    )
    @ManyToMany(cascade = CascadeType.ALL)
    private List<TipoTramite> tipoTramite;

    public Area() {
        this.empleados = new ArrayList<>();
        this.turnos = new ArrayList<>();
        this.tramites = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombre();
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Organismo getUnOrganismoA() {
        return unOrganismoA;
    }

    public void setUnOrganismoA(Organismo unOrganismoA) {
        this.unOrganismoA = unOrganismoA;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
    }

    public List<Tramite> getTramites() {
        return tramites;
    }

    public void setTramites(List<Tramite> tramites) {
        this.tramites = tramites;
    }

    public List<TipoTramite> getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(List<TipoTramite> tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    

    
}
