package acme.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.system_configuration.SystemConfiguration;

@Service
public class MoneyService {
	
	@Autowired
	private SystemCurrencyRepository repository;


	public boolean checkContains(final String currency) {
		final SystemConfiguration configuration = this.repository.getSystemConfiguration().get(0);
		final String acceptedCurrenciesStr = configuration.getAcceptedCurrencies();
		final List<String> currencies = new ArrayList<>();
		for (final String currencyStr : acceptedCurrenciesStr.split(","))
			currencies.add(currencyStr);
		return currencies.contains(currency);
	}

}
