package com.h2o.helper.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.h2o.helper.dao.SolicitudDao;
import com.h2o.helper.dao.UsuarioDao;
import com.h2o.helper.model.Solicitud;
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

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletRequest;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
@EnableJpaRepositories("com.h2o.helper.dao")
@EntityScan("com.h2o.helper.model")
public class UsuarioController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private SolicitudDao solicitudDao;

    @RequestMapping(value = "/")
    public String inicio(HttpServletRequest r){
        return "Hola";
    }





    @RequestMapping(value = "/create",method = RequestMethod.GET)
    public String create(HttpServletRequest r) throws JSONException, JsonProcessingException {
        String nombre = r.getParameter("nombre");
        String rut = r.getParameter("rut");
        String pass = r.getParameter("pass");
        String correo = r.getParameter("correo");
        String textScore = r.getParameter("score");
        String textTelefono = r.getParameter("telefono");

        Usuario usuario = null;
        usuario = new Usuario(nombre,correo,rut, pass, textTelefono,Float.valueOf(textScore));

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

    @RequestMapping(value = "/list",method = RequestMethod.GET, produces = "application/json")
    public String listar() throws Exception {
       Iterable<Usuario> usuariosite = usuarioDao.findAll();
       List<Usuario> usuarios = new ArrayList<>();
       usuariosite.forEach(usuarios::add);
       Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
//       Gson  gson = new Gson();


//        JsonElement element = gson.toJsonTree(usuarios,new TypeToken<List<Usuario>>() {}.getType());

        JsonArray jsonArray = new JsonArray();
        for (Usuario u:usuariosite) {
            JsonObject json = new JsonObject();
//            json.add("usuario",gson.toJsonTree(u));
//            jsonArray.add(json);
            jsonArray.add(gson.toJsonTree(u));
        }


//
//        if(! element.isJsonArray()){
//            throw new Exception("error");
//        }

//         jsonArray= element.getAsJsonArray();
        JsonObject jsonusuarios = new JsonObject();
        jsonusuarios.add("usuarios", gson.toJsonTree(jsonArray));
//        List<Solicitud> solicituds = usuarios.get(0).getSolicitudList();

        return  jsonusuarios.toString();

    }


    @RequestMapping(value = "/createSolcitud",method = RequestMethod.GET)
    public String agregarSolicitud(HttpServletRequest r){
        String problem = r.getParameter("problem");
        String place = r.getParameter("place");
        String urgency = r.getParameter("urgency");
        String name = r.getParameter("name");
        String phone = r.getParameter("phone");
        String state = r.getParameter("state");
        String userId = r.getParameter("userid");


        Solicitud solicitud = new Solicitud(problem,place,urgency,name,phone,state);
        Usuario usuario;


        JsonObject jsonObject = new JsonObject();
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

//        String solicitudJson = objectWriter.writeValueAsString(solicitud);
//
//        json.add("usuario",gson.toJsonTree(u));
        jsonObject.add("solicitud",new Gson().toJsonTree(solicitud));
        usuario = usuarioDao.findById(Long.valueOf(userId)).orElse(new Usuario());
        solicitud.setUsuario(usuario);
        solicitudDao.save(solicitud);
        return jsonObject.toString();
    }


    @RequestMapping(value = "/listSolicitud",method = RequestMethod.GET)
    public JsonObject listarSolicitud() throws Exception {
        Iterable<Solicitud> solicitudIterable = solicitudDao.findAll();
        List<Solicitud> solicitudArrayList = new ArrayList<>();
        solicitudIterable.forEach(solicitudArrayList::add);
//        Gson  gson = new Gson();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        JsonArray jsonArray = new JsonArray();
        for (Solicitud s:solicitudIterable) {
//            JsonObject json = new JsonObject();
            JsonObject jsonSolicitud = new JsonObject();
            jsonSolicitud.addProperty("id",s.getId());
            jsonSolicitud.addProperty("name",s.getName());
            jsonSolicitud.addProperty("phone",s.getPhone());
            jsonSolicitud.addProperty("place",s.getPlace());
            jsonSolicitud.addProperty("problem",s.getProblem());
            jsonSolicitud.addProperty("state",s.getState());
            jsonSolicitud.addProperty("urgency",s.getUrgency());
            jsonSolicitud = gson.toJsonTree(s).getAsJsonObject();

//            json.add("Solicitud",gson.toJsonTree(s));
            jsonArray.add(jsonSolicitud);
        }

        JsonObject jsonusuarios = new JsonObject();
        jsonusuarios.add("Solicitudes", gson.toJsonTree(jsonArray));

        return  jsonusuarios;

    }

    @RequestMapping(value = "/eliminarUsaurios",method = RequestMethod.GET)
    public String eliminarUsuarios() throws Exception {
        usuarioDao.deleteAll();
        return "eliminados";
    }


}
