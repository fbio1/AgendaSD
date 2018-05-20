package com.ufrn.agendasd.security;

import com.ufrn.agendasd.implementations.CredenciaisDaoImpl;
import com.ufrn.agendasd.interfaces.ICredenciasDao;
import com.ufrn.agendasd.model.Credenciais;
import java.security.Key;
import java.util.Calendar;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtil {

    public static String criaToken(String username) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS384;

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("35tdsxz");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .claim("usuario", username)
                .claim("create", Calendar.getInstance().getTime())
                .signWith(signatureAlgorithm, signingKey);//Token completo e compactado

        String compact = builder.compact();

        return compact;
    }

    public static Credenciais validaToken(String token) throws Exception {
        // Verifica se o token existe no banco de dados, caso não existir lançar uma exceção
        try {
            ICredenciasDao credenciaisDao = new CredenciaisDaoImpl();
            Credenciais credenciais = (Credenciais) credenciaisDao.findByTokenUser(token);

            if (credenciais != null) {
                if (credenciais.getToken() == null) {
                    throw new Exception();
                } else {
                    refreshToken(credenciais);
                    return credenciais;
                }
            } else {
                throw new Exception();
            }
        }finally{            
        } 
    }

    public static void refreshToken(Credenciais credencial) {
        ICredenciasDao credenciaisDao = new CredenciaisDaoImpl();
        Credenciais credenciaisNovoToken = new Credenciais();
        credenciaisNovoToken = credenciaisDao.findById(credencial.getId());
        credenciaisNovoToken.setToken(criaToken(credenciaisNovoToken.getUsername()));
        credenciaisDao.save(credenciaisNovoToken);
    }
}
