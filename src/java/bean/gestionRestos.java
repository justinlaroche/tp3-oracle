/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import classe.Commentaire;
import classe.HibernateUtil;
import classe.Membre;
import classe.MembreUtil;
import classe.Restaurant;
import classe.Typecuisine;
import classe.Typemembre;
import classe.restaurantUtil;
import java.io.File;
import static java.util.Collections.list;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.IOException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

@ManagedBean
@RequestScoped
public class gestionRestos {
    Session session = null;
    
    private restaurantUtil restoUti;
    private Typecuisine typecuisine;
    private String nom;
    private String description;
    private String siteweb;
    private int idMembre;
    private long prixmoyen;
    private Part image;
    private String message;
    private String infoRestoRecherche;
    private List<Restaurant> resultatsRecherche;
    private int leIdResto;
     
    /**
     * Creates a new instance of ajouterClient
     */
    public gestionRestos() {
        restoUti = new restaurantUtil();
    }
    
    // Méthode qui ajoute un restaurant.
    public void ajouterResto()
    {      
        restoUti.ajouterResto(description, nom, siteweb, image, prixmoyen);
        message = "Le restaurant a été ajouté.";
    }
    
    // Méthode qui va chercher les types de cuisine dans la BD pour les afficher dans un drop-down list.
    public List<SelectItem> listerTypeCuisine(){
        List<SelectItem> items = new ArrayList<SelectItem>();
        List<Typecuisine> listeTypecuisine = null;
        
        Transaction tx = null;
        this.session = HibernateUtil.getSessionFactory().openSession();       
        try {           
            tx = session.beginTransaction();           
            Query q = session.createQuery("from typecuisine");
            listeTypecuisine = q.list();                     
        } catch (Exception e) {
            e.printStackTrace();
        } 
        items = (List<SelectItem>) (SelectItem) listeTypecuisine;
        return items;
    }   
    
    // Méthode qui va chercher les restaurants ajoutés récemment.
    public List<Restaurant> AfficherRestosRecents(){
        List<Restaurant> lstResto;
        lstResto = restoUti.RestosRécents();
        return lstResto;
    }
    
    // Méthode qui assigne tout les restaurants à notre liste.
    public List<Restaurant> AfficherToutRestos(){
        List<Restaurant> lstResto;
        lstResto = restoUti.listeRestaurant();
        return lstResto;
    }
    
    public List<Commentaire> lstCommentaire(int index)
    {
        Restaurant  unResto =restoUti.getRestoId(index);
        return unResto.ListCommentaire();
    }    
    
    // Méthode qui supprime un restaurant
    public void supprimerResto(Integer id)
    {
        message = restoUti.supResto(id);
    }
    
    // Méthode qui recherche un restaurant et retourne la liste trouvée.
    public void RechercherRestos(){
        resultatsRecherche = restoUti.RechercherRestos(infoRestoRecherche);
        if(resultatsRecherche.size()<=0){
            message="Aucun résultat correspondant à la recherche.";          
        }else{
            message = "Il y a " + resultatsRecherche.size() + " résultat(s) trouvé(s).";
        }
    }
      
    public double MoyenneNote(int id){
        Restaurant  unResto =restoUti.getRestoId(id);
        return unResto.MoyenneNote();
        
    }
   
    // Propriétés
    public List<Restaurant> getResultatsRecherche() {
        return resultatsRecherche;
    }
    public void setResultatsRecherche(List<Restaurant> resultatsRecherche) {
        this.resultatsRecherche = resultatsRecherche;
    }
    public Restaurant getResto(int id)
    {       
       Restaurant unResto = restoUti.getRestoId(id);
        return unResto;
    }
    public Typecuisine getTypecuisine() {
        return typecuisine;
    }
    public void setTypecuisine(Typecuisine typecuisine) {
        this.typecuisine = typecuisine;
    }
    public void setNom(String nom) {
        this.nom= nom;
    }
    public void setDescription(String Description) {
        this.description = Description;
    }
     public void setSiteWeb(String siteweb) {
        this.siteweb = siteweb;
    }
    public void setPrixMoyen(long prixmoyen) {
        this.prixmoyen = prixmoyen;
    }
     public void setImage(Part image) {
        this.image = image;
    }
      public String getNom() {
        return this.nom ;
    }
    public String getDescription() {
       return this.description;
    }
     public Part getImage() {
       return this.image;
    }
       public String getMessage() {
        return message;
    }  
    public String getSiteweb() {
        return siteweb;
    }
    public void setSiteweb(String siteweb) {
        this.siteweb = siteweb;
    }
    public long getPrixmoyen() {
        return prixmoyen;
    }
    public void setPrixmoyen(long prixmoyen) {
        this.prixmoyen = prixmoyen;
    }
     public String  getSiteWeb() {
        return this.siteweb;
    }
    /**
     * @return the infoRestoRecherche
     */
    public String getInfoRestoRecherche() {
        return infoRestoRecherche;
    }
    /**
     * @param infoRestoRecherche the infoRestoRecherche to set
     */
    public void setInfoRestoRecherche(String infoRestoRecherche) {
        this.infoRestoRecherche = infoRestoRecherche;
    }
    public int getLeIdResto() {
        return leIdResto;
    }

    public void setLeIdResto(int leIdResto) {
        this.leIdResto = leIdResto;
    }
}
