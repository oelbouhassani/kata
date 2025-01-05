import com.kata.dto.AccountDto;
import com.kata.dto.TransactionDto;
import com.kata.mapper.AccountMapper;
import com.kata.mapper.TransactionMapper;
import com.kata.model.Account;
import com.kata.model.Transaction;
import com.kata.enumuration.TransactionType;
import com.kata.repository.AccountRepository;
import com.kata.repository.TransactionRepository;
import com.kata.service.AccountManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AccountManagementServiceTest {

    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    @Spy
    private TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private AccountManagementService accountManagementService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Account createMockAccount(Long id, double balance) {
        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);
        return account;
    }

    private Transaction createMockTransaction(Long id, LocalDateTime date, double amount, double balance, TransactionType transactionType,
                                              Account account) {
        return new Transaction(id, date, amount, balance, transactionType, account);
    }


    @TestFactory
    public Stream<DynamicTest> dynamicTestsForDeposit() {
        return Stream.of(

                DynamicTest.dynamicTest("Should Deposit the amount", () -> {
                    Account account = createMockAccount(1L, 100.0);

                    when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
                    when(accountRepository.save(any(Account.class))).thenReturn(account);

                    AccountDto updatedAccount = accountManagementService.deposit(1L, 50.0);

                    assertEquals(150.0, updatedAccount.getBalance());
                    verify(transactionRepository, times(1)).save(any(Transaction.class));
                }),

                DynamicTest.dynamicTest("Should throw exception if Account Not Found", () -> {
                    when(accountRepository.findById(1L)).thenReturn(Optional.empty());

                    Exception exception = assertThrows(RuntimeException.class, () -> accountManagementService.deposit(1L, 50.0));

                    assertEquals("Account not found", exception.getMessage());
                })

        );
    }

    @TestFactory
    public Stream<DynamicTest> dynamicTestsForWithdraw() {
        return Stream.of(
                DynamicTest.dynamicTest("Should Withdraw the amount", () -> {
                    Account account = createMockAccount(1L, 100.0);

                    when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
                    when(accountRepository.save(any(Account.class))).thenReturn(account);

                    AccountDto updatedAccount = accountManagementService.withdraw(1L, 50.0);

                    assertEquals(50.0, updatedAccount.getBalance());
                    verify(transactionRepository, times(1)).save(any(Transaction.class));
                }),

                DynamicTest.dynamicTest("Should throw exception if Funds are Insufficient", () -> {
                    Account account = createMockAccount(1L, 50.0);

                    when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

                    Exception exception = assertThrows(RuntimeException.class, () -> accountManagementService.withdraw(1L, 100.0));

                    assertEquals("Insufficient funds", exception.getMessage());
                }),

                DynamicTest.dynamicTest("Should throw exception if Account Not Found", () -> {
                    when(accountRepository.findById(1L)).thenReturn(Optional.empty());

                    Exception exception = assertThrows(RuntimeException.class, () -> accountManagementService.withdraw(1L, 50.0));

                    assertEquals("Account not found", exception.getMessage());
                })


        );
    }

    @TestFactory
    public Stream<DynamicTest> dynamicTestsForGetAccountStatement() {
        return Stream.of(

                DynamicTest.dynamicTest("Should Get Account Statement", () -> {
                    Account account = createMockAccount(1L, 0.0);

                    List<Transaction> transactions = new ArrayList<>();
                    transactions.add(createMockTransaction(1L, LocalDateTime.parse("2025-01-05T15:30:00"), 100.0, 100.0, TransactionType.DEPOSIT,account));
                    transactions.add(createMockTransaction(2L, LocalDateTime.parse("2025-01-05T15:30:00"), -50.0, 50.0, TransactionType.WITHDRAW, account));

                    account.setTransactions(transactions);

                    when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

                    List<TransactionDto> result = accountManagementService.getAccountStatement(1L);

                    assertEquals(2, result.size());
                    assertEquals(100.0, result.get(0).getAmount());
                    assertEquals(TransactionType.DEPOSIT, result.get(0).getTransactionType());
                    assertEquals(-50.0, result.get(1).getAmount());
                    assertEquals(TransactionType.WITHDRAW, result.get(1).getTransactionType());

                }),

                DynamicTest.dynamicTest("Should throw exception if Account Not Found", () -> {
                    when(accountRepository.findById(1L)).thenReturn(Optional.empty());

                    Exception exception = assertThrows(RuntimeException.class, () -> accountManagementService.getAccountStatement(1L));

                    assertEquals("Account not found", exception.getMessage());
                })


        );
    }
}
