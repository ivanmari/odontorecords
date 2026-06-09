package odontograme.service;

import odontograme.bookkeeping.Charge;
import odontograme.bookkeeping.Installment;
import odontograme.bookkeeping.exceptions.InstallmentPresentException;
import odontograme.bookkeeping.exceptions.ChargeIdNotFoundException;
import odontograme.bookkeeping.exceptions.InstallmentIdNotFoundException;
import odontograme.repository.ChargeRepository;
import odontograme.repository.InstallmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by immari on 10/2/2016.
 */
@Component
public class AccountServiceImpl implements AccountService {

    private ChargeRepository chargeRepository;
    private InstallmentRepository installmentRepository;

    @Autowired
    public AccountServiceImpl(ChargeRepository chargeRepository, InstallmentRepository installmentRepository) {

        this.chargeRepository = chargeRepository;
        this.installmentRepository = installmentRepository;
    }

    @Override
    public int getBalance(String patientId){

        int totalCharges = 0;
        int totalInstallments = 0;

        Iterable<Charge> charges = chargeRepository.findByPatientId(patientId);
        Iterable<Installment> installments = installmentRepository.findByPatientId(patientId);

        for(Charge charge:charges){
            totalCharges += charge.getCharge();
        }

        for(Installment installment: installments){
            totalInstallments += installment.getAmount();
        }

        return totalInstallments - totalCharges;
    }

    @Override
    public void addCharge(Charge charge){
        chargeRepository.save(charge);
    }

    @Override
    public void removeCharge(Optional<Charge> charge) throws InstallmentPresentException {
        try {
            Iterable<Installment> installments = installmentRepository.findByChargeId(charge.orElseThrow(ChargeIdNotFoundException::new).getId().toString());
            if (installments.iterator().hasNext()) {
                throw new InstallmentPresentException();
            } else {
                chargeRepository.delete(charge.orElseThrow(ChargeIdNotFoundException::new));
            }
        }
        catch(ChargeIdNotFoundException e)
        {
            //Charge not found, do nothing
        }

    }

    @Override
    public void addInstallment(Installment i) {
        installmentRepository.save(i);
    }

    @Override
    public Optional<Charge> findChargeById(String id) {
        return this.chargeRepository.findById(id);
    }

    @Override
    public Optional<Installment> findInstallmentById(String id) {
        return this.installmentRepository.findById(id);
    }

    @Override
    public void removeInstallment(Optional<Installment> i) throws InstallmentIdNotFoundException
    {
        installmentRepository.delete(i.orElseThrow(InstallmentIdNotFoundException::new));
    }

    @Override
    public Page<Installment> getInstallments(String patientId, Pageable p) {
        return installmentRepository.findByPatientId(patientId, p);
    }

    @Override
    public Page<Charge> getCharges(String patientId, Pageable p)
    {
        return chargeRepository.findByPatientId(patientId, p);
    }
}
