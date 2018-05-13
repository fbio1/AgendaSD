package com.ufrn.agendasd.services;

import com.ufrn.agendasd.exceptions.OutputMessage;
import com.ufrn.agendasd.implementations.CredenciaisDaoImpl;
import com.ufrn.agendasd.implementations.UsuarioDaoImpl;
import com.ufrn.agendasd.interfaces.ICredenciasDao;
import com.ufrn.agendasd.interfaces.IUsuarioDao;
import com.ufrn.agendasd.model.Credenciais;
import com.ufrn.agendasd.model.Usuario;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.criterion.Order;

@Path("/usuario")
public class ServiceUsuario {
    
//    {
//	"usuario":{
//		"nome": "fabin",
//		"cpf": "132.868.657-45"
//	},
//	"username": "fbio15",
//	"password": "123"
//    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Credenciais c){         
        try{
            ICredenciasDao credenciaisDAO = new CredenciaisDaoImpl();    	
            credenciaisDAO.save(c);
        }catch (Exception e){
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500,e.getMessage()))
                    .build();
        }
        
        return Response
                .status(Response.Status.CREATED)
                .entity(c)
                .build();
    }
    
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listById(@PathParam("id") int id){
        try{
            IUsuarioDao usuarioDAO = new UsuarioDaoImpl(); 
            Usuario obj = usuarioDAO.findById(id);
            if (obj == null){
                return Response
                        .status(Response.Status.NO_CONTENT)
                        .build();
                
            }else{
                return Response
                    .status(Response.Status.OK)
                    .entity(obj)
                    .build();
            }
        }catch (Exception e){
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new OutputMessage(500,e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll(
            @QueryParam("orderby") @DefaultValue("id") String orderBy,
            @QueryParam("sort") @DefaultValue("asc") String sort){
        try{
            IUsuarioDao temperaturaDAO = new UsuarioDaoImpl(); 
            List<Usuario> obj;
            if(sort.equals("desc")){
                obj = temperaturaDAO.findAll(Order.desc(orderBy));
            }else{
                obj = temperaturaDAO.findAll(Order.asc(orderBy));
            }
            if (obj == null){
                return Response
                        .status(Response.Status.NO_CONTENT)
                        .build();
            }else{
                return Response
                    .status(Response.Status.OK)
                    .entity(obj)
                    .build();
            }
        }catch (Exception e){
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new OutputMessage(500,e.getMessage()))
                .build();
        }        
    }
}
