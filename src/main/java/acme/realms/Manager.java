
package acme.realms;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Manager extends AbstractRole {

	private static final long serialVersionUID = 1L;

}
