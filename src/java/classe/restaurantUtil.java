/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import javax.servlet.http.Part;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Alexandre
 */
public class restaurantUtil {
     Session session = null;
     
    // Méthode qui ajoute un nouveau restaurant dans la BD.
    public void ajouterResto(String description, String nom,String siteweb, Part img , long prixmoyen)
    {
        Restaurant unResto = new Restaurant();
        unResto.setDescription(description);
        unResto.setNom(nom);
        unResto.setSiteweb(siteweb);
        unResto.setPrixmoyen(prixmoyen);

        String path = getFilename(img);
        try
        {
            img.write("\\web\\resources\\images\\" +path);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        unResto.setImage(path);
        Typecuisine typeCus = new Typecuisine();
        typeCus.setTypecui("Gastronomique");
        unResto.setTypecuisine(typeCus);

        Transaction tx = null;
        this.session = HibernateUtil.getSessionFactory().openSession();        
        try{    
            tx = session.beginTransaction();
            session.saveOrUpdate(typeCus);           
            session.saveOrUpdate(unResto);
            tx.commit();
        }
        catch(Exception e)
        {
            tx.rollback();
            e.printStackTrace();
        }       
        this.session.close();       
    }
    
    // Méthode qui va chercher tout les restaurants dans la BD.
    public List<Restaurant> listeRestaurant()
    {
        List<Restaurant> listeResto = null;       
        Transaction tx = null;
        this.session = HibernateUtil.getSessionFactory().openSession();       
        try{        
            tx = session.beginTransaction();            
            Query Resto = session.createQuery("from Restaurant");           
            listeResto = Resto.list();
        } catch (Exception e) {
            e.printStackTrace();
        }     
        this.session.close();
        return listeResto;
    }
    
    // Méthode qui supprime un restaurant de la BD.
    public String supResto(Integer id)
    {
        Transaction tx = null;
        List<Restaurant> listeResto = null;
        Restaurant leRestoASup=null;
        this.session = HibernateUtil.getSessionFactory().openSession();       
        try{           
            tx = session.beginTransaction();           
            Query Resto = session.createQuery("from Restaurant where id =" +id);          
            listeResto = (List<Restaurant>)Resto.list(); 
            leRestoASup=(Restaurant)Resto.uniqueResult();           
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listeResto.size()!=0){
            session.delete(leRestoASup);
            tx.commit();
            this.session.close();
            return "Le restaurant a été supprimé.";            
        }else{
          this.session.close();
          return "Impossible de supprimer le restaurant.";
        }
    }
    
    // Méthode qui va chercher les restaurants ajoutés récemment dans la BD.
    public List<Restaurant> RestosRécents() {
        List<Restaurant> listeResto = null;       
        Transaction tx = null;
        this.session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();   
            Query requete = session.createQuery("FROM Restaurant ORDER BY Idresto DESC");
            requete.setFirstResult(0);
            requete.setMaxResults(3);
            listeResto = requete.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.session.close();
        return listeResto;
    }
    
    // Méthode qui recherche un restaurant
    public List<Restaurant> RechercherRestos(String infos){
        List<Restaurant> listeResto = null;
        Transaction tx = null;
        this.session = HibernateUtil.getSessionFactory().openSession();       
        try {
            tx = session.beginTransaction();           
            Query requete = session.createSQLQuery("SELECT * FROM restaurant INNER JOIN typecuisine ON restaurant.Typecuisine = typecuisine.Idtype WHERE restaurant.Nom LIKE :infos OR typecuisine.Typecui LIKE :infos").setParameter("infos", "%"+infos+"%"); 
            listeResto = requete.list();        
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.session.close();
        return listeResto;
    }
     
    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
    
    public Restaurant getRestoId(int id)
    {
        Restaurant unResto = null;
        List<Restaurant> listeRestaurants = null;        
        Transaction tx = null;
        this.session = HibernateUtil.getSessionFactory().openSession();       
        try{            
            tx = session.beginTransaction();            
            Query resto = session.createQuery("from Restaurant where idresto=:id");
            resto.setInteger("id",id);
            listeRestaurants = resto.list();
        } catch (Exception e){
            e.printStackTrace();
        }     
        this.session.close();
        if (listeRestaurants.size()!=0){
            return listeRestaurants.get(0);
        }else{
          return null;
        }    
    }   
}
