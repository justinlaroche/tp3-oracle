/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Alexandre
 */
@ManagedBean
@RequestScoped
public class MembreUtil {
    Session session = null;
    
    public String ajouterCommentaire(Membre mem,String contenu, int note)
    {
        restaurantUtil resUti = new restaurantUtil();
        Restaurant unResto = resUti.getRestoId(1);     
           
        String msgRetour ="";
        Commentaire comPourUserResto = null;
              
         Transaction tx = null;
               
         boolean contient =mem.DejaCom(unResto);
               
        if (contient ==true)
        {
            msgRetour= "L'utilisateur a déjà écris un commentaire pour ce restaurant";
        }
        else
        {
           try{ 
            this.session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Date dateCreation = new Date();
            Commentaire unCommentaire = new Commentaire();
            unCommentaire.setMembre(mem);
            unCommentaire.setContenu(contenu);
            unCommentaire.setDatecreation(dateCreation);
            unCommentaire.setNote(note);
            unCommentaire.setRestaurant(unResto);
                      
            mem.ajoutCommentaire(unCommentaire);
            unResto.AjoutCommentaire(unCommentaire);
	    
            session.saveOrUpdate(mem);
            session.saveOrUpdate(unResto);
            session.saveOrUpdate(unCommentaire);
            tx.commit();
            this.session.close();
        
            msgRetour= "commentaire ajouté";
        }
        catch(Exception e)
        {
            tx.rollback();
            e.printStackTrace();
        }      
       }
       return msgRetour;        
    }  
   
    public void supCommentaire(int index)
    {
        Membre leMembre = this.getClientId(1);
        Transaction tx = null;
        if(leMembre != null)
        {
          try{    
          tx = session.beginTransaction();
          Commentaire unCom = leMembre.GetCommentaire(index);
 
           leMembre.SupCommentaire(unCom);
                    
	    unCom.getRestaurant().SupCommentaire(unCom);
            session.saveOrUpdate(leMembre);
            session.saveOrUpdate(unCom.getRestaurant());
            tx.commit();
            
         this.session.close();     
        }
        catch(Exception e)
        {
            tx.rollback();
            e.printStackTrace();
        }
        }
    }
     
    public Commentaire getCommentaire( int index)
    {
        Membre leMembre = this.getClientId(1);
        Transaction tx = null;
        if(leMembre != null)
        {  
            return leMembre.GetCommentaire(index);     
        }
        else
        {
            return null;
        }       
    }      
    
    // Méthode qui ajoute un nouveau membre dans la BD.
    public void ajouterMembre(String nomUtil, String email,String Mdp)
    {
        Transaction tx = null;
        try{                       
            this.session = HibernateUtil.getSessionFactory().openSession();

            Membre SMembre = new Membre();
            SMembre.setNomutil(nomUtil);
            SMembre.setEmail(email);
            SMembre.setMpd(Mdp);
            typeMemUtil TypeUtil = new typeMemUtil();
            Typemembre typeMem = TypeUtil.getType(1);
            typeMem.setTypemem("membre");
            SMembre.setRestoPref(1);
            SMembre.setTypemembre(typeMem);
            SMembre.setTypecuisinePref(1);

            tx = session.beginTransaction();			           
            session.saveOrUpdate(SMembre);
            tx.commit();
        }
        catch(Exception e)
        {
            tx.rollback();
            e.printStackTrace();
        }      
        this.session.close();      
    }
    
    // Méthode qui va chercher le membre qui s'est connecté dans la BD.
    public Membre getClientConnexion(String nomUtil,String Mdp)
    {
        Membre unMembre = null;
        List<Membre> listeClients = null;
        
        Transaction tx = null;
        this.session = HibernateUtil.getSessionFactory().openSession();       
        try {           
            tx = session.beginTransaction();           
            Query mem = session.createQuery("from Membre where Nomutil = :nomUtil AND Mpd = :Mdp");
            mem.setString("nomUtil",nomUtil);
            mem.setString("Mdp",Mdp);
            listeClients = mem.list();                     
        } catch (Exception e) {
            e.printStackTrace();
        }       
        this.session.close();
        if (listeClients.size()!=0)
        {
            return listeClients.get(0);
        }
        else
        {
          return null;
        }    
    }
     
    public Membre getClientId(int id)
    {
        Membre unMembre = null;
        List<Membre> listeClients = null;
        
        Transaction tx = null;
        this.session = HibernateUtil.getSessionFactory().openSession();
       
        try {            
            tx = session.beginTransaction();
            
            // Liste de tous les livres
            Query mem = session.createQuery("from Membre where Idmembre=:id");
            mem.setInteger("id",id);
            listeClients = mem.list();                      
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        this.session.close();
        if (listeClients.size()!=0)
        {
            return listeClients.get(0);
        }
        else
        {
          return null;
        }    
    }  
    
    // Méthode qui déconnecte l'utilisateur et le renvoie à l'accueil.
    public String logoutUser(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index";
    }
}
