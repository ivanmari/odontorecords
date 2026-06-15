package odontograme.bookkeeping;

import odontograme.bookkeeping.exceptions.InstallmentIdNotFoundException;
import odontograme.bookkeeping.exceptions.InstallmentPresentException;
import odontograme.patientrecords.Practice;
import odontograme.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class AccountServiceMock implements AccountService {
    @Override
    public int getBalance(String patientId) {
        return 0;
    }

    @Override
    public int getPracticeCost(Practice practice) {
        return 0;
    }

    @Override
    public void addCharge(Charge charge) {

    }

    @Override
    public void removeCharge(Optional<Charge> charge) throws InstallmentPresentException {

    }

    @Override
    public void addInstallment(Installment i) {

    }

    @Override
    public Optional<Charge> findChargeById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Installment> findInstallmentById(String id) {
        return Optional.empty();
    }

    @Override
    public void removeInstallment(Optional<Installment> i) throws InstallmentIdNotFoundException {

    }

    @Override
    public Page<Installment> getInstallments(String patientId, Pageable p) {
        return null;
    }

    @Override
    public Page<Charge> getCharges(String patientId, Pageable p) {
        return null;
    }
}
