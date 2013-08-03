package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class CaptchaPanel extends Panel {

	private String imagePassword = randomString(6, 8);

	public CaptchaPanel(String id) {
		super(id);
		CaptchaImageResource captchaImageResource = new CaptchaImageResource(
				imagePassword);
		add(new Image("captchaImage", captchaImageResource));
		add(new RequiredTextField<String>("captchaPassword"));
	}

	private String randomString(int min, int max) {
		int length = randomInt(6, 8);
		char word[] = new char[length];
		for (int i = 0; i < length; i++)
			word[i] = (char) randomInt('a', 'z');
		return new String(word);
	}

	private int randomInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	public String getPassword() {
		return imagePassword;
	}
}
