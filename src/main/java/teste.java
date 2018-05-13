
import com.ufrn.agendasd.implementations.ContatosDaoImpl;
import com.ufrn.agendasd.implementations.CredenciaisDaoImpl;
import com.ufrn.agendasd.implementations.UsuarioDaoImpl;
import com.ufrn.agendasd.model.Contatos;
import com.ufrn.agendasd.model.Credenciais;
import com.ufrn.agendasd.model.Usuario;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fbio_
 */
public class teste {
    public static void main(String[] args) {
        Usuario usuario = new Usuario("taniro", "123123123");
        Contatos contato = new Contatos("asdfasd", "asdfasdf", usuario);
        Credenciais credenciais = new Credenciais("ahsdhfasdh", "asdfasdfasdf", "dasasdfas", usuario);
        
        System.out.println(usuario);
        
        System.out.println(contato);
        
        System.out.println(credenciais);
        
        CredenciaisDaoImpl credenciaisDaoImpl = new CredenciaisDaoImpl();
        credenciaisDaoImpl.save(credenciais);
        
//        UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl();
//        usuarioDaoImpl.save(usuario);    
        
        ContatosDaoImpl contatosDaoImpl = new ContatosDaoImpl();
        contatosDaoImpl.save(contato);
        
        System.exit(0);
    }
}
