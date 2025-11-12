package controllers;
/**
 *
 * @author etena
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.SocketException;
import utils.ConstUtils;

public class ClientController {
    private DatagramSocket socket;
    private InetAddress serverAddress;
  
      public ClientController() throws Exception {
        this.socket =new DatagramSocket();
        this.serverAddress= InetAddress.getByName(ConstUtils.SERVER_NAME);
    }
      
    private String envoyerRequete(String requete) throws Exception{
        try {
              
                byte[] sendData = requete.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress,ConstUtils.SERVER_PORT);
                socket.send(sendPacket);
                
                //Reception d'une reponse du serveur
                byte[] receiveData = new byte[1024];
                 DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                 socket.receive(receivePacket);
                 String reponse=new String(receivePacket.getData(), 0, receivePacket.getLength());
                 
                return reponse;
            } catch (Exception e) {      
              return "Erreur "+e.getMessage();
          }
     }
    
    public String inscription(String identifiant, String motDePasse) throws Exception {
        String requete = ConstUtils.COMMAND_INSCRIPTION+"," + identifiant + "," + motDePasse;
        return envoyerRequete(requete);
    }

    public String connecter(String identifiant, String motDePasse) throws Exception {
            String requete =ConstUtils.COMMAND_CONNEXION+"," + identifiant + "," + motDePasse;
        return envoyerRequete(requete);
    }
    
    public String deconnecter(String identifiant) throws Exception {
        String requete = ConstUtils.COMMAND_DECONNEXION+"," + identifiant;
        return envoyerRequete(requete);
    }

    public String demanderAmi(String identifiant1, String identifiant2) throws Exception {
        String requete = ConstUtils.COMMAND_DEMANDE_AMI+"," + identifiant1 + "," + identifiant2;
        return envoyerRequete(requete);
    }
    public String reponseDemande(String identifiant1,String reponse, String identifiant2) throws Exception{
        String requete = ConstUtils.COMMAND_REPONSE_DEMANDE_AMI+"," + identifiant1 + "," +reponse+","+ identifiant2;
         return envoyerRequete(requete);
    }
    public String listerAmis(String identifiant) throws Exception {
        String requete = ConstUtils.COMMAND_LST_AMIS+"," + identifiant;
        return envoyerRequete(requete);
    }
     public String listerSuggestions(String identifiant) throws Exception {
        String requete = ConstUtils.COMMAND_SUGGESTIONS +"," + identifiant;
        return envoyerRequete(requete);
    }
    
     public String listerDemandesAmis(String identifiant) throws Exception {
        String requete =ConstUtils.COMMAND_LIST_INVITATIONS+"," + identifiant;
        return envoyerRequete(requete);
    }

    public String envoyerMessage(String identifiant, String destinataire,String sujet,String contenu) throws Exception {
        String requete = ConstUtils.COMMAND_MESSAGE+"," + identifiant + ","+destinataire + ","+sujet+"," + contenu;
        return envoyerRequete(requete);
    }
    public String envoyerMessageBroadcast(String identifiant,String sujet,String contenu) throws Exception {
        String requete = ConstUtils.COMMAND_MESSAGE_BROADCAST+"," + identifiant+ ","+sujet+"," + contenu;
        return envoyerRequete(requete);
    }
     public String lectureMessage(String identifiant) throws Exception {
        String requete = ConstUtils.COMMAND_LECTURE+"," + identifiant;
        return envoyerRequete(requete);
    }
    
    // Ferme la connexion
    public void fermer() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
  
}
