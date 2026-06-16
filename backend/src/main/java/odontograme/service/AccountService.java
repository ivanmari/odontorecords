package odontograme.service;

import odontograme.bookkeeping.Charge;
import odontograme.bookkeeping.Installment;
import odontograme.bookkeeping.exceptions.InstallmentIdNotFoundException;
import odontograme.bookkeeping.exceptions.InstallmentPresentException;
import odontograme.patientrecords.Practice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccountService {
    int getBalance(String patientId);

    int getPracticeCost(Practice practice);

    void addCharge(Charge charge);

    void removeCharge(Optional<Charge> charge) throws InstallmentPresentException;

    void addInstallment(Installment i);

    Optional<Charge> findChargeById(String id);

    Optional<Installment> findInstallmentById(String id);

    void removeInstallment(Optional<Installment> i) throws InstallmentIdNotFoundException;

    Page<Installment> getInstallments(String patientId, Pageable p);

    Page<Charge> getCharges(String patientId, Pageable p);
}
