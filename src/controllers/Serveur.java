package controllers;

import java.net.*;
import java.io.*;
import java.util.*;
import models.Ami;
import models.Message;
import models.User;
import utils.ConstUtils;

public class Serveur {
    // Liste des utilisateurs connectés
    private ArrayList<User> utilisateurs;
    // Liste des messages échangés
    private Map<String, ArrayList<Message>> messages; // Clé: email de l'utilisateur, Valeur: liste des messages reçus
    private SessionController sessionControl;
    private DatagramSocket socket;
    // MAP POUR LES USERS COONECTES
    private Map<String, InetSocketAddress> utilisateursconnectes;
    // Constructeur
    public Serveur() throws SocketException {
        utilisateurs = new ArrayList<>();
        messages = new HashMap<>();
        utilisateursconnectes = new HashMap<>();//initialisation utilisateurs connectés
        socket = new DatagramSocket(ConstUtils.SERVER_PORT);
    }
    // Démarrer le serveur pour écouter les messages entrants
    public void demarrer() {
         System.out.println("Serveur lancé sur le port " + ConstUtils.SERVER_PORT);
        while (true) {
            try {
                // Recevoir les données du client
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String messageRecu = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Message reçu: " + messageRecu);

                traiterMessage(messageRecu, receivePacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Traiter le message reçu et effectuer des actions en fonction de la commande
    private void traiterMessage(String requeteRecu, DatagramPacket receivePacket) throws IOException {
        String[] requete = requeteRecu.split(",");
        String login = requete[1]; // Email de l'utilisateur
        // Vérifier que l'utilisateur existe, sinon envoyer une réponse d'erreur
        User utilisateur = trouverUtilisateur(login);
        /*if (utilisateur == null) {
            envoyerMessage("serveur,erreur,utilisateur_inexistant", receivePacket.getAddress(), receivePacket.getPort());
            return;
        }*/
        String commande = requete[0]; // Commande : demande_ami, message, lecture, etc.
        /*if (!FunctionUtils.isValidCommand(commande)) {
            envoyerMessage(ConstUtils.MSG_ERREUR + "commande_invalide", receivePacket.getAddress(), receivePacket.getPort());
            return;
        }*/
        
        switch (commande) {
             case ConstUtils.COMMAND_INSCRIPTION:
                // L'utilisateur s'inscrit
                String motDePasse = requete[2];
                inscrireUtilisateur(login, motDePasse, receivePacket);
                break;
            case ConstUtils.COMMAND_CONNEXION:
                // L'utilisateur se connecte
                motDePasse = requete[2];
                connecterUtilisateur(login, motDePasse, receivePacket);
                break;
            case ConstUtils.COMMAND_DECONNEXION:
                // L'utilisateur se dconnecte
                deconnecterUtilisateur(login,receivePacket);
                break;
            case ConstUtils.COMMAND_DEMANDE_AMI:
                // L'utilisateur souhaite inviter un autre utilisateur
                String ami = requete[2]; // L'ami que l'utilisateur souhaite inviter
                User utilisateurAmi =trouverUtilisateur(ami);
                if(utilisateurAmi==null){ // cas utilisateur inexistant
                      envoyerMessage("serveur,utilisateur_inexistant,erreur,Utilisateur introuvable", receivePacket.getAddress(),receivePacket.getPort());
                }
                else if(utilisateur.getAmis().size()>=3){ //cas depasser 3 amis
                     envoyerMessage("serveur,limite,erreur,Limite d'amis atteinte", receivePacket.getAddress(),receivePacket.getPort());
                }
                else if (utilisateur.estDejaAmi(trouverUtilisateur(ami))==true){ //cas deja amis
                    envoyerMessage("serveur,deja_ami,erreur,Utilisateur est dans votre liste d'amis", receivePacket.getAddress(),receivePacket.getPort());
                }
                else {
                    envoyerMessage("serveur,demande_ami,succes," + ami, receivePacket.getAddress(), receivePacket.getPort());
                    ajouterDemandeAmi(utilisateur, ami);
                }
                break;
                
            case ConstUtils.COMMAND_REPONSE_DEMANDE_AMI:
                String reponse = requete[2]; // "accepter" ou "refuser"
                String amiReponse = requete[3]; // L'ami dont on répond à la demande
                if (reponse.equals("accepter")) {
                    accepterDemande(utilisateur, amiReponse);
                    envoyerMessage("serveur,demande_ami,succes," + amiReponse, receivePacket.getAddress(), receivePacket.getPort());      
                } else if (reponse.equals("refuser")) {
                    refuserDemande(utilisateur, amiReponse);
                    envoyerMessage("serveur,demande_ami,succes," + amiReponse, receivePacket.getAddress(), receivePacket.getPort());
                }
                break;
            case ConstUtils.COMMAND_SUPPRIMER_AMI:
                String amisup = requete[2]; //l'ami que l'utilisateur souhaite supprimer
                    utilisateur.supprimerAmi(amisup); 
                    envoyerMessage("serveur,supprimer_ami,succes," + amisup, receivePacket.getAddress(), receivePacket.getPort());
                break;
                
            case ConstUtils.COMMAND_LST_AMIS:
                String liste= listamis(utilisateur);
                envoyerMessage("serveur,liste_des_amis,succes,"+ liste,receivePacket.getAddress(), receivePacket.getPort());
                break;
            case ConstUtils.COMMAND_SUGGESTIONS: 
                String suggestions= listpasamis(utilisateur);
                envoyerMessage("serveur,liste_suggestions,succes,"+ suggestions,receivePacket.getAddress(), receivePacket.getPort());
                break;
                
            case ConstUtils.COMMAND_LIST_INVITATIONS:
                String invitations=listdemandes(utilisateur);
                envoyerMessage("serveur,liste_des_invitations,succes,"+ invitations,receivePacket.getAddress(), receivePacket.getPort());
                break;
                
             case ConstUtils.COMMAND_LIST_DEMANDES_ENVOYES:
                String demandes=listdemandesenvoyes(utilisateur);
                envoyerMessage("serveur,liste_des_invitations,succes,"+ demandes,receivePacket.getAddress(), receivePacket.getPort());
                break;
            case ConstUtils.COMMAND_MESSAGE:
                // L'utilisateur envoie un message
                String destinataire = requete[2];
                String sujet = requete[3];
                String corps = requete[4];
                envoyerMessage("serveur,message_envoye,succes", receivePacket.getAddress(), receivePacket.getPort());
                envoyerMessageAuDestinataire(utilisateur, destinataire, sujet, corps);
                break;
                
            case ConstUtils.COMMAND_MESSAGE_BROADCAST:
                sujet =requete[2];
                corps = requete[3];
                envoyerMessageBroadcast(utilisateur, sujet, corps);
                envoyerMessage("serveur,message_envoye,succes", receivePacket.getAddress(), receivePacket.getPort());
                break;
                
            case ConstUtils.COMMAND_LECTURE:
                // L'utilisateur souhaite lire ses messages
                String messagesUtilisateur = lireMessages(utilisateur);
                envoyerMessage("serveur,lecture,succes,"+messagesUtilisateur, receivePacket.getAddress(), receivePacket.getPort());
                break;

            default:
                envoyerMessage("serveur,commande_invalide,erreur,ommande invalide", receivePacket.getAddress(), receivePacket.getPort());
                break;
        }
    }
    // Trouver un utilisateur par son email
    private User trouverUtilisateur(String email) {
        for (User user : utilisateurs) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
    // Inscrire un nouvel utilisateur
    private void inscrireUtilisateur(String email, String motDePasse, DatagramPacket receivePacket) throws IOException {
        // Vérifier si l'utilisateur est déjà inscrit
         if (utilisateurs.size() >= 4) {
            envoyerMessage("serveur,inscription,erreur,Limite utilisateurs atteinte", receivePacket.getAddress(), receivePacket.getPort());
            return;
        }
        if (trouverUtilisateur(email) != null) {
            envoyerMessage("serveur,inscription,erreur,Utilisateur existant", receivePacket.getAddress(), receivePacket.getPort());
            return;
        }
        // Ajouter l'utilisateur à la liste
        User nouvelUtilisateur = new User(email, motDePasse);
        utilisateurs.add(nouvelUtilisateur);

        // Envoyer une confirmation d'inscription réussie
        envoyerMessage("serveur,inscription,succes", receivePacket.getAddress(), receivePacket.getPort());
    }

    // Connecter un utilisateur
    private void connecterUtilisateur(String email, String motDePasse, DatagramPacket receivePacket) throws IOException {
        User utilisateur = trouverUtilisateur(email);
        if (utilisateur == null) {
            envoyerMessage("serveur,connexion,erreur,Utilisateur inexistant", receivePacket.getAddress(), receivePacket.getPort());
            return;
        }
        /*if(sessionControl.isSessionActive(email)){
             envoyerMessage("serveur,connexion,erreur,Session deja active", receivePacket.getAddress(), receivePacket.getPort());
            return;
        }*/
        // Vérifier le mot de passe
        if (!utilisateur.getPassword().equals(motDePasse)) {
            envoyerMessage("serveur,connexion,erreur,Mot de passe incorrect", receivePacket.getAddress(), receivePacket.getPort());
            return;
        }
        // création de la session
        //sessionControl.registerSession(email);
        //Enregistrer l'addresse reseau de l'utilisateur
        utilisateursconnectes.put(email, new InetSocketAddress(receivePacket.getAddress(),receivePacket.getPort()));
        envoyerMessage("serveur,connexion,succes,"+utilisateur.getEmail(), receivePacket.getAddress(), receivePacket.getPort());
    }
    
    private void deconnecterUtilisateur(String email,DatagramPacket receivePacket) throws IOException {
            User utilisateur = trouverUtilisateur(email);

            if (utilisateur == null) {
                envoyerMessage("serveur,deconnexion,erreur,Utilisateur Inexistant", receivePacket.getAddress(), receivePacket.getPort());
                return;
            }
        // suppression de la session
      //  sessionControl.removeSession(email);
        // suppression de l'utilisateur de la map des users actives
        utilisateursconnectes.remove(email);
        envoyerMessage("serveur,deconnexion,succes, "+utilisateur.getEmail(), receivePacket.getAddress(), receivePacket.getPort());
    }

    // Ajouter une demande d'ami à l'utilisateur et la demande envoyé au demandeur
    private void ajouterDemandeAmi(User utilisateur, String ami) {
       for (User user : utilisateurs) {
            if (user.getEmail().equals(ami)) {
                user.ajouterDemandeInvitation(utilisateur.getEmail());   
                utilisateur.ajouterDemandeEnvoyes(ami);
                break;
            } 
        }
    }
    private void accepterDemande(User utilisateur, String loginAmi) throws IOException {
        utilisateur.accepterDemande(loginAmi, utilisateurs);
        //Notifier l'utilisateur demandeur si conneté
        InetSocketAddress adresseDemandeur = utilisateursconnectes.get(loginAmi);
        if (adresseDemandeur!=null){
         
                envoyerMessage("serveur,demande ami accepté, "+ utilisateur.getEmail(), adresseDemandeur.getAddress(), adresseDemandeur.getPort());
        }
    }
    
    private void refuserDemande(User utilisateur, String loginAmi) {
        utilisateur.getDemandesInvitation().remove(loginAmi);
        User ami =trouverUtilisateur(loginAmi);        
        ami.getDemandesEnvoyes().remove(utilisateur.getEmail());
    }
    
    public String listdemandes(User utilisateur) {
        StringBuilder sb = new StringBuilder();
        
        //formate les demandes
        for (String invitation : utilisateur.getDemandesInvitation()) {
            sb.append(invitation).append("\n");
        }
        return sb.toString().replaceAll(";$", ""); //pour sup le dernier ;
    }
    public String listamis(User utilisateur) {
        /*if(utilisateur.getAmis().isEmpty()){
           return "Liste vide";  //pas d'ami pour le moment
        }*/
        StringBuilder sb = new StringBuilder();
        for (Ami ami : utilisateur.getAmis()) {
            sb.append(ami.getUtilisateur().getEmail()).append("\n");
        }
        return sb.toString();
    } 
    public String listdemandesenvoyes(User utilisateur){
        StringBuilder sb = new StringBuilder();
        for (String demande : utilisateur.getDemandesEnvoyes()) {
            sb.append(demande).append("\n");
        }
        return sb.toString().replaceAll(";$", ""); //pour sup le dernier ;
        
    }
    
    public String listpasamis(User utilisateurActuel) {   //liste des utilisateurs qui ne font pas partie de ses amis
        StringBuilder suggestions = new StringBuilder();
        // exclure ce user
        for( User utilisateur : utilisateurs){
            if (utilisateur.getEmail().equals(utilisateurActuel.getEmail())){
                continue;
            }
            //exclure les utilisateurs pour lesquels ce user a deja envoyé une demande d'ami
            if (utilisateurActuel.getDemandesEnvoyes().contains(utilisateur.getEmail())){
                continue;
            }
        // excluere ses amis
            if (utilisateurActuel.estDejaAmi(utilisateur)){
                continue;
        }
            //ajouter l'utilisateur à la liste
         suggestions.append(utilisateur.getEmail()).append("\n");
        }
        return suggestions.toString();
      
    }
    // Envoyer un message à un utilisateur (destinataire)
    private void envoyerMessageAuDestinataire(User utilisateur, String destinataire, String sujet, String corps) {
        for (User user : utilisateurs) {
            if (user.getEmail().equals(destinataire)) {
                // Crée un message à envoyer à cet utilisateur
                Message message = new Message(utilisateur.getEmail(), destinataire, sujet, corps);
                // Ajouter le message aux amis de l'utilisateur destinataire
                for (Ami ami : user.getAmis()) {
                    ami.ajouterMessage(message); // Ajouter ce message aux amis aussi
                }
                break;
            }
        }
    }
       // Envoyer un message broadcast
    private void envoyerMessageBroadcast(User utilisateur, String sujet, String corps) {
                // Ajouter le message aux amis de l'utilisateur destinataire
                for (Ami ami : utilisateur.getAmis()) {
                    User destinataire = ami.getUtilisateur();
                     // Crée un message pour chaque ami
                    Message message = new Message(utilisateur.getEmail(), destinataire.getEmail(), sujet, corps);
                    ami.ajouterMessage(message); // Ajouter ce message aux amis aussi
                //notifier l'utilisateur s'il est connecté
               
                } 
        }  
    // Lire les messages reçus par un utilisateur
    private String lireMessages(User utilisateur) {
        StringBuilder messagesUtilisateur = new StringBuilder();
        // Parcours de la liste des amis de l'utilisateur
        for (Ami ami : utilisateur.getAmis()) {
            // Ajouter les messages reçus par cet ami
            for (Message message : ami.lireMessages()) {  // lireMessages() renvoie la liste des messages reçus
                messagesUtilisateur.append(message.toString()).append("\n");
            }
        }
        return messagesUtilisateur.toString();
    }
    // Envoyer un message au client
    private void envoyerMessage(String message, InetAddress clientAddress, int clientPort) throws IOException {
        byte[] sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
        socket.send(sendPacket);
    }
    // Méthode principale pour démarrer le serveur
    public static void main(String[] args) {
        try {
            Serveur serveur = new Serveur();
            serveur.demarrer();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
