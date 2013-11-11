//v.3.6 build 131023

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
/* add item */
dhtmlXForm.prototype.addItem = function(pId, itemData, pos) {
	
	// pId = [id,value] for radiobutton
	var pValue = null;
	if (pId instanceof Array) {pValue = pId[1];pId = pId[0];}
	
	var f = null;
	if (pId != null) {
		f = this._getParentForm(pId, pValue);
		// check if item in "f" have nested form
		if (f != null) {
			if (f.item._list == null) {
				// create list
				if (!itemData.listParent) itemData.listParent = f.item._idd;
				f.form._addItem("list", f.item._idd, [itemData], null, f.item._idd, pos);
			} else {
				f.item._list[0].addItem(null, itemData, pos);
			}
			f.form = f.item = null;
			f = null;
			this._autoCheck();
			return;
		}
	}
	
	this._prepareItem(itemData, pos);
	this._autoCheck();
};

/* remove item */
dhtmlXForm.prototype.removeItem = function(id, value) {
	this._removeItem(id, value);
};

dhtmlXForm.prototype._getParentForm = function(id, value) {
	// check if simple item
	if (this.itemPull[this.idPrefix+id] != null) {
		return {form: this, item: this.itemPull[this.idPrefix+id]};
	}
	// check if radio
	for (var a in this.itemPull) {
		if (this.itemPull[a]._type == "ra" && this.itemPull[a]._group == id && this.itemPull[a]._value == value) {
			return {form: this, item: this.itemPull[a]};
		}
	}
	var f = null;
	for (var a in this.itemPull) {
		if (!f && this.itemPull[a]._list != null) {
			for (var q=0; q<this.itemPull[a]._list.length; q++) {
				if (!f) f = this.itemPull[a]._list[q]._getParentForm(id, value);
			}
		}
	}
	
	return f;
};

(function(){
	for (var a in dhtmlXForm.prototype.items) {
		if (!dhtmlXForm.prototype.items[a]._getItemNode) dhtmlXForm.prototype.items[a]._getItemNode = function(item){return item;}
	}
})();

dhtmlXForm.prototype._getItemNode = function(id, value) {
	if (value != null) id = [id, value];
	return this.doWithItem(id, "_getItemNode");
};


/* set/clear required flag */
dhtmlXForm.prototype.setRequired = function(id, value, state) {
	
	if (typeof(state) == "undefined") state = value; else id = [id,value];
	var item = this._getItemNode(id);
	if (!item) return;
	
	state = this._s2b(state);
	item._required = (state==true);
	
	// validation
	if (item._required) {
		if (!item._validate) item._validate = [];
		var t = false;
		for (var q=0; q<item._validate.length; q++) t = (item._validate[q]=="NotEmpty"||t);
		if (!t) item._validate.push("NotEmpty");
		var p = item.childNodes[item._ll?0:1].childNodes[0];
		if (!(p.lastChild && p.lastChild.className && p.lastChild.className.search(/required/) >= 0)) {
			var k = document.createElement("SPAN");
			k.className = "dhxform_item_required";
			k.innerHTML = "*";
			p.appendChild(k);
			k = p = null;
		}
	} else {
		if (item._validate != null) {
			var t = item._validate;
			item._validate = [];
			for (var q=0; q<t.length; q++) { if (t[q] != "NotEmpty") item._validate.push(t[q]); }
			if (item._validate.length == 0) item._validate = null;
		}
		var p = item.childNodes[item._ll?0:1].childNodes[0];
		if (p.lastChild && p.lastChild.className && p.lastChild.className.search(/required/) >= 0) {
			p.removeChild(p.lastChild);
			p = null;
		}
	}
	
	this._resetValidateCss(item);
	item = null;
	
};

/* set/clear note */
dhtmlXForm.prototype.setNote = function(id, value, note) {
	
	if (typeof(note) == "undefined") note = value; else id = [id,value];
	var item = this._getItemNode(id);
	if (!item) return;
	
	var p = this._getNoteNode(item);
	
	if (!p) {
		if (!note.width) note.width = item.childNodes[item._ll?1:0].childNodes[0].offsetWidth;
		p = document.createElement("DIV");
		p.className = "dhxform_note";
		if ({"ch":1,"ra":1}[item._type]) {
			item.childNodes[item._ll?1:0].insertBefore(p, item.childNodes[item._ll?1:0].lastChild);
		} else {
			item.childNodes[item._ll?1:0].appendChild(p);
		}
		
	}
	
	p.innerHTML = note.text;
	if (note.width != null) {
		p.style.width = note.width+"px";
		p._w = note.width;
	}
	
	p = null;
};

dhtmlXForm.prototype.clearNote = function(id, value) {
	
	if (typeof(value) != "undefined") id = [id,value];
	var item = this._getItemNode(id);
	if (!item) return;
	
	var p = this._getNoteNode(item);
	if (p != null) {
		p.parentNode.removeChild(p);
		p = null;
	}
	
};

dhtmlXForm.prototype._getNoteNode = function(item) {
	
	var p = null;
	for (var q=0; q<item.childNodes[item._ll?1:0].childNodes.length; q++) {
		if (String(item.childNodes[item._ll?1:0].childNodes[q].className).search(/dhxform_note/) >= 0) {
			p = item.childNodes[item._ll?1:0].childNodes[q];
		}
	}
	
	item = null;
	
	return p;
};

/* set/clear validation */

dhtmlXForm.prototype.setValidation = function(id, value, rule) {
	
	if (typeof(note) == "undefined") rule = value; else id = [id,value];
	var item = this._getItemNode(id);
	if (!item) return;
	
	// init state, clear prev
	if (item._validate != null) for (var q=0; q<item._validate.length; q++) item._validate[q] = null;
	item._validate = [];
	
	// apply new rules
	if (typeof(rule) == "function" || typeof(window[rule]) == "function") {
		item._validate = [rule];
	} else {
		item._validate = String(rule).split(this.separator);
	}
	
	// check required state
	if (item._required) {
		var r = false;
		for (var q=0; q<item._validate.length; q++) r = (item._validate[q]=="NotEmpty"||r);
		if (!r) item._validate.push("NotEmpty");
	}
	
	item = null;

};

dhtmlXForm.prototype.clearValidation = function(id, value) {
	
	if (typeof(value) != "undefined") id = [id,value];
	var item = this._getItemNode(id);
	if (!item) return;
	
	// clear
	if (item._validate != null) for (var q=0; q<item._validate.length; q++) item._validate[q] = null;
	
	// check required
	item._validate = item._required?["NotEmpty"]:null;
	
	item = null;
	
};

/* reload options */

dhtmlXForm.prototype.reloadOptions = function(name, data) {
	
	var t = this.getItemType(name);
	
	if (!{select:1,multiselect:1,combo:1}[t]) return;
	
	if (t == "select" || t == "multiselect") {
		var opts = this.getOptions(name);
		while (opts.length > 0) opts.remove(0);
		opts.length = 0;
		opts = null;
		if (typeof(data) == "string") {
			this.doWithItem(name, "doLoadOptsConnector", data);
		} else if (data instanceof Array) {
			this.doWithItem(name, "doLoadOpts", {options:data});
		}
	}
	
	if (t == "combo") {
		var combo = this.getCombo(name);
		combo.clearAll();
		combo.setComboValue("");
		if (typeof(data) == "string") {
			this.doWithItem(name, "doLoadOptsConnector", data);
		} else if (data instanceof Array) {
			var toSelect = null;
			for (var q=0; q<data.length; q++) if (this._s2b(data[q].selected)) toSelect = data[q].value;
			combo.addOption(data);
			if (toSelect != null) this.setItemValue(name, toSelect);
			combo = null;
		}
	}
};
