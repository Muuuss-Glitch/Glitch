import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class Task {

    private String id;
    private String titolo;
    private String descrizione;
    private String idCreatore; //-----serve per tenere traccia di chi ha messo a disposizione la task e in quale profilo deve comparire

    private int ricompensaBaseExp;
    private int ricompensaBaseToken;
    private TipoTask tipo; //--------- grazie al tipo possiamo effettuare tutti i calcoli sulle ricompense base

    private int sogliaMin;
    private List<String> azioniRichieste;
    private long durataMassimaMs;
    //in futuro agg. posizione richiesta per il completamento (oppure la si prende dal luogo inserito nella scheda del profilo del partner)

    public Task(String titolo, String descrizione, String idCreatore, TipoTask tipo,int sogliaMin, int minutiDisp) {
        this.id = UUID.randomUUID().toString();
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.idCreatore = idCreatore;
        this.tipo = tipo;
        this.sogliaMin = sogliaMin;
        inizializzaRicompenseBase();
        this.azioniRichieste = new ArrayList<>();
        this.durataMassimaMs = (long) minutiDisp *60 *1000;
    }

    public void inizializzaRicompenseBase(){
        switch (tipo) {
            case QR_CODE:
                this.ricompensaBaseExp = 100;
                this.ricompensaBaseToken = 50;
                break;
            case FOLLOW:
                this.ricompensaBaseExp = 20;
                this.ricompensaBaseToken = 10;
                break;
            case CHECK_IN:
                this.ricompensaBaseExp = 200;
                this.ricompensaBaseToken = 100;
                break;
            case REFERRAL:
                this.ricompensaBaseExp = 300;
                this.ricompensaBaseToken = 150;
                break;
            case ACQUISTO:
                this.ricompensaBaseExp = 400;
                this.ricompensaBaseToken = 200;
                break;
            default:
                this.ricompensaBaseExp = 10;
                this.ricompensaBaseToken = 5;
                break;
        }
    }



    //aggiungere azionivalide tutte insieme

    public void aggiungiAzioniValide(List<String> azioniRichieste) {
        this.azioniRichieste.clear();
        if (azioniRichieste != null && !azioniRichieste.isEmpty()) { // Se NON è vuota
            this.azioniRichieste.addAll(azioniRichieste); // Usa addAll, non = per non condividere i reference
        }
    }


    //aggiungere azioni valide una alla volta
    public void aggiungiAzioniValide(String azioneRichiesta){
        this.azioniRichieste.add(azioneRichiesta);
    }


    //metodi setter
    public void modificaTitDescr (String titolo, String descrizione){
        this.titolo = titolo;
        this.descrizione = descrizione;
    }

    //metodi getter
    public String getId() { return this.id; }
    public String getTitolo() { return this.titolo; }
    public String getDescrizione() { return this.descrizione; }
    public String getIdCreatore() { return this.idCreatore; }
    public int getRicompensaBaseExp() { return this.ricompensaBaseExp; }
    public int getRicompensaBaseToken() { return this.ricompensaBaseToken; }
    public TipoTask getTipo() { return this.tipo; }
    public long getDurataMassimaMS() { return this.durataMassimaMs; }
    public List<String> getCopiaAzioni() { return new ArrayList<>(this.azioniRichieste); }
    public int getSogliaMin() { return this.sogliaMin; }
    public int getTotAzioni(){ return this.azioniRichieste.size(); }
}
enum TipoTask{
    QR_CODE,
    FOLLOW,
    CHECK_IN,
    NFC,
    ACQUISTO,
    REFERRAL

}