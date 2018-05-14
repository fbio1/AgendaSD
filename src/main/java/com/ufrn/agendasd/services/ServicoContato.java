package com.ufrn.agendasd.services;

import com.ufrn.agendasd.exceptions.OutputMessage;
import com.ufrn.agendasd.implementations.ContatosDaoImpl;
import com.ufrn.agendasd.implementations.CredenciaisDaoImpl;
import com.ufrn.agendasd.implementations.UsuarioDaoImpl;
import com.ufrn.agendasd.interfaces.IContatosDao;
import com.ufrn.agendasd.interfaces.ICredenciasDao;
import com.ufrn.agendasd.interfaces.IUsuarioDao;
import com.ufrn.agendasd.model.Contatos;
import com.ufrn.agendasd.model.Credenciais;
import com.ufrn.agendasd.model.Usuario;
import com.ufrn.agendasd.security.Secured;
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

@Path("/contato")
public class ServicoContato {

    @Secured
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Contatos c, @Context SecurityContext securityContext) {
        try {
            Usuario usuarioLogado = getUsuarioLogado(securityContext);
            
            if (usuarioLogado != null) {
                c.setUsuario(usuarioLogado);
                IContatosDao contatoDAO = new ContatosDaoImpl();
                contatoDAO.save(c);
            } else {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(new OutputMessage(404, "Usuario náo autenticado!")).build();

            }

        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500, e.getMessage()))
                    .build();

        }
        return Response.status(Response.Status.CREATED).entity(c).build();
    }

    @Secured
    public Usuario getUsuarioLogado(SecurityContext securityContext) {

        String token = securityContext.getUserPrincipal().getName();
        System.out.println("token " + token);
        ICredenciasDao credencialDAO = new CredenciaisDaoImpl();
        List<Credenciais> credenciais = credencialDAO.findAll(Order.desc("id"));
        for (int i = 0; i < credenciais.size(); i++) {
            if (token.equals(credenciais.get(i).getToken())) {
                System.out.println("nome " + credenciais.get(i).getUsuario());
                return credenciais.get(i).getUsuario();               
            }

        }
        return null;
    }
}
