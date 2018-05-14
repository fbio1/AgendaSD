package com.ufrn.agendasd.services;

import com.ufrn.agendasd.exceptions.OutputMessage;
import com.ufrn.agendasd.implementations.CredenciaisDaoImpl;
import com.ufrn.agendasd.interfaces.ICredenciasDao;
import com.ufrn.agendasd.model.Credenciais;
import com.ufrn.agendasd.security.TokenUtil;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.hibernate.criterion.Order;

//import com.quaresma.dao.CredenciaisDao;
//import com.quaresma.dao.CredenciaisDaoImpl;
//import com.quaresma.exceptions.OutputMessage;
//import com.quaresma.model.Credenciais;
//import com.quaresma.util.TokenUtil;
@Path("/autenticacao")
public class ServiceAutenticacao {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticaUser(Credenciais login) {
        try {
            Credenciais c = validaCredenciais(login.getUsername(),login.getPassword());
            String token = TokenUtil.criaToken(login.getUsername());
            c.setToken(token);
            ICredenciasDao credencialDAO = new CredenciaisDaoImpl();
            credencialDAO.save(c);
            return Response
                    .status(Response.Status.OK)
                    .entity(new OutputMessage(200, token))
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(new OutputMessage(Response.Status.UNAUTHORIZED.getStatusCode(), "Não autorizado: "
                            + e.getMessage()))
                    .build();
        }
    }

    private Credenciais validaCredenciais(String username, String password) throws Exception {
        
        ICredenciasDao credencialDAO = new CredenciaisDaoImpl();
        List<Credenciais> credenciais = credencialDAO.findAll(Order.desc("id")) ;    
        boolean logado = false;
        Credenciais c = new Credenciais();
        for(int i=0;i<credenciais.size();i++){
            if (username.equals(credenciais.get(i).getUsername()) && password.equals(credenciais.get(i).getPassword())){
               logado = true;
               c = credenciais.get(i);
            }
        
        }
        if(!logado){
            throw new Exception("Verifique o nome de usuário e senha.");
        }
        return c;
        
    }

}
