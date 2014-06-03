package py.una.pol.karaku.dao.filter;

import javax.annotation.PostConstruct;
import org.aspectj.lang.JoinPoint;
import org.hibernate.Filter;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import py.una.pol.karaku.util.ELParser;

/**
 * AOP para la creación activación automática de filtros hibernate
 * 
 * @author Arturo Volpe
 * @see {@link EntityFilter}, {@link ELParser}
 * @since 1.0
 * @version 1.1
 */
@Service
public class SIGHEntityFilterHandler {

	@Autowired
	private SessionFactory sessionFactory;

	private ExpressionParser parser;

	@PostConstruct
	public void init() {

		parser = new SpelExpressionParser();
	}

	public void doBefore(final JoinPoint joinPoint,
			final EntityFilter annotation) {

		Filter f = sessionFactory.getCurrentSession().enableFilter(
				annotation.filter());
		String expression;
		Integer paramNumber;
		Expression exp;
		Object subject;
		Object[] params = joinPoint.getArgs();
		for (FilterAttribute attribute : annotation.params()) {
			if (ELParser.isMethodParamExpression(attribute.path())) {
				paramNumber = ELParser.getParamNumber(attribute.path());
				expression = ELParser.removeParamNumber(attribute.path());
				exp = parser.parseExpression(expression);
				subject = params[paramNumber];
			} else {
				exp = parser.parseExpression(attribute.path());
				subject = joinPoint.getTarget();
			}
			f.setParameter(attribute.name(), exp.getValue(subject));
		}
	}

	public void doAfter(final JoinPoint joinPoint, final EntityFilter annotation) {

		sessionFactory.getCurrentSession().disableFilter(annotation.filter());
	}
}
