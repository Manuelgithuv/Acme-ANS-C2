
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import acme.entities.maintenanceRecord.MaintenanceRecord;

@Component
public class MaintenanceRecordValidator implements ConstraintValidator<ValidMaintenanceRecord, MaintenanceRecord> {

	@Autowired
	public MaintenanceRecordValidator() {
	}

	@Override
	public boolean isValid(final MaintenanceRecord record, final ConstraintValidatorContext context) {
		if (record == null)
			return false;

		Date moment = record.getMoment();
		Date inspectionDueDate = record.getInspectionDueDate();

		if (moment == null || inspectionDueDate == null) {
			context.buildConstraintViolationWithTemplate("Both moment and inspection due date must be provided").addConstraintViolation();
			return false;
		}

		if (!inspectionDueDate.after(moment)) {
			context.buildConstraintViolationWithTemplate("Inspection due date must be after the maintenance record moment").addPropertyNode("inspectionDueDate").addConstraintViolation();
			return false;
		}

		return true;
	}

}
