package com.ufrn.agendasd.services;

import com.ufrn.agendasd.exceptions.OutputMessage;
import com.ufrn.agendasd.implementations.CredenciaisDaoImpl;
import com.ufrn.agendasd.interfaces.ICredenciasDao;
import com.ufrn.agendasd.model.Credenciais;
import com.ufrn.agendasd.security.TokenUtil;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/autenticacao")
public class ServiceAutenticacao {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticUser(Credenciais credenciais, @Context SecurityContext securityContext) {
        ICredenciasDao credenciaisDao = new CredenciaisDaoImpl();
        try {
            Credenciais usuarioLogado = validUser(credenciais.getUsername(), credenciais.getPassword());
            String token = TokenUtil.criaToken(credenciais.getUsername());

            usuarioLogado.setToken(token);
            credenciaisDao.save(usuarioLogado);

            return Response.status(Response.Status.OK).entity(new OutputMessage(200, token)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new OutputMessage(Response.Status.UNAUTHORIZED.getStatusCode(),
                            "Permissão Negada. " + e.getMessage()))
                    .build();
        }

    }

    public Credenciais validUser(String username, String password) throws Exception {
        ICredenciasDao userDao = new CredenciaisDaoImpl();

        Credenciais user = userDao.validUser(username, password);

        if (user == null) {
            throw new Exception("Usuário não existe.");
        } else {
            return user;
        }
    }

}
