package com.ufrn.agendasd.implementations;

import com.ufrn.agendasd.interfaces.IUsuarioDao;
import com.ufrn.agendasd.model.Usuario;

public class UsuarioDaoImpl extends GenericDaoImpl<Usuario, Integer> implements IUsuarioDao {	
    public UsuarioDaoImpl() { 
           super(Usuario.class); 
    } 	 
    
}
