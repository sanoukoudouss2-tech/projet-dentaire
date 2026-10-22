package ma.cabinet.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe abstraite commune à tous les utilisateurs de l'application.
 * Un Utilisateur n'est jamais instancié directement : on crée un
 * Administrateur ou un Patient (abstraction + héritage).
 */
public abstract class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    private String login;
    private String pwd;

    protected Utilisateur(String login, String pwd) {
        setLogin(login);
        setPwd(pwd);
    }

    /** Vérifie si le couple login / mot de passe correspond à cet utilisateur. */
    public boolean authentifier(String login, String pwd) {
        return this.login.equals(login) && this.pwd.equals(pwd);
    }

    /** Rôle de l'utilisateur, utilisé après la connexion pour ouvrir la bonne fenêtre. */
    public abstract Role getRole();

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Le login est obligatoire");
        }
        this.login = login.trim();
    }

    /** Pas de getPwd() public : le mot de passe ne sort jamais de l'objet (encapsulation). */
    public void setPwd(String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }
        this.pwd = pwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur)) return false;
        return login.equals(((Utilisateur) o).login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
