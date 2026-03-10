package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Address;
import org.pfa.models.SessionUser;

public class AdresseRepository {
    public Address find(String pays, String ville, String rue, String numero, String complement, String codePostal) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Address addresse = session.createSelectionQuery("FROM Address WHERE pays = :pays AND ville = :ville AND rue = :rue AND numero = :numero AND complement = :complement AND codePostal = :codePostal", Address.class)
                .setParameter("pays", pays)
                .setParameter("ville", ville)
                .setParameter("rue", rue)
                .setParameter("numero", numero)
                .setParameter("complement", complement)
                .setParameter("codePostal", codePostal)
                .getSingleResultOrNull();
        session.close();
        return addresse;
    }

    public void save(Address addresse) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(addresse);;
        tx.commit();
        session.close();
    }
}
