package br.com.myapp.rules;

import org.springframework.stereotype.Component;

@Component
public class DespacharProdutoRuleAdapter implements DespacharProdutoRulePort {

	@Override
	public void accept(final br.com.myapp.domains.ProdutoDomain input) {
		System.out.println(input);

	}
}
