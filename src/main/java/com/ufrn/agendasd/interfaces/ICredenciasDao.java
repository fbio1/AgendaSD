package com.ufrn.agendasd.interfaces;

import com.ufrn.agendasd.model.Credenciais;

public interface ICredenciasDao extends IGenericDao<Credenciais, Integer>{
    	public Credenciais validUser(String userName, String pass);
	
	public Credenciais findByTokenUser(String token);
}
