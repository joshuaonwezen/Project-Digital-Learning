//v.3.6 build 131023

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
dhtmlXForm.prototype.items.combo = {
	
	render: function(item, data) {
		
		item._type = "combo";
		item._enabled = true;
		item._value = null;
		item._newValue = null;
		
		this.doAddLabel(item, data);
		this.doAddInput(item, data, "SELECT", null, true, true, "dhxform_select");
		this.doAttachEvents(item);
		this.doLoadOpts(item, data);
		
		// allow selection to prevent broking combo logic
		item.onselectstart = function(e){e=e||event;e.returnValue=true;return true;}
		
		// item.childNodes[1].childNodes[0].opt_type = data.comboType||"";
		item.childNodes[item._ll?1:0].childNodes[0].setAttribute("opt_type", data.comboType||"");
		
		item._combo = new dhtmlXComboFromSelect(item.childNodes[item._ll?1:0].childNodes[0]);
		item._combo._currentComboValue = item._combo.getSelectedValue();
		item._combo.DOMelem_input.id = data.uid;
		
		item._combo.DOMelem_button.onmousedown = function(e) {
			return false;
		};
		
		if (data.connector) this.doLoadOptsConnector(item, data.connector);
		
		if (data.filtering) item._combo.enableFilteringMode(true);
		
		if (data.readonly == true) this.setReadonly(item, true);
		
		if (data.style) item._combo.DOMelem_input.style.cssText += data.style;
		
		if (typeof(window.addEventListener) == "function") {
			item._combo.DOMelem_input.addEventListener("focus", this._comboFocus, false);
			item._combo.DOMelem_input.addEventListener("blur", this._comboBlur, false);
		} else {
			item._combo.DOMelem_input.attachEvent("onfocus", this._comboFocus);
			item._combo.DOMelem_input.attachEvent("onblur", this._comboBlur);
		}
		
		return this;
	},
	
	destruct: function(item) {
		
		// unload combo
		item.childNodes[item._ll?1:0].childNodes[0].onchange = null;
		
		if (typeof(window.addEventListener) == "function") {
			item._combo.DOMelem_input.removeEventListener("focus", this._comboFocus, false);
			item._combo.DOMelem_input.removeEventListener("blur", this._comboBlur, false);
		} else {
			item._combo.DOMelem_input.detachEvent("onfocus", this._comboFocus);
			item._combo.DOMelem_input.detachEvent("onblur", this._comboBlur);
		}
		
		item._combo._currentComboValue = null;
		item._combo.destructor();
		item._combo = null;
		
		// unload item
		item._apiChange = null;
		this.d2(item);
		item = null;
		
	},
	
	doAttachEvents: function(item) {
		
		var that = this;
		
		item.childNodes[item._ll?1:0].childNodes[0].onchange = function() {
			that.doOnChange(this);
			that.doValidate(this.DOMParent.parentNode.parentNode);
		}
	},
	
	doValidate: function(item) {
		if (item.getForm().hot_validate) this._validate(item);
	},
	
	doOnChange: function(combo) {
		var item = combo.DOMParent.parentNode.parentNode;
		if (item._apiChange) return;
		combo._newComboValue = combo.getSelectedValue();
		if (combo._newComboValue != combo._currentComboValue) {
			if (item.checkEvent("onBeforeChange")) {
				if (item.callEvent("onBeforeChange", [item._idd, combo._currentComboValue, combo._newComboValue]) !== true) {
					// restore last value
					// not the best solution, should be improved
					window.setTimeout(function(){combo.setComboValue(combo._currentComboValue);},1);
					return false;
				}
			}
			combo._currentComboValue = combo._newComboValue;
			item.callEvent("onChange", [item._idd, combo._currentComboValue]);
		}
		item._autoCheck(item._enabled);
	},
	
	doLoadOptsConnector: function(item, url) {
		var that = this;
		var i = item;
		item._connector_working = true;
		item._apiChange = true;
		item._combo.loadXML(url, function(){
			// try to set value if it was called while options loading was in progress
			i.callEvent("onOptionsLoaded", [i._idd]);
			i._connector_working = false;
			if (i._connector_value != null) {
				that.setValue(i, i._connector_value);
				i._connector_value = null;
			}
			i._apiChange = false;
			that = i = null;
		});
	},
	
	enable: function(item) {
		if (String(item.className).search("disabled") >= 0) item.className = String(item.className).replace(/disabled/gi,"");
		item._enabled = true;
		item._combo.disable(false);
		item._combo.DOMelem_button.src = (window.dhx_globalImgPath?dhx_globalImgPath:"")+'combo_select'+(dhtmlx.skin?"_"+dhtmlx.skin:"")+'.gif';
	},
	
	disable: function(item) {
		if (String(item.className).search("disabled") < 0) item.className += " disabled";
		item._enabled = false;
		item._combo.disable(true);
		item._combo.DOMelem_button.src = (window.dhx_globalImgPath?dhx_globalImgPath:"")+'combo_select_dis'+(dhtmlx.skin?"_"+dhtmlx.skin:"")+'.gif';
	},
	
	getCombo: function(item) {
		return item._combo;
	},
	
	setValue: function(item, val) {
		if (item._connector_working) { // attemp to set value while optins not yet loaded (connector used)
			item._connector_value = val;
			return;
		}
		item._apiChange = true;
		item._combo.setComboValue(val);
		item._combo._currentComboValue = item._combo.getActualValue();
		item._apiChange = false;
	},
	
	getValue: function(item) {
		return item._combo.getActualValue();
	},
	
	setWidth: function(item, width) {
		item.childNodes[item._ll?1:0].childNodes[0].style.width = width+"px";
	},
	
	setReadonly: function(item, state) {
		if (!item._combo) return;
		item._combo_ro = state;
		item._combo.readonly(item._combo_ro);
	},

	isReadonly: function(item, state) {
		return item._combo_ro||false;
	},
	
	setFocus: function(item) {
		if (item._enabled) item._combo.DOMelem_input.focus();
	},
	
	_setCss: function(item, skin, fontSize) {
		// update font-size for list-options div
		item._combo.DOMlist.style.fontSize = fontSize;
	},
	
	_comboFocus: function(e) {
		e = e||event;
		var k = (e.target||e.srcElement).parentNode.parentNode;
		var item = k.parentNode.parentNode;
		
		if (item.getForm().skin == "dhx_terrace") {
			if (k.className.search(/combo_in_focus/) < 0) k.className += " combo_in_focus";
		}
		
		for (var q=0; q<item._combo.DOMlist.childNodes.length; q++) item._combo.DOMlist.childNodes[q].onmousedown = function(){return false;}
		item.callEvent("onFocus",[item._idd]);
		item = k = null;
		
	},
	
	_comboBlur: function(e) {
		
		e = e||event;
		var k = (e.target||e.srcElement).parentNode.parentNode;
		var item = k.parentNode.parentNode;
		
		if (item.getForm().skin == "dhx_terrace") {
			if (k.className.search(/combo_in_focus/) >= 0) k.className = k.className.replace(/combo_in_focus/gi,"");
		}
		
		for (var q=0; q<item._combo.DOMlist.childNodes.length; q++) item._combo.DOMlist.childNodes[q].onmousedown = null;
		item.callEvent("onBlur",[item._idd]);
		item = k = null;
		
	}
	
};

(function(){
	for (var a in {doAddLabel:1,doAddInput:1,doLoadOpts:1,doUnloadNestedLists:1,setText:1,getText:1,isEnabled:1,_checkNoteWidth:1})
		dhtmlXForm.prototype.items.combo[a] = dhtmlXForm.prototype.items.select[a];
})();

dhtmlXForm.prototype.items.combo.d2 = dhtmlXForm.prototype.items.select.destruct;

dhtmlXForm.prototype.getCombo = function(name) {
	return this.doWithItem(name, "getCombo");
};

