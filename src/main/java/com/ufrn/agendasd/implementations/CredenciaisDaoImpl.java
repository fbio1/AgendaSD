package com.ufrn.agendasd.implementations;

import com.ufrn.agendasd.interfaces.ICredenciasDao;
import com.ufrn.agendasd.model.Credenciais;

public class CredenciaisDaoImpl extends GenericDaoImpl<Credenciais, Integer> implements ICredenciasDao {	
    public CredenciaisDaoImpl() { 
           super(Credenciais.class); 
    } 	 
    
}
