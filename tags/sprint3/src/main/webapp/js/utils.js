const DESCRIPTION_MAX_CHARS = 255;
const DESCRIPTION_HINT = "Caracteres restantes: ";

window.onload = initLengthValidator;

function lengthValidator() {
	maxLength = DESCRIPTION_MAX_CHARS;
	field = document.getElementById("form-description");
	countdown = document.getElementById("form-description-countdown");
	if (field.value.length > maxLength)
		field.value = field.value.substring(0, maxLength);
	else {
		countdown.value = maxLength - field.value.length;
		countdown.innerHTML = DESCRIPTION_HINT + (maxLength - field.value.length);
	}
}

function resolutionFormSubmit() {
	document.getElementById('resolution-form').submit();
}

function dependsOnFormSubmit() {
	document.getElementById('dependsOn-form').submit();
}

function necessaryForFormSubmit() {
	document.getElementById('necessaryFor-form').submit();
}

function relatedToFormSubmit() {
	document.getElementById('relatedTo-form').submit();
}

function duplicatedWithFormSubmit() {
	document.getElementById('duplicatedWith-form').submit();
}

function initLengthValidator(){
	if (!document.getElementsByTagName)
		return;
	
	textArea = document.getElementById("form-description")
	if (textArea != null){
		descriptionCountdown = document.getElementById("form-description-countdown");
		descriptionCountdown.value = DESCRIPTION_MAX_CHARS - textArea.value.length;
		descriptionCountdown.innerHTML = DESCRIPTION_HINT + (DESCRIPTION_MAX_CHARS - textArea.value.length);
		textArea.onkeydown = lengthValidator;
		textArea.onkeyup = lengthValidator;
	}
	
	initSelectBehaviour('resolution-select', resolutionFormSubmit);
	/*initSelectBehaviour('dependsOn-select', dependsOnFormSubmit);
	initSelectBehaviour('necessaryFor-select', necessaryForFormSubmit);
	initSelectBehaviour('relatedTo-select', relatedToFormSubmit);
	initSelectBehaviour('duplicatedWith-select', duplicatedWithFormSubmit);*/
	
}

function initSelectBehaviour(id, submitFunction){
	var select = document.getElementById(id);
	if (select != null){
		if (select.addEventListener)
			select.addEventListener('change', submitFunction, false);
		else
			select.onchange = submitFunction;
	}
}
