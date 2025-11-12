package models;

public class Message {
    private String origine;
    private String destinataire;
    private String sujet;
    private String corps;
    

    // Constructeur
    public Message(String origine, String destinataire, String sujet, String corps) {
        if (origine == null || destinataire == null || sujet == null || corps == null) {
            throw new IllegalArgumentException("Tous les champs doivent être non null");
        }
        this.origine = origine;
        this.destinataire = destinataire;
        this.sujet = sujet;
        this.corps = corps;
    }

    // Getters et Setters
    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getCorps() {
        return corps;
    }

    public void setCorps(String corps) {
        this.corps = corps;
    }
     public String getSujet() {
        return corps;
    }

    public void setSujet(String corps) {
        this.corps = corps;
    }

    // Méthode pour afficher le message sous une forme lisible
   /* public String toString() {
        return "De: " + origine + "\nÀ: " + destinataire + "\nSujet:" + sujet + "\nMessage: " +  corps+",";
    }*/
    
      public String toString() {
        return  origine + ":" + destinataire + ":" + sujet + ": " +  corps+"";
      }
}
