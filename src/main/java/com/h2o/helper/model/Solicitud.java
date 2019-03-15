package com.h2o.helper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@Entity
public class Solicitud implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose
    private int id;
    @Expose
    private String problem;
    @Expose
    private String place;
    @Expose
    private String urgency;
    @Expose
    private String name;
    @Expose
    private String phone;
    @Expose
    private String state;

    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    @JoinColumn(name="user_id")
    @JsonIgnore
    @Nullable
    @Expose(serialize = false)
    private  Usuario usuario;

    public Solicitud() {
    }

    public Solicitud(String problem, String place, String urgency, String name, String phone, String state) {
        this.problem = problem;
        this.place = place;
        this.urgency = urgency;
        this.name = name;
        this.phone = phone;
        this.state = state;

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        place = place;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    @Override
    public String toString() {
        return "Solicitud{" +
                "id=" + id +
                ", problem='" + problem + '\'' +
                ", place='" + place + '\'' +
                ", urgency='" + urgency + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
