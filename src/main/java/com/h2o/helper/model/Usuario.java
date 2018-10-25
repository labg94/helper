package com.h2o.helper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario implements Serializable {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose
    private long id;
    @Expose
    private String nombre, mail, rut,pass,phone;
    @Expose
    private float score;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="usuario")
    @Expose
    private List<Solicitud> solicitudList;

    public Usuario() {
        solicitudList= new ArrayList<>();
    }

    public Usuario(String nombre, String mail, String rut, String pass, String phone, float score) {
        this.nombre = nombre;
        this.mail = mail;
        this.rut = rut;
        this.pass = pass;
        this.phone = phone;
        this.score = score;
        solicitudList= new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public List<Solicitud> getSolicitudList() {
        return solicitudList;
    }

    public void setSolicitudList(List<Solicitud> solicitudList) {
        this.solicitudList = solicitudList;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", mail='" + mail + '\'' +
                ", rut='" + rut + '\'' +
                ", pass='" + pass + '\'' +
                ", phone='" + phone + '\'' +
                ", score=" + score +'\'' +
                ", solicitudes=" + solicitudList +'\'' +
                '}';
    }
}
