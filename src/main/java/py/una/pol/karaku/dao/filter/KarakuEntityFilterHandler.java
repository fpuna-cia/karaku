/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
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
public class KarakuEntityFilterHandler {

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
