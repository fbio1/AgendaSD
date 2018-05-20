package com.ufrn.agendasd.implementations;

import com.ufrn.agendasd.interfaces.ICredenciasDao;
import com.ufrn.agendasd.model.Credenciais;
import com.ufrn.agendasd.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

public class CredenciaisDaoImpl extends GenericDaoImpl<Credenciais, Integer> implements ICredenciasDao {

    public CredenciaisDaoImpl() {
        super(Credenciais.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Credenciais validUser(String userName, String pass) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Criteria consulta = session.createCriteria(Credenciais.class);
            consulta.add(Restrictions.eq("username", userName))
                    .add(Restrictions.eq("password", pass));
            Credenciais resultado = (Credenciais) consulta.uniqueResult();

            return resultado;
        } catch (RuntimeException erro) {
            throw erro;
        } finally {
            session.close();
        }
    }

    @SuppressWarnings({"deprecation", "rawtypes"})
    @Override
    public Credenciais findByTokenUser(String token) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Credenciais credenciais = new Credenciais();
        String tokenClean = token.trim();
        try {
            Query query = session.getSession().createQuery(
                    "SELECT c FROM Credenciais c "
                    + " WHERE c.token = :tokenClean");

            query.setParameter("tokenClean", tokenClean);
            credenciais = (Credenciais) query.uniqueResult();
            return credenciais;
        } catch (RuntimeException erro) {
            throw erro;
        } finally {
            session.close();
        }
    }

}
