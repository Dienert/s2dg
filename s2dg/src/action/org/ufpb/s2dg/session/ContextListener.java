package org.ufpb.s2dg.session;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("contextListener")
public class ContextListener {

	@In(value="entityManager")
	EntityManager em;
	
	@SuppressWarnings("unchecked")
	public boolean updateDB() {
		Query query = em.createNativeQuery("select udt_name " +
										   "from information_schema.column_udt_usage " +
										   "where column_name = 'senha';");
		String result = (String)query.getSingleResult();
		if(result != null && result.equals("varchar")) {

			query = em.createNativeQuery("select id, senha from usuario");
			List rows = query.getResultList();
			LinkedHashMap<BigInteger, String> hashMap = new LinkedHashMap<BigInteger, String>();
			for (int i = 0; i < rows.size(); i++) {
				try {
					Object[] row = (Object[]) rows.get(i);
					String senha = (String) row[1];
					byte[] hash = Utils.generateHash(senha);
					String hexaString;
					hexaString = Utils.getHexString(hash);
					hashMap.put((BigInteger)row[0], hexaString);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			query = em.createNativeQuery("alter table usuario "+
										 "drop column senha");
			query.executeUpdate();

			query = em.createNativeQuery("alter table usuario "+
										 "add senha bytea;");
			query.executeUpdate();
			
			Set<BigInteger> keys = hashMap.keySet();

			for (BigInteger key : keys) {
				query = em.createNativeQuery("update Usuario set senha = " +
				   		 "DECODE('"+hashMap.get(key)+"', 'hex')" +
				   		 "where Usuario.id = "+key);
				query.executeUpdate();
	
			}
			return true;
		}
		return false;
	}

}
