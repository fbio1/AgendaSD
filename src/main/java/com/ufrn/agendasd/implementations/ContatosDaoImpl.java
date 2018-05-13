package com.ufrn.agendasd.implementations;

import com.ufrn.agendasd.interfaces.IContatosDao;
import com.ufrn.agendasd.model.Contatos;

public class ContatosDaoImpl extends GenericDaoImpl<Contatos, Integer> implements IContatosDao {	
    public ContatosDaoImpl() { 
           super(Contatos.class); 
    } 	 
    
}
