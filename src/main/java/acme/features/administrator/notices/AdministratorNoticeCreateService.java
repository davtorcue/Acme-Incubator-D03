
package acme.features.administrator.notices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.notices.Notice;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractCreateService;

@Service
public class AdministratorNoticeCreateService implements AbstractCreateService<Administrator, Notice> {

	@Autowired
	AdministratorNoticeRepository repository;


	@Override
	public boolean authorise(final Request<Notice> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Notice> request, final Notice entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "creationDateTime");

	}

	@Override
	public void unbind(final Request<Notice> request, final Notice entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "header", "title", "body", "deadline", "optionalLinks");

	}

	@Override
	public Notice instantiate(final Request<Notice> request) {
		// TODO Auto-generated method stub
		Notice result;
		result = new Notice();
		request.getModel().setAttribute("checkbox", false);

		return result;
	}

	@Override
	public void validate(final Request<Notice> request, final Notice entity, final Errors errors) {
		// TODO Auto-generated method stub
		assert request != null;
		assert entity != null;
		assert errors != null;

		boolean isAccepted;
		Date date;

		isAccepted = request.getModel().getBoolean("checkbox");
		errors.state(request, isAccepted, "checkbox", "administrator.notice.error.confirmCreate");

		if (!errors.hasErrors("deadline")) {
			date = new Date();
			errors.state(request, entity.getDeadline().after(date), "deadline", "administrator.notice.error.incorrectDeadline");
		}
		if (!errors.hasErrors("optionalLinks")) {
			boolean isURLs;
			URLValidator urlValidator = new URLValidator();
			List<String> optLinks = new ArrayList<>();
			optLinks = Arrays.asList(entity.getOptionalLinks().split(","));
			isURLs = optLinks.stream().allMatch(x -> urlValidator.isValid(x.trim(), null));
			errors.state(request, isURLs, "optionalLinks", "administrator.notice.error.NotUrls");
		}

	}

	@Override
	public void create(final Request<Notice> request, final Notice entity) {
		// TODO Auto-generated method stub
		Date creationMoment;

		creationMoment = new Date();

		entity.setCreationDateTime(creationMoment);
		this.repository.save(entity);
	}
}
