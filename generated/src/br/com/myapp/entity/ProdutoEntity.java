package br.com.myapp.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "produtos")
public class ProdutoEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;
        @Column(name = "nome", columnDefinition = "VARCHAR(100)")
        private String nome;
        @Column(name = "descricao")
        private String descricao;
        @Column(name = "preco")
        private Double preco;
}
