//v.3.6 build 131023

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
dhtmlXForm.prototype.addItem=function(b,e,c){var f;var a=null;b instanceof Array&&(a=b[1],b=b[0]);var d=null;if(b!=null&&(d=this._getParentForm(b,a),d!=null)){if(d.item._list==null){if(!e.listParent)e.listParent=d.item._idd;d.form._addItem("list",d.item._idd,[e],null,d.item._idd,c)}else d.item._list[0].addItem(null,e,c);f=d.form=d.item=null,d=f;this._autoCheck();return}this._prepareItem(e,c);this._autoCheck()};dhtmlXForm.prototype.removeItem=function(b,e){this._removeItem(b,e)};
dhtmlXForm.prototype._getParentForm=function(b,e){if(this.itemPull[this.idPrefix+b]!=null)return{form:this,item:this.itemPull[this.idPrefix+b]};for(var c in this.itemPull)if(this.itemPull[c]._type=="ra"&&this.itemPull[c]._group==b&&this.itemPull[c]._value==e)return{form:this,item:this.itemPull[c]};var a=null;for(c in this.itemPull)if(!a&&this.itemPull[c]._list!=null)for(var d=0;d<this.itemPull[c]._list.length;d++)a||(a=this.itemPull[c]._list[d]._getParentForm(b,e));return a};
(function(){for(var b in dhtmlXForm.prototype.items)if(!dhtmlXForm.prototype.items[b]._getItemNode)dhtmlXForm.prototype.items[b]._getItemNode=function(b){return b}})();dhtmlXForm.prototype._getItemNode=function(b,e){e!=null&&(b=[b,e]);return this.doWithItem(b,"_getItemNode")};
dhtmlXForm.prototype.setRequired=function(b,e,c){typeof c=="undefined"?c=e:b=[b,e];var a=this._getItemNode(b);if(a){c=this._s2b(c);a._required=c==!0;if(a._required){if(!a._validate)a._validate=[];for(var d=!1,g=0;g<a._validate.length;g++)d=a._validate[g]=="NotEmpty"||d;d||a._validate.push("NotEmpty");var f=a.childNodes[a._ll?0:1].childNodes[0];if(!f.lastChild||!(f.lastChild.className&&f.lastChild.className.search(/required/)>=0)){var h=document.createElement("SPAN");h.className="dhxform_item_required";
h.innerHTML="*";f.appendChild(h);h=f=null}}else{if(a._validate!=null){d=a._validate;a._validate=[];for(g=0;g<d.length;g++)d[g]!="NotEmpty"&&a._validate.push(d[g]);if(a._validate.length==0)a._validate=null}f=a.childNodes[a._ll?0:1].childNodes[0];f.lastChild&&f.lastChild.className&&f.lastChild.className.search(/required/)>=0&&(f.removeChild(f.lastChild),f=null)}this._resetValidateCss(a);a=null}};
dhtmlXForm.prototype.setNote=function(b,e,c){typeof c=="undefined"?c=e:b=[b,e];var a=this._getItemNode(b);if(a){var d=this._getNoteNode(a);if(!d){if(!c.width)c.width=a.childNodes[a._ll?1:0].childNodes[0].offsetWidth;d=document.createElement("DIV");d.className="dhxform_note";({ch:1,ra:1})[a._type]?a.childNodes[a._ll?1:0].insertBefore(d,a.childNodes[a._ll?1:0].lastChild):a.childNodes[a._ll?1:0].appendChild(d)}d.innerHTML=c.text;if(c.width!=null)d.style.width=c.width+"px",d._w=c.width;d=null}};
dhtmlXForm.prototype.clearNote=function(b,e){typeof e!="undefined"&&(b=[b,e]);var c=this._getItemNode(b);if(c){var a=this._getNoteNode(c);a!=null&&(a.parentNode.removeChild(a),a=null)}};dhtmlXForm.prototype._getNoteNode=function(b){for(var e=null,c=0;c<b.childNodes[b._ll?1:0].childNodes.length;c++)if(String(b.childNodes[b._ll?1:0].childNodes[c].className).search(/dhxform_note/)>=0)e=b.childNodes[b._ll?1:0].childNodes[c];b=null;return e};
dhtmlXForm.prototype.setValidation=function(b,e,c){typeof note=="undefined"?c=e:b=[b,e];var a=this._getItemNode(b);if(a){if(a._validate!=null)for(var d=0;d<a._validate.length;d++)a._validate[d]=null;a._validate=[];a._validate=typeof c=="function"||typeof window[c]=="function"?[c]:String(c).split(this.separator);if(a._required){for(var g=!1,d=0;d<a._validate.length;d++)g=a._validate[d]=="NotEmpty"||g;g||a._validate.push("NotEmpty")}a=null}};
dhtmlXForm.prototype.clearValidation=function(b,e){typeof e!="undefined"&&(b=[b,e]);var c=this._getItemNode(b);if(c){if(c._validate!=null)for(var a=0;a<c._validate.length;a++)c._validate[a]=null;c._validate=c._required?["NotEmpty"]:null;c=null}};
dhtmlXForm.prototype.reloadOptions=function(b,e){var c=this.getItemType(b);if({select:1,multiselect:1,combo:1}[c]){if(c=="select"||c=="multiselect"){for(var a=this.getOptions(b);a.length>0;)a.remove(0);a.length=0;a=null;typeof e=="string"?this.doWithItem(b,"doLoadOptsConnector",e):e instanceof Array&&this.doWithItem(b,"doLoadOpts",{options:e})}if(c=="combo"){var d=this.getCombo(b);d.clearAll();d.setComboValue("");if(typeof e=="string")this.doWithItem(b,"doLoadOptsConnector",e);else if(e instanceof
Array){for(var g=null,f=0;f<e.length;f++)if(this._s2b(e[f].selected))g=e[f].value;d.addOption(e);g!=null&&this.setItemValue(b,g);d=null}}}};

//v.3.6 build 131023

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/