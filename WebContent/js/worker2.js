
onmessage = function(event){	
	//var result = "";
	var result = [];
	var worker = event.data;
	//worker.postMessage(data) 이런식으로 하나만 보내기 때문에 event.data.result 식으로 써줄필요없다.
	var Value = worker[0].split(','); //value 받은 값 배열로 변환
	
	var Interval = worker[1]; //interval
	var IntervalNumber = worker[2];
	var IntervalValue = Interval/1000;
	var IntervalData = worker[3];
	for(i = 0; i<Value.length; i++){
		Value[i] = Value[i].trim(); // 값 앞뒤 공백제거
	}

	setInterval(function(){
		if(i < Value.length-1){
			i = i + 1;
		}else{
			i = 0;
		}
		//result += Value + "<br/>";
			if(Value[i] != "" && Value[i] != " "){
				Value[i] = Number(Value[i]);
				result = [IntervalValue, Value[i], IntervalNumber, IntervalData];
				//result = Value + "<br/>";
				postMessage(result);
			}



	}, Interval);

};
