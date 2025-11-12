package models;

import java.util.*;

public class Ami {
    private User utilisateur;
    private boolean estEncours;  // Indicateur de la validité de l'amitié
    private List<Message> messagesRecus;  // Liste des messages reçus par l'ami

    // Constructeur
    public Ami(User utilisateur) {
        this.utilisateur = utilisateur;
        this.messagesRecus = new ArrayList<>();
        this.estEncours = true; // Par défaut, l'ami est actif
    }

    // Getter pour l'utilisateur associé à cet ami
    public User getUtilisateur() {
        return utilisateur;
    }

    // Vérifier si la relation d'amitié est toujours en cours
    public boolean getEstEncours() {
        return estEncours;
    }

    // Définir si la relation d'amitié est toujours en cours
    public void setEstEncours(boolean estEncours) {
        this.estEncours = estEncours;
    }

    // Ajouter un message à la liste des messages reçus
    public void ajouterMessage(Message message) {
        if (messagesRecus.size() >= 10) {
            messagesRecus.remove(0); // Limiter à 10 messages en supprimant le plus ancien
        }
        messagesRecus.add(message); // Ajouter le message à la liste
    }

    // Lire tous les messages reçus
    public List<Message> lireMessages() {
        return messagesRecus; // Retourne la liste des messages reçus
    }

    // Afficher tous les messages reçus sous forme lisible
    public String afficherMessagesLisibles() {
        StringBuilder sb = new StringBuilder();
        for (Message message : messagesRecus) {
            sb.append(message.toString()).append("\n\n");  // Afficher chaque message avec un espacement
        }
        return sb.toString();
    }

    // Méthode pour supprimer un message spécifique si nécessaire
    public void supprimerMessage(Message message) {
        messagesRecus.remove(message);  
    }

    // Vérifier si l'ami est toujours valide
    public boolean estValide() {
        return this.estEncours;  // L'ami est valide si la relation est toujours en cours
    }
}
