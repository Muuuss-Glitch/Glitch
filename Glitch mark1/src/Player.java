public class Player {

    private String username;
    private String password;
    private RegistroAttivita registroAttivita;

    private int level;
    private int exp;
    private int token;
    private double moltiplicatoreReward;

    public Player(String username, String password){
        this.username = username;
        this.password = password;
        this.level = 1;
        this.exp = 0;
        this.token = 0;
        this.moltiplicatoreReward = 1.0;
        this.registroAttivita = new RegistroAttivita();
    }

    public void aggiungiExpPlayer(int quantita){
        if(quantita <=0){
            System.out.println("[ERRORE] Problemi nell'assegnazione dell'exp");
            return;
        }
        this.exp += quantita;
        checkLvlUp();
    }

    public void checkLvlUp(){
        int sogliaLvlUp = this.level *100;
        if(this.exp >= sogliaLvlUp){
            this.level++;
            this.exp -= sogliaLvlUp;
        }
    }

    public void aggiungiTokenPlayer(int quantita){
        if(quantita <=0){return;}     //---- de gestire in futuro con le exception
        this.token += quantita;
    }
               //----- tutti i metodi che riguardano lo operazioni sul player, sono contenute in PlayerService ----


    //metodi getter
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public int getToken() { return token; }
    public double getMoltiplicatoreReward() { return moltiplicatoreReward; }
    public RegistroAttivita getRegistroAttivita() { return registroAttivita; }
}
