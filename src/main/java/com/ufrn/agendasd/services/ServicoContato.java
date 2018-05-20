package com.ufrn.agendasd.services;

import com.ufrn.agendasd.exceptions.CustomNoContentException;
import com.ufrn.agendasd.exceptions.OutputMessage;
import com.ufrn.agendasd.implementations.ContatosDaoImpl;
import com.ufrn.agendasd.implementations.UsuarioDaoImpl;
import com.ufrn.agendasd.interfaces.IContatosDao;
import com.ufrn.agendasd.interfaces.IUsuarioDao;
import com.ufrn.agendasd.model.Contatos;
import com.ufrn.agendasd.model.Usuario;
import com.ufrn.agendasd.security.Secured;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/contato")
public class ServicoContato {

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Contatos contato, @Context SecurityContext securityContext) {

        IContatosDao contatosDao = new ContatosDaoImpl();
        IUsuarioDao usuariodao = new UsuarioDaoImpl();
        try {
            String idUser = securityContext.getUserPrincipal().getName();
            System.out.println("id usuario: " + idUser);
            Usuario usuario = usuariodao.findById(Integer.parseInt(idUser));

            if (usuario != null) {
                contato.setUsuario(usuario);
                contatosDao.save(contato);
            } else {
                return Response.status(Response.Status.NOT_MODIFIED)
                        .entity(contato)
                        .build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e)
                    .build();
        } 

        return Response.status(Response.Status.CREATED)
                .entity(new OutputMessage(200, "OK! Contato salvo com sucesso!"))
                .build();

    }

    @DELETE
    @Secured
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id, @Context SecurityContext securityContext) throws CustomNoContentException {
        IContatosDao contactDao = new ContatosDaoImpl();
        Contatos contato = contactDao.findById(id);
        String idUser = securityContext.getUserPrincipal().getName();

        if (contato == null) {
            throw new CustomNoContentException();
        }

        if (contato.getUsuario()!= null) {
            if (contato.getUsuario().getId() == Integer.parseInt((idUser))) {
                contactDao.delete(contato);
            } else {
                throw new CustomNoContentException();
            }
        }

        return Response.status(Response.Status.OK)
                .entity(new OutputMessage(200, "Contato " + contato.getNome() + " foi removido com sucesso!"))
                .build();
    }

    @PUT
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Contatos contato, @Context SecurityContext securityContext) {
        String idUser = securityContext.getUserPrincipal().getName();
        
        System.out.println("Id do usuario" + idUser);
        System.out.println("Id do contato" + contato.getUsuario().getId());
        try {
            if (contato.getUsuario().getId() == Integer.parseInt((idUser))) {
                IContatosDao contactDao = new ContatosDaoImpl();
                contactDao.save(contato);
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500, e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).entity(contato).build();
    }

    @GET
    @Secured
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listById(@PathParam("id") int idContato, @Context SecurityContext securityContext) {
        try {
            IContatosDao ContactDAO = new ContatosDaoImpl();
            int idUser = Integer.parseInt(securityContext.getUserPrincipal().getName());
            Contatos contato = ContactDAO.findById(idContato);

            if (contato == null) {
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                if (idUser == contato.getUsuario().getId()) {
                    return Response.status(Response.Status.OK).entity(contato).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500, e.getMessage()))
                    .build();
        }
    }    
}
