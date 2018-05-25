package jp.te4a.spring.boot.myapp12;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AuthorValidator implements ConstraintValidator<AuthorValid, String> {
	String param;
	@Override
	public void initialize(AuthorValid bv) { param = bv.ok(); }
	@Override
	public boolean isValid(String in, ConstraintValidatorContext cxt) {
		if(in == null) {
			return false;
		}
		System.out.println("input " + !in.equals(param));
		return in.equals(param);
	}
}
