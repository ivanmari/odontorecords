package odontograme.bookkeeping;

import odontograme.repository.ChargeRepository;
import odontograme.repository.ChargeRepositoryMock;
import odontograme.repository.InstallmentRepository;
import odontograme.repository.InstallmentRepositoryMock;
import odontograme.service.AccountService;
import odontograme.service.AccountServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by immari on 10/2/2016.
 */
public class AccountServiceTests {

    ChargeRepository chargeRepository = new ChargeRepositoryMock();
    InstallmentRepository installmentRepository = new InstallmentRepositoryMock();
    AccountService account = new AccountServiceImpl(chargeRepository, installmentRepository);

    @BeforeAll
    static void initAll() {


    }

    @BeforeEach
    void init() {
        chargeRepository.deleteAll();
        installmentRepository.deleteAll();
    }


    @Test
    public void onCreationAccountBalanceIsZero(){
        assertThat(account.getBalance("1982")).isZero();
    }

    @Test
    public void chargeAddedGeneratesDebt(){

        Charge charge = new Charge(new ObjectId(), new Date(), 100, "extraccion");

        account.addCharge(charge);

        assertThat(account.getBalance("1982")).isEqualTo(-100);
    }

    @Test
    public void installmentCancelsDebt(){
        Charge charge = new Charge(new ObjectId(), new Date(), 100, "extraccion");

        account.addCharge(charge);

        Installment installment = new Installment(charge.getId(), new Date(), 100);

        account.addInstallment(installment);

        assertThat(account.getBalance("1982")).isZero();
    }

    @Test
    public void installmentCancelsPartialDebt(){
        Charge charge = new Charge(new ObjectId(), new Date(), 100, "extraccion");

        account.addCharge(charge);

        Installment installment = new Installment(charge.getId(), new Date(), 60);

        account.addInstallment(installment);

        assertThat(account.getBalance("1982")).isEqualTo(-40);
    }

    @Test
    public void twoInstallmentsCancelsAllDebt(){
        Charge charge = new Charge(new ObjectId(), new Date(), 100, "extraccion");

        account.addCharge(charge);

        Installment installment = new Installment(charge.getId(), new Date(), 60);

        Installment installment2 = new Installment(charge.getId(), new Date(), 40);

        account.addInstallment(installment);

        account.addInstallment(installment2);

        assertThat(account.getBalance("1982")).isZero();
    }

    @Test
    public void twoChargesOneInstallment(){
        Charge charge = new Charge(new ObjectId(), new Date(), 100, "extraccion");

        account.addCharge(charge);

        Installment installment = new Installment(charge.getId(), new Date(), 60);

        account.addInstallment(installment);

        Charge charge2 = new Charge(new ObjectId(), new Date(), 50, "arreglo");

        account.addCharge(charge2);

        assertThat(account.getBalance("1982")).isEqualTo(-90);
    }
}
