
package acme.features.authenticated.technician;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Technician;

@GuiService
public class AuthenticatedTechnicianUpdateService extends AbstractGuiService<Authenticated, Technician> {

	@Autowired
	private AuthenticatedTechnicianRepository authenticatedTechnicianRepository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Technician technician;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		technician = this.authenticatedTechnicianRepository.findOneTechnicianByUserAccountId(userAccountId);

		super.getBuffer().addData(technician);

	}

	@Override
	public void bind(final Technician technician) {
		assert technician != null;
		super.bindObject(technician, "licenseNumber", "yearsOfExperience", "phoneNumber", "specialisation", "annualHealthTestPassed", "certifications");

	}

	@Override
	public void validate(final Technician technician) {
		assert technician != null;

		Optional<Technician> existingTechnician = this.authenticatedTechnicianRepository.findByLicenseNumber(technician.getLicenseNumber());

		if (!existingTechnician.isEmpty())
			super.state(false, "identifierNumber", "technician.authenticated.invalidIdentifierNumber");
	}

	@Override
	public void perform(final Technician technician) {
		assert technician != null;
		this.authenticatedTechnicianRepository.save(technician);

	}

	@Override
	public void unbind(final Technician technician) {
		Dataset dataset;

		dataset = super.unbindObject(technician, "licenseNumber", "yearsOfExperience", "phoneNumber", "specialisation", "annualHealthTestPassed", "certifications");

		super.getResponse().addData(dataset);

	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
