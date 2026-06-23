import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessioneTask {

    private Task taskSelezionata;
    private Player playerEsecutore;



    private List<String> azioniDaValidare;
    private int counterAzioniCompiute;

    private long timestampAttivazione;
    private Stato statoCompletamento;

    public SessioneTask(Task taskSelezionata, Player playerEsecutore) {
        this.taskSelezionata = taskSelezionata;
        this.playerEsecutore = playerEsecutore;
        this.statoCompletamento = Stato.ATTIVATA;
        this.timestampAttivazione = System.currentTimeMillis();
        this.azioniDaValidare = taskSelezionata.getCopiaAzioni();
        this.counterAzioniCompiute = 0;
    }

    public boolean annullaTask(){
        if(this.statoCompletamento == Stato.ATTIVATA){
            this.statoCompletamento = Stato.ANNULLATA;
            System.out.println("[SESSIONE] task annullata con successo. Nessuna penalitá");
            return true;
        }else{
            System.out.println("[ERROE] Impossibile annullare task.Stato attuale -> "+ this.statoCompletamento);
            return false;
        }
    }

    public void processaScansione(String stringaScansionata){
        if(this.statoCompletamento == Stato.COMPLETATA || this.statoCompletamento == Stato.ANNULLATA ||  this.statoCompletamento == Stato.FALLITA){
            System.out.println("[SISTEMA] Sessione chiusa ("+this.statoCompletamento+")");
            return;
        }
        long tempoTrascorso = System.currentTimeMillis() -  this.timestampAttivazione;
        if(tempoTrascorso >= taskSelezionata.getDurataMassimaMS() && !azioniDaValidare.isEmpty()){
            this.statoCompletamento = Stato.FALLITA;
            System.out.println("[TIME-OUT] Tempo scaduto. L'attivitá é stata FALLITA ");
            return;
        }
        String codicePulito = stringaScansionata.trim();

        if(azioniDaValidare.contains(codicePulito)){
            azioniDaValidare.remove(codicePulito);
            counterAzioniCompiute++;
            System.out.println("[VALIDAZIONE] Codice accettato! Progressi :"+ this.counterAzioniCompiute + "/" + taskSelezionata.getSogliaMin());
            // 1. Aggiorniamo prima lo stato se la task era appena iniziata
            if(this.statoCompletamento == Stato.ATTIVATA){
                this.statoCompletamento = Stato.IN_CORSO;
            }
            if(counterAzioniCompiute >= taskSelezionata.getSogliaMin()){
                this.statoCompletamento = Stato.COMPLETATA;
                System.out.println("[SUCCESSO] Hai raggiunto l'obiettivo! TASK COMPLETATA ");
            }
        }else {
            // Se non contiene il codice, scatta l'errore in modo isolato
            System.out.println("[ERRORE] Codice non valido oppure giá scansionato.");
        }

    }

    public Stato getStatoCompletamento() {return this.statoCompletamento;}
    public long getTimestampAttivazione() {return this.timestampAttivazione;}
    public Player getPlayerEsecutore() {return this.playerEsecutore;}
    public Task getTaskSelezionata() {return this.taskSelezionata;}


}
enum Stato{
    ATTIVATA,
    ANNULLATA,
    IN_CORSO,
    FALLITA,
    COMPLETATA
}
