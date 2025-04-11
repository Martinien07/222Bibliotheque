package model;

import java.util.Date;

public class Emprunt {
    private int id;
    private int livreId;
    private String emprunteur;
    private Date dateEmprunt;
    private Date dateRetour;

    public Emprunt(int id, int livreId, String emprunteur, Date dateEmprunt, Date dateRetour) {
        this.id = id;
        this.livreId = livreId;
        this.emprunteur = emprunteur;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
    }

    public Emprunt(int livreId, String emprunteur, Date dateEmprunt, Date dateRetour) {
        this(-1, livreId, emprunteur, dateEmprunt, dateRetour);
    }

    public int getId() { return id; }
    public int getLivreId() { return livreId; }
    public String getEmprunteur() { return emprunteur; }
    public Date getDateEmprunt() { return dateEmprunt; }
    public Date getDateRetour() { return dateRetour; }

    public void setId(int id) { this.id = id; }
    public void setLivreId(int livreId) { this.livreId = livreId; }
    public void setEmprunteur(String emprunteur) { this.emprunteur = emprunteur; }
    public void setDateEmprunt(Date dateEmprunt) { this.dateEmprunt = dateEmprunt; }
    public void setDateRetour(Date dateRetour) { this.dateRetour = dateRetour; }
}
