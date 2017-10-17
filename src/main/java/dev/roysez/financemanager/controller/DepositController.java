package dev.roysez.financemanager.controller;

import dev.roysez.financemanager.model.Category;
import dev.roysez.financemanager.model.Deposit;
import dev.roysez.financemanager.model.Transaction;
import dev.roysez.financemanager.model.User;
import dev.roysez.financemanager.service.DepositService;
import dev.roysez.financemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;

@Controller
@RequestMapping(value = "/deposits")
public class DepositController {

    @Autowired
    DepositService depositService;

    @Autowired
    UserService userService;

    @RequestMapping(value = {"","/"},method = RequestMethod.GET)
    public String depositsPage(Model model, @ModelAttribute("error") String error){

        if(!error.isEmpty()){
            model.addAttribute("error",error);
        }

        try {

            Set<Deposit> deposits = depositService.findAll();
            model.addAttribute("deposit",new Deposit());
            model.addAttribute("deposits",deposits);
        } catch (Exception e) {

        }

        return "deposit";
    }

    @RequestMapping(value = {"/",""},method = RequestMethod.POST)
    public String postDeposit(Deposit deposit,
                              RedirectAttributes redir){



        try {

            if(deposit.getPercentages()< 0 || deposit.getPercentages()>100 || deposit.getTerm()<1)
                throw new IllegalArgumentException("Percentage should be [0:100] ( or term should be > 1 ) ");

            User user = userService.getUser();

            Long userBalance = user.getBalance();


            if(userBalance -deposit.getSum() < 0)
                throw new IllegalStateException("You don't have enough money to take this deposit");

            user.setBalance(userBalance-deposit.getSum());



            depositService.save(deposit);

            userService.saveUser(user);

            System.out.println("Deposit created ["+ deposit.getSum()+"]");

        } catch (IOException e) {
            redir.addFlashAttribute("error","Error while reading user information");
        } catch (IllegalStateException  | IllegalArgumentException e) {
            redir.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/deposits";
    }
}
