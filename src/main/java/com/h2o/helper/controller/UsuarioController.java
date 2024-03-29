package com.h2o.helper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.google.gson.*;

import com.h2o.helper.dao.SolicitudDao;
import com.h2o.helper.dao.UsuarioDao;
import com.h2o.helper.model.Solicitud;
import com.h2o.helper.model.Usuario;

import org.json.JSONException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


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


    @RequestMapping(value = "/createSolcitud",method = RequestMethod.GET,produces = "application/json")
    public String agregarSolicitud(HttpServletRequest r){
        String problem = r.getParameter("problem");
        String place = r.getParameter("place");
        String urgency = r.getParameter("urgency");
        String name = r.getParameter("name");
        String phone = r.getParameter("phone");
        String state = r.getParameter("state");
        String userId = r.getParameter("userId");



        Solicitud solicitud = new Solicitud(problem,place,urgency,name,phone,state);
        Usuario usuario;


        JsonObject jsonObject = new JsonObject();
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

//        String solicitudJson = objectWriter.writeValueAsString(solicitud);
//
//        json.add("usuario",gson.toJsonTree(u));
        jsonObject.add("solicitud",new Gson().toJsonTree(solicitud));

        if (userId!=null) {
            usuario = usuarioDao.findById(Long.valueOf(userId)).orElse(new Usuario());
            solicitud.setUsuario(usuario);
        }

        solicitudDao.save(solicitud);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/solicitudUsuario",method = RequestMethod.GET, produces = "application/json")
    public String listarDisponibles() throws Exception {
        String state = "Disponible";
        Iterable<Solicitud> solicitudIterable = solicitudDao.findAllByState(state);
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
        jsonusuarios.add("solicituds", gson.toJsonTree(jsonArray));

        return  jsonusuarios.toString();
    }


    @RequestMapping(value = "/listSolicitud",method = RequestMethod.GET, produces = "application/json")
    public String listarSolicitud() throws Exception {
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
        jsonusuarios.add("solicituds", gson.toJsonTree(jsonArray));
//cambia plis
        return  jsonusuarios.toString();
    }

    @RequestMapping(value = "/eliminarUsaurios",method = RequestMethod.GET)
    public String eliminarUsuarios() throws Exception {
        usuarioDao.deleteAll();
        return "eliminados";
    }

    @GetMapping(value ="/asignar")
    public String asignacion(HttpServletRequest r){
        String sol = r.getParameter("solicitud");
        String user = r.getParameter("usuario");


        int idsol = Integer.parseInt(sol);
        int iduser = Integer.parseInt(user);

        Solicitud solicitud = solicitudDao.findById(idsol);

        solicitud.setUsuario(usuarioDao.findById(iduser));
        String respuesta = null;
        try {
            solicitud.setState("actual");
            solicitudDao.save(solicitud);
            respuesta = "actualizado";
        }catch (Exception e){
            respuesta ="Error\n"+ e.getMessage();
        } finally {
          return  respuesta;
        }
    }


    @GetMapping(value ="/terminado")
    public String terminado(HttpServletRequest r){
        String sol = r.getParameter("solicitud");


        int idsol = Integer.parseInt(sol);

        Solicitud solicitud = solicitudDao.findById(idsol);

        String respuesta = null;
        try {
            solicitud.setState("terminado");
            solicitudDao.save(solicitud);
            respuesta = "Terminado";
        }catch (Exception e){
            respuesta ="Error\n"+ e.getMessage();
        } finally {
            return  respuesta;
        }
    }

}
