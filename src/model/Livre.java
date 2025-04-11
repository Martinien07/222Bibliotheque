package model;

public class Livre {
    private int id;
    private String titre;
    private String auteur;
    private boolean disponible;

    public Livre(int id, String titre, String auteur, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.disponible = disponible;
    }

    public Livre(String titre, String auteur, boolean disponible) {
        this(-1, titre, auteur, disponible);
    }

    public int getId() { return id; }
    public String getTitre() { return titre; }
    public String getAuteur() { return auteur; }
    public boolean isDisponible() { return disponible; }

    public void setId(int id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setAuteur(String auteur) { this.auteur = auteur; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    
    
    @Override
    public String toString() {
        return this.titre;
    }
}
