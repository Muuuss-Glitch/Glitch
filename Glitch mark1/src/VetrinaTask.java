import java.util.ArrayList;
import java.util.List;

public class VetrinaTask {
    private ArrayList<Task> taskDisponibili;

    public VetrinaTask() {
        taskDisponibili = new ArrayList<>();
        popolaVetrinaMock();
    }

    private void popolaVetrinaMock() {
        // Creazione di una task fittizia per i test
        Task t1 = new Task("Caccia ai QR nel Centro Commerciale", "Trova 3 QR code per vincere", "Admin", TipoTask.QR_CODE, 3, 30);

        // CORREZIONE AL BUG: Aggiungo le azioni qui e sistemo la logica della soglia
        List<String> codiciSegreti = new ArrayList<>();
        codiciSegreti.add("QR-123");
        codiciSegreti.add("QR-456");
        codiciSegreti.add("QR-789");
        t1.aggiungiAzioniValide(codiciSegreti);

        taskDisponibili.add(t1);
    }

    public void mostraVetrina() {
        System.out.println("\n--- VETRINA TASK DISPONIBILI ---");
        for (int i = 0; i < taskDisponibili.size(); i++) {
            Task t = taskDisponibili.get(i);
            System.out.println((i + 1) + ". " + t.getTitolo() + " | Reward Base: " + t.getRicompensaBaseExp() + " EXP");
        }
    }

    public Task getTask(int index) {
        if (index >= 0 && index < taskDisponibili.size()) {
            return taskDisponibili.get(index);
        }
        return null;
    }
}
