/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package argoview;

import java.util.GregorianCalendar;

/**
 * Objet permettant de gérer les données Argos enregistrées pour chaque animal
 * @author Florent Fayollas
 */
public class DonneeArgos {
    
    /**
     * Constructeur de l'objet contenant les données Argos
     * @param numBalise Numéro de la balise concernée par les données
     * @param precision Précision du positionnement
     * @param heure     Heure d'enregistrement de la position
     * @param date      Date d'enregistrement de la position
     * @param latitude  Latitude de la position
     * @param longitude Longitude de la position
     */
    public DonneeArgos ( String numBalise, String precision,
                            String date, String heure,
                            String latitude, String longitude ) {
        this.numBalise = numBalise;
        this.precision = precision;
        
        int annee = 0;
        int mois = 0;
        int jour = 0;
        int heures = 0;
        int minutes = 0;
        
        try {
            annee   = Integer.parseInt(date.substring(0, 3));
            mois    = Integer.parseInt(date.substring(5, 6));
            jour    = Integer.parseInt(date.substring(8, 9));
            heures  = Integer.parseInt(heure.substring(0, 1));
            minutes = Integer.parseInt(heure.substring(3, 4));
        } catch ( NumberFormatException e ) {
            System.out.println("Impossible de charger les nombres : " + e.toString());
        }
        
        this.date = new GregorianCalendar(annee, mois, jour, heures, minutes);
        
        try {
            this.latitude  = Double.parseDouble(latitude);
            this.longitude = Double.parseDouble(longitude);
        } catch ( NumberFormatException e ) {
            System.out.println("Impossible de charger les nombres : " + e.toString());
        }
    }
    
    /*
        Déclaration des variables
    */
    private String numBalise = new String();
    private String precision = new String();
    private GregorianCalendar date = new GregorianCalendar();
    private double latitude = 0;
    private double longitude = 0;

    /**
     * @return Renvoie le numéro de la balise
     */
    public String getNumBalise() {
        return numBalise;
    }

    /**
     * @param numBalise Le numéro de la balise à modifier
     */
    public void setNumBalise(String numBalise) {
        this.numBalise = numBalise;
    }

    /**
     * @return Renvoie la précision
     */
    public String getPrecision() {
        return precision;
    }

    /**
     * @param precision Précision du postitionnement
     */
    public void setPrecision(String precision) {
        this.precision = precision;
    }

    /**
     * @return Renvoie la date de mesure de position
     */
    public GregorianCalendar getDate() {
        return date;
    }

    /**
     * @param date Date de mesure de position
     */
    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    /**
     * @return Renvoie la latitude de la position
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude Latitude de la position
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return Renvoie la longitude de la position
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude Longitude de la position
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
