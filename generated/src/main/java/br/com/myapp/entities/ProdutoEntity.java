package br.com.myapp.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@Builder(access = AccessLevel.PUBLIC, toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class ProdutoEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private java.lang.Long id;
        @Column(name = "nome", columnDefinition = "VARCHAR(100)")
        private java.lang.String nome;
        @Column(name = "descricao")
        private java.lang.String descricao;
        @Column(name = "preco")
        private java.lang.Double preco;
}
