/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 *
 * @author Alexandre
 */
public class typeMemUtil {
     public Typemembre getType(int id)
    {
        Session session = null;
        Typemembre unType = null;
        List<Typemembre> listeType = null;
        
        Transaction tx = null;
        session = HibernateUtil.getSessionFactory().openSession();
       
        try {
            
            tx = session.beginTransaction();
            
            // Liste de tous les livres
            Query typ = session.createQuery("from Typemembre where Idtypemembre=:id");
            typ.setInteger("id", id);
            listeType =(List<Typemembre>) typ.list();
           
           
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        session.close();
        if (listeType.size()!=0)
        {
            return listeType.get(0);
        }
        else
        {
          return null;
        }
    
    }
    
}
