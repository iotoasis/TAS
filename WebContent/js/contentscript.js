var PREFIX = "crosswebex";
var EVENT_FROM_PAGE = "__" + PREFIX + '__rw_chrome_ext_' + new Date().getTime();
var EVENT_REPLY = "__" + PREFIX + '__rw_chrome_ext_reply_' + new Date().getTime();

var s = document.createElement('script');
s.textContent = '(' + function(prefix, send_event_name, reply_event_name) {

	//var func = "window." + prefix + "_nativecall = function(request, callback) { sendMessage(request, callback); };";
	//eval(func);
	window[prefix + "_nativecall"] = function(request, callback) { sendMessage(request, callback); }
	
    function sendMessage(message, callback) {
		if(message.cmd == "setcallback")
		{
			var transporter = document.getElementById("setcallback");
			var tag_id = "";
			if(transporter == undefined)
			{
				transporter = document.createElement('dummy');
				transporter.id = "setcallback";
				
				tag_id = '_' + prefix + '_dummy_id_' + new Date().getTime();
				if(message.id && typeof message.id == "string")
					tag_id = message.id;
				
				transporter.setAttribute('tag', tag_id);
			
				transporter.addEventListener(reply_event_name, function(event) {
					var result = this.getAttribute('result');
					var tagid = this.getAttribute('tag');
	
					result = JSON.parse(result);
					var cbfunction = result.callback;
					
					var idx = cbfunction.indexOf("_setcallback_");
					if(idx > -1)
					{
						var _tagid = cbfunction.substr(idx + 13);
						if(_tagid != tagid)
							return;
						
						cbfunction = cbfunction.substr(0, idx);
					}
					
					var reply = JSON.stringify(result.reply);
					var script_str = cbfunction + "(" + reply + ");";
					//eval(script_str);
					if(typeof window[cbfunction] == 'function')
					{
						window[cbfunction](reply);
					}
					else
					{
					/*
						var w = window;
						w = event.path[event.path.length - 1];
						var cbfv = cbfunction.split(".");
						cbfunction = cbfv[cbfv.length-1];
						if(typeof w[cbfunction] == 'function')
						{
							w[cbfunction](reply);
						}
					*/
						if(cbfunction.startsWith("top.")){
							var cba = cbfunction.split(".");
							var fwin = top;
							for(var i = 0; i < cba.length -1; i++){
								if(i==0) continue;
								else if(i < cba.length) {
									var cbcb = cba[i];
									cbcb = cbcb.replace("frames[", "");
									cbcb = cbcb.replace("]", "");
									fwin = fwin.frames[cbcb];
								}
							}
							
							var cb = cba[cba.length - 1];
							if(typeof fwin[cb] == 'function')
							{
								fwin[cb](reply);
							}
						}
					}
				});
				(document.body||document.documentElement).appendChild(transporter);
			}
			else
			{
				tag_id = transporter.getAttribute('tag');
			}
			
			try {
				for(i = 0; i < message.exfunc.args.length; i++)
				{
					message.exfunc.args[i].callback = message.exfunc.args[i].callback + "_setcallback_" + tag_id;
				}

			} catch(e) {}
			
			
			var event = document.createEvent('Events');
			event.initEvent(send_event_name, true, false);
			transporter.setAttribute('data', JSON.stringify(message));
			transporter.dispatchEvent(event);	
		}
		else
		{
			var tag_id = '_' + prefix + '_dummy_id_' + new Date().getTime();
			var transporter = document.createElement('dummy');
		
			if(!message.id || typeof message.id != "string")
				transporter.id = tag_id;
			else
				transporter.id = message.id;
			
			transporter.addEventListener(reply_event_name, function(event) {
				var result = this.getAttribute('result');
				
				if(this.parentNode) this.parentNode.removeChild(this);

				if (typeof callback == 'function') {
					result = JSON.parse(result);
					callback(result);
				}
			});

			var event = document.createEvent('Events');
			event.initEvent(send_event_name, true, false);
			transporter.setAttribute('data', JSON.stringify(message));
			(document.body||document.documentElement).appendChild(transporter);
			transporter.dispatchEvent(event);		
		}
	}
} + ')("' + PREFIX + '", ' + JSON.stringify(EVENT_FROM_PAGE) + ', ' +
           JSON.stringify(EVENT_REPLY) + ');';
document.documentElement.appendChild(s);
s.parentNode.removeChild(s);

document.addEventListener(EVENT_FROM_PAGE, function(e) {
	if(!e) return;
	var transporter = e.target;
    if (transporter) {
		var request = JSON.parse(transporter.getAttribute('data'));
        transporter.removeAttribute('data');
		request.id = transporter.id;
		
		try {
			chrome.runtime.sendMessage(request, function(response) {
				if(!response) return;
				var event = document.createEvent('Events');
				event.initEvent(EVENT_REPLY, false, false);
				transporter.setAttribute('result', JSON.stringify(response));
				transporter.dispatchEvent(event);
			});
		} catch(e)
		{
		}
    }
});

chrome.runtime.onMessage.addListener(
	function(request, sender, sendResponse) {
		if(!request) return;
		
		if(request.error != null)
		{
			console.log(PREFIX+" Error: " + request.error);
			return;
		}

		var eventid = request.response.id;
		var transporter = document.getElementById(eventid);

		if(transporter != undefined)
		{
			var event = document.createEvent('Events');
			event.initEvent(EVENT_REPLY, false, false);
			transporter.setAttribute('result', JSON.stringify(request.response));
			transporter.dispatchEvent(event);
		}
	}
);
