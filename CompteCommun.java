package Threads_Bank_shared_account;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cherk
 */
public class CompteCommun {

    public static void main(String[] args) {

        Comptes compte = new Comptes();

        Thread depotThread = new Thread(new DepotThread(compte), "le mari");
        Thread retraitThread = new Thread(new RetraitThread(compte), "le mari");
        Thread depotThread2 = new Thread(new DepotThread(compte), "la femme");
        Thread retraitThread2 = new Thread(new RetraitThread(compte), "la femme");

        depotThread.start();
        retraitThread.start();

        depotThread2.start();
        retraitThread2.start();
    }
}

class Comptes {

    private int solde = 0;
    private int turn = 1;

    public synchronized void deposer(int montant) {

        solde += montant;
        System.out.println(Thread.currentThread().getName() + " Dépôt de " + montant
                + " effectué. Nouveau solde : " + solde);
        turn = 2;
        notifyAll();

    }

    public synchronized boolean retirer(int montant) {

        if (solde >= montant) {
            solde -= montant;
            System.out.println(Thread.currentThread().getName() + " Retrait de " + montant
                    + " effectué. Nouveau solde : " + solde);
        } else {
            System.out.println("\u001B[32m" + Thread.currentThread().getName()
                    + " Solde insuffisant pour un retrait de " + montant);

        }
        turn = 1;
        notifyAll();
        return solde >= 0;

    }

}

class DepotThread implements Runnable {

    private Comptes compte;

    public DepotThread(Comptes compte) {
        this.compte = compte;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep((int) (Math.random() * 2000));
            } catch (InterruptedException ex) {
                Logger.getLogger(CompteCommun.class.getName()).log(Level.SEVERE, null, ex);
            }
            int montant = random.nextInt(100) + 1;
            compte.deposer(montant);
        }
    }
}

class RetraitThread implements Runnable {

    private Comptes compte;

    public RetraitThread(Comptes compte) {
        this.compte = compte;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep((int) (Math.random() * 2000));
            } catch (InterruptedException ex) {
                Logger.getLogger(CompteCommun.class.getName()).log(Level.SEVERE, null, ex);
            }
            int montant = random.nextInt(100) + 1;
            compte.retirer(montant);
        }
    }
}
