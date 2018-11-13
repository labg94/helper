package com.h2o.helper.dao;

import com.h2o.helper.model.Solicitud;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SolicitudDao extends CrudRepository<Solicitud,Long> {

    @Override
    <S extends Solicitud> S save(S s);


    Iterable<Solicitud> findAllByUsuarioId(int id);

    Iterable<Solicitud> findAllByUsuarioRutAndState(String rut,String state);

    Iterable<Solicitud> findAllByState(String state);

    Solicitud findById(int idsol);
}
