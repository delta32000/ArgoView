/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package argoview;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * Objet permettant de gérer les différents animaux
 * @author Florent Fayollas
 */
public class Animal {
    
    /**
     * Constructeur de l'objet Animal
     * @param nom         Nom de l'animal
     * @param nomFichier  Nom de fichier à ouvrir pour lire les positions
     */
    public Animal ( String nom, String nomFichier ) {
        this.nom = nom;
        this.setNomFichier(nomFichier);
        this.positions = new ArrayList();
    }
    
    /*
        Déclaration des variables
    */
    private String nom = new String();
    private String nomFichier = new String();
    private String url = new String();
    private ArrayList<DonneeArgos> positions;
    private final char separateur = ' ';
    private final JTable tableau = new JTable();

    /**
     * Permet de lire les données contenus dans "Positions/%nomFichier.txt"
     */
    public void lireDonnees() {
        int cpt = 0;    // Variable permettant de ne pas enregistrer les deux premières lignes du fichier de position
        try {
            // On ouvre le fichier
            InputStream flux = new FileInputStream(nomFichier);
            InputStreamReader fluxEntree = new InputStreamReader(flux);
            BufferedReader buffer = new BufferedReader(fluxEntree);
            String ligne;
            
            // Tant que l'on peut lire une ligne
            while ( (ligne = buffer.readLine()) != null ) {
                int i = 0;
                
                // Numéro de balise
                String numBalise = "";
                while (ligne.charAt(i) != this.separateur) {
                    numBalise += ligne.charAt(i);
                    i++;
                }
                // On passe aux données suivantes
                i = donneeSuivante(ligne, i);
                
                // Précision
                String precision = "";
                while (ligne.charAt(i) != ' ') {
                    precision += ligne.charAt(i);
                    i++;
                }
                // On passe aux données suivantes
                i = donneeSuivante(ligne, i);
                
                // Date
                String date = "";
                while (ligne.charAt(i) != ' ') {
                    date += ligne.charAt(i);
                    i++;
                }
                // On passe aux données suivantes
                i = donneeSuivante(ligne, i);
                
                // Heure
                String heure = "";
                while (ligne.charAt(i) != ' ') {
                    heure += ligne.charAt(i);
                    i++;
                }
                // On passe aux données suivantes
                i = donneeSuivante(ligne, i);
                
                // Latitude
                String latitude = "";
                while (ligne.charAt(i) != ' ') {
                    latitude += ligne.charAt(i);
                    i++;
                }
                // On passe aux données suivantes
                i = donneeSuivante(ligne, i);
                
                // Longitude
                String longitude = "";
                while (ligne.charAt(i) != ' ') {
                    longitude += ligne.charAt(i);
                    i++;
                }
                
                // On traite les données
                if (cpt != 0 && cpt != 1)
                    this.positions.add(
                            new DonneeArgos(
                                    numBalise, precision,
                                    date, heure,
                                    latitude, longitude));
                cpt++;
                
            }
            // On ferme le fichier
            buffer.close();
            fluxEntree.close();
            flux.close();
        }
        // S'il y a eu une erreur, on l'affiche
        catch (IOException e) {
            JOptionPane.showMessageDialog(tableau,
                    "Impossible d'ouvrir le fichier : " + nomFichier + "\n\tErreur d'entée-sortie :\n" + e.toString(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            System.out.println( e.toString() );
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(tableau,
                    "Impossible de charger le fichier : " + nomFichier + "\n\tLe fichier n'est pas conforme :\n" + e.toString(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            System.out.println( e.toString() );
        }
    }
    
    /**
     * Permet de trouver l'id du premier caractère des données suivantes
     * @param chaine    Chaîne à parcourir
     * @param i         ID de début de parcours
     * @return ID du premier caractère des données suivantes
     */
    private int donneeSuivante( String chaine, int i ) {
        while (chaine.charAt(i) == this.separateur)
            i++;
        return i;
    }

    /**
     * Permet de télécharger le fichier de positions
     */
    public void telechargerFichier() {
        try {
            // On crée les variables pour télécharger le fichier
            URL adresse = new URL("http://" + url);
            URLConnection connexion = adresse.openConnection();
            int taille = connexion.getContentLength();

            // On crée un flux d’entrée pour lire le fichier
            InputStream brut = connexion.getInputStream();
            InputStream entree = new BufferedInputStream(brut);

            // On crée un tableau pour enregistrer les octets bruts
            byte[] donnees = new byte[taille];
            
            // Pour l’instant aucun octet n’a encore été lu
            int octetsLus = 0;

            // Octets de déplacement, et octets déjà lus.
            int deplacement = 0;
            float dejaLu = 0;

            // On boucle pour lire tous les octets 1 à 1
            while(deplacement < taille)
            {
                octetsLus = entree.read(donnees, deplacement, donnees.length - deplacement);

                // Petit calcul: mise à jour du nombre total d’octets lus par ajout au nombre d’octets lus au cours des précédents passages au nombre d’octets lus pendant ce passage
                dejaLu = dejaLu + octetsLus;

                // Si on est à la fin du fichier, on sort de la boucle
                if  (octetsLus == -1)
                    break;

                // se cadrer à un endroit précis du fichier pour lire les octets suivants, c’est le déplacement
                deplacement += octetsLus;
            }
            // On ferme le fichier ouvert en ligne
            entree.close();

            // Enregistrement
            FileOutputStream fichierSortie = new FileOutputStream(nomFichier);
            fichierSortie.write(donnees);

            // On ferme les flux de sortie
            fichierSortie.flush();
            fichierSortie.close();
        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(tableau,
                    "URL de téléchargement mal formée !",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(tableau,
                    "Impossible de télécharger le fichier : " + nomFichier + "\n\tErreur d'entée-sortie :\n" + ex.toString(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * @return Renvoie le nom de l'animal
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom Nom de l'animal
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return Renvoie le nom du fichier de position à lire
     */
    public String getNomFichier() {
        return nomFichier;
    }
    
    /**
     * @return Renvoie l'URL de téléchargement des données
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param nomFichier Nom du fichier
     */
    public void setNomFichier(String nomFichier) {
        this.nomFichier = "Positions/" + nomFichier + ".txt";
        this.url = "argonautica.jason.oceanobs.com/documents/argonautica/2013-2014/" + nomFichier + ".txt";
    }

    /**
     * @return Renvoie un tableau dynamique des positions successives
     */
    public ArrayList<DonneeArgos> getPositions() {
        return positions;
    }

    /**
     * @return Renvoie le tableau contenant les positions
     */
    public JTable getTableau() {
        return tableau;
    }
}
