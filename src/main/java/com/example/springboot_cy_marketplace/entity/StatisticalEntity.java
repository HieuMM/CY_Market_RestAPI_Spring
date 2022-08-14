package com.example.springboot_cy_marketplace.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "statistical")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int day = 0;
    private int month = 0;
    private int year = 0;
    @Column(name = "total_view")
    private int totalView = 0;
    @Column(name = "total_new_user")
    private int totalNewUser = 0;
    @Column(name = "total_new_order")
    private int totalNewOrder = 0;
    @Column(name = "total_order_done")
    private int totalOrderDone = 0;
    @Column(name = "total_order_processing")
    private int totalOrderProcessing = 0;
    @Column(name = "total_order_cancel")
    private int totalOrderCancel = 0;
    @Column(name = "total_paypal_payment")
    private int totalPayPalPayment = 0;
    @Column(name = "total_vnpay_payment")
    private int totalVnPayPayment = 0;
    @Column(name = "total_cod_payment")
    private int totalCodPayment = 0;
    @Column(name = "total_question_send")
    private int totalQuestionSend = 0;
    @Column(name = "total_question_answered")
    private int totalQuestionAnswered = 0;
    @Column(name = "total_question_delete")
    private int totalQuestionDelete = 0;
}
