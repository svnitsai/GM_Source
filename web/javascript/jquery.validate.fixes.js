$.validator.methods.number = function (value, element) {
	var res = value.replace(",", ""); 
    return this.optional(element) || /^-?(?:\d+|\d{1,3}(?:[\s\.,]\d{3})+)(?:[\.,]\d+)?$/.test(res); 
}