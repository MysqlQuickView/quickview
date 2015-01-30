
//################### 创建语句 ###################

function create(functionName,keys,table,filename,selection,clipboard,space){
	keys=eval(keys);
	var f = eval(functionName);
	return f(keys,table,filename,selection,clipboard,space);
}

// JavaScript Document
function demo(keys,table,filename,selection,clipboard,space){
	return table;
}

//增加模板
function addTemplate(keys,table,filename,selection,clipboard,space){
	var c = [];
	for(var i in keys){
		c.push(getTemplate(selection,keys[i],i));
	}
	
	return c.join('');
}

function getTemplate(t,value,i){
	return t.replace(/kkk/g,value.name)
			.replace(/ooo/g,value.oname)
			.replace(/ttt/g,value.type)
			.replace(/iii/g,i)
			.replace(/ccc/g,value.comment);
}

//获取java对应的成员方法
function getJavaParm(keys,table,filename,selection,clipboard,space){
	var c=[];
	var isFirst = true;
	for(var i in keys){
		if(!isFirst){
			c.push(space);
		}else{
			isFirst = false;
		}
		c.push('private '+getJavaType(keys[i].type)+ ' ' +keys[i].oname+';\n');
	}
	return c.join('');
}

//获取java 对应的类型
function getJavaType(type){
	type = type.toLowerCase();
	if(type.indexOf('datetime') >= 0){
		return 'Timestamp';
	}else if(type.indexOf('date') >= 0){
		return 'Date';
	}else if(type.indexOf('int') >= 0 || type.indexOf('integer') >= 0){
		return 'int';
	}else{
		return 'String';
	}
}

function addInput(keys,table,filename,selection,clipboard,space){
	var c = ['$input	= Input::getInstance()->request();\n'];
	for(var i in keys){
		c.push(space+'$'+keys[i].oname+' = Argchecker::valid("');
		c.push(keys[i].type.indexOf('int')>=0?'int':'string');
		c.push('", $input, "'+keys[i].name+'", "basic;", Argchecker::NEED, Argchecker::RIGHT);\n');
	}
	
	return c.join('');
}

function addInputDefault(keys,table,filename,selection,clipboard,space){
	var c = ['$input	= Input::getInstance()->request();\n'];
	
	
	for(var i in keys){
		c.push(space+'$'+keys[i].oname+' = Argchecker::valid("');
		c.push(keys[i].type.indexOf('int')>=0?'int':'string');
		c.push('", $input, "'+keys[i].name+'", "basic;", Argchecker::OPT_USE_DEFAULT,Argchecker::WRONG_USE_DEFAULT,'+(keys[i].type.indexOf('int')>=0?'0':'\'\'')+');\n');
	}
	
	return c.join('');
}

function addDBColumnComment(keys,table,filename,selection,clipboard,space){
	var result = [];
	for(var i in keys){
		result.push(space+"'"+keys[i].name+"':'"+keys[i].comment+"',\n");
	}
	return result.join('').trim();
}

function addCall(keys,table,filename,selection,clipboard,space){
	//Account_Admin_ApiController::editPersonalInfo($options) 
	var apiUrl = '#';
	if(clipboard.indexOf('_ApiController')>=0){
		clipboard.replace(/(\w+?)_(\w+?)_ApiController::(\w+?)\(/g,function(a,file,classname,funcname){
			apiUrl = file.toLowerCase()+'.'+classname.toLowerCase()+'.'+funcname;
		})
	}
	apiUrl=apiUrl.replace(/_/g,'.');
	
	
	var names = [];
	var onames = [];
	for(var i in keys){
		names.push("'"+keys[i].name+"'");
		onames.push("$"+keys[i].oname);
	}
	
	var maxNum = getMaxTabNumber(getMaxLength(names));
	var c = ["$this->call('"+apiUrl+"',[\n"];
	for(var i in names){
		c.push(space+'\t'+names[i]+getNTab(names[i].length,maxNum)+"=> "+onames[i]+",\n");
	}
	c.push(space+'\t]);\n'+space);
	
	return c.join('');
}

function addArgs(keys,table,filename,selection,clipboard,space){
	var c = [];
	for(var i in keys){
		c.push(' $'+keys[i].oname);
	}
	return c.join(',').trim();
}

//######################## 复制 ########################

var _copy = [];

function copy(fileName,clipboard,selection,space){
	for(var i in _copy){
		if(_copy[i].check(fileName,clipboard,selection,space)){
			return _copy[i].print(fileName,clipboard,selection,space);
		}
	}
	return clipboard;
}

//如果文件在api service里面
_copy.push({
	check:function(fileName,clipboard,selection,space){
		return fileName.match(/iron\/v2\/api\/.+?\/service\//) &&
		clipboard.indexOf('column')>0 || clipboard.indexOf(' as ')>0;
	},
	print:function(fileName,clipboard,selection,space){
		return getNullColumn(clipboard,space);
	}
});

//如果文件在service里面 Pay_Settlement_Unionpay_WithdrawAccount_ApiController::modify($param) 
_copy.push({
	check:function(fileName,clipboard,selection,space){
		return fileName.match(/iron\/v2\/.+?\/service\//) &&
		clipboard.match(/(\w+?)_(\w+?)_ApiController::(\w+?)\(/);
	},
	print:function(fileName,clipboard,selection,space){
		var c = [];;
		clipboard.replace(/(\w+?)_ApiController::(\w+?)\(/,function(a,file,funcname){
			var arr = file.split('_');
			for(var i in arr){
				c.push(arr[i].toLowerCase());
			}
			var last = arr[arr.length-1];
			c[c.length-1] = last.substring(0,1).toLowerCase() + last.substring(1,last.length) ;
			c.push(funcname);
		});
		return c.join('.');
	}
});

_copy.push({
	check:function(fileName,clipboard,selection,space){
		return clipboard.indexOf('function')>=0;
	},
	print:function(fileName,clipboard,selection,space){
		var c = [clipboard.split('(')[0]];
		var argName;
		clipboard.replace(/function.+?\(\s*(.+?)\s*\)/,function(a,arg){
			argName = arg;
		});
		
		var reg=new RegExp("\\"+argName+"\\[['\"](\\w+)['\"]\\]","g");
		var match;
		var argarr = [];
		var oname = [];
		while(match=reg.exec(clipboard)){
			argarr.push(match[1]);
			oname.push('$'+getName(match[1]));
		}
		
		c.push('(' + oname.join(', ') +'){');
		c.push('\n\t\treturn ');
		c.push(addCall(argarr,space+'\t\t'));
		c.push('\n\t}')
		
		return c.join('');
	}
});


function addCall(keys,space){
	var apiUrl = '#';
	var names = [];
	var onames = [];
	for(var i in keys){
		names.push("'"+keys[i]+"'");
		onames.push("$"+getName(keys[i]));
	}
	
	var maxNum = getMaxTabNumber(getMaxLength(names));
	var c = ["$this->call('"+apiUrl+"',[\n"];
	for(var i in names){
		c.push(space+'\t'+names[i]+getNTab(names[i].length,maxNum)+"=> "+onames[i]+",\n");
	}
	c.push(space+'\t]);\n'+space);
	
	return c.join('');
}

//返回列的空值
function getNullColumn(content,space){
	var arr = content.split(',');
	var r = [];
	var reg = /(\w+)/g;
	for(var i in arr){
		var temp;
		var match;
		while(match=reg.exec(arr[i])){
			temp=match[1];
		}
		r.push("'"+temp+"'");
	}
	
	var maxNum = getMaxTabNumber(getMaxLength(r));
	
	var c = ['$nullData = [\n'];
	for(var i in r){
		c.push(space+"\t"+r[i]+getNTab(r[i].length,maxNum)+"=> 0,\n");
	}
	c.push(space+'];');
	
	return c.join('');
}

//################# 系统函数 #################
String.prototype.trim=function(){
　　return this.replace(/(^\s*)|(\s*$)/g, "");
}


//获得字符串数组的最长长度
function getMaxLength(arr){
	var maxLength = 0;
	for(var i in arr){
		maxLength = maxLength>arr[i].length?maxLength:arr[i].length;
	}
	return maxLength;
}

//最长字符对应的tab占位符的数量
function getMaxTabNumber(contentLength){
	return Div(contentLength,4) + (contentLength%4 != 0 ? 1 : 0) ;
}

//算出合理的tab
function getNTab(contentLength,maxNum){
	var num = maxNum - Div(contentLength,4);
	var c = [];
	for(var i=0;i<num;i++) c.push('\t');
	return c.join('');
}

//整除
function Div(exp1, exp2) {
	var n1 = Math.round(exp1);
	var n2 = Math.round(exp2);
	var rslt = n1 / n2; 
	return rslt >= 0?Math.floor(rslt):Math.ceil(rslt);
}

function getName(name){
	return name.replace(/_(\w)/g, function(all, letter){
          return letter.toUpperCase();
    })
}

function get_name(name){
	return name.replace(/([A-Z])/g,"_$1").toLowerCase();
}