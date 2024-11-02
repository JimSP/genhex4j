package br.com.myapp.rules;

import org.springframework.stereotype.Component;

@Component
public class CalcularDescontoProdutoRuleAdapter implements CalcularDescontoProdutoRulePort {

	@Override
	public java.lang.Double apply(final br.com.myapp.domains.ProdutoDomain input) {

		if (input.getPreco() > 100) {
			return input.getPreco() * 0.9;
		} else {
			return input.getPreco();
		}
	}
}
