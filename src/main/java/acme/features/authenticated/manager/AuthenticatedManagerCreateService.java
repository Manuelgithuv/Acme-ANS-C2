package acme.features.authenticated.manager;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;

import acme.realms.Manager;

@GuiService
public class AuthenticatedManagerCreateService extends AbstractGuiService<Authenticated, Manager> {
	
	@Autowired
	private AuthenticatedManagerRepository authenticatedManagerRepository;
	
	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Manager.class);

		super.getResponse().setAuthorised(status);
	}
	
	@Override
	public void load() {
		
		Manager manager;
		int userAccountId;
		UserAccount userAccount;
		
		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.authenticatedManagerRepository.findOneUserAccountById(userAccountId);
		
		manager = new Manager();
		manager.setUserAccount(userAccount);
		
		super.getBuffer().addData(manager);
		
		
	}
	
	@Override
	public void bind(final Manager manager) {
		super.bindObject(manager, "identifierNumber","yearsOfExperience","dateOfBirth","picture");
		
	}
	
	@Override
	public void validate(final Manager manager) {
		assert manager!=null;
	}
	
	@Override
	public void perform(final Manager manager) {
		assert manager!=null;
		this.authenticatedManagerRepository.save(manager);
		
	}
	
	@Override
	public void unbind(final Manager manager) {
		Dataset dataset;

		dataset = super.unbindObject(manager, "identifierNumber","yearsOfExperience","dateOfBirth","picture");

		super.getResponse().addData(dataset);
		
	}
	
	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
	

}
