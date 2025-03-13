
package acme.entities.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidPromotionCode;
import acme.constraints.ValidShortText;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidPromotionCode
@Entity
public class Service extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				pictureLink;

	@Mandatory
	@Valid
	@Automapped
	private Double				averageDwellTime;

	@Optional
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$")
	@Column(unique = true)
	private String				promotionCode;

	@Optional
	@ValidMoney
	@Automapped
	private Money				discount;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				airport;
}
