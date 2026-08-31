package org.example.jpaentityrelationships.service;

import org.example.jpaentityrelationships.dto.TransactionRequest;
import org.example.jpaentityrelationships.dto.TransactionResponse;
import org.example.jpaentityrelationships.entity.Order;

import org.example.jpaentityrelationships.entity.Transaction;
import org.example.jpaentityrelationships.exceptions.ResourceNotFoundException;
import org.example.jpaentityrelationships.repository.OrderRepository;
import org.example.jpaentityrelationships.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              OrderRepository orderRepository) {
        this.transactionRepository = transactionRepository;
        this.orderRepository = orderRepository;
    }

    public TransactionResponse createTransaction(TransactionRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found: " + request.getOrderId()
                        ));

        Transaction transaction = new Transaction();

        transaction.setAmount(request.getAmount());
        transaction.setStatus(request.getStatus());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setOrder(order);

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return mapToResponse(savedTransaction);
    }

    public TransactionResponse getTransactionById(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction not found: " + id
                        ));

        return mapToResponse(transaction);
    }

    public List<TransactionResponse> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TransactionResponse updateTransaction(
            Long id,
            TransactionRequest request) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction not found: " + id
                        ));

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found: " + request.getOrderId()
                        ));

        transaction.setAmount(request.getAmount());
        transaction.setStatus(request.getStatus());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setOrder(order);

        Transaction updatedTransaction =
                transactionRepository.save(transaction);

        return mapToResponse(updatedTransaction);
    }

    public void deleteTransaction(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction not found: " + id
                        ));

        transactionRepository.delete(transaction);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setStatus(transaction.getStatus());
        response.setPaymentMethod(transaction.getPaymentMethod());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setOrderId(transaction.getOrder().getId());

        return response;
    }
}