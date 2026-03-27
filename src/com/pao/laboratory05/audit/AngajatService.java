package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {

    private Angajat[] angajati;
    private AuditEntry[] auditLog;

    private AngajatService() {
        angajati = new Angajat[0];
        auditLog = new AuditEntry[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    private void logAction(String action, String target) {
        AuditEntry entry = new AuditEntry(
                action,
                target,
                LocalDateTime.now().toString()
        );

        AuditEntry[] tmp = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, tmp, 0, auditLog.length);
        tmp[tmp.length - 1] = entry;
        auditLog = tmp;
    }

    public void addAngajat(Angajat a) {
        Angajat[] tmp = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, tmp, 0, angajati.length);
        tmp[tmp.length - 1] = a;
        angajati = tmp;

        System.out.println("Angajat adăugat: " + a.getNume());

        logAction("ADD", a.getNume());
    }

    public void listBySalary() {
        System.out.println("--- Angajați după salariu (descrescător) ---");

        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);

        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        logAction("FIND_BY_DEPT", numeDept);

        boolean gasit = false;
        System.out.println("--- Angajați din " + numeDept + " ---");

        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }

    public void printAuditLog() {
        System.out.println("--- Audit Log ---");

        for (AuditEntry e : auditLog) {
            System.out.println(e);
        }
    }
}