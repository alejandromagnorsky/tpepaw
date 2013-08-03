const DESCRIPTION_MAX_CHARS = 255;
const DESCRIPTION_HINT = "Caracteres restantes: ";

window.onload = initLengthValidator;

function lengthValidator() {
	field = document.getElementById("form-description");
	countdown = document.getElementById("form-description-countdown");
	maxLength = DESCRIPTION_MAX_CHARS;
	if (field.value.length > maxLength)
		field.value = field.value.substring(0, maxLength);
	else {
		countdown.value = maxLength - field.value.length;
		countdown.innerHTML = DESCRIPTION_HINT + (maxLength - field.value.length);
	}
}

function initLengthValidator(){
	if (!document.getElementsByTagName)
		return;

	textArea = document.getElementById("form-description");
	descriptionCountdown = document.getElementById("form-description-countdown");
	descriptionCountdown.value = DESCRIPTION_MAX_CHARS - textArea.value.length;
	descriptionCountdown.innerHTML = DESCRIPTION_HINT + (DESCRIPTION_MAX_CHARS - textArea.value.length);
	textArea.onkeydown = lengthValidator;
	textArea.onkeyup = lengthValidator;
}