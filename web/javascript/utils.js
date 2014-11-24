// ********************************************************************
// %name:  NIDSUtils.js %
// %version:  37 %
// %date_modified:  Tue Aug 22 20:35:42 2006 %
// Copyright (c) 2005 Novell, Inc. All Rights Reserved.
// THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL PROPRIETARY
// AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS TO THIS WORK IS
// RESTRICTED TO (I) NOVELL, INC. EMPLOYEES WHO HAVE A NEED TO KNOW HOW
// TO PERFORM TASKS WITHIN THE SCOPE OF THEIR ASSIGNMENTS AND (II)
// ENTITIES OTHER THAN NOVELL, INC. WHO HAVE ENTERED INTO APPROPRIATE
// LICENSE AGREEMENTS. NO PART OF THIS WORK MAY BE USED, PRACTICED,
// PERFORMED COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED, ABRIDGED,
// CONDENSED, EXPANDED, COLLECTED, COMPILED, LINKED, RECAST, TRANSFORMED
// OR ADAPTED WITHOUT THE PRIOR WRITTEN CONSENT OF NOVELL, INC. ANY USE
// OR EXPLOITATION OF THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE
// PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
// ********************************************************************


var BrowserCharset = "utf-8";
var COLOR_SEL      = "#f1f1c4";
var COLOR_NORMAL   = "#ffffff";
var TEXT_NORMAL    = "#000000";
var TEXT_HILITE    = "#000000";
var spinnerbar    = null;

function nidsIsValidNumberRange(num, min, max) {
    if (isNaN(parseInt(num, 10))) {
        return false;
    }
    if (num >= min && num <= max) {
        return true;
    }
    return false;
}


function nidsIsValidIPAddress(address) {
    var frontIindex = 0;
    var backIndex = address.indexOf(".");
    if (backIndex == -1) {
        return false;
    }
    if (!nidsIsValidNumberRange(address.substring(frontIindex, backIndex), 0, 255)) {
        return false;
    }
    frontIindex = backIndex + 1;
    backIndex = address.indexOf(".", frontIindex);
    if (backIndex == -1) {
        return false;
    }
    if (!nidsIsValidNumberRange(address.substring(frontIindex, backIndex), 0, 255)) {
        return false;
    }
    frontIindex = backIndex + 1;
    backIndex = address.indexOf(".", frontIindex);
    if (backIndex == -1) {
        return false;
    }
    if (!nidsIsValidNumberRange(address.substring(frontIindex, backIndex), 0, 255)) {
        return false;
    }
    frontIindex = backIndex + 1;
    backIndex = address.indexOf(".", frontIindex);
    if (backIndex != -1) {
        return false;
    }
    if (!nidsIsValidNumberRange(address.substring(frontIindex), 0, 255)) {
        return false;
    }
    return true;
}

function nidsisValidNumber( number )
{
	if ( "0" == number ) return true;
	var vs_RegEx = /^[1-9][0-9]*$/
	return (vs_RegEx.exec(number) != null);
	
}

function nidsIsValidPort(portNum) {
    var re = /^[1-9][0-9]*$/;
    if (re.exec(portNum) == null) {
        return false;
    }
    if (!nidsIsValidNumberRange(portNum, 0, 65535)) {
        return false;
    }
    return true;
}


function nidsGetElementByName(elementName) {
    var elementRef = null;
    if (document.getElementById) {
        elementRef = document.getElementById(elementName);
    }
    if (null == elementRef && document.all) {
        elementRef = document.all[elementName];
    }
    if (null == elementRef && document.layers) {
        elementRef = document.layers[elementName];
    }
    return elementRef;
}


function nidsGetRadioElementByName(elementName, index) {
    var elementRef = eval("document.forms[0].elements['" + elementName + "'][" + index + "]");
    return elementRef;
}


function nidsGetSelectedRadioButton(elementName) {
    groupLen = eval("document.forms[0].elements['" + elementName + "'].length");
    for (i = 0; i < groupLen; i++) {
        var element = eval("document.forms[0].elements[\"" + elementName + "\"][" + i + "]");
        if (element != null && element.checked) {
            return element;
        }
    }
    return null;
}


function nidsEnableRadioButton(elementName, enabled) {
    groupLen = eval("document.forms[0].elements['" + elementName + "'].length");
    for (i = 0; i < groupLen; i++) {
        var element = eval("document.forms[0].elements[\"" + elementName + "\"][" + i + "]");
        if (element != null) {
            element.disabled = enabled ? false : true;
        }
    }
}


function nidsGetElementValue(elementName) {
    var element = nidsGetElementByName(elementName);
    if (element == null) {
        alert("Element " + elementName + " cannot be found");
        return "";
    }
    if (element.type == "radio") {
        return nidsGetSelectedRadioButton(elementName).value;
    }
    return element.value;
}


function nidsSetElementValue(elementName, elementValue) {
    var element = nidsGetElementByName(elementName);
    if (element == null) {
        alert("Element " + elementName + " cannot be found.  Value '" + elementValue + "' not set");
        return;
    }
    element.value = elementValue;
}


function trimLeft(s) {
    if (s == null || s.length == 0) {
        return "";
    }
    var i = 0;
    while (i < s.length && s.charAt(i) == " ") {
        i++;
    }
    if (i == s.length) {
        return s;
    }
    s = s.substr(i);
    return s;
}


function trimRight(s) {
    if (s == null || s.length == 0) {
        return "";
    }
    var i = s.length - 1;
    while (i > -1 && s.charAt(i) == " ") {
        i--;
    }
    s = i == -1 ? "" : s.substr(0, i + 1);
    return s;
}


function trim(s) {
    if (s == null || s.length == 0) {
        return "";
    }
    s = trimLeft(s);
    s = trimRight(s);
    return s;
}


function nidsIsElementValueEmpty(elementName) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        var s = element.value;
        s = trim(s);
        if (element.type == "file") {
        } else {
            element.value = s;
        }
        return s == "";
    }
    return false;
}


function nidsClearElementValue(elementName) {
    nidsSetElementValue(elementName, "");
}


function nidsIsElementChecked(elementName) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        return element.checked;
    }
    return false;
}


function nidsSetElementChecked(elementName, value) {
    var element = nidsGetElementByName(elementName);
    if ( null != element ) {
        element.checked = value;
    }
    else {
    	alert( elementName + " not found" );
    }
}


function nidsSetElementFocus(elementName) {

    var element = nidsGetElementByName(elementName);

    if ( null != element ) {

        element.focus();
    }
}


function nidsSetPageFocus() {

    try
    {
       var element = nidsGetElementByName("activePage");
       if (null != element) {
           element.focus();
       } else {
           element = nidsGetElementByName("activeChapter");
           if (null != element) {
               element.focus();
           }
       }
   }
   catch( e )
   {
   }
}


function nidsIsOptionSelected(elementName, optionNumber) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        return element.options[optionNumber].selected;
    }
    return false;
}


function nidsIsOptionSelectedByValue(elementName, targetValue) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        for (var i = 0; i < element.options.length; i++) {
            if (element.options[i].value == targetValue) {
                return element.options[i].selected;
            }
        }
    }
    return false;
}


function nidsEnableAllOptions(elementName, enable) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        for (var i = 0; i < element.options.length; i++) {
            element.options[i].disabled = !enable;
        }
        emulate(element);
    }
}

function nidsSetAllSelections(elementName, selected) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        for (var i = 0; i < element.options.length; i++) {
            element.options[i].selected = selected;
        }
    }
}

function nidsSelectOptionByValue(elementName, targetValue) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        for (var i = 0; i < element.options.length; i++) {
            if (element.options[i].value == targetValue) {
                element.options[i].selected = true;
            }
        }
    }
}


function nidsSelectOptionByIndex(elementName, targetIndex) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        if (targetIndex < element.options.length) {
            element.options[targetIndex].selected = true;
        }
    }
}


function nidsGetOptionCount(elementName) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        return element.options.length;
    }
    return 0;
}


function nidsGetOptionValue(elementName, optionNumber) {
    var element = nidsGetElementByName(elementName);
    if (element && optionNumber < element.options.length) {
        return element.options[optionNumber].value;
    }
    return null;
}


function nidsContainsOptionValue(elementName, optionValue) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        for (var i = 0; i < element.options.length; i++) {
            if (element.options[i].value == optionValue) {
                return true;
            }
        }
    }
    return false;
}


function nidsMoveOptionByValue(srcElementName, dstElementName, optionValue) {
    if (nidsContainsOptionValue(srcElementName, optionValue)) {
        var optionTemp;
        var element = nidsGetElementByName(srcElementName);
        if (element) {
            for (var i = 0; i < element.options.length; i++) {
                if (element.options[i].value == optionValue) {
                    optionTemp = element.options[i];
                    element.options[i] = null;
                }
            }
            element = nidsGetElementByName(dstElementName);
            if (element) {
                element.options[element.options.length] = optionTemp;
            }
        }
    }
}


function nidsGetSelectedOptionValue(elementName) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        for (var i = 0; i < element.options.length; i++) {
            if (element.options[i].selected) {
                return element.options[i].value;
            }
        }
    }
    return "";
}


function nidsGetSelectedOptionIndex(elementName) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        for (var i = 0; i < element.options.length; i++) {
            if (element.options[i].selected) {
                return i;
            }
        }
    }
    return -1;
}


function nidsEnableControl(elementName, enabled) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        if (element.type == "radio") {
            nidsEnableRadioButton(elementName, enabled);
        } else {
            element.disabled = enabled ? false : true;
        }
    } else {
        nidsEnableRadioButton(elementName, enabled);
    }
}


function nidsIsElementEnabled(elementName) {
    var element = nidsGetElementByName(elementName);
    if (element) {
        return !element.disabled;
    }
    return false;
}


function nidsRemoveOption(elementName, optionNumber) {
    var element = nidsGetElementByName(elementName);
    if (element && optionNumber < element.options.length) {
        element.options[optionNumber] = null;
    }
}


function nidsAreAllCheckboxesSelected(checkboxElementPrefix) {
    var result = true;
    var i;
    var iPrefixLength = checkboxElementPrefix.length;
    var iElementCount = document.forms[0].elements.length;
    
    for (i = 0; i < iElementCount; i++) {
        var element = document.forms[0].elements[i];
        if (element != null && element.type == "checkbox") {
            var strElementName = element.name;
            if (strElementName.length > iPrefixLength) {
                var strElementPrefix = strElementName.substr(0, iPrefixLength);
                if (strElementPrefix == checkboxElementPrefix) {
                    if (!element.checked && element.style.display != "none") {
                        result = false;
                    }
                }
            }
        }
    }
    return result;
}


function setSelectAllCheckboxState(elementName, checkboxElementPrefix) {
    nidsSetElementChecked(elementName, nidsAreAllCheckboxesSelected(checkboxElementPrefix));
}


function nidsSelectAll(strCheckboxElementPrefix, bCheckedOrClear) {
    var i;
    var iPrefixLength = strCheckboxElementPrefix.length;
    var iElementCount = document.forms[0].elements.length;
    for (i = 0; i < iElementCount; i++) {
        var element = document.forms[0].elements[i];
        if (element != null && element.type == "checkbox") {
            var strElementName = element.name;
            if (strElementName.length > iPrefixLength) {
                var strElementPrefix = strElementName.substr(0, iPrefixLength);
                if (strElementPrefix == strCheckboxElementPrefix) {
                    element.checked = bCheckedOrClear;
                }
            }
        }
    }
}


function nidsFormatString(str, arg1) {
    for (var i = 1; i < arguments.length; i++) {
        var toReplace = "{" + eval(i - 1) + "}";
        var start = str.indexOf(toReplace);
        if (start != -1) {
            var before = str.substring(0, start);
            var insert = arguments[i];
            var after = str.substring(start + toReplace.length, str.length);
            str = before + insert + after;
        } else {
            break;
        }
    }
    return str;
}

function nidsIsValidDomainFormat(domainString) {
    var validDomain = /^([a-zA-Z0-9]([a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])?\.?)+[a-zA-Z]{2,15}$/;
    return domainString.length <= 255 &&
        (validDomain.test(domainString) || nidsIsValidIP(domainString));
}


function nidsIsAncestryVisible(eWidget) {
    var computedStyle;
    var eParent;
    eParent = eWidget.parentNode;
    if (null == eParent) {
        return true;
    }
    if (eParent.currentStyle) {
        computedStyle = eParent.currentStyle;
    } else {
        try
        {
           computedStyle = window.getComputedStyle(eParent, "");
        }
        catch( e )
        {
           return true;
        }
    }
    if ("hidden" == computedStyle.visibility) {
        return false;
    } else if ("visible" == computedStyle.visibility) {
        return true;
    } else if ("inherit" == computedStyle.visibility) {
        return nidsIsAncestryVisible(eParent);
    } else {
        alert("In nidsIsAncestryVisible(), unknown value for visibility: " + computedStyle.visibility);
    }
}


function nidsShowHideAnElement(element, show, considerAncestry) {
    considerAncestry = considerAncestry && true == considerAncestry;
    if (null != element &&
        (!considerAncestry || nidsIsAncestryVisible(element))) {
        if (show) {
            if (!considerAncestry) {
                element.style.visibility = "visible";
            } else {
                element.style.visibility = "inherit";
            }
        } else {
            element.style.visibility = "hidden";
        }
    }
}


function nidsIsElementContainedByDiv(element, divName) {
    if (element.name == divName || element.id == divName) {
        return true;
    }
    if (null == element.parentNode) {
        return false;
    }
    return nidsIsElementContainedByDiv(element.parentNode, divName);
}


function nidsShowHideWindowedWidgets(show) {
    nidsShowHideWindowedWidgets(show, null);
}


function nidsShowHideWindowedWidgets(show, arIgnoreThese) {
    var ae;
    var c;
    var i, k;
    ae = document.getElementsByTagName("SELECT");
    c = ae.length;
    for (i = 0; i < c; i += 1) {
        var bIgnore = false;
        if (null != arIgnoreThese) {
            for (k = 0; k < arIgnoreThese.length; k++) {
                if (arIgnoreThese[k] == ae[i].name) {
                    bIgnore = true;
                }
            }
        }
        if (!bIgnore) {
            nidsShowHideAnElement(ae[i], show, true);
        }
    }
}


function nidsShowHideWindowedWidgetsNotInDiv(show, arIgnoreThese) {
    var ae;
    var c;
    var i, k;
    ae = document.getElementsByTagName("SELECT");
    c = ae.length;
    for (i = 0; i < c; i += 1) {
        var bIgnore = false;
        if (null != arIgnoreThese) {
            bIgnore = nidsIsElementContainedByDiv(ae[i], arIgnoreThese);
        }
        if (!bIgnore) {
            nidsShowHideAnElement(ae[i], show, true);
        }
    }
}

function nidsHideElement(elementName) {
   var element = nidsGetElementByName(elementName);
   if (null != element) {
      if (element.style.display != "none") {
         element.style.display = "none";
      }
   }
}


function nidsShowElement(elementName) {
    var element = nidsGetElementByName(elementName);
    if (null != element) {
        if (element.style.display == "none") {
            element.style.display = "block";
        }
    }
}


function nidsShowHideElement(elementName, showElement) {
    if (showElement) {
        nidsShowElement(elementName);
    } else {
        nidsHideElement(elementName);
    }
}


function nidsShowInlineElement(elementName) {
    var element = nidsGetElementByName(elementName);
    if (null != element) {
        if (element.style.display == "none") {
            element.style.display = "inline";
        }
    }
}


function nidsUpdateAfterMoveOption(element, elementList) {
    var list = new Array;
    var options = element.options;
    for (var i = 0; i < options.length; i++) {
        list[list.length] = options[i].value;
    }
    elementList.value = nidsPack(list);
}


function nidsMoveOption(elementFrom, elementTo, bFromIsInteresting, elementList, strWarnOnFromEmpty) {
    if (null != strWarnOnFromEmpty) {
        if (1 == elementFrom.options.length) {
            alert(strWarnOnFromEmpty);
            return;
        }
    }
    var index = elementFrom.selectedIndex;
    if (index != -1) {
        var option = elementFrom.options[index];
        elementFrom.options[index] = null;
        var length = elementTo.options.length;
        elementTo.options[length] = new Option(option.text, option.value);
    }
    if (bFromIsInteresting) {
        nidsUpdateAfterMoveOption(elementFrom, elementList);
    } else {
        nidsUpdateAfterMoveOption(elementTo, elementList);
    }
}


function nidsMoveOptionUp(sel, elementList) {
    var index = sel.selectedIndex;
    if (index >= 0 && index > 0) {
        var tempVal = sel.options[index].value;
        var tempTxt = sel.options[index].text;
        sel.options[index].value = sel.options[index - 1].value;
        sel.options[index].text = sel.options[index - 1].text;
        sel.options[index - 1].value = tempVal;
        sel.options[index - 1].text = tempTxt;
        sel.selectedIndex = index - 1;
        nidsUpdateAfterMoveOption(sel, elementList);
    }
}


function nidsMoveOptionDown(sel, elementList) {
    var index = sel.selectedIndex;
    if (index >= 0 && index < sel.length - 1) {
        var tempVal = sel.options[index].value;
        var tempTxt = sel.options[index].text;
        sel.options[index].value = sel.options[index + 1].value;
        sel.options[index].text = sel.options[index + 1].text;
        sel.options[index + 1].value = tempVal;
        sel.options[index + 1].text = tempTxt;
        sel.selectedIndex = index + 1;
        nidsUpdateAfterMoveOption(sel, elementList);
    }
}


function nidsEditObject(distinguishedName) {
    parent.disableButtons();
    var dnArray = [distinguishedName];
    document.forms[0].DNList.value = nidsPack(dnArray);
    document.forms[0].NextAction.value = "EditObject";
    document.forms[0].submit();
}


function handleFrameAction(strAction) {
    var result = false;
    if (strAction == "nextButton" ||
        strAction == "finishButton" ||
        strAction == "backButton" || strAction == "closeButton") {
        if (typeof isPageValid == "function" && !isPageValid()) {
            return true;
        }
        parent.disableButtons();
        document.forms[0].Frame_Hidden_PB_Navigation.value = strAction;
        document.forms[0].NextAction.value = "HandleNavigation";
        document.forms[0].submit();
        result = true;
    } else if (strAction == "backButton2") {
        parent.disableButtons();
        history.go(-1);
        result = true;
    }
    return result;
}

var oldButtons = null;
window.nextButton = 1;
window.backButton = 2;
window.applyButton = 4;
window.cancelButton = 8;
window.finishButton = 16;
window.closeButton = 32;
window.defaultButton = 64;
window.okButton = 128;
window.nextDisabledButton = 256;
window.backDisabledButton = 512;
window.applyDisabledButton = 1024;
window.cancelDisabledButton = 2048;
window.finishDisabledButton = 4096;
window.closeDisabledButton = 8192;
window.defaultDisabledButton = 16384
window.okDisabledButton = 32768;

function setHeader(title, subtitle, helpfile) {
    window.helpfile = helpfile;
    wizardHeader.document.getElementById("title").innerHTML = title;
    wizardHeader.document.getElementById("subtitle").innerHTML = subtitle;
    wizardHeader.document.getElementById("helpimage").style.display = helpfile ? "inline" : "none";
}


function nidsSetButton(buttonName, bDisplay) {
    var button = nidsGetElementByName(buttonName);
    if (null != button) {
        try {

            button.style.display = bDisplay != 0 ? "inline" : "none";
        }
        catch(e) {
        }
    }
}

function nidsSetFooterButtons(buttons) {
    if (oldButtons != buttons) {
        nidsSetButton("applyButton", buttons & applyButton);
        nidsSetButton("backButton", buttons & backButton);
        nidsSetButton("nextButton", buttons & nextButton);
        nidsSetButton("cancelButton", buttons & cancelButton);
        nidsSetButton("finishButton", buttons & finishButton);
        nidsSetButton("closeButton", buttons & closeButton);
        nidsSetButton("defaultButton", buttons & defaultButton);
        nidsSetButton("okButton", buttons & okButton);
        nidsSetButton("applyDisabledButton", buttons & applyDisabledButton);
        nidsSetButton("backDisabledButton", buttons & backDisabledButton);
        nidsSetButton("nextDisabledButton", buttons & nextDisabledButton);
        nidsSetButton("cancelDisabledButton", buttons & cancelDisabledButton);
        nidsSetButton("finishDisabledButton", buttons & finishDisabledButton);
        nidsSetButton("closeDisabledButton", buttons & closeDisabledButton);
        nidsSetButton("defaultDisabledButton", buttons & defaultDisabledButton);
        nidsSetButton("okDisabledButton", buttons & okDisabledButton);
        oldButtons = buttons;
    }
}

function nidsSetStandardPageButtons( readOnly ) {

   if ( readOnly ) {

      nidsSetFooterButtons( applyDisabledButton | cancelButton | okDisabledButton );
   }
   else {

      nidsSetFooterButtons( applyButton | cancelButton | okButton );
   }
}

function nidsSetDialogButtons(dialogId, buttons) {
    if (eval(dialogId + "_buttonState") != buttons) {
        nidsSetButton(dialogId + "_cancelButton", buttons & cancelButton);
        nidsSetButton(dialogId + "_closeButton", buttons & closeButton);
        nidsSetButton(dialogId + "_defaultButton", buttons & defaultButton);
        nidsSetButton(dialogId + "_okButton", buttons & okButton);
        nidsSetButton(dialogId + "_cancelButtonDisabled", buttons & cancelDisabledButton);
        nidsSetButton(dialogId + "_closeButtonDisabled", buttons & closeDisabledButton);
        nidsSetButton(dialogId + "_defaultButtonDisabled", buttons & defaultDisabledButton);
        nidsSetButton(dialogId + "_okButtonDisabled", buttons & okDisabledButton);
        eval(dialogId + "_buttonState = buttons;");
    }
}


function nidsDisableButtons() {
    nidsSetFooterButtons(oldButtons << 8 | oldButtons & 65280);
}


function nidsDisableDialogButtons(dialogId) {
    var dlgButtons = eval(dialogId + "_buttonState");
    nidsSetDialogButtons(dialogId, dlgButtons << 8 | dlgButtons & 65280);
}


function nidsSubmitDocumentForm(validateForm) {
    if (validateForm &&
        typeof isPageValid == "function" && !isPageValid()) {
        return;
    }

    nidsDisableButtons();
    try
    {
        document.forms[0].submit();
    }
    catch( e )
    {
       if ( typeof handleSubmitError != "function" ||!handleSubmitError() ) {

          throw e;
       }
    }
}


function nidsDoAction(strAction) {
    if (typeof handleAction == "function" && handleAction(strAction)) {
        return;
    }
    if (strAction == "nextButton" ||
        strAction == "finishButton" ||
        strAction == "applyButton" || strAction == "okButton") {
        if (typeof isPageValid == "function" && !isPageValid()) {
            return false;
        }
    }
    nidsSetElementValue("Frame_Hidden_PB_Navigation", strAction);
    nidsSetElementValue("TaskAction", "Navigate");
    nidsSubmitDocumentForm();
}


function nidsDoKeyAction(strAction) {
    if (window.event.keyCode == 13 || window.event.keyCode == 32) {
        nidsDoAction(strAction);
    }
}


function showHelpWindow() {
    launchHelp(window.helpfile);
}


function nidsMoveToUILevel(level) {
    nidsSetElementValue("Frame_Hidden_PB_Navigation", "breadCrumb");
    nidsSetElementValue("TaskAction", "Navigate");
    nidsSetElementValue("TaskParam", level);
    nidsSubmitDocumentForm();
}

var positionID = 0;

function nidsPositionFooterDiv(headerDivID, bodyDivID, footerDivID) {
    try {
       var headerDiv = document.getElementById(headerDivID);
       var bodyDiv = document.getElementById(bodyDivID);
       var footerDiv = document.getElementById(footerDivID);
       var screenHeight;
       var screenWidth;
       if (window.innerHeight) {
           screenHeight = window.innerHeight;
           screenWidth = window.innerWidth;
       } else if (document.documentElement &&
           document.documentElement.clientHeight) {
           screenHeight = window.innerHeight;
           screenWidth = window.innerWidth;
       } else if (document.body && document.body.clientHeight) {
           screenHeight = document.body.clientHeight;
           screenWidth = document.body.clientWidth;
       } else {
           screenHeight = 0;
           screenWidth = 0;
       }
       var footerHeight = parseInt(footerDiv.style.height, 10);
       yPos = screenHeight - footerHeight;
       if (yPos < 0) {
           yPos = 0;
       }
       var bodyTop = headerDiv.offsetTop + headerDiv.offsetHeight;
       var bodyLeft = bodyDiv.offsetLeft;
       if (isNaN(bodyTop)) {
           bodyTop = bodyDiv.offsetTop;
       }
       if (isNaN(bodyTop)) {
           bodyTop = bodyDiv.clientY;
           bodyLeft = bodyDiv.clientX;
       }
       if (isNaN(bodyTop)) {
           var headerHeight = headerDiv.offsetHeight;
           if (isNaN(headerHeight)) {
               bodyTop = 0;
           } else {
               bodyTop = headerHeight;
           }
       }
       if (isNaN(bodyLeft)) {
           bodyLeft = 0;
       }
       var height = screenHeight - footerHeight;
       var width = screenWidth - 25;
       if (height > bodyTop) {
           height -= bodyTop;
       }
       headerDiv.style.width = width + "px";
       try {
           bodyDiv.style.height = height + "px";
       } catch (e) {
       }
       bodyDiv.style.width = width + "px";
       footerDiv.style.top = yPos + "px";
       footerDiv.style.width = width + "px";
       footerDiv.style.zIndex = 0;
       if (positionID == 0) {
           var func;
           func = "positionID = setInterval( 'nidsPositionFooterDiv(";
           func += "\"" + headerDivID + "\", ";
           func += "\"" + bodyDivID + "\", ";
           func += "\"" + footerDivID + "\" )";
           func += "', 500 );";
           eval(func);
       }
   }
   catch( e ) {
   }
}


function nidsShowSubMenu(event, subMenuName) {
    var element = nidsGetElementByName(subMenuName);
    if (null != element) {
        if (element.style.display == "none") {
            var source = event.target ? event.target : event.srcElement;
            element.style.left = source.offsetLeft;
            element.style.display = "block";
        }
    }
}


function nidsHideSubMenu(subMenuName) {
    var element = nidsGetElementByName(subMenuName);
    if (null != element) {
        if (element.style.display != "none") {
            element.style.display = "none";
        }
    }
}


function nidsShowHelp(helpURL) {
    var w;
    w = window.open(helpURL, "popupHelp", "toolbar=no,location=no,directories=no,menubar=no,scrollbars=yes,resizable=yes,width=500,height=500");
    if (w != null) {
        w.focus();
    }
}


function nidsUpdateWizardSubHeader(subTitle) {
    nidsGetElementByName("WizardSubHeader").innerHTML = subTitle;
}


function nidsUpdateWizardHeader(title) {
    nidsGetElementByName("WizardHeader").innerHTML = title;
}


function nidsUpdateWizardStepText(stepText) {
    nidsGetElementByName("WizardStepText").innerHTML = stepText;
}

function nidsUpdateWizardInstructions(subTitle) {
    nidsGetElementByName("WizardInstructions").innerHTML = subTitle;
}

function nidsGetWizardStepText() {

    return nidsGetElementByName("WizardStepText").innerHTML;
}

var activeDlgID = "";
var timeoutID;

function nidsSetDialogPosition(id, element) {
    dlgObject = document.getElementById(id);
    if (null != dlgObject && null != element) {
        dlgObject.style.left = element.offsetLeft;
        dlgObject.style.top = element.offsetTop + element.offsetHeight;
    }
}


function nidsActivateDialog(id, positionDlg) {
    if (id == null || id.length == 0 || activeDlgID != "") {
        return false;
    }
    activeDlgID = id;
    dlgObject = document.getElementById(id);
    if (null != dlgObject) {
        nidsShowHideWindowedWidgetsNotInDiv(false, id);
        dlgObject.style.display = "block";
        if (arguments.length < 2 || positionDlg) {
            positionIt();
        }
    }
}


function nidsDeactivateDialog(id) {
    if (id == null || id.length == 0) {
        return false;
    }
    document.getElementById(id).style.display = "none";
    activeDlgID = "";
    nidsShowHideWindowedWidgetsNotInDiv(true, id);
    window.clearTimeout(timeoutID);
}


function nidsCenterDialog() {
    try
    {
       var id = activeDlgID;
       if (id == null || id.length == 0) {
           return false;
       }
       var dlg = document.getElementById(id);
       var dlgBody = document.getElementById(id + "_body");
       nidsSetDialogScreenVars();
       yPos = document.scrollTop + (document.screenHeight - dlg.clientHeight) / 2;
       if (yPos < 0) {
           yPos = 0;
       }
       xPos = document.scrollLeft + (document.screenWidth - dlg.clientWidth) / 2;
       if (xPos < 0) {
           xPos = 0;
       }
       dlg.style.top = yPos + "px";
       dlg.style.left = xPos + "px";
       timeoutID = window.setTimeout(nidsCenterDialog, 1000);
   }
   catch( e ) {
   }
}


function nidsSetDialogScreenVars() {
    if (window.innerHeight) {
        document.screenHeight = window.innerHeight;
        document.screenWidth = window.innerWidth;
    } else if (document.body.offsetHeight) {
        document.screenHeight = document.body.offsetHeight;
        document.screenWidth = document.body.offsetWidth;
    } else if (document.documentElement &&
        document.documentElement.clientHeight) {
        document.screenHeight = window.innerHeight;
        document.screenWidth = window.innerWidth;
    } else if (document.body && document.body.clientHeight) {
        document.screenHeight = document.body.clientHeight;
        document.screenWidth = document.body.clientWidth;
    } else {
        document.screenHeight = 0;
        document.screenWidth = 0;
    }
    if (document.documentElement && document.documentElement.scrollTop) {
        document.scrollTop = document.documentElement.scrollTop;
        document.scrollLeft = document.documentElement.scrollLeft;
    } else {
        document.scrollTop = document.body.scrollTop;
        document.scrollLeft = document.body.scrollLeft;
    }
}


function positionIt() {
    try
    {
       if (document.getElementById) {
           var div = document.getElementById(activeDlgID);
           var divWidth = div.offsetWidth ? div.offsetWidth : div.style.width ? parseInt(div.style.width, 10) : 0;
           var divHeight = div.offsetHeight ? div.offsetHeight : div.style.height ? parseInt(div.style.height, 10) : 0;
           var setX = (getViewportWidth() - divWidth) / 2;
           var setY = (getViewportHeight() - divHeight) / 2;
           if (setX < 0) {
               setX = 0;
           }
           if (setY < 0) {
               setY = 0;
           }
           div.style.left = setX + getViewportScrollX() + "px";
           div.style.top = setY + getOffsetTopY() + "px";
           div.style.visibility = "visible";
           timeoutID = window.setTimeout(positionIt, 1000);
       }
   }
   catch( e ) {
   }
}


function getViewportWidth() {
    var width = 0;
    var bodyDiv = document.getElementById("bodyDiv");
    if (null != bodyDiv) {
        width = bodyDiv.offsetWidth ? bodyDiv.offsetWidth : bodyDiv.style.width ? parseInt(bodyDiv.style.width, 10) : 0;
    } else if (document.documentElement &&
        document.documentElement.clientWidth) {
        width = document.documentElement.clientWidth;
    } else if (document.body && document.body.clientWidth) {
        width = document.body.clientWidth;
    } else if (window.innerWidth) {
        width = window.innerWidth - 18;
    }
    return width;
}


function getViewportHeight() {
    var height = 0;
    var bodyDiv = document.getElementById("bodyDiv");
    if (null != bodyDiv) {
        height = bodyDiv.offsetHeight ? bodyDiv.offsetHeight : bodyDiv.style.height ? parseInt(bodyDiv.style.height, 10) : 0;
    } else if (document.documentElement &&
        document.documentElement.clientHeight) {
        height = document.documentElement.clientHeight;
    } else if (document.body && document.body.clientHeight) {
        height = document.body.clientHeight;
    } else if (window.innerHeight) {
        height = window.innerHeight - 18;
    }
    return height;
}


function getViewportScrollX() {
    var scrollX = 0;
    if (document.documentElement && document.documentElement.scrollLeft) {
        scrollX = document.documentElement.scrollLeft;
    } else if (document.body && document.body.scrollLeft) {
        scrollX = document.body.scrollLeft;
    } else if (window.pageXOffset) {
        scrollX = window.pageXOffset;
    } else if (window.scrollX) {
        scrollX = window.scrollX;
    }
    return scrollX;
}


function getViewportScrollY(div) {
    var scrollY = 0;
    var bodyDiv = document.getElementById("bodyDiv");
    if (null != bodyDiv) {
        if (bodyDiv.scrollTop) {
            scrollY = bodyDiv.scrollTop;
        }
    } else if (null != div && null != div.parentNode) {
        var element = div.parentNode;
        if (element.scrollTop) {
            scrollY = element.scrollTop;
        }
    } else {
        if (document.documentElement &&
            document.documentElement.scrollTop) {
            scrollY = document.documentElement.scrollTop;
        } else if (document.body && document.body.scrollTop) {
            scrollY = document.body.scrollTop;
        } else if (window.pageYOffset) {
            scrollY = window.pageYOffset;
        } else if (window.scrollY) {
            scrollY = window.scrollY;
        }
    }
    return scrollY;
}


function isIE() {
    return navigator.appName.indexOf("Microsoft") != -1;
}


function getOffsetTopY(div) {
    var scrollY = 0;
    var bodyDiv = document.getElementById("bodyDiv");
    if (null != bodyDiv) {
        if (isIE()) {
            if (bodyDiv.scrollTop) {
                scrollY = bodyDiv.scrollTop;
            }
        } else if (bodyDiv.offsetTop) {
            scrollY = bodyDiv.offsetTop;
        }
    } else if (null != div && null != div.parentNode) {
        var element = div.parentNode;
        if (element.offsetTop) {
            scrollY = element.offsetTop;
        }
    } else {
        if (document.documentElement &&
            document.documentElement.offsetTop) {
            scrollY = document.documentElement.offsetTop;
        } else if (document.body && document.body.offsetTop) {
            scrollY = document.body.offsetTop;
        } else if (window.pageYOffset) {
            scrollY = window.pageYOffset;
        } else if (window.scrollY) {
            scrollY = window.scrollY;
        }
    }
    return scrollY;
}


function nidsSetDialogTitle(id, title) {
    var element = nidsGetElementByName(id + "_title");
    if (null != element) {
        element.innerHTML = title;
    }
}


function nidsSelectAllRows(masterCheckbox, rowCnt, tableId) {
	var checked = masterCheckbox.checked;
    for (var i = 0; i < rowCnt; i++) {
        var cb = document.getElementById("table_cb_" + tableId + "_" + i);
        if (null != cb) {
            cb.checked = checked;
            nidsSelectRow(i, tableId, checked);
        }
    }
}


function nidsClearAllRows(rowCnt, tableId) {
    for (var i = 0; i < rowCnt; i++) {
        var cb = document.getElementById("table_cb_" + tableId + "_" + i);
        if (null != cb) {
            cb.checked = false;
            nidsSelectRow(i, tableId, false);
        }
    }
}


function nidsGetSelectedTableSubRows(prefixId) {
    var list = new Array;
    var i = 0;
    while (true) {
        var cb = document.getElementById(prefixId + i);
        if (!cb) {
            break;
        }
        if (cb.checked) {
            list[list.length] = i;
        }
        i++;
    }
    return list;
}

var hex = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"];

function nidsIsValidIP(s) {
    var re = /^(0|[1-9]\d{0,2})\.(0|[1-9]\d{0,2})\.(0|[1-9]\d{0,2})\.(0|[1-9]\d{0,2})$/;
    var matchArray = re.exec(s);
    if (!matchArray) {
        return false;
    }
    for (var i = 1; i < 5; i++) {
        if (parseInt(matchArray[i], 10) > 255) {
            return false;
        }
    }
    return true;
}


function nidsIsValidMulticastAddress(s) {
    if (nidsIsValidIP(s)) {
        var subNetlist = s.split(".");
        var firstSubNet = parseInt(subNetlist[0], 10);
        if (firstSubNet >= 224 && firstSubNet <= 239) {
            return true;
        }
    }
    return false;
}


function nidsIsValidHostName(s) {
    var re = /^(([a-zA-Z0-9][a-zA-Z0-9\-]*[a-zA-Z0-9])|[a-zA-Z0-9])$/;
    return s.length <= 255 && re.test(s);
}


function nidsIsValidDNS(s) {
    var domainName = s.toLowerCase();
    var specialChars = "/\\(\\)*?><@,;:\\\\\\\"\\.\\[\\]";
    var validChars = "[^\\s" + specialChars + "]";
    var atom = validChars + "+";
    var atomPat = new RegExp("^" + atom + "$");
    var domArr = domainName.split(".");
    var len = domArr.length;
    for (i = 0; i < len; i++) {
        if (domArr[i].search(atomPat) == -1 || domArr[i].length > 63) {
            return false;
        }
    }
    return true;
}


function nidsIsValidURL(ps_url) {
    str_temp = ps_url;
    if ( ps_url=="") {
       return false;
    }
    if (-1 < str_temp.indexOf("://")) {
        str_temp = str_temp.substring(3 + str_temp.indexOf("://"));
    }
    if (str_temp.lastIndexOf("/") == str_temp.length - 1) {
        str_temp = str_temp.substring(0, str_temp.length - 1);
    }
    if (-1 < str_temp.indexOf(":")) {
        var str_port = str_temp.substring(str_temp.indexOf(":")).replace(/:/, "");
        if (-1 < str_port.indexOf("/")) {
            str_port = str_port.substring(0, str_port.indexOf("/"));
        }
        if(str_port){
        	if (!nidsIsValidPort2(str_port)) {
        		return false;
        	}
        }
        if(str_port){
        	str_temp = str_temp.substring(0, str_temp.indexOf(":"));
        }
        else{
        	str_temp = str_temp.substring(0, str_temp.indexOf("/"));	
        }
        
        if (-1 < str_temp.indexOf("/")) {
            str_temp += str_temp.substring(str_temp.indexOf("/"));
        }
    }
    if (-1 < str_temp.indexOf("/")) {
        if (!nidsIsValidPath(str_temp.substring(str_temp.indexOf("/")))) {
            return false;
        }
        str_temp = str_temp.substring(0, str_temp.indexOf("/"));
    }
    if (str_temp != "") {
        if (!nidsIsValidIP(str_temp) && !nidsIsValidDNS(str_temp)) {
            return false;
        }
    }
    return true;
}

function nidsIsValidURN(ps_urn) {

    var re = new RegExp( "(([a-zA-Z][0-9a-zA-Z+\\-\\.]*:)?/{0,2}[0-9a-zA-Z;/?:@&=+$\\.\\-_!~*'()%]+)?(#[0-9a-zA-Z;/?:@&=+$\\.\\-_!~*'()%]+)?" );

    return re.test( ps_urn );
}


function nidsIsValidScheme(s) {
    if ("http" == s || "https" == s) {
        return true;
    }
    return false;
}


function nidsIsValidPath(s) {
	if (0 == s.indexOf("/")) {
        s = s.substring(1);
    }
    if (s.length - 1 == s.lastIndexOf("/")) {
        s = s.substring(0, s.length - 1);
    }
    var labels = s.split("/");
        
    for (var i = 0; i < labels.length; i++) {
    	if (!nidsIsValidPathSegment(labels[i])) {
            return false;
        }
    }
    return true;
}


function nidsIsValidPathSegment(s) {
    var domainName = s.toLowerCase();
    var specialChars = "/\\(\\)*?><@,;:\\\\\\\"\\.\\[\\]";
    var validChars = "[^\\s" + specialChars + "]";
    var atom = validChars + "+";
    var atomPat = new RegExp("^" + atom + "$");
    var domArr = domainName.split(".");
    var len = domArr.length;
    for (i = 0; i < len; i++) {
    	
        if (domArr[i].search(atomPat) == -1 || domArr[i].length > 63) {
            return false;
        }
        
    }
    return true;
}


function nidsIsValidPathWildCard(s) {
    var vi_length = s.length;
    if (s.substring(vi_length - 2) == "/*" ||
        s.substring(vi_length - 2) == "/?") {
        s = s.substring(0, vi_length - 1);
    }
    return nidsIsValidPath(s);
}


function nidsIsValidFileName(s) {
    if (0 == s.indexOf(".")) {
        s = s.substring(1);
    }
    if (s.length - 1 == s.lastIndexOf(".")) {
        s = s.substring(0, s.length - 1);
    }
    var labels = s.split(".");
    for (var i = 0; i < labels.length; i++) {
        if (!nidsIsValidFileExtension(labels[i])) {
            return false;
        }
    }
    return true;
}


function nidsContainsSpaces(s) {
    if (-1 != s.indexOf(" ")) {
        return true;
    }
    return false;
}


function nidsIsValidFileExtension(s) {
    return !nidsContainsSpaces(s);
}


function nidsIsValidPort2(p) {
    var re = /^[1-9][0-9]*$/;
    return re.exec(p) != null && p <= 65535;
}


function nidsIsValidPortRange(p) {
    var iChar = p.indexOf("-");
    var firstPort = p.substring(0, iChar);
    var secondPort = p.substring(iChar + 1);
    if (-1 == iChar ||
        !nidsIsValidPort2(firstPort) ||
        !nidsIsValidPort2(secondPort) || 1 * firstPort > 1 * secondPort) {
        return false;
    }
    return true;
}


function nidsIsPortInRange(ps_port, ps_range) {
    if (!nidsIsValidPort2(ps_port) || !nidsIsValidPortRange(ps_range)) {
        return false;
    }
    var ls_first = ps_range.substring(0, ps_range.indexOf("-"));
    var ls_last = ps_range.substring(1 + ps_range.indexOf("-"));
    if (1 * ps_port >= 1 * ls_first && 1 * ps_port <= 1 * ls_last) {
        return true;
    }
    return false;
}


function nidsIsValidIPOrDNS(s) {
    if (!nidsIsValidIP(s) && !nidsIsValidDNS(s)) {
        return false;
    }
    return true;
}


function nidsConvertDecimal(n, radix) {
    var s = "";
    while (n >= radix) {
        s += hex[n % radix];
        n = Math.floor(n / radix);
    }
    return nidsTranspose(s += hex[n]);
}


function nidsTranspose(s) {
    var t = "";
    for (var i = s.length - 1; i >= 0; i--) {
        t += s.charAt(i);
    }
    return t;
}


function nidsIsValidSubnetMask(s) {
    if (!nidsIsValidIP(s)) {
        return false;
    }
    var mbin = "";
    var bytes = s.split(".");
    for (var i = 0; i < 4; i++) {
        var decimal = nidsConvertDecimal(bytes[i], 2);
        while (decimal.length < 8) {
            decimal = "0" + decimal;
        }
        mbin += decimal;
    }
    var pos0 = mbin.indexOf("0");
    var pos1 = mbin.indexOf("1", pos0 + 1);
    return pos1 == -1;
}


function nidsComputeSubnet(mask, addr) {
    var s = "";
    var first = true;
    var mbytes = mask.split(".");
    var abytes = addr.split(".");
    for (var i = 0; i < 4; i++) {
        if (first) {
            first = false;
        } else {
            s += ".";
        }
        s += mbytes[i] & abytes[i];
    }
    return s;
}


function nidsComputeBroadcast(mask, addr) {
    var s = "";
    var first = true;
    var mbytes = mask.split(".");
    var abytes = addr.split(".");
    for (var i = 0; i < 4; i++) {
        if (first) {
            first = false;
        } else {
            s += ".";
        }
        tmp = mbytes[i] & abytes[i];
        tmp |= 255 & ~mbytes[i];
        s += tmp;
    }
    return s;
}


function nidsIsValidSubnetAddress(subnet, mask, addr) {
    if (!nidsIsValidIP(addr)) {
        return false;
    }
    if (subnet.length > 0) {
        var s = nidsComputeSubnet(mask, addr);
        if (s != subnet) {
            return false;
        }
    }
    return true;
}


function nidsIsValidIPRange(p) {
    var iChar = p.indexOf("-");
    var firstIP = p.substring(0, iChar);
    var secondIP = p.substring(iChar + 1);
    if (-1 == iChar || !isValidIP(firstIP) || !isValidIP(secondIP)) {
        return false;
    }
    while (0 < firstIP.length) {
        iChar = firstIP.indexOf(".");
        if (-1 == iChar) {
            if (1 * firstIP > 1 * secondIP) {
                return false;
            }
            return true;
        } else if (1 * firstIP.substring(0, iChar) < 1 * secondIP.substring(0, secondIP.indexOf("."))) {
            return true;
        } else if (1 * firstIP.substring(0, iChar) > 1 * secondIP.substring(0, secondIP.indexOf("."))) {
            return false;
        }
        firstIP = firstIP.substring(1 + iChar);
        secondIP = secondIP.substring(1 + secondIP.indexOf("."));
    }
}


function nidsIsIPInRange(ps_ip, ps_range) {
    if (!nidsIsValidIP(ps_ip)) {
        return false;
    }
    if (!nidsIsValidIPRange(ps_range)) {
        return false;
    }
    var ls_range1 = ps_range.substring(0, ps_range.indexOf("-"));
    var ls_range2 = ps_range.substring(1 + ps_range.indexOf("-"));
    var ls_value = "";
    var ls_first = "";
    var ls_last = "";
    while ("" != ps_ip) {
        if (-1 < ps_ip.indexOf(".")) {
            ls_value = ps_ip.substring(0, ps_ip.indexOf("."));
            ps_ip = ps_ip.substring(1 + ps_ip.indexOf("."));
            ls_first = ls_range1.substring(0, ls_range1.indexOf("."));
            ls_range1 = ls_range1.substring(1 + ls_range1.indexOf("."));
            ls_last = ls_range2.substring(0, ls_range2.indexOf("."));
            ls_range2 = ls_range2.substring(1 + ls_range2.indexOf("."));
            if (1 * ls_value > 1 * ls_first &&
                1 * ls_value < 1 * ls_last) {
                return true;
            } else if (1 * ls_value < 1 * ls_first ||
                1 * ls_value > 1 * ls_last) {
                return false;
            }
        } else {
            ls_value = ps_ip;
            ps_ip = "";
            ls_first = ls_range1;
            ls_range1 = "";
            ls_last = ls_range2;
            ls_range2 = "";
            if (1 * ls_value >= 1 * ls_first &&
                1 * ls_value <= 1 * ls_last) {
                return true;
            }
        }
    }
    return false;
}


function nidsIsIPInSubnet(ps_ip, ps_subnet) {
    if (!nidsIsValidIP(ps_ip) || !nidsIsValidCIDRValue(ps_subnet)) {
        return false;
    }
    var ls_ip = ps_ip;
    var ls_decimal_ip = "";
    var li_temp = 0;
    var i;
    while ("" != ls_ip) {
        if (-1 < ls_ip.indexOf(".")) {
            li_temp = ls_ip.substring(0, ls_ip.indexOf("."));
            ls_ip = ls_ip.substring(1 + ls_ip.indexOf("."));
        } else {
            li_temp = ls_ip;
            ls_ip = "";
        }
        for (i = 7; i > 0; i--) {
            if (1 * li_temp >= Math.pow(2, i)) {
                ls_decimal_ip += "1";
                li_temp -= Math.pow(2, i);
            } else {
                ls_decimal_ip += "0";
            }
        }
        ls_decimal_ip += li_temp;
    }
    ls_ip = ps_subnet.substring(0, ps_subnet.indexOf("/"));
    var ls_decimal_first = "";
    var ls_decimal_last = "";
    var li_mask = 1 * ps_subnet.substring(1 + ps_subnet.indexOf("/"));
    var li_current = 0;
    while ("" != ls_ip) {
        if (-1 < ls_ip.indexOf(".")) {
            li_temp = ls_ip.substring(0, ls_ip.indexOf("."));
            ls_ip = ls_ip.substring(1 + ls_ip.indexOf("."));
        } else {
            li_temp = ls_ip;
            ls_ip = "";
        }
        for (i = 7; i > 0; i--) {
            if (1 * li_temp >= Math.pow(2, i)) {
                ls_decimal_first += "1";
                li_temp -= Math.pow(2, i);
            } else {
                ls_decimal_first += "0";
            }
            li_current++;
            if (li_current == li_mask) {
                break;
            }
        }
        if (li_current == li_mask) {
            break;
        }
        ls_decimal_first += li_temp;
        li_current++;
        if (li_current == li_mask) {
            break;
        }
    }
    ls_decimal_last = ls_decimal_first;
    for (i = li_current; i < 33; i++) {
        ls_decimal_first += "0";
        ls_decimal_last += "1";
    }
    if (ls_decimal_ip >= ls_decimal_first &&
        ls_decimal_ip <= ls_decimal_last) {
        return true;
    }
    return false;
}


function nidsIsValidCIDRValue(p) {
    var iSlash = p.indexOf("/");
    if (-1 == iSlash ||
        !nidsIsValidIP(p.substring(0, iSlash)) ||
        !nidsIsValidCIDRSubnetMask(p.substring(iSlash + 1))) {
        return false;
    }
    return true;
}


function nidsIsValidCIDRSubnetMask(mask) {
    var re = /^[1-9][0-9]*$/;
    return re.exec(mask) != null && mask <= 32;
}


function nidsDualListOnLoad() {
    if (document.getElementsByTagName) {
        var s = document.getElementsByTagName("select");
        if (s.length > 0) {
            window.select_current = new Array;
            for (var i = 0, select; select = s[i]; i++) {
                select.onfocus = function () {window.select_current[this.id] = this.selectedIndex;};
                select.onchange = function () {restore(this);};
                emulate(select);
            }
        }
    }
}


function restore(e) {
    if (e.options[e.selectedIndex].disabled) {
        e.selectedIndex = window.select_current[e.id];
    }
}


function emulate(e) {
    for (var i = 0, option; option = e.options[i]; i++) {
        if (option.disabled) {
            option.style.color = "graytext";
        } else {
            option.style.color = "menutext";
        }
    }
}


function nidsIsASCII(charCode) {
    return charCode < 128;
}


function nidsIsASCIIString(str) {
    for (var i = 0; i < str.length; i++) {
        if (!nidsIsASCII(str.charCodeAt(i))) {
            return false;
        }
    }
    return true;
}


function nidsIsValidEmail(email) {
    var r1 = new RegExp("(@.*@)|(\\.\\.)|(@\\.)|(@$)|(^\\.)|(^@)");
    if (r1.test(email)) {
        return false;
    }
    try {
        var temp = email.split("@");
        var user = temp[0];
        var domain = temp[1];
        var r2 = new RegExp("(^\\[.*\\]$)");
        if (r2.test(domain)) {
            domain = domain.substring(1, domain.length - 1);
            return nidsIsASCIIString(user) && nidsIsValidIP(domain);
        } else {
            return nidsIsASCIIString(user) &&
                (nidsIsValidDNS(domain) || nidsIsValidIP(domain));
        }
    } catch (e) {
        return false;
    }
}


function nidsReplace(s, s1, s2) {
    var l = s.split(s1);
    return l.join(s2);
}

xmlEscape = new Array;
xmlEscape['&'] = "&amp;";
xmlEscape['\''] = "&apos;";
xmlEscape['>'] = "&gt;";
xmlEscape['<'] = "&lt;";
xmlEscape['"'] = "&quot;";

function nidsXmlDecode(src) {
    if (src == null || src.length == 0) {
        return "";
    }
    for (i in xmlEscape) {
        src = nidsReplace(src, xmlEscape[i], i);
    }
    var charCode = "";
    var dst = "";
    var idxSrc = 0;
    while (idxSrc < src.length) {
        var ch = src.charAt(idxSrc++);
        if (ch == "&" && src.charAt(idxSrc) == "#") {
            idxSrc++;
            ch = src.charAt(idxSrc++);
            while (ch != ";") {
                charCode += ch;
                ch = src.charAt(idxSrc++);
            }
            ch = String.fromCharCode(parseInt(charCode, 10));
            charCode = "";
        }
        dst += ch;
    }
    return dst;
}


function nidsFormatMessage(format, params) {
    for (var i = 0; i < params.length; i++) {
        format = nidsReplace(format, "{" + i + "}", params[i]);
    }
    return format;
}


function nidsUrlEncode(str) {
    var r;
    if (BrowserCharset != null &&
        BrowserCharset.toLowerCase() == "utf-8") {
        if (encodeURIComponent) {
            r = encodeURIComponent(str);
        } else {
            r = escape(str, 1);
            r = nidsReplace(r, "+", "%2B");
            r = nidsReplace(r, " ", "+");
        }
    } else {
        r = escape(str, 1);
        r = nidsReplace(r, "+", "%2B");
        r = nidsReplace(r, " ", "+");
    }
    return r;
}


function nidsPack(list) {
    if (!list || !list.length || list.length < 1) {
        return "PP";
    }
    var s = "P:" + nidsUrlEncode(list[0]);
    for (var i = 1; i < list.length; i++) {
        s += ":" + nidsUrlEncode(list[i]);
    }
    return s + "P";
}


function nidsUnpack(s) {
    if (!s) {
        return null;
    }
    if (s == "PP") {
        return new Array;
    }
    if (s.length < 3 ||
        s.charAt(0) != "P" ||
        s.charAt(1) != ":" || s.charAt(s.length - 1) != "P") {
        var list = new Array;
        list[0] = s;
        return list;
    }
    s = s.substring(2, s.length - 1);
    var tmplist = s.split(":");
    var list = new Array;
    for (var i = 0; i < tmplist.length; i++) {
        list[i] = nidsUrlDecode(tmplist[i]);
    }
    return list;
}


function nidsUrlDecode(str) {
    str = replace(str, "+", " ");
    if (BrowserCharset != null &&
        BrowserCharset.toLowerCase() == "utf-8") {
        if (decodeURIComponent) {
            return decodeURIComponent(str);
        } else {
            return unescape(str);
        }
    } else {
        return unescape(str);
    }
}


function nidsPackSelect(sel) {
    var o = sel.options;
    var list = new Array;
    for (var i = 0; i < o.length; i++) {
        list[i] = o[i].value;
    }
    return nidsPack(list);
}


function nidsGetSelectedTableRows(tableId) {
    var list = new Array;
    var i = 0;
    var done = false;
    while (true) {
        var cb = document.getElementById("table_cb_" + tableId + "_" + i);
        if (!cb) {
            break;
        }
        if (cb.checked) {
            list[list.length] = i;
        }
        i++;
    }
    return list;
}


function nidsSelectRow(index, tableId, isChecked) {
    var row = document.getElementById("table_row_" + tableId + "_" + index);
    if (isChecked) {
        row.bgColor = COLOR_SEL;
        row.style.color = TEXT_HILITE;
    } else {
        row.bgColor = COLOR_NORMAL;
        row.style.color = TEXT_NORMAL;
    }
    setSelectAllCheckboxState( "selectAll_" + tableId, "table_cb_" + tableId );
}


function nidsToggleCheckbox(cb, index, tableId) {
    var row = document.getElementById("table_row_" + tableId + "_" + index);
    if (cb.checked) {
        row.bgColor = COLOR_SEL;
        row.style.color = TEXT_HILITE;
    } else {
        row.bgColor = COLOR_NORMAL;
        row.style.color = TEXT_NORMAL;
    }
    
    setSelectAllCheckboxState( "selectAll_" + tableId, "table_cb_" + tableId );
}

function nidsValidateWholeIntegerField(field, lowerBound, upperBound)
{
   var success = nidsValidateIntegerField(field, lowerBound, upperBound);
   if(success)
   {
      return nidsValidateNumericString(field, window.rc_mustBeWholeNumber)
   }
   else
   {
      return false;
   }
}


// if no lowerBound or upperBound, will still verify is number
function nidsValidateInteger(intStr, lowerBound, upperBound)
{
   // allow textfields to be empty
   if(intStr=="")
   {
      return true;
   }

   var val = Number(intStr);
   var top = (typeof upperBound == "number" ? upperBound : 99999999999);
   var bottom = (typeof lowerBound == "number" ? lowerBound : -99999999999);

   if (intStr == "")
   {
      alert(nidsFormatMessage(window.rc_invalidNumberMsg, [bottom, top]));
      return false;
   }

   if(isNaN(val) || val>top || val<bottom)
   {
      alert(nidsFormatMessage(window.rc_invalidNumberMsg, [bottom, top]));
      return false;
   }

   return true;
}

function nidsValidateIntegerField(field, low, high)
{
   if(!nidsValidateInteger(field.value, low, high))
   {
      field.value=low;
      field.focus();
      return false;
   }

   var intStr = field.value;

   if ( intStr != "" ) {

      var val = Number( intStr );
      field.value = val;
   }
   return true;
}

function nidsValidateNumericString(field, msg)
{
   var val = field.value;
   var valid = "0123456789 "

   for (var i=0; i<val.length; i++)
   {
      temp = "" + val.substring(i, i+1);
      if(valid.indexOf(temp)=="-1")
      {
         alert(msg);
         field.value = val.substring(0, i);
         field.focus();
         field.select();
         return false;
      }
   }
   return true;
}


function nidsValidateASN1IDString(field, msg)
{
   var val = field.value;
   var valid = "0123456789."

   for (var i=0; i<val.length; i++)
   {
      temp = "" + val.substring(i, i+1);
      if(valid.indexOf(temp)=="-1")
      {
         alert(msg);
         field.value = val.substring(0, i);
         field.focus();
         field.select();
         return false;
      }
   }
   return true;
}


function nidsValidateLength(val, low, high)
{
   // allow textfields to be empty
   if(val=="")
   {
      return true;
   }

   if(val.length<low || val.length>high)
   {
      if (low == high)
      {
         if (low != null)
         {
            alert(nidsFormatMessage(window.rc_invalidExactStringLengthMsg, [low] ));
         }
         else
         {
            return true;
         }
      }
      else
      {
         alert(nidsFormatMessage(window.rc_invalidStringLengthMsg, [low,high] ));
      }
      return false;
   }
   return true;
}

function nidsValidateLengthField(field, low, high)
{
   return nidsValidateLength(field.value, low, high);
}

function nidsIsValidObjectText(ps_name)
{
    var ls_illegalVcdnNameCharacters = /[\|\\\/,.=\+<>\[\]\{\}:;"'`~!@#$%^&*()\?]/;   // Regular Expression
    //For display purposes please keep in sync with the string msg.name.invalid_characters in SystemControllerResources.properties

    if (-1 < ps_name.search(ls_illegalVcdnNameCharacters))
    { // Name contains illegal characters
        return false;
    }
    return true;
}

function nidsIsValidObjectTextDotAllowed(ps_name)
{
    var ls_illegalVcdnNameCharacters = /[\|\\\/,=\+<>\[\]\{\}:;"'`~!@#$%^&*()\?]/;   // Regular Expression
    //For display purposes please keep in sync with the string msg.name.invalid_characters in SystemControllerResources.properties

    if (-1 < ps_name.search(ls_illegalVcdnNameCharacters))
    { // Name contains illegal characters
        return false;
    }
    return true;
}
function nidsValidateHex(IntegerField, length, strMsg)
{
   var val = IntegerField.value;
   var valid = "0123456789abcdefABCDEF"
   var temp;
   var err = false;

   if ( -1 != length && val.length != length ) {

      alert( strMsg );
      IntegerField.focus();
      return false;
   }

   for (var i = 0; i < val.length; i++)
   {
      temp = "" + val.substring(i, i+1);
      if (valid.indexOf(temp) == "-1")
      {
         err = true;
      }
   }
   if (err == true)
   {
      alert(strMsg);
      IntegerField.value="";
      IntegerField.focus();
      return false;
   }
   return true;
}

function nidsShowProgressBar()
{
   var opts = {
	lines: 12, // The number of lines to draw
  	length: 12, // The length of each line
  	width: 5, // The line thickness
  	radius: 10, // The radius of the inner circle
  	corners: 1, // Corner roundness (0..1)
  	rotate: 0, // The rotation offset
  	direction: 1, // 1: clockwise, -1: counterclockwise
  	color: '#000', // #rgb or #rrggbb or array of colors
  	speed: 1, // Rounds per second
  	trail: 60, // Afterglow percentage
  	shadow: false, // Whether to render a shadow
  	hwaccel: false, // Whether to use hardware acceleration
  	className: 'spinner', // The CSS class to assign to the spinner
  	zIndex: 2e9, // The z-index (defaults to 2000000000)
  	top: 'auto', // Top position relative to parent in px
  	left: 'auto' // Left position relative to parent in px
   };

   spinnerbar = new Spinner(opts).spin(document.getElementById('progressBarCenter'));;
}

function nidsHideProgressBar() 
{
   if(spinnerbar != null)
       spinnerbar.stop();
}

(function(t,e){if(typeof exports=="object")module.exports=e();else if(typeof define=="function"&&define.amd)define(e);else t.Spinner=e()})(this,function(){"use strict";var t=["webkit","Moz","ms","O"],e={},i;function o(t,e){var i=document.createElement(t||"div"),o;for(o in e)i[o]=e[o];return i}function n(t){for(var e=1,i=arguments.length;e<i;e++)t.appendChild(arguments[e]);return t}var r=function(){var t=o("style",{type:"text/css"});n(document.getElementsByTagName("head")[0],t);return t.sheet||t.styleSheet}();function s(t,o,n,s){var a=["opacity",o,~~(t*100),n,s].join("-"),f=.01+n/s*100,l=Math.max(1-(1-t)/o*(100-f),t),u=i.substring(0,i.indexOf("Animation")).toLowerCase(),d=u&&"-"+u+"-"||"";if(!e[a]){r.insertRule("@"+d+"keyframes "+a+"{"+"0%{opacity:"+l+"}"+f+"%{opacity:"+t+"}"+(f+.01)+"%{opacity:1}"+(f+o)%100+"%{opacity:"+t+"}"+"100%{opacity:"+l+"}"+"}",r.cssRules.length);e[a]=1}return a}function a(e,i){var o=e.style,n,r;i=i.charAt(0).toUpperCase()+i.slice(1);for(r=0;r<t.length;r++){n=t[r]+i;if(o[n]!==undefined)return n}if(o[i]!==undefined)return i}function f(t,e){for(var i in e)t.style[a(t,i)||i]=e[i];return t}function l(t){for(var e=1;e<arguments.length;e++){var i=arguments[e];for(var o in i)if(t[o]===undefined)t[o]=i[o]}return t}function u(t){var e={x:t.offsetLeft,y:t.offsetTop};while(t=t.offsetParent)e.x+=t.offsetLeft,e.y+=t.offsetTop;return e}function d(t,e){return typeof t=="string"?t:t[e%t.length]}var p={lines:12,length:7,width:5,radius:10,rotate:0,corners:1,color:"#000",direction:1,speed:1,trail:100,opacity:1/4,fps:20,zIndex:2e9,className:"spinner",top:"auto",left:"auto",position:"relative"};function c(t){if(typeof this=="undefined")return new c(t);this.opts=l(t||{},c.defaults,p)}c.defaults={};l(c.prototype,{spin:function(t){this.stop();var e=this,n=e.opts,r=e.el=f(o(0,{className:n.className}),{position:n.position,width:0,zIndex:n.zIndex}),s=n.radius+n.length+n.width,a,l;if(t){t.insertBefore(r,t.firstChild||null);l=u(t);a=u(r);f(r,{left:(n.left=="auto"?l.x-a.x+(t.offsetWidth>>1):parseInt(n.left,10)+s)+"px",top:(n.top=="auto"?l.y-a.y+(t.offsetHeight>>1):parseInt(n.top,10)+s)+"px"})}r.setAttribute("role","progressbar");e.lines(r,e.opts);if(!i){var d=0,p=(n.lines-1)*(1-n.direction)/2,c,h=n.fps,m=h/n.speed,y=(1-n.opacity)/(m*n.trail/100),g=m/n.lines;(function v(){d++;for(var t=0;t<n.lines;t++){c=Math.max(1-(d+(n.lines-t)*g)%m*y,n.opacity);e.opacity(r,t*n.direction+p,c,n)}e.timeout=e.el&&setTimeout(v,~~(1e3/h))})()}return e},stop:function(){var t=this.el;if(t){clearTimeout(this.timeout);if(t.parentNode)t.parentNode.removeChild(t);this.el=undefined}return this},lines:function(t,e){var r=0,a=(e.lines-1)*(1-e.direction)/2,l;function u(t,i){return f(o(),{position:"absolute",width:e.length+e.width+"px",height:e.width+"px",background:t,boxShadow:i,transformOrigin:"left",transform:"rotate("+~~(360/e.lines*r+e.rotate)+"deg) translate("+e.radius+"px"+",0)",borderRadius:(e.corners*e.width>>1)+"px"})}for(;r<e.lines;r++){l=f(o(),{position:"absolute",top:1+~(e.width/2)+"px",transform:e.hwaccel?"translate3d(0,0,0)":"",opacity:e.opacity,animation:i&&s(e.opacity,e.trail,a+r*e.direction,e.lines)+" "+1/e.speed+"s linear infinite"});if(e.shadow)n(l,f(u("#000","0 0 4px "+"#000"),{top:2+"px"}));n(t,n(l,u(d(e.color,r),"0 0 1px rgba(0,0,0,.1)")))}return t},opacity:function(t,e,i){if(e<t.childNodes.length)t.childNodes[e].style.opacity=i}});function h(){function t(t,e){return o("<"+t+' xmlns="urn:schemas-microsoft.com:vml" class="spin-vml">',e)}r.addRule(".spin-vml","behavior:url(#default#VML)");c.prototype.lines=function(e,i){var o=i.length+i.width,r=2*o;function s(){return f(t("group",{coordsize:r+" "+r,coordorigin:-o+" "+-o}),{width:r,height:r})}var a=-(i.width+i.length)*2+"px",l=f(s(),{position:"absolute",top:a,left:a}),u;function p(e,r,a){n(l,n(f(s(),{rotation:360/i.lines*e+"deg",left:~~r}),n(f(t("roundrect",{arcsize:i.corners}),{width:o,height:i.width,left:i.radius,top:-i.width>>1,filter:a}),t("fill",{color:d(i.color,e),opacity:i.opacity}),t("stroke",{opacity:0}))))}if(i.shadow)for(u=1;u<=i.lines;u++)p(u,-2,"progid:DXImageTransform.Microsoft.Blur(pixelradius=2,makeshadow=1,shadowopacity=.3)");for(u=1;u<=i.lines;u++)p(u);return n(e,l)};c.prototype.opacity=function(t,e,i,o){var n=t.firstChild;o=o.shadow&&o.lines||0;if(n&&e+o<n.childNodes.length){n=n.childNodes[e+o];n=n&&n.firstChild;n=n&&n.firstChild;if(n)n.opacity=i}}}var m=f(o("group"),{behavior:"url(#default#VML)"});if(!a(m,"transform")&&m.adj)h();else i=a(m,"animation");return c});
