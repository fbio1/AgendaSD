/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufrn.agendasd.security;

import com.ufrn.agendasd.exceptions.CustomNotAuthorizedException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import org.hibernate.criterion.Order;

/**
 *
 * @author Taniro
 */

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
String token;
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
                
        // Obtem o valor do dado do HEADER AUTHORIZATION da requisição HTTP
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Verifica existe o header e se ele está no formato correto (iniciando com "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomNotAuthorizedException("Usuário não está logado.");
        }

        // Obtem o token
        token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            // Valida o token
            TokenUtil.validaToken(token);
            
        } catch (Exception e) {
            //Aborta a execução
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build());
        }
        
        /*
        Implementação para obter o usuário logado na requisição
        */
        
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {
                    @Override
                    public String getName() {
                        //Aqui vai o nome do usuario/ID/etc que veio do banco após a validacao do token
                        if(token.length() >=7){
                            return token.substring(7, token.length());
                        }else{
                            return "Bearer";
                        }
                    }
                   
                   
                };
                 
            }
           
            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        });

    }
    
}
