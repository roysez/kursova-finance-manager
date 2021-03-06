package dev.roysez.financemanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.roysez.financemanager.FinanceManagerApplication;
import dev.roysez.financemanager.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(FinanceManagerApplication.class);
    /**
     * Файл для транзакцій
     */
    private final String fileDest = "C:\\Users\\roysez\\IdeaProjects\\finance-manager\\documents\\transactions.json";

    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public boolean save(Transaction entity) {
        Set<Transaction> list = findAll();
        if (list.isEmpty())
            list = new TreeSet<>();

        try {
            entity.setId(list.stream()
                    .reduce((first, second) -> second)
                    .orElseThrow(ArrayIndexOutOfBoundsException::new)
                    .getId() + 1);
        } catch (IndexOutOfBoundsException e) {
            entity.setId(0);
        }

        boolean result = list.add(entity);
        log.info("Transaction entity " + (result ? "saved" : "already exist") + " - {}", entity);

        if (result)
            save(list);

        return result;
    }


    @Override
    public Set<Transaction> save(Set<Transaction> entities) {

        try {

            mapper.writeValue(new File(fileDest), entities);
            return entities;

        } catch (Throwable e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    @Override
    public Transaction findOne(Integer id) {
        Set<Transaction> list = findAll();
        Transaction transaction = list.stream()
                .filter(tr -> tr.getId().equals(id))
                .reduce((a, b) -> {
                    throw new IllegalStateException("Multiple elements: " + a + ", " + b);
                })
                .orElseThrow(NoSuchElementException::new);

        System.out.println(transaction);
        return transaction;
    }

    @Override
    public boolean exists(Integer id) {
        Set<Transaction> list = findAll();
        Optional<Transaction> transaction = list.stream()
                .filter(tr -> tr.getId().equals(id)).reduce((a, b) -> null);

        return transaction.isPresent();
    }

    @Override
    public Set<Transaction> findAll() {
        try {
            Set<Transaction> list = mapper.readValue(new File(fileDest),
                    new com.fasterxml.jackson.core.type.TypeReference<TreeSet<Transaction>>() {
                    });

            return list == null ? Collections.emptySet() : list;
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//            return Collections.emptySet();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    @Override
    public void delete(Integer id) {
        Set<Transaction> list = findAll();
        Optional<Transaction> transaction = list.stream()
                .filter(tr -> tr.getId().equals(id)).reduce((a, b) -> null);

        if (transaction.isPresent())
            list.remove(transaction.get());

        save(list);

    }


}
