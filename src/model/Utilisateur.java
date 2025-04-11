package model;

public class Utilisateur {
    private int id;
    private String nomUtilisateur;
    private String motDePasse;
    private String role;

    public Utilisateur(int id, String nomUtilisateur, String motDePasse, String role) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
    }
    
    public Utilisateur(String nomUtilisateur, String motDePasse, String role) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    
    
    

    public int getId() { return id; }
    public String getNomUtilisateur() { return nomUtilisateur; }
    public String getMotDePasse() { return motDePasse; }
    public String getRole() { return role; }

    public void setId(int id) { this.id = id; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setRole(String role) { this.role = role; }
}