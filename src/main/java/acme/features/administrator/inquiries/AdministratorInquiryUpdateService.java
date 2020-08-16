
package acme.features.administrator.inquiries;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.inquiries.Inquiry;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.datatypes.Money;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractUpdateService;

@Service
public class AdministratorInquiryUpdateService implements AbstractUpdateService<Administrator, Inquiry> {

	@Autowired
	private AdministratorInquiryRepository repository;


	@Override
	public boolean authorise(final Request<Inquiry> request) {
		assert request != null;
		return true;
	}

	@Override
	public void bind(final Request<Inquiry> request, final Inquiry entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Inquiry> request, final Inquiry entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "creation", "deadline", "description", "minMoney", "maxMoney", "email");

	}

	@Override
	public Inquiry findOne(final Request<Inquiry> request) {

		assert request != null;

		Inquiry result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneById(id);

		return result;
	}

	@Override
	public void validate(final Request<Inquiry> request, final Inquiry entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Calendar calendar;
		Date minimumDeadline;

		if (!errors.hasErrors("deadline")) {
			calendar = new GregorianCalendar();
			calendar.add(Calendar.DAY_OF_MONTH, 7);
			minimumDeadline = calendar.getTime();
			errors.state(request, entity.getDeadline().after(minimumDeadline), "deadline", "administrator.inquiry.deadline.error");
		}

		if (!errors.hasErrors("minMoney")) {
			Money min = entity.getMinMoney();
			errors.state(request, min.getCurrency().contentEquals("EUR") || min.getCurrency().contentEquals("€"), "minMoney", "administrator.inquiry.minMoney.currency.error");
		}

		if (!errors.hasErrors("maxMoney")) {
			Money max = entity.getMaxMoney();
			errors.state(request, max.getCurrency().contentEquals("EUR") || max.getCurrency().contentEquals("€"), "maxMoney", "administrator.inquiry.maxMoney.currency.error");
		}

		if (!errors.hasErrors("minMoney") && !errors.hasErrors("maxMoney")) {
			Money min = entity.getMinMoney();
			Money max = entity.getMaxMoney();
			errors.state(request, max.getAmount() >= min.getAmount(), "minMoney", "administrator.inquiry.moneyRange.error");
		}

	}

	@Override
	public void update(final Request<Inquiry> request, final Inquiry entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

}
