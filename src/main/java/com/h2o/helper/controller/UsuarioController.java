package com.h2o.helper.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.h2o.helper.dao.UsuarioDao;
import com.h2o.helper.model.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
@EnableJpaRepositories("com.h2o.helper.dao")
@EntityScan("com.h2o.helper.model")
public class UsuarioController {

    @Autowired
    private UsuarioDao usuarioDao;


//    this.nombre = nombre;
//        this.apellido = apellido;
//        this.direccion = direccion;
//        this.correo = correo;
//        this.foto = foto;
//        this.cuenta = cuenta;
//        this.telefono = telefono;

    @RequestMapping(value = "/")
    public String inicio(HttpServletRequest r){
        return "Hola";
    }





    @RequestMapping(value = "/create",method = RequestMethod.GET)
    public String create(HttpServletRequest r) throws JSONException, JsonProcessingException {
        String nombre = r.getParameter("nombre");
        String apellido = r.getParameter("apellido");
        String direccion = r.getParameter("direccion");
        String correo = r.getParameter("correo");
        String foto = r.getParameter("foto");
        String cuenta = r.getParameter("cuenta");
        String textTelefono = r.getParameter("telefono");

        Usuario usuario = null;
        usuario = new Usuario(nombre, apellido, direccion, correo, foto, cuenta, Integer.valueOf(textTelefono));

        if (usuario != null) {
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

            String usuarioJson = objectWriter.writeValueAsString(usuario);


            try {
                usuarioDao.save(usuario);
                return "\"usuario\" : " +usuarioJson;
            } catch (Exception e) {
               return "no se guardo";
            }
//                return  usuario.getNombre();
        } else {
            return "error";
        }

    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String listar() throws Exception {
       Iterable<Usuario> usuariosite = usuarioDao.findAll();
       List<Usuario> usuarios = new ArrayList<>();
       usuariosite.forEach(usuarios::add);
       Gson  gson = new Gson();



        JsonElement element = gson.toJsonTree(usuarios,new TypeToken<List<Usuario>>() {}.getType());

        JsonArray jsonArray = new JsonArray();
        for (Usuario u:usuariosite) {
            JsonObject json = new JsonObject();
            json.add("usuario",gson.toJsonTree(u));
            jsonArray.add(json);
        }


        
        if(! element.isJsonArray()){
            throw new Exception("error");
        }

//         jsonArray= element.getAsJsonArray();
        JsonObject jsonusuarios = new JsonObject();
        jsonusuarios.add("usuarios", gson.toJsonTree(jsonArray));

        return  jsonusuarios.toString();

    }

}
