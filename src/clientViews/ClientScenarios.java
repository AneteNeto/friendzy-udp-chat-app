package clientViews;

 /*
 * @author etena
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Scanner;
import java.net.SocketException;
import controllers.ClientController;

public class ClientScenarios {
   
    public static void affichageMenu() {
                System.out.println("Choisissez une option :");
                System.out.println("1. Inscription");
                System.out.println("2. Connexion");
                System.out.println("3. Envoyer une demande d’amitié");
                System.out.println("4. Lister les amitiés");
                System.out.println("5. Lister demandes de amis");
                System.out.println("6. Accepter/Refuser");
                System.out.println("7. Envoyer un message");
                System.out.println("8. Lecture Message");
                System.out.println("9. Envoyer un message Broacast");
                System.out.println("10. Voir Demandes envoyes");
                System.out.println("11. Lister Suggestions d'amis");
                System.out.println("12. Supprimer un ami");
                System.out.println("13. Quitter");
                
               
    }
    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        ClientController clientController=null;
        
        while(true){
             try {
                clientController=new ClientController();
                affichageMenu();
                int choix = scanner.nextInt();
                scanner.nextLine(); // Nettoyer le buffer
                String texteMessage,sujet;

                String message = "";
                String reponse="";
                switch (choix) {
                    case 1:
                        System.out.print("Entrez votre identifiant : ");
                        String identifiant = scanner.nextLine();
                        System.out.print("Entrez votre mot de passe : ");
                        String motDePasse = scanner.nextLine();
                        reponse=clientController.inscription(identifiant, motDePasse);
                        System.out.println(reponse);
                        break;
                    case 2:
                        System.out.print("Entrez votre identifiant : ");
                        identifiant = scanner.nextLine();
                        System.out.print("Entrez votre mot de passe : ");
                        motDePasse = scanner.nextLine();
                        reponse=clientController.connecter(identifiant, motDePasse);
                        System.out.println(reponse);
                        break;
                    case 3:
                        System.out.print("Entrez votre identifiant : ");
                        String id1 = scanner.nextLine();
                        System.out.print("Entrez l’identifiant de votre ami : ");
                        String id2 = scanner.nextLine();
                        reponse=clientController.demanderAmi(id1, id2);
                        System.out.println(reponse);
                        break;
                    case 4:
                        System.out.print("Entrez votre identifiant : ");
                        identifiant = scanner.nextLine();
                        reponse=clientController.listerAmis(identifiant);
                        System.out.println(reponse);
                        break;
                    case 5:
                        System.out.print("Entrez votre identifiant : ");
                        identifiant = scanner.nextLine();
                        reponse=clientController.listerDemandesAmis(identifiant);
                        System.out.println(reponse);
                        break;
                    case 6:
                        System.out.print("Entrez votre identifiant : ");
                        identifiant = scanner.nextLine();
                        System.out.print("Entrez votre reponse accepter/refuser : ");
                         String reponse_demande = scanner.nextLine();
                         System.out.print("Entrez l’identifiant de  l'ami : ");
                         id2 = scanner.nextLine();
                        reponse=clientController.reponseDemande(identifiant,reponse_demande,id2);
                        System.out.println(reponse);
                        break;
                    case 7:
                        System.out.print("Entrez votre identifiant : ");
                        identifiant = scanner.nextLine();
                        System.out.print("Entrez l’identifiant du destinataire : ");
                        String destinataire = scanner.nextLine();
                        System.out.print("Entrez votre sujet: ");
                        sujet = scanner.nextLine();
                        System.out.print("Entrez votre message : ");
                        texteMessage = scanner.nextLine();
                        reponse=clientController.envoyerMessage(identifiant, destinataire,sujet,texteMessage);
                        System.out.println(reponse);
                        break;
                     case 8:
                        System.out.print("Entrez votre identifiant : ");
                        identifiant = scanner.nextLine();
                       // System.out.print("Entrez l’identifiant du ami : ");
                        //String amides = scanner.nextLine();
                        reponse=clientController.lectureMessage(identifiant);
                        System.out.println(reponse);
                        break;
                        
                     case 9:
                        System.out.print("Entrez votre identifiant : ");
                        identifiant = scanner.nextLine();
                        System.out.print("Entrez votre Sujet : ");
                        sujet = scanner.nextLine();
                        System.out.print("Entrez votre message : ");
                        texteMessage = scanner.nextLine();
                        reponse=clientController.envoyerMessageBroadcast(identifiant,sujet,texteMessage);
                        System.out.println(reponse);
                        break;
                     case 10:
                         System.out.print("Entrez votre identifiant : ");
                          identifiant = scanner.nextLine();
                          reponse=clientController.listerSuggestions(identifiant);
                          System.out.println(reponse);
                         break;
                         
                     case 11:
                         System.out.print("Entrez votre identifiant : ");
                          identifiant = scanner.nextLine();
                          reponse=clientController.listerSuggestions(identifiant);
                          System.out.println(reponse);
                         break;
                     case 12:
                         System.out.print("Entrez votre identifiant : ");
                          identifiant = scanner.nextLine();
                          reponse=clientController.listerSuggestions(identifiant);
                          System.out.println(reponse);
                         break;
                    case 13:
                        System.out.println("Au revoir !");
                        break;
                    default:
                        System.out.println("Option invalide");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
}