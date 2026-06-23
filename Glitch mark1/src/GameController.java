import java.util.Scanner;

public class GameController {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PlayerMemory memoria = new PlayerMemory();
        VetrinaTask vetrina = new VetrinaTask();
        Player playerAttivo = null;

        System.out.println("=== GLITCH MARK 1 : PRE-ALPHA ===");

        // --- OBIETTIVO 1: Autenticazione ---
        while (playerAttivo == null) {
            System.out.println("\n1. Login");
            System.out.println("2. Registrazione");
            System.out.println("3. Esci");
            System.out.print("Scegli un'opzione: ");
            String scelta = scanner.nextLine();

            if (scelta.equals("3")) return;

            System.out.print("Username: ");
            String user = scanner.nextLine();
            System.out.print("Password: ");
            String pass = scanner.nextLine();

            if (scelta.equals("1")) {
                playerAttivo = memoria.login(user, pass);
            } else if (scelta.equals("2")) {
                playerAttivo = memoria.registra(user, pass);
            }
        }

        // --- OBIETTIVO 2 & 3: Loop Applicativo ---
        SessioneTask sessioneAttuale = null;
        boolean inEsecuzione = true;

        while (inEsecuzione) {
            System.out.println("\n--- MENU PRINCIPALE di " + playerAttivo.getUsername() + " ---");
            System.out.println("1. Esplora Vetrina e Attiva Task");
            System.out.println("2. Simula scansione fisica (Gioca task attiva)");
            System.out.println("3. Profilo e Statistiche");
            System.out.println("4. Esci");
            System.out.print("> ");
            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    if (sessioneAttuale != null && (sessioneAttuale.getStatoCompletamento() == Stato.ATTIVATA || sessioneAttuale.getStatoCompletamento() == Stato.IN_CORSO)) {
                        System.out.println("[SISTEMA] Hai già una task in corso. Terminala prima di accettarne altre.");
                        break;
                    }

                    vetrina.mostraVetrina();
                    System.out.print("Inserisci il numero della task da attivare (0 per annullare): ");
                    int idTask = Integer.parseInt(scanner.nextLine());

                    if (idTask > 0) {
                        Task taskScelta = vetrina.getTask(idTask - 1);
                        if (taskScelta != null) {
                            sessioneAttuale = new SessioneTask(taskScelta, playerAttivo);
                            System.out.println("[SISTEMA] Sessione creata! Stato: " + sessioneAttuale.getStatoCompletamento());
                        }
                    }
                    break;

                case "2":
                    if (sessioneAttuale == null || sessioneAttuale.getStatoCompletamento() == Stato.COMPLETATA || sessioneAttuale.getStatoCompletamento() == Stato.FALLITA) {
                        System.out.println("[SISTEMA] Nessuna task attiva al momento.");
                        break;
                    }

                    System.out.print("Scannerizza il codice (Inserisci la stringa letta dal sensore): ");
                    String scansione = scanner.nextLine();

                    // Passiamo il dato fisico alla sessione digitale
                    sessioneAttuale.processaScansione(scansione);

                    // LOGICA DI CONTROLLO POST-SCANSIONE:
                    // Se la scansione appena fatta ha portato lo stato a COMPLETATA, applichiamo i reward
                    if (sessioneAttuale.getStatoCompletamento() == Stato.COMPLETATA) {
                        // 1. Il registro archivia la task
                        playerAttivo.getRegistroAttivita().aggiungiTaskCompletata(sessioneAttuale);

                        // 2. Eroghiamo exp e token
                        Task t = sessioneAttuale.getTaskSelezionata();
                        playerAttivo.aggiungiExpPlayer(t.getRicompensaBaseExp());
                        playerAttivo.aggiungiTokenPlayer(t.getRicompensaBaseToken());
                        System.out.println("[RICOMPENSE] Erogati " + t.getRicompensaBaseExp() + " EXP e " + t.getRicompensaBaseToken() + " Token!");
                    } else if (sessioneAttuale.getStatoCompletamento() == Stato.FALLITA) {
                        playerAttivo.getRegistroAttivita().aggiungiTaskFalliteAnnullate(sessioneAttuale);
                    }
                    break;

                case "3":
                    System.out.println("\n--- PROFILO PLAYER ---");
                    System.out.println("Username: " + playerAttivo.getUsername());
                    System.out.println("Livello: " + playerAttivo.getLevel());
                    System.out.println("EXP: " + playerAttivo.getExp());
                    System.out.println("Token: " + playerAttivo.getToken());
                    System.out.println("Task Completate Totali: " + playerAttivo.getRegistroAttivita().numeroTaskCompletate());
                    break;

                case "4":
                    inEsecuzione = false;
                    break;

                default:
                    System.out.println("Opzione non valida.");
            }
        }
        scanner.close();
    }
}