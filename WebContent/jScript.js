$(document).ready(function(){
		$('#result').val('');
		$('#calc_form').on('submit', function() {
			$form = $(this);
			$.ajax({
				type : $form.attr('method'),
				url : $form.attr('action'),
				data : $form.serialize(),
				dataType: 'json',
				success	: function(data) {
					var flagError = false;
					$.each(data.errors, function(key, val) {
						if(val == 'true') {
							$('input[name='+key+']').parent().addClass('error');
							flagError = true;
						}
					});
					if(!flagError) {
						$('#result').val(data.result).addClass('succes').removeClass('error').animate({width:890},1000);
						$('input').parent().removeClass('error');
					} else {
						$('#result').addClass('error').addClass('succes').animate({width:100},1000);
					}
				}				
			});
			return false;
		});
	});