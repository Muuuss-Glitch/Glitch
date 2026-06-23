import java.util.ArrayList;
import java.util.List;

public class RegistroAttivita {


    private List<SessioneTask> taskCompletate;
    private List<SessioneTask> taskFalliteAnnullate;
    private SessioneTask ultimaTaskEseguita;


    public RegistroAttivita () {

        this.taskCompletate = new ArrayList<>();
        this.taskFalliteAnnullate = new ArrayList<>();
        this.ultimaTaskEseguita = null;
    }

    public int numeroTaskCompletate () {
        return taskCompletate.size();
    }
    public int numeroTaskFalliteAnnullate () {
        return taskFalliteAnnullate.size();
    }

    public boolean aggiungiTaskCompletata(SessioneTask task){
        int nTask = numeroTaskCompletate();

        if(task.getStatoCompletamento() != Stato.COMPLETATA){
            System.out.println("[ERRORE] La task non é stata completata del tutto.");
            return false;
        }else {
            taskCompletate.add(task);
            this.ultimaTaskEseguita = task;
            if(nTask < numeroTaskCompletate()){
                System.out.println("[SISTEMA] La task é stata aggiunta al registro!");
            }
            return true;
        }
    }
    public boolean aggiungiTaskFalliteAnnullate(SessioneTask task){
        int nTask = numeroTaskFalliteAnnullate();
        if(task.getStatoCompletamento().equals(Stato.FALLITA) || task.getStatoCompletamento().equals(Stato.ANNULLATA)){
            taskFalliteAnnullate.add(task);
            if(nTask < numeroTaskFalliteAnnullate()){
                System.out.println("[SUCCESSO] task fallita o annullata presente nell'apposita lista (X 30 giorni)");
                return true;
            }
        }
        return false;
    }

    // metodi getter
    public SessioneTask getUltimaTaskEseguita() { return this.ultimaTaskEseguita; }
    public List<SessioneTask> getTaskCompletate() { return this.taskCompletate; }
    public List<SessioneTask>  getTaskFalliteAnnullate() { return this.taskFalliteAnnullate; }

}
