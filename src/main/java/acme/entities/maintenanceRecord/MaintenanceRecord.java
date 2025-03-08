
package acme.entities.maintenanceRecord;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.datatypes.MaintenanceRecordStatus;
import acme.entities.aircraft.Aircraft;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MaintenanceRecord extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date					moment;

	@Mandatory
	@Valid
	@Automapped
	private MaintenanceRecordStatus	status;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date					inspectionDueDate;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money					estimatedCost;

	@Optional
	@Size(max = 255)
	@Automapped
	private String					notes;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne
	private Aircraft				aircraft;

}
