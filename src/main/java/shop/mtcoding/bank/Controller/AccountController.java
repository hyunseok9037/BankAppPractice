package shop.mtcoding.bank.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import shop.mtcoding.bank.dto.account.AccountDepositReqDto;
import shop.mtcoding.bank.dto.account.AccountDetailRespDto;
import shop.mtcoding.bank.dto.account.AccountSaveReqDto;
import shop.mtcoding.bank.dto.account.AccountTransferReqDto;
import shop.mtcoding.bank.dto.account.AccountWithdrawReqDto;
import shop.mtcoding.bank.dto.history.HistoryRespDto;
import shop.mtcoding.bank.handler.ex.CustomException;
import shop.mtcoding.bank.model.account.Account;
import shop.mtcoding.bank.model.account.AccountRepository;
import shop.mtcoding.bank.model.history.HistoryRepository;
import shop.mtcoding.bank.model.user.User;
import shop.mtcoding.bank.service.AccountService;

@Controller
public class AccountController {

    @Autowired
    private HttpSession session;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @PostMapping("/account/transfer")
    public String transfer(AccountTransferReqDto accountTransferReqDto) {
        // 1. 인증 필요
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("로그인을 먼저 해주세요", HttpStatus.UNAUTHORIZED);
        }

        // 2. 유효성 검사
        if (accountTransferReqDto.getWAccountNumber().equals(accountTransferReqDto.getDAccountNumber())) {
            throw new CustomException("출금계좌와 입금계좌가 동일할 수 없습니다", HttpStatus.BAD_REQUEST);
        }
        if (accountTransferReqDto.getAmount() == null) {
            throw new CustomException("amount를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountTransferReqDto.getAmount().longValue() <= 0) {
            throw new CustomException("이체액이 0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
        }
        if (accountTransferReqDto.getWAccountNumber() == null || accountTransferReqDto.getWAccountNumber().isEmpty()) {
            throw new CustomException("출금 계좌번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountTransferReqDto.getDAccountNumber() == null || accountTransferReqDto.getDAccountNumber().isEmpty()) {
            throw new CustomException("입금 계좌번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountTransferReqDto.getWAccountPassword() == null
                || accountTransferReqDto.getWAccountPassword().isEmpty()) {
            throw new CustomException("출금 계좌비밀번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        // 3. 서비스 호출
        int accountId = accountService.이체하기(accountTransferReqDto, principal.getId());

        return "redirect:/account/" + accountId;
    }

    @PostMapping("/account/deposit")
    public String deposit(AccountDepositReqDto accountDepositReqDto) {
        if (accountDepositReqDto.getAmount() == null) {
            throw new CustomException("amount를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountDepositReqDto.getAmount().longValue() <= 0) {
            throw new CustomException("입금액이 0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
        }
        if (accountDepositReqDto.getDAccountNumber() == null || accountDepositReqDto.getDAccountNumber().isEmpty()) {
            throw new CustomException("계좌번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        accountService.입금하기(accountDepositReqDto);

        return "redirect:/";
    }

    @PostMapping("/account/withdraw")
    public String withdraw(AccountWithdrawReqDto accountWithdrawReqDto) {
        if (accountWithdrawReqDto.getAmount() == null) {
            throw new CustomException("amount를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountWithdrawReqDto.getAmount().longValue() <= 0) {
            throw new CustomException("출금액이 0원 이하일 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        if (accountWithdrawReqDto.getWAccountNumber() == null || accountWithdrawReqDto.getWAccountNumber().isEmpty()) {
            throw new CustomException("계좌번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountWithdrawReqDto.getWAccountPassword() == null
                || accountWithdrawReqDto.getWAccountPassword().isEmpty()) {
            throw new CustomException("계좌비밀번호를 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        int accountId = accountService.계좌출금(accountWithdrawReqDto);

        return "redirect:/account/1";
    }

    @PostMapping("/account")
    public String save(AccountSaveReqDto accountSaveReqDto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("로그인을 먼저 해주세요", HttpStatus.UNAUTHORIZED);
        }
        if (accountSaveReqDto.getNumber() == null || accountSaveReqDto.getNumber().isEmpty()) {
            throw new CustomException("Number을 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (accountSaveReqDto.getPassword() == null || accountSaveReqDto.getPassword().isEmpty()) {
            throw new CustomException("password를 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        accountService.계좌생성(accountSaveReqDto, principal.getId());

        return "redirect:/";

    }

    @GetMapping({ "/", "/account" })
    public String main(Model model) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }

        List<Account> accountList = accountRepository.findByUserId(principal.getId());
        model.addAttribute("accountList", accountList);

        return "account/main";
    }

    @GetMapping("/account/{id}")
    public String detail(@PathVariable int id,
            @RequestParam(name = "gubun", defaultValue = "all") String gubun, Model model) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }
        // 2.히스토리레포지토리 호출(메서드 3개, or 마이바티스 동적쿼리로 처리하는 방법)
        AccountDetailRespDto aDto = accountRepository.findByIdWithUser(id);
        if (aDto.getUserId() != principal.getId()) {
            throw new CustomException("해당 계좌를 볼 수 있는 권한이 없습니다",
                    HttpStatus.FORBIDDEN);
        }
        List<HistoryRespDto> hDtoList = historyRepository.findByGubun(gubun, id);
        model.addAttribute("aDto", aDto);
        model.addAttribute("hDtoList", hDtoList);
        return "account/detail";

    }

    @GetMapping("/account/saveForm")
    public String saveForm() {
        return "account/saveForm";
    }

    @GetMapping("/account/withdrawForm")
    public String withdrawForm() {
        return "account/withdrawForm";
    }

    @GetMapping("/account/depositForm")
    public String depositForm() {
        return "account/depositForm";
    }

    @GetMapping("/account/transferForm")
    public String transferForm() {
        return "account/transferForm";
    }

}
