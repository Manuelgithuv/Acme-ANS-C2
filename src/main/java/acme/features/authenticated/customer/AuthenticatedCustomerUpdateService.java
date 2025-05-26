
package acme.features.authenticated.customer;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Customer;

@GuiService
public class AuthenticatedCustomerUpdateService extends AbstractGuiService<Authenticated, Customer> {

	@Autowired
	private AuthenticatedCustomerRepository authenticatedCustomerRepository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Customer customer;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		customer = this.authenticatedCustomerRepository.findOneCustomerByUserAccountId(userAccountId);

		super.getBuffer().addData(customer);

	}

	@Override
	public void bind(final Customer customer) {
		assert customer != null;
		super.bindObject(customer, "identifier", "phoneNumber", "physicalAddress", "city", "country", "earnedPoints");

	}

	@Override
	public void validate(final Customer customer) {
		assert customer != null;

		if (customer.getIdentifier() != null && !customer.getIdentifier().isEmpty()) {
			Optional<Customer> existingCustomer = this.authenticatedCustomerRepository.findByIdentifier(customer.getIdentifier());

			if (existingCustomer.isPresent() && existingCustomer.get().getId() != customer.getId())
				super.state(false, "identifier", "Customer.authenticated.invalidIdentifierNumber");
		} else
			super.state(false, "identifier", "Customer.authenticated.identifierRequired");
	}

	@Override
	public void perform(final Customer customer) {
		assert customer != null;
		this.authenticatedCustomerRepository.save(customer);

	}

	@Override
	public void unbind(final Customer customer) {
		Dataset dataset;

		dataset = super.unbindObject(customer, "identifier", "phoneNumber", "physicalAddress", "city", "country", "earnedPoints");

		super.getResponse().addData(dataset);

	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
