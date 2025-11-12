package models;

import java.util.*;

public class User {
    private String email;
    private String password;
    private ArrayList<Ami> amis; // Liste des amis de type Ami
    private ArrayList<String> demandesInvitation; // Invitations reçues
    private ArrayList<String> demandesEnvoyes; // Invitations reçues

    // Constructeur
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.amis = new ArrayList<>();
        this.demandesInvitation = new ArrayList<>();
        this.demandesEnvoyes = new ArrayList<>();
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Ami> getAmis() {
        return amis;
    }

    public ArrayList<String> getDemandesInvitation() {
        return demandesInvitation;
    }
       public ArrayList<String> getDemandesEnvoyes() {
        return demandesInvitation;
    }

    // Méthode pour ajouter un ami
  /*  public void ajouterAmi(User utilisateur) {
        // Limite à 4 amis max et vérifie si c'est déjà un ami
        if (amis.size() < 4 && !estDejaAmi(utilisateur)) {
            Ami nouvelAmi = new Ami(utilisateur);
             nouvelAmi.setEstEncours(true); // Relation activée
            amis.add(nouvelAmi);
        }
    }*/

    // Vérifie si l'utilisateur est déjà ami avec un autre utilisateur
    public boolean estDejaAmi(User utilisateur) {
        for (Ami ami : amis) {
            if (ami.getUtilisateur().getEmail().equals(utilisateur.getEmail())) {
                return true;
            }
        }
        return false;
    }

    // Ajouter une demande d'invitation
    public void ajouterDemandeInvitation(String demande) {
        if (!demandesInvitation.contains(demande)) {
            demandesInvitation.add(demande);
        }
    }
    // Ajouter une demande d'invitation
    public void ajouterDemandeEnvoyes(String demande) {
        if (!demandesEnvoyes.contains(demande)) {
            demandesEnvoyes.add(demande);
        }
    }



    // Méthode pour supprimer un ami
    public void supprimerAmi(String emailAmi) {
        // Recherche et supprime l'ami dans la liste
        for (Iterator<Ami> iterator = amis.iterator(); iterator.hasNext();) {
            Ami ami = iterator.next();
            if (ami.getUtilisateur().getEmail().equals(emailAmi)) {
                iterator.remove();  // Supprimer l'ami
                break;
            }
        }
    }
     public void accepterDemande(String loginAmi, ArrayList<User> tousLesUtilisateurs) {
        for (User utilisateur : tousLesUtilisateurs) {
            if (utilisateur.getEmail().equals(loginAmi)) {
                // Crée un nouvel Ami pour l'utilisateur
                if (amis.size() < 4 && !estDejaAmi(utilisateur)) {
                Ami nouvelAmi = new Ami(utilisateur);
                nouvelAmi.setEstEncours(true); // Relation activée
                amis.add(nouvelAmi);
                }

                // Ajoute également l'amitié côté de l'autre utilisateur
                Ami reciproque = new Ami(this);  // Crée la réciproque
                reciproque.setEstEncours(true);
                utilisateur.getAmis().add(reciproque);

                // Supprime la demande d'ami de l'utilisateur
                demandesInvitation.remove(loginAmi);
                demandesEnvoyes.remove(loginAmi);

                // Supprime également la demande d'ami de l'autre côté si nécessaire
                utilisateur.getDemandesInvitation().remove(this.email);
               

                break;
            }
        }
    }

    // Méthode pour afficher tous les amis sous forme lisible
   
}
