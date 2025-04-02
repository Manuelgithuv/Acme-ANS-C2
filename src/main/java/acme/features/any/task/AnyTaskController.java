
package acme.features.any.task;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.task.Task;

@GuiController
public class AnyTaskController extends AbstractGuiController<Any, Task> {

	//Internal state --------------------------------------------------------------

	@Autowired
	private AnyTaskListService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}

}
