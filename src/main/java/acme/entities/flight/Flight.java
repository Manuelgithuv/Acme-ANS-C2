
package acme.entities.flight;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.features.manager.leg.LegRepository;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Atributos directos
	// -------------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				indication;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Mandatory
	@Automapped
	private boolean				published;

	@Optional
	@ValidLongText
	@Automapped
	private String				description;

	// -------------------------------------------------------------------

	// Derivados


	@Transient
	public Date getScheduledDeparture() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);

		return repository.findFirstScheduledDeparture(this.getId()).orElse(null);

	}

	@Transient
	public Date getScheduledArrival() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);

		return repository.findLastScheduledArrival(this.getId()).orElse(null);

	}

	@Transient
	public String getOriginCity() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);

		return repository.findOriginCity(this.getId()).orElse(null);
	}

	@Transient
	public String getDestinationCity() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);

		return repository.findDestinationCity(this.getId()).orElse(null);
	}

	@Transient
	public Long getLayovers() {
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		
		Long numberOfLayovers = repository.countLayoversByFlight(this.getId());

		return numberOfLayovers==-1? 0: numberOfLayovers;

	}
	// ----------------------------------------

	// Relaciones 


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager manager;

	// -------------------------------------------------------------------

}
