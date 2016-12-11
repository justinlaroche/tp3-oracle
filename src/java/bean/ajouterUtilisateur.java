/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import classe.Commentaire;
import classe.Membre;
import classe.MembreUtil;
import classe.Restaurant;
import classe.Typemembre;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author kfiliatreault
 */
@ManagedBean
@RequestScoped
@SessionScoped
public class ajouterUtilisateur {
    // Attributs
    private MembreUtil MembreUtil;
    private String nomUtil;
    private String email;
    private String Mdp;
    private String MdpConf;
    private Typemembre Type;
    private String message;
    private String contenu;
    private int note;
    
    /**
     * Creates a new instance of ajouterClient
     */
    public ajouterUtilisateur() {
        MembreUtil = new MembreUtil();
    }
    
    // Méthode qui va modifier le mot de passe d'un membre.
    public void modifierMembreMdp(){
        if(Mdp.equals(MdpConf)){
            FacesContext facesContext = FacesContext.getCurrentInstance();          
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            Membre unMembre = (Membre) session.getAttribute("membreConnecte");
            unMembre.modificationMotPasse(Mdp);
            message="Votre mot de passe a été modifié!";
        }else{
            message="Le mot de passe ne concorde pas dans les deux champs.";
        }
    }
    
    // Méthode qui va modifier les informations d'un membre.
    public void modifierMembreInfos(){
        FacesContext facesContext = FacesContext.getCurrentInstance();          
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        Membre unMembre = (Membre) session.getAttribute("membreConnecte");
        unMembre.modificationInfos(nomUtil, email);
        message="Vos informations ont été modifié!";
    }
    
    // Méthode qui call la méthode pour ajouter un membre à la BD.
    public void ajouterMembre()
    {       
        if(Mdp.equals(MdpConf)){
            MembreUtil.ajouterMembre(nomUtil, email, Mdp);
            message = "Votre compte a été créé.";
        }else{
            message = "Votre mot de passe ne concorde pas dans les deux champs.";
        }
    }
    
    // Méthode qui connecte l'utilisateur et débute sa session.
    public String getMembreCon()
    {
        Membre unMem ;
        unMem=MembreUtil.getClientConnexion(nomUtil,Mdp);
        if(unMem == null)
        {           
          message = "La connexion a échouée. Les données sont invalides.";
        }
        else
        {  
          FacesContext facesContext = FacesContext.getCurrentInstance();          
          HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
          session.setAttribute("membreConnecte", unMem);   
          session.setAttribute("nomutil", unMem.getNomutil());
          return "index";
        }
        return null;
    }
    
   public void ajoutCommentaire()
    {
         FacesContext facesContext = FacesContext.getCurrentInstance();          
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        Membre unMem =(Membre) session.getAttribute("membreConnecte");
        if(unMem != null)
        {
               String succes = MembreUtil.ajouterCommentaire(unMem,contenu, note);
                message =succes;
            
        }
        else
        {
            message= "Vous devez vous connectez avant d'inscrire un commentaire";
        }
     
    }
   public void supCommentaire(int index)
    {
        
        
        MembreUtil.supCommentaire(index);
        message ="le commentaire a été suprimmé";
    }
    public List<Commentaire> lstCommentaire()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();          
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        Membre unMem =(Membre) session.getAttribute("membreConnecte");
        return unMem.ListCommentaire();
    }
   public Commentaire getCommentaire(int index)
   {
      Commentaire unCom= MembreUtil.getCommentaire(index);
      return unCom;
   }


    // Propriétés
    public void setNom(String nom) {
        this.nomUtil = nom;
    }
    public void setEmail(String Email) {
        this.email = Email;
    }
     public void setMdp(String Mdp) {
        this.Mdp = Mdp;
    }
    public void setMdpConf(String Mdp) {
        this.MdpConf = Mdp;
    }
     public void setType(Typemembre Type) {
        this.Type = Type;
    }
      public String getNom() {
        return this.nomUtil ;
    }
    public String getEmail() {
       return this.email;
    }
    public String getMdp() {
       return this.Mdp;
    }  
    public Typemembre getType() {
        return this.Type;
    }
    public String getMdpConf()
    {
        return this.MdpConf;
    }
    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }
    public String getMessage() {
        return message;
    }
}
