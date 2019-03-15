package com.h2o.helper.dao;

import com.h2o.helper.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface UsuarioDao  extends CrudRepository<Usuario,Long> {

    Usuario findById(long id);

    @Override
    <S extends Usuario> S save(S s);

    @Override
    @Transactional
    Iterable<Usuario> findAll();


    @Override
    void deleteAll();
}
