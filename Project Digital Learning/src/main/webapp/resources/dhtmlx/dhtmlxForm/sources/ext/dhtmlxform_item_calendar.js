//v.3.6 build 131023

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
dhtmlXForm.prototype.items.calendar = {
	
	render: function(item, data) {
		
		var t = this;
		
		item._type = "calendar";
		item._enabled = true;
		
		this.doAddLabel(item, data);
		this.doAddInput(item, data, "INPUT", "TEXT", true, true, "dhxform_textarea calendar");
		this.doAttachChangeLS(item);
		
		item.childNodes[item._ll?1:0].childNodes[0]._idd = item._idd;
		item.childNodes[item._ll?1:0].childNodes[0].onblur = function() {
			var i = this.parentNode.parentNode;
			i.getForm()._ccDeactivate(i._idd);
			t.checkEnteredValue(this.parentNode.parentNode);
			i.getForm().callEvent("onBlur",[i._idd]);
			i = null;
		}
		
		item._f = (data.dateFormat||null); // formats
		item._f0 = (data.serverDateFormat||item._f); // formats for save-load, if set - use them for saving and loading only
		
		var f = item.getForm();
		
		item._c = new dhtmlXCalendarObject(item.childNodes[item._ll?1:0].childNodes[0], data.skin||f.skin||"dhx_skyblue");
		item._c._nullInInput = true; // allow null value from input
		item._c.enableListener(item.childNodes[item._ll?1:0].childNodes[0]);
		if (item._f != null) item._c.setDateFormat(item._f);
		if (!f._s2b(data.enableTime)) item._c.hideTime();
		if (f._s2b(data.showWeekNumbers)) item._c.showWeekNumbers();
		if (!isNaN(data.weekStart)) item._c.setWeekStartDay(data.weekStart);
		if (typeof(data.calendarPosition) != "undefined") item._c.setPosition(data.calendarPosition);
		if (data.minutesInterval != null) item._c.setMinutesInterval(data.minutesInterval);
		
		item._c._itemIdd = item._idd;
		
		item._c.attachEvent("onBeforeChange", function(d) {
			
			if (item._value != d) {
				// call some events
				if (item.checkEvent("onBeforeChange")) {
					if (item.callEvent("onBeforeChange",[item._idd, item._value, d]) !== true) {
						return false;
					}
				}
				// accepted
				item._value = d;
				t.setValue(item, d);
				item.callEvent("onChange", [this._itemIdd, item._value]);
			}
			return true;
			
		});
		
		this.setValue(item, data.value);
		
		f = null;
		
		return this;
		
	},
	
	getCalendar: function(item) {
		return item._c;
	},
	
	setSkin: function(item, skin) {
		item._c.setSkin(skin);
	},
	
	setValue: function(item, value) {
		if (!value || value == null || typeof(value) == "undefined" || value == "") {
			item._value = null;
			item.childNodes[item._ll?1:0].childNodes[0].value = "";
		} else {
			item._value = (value instanceof Date ? value : item._c._strToDate(value, item._f0||item._c._dateFormat));
			item.childNodes[item._ll?1:0].childNodes[0].value = item._c._dateToStr(item._value, item._f||item._c._dateFormat);
		}
		item._c.setDate(item._value);
	},
	
	getValue: function(item, asString) {
		var d = item._c.getDate();
		if (asString===true && d == null) return "";
		return (asString===true?item._c._dateToStr(d,item._f0||item._c._dateFormat):d);
	},
	
	setDateFormat: function(item, dateFormat, serverDateFormat) {
		item._f = dateFormat;
		item._f0 = (serverDateFormat||item._f);
		item._c.setDateFormat(item._f);
		this.setValue(item, this.getValue(item));
	},
	
	destruct: function(item) {
		
		// unload calendar instance
		item._c.disableListener(item.childNodes[item._ll?1:0].childNodes[0]);
		item._c.unload();
		item._c = null;
		try {delete item._c;} catch(e){}
		
		item._f = null;
		try {delete item._f;} catch(e){}
		
		item._f0 = null;
		try {delete item._f0;} catch(e){}
		
		// remove custom events/objects
		item.childNodes[item._ll?1:0].childNodes[0]._idd = null;
		item.childNodes[item._ll?1:0].childNodes[0].onblur = null;
		
		// unload item
		this.d2(item);
		item = null;
	},
	
	checkEnteredValue: function(item) {
		this.setValue(item, item._c.getDate());
	}
	
};

(function(){
	for (var a in {doAddLabel:1,doAddInput:1,doUnloadNestedLists:1,setText:1,getText:1,enable:1,disable:1,isEnabled:1,setWidth:1,setReadonly:1,isReadonly:1,setFocus:1,getInput:1})
		dhtmlXForm.prototype.items.calendar[a] = dhtmlXForm.prototype.items.input[a];
})();
dhtmlXForm.prototype.items.calendar.doAttachChangeLS = dhtmlXForm.prototype.items.select.doAttachChangeLS;
dhtmlXForm.prototype.items.calendar.d2 = dhtmlXForm.prototype.items.input.destruct;

dhtmlXForm.prototype.getCalendar = function(name) {
	return this.doWithItem(name, "getCalendar");
};

dhtmlXForm.prototype.setCalendarDateFormat = function(name, dateFormat, serverDateFormat) {
	this.doWithItem(name, "setDateFormat", dateFormat, serverDateFormat);
};
