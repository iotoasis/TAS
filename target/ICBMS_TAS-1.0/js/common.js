$.datepicker.regional['ko'] = {
	closeText : '닫기',
	prevText : '이전',
	nextText : '다음',
	currentText : '오늘',
	monthNames : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월',
			'11월', '12월' ],
	monthNamesShort : [ '1', '2', '3', '4', '5', '6', '7', '8', '9', '10',
			'11', '12' ],
	dayNames : [ '일', '월', '화', '수', '목', '금', '토' ],
	dayNamesShort : [ '일', '월', '화', '수', '목', '금', '토' ],
	dayNamesMin : [ '일', '월', '화', '수', '목', '금', '토' ],
	weekHeader : 'Wk',
	dateFormat : 'yy-mm-dd',
	firstDay : 0,
	isRTL : false,
	showOn : 'button',
	buttonImage : '/images/btn_calendar_320.png',
	showMonthAfterYear : true,
	yearSuffix : '',
	showAnim: "slide", //애니메이션을 적용한다.
	showOn: "both", // 버튼과 텍스트 필드 모두 캘린더를 보여준다.
	minDate: '-100y', // 현재날짜로부터 100년이전까지 년을 표시한다.
	showButtonPanel: true, // 캘린더 하단에 버튼 패널을 표시한다.
	yearRange: 'c-100:c+10', // 년도 선택 셀렉트박스를 현재 년도에서 이전, 이후로 얼마의 범위를 표시할것인가
	changeMonth: true, // 월을 바꿀수 있는 셀렉트 박스를 표시한다.
  	changeYear: true // 년을 바꿀 수 있는 셀렉트 박스를 표시한다.
};
$.datepicker.setDefaults($.datepicker.regional['ko']);

function getUrlVars(url) {
	var vars = [], hash;
	var hashes = url.slice(url.indexOf('?') + 1).split('&');
	for ( var i = 0; i < hashes.length; i++) {
		hash = hashes[i].split('=');
		vars.push(hash[0]);
		vars[hash[0]] = hash[1];
	}
	return vars;
}

function numbersonly(e, decimal) {
	var key;
	var keychar;

	if (window.event) {
		key = window.event.keyCode;
	} else if (e) {
		key = e.which;
	} else {
		return true;
	}
	keychar = String.fromCharCode(key);

	if ((key == null) || (key == 0) || (key == 8) || (key == 9) || (key == 13)
			|| (key == 27)) {
		return true;
	} else if ((("0123456789").indexOf(keychar) > -1)) {
		return true;
	} else if (decimal && (keychar == ".")) {
		return true;
	} else
		return false;
}
