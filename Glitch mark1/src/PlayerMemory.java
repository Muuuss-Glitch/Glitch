import java.util.ArrayList;

public class PlayerMemory {
    private ArrayList<Player> playerRegistrati;

    public PlayerMemory() {
        this.playerRegistrati = new ArrayList<>();
    }

    public Player registra(String username, String password) {
        for (Player p : playerRegistrati) {
            if (p.getUsername().equals(username)) {
                System.out.println("[ERRORE] Username già in uso.");
                return null;
            }
        }
        Player nuovoPlayer = new Player(username, password);
        playerRegistrati.add(nuovoPlayer);
        System.out.println("[SUCCESSO] Registrazione completata!");
        return nuovoPlayer;
    }

    public Player login(String username, String password) {
        for (Player p : playerRegistrati) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                System.out.println("[SUCCESSO] Login effettuato! Bentornato " + username);
                return p;
            }
        }
        System.out.println("[ERRORE] Credenziali errate.");
        return null;
    }
}