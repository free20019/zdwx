//wxTrips.html
$('.container-fluid .iFComboBox').on('click', function () {
 	var thisOne = this;
 	if ($(this).hasClass('selected')) {
 		$(this).removeClass('selected');
 	} else {
 		$(this).addClass('selected');
 		$(this).find('.iFList').on('click', function () {
 			if (event.stopPropagation) {
 		        // 闁藉牆顕�Mozilla 閸滐拷Opera
 		        event.stopPropagation();
 	        }else if (window.event) {
 		        // 闁藉牆顕�IE
 	        	window.event.cancelBubble = true;
 	        }
 		}).find('li').off('click').on('click', function () {
 			$(this).addClass('selected').siblings('.selected').removeClass('selected');
 			$(thisOne).find('input').data('value', $(this).data('value')).val($(this).text()).end().removeClass('selected');
 			$(thisOne).find('input').trigger('change');
 		});
 	}
 });
addEventComboBox('.container-fluid');
//鍐呭

function fundqk(obj){
	$.ajax({
		type: "POST",
		url: basePath + "common1/findnr",
		data:{},
		timeout : 180000,
		dataType: 'json',
		success:function(data){
			$(obj).empty();
			for(var i=0; i<data.datas.length;i++){
				$(obj).append('<li data-value="'+data.datas[i].id+'">'+data.datas[i].name+'</li>');
			}
		},
		error: function(){
		}         
	});
}
//閸栧搫娼�
function fundqk(obj){
	$.ajax({
		type: "POST",
		url: basePath + "common1/findqk",
		data:{},
		timeout : 180000,
		dataType: 'json',
		success:function(data){
			$(obj).empty();
			for(var i=0; i<data.datas.length;i++){
				$(obj).append('<li data-value="'+data.datas[i].id+'">'+data.datas[i].name+'</li>');
			}
		},
		error: function(){
		}         
	});
}
//閸忣剙寰�
function fundcomp(obj){
	$.ajax({
		 type: "POST",
		url: basePath + "common1/findcomp",
		data:{
			"ba_id":""
		},
		timeout : 180000,
		dataType: 'json',
		success:function(data){
			$(obj).empty();
			for(var i=0; i<data.datas.length;i++){
				$(obj).append('<li data-value="'+data.datas[i].id+'">'+data.datas[i].name+'</li>');
			}
		},
		error: function(){
		}         
	});
}
//閸忣剙寰冮弶鈥叉閺屻儴顕�
function gscx(obj){
	$(obj).find('input').on('keyup',function(){
		var cpmhs=$(obj).find('input').val();
		if(cpmhs==""){
			$.ajax({
				 type: "POST",
				url: basePath + "common1/findcomp_tj",
				data:{
					"comp_name":""
				},
				timeout : 180000,
				dataType: 'json',
				success:function(data){
					var data2 = data.datas;
					$(obj).find('.iFList').html("");
					for (var i = 0; i < data2.length; i++) {
						var cphms="<li data-value='"+data2[i].id+"'>"+data2[i].name+"</li>";
						$(obj).find('.iFList').append(cphms);
					}
					$(obj).find('.iFList').on('click', function () {
						if (event.stopPropagation){event.stopPropagation();}else if (window.event) {window.event.cancelBubble = true;}
					}).find('li').off('click').on('click', function () {
						$(this).addClass('selected').siblings('.selected').removeClass('selected');
						$(obj).find('input').data('value', $(this).data('value')).val($(this).text()).end().removeClass('selected');
					});
				},
				error: function(){
				}         
			});
		}else{
			$.ajax({
				 type: "POST",
				url: basePath + "common1/findcomp_tj",
				data:{
					"comp_name":cpmhs
				},
				timeout : 180000,
				dataType: 'json',
				success:function(data){
					var data2 = data.datas;
					$(obj).find('.iFList').html("");
					for (var i = 0; i < data2.length; i++) {
						var cphms="<li data-value='"+data2[i].id+"'>"+data2[i].name+"</li>";
						$(obj).find('.iFList').append(cphms);
					}
					$(obj).find('.iFList').on('click', function () {
						if (event.stopPropagation){event.stopPropagation();}else if (window.event) {window.event.cancelBubble = true;}
					}).find('li').off('click').on('click', function () {
						$(this).addClass('selected').siblings('.selected').removeClass('selected');
						$(obj).find('input').data('value', $(this).data('value')).val($(this).text()).end().removeClass('selected');
					});
				},
				error: function(){
				}         
			});
		}
	});
}
//鏉烇妇澧�
function cpcx(obj,cp){
	$(cp).on('keyup',function(){
		var cpmhs=$(cp).val();
		if(cpmhs.length>2){
			$.ajax({
				 type: "POST",
				url: basePath + "common1/findvhictj",
				data:{
					"vhic":cpmhs
				},
				timeout : 180000,
				dataType: 'json',
				success:function(data){
					var data2 = data.datas;
					$(obj).find('.iFList').html("");
					for (var i = 0; i < data2.length; i++) {
						var cphm="<li data-value='"+data2[i].id+"'>"+data2[i].name+"</li>";
						$(obj).find('.iFList').append(cphm);
					}
					$(obj).find('.iFList').on('click', function () {
						if (event.stopPropagation){event.stopPropagation();}else if (window.event) {window.event.cancelBubble = true;}
					}).find('li').off('click').on('click', function () {
						$(this).addClass('selected').siblings('.selected').removeClass('selected');
						$(obj).find('input').data('value', $(this).data('value')).val($(this).text()).end().removeClass('selected');
					});
				},
				error: function(){
				}         
			});
		}
	});
}
//缂佺繝鎱ㄦ禍鍝勬喅
function findry(obj){
	$.ajax({
		type: "POST",
		url: basePath + "common1/findwxry",
		data:{},
		timeout : 180000,
		dataType: 'json',
		success:function(data){
			$(obj).empty();
			for(var i=0; i<data.datas.length;i++){
				$(obj).append('<li data-value="'+data.datas[i].id+'">'+data.datas[i].name+'</li>');
			}
		},
		error: function(){
		}         
	});
}
//缂佺繝鎱ㄧ猾璇茬�
function findwxlx(obj){
	$.ajax({
		type: "POST",
		url: basePath + "common1/findwxlx",
		data:{},
		timeout : 180000,
		dataType: 'json',
		success:function(data){
			console.log(data)
			$(obj).empty();
			for(var i=0; i<data.datas.length;i++){
				$(obj).append('<li data-value="'+data.datas[i].id+'">'+data.datas[i].name+'</li>');
			}
		},
		error: function(){
		}         
	});
}
//缂佸牏顏猾璇茬�
function findzdlx(obj){
	$.ajax({
		type: "POST",
		url: basePath + "common1/findzdlx",
		data:{},
		timeout : 180000,
		dataType: 'json',
		success:function(data){
			$(obj).empty();
			for(var i=0; i<data.datas.length;i++){
				$(obj).append('<li data-value="'+data.datas[i].id+'">'+data.datas[i].name+'</li>');
			}
		},
		error: function(){
		}         
	});
}