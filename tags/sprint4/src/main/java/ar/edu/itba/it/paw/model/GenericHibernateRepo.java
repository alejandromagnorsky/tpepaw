package ar.edu.itba.it.paw.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

public abstract class GenericHibernateRepo {
	private final SessionFactory sessionFactory;

	public GenericHibernateRepo(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, Serializable id) {
		return (T) getSession().get(type, id);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> find(String hql, Object... params) {
		Session session = getSession();

		Query query = session.createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		List<T> list = query.list();
		return list;
	}

	protected org.hibernate.classic.Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Serializable set(Object o) {
		return getSession().save(o);
	}
	
	public void delete(Object o){
		getSession().delete(o);
	}
	
	public void persist(Object o) {
		// For example, an IssueFile must be persisted 
		// because the file view uses the file id as a parameter
		// for the remove handler
		getSession().persist(o);		
	}
}
