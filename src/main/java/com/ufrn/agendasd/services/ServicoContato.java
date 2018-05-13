package com.ufrn.agendasd.services;

import com.ufrn.agendasd.exceptions.OutputMessage;
import com.ufrn.agendasd.implementations.ContatosDaoImpl;
import com.ufrn.agendasd.interfaces.IContatosDao;
import com.ufrn.agendasd.model.Contatos;
import com.ufrn.agendasd.security.Secured;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/contato")
public class ServicoContato {
     
    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Contatos contato){         
        try{
            IContatosDao contatosDao = new ContatosDaoImpl();    	
            contatosDao.save(contato);
        }catch (Exception e){
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500,e.getMessage()))
                    .build();
        }
        
        return Response
                .status(Response.Status.CREATED)
                .entity(contato)
                .build();
    }
}
