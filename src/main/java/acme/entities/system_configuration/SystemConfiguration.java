package acme.entities.system_configuration;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SystemConfiguration extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;
	@Mandatory
	@ValidString(pattern="[A-Z]{3}")
	@Column(unique=true)
	protected String			systemCurrency;
	
	@Mandatory
	@ValidString(pattern= "([A-Z]{3},)*[A-Z]{3}")
	@Column(unique=true)
	protected String			acceptedCurrencies;
}