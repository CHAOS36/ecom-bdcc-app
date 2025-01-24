package org.sid.orderservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder @ToString
//ca marche avec sql vu que order un mot reserve en sql
@Table(name = "ORDERS")
public class Order {
    @Id
    private String id;
    private LocalDate date;
    //on va stocker ca sous forme de string dans la base de donnée
    @Enumerated(EnumType.STRING)
    private OrderState state;
    //si on a une relation biderictionnelle on doit mettre mappedBy
    @OneToMany(mappedBy = "order")
    private List<ProductItem> productItems;
}
