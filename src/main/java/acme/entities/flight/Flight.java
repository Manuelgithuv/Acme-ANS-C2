
package acme.entities.flight;

import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidShortText;
import acme.features.manager.leg.LegRepository;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
  name = "flight",
  indexes = {
    @Index(name = "idx_flight_published", columnList = "published")
  }
)
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
	@ValidString(max=255)
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

	    // Paso 1: Obtener el valor mínimo de scheduledDeparture
	    Date minScheduledDeparture = repository.findFirstScheduledDeparture(this.getId()).orElse(null);

	    if (minScheduledDeparture == null) {
	        return null;
	    }

	    // Paso 2: Obtener la ciudad correspondiente al valor mínimo
	    return repository.findCityByFlightIdAndScheduledDeparture(this.getId(), minScheduledDeparture).orElse(null);
	}

	@Transient
	public String getDestinationCity() {
	    LegRepository repository;

	    repository = SpringHelper.getBean(LegRepository.class);

	    // Paso 1: Obtener el valor máximo de scheduledArrival
	    Date maxScheduledArrival = repository.findLastScheduledArrival(this.getId()).orElse(null);

	    if (maxScheduledArrival == null) {
	        return null;
	    }

	    // Paso 2: Obtener la ciudad correspondiente al valor máximo
	    return repository.findCityByFlightIdAndScheduledArrival(this.getId(), maxScheduledArrival).orElse(null);
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
