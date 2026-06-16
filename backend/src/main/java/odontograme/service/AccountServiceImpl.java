package odontograme.service;

import odontograme.bookkeeping.Charge;
import odontograme.bookkeeping.Installment;
import odontograme.bookkeeping.exceptions.InstallmentPresentException;
import odontograme.bookkeeping.exceptions.ChargeIdNotFoundException;
import odontograme.bookkeeping.exceptions.InstallmentIdNotFoundException;
import odontograme.patientrecords.Practice;
import odontograme.repository.ChargeRepository;
import odontograme.repository.InstallmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Updated for Spring Boot 3.x
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final ChargeRepository chargeRepository;
    private final InstallmentRepository installmentRepository;

    @Autowired
    public AccountServiceImpl(ChargeRepository chargeRepository, InstallmentRepository installmentRepository) {
        this.chargeRepository = chargeRepository;
        this.installmentRepository = installmentRepository;
    }

    @Override
    public int getPracticeCost(Practice practice) {
        return practice.getSuppliesCost();
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
            Charge c = charge.orElseThrow(ChargeIdNotFoundException::new);
            Iterable<Installment> installments = installmentRepository.findByChargeId(c.getId().toString());
            if (installments.iterator().hasNext()) {
                throw new InstallmentPresentException();
            } else {
                chargeRepository.delete(c);
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
